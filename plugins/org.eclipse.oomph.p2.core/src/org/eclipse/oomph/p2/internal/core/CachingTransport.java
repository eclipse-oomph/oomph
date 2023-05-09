/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.CacheManager;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.repository.IStateful;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingTransport extends Transport
{
  public static final String SERVICE_NAME = Transport.SERVICE_NAME;

  private static final ThreadLocal<LocationStack> REPOSITORY_LOCATIONS = new ThreadLocal<>()
  {
    @Override
    protected LocationStack initialValue()
    {
      return new LocationStack();
    }
  };

  private static final Map<URI, Object> URI_LOCKS = new HashMap<>();

  private static boolean DEBUG = false;

  private final IProvisioningAgent agent;

  private IProvisioningEventBus eventBus;

  private final File cacheFolder;

  private Transport delegate;

  public CachingTransport(Transport delegate, IProvisioningAgent agent)
  {
    setDelegate(delegate);
    this.agent = agent;
    eventBus = (IProvisioningEventBus)agent.getService(IProvisioningEventBus.SERVICE_NAME);

    File folder = P2CorePlugin.getUserStateFolder(new File(PropertiesUtil.getUserHome()));
    cacheFolder = new File(folder, "cache"); //$NON-NLS-1$
    cacheFolder.mkdirs();
  }

  public final Transport getDelegate()
  {
    return delegate;
  }

  public final void setDelegate(Transport delegate)
  {
    if (delegate instanceof CachingTransport)
    {
      throw new IllegalArgumentException(Messages.CachingTransport_Chained_exception);
    }

    this.delegate = delegate;
  }

  public File getCacheFile(URI uri)
  {
    return new File(cacheFolder, IOUtil.encodeFileName(uri.toString()));
  }

  @Override
  public IStatus download(URI uri, OutputStream target, IProgressMonitor monitor)
  {
    try
    {
      uri = getSecureLocation(uri);
    }
    catch (CoreException e)
    {
      return e.getStatus();
    }

    if (DEBUG)
    {
      log("  ! " + uri); //$NON-NLS-1$
    }

    if (!isLoadingRepository(uri))
    {
      // If an artifact is repeatedly downloaded, we limit the number of attempts to three.
      // See org.eclipse.oomph.p2.internal.core.CachingRepositoryManager.Artifact.BetterMirrorSelector.ArtifactActivity.retry(ArtifactActivity)
      if (CachingRepositoryManager.BOGUS_SCHEME.equals(uri.getScheme()))
      {
        IOException ex = new IOException(NLS.bind(Messages.CachingTransport_RepeatedDownloads_exception, uri.getRawSchemeSpecificPart()));
        ex.fillInStackTrace();
        return P2CorePlugin.INSTANCE.getStatus(ex);
      }

      IStatus status = Status.CANCEL_STATUS;

      try
      {
        if (eventBus != null)
        {
          eventBus.publishEvent(new DownloadArtifactEvent(uri));
        }

        status = delegate.download(uri, target, monitor);
        return status;
      }
      finally
      {
        if (eventBus != null)
        {
          eventBus.publishEvent(new DownloadArtifactEvent(uri, status));
        }
      }
    }

    synchronized (getLock(uri))
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.length() > 0)
      {
        String path = uri.getSchemeSpecificPart();
        if (OfflineMode.isEnabled() || !path.endsWith("/site.xml") && !path.endsWith("/digest.zip")) //$NON-NLS-1$ //$NON-NLS-2$
        {
          FileInputStream cacheInputStream = null;

          try
          {
            cacheInputStream = new FileInputStream(cacheFile);
            IOUtil.copy(cacheInputStream, target);
            removeLock(uri);
            return Status.OK_STATUS;
          }
          catch (Exception ex)
          {
            //$FALL-THROUGH$
          }
          finally
          {
            IOUtil.closeSilent(cacheInputStream);
          }
        }
      }

      // If the offline mode is not enabled we must make p2 and (for later) ourselves happy.
      StatefulFileOutputStream statefulTarget = null;
      FileInputStream cacheInputStream = null;

      try
      {
        cacheFile.getParentFile().mkdirs();
        File tempCacheFile = new File(cacheFile.getPath() + ".downloading"); //$NON-NLS-1$
        try
        {
          statefulTarget = new StatefulFileOutputStream(tempCacheFile);
        }
        catch (IOException ex)
        {
          // Can't open an output stream on the cache location.
        }

        IStatus status = delegate.download(uri, statefulTarget != null ? statefulTarget : target, monitor);

        // If we have a cached stateful target, we need to transfer the bytes into the original target.
        if (statefulTarget != null)
        {
          IOUtil.closeSilent(statefulTarget);
          if (status.isOK())
          {
            cacheFile.delete();
            tempCacheFile.renameTo(cacheFile);

            // Files can be many megabytes large, so download them directly to a file.
            cacheInputStream = new FileInputStream(cacheFile);
            IOUtil.copy(cacheInputStream, target);

            DownloadStatus downloadStatus = (DownloadStatus)status;
            long lastModified = downloadStatus.getLastModified();
            if (lastModified >= 0)
            {
              cacheFile.setLastModified(lastModified);
            }

            // Remove the other form that might be cached.
            String path = cacheFile.getPath();
            if (path.endsWith(".xml")) //$NON-NLS-1$
            {
              new File(path.substring(0, path.length() - 4) + ".jar").delete(); //$NON-NLS-1$
            }
            else if (path.endsWith(".jar")) //$NON-NLS-1$
            {
              new File(path.substring(0, path.length() - 4) + ".xml").delete(); //$NON-NLS-1$
            }
          }
          else
          {
            IOUtil.deleteBestEffort(tempCacheFile);
          }
        }

        return status;
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      finally
      {
        if (target instanceof IStateful && statefulTarget != null)
        {
          ((IStateful)target).setStatus(statefulTarget.getStatus());
        }

        IOUtil.closeSilent(cacheInputStream);
        IOUtil.closeSilent(statefulTarget);

        removeLock(uri);
      }
    }
  }

  @Override
  @Deprecated
  public IStatus download(URI uri, OutputStream target, long startPos, IProgressMonitor monitor)
  {
    if (startPos <= -1)
    {
      return download(uri, target, monitor);
    }

    throw new UnsupportedOperationException(
        "Positional downloads are actually never called from p2 code and thus disabled by default, please use the method without a position instead."); //$NON-NLS-1$
  }

  @Override
  public InputStream stream(URI uri, IProgressMonitor monitor) throws FileNotFoundException, CoreException, AuthenticationFailedException
  {
    return delegate.stream(uri, monitor);
  }

  @Override
  public long getLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    uri = getSecureLocation(uri);

    if (DEBUG)
    {
      log("  ? " + uri); //$NON-NLS-1$
    }

    if (isLoadingRepository(uri) && OfflineMode.isEnabled())
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.length() > 0)
      {
        return cacheFile.lastModified();
      }

      CacheManager cacheManager = (CacheManager)agent.getService(CacheManager.SERVICE_NAME);
      if (cacheManager == null)
      {
        throw new IllegalArgumentException(Messages.CachingTransport_ServiceNotAvailable_exception);
      }

      // The file is not in Oomph's cache, so try to find if it's in p2's cache.
      org.eclipse.emf.common.util.URI location = org.eclipse.emf.common.util.URI.createURI(uri.toString());
      String fileExtension = location.fileExtension();
      if ("xz".equals(fileExtension)) //$NON-NLS-1$
      {
        // The .xml.xz repository implementation caches using this approach.
        Method method = ReflectUtil.getMethod(cacheManager, "getCacheFile", URI.class); //$NON-NLS-1$
        File file = (File)ReflectUtil.invokeMethod(method, cacheManager, uri);
        if (file != null && file.exists())
        {
          return file.lastModified();
        }
      }
      else
      {
        // For .xml and .jar, the repository implementations caching using this approach.
        String prefix = location.trimFileExtension().lastSegment();
        try
        {
          org.eclipse.emf.common.util.URI repositoryLocation = location.trimSegments(1);
          URI repositoryURI = new URI(repositoryLocation.toString());
          Method method = ReflectUtil.getMethod(cacheManager, "getCache", URI.class, String.class); //$NON-NLS-1$
          File file = (File)ReflectUtil.invokeMethod(method, cacheManager, repositoryURI, prefix);
          if (file != null && file.exists())
          {
            if (!file.toString().endsWith(fileExtension))
            {
              throw new FileNotFoundException(Messages.CachingTransport_UseOfflineCache_exception);
            }

            return file.lastModified();
          }
        }
        catch (URISyntaxException ex1)
        {
          // Ignore.
        }
      }

      try
      {
        return delegateGetLastModified(uri, monitor);
      }
      catch (FileNotFoundException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        // When being physically disconnected it's likely that DNS problems pop up in the form of CoreExceptions.
        // Since we are in offline mode just pretend the file is not found.
        FileNotFoundException exception = new FileNotFoundException(ex.getMessage());
        exception.initCause(ex);
        throw exception;
      }
    }

    try
    {
      return delegateGetLastModified(uri, monitor);
    }
    catch (CoreException exception)
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.length() > 0 && confirmCacheUsage(uri, cacheFile))
      {
        return cacheFile.lastModified();
      }

      if (uri.toString().endsWith(".jar")) //$NON-NLS-1$
      {
        // When p2 tries to load a content.xml, it still first tries a content.jar.
        // If there is a socket timeout exception, it has special case code that doesn't try to load the content.xml.
        // But an overloaded server might return socket timeout exceptions, even for files that doesn't exist.
        // So it's better if p2 tries the *.xml variant as well.
        IStatus status = exception.getStatus();
        Throwable statusException = status.getException();
        if (statusException instanceof SocketTimeoutException)
        {
          IOException wrappedException = new IOExceptionWithCause(statusException);
          ReflectUtil.setValue("exception", status, wrappedException); //$NON-NLS-1$
        }
      }

      throw exception;
    }
    catch (FileNotFoundException exception)
    {
      throw exception;
    }
    catch (AuthenticationFailedException exception)
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.length() > 0 && confirmCacheUsage(uri, cacheFile))
      {
        return cacheFile.lastModified();
      }

      throw exception;
    }
  }

  private long delegateGetLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    File cacheFile = getCacheFile(uri);

    long lastModified;

    try
    {
      lastModified = delegate.getLastModified(uri, monitor);
    }
    catch (FileNotFoundException ex)
    {
      synchronized (getLock(uri))
      {
        cacheFile.delete();
      }

      throw ex;
    }

    if (cacheFile.length() == 0)
    {
      return lastModified - 1;
    }

    if (cacheFile.lastModified() != lastModified || lastModified == 0)
    {
      synchronized (getLock(uri))
      {
        cacheFile.delete();
      }
      return lastModified - 1;
    }

    return lastModified;
  }

  private synchronized boolean confirmCacheUsage(URI uri, File file)
  {
    CacheUsageConfirmer cacheUsageConfirmer = (CacheUsageConfirmer)agent.getService(CacheUsageConfirmer.SERVICE_NAME);
    if (cacheUsageConfirmer != null)
    {
      return cacheUsageConfirmer.confirmCacheUsage(uri, file);
    }

    return false;
  }

  private synchronized Object getLock(URI uri)
  {
    Object result = URI_LOCKS.get(uri);
    if (result == null)
    {
      result = new Object();
      URI_LOCKS.put(uri, result);
    }

    return result;
  }

  @Override
  public URI getSecureLocation(URI location) throws CoreException
  {
    try
    {
      return super.getSecureLocation(location);
    }
    catch (NoSuchMethodError ex)
    {
      return location;
    }
  }

  private synchronized void removeLock(URI uri)
  {
    URI_LOCKS.remove(uri);
  }

  private static boolean isLoadingRepository(URI uri)
  {
    LocationStack stack = REPOSITORY_LOCATIONS.get();
    return !stack.isEmpty();
  }

  private static void log(String message)
  {
    LocationStack stack = REPOSITORY_LOCATIONS.get();
    for (int i = 1; i < stack.size(); i++)
    {
      message = "   " + message; //$NON-NLS-1$
    }

    System.out.println(message);
  }

  static void startLoadingRepository(URI location)
  {
    String uri = location.toString();
    if (uri.endsWith("/")) //$NON-NLS-1$
    {
      uri = uri.substring(0, uri.length() - 1);
    }

    LocationStack stack = REPOSITORY_LOCATIONS.get();
    stack.push(uri);

    if (DEBUG && !uri.startsWith("file:")) //$NON-NLS-1$
    {
      log("--> " + location); //$NON-NLS-1$
    }
  }

  static void stopLoadingRepository()
  {
    LocationStack stack = REPOSITORY_LOCATIONS.get();
    int size = stack.size();
    if (size != 0)
    {
      if (DEBUG)
      {
        String location = stack.peek();
        if (!location.startsWith("file:")) //$NON-NLS-1$
        {
          log("<-- " + location); //$NON-NLS-1$
        }
      }

      if (size > 1)
      {
        stack.pop();
      }
      else
      {
        REPOSITORY_LOCATIONS.remove();
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocationStack extends LinkedList<String>
  {
    private static final long serialVersionUID = 1L;

    @Override
    public void push(String location)
    {
      addLast(location);
    }

    @Override
    public String pop()
    {
      return removeLast();
    }

    @Override
    public String peek()
    {
      return isEmpty() ? null : getLast();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class StatefulFileOutputStream extends FileOutputStream implements IStateful
  {
    private IStatus status;

    public StatefulFileOutputStream(File file) throws FileNotFoundException
    {
      super(file);
    }

    @Override
    public IStatus getStatus()
    {
      return status;
    }

    @Override
    public void setStatus(IStatus status)
    {
      this.status = status;
    }
  }
}

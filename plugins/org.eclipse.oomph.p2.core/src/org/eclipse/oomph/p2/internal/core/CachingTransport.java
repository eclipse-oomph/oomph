/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.provisional.p2.repository.IStateful;
import org.eclipse.equinox.p2.core.IProvisioningAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingTransport extends Transport
{
  private static final ThreadLocal<Stack<String>> REPOSITORY_LOCATIONS = new InheritableThreadLocal<Stack<String>>()
  {
    @Override
    protected Stack<String> initialValue()
    {
      return new Stack<String>();
    }
  };

  private static final Map<URI, Object> URI_LOCKS = new HashMap<URI, Object>();

  private static boolean DEBUG = false;

  private final Transport delegate;

  private final IProvisioningAgent agent;

  private final File cacheFolder;

  public CachingTransport(Transport delegate, IProvisioningAgent agent)
  {
    if (delegate instanceof CachingTransport)
    {
      throw new IllegalArgumentException("CachingTransport should not be chained.");
    }

    this.delegate = delegate;
    this.agent = agent;

    File folder = P2CorePlugin.getUserStateFolder(new File(PropertiesUtil.USER_HOME));
    cacheFolder = new File(folder, "cache");
    cacheFolder.mkdirs();
  }

  public File getCacheFile(URI uri)
  {
    return new File(cacheFolder, IOUtil.encodeFileName(uri.toString()));
  }

  @Override
  public IStatus download(URI uri, OutputStream target, long startPos, IProgressMonitor monitor)
  {
    if (DEBUG)
    {
      log("  ! " + uri);
    }

    if (!isLoadingRepository(uri))
    {
      return delegate.download(uri, target, startPos, monitor);
    }

    synchronized (getLock(uri))
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists())
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

      // If the offline mode is not enabled we must make p2 and (for later) ourselves happy.
      StatefulFileOutputStream statefulTarget = null;
      FileInputStream cacheInputStream = null;

      try
      {
        statefulTarget = new StatefulFileOutputStream(cacheFile);

        IStatus status = delegate.download(uri, statefulTarget, startPos, monitor);
        IOUtil.closeSilent(statefulTarget);
        if (status.isOK())
        {
          // Files can be many megabytes large, so download them directly to a file.
          cacheInputStream = new FileInputStream(cacheFile);
          IOUtil.copy(cacheInputStream, target);

          DownloadStatus downloadStatus = (DownloadStatus)status;
          long lastModified = downloadStatus.getLastModified();
          cacheFile.setLastModified(lastModified);
        }
        else
        {
          IOUtil.deleteBestEffort(cacheFile);
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
  public IStatus download(URI uri, OutputStream target, IProgressMonitor monitor)
  {
    return download(uri, target, 0, monitor);
  }

  @Override
  public InputStream stream(URI uri, IProgressMonitor monitor) throws FileNotFoundException, CoreException, AuthenticationFailedException
  {
    return delegate.stream(uri, monitor);
  }

  @Override
  public long getLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    if (DEBUG)
    {
      log("  ? " + uri);
    }

    if (isLoadingRepository(uri) && OfflineMode.isEnabled())
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists())
      {
        return cacheFile.lastModified();
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
      if (cacheFile.exists() && confirmCacheUsage(uri, cacheFile))
      {
        return cacheFile.lastModified();
      }

      throw exception;
    }
    catch (FileNotFoundException exception)
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists() && confirmCacheUsage(uri, cacheFile))
      {
        return cacheFile.lastModified();
      }

      throw exception;
    }
    catch (AuthenticationFailedException exception)
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists() && confirmCacheUsage(uri, cacheFile))
      {
        return cacheFile.lastModified();
      }

      throw exception;
    }
  }

  private long delegateGetLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    long lastModified = delegate.getLastModified(uri, monitor);

    File cacheFile = getCacheFile(uri);
    if (!cacheFile.exists())
    {
      return lastModified - 1;
    }

    if (cacheFile.lastModified() != lastModified)
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

  private synchronized void removeLock(URI uri)
  {
    URI_LOCKS.remove(uri);
  }

  private static boolean isLoadingRepository(URI uri)
  {
    String location = org.eclipse.emf.common.util.URI.createURI(uri.toString()).trimSegments(1).toString();
    if (location.endsWith("/"))
    {
      location = location.substring(0, location.length() - 1);
    }

    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    return !stack.isEmpty() && stack.peek().equals(location);
  }

  private static void log(String message)
  {
    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    for (int i = 1; i < stack.size(); i++)
    {
      message = "   " + message;
    }

    System.out.println(message);
  }

  static void startLoadingRepository(URI location)
  {
    String uri = location.toString();
    if (uri.endsWith("/"))
    {
      uri = uri.substring(0, uri.length() - 1);
    }

    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    stack.push(uri);

    if (DEBUG && !uri.startsWith("file:"))
    {
      log("--> " + location);
    }
  }

  static void stopLoadingRepository()
  {
    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    if (DEBUG && !stack.isEmpty())
    {
      String location = stack.peek();
      if (!location.startsWith("file:"))
      {
        log("<-- " + location);
      }
    }

    stack.pop();
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

    public IStatus getStatus()
    {
      return status;
    }

    public void setStatus(IStatus status)
    {
      this.status = status;
    }
  }
}

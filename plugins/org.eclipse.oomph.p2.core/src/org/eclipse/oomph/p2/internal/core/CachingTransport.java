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

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingTransport extends Transport
{
  private static final Long NOT_FOUND = Long.MIN_VALUE;

  private static final String NOT_FOUND_TOKEN = "NOT FOUND";

  private final Transport delegate;

  private final File cacheFolder;

  private boolean offline;

  public CachingTransport(Transport delegate)
  {
    this.delegate = delegate;

    File folder = P2CorePlugin.getUserStateFolder(new File(PropertiesUtil.USER_HOME));
    cacheFolder = new File(folder, "cache");
    cacheFolder.mkdirs();

    // TODO Delete this legacy code for 1.0 release
    File oldCacheFile = new File(folder, "metadata.cache");
    if (oldCacheFile.exists())
    {
      IOUtil.deleteBestEffort(oldCacheFile);
    }
  }

  public boolean isOffline()
  {
    return offline;
  }

  public void setOffline(boolean offline)
  {
    this.offline = offline;
  }

  @Override
  public IStatus download(URI uri, OutputStream target, long startPos, IProgressMonitor monitor)
  {
    File modifiedFile = getModifiedFile(uri);
    File contentFile = getContentFile(uri);

    if (offline)
    {
      Long modified = loadModified(modifiedFile);
      if (NOT_FOUND.equals(modified))
      {
        return P2CorePlugin.INSTANCE.getStatus(new Exception());
      }

      byte[] b = loadContent(contentFile);
      if (b != null)
      {
        IOUtil.copy(new ByteArrayInputStream(b), target);
        return Status.OK_STATUS;
      }
    }

    OutputStream oldTarget = null;
    if (uri.getPath().endsWith("/p2.index"))
    {
      oldTarget = target;
      target = new ByteArrayOutputStream();
    }

    IStatus status = delegate.download(uri, target, startPos, monitor);
    if (!status.isOK())
    {
      saveModified(modifiedFile, NOT_FOUND);
    }
    else
    {
      long lastModified = NOT_FOUND;
      if (status instanceof DownloadStatus)
      {
        DownloadStatus downloadStatus = (DownloadStatus)status;
        lastModified = downloadStatus.getLastModified();
        saveModified(modifiedFile, lastModified);
      }

      if (oldTarget != null)
      {
        byte[] content = ((ByteArrayOutputStream)target).toByteArray();
        IOUtil.copy(new ByteArrayInputStream(content), oldTarget);
        saveContent(contentFile, content, lastModified);
      }
    }

    return status;
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
    File modifiedFile = getModifiedFile(uri);

    if (offline)
    {
      Long timeStamp = loadModified(modifiedFile);
      if (timeStamp != null)
      {
        if (NOT_FOUND.equals(timeStamp))
        {
          throw new FileNotFoundException(uri.toString());
        }

        return timeStamp;
      }
    }

    long lastModified;

    try
    {
      lastModified = delegate.getLastModified(uri, monitor);
      saveModified(modifiedFile, lastModified);
    }
    catch (CoreException ex)
    {
      saveModified(modifiedFile, NOT_FOUND);
      throw ex;
    }
    catch (FileNotFoundException ex)
    {
      saveModified(modifiedFile, NOT_FOUND);
      throw ex;
    }
    catch (AuthenticationFailedException ex)
    {
      saveModified(modifiedFile, NOT_FOUND);
      throw ex;
    }
    catch (RuntimeException ex)
    {
      saveModified(modifiedFile, NOT_FOUND);
      throw ex;
    }
    catch (Error ex)
    {
      saveModified(modifiedFile, NOT_FOUND);
      throw ex;
    }

    return lastModified;
  }

  private File getModifiedFile(URI uri)
  {
    return new File(cacheFolder, IOUtil.encodeFileName(uri.toString()) + ".txt");
  }

  private Long loadModified(File file)
  {
    if (!file.exists())
    {
      return null;
    }

    try
    {
      List<String> lines = IOUtil.readLines(file);
      String line = lines.get(0);
      if (!NOT_FOUND_TOKEN.equals(line))
      {
        return Long.parseLong(line);
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return NOT_FOUND;
  }

  private void saveModified(File file, long modified)
  {
    if (modified != NOT_FOUND)
    {
      IOUtil.writeFile(file, Long.toString(modified).getBytes());
      file.setLastModified(modified);
    }
    else
    {
      IOUtil.writeFile(file, NOT_FOUND_TOKEN.getBytes());
    }
  }

  private File getContentFile(URI uri)
  {
    return new File(cacheFolder, IOUtil.encodeFileName(uri.toString()));
  }

  private byte[] loadContent(File file)
  {
    if (!file.exists())
    {
      return null;
    }

    return IOUtil.readFile(file);
  }

  private void saveContent(File file, byte[] content, long modified)
  {
    IOUtil.writeFile(file, content);
    if (modified != NOT_FOUND)
    {
      file.setLastModified(modified);
    }
  }
}

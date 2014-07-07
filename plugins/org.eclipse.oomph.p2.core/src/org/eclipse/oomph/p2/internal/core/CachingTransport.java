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

import org.eclipse.oomph.p2.core.Agent;
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

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingTransport extends Transport
{
  private final Agent agent;

  private final Transport delegate;

  private final File cacheFolder;

  public CachingTransport(Agent agent, Transport delegate)
  {
    this.agent = agent;
    this.delegate = delegate;

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
    if (agent.isOffline())
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists())
      {
        try
        {
          byte[] content = IOUtil.readFile(cacheFile);
          IOUtil.copy(new ByteArrayInputStream(content), target);
          return Status.OK_STATUS;
        }
        catch (Exception ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    OutputStream oldTarget = target;
    target = new ByteArrayOutputStream();

    IStatus status = delegate.download(uri, target, startPos, monitor);
    if (status.isOK())
    {
      byte[] content = ((ByteArrayOutputStream)target).toByteArray();
      IOUtil.copy(new ByteArrayInputStream(content), oldTarget);

      File cacheFile = getCacheFile(uri);
      IOUtil.writeFile(cacheFile, content);

      DownloadStatus downloadStatus = (DownloadStatus)status;
      long lastModified = downloadStatus.getLastModified();
      cacheFile.setLastModified(lastModified);
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
    if (agent.isOffline())
    {
      File cacheFile = getCacheFile(uri);
      if (cacheFile.exists())
      {
        return cacheFile.lastModified();
      }

      try
      {
        return delegate.getLastModified(uri, monitor);
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

    return delegate.getLastModified(uri, monitor);
  }
}

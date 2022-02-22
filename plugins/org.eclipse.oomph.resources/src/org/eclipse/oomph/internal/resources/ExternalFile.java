/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.resources;

import org.eclipse.oomph.resources.backend.BackendFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentDescription;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public final class ExternalFile extends ExternalResource implements IFile
{
  protected ExternalFile(ExternalContainer parent, BackendFile backendFile)
  {
    super(parent, backendFile);
  }

  @Override
  protected BackendFile getBackendResource()
  {
    return (BackendFile)super.getBackendResource();
  }

  @Override
  public int getType()
  {
    return FILE;
  }

  @Override
  public void appendContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void appendContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void create(InputStream source, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public String getCharset() throws CoreException
  {
    return "UTF-8"; //$NON-NLS-1$
  }

  @Override
  public String getCharset(boolean checkImplicit) throws CoreException
  {
    return getCharset();
  }

  @Override
  public String getCharsetFor(Reader reader) throws CoreException
  {
    return getCharset();
  }

  @Override
  public IContentDescription getContentDescription() throws CoreException
  {
    // TODO
    return null;
  }

  @Override
  public InputStream getContents() throws CoreException
  {
    try
    {
      return getBackendResource().getContents(null);
    }
    catch (Exception ex)
    {
      throw new CoreException(new Status(IStatus.ERROR, "org.eclipse.oomph.predicates", ex.getMessage(), ex)); //$NON-NLS-1$
    }
  }

  @Override
  public InputStream getContents(boolean force) throws CoreException
  {
    return getContents();
  }

  @Override
  @Deprecated
  public int getEncoding() throws CoreException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public IFileState[] getHistory(IProgressMonitor monitor) throws CoreException
  {
    return new IFileState[0];
  }

  @Override
  public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  @Deprecated
  public void setCharset(String newCharset) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setCharset(String newCharset, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setContents(IFileState source, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setContents(IFileState source, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }
}

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
package org.eclipse.oomph.internal.resources;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import java.net.URI;

/**
 * @author Eike Stepper
 */
public final class ExternalFolder extends ExternalContainer implements IFolder
{
  protected ExternalFolder(ExternalContainer parent, String name)
  {
    super(parent, name);
  }

  public int getType()
  {
    return FOLDER;
  }

  public void create(boolean force, boolean local, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void create(int updateFlags, boolean local, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IFile getFile(String name)
  {
    return new ExternalFile(this, name);
  }

  public IFolder getFolder(String name)
  {
    return new ExternalFolder(this, name);
  }

  public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }
}

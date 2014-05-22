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

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class ExternalContainer extends ExternalResource implements IContainer
{
  protected ExternalContainer(ExternalContainer parent, String name)
  {
    super(parent, name);
  }

  public boolean exists(IPath path)
  {
    return new File(getFile(), path.toOSString()).exists();
  }

  public IResource findMember(IPath path)
  {
    if (path.isEmpty())
    {
      return this;
    }

    String name = path.segment(0);
    File member = new File(getFile(), name);
    if (member.isFile())
    {
      return new ExternalFile(this, name);
    }

    if (member.isDirectory())
    {
      return new ExternalFolder(this, name).findMember(path.removeFirstSegments(1));
    }

    return null;
  }

  public IResource findMember(IPath path, boolean includePhantoms)
  {
    return findMember(path);
  }

  public IResource findMember(String path, boolean includePhantoms)
  {
    return findMember(path);
  }

  public IResource findMember(String path)
  {
    return findMember(new Path(path));
  }

  public String getDefaultCharset() throws CoreException
  {
    // TODO
    return "UTF-8";
  }

  public String getDefaultCharset(boolean checkImplicit) throws CoreException
  {
    return getDefaultCharset();
  }

  public IFile getFile(IPath path)
  {
    ExternalContainer parent = getParent(path);
    return new ExternalFile(parent, path.lastSegment());
  }

  public IFolder getFolder(IPath path)
  {
    ExternalContainer parent = getParent(path);
    return new ExternalFolder(parent, path.lastSegment());
  }

  public IResource[] members() throws CoreException
  {
    List<IResource> members = new ArrayList<IResource>();
    for (File file : getFile().listFiles())
    {
      if (file.isFile())
      {
        members.add(new ExternalFile(this, file.getName()));
      }
      else if (file.isDirectory())
      {
        members.add(new ExternalFolder(this, file.getName()));
      }
    }

    return members.toArray(new IResource[members.size()]);
  }

  public IResource[] members(boolean includePhantoms) throws CoreException
  {
    return members();
  }

  public IResource[] members(int memberFlags) throws CoreException
  {
    return members();
  }

  public IFile[] findDeletedMembersWithHistory(int depth, IProgressMonitor monitor) throws CoreException
  {
    return new IFile[0];
  }

  @Deprecated
  public void setDefaultCharset(String charset) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setDefaultCharset(String charset, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IResourceFilterDescription createFilter(int type, FileInfoMatcherDescription matcherDescription, int updateFlags, IProgressMonitor monitor)
      throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IResourceFilterDescription[] getFilters() throws CoreException
  {
    return new IResourceFilterDescription[0];
  }

  protected ExternalContainer getParent(IPath path)
  {
    int segmentCount = path.segmentCount();
    if (segmentCount > 1)
    {
      return new ExternalFolder(this, path.segment(0)).getParent(path.removeFirstSegments(1));
    }

    if (segmentCount == 1)
    {
      return this;
    }

    return null;
  }

  @Override
  protected boolean visit(IResourceVisitor visitor, int depth) throws CoreException
  {
    if (!super.visit(visitor, depth))
    {
      return false;
    }

    if (depth < 1)
    {
      return false;
    }

    --depth;
    for (IResource member : members())
    {
      ((ExternalResource)member).visit(visitor, depth);
    }

    return true;
  }
}

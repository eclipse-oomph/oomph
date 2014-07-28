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
package org.eclipse.oomph.resources.backend;

import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.ResourcesUtil.ImportResult;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Eike Stepper
 */
public final class LocalBackendSystem extends BackendSystem
{
  private final File root;

  private LocalBackendSystem(URI systemURI) throws BackendException
  {
    super(systemURI);
    root = new File(systemURI.toFileString());
  }

  public File getRoot()
  {
    return root;
  }

  @Override
  protected File getDelegate(BackendResource resource) throws Exception
  {
    if (resource == this)
    {
      return root;
    }

    return new File(root, resource.getSystemRelativeURI().toFileString());
  }

  @Override
  protected boolean exists(BackendResource resource, IProgressMonitor monitor) throws Exception
  {
    File file = getDelegate(resource);
    if (StringUtil.isEmpty(file.getName()))
    {
      // This file is a root as returned by File.listRoots().
      return file.exists();
    }

    if (resource.isContainer())
    {
      return file.isDirectory();
    }

    return file.isFile();
  }

  @Override
  protected IPath getLocation(BackendResource resource) throws Exception
  {
    return new Path(getDelegate(resource).getAbsolutePath());
  }

  @Override
  protected long getLastModified(BackendResource resource) throws Exception
  {
    return getDelegate(resource).lastModified();
  }

  @Override
  protected InputStream getContents(BackendFile file, IProgressMonitor monitor) throws Exception
  {
    return new FileInputStream(getDelegate(file));
  }

  @Override
  protected BackendResource[] getMembers(BackendContainer container, IProgressMonitor monitor) throws Exception
  {
    File folder = getDelegate(container);
    File[] members = folder.listFiles();
    if (members == null)
    {
      return new BackendResource[0];
    }

    BackendResource[] result = new BackendResource[members.length];
    for (int i = 0; i < members.length; i++)
    {
      ResourcesPlugin.checkCancelation(monitor);

      File member = members[i];
      URI systemRelativeURI = container.getSystemRelativeURI().appendSegment(member.getName());

      if (member.isFile())
      {
        result[i] = createBackendFile(systemRelativeURI);
      }
      else if (member.isDirectory())
      {
        result[i] = createBackendFolder(systemRelativeURI);
      }
    }

    return result;
  }

  @Override
  protected BackendResource findMember(BackendContainer container, URI relativeURI, IProgressMonitor monitor) throws Exception
  {
    File folder = getDelegate(container);

    File member = new File(folder, relativeURI.toString());
    URI systemRelativeURI = container.getSystemRelativeURI().appendSegments(relativeURI.segments());

    if (member.isDirectory())
    {
      return createBackendFolder(systemRelativeURI);
    }

    if (member.isFile())
    {
      return createBackendFile(systemRelativeURI);
    }

    throw new BackendException("Member not found: " + member);
  }

  @Override
  protected ImportResult importIntoWorkspace(BackendContainer container, IProject project, IProgressMonitor monitor) throws Exception
  {
    return ResourcesUtil.importProject(getDelegate(container), project, monitor);
  }

  /**
   * @author Eike Stepper
   */
  public static final class Factory implements IFactory
  {
    public LocalBackendSystem createBackendSystem(URI systemURI) throws BackendException
    {
      return new LocalBackendSystem(systemURI);
    }
  }
}

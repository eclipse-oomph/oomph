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

import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.ResourcesUtil.ImportResult;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
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
  protected Object[] getDelegateMembers(Object containerDelegate, IProgressMonitor monitor) throws Exception
  {
    return ((File)containerDelegate).listFiles();
  }

  @Override
  protected Object getDelegateMember(Object containerDelegate, String relativePath, IProgressMonitor monitor) throws Exception
  {
    return new File((File)containerDelegate, relativePath);
  }

  @Override
  protected String getDelegateName(Object resourceDelegate) throws Exception
  {
    return ((File)resourceDelegate).getName();
  }

  @Override
  protected Type getDelegateType(Object resourceDelegate, boolean checkExists) throws Exception
  {
    if (resourceDelegate.equals(getDelegate()))
    {
      return Type.SYSTEM;
    }

    File file = (File)resourceDelegate;
    if (file.isDirectory())
    {
      return Type.FOLDER;
    }

    if (file.isFile())
    {
      return Type.FILE;
    }

    return null;
  }

  @Override
  protected IPath getLocation(BackendResource resource) throws Exception
  {
    return new Path(getDelegate(resource).getAbsolutePath());
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
  protected long getLastModified(BackendResource resource, IProgressMonitor monitor) throws Exception
  {
    return getDelegate(resource).lastModified();
  }

  @Override
  protected InputStream getContents(BackendFile backendFile, IProgressMonitor monitor) throws Exception
  {
    return new FileInputStream(getDelegate(backendFile));
  }

  @Override
  protected ImportResult importIntoWorkspace(BackendContainer container, IProject project, IProgressMonitor monitor) throws Exception
  {
    return ResourcesUtil.importProject(getDelegate(container), project, monitor);
  }

  /**
   * @author Eike Stepper
   */
  public static final class Member
  {
    private final String name;

    private final Type type;

    public Member(String name, Type type)
    {
      Assert.isNotNull(name);
      Assert.isNotNull(type);

      this.name = name;
      this.type = type;
    }

    public String getName()
    {
      return name;
    }

    public Type getType()
    {
      return type;
    }

    @Override
    public String toString()
    {
      String string = name;
      if (type != Type.FILE)
      {
        string += "/";
      }

      return string;
    }
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

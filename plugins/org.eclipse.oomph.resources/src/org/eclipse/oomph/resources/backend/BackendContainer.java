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
import org.eclipse.oomph.resources.ResourcesUtil.ImportResult;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

/**
 * @author Eike Stepper
 */
public abstract class BackendContainer extends BackendResource
{
  BackendContainer(BackendSystem system, URI systemRelativeURI)
  {
    super(system, systemRelativeURI);
  }

  public final BackendResource[] getMembers(IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    try
    {
      BackendSystem system = getSystem();
      return system.getMembers(this, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final BackendResource findMember(URI relativeURI, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    if (relativeURI.isEmpty())
    {
      return this;
    }

    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    try
    {
      BackendSystem system = getSystem();
      return system.findMember(this, relativeURI, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final BackendResource findMember(String relativePath, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    return findMember(URI.createURI(relativePath), monitor);
  }

  public final BackendResource findMember(IPath relativePath, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    return findMember(URI.createURI(relativePath.toString()), monitor);
  }

  public final BackendFolder getFolder(URI relativeURI) throws BackendException
  {
    URI systemRelativeURI = getSystemRelativeURI().appendSegments(relativeURI.segments());
    return new BackendFolder(getSystem(), systemRelativeURI);
  }

  public final BackendFolder getFolder(String relativePath) throws BackendException
  {
    return getFolder(URI.createURI(relativePath));
  }

  public final BackendFolder getFolder(IPath relativePath) throws BackendException
  {
    return getFolder(URI.createURI(relativePath.toString()));
  }

  public final BackendFile getFile(URI relativeURI) throws BackendException
  {
    URI systemRelativeURI = getSystemRelativeURI().appendSegments(relativeURI.segments());
    return new BackendFile(getSystem(), systemRelativeURI);
  }

  public final BackendFile getFile(String relativePath) throws BackendException
  {
    return getFile(URI.createURI(relativePath));
  }

  public final BackendFile getFile(IPath relativePath) throws BackendException
  {
    return getFile(URI.createURI(relativePath.toString()));
  }

  public final ImportResult importIntoWorkspace(IProject project, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    try
    {
      BackendSystem system = getSystem();
      return system.importIntoWorkspace(this, project, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  @Override
  void doAccept(Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    for (BackendResource member : getMembers(monitor))
    {
      ResourcesPlugin.checkCancelation(monitor);
      member.doAccept(visitor, monitor);
    }
  }
}

/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.resources;

import org.eclipse.oomph.resources.backend.BackendException;
import org.eclipse.oomph.resources.backend.BackendResource;
import org.eclipse.oomph.resources.backend.BackendSystem;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class ExternalResource extends PlatformObject implements IResource, IResourceProxy
{
  public static final QualifiedName BACKEND_RESOURCE_PROPERTY_NAME = new QualifiedName("org.eclipse.oomph.resources", "backendResource");

  private final ExternalContainer parent;

  private final BackendResource backendResource;

  protected ExternalResource(ExternalContainer parent, BackendResource backendResource)
  {
    this.parent = parent;
    this.backendResource = backendResource;
  }

  protected BackendResource getBackendResource()
  {
    return backendResource;
  }

  protected BackendSystem getBackendSystem()
  {
    return backendResource.getSystem();
  }

  public boolean contains(ISchedulingRule rule)
  {
    return false;
  }

  public boolean isConflicting(ISchedulingRule rule)
  {
    return false;
  }

  public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException
  {
    accept(visitor, DEPTH_INFINITE, 0);
  }

  public void accept(final IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException
  {
    accept(new IResourceVisitor()
    {
      public boolean visit(IResource resource) throws CoreException
      {
        return visitor.visit((ExternalResource)resource);
      }
    }, depth, 0);
  }

  public void accept(IResourceVisitor visitor) throws CoreException
  {
    accept(visitor, DEPTH_INFINITE, 0);
  }

  public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException
  {
    accept(visitor, depth, 0);
  }

  public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException
  {
    visit(visitor, depth);
  }

  protected boolean visit(IResourceVisitor visitor, int depth) throws CoreException
  {
    return visitor.visit(this);
  }

  public void clearHistory(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IMarker createMarker(String type) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IResourceProxy createProxy()
  {
    return this;
  }

  public void delete(boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public boolean exists()
  {
    return backendResource.exists(new NullProgressMonitor());
  }

  public IMarker findMarker(long id) throws CoreException
  {
    return null;
  }

  public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return new IMarker[0];
  }

  public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return IMarker.SEVERITY_INFO;
  }

  public String getFileExtension()
  {
    return getFullPath().getFileExtension();
  }

  public IPath getFullPath()
  {
    return parent.getFullPath().append(getName());
  }

  public long getLocalTimeStamp()
  {
    return backendResource.getLastModified(null);
  }

  public IPath getLocation()
  {
    return backendResource.getLocation();
  }

  public URI getLocationURI()
  {
    try
    {
      if (backendResource.isLocal())
      {
        return new File(backendResource.getLocation().toString()).toURI();
      }

      return URIUtil.fromString(backendResource.getAbsoluteURI().toString());
    }
    catch (URISyntaxException ex)
    {
      throw new BackendException(ex);
    }
  }

  public IMarker getMarker(long id)
  {
    return null;
  }

  public long getModificationStamp()
  {
    return getLocalTimeStamp();
  }

  public String getName()
  {
    return backendResource.getName();
  }

  public IPathVariableManager getPathVariableManager()
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public IContainer getParent()
  {
    return parent;
  }

  public Map<QualifiedName, String> getPersistentProperties() throws CoreException
  {
    return Collections.emptyMap();
  }

  public String getPersistentProperty(QualifiedName key) throws CoreException
  {
    return null;
  }

  public IProject getProject()
  {
    return parent.getProject();
  }

  public IPath getProjectRelativePath()
  {
    return getLocation().makeRelativeTo(getProject().getLocation());
  }

  public IPath getRawLocation()
  {
    return getLocation();
  }

  public URI getRawLocationURI()
  {
    return getLocationURI();
  }

  public ResourceAttributes getResourceAttributes()
  {
    return null;
  }

  public Map<QualifiedName, Object> getSessionProperties()
  {
    return Collections.singletonMap(BACKEND_RESOURCE_PROPERTY_NAME, (Object)backendResource);
  }

  public Object getSessionProperty(QualifiedName key)
  {
    if (BACKEND_RESOURCE_PROPERTY_NAME.equals(key))
    {
      return backendResource;
    }

    return null;
  }

  public IWorkspace getWorkspace()
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public boolean isAccessible()
  {
    return true;
  }

  public boolean isDerived()
  {
    return false;
  }

  public boolean isDerived(int options)
  {
    return false;
  }

  public boolean isHidden()
  {
    return false;
  }

  public boolean isHidden(int options)
  {
    return false;
  }

  public boolean isLinked()
  {
    return false;
  }

  public boolean isVirtual()
  {
    return false;
  }

  public boolean isLinked(int options)
  {
    return false;
  }

  @Deprecated
  public boolean isLocal(int depth)
  {
    return true;
  }

  public boolean isPhantom()
  {
    return false;
  }

  @Deprecated
  public boolean isReadOnly()
  {
    return true;
  }

  public boolean isSynchronized(int depth)
  {
    return true;
  }

  public boolean isTeamPrivateMember()
  {
    return false;
  }

  public boolean isTeamPrivateMember(int options)
  {
    return false;
  }

  public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException
  {
    // Do nothing
  }

  public void revertModificationStamp(long value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Deprecated
  public void setDerived(boolean isDerived) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setHidden(boolean isHidden) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Deprecated
  public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public long setLocalTimeStamp(long value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setPersistentProperty(QualifiedName key, String value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Deprecated
  public void setReadOnly(boolean readOnly)
  {
  }

  public void setResourceAttributes(ResourceAttributes attributes) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setSessionProperty(QualifiedName key, Object value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void touch(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IPath requestFullPath()
  {
    return getFullPath();
  }

  public IResource requestResource()
  {
    return this;
  }

  @Override
  public String toString()
  {
    return getTypeString() + getFullPath();
  }

  private String getTypeString()
  {
    switch (getType())
    {
      case FILE:
        return "L"; //$NON-NLS-1$

      case FOLDER:
        return "F"; //$NON-NLS-1$

      case PROJECT:
        return "P"; //$NON-NLS-1$

      case ROOT:
        return "R"; //$NON-NLS-1$
    }

    return ""; //$NON-NLS-1$
  }

  /**
   * @author Eike Stepper
   */
  public static class ReadOnlyException extends CoreException
  {
    private static final long serialVersionUID = 1L;

    public ReadOnlyException()
    {
      super(Status.CANCEL_STATUS);
    }
  }
}

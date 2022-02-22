/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
  public static final QualifiedName BACKEND_RESOURCE_PROPERTY_NAME = new QualifiedName("org.eclipse.oomph.resources", "backendResource"); //$NON-NLS-1$ //$NON-NLS-2$

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

  @Override
  public boolean contains(ISchedulingRule rule)
  {
    return false;
  }

  @Override
  public boolean isConflicting(ISchedulingRule rule)
  {
    return false;
  }

  @Override
  public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException
  {
    accept(visitor, DEPTH_INFINITE, 0);
  }

  @Override
  public void accept(final IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException
  {
    accept(new IResourceVisitor()
    {
      @Override
      public boolean visit(IResource resource) throws CoreException
      {
        return visitor.visit((ExternalResource)resource);
      }
    }, depth, 0);
  }

  @Override
  public void accept(IResourceVisitor visitor) throws CoreException
  {
    accept(visitor, DEPTH_INFINITE, 0);
  }

  @Override
  public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException
  {
    accept(visitor, depth, 0);
  }

  @Override
  public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException
  {
    visit(visitor, depth);
  }

  protected boolean visit(IResourceVisitor visitor, int depth) throws CoreException
  {
    return visitor.visit(this);
  }

  @Override
  public void clearHistory(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public IMarker createMarker(String type) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public IMarker createMarker(String type, Map<String, ? extends Object> attributes) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public IResourceProxy createProxy()
  {
    return this;
  }

  @Override
  public void delete(boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public int hashCode()
  {
    // Same as o.e.c.i.Resource.hashCode()
    return getFullPath().hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    // Similar to o.e.c.i.Resource.equals(Object)
    if (this == obj)
    {
      return true;
    }
    if (!(obj instanceof ExternalResource))
    {
      return false;
    }
    ExternalResource other = (ExternalResource)obj;
    return getType() == other.getType() && getFullPath().equals(other.getFullPath());
  }

  @Override
  public boolean exists()
  {
    return backendResource.exists(new NullProgressMonitor());
  }

  @Override
  public IMarker findMarker(long id) throws CoreException
  {
    return null;
  }

  @Override
  public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return new IMarker[0];
  }

  @Override
  public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return IMarker.SEVERITY_INFO;
  }

  @Override
  public String getFileExtension()
  {
    return getFullPath().getFileExtension();
  }

  @Override
  public IPath getFullPath()
  {
    return parent.getFullPath().append(getName());
  }

  @Override
  public long getLocalTimeStamp()
  {
    return backendResource.getLastModified(null);
  }

  @Override
  public IPath getLocation()
  {
    return backendResource.getLocation();
  }

  @Override
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

  @Override
  public IMarker getMarker(long id)
  {
    return null;
  }

  @Override
  public long getModificationStamp()
  {
    return getLocalTimeStamp();
  }

  @Override
  public String getName()
  {
    return backendResource.getName();
  }

  @Override
  public IPathVariableManager getPathVariableManager()
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public IContainer getParent()
  {
    return parent;
  }

  @Override
  public Map<QualifiedName, String> getPersistentProperties() throws CoreException
  {
    return Collections.emptyMap();
  }

  @Override
  public String getPersistentProperty(QualifiedName key) throws CoreException
  {
    return null;
  }

  @Override
  public IProject getProject()
  {
    return parent.getProject();
  }

  @Override
  public IPath getProjectRelativePath()
  {
    return getLocation().makeRelativeTo(getProject().getLocation());
  }

  @Override
  public IPath getRawLocation()
  {
    return getLocation();
  }

  @Override
  public URI getRawLocationURI()
  {
    return getLocationURI();
  }

  @Override
  public ResourceAttributes getResourceAttributes()
  {
    return null;
  }

  @Override
  public Map<QualifiedName, Object> getSessionProperties()
  {
    return Collections.singletonMap(BACKEND_RESOURCE_PROPERTY_NAME, (Object)backendResource);
  }

  @Override
  public Object getSessionProperty(QualifiedName key)
  {
    if (BACKEND_RESOURCE_PROPERTY_NAME.equals(key))
    {
      return backendResource;
    }

    return null;
  }

  @Override
  public IWorkspace getWorkspace()
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAccessible()
  {
    return true;
  }

  @Override
  public boolean isDerived()
  {
    return false;
  }

  @Override
  public boolean isDerived(int options)
  {
    return false;
  }

  @Override
  public boolean isHidden()
  {
    return false;
  }

  @Override
  public boolean isHidden(int options)
  {
    return false;
  }

  @Override
  public boolean isLinked()
  {
    return false;
  }

  @Override
  public boolean isVirtual()
  {
    return false;
  }

  @Override
  public boolean isLinked(int options)
  {
    return false;
  }

  @Override
  @Deprecated
  public boolean isLocal(int depth)
  {
    return true;
  }

  @Override
  public boolean isPhantom()
  {
    return false;
  }

  @Override
  @Deprecated
  public boolean isReadOnly()
  {
    return true;
  }

  @Override
  public boolean isSynchronized(int depth)
  {
    return true;
  }

  @Override
  public boolean isTeamPrivateMember()
  {
    return false;
  }

  @Override
  public boolean isTeamPrivateMember(int options)
  {
    return false;
  }

  @Override
  public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException
  {
    // Do nothing
  }

  @Override
  public void revertModificationStamp(long value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  @Deprecated
  public void setDerived(boolean isDerived) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setHidden(boolean isHidden) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  @Deprecated
  public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public long setLocalTimeStamp(long value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setPersistentProperty(QualifiedName key, String value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  @Deprecated
  public void setReadOnly(boolean readOnly)
  {
  }

  @Override
  public void setResourceAttributes(ResourceAttributes attributes) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setSessionProperty(QualifiedName key, Object value) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public void touch(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  @Override
  public IPath requestFullPath()
  {
    return getFullPath();
  }

  @Override
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

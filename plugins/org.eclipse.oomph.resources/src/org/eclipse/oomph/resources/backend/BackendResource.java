/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources.backend;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import java.io.File;
import java.util.Queue;

/**
 * @author Eike Stepper
 */
public abstract class BackendResource
{
  public static final String RESOURCE_SCHEME = "archive";

  private final BackendSystem system;

  private final URI systemRelativeURI;

  BackendResource(BackendSystem system, URI systemRelativeURI)
  {
    this.system = system;

    if (systemRelativeURI.hasTrailingPathSeparator())
    {
      systemRelativeURI = systemRelativeURI.trimSegments(1);
    }

    this.systemRelativeURI = systemRelativeURI;
  }

  public final BackendSystem getSystem()
  {
    return system != null ? system : (BackendSystem)this;
  }

  public abstract Type getType();

  public final boolean isContainer()
  {
    return getType() != Type.FILE;
  }

  public final String getName()
  {
    return URI.decode(StringUtil.safe(systemRelativeURI.lastSegment()));
  }

  public final BackendContainer getParent() throws BackendException
  {
    try
    {
      int segmentCount = systemRelativeURI.segmentCount();
      if (segmentCount == 0)
      {
        return null;
      }

      BackendSystem system = getSystem();
      if (segmentCount == 1)
      {
        return system;
      }

      return system.getFolder(systemRelativeURI.trimSegments(1));
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final URI getAbsoluteURI() throws BackendException
  {
    try
    {
      BackendSystem system = getSystem();
      String uri = RESOURCE_SCHEME + ":" + system.getSystemURI() + "!";
      if (!systemRelativeURI.isEmpty())
      {
        uri += "/" + systemRelativeURI;
      }

      if (isContainer())
      {
        uri += "/";
      }

      return URI.createURI(uri);
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final URI getSystemRelativeURI()
  {
    return systemRelativeURI;
  }

  public final String getSystemRelativePath()
  {
    return systemRelativeURI.toFileString();
  }

  public final URI getRelativeURI(BackendContainer base)
  {
    if (base.getSystem() == system)
    {
      URI baseURI = makeAbsolute(base.getSystemRelativeURI()).appendSegment("");
      URI uri = makeAbsolute(systemRelativeURI).appendSegment("");
      return uri.deresolve(baseURI, true, true, false).trimSegments(1);
    }

    return null;
  }

  public final String getRelativePath(BackendContainer base)
  {
    URI relativeURI = getRelativeURI(base);
    if (relativeURI != null)
    {
      return relativeURI.toFileString();
    }

    return null;
  }

  public final boolean isLocal() throws BackendException
  {
    return getLocation() != null;
  }

  public final IPath getLocation() throws BackendException
  {
    try
    {
      BackendSystem system = getSystem();
      return system.getLocation(this);
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final boolean exists(IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    try
    {
      BackendSystem system = getSystem();
      return system.exists(this, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final long getLastModified(IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    try
    {
      BackendSystem system = getSystem();
      return system.getLastModified(this, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final Object getDelegate() throws BackendException
  {
    try
    {
      BackendSystem system = getSystem();
      return system.getDelegate(this);
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public final void accept(Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    try
    {
      BackendSystem system = getSystem();
      system.accept(this, visitor, monitor);
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  abstract void visit(Queue<BackendResource> queue, Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException;

  @Override
  public final int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (system == null ? 0 : system.hashCode());
    result = prime * result + (systemRelativeURI == null ? 0 : systemRelativeURI.hashCode());
    return result;
  }

  @Override
  public final boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (!(obj instanceof BackendResource))
    {
      return false;
    }

    BackendResource other = (BackendResource)obj;
    if (system == null)
    {
      return this == obj;
    }

    if (system != other.system)
    {
      return false;
    }

    if (systemRelativeURI == null)
    {
      if (other.systemRelativeURI != null)
      {
        return false;
      }
    }
    else if (!systemRelativeURI.equals(other.systemRelativeURI))
    {
      return false;
    }

    return true;
  }

  @Override
  public final String toString()
  {
    return getAbsoluteURI().toString();
  }

  public static BackendResource get(URI absoluteURI) throws BackendException
  {
    try
    {
      URI systemURI;
      URI systemRelativeURI = null;

      String scheme = absoluteURI.scheme();
      if (RESOURCE_SCHEME.equals(scheme))
      {
        String authority = absoluteURI.authority();
        if (authority.endsWith("!"))
        {
          authority = authority.substring(0, authority.length() - 1);
        }

        systemURI = URI.createURI(authority);

        if (absoluteURI.segmentCount() != 0)
        {
          systemRelativeURI = URI.createHierarchicalURI(absoluteURI.segments(), null, null);
        }
      }
      else
      {
        systemURI = absoluteURI;
      }

      BackendSystem backendSystem = BackendSystem.Registry.INSTANCE.getBackendSystem(systemURI);
      if (systemRelativeURI == null)
      {
        return backendSystem;
      }

      if (systemRelativeURI.hasTrailingPathSeparator())
      {
        return backendSystem.getFolder(systemRelativeURI);
      }

      return backendSystem.getFile(systemRelativeURI);
    }
    catch (BackendException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  public static BackendResource get(String rootFolder) throws BackendException
  {
    String prefix = RESOURCE_SCHEME + ":";
    if (rootFolder.startsWith(prefix))
    {
      URI uri = URI.createURI(rootFolder);
      return get(uri);
    }

    File folder = new File(rootFolder);
    String absolutePath = folder.getAbsolutePath();

    URI fileURI = URI.createFileURI(absolutePath);
    URI uri = URI.createURI(prefix + fileURI.trimSegments(fileURI.segmentCount()).toString() + "!/").appendSegments(fileURI.segments());

    if (folder.isDirectory())
    {
      uri = uri.appendSegment("");
    }

    return get(uri);
  }

  private static URI makeAbsolute(URI uri)
  {
    return URI.createHierarchicalURI("absolute", null, null, uri.segments(), null, null);
  }

  /**
   * @author Eike Stepper
   */
  public static enum Type
  {
    SYSTEM, FOLDER, FILE
  }

  /**
     * @author Eike Stepper
     */
  public interface Visitor
  {
    public boolean visit(BackendSystem system, IProgressMonitor monitor) throws BackendException;

    public boolean visit(BackendFolder folder, IProgressMonitor monitor) throws BackendException;

    public boolean visit(BackendFile file, IProgressMonitor monitor) throws BackendException;

    /**
     * @author Eike Stepper
     */
    public static class Default implements Visitor
    {
      public boolean visitContainer(BackendContainer container, IProgressMonitor monitor) throws BackendException
      {
        return visitDefault(container, monitor);
      }

      public boolean visit(BackendSystem system, IProgressMonitor monitor) throws BackendException
      {
        return visitContainer(system, monitor);
      }

      public boolean visit(BackendFolder folder, IProgressMonitor monitor) throws BackendException
      {
        return visitContainer(folder, monitor);
      }

      public boolean visit(BackendFile file, IProgressMonitor monitor) throws BackendException
      {
        return visitDefault(file, monitor);
      }

      protected boolean visitDefault(BackendResource resource, IProgressMonitor monitor)
      {
        return true;
      }
    }
  }
}

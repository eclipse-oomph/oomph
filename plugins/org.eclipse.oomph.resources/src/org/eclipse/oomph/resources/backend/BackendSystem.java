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
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author Eike Stepper
 */
public abstract class BackendSystem extends BackendContainer
{
  private static final BackendResource[] NO_MEMBERS = new BackendResource[0];

  private static final String[] EMPTY_SEGMENTS = {};

  private static final URI EMPTY_URI = URI.createHierarchicalURI(EMPTY_SEGMENTS, null, null);

  private final String systemURI; // Store as string to not lock this system in the system registry's weak map.

  protected BackendSystem(URI systemURI) throws BackendException
  {
    super(null, EMPTY_URI);
    this.systemURI = systemURI.toString();
  }

  public final URI getSystemURI()
  {
    return URI.createURI(systemURI);
  }

  @Override
  public final Type getResourceType()
  {
    return Type.SYSTEM;
  }

  protected abstract Object getDelegate(BackendResource resource) throws Exception;

  protected abstract Object[] getDelegateMembers(Object containerDelegate, IProgressMonitor monitor) throws Exception;

  protected abstract Object getDelegateMember(Object containerDelegate, String relativePath, IProgressMonitor monitor) throws Exception;

  protected abstract String getDelegateName(Object resourceDelegate) throws Exception;

  protected abstract Type getDelegateType(Object resourceDelegate, boolean checkExists) throws Exception;

  protected abstract IPath getLocation(BackendResource backendResource) throws Exception;

  protected abstract boolean exists(BackendResource backendResource, IProgressMonitor monitor) throws Exception;

  protected abstract long getLastModified(BackendResource backendResource, IProgressMonitor monitor) throws Exception;

  protected abstract InputStream getContents(BackendFile backendFile, IProgressMonitor monitor) throws Exception;

  protected abstract ImportResult importIntoWorkspace(BackendContainer backendContainer, IProject project, IProgressMonitor monitor) throws Exception;

  protected final BackendFolder createBackendFolder(URI systemRelativeURI)
  {
    return new BackendFolder(this, systemRelativeURI);
  }

  protected final BackendFile createBackendFile(URI systemRelativeURI)
  {
    return new BackendFile(this, systemRelativeURI);
  }

  protected BackendResource[] getMembers(BackendContainer backendContainer, IProgressMonitor monitor) throws Exception
  {
    Object delegate = getDelegate(backendContainer);

    Object[] delegateMembers = getDelegateMembers(delegate, monitor);
    if (delegateMembers == null)
    {
      return NO_MEMBERS;
    }

    BackendResource[] result = new BackendResource[delegateMembers.length];
    for (int i = 0; i < delegateMembers.length; i++)
    {
      ResourcesPlugin.checkCancelation(monitor);

      Object delegateMember = delegateMembers[i];
      String name = getDelegateName(delegateMember);
      URI systemRelativeURI = backendContainer.getSystemRelativeURI().appendSegment(name);

      result[i] = createMember(delegateMember, systemRelativeURI, false);
    }

    return result;
  }

  protected BackendResource findMember(BackendContainer backendContainer, URI relativeURI, IProgressMonitor monitor) throws Exception
  {
    Object delegate = getDelegate(backendContainer);

    Object delegateMember = getDelegateMember(delegate, relativeURI.toString(), monitor);
    URI systemRelativeURI = backendContainer.getSystemRelativeURI().appendSegments(relativeURI.segments());

    return createMember(delegateMember, systemRelativeURI, true);
  }

  private BackendResource createMember(Object delegateMember, URI systemRelativeURI, boolean checkExists) throws Exception
  {
    BackendSystem system = getSystem();
    Type type = system.getDelegateType(delegateMember, checkExists);
    if (type == null)
    {
      return null;
    }

    switch (type)
    {
      case FOLDER:
        return system.createBackendFolder(systemRelativeURI);

      case FILE:
        return system.createBackendFile(systemRelativeURI);

      default:
        throw new BackendException("The " + type.toString().toLowerCase() + " can't be a member: " + delegateMember);
    }
  }

  @Override
  void doAccept(Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    visitor.visit(this);
    super.doAccept(visitor, monitor);
  }

  /**
   * @author Eike Stepper
   */
  public static final class Registry
  {
    public static final Registry INSTANCE = new Registry();

    private final Map<URI, BackendSystem> backendSystems = new WeakHashMap<URI, BackendSystem>();

    private Registry()
    {
    }

    public synchronized BackendSystem getBackendSystem(URI systemURI) throws BackendException
    {
      if (systemURI.hasTrailingPathSeparator())
      {
        systemURI = systemURI.trimSegments(1);
      }

      BackendSystem backendSystem = backendSystems.get(systemURI);
      if (backendSystem == null)
      {
        IFactory factory = IFactory.Registry.INSTANCE.getFactory(systemURI.scheme());
        backendSystem = factory.createBackendSystem(systemURI);
        backendSystems.put(systemURI, backendSystem);
      }

      return backendSystem;
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface IFactory
  {
    public BackendSystem createBackendSystem(URI systemURI) throws BackendException;

    /**
     * @author Eike Stepper
     */
    public static final class Registry
    {
      public static final Registry INSTANCE = new Registry();

      private final Map<String, IFactory> factories = new HashMap<String, IFactory>();

      private Registry()
      {
        addFactory("file", new LocalBackendSystem.Factory());
      }

      private IFactory loadFactory(String scheme) throws BackendException
      {
        if (ResourcesPlugin.INSTANCE.isOSGiRunning())
        {
          try
          {
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

            for (IConfigurationElement configurationElement : extensionRegistry
                .getConfigurationElementsFor("org.eclipse.oomph.resources.backendSystemFactories"))
            {
              String factoryScheme = configurationElement.getAttribute("scheme");
              if (ObjectUtil.equals(factoryScheme, scheme))
              {
                try
                {
                  return (IFactory)configurationElement.createExecutableExtension("class");
                }
                catch (Exception ex)
                {
                  ResourcesPlugin.INSTANCE.log(ex);
                }
              }
            }
          }
          catch (Exception ex)
          {
            ResourcesPlugin.INSTANCE.log(ex);
          }
        }

        throw new BackendException("Backend system factory with scheme '" + scheme + "' not found");
      }

      public synchronized IFactory getFactory(String scheme) throws BackendException
      {
        IFactory factory = factories.get(scheme);
        if (factory == null)
        {
          factory = loadFactory(scheme);
          factories.put(scheme, factory);
        }

        return factory;
      }

      public synchronized IFactory addFactory(String scheme, IFactory factory) throws BackendException
      {
        return factories.put(scheme, factory);
      }

      public synchronized IFactory removeFactory(String scheme) throws BackendException
      {
        return factories.remove(scheme);
      }

      public synchronized Set<String> removeFactory(IFactory factory) throws BackendException
      {
        Set<String> schemes = new HashSet<String>();
        for (Iterator<Map.Entry<String, IFactory>> it = factories.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry<String, IFactory> entry = it.next();
          if (entry.getValue() == factory)
          {
            schemes.add(entry.getKey());
            it.remove();
          }
        }

        return schemes;
      }
    }
  }
}

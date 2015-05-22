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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.ReflectUtil.ReflectionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.internal.p2.artifact.repository.ArtifactRepositoryManager;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.internal.p2.repository.helpers.AbstractRepositoryManager;
import org.eclipse.equinox.internal.p2.repository.helpers.LocationProperties;
import org.eclipse.equinox.internal.provisional.p2.repository.RepositoryEvent;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingRepositoryManager<T>
{
  private static final Method METHOD_checkValidLocation = ReflectUtil.getMethod(AbstractRepositoryManager.class, "checkValidLocation", URI.class);

  private static final Method METHOD_enterLoad = ReflectUtil.getMethod(AbstractRepositoryManager.class, "enterLoad", URI.class, IProgressMonitor.class);

  private static final Method METHOD_basicGetRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "basicGetRepository", URI.class);

  // private static final Method METHOD_checkNotFound = ReflectUtil.getMethod(AbstractRepositoryManager.class, "checkNotFound", URI.class);

  private static final Method METHOD_fail = ReflectUtil.getMethod(AbstractRepositoryManager.class, "fail", URI.class, int.class);

  private static final Method METHOD_addRepository1 = ReflectUtil.getMethod(AbstractRepositoryManager.class, "addRepository", URI.class, boolean.class,
      boolean.class);

  private static final Method METHOD_loadIndexFile = ReflectUtil.getMethod(AbstractRepositoryManager.class, "loadIndexFile", URI.class, IProgressMonitor.class);

  private static final Method METHOD_getPreferredRepositorySearchOrder = ReflectUtil.getMethod(AbstractRepositoryManager.class,
      "getPreferredRepositorySearchOrder", LocationProperties.class);

  private static final Method METHOD_getAllSuffixes = ReflectUtil.getMethod(AbstractRepositoryManager.class, "getAllSuffixes");

  private static final Method METHOD_sortSuffixes = ReflectUtil.getMethod(AbstractRepositoryManager.class, "sortSuffixes", String[].class, URI.class,
      String[].class);

  private static final Method METHOD_loadRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "loadRepository", URI.class, String.class,
      String.class, int.class, SubMonitor.class);

  private static final Method METHOD_addRepository2 = ReflectUtil.getMethod(AbstractRepositoryManager.class, "addRepository", IRepository.class, boolean.class,
      String.class);

  private static final Method METHOD_removeRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "removeRepository", URI.class, boolean.class);

  // private static final Method METHOD_rememberNotFound = ReflectUtil.getMethod(AbstractRepositoryManager.class, "rememberNotFound", URI.class);

  private static final Method METHOD_exitLoad = ReflectUtil.getMethod(AbstractRepositoryManager.class, "exitLoad", URI.class);

  private static final Method METHOD_broadcastChangeEvent = ReflectUtil.getMethod(AbstractRepositoryManager.class, "broadcastChangeEvent", URI.class, int.class,
      int.class, boolean.class);

  private static final String PROPERTY_VERSION = "version";

  private static final String PROPERTY_GENERATED = "generated";

  private final AbstractRepositoryManager<T> delegate;

  private final int repositoryType;

  private final CachingTransport transport;

  public CachingRepositoryManager(AbstractRepositoryManager<T> delegate, int repositoryType, CachingTransport transport)
  {
    this.delegate = delegate;
    this.repositoryType = repositoryType;
    this.transport = transport;
  }

  public IRepository<T> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
  {
    checkValidLocation(location);
    SubMonitor sub = SubMonitor.convert(monitor, 100);
    boolean added = false;
    IRepository<T> result = null;

    try
    {
      CachingTransport.startLoadingRepository(location);
      enterLoad(location, sub.newChild(5));

      result = basicGetRepository(location);
      if (result != null)
      {
        return result;
      }

      // if (checkNotFound(location))
      // {
      // fail(location, ProvisionException.REPOSITORY_NOT_FOUND);
      // }

      // Add the repository first so that it will be enabled, but don't send add event until after the load.
      added = addRepository(location, true, false);

      LocationProperties indexFile = loadIndexFile(location, sub.newChild(15));
      String[] preferredOrder = getPreferredRepositorySearchOrder(indexFile);
      String[] allSuffixes = getAllSuffixes();
      String[] suffixes = sortSuffixes(allSuffixes, location, preferredOrder);

      sub = SubMonitor.convert(sub, NLS.bind("Adding repository {0}", location), suffixes.length * 100);
      ProvisionException failure = null;

      try
      {
        for (int i = 0; i < suffixes.length; i++)
        {
          if (sub.isCanceled())
          {
            throw new OperationCanceledException();
          }

          try
          {
            result = loadRepository(location, suffixes[i], type, flags, sub.newChild(100));
          }
          catch (ProvisionException e)
          {
            failure = e;
            break;
          }

          if (result != null)
          {
            addRepository(result, false, suffixes[i]);

            if (!indexFile.exists() || preferredOrder.length == 0)
            {
              cacheIndexFile(location, suffixes[i], allSuffixes);
            }

            break;
          }
        }
      }
      finally
      {
        sub.done();
      }

      if (result == null)
      {
        // If we just added the repository, remove it because it cannot be loaded.
        if (added)
        {
          removeRepository(location, false);
        }

        // Eagerly cleanup missing system repositories.
        if (Boolean.valueOf(delegate.getRepositoryProperty(location, IRepository.PROP_SYSTEM)).booleanValue())
        {
          delegate.removeRepository(location);
        }
        // else if (failure == null || failure.getStatus().getCode() != ProvisionException.REPOSITORY_FAILED_AUTHENTICATION
        // && failure.getStatus().getCode() != ProvisionException.REPOSITORY_FAILED_READ)
        // {
        // rememberNotFound(location);
        // }

        if (failure != null)
        {
          throw failure;
        }

        fail(location, ProvisionException.REPOSITORY_NOT_FOUND);
      }
    }
    finally
    {
      CachingTransport.stopLoadingRepository();
      exitLoad(location);
    }

    // Broadcast the add event after releasing lock.
    if (added)
    {
      broadcastChangeEvent(location, repositoryType, RepositoryEvent.ADDED, true);
    }

    return result;
  }

  private File getCachedIndexFile(URI location)
  {
    try
    {
      String path = location.toString();
      if (!path.endsWith("/"))
      {
        path += "/";
      }

      return transport.getCacheFile(new URI(path + "p2.index"));
    }
    catch (URISyntaxException ex)
    {
      // Can't happen.
      throw new RuntimeException(ex);
    }
  }

  private void cacheIndexFile(URI location, String suffix, String[] allSuffixes)
  {
    if ("file".equals(location.getScheme()))
    {
      return;
    }

    Map<String, String> properties;

    File cachedIndexFile = getCachedIndexFile(location);
    if (cachedIndexFile.exists())
    {
      properties = PropertiesUtil.loadProperties(cachedIndexFile);
    }
    else
    {
      properties = new LinkedHashMap<String, String>();
      properties.put(PROPERTY_VERSION, "1");
      properties.put(PROPERTY_GENERATED, "true");
    }

    String prefix = repositoryType == IRepository.TYPE_METADATA ? "metadata" : "artifact";

    StringBuilder builder = new StringBuilder(suffix);
    for (String otherSuffix : allSuffixes)
    {
      if (!ObjectUtil.equals(otherSuffix, suffix))
      {
        builder.append(",");
        builder.append(otherSuffix);
      }
    }

    properties.put(prefix + ".repository.factory.order", builder.toString() + ",!");
    PropertiesUtil.saveProperties(cachedIndexFile, properties, false);
  }

  private URI checkValidLocation(URI location)
  {
    return (URI)ReflectUtil.invokeMethod(METHOD_checkValidLocation, delegate, location);
  }

  private void enterLoad(URI location, IProgressMonitor monitor)
  {
    ReflectUtil.invokeMethod(METHOD_enterLoad, delegate, location, monitor);
  }

  @SuppressWarnings("unchecked")
  protected IRepository<T> basicGetRepository(URI location)
  {
    return (IRepository<T>)ReflectUtil.invokeMethod(METHOD_basicGetRepository, delegate, location);
  }

  // private boolean checkNotFound(URI location)
  // {
  // return (Boolean)ReflectUtil.invokeMethod(METHOD_checkNotFound, delegate, location);
  // }
  //
  // private void rememberNotFound(URI location)
  // {
  // ReflectUtil.invokeMethod(METHOD_rememberNotFound, delegate, location);
  // }

  private void fail(URI location, int code) throws ProvisionException
  {
    try
    {
      ReflectUtil.invokeMethod(METHOD_fail, delegate, location, code);
    }
    catch (ReflectionException ex)
    {
      Throwable cause = ex.getCause();
      if (cause instanceof ProvisionException)
      {
        throw (ProvisionException)cause;
      }

      throw ex;
    }
  }

  private boolean addRepository(URI location, boolean isEnabled, boolean signalAdd)
  {
    return (Boolean)ReflectUtil.invokeMethod(METHOD_addRepository1, delegate, location, isEnabled, signalAdd);
  }

  private LocationProperties loadIndexFile(URI location, IProgressMonitor monitor)
  {
    return (LocationProperties)ReflectUtil.invokeMethod(METHOD_loadIndexFile, delegate, location, monitor);
  }

  protected String[] getPreferredRepositorySearchOrder(LocationProperties properties)
  {
    return (String[])ReflectUtil.invokeMethod(METHOD_getPreferredRepositorySearchOrder, delegate, properties);
  }

  protected String[] getAllSuffixes()
  {
    return (String[])ReflectUtil.invokeMethod(METHOD_getAllSuffixes, delegate);
  }

  protected String[] sortSuffixes(String[] suffixes, URI location, String[] preferredOrder)
  {
    return (String[])ReflectUtil.invokeMethod(METHOD_sortSuffixes, delegate, suffixes, location, preferredOrder);
  }

  @SuppressWarnings("unchecked")
  private IRepository<T> loadRepository(URI location, String suffix, String type, int flags, SubMonitor monitor) throws ProvisionException
  {
    try
    {
      return (IRepository<T>)ReflectUtil.invokeMethod(METHOD_loadRepository, delegate, location, suffix, type, flags, monitor);
    }
    catch (ReflectionException ex)
    {
      Throwable cause = ex.getCause();
      if (cause instanceof ProvisionException)
      {
        throw (ProvisionException)cause;
      }

      throw ex;
    }
  }

  protected void addRepository(IRepository<T> repository, boolean signalAdd, String suffix)
  {
    ReflectUtil.invokeMethod(METHOD_addRepository2, delegate, repository, signalAdd, suffix);
  }

  private boolean removeRepository(URI toRemove, boolean signalRemove)
  {
    return (Boolean)ReflectUtil.invokeMethod(METHOD_removeRepository, delegate, toRemove, signalRemove);
  }

  private void exitLoad(URI location)
  {
    ReflectUtil.invokeMethod(METHOD_exitLoad, delegate, location);
  }

  private void broadcastChangeEvent(URI location, int repositoryType, int kind, boolean isEnabled)
  {
    ReflectUtil.invokeMethod(METHOD_broadcastChangeEvent, delegate, location, repositoryType, kind, isEnabled);
  }

  /**
   * @author Eike Stepper
   */
  public static final class Metadata extends MetadataRepositoryManager
  {
    private final CachingRepositoryManager<IInstallableUnit> loader;

    public Metadata(IProvisioningAgent agent, CachingTransport transport)
    {
      super(agent);
      loader = new CachingRepositoryManager<IInstallableUnit>(this, IRepository.TYPE_METADATA, transport);
    }

    @Override
    protected IRepository<IInstallableUnit> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
    {
      return loader.loadRepository(location, monitor, type, flags);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Artifact extends ArtifactRepositoryManager
  {
    private final CachingRepositoryManager<IArtifactKey> loader;

    public Artifact(IProvisioningAgent agent, CachingTransport transport)
    {
      super(agent);
      loader = new CachingRepositoryManager<IArtifactKey>(this, IRepository.TYPE_ARTIFACT, transport);
    }

    @Override
    protected IRepository<IArtifactKey> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
    {
      return loader.loadRepository(location, monitor, type, flags);
    }
  }
}

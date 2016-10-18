/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BytesResourceFactoryImpl;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.WorkerPool;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class ResourceMirror extends WorkerPool<ResourceMirror, URI, ResourceMirror.LoadJob>
{
  private ResourceSet resourceSet;

  private DelegatingResourceLocator resourceLocator;

  private HashSet<URI> visited = new HashSet<URI>();

  public ResourceMirror()
  {
    this(SetupCoreUtil.createResourceSet());
  }

  public ResourceMirror(ResourceSet resourceSet)
  {
    this.resourceSet = resourceSet;
    resourceLocator = new DelegatingResourceLocator((ResourceSetImpl)resourceSet);
  }

  @Override
  protected LoadJob createWorker(URI key, int workerID, boolean secondary)
  {
    LoadJob loadJob = new LoadJob(this, key, workerID, secondary);
    loadJob.setSystem(true);
    return loadJob;
  }

  public ResourceSet getResourceSet()
  {
    return resourceSet;
  }

  public void begin(final IProgressMonitor monitor)
  {
    Map<Object, Object> loadOptions = resourceSet.getLoadOptions();
    final String taskName = loadOptions.get(ECFURIHandlerImpl.OPTION_CACHE_HANDLING) == ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING
        ? "Loading from local cache " : "Loading from internet ";
    XMLResource.ResourceHandler resourceHandler = new BasicResourceHandler()
    {
      private int counter;

      private final Set<ResourceSet> resourceSets = new HashSet<ResourceSet>();

      @Override
      public synchronized void preLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options)
      {
        ResourceSet resourceSet = getResourceSet();
        resourceSets.add(resourceSet);

        int total = 0;
        for (ResourceSet rs : resourceSets)
        {
          total += rs.getResources().size();
        }

        ++counter;
        if (total < counter)
        {
          total = counter;
        }

        monitor.setTaskName(taskName + counter + " of " + total);
        monitor.subTask("Loading " + resource.getURI());
        monitor.worked(1);
      }
    };

    Object oldResourceHandler = loadOptions.put(XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);
    loadOptions.put(ECFURIHandlerImpl.OPTION_MONITOR, monitor);

    monitor.beginTask(taskName, 50);

    begin(taskName, monitor);

    loadOptions.put(XMLResource.OPTION_RESOURCE_HANDLER, oldResourceHandler);
    loadOptions.remove(ECFURIHandlerImpl.OPTION_MONITOR);
  }

  protected void resolveProxies()
  {
    IProgressMonitor monitor = getMonitor();
    resourceLocator.dispose();
    resourceLocator = null;
    new ProxyResolver(resourceSet).begin(monitor);
  }

  @Override
  public void dispose()
  {
    resourceSet = null;
    if (resourceLocator != null)
    {
      resourceLocator.dispose();
    }
  }

  @Override
  protected boolean isCompleted(URI uri)
  {
    if (resourceSet == null)
    {
      return true;
    }

    synchronized (resourceSet)
    {
      Resource resource = resourceSet.getResource(uri, false);
      if (resource != null && resource.isLoaded() && !visited.add(uri))
      {
        return true;
      }
    }

    return false;
  }

  protected Resource createResource(URI uri)
  {
    Resource resource;
    synchronized (resourceSet)
    {
      resource = resourceSet.getResource(uri, false);
      if (resource == null)
      {
        resource = resourceSet.createResource(uri);
      }
    }
    return resource;
  }

  protected void visit(EObject eObject)
  {
  }

  /**
   * @author Eike Stepper
   */
  private final class DelegatingResourceLocator extends ResourceSetImpl.ResourceLocator
  {
    private DelegatingResourceLocator(ResourceSetImpl resourceSet)
    {
      super(resourceSet);
    }

    @Override
    public Resource getResource(URI uri, boolean loadOnDemand)
    {
      if (loadOnDemand && "setup".equals(uri.fileExtension()))
      {
        return null;
      }

      return basicGetResource(uri, loadOnDemand);
    }

    @Override
    public void dispose()
    {
      super.dispose();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class LoadJob extends WorkerPool.Worker<URI, ResourceMirror>
  {
    private LoadJob(ResourceMirror resourceMirror, URI uri, int id, boolean secondary)
    {
      super("Load " + uri, resourceMirror, uri, id, secondary);
    }

    @Override
    protected IStatus perform(IProgressMonitor monitor)
    {
      Resource resource = getWorkPool().createResource(getKey());

      if (!resource.isLoaded())
      {
        try
        {
          resource.load(getWorkPool().resourceSet.getLoadOptions());
        }
        catch (Throwable throwable)
        {
          new ResourceSetImpl()
          {
            @Override
            public void handleDemandLoadException(Resource resource, IOException exception) throws RuntimeException
            {
              try
              {
                super.handleDemandLoadException(resource, exception);
              }
              catch (RuntimeException ex)
              {
                // Ignore
              }
            }
          }.handleDemandLoadException(resource, throwable instanceof IOException ? (IOException)throwable : new IOExceptionWithCause(throwable));
        }
      }

      visit(resource);

      return Status.OK_STATUS;
    }

    private void delay()
    {
      // try
      // {
      // Thread.sleep(100);
      // }
      // catch (InterruptedException ex)
      // {
      // ex.printStackTrace();
      // }
    }

    private void visit(Resource resource)
    {
      ResourceMirror workPool = getWorkPool();
      for (Iterator<EObject> it = EcoreUtil.getAllContents(resource, false); it.hasNext();)
      {
        delay();

        if (workPool.isCanceled())
        {
          break;
        }

        EObject eObject = it.next();
        if (eObject.eIsProxy())
        {
          URI proxyURI = ((InternalEObject)eObject).eProxyURI().trimFragment();
          workPool.schedule(proxyURI, false);
        }
        else
        {
          workPool.visit(eObject);

          for (Iterator<EObject> it2 = ((InternalEList<EObject>)eObject.eCrossReferences()).basicListIterator(); it2.hasNext();)
          {
            delay();

            if (workPool.isCanceled())
            {
              break;
            }

            EObject eCrossReference = it2.next();
            if (eCrossReference.eIsProxy())
            {
              URI proxyURI = ((InternalEObject)eCrossReference).eProxyURI().trimFragment();
              workPool.schedule(proxyURI, true);
            }
          }
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class WithProductImages extends ResourceMirror
  {
    public WithProductImages(ResourceSet resourceSet)
    {
      super(resourceSet);

      BytesResourceFactoryImpl bytesResourceFactory = new BytesResourceFactoryImpl();
      Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
      extensionToFactoryMap.put("gif", bytesResourceFactory);
      extensionToFactoryMap.put("png", bytesResourceFactory);
      extensionToFactoryMap.put("jpeg", bytesResourceFactory);
      extensionToFactoryMap.put("jpg", bytesResourceFactory);
    }

    @Override
    protected void visit(EObject eObject)
    {
      if (eObject instanceof Scope)
      {
        Scope scope = (Scope)eObject;
        URI uri = SetupCoreUtil.getBrandingImageURI(scope);
        if (uri != null)
        {
          if (getResourceSet().getResourceFactoryRegistry().getExtensionToFactoryMap().containsKey(uri.fileExtension()))
          {
            schedule(uri, true);
          }
        }
      }

      super.visit(eObject);
    }
  }
}

/*
 * Copyright (c) 2015, 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.util.WorkerPool;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ed Merks
 */
public class ProxyResolver extends WorkerPool<ProxyResolver, Resource, ProxyResolver.ResolveJob>
{
  private ResourceSet resourceSet;

  public ProxyResolver(ResourceSet resourceSet)
  {
    this.resourceSet = resourceSet;
  }

  @Override
  protected ResolveJob createWorker(Resource key, int workerID, boolean secondary)
  {
    ResolveJob resolveJob = new ResolveJob(this, key, workerID, secondary);
    resolveJob.setSystem(true);
    return resolveJob;
  }

  public ResourceSet getResourceSet()
  {
    return resourceSet;
  }

  public void begin(IProgressMonitor monitor)
  {
    final String taskName = Messages.ProxyResolver_Resolving_task;
    super.begin(taskName, monitor);
  }

  @Override
  protected void run(String taskName, final IProgressMonitor monitor)
  {
    final EList<Resource> resources = resourceSet.getResources();
    monitor.setTaskName(Messages.ProxyResolver_Resolving_task);
    monitor.subTask(NLS.bind(Messages.ProxyResolver_ResolvingProxies_task, resources.size()));

    Adapter adapter = new AdapterImpl()
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        if (notification.getEventType() == Notification.ADD)
        {
          Resource resource = (Resource)notification.getNewValue();
          schedule(resource);
          monitor.subTask(NLS.bind(Messages.ProxyResolver_ResolvingProxies_task, resources.size()));
        }
      }
    };

    try
    {
      resourceSet.eAdapters().add(adapter);
      perform(new ArrayList<>(resources));
    }
    finally
    {
      resourceSet.eAdapters().remove(adapter);
    }
  }

  @Override
  public void dispose()
  {
    resourceSet = null;
  }

  /**
   * @author Ed Merks
   */
  public static class ResolveJob extends WorkerPool.Worker<Resource, ProxyResolver>
  {
    private final ResourceSet resourceSet;

    private ResolveJob(ProxyResolver proxyResolver, Resource resource, int id, boolean secondary)
    {
      super(Messages.ProxyResolver_Resolver_job, proxyResolver, resource, id, secondary);
      resourceSet = getWorkPool().getResourceSet();
    }

    @Override
    protected IStatus perform(IProgressMonitor monitor)
    {
      EList<EObject> contents;
      synchronized (resourceSet)
      {
        contents = getKey().getContents();
      }

      for (EObject eObject : contents)
      {
        visit(eObject);
      }

      return Status.OK_STATUS;
    }

    protected void visit(EObject eObject)
    {
      EClass eClass = eObject.eClass();
      List<EObject> properContentObjects = new ArrayList<>();
      for (EReference eReference : eClass.getEAllReferences())
      {
        if (!eReference.isDerived())
        {
          boolean containment = eReference.isContainment();
          if (eReference.isMany())
          {
            @SuppressWarnings("unchecked")
            InternalEList<InternalEObject> eObjects = (InternalEList<InternalEObject>)eObject.eGet(eReference);
            for (int i = 0;;)
            {
              try
              {
                InternalEObject referencedEObject;
                synchronized (resourceSet)
                {
                  if (i >= eObjects.size())
                  {
                    break;
                  }

                  referencedEObject = eObjects.get(i);
                }

                ++i;
                if (containment && referencedEObject.eDirectResource() == null)
                {
                  properContentObjects.add(referencedEObject);
                }
              }
              catch (RuntimeException ex)
              {
                InternalEObject referencedEObject = eObjects.basicGet(i);
                URI eProxyURI = referencedEObject.eProxyURI();
                if (eProxyURI != null)
                {
                  ++i;
                  referencedEObject.eSetProxyURI(BaseUtil.createBogusURI(eProxyURI));
                }
                else
                {
                  eObjects.remove(i);
                }
              }
            }
          }
          else
          {
            try
            {
              InternalEObject referencedEObject;
              synchronized (resourceSet)
              {
                referencedEObject = (InternalEObject)eObject.eGet(eReference);
              }

              if (referencedEObject != null && containment && referencedEObject.eDirectResource() == null)
              {
                properContentObjects.add(referencedEObject);
              }
            }
            catch (RuntimeException ex)
            {
              InternalEObject referencedEObject = (InternalEObject)eObject.eGet(eReference, false);
              URI eProxyURI = referencedEObject.eProxyURI();
              if (eProxyURI != null)
              {
                referencedEObject.eSetProxyURI(BaseUtil.createBogusURI(eProxyURI));
              }
              else
              {
                eObject.eSet(eReference, null);
              }
            }
          }

          if (containment && !properContentObjects.isEmpty())
          {
            for (EObject properContent : properContentObjects)
            {
              visit(properContent);
            }
          }

          properContentObjects.clear();
        }
      }
    }
  }
}

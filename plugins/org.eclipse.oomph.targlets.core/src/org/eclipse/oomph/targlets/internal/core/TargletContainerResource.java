/*
 * Copyright (c) 2024 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.targlets.TargletContainer;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.target.ITargetHandle;

import java.io.IOException;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class TargletContainerResource extends ResourceImpl
{
  public static final String PROTOCOL_NAME = "targlet_container"; //$NON-NLS-1$

  public static final String OPTION_MIRRORS = "MIRRORS"; //$NON-NLS-1$

  private final String targletContainerID;

  private ITargletContainer targletContainer;

  private ITargetHandle targetHandle;

  private Job updateJob;

  private TargletContainerResource(URI uri)
  {
    super(uri);
    targletContainerID = uri.opaquePart();
  }

  public String getTargletContainerID()
  {
    return targletContainerID;
  }

  public ITargetHandle getTargetHandle()
  {
    return targetHandle;
  }

  @Override
  public void load(Map<?, ?> options) throws IOException
  {
    TargletContainer wrapper = TargletFactory.eINSTANCE.createTargletContainer();
    wrapper.setID(targletContainerID);

    targletContainer = TargletContainerDescriptorManager.getContainer(targletContainerID);
    if (targletContainer != null)
    {
      targetHandle = targletContainer.getTargetDefinition().getHandle();

      wrapper.getTarglets().addAll(targletContainer.getTarglets());
      wrapper.getComposedTargets().addAll(targletContainer.getComposedTargets());
    }
    else
    {
      targetHandle = null;
    }

    getContents().clear();
    getContents().add(wrapper); // The add() call sets the resource isLoaded.
  }

  @Override
  public void save(Map<?, ?> options) throws IOException
  {
    if (targletContainer != null)
    {
      boolean mirrors = options != null && options.containsKey(TargletContainerResource.OPTION_MIRRORS)
          ? (Boolean)options.get(TargletContainerResource.OPTION_MIRRORS)
          : defaultSaveOptions != null ? (Boolean)defaultSaveOptions.get(TargletContainerResource.OPTION_MIRRORS) : Boolean.TRUE;

      try
      {
        TargletContainer wrapper = (TargletContainer)getContents().get(0);
        targletContainer.setTarglets(wrapper.getTarglets(), wrapper.getComposedTargets());

        if (updateJob != null)
        {
          // Cancel the job and wait for it to stop running.
          updateJob.cancel();

          while (updateJob.getState() == Job.RUNNING)
          {
            try
            {
              Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
              //$FALL-THROUGH$
            }
          }
        }

        updateJob = new Job(NLS.bind(Messages.TargletContainerResourceFactory_Resolve_job, targletContainer.getID()))
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            try
            {
              targletContainer.forceUpdate(false, mirrors, monitor);
            }
            catch (CoreException ex)
            {
              TargletsCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
            }

            return Status.OK_STATUS;
          }
        };

        updateJob.schedule();
      }
      catch (CoreException ex)
      {
        TargletsCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Factory implements org.eclipse.emf.ecore.resource.Resource.Factory
  {
    public Factory()
    {
    }

    @Override
    public Resource createResource(URI uri)
    {
      return new TargletContainerResource(uri);
    }
  }
}

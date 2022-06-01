/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import java.io.IOException;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class TargletContainerResourceFactory implements Factory
{
  public static final String PROTOCOL_NAME = "targlet_container"; //$NON-NLS-1$

  public static final String OPTION_MIRRORS = "MIRRORS"; //$NON-NLS-1$

  public TargletContainerResourceFactory()
  {
  }

  @Override
  public Resource createResource(URI uri)
  {
    final String id = uri.opaquePart();
    final ITargletContainer targletContainer = TargletContainerDescriptorManager.getContainer(id);
    final org.eclipse.oomph.targlets.TargletContainer wrapper = TargletFactory.eINSTANCE.createTargletContainer();
    wrapper.setID(id);

    Resource resource = new ResourceImpl(uri)
    {
      private Job updateJob;

      @Override
      public void save(Map<?, ?> options) throws IOException
      {
        if (targletContainer != null)
        {
          final boolean mirrors = options != null && options.containsKey(OPTION_MIRRORS) ? (Boolean)options.get(OPTION_MIRRORS)
              : defaultSaveOptions != null ? (Boolean)defaultSaveOptions.get(OPTION_MIRRORS) : Boolean.TRUE;

          try
          {
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
    };

    if (targletContainer != null)
    {
      wrapper.getTarglets().addAll(targletContainer.getTarglets());
      wrapper.getComposedTargets().addAll(targletContainer.getComposedTargets());
    }

    resource.getContents().add(wrapper);
    return resource;
  }
}

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
package org.eclipse.oomph.setup.pde.util;

import org.eclipse.oomph.internal.setup.pde.PDEPlugin;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public final class TargetPlatformUtil
{
  public static ITargetDefinition getActiveTargetPlatform()
  {
    ITargetPlatformService service = null;

    try
    {
      service = PDEPlugin.getService(ITargetPlatformService.class);
      ITargetHandle workspaceTargetHandle = service.getWorkspaceTargetHandle();
      if (workspaceTargetHandle != null)
      {
        return workspaceTargetHandle.getTargetDefinition();
      }
    }
    catch (CoreException ex)
    {
      // Ignore
    }
    finally
    {
      PDEPlugin.ungetService(service);
    }

    return null;
  }

  public static ITargetDefinition getTargetPlatform(String targetPlatformName, ProgressLog progress)
  {
    ITargetPlatformService service = null;

    try
    {
      service = PDEPlugin.getService(ITargetPlatformService.class);
      for (ITargetHandle targetHandle : service.getTargets(new ProgressLogMonitor(progress)))
      {
        try
        {
          ITargetDefinition candidate = targetHandle.getTargetDefinition();
          if (targetPlatformName.equals(candidate.getName()))
          {
            return candidate;
          }
        }
        catch (CoreException ex)
        {
          // Ignore
        }
      }
    }
    finally
    {
      PDEPlugin.ungetService(service);
    }

    return null;
  }

  public static boolean hasTargetPlatform(String targetPlatformPath, ProgressLog progress)
  {
    ITargetPlatformService service = null;

    try
    {
      service = PDEPlugin.getService(ITargetPlatformService.class);
      for (ITargetHandle targetHandle : service.getTargets(new ProgressLogMonitor(progress)))
      {
        try
        {
          ITargetDefinition candidate = targetHandle.getTargetDefinition();
          ITargetLocation[] containers = candidate.getTargetLocations();
          if (containers == null || containers.length != 1)
          {
            continue;
          }

          String location = containers[0].getLocation(true);
          if (targetPlatformPath.equals(location))
          {
            File file = new File(location);
            return file.isDirectory() && file.listFiles().length >= 2;
          }
        }
        catch (CoreException ex)
        {
          // Ignore
        }
      }
    }
    finally
    {
      PDEPlugin.ungetService(service);
    }

    return false;
  }

  public static void setTargetPlatform(String targetPlatformPath, String name, boolean activate, ProgressLog progress) throws Exception
  {
    ITargetPlatformService service = null;

    try
    {
      service = PDEPlugin.getService(ITargetPlatformService.class);
      for (ITargetHandle targetHandle : service.getTargets(new ProgressLogMonitor(progress)))
      {
        ITargetDefinition candidate = targetHandle.getTargetDefinition();
        ITargetLocation[] containers = candidate.getTargetLocations();
        if (containers == null || containers.length != 1)
        {
          continue;
        }

        if (targetPlatformPath.equals(containers[0].getLocation(true)))
        {
          service.deleteTarget(targetHandle);
          break;
        }
      }

      ITargetDefinition target = service.newTarget();
      target.setName(name);
      File tpDir = new File(targetPlatformPath);
      if (!tpDir.isDirectory())
      {
        tpDir.mkdirs();
        if (!tpDir.isDirectory())
        {
          throw new IOException("Unable to create target platform directory: " + tpDir);
        }
      }

      ITargetLocation container = service.newDirectoryLocation(targetPlatformPath);
      target.setTargetLocations(new ITargetLocation[] { container });
      service.saveTargetDefinition(target);
      target.resolve(null);

      if (activate)
      {
        setTargetActive(target, new ProgressLogMonitor(progress));
      }
    }
    finally
    {
      PDEPlugin.ungetService(service);
    }
  }

  public static void setTargetActive(ITargetDefinition target, IProgressMonitor monitor) throws Exception
  {
    monitor.beginTask("Set active target platform", 100);

    try
    {
      target.resolve(new SubProgressMonitor(monitor, 50));

      LoadTargetDefinitionJob job = new LoadTargetDefinitionJob(target);
      IStatus status = job.run(new SubProgressMonitor(monitor, 50));
      if (status.getSeverity() == IStatus.ERROR)
      {
        throw new CoreException(status);
      }
    }
    finally
    {
      monitor.done();
    }
  }
}

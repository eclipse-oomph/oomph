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
package org.eclipse.oomph.util.pde;

import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.internal.pde.UtilPDEPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Eike Stepper
 */
public final class TargetPlatformUtil
{
  private static final List<TargetPlatformListener> LISTENERS = new CopyOnWriteArrayList<TargetPlatformListener>();

  private static final Method GET_WORKSPACE_TARGET_DEFINITION_METHOD;

  static
  {
    Method method = null;

    try
    {
      method = ITargetPlatformService.class.getMethod("getWorkspaceTargetDefinition");
    }
    catch (NoSuchMethodException ex)
    {
      // Ignore.
    }
    catch (SecurityException ex)
    {
      // Ignore.
    }

    GET_WORKSPACE_TARGET_DEFINITION_METHOD = method;

    Job.getJobManager().addJobChangeListener(new JobChangeAdapter()
    {
      private ITargetDefinition oldTargetDefinition;

      @Override
      public void aboutToRun(IJobChangeEvent event)
      {
        oldTargetDefinition = null;
        if (event.getJob() instanceof LoadTargetDefinitionJob)
        {
          oldTargetDefinition = getCurrentTargetDefinition();
        }
      }

      @Override
      public void done(IJobChangeEvent event)
      {
        try
        {
          Job job = event.getJob();
          if (job instanceof LoadTargetDefinitionJob)
          {
            ITargetDefinition newTargetDefinition = getCurrentTargetDefinition();
            notifyListeners(oldTargetDefinition, newTargetDefinition);
          }
        }
        finally
        {
          oldTargetDefinition = null;
        }
      }

      private ITargetDefinition getCurrentTargetDefinition()
      {
        try
        {
          return getActiveTargetDefinition();
        }
        catch (Exception ex)
        {
          UtilPDEPlugin.INSTANCE.log(ex);
          return null;
        }
      }
    });
  }

  private TargetPlatformUtil()
  {
  }

  public static <T> T runWithTargetPlatformService(TargetPlatformRunnable<T> runnable) throws CoreException
  {
    ITargetPlatformService service = null;

    try
    {
      service = UtilPDEPlugin.INSTANCE.getService(ITargetPlatformService.class);
      return runnable.run(service);
    }
    finally
    {
      UtilPDEPlugin.INSTANCE.ungetService(service);
    }
  }

  public static void resolveTargetDefinition(ITargetDefinition targetDefinition, IProgressMonitor monitor) throws CoreException
  {
    monitor.beginTask("Resolving target platform", 100);

    try
    {
      IStatus status = targetDefinition.resolve(MonitorUtil.create(monitor, 100));
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

  public static void activateTargetDefinition(ITargetDefinition targetDefinition, IProgressMonitor monitor) throws CoreException
  {
    ITargetDefinition oldTargetDefinition = getActiveTargetDefinition();
    monitor.beginTask("Set active target platform", 100);

    try
    {
      targetDefinition.resolve(MonitorUtil.create(monitor, 50));

      LoadTargetDefinitionJob job = new LoadTargetDefinitionJob(targetDefinition);

      @SuppressWarnings("restriction")
      IStatus status = job.run(MonitorUtil.create(monitor, 50));
      if (status.getSeverity() == IStatus.ERROR)
      {
        throw new CoreException(status);
      }
    }
    finally
    {
      monitor.done();
    }

    notifyListeners(oldTargetDefinition, targetDefinition);
  }

  public static boolean isActiveTargetDefinition(final ITargetDefinition targetDefinition)
  {
    try
    {
      return runWithTargetPlatformService(new TargetPlatformRunnable<Boolean>()
      {
        public Boolean run(ITargetPlatformService service) throws CoreException
        {
          return targetDefinition.getHandle().equals(service.getWorkspaceTargetHandle());
        }
      });
    }
    catch (CoreException ex)
    {
      UtilPDEPlugin.INSTANCE.log(ex);
    }

    return false;
  }

  public static ITargetDefinition getActiveTargetDefinition()
  {
    try
    {
      return runWithTargetPlatformService(new TargetPlatformRunnable<ITargetDefinition>()
      {
        public ITargetDefinition run(ITargetPlatformService service) throws CoreException
        {
          return getActiveTargetDefinition(service);
        }
      });
    }
    catch (CoreException ex)
    {
      UtilPDEPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  public static ITargetDefinition getActiveTargetDefinition(ITargetPlatformService service)
  {
    if (GET_WORKSPACE_TARGET_DEFINITION_METHOD != null)
    {
      try
      {
        return (ITargetDefinition)GET_WORKSPACE_TARGET_DEFINITION_METHOD.invoke(service);
      }
      catch (IllegalAccessException ex)
      {
        // Ignore.
      }
      catch (IllegalArgumentException ex)
      {
        // Ignore.
      }
      catch (InvocationTargetException ex)
      {
        // Ignore.
      }
    }

    try
    {
      // Handle gracefully that getWorkspaceTargetDefinition() has been added in Eclipse 4.4
      ITargetHandle handle = service.getWorkspaceTargetHandle();
      if (handle != null)
      {
        return handle.getTargetDefinition();
      }
    }
    catch (CoreException ex)
    {
      UtilPDEPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  public static ITargetDefinition getTargetDefinition(final String targetDefinitionName)
  {
    try
    {
      return runWithTargetPlatformService(new TargetPlatformRunnable<ITargetDefinition>()
      {
        public ITargetDefinition run(ITargetPlatformService service) throws CoreException
        {
          for (ITargetHandle targetHandle : service.getTargets(new NullProgressMonitor()))
          {
            try
            {
              ITargetDefinition candidate = targetHandle.getTargetDefinition();
              if (targetDefinitionName.equals(candidate.getName()))
              {
                return candidate;
              }
            }
            catch (CoreException ex)
            {
              // Ignore
            }
          }

          return null;
        }
      });
    }
    catch (CoreException ex)
    {
      UtilPDEPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  public static ITargetDefinition[] getTargetDefinitions(final IProgressMonitor monitor)
  {
    try
    {
      return runWithTargetPlatformService(new TargetPlatformRunnable<ITargetDefinition[]>()
      {
        public ITargetDefinition[] run(ITargetPlatformService service) throws CoreException
        {
          List<ITargetDefinition> targetDefinitions = new ArrayList<ITargetDefinition>();

          for (ITargetHandle targetHandle : service.getTargets(monitor))
          {
            try
            {
              if (targetHandle.exists())
              {
                ITargetDefinition targetDefinition = targetHandle.getTargetDefinition();
                targetDefinitions.add(targetDefinition);
              }
            }
            catch (CoreException ex)
            {
              // Ignore
            }
          }

          return targetDefinitions.toArray(new ITargetDefinition[targetDefinitions.size()]);
        }
      });
    }
    catch (CoreException ex)
    {
      UtilPDEPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  public static void addListener(TargetPlatformListener listener)
  {
    LISTENERS.add(listener);
  }

  public static void removeListener(TargetPlatformListener listener)
  {
    LISTENERS.remove(listener);
  }

  private static void notifyListeners(ITargetDefinition oldTargetDefinition, ITargetDefinition newTargetDefinition)
  {
    for (TargetPlatformListener listener : LISTENERS)
    {
      try
      {
        listener.targetDefinitionActivated(oldTargetDefinition, newTargetDefinition);
      }
      catch (Exception ex)
      {
        UtilPDEPlugin.INSTANCE.log(ex);
      }
    }
  }
}

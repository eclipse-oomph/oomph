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
package org.eclipse.oomph.setup.presentation.actions;

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTask.MirrorContext;
import org.eclipse.oomph.setup.SetupTask.MirrorRunnable;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class MirrorBranchAction extends AbstractSetupAction
{
  public MirrorBranchAction()
  {
  }

  public void run(IAction action)
  {
    // try
    // {
    // MirrorPerformer performer = new MirrorPerformer();
    //
    // Job job = new MirrorJob(performer);
    // job.schedule();
    // }
    // catch (UpdatingException ex)
    // {
    // PlatformUI.getWorkbench().restart();
    // }
    // catch (Throwable ex)
    // {
    // SetupEditorPlugin.INSTANCE.log(ex);
    // ErrorDialog.open(ex);
    // }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class MirrorPerformer extends SetupTaskPerformer implements MirrorContext
  {
    public MirrorPerformer(Trigger trigger, Workspace workspace)
    {
      super(workspace.eResource().getResourceSet().getURIConverter(), null, trigger, SetupContext.create(null, workspace, null), (Stream)null);
    }

    public void addRedirection(String sourceURL, String targetURL)
    {
      System.out.println("Redirection: " + sourceURL + " --> " + targetURL);

      // Setup setup = getOriginalSetup();
      // CompoundTask container = getContainer(setup);
      // RedirectionTask redirection = getRedirection(container, sourceURL);
      // redirection.setTargetURL(targetURL);
      //
      // Preferences preferences = setup.getPreferences();
      // BaseUtil.saveEObject(preferences);
    }

    @SuppressWarnings("unused")
    private RedirectionTask getRedirection(CompoundTask container, String sourceURL)
    {
      EList<SetupTask> tasks = container.getSetupTasks();
      for (SetupTask task : tasks)
      {
        if (task instanceof RedirectionTask)
        {
          RedirectionTask redirection = (RedirectionTask)task;
          if (sourceURL.equals(redirection.getSourceURL()))
          {
            return redirection;
          }
        }
      }

      RedirectionTask redirection = SetupFactory.eINSTANCE.createRedirectionTask();
      redirection.setSourceURL(sourceURL);
      tasks.add(redirection);
      return redirection;
    }

    @SuppressWarnings("unused")
    private CompoundTask getContainer(Workspace workspace)
    {
      String name = "Mirror Configuration ("; // + get(SetupConstants.KEY_BRANCH_LABEL) + ")";

      // EList<SetupTask> tasks = setup.getPreferences().getSetupTasks();
      // for (Branch branch : setup.getBranches())
      // {
      //
      // for (SetupTask task : tasks)
      // {
      // if (task instanceof CompoundTask)
      // {
      // CompoundTask container = (CompoundTask)task;
      // if (name.equals(container.getName()) && container.getRestrictions().contains(branch))
      // {
      // return container;
      // }
      // }
      // }
      //
      // CompoundTask container = SetupFactory.eINSTANCE.createCompoundTask(name);
      // container.getRestrictions().add(branch);
      //
      // tasks.add(container);
      // return container;
      // }

      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  @SuppressWarnings("unused")
  private static final class MirrorJob extends Job
  {
    private MirrorPerformer performer;

    private MirrorJob(MirrorPerformer performer)
    {
      super("Mirroring");
      this.performer = performer;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
      try
      {
        File mirrorsDir = new File(PropertiesUtil.USER_HOME, ".mirrors");
        mirrorsDir.mkdirs();

        List<MirrorRunnable> runnables = getMirrorRunnables(performer, mirrorsDir, monitor);
        monitor.beginTask("Mirroring", runnables.size());

        for (MirrorRunnable runnable : runnables)
        {
          checkCancelation(monitor);
          run(monitor, performer, runnable);
        }
      }
      catch (OperationCanceledException ex)
      {
        return Status.CANCEL_STATUS;
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.INSTANCE.log(ex);
      }
      finally
      {
        monitor.done();
      }

      return Status.OK_STATUS;
    }

    private void run(IProgressMonitor monitor, MirrorPerformer performer, MirrorRunnable runnable) throws Exception
    {
      runnable.run(new SubProgressMonitor(monitor, 1));
    }

    private List<MirrorRunnable> getMirrorRunnables(MirrorPerformer performer, File mirrorsDir, IProgressMonitor monitor) throws Exception
    {
      List<MirrorRunnable> runnables = new ArrayList<MirrorRunnable>();
      for (SetupTask task : performer.getTriggeredSetupTasks())
      {
        checkCancelation(monitor);

        MirrorRunnable runnable = task.mirror(performer, mirrorsDir, true);
        if (runnable != null)
        {
          runnables.add(runnable);
        }
      }

      return runnables;
    }

    private void checkCancelation(IProgressMonitor monitor)
    {
      if (monitor.isCanceled())
      {
        throw new OperationCanceledException();
      }
    }
  }
}

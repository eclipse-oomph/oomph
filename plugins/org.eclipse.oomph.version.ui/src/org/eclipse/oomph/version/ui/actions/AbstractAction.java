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
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Eike Stepper
 */
public abstract class AbstractAction<ARGS> implements IObjectActionDelegate
{
  protected Shell shell;

  protected ISelection selection;

  private String jobName;

  public AbstractAction(String jobName)
  {
    this.jobName = jobName;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    shell = targetPart.getSite().getShell();
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }

  public void run(IAction action)
  {
    try
    {
      final ARGS arguments = promptArguments();
      if (arguments != null)
      {
        new Job(jobName)
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            try
            {
              ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
              {
                public void run(IProgressMonitor monitor) throws CoreException
                {
                  runWithArguments(arguments);
                }
              }, monitor);

              return Status.OK_STATUS;
            }
            catch (CoreException ex)
            {
              return ex.getStatus();
            }
          }
        }.schedule();
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      MessageDialog.openError(shell, "Version Management", "An error occured. Consult the error log for details.");
    }
  }

  protected abstract ARGS promptArguments();

  protected abstract void runWithArguments(ARGS arguments) throws CoreException;
}

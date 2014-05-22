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

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Eike Stepper
 */
public class PushDirectAction implements IObjectActionDelegate
{
  private Repository repository;

  public PushDirectAction()
  {
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    repository = null;
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)selection;
      Object element = ssel.getFirstElement();
      repository = getAdapter(element, Repository.class);
    }
  }

  public void run(IAction action)
  {
    if (repository != null)
    {
      new Job("Pushing directly")
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          monitor.beginTask(getName(), 101);

          try
          {
            Git git = Git.wrap(repository);
            monitor.worked(1);

            git.push().setRemote("direct").setProgressMonitor(new EclipseGitProgressTransformer(new SubProgressMonitor(monitor, 50))).call();

            monitor.setTaskName("Pulling");
            git.pull().setProgressMonitor(new EclipseGitProgressTransformer(new SubProgressMonitor(monitor, 50))).call();

            return Status.OK_STATUS;
          }
          catch (Exception ex)
          {
            return SetupEditorPlugin.getStatus(ex);
          }
          finally
          {
            monitor.done();
          }
        }
      }.schedule();
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T getAdapter(Object adaptable, Class<T> c)
  {
    if (c.isInstance(adaptable))
    {
      return (T)adaptable;
    }

    if (adaptable instanceof IAdaptable)
    {
      IAdaptable a = (IAdaptable)adaptable;
      Object adapter = a.getAdapter(c);
      if (c.isInstance(adapter))
      {
        return (T)adapter;
      }
    }

    return null;
  }
}

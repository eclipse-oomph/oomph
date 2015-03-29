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
package org.eclipse.oomph.manifests.handlers;

import org.eclipse.oomph.manifests.Activator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.BuildAction;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class CleanBuildHandler extends AbstractHandler
{
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    if (window != null)
    {
      final IStructuredSelection selection = getSelection(event);
      if (selection != null && !selection.isEmpty())
      {
        try
        {
          ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
          {
            public void run(IProgressMonitor monitor) throws CoreException
            {
              Object[] array = selection.toArray();
              for (int i = 0; i < array.length; i++)
              {
                IProject project = (IProject)array[i];

                try
                {
                  IMarker[] markers = project.findMarkers(null, true, IResource.DEPTH_INFINITE);
                  for (IMarker marker : markers)
                  {
                    try
                    {
                      marker.delete();
                    }
                    catch (CoreException ex)
                    {
                      log(ex);
                    }
                  }
                }
                catch (CoreException ex)
                {
                  log(ex);
                }
              }
            }
          }, null, IWorkspace.AVOID_UPDATE, null);
        }
        catch (CoreException ex)
        {
          log(ex);
        }

        BuildAction buildAction = new BuildAction(window, IncrementalProjectBuilder.CLEAN_BUILD);
        buildAction.selectionChanged(selection);
        buildAction.run();
      }
    }

    return null;
  }

  private IStructuredSelection getSelection(ExecutionEvent event)
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IStructuredSelection)
    {
      Set<IProject> projects = new HashSet<IProject>();
      for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
      {
        Object element = it.next();
        if (element instanceof IAdaptable)
        {
          IAdaptable adaptable = (IAdaptable)element;
          element = adaptable.getAdapter(IResource.class);

          if (element == null)
          {
            IMarker marker = adaptable.getAdapter(IMarker.class);
            if (marker != null)
            {
              element = marker.getResource();
            }
          }
        }

        if (element instanceof IProject)
        {
          projects.add((IProject)element);
        }
        else if (element instanceof IResource)
        {
          IResource resource = (IResource)element;
          IProject project = resource.getProject();
          if (project != null)
          {
            projects.add(project);
          }
        }
      }

      if (projects.isEmpty())
      {
        return null;
      }

      return new StructuredSelection(projects.toArray());
    }

    IEditorInput activeEditorInput = HandlerUtil.getActiveEditorInput(event);
    if (activeEditorInput instanceof FileEditorInput)
    {
      IProject project = ((FileEditorInput)activeEditorInput).getFile().getProject();
      return new StructuredSelection(project);
    }

    return null;
  }

  private void log(CoreException ex)
  {
    Activator.getDefault().getLog().log(ex.getStatus());
  }
}

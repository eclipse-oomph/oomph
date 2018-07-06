/**
 * Copyright (c) 2014, 2015, 2017 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 */
package org.eclipse.oomph.preferences.presentation.actions;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.presentation.PreferencesEditorPlugin;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowInExplorerAction extends ActionDelegate
{
  private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

  protected ISelection targetSelection;

  public ShowInExplorerAction()
  {
    super();
  }

  @Override
  public void run(IAction action)
  {
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IWorkbenchPage page = workbenchWindow.getActivePage();
    try
    {
      IViewPart packageExplorerView = page.showView("org.eclipse.jdt.ui.PackageExplorer");
      IShowInTarget showInTarget = ObjectUtil.adapt(packageExplorerView, IShowInTarget.class);
      showInTarget.show(new ShowInContext(null, targetSelection));
    }
    catch (PartInitException ex)
    {
      PreferencesEditorPlugin.INSTANCE.log(ex);
    }
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    if (selection instanceof IStructuredSelection)
    {
      List<IResource> resources = new ArrayList<IResource>();
      for (Object object : ((IStructuredSelection)selection).toArray())
      {
        if (object instanceof PreferenceNode)
        {
          PreferenceNode preferenceNode = (PreferenceNode)object;
          String path = preferenceNode.getLocation();

          if (path != null)
          {
            File file = new File(path);
            URI uri = file.toURI();

            IFile[] files = WORKSPACE_ROOT.findFilesForLocationURI(uri);
            if (files.length > 0)
            {
              resources.addAll(Arrays.asList(files));
            }
          }
          else
          {
            PreferenceNode parent = preferenceNode.getParent();
            if (parent != null && "project".equals(parent.getName()))
            {
              PreferenceNode grandParent = parent.getParent();
              if (grandParent != null && "".equals(grandParent.getName()))
              {
                IProject project = WORKSPACE_ROOT.getProject(preferenceNode.getName());
                if (project.isAccessible())
                {
                  resources.add(project);
                }
              }
            }
          }
        }
      }
      if (!resources.isEmpty())
      {
        targetSelection = new StructuredSelection(resources);
        action.setEnabled(true);
        return;
      }
    }

    targetSelection = null;
    action.setEnabled(false);
  }
}

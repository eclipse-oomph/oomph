/**
 * Copyright (c) 2014 IBM Corporation and others.
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

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.ide.IDE;

import java.io.File;
import java.net.URI;

public class OpenEditorAction extends ActionDelegate
{
  protected String path;

  public OpenEditorAction()
  {
    super();
  }

  @Override
  public void run(IAction action)
  {
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IWorkbenchPage page = workbenchWindow.getActivePage();

    // Open an editor on the new file.
    //
    try
    {
      File file = new File(path);
      URI uri = file.toURI();

      IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(uri);
      if (files.length > 0)
      {
        IDE.openEditor(page, files[0]);
      }
      else
      {
        IFileStore fileStore = EFS.getStore(uri);
        IDE.openEditorOnFileStore(page, fileStore);
      }
    }
    catch (Exception exception)
    {
      MessageDialog.openError(workbenchWindow.getShell(), Messages.OpenEditorAction_OpenEditor_title, exception.getMessage());
    }
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    if (selection instanceof IStructuredSelection)
    {
      Object object = ((IStructuredSelection)selection).getFirstElement();
      if (object instanceof PreferenceNode)
      {
        path = ((PreferenceNode)object).getLocation();

        action.setEnabled(path != null);

        return;
      }
    }
    path = null;
    action.setEnabled(false);
  }
}

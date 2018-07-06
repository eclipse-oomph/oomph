/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.manifests;

import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public abstract class AbstractProjectHandler extends AbstractHandler
{
  public AbstractProjectHandler()
  {
  }

  public final Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    IWorkbenchPage page = window.getActivePage();
    if (page == null)
    {
      return null;
    }

    IProject project = getProject(page);
    if (project != null)
    {
      if (executeWithProject(page, project))
      {
        return null;
      }
    }

    try
    {
      Object element = getSelectedElement(page);
      executeWithElement(page, element);
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }

  protected abstract boolean executeWithProject(IWorkbenchPage page, IProject project);

  protected void executeWithElement(IWorkbenchPage page, Object element) throws Exception
  {
  }

  private static Object getSelectedElement(IWorkbenchPage page)
  {
    ISelection selection = page.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)selection;
      if (ssel.size() == 1)
      {
        return ssel.getFirstElement();
      }
    }

    return null;
  }

  private static IProject getProject(IWorkbenchPage page)
  {
    Object element = getSelectedElement(page);

    IResource resource = ObjectUtil.adapt(element, IResource.class);
    if (resource == null)
    {
      IEditorPart editor = page.getActiveEditor();
      if (editor != null)
      {
        IEditorInput input = editor.getEditorInput();
        if (input instanceof IFileEditorInput)
        {
          resource = ((IFileEditorInput)input).getFile();
        }
      }
    }

    if (resource == null)
    {
      return null;
    }

    return resource.getProject();
  }
}

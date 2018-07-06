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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.dialogs.PropertyDialogAction;

/**
 * @author Eike Stepper
 */
public class OpenPropertiesHandler extends AbstractProjectHandler
{
  public OpenPropertiesHandler()
  {
  }

  @Override
  protected boolean executeWithProject(IWorkbenchPage page, final IProject project)
  {
    PropertyDialogAction action = new PropertyDialogAction(page.getWorkbenchWindow(), new ISelectionProvider()
    {
      public ISelection getSelection()
      {
        return new StructuredSelection(project);
      }

      public void setSelection(ISelection selection)
      {
      }

      public void addSelectionChangedListener(ISelectionChangedListener listener)
      {
      }

      public void removeSelectionChangedListener(ISelectionChangedListener listener)
      {
      }
    });

    try
    {
      action.runWithEvent(null);
    }
    finally
    {
      action.dispose();
    }

    return true;
  }
}

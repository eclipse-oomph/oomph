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

import org.eclipse.oomph.setup.internal.core.SetupContext;

import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSetupAction implements IWorkbenchWindowActionDelegate
{
  private IWorkbenchWindow window;

  private boolean initialized;

  public AbstractSetupAction()
  {
  }

  public void init(IWorkbenchWindow window)
  {
    this.window = window;
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    if (!initialized)
    {
      boolean exists = URIConverter.INSTANCE.exists(SetupContext.WORKSPACE_SETUP_URI, null);
      action.setEnabled(exists);
      initialized = true;
    }
  }

  public void dispose()
  {
  }

  public IWorkbenchWindow getWindow()
  {
    return window;
  }
}

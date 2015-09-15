/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author Eike Stepper
 */
public class SynchronizerAction implements IWorkbenchWindowActionDelegate
{
  private IWorkbenchWindow window;

  public SynchronizerAction()
  {
  }

  public void init(IWorkbenchWindow window)
  {
    this.window = window;
  }

  public void dispose()
  {
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
  }

  public void run(IAction action)
  {
    new SynchronizerDialog(window.getShell()).open();
  }
}

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
package org.eclipse.oomph.internal.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public class ToggleOfflineModeHandler extends AbstractHandler
{
  public ToggleOfflineModeHandler()
  {
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    HandlerUtil.toggleCommandState(event.getCommand());

    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    // MessageDialog.openInformation(
    // window.getShell(),
    // "Oomph Common UI",
    // "Hello, Eclipse world");

    return null;
  }
}

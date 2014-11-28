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
package org.eclipse.oomph.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public class ToggleCommandHandler extends AbstractHandler
{
  public ToggleCommandHandler()
  {
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    HandlerUtil.toggleCommandState(event.getCommand());
    return null;
  }

  public static boolean getToggleState(String commandID)
  {
    State commandState = getCommandState(commandID);
    return ((Boolean)commandState.getValue()).booleanValue();
  }

  public static void setToggleState(String commandID, boolean toggleState)
  {
    State commandState = getCommandState(commandID);
    commandState.setValue(toggleState);
  }

  private static State getCommandState(String commandID)
  {
    ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
    Command command = commandService.getCommand(commandID);
    return command.getState("org.eclipse.ui.commands.toggleState");
  }
}

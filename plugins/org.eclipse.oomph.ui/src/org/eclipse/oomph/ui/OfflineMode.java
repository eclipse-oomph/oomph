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

/**
 * @author Eike Stepper
 */
public final class OfflineMode
{
  private static final String COMMAND_ID = "org.eclipse.oomph.ui.ToggleOfflineMode";

  private OfflineMode()
  {
  }

  public static boolean isEnabled()
  {
    return ToggleCommandHandler.getToggleState(COMMAND_ID);
  }

  public static void setEnabled(boolean enabled)
  {
    ToggleCommandHandler.setToggleState(COMMAND_ID, enabled);
  }
}

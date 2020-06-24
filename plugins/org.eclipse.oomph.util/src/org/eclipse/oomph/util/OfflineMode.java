/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;
import org.eclipse.oomph.internal.util.UtilPlugin.ToggleStateAccessor;

import org.eclipse.emf.common.CommonPlugin;

/**
 * @author Eike Stepper
 */
public class OfflineMode
{
  private static final String COMMAND_ID = "org.eclipse.oomph.ui.ToggleOfflineMode"; //$NON-NLS-1$

  private static boolean initializedToggleStateAccessor;

  public OfflineMode()
  {
  }

  public static boolean isEnabled()
  {
    try
    {
      ToggleStateAccessor toggleStateAccessor = UtilPlugin.getToggleStateAccessor();
      return toggleStateAccessor.isEnabled(COMMAND_ID);
    }
    catch (Exception ex)
    {
      return UtilPlugin.DEFAULT_TOGGLE_STATE_ACCESSOR.isEnabled(COMMAND_ID);
    }
  }

  public static void setEnabled(boolean enabled)
  {
    if (!initializedToggleStateAccessor)
    {
      injectUILevelAccessor();
      initializedToggleStateAccessor = true;
    }

    try
    {
      ToggleStateAccessor toggleStateAccessor = UtilPlugin.getToggleStateAccessor();
      toggleStateAccessor.setEnabled(COMMAND_ID, enabled);
    }
    catch (Exception ex)
    {
      UtilPlugin.DEFAULT_TOGGLE_STATE_ACCESSOR.setEnabled(COMMAND_ID, enabled);
    }
  }

  private static void injectUILevelAccessor()
  {
    try
    {
      CommonPlugin.loadClass("org.eclipse.oomph.ui", "org.eclipse.oomph.internal.ui.UIPlugin"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    catch (Throwable t)
    {
      // Ignore.
    }
  }
}

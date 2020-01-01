/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public interface InstallerUI
{
  public static final int RETURN_OK = Window.OK;

  public static final int RETURN_SIMPLE = Window.CANCEL + 1;

  public static final int RETURN_ADVANCED = Window.CANCEL + 2;

  public static final int RETURN_RESTART = -4;

  /**
   * Adds the p2 bundle pool buttons to the UI if the bundle pool location isn't specified or isn't specified to be '@none'.
   */
  public static final boolean SHOW_BUNDLE_POOL_UI = PropertiesUtil.getProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION) == null
      || !AgentManager.BUNDLE_POOL_LOCATION_NONE.equalsIgnoreCase(PropertiesUtil.getProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION));

  public int show();

  public void showAbout();

  public Shell getShell();

  public boolean refreshJREs();
}

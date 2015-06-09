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
package org.eclipse.oomph.setup.internal.installer;

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

  public int show();

  public void showAbout();

  public Shell getShell();

  public boolean refreshJREs();
}

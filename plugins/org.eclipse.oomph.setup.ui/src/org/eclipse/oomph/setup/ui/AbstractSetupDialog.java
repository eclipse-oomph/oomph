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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.ui.AbstractDialog;
import org.eclipse.oomph.ui.AbstractOomphUIPlugin;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSetupDialog extends AbstractDialog
{
  public static final String SHELL_TEXT = "Oomph Installer";

  public AbstractSetupDialog(Shell parentShell, String title, int width, int height, AbstractOomphUIPlugin plugin, String help)
  {
    super(parentShell, title, width, height, plugin, help);
  }

  @Override
  protected String getImagePath()
  {
    return "install_wiz.png";
  }

  @Override
  protected String getShellText()
  {
    return SHELL_TEXT;
  }
}

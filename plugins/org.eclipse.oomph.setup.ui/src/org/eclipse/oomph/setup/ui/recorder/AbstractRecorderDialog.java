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
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractRecorderDialog extends AbstractSetupDialog
{
  private boolean enableRecorder = RecorderManager.INSTANCE.isRecorderEnabled();

  public AbstractRecorderDialog(Shell parentShell, String title, int width, int height)
  {
    super(parentShell, title, width, height, SetupUIPlugin.INSTANCE, true);
  }

  public boolean isEnableRecorder()
  {
    return enableRecorder;
  }

  public void setEnableRecorder(boolean enableRecorder)
  {
    this.enableRecorder = enableRecorder;
  }
}

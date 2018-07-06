/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.Impact;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class SynchronizePreferencesHandler extends AbstractDropdownItemHandler
{
  public SynchronizePreferencesHandler()
  {
    super("sync/Remote", "Synchronize Preferences...");
  }

  public void run()
  {
    Shell shell = SetupPropertyTester.getHandlingShell();
    if (shell != null)
    {
      shell.setVisible(true);
    }
    else
    {
      if (SynchronizerManager.Availability.AVAILABLE)
      {
        Impact impact = SynchronizerManager.INSTANCE.performFullSynchronization();
        if (impact != null && impact.hasLocalImpact())
        {
          SetupWizard.Updater.perform(false);
        }
      }
    }
  }
}

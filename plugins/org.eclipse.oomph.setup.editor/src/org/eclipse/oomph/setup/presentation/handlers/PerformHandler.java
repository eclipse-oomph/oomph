/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class PerformHandler extends AbstractDropdownItemHandler
{
  protected boolean manual;

  public PerformHandler()
  {
    super("update.png", Messages.PerformHandler_text); //$NON-NLS-1$
    manual = true;
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
        Synchronization synchronization = SynchronizerManager.INSTANCE.synchronize(true, false, false);
        if (synchronization != null)
        {
          SynchronizerManager.INSTANCE.performSynchronization(synchronization, false, false);
        }
      }

      SetupWizard.Updater.perform(manual);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class StartupPerformHandler extends PerformHandler
  {
    public StartupPerformHandler()
    {
      manual = false;
    }
  }
}

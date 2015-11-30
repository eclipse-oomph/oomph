/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;

/**
 * @author Eike Stepper
 */
public class PerformHandler extends AbstractDropdownItemHandler
{
  protected boolean manual;

  public PerformHandler()
  {
    super("update", "Perform Setup Tasks...");
    manual = true;
  }

  public void run()
  {
    Synchronization synchronization = SynchronizerManager.INSTANCE.synchronize(true, false, false);
    if (synchronization != null)
    {
      SynchronizerManager.INSTANCE.performSynchronization(synchronization, false, false);
    }

    SetupWizard.Updater.perform(manual);
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

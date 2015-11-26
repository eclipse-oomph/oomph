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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;

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
    SynchronizerManager.INSTANCE.performFullSynchronization();
  }
}

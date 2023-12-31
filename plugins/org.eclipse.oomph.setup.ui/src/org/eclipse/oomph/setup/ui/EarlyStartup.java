/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.ui.IStartup;

/**
 * @author Eike Stepper
 */
public final class EarlyStartup implements IStartup
{
  @Override
  public void earlyStartup()
  {
    SetupUIPlugin.performStartup();
  }
}

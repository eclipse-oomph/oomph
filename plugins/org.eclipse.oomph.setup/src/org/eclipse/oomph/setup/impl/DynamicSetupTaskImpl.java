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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.SetupTaskContext;

/**
 * @author Eike Stepper
 */
public final class DynamicSetupTaskImpl extends SetupTaskImpl
{
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    // Do nothing.
  }
}

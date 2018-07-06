/*
 * Copyright (c) 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.ui.IStartup;

/**
 * @author Eike Stepper
 */
public class EarlyStartup implements IStartup
{
  private static final boolean MANIFEST_DISCOVERY = !PropertiesUtil.isProperty("org.eclipse.oomph.targlets.ui.SKIP_MANIFEST_DISCOVERY");

  public void earlyStartup()
  {
    if (MANIFEST_DISCOVERY)
    {
      ManifestDiscovery.INSTANCE.start();
    }
  }
}

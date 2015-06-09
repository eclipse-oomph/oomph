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
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.oomph.ui.OomphUIPlugin;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

/**
 * @author Eike Stepper
 */
public final class JREInfoUIPlugin extends OomphUIPlugin
{
  public static final JREInfoUIPlugin INSTANCE = new JREInfoUIPlugin();

  private static Implementation plugin;

  public JREInfoUIPlugin()
  {
    super(new ResourceLocator[] { org.eclipse.oomph.internal.ui.UIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }
  }
}

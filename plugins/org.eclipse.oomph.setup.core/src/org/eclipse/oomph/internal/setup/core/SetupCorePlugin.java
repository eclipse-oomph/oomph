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
package org.eclipse.oomph.internal.setup.core;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.util.AbstractOomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.ResourceLocator;

/**
 * @author Eike Stepper
 */
public final class SetupCorePlugin extends AbstractOomphPlugin
{
  private static final String DEFAULT_UPDATE_URL = "http://download.eclipse.org/oomph/updates";

  public static final String UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_UPDATE_URL, DEFAULT_UPDATE_URL).replace('\\', '/');

  public static final SetupCorePlugin INSTANCE = new SetupCorePlugin();

  private static Implementation plugin;

  public SetupCorePlugin()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipsePlugin
  {
    public Implementation()
    {
      plugin = this;
    }
  }
}

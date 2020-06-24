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
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.provider.BaseEditPlugin;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public final class SetupCorePlugin extends OomphPlugin
{
  private static final String DEFAULT_UPDATE_URL = "http://download.eclipse.org/oomph/updates/milestone/latest"; //$NON-NLS-1$

  public static final String UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_UPDATE_URL, DEFAULT_UPDATE_URL).replace('\\', '/');

  public static final SetupCorePlugin INSTANCE = new SetupCorePlugin();

  private static Implementation plugin;

  public SetupCorePlugin()
  {
    super(new ResourceLocator[] { BaseEditPlugin.INSTANCE });
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

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      StringFilterRegistry.INSTANCE.initContributions();
    }
  }
}

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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public final class TargletsCorePlugin extends OomphPlugin
{
  public static final TargletsCorePlugin INSTANCE = new TargletsCorePlugin();

  private static Implementation plugin;

  public TargletsCorePlugin()
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

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      TargletContainerListenerRegistryImpl.INSTANCE.start();
      WorkspaceIUImporter.INSTANCE.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      WorkspaceIUImporter.INSTANCE.stop();
      TargletContainerListenerRegistryImpl.INSTANCE.stop();
      super.stop(context);
    }
  }
}

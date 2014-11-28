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
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.internal.util.UtilPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.ToggleCommandHandler;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public final class UIPlugin extends OomphUIPlugin
{
  public static final UIPlugin INSTANCE = new UIPlugin();

  private static Implementation plugin;

  public UIPlugin()
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
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      UtilPlugin.setToggleStateAccessor(new UtilPlugin.ToggleStateAccessor()
      {
        public boolean isEnabled(String id)
        {
          return ToggleCommandHandler.getToggleState(id);
        }

        public void setEnabled(String id, boolean enabled)
        {
          ToggleCommandHandler.setToggleState(id, enabled);
        }
      });
    }
  }
}

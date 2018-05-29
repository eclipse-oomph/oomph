/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.base.provider.BaseEditPlugin;
import org.eclipse.oomph.internal.util.UtilPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.ToggleCommandHandler;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
public final class UIPlugin extends OomphUIPlugin
{
  public static final UIPlugin INSTANCE = new UIPlugin();

  private static final Method OPEN_RECORDER_METHOD;

  static
  {
    Method openRecorderMethod = null;

    if (PropertiesUtil.isProperty("org.eclipse.swtbot.generator.enable"))
    {
      try
      {
        Class<?> startupRecorderClass = INSTANCE.getClass().getClassLoader().loadClass("org.eclipse.swtbot.generator.ui.StartupRecorder");
        openRecorderMethod = ReflectUtil.getMethod(startupRecorderClass, "openRecorder", String.class);
      }
      catch (Throwable t)
      {
        //$FALL-THROUGH$
      }
    }

    OPEN_RECORDER_METHOD = openRecorderMethod;
  }

  private static Implementation plugin;

  public UIPlugin()
  {
    super(new ResourceLocator[] { BaseEditPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static boolean isRecorderEnabled()
  {
    return OPEN_RECORDER_METHOD != null;
  }

  public static void openRecorderIfEnabled()
  {
    if (isRecorderEnabled())
    {
      try
      {
        OPEN_RECORDER_METHOD.invoke(null, (String)null);
      }
      catch (Throwable t)
      {
        t.printStackTrace();
      }
    }
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

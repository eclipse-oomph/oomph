/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Display;

import java.util.function.Consumer;

/**
 * @author Eike Stepper
 */
public final class P2UIPlugin extends OomphUIPlugin
{
  public static final P2UIPlugin INSTANCE = new P2UIPlugin();

  private static Implementation plugin;

  public P2UIPlugin()
  {
    super(new ResourceLocator[] { org.eclipse.oomph.p2.provider.P2EditPlugin.INSTANCE, org.eclipse.oomph.internal.ui.UIPlugin.INSTANCE });
  }

  public static void init(UIServices serviceUI, Display display)
  {
    ReflectUtil.invokeMethod(ReflectUtil.getMethod(serviceUI.getClass(), "setDisplay", Display.class), serviceUI, display); //$NON-NLS-1$
    Consumer<String> linkHandler = url -> OS.INSTANCE.openSystemBrowser(url);
    ReflectUtil.invokeMethod(ReflectUtil.getMethod(serviceUI.getClass(), "setLinkHandler", Consumer.class), serviceUI, linkHandler); //$NON-NLS-1$
    IShellProvider shellProvider = () -> UIUtil.getShell();
    ReflectUtil.invokeMethod(ReflectUtil.getMethod(serviceUI.getClass(), "setShellProvider", IShellProvider.class), serviceUI, shellProvider); //$NON-NLS-1$
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

/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public class Activator extends AbstractUIPlugin
{
  public static final String CORRECTION_DELETE_GIF = "icons/correction_delete.gif";

  public static final String CORRECTION_CHANGE_GIF = "icons/correction_change.gif";

  public static final String CORRECTION_CONFIGURE_GIF = "icons/correction_configure.gif";

  public static final String PLUGIN_ID = "org.eclipse.oomph.version.ui";

  private static Activator plugin;

  public Activator()
  {
  }

  public static Activator getPlugin()
  {
    return plugin;
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;

    registerImage(CORRECTION_CHANGE_GIF);
    registerImage(CORRECTION_DELETE_GIF);
    registerImage(CORRECTION_CONFIGURE_GIF);
  }

  private void registerImage(String key)
  {
    Image image = imageDescriptorFromPlugin(PLUGIN_ID, key).createImage();
    getImageRegistry().put(key, image);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  public static void log(String message)
  {
    plugin.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
  }

  public static void log(IStatus status)
  {
    plugin.getLog().log(status);
  }

  public static String log(Throwable t)
  {
    IStatus status = getStatus(t);
    log(status);
    return status.getMessage();
  }

  public static IStatus getStatus(Throwable t)
  {
    if (t instanceof CoreException)
    {
      CoreException coreException = (CoreException)t;
      return coreException.getStatus();
    }

    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(IStatus.ERROR, PLUGIN_ID, msg, t);
  }
}

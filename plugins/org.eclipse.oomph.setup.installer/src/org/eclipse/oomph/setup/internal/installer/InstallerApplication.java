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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class InstallerApplication implements IApplication
{
  public Object start(final IApplicationContext context) throws Exception
  {
    boolean restarted = false;
    File restarting = new File(SetupInstallerPlugin.INSTANCE.getStateLocation().toString(), "restarting");

    try
    {
      if (restarting.exists())
      {
        restarted = true;
        if (!restarting.delete())
        {
          restarting.deleteOnExit();
        }
      }
    }
    catch (Exception ex)
    {
      // Ignore
    }

    try
    {
      final Display display = Display.getDefault();
      display.asyncExec(new Runnable()
      {
        public void run()
        {
          // End the splash screen once the dialog is up.
          context.applicationRunning();
        }
      });

      InstallerDialog dialog = new InstallerDialog(null, restarted);
      final int retcode = dialog.open();
      if (retcode == InstallerDialog.RETURN_RESTART)
      {
        try
        {
          restarting.createNewFile();
        }
        catch (Exception ex)
        {
          // Ignore
        }

        return EXIT_RESTART;
      }

      return EXIT_OK;
    }
    catch (Throwable ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
      ErrorDialog.open(ex);
      return 1;
    }
  }

  public void stop()
  {
  }
}

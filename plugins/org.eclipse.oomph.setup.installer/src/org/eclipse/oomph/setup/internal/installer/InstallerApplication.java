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

import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class InstallerApplication implements IApplication
{
  public Object start(final IApplicationContext context) throws Exception
  {
    int[] sizes = { 16, 32, 48, 64, 128, 256 };
    Image[] images = new Image[sizes.length];
    for (int i = 0; i < sizes.length; i++)
    {
      int size = sizes[i];
      images[i] = SetupInstallerPlugin.INSTANCE.getSWTImage("oomph" + size + ".png");
    }

    Window.setDefaultImages(images);

    boolean restarted = false;
    File restarting = new File(SetupContext.CONFIGURATION_STATE_LOCATION_URI.appendSegment("restarting").toFileString());

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
      Display.setAppName("Oomph-Installer");

      // if (Platform.WS_COCOA.equals(Platform.getWS()))
      // {
      // Runnable about = new Runnable()
      // {
      // public void run()
      // {
      // }
      // };
      //
      // Runnable preferences = new Runnable()
      // {
      // public void run()
      // {
      // }
      // };
      //
      // Runnable quit = new Runnable()
      // {
      // public void run()
      // {
      // }
      // };
      //
      // CocoaUtil.register(display, about, preferences, quit);
      // }

      display.asyncExec(new Runnable()
      {
        public void run()
        {
          // End the splash screen once the dialog is up.
          context.applicationRunning();
        }
      });

      SetupUIPlugin.performQuestionnaire(null, false);

      P2Util.getCurrentProvisioningAgent().registerService(UIServices.SERVICE_NAME, SetupWizard.Installer.SERVICE_UI);

      @SuppressWarnings("restriction")
      IProvisioningAgent agent = (IProvisioningAgent)org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper.getService(
          org.eclipse.equinox.internal.p2.repository.Activator.getContext(), IProvisioningAgent.SERVICE_NAME);
      agent.registerService(UIServices.SERVICE_NAME, SetupWizard.Installer.SERVICE_UI);

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

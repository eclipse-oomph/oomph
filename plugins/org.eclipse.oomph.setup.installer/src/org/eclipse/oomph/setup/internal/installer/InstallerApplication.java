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
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Eike Stepper
 */
public class InstallerApplication implements IApplication
{
  public static final Integer EXIT_ERROR = 1;

  private Integer run(final IApplicationContext context) throws Exception
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

    final Display display = Display.getDefault();
    Display.setAppName(AbstractSetupDialog.SHELL_TEXT);

    final InstallerDialog[] installerDialog = { null };
    if (Platform.WS_COCOA.equals(Platform.getWS()))
    {
      Runnable about = new Runnable()
      {
        public void run()
        {
          if (installerDialog[0] != null)
          {
            installerDialog[0].showAbout();
          }
        }
      };

      Runnable preferences = new Runnable()
      {
        public void run()
        {
          if (installerDialog[0] != null)
          {
            ProxyPreferenceDialog proxyPreferenceDialog = new ProxyPreferenceDialog(installerDialog[0].getShell());
            proxyPreferenceDialog.open();
          }
        }
      };

      Runnable quit = new Runnable()
      {
        public void run()
        {
          if (installerDialog[0] != null)
          {
            display.dispose();
          }
        }
      };

      CocoaUtil.register(display, about, preferences, quit);
    }

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

    installerDialog[0] = new InstallerDialog(null, restarted);
    final int retcode = installerDialog[0].open();
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

  public Object start(IApplicationContext context) throws Exception
  {
    try
    {
      return run(context);
    }
    catch (Throwable t)
    {
      SetupInstallerPlugin.INSTANCE.log(t);
      final AtomicInteger exitCode = new AtomicInteger(EXIT_ERROR);

      ErrorDialog dialog = new ErrorDialog("Error", t, 0, 2, IDialogConstants.OK_LABEL, "Update", IDialogConstants.SHOW_DETAILS_LABEL)
      {
        @Override
        protected void buttonPressed(int buttonId)
        {
          super.buttonPressed(buttonId);

          if (buttonId == 1)
          {
            update();
          }
        }

        private void update()
        {
          try
          {
            final Shell shell = getShell();

            if (!MessageDialog.openQuestion(shell, "Emergency Update", "This is an emergency update. Continue?\n\n"
                + "To lower the risk of problems during this update it will be implied that you accept new licenses or unsigned content."))
            {
              return;
            }

            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell)
            {
              @Override
              protected Point getInitialSize()
              {
                Point calculatedSize = super.getInitialSize();
                if (calculatedSize.x < 800)
                {
                  calculatedSize.x = 800;
                }

                return calculatedSize;
              }
            };

            try
            {
              dialog.run(true, true, new IRunnableWithProgress()
              {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                  SubMonitor progress = SubMonitor.convert(monitor, 2);

                  try
                  {
                    Resolution resolution = SelfUpdate.resolve(null, progress.newChild(1));
                    if (resolution == null)
                    {
                      MessageDialog.openInformation(shell, "Update", "No updates were found.");
                      return;
                    }

                    resolution.commit(progress.newChild(1));
                    exitCode.set(EXIT_RESTART);
                  }
                  catch (Throwable ex)
                  {
                    throw new InvocationTargetException(ex);
                  }
                  finally
                  {
                    progress.done();
                  }
                }
              });
            }
            catch (OperationCanceledException ex)
            {
              // Ignore.
            }
            catch (InvocationTargetException ex)
            {
              if (!(ex.getCause() instanceof OperationCanceledException))
              {
                throw ex;
              }
            }
          }
          catch (Throwable ex)
          {
            SetupInstallerPlugin.INSTANCE.log(ex);
            ErrorDialog.open(ex);
          }
        }
      };

      dialog.open();

      return exitCode.get();
    }
  }

  public void stop()
  {
  }
}

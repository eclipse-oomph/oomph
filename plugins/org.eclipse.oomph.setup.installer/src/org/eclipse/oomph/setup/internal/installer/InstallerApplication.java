/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.p2.core.CertificateConfirmer;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.ui.P2ServiceUI;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

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
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.osgi.framework.Bundle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
public class InstallerApplication implements IApplication
{
  public static final Integer EXIT_ERROR = 1;

  private static final Preference PREF_MODE = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("mode"); //$NON-NLS-1$

  private static final boolean SKIP_COCOA_MENU = PropertiesUtil.isProperty("oomph.cocoa.menu.skip"); //$NON-NLS-1$

  private Mode mode = Mode.SIMPLE;

  protected Integer run(final IApplicationContext context) throws Exception
  {
    // This must come very early, before the first model is accessed, so that HTTPS can be authorized.
    IProvisioningAgent agent = P2Util.getCurrentProvisioningAgent();
    final AtomicReference<Installer> currentInstaller = new AtomicReference<Installer>();
    agent.registerService(UIServices.SERVICE_NAME, new P2ServiceUI()
    {
      @Override
      protected UIServices getDelegate()
      {
        Installer installer = currentInstaller.get();
        return installer == null ? null : installer.getUiServices();
      }

      @Override
      protected CertificateConfirmer getCertificateConfirmer()
      {
        return null;
      }

      @Override
      protected Confirmer getUnsignedContentConfirmer()
      {
        return null;
      }
    });

    Location location = Platform.getInstanceLocation();
    if (location != null && !location.isSet())
    {
      Location configurationLocation = Platform.getConfigurationLocation();
      if (configurationLocation != null)
      {
        URL configurationLocationURL = configurationLocation.getURL();
        location.set(configurationLocationURL, false);
      }
    }

    // Register this installer to handle all schemes for which there are no conflicts.
    URISchemeUtil.registerDefault();

    final InstallerUI[] installerDialog = { null };

    Thread jreInitializer = new Thread(Messages.InstallerApplication_JREInitializer_thread)
    {
      @Override
      public void run()
      {
        JREManager.INSTANCE.getJREs();

        for (;;)
        {
          InstallerUI installerUI = installerDialog[0];
          if (installerUI != null)
          {
            installerUI.refreshJREs();
            break;
          }

          try
          {
            sleep(100);
          }
          catch (InterruptedException ex)
          {
            return;
          }
        }
      }
    };

    jreInitializer.setDaemon(true);
    jreInitializer.start();

    String windowImages = context.getBrandingProperty("windowImages"); //$NON-NLS-1$
    if (windowImages != null)
    {
      Bundle brandingBundle = context.getBrandingBundle();
      if (brandingBundle != null)
      {
        List<Image> images = new ArrayList<Image>();
        for (String windowImageValue : StringUtil.explode(windowImages, ",")) //$NON-NLS-1$
        {
          URI windowImageURI = URI.createURI(windowImageValue);
          if (windowImageURI.isRelative())
          {
            URL url = brandingBundle.getEntry(windowImageValue);
            if (url == null)
            {
              continue;
            }

            windowImageURI = URI.createURI(url.toString());
          }

          images.add(ExtendedImageRegistry.INSTANCE.getImage(windowImageURI));
        }

        if (!images.isEmpty())
        {
          Window.setDefaultImages(images.toArray(new Image[images.size()]));
        }
      }
    }

    boolean restarted = false;
    File restarting = new File(SetupContext.CONFIGURATION_STATE_LOCATION_URI.appendSegment("restarting").toFileString()); //$NON-NLS-1$
    SelectionMemento selectionMemento = null;

    if (restarting.exists())
    {
      try
      {
        restarted = true;
        selectionMemento = (SelectionMemento)IOUtil.readObject(restarting, getClass().getClassLoader());
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        try
        {
          restarting.delete();
        }
        catch (Throwable ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    final Display display = Display.getDefault();
    handleCocoaMenu(display, installerDialog);

    display.asyncExec(new Runnable()
    {
      public void run()
      {
        // End the splash screen once the dialog is up.
        context.applicationRunning();
      }
    });

    String modeName = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_MODE);
    if (modeName == null)
    {
      modeName = PREF_MODE.get(Mode.SIMPLE.name());
    }

    mode = Mode.valueOf(modeName.toUpperCase());

    Collection<? extends Resource> configurationResources = null;

    for (;;)
    {
      if (selectionMemento == null)
      {
        selectionMemento = new SelectionMemento();
      }

      Installer installer = new Installer(selectionMemento);
      currentInstaller.set(installer);

      if (configurationResources == null)
      {
        // This is the first time, so process command line arguments.
        @SuppressWarnings("rawtypes")
        Map arguments = context.getArguments();
        if (arguments != null)
        {
          String[] applicationArgs = (String[])arguments.get(IApplicationContext.APPLICATION_ARGS);
          if (applicationArgs != null)
          {
            for (String argument : applicationArgs)
            {
              installer.handleArgument(argument);
            }
          }
        }
      }
      else
      {
        // Process and configuration resources that were previous processed, e.g., when switching modes after apply a configuration.
        for (Resource resource : configurationResources)
        {
          installer.handleURI(resource.getURI());
        }
      }

      if (mode == Mode.ADVANCED)
      {
        if (KeepInstallerUtil.canKeepInstaller())
        {
          Shell shell = new Shell(display);
          if (MessageDialog.openQuestion(shell, PropertiesUtil.getProductName(),
              Messages.InstallerApplication_KeepInstaller_message))
          {
            if (new KeepInstallerDialog(shell, true).open() == KeepInstallerDialog.OK)
            {
              return EXIT_OK;
            }
          }
        }

        installerDialog[0] = new InstallerDialog(null, installer, restarted);
      }
      else
      {
        SimpleInstallerDialog dialog = new SimpleInstallerDialog(display, installer, restarted);
        installer.setSimpleShell(dialog);
        installerDialog[0] = dialog;
      }

      final int retcode = installerDialog[0].show();
      if (retcode == InstallerUI.RETURN_SIMPLE)
      {
        setMode(Mode.SIMPLE);
        selectionMemento = null;
        configurationResources = installer.getAppliedConfigurationResources();
        continue;
      }

      if (retcode == InstallerUI.RETURN_ADVANCED)
      {
        setMode(Mode.ADVANCED);
        selectionMemento = null;
        configurationResources = installer.getAppliedConfigurationResources();
        continue;
      }

      if (retcode == InstallerUI.RETURN_RESTART)
      {
        try
        {
          IOUtil.writeObject(restarting, selectionMemento);
        }
        catch (Throwable ex)
        {
          //$FALL-THROUGH$
        }

        String launcher = OS.getCurrentLauncher(false);
        if (launcher != null)
        {
          try
          {
            // EXIT_RESTART often makes the new process come up behind other windows, so try a fresh native process first.
            launch(launcher);
            return EXIT_OK;
          }
          catch (Throwable ex)
          {
            //$FALL-THROUGH$
          }
        }

        return EXIT_RESTART;
      }

      return EXIT_OK;
    }
  }

  private void setMode(Mode mode)
  {
    this.mode = mode;
    PREF_MODE.set(mode.name());
  }

  private void handleCocoaMenu(final Display display, final InstallerUI[] installerDialog)
  {
    if (!SKIP_COCOA_MENU && Platform.WS_COCOA.equals(Platform.getWS()))
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
            NetworkConnectionsDialog proxyPreferenceDialog = new NetworkConnectionsDialog(installerDialog[0].getShell());
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

      ErrorDialog dialog = new ErrorDialog(Messages.InstallerApplication_Error_title, t, 0, 2, IDialogConstants.OK_LABEL, Messages.InstallerApplication_Update_label, IDialogConstants.SHOW_DETAILS_LABEL)
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

            if (!MessageDialog.openQuestion(shell, Messages.InstallerApplication_EmergencyUpdate_title,
                Messages.InstallerApplication_EmergencyUpdate_message))
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
                      UIUtil.syncExec(new Runnable()
                      {
                        public void run()
                        {
                          MessageDialog.openInformation(shell, Messages.InstallerApplication_Update_label, Messages.InstallerApplication_NoUpdates_message);
                        }
                      });

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
              //$FALL-THROUGH$
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
    // Do nothing.
  }

  static void launch(String launcher) throws IOException
  {
    String[] args = Platform.getCommandLineArgs();
    String[] cmdarray = new String[1 + args.length];
    cmdarray[0] = launcher;
    System.arraycopy(args, 0, cmdarray, 1, args.length);

    Runtime.getRuntime().exec(cmdarray);
  }

  /**
   * @author Eike Stepper
   */
  public static enum Mode
  {
    SIMPLE, ADVANCED;
  }
}

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
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.security.storage.PasswordProviderModuleExt;
import org.eclipse.equinox.security.storage.provider.PasswordProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.testing.ITestHarness;
import org.eclipse.ui.testing.TestableObject;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class SetupInstallerPlugin extends OomphUIPlugin
{
  public static final SetupInstallerPlugin INSTANCE = new SetupInstallerPlugin();

  public static final String FONT_OPEN_SANS = "font-open-sans"; //$NON-NLS-1$

  public static final String FONT_LABEL_DEFAULT = FONT_OPEN_SANS + ".label-default"; //$NON-NLS-1$

  private static Implementation plugin;

  public SetupInstallerPlugin()
  {
    super(new ResourceLocator[] { SetupUIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static void runTests()
  {
    try
    {
      TestableObject testableObject = PlatformUI.getTestableObject();
      if (testableObject != null)
      {
        final ITestHarness testHarness = testableObject.getTestHarness();
        if (testHarness != null)
        {
          new Job("Test Harness") //$NON-NLS-1$
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              try
              {
                testHarness.runTests();
              }
              catch (Exception ex)
              {
                INSTANCE.log(ex, IStatus.WARNING);
              }

              return Status.OK_STATUS;
            }
          }.schedule();
        }
      }
    }
    catch (Throwable ex)
    {
      INSTANCE.log(ex);
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

    @SuppressWarnings("restriction")
    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      if (!PropertiesUtil.isProperty(SetupUIPlugin.PREF_HEADLESS) && !SetupUtil.SETUP_ARCHIVER_APPLICATION)
      {
        Display.setAppName(PropertiesUtil.getProductName());
      }

      adjustDefaultPasswordProvider();

      if (PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_USER_HOME_REDIRECT))
      {
        System.setProperty("user.home", new File(PropertiesUtil.getUserHome()).getCanonicalPath()); //$NON-NLS-1$
      }

      if (!PropertiesUtil.isProperty(SetupUIPlugin.PREF_HEADLESS) && !SetupUtil.SETUP_ARCHIVER_APPLICATION)
      {
        UIUtil.syncExec(new Runnable()
        {
          @Override
          public void run()
          {
            initializeFonts();
          }
        });
      }

      // Replace the default UI Callback Provider with our own because the default one doesn't work when there is no workbench.
      ReflectUtil.setValue("callback", org.eclipse.equinox.internal.security.storage.CallbacksProvider.getDefault(), new UICallbackProvider()); //$NON-NLS-1$

      try
      {
        // Load this bundle early because it registers a system-wide authenticator to does not work when there is no workbench.
        CommonPlugin.loadClass("org.eclipse.core.net", "org.eclipse.core.net.proxy.IProxyService"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      catch (ClassNotFoundException ex)
      {
        // Ignore.
      }

      // Delegate authentication to the handler used by the ECF URI Handler.
      // It uses secure storage to remember the password.
      Authenticator.setDefault(new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
          URI uri = URI.createURI(getRequestingScheme() + "://" + getRequestingHost()); //$NON-NLS-1$
          Authorization authorization = SetupCoreUtil.AUTHORIZATION_HANDLER.authorize(uri);
          if (!authorization.isAuthorized())
          {
            authorization = SetupCoreUtil.AUTHORIZATION_HANDLER.reauthorize(uri, authorization);
          }

          return authorization.isAuthorized() ? new PasswordAuthentication(authorization.getUser(), authorization.getPassword().toCharArray()) : null;
        }
      });
    }

    @SuppressWarnings("restriction")
    private void adjustDefaultPasswordProvider()
    {
      org.eclipse.equinox.internal.security.storage.PasswordProviderSelector passwordProviderSelector = org.eclipse.equinox.internal.security.storage.PasswordProviderSelector
          .getInstance();
      try
      {
        Map<String, org.eclipse.equinox.internal.security.storage.PasswordProviderModuleExt> modules = ReflectUtil.getValue("modules", //$NON-NLS-1$
            passwordProviderSelector);
        org.eclipse.equinox.internal.security.storage.PasswordProviderModuleExt defaultProvider = passwordProviderSelector
            .findStorageModule("org.eclipse.equinox.security.ui.DefaultPasswordProvider"); //$NON-NLS-1$
        if (defaultProvider != null)
        {
          PasswordProvider passwordProvider = ReflectUtil.getValue("providerModule", defaultProvider); //$NON-NLS-1$
          if (org.eclipse.equinox.internal.security.ui.storage.DefaultPasswordProvider.class.equals(passwordProvider.getClass()))
          {
            String id = defaultProvider.getID();
            PasswordProviderModuleExt passwordProviderModuleExt = new org.eclipse.equinox.internal.security.storage.PasswordProviderModuleExt(
                new InstallerUIPrompt(), id, defaultProvider.getObsoleteID());
            modules.put(id, passwordProviderModuleExt); // $NON-NLS-1$
          }
        }
      }
      catch (Exception ex)
      {
        INSTANCE.log(ex);
      }
    }

    private void initializeFonts()
    {
      FontData[] fontData = JFaceResources.getDefaultFont().getFontData();
      int height = fontData == null || fontData.length == 0 ? 9 : (int)fontData[0].height;

      loadFont("/fonts/OpenSans-Regular.ttf"); //$NON-NLS-1$
      JFaceResources.getFontRegistry().put(SetupInstallerPlugin.FONT_LABEL_DEFAULT, new FontData[] { new FontData("Open Sans", height, SWT.BOLD) }); //$NON-NLS-1$
    }

    private boolean loadFont(String path)
    {
      try
      {
        URL url = new URL("platform:/plugin/" + SetupInstallerPlugin.INSTANCE.getSymbolicName() + path); //$NON-NLS-1$
        URL fileURL = FileLocator.toFileURL(url);
        String filePath = fileURL.getPath();
        File file = new File(filePath);
        return UIUtil.getDisplay().loadFont(file.toString());
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    }
  }
}

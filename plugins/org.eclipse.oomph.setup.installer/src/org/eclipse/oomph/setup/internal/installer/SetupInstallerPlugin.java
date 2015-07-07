/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.testing.ITestHarness;
import org.eclipse.ui.testing.TestableObject;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Eike Stepper
 */
public final class SetupInstallerPlugin extends OomphUIPlugin
{
  public static final SetupInstallerPlugin INSTANCE = new SetupInstallerPlugin();

  public static final String FONT_OPEN_SANS = "font-open-sans";

  public static final String FONT_LABEL_DEFAULT = FONT_OPEN_SANS + ".label-default";

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
          new Job("Test Harness")
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

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      if (!PropertiesUtil.isProperty(SetupUIPlugin.PREF_HEADLESS))
      {
        initializeFonts();

        File temporaryIconsFolder = new File(System.getProperty("java.io.tmpdir"), System.currentTimeMillis() + ".oomph.icons");
        temporaryIconsFolder.mkdir();
        temporaryIconsFolder.deleteOnExit();
        ReflectUtil.setValue("imageDirectory", ImageURIRegistry.INSTANCE, temporaryIconsFolder);
      }
    }

    private void initializeFonts()
    {
      FontData[] fontData = JFaceResources.getDefaultFont().getFontData();
      int height = fontData == null || fontData.length == 0 ? 9 : (int)fontData[0].height;

      loadFont("/fonts/OpenSans-Regular.ttf");
      JFaceResources.getFontRegistry().put(SetupInstallerPlugin.FONT_LABEL_DEFAULT, new FontData[] { new FontData("Open Sans", height, SWT.BOLD) });
    }

    private boolean loadFont(String path)
    {
      try
      {
        URL url = new URL("platform:/plugin/" + SetupInstallerPlugin.INSTANCE.getSymbolicName() + path);
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

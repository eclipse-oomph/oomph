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

import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LicensePrePrompter extends AbstractSetupDialog
{
  private final String license;

  private Browser licenseBrowser;

  public LicensePrePrompter(Shell parentShell, String license)
  {
    super(parentShell, "Eclipse Foundation Software User Agreement", 700, 700, SetupInstallerPlugin.INSTANCE, false);
    this.license = license;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Applicable licenses will be discovered and prompted later in the installation process.\n"
        + "Avoid such interruptions by accepting the most common license now.";
  }

  @Override
  protected void createUI(Composite parent)
  {
    licenseBrowser = new Browser(parent, SWT.NONE);
    licenseBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
    licenseBrowser.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    licenseBrowser.setText(license);
    licenseBrowser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        String url = event.location;
        if (!"about:blank".equals(url))
        {
          OS.INSTANCE.openSystemBrowser(url);
          event.doit = false;
        }
      }
    });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, "Accept Now", true);
    createButton(parent, IDialogConstants.CANCEL_ID, "Decide Later", false);
  }

  public static void execute(Shell shell, User user)
  {
    String license = readLicense();
    if (StringUtil.isEmpty(license))
    {
      return;
    }

    new LicensePrePrompter(shell, license).open();
  }

  private static String readLicense()
  {
    StringBuilder builder = new StringBuilder();
    InputStream in = null;

    try
    {
      String path = LicensePrePrompter.class.getPackage().getName().replace('.', '/') + "/license.html";
      in = LicensePrePrompter.class.getClassLoader().getResourceAsStream(path);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null)
      {
        if (builder.length() != 0)
        {
          builder.append(StringUtil.NL);
        }

        builder.append(line);
      }
    }
    catch (Exception ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
    finally
    {
      IOUtil.close(in);
    }

    return builder.toString();
  }
}

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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.setup.ui.bundle.SetupUIPlugin;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.osgi.framework.Bundle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Eike Stepper
 */
public class SetupWizardDialog extends WizardDialog
{
  public SetupWizardDialog(Shell parentShell, SetupWizard wizard)
  {
    super(parentShell, wizard);
    setHelpAvailable(true);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    getShell().setImage(SetupUIPlugin.INSTANCE.getSWTImage("import_wiz"));
    return super.createDialogArea(parent);
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    getShell().addHelpListener(new HelpListener()
    {
      public void helpRequested(HelpEvent e)
      {
        if (getTray() != null)
        {
          closeTray();
          updatedHelpButton(false);
          return;
        }

        DialogTray tray = new DialogTray()
        {
          @Override
          protected Control createContents(Composite parent)
          {
            String prefix = "help/";
            String suffix = "installer/InstallerDialog.html";

            Bundle bundle = SetupUIPlugin.INSTANCE.getBundle();
            URL url = bundle.getResource(prefix + suffix);

            try
            {
              url = FileLocator.resolve(url);
              if (!"file".equalsIgnoreCase(url.getProtocol()))
              {
                url = null;
              }
            }
            catch (IOException ex)
            {
              SetupUIPlugin.INSTANCE.log(ex);
              url = null;
            }

            if (url == null)
            {
              File target = SetupUIPlugin.INSTANCE.exportResources(prefix);

              try
              {
                url = new File(target, suffix).toURI().toURL();
              }
              catch (MalformedURLException ex)
              {
                SetupUIPlugin.INSTANCE.log(ex);
              }
            }

            Browser browser = new Browser(parent, SWT.NONE);
            browser.setSize(500, 800);

            if (url != null)
            {
              browser.setUrl(url.toString());
            }
            else
            {
              browser.setText("Help content not found.");
            }

            return browser;
          }
        };

        openTray(tray);
        updatedHelpButton(true);
      }

      private void updatedHelpButton(boolean pushed)
      {
        try
        {
          Field field = ReflectUtil.getField(TrayDialog.class, "fHelpButton");
          ToolItem fHelpButton = (ToolItem)ReflectUtil.getValue(field, SetupWizardDialog.this);
          fHelpButton.setSelection(pushed);
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    });

    ToolBar toolBar = (ToolBar)super.createHelpControl(parent);
    createToolItemsForToolBar(toolBar);
    return toolBar;
  }

  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
  }
}

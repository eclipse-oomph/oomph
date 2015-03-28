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

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public final class KeepInstallerDialog extends AbstractSetupDialog
{
  private static boolean isKept;

  private final boolean startPermanentInstaller;

  private String location;

  private Button startMenuButton;

  private Button desktopButton;

  private Button quickLaunchButton;

  public KeepInstallerDialog(Shell parentShell, boolean startPermanentInstaller)
  {
    super(parentShell, SHELL_TEXT, 500, 300, SetupInstallerPlugin.INSTANCE, false);
    this.startPermanentInstaller = startPermanentInstaller;
  }

  public final String getLocation()
  {
    return location;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Copy the installer to a permanent location on your disk.";
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected Control createContents(Composite parent)
  {
    Control contents = super.createContents(parent);
    parent.pack();
    return contents;
  }

  @Override
  protected void createUI(Composite parent)
  {
    final Shell shell = getShell();
    setTitle("Keep Installer");

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = getContainerMargin();
    layout.marginHeight = getContainerMargin();
    layout.verticalSpacing = 5;
    parent.setLayout(layout);

    Label locationLabel = new Label(parent, SWT.NONE);
    locationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    locationLabel.setText("Copy to:");

    final Text locationText = new Text(parent, SWT.BORDER);
    locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    locationText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        location = locationText.getText();
        String error = validate();

        setErrorMessage(error);

        Button okButton = getButton(IDialogConstants.OK_ID);
        okButton.setEnabled(error == null && location.length() != 0);
      }

      private String validate()
      {
        if (location.length() == 0)
        {
          return null;
        }

        File folder = new File(location);
        if (!folder.exists())
        {
          return null;
        }

        if (!folder.isDirectory())
        {
          return "Path is not a directory.";
        }

        if (!isEmpty(folder))
        {
          return "Directory is not empty.";
        }

        return null;
      }

      private boolean isEmpty(File folder)
      {
        File[] children = folder.listFiles();
        return children == null || children.length == 0;
      }
    });

    Button browseButton = new Button(parent, SWT.NONE);
    browseButton.setText("Browse...");
    browseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setText("Keep Installer");
        dialog.setMessage("Select an empty permanent location");
        dialog.setFilterPath(locationText.getText());

        String dir = dialog.open();
        if (dir != null)
        {
          locationText.setText(dir);
        }
      }
    });

    Label shortcutsLabel = new Label(parent, SWT.NONE);
    shortcutsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    shortcutsLabel.setText("Create shortcuts:");

    startMenuButton = new Button(parent, SWT.CHECK);
    startMenuButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    startMenuButton.setText("In the start menu");
    startMenuButton.setSelection(true);

    new Label(parent, SWT.NONE);

    desktopButton = new Button(parent, SWT.CHECK);
    desktopButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    desktopButton.setText("On the desktop");

    new Label(parent, SWT.NONE);

    quickLaunchButton = new Button(parent, SWT.CHECK);
    quickLaunchButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    quickLaunchButton.setText("As a quick launch");

    getShell().getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        File home = new File(PropertiesUtil.USER_HOME);
        for (int i = 1; i < Integer.MAX_VALUE; i++)
        {
          File folder = new File(home, "oomph" + (i > 1 ? i : ""));
          if (!folder.exists())
          {
            String path = folder.getAbsolutePath();
            locationText.setText(path);
            locationText.setSelection(path.length());
            return;
          }
        }
      }
    });
  }

  @Override
  protected void okPressed()
  {
    final String launcher = InstallerApplication.getLauncher();
    if (launcher != null)
    {
      final boolean startMenu = startMenuButton.getSelection();
      final boolean desktop = desktopButton.getSelection();
      final boolean quickLaunch = quickLaunchButton.getSelection();

      ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog((Shell)getShell().getParent());

      try
      {
        progressMonitorDialog.run(true, false, new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            monitor.beginTask("Copying installer to " + location, IProgressMonitor.UNKNOWN);
            keepInstaller(launcher, startMenu, desktop, quickLaunch);
            monitor.done();
          }
        });
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    super.okPressed();
  }

  protected void keepInstaller(String launcher, boolean startMenu, boolean desktop, boolean quickLaunch)
  {
    File source = new File(launcher).getParentFile();
    File target = new File(location);
    IOUtil.copyTree(source, target, true);

    if (startPermanentInstaller)
    {
      try
      {
        Runtime.getRuntime().exec(new File(target, new File(launcher).getName()).getAbsolutePath());
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
    else
    {
      String url = target.toURI().toString();
      OS.INSTANCE.openSystemBrowser(url);
    }

    isKept = true;
  }

  public static boolean canKeepInstaller()
  {
    if (!isKept && OS.INSTANCE.isWin())
    {
      String launcher = InstallerApplication.getLauncher();
      return launcher != null && launcher.startsWith(PropertiesUtil.TEMP_DIR);
    }

    return false;
  }
}

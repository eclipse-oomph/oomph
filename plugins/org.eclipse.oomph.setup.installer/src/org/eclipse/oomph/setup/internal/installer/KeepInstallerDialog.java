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
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public final class KeepInstallerDialog extends AbstractSetupDialog
{
  private static final Preference PREF_KEPT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("kept");

  private static boolean kept = PREF_KEPT.get(false);

  private static String powerShell;

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
        FileDialog dialog = new FileDialog(shell, SWT.APPLICATION_MODAL | SWT.SAVE);
        dialog.setText("Keep Installer");

        if (!StringUtil.isEmpty(location))
        {
          final File file = new File(location).getAbsoluteFile();
          dialog.setFilterPath(file.getParent());
          dialog.setFileName(file.getName());
        }

        String dir = dialog.open();
        if (dir != null)
        {
          locationText.setText(dir);
        }
      }
    });

    if (getPowerShell() != null)
    {
      new Label(parent, SWT.NONE);
      startMenuButton = new Button(parent, SWT.CHECK);
      startMenuButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
      startMenuButton.setText("Create start menu entry");
      startMenuButton.setSelection(true);

      new Label(parent, SWT.NONE);
      desktopButton = new Button(parent, SWT.CHECK);
      desktopButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
      desktopButton.setText("Create desktop shortcut");

      new Label(parent, SWT.NONE);
      quickLaunchButton = new Button(parent, SWT.CHECK);
      quickLaunchButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
      quickLaunchButton.setText("Pin to task bar");
    }

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
      final boolean startMenu = startMenuButton == null ? false : startMenuButton.getSelection();
      final boolean desktop = desktopButton == null ? false : desktopButton.getSelection();
      final boolean quickLaunch = quickLaunchButton == null ? false : quickLaunchButton.getSelection();

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

    String launcherName = new File(launcher).getName();
    String permanentLauncher = new File(target, launcherName).getAbsolutePath();

    if (startPermanentInstaller)
    {
      try
      {
        Runtime.getRuntime().exec(permanentLauncher);
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

    if (startMenu)
    {
      createShortCut("Programs", permanentLauncher);
    }

    if (desktop)
    {
      createShortCut("Desktop", permanentLauncher);
    }

    if (quickLaunch)
    {
      pinToTaskBar(location, launcherName);
    }

    kept = true;
    PREF_KEPT.set(true);
  }

  private static void createShortCut(String specialFolder, String target)
  {
    try
    {
      String powerShell = getPowerShell();
      if (powerShell != null)
      {
        Runtime.getRuntime()
            .exec(new String[] { powerShell, "-command",
                "& {$linkPath = Join-Path ([Environment]::GetFolderPath('" + specialFolder + "')) 'Eclipse Installer.lnk'; $targetPath = '" + target
                    + "'; $link = (New-Object -ComObject WScript.Shell).CreateShortcut( $linkpath ); $link.TargetPath = $targetPath; $link.Save()}" });
      }
    }
    catch (IOException ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
  }

  private static void pinToTaskBar(String location, String launcherName)
  {
    try
    {
      String powerShell = getPowerShell();
      if (powerShell != null)
      {
        Runtime.getRuntime().exec(new String[] { powerShell, "-command",
            "& { (new-object -c shell.application).namespace('" + location + "').parsename('" + launcherName + "').invokeverb('taskbarpin') }" });
      }
    }
    catch (IOException ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
  }

  private static String getPowerShell()
  {
    if (powerShell == null)
    {
      try
      {
        String systemRoot = System.getenv("SystemRoot");
        if (systemRoot != null)
        {
          File system32 = new File(systemRoot, "system32");
          if (system32.isDirectory())
          {
            File powerShellFolder = new File(system32, "WindowsPowerShell");
            if (powerShellFolder.isDirectory())
            {
              File[] versions = powerShellFolder.listFiles();
              if (versions != null)
              {
                for (File version : versions)
                {
                  try
                  {
                    File executable = new File(version, "powershell.exe");
                    if (executable.isFile())
                    {
                      powerShell = executable.getAbsolutePath();
                      break;
                    }
                  }
                  catch (Exception ex)
                  {
                    //$FALL-THROUGH$
                  }
                }
              }
            }
          }
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }

    return powerShell;
  }

  public static boolean canKeepInstaller()
  {
    if (!kept && OS.INSTANCE.isWin())
    {
      String launcher = InstallerApplication.getLauncher();
      return launcher != null && launcher.startsWith(PropertiesUtil.TEMP_DIR);
    }

    return false;
  }
}

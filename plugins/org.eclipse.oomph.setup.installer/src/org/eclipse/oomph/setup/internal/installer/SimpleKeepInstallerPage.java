/*
 * Copyright (c) 2015, 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 *    Christoph Laeubrich - [494735] - Eclipse Installer does not create .desktop file for the menu
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.setup.internal.installer.SimpleMessageOverlay.Type;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public class SimpleKeepInstallerPage extends SimpleInstallerPage
{
  private String location;

  private FlatButton applyButton;

  private SimpleCheckbox startMenuButton;

  private SimpleCheckbox desktopButton;

  private boolean startPermanentInstaller;

  public SimpleKeepInstallerPage(Composite parent, SimpleInstallerDialog dialog)
  {
    super(parent, dialog, true);
  }

  @Override
  protected void createContent(Composite container)
  {
    GridLayout layout = new GridLayout(1, false);
    layout.marginLeft = 17;
    layout.marginRight = 11;
    layout.marginTop = 39;
    layout.marginBottom = 30;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;

    container.setLayout(layout);
    container.setBackgroundMode(SWT.INHERIT_FORCE);
    container.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    Label title = new Label(container, SWT.NONE);
    title.setText(Messages.SimpleKeepInstallerPage_title);
    title.setForeground(UIUtil.getEclipseThemeColor());
    title.setFont(SimpleInstallerDialog.getFont(3, "bold")); //$NON-NLS-1$
    title.setLayoutData(GridDataFactory.swtDefaults().create());

    Label description = new Label(container, SWT.WRAP);
    description.setText(KeepInstallerUtil.KEEP_INSTALLER_DESCRIPTION + "."); //$NON-NLS-1$
    description.setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);
    description.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).indent(0, 10).create());

    Composite varContainer = new Composite(container, SWT.NONE);
    GridLayout varContainerLayout = new GridLayout(3, false);
    varContainerLayout.marginWidth = 0;
    varContainerLayout.marginHeight = 0;
    varContainerLayout.verticalSpacing = 3;
    varContainer.setLayout(varContainerLayout);
    varContainer.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).indent(0, 48).create());

    Label copyToLabel = createLabel(varContainer, Messages.SimpleKeepInstallerPage_CopyTo_label);
    copyToLabel.setLayoutData(GridDataFactory.swtDefaults().hint(144, SWT.DEFAULT).create());

    final Text locationText = createTextField(varContainer);
    locationText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        location = locationText.getText();
        String error = validate();

        setErrorMessage(error);

        applyButton.setEnabled(error == null && location.length() != 0);
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
          return Messages.SimpleKeepInstallerPage_PathNotDirectory_message;
        }

        if (!isEmpty(folder))
        {
          return Messages.SimpleKeepInstallerPage_DirectoryNotEmpty_message;
        }

        return null;
      }

      private boolean isEmpty(File folder)
      {
        File[] children = folder.listFiles();
        return children == null || children.length == 0;
      }
    });

    FlatButton folderButton = new ImageHoverButton(varContainer, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"), //$NON-NLS-1$
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_hover.png")); //$NON-NLS-1$
    folderButton.setLayoutData(GridDataFactory.swtDefaults().indent(12, 0).create());
    folderButton.setToolTipText(Messages.SimpleKeepInstallerPage_Browse_label + StringUtil.HORIZONTAL_ELLIPSIS);
    folderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        FileDialog chooser = new FileDialog(dialog.getShell(), SWT.APPLICATION_MODAL | SWT.SAVE);
        chooser.setText(Messages.SimpleKeepInstallerPage_KeepInstaller_label);

        if (!StringUtil.isEmpty(location))
        {
          final File file = new File(location).getAbsoluteFile();
          chooser.setFilterPath(file.getParent());
          chooser.setFileName(file.getName());
        }

        String dir = chooser.open();
        if (dir != null)
        {
          locationText.setText(dir);
        }
      }
    });

    if (KeepInstallerUtil.getDesktopSupport() != null)
    {
      new Label(varContainer, SWT.NONE);
      startMenuButton = createCheckbox(varContainer, Messages.SimpleKeepInstallerPage_StartMenu_label);
      startMenuButton.setChecked(true);

      new Label(varContainer, SWT.NONE);
      desktopButton = createCheckbox(varContainer, Messages.SimpleKeepInstallerPage_DesktopShortcut_message);
      desktopButton.setChecked(true);
    }

    new Label(varContainer, SWT.NONE);

    applyButton = new FlatButton(varContainer, SWT.PUSH);
    applyButton.setLayoutData(GridDataFactory.fillDefaults().indent(0, 43).hint(SWT.DEFAULT, 36).create());
    applyButton.setText(Messages.SimpleKeepInstallerPage_Apply_label);
    applyButton.setBackground(SetupInstallerPlugin.getColor(50, 196, 0));
    applyButton.setForeground(AbstractSimpleDialog.COLOR_WHITE);
    applyButton.setFont(SimpleInstallerDialog.getFont(5, "bold")); //$NON-NLS-1$
    applyButton.setCornerWidth(10);
    applyButton.setAlignment(SWT.CENTER);
    applyButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        final String launcher = OS.getCurrentLauncher(false);
        if (launcher != null)
        {
          final boolean startMenu = startMenuButton == null ? false : startMenuButton.isChecked();
          final boolean desktop = desktopButton == null ? false : desktopButton.isChecked();

          ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog((Shell)getShell().getParent());

          try
          {
            progressMonitorDialog.run(true, false, new IRunnableWithProgress()
            {
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                monitor.beginTask(NLS.bind(Messages.SimpleKeepInstallerPage_CopyingInstaller_task, location), IProgressMonitor.UNKNOWN);
                KeepInstallerUtil.keepInstaller(location, startPermanentInstaller, launcher, startMenu, desktop, false);

                UIUtil.getDisplay().asyncExec(new Runnable()
                {
                  public void run()
                  {
                    if (startPermanentInstaller)
                    {
                      dialog.close();
                    }
                    else
                    {
                      dialog.backSelected();
                    }
                  }
                });

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
      }
    });

    KeepInstallerDialog.setDefaultLocation(locationText);
  }

  public void setStartPermanentInstaller(boolean startPermanentInstaller)
  {
    this.startPermanentInstaller = startPermanentInstaller;
    applyButton.setText(startPermanentInstaller ? Messages.SimpleKeepInstallerPage_ApplyAndLaunch_label : Messages.SimpleKeepInstallerPage_Apply_label);
  }

  private SimpleCheckbox createCheckbox(Composite parent, String text)
  {
    final SimpleCheckbox checkbox = new SimpleCheckbox(parent);
    checkbox.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).span(2, 1).create());
    checkbox.setText(text);
    return checkbox;
  }

  private void setErrorMessage(String text)
  {
    if (text != null)
    {
      dialog.showMessage(text, Type.ERROR, false);
    }
  }
}

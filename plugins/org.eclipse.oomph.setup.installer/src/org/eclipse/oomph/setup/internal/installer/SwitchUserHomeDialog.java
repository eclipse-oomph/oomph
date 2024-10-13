/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.Objects;

public final class SwitchUserHomeDialog extends AbstractSetupDialog
{
  private String location;

  private String defaultLocation;

  public SwitchUserHomeDialog(Shell parentShell)
  {
    super(parentShell, PropertiesUtil.getProductName(), 780, 240, SetupInstallerPlugin.INSTANCE, false);
  }

  public String getUserHomeLocation()
  {
    return location;
  }

  @Override
  protected String getDefaultMessage()
  {
    return Messages.SwitchUserHomeDialog_Title_description;
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    final Shell shell = getShell();
    setTitle(Messages.SwitchUserHomeDialog_Title_text);

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = getContainerMargin();
    layout.marginHeight = getContainerMargin();
    layout.verticalSpacing = 5;
    parent.setLayout(layout);

    Label locationLabel = new Label(parent, SWT.NONE);
    locationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    locationLabel.setText(Messages.SwitchUserHomeDialog_UserHomeFolder_label);

    final Text locationText = new Text(parent, SWT.BORDER);
    locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    locationText.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        location = locationText.getText();
        String error = validate();

        setErrorMessage(error);

        Button okButton = getButton(IDialogConstants.OK_ID);
        okButton.setEnabled(error == null && location.length() != 0 && !Objects.equals(location, defaultLocation));
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
          return Messages.KeepInstallerDialog_PathNoDirectory_message;
        }

        return null;
      }
    });

    Button browseButton = new Button(parent, SWT.NONE);
    browseButton.setText(Messages.KeepInstallerDialog_Browse_label + StringUtil.HORIZONTAL_ELLIPSIS);
    browseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        FileDialog dialog = new FileDialog(shell, SWT.APPLICATION_MODAL | SWT.SAVE);
        dialog.setText(locationLabel.getText());

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

    setDefaultLocation(locationText);
  }

  @Override
  protected String getShellText()
  {
    return PropertiesUtil.getProductName();
  }

  protected void setDefaultLocation(final Text locationText)
  {
    UIUtil.asyncExec(locationText, new Runnable()
    {
      @Override
      public void run()
      {
        File home = new File(PropertiesUtil.getUserHome());
        defaultLocation = home.getAbsolutePath();
        locationText.setText(defaultLocation);
        locationText.setSelection(defaultLocation.length());
      }
    });
  }
}

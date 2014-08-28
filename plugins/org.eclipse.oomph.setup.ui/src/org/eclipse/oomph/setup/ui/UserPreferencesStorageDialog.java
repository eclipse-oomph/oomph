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
package org.eclipse.oomph.setup.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class UserPreferencesStorageDialog extends AbstractConfirmDialog
{
  private UserPreferencesManager.Storage storage;

  public UserPreferencesStorageDialog(Shell parentShell, UserPreferencesManager.Storage storage)
  {
    super(parentShell, "User Preferences Storage", 440, 305, "Remember choice");
    this.storage = storage;
  }

  public UserPreferencesStorageDialog(Shell parentShell)
  {
    this(parentShell, UserPreferencesManager.Storage.NONE);
  }

  public UserPreferencesManager.Storage getStorage()
  {
    return storage;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Choose how to store user tasks for changed preferences.";
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));
    composite.setLayout(new GridLayout(1, false));

    new Label(composite, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));

    createChoice(composite, UserPreferencesManager.Storage.NONE, "Don't store");
    createChoice(composite, UserPreferencesManager.Storage.MODEL, "Store in model (if the user.setup editor is not dirty)");
    createChoice(composite, UserPreferencesManager.Storage.EDITOR, "Store in editor (open an editor for user.setup if necessary)");

    new Label(composite, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));

    Dialog.applyDialogFont(composite);

  }

  protected void createChoice(Composite composite, UserPreferencesManager.Storage storage, String label)
  {
    Button button = new Button(composite, SWT.RADIO);
    button.setText(label);
    button.setData(storage);

    if (storage == this.storage)
    {
      button.setSelection(true);
    }

    button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        UserPreferencesStorageDialog.this.storage = (UserPreferencesManager.Storage)e.widget.getData();
      }
    });

    button.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDoubleClick(MouseEvent e)
      {
        setReturnCode(OK);
        close();
      }
    });
  }

  @Override
  protected String getRememberButtonToolTip()
  {
    return "The remembered choice can later be changed via Preferences | Oomph | Setup";
  }

  @Override
  protected void doCreateButtons(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }
}

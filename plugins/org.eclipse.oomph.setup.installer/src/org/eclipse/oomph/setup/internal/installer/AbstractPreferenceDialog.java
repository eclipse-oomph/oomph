/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractPreferenceDialog extends AbstractSetupDialog implements IPreferencePageContainer
{
  private PreferencePage preferencePage;

  private boolean showingError;

  public AbstractPreferenceDialog(Shell parentShell, String title)
  {
    super(parentShell, title, 600, 600, SetupInstallerPlugin.INSTANCE, false);
  }

  public IPreferenceStore getPreferenceStore()
  {
    return SetupInstallerPlugin.INSTANCE.getPreferenceStore();
  }

  public void updateTitle()
  {
    // Do nothing
  }

  public void updateMessage()
  {
    String message = null;
    String errorMessage = null;
    if (preferencePage != null)
    {
      message = preferencePage.getMessage();
      errorMessage = preferencePage.getErrorMessage();
    }

    int messageType = IMessageProvider.NONE;
    if (message != null)
    {
      messageType = ((IMessageProvider)preferencePage).getMessageType();
    }

    if (errorMessage == null)
    {
      if (showingError)
      {
        // We were previously showing an error
        showingError = false;
      }
    }
    else
    {
      message = errorMessage;
      messageType = IMessageProvider.ERROR;
      if (!showingError)
      {
        // We were not previously showing an error
        showingError = true;
      }
    }

    if (message == null)
    {
      message = getDefaultMessage();
    }

    setMessage(message, messageType);
  }

  public void updateButtons()
  {
    Button button = getButton(IDialogConstants.OK_ID);
    if (button != null)
    {
      button.setEnabled(preferencePage.isValid());
    }
  }

  @Override
  protected void createUI(Composite parent)
  {
    preferencePage = createPreferencePage();
    preferencePage.setContainer(this);
    preferencePage.createControl(parent);
    preferencePage.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  protected abstract PreferencePage createPreferencePage();

  @Override
  protected void okPressed()
  {
    preferencePage.performOk();
    super.okPressed();
  }
}

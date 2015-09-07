/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.setup.internal.sync.SynchronizerCredentials;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class SynchronizerLoginDialog extends AbstractSetupDialog implements ModifyListener
{
  private SynchronizerService service;

  private SynchronizerCredentials credentials;

  private SynchronizerLoginComposite loginComposite;

  private Button okButton;

  public SynchronizerLoginDialog(Shell parentShell, SynchronizerService service)
  {
    super(parentShell, "Configure Synchronizer", 400, 280, SetupUIPlugin.INSTANCE, false);
    this.service = service;
  }

  public SynchronizerService getService()
  {
    return service;
  }

  public SynchronizerCredentials getCredentials()
  {
    return credentials;
  }

  @Override
  protected String getShellText()
  {
    return "Oomph Preference Synchronizer";
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Connect your local workstation to your Eclipse.org account.  Press 'F1' for details.";
  }

  @Override
  protected String getImagePath()
  {
    return "sync/LoginBanner.png";
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    loginComposite = new SynchronizerLoginComposite(parent, SWT.NONE, 10, 10)
    {
      @Override
      protected void validate()
      {
        validatePage();
      }
    };

    loginComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    loginComposite.setService(service);
    Dialog.applyDialogFont(loginComposite);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);
    okButton = getButton(IDialogConstants.OK_ID);
    validatePage();
  }

  public void modifyText(ModifyEvent e)
  {
    validatePage();
  }

  @Override
  protected void okPressed()
  {
    SynchronizerCredentials credentials = loginComposite.getCredentials();

    try
    {
      if (!service.validateCredentials(credentials))
      {
        MessageDialog.openError(getShell(), "Authorization Failed", "You could not be authorized with the specified credentials.");
        return;
      }
    }
    catch (IOException ex)
    {
      ErrorDialog.open(ex);
      return;
    }

    this.credentials = credentials;
    super.okPressed();
  }

  private void validatePage()
  {
    if (okButton != null)
    {
      SynchronizerCredentials credentials = loginComposite.getCredentials();
      okButton.setEnabled(credentials != null);
    }
  }
}

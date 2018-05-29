/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.ui.EnablementComposite.InstallHandler;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public class EnablementDialog extends AbstractSetupDialog
{
  private static final String TITLE = "Oomph Extension Installation";

  private static final String BUTTON_LABEL = "OK";

  private final String productLabel;

  private final Map<EClass, EList<SetupTask>> enablementTasks;

  private EnablementComposite enablementComposite;

  private Button okButton;

  private Button cancelButton;

  public EnablementDialog(Shell parentShell, String productLabel, Map<EClass, EList<SetupTask>> enablementTasks)
  {
    super(parentShell, TITLE, 600, 400, SetupUIPlugin.INSTANCE, false);
    this.productLabel = productLabel;
    this.enablementTasks = enablementTasks;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return getDescription(productLabel, BUTTON_LABEL);
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    enablementComposite = new EnablementComposite(parent, SWT.BORDER);
    enablementComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    enablementComposite.setInput(enablementTasks);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    okButton = createButton(parent, IDialogConstants.OK_ID, BUTTON_LABEL, true);
    cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected void okPressed()
  {
    enableButtons(false);

    enablementComposite.install(new InstallHandler()
    {
      public void installSucceeded()
      {
        enableButtons(true);
        EnablementDialog.super.okPressed();
      }

      public void installFailed(Throwable t)
      {
        ErrorDialog.open(t);
        installCanceled();
      }

      public void installCanceled()
      {
        enableButtons(true);
      }
    });
  }

  private void enableButtons(boolean enabled)
  {
    okButton.setEnabled(enabled);
    cancelButton.setEnabled(enabled);
  }

  public static String getDescription(String productLabel, String buttonLabel)
  {
    return "This setup requires " + productLabel + " to be updated to include extended task implementations. Press " + buttonLabel + " to update "
        + productLabel + ".";
  }
}

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
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.IStorageService;

/**
 * @author Eike Stepper
 */
public class OptInDialog extends AbstractServiceDialog
{
  private Boolean answer = true;

  public OptInDialog(Shell parentShell, IStorageService service)
  {
    super(parentShell, service);
  }

  public Boolean getAnswer()
  {
    return answer;
  }

  @Override
  protected void createUI(Composite parent, String serviceLabel, String shortLabel)
  {
    setMessage("Do you want to save your preferences on the " + serviceLabel + " server so you can share them on other workstations?");

    Button yesButton = new Button(parent, SWT.RADIO);
    yesButton.setText("Yes  - You will be required to login to your " + shortLabel + " account.");
    yesButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        answer = true;
      }
    });

    Button noButton = new Button(parent, SWT.RADIO);
    noButton.setText("No  - Preferences will be saved locally.");
    noButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        answer = false;
      }
    });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, "Ask Me Later", false);
  }

  @Override
  protected void cancelPressed()
  {
    answer = null;
    super.cancelPressed();
  }
}

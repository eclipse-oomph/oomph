/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.IStorageService;

/**
 * @author Eike Stepper
 */
public class OptOutDialog extends AbstractServiceDialog
{
  private boolean answer = true;

  public OptOutDialog(Shell parentShell, IStorageService service)
  {
    super(parentShell, service);
  }

  public boolean getAnswer()
  {
    return answer;
  }

  @Override
  protected void createUI(Composite parent, String serviceLabel, String shortLabel)
  {
    setMessage("You did not login to your " + shortLabel + " account. Do you want to try again later?");

    Button yesButton = new Button(parent, SWT.RADIO);
    yesButton.setText("Yes  - Skip now and try again later.");
    yesButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        answer = true;
      }
    });

    Button noButton = new Button(parent, SWT.RADIO);
    noButton.setText("No  - Disable preference synchronization.");
    noButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        answer = false;
      }
    });

    new Label(parent, SWT.NONE);

    Label hint = new Label(parent, SWT.WRAP);
    hint.setText("You can change the synchronization settings at any time via\nPreferences | Oomph | Setup Tasks | Preference Synchronizer.");
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }
}

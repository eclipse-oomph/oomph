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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eike Stepper
 */
public abstract class AbstractConfirmDialog extends AbstractSetupDialog
{
  private final String rememberButtonText;

  private boolean remember;

  public AbstractConfirmDialog(String title, int width, int height, String rememberButtonText)
  {
    super(null, title, width, height, SetupUIPlugin.INSTANCE, null);
    this.rememberButtonText = rememberButtonText;
  }

  public boolean isRemember()
  {
    return remember;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    final Button rememberButton = createCheckbox(parent, rememberButtonText);
    rememberButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        remember = rememberButton.getSelection();
      }
    });

    createButton(parent, IDialogConstants.OK_ID, "Accept", false);
    createButton(parent, IDialogConstants.CANCEL_ID, "Decline", true);
  }
}

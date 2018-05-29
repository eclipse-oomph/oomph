/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractConfirmDialog extends AbstractSetupDialog
{
  private final String rememberButtonText;

  private boolean remember;

  public AbstractConfirmDialog(Shell parentShell, String title, int width, int height, String rememberButtonText)
  {
    super(parentShell, title, width, height, SetupUIPlugin.INSTANCE, false);
    this.rememberButtonText = rememberButtonText;
  }

  public AbstractConfirmDialog(String title, int width, int height, String rememberButtonText)
  {
    this(null, title, width, height, rememberButtonText);
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

    String rememberButtonToolTip = getRememberButtonToolTip();
    if (!StringUtil.isEmpty(rememberButtonToolTip))
    {
      rememberButton.setToolTipText(rememberButtonToolTip);
    }

    doCreateButtons(parent);
  }

  protected void doCreateButtons(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, "Accept", false);
    createButton(parent, IDialogConstants.CANCEL_ID, "Decline", true);
  }

  protected String getRememberButtonToolTip()
  {
    return null;
  }
}

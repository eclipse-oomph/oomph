/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.debug.ui.stringsubstitution.IArgumentSelector;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Ed Merks
 */
public class URIArgumentSelector implements IArgumentSelector
{
  public String selectArgument(IStringVariable variable, Shell shell)
  {
    URIArgumentDialog uriArgumentDialog = new URIArgumentDialog(shell);
    if (uriArgumentDialog.open() == Window.OK)
    {
      return uriArgumentDialog.getValue();
    }

    return null;
  }

  private static class URIArgumentDialog extends TrayDialog
  {
    private Text text;

    private String value;

    protected URIArgumentDialog(Shell parentShell)
    {
      super(parentShell);
      setHelpAvailable(false);
    }

    public String getValue()
    {
      return value;
    }

    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);
      newShell.setText("Variable Selection");
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(new GridLayout(2, false));

      text = new Text(composite, SWT.BORDER);
      GridData gridData = UIUtil.applyGridData(text);
      gridData.grabExcessHorizontalSpace = true;
      gridData.widthHint = 400;

      createButton(composite, IDialogConstants.OPEN_ID, "Variables...", false);

      return composite;
    }

    @Override
    protected void buttonPressed(int buttonId)
    {
      if (buttonId == IDialogConstants.OPEN_ID)
      {
        StringVariableSelectionDialog stringVariableSelectionDialog = new StringVariableSelectionDialog(getShell());
        if (stringVariableSelectionDialog.open() == Window.OK)
        {
          text.insert(stringVariableSelectionDialog.getVariableExpression());
        }
      }
      else
      {
        value = text.getText();
        super.buttonPressed(buttonId);
      }
    }
  }
}

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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Eike Stepper
 */
public abstract class AbstractTableAction extends Action
{
  private IWorkbenchPart part;

  public AbstractTableAction(String text)
  {
    super(text);
  }

  public void setActivePart(IWorkbenchPart part)
  {
    this.part = part;
  }

  @Override
  public void run()
  {
    Dialog dialog = new CommandTableDialog(part.getSite().getShell());
    dialog.open();
  }

  protected abstract String renderHTML();

  /**
   * @author Eike Stepper
   */
  private final class CommandTableDialog extends Dialog
  {
    public CommandTableDialog(Shell parentShell)
    {
      super(parentShell);
      setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MIN | SWT.MAX | SWT.DIALOG_TRIM);
      setBlockOnOpen(false);
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      getShell().setText(getText());

      String html = renderHTML();

      Browser browser = new Browser(parent, SWT.NONE);
      browser.setText(html);

      GridData layoutData = new GridData(GridData.FILL_BOTH);
      layoutData.heightHint = 800;
      layoutData.widthHint = 1000;
      browser.setLayoutData(layoutData);

      applyDialogFont(browser);

      return browser;
    }

    @Override
    protected Control createButtonBar(Composite parent)
    {
      return null;
    }
  }
}

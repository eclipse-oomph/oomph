/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import java.io.File;

/**
 * @author Eike Stepper
 */
public abstract class AbstractTableAction extends Action
{
  private static final boolean BROWSE_AVAILBLE = UIUtil.isBrowserAvailable();

  private IWorkbenchPart part;

  public AbstractTableAction(String text)
  {
    super(text, BROWSE_AVAILBLE ? IAction.AS_CHECK_BOX : IAction.AS_PUSH_BUTTON);
  }

  public void setActivePart(IWorkbenchPart part)
  {
    this.part = part;
    if (part != null)
    {
      DockableDialog dialog = DockableDialog.getFor(getDialogClass(), part.getSite().getWorkbenchWindow());
      if (dialog != null)
      {
        dialog.associate(this);
      }
    }
  }

  @Override
  public void run()
  {
    if (BROWSE_AVAILBLE)
    {
      if (isChecked())
      {
        DockableDialog dialog = DockableDialog.openFor(getDialogClass(), getDialogFactory(), part.getSite().getWorkbenchWindow());
        dialog.associate(this);
      }
      else
      {
        DockableDialog.closeFor(getDialogClass(), part.getSite().getWorkbenchWindow());
      }
    }
    else
    {
      try
      {
        String html = renderHTML();
        File tempFile = File.createTempFile("table", ".html");
        IOUtil.writeUTF8(tempFile, html);
        OS.INSTANCE.openSystemBrowser(tempFile.toURI().toString());
      }
      catch (Exception ex)
      {
        ErrorDialog.open(ex);
      }
    }
  }

  protected abstract <T extends DockableDialog> Class<T> getDialogClass();

  protected abstract <T extends DockableDialog> DockableDialog.Factory<T> getDialogFactory();

  protected abstract String renderHTML();

  /**
   * @author Eike Stepper
   */
  static class TableDialog extends DockableDialog
  {
    private final AbstractTableAction tableAction;

    public TableDialog(IWorkbenchWindow workbenchWindow, AbstractTableAction tableAction)
    {
      super(workbenchWindow);
      this.tableAction = tableAction;
    }

    @Override
    protected boolean handleWorkbenchPart(IWorkbenchPart part)
    {
      return true;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      getShell().setText(tableAction.getText());

      String html = tableAction.renderHTML();

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

/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.OomphWizardDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public class SetupWizardDialog extends OomphWizardDialog
{
  public SetupWizardDialog(Shell parentShell, SetupWizard wizard)
  {
    super(parentShell, wizard, true);

    if (wizard.getTrigger() != Trigger.BOOTSTRAP)
    {
      setNonModal();
    }
  }

  protected void setNonModal()
  {
    setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.RESIZE | getDefaultOrientation());
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);
    AccessUtil.setKey(getButton(IDialogConstants.BACK_ID), "back");
    AccessUtil.setKey(getButton(IDialogConstants.NEXT_ID), "next");
    AccessUtil.setKey(getButton(IDialogConstants.FINISH_ID), "finish");
    AccessUtil.setKey(getButton(IDialogConstants.CANCEL_ID), "cancel");
  }

  @Override
  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
    ToolItem helpButton = toolBar.getItems()[0];
    AccessUtil.setKey(helpButton, "help");
  }

  @Override
  protected void handleInactivity(Display display, boolean inactive)
  {
    IWizardPage currentPage = getCurrentPage();
    if (currentPage instanceof SetupWizardPage)
    {
      SetupWizardPage setupWizardPage = (SetupWizardPage)currentPage;
      setupWizardPage.handleInactivity(display, inactive);
    }
  }

  @Override
  protected IDialogSettings getDialogBoundsSettings()
  {
    return SetupUIPlugin.INSTANCE.getDialogSettings(getWizard().getClass().getName());
  }

  @Override
  protected int getDialogBoundsStrategy()
  {
    return DIALOG_PERSISTSIZE;
  }

  protected Point basicGetInitialSize()
  {
    return getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
  }

  @Override
  protected Point getInitialSize()
  {
    Point computedSize = basicGetInitialSize();
    Point initialSize = super.getInitialSize();
    return new Point(Math.max(computedSize.x, initialSize.x), Math.max(computedSize.y, initialSize.y));
  }
}

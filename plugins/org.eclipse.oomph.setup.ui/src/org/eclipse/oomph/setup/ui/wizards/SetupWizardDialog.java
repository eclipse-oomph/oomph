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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.ui.OomphWizardDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
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
}

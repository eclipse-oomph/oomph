/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.ui.AbstractDialog;

/**
 * @author Eike Stepper
 */
public class SynchronizerWelcomeDialog extends AbstractDialog
{
  public SynchronizerWelcomeDialog(Shell parentShell)
  {
    super(parentShell);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle("Preference Synchronization");
    setMessage("Enjoy the new preference synchronization service at Eclipse.org.");
    initializeDialogUnits(parent);

    Composite area = (Composite)super.createDialogArea(parent);
    return area;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL, false);
  }
}

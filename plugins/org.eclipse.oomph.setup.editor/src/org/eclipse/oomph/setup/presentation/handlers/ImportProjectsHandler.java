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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class ImportProjectsHandler extends AbstractDropdownItemHandler
{
  public ImportProjectsHandler()
  {
    super("Project", Messages.ImportProjectsHandler_text); //$NON-NLS-1$
  }

  public void run()
  {
    Shell shell = SetupPropertyTester.getHandlingShell();
    if (shell != null)
    {
      shell.setVisible(true);
    }
    else
    {
      SetupWizard wizard = new SetupWizard.Importer();
      wizard.openDialog(UIUtil.getShell());
    }
  }
}

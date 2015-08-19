/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 434525 - Allow prompted variables to be pre-populated
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.ui.EnablementComposite;
import org.eclipse.oomph.setup.ui.EnablementComposite.InstallHandler;
import org.eclipse.oomph.setup.ui.EnablementComposite.InstallOperation;
import org.eclipse.oomph.setup.ui.EnablementDialog;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardPage;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.emf.common.util.EList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class ExtensionPage extends SetupWizardPage
{
  private EnablementComposite enablementComposite;

  private InstallOperation installOperation;

  protected ExtensionPage()
  {
    super("ExtensionPage");
    setTitle("Extensions");
    setDescription(EnablementDialog.getDescription("the installer", "Finish"));
  }

  @Override
  protected Control createUI(Composite parent)
  {
    enablementComposite = new EnablementComposite(parent, SWT.BORDER);
    enablementComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    return enablementComposite;
  }

  @Override
  public WizardFinisher getWizardFinisher()
  {
    return new WizardFinisher()
    {
      public boolean performFinish()
      {
        install();

        // The installation is asynchronous; so don't close the dialog right now.
        return false;
      }
    };
  }

  @Override
  public void enterPage(boolean forward)
  {
    if (forward)
    {
      try
      {
        SetupTaskPerformer performer = createPerformer(SetupPrompter.OK, false);
        EList<SetupTask> triggeredSetupTasks = performer.getTriggeredSetupTasks();

        if (enablementComposite.setInput(triggeredSetupTasks) != null)
        {
          setPageComplete(false);
        }
        else
        {
          setPageComplete(true);
          gotoNextPage();
        }
      }
      catch (Throwable t)
      {
        ErrorDialog.open(t);
      }
    }
    else
    {
      gotoPreviousPage();
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (!forward)
    {
      if (installOperation != null)
      {
        installOperation.cancel();
      }
    }
  }

  public void install()
  {
    setButtonState(IDialogConstants.FINISH_ID, false);
    setButtonState(IDialogConstants.CANCEL_ID, false);

    installOperation = enablementComposite.install(new InstallHandler()
    {
      public void installSucceeded()
      {
        installOperation = null;

        IWizardContainer container = getContainer();
        if (container instanceof InstallerDialog)
        {
          InstallerDialog dialog = (InstallerDialog)container;
          dialog.restart();
        }
      }

      public void installFailed(Throwable t)
      {
        ErrorDialog.open(t);
        installCanceled();
      }

      public void installCanceled()
      {
        setButtonState(IDialogConstants.FINISH_ID, true);
        setButtonState(IDialogConstants.CANCEL_ID, true);
        installOperation = null;
      }
    });
  }
}

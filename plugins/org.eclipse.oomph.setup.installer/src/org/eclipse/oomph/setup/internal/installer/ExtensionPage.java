/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardPage;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class ExtensionPage extends SetupWizardPage
{
  private static final String SETUP_EXTENSION_ANALYSIS_TITLE = "Setup Extension Analysis";

  private EnablementComposite enablementComposite;

  private InstallOperation installOperation;

  private PerformerCreationJob performerCreationJob;

  private StackComposite stackComposite;

  private Composite messageComposite;

  protected ExtensionPage()
  {
    super("ExtensionPage");
    setTitle("Extensions");
  }

  @Override
  protected Control createUI(Composite parent)
  {
    stackComposite = new StackComposite(parent, SWT.NONE);
    stackComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    enablementComposite = new EnablementComposite(stackComposite, SWT.BORDER);

    messageComposite = new Composite(stackComposite, SWT.NONE);
    messageComposite.setLayout(UIUtil.createGridLayout(1));
    stackComposite.setTopControl(messageComposite);

    Label initialLabel = new Label(messageComposite, SWT.NONE);
    initialLabel.setText("Analyzing the needed setup extensions...");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.CENTER;
    data.grabExcessVerticalSpace = true;
    data.verticalAlignment = GridData.CENTER;
    initialLabel.setLayoutData(data);

    return stackComposite;
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
        messageComposite.setVisible(false);
        stackComposite.setTopControl(messageComposite);

        setDescription(StringUtil.cap(SETUP_EXTENSION_ANALYSIS_TITLE.toLowerCase()) + '.');
        setButtonState(IDialogConstants.FINISH_ID, false);
        setButtonState(IDialogConstants.NEXT_ID, false);

        final AtomicBoolean canceled = new AtomicBoolean();
        performerCreationJob = new PerformerCreationJob(SETUP_EXTENSION_ANALYSIS_TITLE)
        {
          @Override
          protected SetupTaskPerformer createPerformer() throws Exception
          {
            return ExtensionPage.this.createPerformer(SetupPrompter.OK, false);
          }

          @Override
          protected Dialog createDialog()
          {
            return createDialog(getShell(), SETUP_EXTENSION_ANALYSIS_TITLE, null,
                "Analyzing the needed setup extensions has taken more than " + (System.currentTimeMillis() - getStart()) / 1000
                    + " seconds.  Would you like to continue this step?",
                MessageDialog.QUESTION, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
          }

          @Override
          protected void handleDialogResult(int result)
          {
            if (result == 0)
            {
              setDelay(Integer.MAX_VALUE);
            }
            else if (result == 1)
            {
              canceled.set(true);
              cancel();
            }
            else
            {
              setDelay(2 * getDelay());
            }
          }

          @Override
          protected void heartBeat()
          {
            messageComposite.setVisible(true);
          }
        };

        performerCreationJob.create();
        if (getControl().isDisposed())
        {
          return;
        }

        Throwable throwable = performerCreationJob.getThrowable();
        if (throwable != null)
        {
          if (throwable instanceof OperationCanceledException)
          {
            // The operation could also be canceled because the user pressed the back button.
            // So only proceed if the operation wasn't explicitly canceled by the user.
            if (canceled.get())
            {
              setPageComplete(true);
              gotoNextPage();
            }

            return;
          }

          throw throwable;
        }

        EList<SetupTask> triggeredSetupTasks = performerCreationJob.getPerformer().getTriggeredSetupTasks();
        if (enablementComposite.setInput(triggeredSetupTasks) != null)
        {
          stackComposite.setTopControl(enablementComposite);
          setDescription(EnablementDialog.getDescription("the installer", "Finish"));
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
        SetupUIPlugin.INSTANCE.log(t);
        ErrorDialog.open(t);
      }
      finally
      {
        performerCreationJob = null;
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

      if (performerCreationJob != null)
      {
        performerCreationJob.cancel();
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

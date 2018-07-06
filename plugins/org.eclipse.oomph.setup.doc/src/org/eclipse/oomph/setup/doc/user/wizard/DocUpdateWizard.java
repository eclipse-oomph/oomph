/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTrigger;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition;
import org.eclipse.oomph.setup.doc.concepts.DocTaskExecution;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.targlets.TargletTask;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import java.util.Iterator;

/**
 * Update Wizard
 * <p>
 * The update wizard is the basis for Oomph's startup- and manually-{@link DocTrigger triggered}, automated installation and provisioning process.
 * <br/>
 * {@link #updater()}
 * </p>
 * <p>
 * {@link #wizardFooter()}
 * </p>
 * <p>
 * The update wizard is also used for startup-triggered, installation and provisioning,
 * i.e., when the Eclipse product starts
 * and the {@select Window|Preferences...|Oomph|Setup|Skip automatic task execution at startup time} preference permits,
 * tasks are {@linkplain DocTaskComposition gathered},
 * and if any tasks {@linkplain DocTaskExecution need to perform},
 * the update wizard is opened on the {@link DocConfirmationPage progress page}.
 * </p>
 *
 * @number 300
 */
public abstract class DocUpdateWizard extends DocInstallWizard
{
  /**
   * @snippet image UpdateWizard.images
   */
  public static Image updater()
  {
    return CaptureUpdateWizard.getInstance().dialogImage;
  }

  /**
   * @ignore
   */
  public static class CaptureUpdateWizard extends CaptureSetupWizard
  {
    static CaptureUpdateWizard instance;

    private Image dialogImage;

    public static CaptureUpdateWizard getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureUpdateWizard();
        instance.dialogImage = instance.capture();
      }

      return instance;
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      // Turn back to the previous page, cleanup the user, validate to build a new performer, and then turn back to this page.
      regressToPreviousPage(wizardDialog);
      AccessUtil.busyWait(10);
      postProcessUser(wizardDialog);
      ReflectUtil.invokeMethod("validate", wizardDialog.getCurrentPage());
      advanceToNextPage(wizardDialog);

      AccessUtil.busyWait(100);

      SetupTaskPerformer performer = getPerformer(wizardDialog);
      for (Iterator<SetupTask> it = performer.getNeededTasks().iterator(); it.hasNext();)
      {
        SetupTask setupTask = it.next();
        if (!(setupTask instanceof P2Task || setupTask instanceof TargletTask))
        {
          it.remove();
        }
      }

      getViewer(wizardDialog, "viewer").refresh();

      postProcessConfirmationPage(wizardDialog, false);
    }

    @Override
    protected WizardDialog create(Shell shell)
    {
      return new WizardDialog(shell, new org.eclipse.oomph.setup.ui.wizards.SetupWizard.Updater(true));
    }
  }
}

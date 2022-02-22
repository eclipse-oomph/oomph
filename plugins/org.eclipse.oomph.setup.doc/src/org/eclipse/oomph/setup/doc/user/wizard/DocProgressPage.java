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
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.doc.concepts.DocTaskExecution;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Progress Page
 * <p>
 * The primary purpose of the progress page is to manage and monitor the tasks while they are {@link DocTaskExecution performing}.
 * {@link #progressPage()}
 * </p>
 *
 * @number 800
 */
@SuppressWarnings("nls")
public class DocProgressPage
{
  /**
   * @snippet image ProgressPage.images
   * @style box
   * @description
   * The page contains the following controls:
   * @callout
   * Displays the tasks being performed.
   * The task currently being performed is automatically selected in this viewer.
   * A user's selection in this viewer selects the corresponding logged output associated with the selected task,
   * assuming it has already performed.
   * @callout
   * Displays a progress log of the tasks being performed.
   * It scrolls automatically unless that is disabled.
   * If the user scrolls this view, automatic scrolling is disabled.
   * @callout
   * Determines whether the log automatically scrolls.
   * @callout
   * Determines whether the wizard is automatically dismissed when task execution completes successfully.
   * @callout
   * Determines whether the installed product is automatically launched upon successful completion,
   * or, if not already launched, to launch it when the wizard is finished.
   * This control is available only in the {@link DocInstallWizard install} wizard.
   * @callout
   * Determines whether the IDE will automatically restart if needed upon successful completion, e.g., if new bundles are installed.
   * Tasks that may require a restart are generally performed early,
   * and once those types of tasks are completed,
   * the IDE needs to be restarted before the remaining tasks can be performed.
   * This is either be done automatically, if enabled, or, if not, the user is be prompted to restart but can opt to restart later.
   * After the IDE restarts,
   * it will automatically being performing the remaining tasks via the updater wizard,
   * with no prompting for {@link DocConfirmationPage confirmation}.
   * This control is only available in the {@link DocImportWizard import} and {@link DocUpdateWizard update} wizards,
   * i.e., only in a running IDE.
   * @callout
   * Displays graphically the overall progress of the task execution.
   * Task execution can be canceled via this control.
   * All the wizard's navigation controls are disabled while tasks are performing.
   * Once task execution terminates,
   * either because it has been completed successfully, has been partially successful but requires a restart, has terminated in failure, or has been canceled,
   * the progress monitor will be hidden.
   * The page banner provides important feedback with regard to the actions to be taken upon termination of task execution.
   */
  public static Image[] progressPage()
  {
    DocProgressPage.CaptureProgressPage instance = CaptureProgressPage.getInstance();
    return new Image[] { instance.progressPage, instance.tree, instance.log, instance.lock, instance.dismiss, instance.launch, instance.restart,
        instance.progress };
  }

  /**
   * @ignore
   */
  public static class CaptureProgressPage extends CaptureSetupWizard
  {
    private static DocProgressPage.CaptureProgressPage instance;

    private Image progressPage;

    private Image tree;

    private Image log;

    private Image lock;

    private Image dismiss;

    private Image launch;

    private Image progress;

    private Image restart;

    public static DocProgressPage.CaptureProgressPage getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureProgressPage();
        instance.progressPage = instance.capture();
      }
      return instance;
    }

    @Override
    protected WizardDialog create(Shell shell)
    {
      return new InstallerDialog(shell, false);
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      postProcessProductPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessProjectPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessVariablePage(wizardDialog, "tmp\\oomph.capture");

      advanceToNextPage(wizardDialog);

      SetupTaskPerformer performer = getPerformer(wizardDialog);
      File installationLocation = performer.getInstallationLocation();

      if (!installationLocation.toString().contains("tmp\\oomph.capture"))
      {
        throw new RuntimeException("Bad install location");
      }

      IOUtil.deleteBestEffort(installationLocation);

      regressToPreviousPage(wizardDialog);

      postProcessVariablePage(wizardDialog, "tmp\\oomph.capture");

      advanceToNextPage(wizardDialog);

      postProcessConfirmationPage(wizardDialog, true);

      ReflectUtil.setValue("progressLogWrapper", wizardDialog.getCurrentPage().getNextPage(),
          ReflectUtil.getConstructor(CaptureProgressPage.ProgressLogWrapper.class, ProgressLog.class));

      advanceToNextPage(wizardDialog);

      while (!ProgressLogWrapper.instance.done)
      {
        AccessUtil.busyWait(1000);
      }

      TreeViewer treeViewer = getViewer(wizardDialog, "treeViewer");
      ITreeContentProvider provider = (ITreeContentProvider)treeViewer.getContentProvider();
      for (Object object : provider.getElements(treeViewer.getInput()))
      {
        if (object instanceof ResourceCreationTask)
        {
          ResourceCreationTask resourceCreationTask = (ResourceCreationTask)object;
          resourceCreationTask.setTargetURL(resourceCreationTask.getTargetURL().replace("tmp/oomph.capture", "oomph"));
          AccessUtil.busyWait(10);
        }
      }

      SashForm sashForm = getWidget(wizardDialog, "sash");
      sashForm.setWeights(new int[] { 40, 60 });
    }

    @Override
    protected Image capture(WizardDialog wizardDialog)
    {
      IWizardPage page = wizardDialog.getCurrentPage();

      Map<Control, Image> decorations = new LinkedHashMap<>();
      tree = getCalloutImage(1);
      decorations.put(getViewerControl(wizardDialog, "treeViewer"), tree);
      log = getCalloutImage(2);
      decorations.put((Control)getWidget(wizardDialog, "log"), log);
      Image result = capture(page, decorations);

      lock = getImage(wizardDialog, "lock");
      dismiss = getImage(wizardDialog, "dismiss");
      launch = getImage(wizardDialog, "launch");

      Button button = getWidget(wizardDialog, "launch");
      button.setText("Restart if needed");
      restart = getImage(button);

      progress = getImage(wizardDialog, "progress");

      ProgressLogWrapper.instance.cancel = true;

      return result;
    }

    /**
     * @ignore
     */
    private static final class ProgressLogWrapper implements ProgressLog
    {
      private static CaptureProgressPage.ProgressLogWrapper instance;

      private boolean done;

      private boolean isP2Task;

      private final ProgressLog log;

      private boolean cancel;

      private ProgressLogWrapper(ProgressLog log)
      {
        this.log = log;
        instance = this;
      }

      @Override
      public void task(SetupTask setupTask)
      {
        log.task(setupTask);

        if (setupTask instanceof P2Task)
        {
          isP2Task = true;
        }
      }

      @Override
      public void setTerminating()
      {
        log.setTerminating();
      }

      @Override
      public void log(Throwable t)
      {
        log.log(t);
      }

      @Override
      public void log(IStatus status)
      {
        log.log(status);
      }

      @Override
      public void log(String line, Severity severity)
      {
        log.log(line, severity);
      }

      @Override
      public void log(String line, boolean filter, Severity severity)
      {
        log.log(line, filter, severity);
      }

      @Override
      public void log(String line, boolean filter)
      {
        log.log(line, filter);
        if (isP2Task)
        {
          done = true;
          while (!cancel)
          {
            try
            {
              Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
              ex.printStackTrace();
            }
          }

          throw new RuntimeException("Canceled");
        }
      }

      @Override
      public void log(String line)
      {
        log.log(line);
      }

      @Override
      public boolean isCanceled()
      {
        return log.isCanceled();
      }
    }
  }
}

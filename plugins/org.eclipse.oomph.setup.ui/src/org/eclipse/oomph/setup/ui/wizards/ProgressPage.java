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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.OomphAdapterFactoryContentProvider;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.UnsignedPolicy;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.impl.DynamicSetupTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer.ExecutableInfo;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLog.Severity;
import org.eclipse.oomph.setup.log.ProgressLogFilter;
import org.eclipse.oomph.setup.log.ProgressLogProvider;
import org.eclipse.oomph.setup.log.ProgressLogRunnable;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.setup.ui.AbstractConfirmDialog;
import org.eclipse.oomph.setup.ui.AbstractDialogConfirmer;
import org.eclipse.oomph.setup.ui.LicenseDialog;
import org.eclipse.oomph.setup.ui.LicensePrePrompter;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.util.FileUtil;
import org.eclipse.oomph.ui.BackgroundProgressPart;
import org.eclipse.oomph.ui.ButtonBar;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.Pair;

import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.progress.JobInfo;
import org.eclipse.ui.internal.progress.ProgressManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class ProgressPage extends SetupWizardPage
{
  public static final Object PROGRESS_FAMILY = new Object();

  public static final String PROGRESS_STATUS = "org.eclipse.oomph.setup.status";

  public static final String PAGE_NAME = "ProgressPage";

  private final Map<SetupTask, Point> setupTaskSelections = new HashMap<SetupTask, Point>();

  private Constructor<? extends ProgressLog> progressLogWrapper;

  private TreeViewer treeViewer;

  private final ISelectionChangedListener treeViewerSelectionChangedListener = new ISelectionChangedListener()
  {
    public void selectionChanged(SelectionChangedEvent event)
    {
      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      Object element = selection.getFirstElement();
      if (element instanceof EObject)
      {
        for (EObject eObject = (EObject)element; eObject != null; eObject = eObject.eContainer())
        {
          if (eObject != currentTask && eObject instanceof SetupTask)
          {
            Point textSelection = setupTaskSelections.get(eObject);
            if (textSelection != null)
            {
              int start = textSelection.x;
              // Treat -1 so that at selects to the very end.
              int end = textSelection.y;
              if (end == -1)
              {
                end = logText.getCharCount();
              }

              String selectedText = logText.getText(start, end);
              int index = 0;
              int length = selectedText.length();

              // Skip leading line feeds.
              for (; index < length; ++index)
              {
                char c = selectedText.charAt(index);
                if (c == '\n' || c == '\r')
                {
                  ++start;
                }
                else
                {
                  break;
                }
              }

              // Force the first line to be scrolled into view
              logText.setSelection(start, start);

              // Determine the number of lines of text to be selected
              int lineFeedCount = 0;
              int carriageReturnCount = 0;
              for (; index < length; ++index)
              {
                char c = selectedText.charAt(index);
                if (c == '\n')
                {
                  ++lineFeedCount;
                }
                else if (c == '\r')
                {
                  ++carriageReturnCount;
                }
              }

              // If the number of visible lines is greater than the number of lines in the selection, invert the
              // selection range to scroll the top line into view.
              int visibleLineCount = logText.getClientArea().height / logText.getLineHeight();
              if (lineFeedCount > visibleLineCount || carriageReturnCount > visibleLineCount)
              {
                logText.setSelection(end, start);
              }
              else
              {
                logText.setSelection(start, end);
              }
            }
          }
        }
      }
    }
  };

  private StyledText logText;

  private ProgressMonitorPart progressMonitorPart;

  private final Document logDocument = new Document();

  private final ProgressLogFilter logFilter = new ProgressLogFilter();

  private SetupTask currentTask;

  private ProgressPageLog progressPageLog;

  private boolean scrollLock;

  private boolean dismissAutomatically;

  private boolean launchAutomatically;

  private Button scrollLockButton;

  private Button dismissButton;

  private Button launchButton;

  private boolean hasLaunched;

  public static final Confirmer LICENSE_CONFIRMER = new AbstractDialogConfirmer()
  {
    @Override
    protected AbstractConfirmDialog createDialog(boolean defaultConfirmed, Object info)
    {
      @SuppressWarnings("unchecked")
      Map<ILicense, List<IInstallableUnit>> licensesToIUs = (Map<ILicense, List<IInstallableUnit>>)info;
      return new LicenseDialog(licensesToIUs);
    }
  };

  public ProgressPage()
  {
    super(PAGE_NAME);
    setTitle("Progress");
    setDescription("Wait for the setup to complete, or cancel the progress indicator and press Back to make changes.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(UIUtil.createGridLayout(1));

    SashForm sashForm = new SashForm(mainComposite, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
    AccessUtil.setKey(sashForm, "sash");

    treeViewer = new TreeViewer(sashForm, SWT.BORDER);
    Tree tree = treeViewer.getTree();
    addHelpCallout(tree, 1);

    ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(treeViewer, new LocationListener()
    {
      public void changing(LocationEvent event)
      {
      }

      public void changed(LocationEvent event)
      {
      }
    });

    ILabelProvider labelProvider = createLabelProvider(toolTipSupport);
    treeViewer.setLabelProvider(labelProvider);

    final AdapterFactoryContentProvider contentProvider = new OomphAdapterFactoryContentProvider(getAdapterFactory());
    treeViewer.setContentProvider(contentProvider);
    treeViewer.addSelectionChangedListener(treeViewerSelectionChangedListener);

    tree.setLayoutData(new GridData(GridData.FILL_BOTH));
    tree.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

    TextViewer logTextViewer = new TextViewer(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
    logTextViewer.setDocument(logDocument);

    logText = logTextViewer.getTextWidget();
    logText.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    logText.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
    logText.setEditable(false);
    logText.setLayoutData(new GridData(GridData.FILL_BOTH));
    logText.getVerticalBar().addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent event)
      {
        if (event.detail == SWT.DRAG && !scrollLock)
        {
          scrollLockButton.setSelection(true);
          scrollLock = true;
        }
      }
    });

    addHelpCallout(logText, 2);
    AccessUtil.setKey(logText, "log");

    return mainComposite;
  }

  @Override
  protected void createCheckButtons(ButtonBar buttonBar)
  {
    scrollLockButton = buttonBar.addCheckButton("Scroll lock", "Keep the log from scrolling to the end when new messages are added", false, null);
    scrollLock = scrollLockButton.getSelection();
    scrollLockButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        scrollLock = scrollLockButton.getSelection();
      }
    });
    AccessUtil.setKey(scrollLockButton, "lock");

    dismissButton = buttonBar.addCheckButton("Dismiss automatically", "Dismiss this wizard when all setup tasks have performed successfully", false,
        "dismissAutomatically");
    dismissAutomatically = dismissButton.getSelection();
    dismissButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        dismissAutomatically = dismissButton.getSelection();
      }
    });
    AccessUtil.setKey(dismissButton, "dismiss");

    if (getWizard().getOS().isCurrentOS())
    {
      if (getTrigger() == Trigger.BOOTSTRAP)
      {
        launchButton = buttonBar.addCheckButton("Launch automatically", "Launch the installed product when all setup tasks have performed successfully", true,
            "launchAutomatically");
      }
      else
      {
        launchButton = buttonBar.addCheckButton("Restart automatically if needed",
            "Restart the current product if the installation has been changed by setup tasks", false, "restartIfNeeded");
      }

      launchAutomatically = launchButton.getSelection();
      launchButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          launchAutomatically = launchButton.getSelection();
        }
      });
      AccessUtil.setKey(launchButton, "launch");
    }
  }

  @Override
  protected void createFooter(Composite parent)
  {
    progressMonitorPart = new BackgroundProgressPart(parent, null, true)
    {
      @Override
      protected void initialize(Layout layout, int progressIndicatorHeight)
      {
        super.initialize(layout, progressIndicatorHeight);
        fLabel.dispose();

        if (PlatformUI.isWorkbenchRunning())
        {
          for (Control child : getChildren())
          {
            if (child instanceof ToolBar)
            {
              ToolBar toolBar = (ToolBar)child;
              ToolItem minimizeButton = new ToolItem(toolBar, SWT.PUSH);
              minimizeButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("hide"));
              minimizeButton.setToolTipText("Minimize to the status area");
              minimizeButton.addSelectionListener(new SelectionAdapter()
              {
                @Override
                public void widgetSelected(SelectionEvent event)
                {
                  getWizard().getShell().setVisible(false);
                }
              });

              break;
            }
          }
        }
      }

      @Override
      protected void updateLabel()
      {
      }

      @Override
      public void done()
      {
        UIUtil.syncExec(new Runnable()
        {
          public void run()
          {
            fProgressIndicator.sendRemainingWork();
            fProgressIndicator.done();
            removeFromCancelComponent(null);
            progressMonitorPart.setLayoutData(new GridData(0, 0));
            progressMonitorPart.getParent().layout();
          }
        });
      }
    };

    progressMonitorPart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    AccessUtil.setKey(progressMonitorPart, "progress");
  }

  @Override
  public void enterPage(boolean forward)
  {
    if (forward)
    {
      setPageComplete(false);
      setErrorMessage(null);
      setMessage(null);

      hasLaunched = false;

      progressPageLog = new ProgressPageLog(progressMonitorPart);
      logDocument.set("");

      // This is a private hook so we can monitor progress for documentation capture.
      ProgressLog progressLog = progressPageLog;
      if (progressLogWrapper != null)
      {
        try
        {
          progressLog = progressLogWrapper.newInstance(progressLog);
        }
        catch (Throwable ex)
        {
          // Ignore.
        }
      }

      final SetupTaskPerformer performer = getPerformer();
      User performerUser = performer.getUser();

      performer.setProgress(progressLog);
      performer.put(ILicense.class, LICENSE_CONFIRMER);
      performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(performerUser, false));

      File renamed = null;
      if (getTrigger() == Trigger.BOOTSTRAP)
      {
        File configurationLocation = performer.getProductConfigurationLocation();
        if (configurationLocation.exists())
        {
          try
          {
            // This must happen before the performer opens the logStream for the first logging!
            renamed = FileUtil.rename(configurationLocation);
          }
          catch (RuntimeException ex)
          {
            throw ex;
          }
          catch (Exception ex)
          {
            progressPageLog.log(ex);
            setErrorMessage("Could not rename '" + configurationLocation + "'.  Press Back twice to choose a different installation location.");
            progressPageLog.done();

            throw new IORuntimeException(ex);
          }
        }

        if (UIUtil.isBrowserAvailable())
        {
          EList<LicenseInfo> licenses = LicensePrePrompter.execute(getShell(), performerUser);
          if (licenses != null)
          {
            ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
            User user = SetupContext.createUserOnly(resourceSet).getUser();
            user.getAcceptedLicenses().addAll(licenses);
            BaseUtil.saveEObject(user);
          }
        }
      }

      treeViewer.setInput(new ItemProvider(performer.getNeededTasks()));

      String jobName = "Executing " + getTriggerName().toLowerCase() + " tasks";
      performer.log(jobName, false, Severity.INFO);

      if (renamed != null)
      {
        performer.log("Renamed existing configuration folder to " + renamed);
      }

      run(jobName, new ProgressLogRunnable()
      {
        public Set<String> run(ProgressLog log) throws Exception
        {
          final ProgressManager oldProgressProvider = ProgressManager.getInstance();
          ProgressLogProvider newProgressLogProvider = new ProgressLogProvider(progressPageLog, oldProgressProvider);

          // When the workbench isn't running org.eclipse.ui.internal.progress.new JobChangeAdapter()'s done(IJobChangeEvent) method one cleanup.
          JobChangeAdapter jobChangeListener = PlatformUI.isWorkbenchRunning() ? null : new JobChangeAdapter()
          {
            @Override
            public void done(IJobChangeEvent event)
            {
              Job job = event.getJob();
              for (JobInfo jobInfo : oldProgressProvider.getJobInfos(true))
              {
                if (jobInfo.getJob() == job)
                {
                  oldProgressProvider.removeJobInfo(jobInfo);
                  break;
                }
              }
            }
          };

          IJobManager jobManager = Job.getJobManager();
          jobManager.setProgressProvider(newProgressLogProvider);
          if (jobChangeListener != null)
          {
            jobManager.addJobChangeListener(jobChangeListener);
          }

          try
          {
            performer.perform(progressPageLog);
            return performer.getRestartReasons();
          }
          finally
          {
            jobManager.setProgressProvider(oldProgressProvider);
            if (jobChangeListener != null)
            {
              jobManager.removeJobChangeListener(jobChangeListener);
            }
          }
        }
      });
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      setPageComplete(false);
    }
    else
    {
      setPageComplete(false);
      hasLaunched = false;
      setButtonState(IDialogConstants.CANCEL_ID, true);
    }
  }

  @Override
  public boolean performCancel()
  {
    boolean isPerforming = getWizard().getCurrentPage() instanceof ProgressPage && !progressPageLog.isCanceled() && !progressPageLog.isDone();
    if (isPerforming && PlatformUI.isWorkbenchRunning())
    {
      getWizard().getShell().setVisible(false);
      return false;
    }

    return !isPerforming;
  }

  @Override
  protected void setButtonState(int buttonID, boolean enabled)
  {
    super.setButtonState(buttonID, enabled);

    if (buttonID == IDialogConstants.CANCEL_ID && getPerformer().hasSuccessfullyPerformed())
    {
      scrollLockButton.setEnabled(enabled);
      dismissButton.setEnabled(enabled);

      if (launchButton != null)
      {
        launchButton.setEnabled(!hasLaunched);
      }
    }
  }

  private ILabelProvider createLabelProvider(ColumnViewerInformationControlToolTipSupport toolTipSupport)
  {
    return new ToolTipLabelProvider(getAdapterFactory(), toolTipSupport)
    {
      @Override
      public Font getFont(Object element)
      {
        if (element == currentTask)
        {
          return ExtendedFontRegistry.INSTANCE.getFont(treeViewer.getControl().getFont(), IItemFontProvider.BOLD_FONT);
        }

        return super.getFont(element);
      }
    };
  }

  private void run(final String jobName, final ProgressLogRunnable runnable)
  {
    try
    {
      // Remember and use the progressPageLog that is valid at this point in time.
      final ProgressPageLog progressLog = progressPageLog;

      Runnable jobRunnable = new Runnable()
      {
        public void run()
        {
          final SetupWizard wizard = getWizard();
          final Shell shell = wizard.getShell();

          setButtonState(IDialogConstants.CANCEL_ID, false);
          setButtonState(IDialogConstants.BACK_ID, false);

          final Job job = new Job(jobName)
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              progressLog.setProgressMonitor(monitor);

              final Trigger trigger = getTrigger();
              long start = System.currentTimeMillis();
              boolean success = false;
              Set<String> restartReasons = null;

              UIUtil.syncExec(new Runnable()
              {
                public void run()
                {
                  shell.setData(PROGRESS_STATUS, null);
                  if (trigger != Trigger.BOOTSTRAP)
                  {
                    if (trigger == Trigger.STARTUP || !SetupPropertyTester.isShowProgressInWizard())
                    {
                      shell.setVisible(false);
                    }

                    SetupPropertyTester.setPerformingShell(shell);
                  }
                }
              });

              try
              {
                restartReasons = runnable.run(progressLog);

                SetupTaskPerformer performer = getPerformer();
                saveLocalFiles(performer);

                if (launchAutomatically && trigger == Trigger.BOOTSTRAP)
                {
                  progressLog.setTerminating();
                  hasLaunched = launchProduct(performer);
                }

                success = true;
              }
              catch (OperationCanceledException ex)
              {
                // Do nothing
              }
              catch (Throwable ex)
              {
                final IStatus status = SetupUIPlugin.INSTANCE.getStatus(ex);
                SetupUIPlugin.INSTANCE.log(new IStatus()
                {
                  public IStatus[] getChildren()
                  {
                    return status.getChildren();
                  }

                  public int getCode()
                  {
                    return status.getCode();
                  }

                  public Throwable getException()
                  {
                    return status.getException();
                  }

                  public String getMessage()
                  {
                    return status.getMessage();
                  }

                  public String getPlugin()
                  {
                    return status.getPlugin();
                  }

                  public int getSeverity()
                  {
                    return IStatus.WARNING;
                  }

                  public boolean isMultiStatus()
                  {
                    return status.isMultiStatus();
                  }

                  public boolean isOK()
                  {
                    return false;
                  }

                  public boolean matches(int severityMask)
                  {
                    return (severityMask & IStatus.WARNING) != 0;
                  }

                  @Override
                  public String toString()
                  {
                    StringBuilder result = new StringBuilder();
                    result.append("Status WARNING");
                    result.append(": ");
                    result.append(getPlugin());
                    result.append(" code=");
                    result.append(getCode());
                    result.append(' ');
                    result.append(getMessage());
                    result.append(' ');
                    result.append(getException());
                    result.append(" children=[");
                    result.append(status);
                    result.append("]");
                    return result.toString();
                  }
                });
                progressLog.log(ex);
              }
              finally
              {
                long seconds = (System.currentTimeMillis() - start) / 1000;
                progressLog.setTerminating();
                progressLog.message("Took " + seconds + " seconds.");

                getWizard().sendStats(success);

                final AtomicBoolean disableCancelButton = new AtomicBoolean(true);

                final boolean restart = restartReasons != null && !restartReasons.isEmpty() && trigger != Trigger.BOOTSTRAP;

                if (trigger == Trigger.STARTUP)
                {
                  StringBuilder startupLogMessage = new StringBuilder("Setup tasks were performed during startup");
                  int preferenceTaskCount = 0;
                  if (!restart)
                  {
                    for (SetupTask setupTask : getPerformer().getTriggeredSetupTasks())
                    {
                      if (setupTask instanceof PreferenceTask)
                      {
                        ++preferenceTaskCount;
                      }
                    }
                  }

                  if (preferenceTaskCount == 1)
                  {
                    startupLogMessage.append(" updating 1 preference");
                  }
                  else if (preferenceTaskCount > 1)
                  {
                    startupLogMessage.append(" updating ").append(preferenceTaskCount).append(" preferences");
                  }

                  startupLogMessage.append(". See '").append(SetupContext.SETUP_LOG_URI.toFileString()).append("' for details");

                  SetupUIPlugin.INSTANCE.log(startupLogMessage.toString(), IStatus.INFO);
                }

                if (restart)
                {
                  progressLog.message("A restart is needed for the following reasons:", false, Severity.INFO);
                  for (String reason : restartReasons)
                  {
                    progressLog.message("  - " + reason);
                  }

                  wizard.setFinishAction(new Runnable()
                  {
                    public void run()
                    {
                      progressLog.done();

                      UIUtil.asyncExec(new Runnable()
                      {
                        public void run()
                        {
                          // Also include any triggered task whose implementation is currently unavailable.
                          // Such tasks will not be needed by are likely needed after the restart when their implementations have been installed.
                          SetupTaskPerformer performer = getPerformer();
                          EList<SetupTask> remainingTasks = new BasicEList<SetupTask>(performer.getNeededTasks());
                          for (SetupTask setupTask : performer.getTriggeredSetupTasks())
                          {
                            if (setupTask instanceof DynamicSetupTaskImpl)
                            {
                              remainingTasks.add(setupTask);
                            }
                          }

                          SetupUIPlugin.restart(trigger, remainingTasks);
                        }
                      });
                    }
                  });

                  if (success && launchAutomatically)
                  {
                    wizard.performFinish();
                    progressLog.setTerminated();
                    return Status.OK_STATUS;
                  }

                  progressLog.message("Press Finish to restart now or Cancel to restart later.", Severity.INFO);
                  disableCancelButton.set(false);
                }
                else
                {
                  if (success && dismissAutomatically)
                  {
                    wizard.setFinishAction(new Runnable()
                    {
                      public void run()
                      {
                        IWizardContainer container = getContainer();
                        if (container instanceof WizardDialog)
                        {
                          WizardDialog dialog = (WizardDialog)container;
                          progressLog.done();
                          progressLog.setTerminated();
                          dialog.close();
                        }
                      }
                    });

                    wizard.performFinish();
                    return Status.OK_STATUS;
                  }

                  if (success)
                  {
                    progressLog.message("Press Finish to close the dialog.", Severity.INFO);

                    if (launchButton != null && !hasLaunched && trigger == Trigger.BOOTSTRAP)
                    {
                      wizard.setFinishAction(new Runnable()
                      {
                        public void run()
                        {
                          if (launchAutomatically)
                          {
                            try
                            {
                              hasLaunched = launchProduct(getPerformer());
                            }
                            catch (Exception ex)
                            {
                              SetupUIPlugin.INSTANCE.log(ex);
                            }
                          }
                        }
                      });
                    }
                  }
                  else
                  {
                    if (progressLog.isCanceled())
                    {
                      progressLog.message("Task execution was canceled.", Severity.WARNING);
                    }
                    else
                    {
                      progressLog.message("There are failed tasks.", Severity.ERROR);
                    }

                    progressLog.message("Press Back to choose different settings or Cancel to abort.", Severity.INFO);
                  }
                }

                IOUtil.close(getPerformer().getLogStream());

                final boolean finalSuccess = success;
                UIUtil.syncExec(new Runnable()
                {
                  public void run()
                  {
                    progressLog.done();
                    setPageComplete(finalSuccess);
                    setButtonState(IDialogConstants.BACK_ID, true);

                    if (finalSuccess)
                    {
                      if (restart)
                      {
                        setMessage("Task execution has successfully completed but requires a restart.  Press Finish to restart now or Cancel to restart later.",
                            IMessageProvider.WARNING);
                        setButtonState(IDialogConstants.CANCEL_ID, true);

                        shell.setData(PROGRESS_STATUS, new Status(IStatus.WARNING, SetupEditPlugin.INSTANCE.getSymbolicName(),
                            "Task execution has successfully completed but requires a restart"));
                      }
                      else
                      {
                        setMessage("Task execution has successfully completed.  Press Back to choose different settings or Finish to exit.");
                        if (disableCancelButton.get())
                        {
                          setButtonState(IDialogConstants.CANCEL_ID, false);
                        }

                        shell.setData(PROGRESS_STATUS,
                            new Status(IStatus.OK, SetupEditPlugin.INSTANCE.getSymbolicName(), "Task execution has successfully completed"));
                      }
                    }
                    else
                    {
                      setButtonState(IDialogConstants.CANCEL_ID, true);
                      if (progressLog.isCanceled())
                      {
                        setErrorMessage("Task execution was canceled.  Press Back to choose different settings or Cancel to abort.");

                        shell.setData(PROGRESS_STATUS, new Status(IStatus.CANCEL, SetupEditPlugin.INSTANCE.getSymbolicName(), "Task execution was canceled."));
                      }
                      else
                      {
                        setErrorMessage("There are failed tasks.  Press Back to choose different settings or Cancel to abort.");

                        shell.setData(PROGRESS_STATUS, new Status(IStatus.ERROR, SetupEditPlugin.INSTANCE.getSymbolicName(), "Task execution was failed."));
                      }
                    }
                  }
                });
              }

              progressLog.setTerminated();
              return Status.OK_STATUS;
            }

            @Override
            public boolean belongsTo(Object family)
            {
              return family == PROGRESS_FAMILY;
            }
          };

          job.schedule();
        }
      };

      UIUtil.asyncExec(jobRunnable);
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
      ErrorDialog.open(ex);
    }

  }

  private void saveLocalFiles(SetupTaskPerformer performer)
  {
    User performerUser = performer.getUser();
    ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    User user = SetupContext.createUserOnly(resourceSet).getUser();

    boolean shouldSave = user.getAcceptedLicenses().addAll(performerUser.getAcceptedLicenses());
    UnsignedPolicy userUnsignedPolicy = user.getUnsignedPolicy();
    UnsignedPolicy performerUserUnsignedPolicy = performerUser.getUnsignedPolicy();
    if (userUnsignedPolicy != performerUserUnsignedPolicy)
    {
      user.setUnsignedPolicy(performerUserUnsignedPolicy);
      shouldSave = true;
    }

    if (shouldSave)
    {
      BaseUtil.saveEObject(user);
    }

    SetupContext setupContext = getWizard().getSetupContext();

    Installation installation = setupContext.getInstallation();
    BaseUtil.saveEObject(installation);

    Workspace workspace = setupContext.getWorkspace();
    if (workspace != null)
    {
      BaseUtil.saveEObject(workspace);
    }

    SetupContext.associate(installation, workspace);
  }

  public static boolean launchProduct(SetupTaskPerformer performer) throws Exception
  {
    OS os = performer.getOS();
    if (os.isCurrentOS())
    {
      performer.log("Launching the installed product...");
      ExecutableInfo info = performer.getExecutableInfo();

      List<String> command = new ArrayList<String>();
      File executable = info.getExecutable();
      command.add(executable.toString());

      File ws = performer.getWorkspaceLocation();
      if (ws != null)
      {
        SetupUIPlugin.initialStart(ws, performer.isOffline(), performer.isMirrors());

        command.add("-data");
        command.add(ws.toString());
      }

      command.add("-vmargs");
      command.add("-Duser.dir=" + info.getEclipseLocation());

      try
      {
        os.execute(command, info.needsConsole());
      }
      catch (Exception ex)
      {
        performer.log("Launching the installed product failed.");
        performer.log(ex);
        return false;
      }

      return true;
    }

    performer.log("Launching the installed product is not possible for cross-platform installs. Skipping.");
    return false;
  }

  /**
   * @author Eike Stepper
   */
  private class ProgressPageLog implements ProgressLog, IProgressMonitor
  {
    /**
     * A list that contains instances of String and/or Pair<String, ProgressLog.Severity>.
     */
    private List<Object> queue;

    private final ProgressMonitorPart progressMonitorPart;

    private IProgressMonitor progressMonitor;

    private boolean canceled;

    private boolean terminating;

    private boolean done;

    public ProgressPageLog(ProgressMonitorPart progressMonitorPart)
    {
      this.progressMonitorPart = progressMonitorPart;
      progressMonitorPart.attachToCancelComponent(null);
      progressMonitorPart.setCanceled(false);
      progressMonitorPart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      progressMonitorPart.getParent().layout();
    }

    public void setProgressMonitor(IProgressMonitor progressMonitor)
    {
      this.progressMonitor = progressMonitor;
    }

    public void setTerminating()
    {
      terminating = true;
    }

    public void setTerminated()
    {
      terminating = false;
      done = true;
    }

    public boolean isCanceled()
    {
      if (!canceled)
      {
        if (progressMonitorPart.isCanceled() || progressMonitor != null && progressMonitor.isCanceled())
        {
          canceled = true;
        }
      }

      return canceled;
    }

    public boolean isDone()
    {
      return done;
    }

    public void done()
    {
      done = true;
      progressMonitorPart.done();

      if (progressMonitor != null)
      {
        progressMonitor.done();
      }
    }

    public void beginTask(final String name, final int totalWork)
    {
      progressMonitorPart.beginTask(name, totalWork);
      logTaskName(name);

      if (progressMonitor != null)
      {
        progressMonitor.beginTask(name, totalWork);
      }
    }

    public void internalWorked(final double work)
    {
      progressMonitorPart.internalWorked(work);
      if (progressMonitor != null)
      {
        progressMonitor.internalWorked(work);
      }
    }

    public void setCanceled(boolean canceled)
    {
      this.canceled = canceled;
      if (progressMonitor != null)
      {
        progressMonitor.setCanceled(canceled);
      }
    }

    public void setTaskName(String name)
    {
      logTaskName(name);

      if (progressMonitor != null)
      {
        progressMonitor.setTaskName(name);
      }
    }

    public void subTask(String name)
    {
      logTaskName(name);

      if (progressMonitor != null)
      {
        progressMonitor.subTask(name);
      }
    }

    public void worked(final int work)
    {
      progressMonitorPart.worked(work);
      if (progressMonitor != null)
      {
        progressMonitor.internalWorked(work);
      }
    }

    public void task(final SetupTask setupTask)
    {
      UIUtil.syncExec(new Runnable()
      {
        public void run()
        {
          SetupTask previousCurrentTask = currentTask;
          currentTask = setupTask;

          int offset = 0;
          if (previousCurrentTask != null)
          {
            Point previousTextSelection = setupTaskSelections.get(previousCurrentTask);
            offset = logText.getCharCount() - 1;
            int start = previousTextSelection.x;
            setupTaskSelections.put(previousCurrentTask, new Point(start, offset));
            treeViewer.refresh(previousCurrentTask, true);
          }

          if (setupTask != null)
          {
            setupTaskSelections.put(setupTask, new Point(offset, -1));
            treeViewer.refresh(setupTask, true);
            treeViewer.removeSelectionChangedListener(treeViewerSelectionChangedListener);
            treeViewer.setSelection(new StructuredSelection(setupTask), true);
            treeViewer.addSelectionChangedListener(treeViewerSelectionChangedListener);
          }
        }
      });
    }

    public void log(String line)
    {
      log(line, true);
    }

    public void log(IStatus status)
    {
      String string = SetupUIPlugin.toString(status);
      log(string, false, Severity.fromStatus(status));
    }

    public void log(Throwable t)
    {
      String string = SetupUIPlugin.toString(t);
      log(string, false, Severity.ERROR);
    }

    public void log(String line, Severity severity)
    {
      log(line, true, severity);
    }

    public void log(String line, boolean filter)
    {
      log(line, filter, Severity.OK);
    }

    public void log(String line, boolean filter, Severity severity)
    {
      if (done && !terminating)
      {
        return;
      }

      message(line, filter, severity);
    }

    public void message(String line)
    {
      message(line, true, Severity.OK);
    }

    public void message(String line, Severity severity)
    {
      message(line, true, severity);
    }

    private void message(String line, boolean filter, Severity severity)
    {
      if (!terminating && isCanceled())
      {
        throw new OperationCanceledException();
      }

      if (filter)
      {
        line = logFilter.filter(line);
      }

      if (line == null)
      {
        return;
      }

      boolean wasEmpty = enqueue(line, severity);
      if (wasEmpty)
      {
        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            List<Object> lines = dequeue();
            appendText(lines);
          }
        });
      }
    }

    private synchronized boolean enqueue(String line, Severity severity)
    {
      boolean wasEmpty = queue == null;
      if (wasEmpty)
      {
        queue = new ArrayList<Object>();
      }

      if (severity == Severity.OK)
      {
        queue.add(line);
      }
      else
      {
        queue.add(Pair.create(line, severity));
      }

      return wasEmpty;
    }

    private synchronized List<Object> dequeue()
    {
      List<Object> result = queue;
      queue = null;
      return result;
    }

    private void appendText(List<Object> lines)
    {
      if (logText.isDisposed())
      {
        return;
      }

      for (Object value : lines)
      {
        String line;
        Severity severity;
        if (value instanceof String)
        {
          line = (String)value;
          severity = Severity.OK;
        }
        else
        {
          @SuppressWarnings("unchecked")
          Pair<String, Severity> pair = (Pair<String, Severity>)value;

          line = pair.getElement1();
          severity = pair.getElement2();
        }

        line += "\n";

        try
        {
          int length = logDocument.getLength();
          logDocument.replace(length, 0, line);

          int colorID;
          switch (severity)
          {
            case INFO:
              colorID = SWT.COLOR_BLUE;
              break;

            case WARNING:
              colorID = SWT.COLOR_DARK_YELLOW;
              break;

            case ERROR:
              colorID = SWT.COLOR_RED;
              break;

            default:
              colorID = SWT.COLOR_BLACK;
              break;

          }

          if (colorID != SWT.COLOR_BLACK)
          {
            Color color = logText.getDisplay().getSystemColor(colorID);
            StyleRange styleRange = new StyleRange(length, line.length(), color, null);
            if (severity == Severity.INFO)
            {
              styleRange.fontStyle = SWT.BOLD;
            }

            logText.setStyleRange(styleRange);
          }

          if (!scrollLock)
          {
            int lineCount = logText.getLineCount();
            logText.setTopIndex(lineCount - 1);
          }
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    }

    private void logTaskName(String name)
    {
      SetupTaskPerformer performer = getPerformer();
      if (performer != null)
      {
        performer.log(name);
      }
    }
  }
}

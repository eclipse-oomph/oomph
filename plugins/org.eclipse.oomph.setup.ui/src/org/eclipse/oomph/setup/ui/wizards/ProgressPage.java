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

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogFilter;
import org.eclipse.oomph.setup.log.ProgressLogProvider;
import org.eclipse.oomph.setup.log.ProgressLogRunnable;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.setup.util.OS;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.progress.ProgressManager;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
  private static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");

  private static final String JOB_NAME = "Setting up IDE";

  private final ProgressLogFilter logFilter = new ProgressLogFilter();

  private final ScrollBarListener scrollBarListener = new ScrollBarListener();

  private final Map<SetupTask, Point> setupTaskSelections = new HashMap<SetupTask, Point>();

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
              // Force the first line to be scrolled into view
              logText.setSelection(textSelection.x, textSelection.x);

              // Treat -1 so that at selects to the very end.
              int end = textSelection.y;
              if (end == -1)
              {
                end = logText.getCharCount();
              }

              // Determine the number of lines of text to be selected
              String selectedText = logText.getText(textSelection.x, end);
              int lineFeedCount = 0;
              int carriageReturnCount = 0;
              for (int i = 0, length = selectedText.length(); i < length; ++i)
              {
                char c = selectedText.charAt(i);
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
                logText.setSelection(end, textSelection.x);
              }
              else
              {
                logText.setSelection(textSelection.x, end);
              }
            }
          }
        }
      }
    }
  };

  private TreeViewer treeViewer;

  private Text logText;

  private SetupTask currentTask;

  private ProgressPageLog progressPageLog;

  private boolean dismissAutomatically;

  private boolean launchAutomatically = true;

  private Button dismissButton;

  private Button launchButton;

  public ProgressPage()
  {
    super("ProgressPage");
    setTitle("Progress");
    setDescription("Wait for the setup to complete or press Back to cancel and make changes.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);

    GridLayout mainLayout = new GridLayout();
    mainLayout.marginHeight = 0;
    mainLayout.marginBottom = 5;
    mainComposite.setLayout(mainLayout);

    SashForm sashForm = new SashForm(mainComposite, SWT.VERTICAL);
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

    treeViewer = new TreeViewer(sashForm, SWT.BORDER);
    Tree tree = treeViewer.getTree();

    ILabelProvider labelProvider = createLabelProvider();
    treeViewer.setLabelProvider(labelProvider);

    final AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(getAdapterFactory());
    treeViewer.setContentProvider(contentProvider);
    treeViewer.addSelectionChangedListener(treeViewerSelectionChangedListener);

    new ColumnViewerInformationControlToolTipSupport(treeViewer, new LocationListener()
    {
      public void changing(LocationEvent event)
      {
      }

      public void changed(LocationEvent event)
      {
      }
    });

    tree.setLayoutData(new GridData(GridData.FILL_BOTH));
    tree.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

    logText = new Text(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
    logText.setBackground(logText.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    logText.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
    logText.setEditable(false);
    logText.setLayoutData(new GridData(GridData.FILL_BOTH));
    logText.getVerticalBar().addSelectionListener(scrollBarListener);

    GridLayout buttonLayout = new GridLayout(2, false);
    buttonLayout.marginHeight = 0;
    buttonLayout.marginWidth = 0;

    Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
    buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    buttonComposite.setLayout(buttonLayout);

    dismissButton = new Button(buttonComposite, SWT.CHECK);
    dismissButton.setText("Dismiss automatically");
    dismissButton.setToolTipText("Dismiss this wizard when all setup tasks have performed successfully");
    dismissButton.setLayoutData(new GridData());
    dismissButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        dismissAutomatically = dismissButton.getSelection();
      }
    });

    if (getTrigger() == Trigger.BOOTSTRAP)
    {
      launchButton = new Button(buttonComposite, SWT.CHECK);
      launchButton.setText("Launch automatically");
      launchButton.setToolTipText("Launch the installed product when all setup tasks have performed successfully");
      launchButton.setLayoutData(new GridData());
      launchButton.setSelection(true);
      launchButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          launchAutomatically = launchButton.getSelection();
        }
      });
    }

    return mainComposite;
  }

  @Override
  public void enterPage(boolean forward)
  {
    if (forward)
    {
      progressPageLog = new ProgressPageLog();
      logText.setText("");

      SetupTaskPerformer.setProgress(progressPageLog);

      final SetupTaskPerformer performer = getPerformer();
      treeViewer.setInput(new ItemProvider(performer.getNeededTasks()));

      run(new ProgressLogRunnable()
      {
        public Set<String> run(ProgressLog log) throws Exception
        {
          ProgressManager oldProgressProvider = ProgressManager.getInstance();
          ProgressLogProvider newProgressLogProvider = new ProgressLogProvider(progressPageLog, oldProgressProvider);

          IJobManager jobManager = Job.getJobManager();
          jobManager.setProgressProvider(newProgressLogProvider);

          try
          {
            performer.perform();
            return performer.getRestartReasons();
          }
          finally
          {
            jobManager.setProgressProvider(oldProgressProvider);
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
      SetupTaskPerformer.setProgress(null);
      setPageComplete(false);
    }
    else
    {
      performCancel();
      setPageComplete(false);
      setCancelState(true);
    }
  }

  @Override
  public void performCancel()
  {
    if (progressPageLog != null)
    {
      progressPageLog.cancel();
      progressPageLog = null;
    }
  }

  private void appendText(String string)
  {
    Rectangle clientArea = logText.getClientArea();
    int visibleLines = clientArea.height / logText.getLineHeight();
    int topVisibleLine = logText.getLineCount() - visibleLines;
    int topIndex = logText.getTopIndex();
    int delta = topVisibleLine - topIndex;
    if (delta <= 2)
    {
      logText.append(string);
    }
    else
    {
      logText.setRedraw(false);
      logText.setText(logText.getText().trim() + logText.getLineDelimiter() + string);
      logText.setTopIndex(topIndex);
      logText.setRedraw(true);
    }
  }

  private ILabelProvider createLabelProvider()
  {
    return new ToolTipLabelProvider(getAdapterFactory())
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

  private void run(final ProgressLogRunnable runnable)
  {
    try
    {
      // Remember and use the progressPageLog that is valid at this point in time.
      final ProgressLog progressLog = progressPageLog;

      Runnable jobRunnable = new Runnable()
      {
        public void run()
        {
          final Job job = new Job(JOB_NAME)
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              long start = System.currentTimeMillis();
              boolean success = false;
              Set<String> restartReasons = null;

              try
              {
                progressLog.log(JOB_NAME);

                restartReasons = runnable.run(progressLog);

                SetupTaskPerformer performer = getPerformer();
                saveLocalFiles(performer);

                if (getTrigger() == Trigger.BOOTSTRAP && launchAutomatically)
                {
                  launchProduct(performer);
                }

                success = true;
              }
              catch (OperationCanceledException ex)
              {
                // Do nothing
              }
              catch (Throwable ex)
              {
                SetupUIPlugin.INSTANCE.log(ex);
                progressLog.log(ex);
              }
              finally
              {
                long seconds = (System.currentTimeMillis() - start) / 1000;
                progressLog.log("Took " + seconds + " seconds.");

                final AtomicBoolean disableCancelButton = new AtomicBoolean(true);
                SetupWizard wizard = getWizard();

                if (!(restartReasons == null || restartReasons.isEmpty()) && SetupUIPlugin.SETUP_IDE)
                {
                  progressLog.log("A restart is needed for the following reasons:");
                  for (String reason : restartReasons)
                  {
                    progressLog.log("  - " + reason);
                  }

                  wizard.setFinishAction(new Runnable()
                  {
                    public void run()
                    {
                      PlatformUI.getWorkbench().restart();
                    }
                  });

                  if (success && dismissAutomatically)
                  {
                    wizard.performFinish();
                    return Status.OK_STATUS;
                  }

                  progressLog.log("Press Finish to restart now or Cancel to restart later...");
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
                          dialog.close();
                        }
                      }
                    });

                    wizard.performFinish();
                    return Status.OK_STATUS;
                  }

                  progressLog.log("Press Finish to close the dialog...");
                }

                UIUtil.asyncExec(new Runnable()
                {
                  public void run()
                  {
                    progressLog.task(null);
                    setPageComplete(true);

                    if (disableCancelButton.get())
                    {
                      setCancelState(false);
                    }
                  }
                });
              }

              return Status.OK_STATUS;
            }
          };

          UIUtil.asyncExec(new Runnable()
          {
            public void run()
            {
              job.schedule();
            }
          });
        }
      };

      // jobRunnable.run();
      UIUtil.asyncExec(jobRunnable);
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
      ErrorDialog.open(ex);
    }
  }

  private void launchProduct(SetupTaskPerformer performer) throws Exception
  {
    OS os = performer.getOS();
    if (os.isCurrent())
    {
      performer.log("Launching the installed product...");

      String eclipseDir = os.getEclipseDir();
      String eclipseExecutable = os.getEclipseExecutable();
      String eclipsePath = new File(performer.getInstallationLocation(), eclipseDir + "/" + eclipseExecutable).getAbsolutePath();

      List<String> command = new ArrayList<String>();
      command.add(eclipsePath);

      File ws = performer.getWorkspaceLocation();
      if (ws != null)
      {
        command.add("-data");
        command.add("\"" + ws + "\"");
      }

      command.add("-vmargs");
      command.add("-D" + SetupProperties.PROP_SETUP_CONFIRM_SKIP + "=true");

      if (performer.isOffline())
      {
        command.add("-D" + SetupProperties.PROP_SETUP_OFFLINE_STARTUP + "=true");
      }

      ProcessBuilder builder = new ProcessBuilder(command);
      builder.start();
    }
    else
    {
      performer.log("Launching the installed product is not possible for cross-platform installs. Skipping.");
    }
  }

  private void saveLocalFiles(SetupTaskPerformer performer)
  {
    Installation installation = getInstallation();
    Resource installationResource = installation.eResource();
    URI installationResourceURI = installationResource.getURI();
    installationResource.setURI(URI.createFileURI(new File(performer.getProductLocation(), "configuration/org.eclipse.oomph.setup/installation.setup")
        .toString()));

    Workspace workspace = getWorkspace();
    Resource workspaceResource = null;
    URI workspaceResourceURI = null;
    if (workspace != null)
    {
      workspaceResource = workspace.eResource();
      workspaceResourceURI = workspaceResource.getURI();
      workspaceResource.setURI(URI.createFileURI(new File(performer.getWorkspaceLocation(), ".metadata/.plugins/org.eclipse.oomph.setup/workspace.setup")
          .toString()));
    }

    User user = getUser();
    user.getAcceptedLicenses().addAll(performer.getUser().getAcceptedLicenses());

    EMFUtil.saveEObject(installation);
    if (workspace != null)
    {
      EMFUtil.saveEObject(workspace);
    }

    EMFUtil.saveEObject(user);

    installationResource.setURI(installationResourceURI);
    if (workspaceResource != null)
    {
      workspaceResource.setURI(workspaceResourceURI);
    }
  }

  private void setCancelState(boolean enabled)
  {
    try
    {
      IWizardContainer container = getContainer();
      Method method = ReflectUtil.getMethod(container.getClass(), "getButton", int.class);
      method.setAccessible(true);
      Button cancelButton = (Button)method.invoke(container, IDialogConstants.CANCEL_ID);
      cancelButton.setEnabled(enabled);

      dismissButton.setEnabled(enabled);
      if (launchButton != null)
      {
        launchButton.setEnabled(enabled);
      }
    }
    catch (Throwable ex)
    {
      // Ignore
    }
  }

  /**
   * @author Eike Stepper
   */
  protected class ScrollBarListener extends SelectionAdapter
  {
    private boolean isDragging;

    private String pendingLines;

    @Override
    public void widgetSelected(SelectionEvent event)
    {
      if (event.detail == SWT.NONE)
      {
        isDragging = false;
        if (pendingLines != null)
        {
          ProgressPage.this.appendText(pendingLines);
          pendingLines = null;
        }
      }
      else if (event.detail == SWT.DRAG)
      {
        isDragging = true;
      }
    }

    public boolean isDragging()
    {
      return isDragging;
    }

    public void appendText(String line)
    {
      if (pendingLines == null)
      {
        pendingLines = line;
      }
      else
      {
        pendingLines = pendingLines.trim() + logText.getLineDelimiter() + line;
      }
    }
  }

  private class ProgressPageLog implements ProgressLog
  {
    private boolean canceled;

    public boolean isCanceled()
    {
      return canceled;
    }

    public void cancel()
    {
      canceled = true;
    }

    public void log(String line)
    {
      log(line, true);
    }

    public void log(String line, boolean filter)
    {
      if (isCanceled())
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

      final String message = "[" + TIME.format(new Date()) + "] " + line + "\n";
      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          if (!getWizard().canFinish())
          {
            if (scrollBarListener.isDragging())
            {
              scrollBarListener.appendText(message);
            }
            else
            {
              appendText(message);
            }
          }
        }
      });
    }

    public void log(IStatus status)
    {
      String string = SetupUIPlugin.toString(status);
      log(string, false);
    }

    public void log(Throwable t)
    {
      String string = SetupUIPlugin.toString(t);
      log(string, false);
    }

    public void task(final SetupTask setupTask)
    {
      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          SetupTask previousCurrentTask = currentTask;
          currentTask = setupTask;

          int offset = 0;
          if (previousCurrentTask != null)
          {
            Point previousTextSelection = setupTaskSelections.get(previousCurrentTask);
            offset = logText.getCharCount();
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

  }
}

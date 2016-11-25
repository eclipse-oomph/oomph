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

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.ui.ButtonBar;
import org.eclipse.oomph.ui.HelpSupport.HelpProvider;
import org.eclipse.oomph.ui.OomphWizardDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public abstract class SetupWizardPage extends WizardPage implements HelpProvider
{
  public SetupWizardPage(String pageName)
  {
    super(pageName);
    setPageComplete(false);
  }

  @Override
  public SetupWizard getWizard()
  {
    return (SetupWizard)super.getWizard();
  }

  @Override
  protected IDialogSettings getDialogSettings()
  {
    IDialogSettings settings = super.getDialogSettings();
    String sectionName = getName();
    return UIUtil.getOrCreateSection(settings, sectionName);
  }

  public ComposedAdapterFactory getAdapterFactory()
  {
    return getWizard().getAdapterFactory();
  }

  public String getHelpPath()
  {
    String id = "Doc" + getClass().getSimpleName();
    return SetupWizard.HELP_FOLDER + id + ".html#" + id + "_1_table";
  }

  public ResourceSet getResourceSet()
  {
    return getWizard().getResourceSet();
  }

  public CatalogManager getCatalogManager()
  {
    return getWizard().getCatalogManager();
  }

  public Trigger getTrigger()
  {
    return getWizard().getTrigger();
  }

  public String getTriggerName()
  {
    return getWizard().getTriggerName();
  }

  public Installation getInstallation()
  {
    return getWizard().getInstallation();
  }

  public Workspace getWorkspace()
  {
    return getWizard().getWorkspace();
  }

  public User getUser()
  {
    return getWizard().getUser();
  }

  public SetupTaskPerformer getPerformer()
  {
    return getWizard().getPerformer();
  }

  public void setPerformer(SetupTaskPerformer performer)
  {
    getWizard().setPerformer(performer);
  }

  protected final SetupTaskPerformer createPerformer(SetupPrompter prompter, boolean fullPrompt) throws Exception
  {
    User originalUser = getUser();
    Installation originalInstallation = getInstallation();
    Workspace originalWorkspace = getWorkspace();

    Copier copier = new EcoreUtil.Copier();
    User user = (User)copier.copy(originalUser);
    Installation installation = (Installation)copier.copy(originalInstallation);
    Workspace workspace = (Workspace)copier.copy(originalWorkspace);
    copier.copyReferences();

    createResource(originalUser, user);
    createResource(originalInstallation, installation);
    createResource(originalWorkspace, workspace);

    SetupContext context = SetupContext.create(installation, workspace, user);

    URIConverter uriConverter = getResourceSet().getURIConverter();
    Trigger trigger = getTrigger();
    return SetupTaskPerformer.create(uriConverter, prompter, trigger, context, fullPrompt);
  }

  private void createResource(EObject originalEObject, EObject copiedEObject)
  {
    if (originalEObject != null)
    {
      URI uri = originalEObject.eResource().getURI();
      Resource resource = SetupCoreUtil.RESOURCE_FACTORY_REGISTRY.getFactory(uri).createResource(uri);
      resource.getContents().add(copiedEObject);
    }
  }

  public WizardFinisher getWizardFinisher()
  {
    return null;
  }

  public boolean performCancel()
  {
    return true;
  }

  public void sendStats(boolean success)
  {
  }

  protected void handleInactivity(Display display, boolean inactive)
  {
  }

  public void enterPage(boolean forward)
  {
  }

  public void leavePage(boolean forward)
  {
  }

  public final void gotoNextPage()
  {
    IWizardPage page = getNextPage();
    gotoPage("nextPressed", page);
  }

  public final void gotoPreviousPage()
  {
    // Going back via WizardDialog.showPage() does not work because it screws up the previousPage of the current page!
    // See how isMovingToPreviousPage is (not!) used there...
    IWizardPage page = getPreviousPage();
    gotoPage("backPressed", page);
  }

  private void gotoPage(String methodName, IWizardPage page)
  {
    IWizardContainer container = getContainer();
    if (container instanceof WizardDialog)
    {
      try
      {
        ReflectUtil.invokeMethod(methodName, container);
        return;
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }
    }

    container.showPage(page);
  }

  public final void createControl(Composite parent)
  {
    GridLayout gridLayout = UIUtil.createGridLayout(1);
    gridLayout.marginWidth = 5;

    Composite pageControl = new Composite(parent, SWT.NONE);
    pageControl.setLayout(gridLayout);
    super.setControl(pageControl);
    setPageComplete(false);

    Composite uiContainer = new Composite(pageControl, SWT.NONE);
    uiContainer.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    uiContainer.setLayout(UIUtil.createGridLayout(1));

    Point sizeHint = getSizeHint();

    GridData layoutData = new GridData(GridData.FILL_BOTH);
    layoutData.widthHint = sizeHint.x;
    layoutData.heightHint = sizeHint.y;

    Control ui = createUI(uiContainer);
    ui.setLayoutData(layoutData);

    ButtonBar buttonBar = new ButtonBar(pageControl)
    {
      @Override
      protected IDialogSettings getDialogSettings()
      {
        return SetupWizardPage.this.getDialogSettings();
      }
    };

    createCheckButtons(buttonBar);
    createFooter(pageControl);
    parent.layout(true, true);
  }

  protected void createFooter(Composite parent)
  {
  }

  protected void createCheckButtons(ButtonBar buttonBar)
  {
  }

  @Override
  protected final void setControl(Control newControl)
  {
    throw new UnsupportedOperationException();
  }

  protected Point getSizeHint()
  {
    return new Point(800, 500);
  }

  protected abstract Control createUI(Composite parent);

  protected final void addHelpCallout(Control control, int number)
  {
    IWizardContainer container = getContainer();
    if (container instanceof OomphWizardDialog)
    {
      OomphWizardDialog dialog = (OomphWizardDialog)container;
      dialog.getHelpSupport().addHelpCallout(control, number);
    }
  }

  protected void setButtonState(int buttonID, boolean enabled)
  {
    Button button = getButton(buttonID);
    if (button != null)
    {
      button.setEnabled(enabled);
    }
  }

  protected Button getButton(int buttonID)
  {
    try
    {
      IWizardContainer container = getContainer();
      Method method = ReflectUtil.getMethod(container.getClass(), "getButton", int.class);
      method.setAccessible(true);
      Button button = (Button)method.invoke(container, buttonID);
      return button;
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface WizardFinisher
  {
    public boolean performFinish();
  }

  /**
   * @author Ed Merks
   */
  protected abstract class PerformerCreationJob extends Job
  {
    private SetupTaskPerformer performer;

    private Throwable throwable;

    private long start;

    private long interval;

    private long delay = 5000;

    public PerformerCreationJob(String name)
    {
      super(name);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
      try
      {
        SetupTaskPerformer.setCreationMonitor(monitor);
        performer = createPerformer();
      }
      catch (Throwable throwable)
      {
        this.throwable = throwable;
      }
      finally
      {
        SetupTaskPerformer.setCreationMonitor(null);
      }

      return Status.OK_STATUS;
    }

    protected abstract SetupTaskPerformer createPerformer() throws Exception;

    protected abstract Dialog createDialog();

    protected abstract void handleDialogResult(int result);

    protected void heartBeat()
    {

    }

    public long getStart()
    {
      return start;
    }

    public long getDelay()
    {
      return delay;
    }

    public void setDelay(long delay)
    {
      interval = System.currentTimeMillis();
      this.delay = delay;
    }

    public SetupTaskPerformer getPerformer()
    {
      return performer;
    }

    public Throwable getThrowable()
    {
      return throwable;
    }

    public void create()
    {
      final Button button = getButton(IDialogConstants.NEXT_ID);
      final String originalText = button.getText();
      final String[] animationText = new String[] { originalText };

      schedule();

      start = System.currentTimeMillis();
      interval = start;
      long nextAnimation = start + 500;

      Shell shell = getShell();
      final Display display = shell.getDisplay();
      while (getState() != Job.NONE)
      {
        if (!display.readAndDispatch())
        {
          display.sleep();
        }

        if (button.isDisposed())
        {
          return;
        }

        long now = System.currentTimeMillis();
        if (now > nextAnimation)
        {
          nextAnimation = now + 500;
          button.setText(animationText[0] = getNextAnimationText(originalText, animationText[0]));
          heartBeat();
        }

        if (now - interval > delay)
        {
          final Dialog dialog = createDialog();

          final AtomicBoolean closedAfterJobCompletion = new AtomicBoolean();
          Runnable livenessChecker = new Runnable()
          {
            public void run()
            {
              if (getState() == Job.NONE)
              {
                closedAfterJobCompletion.set(true);
                dialog.close();
              }
              else
              {
                button.setText(animationText[0] = getNextAnimationText(originalText, animationText[0]));
                heartBeat();
                display.timerExec(1000, this);
              }
            }
          };

          display.asyncExec(livenessChecker);

          int result = dialog.open();
          if (!closedAfterJobCompletion.get())
          {
            handleDialogResult(result);
          }
        }

        if (shell.isDisposed())
        {
          return;
        }
      }

      button.setText(originalText);
    }

    private String getNextAnimationText(String originalText, String text)
    {
      if (text.length() > originalText.length() + 10)
      {
        return originalText;
      }

      return " " + text.substring(0, text.length() - 1) + " " + text.charAt(text.length() - 1);
    }

    protected Dialog createDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType,
        String[] dialogButtonLabels, int defaultIndex)
    {
      Dialog dialog = new MessageDialog(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex)
      {
        @Override
        protected int getShellStyle()
        {
          return super.getShellStyle() | SWT.SHEET;
        }
      };

      return dialog;
    }
  }
}

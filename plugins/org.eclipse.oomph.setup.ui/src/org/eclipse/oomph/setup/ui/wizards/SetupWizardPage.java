/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.ui.ButtonBar;
import org.eclipse.oomph.ui.HelpSupport.HelpProvider;
import org.eclipse.oomph.ui.OomphWizardDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.lang.reflect.Method;

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
    URI uri = originalUser.eResource().getURI();

    final User user = EcoreUtil.copy(originalUser);
    Resource userResource = Resource.Factory.Registry.INSTANCE.getFactory(uri).createResource(uri);
    userResource.getContents().add(user);

    Trigger trigger = getTrigger();
    Installation installation = getInstallation();
    Workspace workspace = getWorkspace();

    URIConverter uriConverter = getResourceSet().getURIConverter();
    SetupContext context = SetupContext.create(installation, workspace, user);

    return SetupTaskPerformer.create(uriConverter, prompter, trigger, context, fullPrompt);
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
    try
    {
      IWizardContainer container = getContainer();
      Method method = ReflectUtil.getMethod(container.getClass(), "getButton", int.class);
      method.setAccessible(true);
      Button button = (Button)method.invoke(container, buttonID);
      button.setEnabled(enabled);
    }
    catch (Throwable ex)
    {
      // Ignore
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface WizardFinisher
  {
    public boolean performFinish();
  }
}

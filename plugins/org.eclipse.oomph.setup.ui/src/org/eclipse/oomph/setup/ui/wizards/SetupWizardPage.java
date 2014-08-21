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

import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.ui.PersistentButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public abstract class SetupWizardPage extends WizardPage
{
  private static final String TOGGLE_COMMAND_PREFIX = "toggleCommand:";

  private Composite checkComposite;

  public SetupWizardPage(String pageName)
  {
    super(pageName);
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

  public void performCancel()
  {
  }

  public void enterPage(boolean forward)
  {
  }

  public void leavePage(boolean forward)
  {
  }

  public final void advanceToNextPage()
  {
    IWizardPage nextPage = getNextPage();
    getContainer().showPage(nextPage);
  }

  public final void createControl(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;

    Composite pageControl = new Composite(parent, SWT.NONE);
    pageControl.setLayout(layout);
    super.setControl(pageControl);
    setPageComplete(false);

    Point sizeHint = getSizeHint();

    GridData layoutData = new GridData(GridData.FILL_BOTH);
    layoutData.widthHint = sizeHint.x;
    layoutData.heightHint = sizeHint.y;

    final Control ui = createUI(pageControl);
    ui.setLayoutData(layoutData);

    createCheckButtons();
  }

  protected void createCheckButtons()
  {
  }

  protected final Button addCheckButton(String text, String toolTip, boolean defaultSelection, String persistenceKey)
  {
    if (checkComposite == null)
    {
      GridLayout checkLayout = new GridLayout();
      checkLayout.marginWidth = 5;
      checkLayout.marginHeight = 0;

      checkComposite = new Composite((Composite)getControl(), SWT.NONE);
      checkComposite.setLayout(checkLayout);
      checkComposite.setLayoutData(new GridData());
    }
    else
    {
      GridLayout checkLayout = (GridLayout)checkComposite.getLayout();
      ++checkLayout.numColumns;
    }

    Button button;
    if (persistenceKey != null)
    {
      boolean toggleCommand = false;
      if (persistenceKey.startsWith(TOGGLE_COMMAND_PREFIX))
      {
        persistenceKey = persistenceKey.substring(TOGGLE_COMMAND_PREFIX.length());
        toggleCommand = UIUtil.WORKBENCH != null;
      }

      PersistentButton.Persistence persistence;
      if (toggleCommand)
      {
        persistence = new PersistentButton.ToggleCommandPersistence(persistenceKey);
      }
      else
      {
        persistence = new PersistentButton.DialogSettingsPersistence(getDialogSettings(), persistenceKey);
      }

      button = new PersistentButton(checkComposite, SWT.CHECK, defaultSelection, persistence);
    }
    else
    {
      button = new Button(checkComposite, SWT.CHECK);
      button.setSelection(defaultSelection);
    }

    button.setLayoutData(new GridData());
    button.setText(text);
    if (!StringUtil.isEmpty(toolTip))
    {
      button.setToolTipText(toolTip);
    }

    return button;
  }

  protected final Composite getCheckComposite()
  {
    return checkComposite;
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
}

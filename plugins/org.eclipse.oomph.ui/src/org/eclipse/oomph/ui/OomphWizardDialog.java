/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.ui.HelpSupport.HelpProvider;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public class OomphWizardDialog extends WizardDialog implements HelpProvider
{
  private HelpSupport helpSupport;

  private IPageChangedListener pageChangedListener;

  public OomphWizardDialog(Shell parentShell, IWizard wizard, boolean helpAvailable)
  {
    super(parentShell, wizard);

    if (helpAvailable)
    {
      helpSupport = new HelpSupport(this)
      {
        @Override
        protected void handleInactivity(Display display, boolean inactive)
        {
          OomphWizardDialog.this.handleInactivity(display, inactive);
        }
      };

      pageChangedListener = new IPageChangedListener()
      {
        public void pageChanged(PageChangedEvent event)
        {
          if (helpSupport != null)
          {
            helpSupport.updateHelp();
          }
        }
      };

      addPageChangedListener(pageChangedListener);
    }
  }

  public final HelpSupport getHelpSupport()
  {
    return helpSupport;
  }

  public String getHelpPath()
  {
    IWizardPage page = getCurrentPage();
    if (page instanceof HelpProvider)
    {
      HelpProvider helpProvider = (HelpProvider)page;
      String helpPath = helpProvider.getHelpPath();
      if (helpPath != null)
      {
        return helpPath;
      }
    }

    return null;
  }

  @Override
  public boolean close()
  {
    if (helpSupport != null)
    {
      helpSupport.dispose();
      helpSupport = null;
    }

    return super.close();
  }

  @Override
  public void openTray(DialogTray tray) throws IllegalStateException, UnsupportedOperationException
  {
    super.openTray(tray);
    OomphDialog.hookTray(this);
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    ToolBar toolBar = (ToolBar)super.createHelpControl(parent);
    if (helpSupport != null)
    {
      ToolItem helpButton = toolBar.getItems()[0];
      AccessUtil.setKey(helpButton, "help");
      helpSupport.hook(helpButton);
    }

    createToolItemsForToolBar(toolBar);
    return toolBar;
  }

  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
  }

  @Override
  protected Control createContents(Composite parent)
  {
    final Control contents = super.createContents(parent);
    OomphDialog.fixTitleImageLayout(this);
    return contents;
  }

  protected void handleInactivity(Display display, boolean inactive)
  {
  }

  @Override
  public void setTitleImage(Image newTitleImage)
  {
    super.setTitleImage(newTitleImage);
    OomphDialog.fixTitleImageLayout(this);
  }

  public int openInBackground()
  {
    create();

    constrainShellSize();

    return getReturnCode();
  }
}

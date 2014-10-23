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

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.AbstractDialog;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.lang.reflect.Field;

/**
 * @author Eike Stepper
 */
public class SetupWizardDialog extends WizardDialog
{
  private static final String HELP_CONTEXT = "/help";

  private final IPageChangedListener pageChangedListener = new PageChangedListener();

  private HTTPServer helpServer;

  private Browser helpBrowser;

  public SetupWizardDialog(Shell parentShell, SetupWizard wizard)
  {
    super(parentShell, wizard);
    setHelpAvailable(true);
    addPageChangedListener(pageChangedListener);
  }

  @Override
  public boolean close()
  {
    if (helpServer != null)
    {
      try
      {
        helpServer.stop();
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }

      helpServer = null;
    }

    return super.close();
  }

  private synchronized String getHelpURL(String path)
  {
    if (helpServer == null)
    {
      try
      {
        helpServer = new HTTPServer();
        helpServer.addContext(new HTTPServer.PluginContext(HELP_CONTEXT, true));
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
        return null;
      }
    }

    return "http://localhost:" + helpServer.getPort() + HELP_CONTEXT + path;
  }

  private void setHelpPath(String path)
  {
    if (helpBrowser != null)
    {
      String url = getHelpURL(path);
      if (url != null)
      {
        if (!url.equals(helpBrowser.getUrl()))
        {
          helpBrowser.setUrl(url);
        }
      }
      else
      {
        helpBrowser.setText("<h3>Help content not found.</h3>");
      }
    }
  }

  private void setHelpPath(IWizardPage page)
  {
    if (page instanceof SetupWizardPage)
    {
      SetupWizardPage setupWizardPage = (SetupWizardPage)page;
      String helpPath = setupWizardPage.getHelpPath();
      if (helpPath != null)
      {
        setHelpPath(helpPath);
      }
    }
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    getShell().addHelpListener(new HelpListener()
    {
      public void helpRequested(HelpEvent e)
      {
        if (getTray() != null)
        {
          closeTray();
          updateHelpButton(false);
          return;
        }

        DialogTray tray = new DialogTray()
        {
          @Override
          protected Control createContents(Composite parent)
          {
            helpBrowser = new Browser(parent, SWT.NONE);
            helpBrowser.setSize(500, 800);
            helpBrowser.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                helpBrowser = null;
              }
            });

            setHelpPath(getCurrentPage());
            return helpBrowser;
          }
        };

        openTray(tray);
        updateHelpButton(true);
      }

      private void updateHelpButton(boolean pushed)
      {
        try
        {
          Field field = ReflectUtil.getField(TrayDialog.class, "fHelpButton");
          ToolItem fHelpButton = (ToolItem)ReflectUtil.getValue(field, SetupWizardDialog.this);
          fHelpButton.setSelection(pushed);
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    });

    ToolBar toolBar = (ToolBar)super.createHelpControl(parent);
    AccessUtil.setKey(toolBar.getItems()[0], "help");
    createToolItemsForToolBar(toolBar);
    return toolBar;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);

    AccessUtil.setKey(getButton(IDialogConstants.BACK_ID), "back");
    AccessUtil.setKey(getButton(IDialogConstants.NEXT_ID), "next");
    AccessUtil.setKey(getButton(IDialogConstants.FINISH_ID), "finish");
    AccessUtil.setKey(getButton(IDialogConstants.CANCEL_ID), "cancel");
  }

  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
  }

  @Override
  protected Control createContents(Composite parent)
  {
    Control contents = super.createContents(parent);
    AbstractDialog.fixTitleImageLayout(this);
    return contents;
  }

  @Override
  public void setTitleImage(Image newTitleImage)
  {
    super.setTitleImage(newTitleImage);
    AbstractDialog.fixTitleImageLayout(this);
  }

  /**
   * @author Eike Stepper
   */
  private final class PageChangedListener implements IPageChangedListener
  {
    public void pageChanged(PageChangedEvent event)
    {
      setHelpPath((IWizardPage)event.getSelectedPage());
    }
  }
}

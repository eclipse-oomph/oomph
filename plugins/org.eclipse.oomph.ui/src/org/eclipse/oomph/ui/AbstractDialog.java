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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * @author Eike Stepper
 */
public abstract class AbstractDialog extends TitleAreaDialog
{
  private String title;

  private int width;

  private int height;

  private AbstractOomphUIPlugin plugin;

  private String help;

  protected AbstractDialog(Shell parentShell, String title, int width, int height, AbstractOomphUIPlugin plugin, String help)
  {
    super(parentShell);
    this.title = title;
    this.width = width;
    this.height = height;
    this.plugin = plugin;
    this.help = help;

    setHelpAvailable(help != null);
    setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public String getTitle()
  {
    return title;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public String getHelp()
  {
    return help;
  }

  @Override
  public void openTray(DialogTray tray) throws IllegalStateException, UnsupportedOperationException
  {
    super.openTray(tray);

    final Control trayControl = getFieldValue("trayControl");
    final Label rightSeparator = getFieldValue("rightSeparator");
    final Sash sash = getFieldValue("sash");
    if (trayControl == null || rightSeparator == null || sash == null)
    {
      return;
    }

    final GridData data = (GridData)trayControl.getLayoutData();
    sash.addListener(SWT.Selection, new Listener()
    {
      public void handleEvent(Event event)
      {
        if (event.detail == SWT.DRAG)
        {
          Shell shell = getShell();
          Rectangle clientArea = shell.getClientArea();
          int newWidth = clientArea.width - event.x - (sash.getSize().x + rightSeparator.getSize().x);
          if (newWidth != data.widthHint)
          {
            data.widthHint = newWidth;
            shell.layout();
          }
        }
      }
    });
  }

  private <T> T getFieldValue(String name)
  {
    try
    {
      Field field = ReflectUtil.getField(TrayDialog.class, name);
      if (field != null)
      {
        @SuppressWarnings("unchecked")
        T value = (T)ReflectUtil.getValue(field, this);
        return value;
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Shell shell = getShell();
    shell.setText(getShellText());

    setTitle(title);
    setTitleImage(getDefaultImage(getImagePath()));
    setMessage(getDefaultMessage());

    Composite area = (Composite)super.createDialogArea(parent);

    GridLayout layout = new GridLayout();
    layout.marginWidth = getContainerMargin();
    layout.marginHeight = getContainerMargin();
    layout.verticalSpacing = 0;

    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(layout);
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    createUI(container);

    if (getContainerMargin() == 0)
    {
      createSeparator(container);
    }

    shell.addHelpListener(new HelpListener()
    {
      public void helpRequested(HelpEvent e)
      {
        if (getTray() != null)
        {
          closeTray();
          updatedHelpButton(false);
          return;
        }

        DialogTray tray = new DialogTray()
        {
          @Override
          protected Control createContents(Composite parent)
          {
            URL resource = plugin.getBundle().getResource(help);

            try
            {
              resource = FileLocator.resolve(resource);
            }
            catch (IOException ex)
            {
              UIPlugin.INSTANCE.log(ex);
            }

            Browser browser = new Browser(parent, SWT.NONE);
            browser.setSize(500, 800);
            browser.setUrl(resource.toString());
            return browser;
          }
        };

        openTray(tray);
        updatedHelpButton(true);
      }

      private void updatedHelpButton(boolean pushed)
      {
        try
        {
          Field field = ReflectUtil.getField(TrayDialog.class, "fHelpButton");
          ToolItem fHelpButton = (ToolItem)ReflectUtil.getValue(field, AbstractDialog.this);
          fHelpButton.setSelection(pushed);
        }
        catch (Exception ex)
        {
          UIPlugin.INSTANCE.log(ex);
        }
      }
    });

    shell.setActive();
    return area;
  }

  protected Button createCheckbox(Composite parent, String label)
  {
    ((GridLayout)parent.getLayout()).numColumns++;

    Button button = new Button(parent, SWT.CHECK);
    button.setText(label);
    button.setFont(JFaceResources.getDialogFont());

    setButtonLayoutData(button);
    return button;
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    ToolBar toolBar = (ToolBar)super.createHelpControl(parent);
    createToolItemsForToolBar(toolBar);
    return toolBar;
  }

  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
  }

  protected final ToolItem createToolItem(ToolBar toolBar, String label)
  {
    return createToolItem(toolBar, null, label);
  }

  protected final ToolItem createToolItem(ToolBar toolBar, String iconPath, String toolTip)
  {
    ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
    if (iconPath == null)
    {
      toolItem.setText(toolTip);
    }
    else
    {
      Image image = getDefaultImage(iconPath);
      toolItem.setImage(image);
      toolItem.setToolTipText(toolTip);
    }

    return toolItem;
  }

  protected Label createSeparator(Composite parent)
  {
    Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
    separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    return separator;
  }

  protected int getContainerMargin()
  {
    return 0;
  }

  protected abstract String getImagePath();

  @Override
  protected final Point getInitialSize()
  {
    return new Point(width, height);
  }

  protected final Image getDefaultImage(String path)
  {
    return plugin.getSWTImage(path);
  }

  protected abstract String getDefaultMessage();

  protected abstract String getShellText();

  protected abstract void createUI(Composite parent);
}

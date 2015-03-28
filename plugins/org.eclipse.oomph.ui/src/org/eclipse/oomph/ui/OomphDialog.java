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
import org.eclipse.oomph.ui.HelpSupport.HelpProvider;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.lang.reflect.Field;

/**
 * @author Eike Stepper
 */
public abstract class OomphDialog extends TitleAreaDialog implements HelpProvider
{
  private static final String SETTING_DIALOG_WIDTH = "dialogWidth";

  private static final String SETTING_DIALOG_HEIGHT = "dialogHeight";

  private String title;

  private int width;

  private int height;

  private OomphUIPlugin plugin;

  private HelpSupport helpSupport;

  protected OomphDialog(Shell parentShell, String title, int defaultWidth, int defaultHeight, OomphUIPlugin plugin, boolean helpAvailable)
  {
    super(parentShell);
    setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);

    this.title = title;
    this.plugin = plugin;

    IDialogSettings settings = getDialogSettings();

    try
    {
      int dialogWidth = settings.getInt(SETTING_DIALOG_WIDTH);
      width = dialogWidth;
    }
    catch (NumberFormatException ex)
    {
      width = defaultWidth;
    }
    try
    {
      int dialogHeight = settings.getInt(SETTING_DIALOG_HEIGHT);
      height = dialogHeight;
    }
    catch (NumberFormatException ex)
    {
      height = defaultHeight;
    }

    if (helpAvailable)
    {
      helpSupport = new HelpSupport(this)
      {
        @Override
        protected void handleInactivity(Display display, boolean inactive)
        {
          OomphDialog.this.handleInactivity(display, inactive);
        }
      };
    }
  }

  public final HelpSupport getHelpSupport()
  {
    return helpSupport;
  }

  public String getHelpPath()
  {
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

    Point size = getShell().getSize();
    IDialogSettings settings = getDialogSettings();
    settings.put(SETTING_DIALOG_WIDTH, size.x);
    settings.put(SETTING_DIALOG_HEIGHT, size.y);

    return super.close();
  }

  public String getTitle()
  {
    return title;
  }

  @Override
  public void openTray(DialogTray tray) throws IllegalStateException, UnsupportedOperationException
  {
    super.openTray(tray);
    hookTray(this);
  }

  @Override
  public void setTitleImage(Image newTitleImage)
  {
    super.setTitleImage(newTitleImage);
    fixTitleImageLayout(this);
  }

  @Override
  protected Control createContents(Composite parent)
  {
    Control contents = super.createContents(parent);
    fixTitleImageLayout(this);
    return contents;
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

    GridLayout layout = new GridLayout(getContainerColumns(), false);
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
    if (helpSupport != null)
    {
      ToolItem helpButton = toolBar.getItems()[0];
      helpSupport.hook(helpButton);
    }

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
    separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, getContainerColumns(), 1));
    return separator;
  }

  protected int getContainerColumns()
  {
    return 1;
  }

  protected int getContainerMargin()
  {
    return 0;
  }

  protected abstract String getImagePath();

  protected IDialogSettings getDialogSettings()
  {
    String sectionName = getClass().getName();
    OomphUIPlugin plugin = this.plugin != null ? this.plugin : UIPlugin.INSTANCE;
    return plugin.getDialogSettings(sectionName);
  }

  protected void handleInactivity(Display display, boolean inactive)
  {
  }

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

  public static void fixTitleImageLayout(TitleAreaDialog dialog)
  {
    try
    {
      Field titleImageLargestField = ReflectUtil.getField(TitleAreaDialog.class, "titleImageLargest");
      boolean titleImageLargest = (Boolean)ReflectUtil.getValue(titleImageLargestField, dialog);

      Field workAreaField = ReflectUtil.getField(TitleAreaDialog.class, "workArea");
      Composite workArea = (Composite)ReflectUtil.getValue(workAreaField, dialog);

      Field titleImageLabelField = ReflectUtil.getField(TitleAreaDialog.class, "titleImageLabel");
      Label titleImageLabel = (Label)ReflectUtil.getValue(titleImageLabelField, dialog);

      FormData layoutData = (FormData)titleImageLabel.getLayoutData();

      if (titleImageLargest)
      {
        layoutData.top = new FormAttachment(0, 0);
        layoutData.bottom = null;
      }
      else
      {
        layoutData.top = null;
        layoutData.bottom = new FormAttachment(workArea);
      }
    }
    catch (Throwable t)
    {
      // Ignore.
    }
  }

  public static void hookTray(final TrayDialog dialog) throws IllegalStateException, UnsupportedOperationException
  {
    final Control trayControl = getFieldValue(dialog, "trayControl");
    final Label rightSeparator = getFieldValue(dialog, "rightSeparator");
    final Sash sash = getFieldValue(dialog, "sash");
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
          Shell shell = dialog.getShell();
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

  private static <T> T getFieldValue(TrayDialog dialog, String name)
  {
    try
    {
      Field field = ReflectUtil.getField(TrayDialog.class, name);
      if (field != null)
      {
        @SuppressWarnings("unchecked")
        T value = (T)ReflectUtil.getValue(field, dialog);
        return value;
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }
}

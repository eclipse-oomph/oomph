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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.ui.HelpSupport.HelpProvider;
import org.eclipse.oomph.util.PropertyFile;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author Eike Stepper
 */
public abstract class OomphDialog extends TitleAreaDialog implements HelpProvider
{
  private static final PropertyFile HISTORY = new PropertyFile(UIPlugin.INSTANCE.getUserLocation().append("dialog-help-shown.properties").toFile());

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

    width = defaultWidth;
    height = defaultHeight;

    IDialogSettings settings = getDialogSettings();

    try
    {
      int dialogWidth = settings.getInt(SETTING_DIALOG_WIDTH);
      width = dialogWidth;
    }
    catch (NumberFormatException ex)
    {
      //$FALL-THROUGH$
    }

    try
    {
      int dialogHeight = settings.getInt(SETTING_DIALOG_HEIGHT);
      height = dialogHeight;
    }
    catch (NumberFormatException ex)
    {
      //$FALL-THROUGH$
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

  @Override
  public boolean isHelpAvailable()
  {
    return helpSupport != null;
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

    if (getTray() != null)
    {
      closeTray();
    }

    Shell shell = getShell();
    if (shell != null)
    {
      Point size = shell.getSize();
      IDialogSettings settings = getDialogSettings();
      settings.put(SETTING_DIALOG_WIDTH, size.x);
      settings.put(SETTING_DIALOG_HEIGHT, size.y);
    }

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
    container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

    createUI(container);

    if (getContainerMargin() == 0)
    {
      createSeparator(container);
    }

    shell.setActive();
    return area;
  }

  protected Control createButtonBarWithControls(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.marginRight = 0;
    layout.marginLeft = 0;
    layout.marginHeight = 0;
    layout.horizontalSpacing = 5;

    Composite controlArea = new Composite(parent, SWT.NONE);
    controlArea.setLayout(layout);
    controlArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    controlArea.setFont(parent.getFont());

    boolean helpAvailable = isHelpAvailable();
    if (helpAvailable)
    {
      Control helpControl = createHelpControl(controlArea);
      ((GridData)helpControl.getLayoutData()).horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
    }

    createControlsForButtonBar(controlArea);

    HelpSupport oldHelpSupport = helpSupport;
    helpSupport = null;
    super.createButtonBar(controlArea);
    helpSupport = oldHelpSupport;

    return controlArea;
  }

  protected void createControlsForButtonBar(Composite parent)
  {
    throw new UnsupportedOperationException("At least one control must be added when createButtonBarWithControls is called.");
  }

  public CCombo createCCombo(Composite parent)
  {
    GridLayout layout = (GridLayout)parent.getLayout();
    if (layout.numColumns == 1)
    {
      layout.marginLeft = 10;
    }

    layout.numColumns++;

    final CCombo combo = new CCombo(parent, SWT.BORDER | SWT.READ_ONLY | SWT.FLAT);
    combo.addControlListener(new ControlAdapter()
    {
      boolean firstTime = true;

      @Override
      public void controlResized(ControlEvent e)
      {
        Button button = getButton(IDialogConstants.CANCEL_ID);
        if (firstTime)
        {
          button.addControlListener(this);
          firstTime = false;
        }

        Rectangle buttonBounds = button.getBounds();
        if (buttonBounds.height != 0)
        {
          int borderCompensation = button.getBorderWidth() - combo.getBorderWidth();

          Rectangle bounds = combo.getBounds();
          bounds.y = buttonBounds.y - borderCompensation / 2;
          bounds.height = buttonBounds.height + borderCompensation;

          combo.setBounds(bounds);
        }
      }
    });

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    data.grabExcessHorizontalSpace = true;
    data.widthHint = widthHint;
    combo.setLayoutData(data);

    return combo;
  }

  public Combo createCombo(Composite parent)
  {
    GridLayout layout = (GridLayout)parent.getLayout();
    if (layout.numColumns == 1)
    {
      layout.marginLeft = 10;
    }

    layout.numColumns++;

    Combo combo = new Combo(parent, SWT.READ_ONLY);

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    data.grabExcessHorizontalSpace = true;
    data.widthHint = widthHint;
    combo.setLayoutData(data);

    return combo;
  }

  public Label createLabel(Composite parent, String text)
  {
    GridLayout layout = (GridLayout)parent.getLayout();
    if (layout.numColumns == 1 && parent.getChildren().length == 0)
    {
      layout.marginLeft = 10;
    }

    layout.numColumns++;

    Label label = new Label(parent, SWT.NONE);
    label.setText(text);

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    label.setLayoutData(data);

    return label;
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

  public static boolean showFirstTimeHelp(final TrayDialog dialog)
  {
    try
    {
      if (dialog.isHelpAvailable())
      {
        String key = dialog.getClass().getName();
        String value = new Date().toString();

        if (HISTORY.compareAndSetProperty(key, value))
        {
          UIUtil.asyncExec(dialog.getShell(), new Runnable()
          {
            public void run()
            {
              try
              {
                Method method = ReflectUtil.getMethod(TrayDialog.class, "helpPressed");
                ReflectUtil.invokeMethod(method, dialog);
              }
              catch (Throwable ex)
              {
                UIPlugin.INSTANCE.log(ex);
              }
            }
          });

          return true;
        }
      }
    }
    catch (Throwable ex)
    {
      UIPlugin.INSTANCE.log(ex);
    }

    return false;
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

  /**
   * This is a prototype, maybe worth showing to people later...
   */
  @SuppressWarnings("unused")
  private static void animateMessage(TitleAreaDialog dialog)
  {
    try
    {
      final Text messageLabel = ReflectUtil.getValue("messageLabel", dialog);
      final Display display = messageLabel.getDisplay();

      final Color[] colors = new Color[8];
      for (int color = 0; color < colors.length; color++)
      {
        colors[color] = new Color(display, 0, color * 32, 0);
      }

      display.asyncExec(new Runnable()
      {
        private int loop;

        private int color;

        private boolean reverse;

        public void run()
        {
          // System.out.println(loop + ", " + color);
          messageLabel.setForeground(colors[color]);

          if (reverse)
          {
            if (--color == -1)
            {
              if (++loop == 5)
              {
                for (int i = 0; i < colors.length; i++)
                {
                  colors[i].dispose();
                }

                return;
              }

              color = 1;
              reverse = false;
            }
          }
          else
          {
            if (++color == colors.length)
            {
              color = colors.length - 2;
              reverse = true;
            }
          }

          display.timerExec(80, this);
        }
      });
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
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

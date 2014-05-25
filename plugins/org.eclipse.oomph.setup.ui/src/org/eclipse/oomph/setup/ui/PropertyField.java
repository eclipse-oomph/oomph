/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Julian Enoch - Expand the functionality for PASSWORD variables
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.util.ConcurrentArray;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class PropertyField<CONTROL extends Control>
{
  private static final String EMPTY = "";

  private final GridData labelGridData = new GridData(SWT.RIGHT, SWT.TOP, false, false);

  private final GridData controlGridData = new GridData(SWT.FILL, SWT.TOP, true, false);

  private final GridData helperGridData = new GridData(SWT.FILL, SWT.TOP, false, false);

  private final ValueListenerArray valueListeners = new ValueListenerArray();

  private String value = EMPTY;

  private String labelText;

  private String toolTip;

  private Label label;

  private CONTROL control;

  private Control helper;

  private boolean enabled = true;

  public PropertyField()
  {
    this(null);
  }

  public PropertyField(String labelText)
  {
    this.labelText = labelText;
    setValue(null);
  }

  public final String getLabelText()
  {
    return labelText;
  }

  public final void setLabelText(String labelText)
  {
    if (labelText.endsWith(":"))
    {
      labelText = labelText.substring(0, labelText.length() - 1);
    }

    this.labelText = labelText.trim();
  }

  public final String getToolTip()
  {
    return toolTip;
  }

  public final void setToolTip(String toolTip)
  {
    this.toolTip = toolTip;
  }

  public final String getValue()
  {
    return value;
  }

  public final void setValue(String value)
  {
    if (value == null)
    {
      value = EMPTY;
    }

    String oldValue = this.value;
    if (!oldValue.equals(value))
    {
      this.value = value;

      if (control != null)
      {
        String controlValue = getControlValue(control);
        if (!controlValue.equals(value))
        {
          transferValueToControl(value, control);
        }
      }

      notifyValueListeners(oldValue, value);
    }
  }

  public final void addValueListener(ValueListener listener)
  {
    if (listener == null)
    {
      throw new IllegalArgumentException("listener is null"); //$NON-NLS-1$
    }

    valueListeners.add(listener);
  }

  public final void removeValueListener(ValueListener listener)
  {
    if (listener == null)
    {
      throw new IllegalArgumentException("listener is null"); //$NON-NLS-1$
    }

    valueListeners.remove(listener);
  }

  public final void fill(final Composite parent)
  {
    checkParentLayout(parent);

    label = new Label(parent, SWT.WRAP);
    label.setLayoutData(labelGridData);
    if (labelText != null)
    {
      label.setText(labelText + ":");
    }

    control = createControl(parent);
    getMainControl().setLayoutData(controlGridData);

    if (toolTip != null && toolTip.length() != 0)
    {
      label.setToolTipText(toolTip);
      control.setToolTipText(toolTip);
      getMainControl().setToolTipText(toolTip);
    }

    helper = createHelper(parent);
    if (helper == null)
    {
      helper = new Label(parent, SWT.NONE);
    }
    else
    {
      helper = getMainHelper();
    }

    helper.setLayoutData(helperGridData);
    setEnabled(enabled);
    transferValueToControl(value, control);
  }

  public final Label getLabel()
  {
    return label;
  }

  public final GridData getLabelGridData()
  {
    return labelGridData;
  }

  public final CONTROL getControl()
  {
    return control;
  }

  public final GridData getControlGridData()
  {
    return controlGridData;
  }

  public final Control getHelper()
  {
    return helper;
  }

  public final GridData getHelperGridData()
  {
    return helperGridData;
  }

  public final void setFocus()
  {
    if (control != null)
    {
      control.setFocus();
    }
  }

  public final boolean isEnabled()
  {
    return enabled;
  }

  public final void setEnabled(boolean enabled)
  {
    this.enabled = enabled;

    if (label != null)
    {
      label.setEnabled(enabled);
    }

    setControlEnabled(enabled);

    Control mainHelper = getMainHelper();
    if (mainHelper != null)
    {
      mainHelper.setEnabled(enabled);
    }
  }

  protected void setControlEnabled(boolean enabled)
  {
    Control mainControl = getMainControl();
    if (mainControl != null)
    {
      mainControl.setEnabled(enabled);
    }
  }

  protected Control getMainControl()
  {
    return control;
  }

  protected Control getMainHelper()
  {
    return helper;
  }

  protected abstract String getControlValue(CONTROL control);

  protected abstract void transferValueToControl(String value, CONTROL control);

  protected abstract CONTROL createControl(Composite parent);

  protected Control createHelper(Composite parent)
  {
    return null;
  }

  private void checkParentLayout(Composite parent)
  {
    Layout layout = parent.getLayout();
    if (layout instanceof GridLayout)
    {
      GridLayout gridLayout = (GridLayout)layout;
      if (gridLayout.numColumns == 3)
      {
        return;
      }
    }

    throw new IllegalArgumentException("Parent must have a GridLayout with 3 columns");
  }

  private void notifyValueListeners(String oldValue, String newValue)
  {
    ValueListener[] listeners = valueListeners.get();
    if (listeners != null)
    {
      for (int i = 0; i < listeners.length; i++)
      {
        ValueListener listener = listeners[i];

        try
        {
          listener.valueChanged(oldValue, newValue);
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ValueListenerArray extends ConcurrentArray<ValueListener>
  {
    @Override
    protected ValueListener[] newArray(int length)
    {
      return new ValueListener[length];
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface ValueListener
  {
    public void valueChanged(String oldValue, String newValue) throws Exception;
  }

  /**
   * @author Eike Stepper
   */
  public static class TextField<H extends Control> extends PropertyField<Text>
  {
    private final boolean secret;

    private PropertyField<?> linkField;

    private Composite mainControl;

    private ToolItem linkButton;

    private boolean linked = true;

    public TextField()
    {
      this(null, false);
    }

    public TextField(boolean secret)
    {
      this(null, secret);
    }

    public TextField(String labelText)
    {
      this(labelText, false);
    }

    public TextField(String labelText, boolean secret)
    {
      super(labelText);
      this.secret = secret;
    }

    public final PropertyField<?> getLinkField()
    {
      return linkField;
    }

    public final void setLinkField(PropertyField<?> field)
    {
      linkField = field;
    }

    public final boolean isLinked()
    {
      return linked;
    }

    public final void setLinked(boolean linked)
    {
      this.linked = linked;

      if (linkButton != null)
      {
        String path = linked ? "linked.gif" : "icons/unlinked";
        Image image = SetupUIPlugin.INSTANCE.getSWTImage(path);

        linkButton.setImage(image);
        linkButton.setSelection(linked);

        if (linked)
        {
          setLinkedValue(linkField.getValue());
        }
      }
    }

    public final void setLinkedFromValue()
    {
      String thisValue = getValue();
      String linkValue = linkField.getValue();
      setLinked(thisValue.length() == 0 && linkValue.length() == 0 || thisValue.equals(computeLinkedValue(thisValue, linkValue)));
    }

    @Override
    protected Control getMainControl()
    {
      if (mainControl != null)
      {
        return mainControl;
      }

      return super.getMainControl();
    }

    @Override
    protected String getControlValue(Text text)
    {
      return text.getText();
    }

    @Override
    protected void transferValueToControl(String value, Text text)
    {
      text.setText(value);
    }

    @Override
    protected Text createControl(Composite parent)
    {
      if (linkField == null)
      {
        return createText(parent);
      }

      GridLayout mainLayout = new GridLayout(2, false);
      mainLayout.marginWidth = 0;
      mainLayout.marginHeight = 0;
      mainLayout.horizontalSpacing = 0;

      final Text[] text = { null };
      mainControl = new Composite(parent, SWT.NULL)
      {
        @Override
        public void setEnabled(boolean enabled)
        {
          text[0].setEnabled(enabled);

          // super.setEnabled(enabled);
          //
          // Control[] children = getChildren();
          // for (int i = 0; i < children.length; i++)
          // {
          // Control child = children[i];
          // child.setEnabled(enabled);
          // }
        }
      };
      mainControl.setLayout(mainLayout);

      text[0] = createText(mainControl);
      text[0].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

      ToolBar toolBar = new ToolBar(mainControl, SWT.FLAT | SWT.NO_FOCUS);
      toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

      linkButton = new ToolItem(toolBar, SWT.PUSH);
      linkButton.setToolTipText("Link with the '" + linkField.getLabelText() + "' field");
      linkButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          setLinked(!linked);
        }
      });

      linkField.addValueListener(new ValueListener()
      {
        public void valueChanged(String oldValue, String newValue) throws Exception
        {
          if (linked)
          {
            setLinkedValue(newValue);
          }
        }
      });

      setLinkedFromValue();

      return text[0];
    }

    protected String computeLinkedValue(String thisValue, String linkValue)
    {
      return linkValue;
    }

    private void setLinkedValue(String newValue)
    {
      String thisValue = getValue();
      String value = computeLinkedValue(thisValue, newValue);
      setValue(value);
    }

    private Text createText(Composite parent)
    {
      int style = SWT.BORDER;
      if (secret)
      {
        style |= SWT.PASSWORD;
      }

      final Text text = new Text(parent, style);

      String toolTip = getToolTip();
      if (toolTip != null)
      {
        text.setToolTipText(toolTip);
      }

      text.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          String value = text.getText();
          if (!value.equals(getValue()))
          {
            setValue(value);

            if (linkButton != null && linked)
            {
              setLinkedFromValue();
            }
          }
        }
      });

      return text;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class TextButtonField extends TextField<Button>
  {
    private String buttonText;

    public TextButtonField()
    {
    }

    public TextButtonField(boolean secret)
    {
      super(secret);
    }

    public TextButtonField(String labelText)
    {
      super(labelText);
    }

    public TextButtonField(String labelText, boolean secret)
    {
      super(labelText, secret);
    }

    public final String getButtonText()
    {
      return buttonText;
    }

    public final void setButtonText(String buttonText)
    {
      this.buttonText = buttonText;
    }

    @Override
    protected Button createHelper(Composite parent)
    {
      final Button button = new Button(parent, SWT.NONE);
      if (buttonText != null)
      {
        button.setText(buttonText);
      }

      button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          helperButtonSelected(e);
        }
      });

      return button;
    }

    protected void helperButtonSelected(SelectionEvent e)
    {
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class FileField extends TextButtonField
  {
    private String dialogText;

    private String dialogMessage;

    public FileField()
    {
      this(null);
    }

    public FileField(String labelText)
    {
      super(labelText);
      setButtonText("Browse...");
    }

    public final String getDialogText()
    {
      return dialogText;
    }

    public final void setDialogText(String dialogText)
    {
      this.dialogText = dialogText;
    }

    public final String getDialogMessage()
    {
      return dialogMessage;
    }

    public final void setDialogMessage(String dialogMessage)
    {
      this.dialogMessage = dialogMessage;
    }

    @Override
    protected void helperButtonSelected(SelectionEvent e)
    {
      Shell shell = getHelper().getShell();
      DirectoryDialog dialog = new DirectoryDialog(shell);
      if (dialogText != null)
      {
        dialog.setText(dialogText);
      }

      if (dialogMessage != null)
      {
        dialog.setMessage(dialogMessage);
      }

      String value = getValue();
      if (value.length() != 0)
      {
        dialog.setFilterPath(value);
      }

      String dir = dialog.open();
      if (dir != null)
      {
        Text control = getControl();
        control.setText(dir);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ChoiceField<H extends Control> extends PropertyField<Control>
  {
    private Composite mainControl;

    private ComboViewer comboViewer;

    private List<VariableChoice> choices;

    public ChoiceField(List<VariableChoice> choices)
    {
      this.choices = choices;
    }

    @Override
    protected Control getMainControl()
    {
      if (mainControl != null)
      {
        return mainControl;
      }

      return super.getMainControl();
    }

    @Override
    protected String getControlValue(Control control)
    {
      return comboViewer.getCombo().getText();
    }

    @Override
    protected void transferValueToControl(String value, Control control)
    {
      comboViewer.getCombo().setText(value);
    }

    @Override
    protected Control createControl(Composite parent)
    {
      GridLayout mainLayout = new GridLayout(2, false);
      mainLayout.marginWidth = 0;
      mainLayout.marginHeight = 0;
      mainLayout.horizontalSpacing = 0;

      mainControl = new Composite(parent, SWT.NULL)
      {
        @Override
        public void setEnabled(boolean enabled)
        {
          super.setEnabled(enabled);

          Control[] children = getChildren();
          for (int i = 0; i < children.length; i++)
          {
            Control child = children[i];
            child.setEnabled(enabled);
          }
        }
      };

      mainControl.setLayout(mainLayout);

      comboViewer = new ComboViewer(mainControl, SWT.BORDER);
      comboViewer.setLabelProvider(new LabelProvider()
      {
        @Override
        public String getText(Object element)
        {
          return ((VariableChoice)element).getLabel();
        }
      });

      comboViewer.setContentProvider(new ArrayContentProvider());
      comboViewer.setInput(choices);
      comboViewer.getCombo().addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          setValue(comboViewer.getCombo().getText());
        }
      });

      comboViewer.getCombo().addVerifyListener(new VerifyListener()
      {
        public void verifyText(VerifyEvent e)
        {
          if (e.character == 0)
          {
            for (VariableChoice choice : choices)
            {
              if (choice.getLabel().equals(e.text))
              {
                e.text = choice.getValue();
                break;
              }
            }
          }
        }
      });

      Control control = comboViewer.getControl();
      control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      return control;
    }
  }

  public void dispose()
  {
    Label label = getLabel();
    if (label != null)
    {
      label.dispose();
    }

    Control mainControl = getMainControl();
    if (mainControl != null)
    {
      mainControl.dispose();
    }

    Control mainHelper = getMainHelper();
    if (mainHelper != null)
    {
      mainHelper.dispose();
    }

  }
}

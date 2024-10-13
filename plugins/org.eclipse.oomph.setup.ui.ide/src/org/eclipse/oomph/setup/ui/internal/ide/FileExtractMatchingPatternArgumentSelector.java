/*
 * Copyright (c) 2014 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ide.IDEEncoding;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class FileExtractMatchingPatternArgumentSelector extends AbstractArgumentSelector
{
  @Override
  public String selectArgument(IStringVariable variable, Shell shell)
  {
    ArgumentDialog uriArgumentDialog = new ArgumentDialog(shell);
    if (uriArgumentDialog.open() == Window.OK)
    {
      return uriArgumentDialog.getValue();
    }

    return null;
  }

  public static class ExtensionFactory extends AbstractExtensionFactory
  {
    @Override
    protected AbstractArgumentSelector createSelector()
    {
      return new FileExtractMatchingPatternArgumentSelector();
    }
  }

  private static class ArgumentDialog extends OomphDialog
  {
    private Text fileText;

    private Combo encodingCombo;

    private Text patternText;

    private Text substitutionText;

    private Text defaultText;

    private String value;

    protected ArgumentDialog(Shell parentShell)
    {
      super(parentShell, Messages.FileExtractMatchingPatternArgumentSelector_ConfigureCommandParameters_title, 600, 500, SetupUIIDEPlugin.INSTANCE, false);
      setHelpAvailable(false);
    }

    @SuppressWarnings("nls")
    public String getValue()
    {
      return value != null ? value
          : String.join(",", List.of(fileText.getText(), encodingCombo.getText(), escapeComma(patternText.getText()), escapeComma(substitutionText.getText()),
              escapeComma(defaultText.getText())));
    }

    @SuppressWarnings("nls")
    protected String escapeComma(String value)
    {
      return value.replace(",", "\\,");
    }

    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);
      newShell.setText(Messages.FileExtractMatchingPatternArgumentSelector_Configure_label);
    }

    @Override
    protected void createUI(Composite composite)
    {
      ((GridLayout)composite.getLayout()).verticalSpacing = 5;

      {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.FileExtractMatchingPatternArgumentSelector_File_label);
        UIUtil.applyGridData(label).grabExcessHorizontalSpace = false;

        fileText = new Text(composite, SWT.BORDER);
        fileText.addModifyListener(this::modified);
        GridData gridData = UIUtil.applyGridData(fileText);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 400;

        UIUtil.applyGridData(createButtonInline(composite, IDialogConstants.OPEN_ID, Messages.URIArgumentSelector_variablesButton_text,
            false)).grabExcessHorizontalSpace = false;
      }

      {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.FileExtractMatchingPatternArgumentSelector_Encoding_label);
        UIUtil.applyGridData(label).grabExcessHorizontalSpace = false;

        encodingCombo = new Combo(composite, SWT.BORDER);
        GridData gridData = UIUtil.applyGridData(encodingCombo);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;

        List<String> encodings = IDEEncoding.getIDEEncodings();
        String[] encodingStrings = new String[encodings.size()];
        encodings.toArray(encodingStrings);
        encodingCombo.setItems(encodingStrings);
        encodingCombo.setText(StandardCharsets.UTF_8.name());

        encodingCombo.addModifyListener(this::modified);
      }

      {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.FileExtractMatchingPatternArgumentSelector_Pattern_label);
        UIUtil.applyGridData(label).grabExcessHorizontalSpace = false;

        patternText = new Text(composite, SWT.BORDER);
        patternText.addModifyListener(this::modified);
        GridData gridData = UIUtil.applyGridData(patternText);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
      }

      {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.FileExtractMatchingPatternArgumentSelector_Subsitutions_label);
        UIUtil.applyGridData(label).grabExcessHorizontalSpace = false;

        substitutionText = new Text(composite, SWT.BORDER);
        substitutionText.addModifyListener(this::modified);
        GridData gridData = UIUtil.applyGridData(substitutionText);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
      }

      {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.FileExtractMatchingPatternArgumentSelector_DefaultValue_label);
        UIUtil.applyGridData(label).grabExcessHorizontalSpace = false;

        defaultText = new Text(composite, SWT.BORDER);
        GridData gridData = UIUtil.applyGridData(defaultText);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
      }
    }

    @Override
    protected Control createButtonBar(Composite parent)
    {
      Control result = super.createButtonBar(parent);
      getButton(IDialogConstants.OK_ID).setEnabled(false);
      return result;
    }

    @SuppressWarnings("nls")
    public void modified(ModifyEvent event)
    {
      setErrorMessage(null);

      String text = fileText.getText();
      if (text.isBlank())
      {
        setErrorMessage(Messages.FileExtractMatchingPatternArgumentSelector_FileUnspecified_message);
        return;
      }

      String charset = encodingCombo.getText();
      try
      {
        Charset.forName(charset);
      }
      catch (RuntimeException ex)
      {
        setErrorMessage(Messages.FileExtractMatchingPatternArgumentSelector_InvalidEncoding_message);
        return;
      }

      String pattern = patternText.getText();
      if (pattern.isBlank())
      {
        setErrorMessage(Messages.FileExtractMatchingPatternArgumentSelector_MissingPattern_message);
        return;
      }

      try
      {
        Pattern.compile(pattern);
      }
      catch (RuntimeException ex)
      {
        setErrorMessage(Messages.FileExtractMatchingPatternArgumentSelector_InvalidPattern_message + ex.getLocalizedMessage().replaceAll("\r?\n.*", ""));
        return;
      }

      String substitution = substitutionText.getText();
      if (substitution.isBlank())
      {
        setErrorMessage(Messages.FileExtractMatchingPatternArgumentSelector_MissingSubstitution_message);
      }
    }

    @Override
    public void setErrorMessage(String newErrorMessage)
    {
      getButton(IDialogConstants.OK_ID).setEnabled(newErrorMessage == null);
      super.setErrorMessage(newErrorMessage);
    }

    protected Button createButtonInline(Composite parent, int id, String label, boolean defaultButton)
    {
      ((GridLayout)parent.getLayout()).numColumns--;
      return createButton(parent, id, label, defaultButton);
    }

    @Override
    protected void buttonPressed(int buttonId)
    {
      if (buttonId == IDialogConstants.OPEN_ID)
      {
        StringVariableSelectionDialog stringVariableSelectionDialog = new StringVariableSelectionDialog(getShell());
        if (stringVariableSelectionDialog.open() == Window.OK)
        {
          fileText.insert(stringVariableSelectionDialog.getVariableExpression());
        }
      }
      else
      {
        value = getValue();
        super.buttonPressed(buttonId);
      }
    }

    @Override
    protected boolean isResizable()
    {
      return true;
    }

    @Override
    protected String getImagePath()
    {
      return null;
    }

    @Override
    protected String getDefaultMessage()
    {
      return Messages.FileExtractMatchingPatternArgumentSelector_TitleArea_description;
    }

    @Override
    protected String getShellText()
    {
      return Messages.FileExtractMatchingPatternArgumentSelector_FileExtractMatchingPattern_title;
    }

    @Override
    protected int getContainerColumns()
    {
      return 3;
    }

    @Override
    protected int getContainerMargin()
    {
      return 10;
    }
  }
}

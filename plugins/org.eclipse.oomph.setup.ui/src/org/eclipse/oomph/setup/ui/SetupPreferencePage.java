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
package org.eclipse.oomph.setup.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.EditorSelectionDialog;

/**
 * @author Eike Stepper
 */
public class SetupPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  public static final String ID = "org.eclipse.oomph.setup.SetupPreferencePage";

  private IWorkbench workbench;

  public SetupPreferencePage()
  {
  }

  public void init(IWorkbench workbench)
  {
    this.workbench = workbench;
  }

  @Override
  protected IPreferenceStore doGetPreferenceStore()
  {
    return SetupUIPlugin.INSTANCE.getPreferenceStore();
  }

  @Override
  protected void createFieldEditors()
  {
    final Composite parent = getFieldEditorParent();

    addBooleanField(parent, //
        SetupUIPlugin.PREF_SKIP_STARTUP_TASKS, //
        "Skip automatic task execution at startup time", //
        "Don't automatically perform setup tasks when a new workspace is opened");

    addBooleanField(parent, //
        SetupPropertyTester.SHOW_TOOL_BAR_CONTRIBUTIONS, //
        "Show tool bar contributions", //
        "Show the 'Perform Setup Tasks' and 'Open Setups' tool bar contributions on the main tool bar");

    addBooleanField(parent, //
        SetupPropertyTester.SHOW_PROGRESS_IN_WIZARD, //
        "Show progress in setup wizard", //
        "Don't automatically minimize the setup wizard when it starts performing.\n" //
            + "If this setting is enabled the wizard can be manually minimized.\n" //
            + "A minimized wizard can be restored by clicking the animated perform\n" //
            + "button in the status bar in front of the progress indicator.");

    final StringButtonFieldEditor preferredTextEditor = new StringButtonFieldEditor(SetupEditorSupport.PREF_TEXT_EDITOR_ID, "Preferred text editor for models",
        parent)
    {
      @Override
      protected String changePressed()
      {
        EditorSelectionDialog dialog = new EditorSelectionDialog(getControl().getShell());
        dialog.setMessage("Choose the editor to open when 'Open in text editor' is selected in a model editor:");
        if (dialog.open() == EditorSelectionDialog.OK)
        {
          IEditorDescriptor descriptor = dialog.getSelectedEditor();
          if (descriptor != null)
          {
            return descriptor.getId();
          }
        }

        return null;
      }
    };
    addField(preferredTextEditor);
    preferredTextEditor.fillIntoGrid(parent, 3);
    preferredTextEditor.getLabelControl(parent).setToolTipText("The editor to open when 'Open in text editor' is selected in a model editor");

    if (Questionnaire.exists())
    {
      Button questionnaireButton = new Button(parent, SWT.PUSH);
      questionnaireButton.setText("Start Welcome Questionnaire...");
      questionnaireButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Shell shell = workbench.getActiveWorkbenchWindow().getShell();

          IPreferencePageContainer container = getContainer();
          if (container instanceof IShellProvider)
          {
            shell = ((IShellProvider)container).getShell();
          }

          final Shell parentShell = shell;

          // Don't perform the questionnaire on the UI thread or RecorderTransaction.open() will deadlock.
          new Thread("Questionnaire")
          {
            @Override
            public void run()
            {
              Questionnaire.perform(parentShell, true);
            }
          }.start();
        }
      });
    }
  }

  private void addBooleanField(final Composite parent, String preferenceName, String label, String toolTip)
  {
    BooleanFieldEditor fieldEditor = new BooleanFieldEditor(preferenceName, label, parent);
    fieldEditor.fillIntoGrid(parent, 3);
    addField(fieldEditor);
    fieldEditor.getDescriptionControl(parent).setToolTipText(toolTip);
  }
}

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
package org.eclipse.oomph.setup.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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

  private ComboFieldEditor p2StartupTasks;

  private BooleanFieldEditor skipStartupTasks;

  private Composite parent;

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
    parent = getFieldEditorParent();

    skipStartupTasks = addBooleanField(parent, //
        SetupUIPlugin.PREF_SKIP_STARTUP_TASKS, //
        "Skip automatic task execution at startup time", //
        "Don't automatically perform setup tasks when a new workspace is opened");

    p2StartupTasks = new ComboFieldEditor(SetupUIPlugin.PREF_P2_STARTUP_TASKS, "Install requirements on startup", //
        new String[][] { //
            new String[] { "Prompt", P2TaskUIServicesPrompter.Action.PROMPT.name() }, //
            new String[] { "Always", P2TaskUIServicesPrompter.Action.ALWAYS.name() }, //
            new String[] { "Never", P2TaskUIServicesPrompter.Action.NEVER.name() } //
        }, parent);
    addField(p2StartupTasks);
    p2StartupTasks.fillIntoGrid(parent, 3);
    Label p2StartupTasksLabel = p2StartupTasks.getLabelControl(parent);
    GridData gridData = (GridData)p2StartupTasksLabel.getLayoutData();
    gridData.horizontalIndent = 20;
    p2StartupTasksLabel.setToolTipText("What to do when the installation doesn't satisfy the requirements of what should be installed");
    p2StartupTasks.setEnabled(!skipStartupTasks.getBooleanValue(), parent);

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

          Questionnaire.perform(parentShell, true);
        }
      });
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent event)
  {
    super.propertyChange(event);
    if (event.getSource() == skipStartupTasks)
    {
      p2StartupTasks.setEnabled(!Boolean.TRUE.equals(event.getNewValue()), parent);
    }
  }

  private BooleanFieldEditor addBooleanField(final Composite parent, String preferenceName, String label, String toolTip)
  {
    BooleanFieldEditor fieldEditor = new BooleanFieldEditor(preferenceName, label, parent);
    fieldEditor.fillIntoGrid(parent, 3);
    addField(fieldEditor);
    fieldEditor.getDescriptionControl(parent).setToolTipText(toolTip);
    return fieldEditor;
  }
}

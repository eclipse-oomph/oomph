/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
  public static final String ID = "org.eclipse.oomph.setup.SetupPreferencePage"; //$NON-NLS-1$

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
        Messages.SetupPreferencePage_skipStartupTasks_label, //
        Messages.SetupPreferencePage_skipStartupTasks_tooltip);

    p2StartupTasks = new ComboFieldEditor(SetupUIPlugin.PREF_P2_STARTUP_TASKS, Messages.SetupPreferencePage_p2StartupTasks_label, //
        new String[][] { //
            new String[] { Messages.SetupPreferencePage_p2StartupTasks_prompt, P2TaskUIServicesPrompter.Action.PROMPT.name() }, //
            new String[] { Messages.SetupPreferencePage_p2StartupTasks_always, P2TaskUIServicesPrompter.Action.ALWAYS.name() }, //
            new String[] { Messages.SetupPreferencePage_p2StartupTasks_never, P2TaskUIServicesPrompter.Action.NEVER.name() } //
        }, parent);
    addField(p2StartupTasks);
    p2StartupTasks.fillIntoGrid(parent, 3);
    Label p2StartupTasksLabel = p2StartupTasks.getLabelControl(parent);
    GridData gridData = (GridData)p2StartupTasksLabel.getLayoutData();
    gridData.horizontalIndent = 20;
    p2StartupTasksLabel.setToolTipText(Messages.SetupPreferencePage_p2StartupTasks_labelTooltip);
    p2StartupTasks.setEnabled(!skipStartupTasks.getBooleanValue(), parent);

    addBooleanField(parent, //
        SetupPropertyTester.SHOW_TOOL_BAR_CONTRIBUTIONS, //
        Messages.SetupPreferencePage_showToolbarContributions_label, //
        Messages.SetupPreferencePage_showToolbarContributions_tooltip);

    addBooleanField(parent, //
        SetupPropertyTester.SHOW_PROGRESS_IN_WIZARD, //
        Messages.SetupPreferencePage_showProgressInWizard_label, Messages.SetupPreferencePage_showProgressInWizard_tooltip);

    final StringButtonFieldEditor preferredTextEditor = new StringButtonFieldEditor(SetupEditorSupport.PREF_TEXT_EDITOR_ID,
        Messages.SetupPreferencePage_preferredTextEditor_text, parent)
    {
      @Override
      protected String changePressed()
      {
        EditorSelectionDialog dialog = new EditorSelectionDialog(getControl().getShell());
        dialog.setMessage(Messages.SetupPreferencePage_preferredTextEditor_dialogMessage);
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
    preferredTextEditor.getLabelControl(parent).setToolTipText(Messages.SetupPreferencePage_preferredTextEditor_labelTooltip);

    if (Questionnaire.exists())
    {
      Button questionnaireButton = new Button(parent, SWT.PUSH);
      questionnaireButton.setText(Messages.SetupPreferencePage_questionnaireButton_text);
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

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
package org.eclipse.oomph.setup.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Eike Stepper
 */
public class SetupPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
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
    Composite parent = getFieldEditorParent();

    BooleanFieldEditor skipAutomaticTaskExecution = new BooleanFieldEditor(SetupUIPlugin.PREF_SKIP_STARTUP_TASKS,
        "Skip automatic task execution at startup time", parent);
    skipAutomaticTaskExecution.fillIntoGrid(parent, 2);
    addField(skipAutomaticTaskExecution);
    skipAutomaticTaskExecution.getDescriptionControl(parent).setToolTipText("Don't automatically perform setup tasks when a new workspace is opened");

    BooleanFieldEditor showToolBars = new BooleanFieldEditor(SetupPropertyTester.SHOW_TOOL_BAR_CONTRIBUTIONS, "Show tool bar contributions", parent);
    showToolBars.fillIntoGrid(parent, 2);
    addField(showToolBars);
    showToolBars.getDescriptionControl(parent).setToolTipText("Show the 'Perform Setup Tasks' and 'Open Setups' tool bar contributions on the main tool bar");

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
}

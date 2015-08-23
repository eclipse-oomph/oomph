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
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public class RecorderPoliciesDialog extends AbstractSetupDialog
{
  private final RecorderTransaction transaction;

  private final Map<URI, String> preferences;

  private boolean enablePreferenceRecorder = true;

  private RecorderPoliciesComposite recorderPoliciesComposite;

  private Text valueText;

  public RecorderPoliciesDialog(Shell parentShell, RecorderTransaction transaction, Map<URI, String> preferences)
  {
    super(parentShell, "Preference Recorder", 600, 400, SetupUIPlugin.INSTANCE, true);
    this.transaction = transaction;
    this.preferences = preferences;
  }

  public boolean isEnablePreferenceRecorder()
  {
    return enablePreferenceRecorder;
  }

  public void setEnablePreferenceRecorder(boolean enablePreferenceRecorder)
  {
    this.enablePreferenceRecorder = enablePreferenceRecorder;
  }

  @Override
  protected String getShellText()
  {
    return "Oomph Preference Recorder";
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Select which preferences to record for reuse across all workspaces.  Press 'F1' for details.";
  }

  @Override
  public String getHelpPath()
  {
    return SetupUIPlugin.INSTANCE.getSymbolicName() + "/html/RecorderHelp.html";
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    SashForm sashForm = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    recorderPoliciesComposite = new RecorderPoliciesComposite(sashForm, SWT.FULL_SELECTION, false);
    recorderPoliciesComposite.setRecorderTransaction(transaction);
    recorderPoliciesComposite.setFocus();
    recorderPoliciesComposite.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        updateValue((IStructuredSelection)event.getSelection());
      }
    });

    valueText = new Text(sashForm, SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
    valueText.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

    Listener scrollBarListener = new Listener()
    {
      protected boolean changing;

      public void handleEvent(Event event)
      {
        if (!changing)
        {
          changing = true;
          Rectangle clientArea = valueText.getClientArea();
          Rectangle trimArea = valueText.computeTrim(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
          Point size = valueText.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
          valueText.getHorizontalBar().setVisible(trimArea.width <= size.x);
          valueText.getVerticalBar().setVisible(trimArea.height <= size.y);
          changing = false;
        }
      }
    };

    valueText.addListener(SWT.Resize, scrollBarListener);
    valueText.addListener(SWT.Modify, scrollBarListener);

    sashForm.setWeights(new int[] { 4, 1 });
    Dialog.applyDialogFont(sashForm);

    updateValue(recorderPoliciesComposite.getSelection());

    showFirstTimeHelp(this);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    final Button enableButton = createCheckbox(parent, "Recorder enabled");
    enableButton.setToolTipText("The enablement can be changed later on the preference page Oomph | Setup | Preference Recorder");
    enableButton.setSelection(enablePreferenceRecorder);
    enableButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        enablePreferenceRecorder = enableButton.getSelection();
        recorderPoliciesComposite.setEnabled(enablePreferenceRecorder);
        valueText.setVisible(enablePreferenceRecorder);
      }
    });

    super.createButtonsForButtonBar(parent);
  }

  private void updateValue(IStructuredSelection selection)
  {
    String path = (String)selection.getFirstElement();
    URI uri = PreferencesFactory.eINSTANCE.createURI(path);
    String value = StringUtil.safe(preferences.get(uri));
    valueText.setText(value);
  }
}

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

import org.eclipse.oomph.ui.SearchField;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Eike Stepper
 */
public class RecorderPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  private static RecorderPreferencePage instance;

  private RecorderTransaction transaction;

  private Button enableButton;

  private Label policiesLabel;

  private SearchField searchField;

  private RecorderPoliciesComposite policiesComposite;

  public RecorderPreferencePage()
  {
    instance = this;
  }

  public void init(IWorkbench workbench)
  {
    // Do nothing.
  }

  @Override
  public void dispose()
  {
    if (transaction != null && !enableButton.getSelection())
    {
      transaction.close();
      transaction = null;
    }

    instance = null;
    super.dispose();
  }

  @Override
  protected Control createContents(Composite parent)
  {
    final Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));

    enableButton = new Button(composite, SWT.CHECK);
    enableButton.setText("Enabled");
    enableButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean recorderEnabled = enableButton.getSelection();
        RecorderManager.INSTANCE.setRecorderEnabled(recorderEnabled);

        if (!recorderEnabled)
        {
          searchField.getFilterControl().setText("");
        }

        updateEnablement();
        RecorderManager.updateRecorderCheckboxState();
      }
    });

    Job job = new Job("Open recorder transaction")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        transaction = RecorderTransaction.open();

        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
            separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            policiesLabel = new Label(composite, SWT.NONE);
            policiesLabel.setText("Policies define whether to record or ignore preference changes:");

            FilterHandler filterHandler = new FilterHandler()
            {
              public void handleFilter(String filter)
              {
                ViewerFilter viewerFilter = null;
                if (filter.length() != 0)
                {
                  final String subString = filter.toLowerCase();
                  viewerFilter = new ViewerFilter()
                  {
                    @Override
                    public boolean select(Viewer viewer, Object parentElement, Object element)
                    {
                      return ((String)element).toLowerCase().contains(subString);
                    }
                  };
                }

                policiesComposite.setFilter(viewerFilter);
                policiesComposite.selectFirstPolicy();
              }
            };

            searchField = new SearchField(composite, filterHandler)
            {
              @Override
              protected void finishFilter()
              {
                policiesComposite.setFocus();
                policiesComposite.selectFirstPolicy();
              }
            };

            searchField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            policiesComposite = new RecorderPoliciesComposite(composite, SWT.BORDER | SWT.FULL_SELECTION, transaction, true);
            policiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
            policiesComposite.addCheckStateListener(new ICheckStateListener()
            {
              public void checkStateChanged(CheckStateChangedEvent event)
              {
                updateEnablement();
              }
            });

            composite.layout();
            updateEnablement();
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();

    return composite;
  }

  @Override
  protected void performDefaults()
  {
    transaction.resetPolicies();
    policiesComposite.refresh();
    super.performDefaults();
  }

  @Override
  protected void performApply()
  {
    super.performApply();
    updateEnablement();
  }

  @Override
  public boolean performOk()
  {
    if (transaction != null)
    {
      transaction.commit();
    }

    return super.performOk();
  }

  private boolean needsApply()
  {
    return transaction != null && transaction.isDirty();
  }

  static void updateEnablement()
  {
    if (instance != null)
    {
      boolean recorderEnabled = RecorderManager.INSTANCE.isRecorderEnabled();
      if (instance.enableButton != null)
      {
        instance.enableButton.setSelection(recorderEnabled);
      }

      if (instance.policiesComposite != null)
      {
        instance.policiesLabel.setEnabled(recorderEnabled);
        instance.searchField.setEnabled(recorderEnabled);
        instance.policiesComposite.setEnabled(recorderEnabled);
      }

      Button applyButton = instance.getApplyButton();
      if (applyButton != null)
      {
        boolean needsApply = instance.needsApply();
        applyButton.setEnabled(needsApply);
      }
    }
  }
}

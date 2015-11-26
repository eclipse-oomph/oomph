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
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
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
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.ui.StorageConfigurationComposite;

/**
 * @author Eike Stepper
 */
public class SynchronizerPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  public static final String ID = "org.eclipse.oomph.setup.SynchronizerPreferencePage";

  private Button enableButton;

  private StorageConfigurationComposite storageConfigurationComposite;

  private Button syncButton;

  private boolean initialEnabled;

  public SynchronizerPreferencePage()
  {
    initialEnabled = SynchronizerManager.INSTANCE.isSyncEnabled();
  }

  public void init(IWorkbench workbench)
  {
    // Do nothing.
  }

  @Override
  protected Control createContents(Composite parent)
  {
    IStorage storage = SynchronizerManager.INSTANCE.getStorage();
    IStorageService service = storage.getService();
    boolean showServices = StorageConfigurationComposite.isShowServices();

    GridLayout layout = new GridLayout(showServices ? 2 : 1, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;

    final Composite main = new Composite(parent, SWT.NONE);
    main.setLayout(layout);

    if (service == null && !showServices)
    {
      Label label = new Label(main, SWT.NONE);
      label.setText("No service available.");
    }
    else
    {
      enableButton = new Button(main, SWT.CHECK);
      enableButton.setText("Synchronize with" + (showServices ? ":" : " " + service.getServiceLabel()));
      enableButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          updateEnablement();
        }
      });

      if (showServices)
      {
        storageConfigurationComposite = new StorageConfigurationComposite(main, SWT.NONE, storage)
        {
          @Override
          protected StructuredViewer createViewer(Composite parent)
          {
            TableComboViewer viewer = new TableComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);

            TableCombo tableCombo = viewer.getTableCombo();
            tableCombo.defineColumns(2);
            tableCombo.setToolTipText("Select the service to synchronize with");

            return viewer;
          }
        };
      }

      syncButton = new Button(main, SWT.PUSH);
      syncButton.setText("Synchronize Now...");
      syncButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, layout.numColumns, 1));
      syncButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          SynchronizerManager.INSTANCE.performFullSynchronization();
        }
      });

      enableButton.setSelection(initialEnabled);
      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          updateEnablement();
        }
      });
    }

    return main;
  }

  @Override
  protected void performDefaults()
  {
    if (enableButton != null)
    {
      enableButton.setSelection(initialEnabled);
      updateEnablement();
    }

    if (storageConfigurationComposite != null)
    {
      storageConfigurationComposite.performDefaults();
    }

    super.performDefaults();
  }

  @Override
  protected void performApply()
  {
    if (enableButton != null)
    {
      updateEnablement();
    }

    super.performApply();
  }

  @Override
  public boolean performOk()
  {
    if (enableButton != null)
    {
      if (storageConfigurationComposite != null)
      {
        storageConfigurationComposite.performApply();
      }

      SynchronizerManager.INSTANCE.setSyncEnabled(initialEnabled = enableButton.getSelection());
    }

    return super.performOk();
  }

  private boolean needsApply()
  {
    try
    {
      if (enableButton != null && enableButton.getSelection() != initialEnabled)
      {
        return true;
      }

      if (storageConfigurationComposite != null)
      {
        return storageConfigurationComposite.isDirty();
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }

    return false;
  }

  private void updateEnablement()
  {
    boolean enabled = enableButton != null ? enableButton.getSelection() : false;

    if (storageConfigurationComposite != null)
    {
      storageConfigurationComposite.setEnabled(enabled);
    }

    if (syncButton != null)
    {
      syncButton.setEnabled(enabled);
    }

    boolean needsApply = needsApply();

    Button defaultsButton = getDefaultsButton();
    if (defaultsButton != null)
    {
      defaultsButton.setEnabled(needsApply);
    }

    Button applyButton = getApplyButton();
    if (applyButton != null)
    {
      applyButton.setEnabled(needsApply);
    }
  }
}

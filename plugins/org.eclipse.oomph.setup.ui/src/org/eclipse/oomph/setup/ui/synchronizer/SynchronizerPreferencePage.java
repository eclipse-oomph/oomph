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

import org.eclipse.oomph.setup.internal.sync.SynchronizerCredentials;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Eike Stepper
 */
public class SynchronizerPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  public static final String ID = "org.eclipse.oomph.setup.SynchronizerPreferencePage";

  private Button enableButton;

  private TableCombo serviceCombo;

  private SynchronizerLoginComposite loginComposite;

  private boolean initialEnabled;

  private SynchronizerService initialService;

  private SynchronizerCredentials initialCredentials;

  private SynchronizerService currentService;

  public SynchronizerPreferencePage()
  {
    initialEnabled = SynchronizerManager.INSTANCE.isSyncEnabled();
    initialService = SynchronizerManager.INSTANCE.getService();
    initialCredentials = initialService.getCredentials();
  }

  public void init(IWorkbench workbench)
  {
    // Do nothing.
  }

  @Override
  protected Control createContents(Composite parent)
  {
    loginComposite = new SynchronizerLoginComposite(parent, SWT.NONE, 0, 0)
    {
      @Override
      protected void createUI(Composite parent, int columns)
      {
        enableButton = new Button(parent, SWT.CHECK);
        enableButton.setText("Synchronize with:");
        enableButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            updateEnablement();
          }
        });

        serviceCombo = new TableCombo(parent, SWT.BORDER | SWT.READ_ONLY);
        serviceCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns - 1, 1));
        serviceCombo.defineColumns(2);
        serviceCombo.setToolTipText("Select the service to synchronize with");

        Table table = serviceCombo.getTable();
        table.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            if (e.item instanceof TableItem)
            {
              TableItem item = (TableItem)e.item;
              currentService = (SynchronizerService)item.getData();
            }
          }
        });

        for (SynchronizerService service : SynchronizerService.Registry.INSTANCE.getServices())
        {
          TableItem item = new TableItem(table, SWT.NONE);
          item.setData(service);
          item.setImage(0, SetupUIPlugin.INSTANCE.getSWTImage("sync/Remote"));
          item.setText(0, service.getLabel());
          item.setText(1, service.getServiceURI().toString());
        }

        super.createUI(parent, columns);
      }

      @Override
      protected void validate()
      {
        updateEnablement();
      }
    };

    enableButton.setSelection(initialEnabled);
    selectService(initialService);

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        updateEnablement();
      }
    });

    return loginComposite;
  }

  @Override
  protected void performDefaults()
  {
    enableButton.setSelection(initialEnabled);
    selectService(initialService);
    loginComposite.setCredentials(initialCredentials);

    super.performDefaults();
    updateEnablement();
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
    currentService.setCredentials(initialCredentials = loginComposite.getCredentials());
    SynchronizerManager.INSTANCE.setSyncEnabled(initialEnabled = enableButton.getSelection());
    SynchronizerManager.INSTANCE.setService(initialService = currentService);
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

      if (!ObjectUtil.equals(currentService, initialService))
      {
        return true;
      }

      SynchronizerCredentials currentCredentials = loginComposite.getCredentials();
      if (!ObjectUtil.equals(currentCredentials, initialCredentials))
      {
        return true;
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }

    return false;
  }

  private void selectService(SynchronizerService service)
  {
    if (service != null)
    {
      Table table = serviceCombo.getTable();
      int i = 0;

      for (TableItem item : table.getItems())
      {
        SynchronizerService itemService = (SynchronizerService)item.getData();
        if (ObjectUtil.equals(itemService, service))
        {
          currentService = itemService;
          serviceCombo.select(i);
          loginComposite.setService(currentService);
          return;
        }

        ++i;
      }

      if (serviceCombo.getItemCount() != 0)
      {
        currentService = (SynchronizerService)table.getItem(0).getData();
        serviceCombo.select(0);
        loginComposite.setService(currentService);
        return;
      }
    }

    currentService = null;
    loginComposite.setService(null);
  }

  private void updateEnablement()
  {
    boolean enabled = enableButton.getSelection();
    serviceCombo.setEnabled(enabled);
    loginComposite.setEnabled(enabled);

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

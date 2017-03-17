/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.Impact;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.preference.PreferenceDialog;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.ui.StorageConfigurationComposite;

/**
 * @author Eike Stepper
 */
public class SynchronizerPreferencePage extends AbstractPreferencePage
{
  public static final String ID = "org.eclipse.oomph.setup.SynchronizerPreferencePage";

  private final PageHandler pageHandler;

  public SynchronizerPreferencePage()
  {
    pageHandler = SynchronizerManager.Availability.AVAILABLE ? new StorageHandler() : new DisabledHandler();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    return pageHandler.createContents(parent);
  }

  @Override
  protected void performDefaults()
  {
    pageHandler.performDefaults();
    super.performDefaults();
  }

  @Override
  protected void performApply()
  {
    pageHandler.performApply();
    super.performApply();
  }

  @Override
  public boolean performOk()
  {
    pageHandler.performOk();
    return super.performOk();
  }

  @Override
  public void createControl(Composite parent)
  {
    super.createControl(parent);
    pageHandler.updateButtons();
  }

  /**
   * @author Ed Merks
   */
  private abstract class PageHandler
  {
    public abstract Control createContents(Composite parent);

    public void updateButtons()
    {
    }

    public void performOk()
    {
    }

    public void performApply()
    {
    }

    public void performDefaults()
    {
    }
  }

  /**
   * @author Ed Merks
   */
  private class DisabledHandler extends PageHandler
  {
    @Override
    public Control createContents(Composite parent)
    {
      Text text = new Text(parent, SWT.READ_ONLY);
      text.setText("User storage support has not been installed.");
      return text;
    }

    @Override
    public void updateButtons()
    {
      getApplyButton().setEnabled(false);
      getDefaultsButton().setEnabled(false);
    }
  }

  /**
   * @author Ed Merks
   */
  private class StorageHandler extends PageHandler
  {
    private Button enableButton;

    private StorageConfigurationComposite storageConfigurationComposite;

    private Button syncButton;

    private Button deleteButton;

    private boolean initialEnabled;

    public StorageHandler()
    {
      initialEnabled = SynchronizerManager.INSTANCE.isSyncEnabled();
    }

    @Override
    public Control createContents(Composite parent)
    {
      final IStorage storage = SynchronizerManager.INSTANCE.getStorage();
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
            performApply();

            final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            final Display display = window.getShell().getDisplay();

            UIUtil.asyncExec(display, new Runnable()
            {
              public void run()
              {
                try
                {
                  Shell activeShell = display.getActiveShell();
                  Object data = activeShell.getData();
                  if (data instanceof PreferenceDialog)
                  {
                    PreferenceDialog preferenceDialog = (PreferenceDialog)data;
                    ReflectUtil.invokeMethod("okPressed", preferenceDialog);
                  }
                }
                catch (Throwable ex)
                {
                  SetupUIPlugin.INSTANCE.log(ex);
                }
              }
            });

            UIUtil.asyncExec(display, new Runnable()
            {
              public void run()
              {
                try
                {
                  Shell shell = SetupPropertyTester.getHandlingShell();
                  if (shell != null)
                  {
                    shell.setVisible(true);
                  }
                  else
                  {
                    Impact impact = SynchronizerManager.INSTANCE.performFullSynchronization();
                    if (impact != null && impact.hasLocalImpact())
                    {
                      SetupWizard.Updater.perform(false);
                    }
                  }
                }
                catch (Throwable ex)
                {
                  SetupUIPlugin.INSTANCE.log(ex);
                }
              }
            });
          }
        });

        // deleteButton = new Button(main, SWT.PUSH);
        // deleteButton.setText("Delete Remote Data");
        // deleteButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, layout.numColumns, 1));
        // deleteButton.addSelectionListener(new SelectionAdapter()
        // {
        // @Override
        // public void widgetSelected(SelectionEvent e)
        // {
        // Shell shell = UIUtil.getShell();
        //
        // try
        // {
        // ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
        //
        // dialog.run(true, true, new IRunnableWithProgress()
        // {
        // public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        // {
        // try
        // {
        // RemoteDataProvider remoteDataProvider = new RemoteDataProvider(storage);
        // remoteDataProvider.delete();
        // }
        // catch (Throwable ex)
        // {
        // throw new InvocationTargetException(ex);
        // }
        // }
        // });
        // }
        // catch (InvocationTargetException ex)
        // {
        // Throwable cause = ex.getCause();
        // SetupUIPlugin.INSTANCE.log(cause);
        // ErrorDialog.open(cause);
        // }
        // catch (InterruptedException ex)
        // {
        // //$FALL-THROUGH$
        // }
        // }
        // });

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
    public void performDefaults()
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
    }

    @Override
    public void performApply()
    {
      if (enableButton != null)
      {
        updateEnablement();
      }
    }

    @Override
    public void performOk()
    {
      if (enableButton != null)
      {
        if (storageConfigurationComposite != null)
        {
          storageConfigurationComposite.performApply();
        }

        SynchronizerManager.INSTANCE.setSyncEnabled(initialEnabled = enableButton.getSelection());
      }
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

      if (deleteButton != null)
      {
        deleteButton.setEnabled(enabled);
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
}

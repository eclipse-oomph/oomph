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

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.ui.SearchField;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class RecorderPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  private static RecorderPreferencePage instance;

  private final URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();

  private RecorderTransaction transaction;

  private Button enableButton;

  private TableCombo targetCombo;

  private ToolButton editButton;

  private Label policiesLabel;

  private SearchField policiesFilter;

  private RecorderPoliciesComposite policiesComposite;

  private Button initializePreferencesButton;

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
    if (transaction != null)
    {
      closeRecorderTransaction();
    }

    instance = null;
    super.dispose();
  }

  @Override
  protected Control createContents(Composite parent)
  {
    if (RecorderManager.INSTANCE.hasTemporaryRecorderTarget())
    {
      Label label = new Label(parent, SWT.NONE);
      label.setText("The preference recorder settings are unavailable while recording into the selected file.");
      return label;
    }

    int columns = 3;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(UIUtil.createGridLayout(columns));

    enableButton = new Button(composite, SWT.CHECK);
    enableButton.setText("Record into:");
    enableButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enableRecorder = enableButton.getSelection();
        RecorderManager.INSTANCE.setRecorderEnabled(enableRecorder);

        if (!enableRecorder)
        {
          policiesFilter.getFilterControl().setText("");
        }

        updateEnablement();
        RecorderManager.updateRecorderToggleButton();

        if (enableRecorder)
        {
          boolean firstTime = SynchronizerManager.INSTANCE.offerFirstTimeConnect(getShell());
          RecorderManager.INSTANCE.startEarlySynchronization(firstTime);
        }
      }
    });

    targetCombo = new TableCombo(composite, SWT.BORDER | SWT.READ_ONLY);
    targetCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    targetCombo.defineColumns(2);
    targetCombo.setToolTipText("Select the recorder target file");
    targetCombo.getTable().addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (e.item instanceof TableItem)
        {
          TableItem item = (TableItem)e.item;
          URI uri = (URI)item.getData();

          URI oldURI = RecorderManager.INSTANCE.setRecorderTarget(uri);
          if (oldURI != null)
          {
            if (transaction != null)
            {
              int dirtyPolicies = transaction.getPolicies(false).size();
              if (dirtyPolicies != 0)
              {
                if (MessageDialog.openQuestion(getShell(), "Changed Recorder Policies",
                    "Do you want to save the changed policies before you change the recorder target?"))
                {
                  transaction.commit();
                }
              }

              closeRecorderTransaction();
            }

            openRecorderTransaction(null);
          }
        }
      }
    });

    ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();
    SetupContext setupContext = SetupContext.getSelf();
    Set<Scope> targets = new HashSet<Scope>();

    addTarget(adapterFactory, targets, setupContext.getUser());

    Installation installation = setupContext.getInstallation();
    addTarget(adapterFactory, targets, installation);

    Workspace workspace = setupContext.getWorkspace();
    addTarget(adapterFactory, targets, workspace);

    if (installation != null)
    {
      addTarget(adapterFactory, targets, installation.getProductVersion());
    }

    if (workspace != null)
    {
      for (Stream stream : workspace.getStreams())
      {
        addTarget(adapterFactory, targets, stream);
      }
    }

    URI uri = RecorderManager.INSTANCE.getRecorderTarget();
    selectRecorderTarget(uri);
    adapterFactory.dispose();

    editButton = new ToolButton(composite, 0, SetupUIPlugin.INSTANCE.getSWTImage("edit.gif"), false);
    editButton.setToolTipText("Open the recorder target file");
    editButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        SetupEditorSupport.getEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), RecorderManager.INSTANCE.getRecorderTarget(), true);

        Map<String, Boolean> policies = null;
        if (transaction != null)
        {
          policies = transaction.getPolicies(false);
          closeRecorderTransaction();
        }

        openRecorderTransaction(policies);
        updateEnablement();
      }
    });

    Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
    separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns, 1));

    policiesLabel = new Label(composite, SWT.NONE);
    policiesLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns, 1));
    policiesLabel.setText("Check the preferences to record and uncheck those to ignore:");

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

    policiesFilter = new SearchField(composite, filterHandler)
    {
      @Override
      protected void finishFilter()
      {
        policiesComposite.setFocus();
        policiesComposite.selectFirstPolicy();
      }
    };

    policiesFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns, 1));

    policiesComposite = new RecorderPoliciesComposite(composite, SWT.BORDER | SWT.FULL_SELECTION, true);
    policiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, columns, 1));
    policiesComposite.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        updateEnablement();
      }
    });

    updateEnablement();
    openRecorderTransaction(null);
    return composite;
  }

  @Override
  public void createControl(Composite parent)
  {
    super.createControl(parent);
    ((GridLayout)initializePreferencesButton.getParent().getLayout()).numColumns += 1;
  }

  @Override
  protected void contributeButtons(Composite parent)
  {
    initializePreferencesButton = new Button(parent, SWT.PUSH);
    initializePreferencesButton.setText("Initialize...");

    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    Dialog.applyDialogFont(initializePreferencesButton);
    Point minButtonSize = initializePreferencesButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    data.widthHint = Math.max(widthHint, minButtonSize.x);
    initializePreferencesButton.setLayoutData(data);

    final PreferenceManager preferenceManager = PlatformUI.getWorkbench().getPreferenceManager();
    initializePreferencesButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        new PreferenceInitializationDialog((PreferenceDialog)getContainer(), preferenceManager).open();
      }
    });

    Set<String> preferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();
    List<IPreferenceNode> preferenceNodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER);
    for (IPreferenceNode element : preferenceNodes)
    {
      String id = element.getId();
      if (preferencePages.contains(id))
      {
        initializePreferencesButton.setEnabled(false);
        break;
      }
    }
  }

  private void openRecorderTransaction(final Map<String, Boolean> policies)
  {
    Job job = new Job("Open recorder transaction")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        transaction = RecorderTransaction.open();
        transaction.setPolicies(policies);

        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            updateEnablement();
            policiesComposite.setRecorderTransaction(transaction);
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();
  }

  private void closeRecorderTransaction()
  {
    transaction.close();
    transaction = null;
  }

  @Override
  protected void performDefaults()
  {
    transaction.resetPolicies();
    policiesComposite.setRecorderTransaction(transaction);
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
      closeRecorderTransaction();
    }

    return super.performOk();
  }

  private boolean needsApply()
  {
    return transaction != null && transaction.isDirty();
  }

  private void selectRecorderTarget(URI uri)
  {
    int i = 0;
    for (TableItem item : targetCombo.getTable().getItems())
    {
      URI itemURI = (URI)item.getData();
      if (ObjectUtil.equals(itemURI, uri))
      {
        targetCombo.select(i);
        return;
      }

      ++i;
    }
  }

  private boolean addTarget(ComposedAdapterFactory adapterFactory, Set<Scope> targets, Scope target)
  {
    if (target != null && targets.add(target))
    {
      URI uri = EcoreUtil.getURI(target);
      if (uri != null)
      {
        Map<?, ?> options = Collections.singletonMap(URIConverter.OPTION_REQUESTED_ATTRIBUTES, Collections.singleton(URIConverter.ATTRIBUTE_READ_ONLY));
        Map<String, ?> attributes = uriConverter.getAttributes(uri, options);
        boolean writable = !Boolean.TRUE.equals(attributes.get(URIConverter.ATTRIBUTE_READ_ONLY));

        if (writable)
        {
          Scope parentScope = target.getParentScope();
          if (parentScope instanceof Product || parentScope instanceof Project)
          {
            addTarget(adapterFactory, targets, parentScope);
          }

          String location = uri.trimFragment().toString();
          ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(target, IItemLabelProvider.class);

          TableItem item = new TableItem(targetCombo.getTable(), SWT.NONE);
          item.setData(uri);
          item.setImage(0, SetupUIPlugin.INSTANCE.getSWTImage(SetupLabelProvider.getImageDescriptor(itemProvider, target)));
          item.setText(0, SetupLabelProvider.getText(itemProvider, target));
          item.setText(1, location);
          return true;
        }
      }
    }

    return false;
  }

  static void updateEnablement()
  {
    if (instance != null)
    {
      boolean recorderEnabled = RecorderManager.INSTANCE.isRecorderEnabled();
      if (instance.enableButton != null)
      {
        instance.enableButton.setSelection(recorderEnabled);
        instance.targetCombo.setEnabled(recorderEnabled);
        instance.editButton.setEnabled(recorderEnabled && (instance.transaction == null || instance.transaction.getEditor() == null));
        instance.policiesLabel.setEnabled(recorderEnabled);
        instance.policiesFilter.setEnabled(recorderEnabled);
        instance.policiesComposite.setEnabled(recorderEnabled);
      }

      Button applyButton = instance.getApplyButton();
      if (applyButton != null)
      {
        boolean needsApply = instance.needsApply();
        applyButton.setEnabled(needsApply);
      }

      Button defaultsButton = instance.getDefaultsButton();
      if (defaultsButton != null)
      {
        defaultsButton.setEnabled(recorderEnabled);
      }
    }
  }
}

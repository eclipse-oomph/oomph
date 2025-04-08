/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.Snapshot;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.provider.PreferenceTaskItemProvider;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class SynchronizerDialog extends AbstractSetupDialog
{
  /**
   * If set to <code>true</code> the effective sync actions are dumped to the console.
   * <p>
   * <b>Should never be committed with a <code>true</code> value!</b>
   *
   * @see #adjustSyncActions(Set)
   */
  private static final boolean DEBUG_SYNC_ACTIONS = false;

  private static final int ICON = 16;

  private static final int SPACE = 5;

  private static final int H_INDENT = 2 * SPACE;

  private static final int V_INDENT = 1;

  private static String POLICY_HELP_MESSAGE = " " + Messages.SynchronizerDialog_policyHelpMessage; //$NON-NLS-1$

  private final PaintHandler paintHandler = new PaintHandler();

  private final MouseHandler mouseHandler = new MouseHandler();

  private final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

  private final ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();

  private final Map<SetupTask, TaskItem> taskItems = new HashMap<>();

  private final Map<String, TaskItem> taskItemsByPreferenceKey = new HashMap<>();

  private final Mode mode;

  private final RecorderTransaction recorderTransaction;

  private final Set<URI> recorderValuesToRemove = new HashSet<>();

  private final Scope recorderTarget;

  private final Image recorderTargetImage;

  private final String recorderTargetText;

  private final Synchronization synchronization;

  private Tree tree;

  private boolean treePopulated;

  private TreeColumn labelColumn;

  private TreeColumn localColumn;

  private TreeColumn remoteColumn;

  private final ColumnManager[] columnManagers = { null, null, null };

  private LocalColumnManager localColumnManager;

  private RemoteColumnManager remoteColumnManager;

  private Text valueText;

  private Button enableRecorderButton;

  private boolean enableRecorder = RecorderManager.INSTANCE.isRecorderEnabled();

  private Button okButton;

  public SynchronizerDialog(Shell parentShell)
  {
    this(parentShell, null, null);
  }

  public SynchronizerDialog(Shell parentShell, RecorderTransaction transaction, Synchronization sync)
  {
    super(parentShell, getTitle(transaction), 800, 600, SetupUIPlugin.INSTANCE, true);
    PreferenceTaskItemProvider.setShortenLabelText(adapterFactory);

    if (transaction != null)
    {
      recorderTarget = RecorderManager.INSTANCE.getRecorderTargetObject(resourceSet);

      ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(recorderTarget, IItemLabelProvider.class);
      recorderTargetImage = SetupUIPlugin.INSTANCE.getSWTImage(SetupLabelProvider.getImageDescriptor(itemProvider, recorderTarget));
      recorderTargetText = SetupLabelProvider.getText(itemProvider, recorderTarget);

      if (sync != null)
      {
        mode = Mode.RECORD_AND_SYNC;
        recorderTransaction = transaction;
        synchronization = sync;
      }
      else
      {
        mode = Mode.RECORD;
        recorderTransaction = transaction;
        synchronization = null;
      }
    }
    else
    {
      mode = Mode.SYNC;
      recorderTransaction = null;
      recorderTarget = null;
      recorderTargetImage = null;
      recorderTargetText = null;
      synchronization = sync;
    }
  }

  public boolean isEnableRecorder()
  {
    return enableRecorder;
  }

  @Override
  public String getHelpPath()
  {
    return SetupUIPlugin.INSTANCE.getSymbolicName() + "/html/RecorderHelp.html"; //$NON-NLS-1$
  }

  @Override
  protected String getShellText()
  {
    return getTitle(recorderTransaction);
  }

  @Override
  protected String getDefaultMessage()
  {
    switch (mode)
    {
      case RECORD:
        if (recorderTarget instanceof User)
        {
          return Messages.SynchronizerDialog_defaultMessage_selectWhatToRecord_allWorkspaces + POLICY_HELP_MESSAGE;
        }

        if (recorderTarget instanceof Installation)
        {
          return Messages.SynchronizerDialog_defaultMessage_selectWhatToRecord_installationWorkspaces + POLICY_HELP_MESSAGE;
        }

        if (recorderTarget instanceof Workspace)
        {
          return Messages.SynchronizerDialog_defaultMessage_selectWhatToRecord_workspace + POLICY_HELP_MESSAGE;
        }

        return NLS.bind(Messages.SynchronizerDialog_defaultMessage_selectWhatToRecord_intoRecorderTarget, recorderTargetText) + POLICY_HELP_MESSAGE;

      case SYNC:
        return NLS.bind(Messages.SynchronizerDialog_defaultMessage_selectWhatToSyncWithRemote, SynchronizerManager.INSTANCE.getServiceLabel())
            + POLICY_HELP_MESSAGE;

      case RECORD_AND_SYNC:
        return NLS.bind(Messages.SynchronizerDialog_defaultMessage_selectWhatToRecordAndSync, SynchronizerManager.INSTANCE.getServiceLabel())
            + POLICY_HELP_MESSAGE;

      default:
        return null;
    }
  }

  @Override
  protected void createUI(Composite parent)
  {
    parent.addDisposeListener(new DisposeListener()
    {
      @Override
      public void widgetDisposed(DisposeEvent e)
      {
        if (localColumnManager != null)
        {
          localColumnManager.dispose();
        }

        if (remoteColumnManager != null)
        {
          remoteColumnManager.dispose();
        }

        adapterFactory.dispose();
      }
    });

    initializeDialogUnits(parent);

    SashForm sashForm = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    tree = new Tree(sashForm, SWT.FULL_SELECTION);
    tree.setHeaderVisible(true);
    tree.setLinesVisible(true);
    tree.addListener(SWT.MeasureItem, paintHandler);
    tree.addListener(SWT.PaintItem, paintHandler);
    tree.addListener(SWT.MouseDown, mouseHandler);
    tree.setFocus();

    labelColumn = new TreeColumn(tree, SWT.NONE);
    labelColumn.setText(Messages.SynchronizerDialog_column_label);
    labelColumn.setWidth(200);
    labelColumn.setResizable(true);

    int column = 1;

    if (mode.isRecord())
    {
      localColumn = new TreeColumn(tree, SWT.NONE);
      localColumn.setText(Messages.SynchronizerDialog_column_local);
      localColumn.setWidth(200);
      localColumn.setResizable(true);

      localColumnManager = new LocalColumnManager(this);
      columnManagers[column++] = localColumnManager;
    }

    if (mode.isSync())
    {
      remoteColumn = new TreeColumn(tree, SWT.NONE);
      remoteColumn.setText(Messages.SynchronizerDialog_column_remote);
      remoteColumn.setWidth(200);
      remoteColumn.setResizable(true);

      remoteColumnManager = new RemoteColumnManager(this, SynchronizerManager.INSTANCE.getServiceLabel());
      columnManagers[column++] = remoteColumnManager;
    }

    populateTree();
    initColumnResizer();

    valueText = new Text(sashForm, SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
    valueText.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

    tree.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent event)
      {
        String value = ""; //$NON-NLS-1$
        if (event.item instanceof TaskItem)
        {
          TaskItem taskItem = (TaskItem)event.item;
          SetupTask task = taskItem.getTask();
          if (task instanceof PreferenceTask)
          {
            PreferenceTask preferenceTask = (PreferenceTask)task;
            value = preferenceTask.getValue();
          }
        }

        valueText.setText(StringUtil.safe(value).replace('\u0000', '\n'));
      }
    });

    Listener scrollBarListener = new Listener()
    {
      protected boolean changing;

      @Override
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

    showFirstTimeHelp(this);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    if (mode.isRecord())
    {
      enableRecorderButton = createCheckbox(parent, Messages.SynchronizerDialog_enableRecorderButton_text);
      enableRecorderButton.setToolTipText(Messages.SynchronizerDialog_enableRecorderButton_tooltip);
      enableRecorderButton.setSelection(enableRecorder);
      enableRecorderButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          validatePage();
        }
      });
    }

    super.createButtonsForButtonBar(parent);
    okButton = getButton(IDialogConstants.OK_ID);
    validatePage();
  }

  protected void validatePage()
  {
    if (treePopulated)
    {
      if (mode.isRecord())
      {
        if (enableRecorderButton != null)
        {
          enableRecorder = enableRecorderButton.getSelection();

          tree.setEnabled(enableRecorder);

          if (valueText != null)
          {
            valueText.setVisible(enableRecorder);
          }
        }
      }

      boolean noConflicts = true;
      if (mode.isSync())
      {
        noConflicts = synchronization.getUnresolvedActions().isEmpty();
      }

      if (okButton != null)
      {
        okButton.setEnabled(noConflicts);
      }
    }
  }

  @Override
  protected void okPressed()
  {
    if (mode.isRecord())
    {
      Map<URI, Pair<String, String>> preferences = recorderTransaction.getPreferences();
      if (preferences != null)
      {
        preferences.keySet().removeAll(recorderValuesToRemove);
      }
    }

    super.okPressed();
  }

  private ColumnManager getColumnManager(int column)
  {
    if (column < columnManagers.length)
    {
      return columnManagers[column];
    }

    return null;
  }

  private void initColumnResizer()
  {
    ControlAdapter columnResizer = new ControlAdapter()
    {
      private int clientWidth = 0;

      private int labelWidth = 0;

      private int recorderWidth = 0;

      private int synchronizerWidth = 0;

      private boolean resizing;

      @Override
      public void controlResized(ControlEvent e)
      {
        if (resizing)
        {
          return;
        }

        Rectangle clientArea = tree.getClientArea();
        int clientWidth = clientArea.width - clientArea.x;
        // ScrollBar bar = tree.getVerticalBar();
        // if (bar != null && bar.isVisible())
        // {
        // clientWidth -= bar.getSize().x;
        // }

        int labelWidth = labelColumn.getWidth();
        int recorderWidth = localColumn == null ? 0 : localColumn.getWidth();
        int synchronizerWidth = remoteColumn == null ? 0 : remoteColumn.getWidth();

        boolean inputChanged = e == null;
        if (inputChanged || clientWidth != this.clientWidth || labelWidth != this.labelWidth || recorderWidth != this.recorderWidth
            || synchronizerWidth != this.synchronizerWidth)
        {
          try
          {
            resizing = true;
            tree.setRedraw(false);

            TreeItem[] items = tree.getItems();
            if (items.length == 0)
            {
              recorderWidth = clientWidth / 2;
              labelColumn.setWidth(clientWidth - recorderWidth - synchronizerWidth);

              if (localColumn != null)
              {
                localColumn.setWidth(recorderWidth);
              }

              if (remoteColumn != null)
              {
                remoteColumn.setWidth(synchronizerWidth);
              }
            }
            else
            {
              if (localColumn != null)
              {
                localColumn.pack();
                recorderWidth = localColumn.getWidth() + 10;
                localColumn.setWidth(recorderWidth);
              }

              if (remoteColumn != null)
              {
                remoteColumn.pack();
                synchronizerWidth = remoteColumn.getWidth() + 10;
                remoteColumn.setWidth(synchronizerWidth);
              }

              labelWidth = clientWidth - recorderWidth - synchronizerWidth;
              labelColumn.setWidth(labelWidth);
            }
          }
          finally
          {
            this.clientWidth = clientWidth;
            this.labelWidth = labelWidth;
            this.recorderWidth = recorderWidth;
            this.synchronizerWidth = synchronizerWidth;

            tree.setRedraw(true);
            resizing = false;
          }
        }
      }
    };

    tree.addControlListener(columnResizer);
    labelColumn.addControlListener(columnResizer);

    if (localColumn != null)
    {
      localColumn.addControlListener(columnResizer);
    }

    if (remoteColumn != null)
    {
      remoteColumn.addControlListener(columnResizer);
    }

    columnResizer.controlResized(null);
  }

  private Map<URI, String> getRecorderPreferences()
  {
    Map<URI, String> result = new HashMap<>();

    Map<String, PreferenceTask> commitResult = recorderTransaction.getCommitResult();
    if (commitResult != null)
    {
      for (PreferenceTask preferenceTask : commitResult.values())
      {
        URI uri = PreferencesFactory.eINSTANCE.createURI(preferenceTask.getKey());
        String value = preferenceTask.getValue();
        result.put(uri, value);
      }
    }
    else
    {
      for (Map.Entry<URI, Pair<String, String>> entry : recorderTransaction.getPreferences().entrySet())
      {
        URI uri = entry.getKey();
        String value = entry.getValue().getElement2();
        result.put(uri, value);
      }
    }

    return result;
  }

  private void addTask(PreferenceTask task, String pluginID, final Map<String, Set<SetupTask>> tasks, final Map<SetupTask, String> labels,
      final Map<SetupTask, Image> images, Map<String, CompoundTask> compounds)
  {
    CollectionUtil.add(tasks, pluginID, task);

    // Put the preference task into a compound task so that PreferenceTaskItemProvider shortens the label.
    CompoundTask compound = compounds.get(pluginID);
    if (compound == null)
    {
      compound = SetupFactory.eINSTANCE.createCompoundTask(pluginID);
    }

    compound.getSetupTasks().add(task);

    // Remember task label.
    ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(task, IItemLabelProvider.class);
    labels.put(task, SetupLabelProvider.getText(itemProvider, task));
    images.put(task, SetupUIPlugin.INSTANCE.getSWTImage(SetupLabelProvider.getImageDescriptor(itemProvider, task)));
  }

  private void populateTree()
  {
    final Map<String, Set<SetupTask>> tasks = new HashMap<>();
    final Map<SetupTask, String> labels = new HashMap<>();
    final Map<SetupTask, Image> images = new HashMap<>();

    if (mode.isRecord())
    {
      Map<String, CompoundTask> compounds = new HashMap<>();
      Map<String, Boolean> cleanLocalPolicies = recorderTransaction.getPolicies(true);
      Map<String, Boolean> dirtyLocalPolicies = recorderTransaction.getPolicies(false);

      Map<URI, String> preferences = getRecorderPreferences();
      for (Map.Entry<URI, String> entry : preferences.entrySet())
      {
        URI uri = entry.getKey();
        String pluginID = uri.segment(0);

        String key = PreferencesFactory.eINSTANCE.convertURI(uri);
        String value = entry.getValue();

        PreferenceTask task = SetupFactory.eINSTANCE.createPreferenceTask();
        task.setKey(key);
        task.setValue(value);

        boolean remotePolicyMissing = false;
        boolean conflict = false;

        if (mode.isSync())
        {
          EMap<String, SyncPolicy> remotePolicies = synchronization.getRemotePolicies();

          Map<String, String> preferenceIDs = synchronization.getPreferenceIDs();
          String syncID = preferenceIDs.get(key);
          if (syncID != null)
          {
            task.setID(syncID);

            SyncAction syncAction = synchronization.getActions().get(syncID);
            if (syncAction != null)
            {
              if (syncAction.getComputedType() == SyncActionType.CONFLICT)
              {
                conflict = true;
                task.eAdapters().add(new ConflictAdapter());
              }
            }

            SyncPolicy remotePolicy = remotePolicies.get(syncID);
            if (remotePolicy == null)
            {
              remotePolicyMissing = true;
              if (cleanLocalPolicies.containsKey(key))
              {
                task.eAdapters().add(new LocalChoiceDisabledAdapter());
              }
            }
          }
        }

        // Only offer preferences with *new* policies for review.
        if (dirtyLocalPolicies.containsKey(key) || remotePolicyMissing || conflict)
        {
          addTask(task, pluginID, tasks, labels, images, compounds);
        }
      }
    }
    else // if (mode.isSync())
    {
      Map<String, CompoundTask> compounds = new HashMap<>();

      Map<String, SyncAction> syncActions = synchronization.getActions();
      for (Entry<String, SyncAction> entry : syncActions.entrySet())
      {
        String syncID = entry.getKey();
        SyncAction syncAction = entry.getValue();

        Map.Entry<String, String> preference = syncAction.getPreference();
        String key = preference.getKey();
        String value = preference.getValue();

        PreferenceTask task = SetupFactory.eINSTANCE.createPreferenceTask();
        task.setKey(key);
        task.setValue(value);
        task.setID(syncID);

        if (syncAction.getComputedType() == SyncActionType.CONFLICT)
        {
          task.eAdapters().add(new ConflictAdapter());
        }

        URI uri = PreferencesFactory.eINSTANCE.createURI(key);
        String pluginID = uri.segment(0);

        addTask(task, pluginID, tasks, labels, images, compounds);
      }
    }

    populateTree(tasks, labels, images);
  }

  private void populateTree(final Map<String, Set<SetupTask>> tasks, final Map<SetupTask, String> labels, final Map<SetupTask, Image> images)
  {
    List<String> nodes = new ArrayList<>(tasks.keySet());
    Collections.sort(nodes);

    Comparator<SetupTask> comparator = new Comparator<>()
    {
      @Override
      public int compare(SetupTask t1, SetupTask t2)
      {
        String l1 = labels.get(t1);
        String l2 = labels.get(t2);
        return l1.compareTo(l2);
      }
    };

    for (String node : nodes)
    {
      NodeItem nodeItem = new NodeItem(tree, adapterFactory, node);

      List<SetupTask> list = new ArrayList<>(tasks.get(node));
      Collections.sort(list, comparator);

      for (SetupTask task : list)
      {
        EList<Adapter> adapters = task.eAdapters();
        boolean conflict = EcoreUtil.getAdapter(adapters, ConflictAdapter.class) != null;
        boolean localChoiceDisabled = EcoreUtil.getAdapter(adapters, LocalChoiceDisabledAdapter.class) != null;

        Image image = images.get(task);
        String text = labels.get(task);

        TaskItem taskItem = new TaskItem(nodeItem, task, conflict, localChoiceDisabled, image, text);
        taskItems.put(task, taskItem);

        if (task instanceof PreferenceTask)
        {
          PreferenceTask preferenceTask = (PreferenceTask)task;
          taskItemsByPreferenceKey.put(preferenceTask.getKey(), taskItem);
        }

        if (localColumnManager != null)
        {
          Choice[] choices = localColumnManager.getChoices(taskItem);
          taskItem.setLocalChoice(choices[0]);
        }

        if (remoteColumnManager != null)
        {
          Choice[] choices = remoteColumnManager.getChoices(taskItem);
          taskItem.setRemoteChoice(choices[0]);
        }
      }

      nodeItem.setExpanded(true);
    }

    synchronizeLocal(false);
    treePopulated = true;
  }

  private void synchronizeLocal(boolean resynchronize)
  {
    try
    {
      if (mode.isSync())
      {
        if (resynchronize)
        {
          Snapshot localSnapshot = synchronization.getSynchronizer().getLocalSnapshot();
          SyncUtil.deleteFile(localSnapshot.getNewFile());
          RecorderManager.copyRecorderTarget(recorderTarget, localSnapshot.getFolder());
        }

        Set<String> preferenceKeys = null;
        if (mode.isRecord())
        {
          Map<String, PolicyAndValue> preferenceChanges = new HashMap<>();
          for (TaskItem taskItem : taskItems.values())
          {
            SetupTask task = taskItem.getTask();
            if (task instanceof PreferenceTask)
            {
              PreferenceTask preferenceTask = (PreferenceTask)task;
              String key = preferenceTask.getKey();

              Choice localChoice = taskItem.getLocalChoice();
              if (localChoice instanceof Choice.RecordAlways)
              {
                preferenceChanges.put(key, new PolicyAndValue(preferenceTask.getValue()));
              }
              else if (localChoice instanceof Choice.RecordNever || localChoice instanceof Choice.RecordSkip)
              {
                preferenceChanges.put(key, new PolicyAndValue());
              }
            }
          }

          preferenceKeys = adjustLocalSnapshot(synchronization, preferenceChanges);
        }

        synchronization.synchronizeLocal();
        adjustSyncActions(preferenceKeys);
      }
    }
    catch (IOException ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
  }

  private void adjustSyncActions(Set<String> preferenceKeys)
  {
    if (mode.isRecord() || preferenceKeys == null)
    {
      Map<String, SyncAction> syncActions = synchronization.getActions();

      for (Iterator<Map.Entry<String, SyncAction>> it = syncActions.entrySet().iterator(); it.hasNext();)
      {
        Map.Entry<String, SyncAction> entry = it.next();
        SyncAction syncAction = entry.getValue();
        SyncDelta localDelta = syncAction.getLocalDelta();
        if (localDelta != null)
        {
          SetupTask task = localDelta.getNewTask();
          if (task instanceof PreferenceTask)
          {
            PreferenceTask preferenceTask = (PreferenceTask)task;
            String key = preferenceTask.getKey();

            if (preferenceKeys == null || preferenceKeys.contains(key))
            {
              TaskItem taskItem = taskItemsByPreferenceKey.get(key);
              if (taskItem != null)
              {
                Choice remoteChoice = taskItem.getRemoteChoice();
                remoteChoice.applyTo(syncAction);
              }
            }
            else
            {
              it.remove();
            }
          }
        }
      }

      if (DEBUG_SYNC_ACTIONS)
      {
        for (SyncAction syncAction : syncActions.values())
        {
          System.out.println(syncAction);
        }

        System.out.println("----------------------------"); //$NON-NLS-1$
      }
    }

    validatePage();
  }

  private static String getTitle(RecorderTransaction recorderTransaction)
  {
    return recorderTransaction != null ? Messages.SynchronizerDialog_title_recorder : Messages.SynchronizerDialog_title_synchronizer;
  }

  /**
   * Creates, configures and commits a temporary recorder transaction in preparation for the synchronization.
   * The original recorder transaction is modified via {@link LocalColumnManager#setChoice(TaskItem, Choice)}.
   */
  public static Set<String> adjustLocalSnapshot(Synchronization synchronization, Map<String, PolicyAndValue> preferenceChanges)
  {
    Snapshot localSnapshot = synchronization.getSynchronizer().getLocalSnapshot();

    // Transfer the selected recordings into the temporary user.setup in preparation for the synchronization
    LocalDataProvider localDataProvider = (LocalDataProvider)localSnapshot.getDataProvider();
    File target = localDataProvider.getLocalFile();

    URI targetURI = URI.createFileURI(target.getAbsolutePath());
    Resource resource = synchronization.getResourceSet().getResource(targetURI, true);

    Set<String> preferenceKeys = new HashSet<>();
    Map<URI, Pair<String, String>> preferenceMap = new HashMap<>();

    RecorderTransaction tmpTransaction = RecorderTransaction.openTmp(resource);
    tmpTransaction.setPreferences(preferenceMap);

    for (Map.Entry<String, PolicyAndValue> entry : preferenceChanges.entrySet())
    {
      String key = entry.getKey();
      PolicyAndValue policyAndValue = entry.getValue();
      if (policyAndValue.isRecord())
      {
        tmpTransaction.setPolicy(key, true);
        String value = policyAndValue.getValue();
        preferenceMap.put(PreferencesFactory.eINSTANCE.createURI(key), new Pair<String, String>(null, value));
        preferenceKeys.add(key);
      }
      else
      {
        tmpTransaction.setPolicy(key, false);
      }
    }

    try
    {
      tmpTransaction.commit();
    }
    finally
    {
      tmpTransaction.close();
      resource.unload();
    }

    return preferenceKeys;
  }

  /**
   * @author Eike Stepper
   */
  public static final class PolicyAndValue
  {
    private final boolean record;

    private final String value;

    public PolicyAndValue()
    {
      record = false;
      value = null;
    }

    public PolicyAndValue(String value)
    {
      record = true;
      this.value = value;
    }

    public boolean isRecord()
    {
      return record;
    }

    public String getValue()
    {
      return value;
    }

    @Override
    public String toString()
    {
      return record ? "record (" + value + ")" : "ignore"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ConflictAdapter extends AdapterImpl
  {
    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == ConflictAdapter.class;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocalChoiceDisabledAdapter extends AdapterImpl
  {
    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == LocalChoiceDisabledAdapter.class;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static enum Mode
  {
    RECORD_AND_SYNC(true, true),

    RECORD(true, false),

    SYNC(false, true);

    private final boolean record;

    private final boolean sync;

    private Mode(boolean record, boolean sync)
    {
      this.record = record;
      this.sync = sync;
    }

    public boolean isRecord()
    {
      return record;
    }

    public boolean isSync()
    {
      return sync;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class PaintHandler implements Listener
  {
    @Override
    public void handleEvent(Event event)
    {
      ColumnManager manager = getColumnManager(event.index);
      if (manager != null)
      {
        switch (event.type)
        {
          case SWT.MeasureItem:
          {
            manager.measureItem(event);
            break;
          }
          case SWT.PaintItem:
          {
            manager.paintItem(event);
            break;
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class MouseHandler implements Listener
  {
    private boolean lastOpen;

    private TaskItem lastTaskItem;

    private int lastColumn;

    public void reset()
    {
      lastOpen = false;
      lastTaskItem = null;
      lastColumn = 0;
    }

    @Override
    public void handleEvent(Event event)
    {
      Point eventPoint = new Point(event.x, event.y);

      TreeItem treeItem = tree.getItem(eventPoint);
      if (treeItem instanceof TaskItem)
      {
        TaskItem taskItem = (TaskItem)treeItem;

        int columns = tree.getColumnCount();
        for (int column = 1; column < columns; column++)
        {
          Rectangle itemBounds = taskItem.getBounds(column);
          if (itemBounds.contains(eventPoint))
          {
            int x = eventPoint.x - itemBounds.x;
            int y = eventPoint.y - itemBounds.y;

            boolean close = lastOpen && taskItem == lastTaskItem && column == lastColumn;
            lastOpen = handleClick(taskItem, close, column, itemBounds, eventPoint, new Point(x, y));

            lastTaskItem = taskItem;
            lastColumn = column;
            return;
          }
        }
      }

      reset();
    }

    private boolean handleClick(TaskItem taskItem, boolean close, int column, Rectangle itemBounds, Point eventPoint, Point point)
    {
      ColumnManager manager = getColumnManager(column);
      if (manager != null)
      {
        return manager.handleClick(taskItem, close, column, itemBounds, eventPoint, point);
      }

      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class NodeItem extends TreeItem
  {
    private static Image compoundImage;

    public NodeItem(Tree tree, ComposedAdapterFactory adapterFactory, String text)
    {
      super(tree, SWT.NONE);

      if (compoundImage == null)
      {
        CompoundTask compoundTask = SetupFactory.eINSTANCE.createCompoundTask();
        ItemProviderAdapter compoundItemProvider = (ItemProviderAdapter)adapterFactory.adapt(compoundTask, IItemLabelProvider.class);
        compoundImage = SetupUIPlugin.INSTANCE.getSWTImage(SetupLabelProvider.getImageDescriptor(compoundItemProvider, compoundTask));
      }

      setImage(compoundImage);
      setText(text);
    }

    @Override
    protected void checkSubclass()
    {
      // Allow subclassing.
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class TaskItem extends TreeItem
  {
    private final SetupTask task;

    private final boolean conflict;

    private final boolean localChoiceDisabled;

    private Choice localChoice;

    private Choice remoteChoice;

    public TaskItem(NodeItem nodeItem, SetupTask task, boolean conflict, boolean localChoiceDisabled, Image image, String text)
    {
      super(nodeItem, SWT.NONE);
      this.task = task;
      this.conflict = conflict;
      this.localChoiceDisabled = localChoiceDisabled;

      setImage(image);
      setText(text);
    }

    public SetupTask getTask()
    {
      return task;
    }

    public boolean isConflict()
    {
      return conflict;
    }

    public boolean isLocalChoiceDisabled()
    {
      return localChoiceDisabled;
    }

    public Choice getLocalChoice()
    {
      return localChoice;
    }

    public void setLocalChoice(Choice choice)
    {
      localChoice = choice;
    }

    public Choice getRemoteChoice()
    {
      return remoteChoice;
    }

    public void setRemoteChoice(Choice choice)
    {
      remoteChoice = choice;
    }

    @Override
    protected void checkSubclass()
    {
      // Allow subclassing.
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class Choice
  {
    private final Image image;

    private final Image imageDisabled;

    private final String text;

    public Choice(Image image, String text)
    {
      this.image = image;
      this.text = text;

      imageDisabled = new Image(image.getDevice(), image, SWT.IMAGE_DISABLE);
    }

    public Image getImage()
    {
      return image;
    }

    public String getText()
    {
      return text;
    }

    public int paintItem(GC gc, int x, int y, TaskItem taskItem, boolean enabled)
    {
      gc.drawImage(enabled ? image : imageDisabled, x, y);
      return x + ICON + SPACE;
    }

    public boolean isRecord()
    {
      return false;
    }

    public boolean hasTarget()
    {
      return isRecord();
    }

    public void applyTo(SyncAction syncAction)
    {
      // Do nothing.
    }

    protected final void applyTo(SyncAction syncAction, SyncDelta delta, SyncActionType setActionType, SyncActionType removeActionType)
    {
      SetupTask newTask = delta.getNewTask();
      if (newTask == null)
      {
        syncAction.setResolvedType(removeActionType);
      }
      else
      {
        syncAction.setResolvedType(setActionType);
      }
    }

    protected void dispose()
    {
      imageDisabled.dispose();
    }

    /**
     * @author Eike Stepper
     */
    private static final class RecordAlways extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right"); //$NON-NLS-1$

      public RecordAlways(String targetText)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_recordAlways, targetText));
      }

      @Override
      public boolean isRecord()
      {
        return true;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class RecordNever extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Exclude"); //$NON-NLS-1$

      public RecordNever(String targetText)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_recordNever, targetText));
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class RecordSkip extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Skip"); //$NON-NLS-1$

      public RecordSkip()
      {
        super(IMAGE, Messages.SynchronizerDialog_choice_recordSkip);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncConflict extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Conflict"); //$NON-NLS-1$

      public SyncConflict(String serviceLabel)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_syncConflict, serviceLabel));
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        syncAction.setResolvedType(SyncActionType.CONFLICT);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncLocal extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right"); //$NON-NLS-1$

      public SyncLocal()
      {
        super(IMAGE, Messages.SynchronizerDialog_choice_syncLocal);
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        applyTo(syncAction, syncAction.getLocalDelta(), SyncActionType.SET_LOCAL, SyncActionType.REMOVE_LOCAL);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncRemote extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Left"); //$NON-NLS-1$

      public SyncRemote(String serviceLabel)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_syncRemote, serviceLabel));
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        applyTo(syncAction, syncAction.getRemoteDelta(), SyncActionType.SET_REMOTE, SyncActionType.REMOVE_REMOTE);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncAlways extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right"); //$NON-NLS-1$

      public SyncAlways(String serviceLabel)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_syncAlways, serviceLabel));
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        // This choice is only available if no conflict has been detected. So there can be either a remote or a local delta.
        SyncDelta remoteDelta = syncAction.getRemoteDelta();
        if (remoteDelta != null)
        {
          applyTo(syncAction, remoteDelta, SyncActionType.SET_REMOTE, SyncActionType.REMOVE_REMOTE);
          return;
        }

        SyncDelta localDelta = syncAction.getLocalDelta();
        if (localDelta != null)
        {
          applyTo(syncAction, localDelta, SyncActionType.SET_LOCAL, SyncActionType.REMOVE_LOCAL);
          return;
        }
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncNever extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Exclude"); //$NON-NLS-1$

      public SyncNever(String serviceLabel)
      {
        super(IMAGE, NLS.bind(Messages.SynchronizerDialog_choice_syncNever, serviceLabel));
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        syncAction.setResolvedType(SyncActionType.EXCLUDE);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncSkip extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Skip"); //$NON-NLS-1$

      public SyncSkip()
      {
        super(IMAGE, Messages.SynchronizerDialog_choice_syncSkip);
      }

      @Override
      public void applyTo(SyncAction syncAction)
      {
        syncAction.setResolvedType(SyncActionType.NONE);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class ColumnManager
  {
    private static final ColorRegistry COLOR_REGISTRY = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();

    private static final Color CHEVRON_COLOR = COLOR_REGISTRY.get(JFacePreferences.CONTENT_ASSIST_FOREGROUND_COLOR);

    private static final int CHEVRON_WIDTH = 9;

    private static final int WIDTH = H_INDENT + ICON + SPACE + ICON + SPACE + CHEVRON_WIDTH + H_INDENT;

    private static final int HEIGHT = V_INDENT + ICON + V_INDENT;

    private final SynchronizerDialog dialog;

    private final Image targetImage;

    private final Image targetImageDisabled;

    public ColumnManager(final SynchronizerDialog dialog, TreeColumn targetColumn, Image targetImage)
    {
      this.dialog = dialog;
      this.targetImage = targetImage;
      targetImageDisabled = new Image(targetImage.getDevice(), targetImage, SWT.IMAGE_DISABLE);

      targetColumn.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent event)
        {
          if (dialog.treePopulated)
          {
            handleToggleAllChoices();
          }
        }
      });
    }

    protected void handleToggleAllChoices()
    {
      TreeItem[] items = dialog.tree.getItems();
      Map<Choice[], Choice> minimumnChoice = new HashMap<>();
      Set<TaskItem> taskItems = new LinkedHashSet<>();
      visit(minimumnChoice, taskItems, items);
      boolean hasConflictChoice = false;
      for (Entry<Choice[], Choice> entry : minimumnChoice.entrySet())
      {
        Choice[] choices = entry.getKey();
        int index = indexOf(choices, entry.getValue());
        Choice nextChoice = index + 1 == choices.length ? choices[0] : choices[index + 1];
        if (nextChoice instanceof Choice.SyncConflict)
        {
          nextChoice = choices[1];
        }

        if (nextChoice instanceof Choice.SyncLocal || nextChoice instanceof Choice.SyncRemote)
        {
          hasConflictChoice = true;
        }

        entry.setValue(nextChoice);
      }

      // If the next choice is a conflict choice, make sure the non-conflict next choice is to record.
      if (hasConflictChoice)
      {
        for (Entry<Choice[], Choice> entry : minimumnChoice.entrySet())
        {
          Choice choice = entry.getValue();
          if (!(choice instanceof Choice.SyncLocal || choice instanceof Choice.SyncRemote))
          {
            entry.setValue(entry.getKey()[0]);
          }
        }
      }

      for (TaskItem taskItem : taskItems)
      {
        Choice choice = minimumnChoice.get(getChoices(taskItem));
        setChoice(taskItem, choice);
      }

      dialog.tree.redraw();
    }

    private void visit(Map<Choice[], Choice> minimumnChoice, Set<TaskItem> taskItems, TreeItem[] items)
    {
      for (TreeItem item : items)
      {
        if (item instanceof TaskItem)
        {
          TaskItem taskItem = (TaskItem)item;
          if (!isChoiceDisabled(taskItem))
          {
            taskItems.add(taskItem);

            Choice[] choices = getChoices(taskItem);
            Choice choice = getChoice(taskItem);
            Choice currentMinimumChoice = minimumnChoice.get(choices);
            if (currentMinimumChoice == null || indexOf(choices, choice) < indexOf(choices, currentMinimumChoice))
            {
              minimumnChoice.put(choices, choice);
              currentMinimumChoice = choice;
            }
          }
        }

        visit(minimumnChoice, taskItems, item.getItems());
      }
    }

    private static int indexOf(Choice[] choices, Choice choice)
    {
      for (int i = 0; i < choices.length; i++)
      {
        if (choices[i] == choice)
        {
          return i;
        }
      }

      return -1;
    }

    public SynchronizerDialog getDialog()
    {
      return dialog;
    }

    public boolean isChoiceDisabled(TaskItem taskItem)
    {
      return false;
    }

    public abstract Choice[] getChoices(TaskItem taskItem);

    public abstract Choice getChoice(TaskItem taskItem);

    public abstract void setChoice(TaskItem taskItem, Choice choice);

    public void measureItem(Event event)
    {
      if (event.item instanceof TaskItem)
      {
        event.width = Math.max(event.width, WIDTH);
        event.height = Math.max(event.height, HEIGHT);
      }
    }

    public void paintItem(Event event)
    {
      if (event.item instanceof TaskItem)
      {
        TaskItem taskItem = (TaskItem)event.item;

        GC gc = event.gc;
        int x = event.x + event.width + H_INDENT;
        int y = event.y + V_INDENT;

        Choice choice = getChoice(taskItem);
        boolean choiceDisabled = isChoiceDisabled(taskItem);
        boolean enabled = taskItem.getParent().isEnabled() && !choiceDisabled;

        x = choice.paintItem(gc, x, y, taskItem, enabled);
        gc.drawImage(enabled && choice.hasTarget() ? targetImage : targetImageDisabled, x, y);

        if (enabled)
        {
          x += ICON + SPACE;
          int[] chevronPoints = { x + 1, y + 4, x + 4, y + 8, x + 8, y + 4 };

          if (CHEVRON_COLOR != null)
          {
            Color oldBackground = gc.getBackground();
            gc.setBackground(CHEVRON_COLOR);
            gc.fillPolygon(chevronPoints);
            gc.setBackground(oldBackground);
          }
          else
          {
            gc.fillPolygon(chevronPoints);
          }
        }
      }
    }

    public boolean handleClick(final TaskItem taskItem, boolean close, final int column, Rectangle itemBounds, Point eventPoint, Point point)
    {
      final Tree tree = taskItem.getParent();
      if (!tree.isEnabled() || isChoiceDisabled(taskItem))
      {
        return false;
      }

      Menu menu = tree.getMenu();
      if (menu != null)
      {
        menu.dispose();
      }

      if (close)
      {
        return false;
      }

      menu = new Menu(tree);
      Choice currentChoice = getChoice(taskItem);
      for (final Choice choice : getChoices(taskItem))
      {
        if (choice instanceof Choice.SyncConflict)
        {
          continue;
        }

        MenuItem menuItem = new MenuItem(menu, SWT.RADIO);
        menuItem.setSelection(choice == currentChoice);
        menuItem.setImage(choice.getImage());
        menuItem.setText(choice.getText());
        menuItem.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            handleMenu(taskItem, column, choice, tree);
          }
        });
      }

      Point location = tree.toDisplay(itemBounds.x, itemBounds.y);
      location.x += 4;
      location.y += itemBounds.height;

      menu.setLocation(location);
      menu.setVisible(true);
      return true;
    }

    public void handleMenu(TaskItem taskItem, int column, Choice choice, Tree tree)
    {
      dialog.mouseHandler.reset();

      setChoice(taskItem, choice);

      Rectangle bounds = taskItem.getBounds(column);
      tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
    }

    public void dispose()
    {
      targetImageDisabled.dispose();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocalColumnManager extends ColumnManager
  {
    private final Choice[] choices = new Choice[3];

    public LocalColumnManager(SynchronizerDialog dialog)
    {
      super(dialog, dialog.localColumn, dialog.recorderTargetImage);

      choices[0] = new Choice.RecordAlways(dialog.recorderTargetText);
      choices[1] = new Choice.RecordNever(dialog.recorderTargetText);
      choices[2] = new Choice.RecordSkip();
    }

    @Override
    public Choice[] getChoices(TaskItem taskItem)
    {
      return choices;
    }

    @Override
    public Choice getChoice(TaskItem taskItem)
    {
      return taskItem.getLocalChoice();
    }

    @Override
    public void setChoice(TaskItem taskItem, Choice choice)
    {
      taskItem.setLocalChoice(choice);

      PreferenceTask task = (PreferenceTask)taskItem.getTask();
      String key = task.getKey();
      URI uri = PreferencesFactory.eINSTANCE.createURI(key);

      SynchronizerDialog dialog = getDialog();
      RecorderTransaction transaction = dialog.recorderTransaction;
      Set<URI> recorderValuesToRemove = dialog.recorderValuesToRemove;

      if (choice instanceof Choice.RecordAlways)
      {
        transaction.setPolicy(key, true);
        recorderValuesToRemove.remove(uri);
      }
      else if (choice instanceof Choice.RecordNever)
      {
        transaction.setPolicy(key, false);
        recorderValuesToRemove.remove(uri);
      }
      else if (choice instanceof Choice.RecordSkip)
      {
        transaction.removePolicy(key);
        recorderValuesToRemove.add(uri);
      }
    }

    @Override
    public boolean isChoiceDisabled(TaskItem taskItem)
    {
      return taskItem.isLocalChoiceDisabled();
    }

    @Override
    public void handleMenu(TaskItem taskItem, int column, Choice choice, Tree tree)
    {
      super.handleMenu(taskItem, column, choice, tree);

      int nextColumn = column + 1;
      if (nextColumn < tree.getColumnCount())
      {
        SynchronizerDialog dialog = getDialog();
        dialog.synchronizeLocal(true);

        // If there's a remote column redraw it to reflect a possible choice change in the local column.
        Rectangle bounds = taskItem.getBounds(nextColumn);
        tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
      }
    }

    @Override
    public void dispose()
    {
      for (Choice choice : choices)
      {
        choice.dispose();
      }
      super.dispose();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class RemoteColumnManager extends ColumnManager
  {
    private static final Image TARGET_IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Remote"); //$NON-NLS-1$

    private final Choice[] normalChoices = new Choice[3];

    private final Choice[] conflictChoices = new Choice[5];

    public RemoteColumnManager(SynchronizerDialog dialog, String serviceLabel)
    {
      super(dialog, dialog.remoteColumn, TARGET_IMAGE);

      normalChoices[0] = new Choice.SyncAlways(serviceLabel);
      normalChoices[1] = new Choice.SyncNever(serviceLabel);
      normalChoices[2] = new Choice.SyncSkip();

      conflictChoices[0] = new Choice.SyncConflict(serviceLabel);
      conflictChoices[1] = new Choice.SyncLocal();
      conflictChoices[2] = new Choice.SyncRemote(serviceLabel);
      conflictChoices[3] = new Choice.SyncNever(serviceLabel);
      conflictChoices[4] = new Choice.SyncSkip();
    }

    @Override
    protected void handleToggleAllChoices()
    {
      super.handleToggleAllChoices();
      getDialog().adjustSyncActions(null);
    }

    @Override
    public Choice[] getChoices(TaskItem taskItem)
    {
      return taskItem.isConflict() ? conflictChoices : normalChoices;
    }

    @Override
    public Choice getChoice(TaskItem taskItem)
    {
      return taskItem.getRemoteChoice();
    }

    @Override
    public void setChoice(TaskItem taskItem, Choice choice)
    {
      taskItem.setRemoteChoice(choice);
    }

    @Override
    public void measureItem(Event event)
    {
      if (isApplicable(event.item))
      {
        super.measureItem(event);
      }
    }

    @Override
    public void paintItem(Event event)
    {
      if (isApplicable(event.item))
      {
        super.paintItem(event);
      }
    }

    @Override
    public boolean handleClick(TaskItem taskItem, boolean close, int column, Rectangle itemBounds, Point eventPoint, Point point)
    {
      if (isApplicable(taskItem))
      {
        return super.handleClick(taskItem, close, column, itemBounds, eventPoint, point);
      }

      return false;
    }

    @Override
    public void handleMenu(TaskItem taskItem, int column, Choice choice, Tree tree)
    {
      super.handleMenu(taskItem, column, choice, tree);

      SynchronizerDialog dialog = getDialog();
      dialog.adjustSyncActions(null);
    }

    private boolean isApplicable(Widget item)
    {
      if (item instanceof TaskItem)
      {
        TaskItem taskItem = (TaskItem)item;
        Choice localChoice = taskItem.getLocalChoice();
        if (localChoice != null && !localChoice.isRecord())
        {
          return false;
        }
      }

      return true;
    }
  }
}

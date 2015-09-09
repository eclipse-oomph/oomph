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
import org.eclipse.oomph.setup.internal.sync.Snapshot;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.provider.PreferenceTaskItemProvider;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.AbstractRecorderDialog;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.util.CollectionUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class SynchronizerDialog extends AbstractRecorderDialog
{
  private static final int ICON = 16;

  private static final int SPACE = 5;

  private static final int H_INDENT = 2 * SPACE;

  private static final int V_INDENT = 1;

  private final PaintHandler paintHandler = new PaintHandler();

  private final MouseHandler mouseHandler = new MouseHandler();

  private final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

  private final URIConverter uriConverter = resourceSet.getURIConverter();

  private final ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();

  private final Map<SetupTask, TaskItem> taskItems = new HashMap<SetupTask, TaskItem>();

  private final Mode mode;

  private final RecorderTransaction recorderTransaction;

  private final Map<URI, String> recorderValues;

  private final Set<URI> recorderValuesToRemove = new HashSet<URI>();

  private final Scope recorderTarget;

  private final Image recorderTargetImage;

  private final String recorderTargetText;

  private final Synchronization synchronization;

  private Tree tree;

  private TreeColumn labelColumn;

  private TreeColumn localColumn;

  private TreeColumn remoteColumn;

  private ControlAdapter columnResizer;

  private final ColumnManager[] columnManagers = { null, null, null };

  private LocalColumnManager localColumnManager;

  private RemoteColumnManager remoteColumnManager;

  private Text valueText;

  private Button enableRecorderButton;

  public SynchronizerDialog(Shell parentShell)
  {
    this(parentShell, null, null, null);
  }

  public SynchronizerDialog(Shell parentShell, RecorderTransaction transaction, Map<URI, String> preferences, Synchronization sync)
  {
    super(parentShell, getTitle(transaction), 800, 600);
    PreferenceTaskItemProvider.setShortenLabelText(adapterFactory);

    if (transaction != null)
    {
      URI uri = RecorderManager.normalize(uriConverter, RecorderManager.INSTANCE.getRecorderTarget());
      recorderTarget = (Scope)resourceSet.getEObject(uri, true);

      ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(recorderTarget, IItemLabelProvider.class);
      recorderTargetImage = SetupUIPlugin.INSTANCE.getSWTImage(SetupLabelProvider.getImageDescriptor(itemProvider, recorderTarget));
      recorderTargetText = SetupLabelProvider.getText(itemProvider, recorderTarget);

      if (sync != null)
      {
        mode = Mode.RECORD_AND_SYNC;
        recorderTransaction = transaction;
        recorderValues = preferences;
        synchronization = sync;
      }
      else
      {
        mode = Mode.RECORD;
        recorderTransaction = transaction;
        recorderValues = preferences;
        synchronization = null;
      }
    }
    else
    {
      mode = Mode.SYNC;
      recorderTransaction = null;
      recorderValues = null;
      recorderTarget = null;
      recorderTargetImage = null;
      recorderTargetText = null;
      synchronization = sync;
    }
  }

  @Override
  public String getHelpPath()
  {
    return SetupUIPlugin.INSTANCE.getSymbolicName() + "/html/RecorderHelp.html";
  }

  @Override
  protected String getShellText()
  {
    return "Oomph " + getTitle(recorderTransaction);
  }

  @Override
  protected String getDefaultMessage()
  {
    switch (mode)
    {
      case RECORD:
        if (recorderTarget instanceof User)
        {
          return "Select what to record for reuse across all workspaces.";
        }

        if (recorderTarget instanceof Installation)
        {
          return "Select what to record for reuse across all workspaces of the current installation.";
        }

        if (recorderTarget instanceof Workspace)
        {
          return "Select what to record for the use of just this workspace.";
        }

        return "Select what to record into " + recorderTargetText + ".";

      case SYNC:
        return "Select what to synchronize with " + SynchronizerManager.INSTANCE.getService().getLabel() + " for reuse across all machines.";

      case RECORD_AND_SYNC:
        return "Select what to record for reuse across all workspaces on this machine and what to synchronize with "
            + SynchronizerManager.INSTANCE.getService().getLabel() + " for reuse across all your other machines.";

      default:
        return null;
    }
  }

  @Override
  protected void createUI(Composite parent)
  {
    parent.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
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
    labelColumn.setText("Preference");
    labelColumn.setWidth(200);
    labelColumn.setResizable(true);

    if (mode.isRecord())
    {
      localColumn = new TreeColumn(tree, SWT.NONE);
      localColumn.setText("Local Policy");
      localColumn.setWidth(200);
      localColumn.setResizable(true);

      localColumnManager = new LocalColumnManager(this);
    }

    if (mode.isSync())
    {
      SynchronizerService service = SynchronizerManager.INSTANCE.getService();
      remoteColumnManager = new RemoteColumnManager(this, service.getLabel());
    }

    populateTree();
    initColumnResizer();

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

    showFirstTimeHelp(this);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    enableRecorderButton = createCheckbox(parent, "Recorder enabled");
    enableRecorderButton.setToolTipText("The enablement can be changed later on the preference page Oomph | Setup | Preference Recorder");
    enableRecorderButton.setSelection(isEnableRecorder());
    enableRecorderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        validatePage();
      }
    });

    super.createButtonsForButtonBar(parent);
    validatePage();
  }

  protected void validatePage()
  {
    boolean enableRecorder = enableRecorderButton != null && enableRecorderButton.getSelection();
    setEnableRecorder(enableRecorder);

    tree.setEnabled(enableRecorder);
    updateColumns();

    if (valueText != null)
    {
      valueText.setVisible(enableRecorder);
    }
  }

  @Override
  protected void okPressed()
  {
    recorderValues.keySet().removeAll(recorderValuesToRemove);
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

  private void updateColumns()
  {
    int remoteColumnIndex;
    if (localColumn != null)
    {
      columnManagers[1] = localColumnManager;
      remoteColumnIndex = 2;
    }
    else
    {
      columnManagers[1] = null;
      remoteColumnIndex = 1;
    }

    if (mode.isSync())
    {
      if (remoteColumn == null)
      {
        remoteColumn = new TreeColumn(tree, SWT.NONE, 2);
        remoteColumn.setText("Remote Policy");
        remoteColumn.setWidth(200);
        remoteColumn.setResizable(true);
        remoteColumn.addControlListener(columnResizer);

        columnManagers[remoteColumnIndex] = remoteColumnManager;
        resizeColumns();
      }
    }
    else
    {
      if (remoteColumn != null)
      {
        remoteColumn.dispose();
        remoteColumn = null;

        columnManagers[remoteColumnIndex] = null;
        resizeColumns();
      }
    }
  }

  private void resizeColumns()
  {
    columnResizer.controlResized(null);
  }

  private void initColumnResizer()
  {
    columnResizer = new ControlAdapter()
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
  }

  private void populateTree()
  {
    final Map<String, Set<SetupTask>> tasks = new HashMap<String, Set<SetupTask>>();
    final Map<SetupTask, String> labels = new HashMap<SetupTask, String>();
    final Map<SetupTask, Image> images = new HashMap<SetupTask, Image>();

    if (mode.isRecord())
    {
      Map<String, CompoundTask> compounds = new HashMap<String, CompoundTask>();
      Map<String, Boolean> policies = recorderTransaction.getPolicies(false);

      for (Map.Entry<URI, String> entry : recorderValues.entrySet())
      {
        URI uri = entry.getKey();
        String key = PreferencesFactory.eINSTANCE.convertURI(uri);

        // Only offer preferences with *new* policies for review.
        if (policies.containsKey(key))
        {
          String pluginID = uri.segment(0);
          String value = entry.getValue();

          PreferenceTask task = SetupFactory.eINSTANCE.createPreferenceTask();
          task.setKey(key);
          task.setValue(value);

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
      }

      if (mode.isSync())
      {
        synchronizeLocal(false);
      }
    }
    else
    {
    }

    populateTree(tasks, labels, images);
  }

  private void populateTree(final Map<String, Set<SetupTask>> tasks, final Map<SetupTask, String> labels, final Map<SetupTask, Image> images)
  {
    List<String> nodes = new ArrayList<String>(tasks.keySet());
    Collections.sort(nodes);

    Comparator<SetupTask> comparator = new Comparator<SetupTask>()
    {
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

      List<SetupTask> list = new ArrayList<SetupTask>(tasks.get(node));
      Collections.sort(list, comparator);

      for (SetupTask task : list)
      {
        Image image = images.get(task);
        String text = labels.get(task);
        TaskItem taskItem = new TaskItem(nodeItem, task, false, image, text);
        taskItems.put(task, taskItem);

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
  }

  private void synchronizeLocal(boolean resynchronize)
  {
    try
    {
      if (resynchronize)
      {
        Synchronizer synchronizer = synchronization.getSynchronizer();
        Snapshot localSnapshot = synchronizer.getLocalSnapshot();

        SyncUtil.deleteFile(localSnapshot.getNewFile());
        RecorderManager.copyRecorderTarget(recorderTarget, localSnapshot.getFolder());
      }

      synchronization.synchronizeLocal();
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }

  private static String getTitle(RecorderTransaction recorderTransaction)
  {
    return recorderTransaction != null ? "Preference Recorder" : "Preference Synchronizer";
  }

  // private static Map<String, Set<SetupTask>> collectTasks()
  // {
  // ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
  // SetupContext setupContext = SetupContext.createUserOnly(resourceSet);
  // User user = setupContext.getUser();
  // return collectTasks(user);
  // }
  //
  // private static Map<String, Set<SetupTask>> collectTasks(SetupTaskContainer taskContainer)
  // {
  // Map<String, Set<SetupTask>> tasks = new HashMap<String, Set<SetupTask>>();
  // collectTasks(taskContainer.getSetupTasks(), tasks);
  // return tasks;
  // }
  //
  // private static void collectTasks(EList<SetupTask> tasks, Map<String, Set<SetupTask>> result)
  // {
  // for (SetupTask task : tasks)
  // {
  // if (task instanceof CompoundTask)
  // {
  // CompoundTask compoundTask = (CompoundTask)task;
  // collectTasks(compoundTask.getSetupTasks(), result);
  // }
  // else if (task instanceof PreferenceTask)
  // {
  // PreferenceTask preferenceTask = (PreferenceTask)task;
  // String key = preferenceTask.getKey();
  // if (!StringUtil.isEmpty(key))
  // {
  // Path path = new Path(key);
  // if (path.segmentCount() > 1)
  // {
  // String node = path.segment(1);
  // CollectionUtil.add(result, node, preferenceTask);
  // }
  // }
  // }
  // else
  // {
  // String className = task.eClass().getName();
  // String node = null;
  //
  // try
  // {
  // node = SetupUIPlugin.INSTANCE.getString("_UI_" + className + "_type");
  // }
  // catch (Throwable ex)
  // {
  // //$FALL-THROUGH$
  // }
  //
  // if (StringUtil.isEmpty(node))
  // {
  // node = className;
  // }
  //
  // CollectionUtil.add(result, node + " Tasks", task);
  // }
  // }
  // }

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

    private Choice localChoice;

    private Choice remoteChoice;

    public TaskItem(NodeItem nodeItem, SetupTask task, boolean conflict, Image image, String text)
    {
      super(nodeItem, SWT.NONE);
      this.task = task;
      this.conflict = conflict;

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

    public int paintItem(GC gc, int x, int y, TaskItem taskItem)
    {
      boolean enabled = taskItem.getParent().isEnabled();
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

    /**
     * @author Eike Stepper
     */
    private static final class RecordAlways extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right");

      public RecordAlways(String targetText)
      {
        super(IMAGE, "Always record into " + targetText);
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
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Exclude");

      public RecordNever(String targetText)
      {
        super(IMAGE, "Never record into " + targetText);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class RecordSkip extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Skip");

      public RecordSkip()
      {
        super(IMAGE, "Skip this time");
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncConflict extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Conflict");

      public SyncConflict(String serviceLabel)
      {
        super(IMAGE, "Conflict between local and " + serviceLabel + " values");
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncLocal extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right");

      public SyncLocal()
      {
        super(IMAGE, "Resolve with local value");
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncRemote extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Left");

      public SyncRemote(String serviceLabel)
      {
        super(IMAGE, "Resolve with " + serviceLabel + " value");
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncAlways extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Right");

      public SyncAlways(String serviceLabel)
      {
        super(IMAGE, "Always synchronize with " + serviceLabel);
      }

      @Override
      public boolean hasTarget()
      {
        return true;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncNever extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Exclude");

      public SyncNever(String serviceLabel)
      {
        super(IMAGE, "Never synchronize with " + serviceLabel);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class SyncSkip extends Choice
    {
      private static final Image IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Skip");

      public SyncSkip()
      {
        super(IMAGE, "Skip this time");
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class ColumnManager
  {
    protected static final Image CHEVRON_IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Chevron");

    protected static final Image CHEVRON_IMAGE_DISABLED = new Image(CHEVRON_IMAGE.getDevice(), CHEVRON_IMAGE, SWT.IMAGE_DISABLE);

    private static final int CHEVRON_WIDTH = CHEVRON_IMAGE.getBounds().width;

    private static final int WIDTH = H_INDENT + ICON + SPACE + ICON + SPACE + CHEVRON_WIDTH + H_INDENT;

    private static final int HEIGHT = V_INDENT + ICON + V_INDENT;

    private final SynchronizerDialog dialog;

    private final Image targetImage;

    private final Image targetImageDisabled;

    public ColumnManager(SynchronizerDialog dialog, Image targetImage)
    {
      this.dialog = dialog;
      this.targetImage = targetImage;
      targetImageDisabled = new Image(targetImage.getDevice(), targetImage, SWT.IMAGE_DISABLE);
    }

    public SynchronizerDialog getDialog()
    {
      return dialog;
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

        int x = event.x + event.width + H_INDENT;
        int y = event.y + V_INDENT;

        GC gc = event.gc;

        Choice choice = getChoice(taskItem);
        x = choice.paintItem(gc, x, y, taskItem);

        boolean enabled = taskItem.getParent().isEnabled();
        gc.drawImage(enabled && choice.hasTarget() ? targetImage : targetImageDisabled, x, y);
        gc.drawImage(enabled ? CHEVRON_IMAGE : CHEVRON_IMAGE_DISABLED, x + ICON + SPACE, y);
      }
    }

    public boolean handleClick(final TaskItem taskItem, boolean close, final int column, Rectangle itemBounds, Point eventPoint, Point point)
    {
      final Tree tree = taskItem.getParent();

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
      for (final Choice choice : getChoices(taskItem))
      {
        if (choice instanceof Choice.SyncConflict)
        {
          continue;
        }

        MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
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
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocalColumnManager extends ColumnManager
  {
    private final Choice[] choices = new Choice[3];

    public LocalColumnManager(SynchronizerDialog dialog)
    {
      super(dialog, dialog.recorderTargetImage);

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

      if (choice instanceof Choice.RecordAlways)
      {
        transaction.setPolicy(key, true);
        dialog.recorderValuesToRemove.remove(uri);
      }
      else if (choice instanceof Choice.RecordNever)
      {
        transaction.setPolicy(key, false);
        dialog.recorderValuesToRemove.remove(uri);
      }
      else if (choice instanceof Choice.RecordSkip)
      {
        transaction.removePolicy(key);
        dialog.recorderValuesToRemove.add(uri);
      }
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
  }

  /**
   * @author Eike Stepper
   */
  private static final class RemoteColumnManager extends ColumnManager
  {
    private static final Image TARGET_IMAGE = SetupUIPlugin.INSTANCE.getSWTImage("sync/Remote");

    private final Choice[] normalChoices = new Choice[3];

    private final Choice[] conflictChoices = new Choice[5];

    public RemoteColumnManager(SynchronizerDialog dialog, String serviceLabel)
    {
      super(dialog, TARGET_IMAGE);

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

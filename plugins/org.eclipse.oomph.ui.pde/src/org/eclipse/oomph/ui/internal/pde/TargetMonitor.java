/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.ui.internal.pde;

import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.ui.internal.pde.TargetSnapshot.Delta;
import org.eclipse.oomph.ui.internal.pde.TargetSnapshot.Delta.Change;
import org.eclipse.oomph.ui.internal.pde.TargetSnapshot.Delta.Change.Kind;
import org.eclipse.oomph.ui.internal.pde.TargetSnapshot.State;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.IToolTipProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.ViewPart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import jakarta.inject.Inject;

/**
 * @author Eike Stepper
 */
public class TargetMonitor extends ViewPart implements ISelectionProvider
{
  public static final String ID = "org.eclipse.oomph.ui.pde.TargetMonitor"; //$NON-NLS-1$

  private static final IPersistentPreferenceStore PREFERENCES = (IPersistentPreferenceStore)UIPDEPlugin.INSTANCE.getPreferenceStore();

  private static final String PREF_LINK_WITH_EDITOR = "link.with.editor"; //$NON-NLS-1$

  private static final String PREF_AUTO_SNAPSHOT = "auto.snapshot"; //$NON-NLS-1$

  private static final String PREF_AUTO_FOCUS = "auto.focus"; //$NON-NLS-1$

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss"); //$NON-NLS-1$

  private static final Object NO_DELTA = new Object();

  private static final Object[] NO_CHILDREN = {};

  private static final String SPACE = "   "; //$NON-NLS-1$

  private static final String EMPTY = ""; //$NON-NLS-1$

  @Inject
  IWorkbench workbench;

  @Inject
  IEventBroker broker;

  private Color redColor;

  private Color greenColor;

  private Color grayColor;

  private Font boldFont;

  private SharedLabelProvider sharedLabelProvider;

  private TableCombo targetsCombo;

  private TreeViewer snapshotsViewer;

  private Label changesLabel;

  private Label removalsLabel;

  private Label additionsLabel;

  private TreeViewer deltaViewer;

  private final Map<TargetSnapshot, Object> deltas = new WeakHashMap<>();

  private final TakeSnapshotAction takeSnapshotAction = new TakeSnapshotAction();

  private final DeleteSelectedSnapshotsAction deleteSelectedSnapshotsAction = new DeleteSelectedSnapshotsAction();

  private final DeleteOldSnapshotsAction deleteOldSnapshotsAction = new DeleteOldSnapshotsAction();

  private boolean linkWithEditor;

  private boolean autoFocus;

  private final TargetManager targetManager = new TargetManager();

  private final TargetManager.Listener targetManagerListener = events -> UIUtil.asyncExec(() -> handleTargetManagerEvents(events));

  private final IPartListener2 partListener = new IPartListener2()
  {
    @Override
    public void partActivated(IWorkbenchPartReference partRef)
    {
      if (linkWithEditor)
      {
        IWorkbenchPart part = partRef.getPart(false);
        monitorTarget(part);
      }
    }
  };

  private boolean settingTargetSelection;

  public TargetMonitor()
  {
  }

  @Override
  public void createPartControl(Composite parent)
  {
    Display display = parent.getDisplay();
    redColor = display.getSystemColor(SWT.COLOR_RED);
    greenColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
    grayColor = display.getSystemColor(SWT.COLOR_GRAY);
    boldFont = UIPDEPlugin.getBoldFont(parent.getFont());

    sharedLabelProvider = new SharedLabelProvider();

    SashForm sashForm = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
    createArea(sashForm, this::createUpperHeader, this::createUpperBody);
    createArea(sashForm, this::createLowerHeader, this::createLowerBody);
    sashForm.setWeights(new int[] { 3, 1 });

    hookActionBars();
    hookContextMenu();
    hookDoubleClickAction();

    deleteSelectedSnapshotsAction.updateEnablement();
    deleteOldSnapshotsAction.updateEnablement();

    getSite().setSelectionProvider(this);
    getSite().getPage().addPartListener(partListener);

    setDeltaViewerInput(null);
    targetManager.addListener(targetManagerListener);
  }

  @Override
  public void dispose()
  {
    targetManager.dispose();
    getSite().getPage().removePartListener(partListener);
    super.dispose();
  }

  @Override
  public void setFocus()
  {
    snapshotsViewer.getControl().setFocus();
  }

  @Override
  public ISelection getSelection()
  {
    if (snapshotsViewer.getControl().isFocusControl())
    {
      return snapshotsViewer.getSelection();
    }

    if (deltaViewer.getControl().isFocusControl())
    {
      return deltaViewer.getSelection();
    }

    return StructuredSelection.EMPTY;
  }

  @Override
  public void setSelection(ISelection selection)
  {
    if (snapshotsViewer.getControl().isFocusControl())
    {
      snapshotsViewer.setSelection(selection);
    }
    else if (deltaViewer.getControl().isFocusControl())
    {
      deltaViewer.setSelection(selection);
    }
  }

  @Override
  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    snapshotsViewer.addSelectionChangedListener(listener);
    deltaViewer.addSelectionChangedListener(listener);
  }

  @Override
  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    snapshotsViewer.removeSelectionChangedListener(listener);
    deltaViewer.removeSelectionChangedListener(listener);
  }

  private void bringEditorToTop(Target target)
  {
    ITargetHandle targetHandle = target.getHandle();
    if (targetHandle != null)
    {
      IWorkbenchPage page = getSite().getPage();
      for (IEditorReference reference : page.getEditorReferences())
      {
        IEditorPart editor = reference.getEditor(false);
        if (editor != null)
        {
          ITargetHandle editorHandle = editor.getAdapter(ITargetHandle.class);
          if (targetHandle.equals(editorHandle))
          {
            page.bringToTop(editor);
          }
        }
      }
    }
  }

  private Target getSelectedTarget()
  {
    int selection = targetsCombo.getSelectionIndex();
    if (selection == -1)
    {
      return null;
    }

    TableItem item = targetsCombo.getTable().getItem(selection);
    return (Target)item.getData();
  }

  private void setSelectedTarget(Target target)
  {
    int i = 0;
    for (TableItem item : targetsCombo.getTable().getItems())
    {
      if (ObjectUtil.equals(item.getData(), target))
      {
        targetsCombo.select(i);
        return;
      }

      ++i;
    }
  }

  private void handleTargetManagerEvents(List<TargetManager.Event> events)
  {
    boolean refreshTargets = false;
    boolean refreshSnapshots = false;

    for (TargetManager.Event event : events)
    {
      Target target = event.getTarget();
      TargetSnapshot snapshot = event.getSnapshot();

      switch (event.getKind())
      {
        case TargetAdded:
        case TargetRemoved:
        case TargetNameChanged:
        case ActiveTargetChanged:
          refreshTargets = true;
          break;

        case MonitoredTargetChanged:
          refreshTargets = true;

          UIUtil.asyncExec(() -> {
            try
            {
              settingTargetSelection = true;
              setSelectedTarget(target);
            }
            finally
            {
              settingTargetSelection = false;
            }

            snapshotsViewer.setInput(target);

            if (target != null && autoFocus)
            {
              focusOn(target.getCurrentSnapshot());
            }
          });

          break;

        case TargetSnapshotAdded:
        case TargetSnapshotRemoved:
          refreshTargets = true;
          refreshSnapshots = true;
          break;

        case TargetSnapshotResolutionFinished:
          refreshSnapshots = true;

          if (snapshot.getState() != State.Resolving && autoFocus)
          {
            focusOn(snapshot);
          }

          break;
      }
    }

    if (refreshTargets)
    {
      refreshTargetsCombo();
    }

    if (refreshSnapshots)
    {
      snapshotsViewer.refresh();
    }
  }

  private void refreshTargetsCombo()
  {
    Target monitoredTarget = targetManager.getMonitoredTarget();
    int selection = -1;

    Table table = targetsCombo.getTable();
    table.removeAll();

    Target[] targets = targetManager.getTargets();
    for (int i = 0; i < targets.length; i++)
    {
      Target target = targets[i];

      TableItem item = new TableItem(table, SWT.NONE);
      item.setData(target);
      item.setImage(0, UIPDEPlugin.INSTANCE.getSWTImage(target.isDefault() ? "target-default.png" : "target.png")); //$NON-NLS-1$ //$NON-NLS-2$

      String label = target.getName();

      if (target.isActive())
      {
        label += " (" + Messages.TargetMonitor_Active + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        item.setFont(0, boldFont);
      }

      item.setText(0, Objects.requireNonNullElse(label, EMPTY));
      item.setText(1, Objects.requireNonNullElse(target.getLocation(), EMPTY));

      int snapshots = target.getSnapshots().length;
      if (snapshots > 0)
      {
        item.setText(2, NLS.bind(snapshots == 1 ? Messages.TargetMonitor_OneSnapshot : Messages.TargetMonitor_Snapshots, snapshots));
      }

      if (target == monitoredTarget)
      {
        selection = i;
      }
    }

    if (selection != -1)
    {
      targetsCombo.select(selection);
    }

    takeSnapshotAction.updateEnablement();
  }

  private void targetSelectionChanged()
  {
    takeSnapshotAction.updateEnablement();

    if (!settingTargetSelection)
    {
      Target target = getSelectedTarget();
      if (target != null)
      {
        if (target.monitor() && linkWithEditor)
        {
          bringEditorToTop(target);
        }
      }
    }
  }

  private void snapshotSelectionChanged()
  {
    deleteSelectedSnapshotsAction.updateEnablement();
    deleteOldSnapshotsAction.updateEnablement();

    IStructuredSelection selection = snapshotsViewer.getStructuredSelection();
    int size = selection.size();
    if (size == 1)
    {
      Object object = selection.getFirstElement();

      TargetSnapshot snapshot = getSnapshot(object);
      if (snapshot != null)
      {
        Delta delta = getDelta(snapshot);
        setDeltaViewerInput(delta);
        return;
      }
    }
    else if (size == 2)
    {
      Object[] objects = selection.toArray();

      TargetSnapshot snapshot = getSnapshot(objects[0]);
      TargetSnapshot previous = getSnapshot(objects[1]);
      if (snapshot != null && previous != null && snapshot != previous)
      {
        if (snapshot.getNumber() < previous.getNumber())
        {
          TargetSnapshot tmp = snapshot;
          snapshot = previous;
          previous = tmp;
        }

        Delta delta = snapshot.createDelta(previous);
        setDeltaViewerInput(delta);
        return;
      }
    }

    setDeltaViewerInput(null);
  }

  private void focusOn(TargetSnapshot snapshot)
  {
    if (snapshot != null)
    {
      UIUtil.asyncExec(snapshotsViewer.getControl(), () -> {
        snapshotsViewer.collapseAll();
        snapshotsViewer.expandToLevel(snapshot, AbstractTreeViewer.ALL_LEVELS);

        ITreeSelection oldSelection = snapshotsViewer.getStructuredSelection();
        StructuredSelection newSelection = new StructuredSelection(snapshot);
        if (!Objects.equals(newSelection, oldSelection))
        {
          snapshotsViewer.setSelection(newSelection);
        }
      });
    }
  }

  private void monitorTarget(IWorkbenchPart part)
  {
    if (part instanceof IEditorPart)
    {
      IEditorPart editor = (IEditorPart)part;

      ITargetHandle handle = editor.getAdapter(ITargetHandle.class);
      if (handle != null)
      {
        Target target = targetManager.getTarget(handle);
        if (target != null)
        {
          target.monitor();
          return;
        }
      }
      else
      {
        IFile file = editor.getEditorInput().getAdapter(IFile.class);
        if (file != null)
        {
          for (Target target : targetManager.getTargets())
          {
            if (file.equals(target.getWorkspaceFile()))
            {
              target.monitor();
              return;
            }
          }
        }
      }
    }
  }

  private Delta getDelta(TargetSnapshot snapshot)
  {
    Object result = deltas.computeIfAbsent(snapshot, k -> {
      Delta delta = snapshot.createDelta();
      return delta == null ? NO_DELTA : delta;
    });

    return result == NO_DELTA ? null : (Delta)result;
  }

  private void setDeltaViewerInput(Delta delta)
  {
    Delta oldDelta = (Delta)deltaViewer.getInput();
    if (oldDelta != delta)
    {
      if (delta != null)
      {
        changesLabel.setText(delta.getChanges().length + SPACE + "(" + NLS.bind(Messages.TargetMonitor_SnapshotN, delta.getPrevious().getNumber()) + " \u2192 " //$NON-NLS-1$ //$NON-NLS-2$
            + NLS.bind(Messages.TargetMonitor_SnapshotN, delta.getSnapshot().getNumber()) + ")"); //$NON-NLS-1$

        int removals = delta.getRemovedElements().size();
        removalsLabel.setText(removals == 0 ? EMPTY : "-" + removals); //$NON-NLS-1$

        int additions = delta.getAddedElements().size();
        additionsLabel.setText(additions == 0 ? EMPTY : "+" + additions); //$NON-NLS-1$
      }
      else
      {
        changesLabel.setText("0" + SPACE + "(" + Messages.TargetMonitor_NoDelta + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        removalsLabel.setText(EMPTY);
        additionsLabel.setText(EMPTY);
      }

      deltaViewer.setInput(delta);
    }
  }

  private TargetSnapshot getSnapshot(Object object)
  {
    while (object != null)
    {
      if (object instanceof TargetSnapshot)
      {
        return (TargetSnapshot)object;
      }

      SnapshotsContentProvider contentProvider = (SnapshotsContentProvider)snapshotsViewer.getContentProvider();
      object = contentProvider.getParent(object);
    }

    return null;
  }

  private void hookActionBars()
  {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  private void hookContextMenu()
  {
    MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener()
    {
      @Override
      public void menuAboutToShow(IMenuManager manager)
      {
        TargetMonitor.this.fillContextMenu(manager);
      }
    });

    Menu menu = menuMgr.createContextMenu(snapshotsViewer.getControl());
    snapshotsViewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, snapshotsViewer);
  }

  private void hookDoubleClickAction()
  {
    snapshotsViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        // Perhaps needed later...
      }
    });
  }

  private void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(takeSnapshotAction);
    manager.add(deleteOldSnapshotsAction);
    manager.add(new Action(Messages.TargetMonitor_CollapseAll, UIPDEPlugin.INSTANCE.getImageDescriptor("collapse-all")) //$NON-NLS-1$
    {
      @Override
      public void run()
      {
        snapshotsViewer.collapseAll();
      }
    });

    manager.add(new LinkWithEditorAction());
  }

  private void fillLocalPullDown(IMenuManager manager)
  {
    manager.add(new AutoSnapshotAction());
    manager.add(new AutoFocusAction());
    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
  }

  private void fillContextMenu(IMenuManager manager)
  {
    ITreeSelection selection = snapshotsViewer.getStructuredSelection();
    int size = selection.size();

    if (size == 1)
    {
      Object object = selection.getFirstElement();
      if (object instanceof TargetSnapshot)
      {
        TargetSnapshot snapshot = (TargetSnapshot)object;

        if (snapshot.getXML() != null)
        {
          manager.add(new Action(Messages.TargetMonitor_Restore, UIPDEPlugin.INSTANCE.getImageDescriptor("restore.png")) //$NON-NLS-1$
          {
            @Override
            public void run()
            {
              snapshot.restore();
            }
          });
        }
      }
    }

    if (deleteSelectedSnapshotsAction.isEnabled())
    {
      manager.add(deleteSelectedSnapshotsAction);
    }

    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
  }

  private void createUpperHeader(Composite parent)
  {
    Composite header = new Composite(parent, SWT.NONE);
    header.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());

    Label label = new Label(header, SWT.NONE);
    label.setLayoutData(GridDataFactory.swtDefaults().create());
    label.setText(Messages.TargetMonitor_MonitoredTarget + ":"); //$NON-NLS-1$

    targetsCombo = new TableCombo(header, SWT.BORDER | SWT.READ_ONLY);
    targetsCombo.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
    targetsCombo.defineColumns(3);
    targetsCombo.getTable().addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        targetSelectionChanged();
      }
    });

    refreshTargetsCombo();

    ToolButton button = new ToolButton(header, SWT.PUSH, UIPDEPlugin.INSTANCE.getSWTImage("preferences.png"), false); //$NON-NLS-1$
    button.setLayoutData(GridDataFactory.fillDefaults().create());
    button.setToolTipText(Messages.TargetMonitor_Preferences);
    button.addSelectionListener(SelectionListener.widgetSelectedAdapter(event -> {
      PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getSite().getShell(), "org.eclipse.pde.ui.TargetPlatformPreferencePage", null, null); //$NON-NLS-1$
      dialog.open();
    }));
  }

  private void createUpperBody(Composite parent)
  {
    snapshotsViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    snapshotsViewer.setUseHashlookup(true);
    snapshotsViewer.setContentProvider(new SnapshotsContentProvider());
    snapshotsViewer.setLabelProvider(new DelegatingLabelProvider(sharedLabelProvider));
    snapshotsViewer.setInput(targetManager.getMonitoredTarget());
    snapshotsViewer.addSelectionChangedListener(event -> snapshotSelectionChanged());
    snapshotsViewer.addTreeListener(new ITreeViewerListener()
    {
      @Override
      public void treeExpanded(TreeExpansionEvent event)
      {
        UIUtil.asyncExec(parent, () -> snapshotsViewer.expandToLevel(event.getElement(), AbstractTreeViewer.ALL_LEVELS));
      }

      @Override
      public void treeCollapsed(TreeExpansionEvent event)
      {
        // Do nothing.
      }
    });

    // The label provider must be a CellLabelProvider, ideally a ColumnLabelProvider.
    ColumnViewerToolTipSupport.enableFor(snapshotsViewer);
  }

  private void createLowerHeader(Composite parent)
  {
    Composite header = new Composite(parent, SWT.NONE);
    header.setLayout(GridLayoutFactory.swtDefaults().numColumns(5).create());

    Label label = new Label(header, SWT.NONE);
    label.setLayoutData(GridDataFactory.swtDefaults().create());
    label.setText(Messages.TargetMonitor_Changes + ":"); //$NON-NLS-1$

    changesLabel = new Label(header, SWT.NONE);
    changesLabel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

    removalsLabel = new Label(header, SWT.NONE);
    removalsLabel.setLayoutData(GridDataFactory.fillDefaults().hint(36, SWT.DEFAULT).create());
    removalsLabel.setForeground(redColor);

    additionsLabel = new Label(header, SWT.NONE);
    additionsLabel.setLayoutData(GridDataFactory.fillDefaults().hint(36, SWT.DEFAULT).create());
    additionsLabel.setForeground(greenColor);
  }

  private void createLowerBody(Composite parent)
  {
    deltaViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    deltaViewer.setUseHashlookup(true);
    deltaViewer.setContentProvider(new DeltaContentProvider());
    deltaViewer.setLabelProvider(new DelegatingLabelProvider(sharedLabelProvider));
    deltaViewer.setInput(null);
  }

  private static Composite createArea(Composite parent, Consumer<Composite> headerCreator, Consumer<Composite> bodyCreator)
  {
    boolean firstArea = parent.getChildren().length == 0;

    Composite area = new Composite(parent, SWT.NONE);
    area.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 0).create());

    if (!firstArea)
    {
      createSeparator(area);
    }

    Composite header = new Composite(area, SWT.NONE);
    header.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
    header.setLayout(new FillLayout());
    header.setBackground(header.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    headerCreator.accept(header);

    createSeparator(area);

    Composite body = new Composite(area, SWT.NONE);
    body.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    body.setLayout(new FillLayout());
    bodyCreator.accept(body);

    return area;
  }

  private static void createSeparator(Composite parent)
  {
    GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.heightHint = 1;

    Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
    separator.setLayoutData(gridData);
  }

  /**
   * @author Eike Stepper
   */
  private final class SnapshotsContentProvider implements ITreeContentProvider
  {
    private final Map<IStatus, Object> statusParents = new WeakHashMap<>();

    public SnapshotsContentProvider()
    {
    }

    @Override
    public Object[] getElements(Object obj)
    {
      return getChildren(obj);
    }

    @Override
    public boolean hasChildren(Object obj)
    {
      return getChildren(obj).length != 0;
    }

    @Override
    public Object[] getChildren(Object obj)
    {
      if (obj instanceof Target)
      {
        return ((Target)obj).getSnapshots();
      }

      if (obj instanceof TargetSnapshot)
      {
        TargetSnapshot snapshot = (TargetSnapshot)obj;

        switch (snapshot.getState())
        {
          case Resolved:
            return snapshot.getElements();

          case Error:
            IStatus child = snapshot.getResolutionStatus();
            statusParents.put(child, obj);
            return new Object[] { child };
        }
      }

      if (obj instanceof IStatus)
      {
        IStatus[] children = ((IStatus)obj).getChildren();
        for (IStatus child : children)
        {
          statusParents.put(child, obj);
        }

        return children;
      }

      return NO_CHILDREN;
    }

    @Override
    public Object getParent(Object obj)
    {
      if (obj instanceof IStatus)
      {
        return statusParents.get(obj);
      }

      if (obj instanceof TargetElement)
      {
        return ((TargetElement)obj).getSnapshot();
      }

      if (obj instanceof TargetSnapshot)
      {
        return ((TargetSnapshot)obj).getTarget();
      }

      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DeltaContentProvider implements ITreeContentProvider
  {
    public DeltaContentProvider()
    {
    }

    @Override
    public Object[] getElements(Object obj)
    {
      return getChildren(obj);
    }

    @Override
    public Object[] getChildren(Object obj)
    {
      if (obj instanceof Delta)
      {
        return ((Delta)obj).getChanges();
      }

      return NO_CHILDREN;
    }

    @Override
    public Object getParent(Object obj)
    {
      if (obj instanceof Change)
      {
        return ((Change)obj).getDelta();
      }

      return null;
    }

    @Override
    public boolean hasChildren(Object obj)
    {
      return obj instanceof Delta;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SharedLabelProvider implements ILabelProvider, IStyledLabelProvider, IColorProvider, IToolTipProvider
  {
    private final Image snapshotImage = UIPDEPlugin.INSTANCE.getSWTImage("snapshot.png"); //$NON-NLS-1$

    private final Image featureImage = UIPDEPlugin.INSTANCE.getSWTImage("feature.png"); //$NON-NLS-1$

    private final Image bundleImage = UIPDEPlugin.INSTANCE.getSWTImage("bundle.png"); //$NON-NLS-1$

    private final Image fragmentImage = UIPDEPlugin.INSTANCE.getSWTImage("fragment.png"); //$NON-NLS-1$

    private final Image sourceBundleImage = UIPDEPlugin.INSTANCE.getSWTImage("source-bundle.png"); //$NON-NLS-1$

    private final Image sourceFragmentImage = UIPDEPlugin.INSTANCE.getSWTImage("source-fragment.png"); //$NON-NLS-1$

    private final Styler redStyler = new Styler()
    {
      @Override
      public void applyStyles(TextStyle textStyle)
      {
        textStyle.foreground = redColor;
      }
    };

    private final Styler greenStyler = new Styler()
    {
      @Override
      public void applyStyles(TextStyle textStyle)
      {
        textStyle.foreground = greenColor;
      }
    };

    public SharedLabelProvider()
    {
    }

    @Override
    public String getText(Object obj)
    {
      return getStyledText(obj).getString();
    }

    @Override
    public StyledString getStyledText(Object obj)
    {
      if (obj instanceof TargetSnapshot)
      {
        TargetSnapshot snapshot = (TargetSnapshot)obj;
        StyledString str = new StyledString(NLS.bind(Messages.TargetMonitor_SnapshotN, snapshot.getNumber()));

        State state = snapshot.getState();
        if (state == State.Resolved)
        {
          int elements = snapshot.getElements().length;
          str.append(SPACE + NLS.bind(elements == 1 ? Messages.TargetMonitor_OneElement : Messages.TargetMonitor_Elements, elements),
              StyledString.COUNTER_STYLER);

          Delta delta = getDelta(snapshot);
          if (delta != null)
          {
            int removals = delta.getRemovedElements().size();
            if (removals != 0)
            {
              str.append(SPACE + "-" + removals, redStyler); //$NON-NLS-1$
            }

            int additions = delta.getAddedElements().size();
            if (additions != 0)
            {
              str.append(SPACE + "+" + additions, greenStyler); //$NON-NLS-1$
            }
          }
        }
        else
        {
          str.append(SPACE + state.name(), new Styler()
          {
            @Override
            public void applyStyles(TextStyle textStyle)
            {
              textStyle.foreground = state == State.Resolving ? grayColor : redColor;
            }
          });
        }

        return str;
      }

      if (obj instanceof TargetElement)
      {
        TargetElement element = (TargetElement)obj;
        return new StyledString(element.getName() + SPACE).append(element.getVersion().toString(), StyledString.DECORATIONS_STYLER);
      }

      if (obj instanceof IStatus)
      {
        IStatus status = (IStatus)obj;
        return new StyledString(status.getMessage());
      }

      if (obj instanceof Change)
      {
        Change change = (Change)obj;
        return getStyledText(change.getElement());
      }

      return new StyledString(obj.toString());
    }

    @Override
    public Image getImage(Object obj)
    {
      if (obj instanceof TargetSnapshot)
      {
        return snapshotImage;
      }

      if (obj instanceof TargetElement)
      {
        TargetElement element = (TargetElement)obj;
        switch (element.getType())
        {
          case Feature:
            return featureImage;

          case Bundle:
            return element.isSource() ? sourceBundleImage : bundleImage;

          case Fragment:
            return element.isSource() ? sourceFragmentImage : fragmentImage;
        }
      }

      if (obj instanceof IStatus)
      {
        IStatus status = (IStatus)obj;
        return UIUtil.getStatusImage(status.getSeverity());
      }

      if (obj instanceof Change)
      {
        Change change = (Change)obj;
        return getImage(change.getElement());
      }

      return null;
    }

    @Override
    public Color getForeground(Object obj)
    {
      if (obj instanceof TargetSnapshot)
      {
        State state = ((TargetSnapshot)obj).getState();
        return state == State.Resolving || state == State.Canceled ? grayColor : null;
      }

      if (obj instanceof Change)
      {
        Change change = (Change)obj;
        return change.getKind() == Kind.Removal ? redColor : greenColor;
      }

      return null;
    }

    @Override
    public Color getBackground(Object obj)
    {
      return null;
    }

    @Override
    public String getToolTipText(Object obj)
    {
      if (obj instanceof TargetSnapshot)
      {
        TargetSnapshot snapshot = (TargetSnapshot)obj;
        String tooTip = DATE_FORMAT.format(new Date(snapshot.getTimeStamp()));

        String xml = snapshot.getXML();
        if (xml != null)
        {
          tooTip += "\n\n" + xml; //$NON-NLS-1$
        }

        return tooTip;
      }

      if (obj instanceof TargetElement)
      {
        TargetElement element = (TargetElement)obj;
        return element.getLocation();
      }

      return null;
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
      return true;
    }

    @Override
    public void addListener(ILabelProviderListener listener)
    {
      // Do nothing.
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
      // Do nothing.
    }

    @Override
    public void dispose()
    {
      // Do nothing.
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DelegatingLabelProvider extends DelegatingStyledCellLabelProvider implements ILabelProvider
  {
    public DelegatingLabelProvider(SharedLabelProvider delegate)
    {
      super(delegate);
    }

    @Override
    public SharedLabelProvider getStyledStringProvider()
    {
      return (SharedLabelProvider)super.getStyledStringProvider();
    }

    @Override
    public String getText(Object obj)
    {
      return getStyledStringProvider().getText(obj);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class TakeSnapshotAction extends Action
  {
    public TakeSnapshotAction()
    {
      super(Messages.TargetMonitor_TakeSnapshot, UIPDEPlugin.INSTANCE.getImageDescriptor("add.png")); //$NON-NLS-1$
    }

    public void updateEnablement()
    {
      setEnabled(getSelectedTarget() != null);
    }

    @Override
    public void run()
    {
      Target target = getSelectedTarget();
      if (target != null)
      {
        target.takeSnapshot();
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DeleteSelectedSnapshotsAction extends Action
  {
    private final List<TargetSnapshot> snapshots = new ArrayList<>();

    public DeleteSelectedSnapshotsAction()
    {
      super(Messages.TargetMonitor_DeleteSnapshots, UIPDEPlugin.INSTANCE.getImageDescriptor("delete.png")); //$NON-NLS-1$
      setEnabled(false);
    }

    public void updateEnablement()
    {
      try
      {
        snapshots.clear();

        IStructuredSelection selection = snapshotsViewer.getStructuredSelection();
        if (selection != null && !selection.isEmpty())
        {
          for (Object object : selection)
          {
            if (object instanceof TargetSnapshot)
            {
              snapshots.add((TargetSnapshot)object);
            }
            else
            {
              snapshots.clear();
              return;
            }
          }
        }
      }
      finally
      {
        setEnabled(!snapshots.isEmpty());
      }
    }

    @Override
    public void run()
    {
      if (!snapshots.isEmpty())
      {
        Target target = snapshots.get(0).getTarget();
        target.deleteSnapshots(snapshots);

        deltas.clear(); // Deltas may need to be recomputed.
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DeleteOldSnapshotsAction extends Action
  {
    public DeleteOldSnapshotsAction()
    {
      super(Messages.TargetMonitor_DeleteOldSnapshots, UIPDEPlugin.INSTANCE.getImageDescriptor("delete-old.png")); //$NON-NLS-1$
      setEnabled(false);
    }

    public void updateEnablement()
    {
      Target target = getSelectedTarget();
      setEnabled(target != null && target.getSnapshots().length > 1);
    }

    @Override
    public void run()
    {
      Target target = getSelectedTarget();
      List<TargetSnapshot> oldSnapshots = target.getOldSnapshots();
      target.deleteSnapshots(oldSnapshots);

      deltas.clear(); // Deltas may need to be recomputed.
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class ToggleAction extends Action
  {
    private final String preferenceKey;

    private boolean checked;

    public ToggleAction(String text, String preferenceKey, String imageKey)
    {
      super(text, AS_CHECK_BOX);
      this.preferenceKey = preferenceKey;

      if (imageKey != null)
      {
        setImageDescriptor(UIPDEPlugin.INSTANCE.getImageDescriptor(imageKey));
      }

      checked = PREFERENCES.getBoolean(preferenceKey);
      setChecked(checked);
      toggled(checked);
    }

    @Override
    public final void run()
    {
      checked = !checked;
      PREFERENCES.setValue(preferenceKey, checked);
      toggled(checked);
    }

    protected abstract void toggled(boolean checked);
  }

  /**
   * @author Eike Stepper
   */
  private final class LinkWithEditorAction extends ToggleAction
  {
    public LinkWithEditorAction()
    {
      super(Messages.TargetMonitor_LinkWithEditor, PREF_LINK_WITH_EDITOR, "link-editor"); //$NON-NLS-1$
    }

    @Override
    protected void toggled(boolean checked)
    {
      linkWithEditor = checked;

      if (linkWithEditor)
      {
        UIUtil.asyncExec(() -> {
          IEditorPart editor = getSite().getPage().getActiveEditor();
          if (editor != null)
          {
            monitorTarget(editor);
          }
        });
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class AutoSnapshotAction extends ToggleAction
  {
    public AutoSnapshotAction()
    {
      super(Messages.TargetMonitor_AutoSnapshots, PREF_AUTO_SNAPSHOT, null);
    }

    @Override
    protected void toggled(boolean checked)
    {
      targetManager.setAutoSnapshot(checked);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class AutoFocusAction extends ToggleAction
  {
    public AutoFocusAction()
    {
      super(Messages.TargetMonitor_AutoFocus, PREF_AUTO_FOCUS, null);
    }

    @Override
    protected void toggled(boolean checked)
    {
      autoFocus = checked;

      if (autoFocus)
      {
        Target target = getSelectedTarget();
        if (target != null)
        {
          focusOn(target.getCurrentSnapshot());
        }
      }
    }
  }
}

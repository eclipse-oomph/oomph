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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.util.CatalogManager;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.provider.CatalogSelectionItemProvider;
import org.eclipse.oomph.setup.provider.IndexItemProvider;
import org.eclipse.oomph.setup.provider.ProjectCatalogItemProvider;
import org.eclipse.oomph.setup.provider.ProjectItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.provider.WorkspaceItemProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IUpdateableItemParent;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ProjectPage extends SetupWizardPage
{
  private ComposedAdapterFactory adapterFactory;

  private AdapterFactoryLabelProvider labelProvider;

  private CatalogSelector catalogSelector;

  private TreeViewer projectViewer;

  private TableViewer streamViewer;

  private Button skipButton;

  /**
   * Cannot be removed from {@link #streamViewer}.
   */
  private final Set<URI> existingStreams = new HashSet<URI>();

  public ProjectPage()
  {
    super("ProjectPage");
    setTitle("Projects");
    setDescription("Select the projects you want to work with and choose one stream per project.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    CatalogManager catalogManager = getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, false);

    adapterFactory = new ComposedAdapterFactory(getAdapterFactory());
    adapterFactory.insertAdapterFactory(new ItemProviderAdapterFactory(catalogSelector.getSelection()));
    EMFUtil.replaceReflectiveItemProvider(adapterFactory);

    final Workspace workspace = getWorkspace();
    if (workspace != null)
    {
      for (Stream stream : workspace.getStreams())
      {
        existingStreams.add(EcoreUtil.getURI(stream));
      }
    }

    ResourceSet resourceSet = getResourceSet();
    AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack()
    {
      @Override
      public void execute(Command command)
      {
        super.execute(command);
        final Collection<?> affectedObjects = command.getAffectedObjects();
        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            projectViewer.setSelection(new StructuredSelection(affectedObjects.toArray()), true);
          }
        });
      }
    }, resourceSet);
    resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(editingDomain));

    SashForm sashForm = new SashForm(parent, SWT.VERTICAL);

    GridLayout upperLayout = new GridLayout();
    upperLayout.marginHeight = 0;

    Composite upperComposite = new Composite(sashForm, SWT.NONE);
    upperComposite.setLayout(upperLayout);

    GridLayout filterLayout = new GridLayout(2, false);
    filterLayout.marginWidth = 0;
    filterLayout.marginHeight = 0;

    Composite filterComposite = new Composite(upperComposite, SWT.NONE);
    filterComposite.setLayout(filterLayout);
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    GridLayout filterPlaceholderLayout = new GridLayout();
    filterPlaceholderLayout.marginWidth = 0;
    filterPlaceholderLayout.marginHeight = 0;

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(filterPlaceholderLayout);
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    ToolBar filterToolBar = new ToolBar(filterComposite, SWT.FLAT | SWT.RIGHT);

    final ToolItem showHierarchyButton = new ToolItem(filterToolBar, SWT.CHECK);
    showHierarchyButton.setToolTipText("Show Hierarchy");
    showHierarchyButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("tree"));
    showHierarchyButton.setSelection(true);

    final ToolItem collapseAllButton = new ToolItem(filterToolBar, SWT.NONE);
    collapseAllButton.setToolTipText("Collapse All");
    collapseAllButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("collapseall"));

    final ToolItem refreshButton = new ToolItem(filterToolBar, SWT.NONE);
    refreshButton.setToolTipText("Refresh");
    refreshButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("refresh"));

    final ToolItem catalogsButton = new ToolItem(filterToolBar, SWT.DROP_DOWN);
    catalogsButton.setToolTipText("Select Catalogs");
    catalogsButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("catalogs"));
    catalogSelector.configure(catalogsButton);

    FilteredTree filteredTree = new FilteredTree(upperComposite, SWT.BORDER | SWT.MULTI, new PatternFilter(), true);
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);

    projectViewer = filteredTree.getViewer();
    labelProvider = new AdapterFactoryLabelProvider.FontProvider(adapterFactory, projectViewer)
    {
      private final Font baseFont = projectViewer.getControl().getFont();

      @Override
      public Font getFont(Object object)
      {
        if (object instanceof Project)
        {
          Project project = (Project)object;
          if (isSelected(project))
          {
            return ExtendedFontRegistry.INSTANCE.getFont(baseFont, IItemFontProvider.BOLD_FONT);
          }
        }

        return super.getFont(object);
      }
    };

    projectViewer.setLabelProvider(labelProvider);
    projectViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);

        getShell().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            if (projectViewer.getExpandedElements().length == 0)
            {
              final Object[] elements = getElements(projectViewer.getInput());
              if (elements.length > 0)
              {
                projectViewer.expandToLevel(elements[0], 1);
              }
            }
          }
        });
      }
    });

    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance(), LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance(),
        URLTransfer.getInstance() };
    projectViewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, projectViewer)
    {
      @Override
      protected Collection<?> getDragSource(DropTargetEvent event)
      {
        // Check whether the current data type can be transfered locally.
        //
        URLTransfer urlTransfer = URLTransfer.getInstance();
        if (urlTransfer.isSupportedType(event.currentDataType))
        {
          // Motif kludge: we would get something random instead of null.
          //
          if (IS_MOTIF)
          {
            return null;
          }

          // Transfer the data and, if non-null, extract it.
          //
          Object object = urlTransfer.nativeToJava(event.currentDataType);
          return object == null ? null : extractDragSource(object);
        }

        return super.getDragSource(event);
      }

      @Override
      protected Collection<?> extractDragSource(Object object)
      {
        if (object instanceof String)
        {
          return Collections.singleton(URI.createURI((String)object));
        }

        return super.extractDragSource(object);
      }
    });

    final Tree projectTree = projectViewer.getTree();
    projectTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout bucketCompositeLayout = new GridLayout();
    bucketCompositeLayout.marginWidth = 0;
    bucketCompositeLayout.marginHeight = 0;

    ToolBar bucketToolBar = new ToolBar(upperComposite, SWT.FLAT | SWT.CENTER);
    bucketToolBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    bucketToolBar.setSize(46, 22);

    ToolItem addButton = new ToolItem(bucketToolBar, SWT.PUSH);
    addButton.setToolTipText("Add Project (or double-click in upper tree)");
    addButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add"));

    ToolItem removeButton = new ToolItem(bucketToolBar, SWT.PUSH);
    removeButton.setToolTipText("Remove Project (or double-click in lower table)");
    removeButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("remove"));

    addButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        addSelectedProjects();
      }
    });

    removeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        removeSelectedStreams();
      }
    });

    Composite lowerComposite = new Composite(sashForm, SWT.NONE);
    GridLayout lowerLayout = new GridLayout();
    lowerLayout.marginHeight = 0;
    lowerLayout.marginBottom = 5;
    lowerComposite.setLayout(lowerLayout);

    TableColumnLayout streamTableLayout = new TableColumnLayout();
    Composite streamComposite = new Composite(lowerComposite, SWT.NONE);
    streamComposite.setLayout(streamTableLayout);
    streamComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    streamViewer = new TableViewer(streamComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
    streamViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

    Table streamTable = streamViewer.getTable();
    streamTable.setLinesVisible(true);
    streamTable.setHeaderVisible(true);
    streamTable.setLayoutData(new GridData(GridData.FILL_BOTH));

    TableViewerColumn catalogViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    catalogViewerColumn.setLabelProvider(new CatalogColumnLabelProvider());

    TableColumn catalogColumn = catalogViewerColumn.getColumn();
    catalogColumn.setText("Catalog");
    streamTableLayout.setColumnData(catalogColumn, new ColumnWeightData(30, true));

    TableViewerColumn projectViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    projectViewerColumn.setLabelProvider(new ProjectColumnLabelProvider());

    TableColumn projectColumn = projectViewerColumn.getColumn();
    projectColumn.setText("Project");
    streamTableLayout.setColumnData(projectColumn, new ColumnWeightData(40, true));

    TableViewerColumn streamViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    streamViewerColumn.setLabelProvider(new StreamColumnLabelProvider());
    hookCellEditor(streamViewerColumn);

    TableColumn streamColumn = streamViewerColumn.getColumn();
    streamColumn.setText("Stream");
    streamTableLayout.setColumnData(streamColumn, new ColumnWeightData(30, true));

    sashForm.setWeights(new int[] { 3, 1 });

    CatalogSelection selection = catalogSelector.getSelection();
    projectViewer.setInput(selection);
    streamViewer.setInput(workspace);

    showHierarchyButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        collapseAllButton.setEnabled(showHierarchyButton.getSelection());
      }
    });

    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        projectViewer.collapseAll();
      }
    });

    refreshButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        getWizard().reloadIndex();
      }
    });

    projectViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)projectViewer.getSelection();
        Object element = selection.getFirstElement();
        if (element instanceof Project)
        {
          Project project = (Project)element;

          Workspace workspace = getWorkspace();
          if (workspace != null)
          {
            for (Stream stream : workspace.getStreams())
            {
              if (stream.getProject() == project)
              {
                streamViewer.setSelection(new StructuredSelection(stream));
                removeSelectedStreams();
                return;
              }
            }
          }

          if (!project.getStreams().isEmpty())
          {
            addSelectedProjects();
            return;
          }
        }

        boolean expanded = projectViewer.getExpandedState(element);
        projectViewer.setExpandedState(element, !expanded);
      }
    });

    streamViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        removeSelectedStreams();
      }
    });

    return sashForm;
  }

  @Override
  protected void createCheckButtons()
  {
    skipButton = addCheckButton("Skip Project Selection", "Enable the Next button to proceed without provisioning projects", false, "skipButton");
    skipButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Workspace workspace = getWorkspace();
        if (skipButton.getSelection())
        {
          if (workspace != null)
          {
            streamViewer.setSelection(new StructuredSelection(workspace.getStreams()));
          }

          removeSelectedStreams();
          setPageComplete(true);
        }
        else
        {
          setPageComplete(workspace != null && workspace.getStreams().size() != existingStreams.size());
        }
      }
    });

    setPageComplete(skipButton.getSelection());
  }

  @Override
  public void enterPage(boolean forward)
  {
    if (forward)
    {
      if (projectViewer.getSelection().isEmpty())
      {
        CatalogSelection selection = catalogSelector.getSelection();
        List<Project> projects = new ArrayList<Project>();
        for (Stream stream : selection.getSelectedStreams())
        {
          projects.add(stream.getProject());
        }

        projectViewer.setSelection(new StructuredSelection(projects), true);
      }
    }
  }

  private void saveProjectStreamSelection(Stream stream)
  {
    CatalogManager catalogManager = catalogSelector.getCatalogManager();
    EMap<Project, Stream> defaultStreams = catalogManager.getSelection().getDefaultStreams();
    Project project = stream.getProject();
    defaultStreams.put(project, stream);
    catalogManager.saveSelection();
  }

  private void hookCellEditor(TableViewerColumn viewerColumn)
  {
    Table table = streamViewer.getTable();

    final ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(table);
    cellEditor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_KEY_ACTIVATION | ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
    cellEditor.setLabelProvider(labelProvider);
    cellEditor.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object[] getElements(Object inputElement)
      {
        Project project = (Project)inputElement;
        return project.getStreams().toArray();
      }
    });

    viewerColumn.setEditingSupport(new EditingSupport(streamViewer)
    {
      @Override
      protected void setValue(Object element, Object value)
      {
        if (element != value)
        {
          Workspace workspace = getWorkspace();
          if (workspace != null)
          {
            EList<Stream> streams = workspace.getStreams();
            int index = streams.indexOf(element);
            if (index != -1)
            {
              Stream stream = (Stream)value;
              streams.set(index, stream);
              saveProjectStreamSelection(stream);
              streamViewer.refresh();
            }
          }
        }
      }

      @Override
      protected Object getValue(Object element)
      {
        return element;
      }

      @Override
      protected CellEditor getCellEditor(Object element)
      {
        Stream stream = (Stream)element;
        cellEditor.setInput(stream.getProject());
        return cellEditor;
      }

      @Override
      protected boolean canEdit(Object element)
      {
        return true;
      }
    });

    streamViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        ComboViewer comboViewer = cellEditor.getViewer();
        comboViewer.refresh();
      }
    });
  }

  private void addSelectedProjects()
  {
    List<Project> addedProjects = new ArrayList<Project>();
    List<Stream> addedStreams = new ArrayList<Stream>();

    CatalogManager catalogManager = catalogSelector.getCatalogManager();
    CatalogSelection catalogSelection = catalogManager.getSelection();
    EMap<Project, Stream> defaultStreams = catalogSelection.getDefaultStreams();

    IStructuredSelection selection = (IStructuredSelection)projectViewer.getSelection();
    for (Iterator<?> it = selection.iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof Project)
      {
        Project project = (Project)element;
        EList<Stream> projectStreams = project.getStreams();
        if (!projectStreams.isEmpty())
        {
          if (!isSelected(project))
          {
            Stream stream = defaultStreams.get(project);
            if (stream == null)
            {
              stream = projectStreams.get(0);
            }

            addedStreams.add(stream);
            addedProjects.add(project);
          }
        }
      }
    }

    if (!addedProjects.isEmpty())
    {
      Workspace workspace = getWorkspace();
      if (workspace == null)
      {
        getWizard().setSetupContext(SetupContext.create(getInstallation(), addedStreams, getUser()));
        workspace = getWorkspace();
        streamViewer.setInput(workspace);
      }
      else
      {
        EList<Stream> workspaceStreams = workspace.getStreams();
        workspaceStreams.addAll(addedStreams);
        streamViewer.refresh();
      }

      catalogSelection.getSelectedStreams().clear();
      catalogSelection.getSelectedStreams().addAll(workspace.getStreams());
      catalogManager.saveSelection();

      streamViewer.setSelection(new StructuredSelection(addedStreams));

      projectViewer.update(addedProjects.toArray(), null);

      setPageComplete(true);
      skipButton.setSelection(false);
    }
  }

  private boolean isSelected(Project project)
  {
    Workspace workspace = getWorkspace();
    if (workspace != null)
    {
      for (Stream stream : workspace.getStreams())
      {
        if (project == stream.getProject())
        {
          return true;
        }
      }
    }

    return false;
  }

  private void removeSelectedStreams()
  {
    Workspace workspace = getWorkspace();
    if (workspace != null)
    {
      EList<Stream> workspaceStreams = workspace.getStreams();
      List<Project> removedProjects = new ArrayList<Project>();

      IStructuredSelection selection = (IStructuredSelection)streamViewer.getSelection();
      for (Iterator<?> it = selection.iterator(); it.hasNext();)
      {
        Stream stream = (Stream)it.next();
        if (!existingStreams.contains(EcoreUtil.getURI(stream)))
        {
          workspaceStreams.remove(stream);
          Project project = stream.getProject();
          removedProjects.add(project);
        }
      }

      if (!removedProjects.isEmpty())
      {
        projectViewer.update(removedProjects.toArray(), null);
        projectViewer.setSelection(new StructuredSelection(removedProjects));

        CatalogManager catalogManager = catalogSelector.getCatalogManager();
        CatalogSelection catalogSelection = catalogManager.getSelection();
        catalogSelection.getSelectedStreams().clear();
        catalogSelection.getSelectedStreams().addAll(workspace.getStreams());
        catalogManager.saveSelection();

        streamViewer.refresh();

        if (workspaceStreams.size() == existingStreams.size())
        {
          setPageComplete(false);
        }
      }
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();

    adapterFactory.dispose();
  }

  private static void createProjectLabel(Project project, StringBuilder builder)
  {
    Project parentProject = project.getParentProject();
    if (parentProject != null)
    {
      createProjectLabel(parentProject, builder);
    }

    if (builder.length() != 0)
    {
      builder.append(" - ");
    }

    String label = getLabel(project);
    builder.append(label);
  }

  private static String getLabel(Scope scope)
  {
    if (scope == null)
    {
      return "";
    }

    String label = scope.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = StringUtil.safe(scope.getName());
    }

    return label;
  }

  /**
   * @author Eike Stepper
   */
  private static final class ItemProviderAdapterFactory extends SetupItemProviderAdapterFactory implements SetupPackage.Literals
  {
    private CatalogSelection selection;

    public ItemProviderAdapterFactory(CatalogSelection selection)
    {
      this.selection = selection;
    }

    @Override
    public Adapter createCatalogSelectionAdapter()
    {
      if (catalogSelectionItemProvider == null)
      {
        catalogSelectionItemProvider = new CatalogSelectionItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(CATALOG_SELECTION__PROJECT_CATALOGS);
            }

            return childrenFeatures;
          }

          @Override
          protected Object overlayImage(Object object, Object image)
          {
            return image;
          }
        };
      }

      return catalogSelectionItemProvider;
    }

    @Override
    public Adapter createIndexAdapter()
    {
      if (indexItemProvider == null)
      {
        indexItemProvider = new IndexItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(INDEX__PROJECT_CATALOGS);
            }

            return childrenFeatures;
          }

          @Override
          protected Object overlayImage(Object object, Object image)
          {
            return image;
          }
        };
      }

      return indexItemProvider;
    }

    @Override
    public Adapter createProjectCatalogAdapter()
    {
      return new ProjectCatalogItemProvider(this)
      {
        private AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(getRootAdapterFactory());

        @Override
        public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
        {
          if (childrenFeatures == null)
          {
            childrenFeatures = new ArrayList<EStructuralFeature>();
            childrenFeatures.add(PROJECT_CONTAINER__PROJECTS);
          }

          return childrenFeatures;
        }

        @Override
        public Collection<?> getChildren(Object object)
        {
          @SuppressWarnings("unchecked")
          Collection<Project> result = new ArrayList<Project>((Collection<? extends Project>)super.getChildren(object));
          for (Iterator<Project> it = result.iterator(); it.hasNext();)
          {
            Project project = it.next();
            if (project.getStreams().isEmpty() && project.getProjects().isEmpty())
            {
              it.remove();
            }
            else
            {
              IUpdateableItemParent updateableItemParent = (IUpdateableItemParent)getRootAdapterFactory().adapt(project, IEditingDomainItemProvider.class);
              updateableItemParent.setParent(project, object);
            }

          }
          return result;
        }

        @Override
        protected Command createDragAndDropCommand(EditingDomain domain, ResourceSet resourceSet, float location, int operations, int operation,
            Collection<URI> collection)
        {
          ProjectCatalog projectCatalog = (ProjectCatalog)getTarget();
          for (Project project : projectCatalog.getProjects())
          {
            Command command = itemDelegator.createCommand(project, domain, DragAndDropCommand.class, new CommandParameter(project,
                new DragAndDropCommand.Detail(location, operations, operation), collection));

            if (command.canExecute())
            {
              return command;
            }
          }

          return UnexecutableCommand.INSTANCE;
        }

        @Override
        public Object getParent(Object object)
        {
          return selection;
        }

        @Override
        protected Object overlayImage(Object object, Object image)
        {
          return image;
        }
      };
    }

    @Override
    public Adapter createProjectAdapter()
    {
      class SpecializedProjectItemProvider extends ProjectItemProvider implements IUpdateableItemParent
      {
        private Object parent;

        private SpecializedProjectItemProvider(AdapterFactory adapterFactory)
        {
          super(adapterFactory);
        }

        @Override
        public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
        {
          if (childrenFeatures == null)
          {
            childrenFeatures = new ArrayList<EStructuralFeature>();
            childrenFeatures.add(PROJECT_CONTAINER__PROJECTS);
          }

          return childrenFeatures;
        }

        @Override
        protected Command createDragAndDropCommand(EditingDomain domain, final ResourceSet resourceSet, float location, int operations, int operation,
            Collection<URI> collection)
        {
          final Project targetProject = (Project)getTarget();
          Resource directResource = ((InternalEObject)targetProject).eDirectResource();
          if (directResource != null && "user".equals(directResource.getURI().scheme()))
          {

            return new DragAndDropCommand(domain, resourceSet, location, operations, operation, collection)
            {
              final Set<Project> projects = new LinkedHashSet<Project>();

              final Set<Project> affectedObjects = new LinkedHashSet<Project>();

              @Override
              public void execute()
              {
                EList<Project> targetProjects = targetProject.getProjects();
                LOOP: for (Project project : projects)
                {
                  if (operation != DROP_LINK)
                  {
                    if (project.eContainer() != null)
                    {
                      project = EcoreUtil.copy(project);
                    }
                    else
                    {
                      Resource resource = project.eResource();
                      resource.getContents().clear();
                      resourceSet.getResources().remove(resource);
                    }
                  }

                  affectedObjects.add(project);

                  String name = project.getName();
                  for (Project otherProject : targetProjects)
                  {
                    if (name.equals(otherProject.getName()))
                    {
                      targetProjects.set(targetProjects.indexOf(otherProject), project);
                      continue LOOP;
                    }
                  }

                  targetProjects.add(project);
                }

                EMFUtil.saveEObject(targetProject);
                ProjectCatalog projectCatalog = targetProject.getProjectCatalog();
                if (projectCatalog != null)
                {
                  fireNotifyChanged(new ViewerNotification(new ENotificationImpl((InternalEObject)projectCatalog, Notification.SET,
                      SetupPackage.Literals.PRODUCT_CATALOG__PRODUCTS, targetProject, targetProject, projectCatalog.getProjects().indexOf(targetProject)),
                      projectCatalog, true, false));
                }
              }

              @Override
              protected boolean prepare()
              {
                projects.clear();

                for (Object value : collection)
                {
                  if (value instanceof URI)
                  {
                    URI uri = (URI)value;
                    BaseResource resource = EMFUtil.loadResourceSafely(resourceSet, uri);
                    Project project = (Project)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.PROJECT);
                    if (project != null && project.getName() != null && (operation == DROP_COPY || project.eContainer() == null))
                    {
                      projects.add(project);
                    }
                  }
                }

                if (operation == DROP_MOVE)
                {
                  operation = DROP_LINK;
                }

                return !projects.isEmpty();
              }

              @Override
              public Collection<?> getAffectedObjects()
              {
                return affectedObjects;
              }

              @Override
              public void redo()
              {
                throw new UnsupportedOperationException();
              }
            };
          }

          return UnexecutableCommand.INSTANCE;
        }

        @Override
        protected Object overlayImage(Object object, Object image)
        {
          return image;
        }

        @Override
        public Object getParent(Object object)
        {
          return parent == null ? super.getParent(object) : parent;
        }

        public void setParent(Object object, Object parent)
        {
          this.parent = parent;
        }
      }

      return new SpecializedProjectItemProvider(this);
    }

    @Override
    public Adapter createWorkspaceAdapter()
    {
      if (workspaceItemProvider == null)
      {
        workspaceItemProvider = new WorkspaceItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(WORKSPACE__STREAMS);
            }

            return childrenFeatures;
          }

          @Override
          public void notifyChanged(Notification notification)
          {
            switch (notification.getFeatureID(Workspace.class))
            {
              case SetupPackage.WORKSPACE__STREAMS:
                updateChildren(notification);
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
            }

            super.notifyChanged(notification);
          }

          @Override
          protected Object overlayImage(Object object, Object image)
          {
            return image;
          }
        };
      }

      return workspaceItemProvider;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class CatalogColumnLabelProvider extends ColumnLabelProvider
  {
    private final Image image = labelProvider.getImage(SetupFactory.eINSTANCE.createProjectCatalog());

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      ProjectCatalog catalog = stream.getProject().getProjectCatalog();
      return getLabel(catalog);
    }

    @Override
    public Image getImage(Object element)
    {
      return image;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ProjectColumnLabelProvider extends ColumnLabelProvider
  {
    private final Image image = labelProvider.getImage(SetupFactory.eINSTANCE.createProject());

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      StringBuilder builder = new StringBuilder();
      createProjectLabel(stream.getProject(), builder);
      return builder.toString();
    }

    @Override
    public Image getImage(Object element)
    {
      return image;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class StreamColumnLabelProvider extends ColumnLabelProvider
  {
    private final Image image = labelProvider.getImage(SetupFactory.eINSTANCE.createStream());

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      return getLabel(stream);
    }

    @Override
    public Image getImage(Object element)
    {
      return image;
    }
  }
}

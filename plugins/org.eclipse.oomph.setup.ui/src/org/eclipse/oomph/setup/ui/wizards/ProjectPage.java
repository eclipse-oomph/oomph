/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ProjectContainer;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.provider.CatalogSelectionItemProvider;
import org.eclipse.oomph.setup.provider.IndexItemProvider;
import org.eclipse.oomph.setup.provider.ProjectCatalogItemProvider;
import org.eclipse.oomph.setup.provider.ProjectItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.provider.WorkspaceItemProvider;
import org.eclipse.oomph.setup.ui.SetupLabelProvider.DisabledImageDescriptor;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.IndexLoader;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.ui.ButtonAnimator;
import org.eclipse.oomph.ui.FilteredTreeWithoutWorkbench;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.DragAndDropFeedback;
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
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.PatternFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class ProjectPage extends SetupWizardPage
{
  /**
   * Cannot be removed from {@link #streamViewer}.
   */
  private final Set<URI> existingStreams = new HashSet<URI>();

  private final SelectionMemento selectionMemento;

  private final AtomicBoolean selectionMementoTried = new AtomicBoolean();

  private ComposedAdapterFactory adapterFactory;

  private ToolTipLabelProvider labelProvider;

  private CatalogSelector catalogSelector;

  private CheckboxTreeViewer projectViewer;

  private TableViewer streamViewer;

  private AddButtonAnimator addButtonAnimator;

  private boolean projectsChanged;

  private boolean inactive;

  private ConfigurationListener configurationListener;

  private SetupTransferSupport.DropListener dropListener;

  private FilteredTreeWithoutWorkbench.WithCheckboxes filteredTree;

  public ProjectPage(SelectionMemento selectionMemento)
  {
    super("ProjectPage");
    this.selectionMemento = selectionMemento;
    setTitle("Projects");
    setDescription("Check the projects you want to provision, and for each choose its stream in the table column.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    CatalogManager catalogManager = getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, false);

    adapterFactory = new ComposedAdapterFactory(getAdapterFactory());
    adapterFactory.insertAdapterFactory(new ItemProviderAdapterFactory(catalogSelector.getSelection()));
    BaseEditUtil.replaceReflectiveItemProvider(adapterFactory);

    ResourceSet resourceSet = getResourceSet();
    final AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack()
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

    SashForm sashForm = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);

    Composite upperComposite = new Composite(sashForm, SWT.NONE);
    upperComposite.setLayout(UIUtil.createGridLayout(1));

    Composite filterComposite = new Composite(upperComposite, SWT.NONE);
    filterComposite.setLayout(UIUtil.createGridLayout(2));
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(UIUtil.createGridLayout(1));
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    ToolBar filterToolBar = new ToolBar(filterComposite, SWT.FLAT | SWT.RIGHT);

    final ToolItem addProjectButton = new ToolItem(filterToolBar, SWT.NONE);
    addProjectButton.setToolTipText("Add user projects");
    addProjectButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add_project"));
    AccessUtil.setKey(addProjectButton, "addProject");

    final Set<ProjectCatalog> projectCatalogs = new HashSet<ProjectCatalog>();
    addProjectButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        ResourceDialog dialog = new AddUserProjectDialog(getShell(), projectCatalogs, catalogSelector, editingDomain);
        dialog.open();
      }
    });

    final ToolItem removeProjectButton = new ToolItem(filterToolBar, SWT.NONE);
    removeProjectButton.setToolTipText("Remove the selected user projects");
    removeProjectButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("remove_project"));
    removeProjectButton.setEnabled(false);
    AccessUtil.setKey(removeProjectButton, "removeProject");

    final List<Project> projects = new ArrayList<Project>();
    final List<Project> userProjects = new ArrayList<Project>();
    final SelectionAdapter removeProjectSelectionAdapter = new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent event)
      {
        Workspace workspace = getWorkspace();
        List<Stream> streamsToRemove = new ArrayList<Stream>();

        List<Project> parents = new UniqueEList<Project>();
        for (Project project : userProjects)
        {
          Project parentProject = project.getParentProject();
          parentProject.getProjects().remove(project);
          parents.add(parentProject);

          if (workspace != null)
          {
            EList<Stream> streams = project.getStreams();
            for (Stream stream : workspace.getStreams())
            {
              if (streams.contains(stream))
              {
                streamsToRemove.add(stream);
                break;
              }
            }
          }
        }

        for (Project parent : parents)
        {
          BaseUtil.saveEObject(parent);
        }

        if (!streamsToRemove.isEmpty())
        {
          streamViewer.setSelection(new StructuredSelection(streamsToRemove));
          removeSelectedStreams();
        }

        projectViewer.setSelection(new StructuredSelection(parents));
      }
    };
    removeProjectButton.addSelectionListener(removeProjectSelectionAdapter);

    final ToolItem collapseAllButton = new ToolItem(filterToolBar, SWT.NONE);
    collapseAllButton.setToolTipText("Collapse All");
    collapseAllButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("collapse-all"));
    AccessUtil.setKey(collapseAllButton, "collapse");

    final boolean supportsIndexSwitching = getPreviousPage() instanceof SetupWizardPage;
    configurationListener = new ConfigurationListener(getWizard().getTransferSupport(), catalogManager, filterToolBar)
    {
      @Override
      protected void filter(Collection<? extends Resource> resources)
      {
        if (!supportsIndexSwitching)
        {
          for (Iterator<? extends Resource> it = resources.iterator(); it.hasNext();)
          {
            Resource resource = it.next();
            if (ConfigurationListener.isIndexURI(resource.getURI()))
            {
              it.remove();
            }
          }
        }
      }
    };

    final ToolItem catalogsButton = new ToolItem(filterToolBar, SWT.DROP_DOWN);
    catalogsButton.setToolTipText("Select Catalogs");
    catalogsButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("catalogs"));
    catalogSelector.configure(getWizard(), catalogsButton, supportsIndexSwitching);
    AccessUtil.setKey(catalogsButton, "catalogs");

    PatternFilter patternFilter = new PatternFilter()
    {
      @Override
      protected boolean isLeafMatch(Viewer viewer, Object element)
      {
        boolean result = super.isLeafMatch(viewer, element);
        if (!result)
        {
          if (element instanceof Project)
          {
            Project project = (Project)element;
            ProjectContainer projectContainer = project.getProjectContainer();
            if (projectContainer instanceof Project)
            {
              Project parentProject = (Project)projectContainer;
              return isLeafMatch(viewer, parentProject);
            }
          }
        }

        return result;
      }
    };

    filteredTree = new FilteredTreeWithoutWorkbench.WithCheckboxes(upperComposite, SWT.BORDER | SWT.MULTI, patternFilter, null);
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);
    AccessUtil.setKey(filteredTree.getFilterControl(), "filter");

    projectViewer = filteredTree.getViewer();
    projectViewer.setCheckStateProvider(new ICheckStateProvider()
    {
      private Set<ProjectContainer> projectContainers;

      private boolean containsAll(List<Project> projects)
      {
        if (projects.isEmpty())
        {
          return true;
        }

        for (Project project : projects)
        {
          if (!projectContainers.contains(project))
          {
            // Ignore empty leaf projects.
            if (project.getProjects().isEmpty() && project.getStreams().isEmpty())
            {
              continue;
            }

            return false;
          }

          if (!containsAll(project.getProjects()))
          {
            return false;
          }
        }

        return true;
      }

      public boolean isGrayed(Object element)
      {
        Workspace workspace = getWorkspace();
        if (workspace == null)
        {
          return false;
        }

        projectContainers = new HashSet<ProjectContainer>();
        for (Stream stream : workspace.getStreams())
        {
          for (ProjectContainer projectContainer = stream.getProject(); projectContainer != null; projectContainer = projectContainer.getProjectContainer())
          {
            projectContainers.add(projectContainer);
          }
        }

        ProjectContainer projectContainer = (ProjectContainer)element;
        return !containsAll(projectContainer.getProjects());
      }

      public boolean isChecked(Object element)
      {
        Workspace workspace = getWorkspace();
        if (workspace != null)
        {
          for (Stream stream : workspace.getStreams())
          {
            for (ProjectContainer projectContainer = stream.getProject(); projectContainer != null; projectContainer = projectContainer.getProjectContainer())
            {
              if (projectContainer == element)
              {
                return true;
              }
            }
          }
        }

        return false;
      }
    });

    ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(projectViewer, new LocationListener()
    {
      public void changing(LocationEvent event)
      {
        if (!"about:blank".equals(event.location))
        {
          OS.INSTANCE.openSystemBrowser(event.location);
          event.doit = false;
        }
      }

      public void changed(LocationEvent event)
      {
      }
    });

    labelProvider = new ToolTipLabelProvider(adapterFactory, toolTipSupport)
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

      @Override
      public String getToolTipText(Object element)
      {
        if (element instanceof Scope)
        {
          Scope scope = (Scope)element;
          String localBrandingImageURI = SetupWizard.getLocalBrandingImageURI(scope);
          URI brandingSiteURI = SetupWizard.getBrandingSiteURI(scope);
          String label = SetupCoreUtil.getLabel(scope);

          StringBuilder result = new StringBuilder();
          result.append("<span style='white-space: nowrap; font-size: 150%;'><b>");
          if (brandingSiteURI != null)
          {
            result.append("<a style='text-decoration: none; color: inherit;' href='");
            result.append(brandingSiteURI);
            result.append("'>");
          }

          result.append("<img style='padding-top: 4px;' src='");
          result.append(localBrandingImageURI);
          result.append("' width='42' height='42' align='absmiddle'/>&nbsp;");
          result.append(DiagnosticDecorator.escapeContent(label).replace(" ", "&nbsp;"));
          result.append("</b></span>");
          if (brandingSiteURI != null)
          {
            result.append("</a>");
          }

          String description = scope.getDescription();
          if (!StringUtil.isEmpty(description))
          {
            result.append("<br/>");
            result.append("<span style='font-size: 50%;'><br/></span>");
            result.append(description);
            result.append("<br/>");
          }

          // Add extra invisible lines to convince the tool tip size calculation that the text is 3 lines longer.
          // result.append("<div style='height=0px; display:none;'>&nbsp;&nbsp;&nbsp;&nbps;&nbps;&nbps;&nbps;&nbps;&nbps;&nbps;<br/><br/></br></div>");
          result.append("<div style='height=0px; display:none;'>&nbps;&nbps;&nbps;&nbps;&nbps;<br/><br/></br></div>");

          return result.toString();
        }

        return "";
      }
    };

    projectViewer.setLabelProvider(labelProvider);
    projectViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      private boolean applySelectionMemento()
      {
        List<URI> uris = selectionMemento.getStreams();
        if (uris != null)
        {
          List<Project> projects = new ArrayList<Project>();
          ResourceSet resourceSet = getResourceSet();

          for (URI uri : uris)
          {
            EObject object = resourceSet.getEObject(uri, true);
            if (object instanceof Stream)
            {
              Stream stream = (Stream)object;
              projects.add(stream.getProject());
            }
          }

          if (!projects.isEmpty())
          {
            projectViewer.setSelection(new StructuredSelection(projects), true);
            addProjects(projects);
            addProjectButton.setEnabled(false);
            gotoNextPage();
            return true;
          }
        }

        return false;
      }

      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);

        Workspace workspace = getWorkspace();
        if (streamViewer.getInput() != workspace)
        {
          streamViewer.setInput(workspace);
        }

        if (notification.getFeature() == SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS)
        {
          getShell().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              if (!selectionMementoTried.getAndSet(true))
              {
                if (applySelectionMemento())
                {
                  return;
                }
              }

              if (projectViewer.getExpandedElements().length == 0)
              {
                Object[] elements = getElements(projectViewer.getInput());
                if (elements.length > 0)
                {
                  projectViewer.setExpandedState(elements[0], true);
                }
              }
            }
          });
        }
      }
    });

    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance(), LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance(),
        URLTransfer.getInstance() };
    projectViewer.addDropSupport(dndOperations, transfers, new URIDropAdapter(editingDomain, projectViewer));

    final Tree projectTree = projectViewer.getTree();
    projectTree.setLayoutData(new GridData(GridData.FILL_BOTH));
    addHelpCallout(projectTree, 1);

    ToolBar bucketToolBar = new ToolBar(upperComposite, SWT.FLAT | SWT.CENTER);
    bucketToolBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    bucketToolBar.setSize(46, 22);

    final ToolItem addButton = new ToolItem(bucketToolBar, SWT.PUSH);
    addButton.setToolTipText("Add Projects (or double-click in upper tree)");
    addButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add"));
    addButton.setEnabled(false);
    AccessUtil.setKey(addButton, "choose");
    addButtonAnimator = new AddButtonAnimator(addButton);

    final ToolItem removeButton = new ToolItem(bucketToolBar, SWT.PUSH);
    removeButton.setToolTipText("Remove Projects (or double-click in lower table)");
    removeButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("remove"));
    removeButton.setEnabled(false);
    AccessUtil.setKey(removeButton, "unchoose");

    addButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        addSelectedProjects();
        addButton.setEnabled(false);
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
    lowerComposite.setLayout(UIUtil.createGridLayout(1));

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
    addHelpCallout(streamTable, 2);

    TableViewerColumn catalogViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    catalogViewerColumn
        .setLabelProvider(new CatalogColumnLabelProvider(labelProvider.getImage(SetupFactory.eINSTANCE.createProjectCatalog()), existingStreams));

    TableColumn catalogColumn = catalogViewerColumn.getColumn();
    catalogColumn.setText("Catalog");
    streamTableLayout.setColumnData(catalogColumn, new ColumnWeightData(30, true));

    TableViewerColumn projectViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    projectViewerColumn.setLabelProvider(new ProjectColumnLabelProvider(labelProvider.getImage(SetupFactory.eINSTANCE.createProject()), existingStreams));

    TableColumn projectColumn = projectViewerColumn.getColumn();
    projectColumn.setText("Project");
    streamTableLayout.setColumnData(projectColumn, new ColumnWeightData(40, true));

    TableViewerColumn streamViewerColumn = new TableViewerColumn(streamViewer, SWT.NONE);
    streamViewerColumn.setLabelProvider(new StreamColumnLabelProvider(labelProvider.getImage(SetupFactory.eINSTANCE.createStream()), existingStreams));
    hookCellEditor(streamViewerColumn);

    TableColumn streamColumn = streamViewerColumn.getColumn();
    streamColumn.setText("Stream");
    streamTableLayout.setColumnData(streamColumn, new ColumnWeightData(30, true));

    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        projectViewer.collapseAll();
      }
    });

    projectViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        projects.clear();
        userProjects.clear();
        projectCatalogs.clear();

        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        for (Object value : selection.toArray())
        {
          if (value instanceof Project)
          {
            Project project = (Project)value;
            projects.add(project);
            projectCatalogs.add(project.getProjectCatalog());
            addRootUserProject(project);
          }
          else if (value instanceof ProjectCatalog)
          {
            ProjectCatalog projectCatalog = (ProjectCatalog)value;
            projectCatalogs.add(projectCatalog);
          }
        }

        removeProjectButton.setEnabled(!userProjects.isEmpty());

        Workspace workspace = getWorkspace();
        List<Project> projectsToAdd = new ArrayList<Project>(projects);
        if (workspace != null)
        {
          for (Stream stream : workspace.getStreams())
          {
            projectsToAdd.remove(stream.getProject());
          }
        }

        for (Project project : projectsToAdd)
        {
          if (!project.getStreams().isEmpty())
          {
            addButton.setEnabled(true);
            return;
          }
        }

        addButton.setEnabled(false);
      }

      protected void addRootUserProject(Project project)
      {
        Project parentProject = project.getParentProject();
        if (parentProject != null)
        {
          if (ConfigurationProcessor.isUserProject(parentProject))
          {
            userProjects.add(project);
          }
          else
          {
            addRootUserProject(project.getParentProject());
          }
        }
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
          EList<Stream> streams = project.getStreams();
          if (!streams.isEmpty())
          {
            Workspace workspace = getWorkspace();
            if (workspace != null)
            {
              for (Stream stream : workspace.getStreams())
              {
                if (streams.contains(stream))
                {
                  removeStreams(Collections.singletonList(stream), false);
                  addButton.setEnabled(true);
                  return;
                }
              }
            }

            addProjects(Collections.singletonList(project));
            addButton.setEnabled(false);
            return;
          }
        }

        boolean expanded = projectViewer.getExpandedState(element);
        projectViewer.setExpandedState(element, !expanded);
      }
    });

    projectViewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        ProjectContainer element = (ProjectContainer)event.getElement();
        projectViewer.setSelection(new StructuredSelection(element));

        List<Stream> streams = new ArrayList<Stream>();
        Workspace workspace = getWorkspace();
        if (workspace != null)
        {
          for (Stream stream : workspace.getStreams())
          {
            if (!existingStreams.contains(EcoreUtil.getURI(stream)))
            {
              for (ProjectContainer projectContainer = stream.getProject(); projectContainer != null; projectContainer = projectContainer.getProjectContainer())
              {
                if (projectContainer == element)
                {
                  streams.add(stream);
                }
              }
            }
          }
        }

        if (streams.isEmpty())
        {
          List<Project> projects = new ArrayList<Project>();
          if (element instanceof Project)
          {
            projects.add((Project)element);
          }
          else
          {
            projects.addAll(element.getProjects());
          }

          for (int i = 0; i < projects.size(); ++i)
          {
            projects.addAll(projects.get(i).getProjects());
          }

          addProjects(projects);
          addButton.setEnabled(false);
        }
        else
        {
          removeStreams(streams, false);
          addButton.setEnabled(!(element instanceof Project && ((Project)element).getStreams().isEmpty()));
        }

        projectViewer.refresh();
      }
    });

    projectViewer.getControl().addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent event)
      {
        if (event.character == SWT.DEL)
        {
          List<Stream> selectedProjectStreams = new ArrayList<Stream>();
          Workspace workspace = getWorkspace();
          if (workspace != null)
          {
            for (Stream stream : workspace.getStreams())
            {
              Project project = stream.getProject();
              if (projects.contains(project))
              {
                selectedProjectStreams.add(stream);
              }
            }
          }

          if (selectedProjectStreams.isEmpty())
          {
            if (removeProjectButton.isEnabled())
            {
              removeProjectSelectionAdapter.widgetSelected(null);
            }
          }
          else
          {
            streamViewer.setSelection(new StructuredSelection(selectedProjectStreams), true);
            removeSelectedStreams();
          }
        }
        else if (event.keyCode == SWT.INSERT && addButton.isEnabled())
        {
          addSelectedProjects();
          addButton.setEnabled(false);
        }
      }
    });

    streamViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        boolean enabled = false;
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        for (Object object : selection.toArray())
        {
          if (object instanceof Stream)
          {
            Stream stream = (Stream)object;
            if (!existingStreams.contains(EcoreUtil.getURI(stream)))
            {
              enabled = true;
              break;
            }
          }
        }
        removeButton.setEnabled(enabled);
      }
    });

    streamViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        removeSelectedStreams();
      }
    });

    streamViewer.getControl().addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent event)
      {
        if (event.character == SWT.DEL && removeButton.isEnabled())
        {
          removeSelectedStreams();
        }
        else if (event.keyCode == SWT.INSERT && addButton.isEnabled())
        {
          addSelectedProjects();
          addProjectButton.setEnabled(false);
        }
      }
    });

    setPageComplete(false);

    sashForm.setWeights(new int[] { 3, 1 });

    dropListener = new SetupTransferSupport.DropListener()
    {
      public void resourcesDropped(Collection<? extends Resource> resources)
      {
        SetupWizard setupWizard = getWizard();
        setupWizard.setConfigurationResources(resources);

        ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(getWizard())
        {
          @Override
          protected boolean applyStreams(List<Stream> streams)
          {
            filteredTree.clearText();
            List<Project> projects = new ArrayList<Project>();
            for (Stream stream : streams)
            {
              Project project = stream.getProject();
              projects.add(project);
            }

            addProjects(projects);
            projectViewer.setSelection(new StructuredSelection(projects), true);

            Workspace viewerWorkspace = getWorkspace();
            EList<Stream> viewerWorkspaceStreams = viewerWorkspace.getStreams();
            for (Stream stream : streams)
            {
              Project project = stream.getProject();
              LOOP: for (Stream viewerWorkspaceStream : viewerWorkspaceStreams)
              {
                if (viewerWorkspaceStream.getProject() == project)
                {
                  if (viewerWorkspaceStream != stream)
                  {
                    viewerWorkspaceStreams.remove(viewerWorkspaceStream);
                    break;
                  }

                  continue LOOP;
                }
              }

              viewerWorkspaceStreams.add(stream);
              saveProjectStreamSelection(stream);
            }

            streamViewer.refresh();
            projectViewer.refresh();
            if (isCurrentPage())
            {
              gotoNextPage();
            }

            return true;
          }

          @Override
          protected boolean applyNoStreams()
          {
            Workspace workspace = getWorkspace();
            if (workspace == null)
            {
              SetupWizard wizard = getWizard();
              wizard.setSetupContext(SetupContext.create(getInstallation(), Collections.<Stream> emptyList(), getUser()));
              workspace = getWorkspace();
              streamViewer.setInput(workspace);
            }

            return true;
          }
        };

        configurationProcessor.processWorkspace();
        IStatus status = configurationProcessor.getStatus();
        if (!status.isOK())
        {
          ErrorDialog.openError(getShell(), "Workspace Problems", null, status);
        }
      }
    };

    getWizard().getTransferSupport().addDropListener(dropListener);

    return sashForm;
  }

  protected void checkPageComplete()
  {
    // If we are in the Importer (i.e., no page before the ProjectPage) there must be at least one stream.
    Workspace workspace = getWorkspace();
    setPageComplete(getPreviousPage() instanceof SetupWizardPage || workspace != null && workspace.getStreams().size() > existingStreams.size());
  }

  @Override
  protected void handleInactivity(Display display, boolean inactive)
  {
    this.inactive = inactive;
    if (addButtonAnimator.shouldAnimate())
    {
      display.asyncExec(addButtonAnimator);
    }
  }

  @Override
  public void enterPage(boolean forward)
  {
    hookTransferControl(getShell(), getControl(), getWizard().getTransferSupport(), configurationListener);

    if (forward)
    {
      if (SetupPropertyTester.getHandlingShell() != getShell())
      {
        setErrorMessage("Another setup wizard is already open.  Complete that interaction before importing projects.");
        projectViewer.getTree().setEnabled(false);
      }
      else if (!isPageComplete() || projectViewer.getInput() == null)
      {
        Runnable initializer = new Runnable()
        {
          public void run()
          {
            // If there is an index loader, await for the index to finish loading.
            // This generally only happens if this page is the first page of the wizard, i.e., in the project importer wizard.
            IndexLoader indexLoader = getWizard().getIndexLoader();
            if (indexLoader != null)
            {
              indexLoader.awaitIndexLoad();
            }

            // Hide the view while we're updating it with input and then a selection to avoid flickering.
            Tree tree = projectViewer.getTree();
            tree.setRedraw(false);

            CatalogSelection selection = catalogSelector.getSelection();
            projectViewer.setInput(selection);

            // We definitely have a workspace with a fully mirrored resource set, so it's okay to access this on the UI thread now.
            final boolean isPageComplete = isPageComplete();
            final Workspace workspace = getWorkspace();
            if (workspace != null)
            {
              for (Iterator<Stream> it = workspace.getStreams().iterator(); it.hasNext();)
              {
                Stream stream = it.next();
                if (stream.eIsProxy())
                {
                  it.remove();
                }
                else if (!isPageComplete)
                {
                  existingStreams.add(EcoreUtil.getURI(stream));
                }
              }

              streamViewer.setInput(workspace);
            }

            List<Project> projects = new ArrayList<Project>();
            for (Stream stream : selection.getSelectedStreams())
            {
              projects.add(stream.getProject());
            }

            if (projects.isEmpty())
            {
              EList<ProjectCatalog> projectCatalogs = selection.getProjectCatalogs();
              if (!projectCatalogs.isEmpty())
              {
                projectViewer.expandToLevel(projectCatalogs.get(0), 1);
              }
            }
            else
            {
              projectViewer.setSelection(new StructuredSelection(projects), true);
            }

            tree.setRedraw(true);

            checkPageComplete();
          }
        };

        Index index = catalogSelector.getCatalogManager().getIndex();
        if (index == null)
        {
          // If there is no index yet, defer the runnable until the index loader has started.
          // This generally only happens if this page is the first page of the wizard, i.e., in the project importer wizard.
          UIUtil.timerExec(500, initializer);
        }
        else
        {
          // Otherwise, just run it.
          initializer.run();
        }
      }
    }
  }

  public static void hookTransferControl(Shell shell, Control execludedControl, SetupTransferSupport transferSupport,
      ConfigurationListener configurationListener)
  {
    shell.addShellListener(configurationListener);

    // Listen for drops on the overall composite.
    for (Control control : shell.getChildren())
    {
      if (control instanceof Composite)
      {
        transferSupport.addControl(control);
        break;
      }
    }

    // But exclude the page itself.
    transferSupport.excludeControl(execludedControl);
    configurationListener.checkConfigurationAvailability();
  }

  @Override
  public void leavePage(boolean forward)
  {
    unhookTransferControl(getShell(), getControl(), getWizard().getTransferSupport(), configurationListener);

    if (forward)
    {
      Workspace workspace = getWorkspace();
      if (workspace != null)
      {
        List<URI> uris = new ArrayList<URI>();
        for (Stream stream : workspace.getStreams())
        {
          URI uri = EcoreUtil.getURI(stream);
          uris.add(uri);
        }

        selectionMemento.setStreams(uris);
        selectionMementoTried.set(true);
      }
    }
  }

  public static void unhookTransferControl(Shell shell, Control execludedControl, SetupTransferSupport transferSupport,
      ConfigurationListener configurationListener)
  {
    shell.removeShellListener(configurationListener);
    transferSupport.removeControls();
  }

  @Override
  public void sendStats(boolean success)
  {
    if (getTrigger() == Trigger.BOOTSTRAP)
    {
      return;
    }

    super.sendStats(success);

    // If we've failed but there are streams involved, they can be the cause of the failure so don't blame the product.
    Workspace workspace = getWizard().getSetupContext().getWorkspace();
    if (workspace != null)
    {
      List<Stream> streams = new ArrayList<Stream>(workspace.getStreams());
      streams.removeAll(existingStreams);

      OS os = getPerformer().getOS();

      for (Stream stream : streams)
      {
        SetupCoreUtil.sendStats(success, stream, os);
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
        if (existingStreams.contains(EcoreUtil.getURI(stream)))
        {
          return null;
        }

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

  private void addSelectedProjects()
  {
    List<Project> projects = new ArrayList<Project>();
    IStructuredSelection selection = (IStructuredSelection)projectViewer.getSelection();
    for (Object element : selection.toArray())
    {
      if (element instanceof Project)
      {
        Project project = (Project)element;
        projects.add(project);
      }
    }

    addProjects(projects);
  }

  private void addProjects(List<Project> projects)
  {
    projectsChanged = true;

    List<Project> addedProjects = new ArrayList<Project>();
    List<Stream> addedStreams = new ArrayList<Stream>();

    CatalogManager catalogManager = catalogSelector.getCatalogManager();
    CatalogSelection catalogSelection = catalogManager.getSelection();
    EMap<Project, Stream> defaultStreams = catalogSelection.getDefaultStreams();

    for (Project project : projects)
    {
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

    if (!addedProjects.isEmpty())
    {
      Workspace workspace = getWorkspace();
      if (workspace == null)
      {
        SetupWizard wizard = getWizard();
        wizard.setSetupContext(SetupContext.create(getInstallation(), addedStreams, getUser()));
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

      streamViewer.setSelection(new StructuredSelection(addedStreams), true);
      projectViewer.refresh();

      checkPageComplete();
    }
  }

  private void removeSelectedStreams()
  {
    List<Stream> streams = new ArrayList<Stream>();
    IStructuredSelection selection = (IStructuredSelection)streamViewer.getSelection();
    for (Object element : selection.toArray())
    {
      streams.add((Stream)element);
    }

    removeStreams(streams, true);
  }

  private void removeStreams(List<Stream> streams, boolean select)
  {
    projectsChanged = true;

    Workspace workspace = getWorkspace();
    if (workspace != null)
    {
      EList<Stream> workspaceStreams = workspace.getStreams();
      List<Project> removedProjects = new ArrayList<Project>();

      int selectionIndex = -1;
      for (Stream stream : streams)
      {
        if (!existingStreams.contains(EcoreUtil.getURI(stream)))
        {
          selectionIndex = Math.max(selectionIndex, workspaceStreams.indexOf(stream) - 1);
          workspaceStreams.remove(stream);
          Project project = stream.getProject();
          removedProjects.add(project);
        }
      }

      if (!removedProjects.isEmpty())
      {
        if (select)
        {
          projectViewer.setSelection(new StructuredSelection(removedProjects));
        }

        CatalogManager catalogManager = catalogSelector.getCatalogManager();
        CatalogSelection catalogSelection = catalogManager.getSelection();
        catalogSelection.getSelectedStreams().clear();
        catalogSelection.getSelectedStreams().addAll(workspace.getStreams());
        catalogManager.saveSelection();

        streamViewer.refresh();
        projectViewer.refresh();

        checkPageComplete();
      }

      if (streamViewer.getSelection().isEmpty() && !workspaceStreams.isEmpty())
      {
        streamViewer.setSelection(new StructuredSelection(workspaceStreams.get(selectionIndex <= 0 ? 0 : selectionIndex)), true);
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

    String label = SetupCoreUtil.getLabel(project);
    builder.append(label);
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
        protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
            Collection<?> collection)
        {
          ProjectCatalog projectCatalog = (ProjectCatalog)owner;
          for (Project project : projectCatalog.getProjects())
          {
            Command command = itemDelegator.createCommand(project, domain, DragAndDropCommand.class,
                new CommandParameter(project, new DragAndDropCommand.Detail(location, operations, operation), collection));

            if (command.canExecute())
            {
              return command;
            }
          }

          return UnexecutableCommand.INSTANCE;
        }

        @Override
        protected Command createDragAndDropCommand(EditingDomain domain, ResourceSet resourceSet, float location, int operations, int operation,
            Collection<URI> collection)
        {
          return createPrimaryDragAndDropCommand(domain, getTarget(), location, operations, operation, collection);
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
        public Object getImage(Object object)
        {
          if (object instanceof Project)
          {
            Project project = (Project)object;
            if (project.getStreams().isEmpty())
            {
              return SetupUIPlugin.INSTANCE.getSWTImage("folder");
            }
          }

          return super.getImage(object);
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
        protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
            Collection<?> collection)
        {
          final Project targetProject = (Project)getTarget();
          Resource directResource = ((InternalEObject)targetProject).eDirectResource();
          if (directResource != null && SetupContext.isUserScheme(directResource.getURI().scheme()))
          {
            final ResourceSet resourceSet = domain.getResourceSet();
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

                BaseUtil.saveEObject(targetProject);
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
                    BaseResource resource = BaseUtil.loadResourceSafely(resourceSet, uri);
                    Project project = (Project)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.PROJECT);
                    if (project != null && project.getName() != null && (operation == DROP_COPY || project.eContainer() == null))
                    {
                      projects.add(project);
                    }
                  }
                  else if (value instanceof Project)
                  {
                    Project project = (Project)value;
                    if (project.getName() != null && (operation == DROP_COPY || project.eContainer() == null))
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
        protected Command createDragAndDropCommand(EditingDomain domain, final ResourceSet resourceSet, float location, int operations, int operation,
            Collection<URI> collection)
        {
          return createPrimaryDragAndDropCommand(domain, getTarget(), location, operations, operation, collection);
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

  private static class DisablingColumnLabelProvider extends ColumnLabelProvider
  {
    private static final Color DARK_GRAY = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);

    private final Image image;

    private final Image disabledImage;

    private final Set<URI> existingStreams;

    public DisablingColumnLabelProvider(Image image, Set<URI> existingStreams)
    {
      this.image = image;
      this.existingStreams = existingStreams;
      disabledImage = ExtendedImageRegistry.INSTANCE.getImage(new DisabledImageDescriptor(image));
    }

    @Override
    public Color getForeground(Object element)
    {
      if (isDisabled(element))
      {
        return DARK_GRAY;
      }

      return super.getForeground(element);
    }

    @Override
    public Image getImage(Object element)
    {
      return isDisabled(element) ? disabledImage : image;
    }

    private boolean isDisabled(Object element)
    {
      return existingStreams.contains(EcoreUtil.getURI((EObject)element));
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class CatalogColumnLabelProvider extends DisablingColumnLabelProvider
  {
    public CatalogColumnLabelProvider(Image image, Set<URI> existingStreams)
    {
      super(image, existingStreams);
    }

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      ProjectCatalog catalog = stream.getProject().getProjectCatalog();
      return SetupCoreUtil.getLabel(catalog);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ProjectColumnLabelProvider extends DisablingColumnLabelProvider
  {
    public ProjectColumnLabelProvider(Image image, Set<URI> existingStreams)
    {
      super(image, existingStreams);
    }

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      StringBuilder builder = new StringBuilder();
      createProjectLabel(stream.getProject(), builder);
      return builder.toString();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class StreamColumnLabelProvider extends DisablingColumnLabelProvider
  {
    public StreamColumnLabelProvider(Image image, Set<URI> existingStreams)
    {
      super(image, existingStreams);
    }

    @Override
    public String getText(Object element)
    {
      Stream stream = (Stream)element;
      return SetupCoreUtil.getLabel(stream);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class AddButtonAnimator extends ButtonAnimator
  {
    public AddButtonAnimator(ToolItem addButton)
    {
      super(SetupUIPlugin.INSTANCE, addButton, "add", 7);
    }

    @Override
    public Shell getShell()
    {
      IWizardContainer container = getContainer();
      return container == null ? null : container.getShell();
    }

    @Override
    protected boolean doAnimate()
    {
      return inactive && !projectsChanged;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class AddUserProjectDialog extends ResourceDialog
  {
    private final Set<ProjectCatalog> projectCatalogs;

    private final CatalogSelector catalogSelector;

    private final AdapterFactoryEditingDomain editingDomain;

    private ComboViewer catalogViewer;

    public AddUserProjectDialog(Shell parent, Set<ProjectCatalog> projectCatalogs, CatalogSelector catalogSelector, AdapterFactoryEditingDomain editingDomain)
    {
      super(parent, "Add User Projects", SWT.OPEN | SWT.MULTI);
      this.projectCatalogs = projectCatalogs;
      this.catalogSelector = catalogSelector;
      this.editingDomain = editingDomain;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(UIUtil.createGridLayout(1));
      main.setLayoutData(new GridData(GridData.FILL_BOTH));

      Composite upperComposite = new Composite(main, 0);
      GridLayout upperLayout = new GridLayout(2, false);
      upperLayout.marginTop = 10;
      upperLayout.marginWidth = 10;
      upperComposite.setLayout(upperLayout);
      upperComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
      applyDialogFont(upperComposite);

      Label label = new Label(upperComposite, SWT.NONE);
      label.setText("Catalog:");
      label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

      catalogViewer = new ComboViewer(upperComposite, SWT.READ_ONLY);
      catalogViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      catalogViewer.setContentProvider(ArrayContentProvider.getInstance());
      catalogViewer.setLabelProvider(new LabelProvider()
      {
        @Override
        public String getText(Object element)
        {
          return SetupCoreUtil.getLabel((Scope)element);
        }
      });

      List<Scope> catalogs = new ArrayList<Scope>();
      for (Scope scope : catalogSelector.getCatalogs())
      {
        for (EObject eObject : scope.eContents())
        {
          InternalEObject internalEObject = (InternalEObject)eObject;
          Resource resource = internalEObject.eDirectResource();
          if (resource != null && SetupContext.isUserScheme(resource.getURI().scheme()))
          {
            catalogs.add(scope);
            break;
          }
        }
      }

      catalogViewer.setInput(catalogs);

      if (catalogs.size() == 1)
      {
        catalogViewer.setSelection(new StructuredSelection(catalogs.get(0)));
      }
      else if (projectCatalogs.size() == 1 && catalogs.containsAll(projectCatalogs))
      {
        catalogViewer.setSelection(new StructuredSelection(projectCatalogs.iterator().next()));
      }

      catalogViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          validate();
        }
      });

      Composite lowerComposite = new Composite(main, 0);
      GridLayout lowerLayout = new GridLayout();
      lowerLayout.marginHeight = 0;
      lowerLayout.marginWidth = 0;
      lowerLayout.verticalSpacing = 0;
      lowerComposite.setLayout(lowerLayout);
      lowerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
      applyDialogFont(lowerComposite);

      parent.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          validate();
        }
      });

      return super.createDialogArea(lowerComposite);
    }

    @Override
    protected void prepareBrowseFileSystemButton(Button browseFileSystemButton)
    {
      browseFileSystemButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent event)
        {
          FileDialog fileDialog = new FileDialog(getShell(), style);
          fileDialog.setFilterExtensions(new String[] { "*.setup" });
          fileDialog.open();

          String filterPath = fileDialog.getFilterPath();
          String[] fileNames = fileDialog.getFileNames();
          StringBuffer uris = new StringBuffer();

          for (int i = 0, len = fileNames.length; i < len; i++)
          {
            uris.append(URI.createFileURI(filterPath + File.separator + fileNames[i]).toString());
            uris.append("  ");
          }

          uriField.setText((uriField.getText() + "  " + uris.toString()).trim());
        }
      });
    }

    @Override
    protected void prepareBrowseWorkspaceButton(Button browseWorkspaceButton)
    {
      browseWorkspaceButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent event)
        {
          StringBuffer uris = new StringBuffer();

          IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(), null, null, true, getContextSelection(),
              Collections.<ViewerFilter> singletonList(new ViewerFilter()
              {
                @Override
                public boolean select(Viewer viewer, Object parentElement, Object element)
                {
                  if (element instanceof IFile)
                  {
                    IFile file = (IFile)element;
                    return "setup".equals(file.getFileExtension());
                  }

                  return true;
                }
              }));

          for (int i = 0, len = files.length; i < len; i++)
          {
            uris.append(URI.createURI(files[i].getLocationURI().toString(), true));
            uris.append("  ");
          }

          uriField.setText((uriField.getText() + "  " + uris.toString()).trim());
        }

        private String getContextPath()
        {
          return context != null && context.isPlatformResource() ? URI.createURI(".").resolve(context).path().substring(9) : null;
        }

        private Object[] getContextSelection()
        {
          String path = getContextPath();
          if (path != null)
          {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource resource = root.findMember(path);
            if (resource != null && resource.isAccessible())
            {
              return new Object[] { resource };
            }
          }
          return null;
        }
      });
    }

    protected void validate()
    {
      Button button = getButton(IDialogConstants.OK_ID);
      if (button != null)
      {
        button.setEnabled(getSelectedCatalog() != null);
      }
    }

    @Override
    protected boolean processResources()
    {
      List<Project> validProjects = new ArrayList<Project>();
      List<Project> invalidProjects = new ArrayList<Project>();
      List<URI> invalidURIs = new ArrayList<URI>();
      ResourceSet resourceSet = editingDomain.getResourceSet();
      for (URI uri : getURIs())
      {
        BaseResource resource = BaseUtil.loadResourceSafely(resourceSet, uri);
        Project project = (Project)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.PROJECT);
        if (project == null)
        {
          invalidURIs.add(uri);
        }
        else if (project.eContainer() != null)
        {
          invalidProjects.add(project);
        }
        else
        {
          validProjects.add(project);
        }
      }

      if (!validProjects.isEmpty())
      {
        ProjectCatalog selectedCatalog = getSelectedCatalog();
        if (!catalogSelector.getSelectedCatalogs().contains(selectedCatalog))
        {
          catalogSelector.select(selectedCatalog, true);
        }

        Command command = DragAndDropCommand.create(editingDomain, selectedCatalog, 0.5F, DragAndDropFeedback.DROP_LINK, DragAndDropFeedback.DROP_LINK,
            validProjects);
        editingDomain.getCommandStack().execute(command);
        return true;
      }

      StringBuilder message = new StringBuilder();

      int invalidURIsSize = invalidURIs.size();
      if (invalidURIsSize != 0)
      {
        if (invalidURIsSize == 1)
        {
          message.append("The URI ");
        }
        else
        {
          message.append("The URIs ");
        }

        for (int i = 0; i < invalidURIsSize; ++i)
        {
          if (i != 0)
          {
            message.append(", ");

            if (i + 1 == invalidURIsSize)
            {
              message.append(" and ");
            }
          }

          message.append('\'');
          message.append(invalidURIs.get(i));
          message.append('\'');
        }

        if (invalidURIsSize == 1)
        {
          message.append(" does not contain a valid project.");
        }
        else
        {
          message.append(" do not contain valid projects.");
        }
      }

      int invalidProjectsSize = invalidProjects.size();
      if (invalidProjectsSize != 0)
      {
        if (message.length() != 0)
        {
          message.append("\n\n");
        }

        if (invalidProjectsSize == 1)
        {
          message.append("The project ");
        }
        else
        {
          message.append("The projects ");
        }

        for (int i = 0; i < invalidProjectsSize; ++i)
        {
          if (i != 0)
          {
            message.append(", ");

            if (i + 1 == invalidProjectsSize)
            {
              message.append(" and ");
            }
          }

          message.append('\'');
          message.append(invalidProjects.get(i).getLabel());
          message.append('\'');
        }

        if (invalidProjectsSize == 1)
        {
          message.append(" is already contained in the index.");
        }
        else
        {
          message.append(" are already contained in the index.");
        }
      }

      if (message.length() == 0)
      {
        message.append("No URIs were specified. Hit Cancel to terminate the dialog.");
      }

      ErrorDialog.openError(getShell(), "Error Adding Projects", null, new Status(IStatus.ERROR, SetupUIPlugin.INSTANCE.getSymbolicName(), message.toString()));
      return false;
    }

    private ProjectCatalog getSelectedCatalog()
    {
      IStructuredSelection selection = (IStructuredSelection)catalogViewer.getSelection();
      ProjectCatalog selectedCatalog = (ProjectCatalog)selection.getFirstElement();
      return selectedCatalog;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class URIDropAdapter extends EditingDomainViewerDropAdapter
  {
    public URIDropAdapter(EditingDomain domain, Viewer viewer)
    {
      super(domain, viewer);
    }

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
        return convertPlatformResourceURIs(Collections.singleton(URI.createURI((String)object)));
      }

      return convertPlatformResourceURIs(super.extractDragSource(object));
    }

    protected Collection<?> convertPlatformResourceURIs(Collection<?> uris)
    {
      List<Object> result = new ArrayList<Object>(uris);
      for (ListIterator<Object> it = result.listIterator(); it.hasNext();)
      {
        Object object = it.next();
        if (object instanceof URI)
        {
          URI uri = (URI)object;
          if (uri.isPlatformResource())
          {
            if (CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE)
            {
              it.set(ResourceHelper.convert(uri));
            }
          }
        }
      }

      return result;
    }

    private static class ResourceHelper
    {
      public static URI convert(URI uri)
      {
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true)));
        if (file.isAccessible())
        {
          IPath location = file.getLocation();
          if (location != null)
          {
            return URI.createFileURI(location.toOSString());
          }
        }

        return uri;
      }
    }
  }

  public static class ConfigurationListener extends ShellAdapter
  {
    private final SetupTransferSupport transferSupport;

    private final CatalogManager catalogManager;

    private final ToolBar toolBar;

    private ToolItem applyConfigurationButton;

    public ConfigurationListener(SetupTransferSupport transferSupport, CatalogManager catalogManager, ToolBar toolBar)
    {
      this.transferSupport = transferSupport;
      this.catalogManager = catalogManager;
      this.toolBar = toolBar;
    }

    @Override
    public void shellActivated(ShellEvent e)
    {
      checkConfigurationAvailability();
    }

    public void checkConfigurationAvailability()
    {
      Collection<? extends Resource> resources = transferSupport.getResources();
      filter(resources);

      URI indexLocation = getIndexURI(resources);
      if (!resources.isEmpty() && (indexLocation == null || !catalogManager.isCurrentIndex(indexLocation)))
      {
        class ConfigurationSelectionAdapter extends SelectionAdapter
        {
          final Collection<Resource> resources = new ArrayList<Resource>();

          @Override
          public void widgetSelected(SelectionEvent e)
          {
            transferSupport.resourcesDropped(resources);
            disposeApplyConfigurationButton();
          }

          public void updateResources(Collection<? extends Resource> resources)
          {
            this.resources.clear();
            this.resources.addAll(resources);
          }
        }

        if (applyConfigurationButton == null)
        {
          applyConfigurationButton = new ToolItem(toolBar, SWT.NONE, 0);
          applyConfigurationButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("full/obj16/Configuration"));

          ConfigurationSelectionAdapter selectionListener = new ConfigurationSelectionAdapter();
          selectionListener.updateResources(resources);
          applyConfigurationButton.addSelectionListener(selectionListener);
          applyConfigurationButton.setData("ConfigurationSelectionAdapter", selectionListener);
          AccessUtil.setKey(applyConfigurationButton, "applyConfiguration");

          toolBar.getParent().layout(true);
          toolBar.layout(true);
        }
        else
        {
          ConfigurationSelectionAdapter selectionListener = (ConfigurationSelectionAdapter)applyConfigurationButton.getData("ConfigurationSelectionAdapter");
          selectionListener.updateResources(resources);
        }

        if (indexLocation != null)
        {
          applyConfigurationButton.setToolTipText("Switch to the catalog index from the clipboard: " + IndexManager.getUnderlyingLocation(indexLocation));
        }
        else
        {
          applyConfigurationButton.setToolTipText("Apply the configuration from the clipboard");
        }
      }
      else if (applyConfigurationButton != null)
      {
        disposeApplyConfigurationButton();
      }
    }

    protected void filter(Collection<? extends Resource> resources)
    {
    }

    private void disposeApplyConfigurationButton()
    {
      applyConfigurationButton.dispose();
      applyConfigurationButton = null;

      toolBar.getParent().layout(true);
      toolBar.layout(true);
    }

    public static boolean isIndexURI(URI uri)
    {
      return uri != null && (uri.isArchive() || SetupContext.INDEX_SETUP_NAME.equals(uri.lastSegment()) || "zip".equals(uri.fileExtension()));
    }

    public static URI getIndexURI(Collection<? extends Resource> resources)
    {
      for (Resource resource : resources)
      {
        URI uri = resource.getURI();
        if (isIndexURI(uri))
        {
          return uri;
        }
      }

      return null;
    }
  }
}

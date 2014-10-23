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

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
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
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
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
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
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
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.progress.WorkbenchJob;

import java.io.File;
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
    setDescription("Double click the projects you want to provision, and for each choose its Stream in the table column.");
  }

  // @Override
  // public String getHelpPath()
  // {
  // return "/" + SetupUIPlugin.INSTANCE.getSymbolicName() + "/help/installer/ProjectPage.html";
  // }

  @Override
  protected Control createUI(final Composite parent)
  {
    CatalogManager catalogManager = getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, false);

    adapterFactory = new ComposedAdapterFactory(getAdapterFactory());
    adapterFactory.insertAdapterFactory(new ItemProviderAdapterFactory(catalogSelector.getSelection()));
    BaseEditUtil.replaceReflectiveItemProvider(adapterFactory);

    final Workspace workspace = getWorkspace();
    if (workspace != null)
    {
      for (Stream stream : workspace.getStreams())
      {
        existingStreams.add(EcoreUtil.getURI(stream));
      }
    }

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

    final ToolItem addProjectButton = new ToolItem(filterToolBar, SWT.NONE);
    addProjectButton.setToolTipText("Add a project to the user project of the selected catalog");
    addProjectButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add_project"));
    addProjectButton.setEnabled(false);
    AccessUtil.setKey(addProjectButton, "addProject");

    final List<ProjectCatalog> projectCatalogs = new ArrayList<ProjectCatalog>();
    addProjectButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        ResourceDialog resourceDialog = new ResourceDialog(getShell(), "Locate Project", SWT.OPEN | SWT.MULTI)
        {
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
              Command command = DragAndDropCommand.create(editingDomain, projectCatalogs.get(0), 0.5F, DragAndDropFeedback.DROP_LINK,
                  DragAndDropFeedback.DROP_LINK, validProjects);
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

            ErrorDialog.openError(getShell(), "Error Adding Projects", null,
                new Status(IStatus.ERROR, SetupUIPlugin.INSTANCE.getSymbolicName(), message.toString()));
            return false;
          }
        };

        if (resourceDialog.open() == Window.OK)
        {
        }
      }
    });

    final ToolItem removeProjectButton = new ToolItem(filterToolBar, SWT.NONE);
    removeProjectButton.setToolTipText("Remove the select project from the user project");
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

    final ToolItem refreshButton = new ToolItem(filterToolBar, SWT.NONE);
    refreshButton.setToolTipText("Refresh");
    refreshButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("refresh"));
    AccessUtil.setKey(refreshButton, "refresh");

    final ToolItem catalogsButton = new ToolItem(filterToolBar, SWT.DROP_DOWN);
    catalogsButton.setToolTipText("Select Catalogs");
    catalogsButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("catalogs"));
    catalogSelector.configure(catalogsButton);
    AccessUtil.setKey(catalogsButton, "catalogs");

    final Object projectFilterJobFamily = new Object();

    FilteredTree filteredTree = new FilteredTree(upperComposite, SWT.BORDER | SWT.MULTI, new PatternFilter(), true)
    {
      @Override
      protected WorkbenchJob doCreateRefreshJob()
      {
        return new WorkbenchJob("Refresh Filter")
        {
          @Override
          public IStatus runInUIThread(IProgressMonitor monitor)
          {
            if (treeViewer.getControl().isDisposed())
            {
              return Status.CANCEL_STATUS;
            }

            String text = getFilterString();
            if (StringUtil.isEmpty(text))
            {
              return Status.OK_STATUS;
            }

            boolean initial = initialText != null && initialText.equals(text);
            if (initial)
            {
              getPatternFilter().setPattern(null);
            }
            else
            {
              getPatternFilter().setPattern(text);
            }

            Control redrawFalseControl = treeComposite != null ? treeComposite : treeViewer.getControl();
            try
            {
              redrawFalseControl.setRedraw(false);
              TreeItem[] is = treeViewer.getTree().getItems();
              for (int i = 0; i < is.length; i++)
              {
                TreeItem item = is[i];
                if (item.getExpanded())
                {
                  treeViewer.setExpandedState(item.getData(), false);
                }
              }

              treeViewer.refresh(true);

              if (text.length() > 0 && !initial)
              {
                TreeItem[] items = getViewer().getTree().getItems();
                int treeHeight = getViewer().getTree().getBounds().height;
                int numVisibleItems = treeHeight / getViewer().getTree().getItemHeight();
                long stopTime = 200 + System.currentTimeMillis();
                boolean cancel = false;
                if (items.length > 0 && recursiveExpand(items, monitor, stopTime, new int[] { numVisibleItems }))
                {
                  cancel = true;
                }

                updateToolbar(true);

                if (cancel)
                {
                  return Status.CANCEL_STATUS;
                }
              }
              else
              {
                updateToolbar(false);
              }
            }
            finally
            {
              TreeItem[] items = getViewer().getTree().getItems();
              if (items.length > 0 && getViewer().getTree().getSelectionCount() == 0)
              {
                treeViewer.getTree().setTopItem(items[0]);
              }
              redrawFalseControl.setRedraw(true);
            }
            return Status.OK_STATUS;
          }

          private boolean recursiveExpand(TreeItem[] items, IProgressMonitor monitor, long cancelTime, int[] numItemsLeft)
          {
            boolean canceled = false;
            for (int i = 0; !canceled && i < items.length; i++)
            {
              TreeItem item = items[i];
              boolean visible = numItemsLeft[0]-- >= 0;
              if (monitor.isCanceled() || !visible && System.currentTimeMillis() > cancelTime)
              {
                canceled = true;
              }
              else
              {
                Object itemData = item.getData();
                if (itemData != null)
                {
                  if (!item.getExpanded())
                  {
                    treeViewer.setExpandedState(itemData, true);
                  }

                  TreeItem[] children = item.getItems();
                  if (items.length > 0)
                  {
                    canceled = recursiveExpand(children, monitor, cancelTime, numItemsLeft);
                  }
                }
              }
            }
            return canceled;
          }

          @Override
          public Display getDisplay()
          {
            return UIUtil.getDisplay();
          }

          @Override
          public boolean shouldSchedule()
          {
            return true;
          }

          @Override
          public boolean shouldRun()
          {
            return true;
          }

          @Override
          public boolean belongsTo(Object family)
          {
            return family == projectFilterJobFamily;
          }
        };
      }
    };
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);
    AccessUtil.setKey(filteredTree.getFilterControl(), "filter");

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

        if (notification.getFeature() == SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS)
        {
          getShell().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                Job.getJobManager().join(projectFilterJobFamily, new NullProgressMonitor());
              }
              catch (OperationCanceledException ex)
              {
                // Ignore.
              }
              catch (InterruptedException ex)
              {
                // Ignore.
              }

              if (projectViewer.getExpandedElements().length == 0)
              {
                final Object[] elements = getElements(projectViewer.getInput());
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

    final ToolItem addButton = new ToolItem(bucketToolBar, SWT.PUSH);
    addButton.setToolTipText("Add Project (or double-click in upper tree)");
    addButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add"));
    addButton.setEnabled(false);
    AccessUtil.setKey(addButton, "choose");

    final ToolItem removeButton = new ToolItem(bucketToolBar, SWT.PUSH);
    removeButton.setToolTipText("Remove Project (or double-click in lower table)");
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

    if (workspace != null)
    {
      for (Iterator<Stream> it = workspace.getStreams().iterator(); it.hasNext();)
      {
        Stream stream = it.next();
        if (stream.eIsProxy())
        {
          it.remove();
        }
      }
    }

    streamViewer.setInput(workspace);

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

        addProjectButton.setEnabled(!projectCatalogs.isEmpty());
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
          Resource resource = parentProject.eResource();
          if (resource != null && SetupContext.USER_SCHEME.equals(resource.getURI().scheme()))
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
            addButton.setEnabled(false);
            return;
          }
        }

        boolean expanded = projectViewer.getExpandedState(element);
        projectViewer.setExpandedState(element, !expanded);
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
            streamViewer.setSelection(new StructuredSelection(selectedProjectStreams));
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
        removeButton.setEnabled(!event.getSelection().isEmpty());
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
        }
      }
    });

    return sashForm;
  }

  @Override
  protected void createCheckButtons()
  {
    if (existingStreams.isEmpty() && getPreviousPage() instanceof ProductPage)
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
            getWizard().setSetupContext(SetupContext.create(getInstallation(), (Workspace)null, getUser()));
            setPageComplete(true);
          }
          else
          {
            setPageComplete(workspace != null && workspace.getStreams().size() != existingStreams.size());
          }
        }
      });
      AccessUtil.setKey(skipButton, "skip");

      setPageComplete(skipButton.getSelection());
    }
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
      if (skipButton != null)
      {
        skipButton.setSelection(false);
      }
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
        protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
            Collection<?> collection)
        {
          ProjectCatalog projectCatalog = (ProjectCatalog)owner;
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
          if (directResource != null && "user".equals(directResource.getURI().scheme()))
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

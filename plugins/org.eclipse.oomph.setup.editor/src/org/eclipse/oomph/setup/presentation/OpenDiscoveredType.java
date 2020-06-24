/*
 * Copyright (c) 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.provider.P2EditPlugin;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.DockableDialog.Factory;
import org.eclipse.oomph.ui.FilteredTreeWithoutWorkbench;
import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IStyledLabelDecorator;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.dialogs.PatternFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

/**
 * @author Ed Merks
 */
public class OpenDiscoveredType extends OomphDialog
{
  public static final String TITLE = Messages.OpenDiscoveredType_title;

  public static final String MESSAGE = Messages.OpenDiscoveredType_message;

  private static final String GIT_INDICES = PropertiesUtil.getProperty("oomph.git.index", "http://download.eclipse.org/oomph/git/git-index.zip"); //$NON-NLS-1$ //$NON-NLS-2$

  private final DockableDialog.Dockable dockable = new DockableDialog.Dockable(this);

  private final IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();

  private Item selectedItem;

  private String selectedLink;

  private ExtendedFilteredTree filteredTree;

  private TreeViewer masterViewer;

  private TreeViewer detailsViewer;

  private Job detailsLoadJob;

  private Map<String, URI> repositoryProjectSetups;

  final private ItemFilter filter = new ItemFilter();

  public OpenDiscoveredType(Shell parentShell)
  {
    super(parentShell, TITLE, 700, 500, SetupEditorPlugin.INSTANCE, true);

    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MAX | (OS.INSTANCE.isWin() ? SWT.MIN : SWT.NONE));
    setBlockOnOpen(false);
  }

  @Override
  public String getHelpPath()
  {
    return SetupEditorPlugin.INSTANCE.getSymbolicName() + "/html/OpenInTypeBrowserHelp.html"; //$NON-NLS-1$
  }

  public DockableDialog.Dockable getDockable()
  {
    return dockable;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return MESSAGE + "."; //$NON-NLS-1$
  }

  @Override
  protected String getImagePath()
  {
    return "full/wizban/BrowseType"; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  private void setSelectedLink(String selectedLink)
  {
    Button button = getButton(IDialogConstants.OK_ID);
    button.setEnabled(selectedLink != null);
    this.selectedLink = selectedLink;
  }

  private void setSelectedItem(Item item)
  {
    selectedItem = item;
    setSelectedLink(item != null && item.getName() != null && item.getName().startsWith("http") ? item.getName() : null); //$NON-NLS-1$
  }

  @Override
  protected void createUI(Composite composite)
  {
    getShell().setImage(SetupEditorPlugin.INSTANCE.getSWTImage("BrowseType")); //$NON-NLS-1$

    SashForm sashForm = new SashForm(composite, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayout(new GridLayout());
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite treeComposite = new Composite(sashForm, SWT.NONE);
    treeComposite.setLayout(UIUtil.createGridLayout(1));

    Composite filterComposite = new Composite(treeComposite, SWT.NONE);
    filterComposite.setLayout(UIUtil.createGridLayout(2));
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(UIUtil.createGridLayout(1));
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    ToolBar filterToolBar = new ToolBar(filterComposite, SWT.FLAT | SWT.RIGHT);

    final ToolItem collapseAllButton = new ToolItem(filterToolBar, SWT.NONE);
    collapseAllButton.setToolTipText(Messages.OpenDiscoveredType_collapseAllButton_tooltip);
    collapseAllButton.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("collapse-all")); //$NON-NLS-1$
    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        masterViewer.collapseAll();
      }
    });

    filter.setIncludeLeadingWildcard(true);

    filteredTree = new ExtendedFilteredTree(treeComposite, SWT.BORDER, filter, filter);
    filteredTree.setExpansionCount(100);

    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);

    final AdapterFactory adapterFactory = new ComposedAdapterFactory();
    masterViewer = filteredTree.getViewer();

    ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(masterViewer, new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        if (event.location != null && !event.location.startsWith("about:")) //$NON-NLS-1$
        {
          event.doit = false;
          openProjectImporter(URI.createURI(event.location));
        }
      }
    });
    masterViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, masterViewer, toolTipSupport)));
    masterViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    masterViewer.setUseHashlookup(true);

    final Tree masterTree = masterViewer.getTree();
    masterTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    masterViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)masterViewer.getSelection();
        Item item = (Item)selection.getFirstElement();
        boolean expanded = masterViewer.getExpandedState(item);
        masterViewer.setExpandedState(item, !expanded);

        if (item.getType() == Item.Type.PACKAGE)
        {
          LOOP: while (item != null)
          {
            for (Item child : item.getItems())
            {
              if (filter.isElementVisible(masterViewer, child))
              {
                item = child;
                if (child.getType() == Item.Type.CLASS)
                {
                  break LOOP;
                }

                masterViewer.setExpandedState(child, true);
                continue LOOP;
              }
            }

            break;
          }

          masterViewer.setSelection(new StructuredSelection(item), true);
        }
      }
    });

    masterViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        loadDetails(item);
        setSelectedItem(null);
      }
    });

    Composite detailsComposite = new Composite(sashForm, SWT.NONE);
    detailsComposite.setLayout(new FillLayout());
    detailsComposite.setForeground(masterTree.getForeground());
    detailsComposite.setBackground(masterTree.getBackground());
    detailsViewer = new TreeViewer(detailsComposite);
    detailsViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, detailsViewer, null)));
    detailsViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

    detailsViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        setSelectedItem(item);
      }
    });

    detailsViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        if (selectedLink != null)
        {
          openURL(getOpenLinkStyle());
        }
      }
    });

    List<? extends OomphTransferDelegate> dndDelegates = Arrays
        .asList(new OomphTransferDelegate[] { new OomphTransferDelegate.URLTransferDelegate(), new OomphTransferDelegate.TextTransferDelegate() });
    Transfer[] dndTransfers = new Transfer[] { dndDelegates.get(0).getTransfer(), dndDelegates.get(1).getTransfer() };
    detailsViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, dndTransfers,
        new GeneralDragAdapter(detailsViewer, new GeneralDragAdapter.DraggedObjectsFactory()
        {
          public List<Object> createDraggedObjects(ISelection selection) throws Exception
          {
            List<Object> result = new ArrayList<Object>();
            for (Object object : ((IStructuredSelection)selection).toArray())
            {
              if (object instanceof Item)
              {
                Item item = (Item)object;
                String name = item.getName();
                if (name != null && name.startsWith("http")) //$NON-NLS-1$
                {
                  result.add(name);
                }
              }
            }

            return result;
          }
        }, dndDelegates));

    Menu menu = detailsViewer.getControl().getMenu();
    MenuManager contextMenu = (MenuManager)menu.getData(MenuManager.MANAGER_KEY);
    contextMenu.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        manager.add(new Separator());

        if (selectedLink != null)
        {
          final boolean internalWebBrowserAvailable = browserSupport.isInternalWebBrowserAvailable();
          final OpenLinkStyle openLinkStyle = getOpenLinkStyle();
          manager.add(new Action(Messages.OpenDiscoveredType_action_openInExtBrowser)
          {
            {
              if (internalWebBrowserAvailable)
              {
                setChecked(openLinkStyle == OpenLinkStyle.EXTERNAL);
              }
            }

            @Override
            public void run()
            {
              openURL(OpenLinkStyle.EXTERNAL);
            }
          });

          if (internalWebBrowserAvailable)
          {
            manager.add(new Action(Messages.OpenDiscoveredType_action_openInEditor)
            {
              {
                setChecked(openLinkStyle == OpenLinkStyle.EDITOR);
              }

              @Override
              public void run()
              {
                openURL(OpenLinkStyle.EDITOR);
              }
            });

            manager.add(new Action(Messages.OpenDiscoveredType_action_openInView)
            {
              {
                setChecked(openLinkStyle == OpenLinkStyle.VIEW);
              }

              @Override
              public void run()
              {
                openURL(OpenLinkStyle.VIEW);
              }
            });
          }

          if (selectedItem.rawLinks != null && selectedItem.rawLinks.get(selectedLink) != null)
          {
            manager.add(new Action(Messages.OpenDiscoveredType_action_openInJavaEditor)
            {
              {
                setChecked(openLinkStyle == OpenLinkStyle.JAVA);
              }

              @Override
              public void run()
              {
                openURL(OpenLinkStyle.JAVA);
              }
            });
          }

          if (repositoryProjectSetups != null)
          {
            manager.add(new Separator());
            for (final URI projectSetupURI : getProjectSetups(selectedItem))
            {
              manager.add(new Action(NLS.bind(Messages.OpenDiscoveredType_action_openInImportProjectsWizard, projectSetupURI.lastSegment()))
              {
                @Override
                public void run()
                {
                  openProjectImporter(projectSetupURI);
                }
              });
            }
          }
        }
      }
    });

    sashForm.setWeights(new int[] { 14, 5 });

    UIUtil.asyncExec(composite, new Runnable()
    {
      public void run()
      {
        setSelectedItem(null);
      }
    });

    loadModel();
  }

  private void openProjectImporter(URI projectSetupURI)
  {
    SetupWizard.Importer wizard = new SetupWizard.Importer();
    wizard.setProject(projectSetupURI);
    wizard.openDialog(getParentShell());
  }

  private List<URI> getProjectSetups(Item item)
  {
    List<URI> result = new ArrayList<URI>();
    if (repositoryProjectSetups != null)
    {
      for (String repo : item.getRepos())
      {
        URI uri = repositoryProjectSetups.get(repo);
        if (uri == null && repo.endsWith(".git")) //$NON-NLS-1$
        {
          uri = repositoryProjectSetups.get(repo.substring(0, repo.length() - 4));
        }

        if (uri != null && !result.contains(uri))
        {
          result.add(uri);
        }
      }
    }

    return result;
  }

  private void decorate(Item item)
  {
    EList<Item> items = item.getItems();
    Set<String> decorations = new HashSet<String>();
    List<Item> visibleChildren = new ArrayList<Item>();
    for (Item child : items)
    {
      if (filter.isElementVisible(masterViewer, child))
      {
        visibleChildren.add(child);
        decorate(child);
        decorations.add(child.getDecoration());
      }
    }

    if (visibleChildren.isEmpty())
    {
      Set<String> labels = new LinkedHashSet<String>();
      for (URI projectSetupURI : getProjectSetups(item))
      {
        labels.add(projectSetupURI.lastSegment());
      }

      if (!labels.isEmpty())
      {
        StringBuilder decoration = new StringBuilder();
        for (String label : labels)
        {
          if (decoration.length() != 0)
          {
            decoration.append(' ');
          }

          decoration.append(label);
        }
        item.setDecoration(decoration.toString());
      }
      else
      {
        item.setDecoration(null);
      }

    }
    else if (decorations.size() == 1 && !decorations.contains(null))
    {
      if (item.getType() != Item.Type.ROOT)
      {
        item.setDecoration(decorations.iterator().next());
        for (Item child : visibleChildren)
        {
          child.setDecoration(null);
        }
      }
    }
    else
    {
      item.setDecoration(null);
    }
  }

  protected void loadModel()
  {
    Item root = Item.createRootItem();
    root.getChildren().add(Item.createPlaceholderItem());
    masterViewer.setInput(root);

    Job job = new Job(Messages.OpenDiscoveredType_gitIndexLoaderJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        final Item root = Item.createRootItem();
        Item packageItem = null;
        SegmentSequence qualifiedName = null;

        ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
        for (String gitIndex : StringUtil.explode(GIT_INDICES, " ", (char)0)) //$NON-NLS-1$
        {
          ZipInputStream zipInputStream = null;
          try
          {
            String link = null;
            String rawLink = null;
            String repo = null;
            String repoLink = null;
            String rawRepoLink = null;
            String sourceFolder = null;
            String sourceFolderLink = null;
            String rawSourceFolderLink = null;
            String packageName = null;
            String className = null;

            zipInputStream = new ZipInputStream(resourceSet.getURIConverter().createInputStream(URI.createURI(gitIndex)));
            if (zipInputStream.getNextEntry() != null)
            {
              List<String> lines = IOUtil.readLines(zipInputStream, "UTF-8"); //$NON-NLS-1$
              for (String line : lines)
              {
                if (line.startsWith("    ")) //$NON-NLS-1$
                {
                  className = line.trim();
                  Item classItem = packageItem.getItem(className);
                  if (classItem == null)
                  {
                    classItem = Item.create(Item.Type.CLASS, qualifiedName.append(className).toString());
                    classItem.itemFilter = filter;
                    packageItem.getChildren().add(classItem);
                  }

                  classItem.addLink(rawSourceFolderLink, sourceFolderLink, repo, repoLink);
                }
                else if (line.startsWith("   ")) //$NON-NLS-1$
                {
                  packageName = line.trim();
                  SegmentSequence segments = SegmentSequence.create(".", packageName); //$NON-NLS-1$
                  qualifiedName = SegmentSequence.create("."); //$NON-NLS-1$
                  Item item = root;
                  for (String segment : segments.segments())
                  {
                    qualifiedName = qualifiedName.append(segment);
                    packageItem = item.getItem(segment);
                    if (packageItem == null)
                    {
                      packageItem = Item.create(Item.Type.PACKAGE, qualifiedName.toString());
                      packageItem.itemFilter = filter;
                      item.getChildren().add(packageItem);
                    }

                    packageItem.addLink(sourceFolderLink, repo, repoLink);
                    item = packageItem;
                  }
                }
                else if (line.startsWith("  ")) //$NON-NLS-1$
                {
                  sourceFolder = line.trim();
                  sourceFolderLink = repoLink.replace("${1}", sourceFolder + "/${1}"); //$NON-NLS-1$ //$NON-NLS-2$
                  if (rawRepoLink == null)
                  {
                    rawSourceFolderLink = null;
                  }
                  else
                  {
                    rawSourceFolderLink = rawRepoLink.replace("${1}", sourceFolder + "/${1}"); //$NON-NLS-1$ //$NON-NLS-2$
                  }
                }
                else if (line.startsWith(" ")) //$NON-NLS-1$
                {
                  repo = line.trim();
                  repoLink = link.replace("${0}", repo); //$NON-NLS-1$
                  if (rawLink == null)
                  {
                    rawRepoLink = null;
                  }
                  else
                  {
                    rawRepoLink = rawLink.replace("${0}", repo); //$NON-NLS-1$
                  }
                }
                else
                {
                  List<String> links = StringUtil.explode(line, " ", (char)0); //$NON-NLS-1$
                  link = links.get(0);
                  if (links.size() > 1)
                  {
                    rawLink = links.get(1);
                  }
                  else
                  {
                    rawLink = null;
                  }
                }
              }
            }
          }
          catch (Exception ex)
          {
            IOUtil.close(zipInputStream);
          }
        }

        root.sort();

        UIUtil.asyncExec(masterViewer.getControl(), new Runnable()
        {
          public void run()
          {
            masterViewer.setInput(root);
            filteredTree.textChanged();
          }
        });

        Map<String, URI> remoteURIs = new HashMap<String, URI>();
        Resource resource = resourceSet.getResource(SetupContext.INDEX_SETUP_URI, true);
        for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
        {
          EObject eObject = it.next();
          EClass eClass = eObject.eClass();
          if ("GitCloneTask".equals(eClass.getName())) //$NON-NLS-1$
          {
            EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature("remoteURI"); //$NON-NLS-1$
            if (eStructuralFeature != null)
            {
              String remoteURI = (String)eObject.eGet(eStructuralFeature);
              if (remoteURI != null)
              {
                Scope scope = ((SetupTask)eObject).getScope();
                if (scope instanceof Stream)
                {
                  scope = scope.getParentScope();
                }

                if (scope instanceof Project)
                {
                  URI uri = EcoreUtil.getURI(scope);
                  URI oldURI = remoteURIs.put(remoteURI, uri);
                  if (oldURI != null)
                  {
                    if (oldURI.toString().contains(remoteURI))
                    {
                      remoteURIs.put(remoteURI, oldURI);
                    }
                    else
                    {
                      int index = remoteURI.indexOf("/"); //$NON-NLS-1$
                      if (index != -1)
                      {
                        if (oldURI.toString().toLowerCase().contains(remoteURI.substring(0, index).toLowerCase()))
                        {
                          remoteURIs.put(remoteURI, oldURI);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }

        repositoryProjectSetups = remoteURIs;

        if (!masterViewer.getControl().isDisposed())
        {
          try
          {
            decorate(root);
          }
          catch (RuntimeException ex)
          {
            // Ignore.
          }
        }

        UIUtil.asyncExec(masterViewer.getControl(), new Runnable()
        {
          public void run()
          {
            masterViewer.refresh();
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();
  }

  protected void loadDetails(final Item item)
  {
    if (detailsLoadJob != null)
    {
      detailsLoadJob.cancel();
    }

    if (item == null)
    {
      detailsViewer.setInput(null);
    }
    else
    {
      Item root = Item.createRootItem();
      root.getChildren().add(Item.createPlaceholderItem());
      detailsViewer.setInput(root);
    }

    detailsLoadJob = new Job(Messages.OpenDiscoveredType_detailLoaderJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        if (item != null)
        {
          final Item input = Item.createRootItem();
          Item javaItem = Item.create(item.getType(), item.getName());
          input.getChildren().add(javaItem);

          Map<String, Set<String>> unfilteredGroups = new HashMap<String, Set<String>>();
          collectUnfilteredGroups(unfilteredGroups, item);

          EList<Object> children = javaItem.getChildren();

          Item.Type type = item.getType();
          Item.Type linkType = type == Item.Type.CLASS ? Item.Type.CLASS_LINK : Item.Type.PACKAGE_LINK;
          int count = 0;
          Map<String, String> rawLinks = type == Item.Type.CLASS ? new HashMap<String, String>() : null;

          Map<String, Map<String, Set<String>>> groupLinks = item.getGroupLinks(unfilteredGroups, rawLinks);
          for (Entry<String, Map<String, Set<String>>> groupEntry : groupLinks.entrySet())
          {
            String repoLink = groupEntry.getKey();
            Item groupLinkItem = Item.create(Item.Type.FOLDER_LINK, repoLink);
            ++count;
            children.add(groupLinkItem);

            EList<Item> groupChildren = groupLinkItem.getItems();

            Map<String, Set<String>> links = groupEntry.getValue();
            for (Map.Entry<String, Set<String>> entry : links.entrySet())
            {
              String link = entry.getKey();
              Item linkItem = Item.create(linkType, link);
              linkItem.setText(link.replace(repoLink, "")); //$NON-NLS-1$
              ++count;
              groupChildren.add(linkItem);
              for (String repo : entry.getValue())
              {
                linkItem.addLink(repo, repo, ""); //$NON-NLS-1$
                groupLinkItem.addLink(repo, repo, ""); //$NON-NLS-1$
                linkItem.rawLinks = rawLinks;
              }
            }

            decorate(groupLinkItem);

            if (monitor.isCanceled())
            {
              break;
            }
          }

          if (!monitor.isCanceled())
          {
            input.sort();

            final int limit = count;
            UIUtil.asyncExec(detailsViewer.getControl(), new Runnable()
            {
              public void run()
              {
                detailsViewer.setInput(input);
                detailsViewer.expandToLevel(limit > 1000 ? 2 : 3);
              }
            });
          }
        }

        if (!masterViewer.getControl().isDisposed())
        {
          try
          {
            Item root = (Item)masterViewer.getInput();
            decorate(root);
          }
          catch (RuntimeException ex)
          {
            // Ignore.
          }
        }

        UIUtil.asyncExec(masterViewer.getControl(), new Runnable()
        {
          public void run()
          {
            masterViewer.refresh(true);
          }
        });

        return Status.OK_STATUS;
      }

      public void collectUnfilteredGroups(Map<String, Set<String>> groups, Item item)
      {
        boolean hasVisibleChildren = false;
        for (Item child : item.getItems())
        {
          if (filter.select(detailsViewer, item, child))
          {
            hasVisibleChildren = true;
            collectUnfilteredGroups(groups, child);
          }
        }

        if (!hasVisibleChildren)
        {
          for (Map.Entry<String, Map<String, Set<String>>> entry : item.groupLinks.entrySet())
          {
            CollectionUtil.addAll(groups, entry.getKey(), entry.getValue().keySet());
          }

        }
      }

    };

    detailsLoadJob.setSystem(true);
    detailsLoadJob.schedule();
  }

  @Override
  protected void okPressed()
  {
    super.okPressed();
    openURL(getOpenLinkStyle());
  }

  private void setOpenLinkStyle(OpenLinkStyle style)
  {
    Preference instancePreference = SetupEditorPlugin.INSTANCE.getInstancePreference(selectedItem.getType() + ".open.style"); //$NON-NLS-1$
    instancePreference.set(style.name());
  }

  private OpenLinkStyle getOpenLinkStyle()
  {
    Preference instancePreference = SetupEditorPlugin.INSTANCE.getInstancePreference(selectedItem.getType() + ".open.style"); //$NON-NLS-1$
    String name = instancePreference.get("VIEW"); //$NON-NLS-1$
    try
    {
      return OpenLinkStyle.valueOf(OpenLinkStyle.class, name);
    }
    catch (Exception ex)
    {
      return OpenLinkStyle.VIEW;
    }
  }

  private void openURL(OpenLinkStyle openLinkStyle)
  {
    try
    {
      setOpenLinkStyle(openLinkStyle);

      String id = "org.eclipse.oomph.setup.type.browser"; //$NON-NLS-1$
      int style = 0;
      switch (openLinkStyle)
      {
        case EDITOR:
        {
          id += ".editor"; //$NON-NLS-1$
          style = IWorkbenchBrowserSupport.AS_EDITOR;
          break;
        }
        case EXTERNAL:
        {
          style = IWorkbenchBrowserSupport.AS_EXTERNAL;
          id += ".external"; //$NON-NLS-1$
          break;
        }
        case VIEW:
        {
          style = IWorkbenchBrowserSupport.AS_VIEW;
          id += ".view"; //$NON-NLS-1$
          break;
        }
        case JAVA:
        {
          final String rawLink = selectedItem.rawLinks.get(selectedLink);
          class OpenJavaEditorJob extends Job
          {
            private IOException ioException;

            private byte[] bytes;

            public OpenJavaEditorJob()
            {
              super(NLS.bind(Messages.OpenDiscoveredType_openJavaEditorJob_name, rawLink));
            }

            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              InputStream in = null;
              try
              {
                URL url = new URL(rawLink);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                in = url.openStream();
                IOUtil.copy(in, out);
                bytes = out.toByteArray();
              }
              catch (IOException ex)
              {
                ioException = ex;
              }
              finally
              {
                IOUtil.closeSilent(in);
              }

              class StorageEditorInput implements IStorageEditorInput
              {
                @SuppressWarnings("all")
                public Object getAdapter(Class adapter)
                {
                  return null;
                }

                public String getToolTipText()
                {
                  return rawLink;
                }

                public IPersistableElement getPersistable()
                {
                  return null;
                }

                public String getName()
                {
                  return URI.createURI(rawLink).lastSegment();
                }

                public ImageDescriptor getImageDescriptor()
                {
                  return null;
                }

                public boolean exists()
                {
                  return true;
                }

                public IStorage getStorage() throws CoreException
                {
                  return new IStorage()
                  {
                    @SuppressWarnings("all")
                    public Object getAdapter(Class adapter)
                    {
                      return null;
                    }

                    public boolean isReadOnly()
                    {
                      return true;
                    }

                    public String getName()
                    {
                      return URI.createURI(rawLink).lastSegment();
                    }

                    public IPath getFullPath()
                    {
                      return new Path(rawLink);
                    }

                    public InputStream getContents() throws CoreException
                    {
                      if (ioException != null)
                      {
                        SetupUIPlugin.INSTANCE.coreException(ioException);
                        return null;
                      }

                      return new ByteArrayInputStream(bytes);
                    }
                  };
                }
              }

              UIUtil.asyncExec(getShell(), new Runnable()
              {
                public void run()
                {
                  try
                  {
                    final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    for (IEditorReference editorReference : page.getEditorReferences())
                    {
                      IEditorInput editorInput = editorReference.getEditorInput();
                      if (editorInput instanceof StorageEditorInput && rawLink.equals(((StorageEditorInput)editorInput).getToolTipText()))
                      {
                        IEditorPart editor = editorReference.getEditor(true);
                        if (editor != null)
                        {
                          page.activate(editor);
                          return;
                        }
                      }
                    }

                    page.openEditor(new StorageEditorInput(), "org.eclipse.jdt.ui.CompilationUnitEditor", true, //$NON-NLS-1$
                        IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
                  }
                  catch (PartInitException ex)
                  {
                    SetupEditorPlugin.INSTANCE.log(ex);
                  }
                }
              });

              return Status.OK_STATUS;
            }
          }

          new OpenJavaEditorJob().schedule();
          return;
        }
      }

      IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
      IWebBrowser browser = support.createBrowser(style | IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, id, selectedLink,
          selectedLink);
      browser.openURL(new URL(selectedLink));

    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex);
    }
  }

  /**
   * @author Ed Merks
   */
  private final class ExtendedFilteredTree extends FilteredTreeWithoutWorkbench
  {
    private ExtendedFilteredTree(Composite parent, int style, PatternFilter patternFilter, ExpansionFilter expansionFilter)
    {
      super(parent, style, patternFilter, expansionFilter);
    }

    @Override
    protected void refreshed()
    {
      masterViewer.setSelection(masterViewer.getSelection());
    }

    @Override
    public void textChanged()
    {
      super.textChanged();
    }
  }

  /**
   * @author Ed Merks
   */
  private enum OpenLinkStyle
  {
    EXTERNAL, EDITOR, VIEW, JAVA
  }

  /**
   * @author Ed Merks
   */
  private final class ItemFilter extends PatternFilter implements FilteredTreeWithoutWorkbench.ExpansionFilter
  {
    private Pattern pattern;

    private Pattern packagePattern;

    @Override
    public void setPattern(String patternString)
    {
      if (detailsLoadJob != null)
      {
        // Wait for the details job to not be running anymore.
        detailsLoadJob.cancel();
        for (int i = 0; i < 100 && detailsLoadJob.getState() != Job.NONE; ++i)
        {
          try
          {
            Thread.sleep(100);
          }
          catch (InterruptedException ex)
          {
            // Ignore.
          }
        }
      }

      super.setPattern(patternString);

      if (patternString == null)
      {
        pattern = null;
      }
      else
      {
        StringBuilder patternLiteral = new StringBuilder();
        if (StringUtil.trimLeft(patternString).equals(patternString))
        {
          patternLiteral.append(".*?"); //$NON-NLS-1$
        }

        List<Integer> packageIndices = new ArrayList<Integer>();

        boolean previousUpperCase = false;
        boolean grouped = false;
        for (int i = Character.offsetByCodePoints(patternString, 0, 0), length = patternString.length(); i < length; i = Character
            .offsetByCodePoints(patternString, i, 1))
        {
          int codePoint = patternString.codePointAt(i);
          if (Character.isJavaIdentifierStart(codePoint) || Character.isJavaIdentifierPart(codePoint))
          {
            boolean upperCase = Character.isUpperCase(codePoint);
            boolean bothUpperCase = previousUpperCase && upperCase;
            if (bothUpperCase)
            {
              if (grouped)
              {
                patternLiteral.append(')');
              }

              int lowerCaseCodePoint = Character.toLowerCase(codePoint);
              patternLiteral.append("(?:[^\\p{Lu}&&[^").appendCodePoint(lowerCaseCodePoint).append("]]*(").appendCodePoint(codePoint).append(")|(") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                  .appendCodePoint(lowerCaseCodePoint).append("))"); //$NON-NLS-1$

              if (grouped)
              {
                patternLiteral.append('(');
              }
            }

            if (!grouped)
            {
              grouped = true;
              patternLiteral.append('(');
            }

            if (!bothUpperCase)
            {
              patternLiteral.appendCodePoint(codePoint);
            }

            previousUpperCase = upperCase;
          }
          else if (codePoint == '*')
          {
            if (i != 0)
            {
              if (grouped)
              {
                patternLiteral.append(").*?("); //$NON-NLS-1$
              }
              else
              {
                patternLiteral.append(".*?"); //$NON-NLS-1$
              }

              previousUpperCase = false;
            }
          }
          else if (codePoint == '?')
          {
            if (grouped)
            {
              patternLiteral.append(").("); //$NON-NLS-1$
            }
            else
            {
              patternLiteral.append("."); //$NON-NLS-1$
            }

            previousUpperCase = false;
          }
          else if (!Character.isWhitespace(codePoint))
          {
            if (grouped)
            {
              patternLiteral.append(')');
              packageIndices.add(patternLiteral.length());
              patternLiteral.append('(');
            }

            patternLiteral.append(Pattern.quote(new String(Character.toChars(codePoint))));

            if (!grouped)
            {
              grouped = true;
              patternLiteral.append('(');
            }

            previousUpperCase = false;
          }
          else if (i + 1 != length)
          {
            previousUpperCase = false;
          }
        }

        if (grouped)
        {
          patternLiteral.append(')');
        }

        if (previousUpperCase)
        {
          patternLiteral.append("[^\\p{Lu}]*"); //$NON-NLS-1$
        }

        if (StringUtil.trimRight(patternString).equals(patternString))
        {
          patternLiteral.append(".*"); //$NON-NLS-1$
        }
        else
        {
          patternLiteral.append('$');
        }

        pattern = Pattern.compile(patternLiteral.toString());

        if (!packageIndices.isEmpty())
        {
          int last = packageIndices.size() - 1;
          patternLiteral.delete(packageIndices.get(last), patternLiteral.length());
          for (int i = last; i > 0; --i)
          {
            patternLiteral.insert(packageIndices.get(i), "?"); //$NON-NLS-1$
          }

          packagePattern = Pattern.compile(patternLiteral.toString());
        }
        else
        {
          packagePattern = null;
        }
      }
    }

    @Override
    protected boolean wordMatches(String text)
    {
      if (pattern == null)
      {
        return true;
      }

      if (text == null)
      {
        return false;
      }

      return pattern.matcher(text).matches();
    }

    @Override
    protected boolean isLeafMatch(Viewer viewer, Object element)
    {
      // Match against the fully qualified name of the item, not just the label text.
      Item item = (Item)element;
      String text = item.getName();
      if (text == null)
      {
        text = item.getText();
      }

      return wordMatches(text);
    }

    public boolean shouldExpand(Object element)
    {
      return !isLeafMatch(null, element);
    }
  }

  /**
   * @author Ed Merks
   */
  private static class Item extends ItemProvider
  {
    /**
     * @author Ed Merks
     */
    private enum Type
    {
      PLACEHOLDER, //
      ROOT, //
      PACKAGE
      {
        @Override
        public String getText(String name)
        {
          int index = name.lastIndexOf('.');
          return index == -1 ? name : name.substring(index + 1);
        }
      },
      CLASS
      {
        @Override
        public String getText(String name)
        {
          int index = name.lastIndexOf('.');
          return index == -1 ? name : name.substring(index + 1);
        }
      },
      FOLDER_LINK, //
      PACKAGE_LINK, //
      CLASS_LINK;

      public String getText(String name)
      {
        return name;
      }
    }

    private static final org.eclipse.emf.edit.provider.StyledString.Style BOLD_STYLER = org.eclipse.emf.edit.provider.StyledString.Style.newBuilder()
        .setFont(IItemFontProvider.BOLD_FONT).toStyle();

    private static final Comparator<String> STRING_COMPARATOR = CommonPlugin.INSTANCE.getComparator();

    private static final Comparator<Item> COMPARATOR = new Comparator<Item>()
    {
      public int compare(Item item1, Item item2)
      {
        int result = item1.getType().compareTo(item2.getType());
        return result == 0 ? STRING_COMPARATOR.compare(item1.getText(), item2.getText()) : result;
      }
    };

    private static final Image FOLDER_IMAGE = SetupEditorPlugin.INSTANCE.getSWTImage("obj16/folder"); //$NON-NLS-1$

    private static final Image JAVA_CLASS_IMAGE = SetupEditorPlugin.INSTANCE.getSWTImage("full/obj16/JavaCompilationUnit"); //$NON-NLS-1$

    private static final Image JAVA_PACKAGE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Package")); //$NON-NLS-1$

    private static final Image SETUP_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(SetupEditorPlugin.INSTANCE.getImage("full/obj16/SetupModelFile")); //$NON-NLS-1$

    private Type type;

    private String name;

    private String decoration;

    private Map<String, Map<String, Set<String>>> groupLinks = new TreeMap<String, Map<String, Set<String>>>();

    private Map<String, String> rawLinks;

    private ItemFilter itemFilter;

    private Item(Type type, String name)
    {
      super(type.getText(name), getImage(type));
      this.type = type;
      this.name = name;
    }

    public String getDecoration()
    {
      return decoration;
    }

    public void setDecoration(String decoration)
    {
      this.decoration = decoration;
    }

    public void addLink(String rawSourceFolderLink, String sourceFolderLink, String gitRepo, String repoLink)
    {
      if (rawSourceFolderLink != null)
      {
        if (rawLinks == null)
        {
          rawLinks = new HashMap<String, String>();
        }
        rawLinks.put(sourceFolderLink, rawSourceFolderLink);
      }

      addLink(sourceFolderLink, gitRepo, repoLink);
    }

    public void addLink(String sourceFolderLink, String gitRepo, String repoLink)
    {
      Map<String, Set<String>> map = groupLinks.get(repoLink);
      if (map == null)
      {
        map = new TreeMap<String, Set<String>>();
        groupLinks.put(repoLink, map);
      }

      CollectionUtil.add(map, sourceFolderLink, gitRepo);
    }

    public Map<String, Map<String, Set<String>>> getGroupLinks(Map<String, Set<String>> filter, Map<String, String> rawLinks)
    {
      Map<String, Map<String, Set<String>>> result = new LinkedHashMap<String, Map<String, Set<String>>>();

      for (Map.Entry<String, Map<String, Set<String>>> groupEntry : groupLinks.entrySet())
      {
        String key = groupEntry.getKey();
        Set<String> sourceLinks = filter.get(key);
        if (sourceLinks != null)
        {
          Map<String, Set<String>> links = new LinkedHashMap<String, Set<String>>();
          String path = type == Type.CLASS ? name.replace('.', '/') + ".java" : name.replace('.', '/'); //$NON-NLS-1$
          for (Map.Entry<String, Set<String>> entry : groupEntry.getValue().entrySet())
          {
            String link = entry.getKey();
            if (sourceLinks.contains(link))
            {
              String javaLink = link.replace("${1}", path); //$NON-NLS-1$
              links.put(javaLink, entry.getValue());
              if (rawLinks != null && this.rawLinks != null)
              {
                String rawLink = this.rawLinks.get(link);
                if (rawLink != null)
                {
                  String rawJavaLink = rawLink.replace("${1}", path); //$NON-NLS-1$
                  rawLinks.put(javaLink, rawJavaLink);
                }
              }
            }
          }

          if (!links.isEmpty())
          {
            String repoLink = key.replace("${1}", "/").replace("://", ":////").replace("//", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            result.put(repoLink, links);
          }
        }
      }

      return result;
    }

    public Set<String> getRepos()
    {
      Set<String> result = new TreeSet<String>();
      for (Map.Entry<String, Map<String, Set<String>>> groupEntry : groupLinks.entrySet())
      {
        for (Map.Entry<String, Set<String>> entry : groupEntry.getValue().entrySet())
        {
          result.addAll(entry.getValue());
        }
      }
      return result;
    }

    public Type getType()
    {
      return type;
    }

    public String getName()
    {
      return name;
    }

    public static Item create(Type type, String name)
    {
      return new Item(type, name);
    }

    public static Item createRootItem()
    {
      return new Item(Type.ROOT, Messages.OpenDiscoveredType_rootItem_name);
    }

    public static Item createPlaceholderItem()
    {
      return new Item(Type.PLACEHOLDER, Messages.OpenDiscoveredType_placeHolderItem_name);
    }

    @Override
    public Object getStyledText(Object object)
    {
      if (itemFilter != null && itemFilter.pattern != null)
      {
        Matcher matcher = itemFilter.pattern.matcher(name);
        boolean patternMatches = matcher.matches();
        if (!patternMatches && type == Type.PACKAGE && itemFilter.packagePattern != null)
        {
          matcher = itemFilter.packagePattern.matcher(name);
          patternMatches = matcher.matches();
        }

        if (patternMatches)
        {
          // System.err.println("" + text);
          org.eclipse.emf.edit.provider.StyledString styledLabel = new org.eclipse.emf.edit.provider.StyledString();
          int groupCount = matcher.groupCount();
          StringBuilder styleRun = null;
          boolean previousMatches = false;
          for (int i = name.length() - text.length(); i < name.length(); ++i)
          {
            boolean matches = false;
            for (int j = 1; j <= groupCount; ++j)
            {
              int start = matcher.start(j);
              int end = matcher.end(j);
              if (i >= start && i < end)
              {
                matches = true;
                break;
              }
            }

            if (previousMatches != matches)
            {
              if (styleRun != null)
              {
                if (previousMatches)
                {
                  styledLabel.append(styleRun.toString(), BOLD_STYLER);
                }
                else
                {
                  styledLabel.append(styleRun.toString());
                }
                styleRun = null;
              }

              previousMatches = matches;
            }

            if (styleRun == null)
            {
              styleRun = new StringBuilder();
            }

            styleRun.append(name.charAt(i));

            // System.err.print(matches ? '^' : ' ');
          }

          if (styleRun != null)
          {
            if (previousMatches)
            {
              styledLabel.append(styleRun.toString(), BOLD_STYLER);
            }
            else
            {
              styledLabel.append(styleRun.toString());
            }
          }

          // System.err.println();

          if (decoration != null)
          {
            styledLabel.append(" - " + decoration, org.eclipse.emf.edit.provider.StyledString.Style.DECORATIONS_STYLER); //$NON-NLS-1$
          }

          return styledLabel;
        }
      }

      if (decoration != null)
      {
        org.eclipse.emf.edit.provider.StyledString styledLabel = new org.eclipse.emf.edit.provider.StyledString();
        styledLabel.append(getText());
        styledLabel.append(" - " + decoration, org.eclipse.emf.edit.provider.StyledString.Style.DECORATIONS_STYLER); //$NON-NLS-1$
        return styledLabel;
      }

      return super.getStyledText(object);
    }

    @Override
    public Item getParent()
    {
      return (Item)super.getParent();
    }

    public EList<Item> getItems()
    {
      @SuppressWarnings("unchecked")
      EList<Item> children = (EList<Item>)(EList<?>)getChildren();
      return children;
    }

    public Item getItem(String text)
    {
      for (Item item : getItems())
      {
        if (text.equals(item.getText()))
        {
          return item;
        }
      }

      return null;
    }

    public void sort()
    {
      EList<Item> children = getItems();
      ECollections.sort(children, COMPARATOR);
      for (Item child : children)
      {
        child.sort();
      }
    }

    private static Image getImage(Type type)
    {
      switch (type)
      {
        case CLASS:
        case CLASS_LINK:
        {
          return JAVA_CLASS_IMAGE;
        }
        case FOLDER_LINK:
        {
          return FOLDER_IMAGE;
        }

        case PACKAGE:
        case PACKAGE_LINK:
        {
          return JAVA_PACKAGE_IMAGE;
        }
        default:
        case PLACEHOLDER:
        {
          return null;
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private class ToolTipLabelProvider extends DecoratingColumLabelProvider.StyledLabelProvider
  {
    final private ColumnViewerInformationControlToolTipSupport toolTipSupport;

    public ToolTipLabelProvider(AdapterFactory adapterFactory, Viewer viewer, ColumnViewerInformationControlToolTipSupport toolTipSupport)
    {
      super(new AdapterFactoryLabelProvider.StyledLabelProvider(adapterFactory, viewer), new IStyledLabelDecorator()
      {
        public void removeListener(ILabelProviderListener listener)
        {
        }

        public boolean isLabelProperty(Object element, String property)
        {
          return true;
        }

        public void dispose()
        {
        }

        public void addListener(ILabelProviderListener listener)
        {
        }

        public String decorateText(String text, Object element)
        {
          return text;
        }

        public Image decorateImage(Image image, Object element)
        {
          return image;
        }

        public StyledString decorateStyledText(StyledString styledString, Object element)
        {
          return styledString;
        }
      });

      this.toolTipSupport = toolTipSupport;
    }

    @Override
    public String getToolTipText(Object element)
    {
      Item item = (Item)element;
      StringBuilder result = new StringBuilder();
      result.append("<span style='white-space: nowrap;'>"); //$NON-NLS-1$
      result.append(DiagnosticDecorator.enquote("<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='" //$NON-NLS-1$
          + ImageURIRegistry.INSTANCE.getImageURI(ExtendedImageRegistry.INSTANCE.getImage(item.getImage())) + "'/> ")); //$NON-NLS-1$
      result.append(URI.decode(item.getName()));
      result.append("</span>"); //$NON-NLS-1$

      List<URI> projectSetups = getProjectSetups(item);
      String decoration = item.getDecoration();
      for (Item parent = item.getParent(); decoration == null && parent != null; parent = parent.getParent())
      {
        decoration = parent.getDecoration();
      }

      if (decoration != null && decoration.indexOf(' ') == -1)
      {
        for (Iterator<URI> it = projectSetups.iterator(); it.hasNext();)
        {
          URI uri = it.next();
          if (!decoration.equals(uri.lastSegment()))
          {
            it.remove();
          }
        }
      }

      if (!projectSetups.isEmpty())
      {
        Set<URI> uris = new HashSet<URI>();
        for (URI uri : projectSetups)
        {
          uris.add(uri.trimFragment());
        }
        if (uris.size() == 1)
        {
          projectSetups.clear();
          projectSetups.add(uris.iterator().next().trimFragment().appendFragment("/")); //$NON-NLS-1$
        }
      }

      if (projectSetups.size() == 1)
      {
        URI projectSetupURI = projectSetups.get(0);
        result.append("<br/>"); //$NON-NLS-1$
        result.append("<span style='white-space: nowrap;'>"); //$NON-NLS-1$
        result.append(DiagnosticDecorator.enquote(
            "<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='" + ImageURIRegistry.INSTANCE.getImageURI(Item.SETUP_IMAGE)) + "'/> "); //$NON-NLS-1$ //$NON-NLS-2$
        result.append("<a href=\""); //$NON-NLS-1$
        result.append(projectSetupURI);
        result.append("\">"); //$NON-NLS-1$
        result.append(projectSetupURI.lastSegment());
        result.append("</a>"); //$NON-NLS-1$
        result.append("</span>"); //$NON-NLS-1$
      }

      String toolTip = result.toString();
      AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport); //$NON-NLS-1$
      Point size = UIUtil.caclcuateSize(toolTip);
      hoverInformationControlManager.setSizeConstraints(size.x + 4, size.y + 1, true, false);

      return result.toString();
    }
  }

  /**
   * Returns the instance for this workbench window, if there is one.
   */
  public static OpenDiscoveredType getFor(IWorkbenchWindow workbenchWindow)
  {
    return DockableDialog.getFor(OpenDiscoveredType.class, workbenchWindow);
  }

  /**
   * Close the instance for this workbench window, if there is one.
   */
  public static void closeFor(IWorkbenchWindow workbenchWindow)
  {
    DockableDialog.closeFor(OpenDiscoveredType.class, workbenchWindow);
  }

  /**
   * Reopen or create the instance for this workbench window.
   */
  public static OpenDiscoveredType openFor(final IWorkbenchWindow workbenchWindow)
  {
    Factory<OpenDiscoveredType> factory = new Factory<OpenDiscoveredType>()
    {
      public OpenDiscoveredType create(IWorkbenchWindow workbenchWindow)
      {
        return new OpenDiscoveredType(workbenchWindow.getShell());
      }
    };

    return DockableDialog.openFor(OpenDiscoveredType.class, factory, workbenchWindow);
  }
}

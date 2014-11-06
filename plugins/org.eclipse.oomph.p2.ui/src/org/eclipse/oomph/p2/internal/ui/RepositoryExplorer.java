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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.GeneralDropAdapter;
import org.eclipse.oomph.internal.ui.GeneralDropAdapter.DroppedObjectHandler;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.RepositoryProvider;
import org.eclipse.oomph.p2.internal.ui.RepositoryManager.RepositoryManagerListener;
import org.eclipse.oomph.p2.provider.RequirementItemProvider;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class RepositoryExplorer extends ViewPart
{
  public static final String ID = "org.eclipse.oomph.p2.ui.RepositoryExplorer"; //$NON-NLS-1$

  private static final String CURRENT_NAMESPACE_KEY = "currentNamespace";

  private static final String EXPERT_MODE_KEY = "expertMode";

  private static final String CATEGORIZE_ITEMS_KEY = "categorizeItems";

  private static final String SOURCE_SUFFIX = ".source";

  private static final String FEATURE_SUFFIX = ".feature.group";

  private static final String SOURCE_FEATURE_SUFFIX = SOURCE_SUFFIX + FEATURE_SUFFIX;

  private static final String LOADING_INPUT = "Loading";

  private static final String ERROR_INPUT = "Error";

  private final RepositoryHistoryListener repositoryHistoryListener = new RepositoryHistoryListener();

  private final FocusListener focusListener = new FocusListener()
  {
    private Color originalForeground;

    public void focusGained(FocusEvent e)
    {
      if (originalForeground != null)
      {
        repositoryCombo.setText("");
        repositoryCombo.setForeground(originalForeground);
        originalForeground = null;
      }
    }

    public void focusLost(FocusEvent e)
    {
      if (RepositoryManager.INSTANCE.getActiveRepository() == null)
      {
        originalForeground = repositoryCombo.getForeground();
        repositoryCombo.setText("type repository url, drag and drop, or pick from list");
        repositoryCombo.setForeground(gray);
      }
    }
  };

  private final Object loadJobLock = new Object()
  {
    @Override
    public String toString()
    {
      return RepositoryExplorer.this.getClass().getSimpleName() + ".loadJobLock";
    }
  };

  private final IDialogSettings settings;

  private Color gray;

  private ComboViewer repositoryViewer;

  private Combo repositoryCombo;

  private RepositoryProvider.Metadata repositoryProvider;

  private Composite filterComposite;

  private Composite modeComposite;

  private StackLayout modeLayout;

  private Button categorizeItemsButton;

  private ComboViewer namespaceViewer;

  private TreeViewer itemViewer;

  private String currentNamespace;

  private boolean expertMode;

  private boolean categorizeItems;

  private URI loadingLocation;

  private IStatus loadError;

  private Job loadJob;

  public RepositoryExplorer()
  {
    settings = P2UIPlugin.INSTANCE.getDialogSettings(getClass().getSimpleName());
    currentNamespace = settings.get(CURRENT_NAMESPACE_KEY);
    if (currentNamespace == null)
    {
      currentNamespace = IInstallableUnit.NAMESPACE_IU_ID;
    }

    expertMode = settings.getBoolean(EXPERT_MODE_KEY);
    categorizeItems = settings.getBoolean(CATEGORIZE_ITEMS_KEY);
  }

  @Override
  public void dispose()
  {
    gray.dispose();
    gray = null;

    disposeRepositoryProvider();
    super.dispose();
  }

  private void disposeRepositoryProvider()
  {
    if (repositoryProvider != null)
    {
      repositoryProvider.dispose();
      repositoryProvider = null;
    }
  }

  @Override
  public void setFocus()
  {
    if (RepositoryManager.INSTANCE.getActiveRepository() != null)
    {
      repositoryCombo.setFocus();
    }
  }

  @Override
  public void createPartControl(Composite parent)
  {
    final Display display = parent.getDisplay();
    final Color white = display.getSystemColor(SWT.COLOR_WHITE);
    gray = new Color(display, 70, 70, 70);

    Composite container = new Composite(parent, SWT.NONE);
    container.setBackground(white);
    container.setLayout(new GridLayout(1, false));

    repositoryViewer = new ComboViewer(container, SWT.NONE);
    repositoryCombo = repositoryViewer.getCombo();
    repositoryCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    repositoryCombo.setToolTipText("Repository location (type a URL, drop a repository or pick from the drop down history)");
    repositoryCombo.addFocusListener(focusListener);
    repositoryCombo.addKeyListener(repositoryHistoryListener);

    repositoryViewer.setContentProvider(new RepositoryContentProvider());
    repositoryViewer.setLabelProvider(new LabelProvider());
    repositoryViewer.setInput(RepositoryManager.INSTANCE);
    repositoryViewer.addSelectionChangedListener(repositoryHistoryListener);

    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    List<? extends Transfer> transfersList = OomphTransferDelegate.asTransfers(org.eclipse.oomph.internal.ui.OomphTransferDelegate.DELEGATES);
    Transfer[] transfers = transfersList.toArray(new Transfer[transfersList.size()]);

    repositoryViewer.addDropSupport(dndOperations, transfers, new GeneralDropAdapter(repositoryViewer, P2Factory.eINSTANCE.createRepositoryList(),
        P2Package.Literals.REPOSITORY_LIST__REPOSITORIES, new DroppedObjectHandler()
        {
          public void handleDroppedObject(Object object) throws Exception
          {
            if (object instanceof Repository)
            {
              Repository repository = (Repository)object;
              String url = repository.getURL();
              if (!StringUtil.isEmpty(url))
              {
                activateAndLoadRepository(url);
              }
            }
          }
        }));

    // ------------------------------------------------

    GridLayout filterLayout = new GridLayout(2, false);
    filterLayout.marginWidth = 0;
    filterLayout.marginHeight = 0;

    filterComposite = new Composite(container, SWT.NONE);
    filterComposite.setLayout(filterLayout);
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    GridLayout filterPlaceholderLayout = new GridLayout();
    filterPlaceholderLayout.marginWidth = 0;
    filterPlaceholderLayout.marginHeight = 0;

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(filterPlaceholderLayout);
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    modeLayout = new StackLayout();
    modeComposite = new Composite(filterComposite, SWT.NONE);
    modeComposite.setLayout(modeLayout);

    categorizeItemsButton = new Button(modeComposite, SWT.CHECK);
    categorizeItemsButton.setText("Group items by category");
    categorizeItemsButton.setToolTipText("Whether to show items in categories or in a plain list");
    categorizeItemsButton.setSelection(categorizeItems);
    categorizeItemsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        categorizeItems = categorizeItemsButton.getSelection();
        settings.put(CATEGORIZE_ITEMS_KEY, categorizeItems);
        triggerReload();
      }
    });

    namespaceViewer = new ComboViewer(modeComposite, SWT.READ_ONLY);
    namespaceViewer.getCombo().setToolTipText("Select the namespace of the capabilities to show in the filtered tree");
    namespaceViewer.setSorter(new ViewerSorter());
    namespaceViewer.setContentProvider(new ArrayContentProvider());
    namespaceViewer.setLabelProvider(new LabelProvider());
    namespaceViewer.setInput(new String[] { currentNamespace });
    namespaceViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)namespaceViewer.getSelection();
        String newNamespace = (String)selection.getFirstElement();
        if (!ObjectUtil.equals(newNamespace, currentNamespace))
        {
          settings.put(CURRENT_NAMESPACE_KEY, newNamespace);
          currentNamespace = newNamespace;

          triggerReload();
        }
      }
    });

    updateExpertMode();

    PatternFilter filter = new PatternFilter();
    filter.setIncludeLeadingWildcard(true);

    // ------------------------------------------------

    FilteredTree filteredTree = new FilteredTree(container, SWT.BORDER | SWT.MULTI, filter, true);
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);

    itemViewer = filteredTree.getViewer();
    itemViewer.setSorter(new ViewerSorter());
    itemViewer.setContentProvider(new ItemContentProvider());
    itemViewer.setLabelProvider(new ItemLabelProvider());
    itemViewer.addDragSupport(dndOperations, transfers, new GeneralDragAdapter(itemViewer, new GeneralDragAdapter.DraggedObjectsFactory()
    {
      public List<EObject> createDraggedObjects(ISelection selection) throws Exception
      {
        List<EObject> result = new ArrayList<EObject>();

        IStructuredSelection ssel = (IStructuredSelection)selection;
        for (Iterator<?> it = ssel.iterator(); it.hasNext();)
        {
          Object element = it.next();
          if (element instanceof Item)
          {
            Item item = (Item)element;
            String namespace = item.getNamespace();
            if (namespace != null)
            {
              Requirement requirement = P2Factory.eINSTANCE.createRequirement();
              requirement.setNamespace(namespace);
              requirement.setName(item.getName());

              Version version = item.getVersion();
              if (version != null && !Version.emptyVersion.equals(version))
              {
                requirement.setVersionRange(new VersionRange(version.toString()));
              }

              result.add(requirement);
            }
          }
        }

        return result;
      }
    }));

    hookActions();

    String activeRepository = RepositoryManager.INSTANCE.getActiveRepository();
    if (activeRepository == null)
    {
      // Force hint to be shown.
      focusListener.focusLost(null);
    }
    else
    {
      repositoryCombo.setText(activeRepository);
      triggerLoad(activeRepository);
    }

    namespaceViewer.setSelection(new StructuredSelection(currentNamespace));
  }

  private void hookActions()
  {
    IActionBars actionBars = getViewSite().getActionBars();
    IToolBarManager toolbarManager = actionBars.getToolBarManager();
    IMenuManager menuManager = actionBars.getMenuManager();

    toolbarManager.add(new Separator("additions"));
    toolbarManager.add(new Action("Collapse All", P2UIPlugin.INSTANCE.getImageDescriptor("collapse-all"))
    {
      {
        setToolTipText("Collapse all tree items");
      }

      @Override
      public void run()
      {
        itemViewer.collapseAll();
      }
    });

    toolbarManager.add(new Action("Refresh", P2UIPlugin.INSTANCE.getImageDescriptor("refresh"))
    {
      {
        setToolTipText("Reload the active repository and refresh the tree");
      }

      @Override
      public void run()
      {
        String activeRepository = RepositoryManager.INSTANCE.getActiveRepository();
        if (activeRepository != null)
        {
          disposeRepositoryProvider();
          triggerLoad(activeRepository);
        }
      }
    });

    toolbarManager.add(new Separator("end"));

    menuManager.add(new Action("Manage Repositories...", P2UIPlugin.INSTANCE.getImageDescriptor("full/obj16/RepositoryList"))
    {
      {
        setToolTipText("Open the dialog for the management of the repository drop down history");
      }

      @Override
      public void run()
      {
      }
    });

    menuManager.add(new Separator("additions"));
    menuManager.add(new Separator());

    menuManager.add(new Action("Expert Mode", IAction.AS_CHECK_BOX)
    {
      {
        setToolTipText("Toggle between the expert and the simple mode");
        setChecked(expertMode);
      }

      @Override
      public void run()
      {
        expertMode = isChecked();
        settings.put(EXPERT_MODE_KEY, expertMode);
        updateExpertMode();
        triggerReload();
      }
    });
  }

  private void updateExpertMode()
  {
    modeLayout.topControl = expertMode ? namespaceViewer.getControl() : categorizeItemsButton;
    modeComposite.layout();
  }

  private void activateAndLoadRepository(String repository)
  {
    if (RepositoryManager.INSTANCE.setActiveRepository(repository))
    {
      triggerLoad(repository);
    }
  }

  private void triggerReload()
  {
    String activeRepository = RepositoryManager.INSTANCE.getActiveRepository();
    if (activeRepository != null)
    {
      triggerLoad(activeRepository);
    }
  }

  private void triggerLoad(String repository)
  {
    URI location = null;

    try
    {
      location = new URI(repository);
    }
    catch (URISyntaxException ex)
    {
      File folder = new File(repository);
      if (folder.isDirectory())
      {
        location = folder.toURI();
      }
    }

    if (location != null)
    {
      triggerLoad(location);
    }
  }

  private void triggerLoad(final URI location)
  {
    loadError = null;
    loadingLocation = location;
    itemViewer.setInput(LOADING_INPUT);

    synchronized (loadJobLock)
    {
      final Job oldJob = loadJob;
      loadJob = new Job("Loading " + location)
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          if (oldJob != null)
          {
            oldJob.cancel();

            try
            {
              oldJob.join();
            }
            catch (InterruptedException ex)
            {
              return Status.CANCEL_STATUS;
            }
          }

          try
          {
            SubMonitor progress = SubMonitor.convert(monitor, 2);

            if (repositoryProvider == null || !repositoryProvider.getLocation().equals(location))
            {
              disposeRepositoryProvider();

              IMetadataRepositoryManager repositoryManager = P2Util.getAgentManager().getCurrentAgent().getMetadataRepositoryManager();
              repositoryProvider = new RepositoryProvider.Metadata(repositoryManager, location);
            }

            IMetadataRepository repository = repositoryProvider.getRepository(progress.newChild(1));
            IQueryResult<IInstallableUnit> query = repository.query(QueryUtil.createIUAnyQuery(), progress.newChild(1));

            if (expertMode)
            {
              analyzeCapabilities(query, progress);
            }
            else if (categorizeItems)
            {
              analyzeCategories(query, progress);
            }
            else
            {
              analyzeFeatures(query, progress);
            }

            return Status.OK_STATUS;
          }
          catch (OperationCanceledException ex)
          {
            return Status.CANCEL_STATUS;
          }
          catch (Exception ex)
          {
            if (ex instanceof P2Exception)
            {
              Throwable cause = ex.getCause();
              if (cause instanceof CoreException)
              {
                ex = (CoreException)cause;
              }
            }

            loadError = P2UIPlugin.INSTANCE.getStatus(ex);
            UIUtil.asyncExec(new Runnable()
            {
              public void run()
              {
                itemViewer.setInput(ERROR_INPUT);
              }
            });

            return Status.OK_STATUS;
          }
          catch (Throwable t)
          {
            return P2UIPlugin.INSTANCE.getStatus(t);
          }
          finally
          {
            loadingLocation = null;
            synchronized (loadJobLock)
            {
              loadJob = null;
            }
          }
        }
      };

      loadJob.schedule();
    }
  }

  private void analyzeCategories(IQueryResult<IInstallableUnit> query, IProgressMonitor monitor)
  {
    // IU.id -> value
    Map<String, String> names = new HashMap<String, String>();
    Map<String, Set<IInstallableUnit>> ius = new HashMap<String, Set<IInstallableUnit>>();
    Map<String, Set<IRequirement>> categories = new HashMap<String, Set<IRequirement>>();

    for (IInstallableUnit iu : query)
    {
      P2UIPlugin.checkCancelation(monitor);
      String id = iu.getId();

      names.put(id, getName(iu));
      CollectionUtil.add(ius, id, iu);

      if (isCategory(iu))
      {
        CollectionUtil.addAll(categories, id, iu.getRequirements());
      }
    }

    Set<String> rootIDs = new HashSet<String>();

    for (String categoryID : categories.keySet())
    {
      P2UIPlugin.checkCancelation(monitor);
      rootIDs.add(categoryID);
    }

    for (Set<IRequirement> requirements : categories.values())
    {
      for (IRequirement requirement : requirements)
      {
        P2UIPlugin.checkCancelation(monitor);

        if (requirement instanceof IRequiredCapability)
        {
          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
          if (IInstallableUnit.NAMESPACE_IU_ID.equals(requiredCapability.getNamespace()))
          {
            rootIDs.remove(requiredCapability.getName());
          }
        }
      }
    }

    final CategoryItem[] roots = new CategoryItem[rootIDs.size()];
    int i = 0;

    for (String id : rootIDs)
    {
      P2UIPlugin.checkCancelation(monitor);
      roots[i++] = analyzeCategory(names, ius, categories, id, monitor);
    }

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        itemViewer.setInput(roots);
      }
    });
  }

  private CategoryItem analyzeCategory(Map<String, String> names, Map<String, Set<IInstallableUnit>> ius, Map<String, Set<IRequirement>> categories, String id,
      IProgressMonitor monitor)
  {
    Map<String, ContainerItem> children = new HashMap<String, ContainerItem>();
    Map<ContainerItem, Set<Version>> versions = new HashMap<ContainerItem, Set<Version>>();

    for (IRequirement requirement : categories.get(id))
    {
      P2UIPlugin.checkCancelation(monitor);

      if (requirement instanceof IRequiredCapability)
      {
        IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
        if (IInstallableUnit.NAMESPACE_IU_ID.equals(requiredCapability.getNamespace()))
        {
          String requiredID = requiredCapability.getName();
          if (categories.containsKey(requiredID))
          {
            CategoryItem child = analyzeCategory(names, ius, categories, requiredID, monitor);
            children.put(requiredID, child);
          }
          else
          {
            VersionRange range = requiredCapability.getRange();
            ContainerItem child = children.get(requiredID);

            Set<IInstallableUnit> set = ius.get(requiredID);
            for (IInstallableUnit iu : set)
            {
              P2UIPlugin.checkCancelation(monitor);
              Version version = iu.getVersion();

              if (range.isIncluded(version))
              {
                if (child == null)
                {
                  String name = names.get(requiredID);
                  if (isFeature(iu))
                  {
                    if (requiredID.endsWith(SOURCE_FEATURE_SUFFIX))
                    {
                      String mainID = requiredID.substring(0, requiredID.length() - SOURCE_FEATURE_SUFFIX.length()) + FEATURE_SUFFIX;
                      String mainName = names.get(mainID);

                      if (ObjectUtil.equals(name, mainName))
                      {
                        name += " (Source)";
                      }
                    }

                    child = new FeatureItem(requiredID);
                  }
                  else
                  {
                    if (requiredID.endsWith(SOURCE_SUFFIX))
                    {
                      String mainID = requiredID.substring(0, requiredID.length() - SOURCE_SUFFIX.length());
                      String mainName = names.get(mainID);

                      if (ObjectUtil.equals(name, mainName))
                      {
                        name += " (Source)";
                      }
                    }

                    child = new PluginItem(requiredID);
                  }

                  child.setLabel(name);
                  children.put(requiredID, child);
                }

                CollectionUtil.add(versions, child, version);
              }
            }
          }
        }
      }
    }

    for (Map.Entry<ContainerItem, Set<Version>> entry : versions.entrySet())
    {
      ContainerItem child = entry.getKey();
      List<VersionItem> versionItems = new ArrayList<VersionItem>();

      for (Version version : sortVersions(entry.getValue()))
      {
        P2UIPlugin.checkCancelation(monitor);

        VersionItem versionItem = new VersionItem(child, version);
        versionItems.add(versionItem);
      }

      child.setChildren(versionItems.toArray(new VersionItem[versionItems.size()]));
    }

    CategoryItem categoryItem = new CategoryItem();
    categoryItem.setLabel(names.get(id));
    categoryItem.setChildren(children.values().toArray(new ContainerItem[children.size()]));
    return categoryItem;
  }

  private void analyzeFeatures(IQueryResult<IInstallableUnit> query, IProgressMonitor monitor)
  {
    Map<String, String> names = new HashMap<String, String>();
    Map<String, Set<Version>> versions = new HashMap<String, Set<Version>>();

    for (IInstallableUnit iu : query)
    {
      P2UIPlugin.checkCancelation(monitor);
      String id = iu.getId();

      if (id.endsWith(FEATURE_SUFFIX) && !id.endsWith(SOURCE_FEATURE_SUFFIX))
      {
        names.put(id, getName(iu));
        CollectionUtil.add(versions, id, iu.getVersion());
      }
    }

    String[] sortedIDs = sortStrings(versions.keySet()); // TODO Sort by name later!
    final FeatureItem[] featureItems = new FeatureItem[sortedIDs.length];

    for (int i = 0; i < sortedIDs.length; i++)
    {
      String id = sortedIDs[i];
      FeatureItem featureItem = new FeatureItem(id);
      featureItem.setLabel(names.get(id));
      featureItems[i] = featureItem;

      Version[] sortedVersions = sortVersions(versions.get(id));
      VersionItem[] versionItems = new VersionItem[sortedVersions.length];

      for (int j = 0; j < sortedVersions.length; j++)
      {
        P2UIPlugin.checkCancelation(monitor);
        Version version = sortedVersions[j];

        VersionItem versionItem = new VersionItem(featureItem, version);
        versionItems[j] = versionItem;
      }

      featureItem.setChildren(versionItems);
    }

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        itemViewer.setInput(featureItems);
      }
    });
  }

  private void analyzeCapabilities(IQueryResult<IInstallableUnit> query, IProgressMonitor monitor)
  {
    final Set<String> flavors = new HashSet<String>();
    final Set<String> namespaces = new HashSet<String>();
    Map<String, Set<Version>> versions = new HashMap<String, Set<Version>>();

    for (IInstallableUnit iu : query)
    {
      for (IProvidedCapability capability : iu.getProvidedCapabilities())
      {
        P2UIPlugin.checkCancelation(monitor);

        String namespace = capability.getNamespace();
        if ("org.eclipse.equinox.p2.flavor".equals(namespace))
        {
          flavors.add(capability.getName());
        }
        else if (!"A.PDE.Target.Platform".equalsIgnoreCase(namespace))
        {
          namespaces.add(namespace);
        }

        if (ObjectUtil.equals(namespace, currentNamespace))
        {
          CollectionUtil.add(versions, capability.getName(), capability.getVersion());
        }
      }
    }

    String[] flavorIDs = getMinimalFlavors(flavors);
    for (Iterator<String> it = namespaces.iterator(); it.hasNext();)
    {
      String namespace = it.next();
      for (int i = 0; i < flavorIDs.length; i++)
      {
        String flavor = flavorIDs[i];
        if (namespace.startsWith(flavor))
        {
          it.remove();
          break;
        }
      }
    }

    String[] sortedIDs = sortStrings(versions.keySet());
    final CapabilityItem[] capabilityItems = new CapabilityItem[sortedIDs.length];

    for (int i = 0; i < sortedIDs.length; i++)
    {
      String id = sortedIDs[i];
      CapabilityItem capabilityItem = new CapabilityItem();
      capabilityItem.setNamespace(currentNamespace);
      capabilityItem.setLabel(id);
      capabilityItems[i] = capabilityItem;

      Version[] sortedVersions = sortVersions(versions.get(id));
      VersionItem[] versionItems = new VersionItem[sortedVersions.length];

      for (int j = 0; j < sortedVersions.length; j++)
      {
        P2UIPlugin.checkCancelation(monitor);
        Version version = sortedVersions[j];

        VersionItem versionItem = new VersionItem(capabilityItem, version);
        versionItems[j] = versionItem;
      }

      capabilityItem.setChildren(versionItems);
    }

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        itemViewer.setInput(capabilityItems);

        namespaceViewer.setInput(namespaces);
        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            namespaceViewer.setSelection(new StructuredSelection(currentNamespace));
            filterComposite.layout();
          }
        });
      }
    });
  }

  private static String[] getMinimalFlavors(final Set<String> flavors)
  {
    String[] flavorIDs = sortStrings(flavors);
    int start = 0;

    while (start < flavorIDs.length)
    {
      boolean changed = false;
      for (int i = start + 1; i < flavorIDs.length; i++)
      {
        String flavorID = flavorIDs[i];
        if (flavorID.startsWith(flavorIDs[start]))
        {
          flavors.remove(flavorID);
          changed = true;
        }
      }

      if (changed)
      {
        flavorIDs = sortStrings(flavors);
      }

      ++start;
    }

    return flavorIDs;
  }

  private static String getName(IInstallableUnit iu)
  {
    String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
    if (StringUtil.isEmpty(name))
    {
      return iu.getId();
    }

    return name;
  }

  private static boolean isCategory(IInstallableUnit iu)
  {
    return "true".equalsIgnoreCase(iu.getProperty(QueryUtil.PROP_TYPE_CATEGORY));
  }

  private static boolean isFeature(IInstallableUnit iu)
  {
    return iu.getId().endsWith(FEATURE_SUFFIX);
  }

  private static String[] sortStrings(Collection<String> c)
  {
    String[] array = c.toArray(new String[c.size()]);
    Arrays.sort(array);
    return array;
  }

  private static Version[] sortVersions(Collection<Version> c)
  {
    Version[] array = c.toArray(new Version[c.size()]);
    Arrays.sort(array);
    return array;
  }

  public static boolean explore(String repository)
  {
    IWorkbenchWindow window = UIUtil.WORKBENCH.getActiveWorkbenchWindow();
    if (window != null)
    {
      IWorkbenchPage page = window.getActivePage();
      if (page != null)
      {
        IViewPart view = page.findView(ID);
        if (view == null)
        {
          try
          {
            view = page.showView(ID);
          }
          catch (PartInitException ex)
          {
            P2UIPlugin.INSTANCE.log(ex);
          }
        }

        if (view instanceof RepositoryExplorer)
        {
          RepositoryExplorer explorer = (RepositoryExplorer)view;
          explorer.activateAndLoadRepository(repository);
          return true;
        }
      }
    }

    return false;
  }

  /**
   * @author Eike Stepper
   */
  private final class RepositoryHistoryListener extends KeyAdapter implements ISelectionChangedListener
  {
    private boolean listVisible;

    private String listRepository;

    @Override
    public void keyReleased(KeyEvent e)
    {
      boolean currentListVisible = repositoryCombo.getListVisible();
      if (currentListVisible)
      {
        String repository = getSelectedRepository();
        if (!StringUtil.isEmpty(repository))
        {
          listRepository = repository;
        }
      }

      if (e.keyCode == SWT.DEL && currentListVisible)
      {
        RepositoryManager.INSTANCE.removeRepository(listRepository);
      }
      else if (e.keyCode == SWT.CR && listVisible && !currentListVisible)
      {
        selected();
      }

      listVisible = currentListVisible;
    }

    public void selectionChanged(SelectionChangedEvent event)
    {
      listVisible = repositoryCombo.getListVisible();
      if (!listVisible)
      {
        selected();
      }
    }

    private void selected()
    {
      String newRepository = getSelectedRepository();
      activateAndLoadRepository(newRepository);
    }

    private String getSelectedRepository()
    {
      IStructuredSelection selection = (IStructuredSelection)repositoryViewer.getSelection();
      return selection.isEmpty() ? repositoryCombo.getText() : (String)selection.getFirstElement();
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class RepositoryContentProvider implements IStructuredContentProvider, RepositoryManagerListener
  {
    public RepositoryContentProvider()
    {
      RepositoryManager.INSTANCE.addListener(this);
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public void dispose()
    {
      RepositoryManager.INSTANCE.removeListener(this);
    }

    public Object[] getElements(Object element)
    {
      return RepositoryManager.INSTANCE.getRepositories();
    }

    public void repositoriesChanged(RepositoryManager repositoryManager)
    {
      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          repositoryViewer.refresh();

          UIUtil.asyncExec(new Runnable()
          {
            public void run()
            {
              String activeRepository = RepositoryManager.INSTANCE.getActiveRepository();
              if (activeRepository == null)
              {
                repositoryViewer.setSelection(StructuredSelection.EMPTY);
                repositoryCombo.setText("");
              }
              else
              {
                ISelection selection = new StructuredSelection(activeRepository);
                repositoryViewer.setSelection(selection);
                repositoryCombo.setText(activeRepository);
                repositoryCombo.setSelection(new Point(0, activeRepository.length()));
              }
            }
          });
        }
      });
    }

    public void activeRepositoryChanged(RepositoryManager repositoryManager, String repository)
    {
      // Do nothing.
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ItemContentProvider implements ITreeContentProvider
  {
    private final Object[] NO_ELEMENTS = new Object[0];

    private Object input;

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      input = newInput;
    }

    public void dispose()
    {
      input = null;
    }

    public Object getParent(Object element)
    {
      return null;
    }

    public Object[] getElements(Object element)
    {
      return getChildren(element);
    }

    public Object[] getChildren(Object element)
    {
      if (element == LOADING_INPUT)
      {
        return new String[] { "Loading " + loadingLocation + " ..." };
      }

      if (element == ERROR_INPUT)
      {
        return new IStatus[] { loadError };
      }

      if (element instanceof String)
      {
        return NO_ELEMENTS;
      }

      if (element instanceof IStatus)
      {
        IStatus status = (IStatus)element;
        if (status.isMultiStatus())
        {
          return status.getChildren();
        }

        return NO_ELEMENTS;
      }

      if (element == input)
      {
        return (Object[])input;
      }

      Item[] children = ((Item)element).getChildren();
      if (children != null)
      {
        return children;
      }

      return NO_ELEMENTS;
    }

    public boolean hasChildren(Object element)
    {
      if (element == LOADING_INPUT)
      {
        return true;
      }

      if (element == ERROR_INPUT)
      {
        return true;
      }

      if (element instanceof String)
      {
        return false;
      }

      if (element instanceof IStatus)
      {
        IStatus status = (IStatus)element;
        if (status.isMultiStatus() && status.getChildren().length != 0)
        {
          return true;
        }

        return false;
      }

      if (element == input)
      {
        return ((Object[])input).length != 0;
      }

      return ((Item)element).hasChildren();
    }
  }

  /**
  * @author Eike Stepper
  */
  private static final class ItemLabelProvider extends LabelProvider
  {
    @Override
    public Image getImage(Object element)
    {
      if (element instanceof IStatus)
      {
        IStatus status = (IStatus)element;
        return UIUtil.getStatusImage(status.getSeverity());
      }

      if (element instanceof Item)
      {
        Item item = (Item)element;
        return item.getImage();
      }

      return super.getImage(element);
    }

    @Override
    public String getText(Object element)
    {
      if (element instanceof IStatus)
      {
        IStatus status = (IStatus)element;
        return status.getMessage();
      }

      return super.getText(element);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class Item
  {
    private String label;

    public Item()
    {
    }

    public String getNamespace()
    {
      return IInstallableUnit.NAMESPACE_IU_ID;
    }

    public String getName()
    {
      return getLabel();
    }

    public String getLabel()
    {
      return label;
    }

    public void setLabel(String label)
    {
      this.label = label;
    }

    public Version getVersion()
    {
      return null;
    }

    public Item[] getChildren()
    {
      return null;
    }

    public boolean hasChildren()
    {
      return false;
    }

    @Override
    public String toString()
    {
      return label;
    }

    @Override
    public final int hashCode()
    {
      return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj)
    {
      return super.equals(obj);
    }

    public abstract Image getImage();
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class ContainerItem extends Item
  {
    private Item[] children;

    @Override
    public boolean hasChildren()
    {
      return children != null && children.length != 0;
    }

    @Override
    public Item[] getChildren()
    {
      return children;
    }

    public void setChildren(Item[] children)
    {
      this.children = children;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class CategoryItem extends ContainerItem
  {
    private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/category");

    @Override
    public String getNamespace()
    {
      return null;
    }

    @Override
    public Image getImage()
    {
      return IMAGE;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class FeatureItem extends ContainerItem
  {
    private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactFeature");

    private final String id;

    public FeatureItem(String id)
    {
      this.id = id;
    }

    @Override
    public Image getImage()
    {
      return IMAGE;
    }

    @Override
    public String getName()
    {
      return id;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class PluginItem extends ContainerItem
  {
    private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactPlugin");

    private final String id;

    public PluginItem(String id)
    {
      this.id = id;
    }

    @Override
    public Image getImage()
    {
      return IMAGE;
    }

    @Override
    public String getName()
    {
      return id;
    }
  }

  // /**
  // * @author Eike Stepper
  // */
  // private static final class PatchItem extends ContainerItem
  // {
  // private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/patch");
  //
  // @Override
  // public Image getImage()
  // {
  // return IMAGE;
  // }
  // }

  /**
   * @author Eike Stepper
   */
  private static final class CapabilityItem extends ContainerItem
  {
    private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/capability");

    private static final Image FEATURE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactFeature");

    private static final Image PLUGIN_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactPlugin");

    private static final Image PACKAGE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("full/obj16/Requirement_Package");

    private String namespace;

    @Override
    public String getNamespace()
    {
      return namespace;
    }

    public void setNamespace(String namespace)
    {
      this.namespace = namespace;
    }

    @Override
    public Image getImage()
    {
      if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
      {
        if (getLabel().endsWith(FEATURE_SUFFIX))
        {
          return FEATURE_IMAGE;
        }

        return PLUGIN_IMAGE;
      }

      if (RequirementItemProvider.NAMESPACE_PACKAGE_ID.equals(namespace))
      {
        return PACKAGE_IMAGE;
      }

      return IMAGE;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class VersionItem extends Item
  {
    private static final Image IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/version");

    private final Item parent;

    public VersionItem(Item parent, Version version)
    {
      this.parent = parent;
      setLabel(version.toString());
    }

    @Override
    public Image getImage()
    {
      return IMAGE;
    }

    @Override
    public String getNamespace()
    {
      return parent.getNamespace();
    }

    @Override
    public String getLabel()
    {
      return parent.getLabel();
    }

    @Override
    public Version getVersion()
    {
      return Version.parseVersion(super.getLabel());
    }

    @Override
    public String toString()
    {
      return super.getLabel();
    }
  }
}

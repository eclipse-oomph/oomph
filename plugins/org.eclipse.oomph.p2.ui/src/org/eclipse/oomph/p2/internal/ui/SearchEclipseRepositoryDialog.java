/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.internal.core.P2Index;
import org.eclipse.oomph.p2.internal.core.P2Index.Repository;
import org.eclipse.oomph.p2.provider.P2EditPlugin;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.DockableDialog.Factory;
import org.eclipse.oomph.ui.FilteredTreeWithoutWorkbench;
import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IStyledLabelDecorator;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.PatternFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Ed Merks
 */
public class SearchEclipseRepositoryDialog extends OomphDialog
{
  public static final String TITLE = "Eclipse Repository Search";

  public static final String MESSAGE = "Search Eclipse repositories by entering the fully qualified name of a Java package or installable unit";

  private final DockableDialog.Dockable dockable = new DockableDialog.Dockable(this);

  private String selectedRepository;

  private TreeViewer capabilitiesViewer;

  private TreeViewer detailsViewer;

  private Job detailsLoadJob;

  public SearchEclipseRepositoryDialog(Shell parentShell)
  {
    super(parentShell, TITLE, 700, 500, P2UIPlugin.INSTANCE, false);

    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MAX | (OS.INSTANCE.isWin() ? SWT.MIN : SWT.NONE));
    setBlockOnOpen(false);
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
    return MESSAGE + ".";
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentManager.png";
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  public String getSelectedRepository()
  {
    return selectedRepository;
  }

  public void setSelectedRepository(String selectedElement)
  {
    Button button = getButton(IDialogConstants.OK_ID);
    button.setEnabled(selectedElement != null);
    selectedRepository = selectedElement;
  }

  @Override
  protected void createUI(Composite composite)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("tool16/search"));

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
    collapseAllButton.setToolTipText("Collapse All");
    collapseAllButton.setImage(P2UIPlugin.INSTANCE.getSWTImage("collapse-all"));
    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        capabilitiesViewer.collapseAll();
      }
    });

    final ItemFilter filter = new ItemFilter();
    filter.setIncludeLeadingWildcard(true);

    final FilteredTreeWithoutWorkbench filteredTree = new FilteredTreeWithoutWorkbench(treeComposite, SWT.BORDER, filter, filter);
    filteredTree.setExpansionCount(100);

    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);

    final AdapterFactory adapterFactory = new ComposedAdapterFactory();
    capabilitiesViewer = filteredTree.getViewer();
    capabilitiesViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, capabilitiesViewer)));
    capabilitiesViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    capabilitiesViewer.setUseHashlookup(true);

    new ColumnViewerInformationControlToolTipSupport(capabilitiesViewer, null);

    final Tree capabilitiesTree = capabilitiesViewer.getTree();
    capabilitiesTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    capabilitiesViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)capabilitiesViewer.getSelection();
        Item item = (Item)selection.getFirstElement();
        boolean expanded = capabilitiesViewer.getExpandedState(item);
        capabilitiesViewer.setExpandedState(item, !expanded);

        if (!item.isConcrete())
        {
          while (item != null)
          {
            for (Item child : item.getItems())
            {
              if (filter.isElementVisible(capabilitiesViewer, child))
              {
                if (child.isConcrete())
                {
                  capabilitiesViewer.setSelection(new StructuredSelection(child), true);
                  item = null;
                  break;
                }

                capabilitiesViewer.setExpandedState(child, true);
                item = child;
                break;
              }
            }
          }
        }
      }
    });

    capabilitiesViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        if (item != null && item.isCapability())
        {
          String namespace = item.getNamespace();
          String name = item.getName();
          loadDetails(namespace, name);
        }
        else
        {
          detailsViewer.setInput(null);
        }

        setSelectedRepository(null);
      }
    });

    capabilitiesViewer.addDragSupport(RepositoryExplorer.DND_OPERATIONS, RepositoryExplorer.DND_TRANSFERS,
        new GeneralDragAdapter(capabilitiesViewer, new GeneralDragAdapter.DraggedObjectsFactory()
        {
          public List<Object> createDraggedObjects(ISelection selection) throws Exception
          {
            List<Object> result = new ArrayList<Object>();
            for (Object object : ((IStructuredSelection)selection).toArray())
            {
              if (object instanceof Item)
              {
                Item item = (Item)object;
                if (item.isCapability())
                {
                  result.add(createRequirement(item));
                }
              }
            }

            return result;
          }

          private Requirement createRequirement(Item item)
          {
            Requirement requirement = P2Factory.eINSTANCE.createRequirement(item.getName());
            requirement.setNamespace(item.getNamespace());
            return requirement;
          }
        }, RepositoryExplorer.DND_DELEGATES));

    Composite detailsComposite = new Composite(sashForm, SWT.NONE);
    detailsComposite.setLayout(new FillLayout());
    detailsComposite.setForeground(capabilitiesTree.getForeground());
    detailsComposite.setBackground(capabilitiesTree.getBackground());
    detailsViewer = new TreeViewer(detailsComposite);
    detailsViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, detailsViewer)));
    detailsViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

    detailsViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        setSelectedRepository(item != null && item.isRepository() ? item.getText() : null);
      }
    });

    detailsViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        if (selectedRepository != null)
        {
          okPressed();
        }
      }
    });

    detailsViewer.addDragSupport(RepositoryExplorer.DND_OPERATIONS, RepositoryExplorer.DND_TRANSFERS,
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
                if (item.isRepository())
                {
                  result.add(P2Factory.eINSTANCE.createRepository(item.getText()));
                }
                else if (item.isCapability())
                {
                  result.add(createRequirement(item));
                }
                else
                {
                  Requirement requirement = createRequirement(item.getParent());
                  Version version = Version.create(item.getText());
                  if (!Version.emptyVersion.equals(version))
                  {
                    requirement.setVersionRange(new VersionRange(version, true, version, true));
                  }

                  result.add(requirement);
                }
              }
            }

            return result;
          }

          private Requirement createRequirement(Item item)
          {
            Requirement requirement = P2Factory.eINSTANCE.createRequirement(item.getName());
            requirement.setNamespace(item.getNamespace());
            return requirement;
          }
        }, RepositoryExplorer.DND_DELEGATES));

    sashForm.setWeights(new int[] { 14, 5 });

    UIUtil.asyncExec(composite, new Runnable()
    {
      public void run()
      {
        setSelectedRepository(null);
      }
    });

    loadModel();
  }

  protected void loadModel()
  {
    Item root = Item.createItem();
    root.getChildren().add(Item.createNamespaceItem("Loading..."));
    capabilitiesViewer.setInput(root);

    Job job = new Job("Capability Loader")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        final Item root = Item.createItem();
        EList<Object> children = root.getChildren();
        Map<String, Set<String>> capabilities = new LinkedHashMap<String, Set<String>>(P2Index.INSTANCE.getCapabilities());
        if (capabilities.isEmpty())
        {
          children.add(Item.createNamespaceItem("Index unavailable"));
        }
        else
        {
          Set<String> flavors = capabilities.get("org.eclipse.equinox.p2.flavor");
          Set<String> capabilityKeys = capabilities.keySet();
          RepositoryExplorer.minimizeNamespaces(flavors, capabilityKeys);
          capabilityKeys.remove("org.eclipse.equinox.p2.flavor");
          capabilityKeys.remove("A.PDE.Target.Platform");
          for (Entry<String, Set<String>> entry : capabilities.entrySet())
          {
            String namespace = entry.getKey();
            Item namespaceItem = Item.createNamespaceItem(namespace);
            children.add(namespaceItem);

            Map<SegmentSequence, Item> hierarchicalChildren = new LinkedHashMap<SegmentSequence, Item>();
            SegmentSequence baseName = SegmentSequence.create(".");
            hierarchicalChildren.put(baseName, namespaceItem);
            for (String value : entry.getValue())
            {
              SegmentSequence qualifiedName = SegmentSequence.create(".", value);
              SegmentSequence partialName = baseName;
              Item parent = namespaceItem;
              for (String segment : qualifiedName.segments())
              {
                partialName = partialName.append(segment);
                Item itemProvider = hierarchicalChildren.get(partialName);
                if (itemProvider == null)
                {
                  itemProvider = Item.create(namespace, partialName);
                  hierarchicalChildren.put(partialName, itemProvider);
                  parent.getChildren().add(itemProvider);
                }

                parent = itemProvider;
              }

              parent.setConcrete();
            }
          }

          root.sort();
        }

        UIUtil.asyncExec(capabilitiesViewer.getControl(), new Runnable()
        {
          public void run()
          {
            capabilitiesViewer.setInput(root);
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();
  }

  protected void loadDetails(final String namespace, final String name)
  {
    if (detailsLoadJob != null)
    {
      detailsLoadJob.cancel();
    }

    Item root = Item.createItem();
    root.getChildren().add(Item.createNamespaceItem("Loading..."));
    detailsViewer.setInput(root);

    detailsLoadJob = new Job("Detail Loader")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        Map<Repository, Set<Version>> capabilitiesFromSimpleRepositories = P2Index.INSTANCE.lookupCapabilities(namespace, name);
        Map<Repository, Set<Version>> capabilitiesFromComposedRepositories = P2Index.INSTANCE
            .generateCapabilitiesFromComposedRepositories(capabilitiesFromSimpleRepositories);

        CollectionUtil.addAll(capabilitiesFromComposedRepositories, capabilitiesFromSimpleRepositories);
        Map<Version, Item> versionItems = new TreeMap<Version, Item>();
        for (Map.Entry<Repository, Set<Version>> entry : capabilitiesFromComposedRepositories.entrySet())
        {
          Repository key = entry.getKey();
          for (Version version : entry.getValue())
          {
            Item versionItem = versionItems.get(version);
            if (versionItem == null)
            {
              versionItem = Item.createVersion(version);
              versionItems.put(version, versionItem);
            }

            versionItem.getChildren().add(Item.createRepository(key));
          }
        }

        final Item input = Item.createItem();
        Item capabilityItem = Item.create(namespace, name);
        input.getChildren().add(capabilityItem);

        for (Item versionItem : versionItems.values())
        {
          versionItem.sort();
        }

        capabilityItem.getChildren().addAll(versionItems.values());
        ECollections.reverse(capabilityItem.getChildren());

        if (!monitor.isCanceled())
        {
          UIUtil.asyncExec(detailsViewer.getControl(), new Runnable()
          {
            public void run()
            {
              detailsViewer.setInput(input);
              detailsViewer.expandToLevel(2);
            }
          });
        }

        return Status.OK_STATUS;
      }
    };

    detailsLoadJob.setSystem(true);
    detailsLoadJob.schedule();
  }

  /**
   * @author Ed Merks
   */
  private static final class ItemFilter extends PatternFilter implements FilteredTreeWithoutWorkbench.ExpansionFilter
  {
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
      // Don't expand an item if that item directly matches the pattern, unless it's not a capability item.
      Item item = (Item)element;
      return !isLeafMatch(null, element) || !item.isCapability();
    }
  }

  /**
   * @author Ed Merks
   */
  private static class Item extends ItemProvider
  {
    private static final Comparator<String> STRING_COMPARATOR = CommonPlugin.INSTANCE.getComparator();

    private static final Comparator<Item> COMPARATOR = new Comparator<Item>()
    {
      public int compare(Item item1, Item item2)
      {
        return STRING_COMPARATOR.compare(item1.getText(), item2.getText());
      }
    };

    private static final Image VERSION_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/version");

    private static final Image NAMESPACE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/folder");

    private static final Image CAPABILITY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/capability");

    private static final Image REPOSITORY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/repository");

    private static final Image JAVA_PACKAGE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Package"));

    private static final Image BUNDLE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Plugin"));

    private static final Image FEATURE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Feature"));

    private String namespace;

    private String name;

    private String decoration;

    private boolean concrete;

    private boolean repository;

    private Item()
    {
    }

    private Item(String namespace)
    {
      super(namespace, NAMESPACE_IMAGE);
      this.namespace = namespace;
    }

    private Item(String namespace, SegmentSequence partialName)
    {
      super(URI.decode(partialName.lastSegment()), getImage(namespace, partialName.toString()));
      this.namespace = namespace;
      name = partialName.toString();
    }

    private Item(String namespace, String name)
    {
      super(namespace + "/" + URI.decode(name), getImage(namespace, name));
      this.namespace = namespace;
      this.name = name;
    }

    private Item(Version version)
    {
      super(version.toString(), VERSION_IMAGE);
    }

    private Item(Repository repository)
    {
      super(repository.getLocation().toString(), REPOSITORY_IMAGE);
      int capabilityCount = repository.getCapabilityCount();
      decoration = " " + capabilityCount + (capabilityCount == 1 ? " capability" : " capabilities");
      this.repository = true;
    }

    public String getNamespace()
    {
      return namespace;
    }

    public String getName()
    {
      return name;
    }

    public boolean isCapability()
    {
      return namespace != null && name != null;
    }

    public boolean isRepository()
    {
      return repository;
    }

    public void setConcrete()
    {
      setImage(ExtendedImageRegistry.INSTANCE.getImage(new CapabilityComposedImage(getImage())));
      concrete = true;
    }

    public boolean isConcrete()
    {
      return concrete;
    }

    public static Item createRepository(Repository repository)
    {
      Item item = new Item(repository);
      EList<Object> children = item.getChildren();
      for (Repository child : repository.getChildren())
      {
        children.add(createRepository(child));
      }

      return item;
    }

    public static Item createVersion(Version version)
    {
      return new Item(version);
    }

    public static Item create(String namespace, SegmentSequence partialName)
    {
      return new Item(namespace, partialName);
    }

    public static Item create(String namespace, String name)
    {
      return new Item(namespace, name);
    }

    public static Item createItem()
    {
      return new Item();
    }

    public static Item createNamespaceItem(String namespace)
    {
      return new Item(namespace);
    }

    @Override
    public Object getStyledText(Object object)
    {
      if (decoration != null)
      {
        org.eclipse.emf.edit.provider.StyledString styledLabel = new org.eclipse.emf.edit.provider.StyledString();
        styledLabel.append(getText());
        styledLabel.append(" ");
        styledLabel.append(decoration, org.eclipse.emf.edit.provider.StyledString.Style.DECORATIONS_STYLER);
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

    public void sort()
    {
      EList<Item> children = getItems();
      ECollections.sort(children, COMPARATOR);
      for (Item child : children)
      {
        child.sort();
      }
    }

    private static Image getImage(String namespace, String name)
    {
      if ("java.package".equals(namespace))
      {
        return JAVA_PACKAGE_IMAGE;
      }

      if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
      {
        if (name.endsWith(Requirement.FEATURE_SUFFIX))
        {
          return FEATURE_IMAGE;
        }

        return BUNDLE_IMAGE;
      }

      if ("org.eclipse.update.feature".equals(namespace))
      {
        return FEATURE_IMAGE;
      }

      if ("osgi.fragment".equals(namespace) || "osgi.bundle".equals(namespace))
      {
        return BUNDLE_IMAGE;
      }

      return CAPABILITY_IMAGE;
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class CapabilityComposedImage extends ComposedImage
  {
    private static final Image CONCRETE_CAPABILITY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("ovr16/concrete_capability");

    private CapabilityComposedImage(Object image)
    {
      super(Arrays.asList(new Object[] { image, CONCRETE_CAPABILITY_IMAGE }));
    }

    @Override
    public List<ComposedImage.Point> getDrawPoints(Size size)
    {
      List<ComposedImage.Point> result = super.getDrawPoints(size);
      if (result.size() > 1)
      {
        result.get(1).x += 9;
        result.get(1).y += 9;
      }

      return result;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ToolTipLabelProvider extends DecoratingColumLabelProvider.StyledLabelProvider
  {
    public ToolTipLabelProvider(AdapterFactory adapterFactory, Viewer viewer)
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
    }

    @Override
    public String getToolTipText(Object element)
    {
      Item item = (Item)element;
      if (item.isCapability())
      {
        StringBuilder result = new StringBuilder();
        result.append(DiagnosticDecorator
            .enquote("<img src='" + ImageURIRegistry.INSTANCE.getImageURI(ExtendedImageRegistry.INSTANCE.getImage(item.getImage())) + "'/> "));
        result.append("&nbsp;");
        result.append(item.getNamespace());
        result.append('/');
        result.append(URI.decode(item.getName()));
        return result.toString();
      }

      return null;
    }
  }

  /**
   * Returns the instance for this workbench window, if there is one.
   */
  public static SearchEclipseRepositoryDialog getFor(IWorkbenchWindow workbenchWindow)
  {
    return DockableDialog.getFor(SearchEclipseRepositoryDialog.class, workbenchWindow);
  }

  /**
   * Close the instance for this workbench window, if there is one.
   */
  public static void closeFor(IWorkbenchWindow workbenchWindow)
  {
    DockableDialog.closeFor(SearchEclipseRepositoryDialog.class, workbenchWindow);
  }

  /**
   * Reopen or create the instance for this workbench window.
   */
  public static SearchEclipseRepositoryDialog openFor(final IWorkbenchWindow workbenchWindow)
  {
    Factory<SearchEclipseRepositoryDialog> factory = new Factory<SearchEclipseRepositoryDialog>()
    {
      public SearchEclipseRepositoryDialog create(IWorkbenchWindow workbenchWindow)
      {
        return new SearchEclipseRepositoryDialog(workbenchWindow.getShell());
      }
    };

    return DockableDialog.openFor(SearchEclipseRepositoryDialog.class, factory, workbenchWindow);
  }
}

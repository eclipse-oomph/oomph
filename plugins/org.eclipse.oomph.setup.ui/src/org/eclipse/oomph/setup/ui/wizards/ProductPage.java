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

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.util.CatalogManager;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.p2.internal.ui.P2ContentProvider;
import org.eclipse.oomph.p2.internal.ui.P2LabelProvider;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.provider.CatalogSelectionItemProvider;
import org.eclipse.oomph.setup.provider.IndexItemProvider;
import org.eclipse.oomph.setup.provider.InstallationItemProvider;
import org.eclipse.oomph.setup.provider.ProductCatalogItemProvider;
import org.eclipse.oomph.setup.provider.ProductItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.PersistentButton;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class ProductPage extends SetupWizardPage
{
  private static final boolean SHOW_BUNDLE_POOL_UI = PropertiesUtil.getProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION) == null;

  private static final Product NO_PRODUCT = createNoProduct();

  private ComposedAdapterFactory adapterFactory;

  private CatalogSelector catalogSelector;

  private TreeViewer productViewer;

  private Label idLabel;

  private Text idText;

  private Label nameLabel;

  private Text nameText;

  private Label versionLabel;

  private ComboViewer versionComboViewer;

  private Button poolButton;

  private ComboViewer poolComboViewer;

  private Button managePoolsButton;

  private BundlePool currentBundlePool;

  private boolean currentBundlePoolChanging;

  public ProductPage()
  {
    super("ProductPage");
    setTitle("Product");
    setDescription("Select the product and choose the version you want to install.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    adapterFactory = new ComposedAdapterFactory(getAdapterFactory());
    adapterFactory.insertAdapterFactory(new ItemProviderAdapterFactory());
    EMFUtil.replaceReflectiveItemProvider(adapterFactory);

    ResourceSet resourceSet = getResourceSet();
    resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(new AdapterFactoryEditingDomain(adapterFactory, null, resourceSet)));

    final CatalogManager catalogManager = getCatalogManager();

    catalogSelector = new CatalogSelector(catalogManager, true);

    GridLayout mainLayout = new GridLayout();
    mainLayout.marginHeight = 0;
    mainLayout.marginBottom = 5;

    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(mainLayout);

    GridLayout filterLayout = new GridLayout(2, false);
    filterLayout.marginWidth = 0;
    filterLayout.marginHeight = 0;

    Composite filterComposite = new Composite(mainComposite, SWT.NONE);
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

    FilteredTree filteredTree = new FilteredTree(mainComposite, SWT.BORDER, new PatternFilter(), true);
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);

    productViewer = filteredTree.getViewer();
    productViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    productViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);

        getShell().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            if (productViewer.getExpandedElements().length == 0)
            {
              final Object[] elements = getElements(productViewer.getInput());
              if (elements.length > 0)
              {
                productViewer.expandToLevel(elements[0], 1);
                if (productViewer.getSelection().isEmpty())
                {
                  EMap<Product, ProductVersion> defaultProductVersions = catalogManager.getSelection().getDefaultProductVersions();
                  if (!defaultProductVersions.isEmpty())
                  {
                    Product defaultProduct = defaultProductVersions.get(0).getKey();
                    viewer.setSelection(new StructuredSelection(defaultProduct), true);
                  }
                }
              }
            }
          }
        });
      }
    });

    final Tree productTree = productViewer.getTree();
    productTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    CatalogSelection selection = catalogManager.getSelection();
    productViewer.setInput(selection);

    GridLayout installationPaneLayout = new GridLayout(2, false);
    installationPaneLayout.marginTop = 5;
    installationPaneLayout.marginWidth = 0;
    installationPaneLayout.marginHeight = 0;

    Composite installationPane = new Composite(mainComposite, SWT.NONE);
    installationPane.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    installationPane.setLayout(installationPaneLayout);

    idLabel = new Label(installationPane, SWT.NONE);
    idLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    idLabel.setText("Product ID:");

    idText = new Text(installationPane, SWT.READ_ONLY);
    idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    nameLabel = new Label(installationPane, SWT.NONE);
    nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    nameLabel.setText("Product Name:");

    nameText = new Text(installationPane, SWT.READ_ONLY);
    nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    versionLabel = new Label(installationPane, SWT.NONE);
    versionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    versionLabel.setText("Product Version:");

    versionComboViewer = new ComboViewer(installationPane, SWT.READ_ONLY);
    versionComboViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    versionComboViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public Object[] getElements(Object object)
      {
        if (object != NO_PRODUCT)
        {
          return ((Product)object).getVersions().toArray();
        }

        return super.getElements(object);
      }
    });

    Combo versionCombo = versionComboViewer.getCombo();
    versionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    if (SHOW_BUNDLE_POOL_UI)
    {
      initBundlePool();

      poolButton = new PersistentButton(installationPane, SWT.CHECK, getDialogSettings(), "useBundlePool", true);
      poolButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
      poolButton.setText("Bundle Pool:");

      poolButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          if (poolButton.getSelection())
          {
            IStructuredSelection selection = (IStructuredSelection)poolComboViewer.getSelection();
            BundlePool pool = (BundlePool)selection.getFirstElement();
            if (pool != null)
            {
              setCurrentBundlePool(pool);
            }
            else
            {
              initBundlePool();
            }
          }
          else
          {
            setCurrentBundlePool(null);
          }

          updateInstallationPane(false);
        }
      });

      GridLayout poolLayout = new GridLayout(2, false);
      poolLayout.marginWidth = 0;
      poolLayout.marginHeight = 0;

      Composite poolComposite = new Composite(installationPane, SWT.NONE);
      poolComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      poolComposite.setLayout(poolLayout);

      P2LabelProvider labelProvider = new P2LabelProvider();
      labelProvider.setAbsolutePools(true);

      poolComboViewer = new ComboViewer(poolComposite, SWT.READ_ONLY);
      poolComboViewer.setLabelProvider(labelProvider);
      poolComboViewer.setContentProvider(new P2ContentProvider.AllBundlePools());
      poolComboViewer.setInput(P2Util.getAgentManager());
      poolComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          if (currentBundlePoolChanging)
          {
            return;
          }

          if (poolButton.getSelection())
          {
            IStructuredSelection selection = (IStructuredSelection)poolComboViewer.getSelection();
            BundlePool pool = (BundlePool)selection.getFirstElement();
            if (pool != currentBundlePool)
            {
              setCurrentBundlePool(pool);
              updateInstallationPane(false);
            }
          }
        }
      });

      Combo poolCombo = poolComboViewer.getCombo();
      poolCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

      managePoolsButton = new Button(poolComposite, SWT.PUSH);
      managePoolsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
      managePoolsButton.setText("Manage Bundle Pools...");
      managePoolsButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          manageBundlePools();
        }
      });
    }

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
        productViewer.collapseAll();
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

    productViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)productViewer.getSelection();
        Object element = selection.getFirstElement();
        if (element instanceof Product)
        {
          if (isPageComplete())
          {
            advanceToNextPage();
          }

          return;
        }

        boolean expanded = productViewer.getExpandedState(element);
        productViewer.setExpandedState(element, !expanded);
      }
    });

    productViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        updateInstallationPane(false);
      }
    });

    versionComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        ProductVersion version = getSelectedProductVersion();
        if (version != null)
        {
          saveProductVersionSelection(version);
        }
      }
    });

    updateInstallationPane(true);
    return mainComposite;
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      ProductVersion productVersion = getSelectedProductVersion();
      getWizard().setSetupContext(SetupContext.create(getResourceSet(), productVersion));
    }
  }

  private void updateInstallationPane(boolean initial)
  {
    Product product = getSelectedProduct();
    if (product == null)
    {
      product = NO_PRODUCT;
    }

    idText.setText(safe(product.getName()));
    nameText.setText(safe(product.getLabel()));
    versionComboViewer.setInput(product);

    ProductVersion version = catalogSelector.getCatalogManager().getSelection().getDefaultProductVersions().get(product);
    if (version == null)
    {
      EList<ProductVersion> versions = product.getVersions();
      if (!versions.isEmpty())
      {
        version = versions.get(0);
        saveProductVersionSelection(version);
      }
    }

    if (version != null)
    {
      versionComboViewer.setSelection(new StructuredSelection(version));
    }

    boolean productSelected = product != NO_PRODUCT;
    String error = productSelected ? null : "Select a product from the catalogs and choose the product version.";

    idLabel.setEnabled(productSelected);
    idText.setEnabled(productSelected);
    nameLabel.setEnabled(productSelected);
    nameText.setEnabled(productSelected);
    versionLabel.setEnabled(productSelected);
    versionComboViewer.getControl().setEnabled(productSelected);

    if (poolButton != null)
    {
      poolButton.setEnabled(productSelected);

      boolean poolNeeded = productSelected && poolButton.getSelection();
      poolComboViewer.getControl().setEnabled(poolNeeded);
      managePoolsButton.setEnabled(poolNeeded);

      currentBundlePoolChanging = true;
      if (poolNeeded)
      {
        if (currentBundlePool != null)
        {
          poolComboViewer.setSelection(new StructuredSelection(currentBundlePool));
        }
        else
        {
          if (error == null)
          {
            error = "Select a bundle pool or disable the use of a bundle pool.";
          }

          poolComboViewer.setSelection(StructuredSelection.EMPTY);
        }
      }
      else
      {
        poolComboViewer.setSelection(StructuredSelection.EMPTY);
      }

      currentBundlePoolChanging = false;
    }

    if (!initial)
    {
      setErrorMessage(error);
      setPageComplete(error == null);
    }
  }

  private void setCurrentBundlePool(BundlePool pool)
  {
    if (pool != null)
    {
      P2Util.getAgentManager().setDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName(), pool);
      System.setProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION, pool.getLocation().getAbsolutePath());
    }
    else
    {
      System.clearProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION);
    }

    currentBundlePool = pool;
  }

  private void initBundlePool()
  {
    BundlePool pool = P2Util.getAgentManager().getDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName());
    setCurrentBundlePool(pool);
  }

  private void manageBundlePools()
  {
    AgentManagerDialog dialog = new AgentManagerDialog(getShell())
    {
      @Override
      protected void createButtonsForButtonBar(Composite parent)
      {
        super.createButtonsForButtonBar(parent);
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(false);
        }
      }

      @Override
      protected void elementChanged(Object element)
      {
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(element instanceof BundlePool);
        }
      }
    };

    IStructuredSelection selection = (IStructuredSelection)poolComboViewer.getSelection();
    BundlePool pool = (BundlePool)selection.getFirstElement();
    if (pool != null)
    {
      dialog.setSelectedElement(pool);
    }

    if (dialog.open() == AgentManagerDialog.OK)
    {
      pool = (BundlePool)dialog.getSelectedElement();
      poolComboViewer.setSelection(pool == null ? StructuredSelection.EMPTY : new StructuredSelection(pool));
    }
  }

  private Product getSelectedProduct()
  {
    IStructuredSelection selection = (IStructuredSelection)productViewer.getSelection();
    Object element = selection.getFirstElement();
    if (element instanceof Product)
    {
      return (Product)element;
    }

    return null;
  }

  private ProductVersion getSelectedProductVersion()
  {
    IStructuredSelection selection = (IStructuredSelection)versionComboViewer.getSelection();
    Object element = selection.getFirstElement();
    if (element instanceof ProductVersion)
    {
      return (ProductVersion)element;
    }

    return null;
  }

  private void saveProductVersionSelection(ProductVersion version)
  {
    CatalogManager catalogManager = catalogSelector.getCatalogManager();
    EMap<Product, ProductVersion> defaultProductVersions = catalogManager.getSelection().getDefaultProductVersions();
    Product product = version.getProduct();
    defaultProductVersions.put(product, version);
    defaultProductVersions.move(0, defaultProductVersions.indexOfKey(product));
    catalogManager.saveSelection();
  }

  @Override
  public void dispose()
  {
    super.dispose();

    adapterFactory.dispose();
  }

  private static Product createNoProduct()
  {
    Product product = SetupFactory.eINSTANCE.createProduct();
    product.setName("<no product selected>");
    product.setLabel(product.getName());
    return product;
  }

  private static String safe(String string)
  {
    if (string == null)
    {
      return "";
    }

    return string;
  }

  /**
   * @author Eike Stepper
   */
  private static final class ItemProviderAdapterFactory extends SetupItemProviderAdapterFactory implements SetupPackage.Literals
  {
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
              childrenFeatures.add(CATALOG_SELECTION__PRODUCT_CATALOGS);
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
              childrenFeatures.add(INDEX__PRODUCT_CATALOGS);
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
    public Adapter createProductCatalogAdapter()
    {
      if (productCatalogItemProvider == null)
      {
        productCatalogItemProvider = new ProductCatalogItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(PRODUCT_CATALOG__PRODUCTS);
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

      return productCatalogItemProvider;
    }

    @Override
    public Adapter createProductAdapter()
    {
      if (productItemProvider == null)
      {
        productItemProvider = new ProductItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
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

      return productItemProvider;
    }

    @Override
    public Adapter createInstallationAdapter()
    {
      if (installationItemProvider == null)
      {
        installationItemProvider = new InstallationItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(INSTALLATION__PRODUCT_VERSION);
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

      return installationItemProvider;
    }
  }
}

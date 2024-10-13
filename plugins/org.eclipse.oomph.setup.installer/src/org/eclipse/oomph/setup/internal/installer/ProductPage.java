/*
 * Copyright (c) 2015-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.jreinfo.ui.JREInfoUIPlugin;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.p2.internal.ui.P2ContentProvider;
import org.eclipse.oomph.p2.internal.ui.P2LabelProvider;
import org.eclipse.oomph.p2.internal.ui.P2UIPlugin;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.provider.CatalogSelectionItemProvider;
import org.eclipse.oomph.setup.provider.IndexItemProvider;
import org.eclipse.oomph.setup.provider.InstallationItemProvider;
import org.eclipse.oomph.setup.provider.ProductCatalogItemProvider;
import org.eclipse.oomph.setup.provider.ProductItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.JREDownloadHandler;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.CatalogSelector;
import org.eclipse.oomph.setup.ui.wizards.ConfigurationProcessor;
import org.eclipse.oomph.setup.ui.wizards.MarketPlaceListingProcessor;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.IndexLoader;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardPage;
import org.eclipse.oomph.ui.FilteredTreeWithoutWorkbench;
import org.eclipse.oomph.ui.PersistentButton;
import org.eclipse.oomph.ui.PersistentButton.DialogSettingsPersistence;
import org.eclipse.oomph.ui.StatusDialog;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.DragAndDropFeedback;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DelegatingStyledCellLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class ProductPage extends SetupWizardPage
{
  public static final String PAGE_NAME = "ProductPage"; //$NON-NLS-1$

  private static final Product NO_PRODUCT = createNoProduct();

  private static final Pattern RELEASE_LABEL_PATTERN = Pattern.compile(".*\\(([^)]*)\\)[^)]*"); //$NON-NLS-1$

  private static final boolean OS_CHOOSE = PropertiesUtil.isProperty("oomph.setup.os.choose"); //$NON-NLS-1$

  private static final Pattern OSGI_REQUIRED_JAVA_VERSION_VM_ARGUMENT_PATTERN = Pattern
      .compile("-Dosgi\\.requiredJavaVersion=([1-9][0-9]+(\\.[0-9]+(\\.[0-9]+)?)?)"); //$NON-NLS-1$

  private final SelectionMemento selectionMemento;

  private ComposedAdapterFactory adapterFactory;

  private CatalogSelector catalogSelector;

  private TreeViewer productViewer;

  private Browser descriptionBrowser;

  private DescriptionViewer descriptionViewer;

  private Label versionLabel;

  private ComboViewer versionComboViewer;

  private ToolButton bitness32Button;

  private ToolButton bitness64Button;

  private JREController javaController;

  private Label javaLabel;

  private ComboViewer javaViewer;

  private ToolButton javaButton;

  private Button poolButton;

  private ComboViewer poolComboViewer;

  private ToolButton managePoolsButton;

  private BundlePool currentBundlePool;

  private boolean currentBundlePoolChanging;

  private ProjectPage.ConfigurationListener configurationListener;

  private SetupTransferSupport.DropListener dropListener;

  public ProductPage(SelectionMemento selectionMemento)
  {
    super(PAGE_NAME);
    this.selectionMemento = selectionMemento;
    setTitle(Messages.ProductPage_title);
    setDescription(Messages.ProductPage_description);
  }

  @Override
  public void dispose()
  {
    super.dispose();

    adapterFactory.dispose();
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    adapterFactory = new ComposedAdapterFactory(getAdapterFactory());
    adapterFactory.insertAdapterFactory(new ItemProviderAdapterFactory());
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
          @Override
          public void run()
          {
            productViewer.setSelection(new StructuredSelection(affectedObjects.toArray()), true);
          }
        });
      }
    }, resourceSet);

    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(UIUtil.createGridLayout(1));

    Control productSash = createProductSash(mainComposite, editingDomain);
    productSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    Composite lowerComposite = new Composite(mainComposite, SWT.NONE);
    lowerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    lowerComposite.setLayout(UIUtil.createGridLayout(4));

    versionLabel = new Label(lowerComposite, SWT.NONE);
    versionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    versionLabel.setText(Messages.ProductPage_ProductVersion_label);
    AccessUtil.setKey(versionLabel, "productVersion"); //$NON-NLS-1$

    versionComboViewer = new ComboViewer(lowerComposite, SWT.READ_ONLY);
    versionComboViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    versionComboViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public Object[] getElements(Object object)
      {
        return getValidProductVersions((Product)object, null, getWizard().getOS()).toArray();
      }
    });

    versionComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        updateProductVersionDetails(false);
      }
    });

    versionComboViewer.setInput(NO_PRODUCT);
    final Combo versionCombo = versionComboViewer.getCombo();
    versionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    AccessUtil.setKey(versionCombo, "versionChoice"); //$NON-NLS-1$

    if (OS_CHOOSE)
    {
      ComboViewer osComboViewer = new ComboViewer(lowerComposite, SWT.READ_ONLY);
      osComboViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
      osComboViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
      {
        @Override
        public Object[] getElements(Object object)
        {
          return OS.INSTANCES.toArray();
        }
      });

      osComboViewer.setInput(OS.INSTANCES);

      osComboViewer.setSelection(new StructuredSelection(getWizard().getOS()));

      osComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          OS os = (OS)selection.getFirstElement();
          getWizard().setOS(os);
          javaController.setBitness(os.getBitness());
          productViewer.refresh();
        }
      });

      new Label(lowerComposite, SWT.NONE);
    }
    else if (InstallerUI.BITNESS_CHOOSE && JREManager.BITNESS_CHANGEABLE)
    {
      bitness32Button = new ToolButton(lowerComposite, SWT.RADIO, SetupUIPlugin.INSTANCE.getSWTImage("32bit.png"), true); //$NON-NLS-1$
      bitness32Button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
      bitness32Button.setSelection(false);
      bitness32Button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          bitness32Button.setSelection(true);
          bitness64Button.setSelection(false);
          javaController.setBitness(32);
        }
      });

      bitness64Button = new ToolButton(lowerComposite, SWT.RADIO, SetupUIPlugin.INSTANCE.getSWTImage("64bit.png"), true); //$NON-NLS-1$
      bitness64Button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
      bitness64Button.setSelection(true);
      bitness64Button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          bitness32Button.setSelection(false);
          bitness64Button.setSelection(true);
          javaController.setBitness(64);
        }
      });
    }
    else
    {
      new Label(lowerComposite, SWT.NONE);
      new Label(lowerComposite, SWT.NONE);
    }

    javaLabel = new Label(lowerComposite, SWT.RIGHT);
    javaLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    javaLabel.setText(Messages.ProductPage_JVM_label);

    javaViewer = new ComboViewer(lowerComposite, SWT.READ_ONLY);
    javaViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    javaViewer.setLabelProvider(new LabelProvider());
    javaViewer.setContentProvider(new ArrayContentProvider());

    javaViewer.setInput(Collections.singletonList(new JRE(new File(""), 0, 0, 0, 0, false, 0))); //$NON-NLS-1$

    javaButton = new ToolButton(lowerComposite, SWT.PUSH, JREInfoUIPlugin.INSTANCE.getSWTImage("jre"), true); //$NON-NLS-1$
    javaButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    javaButton.setToolTipText(Messages.ProductPage_ManageVirtualMachanges_message);
    javaButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        javaController.configureJREs();
      }
    });

    JREDownloadHandler downloadHandler = new JREDownloadHandler()
    {
      @Override
      protected Product getProduct()
      {
        return getSelectedProduct();
      }
    };

    javaController = new AugmentedJREController(javaLabel, javaViewer, downloadHandler)
    {
      @Override
      protected void modelEmpty(boolean empty)
      {
        super.modelEmpty(empty);
        setPageComplete(!empty);
      }

      @Override
      protected String getArch()
      {
        return getWizard().getOS().getOsgiArch();
      }

      @Override
      protected void jreChanged(JRE jre)
      {
        SetupWizard wizard = getWizard();
        wizard.setVMPath(getVMOption(jre));
        wizard.setOS(wizard.getOS().getForBitness(getBitness()));
        updateSetupContext(wizard.getSetupContext(), jre);
      }

      @Override
      protected void setLabel(String text)
      {
        super.setLabel(text + ":"); //$NON-NLS-1$
      }
    };

    if (InstallerUI.SHOW_BUNDLE_POOL_UI)
    {
      DialogSettingsPersistence useBundlePool = new DialogSettingsPersistence(getDialogSettings(), "useBundlePool"); //$NON-NLS-1$
      poolButton = PersistentButton.create(lowerComposite, SWT.CHECK | SWT.RIGHT, true, useBundlePool);
      AccessUtil.setKey(poolButton, "pools"); //$NON-NLS-1$
      poolButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
      poolButton.setText(Messages.ProductPage_BundlePool_label);

      if (poolButton.getSelection())
      {
        initBundlePool();
      }
      else
      {
        setCurrentBundlePool(null);
      }

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

          updateProductDetails(false);
        }
      });

      // Composite poolComposite = new Composite(lowerComposite, SWT.NONE);
      // poolComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      // poolComposite.setLayout(UIUtil.createGridLayout(2));

      P2LabelProvider labelProvider = new P2LabelProvider();
      labelProvider.setAbsolutePools(true);

      poolComboViewer = new ComboViewer(lowerComposite, SWT.READ_ONLY);
      poolComboViewer.setLabelProvider(labelProvider);
      poolComboViewer.setContentProvider(new P2ContentProvider.AllBundlePools());
      poolComboViewer.setInput(P2Util.getAgentManager());
      poolComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
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
              updateProductDetails(false);
            }
          }
        }
      });

      Combo poolCombo = poolComboViewer.getCombo();
      poolCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      AccessUtil.setKey(poolCombo, "poolChoice"); //$NON-NLS-1$

      managePoolsButton = new ToolButton(lowerComposite, SWT.PUSH, P2UIPlugin.INSTANCE.getSWTImage("obj16/bundlePool"), true); //$NON-NLS-1$
      managePoolsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
      managePoolsButton.setToolTipText(Messages.ProductPage_ManageBundlePools_message);
      managePoolsButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          manageBundlePools();
        }
      });
      AccessUtil.setKey(managePoolsButton, "managePools"); //$NON-NLS-1$
    }

    final CatalogManager catalogManager = catalogSelector.getCatalogManager();
    versionComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        ProductVersion version = getSelectedProductVersion();
        if (version != null)
        {
          String requiredJavaVersion = getRequiredJavaVersion(version, getWizard().getSetupContext());
          javaController.setJavaVersion(requiredJavaVersion);

          saveProductVersionSelection(catalogManager, version);
        }

        versionCombo.setToolTipText(getToolTipText(version));
      }
    });

    updateProductDetails(true);

    return mainComposite;
  }

  public static String getRequiredJavaVersion(ProductVersion productVersion, SetupContext setupContext)
  {
    Installation installation = setupContext.getInstallation();
    String requiredJavaVersion = installation == null ? null : getRequiredJavaVersion(installation.getSetupTasks());
    if (requiredJavaVersion == null)
    {
      Workspace workspace = setupContext.getWorkspace();
      requiredJavaVersion = workspace == null ? null : getRequiredJavaVersion(workspace.getSetupTasks());
      if (requiredJavaVersion == null)
      {
        requiredJavaVersion = productVersion.getRequiredJavaVersion();
      }
    }

    return requiredJavaVersion;
  }

  private static String getRequiredJavaVersion(List<? extends SetupTask> setupTasks)
  {
    for (SetupTask setupTask : setupTasks)
    {
      if (setupTask instanceof EclipseIniTask)
      {
        EclipseIniTask eclipseIniTask = (EclipseIniTask)setupTask;
        if (eclipseIniTask.isVm())
        {
          String vmArgument = eclipseIniTask.getOption() + eclipseIniTask.getValue();
          Matcher matcher = OSGI_REQUIRED_JAVA_VERSION_VM_ARGUMENT_PATTERN.matcher(vmArgument);
          if (matcher.matches())
          {
            return matcher.group(1);
          }
        }
      }
    }

    return null;
  }

  private SashForm createProductSash(Composite composite, final AdapterFactoryEditingDomain editingDomain)
  {
    SashForm sashForm = new SashForm(composite, SWT.SMOOTH | SWT.VERTICAL);

    Composite treeComposite = new Composite(sashForm, SWT.NONE);
    treeComposite.setLayout(UIUtil.createGridLayout(1));

    final CatalogManager catalogManager = getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, true);

    Composite filterComposite = new Composite(treeComposite, SWT.NONE);
    filterComposite.setLayout(UIUtil.createGridLayout(2));
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(UIUtil.createGridLayout(1));
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    ToolBar filterToolBar = new ToolBar(filterComposite, SWT.FLAT | SWT.RIGHT);

    final ToolItem addProductButton = new ToolItem(filterToolBar, SWT.NONE);
    addProductButton.setToolTipText(Messages.ProductPage_AddUserProducts_message);
    addProductButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("add_project")); //$NON-NLS-1$
    addProductButton.setEnabled(false);
    AccessUtil.setKey(addProductButton, "addProduct"); //$NON-NLS-1$

    final Set<ProductCatalog> userProductCatalogs = new HashSet<ProductCatalog>();
    addProductButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        ResourceDialog dialog = new AddUserProductDialog(getShell(), userProductCatalogs, catalogSelector, editingDomain);
        dialog.open();
      }
    });

    final ToolItem removeProductButton = new ToolItem(filterToolBar, SWT.NONE);
    removeProductButton.setToolTipText(Messages.ProductPage_RemoveUserProduct_message);
    removeProductButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("remove_project")); //$NON-NLS-1$
    removeProductButton.setEnabled(false);
    AccessUtil.setKey(removeProductButton, "removeProduct"); //$NON-NLS-1$

    final List<Product> userProducts = new ArrayList<Product>();
    final SelectionAdapter removeProductSelectionAdapter = new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent event)
      {
        List<ProductCatalog> parents = new UniqueEList<ProductCatalog>();
        for (Product product : userProducts)
        {
          ProductCatalog parentProductCatalog = product.getProductCatalog();
          parentProductCatalog.getProducts().remove(product);
          parents.add(parentProductCatalog);
        }

        for (ProductCatalog parent : parents)
        {
          BaseUtil.saveEObject(parent);
        }

        productViewer.setSelection(new StructuredSelection(parents));
      }
    };
    removeProductButton.addSelectionListener(removeProductSelectionAdapter);

    final ToolItem collapseAllButton = new ToolItem(filterToolBar, SWT.NONE);
    collapseAllButton.setToolTipText(Messages.ProductPage_CollapseAll_message);
    collapseAllButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("collapse-all")); //$NON-NLS-1$
    AccessUtil.setKey(collapseAllButton, "collapse"); //$NON-NLS-1$

    ToolItem detailsButton = ProjectPage.createDetailsButton(filterToolBar, getDialogSettings(), "product-location-details"); //$NON-NLS-1$

    configurationListener = new ProjectPage.ConfigurationListener(getWizard(), catalogManager, filterToolBar);
    getWizard().addConfigurationListener(configurationListener);

    final ToolItem catalogsButton = new ToolItem(filterToolBar, SWT.DROP_DOWN);
    catalogsButton.setToolTipText(Messages.ProductPage_SelectCatalogs_message);
    catalogsButton.setImage(SetupUIPlugin.INSTANCE.getSWTImage("catalogs")); //$NON-NLS-1$
    catalogSelector.configure(getWizard(), catalogsButton, true);
    AccessUtil.setKey(catalogsButton, "catalogs"); //$NON-NLS-1$

    final FilteredTreeWithoutWorkbench filteredTree = new FilteredTreeWithoutWorkbench(treeComposite, SWT.BORDER);
    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);
    AccessUtil.setKey(filteredTree.getFilterControl(), "filter"); //$NON-NLS-1$
    AccessUtil.setKey(filteredTree.getViewer().getTree(), "productTree"); //$NON-NLS-1$
    addHelpCallout(filteredTree.getViewer().getTree(), 1);

    productViewer = filteredTree.getViewer();
    productViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ProjectPage.LocationDecoratingLabelProvider(adapterFactory, null, detailsButton)
    {
      @Override
      public String getToolTipText(Object element)
      {
        return null;
      }
    }));

    productViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      private final AtomicBoolean selectionMementoTried = new AtomicBoolean();

      private boolean applySelectionMemento()
      {
        URI uri = selectionMemento.getProductVersion();
        if (uri != null)
        {
          ResourceSet resourceSet = getResourceSet();

          EObject object = resourceSet.getEObject(uri, true);
          if (object instanceof ProductVersion)
          {
            ProductVersion productVersion = (ProductVersion)object;
            Product product = productVersion.getProduct();
            productViewer.expandToLevel(product, 1);
            productViewer.setSelection(new StructuredSelection(product));
            versionComboViewer.setSelection(new StructuredSelection(productVersion));
            updateProductDetails(false);
            gotoNextPage();
            return true;
          }
        }

        return false;
      }

      @Override
      public boolean hasChildren(Object object)
      {
        return getChildren(object).length != 0;
      }

      @Override
      public Object[] getChildren(Object object)
      {
        Object[] children = super.getChildren(object);
        List<Object> filteredChildren = new ArrayList<>();
        for (Object child : children)
        {
          if (!(child instanceof Product) || !getValidProductVersions((Product)child, null, getWizard().getOS()).isEmpty())
          {
            filteredChildren.add(child);
          }
        }

        return filteredChildren.toArray();
      }

      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);

        if (notification.getFeature() == SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS)
        {
          getShell().getDisplay().asyncExec(new Runnable()
          {
            @Override
            public void run()
            {
              if (!selectionMementoTried.getAndSet(true))
              {
                if (applySelectionMemento())
                {
                  return;
                }
              }

              if (productViewer.getExpandedElements().length == 0)
              {
                Object[] elements = getElements(productViewer.getInput());
                if (elements.length > 0)
                {
                  productViewer.expandToLevel(elements[0], 1);
                  if (productViewer.getSelection().isEmpty())
                  {
                    EMap<Product, ProductVersion> defaultProductVersions = catalogManager.getSelection().getDefaultProductVersions();
                    if (!defaultProductVersions.isEmpty())
                    {
                      Product defaultProduct = defaultProductVersions.get(0).getKey();
                      productViewer.setSelection(new StructuredSelection(defaultProduct), true);
                      return;
                    }
                  }

                  productViewer.setSelection(new StructuredSelection(elements[0]));
                  setErrorMessage(null);
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
    productViewer.addDropSupport(dndOperations, transfers, new ProjectPage.URIDropAdapter(editingDomain, productViewer));

    final Tree productTree = productViewer.getTree();
    productTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite descriptionComposite = new Composite(sashForm, SWT.BORDER);
    descriptionComposite.setLayout(new FillLayout());
    if (UIUtil.isBrowserAvailable())
    {
      descriptionBrowser = new Browser(descriptionComposite, SWT.NONE);
      descriptionBrowser.addLocationListener(new LocationAdapter()
      {
        @Override
        public void changing(LocationEvent event)
        {
          if (!"about:blank".equals(event.location)) //$NON-NLS-1$
          {
            OS.INSTANCE.openSystemBrowser(event.location);
            event.doit = false;
          }
        }
      });

      AccessUtil.setKey(descriptionBrowser, "description"); //$NON-NLS-1$
    }
    else
    {
      descriptionComposite.setForeground(productTree.getForeground());
      descriptionComposite.setBackground(productTree.getBackground());
      descriptionViewer = new DescriptionViewer(descriptionComposite, productTree.getFont());
    }

    sashForm.setWeights(new int[] { 14, 5 });

    final CatalogSelection selection = catalogManager.getSelection();
    productViewer.setInput(selection);

    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        productViewer.collapseAll();
      }
    });

    productViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)productViewer.getSelection();
        Object element = selection.getFirstElement();
        if (element instanceof Product)
        {
          if (isPageComplete())
          {
            gotoNextPage();
          }

          return;
        }

        boolean expanded = productViewer.getExpandedState(element);
        productViewer.setExpandedState(element, !expanded);
      }
    });

    productViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        userProductCatalogs.clear();
        userProducts.clear();

        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        for (Object value : selection.toArray())
        {
          if (value instanceof Product)
          {
            Product product = (Product)value;
            ProductCatalog productCatalog = product.getProductCatalog();
            if (ConfigurationProcessor.isUserProductCatalog(productCatalog))
            {
              userProductCatalogs.add(productCatalog);
              userProducts.add(product);
            }
          }
          else if (value instanceof ProductCatalog)
          {
            ProductCatalog productCatalog = (ProductCatalog)value;
            if (ConfigurationProcessor.isUserProductCatalog(productCatalog))
            {
              userProductCatalogs.add(productCatalog);
            }
          }
        }

        removeProductButton.setEnabled(!userProducts.isEmpty());

        boolean hasUserProductCatalogs = false;
        for (Scope scope : catalogSelector.getCatalogs())
        {
          if (ConfigurationProcessor.isUserProductCatalog(scope))
          {
            hasUserProductCatalogs = true;
            break;
          }
        }

        addProductButton.setEnabled(hasUserProductCatalogs);

        updateProductDetails(false);
      }
    });

    dropListener = new SetupTransferSupport.DropListener()
    {
      @Override
      public void resourcesDropped(Collection<? extends Resource> resources)
      {
        SetupWizard setupWizard = getWizard();
        setupWizard.setConfigurationResources(resources);

        MarketPlaceListingProcessor marketPlaceListingProcessor = new MarketPlaceListingProcessor(setupWizard);
        if (marketPlaceListingProcessor.isMarketPlaceListing())
        {
          marketPlaceListingProcessor.processMarketPlaceListing();
          IStatus status = marketPlaceListingProcessor.getStatus();
          if (!status.isOK())
          {
            new StatusDialog(getShell(), Messages.ProductPage_MPCProblems_title, null, status, Diagnostic.ERROR).open();
          }

          return;
        }

        ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(getWizard())
        {
          @Override
          protected boolean applyEmptyProductVersion()
          {
            applyInstallation();
            updateSetupContext();
            return true;
          }

          @Override
          protected boolean applyProductVersion(ProductVersion productVersion)
          {
            applyInstallation();

            // Ensure that the view is updated to show what might be a newly added product catalog.
            while (getShell().getDisplay().readAndDispatch())
            {
              // Do nothing.
            }

            filteredTree.clearText();
            productViewer.setSelection(new StructuredSelection(productVersion.getProduct()), true);
            versionComboViewer.setSelection(new StructuredSelection(productVersion), true);

            // Ensure that asynchronous update of the JRE controller's enablement has completed.
            while (getShell().getDisplay().readAndDispatch())
            {
              // Do nothing.
            }

            if (isCurrentPage() && isPageComplete())
            {
              gotoNextPage();
            }
            else
            {
              updateSetupContext();
            }

            return true;
          }
        };

        IWizardContainer container = getContainer();
        if (container instanceof InstallerDialog)
        {
          if (configurationProcessor.shouldSwitchUserHome())
          {
            SwitchUserHomeDialog switchUserHomeDialog = new SwitchUserHomeDialog(getShell());
            if (switchUserHomeDialog.open() == Window.OK)
            {
              UIUtil.asyncExec(() -> {
                ((InstallerDialog)container).switchToNewUserHome(switchUserHomeDialog.getUserHomeLocation());
              });

              return;
            }
          }
        }

        configurationProcessor.processInstallation();
        IStatus status = configurationProcessor.getStatus();
        if (!status.isOK())
        {
          new StatusDialog(getShell(), Messages.ProductPage_InstallatinProblems_title, null, status, Diagnostic.ERROR).open();
        }
      }
    };

    getWizard().getTransferSupport().addDropListener(dropListener);

    return sashForm;
  }

  @Override
  public void enterPage(boolean forward)
  {
    getWizard().setSetupContext(SetupContext.create(getResourceSet(), null));

    final Collection<? extends Resource> configurationResources = getWizard().getConfigurationResources();
    final SetupTransferSupport transferSupport = getWizard().getTransferSupport();
    ProjectPage.hookTransferControl(getShell(), getControl(), transferSupport, configurationListener);
    if (forward && !configurationResources.isEmpty())
    {
      UIUtil.asyncExec(getShell(), new Runnable()
      {
        private int count;

        @Override
        public void run()
        {
          IndexLoader indexLoader = getWizard().getIndexLoader();
          if (indexLoader != null)
          {
            indexLoader.awaitIndexLoad();
          }
          if (++count < 10)
          {
            UIUtil.asyncExec(getShell(), this);
          }
          else
          {
            transferSupport.resourcesDropped(configurationResources);
          }
        }
      });
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    ProjectPage.unhookTransferControl(getShell(), getControl(), getWizard().getTransferSupport(), configurationListener);

    if (forward)
    {
      updateSetupContext();
    }
  }

  private void updateSetupContext()
  {
    ProductVersion productVersion = getSelectedProductVersion();
    if (productVersion != null)
    {
      selectionMemento.setProductVersion(EcoreUtil.getURI(productVersion));
      getWizard().setSetupContext(SetupContext.create(getResourceSet(), productVersion));
    }
  }

  public boolean refreshJREs()
  {
    if (javaController != null)
    {
      javaController.refresh();
      return true;
    }

    return false;
  }

  @Override
  public void sendStats(boolean success)
  {
    super.sendStats(success);

    SetupContext setupContext = getWizard().getSetupContext();
    if (!success)
    {
      // If we've failed but there are streams involved, they can be the cause of the failure so don't blame the product.
      Workspace workspace = setupContext.getWorkspace();
      if (workspace != null && !workspace.getStreams().isEmpty())
      {
        return;
      }
    }

    ProductVersion productVersion = setupContext.getInstallation().getProductVersion();
    OS os = getPerformer().getOS();
    SetupCoreUtil.sendStats(success, productVersion, os);
  }

  private void updateProductDetails(boolean initial)
  {
    Product product = getSelectedProduct();
    if (product == null)
    {
      product = NO_PRODUCT;
    }

    boolean productSelected = product != NO_PRODUCT;

    versionComboViewer.setInput(product);

    if (productSelected)
    {
      ProductVersion version = getDefaultProductVersion(catalogSelector.getCatalogManager(), product, getWizard().getOS());
      if (version != null)
      {
        versionComboViewer.setSelection(new StructuredSelection(version));
        return;
      }
    }

    updateProductVersionDetails(initial);
  }

  private void updateProductVersionDetails(boolean initial)
  {
    ProductVersion productVersion = getSelectedProductVersion();
    Product product = productVersion == null ? getSelectedProduct() : productVersion.getProduct();
    if (product == null)
    {
      product = NO_PRODUCT;
    }

    boolean productSelected = product != NO_PRODUCT;
    String error = productSelected ? productVersion == null ? Messages.ProductPage_IncompatibleVersion_message : null : Messages.ProductPage_Select_message;

    Scope scope = productSelected ? product : getSelectedProductCatalog();
    if (descriptionBrowser != null)
    {
      String html = safe(getDescriptionHTML(productVersion != null ? productVersion : scope));
      descriptionBrowser.setEnabled(scope != null);
      descriptionBrowser.setText(html);
    }
    else
    {
      String html = safe(scope == null ? null : scope.getDescription());
      URI brandingSiteURI = SetupWizard.getBrandingSiteURI(productVersion == null ? product : productVersion);
      String plain = StringUtil.isEmpty(html) ? Messages.ProductPage_NoDescription_message : UIUtil.stripHTML(html);
      String label = SetupCoreUtil.getLabel(scope);
      if (productVersion != null)
      {
        String productVersionLabel = getLabel(productVersion);
        if (!StringUtil.isEmpty(productVersionLabel))
        {
          label += " \u2013 " + productVersionLabel; //$NON-NLS-1$
        }
      }

      descriptionViewer.update(SetupWizard.getBrandingImage(product), label,
          productVersion != null
              && "true".equals(BaseUtil.getAnnotation(productVersion, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_INCUBATING)), //$NON-NLS-1$
          plain, brandingSiteURI == null ? null : brandingSiteURI.toString());
    }

    versionLabel.setEnabled(productSelected);
    versionComboViewer.getControl().setEnabled(productSelected);

    if (InstallerUI.BITNESS_CHOOSE && JREManager.BITNESS_CHANGEABLE && !OS_CHOOSE)
    {
      bitness32Button.setEnabled(productSelected);
      bitness64Button.setEnabled(productSelected);
    }

    javaLabel.setEnabled(productSelected);
    javaViewer.getControl().setEnabled(productSelected);
    javaButton.setEnabled(productSelected);

    if (poolButton != null)
    {
      currentBundlePoolChanging = true;

      if (currentBundlePool != null)
      {
        poolComboViewer.setSelection(new StructuredSelection(currentBundlePool));
      }
      else
      {
        if (error == null && productSelected && poolButton.getSelection())
        {
          error = Messages.ProductPage_SelectBundlePool_message;
        }

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

  private String getDescriptionHTML(Scope scope)
  {
    if (scope == null)
    {
      return null;
    }

    String imageURI = SetupWizard.getLocalBrandingImageURI(scope);

    Scope effectiveScope = scope instanceof ProductVersion ? scope.getParentScope() : scope;
    String description = effectiveScope.getDescription();
    String label = effectiveScope.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = effectiveScope.getName();
    }

    String versionLabel = null;
    if (!StringUtil.isEmpty(label) && scope instanceof ProductVersion)
    {
      versionLabel = getLabel((ProductVersion)scope);
    }

    URI brandingSiteURI = SetupWizard.getBrandingSiteURI(scope);

    StringBuilder result = new StringBuilder();
    result.append("<html><body style='margin:5px;'>"); //$NON-NLS-1$
    if (brandingSiteURI != null)
    {
      result.append("<a style='text-decoration: none; color: inherit;' href='"); //$NON-NLS-1$
      result.append(brandingSiteURI);
      result.append("'>"); //$NON-NLS-1$
    }

    result.append("<img src='"); //$NON-NLS-1$
    result.append(imageURI);
    result.append("' width='42' height='42' align='absmiddle'></img>"); //$NON-NLS-1$
    result.append("<span><b>&nbsp;&nbsp;&nbsp;<span style='font-family:Arial,Verdana,sans-serif; font-size:100%'>"); //$NON-NLS-1$
    result.append(DiagnosticDecorator.escapeContent(safe(label)));
    if (!StringUtil.isEmpty(versionLabel))
    {
      result.append(" &ndash; "); //$NON-NLS-1$
      result.append(DiagnosticDecorator.escapeContent(versionLabel));
    }

    if ("true".equals(BaseUtil.getAnnotation(scope, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_INCUBATING))) //$NON-NLS-1$
    {
      URI incubationURI = ImageURIRegistry.INSTANCE
          .getImageURI(ExtendedImageRegistry.INSTANCE.getImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/eclipse_incubation.png"))); //$NON-NLS-1$
      result.append("&nbsp;<img title='Contains incubating components' style='height: 2ex;' src='"); //$NON-NLS-1$
      result.append(incubationURI);
      result.append("' align='absmiddle'></img>"); //$NON-NLS-1$
    }

    if (brandingSiteURI != null)
    {
      result.append("</a>"); //$NON-NLS-1$
    }

    result.append("</b>"); //$NON-NLS-1$

    if (brandingSiteURI != null && !StringUtil.isEmpty(versionLabel))
    {
      result.append("<a style='float: right; padding-top: 12px; padding-right: 15px; font-family:Arial,Verdana,sans-serif; font-size:75%' href='"); //$NON-NLS-1$
      result.append(brandingSiteURI);
      result.append("'>"); //$NON-NLS-1$
      result.append("details"); //$NON-NLS-1$
      result.append("</a>"); //$NON-NLS-1$
    }

    result.append("</span>"); //$NON-NLS-1$
    result.append("<br/><hr/></span><span style='font-family:Arial,Verdana,sans-serif; font-size:75%'>"); //$NON-NLS-1$
    result.append(safe(description));
    result.append("</span></body></html>"); //$NON-NLS-1$
    return result.toString();
  }

  private String getLabel(ProductVersion productVersion)
  {
    String label = productVersion.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = productVersion.getName();
    }
    else
    {
      int start = label.indexOf('(');
      if (start != -1)
      {
        int end = label.indexOf(')', start);
        if (end != -1)
        {
          label = label.substring(start + 1, end);
        }
      }
    }

    return label;
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
      System.setProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION, AgentManager.BUNDLE_POOL_LOCATION_NONE);
    }

    currentBundlePool = pool;
  }

  private void initBundlePool()
  {
    String client = SetupUIPlugin.INSTANCE.getSymbolicName();
    AgentManager agentManager = P2Util.getAgentManager();
    BundlePool pool = agentManager.getDefaultBundlePool(client);
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
          button.setEnabled(getSelectedBundlePool() != null);
        }
      }
    };

    IStructuredSelection selection = (IStructuredSelection)poolComboViewer.getSelection();
    BundlePool pool = (BundlePool)selection.getFirstElement();
    if (pool != null)
    {
      dialog.setSelectedPool(pool);
    }

    int result = dialog.open();
    poolComboViewer.refresh();

    if (result == AgentManagerDialog.OK)
    {
      pool = dialog.getSelectedBundlePool();
      poolComboViewer.setSelection(pool == null ? StructuredSelection.EMPTY : new StructuredSelection(pool));
      if (pool != null)
      {
        poolButton.setSelection(true);
      }
    }
  }

  private ProductCatalog getSelectedProductCatalog()
  {
    IStructuredSelection selection = (IStructuredSelection)productViewer.getSelection();
    Object element = selection.getFirstElement();
    if (element instanceof ProductCatalog)
    {
      return (ProductCatalog)element;
    }

    return null;
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

  public static ProductVersion getDefaultProductVersion(CatalogManager catalogManager, Product product, OS os)
  {
    ProductVersion version = catalogManager.getSelection().getDefaultProductVersions().get(product);
    List<ProductVersion> validProductVersions = getValidProductVersions(product, null, os);
    if (!validProductVersions.contains(version))
    {
      ProductVersion firstReleasedProductVersion = null;
      ProductVersion latestProductVersion = null;
      ProductVersion latestReleasedProductVersion = null;
      for (ProductVersion productVersion : validProductVersions)
      {
        String versionName = productVersion.getName();
        if ("latest.released".equals(versionName)) //$NON-NLS-1$
        {
          latestReleasedProductVersion = productVersion;
        }
        else if ("latest".equals(versionName)) //$NON-NLS-1$
        {
          latestProductVersion = productVersion;
        }
        else if (firstReleasedProductVersion == null)
        {
          firstReleasedProductVersion = productVersion;
        }
      }

      if (latestReleasedProductVersion != null)
      {
        if (latestReleasedProductVersion.getLabel().contains("(Luna)") && latestProductVersion != null && latestProductVersion.getLabel().contains("(Mars")) //$NON-NLS-1$ //$NON-NLS-2$
        {
          version = latestProductVersion;
        }
        else
        {
          version = latestReleasedProductVersion;
        }
      }
      else if (firstReleasedProductVersion != null)
      {
        version = firstReleasedProductVersion;
      }
      else
      {
        version = latestProductVersion;
      }

      if (version != null)
      {
        saveProductVersionSelection(catalogManager, version);
      }
    }

    return version;
  }

  public static String getToolTipText(ProductVersion version)
  {
    if (version == null)
    {
      return null;
    }

    String label = SetupCoreUtil.getLabel(version);
    Matcher matcher = RELEASE_LABEL_PATTERN.matcher(label);
    if (matcher.matches())
    {
      String sublabel = matcher.group(1);
      for (ProductVersion otherVersion : version.getProduct().getVersions())
      {
        String otherVersionLabel = SetupCoreUtil.getLabel(otherVersion);
        if (sublabel.equals(otherVersionLabel))
        {
          label = sublabel;
          break;
        }
      }
    }

    String result = NLS.bind(Messages.ProductPage_InstallVersion_message, label);
    String name = version.getName();
    if ("latest.released".equals(name)) //$NON-NLS-1$
    {
      result += Messages.ProductPage_UpdateRelease_message;
    }
    else if ("latest".equals(name)) //$NON-NLS-1$
    {
      result += Messages.ProductPage_Update_message;
    }
    else
    {
      result += Messages.ProductPage_UpdateServiceRelease_message;
    }

    return result;
  }

  public static void saveProductVersionSelection(CatalogManager catalogManager, ProductVersion version)
  {
    EMap<Product, ProductVersion> defaultProductVersions = catalogManager.getSelection().getDefaultProductVersions();
    Product product = version.getProduct();
    defaultProductVersions.put(product, version);
    defaultProductVersions.move(0, defaultProductVersions.indexOfKey(product));
    catalogManager.saveSelection();
  }

  private static Product createNoProduct()
  {
    Product product = SetupFactory.eINSTANCE.createProduct();
    product.setName(Messages.ProductPage_NoProductSelected_label);
    product.setLabel(product.getName());

    ProductVersion version = SetupFactory.eINSTANCE.createProductVersion();
    version.setName(Messages.ProductPage_NoProductVersionSelected_label);
    version.setLabel(version.getName());

    product.getVersions().add(version);
    return product;
  }

  private static String safe(String string)
  {
    if (string == null)
    {
      return ""; //$NON-NLS-1$
    }

    return string;
  }

  public static String getVMOption(JRE jre)
  {
    // There should be no VM option if it's the VM for the system JRE.
    if (jre == null || jre.equals(JREManager.INSTANCE.getSystemJRE()))
    {
      return null;
    }

    // THere should be no VM option if there is no Java home, i.e., if this is a descriptor-based JRE.
    File javaHome = jre.getJavaHome();
    if (javaHome == null)
    {
      return null;
    }

    return new File(javaHome, "bin").toString(); //$NON-NLS-1$
  }

  public static List<ProductVersion> getValidProductVersions(Product product, Pattern filter, OS os)
  {
    if (!matchesFilterContext(product, os))
    {
      return Collections.emptyList();
    }

    List<ProductVersion> versions = new ArrayList<ProductVersion>(product.getVersions());
    if (os.isMac())
    {
      // Filter out the older releases because the latest p2, with it's layout changes for the Mac, can't install a correct image from older repositories.
      for (Iterator<ProductVersion> it = versions.iterator(); it.hasNext();)
      {
        ProductVersion version = it.next();
        String label = version.getLabel();
        if (label != null && (label.contains("Luna") || label.contains("Kepler") || label.contains("Juno"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        {
          it.remove();
        }
      }
    }

    if (filter != null && !StringUtil.isEmpty(filter.pattern()))
    {
      for (Iterator<ProductVersion> it = versions.iterator(); it.hasNext();)
      {
        ProductVersion version = it.next();
        String name = version.getQualifiedName();
        if (name == null || !filter.matcher(name).matches())
        {
          it.remove();
        }
      }
    }

    for (Iterator<ProductVersion> it = versions.iterator(); it.hasNext();)
    {
      ProductVersion version = it.next();
      if (!matchesFilterContext(version, os))
      {
        it.remove();
      }
    }

    return versions;
  }

  private static boolean matchesFilterContext(Scope scope, OS os)
  {
    for (SetupTask setupTask : scope.getSetupTasks())
    {
      if (setupTask instanceof P2Task)
      {
        P2Task p2Task = (P2Task)setupTask;
        for (Requirement requirement : p2Task.getRequirements())
        {
          if (!requirement.isOptional())
          {
            String requirementFilter = requirement.getFilter();
            if (requirementFilter != null && requirementFilter.contains("osgi.arch=") && !SetupWizard.matchesFilterContext(requirementFilter, os)) //$NON-NLS-1$
            {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  /**
   * @author Ed Merks
   */
  private static class DescriptionViewer
  {
    private ScrolledComposite scrolledComposite;

    private Composite descriptionComposite;

    private CLabel descriptionLabel;

    private Link detailsLink;

    private String detailsLocation;

    private Text descriptionText;

    private ControlAdapter resizeListener;

    private CLabel descriptionLabelImage;

    public DescriptionViewer(Composite container, Font font)
    {
      Color foreground = container.getForeground();
      Color background = container.getBackground();

      scrolledComposite = new ScrolledComposite(container, SWT.VERTICAL);
      scrolledComposite.setExpandHorizontal(true);
      scrolledComposite.setExpandVertical(true);

      resizeListener = new ControlAdapter()
      {
        @Override
        public void controlResized(ControlEvent e)
        {
          Point size = descriptionComposite.computeSize(scrolledComposite.getClientArea().width, SWT.DEFAULT);
          scrolledComposite.setMinSize(size);
        }
      };
      scrolledComposite.addControlListener(resizeListener);

      descriptionComposite = new Composite(scrolledComposite, SWT.NONE);
      descriptionComposite.setLayout(new GridLayout());
      descriptionComposite.setForeground(foreground);
      descriptionComposite.setBackground(background);
      scrolledComposite.setContent(descriptionComposite);

      Composite labelComposite = new Composite(descriptionComposite, SWT.NONE);
      GridLayout labelCompositeLayout = new GridLayout(3, false);
      labelCompositeLayout.marginHeight = 0;
      labelCompositeLayout.marginWidth = 0;
      labelCompositeLayout.marginRight = 12;
      labelComposite.setLayout(labelCompositeLayout);
      labelComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      labelComposite.setForeground(foreground);
      labelComposite.setBackground(background);

      descriptionLabel = new CLabel(labelComposite, SWT.NONE);
      descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
      descriptionLabel.setForeground(foreground);
      descriptionLabel.setBackground(background);
      descriptionLabel.setFont(SetupUIPlugin.getFont(font, URI.createURI("font:///+2/bold"))); //$NON-NLS-1$

      descriptionLabelImage = new CLabel(labelComposite, SWT.NONE);
      descriptionLabelImage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      descriptionLabelImage.setForeground(foreground);
      descriptionLabelImage.setBackground(background);
      descriptionLabelImage.setFont(SetupUIPlugin.getFont(font, URI.createURI("font:///+2/bold"))); //$NON-NLS-1$
      descriptionLabelImage.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/eclipse_incubation.png")); //$NON-NLS-1$
      descriptionLabelImage.setToolTipText(Messages.ProductPage_Incubating_message);

      detailsLink = new Link(labelComposite, SWT.NONE);
      detailsLink.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
      detailsLink.setText("<a>details</a>"); //$NON-NLS-1$
      // detailsLink.setForeground(foreground);
      detailsLink.setBackground(background);
      detailsLink.setToolTipText(Messages.ProductPage_OpenDescription_message);
      detailsLink.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser(detailsLocation);
        }
      });

      Label separator = new Label(descriptionComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
      separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      descriptionText = new Text(descriptionComposite, SWT.READ_ONLY | SWT.WRAP);
      descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
      descriptionText.setForeground(foreground);
      descriptionText.setBackground(background);
    }

    public void update(Image image, String title, boolean incubating, String body, String details)
    {
      descriptionLabel.setImage(image);
      descriptionLabel.setText(title);
      descriptionLabelImage.setVisible(incubating);
      descriptionText.setText(body);
      if (details == null)
      {
        detailsLink.setVisible(false);
        detailsLocation = null;
      }
      else
      {
        detailsLink.setVisible(true);
        detailsLocation = details;
      }

      descriptionLabel.getParent().layout(true);

      resizeListener.controlResized(null);
    }
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
      return new ProductCatalogItemProvider(this)
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
        protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
            Collection<?> collection)
        {
          final ProductCatalog targetProductCatalog = (ProductCatalog)owner;
          Resource directResource = ((InternalEObject)targetProductCatalog).eDirectResource();
          if (directResource != null && SetupContext.isUserScheme(directResource.getURI().scheme()))
          {
            final ResourceSet resourceSet = domain.getResourceSet();
            return new DragAndDropCommand(domain, resourceSet, location, operations, operation, collection)
            {
              final Set<Product> products = new LinkedHashSet<Product>();

              final Set<Product> affectedObjects = new LinkedHashSet<Product>();

              @Override
              public void execute()
              {
                EList<Product> targetProducts = targetProductCatalog.getProducts();
                LOOP: for (Product product : products)
                {
                  if (operation != DROP_LINK)
                  {
                    if (product.eContainer() != null)
                    {
                      product = EcoreUtil.copy(product);
                    }
                    else
                    {
                      Resource resource = product.eResource();
                      resource.getContents().clear();
                      resourceSet.getResources().remove(resource);
                    }
                  }

                  affectedObjects.add(product);

                  String name = product.getName();
                  for (Product otherProduct : targetProducts)
                  {
                    if (name.equals(otherProduct.getName()))
                    {
                      targetProducts.set(targetProducts.indexOf(otherProduct), product);
                      continue LOOP;
                    }
                  }

                  targetProducts.add(product);
                }

                BaseUtil.saveEObject(targetProductCatalog);
              }

              @Override
              protected boolean prepare()
              {
                products.clear();

                for (Object value : collection)
                {
                  if (value instanceof URI)
                  {
                    URI uri = (URI)value;
                    BaseResource resource = BaseUtil.loadResourceSafely(resourceSet, uri);
                    Product product = (Product)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.PRODUCT);
                    if (product != null && product.getName() != null && (operation == DROP_COPY || product.eContainer() == null))
                    {
                      products.add(product);
                    }
                  }
                  else if (value instanceof Product)
                  {
                    Product product = (Product)value;
                    if (product.getName() != null && (operation == DROP_COPY || product.eContainer() == null))
                    {
                      products.add(product);
                    }
                  }
                }

                if (operation == DROP_MOVE)
                {
                  operation = DROP_LINK;
                }

                return !products.isEmpty();
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
      };
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
          protected Command createDragAndDropCommand(EditingDomain domain, ResourceSet resourceSet, float location, int operations, int operation,
              Collection<URI> collection)
          {
            return UnexecutableCommand.INSTANCE;
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

  /**
   * @author Eike Stepper
   */
  private static final class AddUserProductDialog extends ResourceDialog
  {
    private final Set<ProductCatalog> productCatalogs;

    private CatalogSelector catalogSelector;

    private final AdapterFactoryEditingDomain editingDomain;

    private ComboViewer catalogViewer;

    public AddUserProductDialog(Shell parent, Set<ProductCatalog> productCatalog, CatalogSelector catalogSelector, AdapterFactoryEditingDomain editingDomain)
    {
      super(parent, Messages.ProductPage_AddUserProducts_title, SWT.OPEN | SWT.MULTI);
      productCatalogs = productCatalog;
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
      label.setText(Messages.ProductPage_Catalog_label);
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

      List<? extends Scope> catalogs = new ArrayList<Scope>(catalogSelector.getCatalogs());
      for (Iterator<? extends Scope> it = catalogs.iterator(); it.hasNext();)
      {
        if (!ConfigurationProcessor.isUserProductCatalog(it.next()))
        {
          it.remove();
        }
      }

      catalogViewer.setInput(catalogs);

      if (catalogs.size() == 1)
      {
        catalogViewer.setSelection(new StructuredSelection(catalogs.get(0)));
      }
      else if (productCatalogs.size() == 1 && catalogs.containsAll(productCatalogs))
      {
        catalogViewer.setSelection(new StructuredSelection(productCatalogs.iterator().next()));
      }

      catalogViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
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
        @Override
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
          fileDialog.setFilterExtensions(new String[] { "*.setup" }); //$NON-NLS-1$
          fileDialog.open();

          String filterPath = fileDialog.getFilterPath();
          String[] fileNames = fileDialog.getFileNames();
          StringBuffer uris = new StringBuffer();

          for (int i = 0, len = fileNames.length; i < len; i++)
          {
            uris.append(URI.createFileURI(filterPath + File.separator + fileNames[i]).toString());
            uris.append("  "); //$NON-NLS-1$
          }

          uriField.setText((uriField.getText() + "  " + uris.toString()).trim()); //$NON-NLS-1$
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
      List<Product> validProducts = new ArrayList<Product>();
      List<Product> invalidProducts = new ArrayList<Product>();
      List<URI> invalidURIs = new ArrayList<URI>();
      ResourceSet resourceSet = editingDomain.getResourceSet();
      for (URI uri : getURIs())
      {
        BaseResource resource = BaseUtil.loadResourceSafely(resourceSet, uri);
        Product product = (Product)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.PRODUCT);
        if (product == null)
        {
          invalidURIs.add(uri);
        }
        else if (product.eContainer() != null)
        {
          invalidProducts.add(product);
        }
        else
        {
          validProducts.add(product);
        }
      }

      if (!validProducts.isEmpty())
      {
        ProductCatalog selectedCatalog = getSelectedCatalog();
        if (!catalogSelector.getSelectedCatalogs().contains(selectedCatalog))
        {
          catalogSelector.select(selectedCatalog, true);
        }

        Command command = DragAndDropCommand.create(editingDomain, selectedCatalog, 0.5F, DragAndDropFeedback.DROP_LINK, DragAndDropFeedback.DROP_LINK,
            validProducts);
        editingDomain.getCommandStack().execute(command);
        return true;
      }

      StringBuilder message = new StringBuilder();

      int invalidURIsSize = invalidURIs.size();
      if (invalidURIsSize != 0)
      {
        if (invalidURIsSize == 1)
        {
          message.append(Messages.ProductPage_URIPrefix_message);
        }
        else
        {
          message.append(Messages.ProductPage_URIsPrefix_message);
        }

        for (int i = 0; i < invalidURIsSize; ++i)
        {
          if (i != 0)
          {
            message.append(", "); //$NON-NLS-1$

            if (i + 1 == invalidURIsSize)
            {
              message.append(Messages.ProductPage_And_message);
            }
          }

          message.append('\'');
          message.append(invalidURIs.get(i));
          message.append('\'');
        }

        if (invalidURIsSize == 1)
        {
          message.append(Messages.ProductPage_InvalidProduct_message);
        }
        else
        {
          message.append(Messages.ProductPage_InvalidProducts_message);
        }
      }

      int invalidProductsSize = invalidProducts.size();
      if (invalidProductsSize != 0)
      {
        if (message.length() != 0)
        {
          message.append("\n\n"); //$NON-NLS-1$
        }

        if (invalidProductsSize == 1)
        {
          message.append(Messages.ProductPage_Product_message);
        }
        else
        {
          message.append(Messages.ProductPage_Products_message);
        }

        for (int i = 0; i < invalidProductsSize; ++i)
        {
          if (i != 0)
          {
            message.append(", "); //$NON-NLS-1$

            if (i + 1 == invalidProductsSize)
            {
              message.append(Messages.ProductPage_And_message);
            }
          }

          message.append('\'');
          message.append(invalidProducts.get(i).getLabel());
          message.append('\'');
        }

        if (invalidProductsSize == 1)
        {
          message.append(Messages.ProductPage_AlreadyContained_message);
        }
        else
        {
          message.append(Messages.ProductPage_AlreadyContainedPlural_message);
        }
      }

      if (message.length() == 0)
      {
        message.append(Messages.ProductPage_NoURIsSpecified_message);
      }

      ErrorDialog.openError(getShell(), Messages.ProductPage_ErrorAdding_title, null,
          new Status(IStatus.ERROR, SetupUIPlugin.INSTANCE.getSymbolicName(), message.toString()));
      return false;
    }

    private ProductCatalog getSelectedCatalog()
    {
      IStructuredSelection selection = (IStructuredSelection)catalogViewer.getSelection();
      ProductCatalog selectedCatalog = (ProductCatalog)selection.getFirstElement();
      return selectedCatalog;
    }
  }
}

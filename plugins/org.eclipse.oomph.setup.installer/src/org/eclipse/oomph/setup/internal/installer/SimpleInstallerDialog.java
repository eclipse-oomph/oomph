/*
 * Copyright (c) 2015-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.internal.ui.ToggleSwitchButton;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.installer.SimpleInstallerMenu.InstallerMenuItem;
import org.eclipse.oomph.setup.internal.installer.SimpleMessageOverlay.ControlRelocator;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.ConfigurationProcessor;
import org.eclipse.oomph.setup.ui.wizards.ExtensionsDialog;
import org.eclipse.oomph.setup.ui.wizards.MarketPlaceListingProcessor;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.StatusDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends AbstractSimpleDialog implements InstallerUI
{
  private static final String CATALOGS_MENU_ITEM_TEXT = ProductCatalogsDialog.TITLE.toUpperCase() + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String APPLY_CONFIGURATION_MENU_ITEM_TEXT = "APPLY CONFIGURATION";

  private static final String APPLY_CONFIGURATION_MENU_ITEM_DESCRIPTION = "Apply the configuration from the clipboard";

  private static final String APPLY_MARKET_PLACE_LISTING_MENU_ITEM_TEXT = "APPLY MARKETPLACE LISTING" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String APPLY_MARKET_PLACE_LISTING_MENU_ITEM_DESCRIPTION = "Apply the marketplace listing from the clipboard";

  private static final String EXTENSIONS_MENU_ITEM_TEXT = "EXTENSIONS" + StringUtil.HORIZONTAL_ELLIPSIS;

  static final String EXTENSIONS_MENU_ITEM_DESCRIPTION = "Manage the applied marketplace listings and configurations";

  private static final String SWITCH_CATALOG_INDEX_MENU_ITEM_TEXT = "SWITCH CATALOG INDEX";

  private static final String BUNDLE_POOLS_MENU_ITEM_TEXT = "BUNDLE POOLS" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String UPDATE_MENU_ITEM_TEXT = "UPDATE";

  private static final String ADVANCED_MENU_ITEM_TEXT = "ADVANCED MODE" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String WEB_LINKS_MENU_ITEM_TEXT = "WEB LINKS" + StringUtil.HORIZONTAL_ELLIPSIS;

  static final String WEB_LINKS_MENU_ITEM_DESCRIPTION = "Register this installer to automatically launch for links in a web browser";

  private static final String MARKET_PLACE_MENU_ITEM_TEXT = "MARKETPLACE" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String ASK_A_QUESTION_MENU_ITEM_TEXT = "ASK A QUESTION" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String REPORT_A_PROBLEM_MENU_ITEM_TEXT = "REPORT A PROBLEM" + StringUtil.HORIZONTAL_ELLIPSIS;

  private static final String ABOUT_MENU_ITEM_TEXT = "ABOUT";

  private static final String EXIT_MENU_ITEM_TEXT = "EXIT";

  private static final Preference PREF_POOL_ENABLED = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("poolEnabled");

  private static final boolean MARKETPLACE_MENU_ITEM_ENABLED = !"false".equals(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_MARKETPLACE));

  private static final String HOST = PropertiesUtil.getProperty("oomph.setup.installer.host", "https://www.eclipse.org/setups/installer");

  private static final String PROBLEM_REPORT_URL = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_PROBLEM_REPORT, HOST + "/problem/");

  private static final String FORUM_URL = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_FORUM, HOST + "/question/");

  private static Font defaultFont;

  private static Point defaultSize;

  private static String css;

  private static String pageTemplate;

  private static String productTemplate;

  private static String productTemplateLarge;

  private final Installer installer;

  private final boolean restarted;

  private final CatalogManager catalogManager;

  private final PageStack pageStack = new PageStack();

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private SimpleReadmePage readmePage;

  private SimpleInstallationLogPage installationLogPage;

  private SimpleKeepInstallerPage keepInstallerPage;

  private SimpleInstallerMenu installerMenu;

  private SimpleInstallerMenuButton menuButton;

  private NotifictionButton notificationButton;

  private boolean poolEnabled;

  private BundlePool pool;

  private Resolution updateResolution;

  private SimpleMessageOverlay currentMessage;

  private ToggleSwitchButton bundlePoolSwitch;

  private ToggleSwitchButton webLinksSwitch;

  private boolean showProductCatalogsItem;

  public SimpleInstallerDialog(Display display, Installer installer, boolean restarted)
  {
    super(display, SWT.NO_TRIM, getDefaultSize(display).x, getDefaultSize(display).y);
    setMinimumSize(385, 75);
    this.installer = installer;
    this.restarted = restarted;
    catalogManager = installer.getCatalogManager();
  }

  protected void applyConfiguration()
  {
    MarketPlaceListingProcessor marketPlaceListingProcessor = new MarketPlaceListingProcessor(installer);
    if (marketPlaceListingProcessor.isMarketPlaceListing())
    {
      marketPlaceListingProcessor.processMarketPlaceListing();
      IStatus status = marketPlaceListingProcessor.getStatus();
      if (!status.isOK())
      {
        new StatusDialog(getShell(), "Marketplace Listing Problems", null, status, Diagnostic.ERROR).open();
      }
    }
    else
    {
      ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(installer)
      {
        @Override
        protected void handleSwitchToAdvancedMode()
        {
          switchToAdvancedMode();
        }

        @Override
        protected boolean applyEmptyProductVersion()
        {
          applyInstallation();
          return true;
        }

        @Override
        protected boolean applyProductVersion(ProductVersion productVersion)
        {
          applyInstallation();

          productPage.handleFilter("");
          productPage.productSelected(productVersion.getProduct());
          variablePage.setProductVersion(productVersion);
          return true;
        }
      };

      if (configurationProcessor.processWorkspace())
      {
        configurationProcessor.processInstallation();
      }

      IStatus status = configurationProcessor.getStatus();
      if (!status.isOK())
      {
        new StatusDialog(getShell(), "Configuration Problems", null, status, Diagnostic.ERROR).open();
      }
    }
  }

  @Override
  protected IDialogSettings getDialogSizeSettings()
  {
    return SetupInstallerPlugin.INSTANCE.getDialogSettings("SimpleInstaller");
  }

  @Override
  protected int getTitleColumnCount()
  {
    return super.getTitleColumnCount() + 1;
  }

  @Override
  protected void createUI(Composite titleComposite)
  {
    Composite notificationContainer = new Composite(titleComposite, SWT.NONE);
    GridLayout notificationContainerLayout = UIUtil.createGridLayout(1);
    notificationContainerLayout.marginRight = 15;

    notificationContainer.setLayout(notificationContainerLayout);
    notificationContainer.setLayoutData(GridDataFactory.swtDefaults().grab(false, true).align(SWT.CENTER, SWT.BEGINNING).create());

    notificationButton = new NotifictionButton(notificationContainer);
    notificationButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BEGINNING).create());
    notificationButton.setVisible(false);

    Composite exitMenuButtonContainer = new Composite(titleComposite, SWT.NONE);
    exitMenuButtonContainer.setLayout(UIUtil.createGridLayout(1));
    exitMenuButtonContainer.setLayoutData(GridDataFactory.swtDefaults().grab(false, true).align(SWT.CENTER, SWT.FILL).create());

    FlatButton exitButton = new ImageHoverButton(exitMenuButtonContainer, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exit_hover.png"));
    exitButton.setShowButtonDownState(false);
    exitButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BEGINNING).create());
    exitButton.setToolTipText("Exit");
    exitButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        exitSelected();
      }
    });

    menuButton = new SimpleInstallerMenuButton(exitMenuButtonContainer);
    menuButton.setLayoutData(GridDataFactory.swtDefaults().grab(false, true).align(SWT.CENTER, SWT.BEGINNING).indent(11, 0).create());
    menuButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // Don't use the event, it can be null!
        toggleMenu();
      }
    });

    stackLayout = new StackLayout();

    stack = new Composite(this, SWT.NONE);
    stack.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    stack.setLayout(stackLayout);

    SelectionMemento selectionMemento = installer.getSelectionMemento();
    productPage = new SimpleProductPage(stack, this, selectionMemento);
    variablePage = new SimpleVariablePage(stack, this, selectionMemento);

    if (UIUtil.isBrowserAvailable())
    {
      readmePage = new SimpleReadmePage(stack, this);
    }

    installationLogPage = new SimpleInstallationLogPage(stack, this);
    keepInstallerPage = new SimpleKeepInstallerPage(stack, this);

    switchToPage(productPage);

    Display display = getDisplay();

    if (!restarted)
    {
      Thread updateSearcher = new UpdateSearcher(display);
      updateSearcher.start();
    }

    display.timerExec(500, new Runnable()
    {
      public void run()
      {
        installer.getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
        installer.loadIndex();
      }
    });

    // Initialize menu.
    getInstallerMenu();

    final PropertyChangeListener catalogManagerListener = new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent evt)
      {
        if (CatalogManager.PROPERTY_INDEX.equals(evt.getPropertyName()))
        {
          indexLoaded((Index)evt.getNewValue());
        }
      }
    };
    catalogManager.addPropertyChangeListener(catalogManagerListener);

    addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        catalogManager.removePropertyChangeListener(catalogManagerListener);
      }
    });

    enablePool(SHOW_BUNDLE_POOL_UI && PREF_POOL_ENABLED.get(true));

    updateAvailable(false);

    titleComposite.layout(true);
  }

  private void indexLoaded(Index index)
  {
    @SuppressWarnings("unchecked")
    List<ProductCatalog> productCatalogs = (List<ProductCatalog>)catalogManager.getCatalogs(true);

    int count = 0;
    if (productCatalogs != null)
    {
      for (ProductCatalog productCatalog : productCatalogs)
      {
        if (SimpleProductPage.isIncluded(productCatalog))
        {
          ++count;
        }
      }
    }

    // Show it if there is more than one catalog,
    // or if there are no filters in place and there indices available to switch to.
    // This will not show it for the crippled installer because it has filters in place.
    showProductCatalogsItem = count > 1
        || StringUtil.isEmpty(SimpleInstallerPage.PRODUCT_CATALOG_FILTER.pattern()) && StringUtil.isEmpty(SimpleInstallerPage.PRODUCT_FILTER.pattern())
            && StringUtil.isEmpty(SimpleInstallerPage.PRODUCT_VERSION_FILTER.pattern()) && new IndexManager().getIndexNames(false).size() > 1;

    // Initialize notification button with the default.
    setBrandingNotificationScope(null);
  }

  public void setBrandingNotificationScope(Scope scope)
  {
    if (scope == null)
    {
      notificationButton.setURI(null, false);

      // If there is no scope, iterate over the catalog defaults.
      @SuppressWarnings("unchecked")
      List<ProductCatalog> productCatalogs = (List<ProductCatalog>)catalogManager.getCatalogs(true);
      if (productCatalogs != null)
      {
        for (ProductCatalog productCatalog : productCatalogs)
        {
          if (SimpleProductPage.isIncluded(productCatalog))
          {
            if (notificationButton.getURI() == null)
            {
              setBrandingNotificationScope(productCatalog);
            }
          }
        }
      }
    }
    else
    {
      String notificationLabel = null;
      URI notificationURI = getBrandingNotificationURI(scope);
      if (notificationURI != null)
      {
        notificationURI = URI.createURI(notificationURI.toString().replace("${installer.version}", URI.encodeQuery(SelfUpdate.getProductVersion(), true)));
        notificationLabel = getBrandingNotificationLabel(scope);

        notificationButton.setURI(notificationURI, isBrandingNotificationAnimated(scope));
        notificationButton.setToolTipText((notificationLabel == null ? "Like..." : notificationLabel) + " " + notificationURI);
      }
    }
  }

  private void toggleMenu()
  {
    SimpleInstallerMenu installerMenu = getInstallerMenu();
    boolean show = !installerMenu.isVisible();

    Installer installer = getInstaller();
    if (show)
    {
      installer.setConfigurationResources(Collections.<Resource> emptySet());
      SimpleInstallerPage topPage = getTopPage();
      if (topPage != null)
      {
        topPage.menuAboutToShow(installerMenu);
      }

      InstallerMenuItem extensionsItem = installerMenu.findMenuItemByName(EXTENSIONS_MENU_ITEM_TEXT);
      extensionsItem.setVisible(!installer.getAppliedConfigurationResources().isEmpty());

      InstallerMenuItem switchCatalogIndexItem = installerMenu.findMenuItemByName(SWITCH_CATALOG_INDEX_MENU_ITEM_TEXT);
      InstallerMenuItem catalogsMenuItem = installerMenu.findMenuItemByName(CATALOGS_MENU_ITEM_TEXT);
      InstallerMenuItem marketPlaceItem = installerMenu.findMenuItemByName(MARKET_PLACE_MENU_ITEM_TEXT);
      InstallerMenuItem applyConfigurationMenuItem = installerMenu.findMenuItemByName(APPLY_CONFIGURATION_MENU_ITEM_TEXT);
      if (applyConfigurationMenuItem == null)
      {
        applyConfigurationMenuItem = installerMenu.findMenuItemByName(APPLY_MARKET_PLACE_LISTING_MENU_ITEM_TEXT);
      }

      if (topPage == productPage)
      {
        Collection<? extends Resource> configurationResources = installer.getUnappliedConfigurationResources();
        URI indexLocation = ProjectPage.ConfigurationListener.getIndexURI(configurationResources);
        boolean visible = !configurationResources.isEmpty() && indexLocation == null;
        if (visible)
        {
          boolean isMarketPlaceListing = false;
          for (Resource resource : configurationResources)
          {
            if (MarketPlaceListing.isMarketPlaceListing(resource.getURI()))
            {
              isMarketPlaceListing = true;
              break;
            }
          }

          applyConfigurationMenuItem.setText(isMarketPlaceListing ? APPLY_MARKET_PLACE_LISTING_MENU_ITEM_TEXT : APPLY_CONFIGURATION_MENU_ITEM_TEXT);
          applyConfigurationMenuItem
              .setToolTipText(isMarketPlaceListing ? APPLY_MARKET_PLACE_LISTING_MENU_ITEM_DESCRIPTION : APPLY_CONFIGURATION_MENU_ITEM_DESCRIPTION);
        }

        applyConfigurationMenuItem.setVisible(visible);
        boolean switchCatalogVisible = indexLocation != null && !catalogManager.isCurrentIndex(indexLocation);
        switchCatalogIndexItem.setVisible(switchCatalogVisible);
        if (switchCatalogVisible)
        {
          switchCatalogIndexItem.setToolTipText("Switch to the catalog index from the clipboard: " + IndexManager.getUnderlyingLocation(indexLocation));
        }

        catalogsMenuItem.setVisible(showProductCatalogsItem);

        if (marketPlaceItem != null)
        {
          marketPlaceItem.setVisible(true);
        }
      }
      else
      {
        catalogsMenuItem.setVisible(false);
        applyConfigurationMenuItem.setVisible(false);
        switchCatalogIndexItem.setVisible(false);
        if (marketPlaceItem != null)
        {
          marketPlaceItem.setVisible(false);
        }
      }
    }

    installerMenu.layout(true, true);
    installerMenu.setVisible(show);
  }

  public Installer getInstaller()
  {
    return installer;
  }

  public void setButtonsEnabled(boolean enabled)
  {
    menuButton.setEnabled(enabled);
  }

  private void enablePool(boolean poolEnabled)
  {
    if (this.poolEnabled != poolEnabled)
    {
      this.poolEnabled = poolEnabled;
      PREF_POOL_ENABLED.set(poolEnabled);
    }

    if (poolEnabled)
    {
      pool = P2Util.getAgentManager().getDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName());
    }
    else
    {
      pool = null;
    }

    if (bundlePoolSwitch != null)
    {
      bundlePoolSwitch.setSelected(poolEnabled);
    }
  }

  public BundlePool getPool()
  {
    return pool;
  }

  private SimpleInstallerMenu getInstallerMenu()
  {
    if (installerMenu == null)
    {
      installerMenu = createInstallerMenu();
    }

    return installerMenu;
  }

  private SimpleInstallerMenu createInstallerMenu()
  {
    final SimpleInstallerMenu menu = new SimpleInstallerMenu(this);

    SimpleInstallerMenu.InstallerMenuItem updateInstallerItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    updateInstallerItem.setDefaultImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exclamation_circle.png"));
    updateInstallerItem.setHoverImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exclamation_circle_hover.png"));
    updateInstallerItem.setText(UPDATE_MENU_ITEM_TEXT);
    updateInstallerItem.setToolTipText("Install available updates");
    updateInstallerItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Runnable successRunnable = new Runnable()
        {
          public void run()
          {
            restart();
          }
        };

        ExceptionHandler<CoreException> exceptionHandler = new ExceptionHandler<CoreException>()
        {
          public void handleException(CoreException ex)
          {
            ErrorDialog.open(ex);
          }
        };

        SelfUpdate.update(getShell(), updateResolution, successRunnable, exceptionHandler, null);
      }
    });

    SimpleInstallerMenu.InstallerMenuItem applyConfigurationItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    applyConfigurationItem.setText(APPLY_CONFIGURATION_MENU_ITEM_TEXT);
    applyConfigurationItem.setToolTipText(APPLY_CONFIGURATION_MENU_ITEM_DESCRIPTION);
    applyConfigurationItem.setVisible(false);
    applyConfigurationItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        applyConfiguration();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem extensionsItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    extensionsItem.setDefaultImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/extension_circle.png"));
    extensionsItem.setHoverImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/extension_circle_hover.png"));
    extensionsItem.setText(EXTENSIONS_MENU_ITEM_TEXT);
    extensionsItem.setToolTipText(EXTENSIONS_MENU_ITEM_DESCRIPTION);
    extensionsItem.setVisible(false);
    extensionsItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Collection<? extends Resource> appliedConfigurationResources = installer.getAppliedConfigurationResources();
        ExtensionsDialog extensionsDialog = new ExtensionsDialog(getShell(), appliedConfigurationResources);
        if (extensionsDialog.open() == Window.OK)
        {
          Collection<? extends Resource> result = extensionsDialog.getResult();
          for (Resource resource : appliedConfigurationResources)
          {
            if (!result.contains(resource))
            {
              installer.removeAppliedConfigurationResource(resource);
            }
          }
        }
      }
    });

    SimpleInstallerMenu.InstallerMenuItem switchCatalogIndexItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    switchCatalogIndexItem.setText(SWITCH_CATALOG_INDEX_MENU_ITEM_TEXT);
    switchCatalogIndexItem.setVisible(false);
    switchCatalogIndexItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        applyConfiguration();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem catalogsItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    catalogsItem.setText(CATALOGS_MENU_ITEM_TEXT);
    catalogsItem.setToolTipText(ProductCatalogsDialog.DESCRIPTION);
    catalogsItem.setVisible(false);
    catalogsItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        ProductCatalogsDialog productCatalogsDialog = new ProductCatalogsDialog(SimpleInstallerDialog.this, getInstaller(), catalogManager);
        productCatalogsDialog.open();
      }
    });
    AccessUtil.setKey(catalogsItem, "catalogs");

    // Label spacer1 = new Label(menu, SWT.NONE);
    // spacer1.setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 46).create());

    SimpleInstallerMenu.InstallerMenuItem advancedModeItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    advancedModeItem.setText(ADVANCED_MENU_ITEM_TEXT);
    advancedModeItem.setToolTipText("Switch to advanced mode");
    advancedModeItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        switchToAdvancedMode();
      }
    });

    if (SHOW_BUNDLE_POOL_UI)
    {
      SimpleInstallerMenu.InstallerMenuItemWithToggle bundlePoolsItem = new SimpleInstallerMenu.InstallerMenuItemWithToggle(menu);
      bundlePoolsItem.setText(BUNDLE_POOLS_MENU_ITEM_TEXT);
      bundlePoolsItem.setToolTipText(AgentManagerDialog.MESSAGE);
      // bundlePoolsItem.setDividerVisible(false);
      bundlePoolsItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          manageBundlePools();
        }
      });

      bundlePoolSwitch = bundlePoolsItem.getToggleSwitch();
      bundlePoolSwitch.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          enablePool(bundlePoolSwitch.isSelected());
        }
      });
    }

    SimpleInstallerMenu.InstallerMenuItemWithToggle webLinksItem = new SimpleInstallerMenu.InstallerMenuItemWithToggle(menu);
    webLinksItem.setText(WEB_LINKS_MENU_ITEM_TEXT);
    webLinksItem.setToolTipText(WEB_LINKS_MENU_ITEM_DESCRIPTION);
    webLinksItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (URISchemeUtil.manageRegistrations(getShell()) == URISchemeUtil.RegistrationConfirmation.KEEP_INSTALLER)
        {
          close();
        }
        else
        {
          webLinksSwitch.setSelected(URISchemeUtil.isRegistered());
        }
      }
    });

    webLinksSwitch = webLinksItem.getToggleSwitch();
    webLinksSwitch.setSelected(URISchemeUtil.isRegistered());
    webLinksSwitch.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enable = webLinksSwitch.isSelected();
        if (enable)
        {
          switch (URISchemeUtil.confirmRegistration(getShell()))
          {
            case KEEP_INSTALLER:
            {
              showKeepInstaller(true);
              break;
            }
            case OK:
            {
              URISchemeUtil.setRegistered(true);
              break;
            }
            case DONE:
            case CANCEL:
            {
              break;
            }
          }
        }
        else
        {
          URISchemeUtil.setRegistered(false);
        }

        webLinksSwitch.setSelected(URISchemeUtil.isRegistered());
      }
    });

    if (MARKETPLACE_MENU_ITEM_ENABLED)
    {
      SimpleInstallerMenu.InstallerMenuItem marketPlaceItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
      marketPlaceItem.setText(MARKET_PLACE_MENU_ITEM_TEXT);
      marketPlaceItem.setToolTipText("Browse for marketplace listings and drag them onto the installer's title");
      marketPlaceItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser("https://marketplace.eclipse.org");
        }
      });
    }

    if (!StringUtil.isEmpty(FORUM_URL))
    {
      SimpleInstallerMenu.InstallerMenuItem askAQuestionItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
      askAQuestionItem.setText(ASK_A_QUESTION_MENU_ITEM_TEXT);
      askAQuestionItem.setToolTipText("Ask a question about the installer");
      askAQuestionItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser(FORUM_URL + "?version=" + URI.encodeQuery(SelfUpdate.getProductVersion(), false));
        }
      });
    }

    if (!StringUtil.isEmpty(PROBLEM_REPORT_URL))
    {
      SimpleInstallerMenu.InstallerMenuItem reportAProblemItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
      reportAProblemItem.setText(REPORT_A_PROBLEM_MENU_ITEM_TEXT);
      reportAProblemItem.setToolTipText("Report a problem with the installer");
      reportAProblemItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser(PROBLEM_REPORT_URL + "?version=" + URI.encodeQuery(SelfUpdate.getProductVersion(), false));
        }
      });
    }

    SimpleInstallerMenu.InstallerMenuItem aboutItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    aboutItem.setText(ABOUT_MENU_ITEM_TEXT);
    aboutItem.setToolTipText("Show information about this installer");
    aboutItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        showAbout();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem exitItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    exitItem.setText(EXIT_MENU_ITEM_TEXT);
    exitItem.setDividerVisible(false);
    exitItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        exitSelected();
      }
    });

    return menu;
  }

  public void setExtensionsAvailable(boolean available)
  {
    menuButton.setExtensionVisible(available);
  }

  private void updateAvailable(boolean available)
  {
    menuButton.setNotificationVisible(available);
    installerMenu.findMenuItemByName(UPDATE_MENU_ITEM_TEXT).setVisible(available);
    installerMenu.layout();
  }

  private void manageBundlePools()
  {
    final boolean[] enabled = { poolEnabled };

    AgentManagerDialog dialog = new AgentManagerDialog(getShell())
    {
      @Override
      protected void createUI(Composite parent)
      {
        final Button enabledButton = new Button(parent, SWT.CHECK);
        enabledButton.setText("Enable shared bundle pool");
        enabledButton.setSelection(poolEnabled);
        enabledButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            enabled[0] = enabledButton.getSelection();
            getComposite().setEnabled(enabled[0]);
          }
        });

        new Label(parent, SWT.NONE);
        super.createUI(parent);
        getComposite().setEnabled(poolEnabled);
      }

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
          button.setEnabled(getSelectedBundlePool() != null || !enabled[0]);
        }
      }
    };

    if (pool != null)
    {
      dialog.setSelectedPool(pool);
    }

    if (dialog.open() == AgentManagerDialog.OK)
    {
      enablePool(enabled[0]);
      pool = dialog.getSelectedBundlePool();
    }
  }

  public boolean refreshJREs()
  {
    if (variablePage != null)
    {
      return variablePage.refreshJREs();
    }

    return false;
  }

  public void showAbout()
  {
    String version = SelfUpdate.getProductVersion();
    new AboutDialog(getShell(), version).open();
  }

  @Override
  protected void runEventLoop()
  {
    getInstaller().runEventLoop(getShell());
  }

  public void productSelected(Product product)
  {
    variablePage.setProduct(product);
    switchToPage(variablePage);
  }

  public SimpleInstallerPage getTopPage()
  {
    return (SimpleInstallerPage)stackLayout.topControl;
  }

  private void doSwitch(final SimpleInstallerPage oldPage, final SimpleInstallerPage newPage)
  {
    if (oldPage != null)
    {
      oldPage.aboutToHide();

      if (newPage instanceof SimpleProductPage)
      {
        setBrandingNotificationScope(null);
      }
    }

    stackLayout.topControl = newPage;
    stack.layout();

    clearMessage();

    newPage.aboutToShow();
    newPage.setFocus();
  }

  private void switchToPage(final SimpleInstallerPage newPage)
  {
    if (newPage != null)
    {
      SimpleInstallerPage oldPage = pageStack.peek();
      if (oldPage == null || oldPage != newPage)
      {
        pageStack.push(newPage);
        doSwitch(oldPage, newPage);
      }
    }
  }

  public void switchToAdvancedMode()
  {
    installer.setConfigurationResources(Collections.<Resource> emptySet());
    setReturnCode(RETURN_ADVANCED);
    exitSelected();
  }

  public void restart()
  {
    setReturnCode(RETURN_RESTART);
    exitSelected();
  }

  protected void exitQuiet()
  {
    super.exitSelected();
  }

  @Override
  protected void exitSelected()
  {
    for (SimpleInstallerPage page : pageStack)
    {
      if (page instanceof SimpleVariablePage)
      {
        SimpleVariablePage variablePage = (SimpleVariablePage)page;
        if (!variablePage.promptLaunchProduct("You're about to exit the installer"))
        {
          return;
        }

        break;
      }
    }

    exitQuiet();
  }

  public void backSelected()
  {
    if (pageStack.size() <= 1)
    {
      return;
    }

    SimpleInstallerPage oldPage = pageStack.pop();
    SimpleInstallerPage newPage = pageStack.peek();

    doSwitch(oldPage, newPage);
  }

  public void showMessage(String message, SimpleMessageOverlay.Type type, boolean dismissAutomatically)
  {
    showMessage(message, type, dismissAutomatically, null);
  }

  public void showMessage(String message, SimpleMessageOverlay.Type type, boolean dismissAutomatically, Runnable action)
  {
    showMessage(message, type, dismissAutomatically, action, null);
  }

  public void showMessage(String message, SimpleMessageOverlay.Type type, boolean dismissAutomatically, Runnable action, Runnable closeAction)
  {
    // Check if we can reuse the current message to reduce flickering.
    if (currentMessage == null || currentMessage.getType() != type || currentMessage.isDismissAutomatically() != dismissAutomatically
        || currentMessage.getAction() != action || currentMessage.getCloseAction() != closeAction)
    {
      clearMessage();

      final Control originalFocusControl = getShell().getDisplay().getFocusControl();
      if (originalFocusControl != null)
      {
        UIUtil.asyncExec(originalFocusControl, new Runnable()
        {
          public void run()
          {
            Control focusControl = getShell().getDisplay().getFocusControl();
            if (focusControl != originalFocusControl)
            {
              originalFocusControl.setFocus();
            }
          }
        });
      }

      currentMessage = new SimpleMessageOverlay(this, type, new ControlRelocator()
      {
        public void relocate(Control control)
        {
          Rectangle bounds = SimpleInstallerDialog.this.getBounds();
          int x = bounds.x + 5;
          int y = bounds.y + 24;

          int width = bounds.width - 9;

          // Depending on the current page, the height varies.
          int height = pageStack.peek() instanceof SimpleProductPage ? 87 : 70;
          control.setBounds(x, y, width, height);
        }
      }, dismissAutomatically, action, closeAction);
    }

    currentMessage.setMessage(message);
    currentMessage.setVisible(true);
  }

  public void clearMessage()
  {
    if (currentMessage != null)
    {
      if (!currentMessage.isDisposed())
      {
        currentMessage.close();
      }

      currentMessage = null;
    }
  }

  public void showReadme(java.net.URI readmeURI)
  {
    if (readmePage != null)
    {
      readmePage.setReadmeURI(readmeURI);
      switchToPage(readmePage);
    }
    else
    {
      OS.INSTANCE.openSystemBrowser(readmeURI.toString());
    }
  }

  public void showInstallationLog(File installationLogFile)
  {
    installationLogPage.setInstallationLogFile(installationLogFile);
    switchToPage(installationLogPage);
  }

  public void showKeepInstaller(boolean startPermanentInstaller)
  {
    keepInstallerPage.setStartPermanentInstaller(startPermanentInstaller);
    switchToPage(keepInstallerPage);
  }

  @Override
  public void dispose()
  {
    // Ensure that no Browser widget has the focus. See bug 466902.
    menuButton.setFocus();
    super.dispose();
  }

  private static URI getBrandingNotificationURI(Scope scope)
  {
    if (scope != null)
    {
      Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      if (annotation != null)
      {
        String detail = annotation.getDetails().get(AnnotationConstants.KEY_NOTIFICATION_URI);
        if (detail != null)
        {
          return URI.createURI(detail);
        }
      }

      return getBrandingNotificationURI(scope.getParentScope());
    }

    return null;
  }

  private static String getBrandingNotificationLabel(Scope scope)
  {
    if (scope != null)
    {
      Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      if (annotation != null)
      {
        String detail = annotation.getDetails().get(AnnotationConstants.KEY_NOTIFICATION_LABEL);
        if (detail != null)
        {
          return detail;
        }
      }

      return getBrandingNotificationLabel(scope.getParentScope());
    }

    return null;
  }

  private static boolean isBrandingNotificationAnimated(Scope scope)
  {
    if (scope != null)
    {
      Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      if (annotation != null)
      {
        String detail = annotation.getDetails().get(AnnotationConstants.KEY_NOTIFICATION_ANIMATED);
        return "true".equals(detail);
      }

      return isBrandingNotificationAnimated(scope.getParentScope());
    }

    return false;
  }

  static URI getBrandingSiteURI(Scope scope)
  {
    if (scope != null)
    {
      Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      if (annotation != null)
      {
        String detail = annotation.getDetails().get(AnnotationConstants.KEY_SITE_URI);
        if (detail != null)
        {
          return URI.createURI(detail);
        }
      }

      return getBrandingSiteURI(scope.getParentScope());
    }

    return null;
  }

  public static Font getFont(int relativeHeight, String style)
  {
    String height = relativeHeight == 0 ? "" : relativeHeight > 0 ? "+" + relativeHeight : Integer.toString(relativeHeight);
    return SetupInstallerPlugin.getFont(getDefaultFont(), org.eclipse.emf.common.util.URI.createURI("font:///" + height + "/" + style));
  }

  static Font getDefaultFont()
  {
    if (defaultFont == null)
    {
      defaultFont = JFaceResources.getFont(SetupInstallerPlugin.FONT_LABEL_DEFAULT);
      if (defaultFont == null)
      {
        defaultFont = UIUtil.getDisplay().getSystemFont();
      }
    }

    return defaultFont;
  }

  static Point getDefaultSize(Drawable drawable)
  {
    if (defaultSize == null)
    {
      defaultSize = computeSize(drawable, getDefaultFont(), 37, 38);
    }

    return defaultSize;
  }

  static Point computeSize(Drawable drawable, Font font, int x, int y)
  {
    GC gc = new GC(drawable);
    gc.setFont(font);

    try
    {
      int height = gc.getFontMetrics().getHeight();
      int totalWidth = height * x;
      int totalHeight = height * y;
      return new Point(totalWidth, totalHeight);
    }
    finally
    {
      gc.dispose();
    }
  }

  static String getCSS()
  {
    if (css == null)
    {
      try
      {
        css = readBundleResource("html/css/simpleInstaller.css");
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    return css;
  }

  static String getPageTemplate()
  {
    if (pageTemplate == null)
    {
      try
      {
        pageTemplate = readBundleResource("html/PageTemplate.html");

        // Embed CSS
        pageTemplate = pageTemplate.replace("%INSTALLER_CSS%", SimpleInstallerDialog.getCSS());
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    return pageTemplate;
  }

  static String getProductTemplate()
  {
    if (productTemplate == null)
    {
      try
      {
        productTemplate = readBundleResource("html/ProductTemplate.html");
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    return productTemplate;
  }

  static String getProductTemplateLarge()
  {
    if (productTemplateLarge == null)
    {
      try
      {
        productTemplateLarge = readBundleResource("html/ProductTemplateLarge.html");
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    return productTemplateLarge;
  }

  private static String readBundleResource(final String name) throws IOException
  {
    try
    {
      BundleFile root = SetupInstallerPlugin.INSTANCE.getRootFile();
      BundleFile child = root.getChild(name);
      return child.getContentsString();
    }
    catch (Exception ex)
    {
      throw new IOExceptionWithCause(ex);
    }
  }

  /**
   * @author Ed Merks
   */
  private static class NotifictionButton extends FlatButton
  {
    static final Color COLOR_INSTALL_UNANIMATED = SetupInstallerPlugin.getColor(210, 210, 210);

    /**
     * The roundness of the button.
     */
    private static final int CORNER_WIDTH = 10;

    /**
     * The offset when the button is almost completely hidden.
     */
    private static final int MAX_OFFSET = 16;

    /**
     * Matches the size of the exit button;  assumes a 16x16 image.
     */
    private static final Point TOTAL_SIZE = new Point(46, 20);

    /**
     * The offset to center the image, assumed to be 16x16.
     */
    private static final int IMAGE_OFFSET = (TOTAL_SIZE.x - 16) / 2;

    /**
     * The current offset that determines how much of the button is showing.
     */
    private int offset = MAX_OFFSET;

    /**
     * Whether the button should be animated.
     */
    private boolean animated;

    /**
     * The timer executed runnable for animating this button.
     */
    private Runnable animator;

    /**
     * The URI to open.
     */
    private URI uri;

    /**
     * The direction in which the button animates.
     */
    private int direction = -1;

    public NotifictionButton(Composite parent)
    {
      super(parent, SWT.PUSH);

      setShowButtonDownState(false);
      setCornerWidth(CORNER_WIDTH);
      setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/white_star.png"));
      setBackground(COLOR_INSTALL_UNANIMATED);
      setToolTipText("Like...");
      setVisible(false);
      addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser(uri.toString());
          setURI(uri, false);
        }
      });
    }

    public URI getURI()
    {
      return uri;
    }

    public void setURI(URI uri, boolean animated)
    {
      if (!ObjectUtil.equals(uri, this.uri))
      {
        this.uri = uri;
        if (uri == null)
        {
          animator = null;
        }
        this.animated = animated;
        offset = animated ? MAX_OFFSET : 0;
        setVisible(uri != null);
        setBackground(animated ? SimpleInstallLaunchButton.COLOR_INSTALL : COLOR_INSTALL_UNANIMATED);
        redraw();
      }
    }

    @Override
    protected void onHover()
    {
      if (isHover())
      {
        animator = null;
        offset = 0;
        setBackground(SimpleInstallLaunchButton.COLOR_INSTALL);
      }
      else
      {
        setBackground(animated ? SimpleInstallLaunchButton.COLOR_INSTALL : COLOR_INSTALL_UNANIMATED);
        offset = animated ? MAX_OFFSET : 0;
      }
    }

    @Override
    protected void onFocusIn(Event event)
    {
      animator = null;
      offset = 0;
      setBackground(SimpleInstallLaunchButton.COLOR_INSTALL);
    }

    @Override
    protected void onFocusOut(Event event)
    {
      setBackground(animated ? SimpleInstallLaunchButton.COLOR_INSTALL : COLOR_INSTALL_UNANIMATED);
      offset = animated ? MAX_OFFSET : 0;
      redraw();
    }

    @Override
    protected Point getTotalSize()
    {
      return TOTAL_SIZE;
    }

    private int getEffectiveOffset()
    {
      return isHover() || isFocusControl() ? direction == 1 ? 0 : -CORNER_WIDTH : direction == 1 ? offset * direction : offset * direction - CORNER_WIDTH;
    }

    @Override
    protected void drawFocusState(GC gc, int x, int y, int width, int height)
    {
      super.drawFocusState(gc, x, y + getEffectiveOffset(), width, height + CORNER_WIDTH);
    }

    @Override
    public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
    {
      if (animated && animator == null)
      {
        animator = new Animator();
        UIUtil.timerExec(1000, animator);
      }

      super.drawBackground(gc, x, y + getEffectiveOffset(), width, height + CORNER_WIDTH, offsetX, offsetY);
    }

    @Override
    protected void drawImage(GC gc, int x, int y)
    {
      int effectiveOffset = getEffectiveOffset();
      if (effectiveOffset != MAX_OFFSET)
      {
        gc.drawImage(getImage(), x + IMAGE_OFFSET, y + effectiveOffset + (direction == 0 ? 0 : CORNER_WIDTH - 1));
      }
    }

    /**
     * @author Ed Merks
     */
    private class Animator implements Runnable
    {
      public void run()
      {
        if (animator == this && !isDisposed())
        {
          redraw();
          offset -= 1;
          if (offset == -1)
          {
            offset = MAX_OFFSET;
            UIUtil.timerExec(500, animator);
          }
          else if (offset == 0)
          {
            UIUtil.timerExec(2000, animator);
          }
          else
          {
            UIUtil.timerExec(100, animator);
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateSearcher extends Thread
  {
    private Display display;

    public UpdateSearcher(Display display)
    {
      super("Simple Update Searcher");
      this.display = display;
    }

    @Override
    public void run()
    {
      try
      {
        User user = getInstaller().getUser();
        updateResolution = SelfUpdate.resolve(user, null);
        if (updateResolution != null && !display.isDisposed())
        {
          display.asyncExec(new Runnable()
          {
            public void run()
            {
              updateAvailable(true);
            }
          });
        }
      }
      catch (CoreException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class PageStack extends LinkedList<SimpleInstallerPage>
  {
    private static final long serialVersionUID = 1L;

    @Override
    public void push(SimpleInstallerPage page)
    {
      addLast(page);
    }

    @Override
    public SimpleInstallerPage pop()
    {
      return removeLast();
    }

    @Override
    public SimpleInstallerPage peek()
    {
      return isEmpty() ? null : getLast();
    }
  }
}

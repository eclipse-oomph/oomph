/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.ui.wizards.CatalogSelector;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.IndexLoader;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.SpriteAnimator;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public class SimpleProductPage extends SimpleInstallerPage implements FilterHandler
{
  private static final int MAX_DESCRIPTION_LENGTH = 120;

  private static final String PRODUCT_PREFIX = "product://";

  private SimpleSearchField searchField;

  private ToolBar buttonBar;

  private CatalogSelector catalogSelector;

  private StackComposite stackComposite;

  private Browser browser;

  public SimpleProductPage(final Composite parent, final SimpleInstallerDialog dialog)
  {
    super(parent, dialog, false);
  }

  @Override
  protected void createContent(Composite container)
  {
    GridLayout searchLayout = UIUtil.createGridLayout(2);
    searchLayout.horizontalSpacing = 0;

    Composite searchComposite = new Composite(container, SWT.NONE);
    searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    searchComposite.setLayout(searchLayout);

    searchField = new SimpleSearchField(searchComposite, SimpleProductPage.this)
    {
      @Override
      protected void finishFilter()
      {
        browser.setFocus();
      }
    };

    searchField.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).hint(SWT.DEFAULT, 34).create());

    buttonBar = new ToolBar(searchComposite, SWT.FLAT | SWT.RIGHT);
    buttonBar.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).exclude(true).create());

    CatalogManager catalogManager = installer.getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, true);

    stackComposite = new StackComposite(container, SWT.NONE);
    stackComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).indent(0, 4).create());
    stackComposite.setBackgroundMode(SWT.INHERIT_FORCE);
    stackComposite.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    final SpriteIndexLoader indexLoader = new SpriteIndexLoader(stackComposite);

    browser = new Browser(stackComposite, SWT.NONE);
    browser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        String url = event.location;
        if (!"about:blank".equals(url))
        {
          if (url.startsWith(PRODUCT_PREFIX))
          {
            indexLoader.awaitIndexLoad();

            url = url.substring(PRODUCT_PREFIX.length());
            int lastSlash = url.lastIndexOf('/');

            String catalogName = url.substring(0, lastSlash);
            String productName = url.substring(lastSlash + 1);

            for (Scope scope : catalogSelector.getSelectedCatalogs())
            {
              if (scope instanceof ProductCatalog)
              {
                ProductCatalog productCatalog = (ProductCatalog)scope;
                if (catalogName.equals(productCatalog.getName()))
                {
                  for (Product product : productCatalog.getProducts())
                  {
                    if (productName.equals(product.getName()))
                    {
                      dialog.productSelected(product);
                    }
                  }
                }
              }
            }
          }
          else
          {
            OS.INSTANCE.openSystemBrowser(url);
          }

          event.doit = false;
        }
      }
    });

    stackComposite.setTopControl(indexLoader.getAnimator());

    installer.setIndexLoader(indexLoader);
    installer.setIndexLoadedAction(new Runnable()
    {
      public void run()
      {
        handleFilter("");
      }
    });
  }

  @Override
  public void aboutToHide()
  {
    super.aboutToHide();
    reset(); // TODO Use JavaScript, so that the browser doesn't scroll to top!
    setFocus();
  }

  @Override
  public boolean setFocus()
  {
    return browser.setFocus();
  }

  public void handleFilter(String filter)
  {
    String filterText = searchField.getFilterText();
    if (filterText.length() != 0)
    {
      filter = filterText;
    }

    StringBuilder productsBuilder = new StringBuilder();

    boolean noFilter = StringUtil.isEmpty(filter);
    if (!noFilter)
    {
      filter = filter.toLowerCase();
    }

    for (Scope scope : catalogSelector.getSelectedCatalogs())
    {
      if (scope instanceof ProductCatalog)
      {
        ProductCatalog productCatalog = (ProductCatalog)scope;
        for (Product product : productCatalog.getProducts())
        {
          if (!ProductPage.getValidProductVersions(product).isEmpty()
              && (noFilter || isFiltered(product.getName(), filter) || isFiltered(product.getLabel(), filter) || isFiltered(product.getDescription(), filter)))
          {
            productsBuilder.append(renderProduct(product, false));
          }
        }
      }
    }

    String productPageHTML = SimpleInstallerDialog.getProductTemplate();
    String simpleInstallerHTML = SimpleInstallerDialog.getPageTemplate();
    productPageHTML = simpleInstallerHTML.replace("%CONTENT%", productsBuilder.toString());
    browser.setText(productPageHTML, true);
  }

  public void reset()
  {
    browser.setText(browser.getText());
  }

  private static String removeLinks(String description)
  {
    return description.replaceAll("</?a[^>]*>", "");
  }

  private static int findFirstDot(String description)
  {
    boolean inElement = false;
    for (int i = 0; i < description.length(); i++)
    {
      char c = description.charAt(i);
      if (inElement)
      {
        if (c == '>')
        {
          inElement = false;
        }
      }
      else
      {
        if (c == '<')
        {
          inElement = true;
        }
        else if (c == '.')
        {
          return i;
        }
      }
    }

    return -1;
  }

  private static boolean isFiltered(String string, String filter)
  {
    if (string == null)
    {
      return false;
    }

    return string.toLowerCase().contains(filter);
  }

  public static String renderProduct(Product product, boolean large)
  {
    String imageURI = ProductPage.getProductImageURI(product);

    String label = product.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = product.getName();
    }

    String description = product.getDescription();
    if (description != null)
    {
      int dot = findFirstDot(description);
      if (dot == -1)
      {
        description += ".";
      }
      else
      {
        description = description.substring(0, dot + 1);
      }
    }
    else
    {
      // TODO: Empty string? Or something like "No description available"?
      description = "";
    }

    String productHtml = large ? SimpleInstallerDialog.getProductTemplateLarge() : SimpleInstallerDialog.getProductTemplate();

    if (!large)
    {
      description = StringUtil.shorten(description, MAX_DESCRIPTION_LENGTH, true);
      description = removeLinks(description);

      String productLink = "product://" + product.getProductCatalog().getName() + "/" + product.getName();
      productHtml = productHtml.replace("%PRODUCT_LINK%", productLink);
    }

    productHtml = productHtml.replace("%PRODUCT_ICON_SRC%", imageURI);
    productHtml = productHtml.replace("%PRODUCT_TITLE%", label);
    productHtml = productHtml.replace("%PRODUCT_DESCRIPTION%", description);
    return productHtml;
  }

  /**
   * @author Eike Stepper
   */
  private final class SpriteIndexLoader extends IndexLoader
  {
    private final SpriteAnimator animator;

    public SpriteIndexLoader(Composite parent)
    {
      animator = new SpriteAnimator(parent, SWT.NO_BACKGROUND, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/progress_sprite.png"), 32, 32, 20);
    }

    public SpriteAnimator getAnimator()
    {
      return animator;
    }

    @Override
    public void loadIndex(final ResourceSet resourceSet, final org.eclipse.emf.common.util.URI... uris)
    {
      searchField.setEnabled(false);

      browser.setText("", true);
      stackComposite.setTopControl(animator);
      animator.start(1, animator.getImages().length - 1);

      final IProgressMonitor monitor = new NullProgressMonitor();

      Thread thread = new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            loadIndex(resourceSet, uris, monitor);
          }
          catch (InvocationTargetException ex)
          {
            if (!animator.isDisposed())
            {
              SetupInstallerPlugin.INSTANCE.log(ex.getCause());
            }
          }
          catch (InterruptedException ex)
          {
            //$FALL-THROUGH$
          }
          finally
          {
            UIUtil.asyncExec(new Runnable()
            {
              public void run()
              {
                stackComposite.setTopControl(browser);
                browser.setFocus();

                CatalogManager catalogManager = catalogSelector.getCatalogManager();
                Index index = catalogManager.getIndex();
                if (index == null)
                {
                  int answer = new MessageDialog(getShell(), "Network Problem", null,
                      "The catalog could not be loaded. Please ensure that you have network access and, if needed, have configured your network proxy.",
                      MessageDialog.ERROR, new String[] { "Retry", "Configure Network Proxy" + StringUtil.HORIZONTAL_ELLIPSIS, "Exit" }, 0).open();
                  switch (answer)
                  {
                    case 0:
                      installer.reloadIndex();
                      return;

                    case 1:
                      new NetworkConnectionsDialog(getShell()).open();
                      installer.reloadIndex();
                      return;

                    default:
                      dialog.exitSelected();
                      return;
                  }
                }

                searchField.setEnabled(true);
              }
            });

            animator.stop();
          }
        }
      };

      thread.setDaemon(true);
      thread.start();
    }

    @Override
    protected void waiting()
    {
      stackComposite.setTopControl(animator);
      animator.start(1, animator.getImages().length - 1);
    }

    @Override
    protected void finishedWaiting()
    {
      stackComposite.setTopControl(browser);
    }

    @Override
    protected boolean shouldReload(EClass eClass)
    {
      return eClass == SetupPackage.Literals.INDEX || eClass == SetupPackage.Literals.PRODUCT_CATALOG || eClass == SetupPackage.Literals.PRODUCT;
    }
  }
}

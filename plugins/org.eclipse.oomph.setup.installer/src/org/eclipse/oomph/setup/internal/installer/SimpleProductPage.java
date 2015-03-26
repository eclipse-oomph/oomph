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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.ui.wizards.CatalogSelector;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.IndexLoader;
import org.eclipse.oomph.ui.SearchField;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.SpriteAnimator;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class SimpleProductPage extends SimpleInstallerPage implements FilterHandler
{
  private static final String PRODUCT_PREFIX = "product://";

  private static final String downloadImageURI = ProductPage.getImageURI(SetupInstallerPlugin.INSTANCE, "simple/download.png");

  private static final String downloadHoverImageURI = ProductPage.getImageURI(SetupInstallerPlugin.INSTANCE, "simple/download_hover.png");

  private static final String downloadActiveImageURI = ProductPage.getImageURI(SetupInstallerPlugin.INSTANCE, "simple/download_active.png");

  private static final boolean FANCY = false; // OS.INSTANCE.isWin();

  private SearchField searchField;

  private ToolBar buttonBar;

  private ToolItem refreshButton;

  private ToolItem catalogsButton;

  private CatalogSelector catalogSelector;

  private StackComposite stackComposite;

  private Browser browser;

  public SimpleProductPage(final Composite parent, int style, final SimpleInstallerDialog dialog)
  {
    super(parent, style, dialog);

    GridLayout layout = UIUtil.createGridLayout(1);
    layout.verticalSpacing = 20;
    setLayout(layout);

    GridLayout searchLayout = UIUtil.createGridLayout(2);
    searchLayout.marginWidth = SimpleInstallerDialog.MARGIN_WIDTH;

    Composite searchComposite = new Composite(this, SWT.NONE);
    searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    searchComposite.setLayout(searchLayout);

    GridData searchFieldData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    searchFieldData.widthHint = 350;

    searchField = new SearchField(searchComposite, SimpleProductPage.this)
    {
      @Override
      protected void finishFilter()
      {
        browser.setFocus();
      }
    };
    searchField.setLayoutData(searchFieldData);
    searchField.getFilterControl().setFont(font);

    buttonBar = new ToolBar(searchComposite, SWT.FLAT | SWT.RIGHT);
    buttonBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

    CatalogManager catalogManager = installer.getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, true);

    refreshButton = new ToolItem(buttonBar, SWT.NONE);
    refreshButton.setToolTipText("Refresh");
    refreshButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/refresh.png"));
    refreshButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        installer.reloadIndex();
      }
    });
    AccessUtil.setKey(refreshButton, "refresh");

    stackComposite = new StackComposite(this, SWT.NONE);
    stackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    SpriteIndexLoader indexLoader = new SpriteIndexLoader(stackComposite);

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
  public boolean setFocus()
  {
    return browser.setFocus();
  }

  public void handleFilter(String filter)
  {
    String filterText = searchField.getFilterControl().getText();
    if (filterText.length() != 0)
    {
      filter = filterText;
    }

    StringBuilder builder = new StringBuilder();
    builder.append("<html><style TYPE=\"text/css\"><!-- ");
    builder.append("table{width:100%; border:none; border-collapse:collapse}");
    builder.append(".label{font-size:1.1em; font-weight:700}");
    builder.append(".description{font-size:14px; color:#333}");
    builder.append(".col{padding:10px; border-top:1px solid #bbbbbb; border-bottom:1px solid #bbbbbb}");
    builder.append(".col1{text-align:center}");
    builder.append(".col2{width:100%}");
    builder.append(".col3{text-align:center}");
    builder.append(".zebra{background-color:#fafafa}");
    if (FANCY)
    {
      builder.append("a.dl{background-image:url('" + downloadImageURI
          + "'); background-repeat:no-repeat; background-position:top left; width:57px; height:56px}");
      builder.append("a.dl:hover{background-image:url('" + downloadHoverImageURI + "')}");
      builder.append("a.dl:active{background-image:url('" + downloadActiveImageURI + "')}");
    }
    else
    {
      builder.append("img.dl{border-style:none}");
    }

    builder.append(" --></style><body style=\"margin:0px; overflow:auto; font-family:'Open Sans','Helvetica Neue',Helvetica,Arial,sans-serif\"><table>\n");

    boolean noFilter = StringUtil.isEmpty(filter);
    if (!noFilter)
    {
      filter = filter.toLowerCase();
    }

    boolean zebra = true;
    for (Scope scope : catalogSelector.getSelectedCatalogs())
    {
      if (scope instanceof ProductCatalog)
      {
        ProductCatalog productCatalog = (ProductCatalog)scope;
        for (Product product : productCatalog.getProducts())
        {
          if (noFilter || isFiltered(product.getName(), filter) || isFiltered(product.getLabel(), filter) || isFiltered(product.getDescription(), filter))
          {
            renderProduct(builder, product, zebra, downloadImageURI);
            zebra = !zebra;
          }
        }
      }
    }

    String html = getHtml(builder);

    // try
    // {
    // IOUtil.writeUTF8(new File("/develop/products.html"), html);
    // }
    // catch (Exception ex)
    // {
    // ex.printStackTrace();
    // }

    browser.setText(html, true);
  }

  public void reset()
  {
    browser.setText(browser.getText());
  }

  private void createCatalogsButton()
  {
    if (catalogsButton == null)
    {
      catalogsButton = new ToolItem(buttonBar, SWT.DROP_DOWN);
      catalogsButton.setToolTipText("Select Catalogs");
      catalogsButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"));

      CatalogManager catalogManager = catalogSelector.getCatalogManager();
      catalogManager.getSelection().eAdapters().add(new AdapterImpl()
      {
        @Override
        public void notifyChanged(Notification msg)
        {
          handleFilter("");
        }
      });

      catalogSelector.configure(catalogsButton);
      AccessUtil.setKey(catalogsButton, "catalogs");
    }
  }

  private void disposeCatalogsButton()
  {
    if (catalogsButton != null)
    {
      catalogsButton.dispose();
      catalogsButton = null;
    }
  }

  public static String getHtml(StringBuilder builder)
  {
    builder.append("</table></body></html>\n");
    return builder.toString();
  }

  public static void renderProduct(StringBuilder builder, Product product, boolean zebra, String downloadImageURI)
  {
    String imageURI = ProductPage.getProductImageURI(product);

    String description = product.getDescription();
    if (description != null && downloadImageURI != null)
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

    String label = product.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = product.getName();
    }

    builder.append("<tr class=\"row" + (zebra ? " zebra" : "") + "\">");

    builder.append("<td class=\"col col1\"><img src=\"");
    builder.append(imageURI);
    builder.append("\" width=\"42\" height=\"42\"></img></td>");

    builder.append("<td class=\"col col2\"><p class=\"label\">");
    builder.append(label);
    builder.append("</p>");

    if (description != null)
    {
      builder.append("<p class=\"description\">");
      builder.append(description);
      builder.append("</p></td>");
    }

    if (downloadImageURI != null)
    {
      if (FANCY)
      {
        builder.append("<td class=\"col col3\"><a class=\"dl\" href=\"product://" + product.getProductCatalog().getName() + "/" + product.getName()
            + "\" title=\"Select\"/></td>");
      }
      else
      {
        builder.append("<td class=\"col col3\"><a class=\"dl\" href=\"product://" + product.getProductCatalog().getName() + "/" + product.getName()
            + "\" title=\"Select\"><img class=\"dl\" src=\"" + downloadImageURI + "\"/></a></td>");
      }
    }

    builder.append("</tr>\n");
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
      refreshButton.setEnabled(false);

      if (catalogsButton != null)
      {
        catalogsButton.setEnabled(false);
      }

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
                refreshButton.setEnabled(true);
                stackComposite.setTopControl(browser);
                browser.setFocus();

                CatalogManager catalogManager = catalogSelector.getCatalogManager();
                Index index = catalogManager.getIndex();
                if (index == null)
                {
                  disposeCatalogsButton();
                  int answer = new MessageDialog(getShell(), "Network Problem", null,
                      "The catalog could not be loaded. Please ensure that you have network access and, if needed, have configured your network proxy.",
                      MessageDialog.ERROR, new String[] { "Retry", "Configure Network Proxy...", "Exit" }, 0).open();
                  switch (answer)
                  {
                    case 0:
                      installer.reloadIndex();
                      return;

                    case 1:
                      new ProxyPreferenceDialog(getShell()).open();
                      installer.reloadIndex();
                      return;

                    default:
                      dialog.exitSelected();
                      return;
                  }
                }

                searchField.setEnabled(true);

                List<? extends Scope> productCatalogs = catalogManager.getCatalogs(true);
                if (productCatalogs != null && productCatalogs.size() >= 3) // Self products + 2 more catalogs
                {
                  createCatalogsButton();
                  catalogsButton.setEnabled(true);
                }
                else
                {
                  disposeCatalogsButton();
                }

                buttonBar.getParent().layout();
              }
            });

            animator.stop();
          }
        }
      };

      thread.setDaemon(true);
      thread.start();
    }
  }
}

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

import org.eclipse.oomph.base.provider.BaseEditUtil;
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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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

  private SpriteIndexLoader indexLoader;

  private ProductList productList;

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
        SimpleProductPage.this.setFocus();
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

    indexLoader = new SpriteIndexLoader(stackComposite);
    productList = new CompositeProductList(this, stackComposite);
    // productList = new BrowserProductList(this, stackComposite, catalogSelector);

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
    productList.reset(); // TODO Use JavaScript, so that the browser doesn't scroll to top!
    setFocus();
  }

  @Override
  public boolean setFocus()
  {
    return productList.getControl().setFocus();
  }

  public void handleFilter(String filter)
  {
    String filterText = searchField.getFilterText();
    if (filterText.length() != 0)
    {
      filter = filterText;
    }

    boolean noFilter = StringUtil.isEmpty(filter);
    if (!noFilter)
    {
      filter = filter.toLowerCase();
    }

    List<Product> products = new ArrayList<Product>();
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
            products.add(product);
          }
        }
      }
    }

    productList.setInput(products);
  }

  protected final void productSelected(Product product)
  {
    indexLoader.awaitIndexLoad();
    dialog.productSelected(product);
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
    URI imageURI = ProductPage.getProductImageURI(product);

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

    productHtml = productHtml.replace("%PRODUCT_ICON_SRC%", imageURI.toString());
    productHtml = productHtml.replace("%PRODUCT_TITLE%", label);
    productHtml = productHtml.replace("%PRODUCT_DESCRIPTION%", description);
    return productHtml;
  }

  /**
   * @author Eike Stepper
   */
  protected static abstract class ProductList
  {
    private final SimpleProductPage page;

    public ProductList(SimpleProductPage page)
    {
      this.page = page;
    }

    public abstract Control getControl();

    public abstract void setInput(List<Product> products);

    public abstract void reset();

    protected final void productSelected(Product product)
    {
      page.productSelected(product);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class BrowserProductList extends ProductList
  {
    private final Browser browser;

    public BrowserProductList(SimpleProductPage page, StackComposite stackComposite, final CatalogSelector catalogSelector)
    {
      super(page);
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
              productSelected(url, catalogSelector);
            }
            else
            {
              OS.INSTANCE.openSystemBrowser(url);
            }

            event.doit = false;
          }
        }
      });
    }

    @Override
    public Control getControl()
    {
      return browser;
    }

    @Override
    public void setInput(List<Product> products)
    {
      StringBuilder productsBuilder = new StringBuilder();
      for (Product product : products)
      {
        productsBuilder.append(renderProduct(product, false));
      }

      String productPageHTML = SimpleInstallerDialog.getProductTemplate();
      String simpleInstallerHTML = SimpleInstallerDialog.getPageTemplate();
      productPageHTML = simpleInstallerHTML.replace("%CONTENT%", productsBuilder.toString());
      browser.setText(productPageHTML, true);
    }

    @Override
    public void reset()
    {
      browser.setText(browser.getText());
    }

    private void productSelected(String url, CatalogSelector catalogSelector)
    {
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
                productSelected(product);
              }
            }
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class CompositeProductList extends ProductList
  {
    private static final int SPACE = 3;

    private final ScrolledComposite scrolledComposite;

    private final Composite scrolledContent;

    private List<Product> products;

    public CompositeProductList(SimpleProductPage page, StackComposite stackComposite)
    {
      super(page);
      scrolledComposite = new ScrolledComposite(stackComposite, SWT.V_SCROLL);
      scrolledComposite.setExpandHorizontal(true);
      scrolledComposite.setExpandVertical(true);

      GridLayout gridLayout = new GridLayout(1, false);
      gridLayout.marginWidth = 0;
      gridLayout.marginHeight = 0;
      gridLayout.verticalSpacing = SPACE;

      scrolledContent = new Composite(scrolledComposite, SWT.NONE);
      scrolledContent.setLayout(gridLayout);
      scrolledContent.setBackground(SimpleProductPage.COLOR_PAGE_BORDER);

      // Workaround for bug 93472 (Content of ScrolledComposite doesn't get scrolled by mousewheel).
      // Setting the focus on the scroller doesn't work, that is why we forward the mouse wheel event.
      scrolledContent.addListener(SWT.MouseVerticalWheel, new Listener()
      {
        public void handleEvent(Event event)
        {
          int value = event.count * SimpleInstallationLogPage.SCROLL_SPEED;
          ScrollBar vbar = scrolledComposite.getVerticalBar();
          vbar.setSelection(vbar.getSelection() - value);

          Listener[] selectionListeners = vbar.getListeners(SWT.Selection);
          for (Listener listener : selectionListeners)
          {
            listener.handleEvent(event);
          }
        }
      });

      scrolledComposite.setContent(scrolledContent);
    }

    @Override
    public Control getControl()
    {
      return scrolledComposite;
    }

    @Override
    public void setInput(final List<Product> products)
    {
      this.products = products;

      Control[] children = scrolledContent.getChildren();
      for (int i = children.length - 1; i >= 0; --i)
      {
        children[i].dispose();
      }

      if (products != null)
      {
        for (Product product : products)
        {
          new ProductComposite(scrolledContent, this, product);
        }

        int size = products.size();
        int height = size * ProductComposite.TOTAL_HEIGHT;
        if (size > 0)
        {
          height += (size - 1) * SPACE;
        }

        scrolledComposite.setMinHeight(height);
      }
      else
      {
        scrolledComposite.setMinHeight(0);
      }

      scrolledContent.layout();
    }

    @Override
    public void reset()
    {
      setInput(products);
    }

    /**
     * @author Eike Stepper
     */
    private static final class ProductComposite extends Composite implements MouseTrackListener, MouseListener
    {
      private static final int BORDER = 17;

      private static final int HEIGHT = 64;

      private static final int TOTAL_HEIGHT = HEIGHT + 2 * BORDER;

      private static final Color COLOR_WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

      private static final Color COLOR_TITLE = UIUtil.getEclipseThemeColor();

      private static final Color COLOR_DESCRIPTION = SetupInstallerPlugin.getColor(85, 85, 85);

      private static final Color COLOR_SELECTION = SetupInstallerPlugin.getColor(174, 187, 221);

      private static final Font FONT_TITLE = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///12/bold"));

      private static final Font FONT_DESCRIPTION = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///10/normal"));

      private final Product product;

      private final CompositeProductList list;

      public ProductComposite(Composite parent, CompositeProductList list, final Product product)
      {
        super(parent, SWT.NONE);
        this.list = list;
        this.product = product;

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.minimumHeight = TOTAL_HEIGHT;
        gridData.heightHint = gridData.minimumHeight;

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = BORDER;
        gridLayout.marginHeight = BORDER;
        gridLayout.horizontalSpacing = BORDER;
        gridLayout.verticalSpacing = 10;

        setLayoutData(gridData);
        setLayout(gridLayout);

        setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        setCursor(getDisplay().getSystemCursor(SWT.CURSOR_HAND));
        listenToMouse(this);

        URI imageURI = ProductPage.getProductImageURI(product);
        Image image = ExtendedImageRegistry.INSTANCE.getImage(BaseEditUtil.getImage(imageURI));

        Logo logo = new Logo(this, image);
        logo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
        listenToMouse(logo);

        Label title = new Label(this, SWT.WRAP);
        title.setForeground(COLOR_TITLE);
        title.setFont(FONT_TITLE);
        title.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        title.setText(product.getLabel());
        listenToMouse(title);

        final Label description = new Label(this, SWT.WRAP);
        description.setForeground(COLOR_DESCRIPTION);
        description.setFont(FONT_DESCRIPTION);
        description.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        listenToMouse(description);

        description.addControlListener(new ControlAdapter()
        {
          @Override
          public void controlResized(ControlEvent e)
          {
            setDescription(description, product);
          }
        });
      }

      private void setDescription(Label label, Product product)
      {
        int width = label.getSize().x;
        GC gc = new GC(label);

        try
        {
          String text = shorten(gc, width, product.getDescription());
          label.setText(text);
          label.getParent().layout();
        }
        finally
        {
          gc.dispose();
        }
      }

      private static String shorten(GC gc, int width, String html)
      {
        String plain = StringUtil.isEmpty(html) ? "No description available." : stripHTML(html);

        StringBuilder builder = new StringBuilder();
        int lineWidth = 0;
        int lines = 1;

        String[] words = plain.split(" ");
        for (String word : words)
        {
          int wordWidth = gc.textExtent(word + " ").x;
          lineWidth += wordWidth;
          if (lineWidth > width)
          {
            if (++lines > 2)
            {
              int length = builder.length();
              builder.replace(length - 1, length, "...");
              break;
            }

            lineWidth = wordWidth;
          }

          builder.append(word);
          builder.append(" ");
        }

        return builder.toString();
      }

      private static String stripHTML(String html)
      {
        try
        {
          final StringBuilder builder = new StringBuilder();
          new ParserDelegator().parse(new StringReader(html), new HTMLEditorKit.ParserCallback()
          {
            @Override
            public void handleText(char[] text, int pos)
            {
              builder.append(text);
            }
          }, Boolean.TRUE);

          return builder.toString();
        }
        catch (IOException ex)
        {
          return html;
        }
      }

      private void listenToMouse(Control control)
      {
        control.addMouseTrackListener(this);
        control.addMouseListener(this);
      }

      public void mouseEnter(MouseEvent e)
      {
        setBackground(COLOR_SELECTION);
      }

      public void mouseExit(MouseEvent e)
      {
        setBackground(COLOR_WHITE);
      }

      public void mouseHover(MouseEvent e)
      {
        // Do nothing.
      }

      public void mouseDoubleClick(MouseEvent e)
      {
        // Do nothing.
      }

      public void mouseDown(MouseEvent e)
      {
        // Do nothing.
      }

      public void mouseUp(MouseEvent e)
      {
        if (getClientArea().contains(e.x, e.y))
        {
          list.productSelected(product);
        }
      }

      /**
       * @author Eike Stepper
       */
      private static final class Logo extends Composite implements PaintListener
      {
        private final Image image;

        private int imageX;

        private int imageY;

        public Logo(Composite parent, Image image)
        {
          super(parent, SWT.DOUBLE_BUFFERED);
          this.image = image;

          ImageData imageData = image.getImageData();
          imageX = (HEIGHT - imageData.width) / 2;
          imageY = (HEIGHT - imageData.height) / 2;

          addPaintListener(this);
          setSize(HEIGHT, HEIGHT);
        }

        public void paintControl(PaintEvent e)
        {
          Rectangle rect = getClientArea();
          GC gc = e.gc;

          int oldAntialias = gc.getAntialias();
          gc.setAntialias(SWT.ON);

          Color oldBackground = gc.getBackground();
          gc.setBackground(COLOR_PAGE_BORDER);

          gc.fillOval(0, 0, rect.width, rect.height);
          gc.drawImage(image, imageX, imageY);

          gc.setBackground(oldBackground);
          gc.setAntialias(oldAntialias);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SpriteIndexLoader extends IndexLoader
  {
    private final SpriteAnimator animator;

    public SpriteIndexLoader(Composite parent)
    {
      animator = new SpriteAnimator(parent, SWT.NONE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/progress_sprite.png"), 8, 4, 20);
    }

    public SpriteAnimator getAnimator()
    {
      return animator;
    }

    @Override
    public void loadIndex(final ResourceSet resourceSet, final org.eclipse.emf.common.util.URI... uris)
    {
      searchField.setEnabled(false);

      productList.reset();
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
                stackComposite.setTopControl(productList.getControl());
                setFocus();

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
      Control control = productList.getControl();
      stackComposite.setTopControl(control);
    }

    @Override
    protected boolean shouldReload(EClass eClass)
    {
      return eClass == SetupPackage.Literals.INDEX || eClass == SetupPackage.Literals.PRODUCT_CATALOG || eClass == SetupPackage.Literals.PRODUCT;
    }
  }
}

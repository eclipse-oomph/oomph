/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
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

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.installer.SimpleProductPage.ProductList.BrowserProductList;
import org.eclipse.oomph.setup.internal.installer.SimpleProductPage.ProductList.CompositeProductList;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.wizards.CatalogSelector;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.IndexLoader;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.SpriteAnimator;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class SimpleProductPage extends SimpleInstallerPage implements FilterHandler
{
  private static final int MAX_DESCRIPTION_LENGTH = 120;

  private static final String PRODUCT_PREFIX = "product://";

  private final SelectionMemento selectionMemento;

  private SimpleSearchField searchField;

  private ToolBar buttonBar;

  private CatalogSelector catalogSelector;

  private StackComposite stackComposite;

  private SpriteIndexLoader indexLoader;

  private ProductList productList;

  public SimpleProductPage(final Composite parent, final SimpleInstallerDialog dialog, SelectionMemento selectionMemento)
  {
    super(parent, dialog, false);
    this.selectionMemento = selectionMemento;
  }

  @Override
  protected void createContent(final Composite container)
  {
    SetupTransferSupport.DropListener dropListener = new SetupTransferSupport.DropListener()
    {
      public void resourcesDropped(Collection<? extends Resource> resources)
      {
        dialog.getInstaller().setConfigurationResources(resources);
        dialog.applyConfiguration();
      }
    };

    installer.getTransferSupport().addDropListener(dropListener);

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

    Point defaultSearchFieldSize = searchField.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    searchField
        .setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).hint(SWT.DEFAULT, defaultSearchFieldSize.y + 10).create());

    buttonBar = new ToolBar(searchComposite, SWT.FLAT | SWT.RIGHT);
    buttonBar.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).exclude(true).create());

    CatalogManager catalogManager = installer.getCatalogManager();
    catalogSelector = new CatalogSelector(catalogManager, true);

    stackComposite = new StackComposite(container, SWT.NONE);
    stackComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).indent(0, 4).create());
    stackComposite.setBackgroundMode(SWT.INHERIT_FORCE);
    stackComposite.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    indexLoader = new SpriteIndexLoader(stackComposite);

    if (UIUtil.isBrowserAvailable() && "browser".equals(PropertiesUtil.getProperty("oomph.product.list")))
    {
      productList = new BrowserProductList(this, stackComposite, catalogSelector);
    }
    else
    {
      productList = new CompositeProductList(this, stackComposite);
    }

    stackComposite.setTopControl(indexLoader.getAnimator());

    installer.setIndexLoader(indexLoader);
    installer.setIndexLoadedAction(new Runnable()
    {
      private final AtomicBoolean selectionMementoTried = new AtomicBoolean();

      public void run()
      {
        handleFilter("");

        UIUtil.asyncExec(container, new Runnable()
        {
          public void run()
          {
            if (!selectionMementoTried.getAndSet(true))
            {
              if (applySelectionMemento())
              {
                return;
              }
            }
          }
        });
      }
    });

    final CatalogSelection selection = catalogManager.getSelection();
    final Adapter selectionAdapter = new AdapterImpl()
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        if (notification.getFeature() == SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS)
        {
          handleFilter("");
        }
      }
    };

    selection.eAdapters().add(selectionAdapter);
    container.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        selection.eAdapters().remove(selectionAdapter);
      }
    });
  }

  @Override
  public void aboutToShow()
  {
    super.aboutToShow();
    installer.getTransferSupport().addControl(dialog.getChildren()[0]);
  }

  @Override
  public void aboutToHide()
  {
    super.aboutToHide();
    productList.reset(false); // TODO Use JavaScript, so that the browser doesn't scroll to top!
    setFocus();

    installer.getTransferSupport().removeControls();
  }

  @Override
  protected void menuAboutToShow(SimpleInstallerMenu menu)
  {
    Collection<? extends Resource> resources = installer.getTransferSupport().getResources();
    dialog.getInstaller().setConfigurationResources(resources);
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
        if (isIncluded(productCatalog))
        {
          for (Product product : productCatalog.getProducts())
          {
            if (isIncluded(product) && (noFilter || isFiltered(product.getName(), filter) || isFiltered(product.getLabel(), filter)
                || isFiltered(product.getDescription(), filter)))
            {
              products.add(product);
            }
          }
        }
      }
    }

    productList.setInput(products);
  }

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
        productSelected(product);
        return true;
      }
    }

    return false;
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
    String imageURI = SetupWizard.getLocalBrandingImageURI(product);

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
  public static abstract class ProductList
  {
    private final SimpleProductPage page;

    public ProductList(SimpleProductPage page)
    {
      this.page = page;
    }

    public abstract Control getControl();

    public abstract void setInput(List<Product> products);

    public abstract void reset(boolean clear);

    protected final void productSelected(Product product)
    {
      page.productSelected(product);
    }

    /**
     * @author Eike Stepper
     */
    public static class BrowserProductList extends ProductList
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
      public void reset(boolean clear)
      {
        browser.setText(clear ? "" : browser.getText());
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
    public static final class CompositeProductList extends ProductList
    {
      public static final int SPACE = 3;

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

        int listHeight = 0;

        if (products != null)
        {
          Cursor handCursor = scrolledContent.getDisplay().getSystemCursor(SWT.CURSOR_HAND);

          for (Product product : products)
          {
            ProductComposite productComposite = new ProductComposite(scrolledContent, this, product);
            int height = productComposite.getTotalHeight();

            if (listHeight != 0)
            {
              listHeight += SPACE;
            }

            listHeight += height;

            GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
            gridData.minimumHeight = height;
            gridData.heightHint = height;

            productComposite.setLayoutData(gridData);
            productComposite.setCursor(handCursor);
          }
        }

        scrolledComposite.setMinHeight(listHeight);
        scrolledContent.layout();
      }

      @Override
      public void reset(boolean clear)
      {
        setInput(clear ? null : products);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class ProductComposite extends Composite implements MouseTrackListener, MouseListener
  {
    public static final int BORDER = 17;

    private static final int VERTICAL_SPACE = 10;

    private static final Color COLOR_WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

    private static final Color COLOR_TITLE = UIUtil.getEclipseThemeColor();

    private static final Color COLOR_DESCRIPTION = SetupInstallerPlugin.getColor(85, 85, 85);

    private static final Color COLOR_SELECTION = SetupInstallerPlugin.getColor(174, 187, 221);

    private static final Font FONT_TITLE = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///+4/bold"));

    private static final Font FONT_DESCRIPTION = SimpleInstallerDialog.getFont(1, "normal");

    private final CompositeProductList list;

    private Product product;

    private Logo logo;

    private Label title;

    private Label description;

    private int contentHeight;

    public ProductComposite(Composite parent, CompositeProductList list, final Product product)
    {
      super(parent, SWT.NONE);
      this.list = list;

      GridLayout gridLayout = new GridLayout(2, false);
      gridLayout.marginWidth = BORDER;
      gridLayout.marginHeight = BORDER;
      gridLayout.horizontalSpacing = BORDER;
      gridLayout.verticalSpacing = VERTICAL_SPACE;
      setLayout(gridLayout);

      setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
      listenToMouse(this);

      logo = new Logo(this);
      logo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
      listenToMouse(logo);

      title = new Label(this, SWT.NONE);
      title.setForeground(COLOR_TITLE);
      title.setFont(FONT_TITLE);
      title.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
      listenToMouse(title);

      description = new Label(this, SWT.WRAP);
      description.setForeground(COLOR_DESCRIPTION);
      description.setFont(FONT_DESCRIPTION);
      description.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
      listenToMouse(description);

      description.addControlListener(new ControlAdapter()
      {
        private boolean settingProduct;

        @Override
        public void controlResized(ControlEvent e)
        {
          if (!settingProduct)
          {
            try
            {
              settingProduct = true;
              setProduct(product);
            }
            finally
            {
              settingProduct = false;
            }
          }
        }
      });

      title.setText("Ag");
      int titleHeight = title.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

      description.setText("Ag\nAg");
      int descriptionHeight = description.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

      contentHeight = titleHeight + VERTICAL_SPACE + descriptionHeight;
    }

    public Product getProduct()
    {
      return product;
    }

    public void setProduct(Product product)
    {
      this.product = product;
      if (product != null)
      {
        Image image = SetupWizard.getBrandingImage(product);
        logo.setImage(image);
        title.setText(product.getLabel());

        GC gc = new GC(description);

        try
        {
          int width = description.getSize().x - 8;
          int lines = list != null ? 2 : 8;

          String text = shorten(gc, width, lines, product.getDescription());
          description.setText(text);
          description.getParent().layout();
        }
        finally
        {
          gc.dispose();
        }
      }
    }

    public int getTotalHeight()
    {
      return contentHeight + 2 * BORDER;
    }

    public void mouseEnter(MouseEvent e)
    {
      if (list != null)
      {
        setBackground(COLOR_SELECTION);
      }
    }

    public void mouseExit(MouseEvent e)
    {
      if (list != null)
      {
        setBackground(COLOR_WHITE);
      }
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
      if (list != null && product != null && getClientArea().contains(e.x, e.y))
      {
        list.productSelected(product);
      }
    }

    private void listenToMouse(Control control)
    {
      control.addMouseTrackListener(this);
      control.addMouseListener(this);
    }

    private static String shorten(GC gc, int width, int lines, String html)
    {
      String plain = StringUtil.isEmpty(html) ? "No description available." : UIUtil.stripHTML(html);

      StringBuilder builder = new StringBuilder();
      int lineWidth = 0;
      int lineCount = 1;

      String[] words = plain.split(" ");
      for (String word : words)
      {
        int wordWidth = gc.textExtent(word + " ").x;
        lineWidth += wordWidth;
        if (lineWidth > width)
        {
          if (++lineCount > lines)
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

    /**
     * @author Eike Stepper
     */
    public static final class Logo extends Composite implements PaintListener
    {
      private static final int HEIGHT = 64;

      private Image image;

      private int imageX;

      private int imageY;

      public Logo(Composite parent)
      {
        super(parent, SWT.DOUBLE_BUFFERED);

        addPaintListener(this);
        setSize(HEIGHT, HEIGHT);
      }

      public Image getImage()
      {
        return image;
      }

      public void setImage(Image image)
      {
        this.image = image;

        ImageData imageData = image.getImageData();
        imageX = (HEIGHT - imageData.width) / 2;
        imageY = (HEIGHT - imageData.height) / 2;
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

        if (image != null)
        {
          gc.drawImage(image, imageX, imageY);
        }

        gc.setBackground(oldBackground);
        gc.setAntialias(oldAntialias);
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
    public void loadIndex(final IRunnableWithProgress runnable, int delay)
    {
      searchField.setEnabled(false);

      productList.reset(true);
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
            runnable.run(monitor);
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
            animator.stop();
          }
        }
      };

      thread.setDaemon(true);
      thread.start();
    }

    @Override
    protected void indexLoaded(Index index)
    {
      super.indexLoaded(index);

      stackComposite.setTopControl(productList.getControl());
      setFocus();

      if (index == null)
      {
        if (!installer.handleMissingIndex(getShell()))
        {
          dialog.exitSelected();
          return;
        }
      }

      searchField.setEnabled(true);

      final Collection<? extends Resource> configurationResources = getWizard().getConfigurationResources();
      if (!configurationResources.isEmpty())
      {
        UIUtil.asyncExec(getShell(), new Runnable()
        {
          private int count;

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
              dialog.applyConfiguration();
            }
          }
        });
      }
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

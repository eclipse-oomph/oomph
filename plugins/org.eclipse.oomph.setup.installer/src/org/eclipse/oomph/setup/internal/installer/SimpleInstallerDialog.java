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
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends Shell implements InstallerUI
{
  public static final int MARGIN_WIDTH = 42;

  private static final ShellMove SHELL_MOVE = new ShellMove();

  private static final boolean CAPTURE = false;

  private static final int WIDTH = 800;

  private static final int HEIGHT = 600;

  private static final Color WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

  private final Installer installer;

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private int returnCode = RETURN_OK;

  private LinkedHashMap<File, JRE> jres;

  public SimpleInstallerDialog(Display display, final Installer installer)
  {
    super(display, OS.INSTANCE.isMac() ? SWT.TOOL : SWT.BORDER);
    this.installer = installer;

    if (CAPTURE)
    {
      captureDownloadButton();
    }

    GridLayout verticalLayout = UIUtil.createGridLayout(1);
    verticalLayout.verticalSpacing = 20;

    setLayout(verticalLayout);
    setSize(WIDTH, HEIGHT);
    setImages(Window.getDefaultImages());
    setText(AbstractSetupDialog.SHELL_TEXT);

    Rectangle bounds = display.getPrimaryMonitor().getBounds();
    setLocation(bounds.x + (bounds.width - WIDTH) / 2, bounds.y + (bounds.height - HEIGHT) / 2);

    addTraverseListener(new TraverseListener()
    {
      public void keyTraversed(TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_ESCAPE)
        {
          exitSelected();
          e.detail = SWT.TRAVERSE_NONE;
          e.doit = false;
        }
      }
    });

    GridLayout titleLayout = UIUtil.createGridLayout(4);
    titleLayout.marginTop = 10;
    titleLayout.marginWidth = MARGIN_WIDTH;
    titleLayout.horizontalSpacing = 0;

    Composite titleComposite = new Composite(this, SWT.NONE);
    titleComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
    titleComposite.setLayout(titleLayout);

    Label titleImage = new Label(titleComposite, SWT.NONE);
    titleImage.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
    titleImage.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("oomph64.png"));

    Label titleText = new Label(titleComposite, SWT.NONE);
    titleText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    titleText.setText(" Oomph Installer");
    titleText.setFont(SetupInstallerPlugin.getFont(getFont(), URI.createURI("font://Helvetica/24///bold")));

    ToolButton advancedButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/advanced.png"), true);
    advancedButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
    advancedButton.setToolTipText("Switch to advanced mode");
    advancedButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        returnCode = RETURN_ADVANCED;
        exitSelected();
      }
    });

    ToolButton exitButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exit.png"), true);
    exitButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
    exitButton.setToolTipText("Exit");
    exitButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        exitSelected();
      }
    });

    stackLayout = new StackLayout();

    stack = new Composite(this, SWT.NONE);
    stack.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    stack.setLayout(stackLayout);

    productPage = new SimpleProductPage(stack, SWT.NONE, this);
    variablePage = new SimpleVariablePage(stack, SWT.NONE, this);

    stackLayout.topControl = productPage;
    productPage.setFocus();

    hook(this);

    display.timerExec(500, new Runnable()
    {
      public void run()
      {
        installer.getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
        installer.loadIndex();
      }
    });
  }

  public Installer getInstaller()
  {
    return installer;
  }

  public LinkedHashMap<File, JRE> getJREs()
  {
    return jres;
  }

  public void setJREs(LinkedHashMap<File, JRE> jres)
  {
    this.jres = jres;

    if (variablePage != null)
    {
      variablePage.refreshJREs();
    }
  }

  public int show()
  {
    open();

    Display display = getDisplay();
    while (!isDisposed())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }

    return returnCode;
  }

  public void showAbout()
  {
    String version = "he.ll.o";
    new AboutDialog(getShell(), version).open();
  }

  public void productSelected(Product product)
  {
    variablePage.setProduct(product);
    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        variablePage.setFocus();
      }
    });

    stackLayout.topControl = variablePage;
    stack.layout();
  }

  public void backSelected()
  {
    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        productPage.reset(); // TODO Use JavaScript, so that the browser doesn't scroll to top!
        productPage.setFocus();
      }
    });

    stackLayout.topControl = productPage;
    stack.layout();
  }

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }

  protected void exitSelected()
  {
    stack.setFocus(); // Browsers with focus make problems on dispose()!
    dispose();
  }

  private void captureDownloadButton()
  {
    final Shell captureShell = new Shell(this, SWT.NO_TRIM | SWT.MODELESS);
    captureShell.setLayout(new FillLayout());

    Image image = SetupInstallerPlugin.INSTANCE.getSWTImage("/download.png");

    final ToolButton downloadActiveButton = new ToolButton(captureShell, SWT.RADIO, image, false);
    downloadActiveButton.setBackground(WHITE);
    downloadActiveButton.setSelection(true);

    final ToolButton downloadHoverButton = new ToolButton(captureShell, SWT.PUSH, image, false);
    downloadHoverButton.setBackground(WHITE);
    downloadHoverButton.addMouseMoveListener(new MouseMoveListener()
    {
      public void mouseMove(MouseEvent e)
      {
        try
        {
          AccessUtil.save(new File("/develop/download_hover.png"), downloadHoverButton);
          AccessUtil.save(new File("/develop/download_active.png"), downloadActiveButton);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        finally
        {
          // captureShell.dispose();
        }
      }
    });

    captureShell.pack();
    captureShell.open();

    Point pt = getDisplay().map(downloadHoverButton, null, 10, 10);
    downloadHoverButton.setFocus();

    Event event = new Event();
    event.type = SWT.MouseMove;
    event.x = pt.x;
    event.y = pt.y;
    getDisplay().post(event);
  }

  public static void hook(Control control)
  {
    control.setBackground(WHITE);

    Class<? extends Control> c = control.getClass();
    boolean composite = c == Composite.class || c == StackComposite.class || control instanceof Shell || control instanceof SimpleInstallerPage;

    if (composite || c == Label.class || c == Link.class)
    {
      control.addMouseTrackListener(SHELL_MOVE);
      control.addMouseMoveListener(SHELL_MOVE);
      control.addMouseListener(SHELL_MOVE);

      if (composite)
      {
        for (Control child : ((Composite)control).getChildren())
        {
          hook(child);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ToolButton extends ToolBar
  {
    private final ToolItem toolItem;

    public ToolButton(Composite parent, int style, Image image, boolean secondary)
    {
      super(parent, SWT.FLAT);

      if (secondary)
      {
        toolItem = new SecondaryToolItem(this, style, image);
      }
      else
      {
        toolItem = new ToolItem(this, style);
        toolItem.setImage(image);
      }
    }

    public final ToolItem getToolItem()
    {
      return toolItem;
    }

    public void setImage(Image image)
    {
      if (toolItem instanceof SecondaryToolItem)
      {
        SecondaryToolItem secondaryToolItem = (SecondaryToolItem)toolItem;
        secondaryToolItem.init(image);
      }
      else
      {
        toolItem.setImage(image);
      }
    }

    public void addSelectionListener(SelectionListener listener)
    {
      toolItem.addSelectionListener(listener);
    }

    public boolean getSelection()
    {
      return toolItem.getSelection();
    }

    @Override
    public String getToolTipText()
    {
      return toolItem.getToolTipText();
    }

    public void removeSelectionListener(SelectionListener listener)
    {
      toolItem.removeSelectionListener(listener);
    }

    public void setSelection(boolean selected)
    {
      toolItem.setSelection(selected);
    }

    @Override
    public void setToolTipText(String string)
    {
      toolItem.setToolTipText(string);
    }

    @Override
    protected void checkSubclass()
    {
      // Do nothing.
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class SecondaryToolItem extends ToolItem
  {
    private Image grayImage;

    public SecondaryToolItem(ToolBar parent, int style, Image image)
    {
      super(parent, style);
      init(image);
    }

    public SecondaryToolItem(ToolBar parent, int style, int index, Image image)
    {
      super(parent, style, index);
      init(image);
    }

    @Override
    public void dispose()
    {
      grayImage.dispose();
      super.dispose();
    }

    @Override
    protected void checkSubclass()
    {
      // Do nothing.
    }

    public void init(Image image)
    {
      if (grayImage != null)
      {
        grayImage.dispose();
      }

      grayImage = new Image(getDisplay(), image, SWT.IMAGE_GRAY);
      setImage(grayImage);
      setHotImage(image);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ShellMove implements MouseTrackListener, MouseMoveListener, MouseListener
  {
    private Point start;

    public void mouseDoubleClick(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseDown(MouseEvent e)
    {
      if (e.button == 1)
      {
        start = new Point(e.x, e.y);
      }
    }

    public void mouseUp(MouseEvent e)
    {
      if (start != null)
      {
        start = null;
      }
    }

    public void mouseMove(MouseEvent e)
    {
      onMouseMove(e.widget, e.x, e.y);
    }

    public void mouseEnter(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseExit(MouseEvent e)
    {
      onMouseMove(e.widget, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public void mouseHover(MouseEvent e)
    {
      // Do nothing.
    }

    private void onMouseMove(Widget widget, int x, int y)
    {
      if (start != null)
      {
        if (widget instanceof Control)
        {
          Control control = (Control)widget;

          Shell shell = control.getShell();
          Point location = shell.getLocation();
          location.x += x - start.x;
          location.y += y - start.y;
          shell.setLocation(location);
        }
      }
    }
  }
}

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
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.ShellMove;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.OS;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends Shell implements InstallerUI
{
  public static final int MARGIN_WIDTH = 42;

  public static final int MARGIN_HEIGHT = 15;

  private static final ShellMove SHELL_MOVE = new ShellMove()
  {
    @Override
    public void hookControl(Control control)
    {
      control.setBackground(WHITE);
      super.hookControl(control);
    }

    @Override
    protected boolean shouldHookControl(Control control)
    {
      return super.shouldHookControl(control) || control instanceof SimpleInstallerPage;
    }
  };

  private static final boolean CAPTURE = false;

  private static final int WIDTH = 800;

  private static final int HEIGHT = 600;

  private static final Color WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

  private final Installer installer;

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private ToolButton updateButton;

  private Resolution updateResolution;

  private int returnCode = RETURN_OK;

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
    titleLayout.marginTop = 15;
    titleLayout.marginWidth = MARGIN_WIDTH;
    titleLayout.horizontalSpacing = 0;

    Composite titleComposite = new Composite(this, SWT.NONE);
    titleComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
    titleComposite.setLayout(titleLayout);

    Label titleImage = new Label(titleComposite, SWT.NONE);
    titleImage.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
    titleImage.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/title.png"));

    updateButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/update.png"), true);
    updateButton.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
    updateButton.setToolTipText("Update installer");
    updateButton.setVisible(false);
    updateButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        final Runnable successRunnable = new Runnable()
        {
          public void run()
          {
            returnCode = RETURN_RESTART;
            exitSelected();
          }
        };

        final ExceptionHandler<CoreException> exceptionHandler = new ExceptionHandler<CoreException>()
        {
          public void handleException(CoreException ex)
          {
            ErrorDialog.open(ex);
          }
        };

        SelfUpdate.update(getShell(), updateResolution, successRunnable, exceptionHandler, null);
      }
    });

    ToolButton advancedButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/advanced.png"), true);
    advancedButton.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
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
    exitButton.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
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

    Thread updateSearcher = new UpdateSearcher(display);
    updateSearcher.start();

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

  public void refreshJREs()
  {
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
    SHELL_MOVE.hookControl(control);
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
              if (!updateButton.isDisposed())
              {
                updateButton.setVisible(true);
              }
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
}

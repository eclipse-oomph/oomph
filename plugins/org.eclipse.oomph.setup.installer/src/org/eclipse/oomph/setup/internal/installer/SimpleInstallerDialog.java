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
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.OS;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends AbstractSimpleDialog implements InstallerUI
{
  public static final int MARGIN_WIDTH = 42;

  public static final int MARGIN_HEIGHT = 15;

  private static final boolean CAPTURE = false;

  private final Installer installer;

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private ToolButton updateButton;

  private Resolution updateResolution;

  private ToolButton advancedButton;

  public SimpleInstallerDialog(Display display, final Installer installer)
  {
    super(display, OS.INSTANCE.isMac() ? SWT.TOOL : SWT.BORDER, 800, 600, MARGIN_WIDTH, MARGIN_HEIGHT);
    this.installer = installer;
  }

  @Override
  protected void createUI(Composite titleComposite)
  {
    if (CAPTURE)
    {
      captureDownloadButton();
    }

    updateButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/update.png"), true);
    updateButton.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
    updateButton.setToolTipText("Update installer");
    updateButton.setVisible(false);
    updateButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Runnable successRunnable = new Runnable()
        {
          public void run()
          {
            setReturnCode(RETURN_RESTART);
            exitSelected();
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

    advancedButton = new ToolButton(titleComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/advanced.png"), true);
    advancedButton.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
    advancedButton.setToolTipText("Switch to advanced mode");
    advancedButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setReturnCode(RETURN_ADVANCED);
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

    Display display = getDisplay();

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

  public void setButtonsEnabled(boolean enabled)
  {
    if (updateButton != null)
    {
      updateButton.setEnabled(enabled);
    }

    if (advancedButton != null)
    {
      advancedButton.setEnabled(enabled);
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
    int xxx;
    // TODO Fix version in about dialog

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

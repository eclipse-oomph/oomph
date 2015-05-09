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

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.installer.MessageOverlay.ControlRelocator;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.OomphPlugin.Preference;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Stack;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends AbstractSimpleDialog implements InstallerUI
{
  private static final int INSTALLER_WIDTH = 523;

  private static final int INSTALLER_HEIGHT = 632;

  private static final Preference PREF_POOL_ENABLED = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("poolEnabled");

  private static Font defaultFont;

  private static String css;

  private static String pageTemplate;

  private static String productTemplate;

  private final Installer installer;

  private final Stack<SimpleInstallerPage> pageStack = new Stack<SimpleInstallerPage>();

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private SimpleReadmePage readmePage;

  private SimpleInstallationLogPage installationLogPage;

  private SimpleKeepInstallerPage keepInstallerPage;

  private SimpleInstallerMenu installerMenu;

  private SimpleInstallerMenu.InstallerMenuItem updateInstallerItem;

  private SimpleInstallerMenuButton menuButton;

  private boolean poolEnabled;

  private BundlePool pool;

  private Resolution updateResolution;

  private MessageOverlay currentMessage;

  public SimpleInstallerDialog(Display display, final Installer installer)
  {
    super(display, OS.INSTANCE.isMac() ? SWT.TOOL : SWT.NO_TRIM, INSTALLER_WIDTH, INSTALLER_HEIGHT);
    this.installer = installer;
  }

  @Override
  protected void createUI(Composite titleComposite)
  {
    poolEnabled = PREF_POOL_ENABLED.get(true);
    enablePool(poolEnabled);

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
        toggleMenu();
      }
    });

    stackLayout = new StackLayout();

    stack = new Composite(this, SWT.NONE);
    stack.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    stack.setLayout(stackLayout);

    productPage = new SimpleProductPage(stack, this);
    variablePage = new SimpleVariablePage(stack, this);
    readmePage = new SimpleReadmePage(stack, this);
    installationLogPage = new SimpleInstallationLogPage(stack, this);
    keepInstallerPage = new SimpleKeepInstallerPage(stack, this);

    switchToPage(productPage);

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

    // Initialize menu
    getInstallerMenu();

    updateAvailable(false);
  }

  private void toggleMenu()
  {
    getInstallerMenu().setVisible(!getInstallerMenu().isVisible());
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

    // FIXME: Enabled/Disabled state for bundle pooling?
    // if (poolButton != null)
    // {
    // poolButton.setImage(getBundlePoolImage());
    // }
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
    SimpleInstallerMenu menu = new SimpleInstallerMenu(this);

    SimpleInstallerMenu.InstallerMenuItem networkConnectionsItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    networkConnectionsItem.setText(NetworkConnectionsDialog.TITLE.toUpperCase() + "...");
    networkConnectionsItem.setToolTipText(NetworkConnectionsDialog.DESCRIPTION);
    networkConnectionsItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new NetworkConnectionsDialog(SimpleInstallerDialog.this);
        dialog.open();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem bundlePoolsItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    bundlePoolsItem.setText("BUNDLE POOLS...");
    bundlePoolsItem.setToolTipText(AgentManagerDialog.MESSAGE);
    bundlePoolsItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        manageBundlePools();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem ssh2KeysItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    ssh2KeysItem.setText(NetworkSSH2Dialog.TITLE.toUpperCase() + "...");
    ssh2KeysItem.setToolTipText(NetworkSSH2Dialog.DESCRIPTION);
    ssh2KeysItem.setDividerVisible(false);
    ssh2KeysItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new NetworkSSH2Dialog(SimpleInstallerDialog.this);
        dialog.open();
      }
    });

    Label spacer1 = new Label(menu, SWT.NONE);
    spacer1.setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 46).create());

    updateInstallerItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    updateInstallerItem.setDefaultImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exclamation_circle.png"));
    updateInstallerItem.setHoverImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exclamation_circle_hover.png"));
    updateInstallerItem.setText("UPDATE");
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

    SimpleInstallerMenu.InstallerMenuItem advancedModeItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    advancedModeItem.setText("ADVANCED");
    advancedModeItem.setToolTipText("Switch to advanced mode");
    advancedModeItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setReturnCode(RETURN_ADVANCED);
        exitSelected();
      }
    });

    SimpleInstallerMenu.InstallerMenuItem aboutItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    aboutItem.setText("ABOUT");
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
    exitItem.setText("EXIT");
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

  private void updateAvailable(boolean available)
  {
    menuButton.setNotificationVisible(available);
    updateInstallerItem.setVisible(available);
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
          button.setEnabled(element instanceof BundlePool);
        }
      }
    };

    if (pool != null)
    {
      dialog.setSelectedElement(pool);
    }

    if (dialog.open() == AgentManagerDialog.OK)
    {
      enablePool(enabled[0]);
      pool = (BundlePool)dialog.getSelectedElement();
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
    switchToPage(variablePage);
  }

  public void backSelected()
  {
    if (pageStack.size() <= 1)
    {
      return;
    }

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        SimpleInstallerPage currentPage = pageStack.pop();

        try
        {
          currentPage.aboutToHide();
        }
        catch (Exception ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }

        SimpleInstallerPage previousPage = pageStack.peek();

        stackLayout.topControl = previousPage;
        stack.layout();

        previousPage.aboutToShow();
        previousPage.setFocus();
      }
    });
  }

  public void showMessage(String message, MessageOverlay.Type type, boolean dismissAutomatically)
  {
    showMessage(message, type, dismissAutomatically, null);
  }

  public void showMessage(String message, MessageOverlay.Type type, boolean dismissAutomatically, Runnable action)
  {
    clearMessage();

    currentMessage = new MessageOverlay(this, type, new ControlRelocator()
    {
      public void relocate(Control control)
      {
        Rectangle bounds = SimpleInstallerDialog.this.getBounds();
        int x = bounds.x + 5;
        int y = bounds.y + 24;

        int width = bounds.width - 9;

        // Depending on the current page, the height varies
        int height = pageStack.peek() instanceof SimpleProductPage ? 87 : 70;
        control.setBounds(x, y, width, height);
      }
    }, dismissAutomatically, action);

    currentMessage.setMessage(message);
    currentMessage.setVisible(true);
  }

  public void clearMessage()
  {
    if (currentMessage != null && !currentMessage.isDisposed())
    {
      if (!currentMessage.isDisposed())
      {
        currentMessage.close();
      }

      currentMessage = null;
    }
  }

  public void showReadme(URI readmeURI)
  {
    readmePage.setReadmeURI(readmeURI);
    switchToPage(readmePage);
  }

  public void showInstallationLog(File installationLogFile)
  {
    installationLogPage.setInstallationLogFile(installationLogFile);
    switchToPage(installationLogPage);
  }

  public void showKeepInstaller()
  {
    switchToPage(keepInstallerPage);
  }

  @Override
  public void dispose()
  {
    // Ensure that no Browser widget has the focus. See bug 466902.
    menuButton.setFocus();

    super.dispose();
  }

  private void switchToPage(final SimpleInstallerPage page)
  {
    if (page != null)
    {
      final SimpleInstallerPage currentPage = !pageStack.isEmpty() ? pageStack.peek() : null;
      if (currentPage == null || currentPage != page)
      {
        pageStack.push(page);

        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            if (currentPage != null)
            {
              currentPage.aboutToHide();
            }

            stackLayout.topControl = page;
            stack.layout();

            page.aboutToShow();
            page.setFocus();
          }
        });
      }
    }
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
}

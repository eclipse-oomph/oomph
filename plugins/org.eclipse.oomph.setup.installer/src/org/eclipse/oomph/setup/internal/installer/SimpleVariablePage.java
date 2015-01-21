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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.JREDownloadHandler;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SimpleVariablePage extends SimpleInstallerPage
{
  private static final Preference PREF_POOL_ENABLED = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("poolEnabled");

  private static final Preference PREF_INSTALL_ROOT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("installRoot");

  private static final File FILE_INSTALL_ROOT = new File(SetupInstallerPlugin.INSTANCE.getUserLocation().toFile(), PREF_INSTALL_ROOT.key() + ".txt");

  private static final String TEXT_LOG = "Show installation log";

  private static final String TEXT_README = "Show readme file";

  private static final String TEXT_LAUNCH = "Launch product";

  private final Map<String, ProductVersion> productVersions = new HashMap<String, ProductVersion>();

  private Product product;

  private ProductVersion selectedProductVersion;

  private String readmePath;

  private BundlePool pool;

  private boolean poolEnabled;

  private Browser browser;

  private Composite versionComposite;

  private CCombo versionCombo;

  private ToolButton bitness32Button;

  private ToolButton bitness64Button;

  private JREController javaController;

  private Label javaLabel;

  private ComboViewer javaViewer;

  private ToolButton javaButton;

  private Text folderText;

  private ToolButton folderButton;

  private ToolButton poolButton;

  private String installRoot;

  private String installFolder;

  private Thread installThread;

  private StackComposite installStack;

  private ToolButton installButton;

  private String installError;

  private boolean installed;

  private SetupTaskPerformer performer;

  private SimpleProgress progress;

  private ProgressBar progressBar;

  private Link progressLabel;

  private ToolButton cancelButton;

  private ToolButton backButton;

  public SimpleVariablePage(final Composite parent, int style, final SimpleInstallerDialog dialog)
  {
    super(parent, style, dialog);

    poolEnabled = PREF_POOL_ENABLED.get(true);
    enablePool(poolEnabled);

    GridLayout layout = UIUtil.createGridLayout(4);
    layout.marginWidth = SimpleInstallerDialog.MARGIN_WIDTH;
    layout.marginTop = 5;
    layout.marginBottom = SimpleInstallerDialog.MARGIN_HEIGHT;
    layout.horizontalSpacing = 5;
    layout.verticalSpacing = 5;
    setLayout(layout);

    // Row 1
    GridData browserLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false, layout.numColumns, 1);
    browserLayoutData.heightHint = OS.INSTANCE.isLinux() ? 120 : 142;

    Composite browserComposite = new Composite(this, SWT.BORDER);
    browserComposite.setLayoutData(browserLayoutData);
    browserComposite.setLayout(new FillLayout());

    browser = new Browser(browserComposite, SWT.NO_SCROLL);
    browser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        String url = event.location;
        if (!"about:blank".equals(url))
        {
          OS.INSTANCE.openSystemBrowser(url);
          event.doit = false;
        }
      }
    });

    // Row 2
    new Label(this, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, layout.numColumns, 1));

    // Row 3
    createLabel("Product Version ");

    versionComposite = new Composite(this, SWT.NONE);
    versionComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    versionComposite.setLayout(UIUtil.createGridLayout(4));

    versionCombo = new CCombo(versionComposite, SWT.BORDER | SWT.READ_ONLY);
    versionCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    versionCombo.setFont(font);
    versionCombo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String label = versionCombo.getItem(versionCombo.getSelectionIndex());
        productVersionSelected(productVersions.get(label));
        UIUtil.clearTextSelection(versionCombo);
      }
    });

    if (JREManager.BITNESS_CHANGEABLE)
    {
      new Label(versionComposite, SWT.NONE);

      bitness32Button = new ToolButton(versionComposite, SWT.RADIO, SetupInstallerPlugin.INSTANCE.getSWTImage("32bit.png"), true);
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

      bitness64Button = new ToolButton(versionComposite, SWT.RADIO, SetupInstallerPlugin.INSTANCE.getSWTImage("64bit.png"), true);
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

    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);

    // Row 4
    javaLabel = createLabel("Java VM ");

    CCombo javaCombo = new CCombo(this, SWT.BORDER | SWT.READ_ONLY);
    javaCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    javaCombo.setFont(font);

    javaViewer = new ComboViewer(javaCombo);
    javaViewer.setContentProvider(new ArrayContentProvider());
    javaViewer.setLabelProvider(new LabelProvider());

    javaButton = new ToolButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"), false);
    javaButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    javaButton.setToolTipText("Select Java VM...");
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
        return product;
      }
    };

    javaController = new JREController(javaLabel, javaViewer, downloadHandler)
    {
      @Override
      protected void modelEmpty(boolean empty)
      {
        super.modelEmpty(empty);
        installButton.setEnabled(!empty);
      }

      @Override
      protected void setLabel(String text)
      {
        super.setLabel(text + " ");
      }
    };

    new Label(this, SWT.NONE);

    // Row 5
    createLabel("Installation Folder ");

    folderText = new Text(this, SWT.BORDER);
    folderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    folderText.setFont(font);
    folderText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        String dir = folderText.getText();
        validateFolderText(dir);
      }
    });

    folderButton = new ToolButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"), false);
    folderButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    folderButton.setToolTipText("Select installation folder...");
    folderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText(AbstractSetupDialog.SHELL_TEXT);
        dialog.setMessage("Select installation folder...");

        if (!StringUtil.isEmpty(installRoot))
        {
          dialog.setFilterPath(installRoot);
        }

        String dir = dialog.open();
        if (dir != null)
        {
          validateFolderText(dir);
          setFolderText(dir);
        }
      }
    });

    poolButton = new ToolButton(this, SWT.PUSH, getBundlePoolImage(), false);
    poolButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    poolButton.setToolTipText("Configure bundle pool...");
    poolButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        manageBundlePools();
      }
    });

    // Row 6
    backButton = new ToolButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/back.png"), true);
    backButton.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 2));
    backButton.setToolTipText("Back");
    backButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        dialog.backSelected();
      }
    });

    installStack = new StackComposite(this, SWT.NONE);
    installStack.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    cancelButton = new ToolButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/cancel.png"), false);
    cancelButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
    cancelButton.setToolTipText("Cancel");
    cancelButton.setVisible(false);
    cancelButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        installCancel();
      }
    });

    new Label(this, SWT.NONE);

    installButton = new ToolButton(installStack, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/download_small.png"), false);

    final Composite progressComposite = new Composite(installStack, SWT.NONE);
    progressComposite.setLayout(UIUtil.createGridLayout(1));

    GridData layoutData2 = new GridData(SWT.FILL, SWT.CENTER, true, true);
    layoutData2.heightHint = 28;

    progressBar = new ProgressBar(progressComposite, SWT.NONE);
    progressBar.setLayoutData(layoutData2);

    installButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (installed)
        {
          launchProduct();
        }
        else
        {
          setEnabled(false);

          installButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/download_small.png"));
          progressBar.setSelection(0);
          progressLabel.setForeground(null);
          cancelButton.setVisible(true);

          installStack.setTopControl(progressComposite);

          install();
        }
      }
    });

    installStack.setTopControl(installButton);

    // Row 7
    progressLabel = new Link(this, SWT.WRAP);
    progressLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    progressLabel.setFont(SetupInstallerPlugin.getFont(font, URI.createURI("font:///9/bold")));
    progressLabel.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (TEXT_LOG.equals(e.text))
        {
          String url = new File(installFolder, "eclipse/configuration/org.eclipse.oomph.setup/setup.log").toURI().toString();
          OS.INSTANCE.openSystemBrowser(url);
        }
        else if (TEXT_README.equals(e.text))
        {
          if (readmePath != null)
          {
            String url = new File(installFolder, "eclipse/" + readmePath).toURI().toString();
            OS.INSTANCE.openSystemBrowser(url);
          }
        }
        else if (TEXT_LAUNCH.equals(e.text))
        {
          launchProduct();
          return;
        }

        installButton.setFocus();
      }
    });

    List<Control> tabList = new ArrayList<Control>(Arrays.asList(getTabList()));
    tabList.remove(browserComposite);
    tabList.remove(backButton);
    tabList.add(backButton);
    setTabList(tabList.toArray(new Control[tabList.size()]));
  }

  protected void productVersionSelected(ProductVersion productVersion)
  {
    if (selectedProductVersion != productVersion)
    {
      selectedProductVersion = productVersion;

      String requiredJavaVersion = productVersion.getRequiredJavaVersion();
      javaController.setJavaVersion(requiredJavaVersion);

      ProductPage.saveProductVersionSelection(installer.getCatalogManager(), selectedProductVersion);
    }
  }

  @Override
  public boolean setFocus()
  {
    return folderText.setFocus();
  }

  public void setProduct(Product product)
  {
    this.product = product;

    StringBuilder builder = new StringBuilder();
    builder.append("<html><style TYPE=\"text/css\"><!-- ");
    builder.append("table{border:none; border-collapse:collapse}");
    builder.append(".label{font-size:1.1em; font-weight:700}");
    builder.append(".description{font-size:14px; color:#333}");
    builder.append(".col1{padding:10px; width:64px; text-align:center; vertical-align:top}");
    builder
        .append(" --></style><body style=\"background-color:#fafafa; overflow:auto; margin:10px; font-family:'Open Sans','Helvetica Neue',Helvetica,Arial,sans-serif\"><table>\n");

    SimpleProductPage.renderProduct(builder, product, true, null);
    browser.setText(SimpleProductPage.getHtml(builder), true);

    productVersions.clear();
    versionCombo.removeAll();

    ProductVersion defaultProductVersion = ProductPage.getDefaultProductVersion(installer.getCatalogManager(), product);
    int i = 0;
    int selection = 0;

    for (ProductVersion productVersion : product.getVersions())
    {
      if (defaultProductVersion == null)
      {
        defaultProductVersion = productVersion;
      }

      String label = productVersion.getLabel();
      if (label == null)
      {
        label = productVersion.getName();
      }

      productVersions.put(label, productVersion);
      versionCombo.add(label);

      if (productVersion == defaultProductVersion)
      {
        selection = i;
      }

      ++i;
    }

    versionCombo.pack();
    Point size = versionCombo.getSize();
    size.x += 10;
    versionCombo.setSize(size);
    versionComposite.layout();

    versionCombo.select(selection);
    versionCombo.setSelection(new Point(0, 0));
    productVersionSelected(defaultProductVersion);

    installFolder = getDefaultInstallationFolder();
    setFolderText(installFolder);

    installStack.setTopControl(installButton);
    installButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/download_small.png"));
    installButton.setToolTipText("Install");
    installed = false;

    progressLabel.setText("");
    setEnabled(true);
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    versionCombo.setEnabled(enabled);

    if (JREManager.BITNESS_CHANGEABLE)
    {
      int bitness = javaController.getBitness();
      bitness32Button.setEnabled(enabled);
      bitness32Button.setVisible(enabled || bitness == 32);
      bitness64Button.setEnabled(enabled);
      bitness64Button.setVisible(enabled || bitness == 64);
    }

    javaViewer.getControl().setEnabled(enabled);
    javaButton.setEnabled(enabled);

    folderText.setEnabled(enabled);
    folderButton.setEnabled(enabled);
    poolButton.setEnabled(enabled);
    backButton.setEnabled(enabled);
  }

  public void refreshJREs()
  {
    javaController.refresh();
  }

  private String getDefaultInstallationFolder()
  {
    String name = product.getName();
    int lastDot = name.lastIndexOf('.');
    if (lastDot != -1)
    {
      name = name.substring(lastDot + 1);
    }

    if (installRoot == null)
    {
      if (FILE_INSTALL_ROOT.isFile())
      {
        List<String> lines = IOUtil.readLines(FILE_INSTALL_ROOT, "UTF-8");
        if (lines != null && !lines.isEmpty())
        {
          installRoot = lines.get(0);
          if (installRoot.length() == 0)
          {
            installRoot = null;
          }
        }
      }

      if (installRoot == null)
      {
        installRoot = PREF_INSTALL_ROOT.get(PropertiesUtil.USER_HOME);
      }
    }

    for (int i = 1; i < 1000; i++)
    {
      String filename = name;
      if (i != 1)
      {
        filename += i;
      }

      File folder = new File(installRoot, filename);
      if (!folder.exists())
      {
        return folder.getAbsolutePath();
      }
    }

    throw new IllegalStateException("User home is full");
  }

  private Image getBundlePoolImage()
  {
    return SetupInstallerPlugin.INSTANCE.getSWTImage("simple/bundle_pool_" + (poolEnabled ? "enabled" : "disabled") + ".png");
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

    if (poolButton != null)
    {
      poolButton.setImage(getBundlePoolImage());
    }
  }

  private Label createLabel(String text)
  {
    Label label = new Label(this, SWT.RIGHT);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    label.setText(text);
    label.setFont(font);
    return label;
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

  private void install()
  {
    installThread = new Thread()
    {
      @Override
      public void run()
      {
        installError = null;
        performer = null;
        progress = new SimpleProgress();

        try
        {
          installPerform();
        }
        catch (InterruptedException ex)
        {
          progress.setCanceled(true);
        }
        catch (OperationCanceledException ex)
        {
          progress.setCanceled(true);
        }
        catch (Exception ex)
        {
          if (!progress.isCanceled())
          {
            SetupInstallerPlugin.INSTANCE.log(ex);
            installError = ex.getMessage();
            if (StringUtil.isEmpty(installError))
            {
              installError = ex.getClass().getName();
            }
          }
        }
        finally
        {
          if (performer != null)
          {
            IOUtil.close(performer.getLogStream());
          }

          if (!progress.isCanceled())
          {
            UIUtil.syncExec(new Runnable()
            {
              public void run()
              {
                try
                {
                  installFinished();
                }
                catch (SWTException ex)
                {
                  //$FALL-THROUGH$
                }
              }
            });
          }
        }
      }
    };

    installThread.setDaemon(true);
    installThread.start();
  }

  private void installPerform() throws Exception
  {
    if (pool != null)
    {
      P2Util.getAgentManager().setDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName(), pool);
      System.setProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION, pool.getLocation().getAbsolutePath());
    }
    else
    {
      System.clearProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION);
    }

    JRE jre = javaController.getJRE();
    String vmPath = new File(jre.getJavaHome(), "bin").getAbsolutePath();

    ResourceSet resourceSet = installer.getResourceSet();
    URIConverter uriConverter = resourceSet.getURIConverter();

    SetupContext setupContext = SetupContext.create(resourceSet, selectedProductVersion);
    Installation installation = setupContext.getInstallation();
    User user = setupContext.getUser();

    UserAdjuster userAdjuster = new UserAdjuster();
    userAdjuster.adjust(user, installFolder);
    IOUtil.writeLines(FILE_INSTALL_ROOT, "UTF-8", Collections.singletonList(installRoot));

    SimplePrompter prompter = new SimplePrompter();

    performer = SetupTaskPerformer.create(uriConverter, prompter, Trigger.BOOTSTRAP, setupContext, false);
    performer.getUnresolvedVariables().clear();
    performer.put(ILicense.class, ProgressPage.LICENSE_CONFIRMER);
    performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(user, false));
    performer.setVMPath(vmPath);
    performer.setProgress(progress);
    performer.log("Executing " + performer.getTrigger().toString().toLowerCase() + " tasks");
    performer.perform(progress);
    performer.recordVariables(installation, null, user);
    performer.savePasswords();

    File configurationLocation = performer.getProductConfigurationLocation();
    installation.eResource().setURI(URI.createFileURI(new File(configurationLocation, "org.eclipse.oomph.setup/installation.setup").toString()));
    BaseUtil.saveEObject(installation);

    userAdjuster.undo();
    BaseUtil.saveEObject(user);
  }

  private void installCancel()
  {
    if (progress != null)
    {
      progress.setCanceled(true);
    }

    if (installThread != null)
    {
      installThread.interrupt();
    }

    setEnabled(true);
    progressLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
    progressLabel.setText("Installation canceled");

    cancelButton.setVisible(false);
    installStack.setTopControl(installButton);
  }

  private void installFinished()
  {
    readmePath = null;
    String message;

    if (installError == null)
    {
      installed = true;
      installButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/launch.png"));
      installButton.setToolTipText("Launch");
      progressLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));

      message = "Installation finished successfully\n\n<a>" + TEXT_LOG + "</a>\n";

      Scope scope = selectedProductVersion;
      while (scope != null)
      {
        Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
        if (annotation != null)
        {
          readmePath = annotation.getDetails().get(AnnotationConstants.KEY_README_PATH);
          if (readmePath != null)
          {
            message += "<a>" + TEXT_README + "</a>\n";
            break;
          }
        }

        scope = scope.getParentScope();
      }

      message += "<a>" + TEXT_LAUNCH + "</a>";
    }
    else
    {
      setEnabled(true);

      progressLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
      message = installError + "\n\n<a>" + TEXT_LOG + "</a>\n";
    }

    progressLabel.setText(message);
    backButton.setEnabled(true);

    cancelButton.setVisible(false);
    installStack.setTopControl(installButton);
  }

  private void launchProduct()
  {
    try
    {
      ProgressPage.launchProduct(performer);
    }
    catch (Exception ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }

    dialog.exitSelected();
  }

  private void setFolderText(String dir)
  {
    folderText.setText(dir);
    folderText.setSelection(dir.length());
  }

  private void validateFolderText(String dir)
  {
    installFolder = dir;
    // TODO validate dir?

    try
    {
      File folder = new File(installFolder);

      File parentFolder = folder.getParentFile();
      if (parentFolder != null)
      {
        installRoot = parentFolder.getAbsolutePath();
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class UserAdjuster
  {
    private static final URI INSTALLATION_LOCATION_ATTRIBUTE_URI = SetupTaskPerformer.getAttributeURI(SetupPackage.Literals.INSTALLATION_TASK__LOCATION);

    private EList<AttributeRule> attributeRules;

    private String oldValue;

    private void adjust(User user, String installFolder)
    {
      attributeRules = user.getAttributeRules();
      for (AttributeRule attributeRule : attributeRules)
      {
        if (INSTALLATION_LOCATION_ATTRIBUTE_URI.equals(attributeRule.getAttributeURI()))
        {
          oldValue = attributeRule.getValue();
          attributeRule.setValue(installFolder);
          return;
        }
      }

      AttributeRule attributeRule = SetupFactory.eINSTANCE.createAttributeRule();
      attributeRule.setAttributeURI(INSTALLATION_LOCATION_ATTRIBUTE_URI);
      attributeRule.setValue(installFolder);
      attributeRules.add(attributeRule);
    }

    public void undo()
    {
      for (Iterator<AttributeRule> it = attributeRules.iterator(); it.hasNext();)
      {
        AttributeRule attributeRule = it.next();
        if (INSTALLATION_LOCATION_ATTRIBUTE_URI.equals(attributeRule.getAttributeURI()))
        {
          if (oldValue == null)
          {
            it.remove();
          }
          else
          {
            attributeRule.setValue(oldValue);
          }

          return;
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SimplePrompter extends HashMap<String, String> implements SetupPrompter
  {
    private static final long serialVersionUID = 1L;

    public SimplePrompter()
    {
    }

    public boolean promptVariables(List<? extends SetupTaskContext> performers)
    {
      for (SetupTaskContext performer : performers)
      {
        List<VariableTask> unresolvedVariables = ((SetupTaskPerformer)performer).getUnresolvedVariables();
        for (VariableTask variable : unresolvedVariables)
        {
          String name = variable.getName();
          // System.out.println(name);

          String value = get(name);
          if (value == null)
          {
            return false;
          }
        }
      }

      return true;
    }

    public String getValue(VariableTask variable)
    {
      String name = variable.getName();
      // System.out.println(name);

      return get(name);
    }

    public UserCallback getUserCallback()
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SimpleProgress implements ProgressLog, IProgressMonitor, Runnable
  {
    private volatile String name;

    private volatile double totalWork;

    private volatile double work;

    private volatile boolean canceled;

    private volatile boolean done;

    private int lastSelection = -1;

    private String lastName;

    public void setTerminating()
    {
    }

    public void log(String line)
    {
      log(line, true, Severity.OK);
    }

    public void log(String line, Severity severity)
    {
      log(line, true, severity);
    }

    public void log(String line, boolean filter)
    {
      log(line, filter, Severity.OK);
    }

    public synchronized void log(String line, boolean filter, Severity severity)
    {
      name = line;
    }

    public void log(IStatus status)
    {
      String string = SetupInstallerPlugin.toString(status);
      log(string, false);
    }

    public void log(Throwable t)
    {
      String string = SetupInstallerPlugin.toString(t);
      log(string, false);
    }

    public synchronized void task(SetupTask setupTask)
    {
      if (setupTask != null)
      {
        name = "Performing setup task " + setupTask.eClass().getName();
      }
      else
      {
        name = null;
      }
    }

    public synchronized boolean isCanceled()
    {
      return canceled;
    }

    public synchronized void setCanceled(boolean canceled)
    {
      this.canceled = canceled;
    }

    public synchronized void beginTask(String name, int totalWork)
    {
      performer.log(name);
      if (this.totalWork == 0)
      {
        this.totalWork = totalWork;
        schedule();
      }
    }

    public synchronized void done()
    {
      work = totalWork;
      done = true;
    }

    public synchronized void setTaskName(String name)
    {
      performer.log(name);
    }

    public synchronized void subTask(String name)
    {
      performer.log(name);
    }

    public synchronized void internalWorked(double work)
    {
      this.work += work;
    }

    public void worked(int work)
    {
      internalWorked(work);
    }

    public void run()
    {
      String name;
      double totalWork;
      double work;
      boolean canceled;
      boolean done;

      synchronized (this)
      {
        name = this.name;
        totalWork = this.totalWork;
        work = this.work;
        canceled = this.canceled;
        done = this.done;
      }

      if (!canceled)
      {
        int smin = progressBar.getMinimum();
        int smax = progressBar.getMaximum();
        int selection = (int)(work * (smax - smin) / totalWork + smin);

        try
        {
          if (selection != lastSelection)
          {
            lastSelection = selection;
            progressBar.setSelection(selection);
          }

          if (!ObjectUtil.equals(name, lastName))
          {
            lastName = name;
            if (!done)
            {
              progressLabel.setText(StringUtil.safe(name));
            }
          }
        }
        catch (SWTException ex)
        {
          return;
        }

        if (!done)
        {
          schedule();
        }
      }
    }

    private void schedule()
    {
      UIUtil.asyncExec(getDisplay(), this);
    }
  }
}

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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageCheckbox;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LicenseInfo;
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
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.installer.InstallLaunchButton.State;
import org.eclipse.oomph.setup.internal.installer.MessageOverlay.Type;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.JREDownloadHandler;
import org.eclipse.oomph.setup.ui.LicensePrePrompter;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;
import org.eclipse.oomph.util.OomphPlugin.Preference;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
  private static final boolean EXIT_AFTER_LAUNCH = !PropertiesUtil.isProperty("oomph.no.exit.after.launch");

  private static final String SETUP_LOG_FILE = OS.INSTANCE.getEclipseDir() + "/configuration/org.eclipse.oomph.setup/setup.log";

  private static final Preference PREF_INSTALL_ROOT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("installRoot");

  private static final File FILE_INSTALL_ROOT = new File(SetupInstallerPlugin.INSTANCE.getUserLocation().toFile(), PREF_INSTALL_ROOT.key() + ".txt");

  private static final String TEXT_README = "show readme file";

  private static final String TEXT_KEEP = "keep installer";

  private static final String MESSAGE_SUCCESS = "Installation completed successfully.";

  private static final String MESSAGE_FAILURE = "Installation failed with an error.";

  private final Map<String, ProductVersion> productVersions = new HashMap<String, ProductVersion>();

  private Product product;

  private ProductVersion selectedProductVersion;

  private String readmePath;

  private Browser browser;

  private CCombo versionCombo;

  private ImageCheckbox bitness32Button;

  private ImageCheckbox bitness64Button;

  private JREController javaController;

  private Label javaLabel;

  private ComboViewer javaViewer;

  private FlatButton javaButton;

  private Text folderText;

  private FlatButton folderButton;

  private String installRoot;

  private String installFolder;

  private Thread installThread;

  private StackComposite installStack;

  private InstallLaunchButton installButton;

  private String installError;

  private boolean installed;

  private SetupTaskPerformer performer;

  private SimpleProgress progress;

  private FlatButton cancelButton;

  private FlatButton showReadmeButton;

  private FlatButton keepInstallerButton;

  private Composite afterInstallComposite;

  private Composite errorComposite;

  public SimpleVariablePage(final Composite parent, final SimpleInstallerDialog dialog)
  {
    super(parent, dialog, true);
  }

  @Override
  protected void createContent(Composite container)
  {
    container.setBackgroundMode(SWT.INHERIT_FORCE);
    container.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    // Row 1
    GridData browserLayoutData = GridDataFactory.fillDefaults().indent(0, 13).grab(true, false).create();
    browserLayoutData.heightHint = 216;

    Composite browserComposite = new Composite(container, SWT.NONE);
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

    Composite variablesComposite = new Composite(container, SWT.NONE);
    GridLayout variablesLayout = new GridLayout(5, false);
    variablesLayout.horizontalSpacing = 8;
    variablesLayout.verticalSpacing = 3;
    variablesLayout.marginLeft = 14;
    variablesLayout.marginRight = 30;
    variablesLayout.marginTop = 24;
    variablesLayout.marginBottom = 0;
    variablesLayout.marginHeight = 0;
    variablesComposite.setLayout(variablesLayout);
    variablesComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

    // Row 3
    createLabel(variablesComposite, "Product Version");

    // Spacer to get a little bit more distance between labels and input fields
    spacer(variablesComposite).setLayoutData(GridDataFactory.swtDefaults().hint(17, SWT.DEFAULT).create());

    versionCombo = createComboBox(variablesComposite, SWT.READ_ONLY);
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

    bitness32Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit_hover.png"));
    bitness32Button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).indent(4, 0).hint(SWT.DEFAULT, 30).create());
    bitness32Button.setChecked(false);
    bitness32Button.setVisible(JREManager.BITNESS_CHANGEABLE);
    bitness32Button.setToolTipText("Create a 32 bit installation");
    bitness32Button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        bitness32Button.setChecked(true);
        bitness64Button.setChecked(false);
        javaController.setBitness(32);
      }
    });

    bitness64Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit_hover.png"));
    bitness64Button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).hint(SWT.DEFAULT, 30).create());
    bitness64Button.setChecked(true);
    bitness64Button.setVisible(JREManager.BITNESS_CHANGEABLE);
    bitness64Button.setToolTipText("Create a 64 bit installation");
    bitness64Button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        bitness32Button.setChecked(false);
        bitness64Button.setChecked(true);
        javaController.setBitness(64);
      }
    });

    // Row 4
    javaLabel = createLabel(variablesComposite, "Java VM");

    spacer(variablesComposite);

    final CCombo javaCombo = createComboBox(variablesComposite, SWT.READ_ONLY);

    javaViewer = new ComboViewer(javaCombo);
    javaViewer.setContentProvider(new ArrayContentProvider());
    javaViewer.setLabelProvider(new LabelProvider());
    javaViewer.addPostSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        UIUtil.setSelectionToEnd(javaCombo);
      }
    });

    javaButton = new ImageHoverButton(variablesComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_hover.png"), SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_disabled.png"));
    javaButton.setLayoutData(GridDataFactory.swtDefaults().indent(4, 0).create());
    javaButton.setToolTipText("Select a Java VM");
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
        validatePage();
      }

      @Override
      protected void jreChanged(JRE jre)
      {
        super.jreChanged(jre);
        validatePage();
        javaCombo.setToolTipText(jre.getJavaHome().toString());
      }

      @Override
      protected void setLabel(String text)
      {
        super.setLabel(text + " ");
      }
    };

    spacer(variablesComposite);

    // Row 5
    createLabel(variablesComposite, "Installation Folder");

    spacer(variablesComposite);

    folderText = createTextField(variablesComposite);
    folderText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        validatePage();
        folderText.setToolTipText(installFolder);

        UIUtil.getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            folderText.setFocus();
          }
        });
      }
    });

    folderButton = new ImageHoverButton(variablesComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_hover.png"), SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_disabled.png"));
    folderButton.setLayoutData(GridDataFactory.swtDefaults().indent(4, 0).create());
    folderButton.setToolTipText("Select an installation folder");
    folderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText(AbstractSetupDialog.SHELL_TEXT);
        dialog.setMessage("Select an installation folder:");

        if (!StringUtil.isEmpty(installFolder))
        {
          try
          {
            File existingFolder = IOUtil.getExistingFolder(new File(installFolder));
            if (existingFolder != null)
            {
              dialog.setFilterPath(existingFolder.getAbsolutePath());
            }
          }
          catch (Exception ex)
          {
            SetupInstallerPlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }

        String dir = dialog.open();
        if (dir != null)
        {
          validatePage();
          setFolderText(dir);
        }
      }
    });

    spacer(variablesComposite);
    spacer(variablesComposite);
    spacer(variablesComposite);

    installButton = new InstallLaunchButton(variablesComposite);
    installButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).hint(SWT.DEFAULT, 36).indent(0, 32).create());
    installButton.setCurrentState(InstallLaunchButton.State.INSTALL);

    spacer(variablesComposite);
    spacer(variablesComposite);
    spacer(variablesComposite);
    spacer(variablesComposite);

    installStack = new StackComposite(variablesComposite, SWT.NONE);
    installStack.setLayoutData(GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 72).create());

    final Composite duringInstallContainer = new Composite(installStack, SWT.NONE);
    duringInstallContainer.setLayout(UIUtil.createGridLayout(1));

    // During installation.
    cancelButton = createButton(duringInstallContainer, "Cancel Installation", "Cancel", SetupInstallerPlugin.INSTANCE.getSWTImage("simple/delete.png"));
    cancelButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        installCancel();
      }
    });

    GridLayout afterInstallLayout = UIUtil.createGridLayout(1);
    afterInstallLayout.verticalSpacing = 3;

    afterInstallComposite = new Composite(installStack, SWT.NONE);
    afterInstallComposite.setLayout(afterInstallLayout);

    showReadmeButton = createButton(afterInstallComposite, TEXT_README, null, null);
    showReadmeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (readmePath != null)
        {
          java.net.URI readmeURI = new File(installFolder, OS.INSTANCE.getEclipseDir() + "/" + readmePath).toURI();
          dialog.showReadme(readmeURI);
        }
      }
    });
    showReadmeButton.setToolTipText("Show the readme file of the installed product");

    keepInstallerButton = createButton(afterInstallComposite, TEXT_KEEP, null, null);
    keepInstallerButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        dialog.showKeepInstaller();
      }
    });
    keepInstallerButton.setToolTipText(KeepInstallerUtil.KEEP_INSTALLER_DESCRIPTION);

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
          dialog.clearMessage();
          dialog.setButtonsEnabled(false);

          setEnabled(false);

          installButton.setCurrentState(State.INSTALLING);
          installButton.setProgress(0);

          installStack.setTopControl(duringInstallContainer);
          installStack.setVisible(true);
          layout(true, true);

          forceFocus();

          install();
        }
      }
    });

    GridLayout errorLayout = UIUtil.createGridLayout(1);
    errorLayout.verticalSpacing = 0;

    errorComposite = new Composite(installStack, SWT.NONE);
    errorComposite.setLayout(errorLayout);

    // Exclude browser from focus traversal because otherwise we get
    // a strange traversal behavior
    List<Control> tabList = new ArrayList<Control>(Arrays.asList(container.getTabList()));
    tabList.remove(browserComposite);
    container.setTabList(tabList.toArray(new Control[tabList.size()]));

    // Just for debugging
    // installStack.setVisible(true);
    // installStack.setTopControl(errorComposite);
    // installButton.setProgress(0.98f);
    // installButton.setCurrentState(InstallLaunchButton.State.INSTALLING);
    // installButton.setEnabled(false);
  }

  private FlatButton createButton(Composite parent, String text, String toolTip, Image icon)
  {
    FlatButton button = new FlatButton(parent, SWT.PUSH);
    button.setBackground(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);
    button.setText(text);
    button.setCornerWidth(10);
    button.setAlignment(SWT.CENTER);
    button.setFont(SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///10/normal")));
    button.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 22).create());
    button.setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);

    if (icon != null)
    {
      button.setImage(icon);
    }

    if (toolTip != null)
    {
      button.setToolTipText(toolTip);
    }

    return button;
  }

  protected void productVersionSelected(ProductVersion productVersion)
  {
    if (selectedProductVersion != productVersion)
    {
      selectedProductVersion = productVersion;

      String requiredJavaVersion = productVersion.getRequiredJavaVersion();
      javaController.setJavaVersion(requiredJavaVersion);

      ProductPage.saveProductVersionSelection(installer.getCatalogManager(), selectedProductVersion);

      versionCombo.setToolTipText(SetupCoreUtil.getLabel(selectedProductVersion));
    }

    installFolder = getDefaultInstallationFolder();
    setFolderText(installFolder);
  }

  @Override
  public boolean setFocus()
  {
    boolean focused = folderText.setFocus();

    // Set the cursor to the end
    UIUtil.setSelectionToEnd(folderText);

    return focused;
  }

  public void setProduct(Product product)
  {
    this.product = product;

    String html = SimpleInstallerDialog.getPageTemplate();
    html = html.replace("%CONTENT%", SimpleProductPage.renderProduct(product, true, false));

    browser.setText(html, true);

    productVersions.clear();
    versionCombo.removeAll();

    ProductVersion defaultProductVersion = ProductPage.getDefaultProductVersion(installer.getCatalogManager(), product);
    int i = 0;
    int selection = 0;

    for (ProductVersion productVersion : ProductPage.getValidProductVersions(product))
    {
      if (defaultProductVersion == null)
      {
        defaultProductVersion = productVersion;
      }

      String label = SetupCoreUtil.getLabel(productVersion);
      productVersions.put(label, productVersion);
      versionCombo.add(label);

      if (productVersion == defaultProductVersion)
      {
        selection = i;
      }

      ++i;
    }

    versionCombo.select(selection);
    productVersionSelected(defaultProductVersion);
    UIUtil.setSelectionToEnd(versionCombo);

    installButton.setCurrentState(State.INSTALL);
    installStack.setVisible(false);
    installed = false;

    setEnabled(true);
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
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
  }

  public boolean refreshJREs()
  {
    if (javaController != null)
    {
      javaController.refresh();
      return true;
    }

    return false;
  }

  private String getDefaultInstallationFolder()
  {
    String name = product.getName();
    int lastDot = name.lastIndexOf('.');
    if (lastDot != -1)
    {
      name = name.substring(lastDot + 1);
    }

    name += "-" + selectedProductVersion.getName().replace('.', '-');

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
            installError = MESSAGE_FAILURE;
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
    if (dialog.getPool() != null)
    {
      P2Util.getAgentManager().setDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName(), dialog.getPool());
      System.setProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION, dialog.getPool().getLocation().getAbsolutePath());
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
    final User user = setupContext.getUser();

    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        EList<LicenseInfo> licenses = LicensePrePrompter.execute(getShell(), user);
        if (licenses != null)
        {
          user.getAcceptedLicenses().addAll(licenses);
          BaseUtil.saveEObject(user);
        }
      }
    });

    UserAdjuster userAdjuster = new UserAdjuster();
    userAdjuster.adjust(user, installFolder);
    IOUtil.writeLines(FILE_INSTALL_ROOT, "UTF-8", Collections.singletonList(installRoot));

    SimplePrompter prompter = new SimplePrompter();

    performer = SetupTaskPerformer.create(uriConverter, prompter, Trigger.BOOTSTRAP, setupContext, false);
    performer.getUnresolvedVariables().clear();
    performer.put(ILicense.class, ProgressPage.LICENSE_CONFIRMER);
    performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(user, false));
    performer.put(OS.class, OS.INSTANCE.getForBitness(javaController.getBitness()));
    performer.setOffline(false);
    performer.setMirrors(true);
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

    dialog.setButtonsEnabled(true);
    setEnabled(true);

    installButton.setCurrentState(State.INSTALL);
    installStack.setVisible(false);
  }

  private void installFinished()
  {
    readmePath = null;

    if (installError == null)
    {
      installed = true;

      installButton.setCurrentState(State.LAUNCH);
      installButton.setToolTipText("Launch");

      keepInstallerButton.setVisible(KeepInstallerUtil.canKeepInstaller());

      Scope scope = selectedProductVersion;
      while (scope != null)
      {
        Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
        if (annotation != null)
        {
          readmePath = annotation.getDetails().get(AnnotationConstants.KEY_README_PATH);
          if (readmePath != null)
          {
            showReadmeButton.setEnabled(true);
            break;
          }
        }

        scope = scope.getParentScope();
      }

      showSuccessMessage();

      backButton.setEnabled(true);
    }
    else
    {
      setEnabled(true);
      installButton.setCurrentState(State.INSTALL);
      showErrorMessage();
    }

    dialog.setButtonsEnabled(true);
  }

  private void showSuccessMessage()
  {
    dialog.showMessage(MESSAGE_SUCCESS, Type.SUCCESS, true);

    installStack.setTopControl(afterInstallComposite);
    installStack.setVisible(true);
    layout(true, true);
  }

  private void showErrorMessage()
  {
    Runnable action = null;
    String errorMessage = installError;

    if (isInstallLogAvailable())
    {
      action = new MessageOverlay.RunnableWithLabel()
      {
        public void run()
        {
          openInstallLog();
        }

        public String getLabel()
        {
          return "Show log.";
        }
      };
    }

    dialog.showMessage(errorMessage, Type.ERROR, false, action);

    installStack.setTopControl(errorComposite);
    installStack.setVisible(true);
    layout(true, true);
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

    if (EXIT_AFTER_LAUNCH)
    {
      dialog.exitSelected();
    }
  }

  private void setFolderText(String dir)
  {
    folderText.setText(dir);
    folderText.setToolTipText(dir);
  }

  private void validatePage()
  {
    String errorMessage = null;

    errorMessage = validateJREs();

    if (errorMessage == null)
    {
      errorMessage = validateFolderText(folderText.getText());
    }

    if (errorMessage != null)
    {
      // TODO it would also be possible to add an action which
      // will trigger the same action as the folderButton
      dialog.showMessage(errorMessage, Type.ERROR, false, null);
    }
    else
    {
      dialog.clearMessage();
    }

    installButton.setEnabled(errorMessage == null);
  }

  @Override
  public void aboutToShow()
  {
    super.aboutToShow();
    keepInstallerButton.setVisible(KeepInstallerUtil.canKeepInstaller());
  }

  private String validateJREs()
  {
    Object input = javaViewer.getInput();
    if (input instanceof Collection<?> && ((Collection<?>)input).isEmpty())
    {
      return "Configuration of JRE failed.";
    }

    return null;
  }

  private String validateFolderText(String dir)
  {
    installFolder = dir;

    // TODO validate dir and set an appropriate error message
    String errorMessage = null;

    try
    {
      if (StringUtil.isEmpty(installFolder))
      {
        return "Installation folder must be specified.";
      }

      File folder = new File(installFolder);

      File parentFolder = folder.getParentFile();
      if (parentFolder == null || !parentFolder.canWrite() || folder.isFile() || folder.isDirectory() && !folder.canWrite())
      {
        return "Folder " + folder.getName() + " cannot be created.";
      }

      installRoot = parentFolder.getAbsolutePath();
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return errorMessage;
  }

  private void openInstallLog()
  {
    File installationLogFile = new File(installFolder, SETUP_LOG_FILE);
    dialog.showInstallationLog(installationLogFile);
  }

  private boolean isInstallLogAvailable()
  {
    return new File(installFolder, SETUP_LOG_FILE).exists();
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
  private final class SimplePrompter extends HashMap<String, String>implements SetupPrompter
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
        double progress = work / totalWork;

        try
        {
          installButton.setProgress((float)progress);

          if (!ObjectUtil.equals(name, lastName))
          {
            lastName = name;
            if (!done)
            {
              installButton.setToolTipText(StringUtil.safe(name));
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

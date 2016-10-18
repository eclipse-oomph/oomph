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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageCheckbox;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.core.DownloadArtifactEvent;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.AbstractSetupTaskContext;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.installer.SimpleInstallLaunchButton.State;
import org.eclipse.oomph.setup.internal.installer.SimpleMessageOverlay.Type;
import org.eclipse.oomph.setup.internal.installer.SimpleProductPage.ProductComposite;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.ui.EnablementComposite;
import org.eclipse.oomph.setup.ui.EnablementDialog;
import org.eclipse.oomph.setup.ui.JREDownloadHandler;
import org.eclipse.oomph.setup.ui.LicensePrePrompter;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.SelectionMemento;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileFilter;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class SimpleVariablePage extends SimpleInstallerPage
{
  public static final int PROGRESS_WATCHDOG_TIMEOUT = PropertiesUtil.getProperty("oomph.progress.watchdog.timeout", 15);

  private static final boolean EXIT_AFTER_LAUNCH = !PropertiesUtil.isProperty("oomph.no.exit.after.launch");

  private static final Preference PREF_STARTMENU_ENTRY = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("startMenuEntry");

  private static final Preference PREF_DESKTOP_SHORTCUT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("desktopShortcut");

  private static final Preference PREF_INSTALL_ROOT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("installRoot");

  private static final File FILE_INSTALL_ROOT = new File(SetupInstallerPlugin.INSTANCE.getUserLocation().toFile(), PREF_INSTALL_ROOT.key() + ".txt");

  private static final String TEXT_README = "show readme file";

  private static final String TEXT_SYSTEM_EXPLOROR = "open in system explorer";

  private static final String TEXT_KEEP = "keep installer";

  private static final String MESSAGE_SUCCESS = "Installation completed successfully.";

  private static final String MESSAGE_FAILURE = "Installation failed with an error.";

  protected static final boolean JRE_CHOICE = Boolean.valueOf(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_JRE_CHOICE, "true"));

  private final SelectionMemento selectionMemento;

  private final Map<String, ProductVersion> productVersions = new HashMap<String, ProductVersion>();

  private Product product;

  private ProductVersion selectedProductVersion;

  private String readmePath;

  private Browser detailBrowser;

  private ProductComposite detailComposite;

  private CCombo versionCombo;

  private Label versionLabel;

  private Control versionSpacer;

  private ImageCheckbox bitness32Button;

  private ImageCheckbox bitness64Button;

  private Label bitness32Spacer;

  private Label bitness64Spacer;

  private JREController javaController;

  private Label javaLabel;

  private Control javaSpacer;

  private ComboViewer javaViewer;

  private FlatButton javaButton;

  private Control javaTrailingSpacer;

  private Text folderText;

  private FlatButton folderButton;

  private String installRoot;

  private String installFolder;

  private Thread installThread;

  private StackComposite installStack;

  private SimpleInstallLaunchButton installButton;

  private String installError;

  private boolean installed;

  private SetupTaskPerformer performer;

  private SimpleProgress progress;

  private FlatButton cancelButton;

  private FlatButton openInSystemExplorerButton;

  private FlatButton showReadmeButton;

  private FlatButton keepInstallerButton;

  private Composite afterInstallComposite;

  private Composite errorComposite;

  private Composite container;

  private SimpleCheckbox createStartMenuEntryButton;

  private SimpleCheckbox createDesktopShortcutButton;

  private Runnable preInstallAction;

  public SimpleVariablePage(final Composite parent, final SimpleInstallerDialog dialog, SelectionMemento selectionMemento)
  {
    super(parent, dialog, true);
    this.selectionMemento = selectionMemento;
  }

  @Override
  protected void createContent(Composite container)
  {
    this.container = container;

    container.setBackgroundMode(SWT.INHERIT_FORCE);
    container.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    String powerShell = KeepInstallerUtil.getPowerShell();

    // Row 1
    GridData browserLayoutData = GridDataFactory.fillDefaults().indent(0, 13).grab(true, false).create();
    Point defaultSize = SimpleInstallerDialog.getDefaultSize(container);
    browserLayoutData.heightHint = defaultSize.y * (powerShell != null ? 20 : 34) / 100;

    Composite detailArea = new Composite(container, SWT.NONE);
    detailArea.setLayoutData(browserLayoutData);
    detailArea.setLayout(new FillLayout());

    if (UIUtil.isBrowserAvailable())
    {
      detailBrowser = new Browser(detailArea, SWT.NO_SCROLL);
      detailBrowser.addLocationListener(new LocationAdapter()
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
    }
    else
    {
      detailBrowser = null;
      detailComposite = new ProductComposite(detailArea, null, null);
    }

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
    versionLabel = createLabel(variablesComposite, "Product Version");

    // Spacer to get a little bit more distance between labels and input fields
    versionSpacer = spacer(variablesComposite);
    versionSpacer.setLayoutData(GridDataFactory.swtDefaults().hint(17, SWT.DEFAULT).create());

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

    if (JREManager.BITNESS_CHANGEABLE)
    {
      bitness32Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit_enabled.png"),
          SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit_selected.png"));
      bitness32Button.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit_disabled.png"));
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

      bitness64Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit_enabled.png"),
          SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit_selected.png"));
      bitness64Button.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit_disabled.png"));
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
    }
    else
    {
      bitness32Spacer = new Label(variablesComposite, SWT.NONE);
      bitness64Spacer = new Label(variablesComposite, SWT.NONE);
    }

    // Row 4
    javaLabel = createLabel(variablesComposite, "Java VM");

    javaSpacer = spacer(variablesComposite);

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
        javaCombo.setToolTipText(jre == null ? null : jre.getJavaHome().toString());
      }

      @Override
      protected void setLabel(String text)
      {
        super.setLabel(text + " ");
      }
    };

    javaTrailingSpacer = spacer(variablesComposite);

    // Row 5
    final Label installFolderLabel = createLabel(variablesComposite, "Installation Folder");

    spacer(variablesComposite);

    folderText = createTextField(variablesComposite);
    folderText.addModifyListener(new ModifyListener()
    {
      /**
       * A class for delayed validation of the install folder.
       * @author Ed Merks
       */
      class Validator extends UIUtil.DelayedRunnable
      {
        public Validator()
        {
          super(folderText, 350);
        }

        @Override
        protected void perform()
        {
          validatePage();
        }

        @Override
        protected void prepareForDispatch()
        {
          // Immediately disable the install button if the install folder is currently invalid.
          if (validateInstallFolder() != null)
          {
            installButton.setEnabled(false);
          }
        }
      }

      private final Validator validator = new Validator();

      public void modifyText(ModifyEvent e)
      {
        final FocusSelectionAdapter focusSelectionAdapter = (FocusSelectionAdapter)folderText.getData(FocusSelectionAdapter.ADAPTER_KEY);
        focusSelectionAdapter.setNextSelectionRange(folderText.getSelection());

        String folder = folderText.getText().trim();
        installFolder = folder;
        folderText.setToolTipText(installFolder);

        validator.schedule();

        String productInstallFolder = getProductInstallFolder().toString();
        installFolderLabel.setToolTipText("Install into the folder '" + productInstallFolder + "'");
        installButton.setToolTipText("Start installing the product into '" + productInstallFolder + "'");
      }
    });

    new FileCompletionSupport(folderText)
    {
      @Override
      protected void setFolderTextAndSelect(String folder)
      {
        SimpleVariablePage.this.setFolderText(folder);
      }
    };

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
        dialog.setText(PropertiesUtil.getProductName());
        dialog.setMessage("Select an installation folder:");

        if (!StringUtil.isEmpty(installFolder))
        {
          try
          {
            File existingFolder = IOUtil.getExistingFolder(getEffectiveInstallFolder());
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

          UIUtil.getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              folderText.setFocus();
            }
          });
        }
      }
    });

    // Pin rows
    if (powerShell != null)
    {
      {
        spacer(variablesComposite);
        spacer(variablesComposite);
        spacer(variablesComposite);
        spacer(variablesComposite);
        spacer(variablesComposite);
      }

      {
        spacer(variablesComposite);
        spacer(variablesComposite);
        spacer(variablesComposite);

        createStartMenuEntryButton = createCheckbox(variablesComposite, "create start menu entry");
        createStartMenuEntryButton.setChecked(PREF_STARTMENU_ENTRY.get(true));
        createStartMenuEntryButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            PREF_STARTMENU_ENTRY.set(createStartMenuEntryButton.isChecked());
          }
        });

        spacer(variablesComposite);
      }

      {
        spacer(variablesComposite);
        spacer(variablesComposite);
        spacer(variablesComposite);

        createDesktopShortcutButton = createCheckbox(variablesComposite, "create desktop shortcut");
        createDesktopShortcutButton.setChecked(PREF_DESKTOP_SHORTCUT.get(true));
        createDesktopShortcutButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            PREF_DESKTOP_SHORTCUT.set(createDesktopShortcutButton.isChecked());
          }
        });

        spacer(variablesComposite);
      }
    }

    spacer(variablesComposite);
    spacer(variablesComposite);
    spacer(variablesComposite);

    installButton = new SimpleInstallLaunchButton(variablesComposite);
    Point defaultInstallButtonSize = installButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    installButton
        .setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).hint(SWT.DEFAULT, defaultInstallButtonSize.y + 3).indent(0, 32).create());
    installButton.setCurrentState(SimpleInstallLaunchButton.State.INSTALL);

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
        if (progress != null)
        {
          progress.setCanceled(true);
        }
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
          String relativeProductFolder = performer.getRelativeProductFolder();
          java.net.URI readmeURI = new File(installFolder, relativeProductFolder + "/" + readmePath).toURI();
          dialog.showReadme(readmeURI);
        }
      }
    });
    showReadmeButton.setToolTipText("Show the readme file of the installed product");

    openInSystemExplorerButton = createButton(afterInstallComposite, TEXT_SYSTEM_EXPLOROR, null, null);
    openInSystemExplorerButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String launchLocation = getProductInstallFolder().toURI().toString();
        OS.INSTANCE.openSystemBrowser(launchLocation);
      }
    });

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
          if (preInstallAction != null)
          {
            preInstallAction.run();
          }

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
    tabList.remove(detailArea);
    container.setTabList(tabList.toArray(new Control[tabList.size()]));

    // Just for debugging
    // installStack.setVisible(true);
    // installStack.setTopControl(errorComposite);
    // installButton.setProgress(0.98f);
    // installButton.setCurrentState(InstallLaunchButton.State.INSTALLING);
    // installButton.setEnabled(false);
  }

  @Override
  protected void backSelected()
  {
    if (promptLaunchProduct("You're about to install another product"))
    {
      super.backSelected();
    }
  }

  public boolean promptLaunchProduct(String currentAction)
  {
    if (installButton != null && installButton.getCurrentState() == State.LAUNCH)
    {
      MessageDialog prompt = new MessageDialog(dialog, "Launch Product", null,
          currentAction + " without having launched the installed product.\n\nDo you want to launch it now?", MessageDialog.QUESTION,
          new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL }, 0);

      int result = prompt.open();
      if (result == 0)
      {
        // Yes: Do launch; then continue.
        launchProduct(false);
        return true;
      }

      if (result == 1)
      {
        // No: Do not launch; but continue.
        return true;
      }

      // Cancel: Do not launch; and do not continue.
      return false;
    }

    // Nothing to launch; just continue.
    return true;
  }

  private FlatButton createButton(Composite parent, String text, String toolTip, Image icon)
  {
    FlatButton button = new FlatButton(parent, SWT.PUSH);
    button.setBackground(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);
    button.setFocusForegroundColor(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);
    button.setText(text);
    button.setCornerWidth(10);
    button.setAlignment(SWT.CENTER);
    button.setFont(SimpleInstallerDialog.getFont(1, "normal"));
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

      String toolTipText = ProductPage.getToolTipText(selectedProductVersion);
      versionLabel.setToolTipText(toolTipText);
      versionCombo.setToolTipText(toolTipText);
    }

    setFolderText(getDefaultInstallationFolder());
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
    ProductVersion defaultProductVersion = ProductPage.getDefaultProductVersion(installer.getCatalogManager(), product);
    List<ProductVersion> validProductVersions = ProductPage.getValidProductVersions(product, PRODUCT_VERSION_FILTER);
    for (ProductVersion productVersion : validProductVersions)
    {
      if (defaultProductVersion == null || !validProductVersions.contains(defaultProductVersion))
      {
        defaultProductVersion = productVersion;
      }
    }

    if (defaultProductVersion != null)
    {
      setProductVersion(defaultProductVersion);
    }
  }

  public void setProductVersion(ProductVersion productVersion)
  {
    product = productVersion.getProduct();

    if (detailBrowser != null)
    {
      String html = SimpleInstallerDialog.getPageTemplate();
      html = html.replace("%CONTENT%", SimpleProductPage.renderProduct(product, true));

      detailBrowser.setText(html, true);
    }
    else
    {
      detailComposite.setProduct(product);
    }

    productVersions.clear();
    versionCombo.removeAll();

    int i = 0;
    int selection = -1;
    List<ProductVersion> validProductVersions = ProductPage.getValidProductVersions(product, PRODUCT_VERSION_FILTER);
    for (ProductVersion version : validProductVersions)
    {
      String label = SetupCoreUtil.getLabel(version);
      productVersions.put(label, version);
      versionCombo.add(label);

      if (version == productVersion)
      {
        selection = i;
      }

      ++i;
    }

    // If the product version is filtered out of the valid available versions, add a choice for it anyway.
    if (selection == -1)
    {
      String label = SetupCoreUtil.getLabel(productVersion);
      productVersions.put(label, productVersion);
      versionCombo.add(label);
      selection = i;
    }

    versionCombo.select(selection);
    productVersionSelected(productVersion);
    UIUtil.setSelectionToEnd(versionCombo);

    installButton.setCurrentState(State.INSTALL);
    installStack.setVisible(false);
    installed = false;

    UIUtil.asyncExec(container, new Runnable()
    {
      public void run()
      {
        setEnabled(true);
      }
    });
  }

  private void setVisible(Control control, boolean visible)
  {
    if (control != null)
    {
      control.setVisible(visible);

      Object layoutData = control.getLayoutData();
      if (layoutData == null)
      {
        layoutData = new GridData();
        control.setLayoutData(layoutData);
      }

      if (layoutData instanceof GridData)
      {
        GridData gridData = (GridData)layoutData;
        gridData.exclude = !visible;
      }
    }
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    versionCombo.setEnabled(enabled);

    javaViewer.getControl().setEnabled(enabled);
    javaButton.setEnabled(enabled);

    folderText.setEnabled(enabled);
    folderButton.setEnabled(enabled);

    if (createStartMenuEntryButton != null)
    {
      createStartMenuEntryButton.setEnabled(enabled);
    }

    if (createDesktopShortcutButton != null)
    {
      createDesktopShortcutButton.setEnabled(enabled);
    }

    boolean versionVisible = versionCombo.getItemCount() != 1;
    setVisible(versionLabel, versionVisible);
    setVisible(versionSpacer, versionVisible);
    setVisible(versionCombo.getParent(), versionVisible);
    setVisible(bitness32Button, versionVisible);
    setVisible(bitness32Spacer, versionVisible);
    setVisible(bitness64Button, versionVisible);
    setVisible(bitness64Spacer, versionVisible);

    if (JREManager.BITNESS_CHANGEABLE)
    {
      int bitness = javaController.getBitness();
      bitness32Button.setEnabled(enabled);
      bitness32Button.setVisible(versionVisible && (enabled || bitness == 32));
      bitness64Button.setEnabled(enabled);
      bitness64Button.setVisible(versionVisible && (enabled || bitness == 64));
    }

    IStructuredSelection structuredSelection = javaViewer.getStructuredSelection();
    boolean javaVisible = JRE_CHOICE || !(structuredSelection.getFirstElement() instanceof JRE);
    setVisible(javaLabel, javaVisible);
    setVisible(javaSpacer, javaVisible);
    setVisible(javaViewer.getCCombo().getParent(), javaVisible);
    setVisible(javaButton, javaVisible);
    setVisible(javaTrailingSpacer, javaVisible);

    container.layout(true, true);
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

  private File getProductInstallFolder()
  {
    String productFolderName = SetupTaskPerformer.getProductFolderName(selectedProductVersion, OS.INSTANCE);
    String relativeProductFolderName = OS.INSTANCE.getRelativeProductFolder(productFolderName);
    File result = new File(getEffectiveInstallFolder(), relativeProductFolderName);
    return result;
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
        // Default to ${user.home}/eclipse, unless there is a file at that location, or what looks like an existing Eclipse installation.
        // In that case default just to ${user.home}.
        String defaultValue = PropertiesUtil.getUserHome();
        File defaultInstallRoot = new File(defaultValue, "eclipse");
        if (!defaultInstallRoot.exists())
        {
          defaultValue = defaultInstallRoot.toString();
        }
        else if (defaultInstallRoot.isDirectory())
        {
          boolean isEclipseInstallation = false;
          for (File file : defaultInstallRoot.listFiles())
          {
            if ("eclipse.ini".equals(file.getName()))
            {
              isEclipseInstallation = true;
            }
          }

          if (!isEclipseInstallation)
          {
            defaultValue = defaultInstallRoot.toString();
          }
        }

        installRoot = PREF_INSTALL_ROOT.get(defaultValue);
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
          installCancel();
        }
        catch (OperationCanceledException ex)
        {
          installCancel();
        }
        catch (UnloggedException ex)
        {
          installError = ex.getMessage();
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

  @SuppressWarnings("restriction")
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
    String vmPath = ProductPage.getVMOption(jre);

    ResourceSet resourceSet = installer.getResourceSet();
    URIConverter uriConverter = resourceSet.getURIConverter();

    SetupContext setupContext = SetupContext.create(resourceSet, selectedProductVersion);
    Installation installation = setupContext.getInstallation();
    final User user = setupContext.getUser();

    if (UIUtil.isBrowserAvailable())
    {
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
    }

    UserAdjuster userAdjuster = new UserAdjuster();
    userAdjuster.adjust(user, installFolder);

    try
    {
      IOUtil.writeLines(FILE_INSTALL_ROOT, "UTF-8", Collections.singletonList(installRoot));

      SimplePrompter prompter = new SimplePrompter(OS.INSTANCE.getForBitness(javaController.getBitness()), vmPath);
      performer = SetupTaskPerformer.create(uriConverter, prompter, Trigger.BOOTSTRAP, setupContext, false);

      final List<VariableTask> unresolvedVariables = prompter.getUnresolvedVariables();
      if (!unresolvedVariables.isEmpty())
      {
        StringBuilder builder = new StringBuilder();
        for (VariableTask variable : unresolvedVariables)
        {
          if (builder.length() != 0)
          {
            builder.append(", ");
          }

          builder.append(variable.getName());
        }

        final String variables = builder.toString();
        final boolean multi = unresolvedVariables.size() > 1;

        UIUtil.syncExec(new Runnable()
        {
          public void run()
          {
            String message = "The variable" + (multi ? "s" : "") + " " + variables + " " + (multi ? "are" : "is") + " undefined. You likely declared "
                + (multi ? "them" : "it")
                + " in your 'user.setup' file without appropriate restrictions, so that you can't use the installer's simple mode anymore."
                + "\n\nDo you want to switch to the advanced mode now?";

            if (MessageDialog.openQuestion(getShell(), "Undefined Variables", message))
            {
              unresolvedVariables.clear();
              dialog.switchToAdvancedMode();
            }
          }
        });

        if (unresolvedVariables.isEmpty())
        {
          return;
        }

        throw new UnloggedException("Undefined variable" + (multi ? "s" : "") + ": " + variables);
      }

      EList<SetupTask> triggeredSetupTasks = performer.getTriggeredSetupTasks();
      final Map<EClass, EList<SetupTask>> enablementTasks = EnablementComposite.getEnablementTasks(triggeredSetupTasks);
      if (!enablementTasks.isEmpty())
      {
        UIUtil.syncExec(

            getDisplay(), new Runnable()
            {
              public void run()
              {
                EnablementDialog enablementDialog = new EnablementDialog(getShell(), "the installer", enablementTasks);
                if (enablementDialog.open() == EnablementDialog.OK)
                {
                  selectionMemento.setProductVersion(EcoreUtil.getURI(selectedProductVersion));
                  dialog.restart();
                }

                installCancel();
              }
            });

        throw new OperationCanceledException();
      }

      performer.getUnresolvedVariables().clear();
      performer.put(UIServices.class, Installer.SERVICE_UI);
      performer.put(ILicense.class, ProgressPage.LICENSE_CONFIRMER);
      performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(user, false));
      performer.setOffline(false);
      performer.setMirrors(true);
      performer.setProgress(progress);
      performer.log("Executing " + performer.getTrigger().toString().toLowerCase() + " tasks");
      performer.put(org.eclipse.equinox.internal.provisional.p2.core.eventbus.ProvisioningListener.class, new DownloadArtifactLister());
      performer.perform(progress);
      performer.recordVariables(installation, null, user);
      performer.savePasswords();

      File configurationLocation = performer.getProductConfigurationLocation();
      installation.eResource().setURI(URI.createFileURI(new File(configurationLocation, "org.eclipse.oomph.setup/installation.setup").toString()));
      BaseUtil.saveEObject(installation);
    }
    finally
    {
      userAdjuster.undo();
      BaseUtil.saveEObject(user);
    }
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

    UIUtil.syncExec(getDisplay(), new Runnable()
    {
      public void run()
      {
        dialog.setButtonsEnabled(true);
        setEnabled(true);

        installButton.setCurrentState(State.INSTALL);
        installStack.setVisible(false);

        dialog.clearMessage();
      }
    });
  }

  private void installFinished()
  {
    readmePath = null;
    boolean success = installError == null;

    if (performer != null)
    {
      Installation installation = performer.getInstallation();
      if (installation != null)
      {
        ProductVersion productVersion = installation.getProductVersion();
        if (productVersion != null)
        {
          OS os = performer.getOS();
          SetupCoreUtil.sendStats(success, productVersion, os);
        }
      }
    }

    if (success)
    {
      installed = true;

      installButton.setCurrentState(State.LAUNCH);
      File launchLocation = performer.getExecutableInfo().getLaunchLocation();
      installButton.setToolTipText("Launch '" + launchLocation + "'");

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
            break;
          }
        }

        scope = scope.getParentScope();
      }

      showReadmeButton.setVisible(readmePath != null);

      openInSystemExplorerButton.setToolTipText("Open the folder '" + getProductInstallFolder() + "' in the system explorer");

      if (createStartMenuEntryButton != null || createDesktopShortcutButton != null)
      {
        File executable = performer.getExecutableInfo().getExecutable();

        // TODO The entire naming of the shortcuts should be revisited and made more flexible with BrandingInfo annotations.

        ProductCatalog productCatalog = product.getProductCatalog();
        String catalogName = "user.products".equals(productCatalog.getName()) ? "" : productCatalog.getLabel();
        int firstDot = catalogName.indexOf('.');
        if (firstDot != -1)
        {
          catalogName = catalogName.substring(0, firstDot);
        }

        String productName = product.getName();
        if (productName.startsWith("epp.package."))
        {
          productName = productName.substring("epp.package.".length());
        }

        productName = productName.replace('.', ' ');

        String qualifiedProductName = productName + " " + selectedProductVersion.getName().replace('.', ' ');

        String shortCutName = StringUtil.capAll(StringUtil.isEmpty(catalogName) ? qualifiedProductName : catalogName + " " + qualifiedProductName);

        if (createStartMenuEntryButton != null && createStartMenuEntryButton.isChecked())
        {
          KeepInstallerUtil.createShortCut("Programs", "Eclipse", executable.getAbsolutePath(), shortCutName);
        }

        if (createDesktopShortcutButton != null && createDesktopShortcutButton.isChecked())
        {
          KeepInstallerUtil.createShortCut("Desktop", null, executable.getAbsolutePath(), shortCutName);
        }
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
      action = new SimpleMessageOverlay.RunnableWithLabel()
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
    launchProduct(EXIT_AFTER_LAUNCH);
  }

  private void launchProduct(boolean exitAfterLaunch)
  {
    try
    {
      ProgressPage.launchProduct(performer);
    }
    catch (Exception ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }

    if (exitAfterLaunch)
    {
      dialog.exitQuiet();
    }
  }

  private void setFolderText(String folder)
  {
    folderText.setText(folder);

    final FocusSelectionAdapter focusSelectionAdapter = (FocusSelectionAdapter)folderText.getData(FocusSelectionAdapter.ADAPTER_KEY);
    int index = folder.length();
    Point selection = new Point(index, index);
    folderText.setSelection(selection);
    focusSelectionAdapter.setNextSelectionRange(selection);
  }

  private void validatePage()
  {
    preInstallAction = null;

    String errorMessage = null;

    Type type = Type.ERROR;
    errorMessage = validateJREs();

    if (errorMessage == null)
    {
      Pair<Type, String> folderValidationMesage = validateInstallFolder();
      if (folderValidationMesage != null)
      {
        type = folderValidationMesage.getElement1();
        errorMessage = folderValidationMesage.getElement2();
      }

      if (errorMessage == null || type == Type.WARNING)
      {
        OS os = OS.INSTANCE.getForBitness(javaController.getBitness());
        String productFolderName = AbstractSetupTaskContext.getProductFolderName(selectedProductVersion, os);
        String relativeProductFolderName = OS.INSTANCE.getRelativeProductFolder(productFolderName);
        folderValidationMesage = validateChildFolder(IOUtil.getCanonicalFile(getEffectiveInstallFolder()), relativeProductFolderName, true);
        if (folderValidationMesage != null)
        {
          type = folderValidationMesage.getElement1();
          errorMessage = folderValidationMesage.getElement2();
        }
      }
    }

    if (isTop())
    {
      if (errorMessage != null)
      {
        if (type == Type.WARNING)
        {
          preInstallAction = new Runnable()
          {
            public void run()
            {
              preInstallAction = null;
              String folder = IOUtil.getCanonicalFile(getEffectiveInstallFolder()).toString();
              setFolderText(folder);
            }
          };
        }

        dialog.showMessage(errorMessage, type, false, preInstallAction, preInstallAction);
      }
      else
      {
        dialog.clearMessage();
      }
    }

    installButton.setEnabled(errorMessage == null || type == Type.WARNING);
  }

  @Override
  public void aboutToShow()
  {
    super.aboutToShow();
    keepInstallerButton.setVisible(KeepInstallerUtil.canKeepInstaller());
    validatePage();
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

  private File getEffectiveInstallFolder()
  {
    File folder = new File(installFolder.trim());
    if (!folder.isAbsolute())
    {
      folder = new File(PropertiesUtil.getUserHome(), "eclipse/" + installFolder.trim());
    }

    return folder;
  }

  private Pair<Type, String> validateInstallFolder()
  {
    File canonicalFolder = IOUtil.getCanonicalFile(getEffectiveInstallFolder());
    File parentFolder = canonicalFolder.getParentFile();
    Pair<Type, String> errorMessage = validateFolder(canonicalFolder.getParentFile(), canonicalFolder.getName());
    if (errorMessage != null)
    {
      return errorMessage;
    }

    if (!canonicalFolder.toString().equals(installFolder.trim()))
    {
      if (StringUtil.isEmpty(installFolder.trim()))
      {
        return Pair.create(Type.WARNING, "The folder is unspecified so '" + canonicalFolder + "' will be used.");
      }

      return Pair.create(Type.WARNING, "The folder is specified as '" + installFolder + "' but '" + canonicalFolder + "' will be used.");
    }

    installRoot = parentFolder == null ? canonicalFolder.getAbsolutePath() : parentFolder.getAbsolutePath();

    return errorMessage;
  }

  private Pair<Type, String> validateFolder(File parentFolder, String childFolder)
  {
    if (parentFolder == null)
    {
      return Pair.create(Type.ERROR, "The product cannot be installed in the root of the file system.");
    }

    if (parentFolder.exists())
    {
      if (parentFolder.isDirectory())
      {
        if (IOUtil.canWriteFolder(parentFolder))
        {
          try
          {
            new File(parentFolder, childFolder).getCanonicalFile();
            Pair<Type, String> result = validateChildFolder(parentFolder, childFolder, false);

            // The folder exists, but that's not a problem we want to report in this case.
            if (result == null || result.getElement1() == Type.WARNING)
            {
              return null;
            }

            return result;
          }
          catch (Exception ex)
          {
            return Pair.create(Type.ERROR, "Folder '" + childFolder + "' cannot be created in the folder '" + parentFolder + "'.");
          }
        }

        Pair<Type, String> result = validateChildFolder(parentFolder, childFolder, false);
        if (result != null)
        {
          // The folder exists, but that's not a problem we want to report in this case.
          if (result.getElement1() == Type.WARNING)
          {
            return null;
          }

          return result;
        }

        return Pair.create(Type.ERROR, "Folder '" + childFolder + "' cannot be created in the read-only folder '" + parentFolder + "'.");
      }

      return Pair.create(Type.ERROR,
          "Folder '" + childFolder + "' cannot be created in the folder '" + parentFolder + "' because '" + parentFolder.getName() + "' exists as a file.");
    }

    return validateFolder(parentFolder.getParentFile(), parentFolder.getName());
  }

  private Pair<Type, String> validateChildFolder(File parentFolder, String childFolder, boolean isProductLocation)
  {
    String prefix = isProductLocation ? "Product folder '" : "Folder '";
    File child = new File(parentFolder, childFolder);
    if (child.exists())
    {
      if (child.isDirectory())
      {
        if (IOUtil.canWriteFolder(child))
        {
          return Pair.create(Type.WARNING, prefix + child + " already exists.");
        }

        return Pair.create(Type.ERROR, prefix + child + "' cannot be used because it is read-only.");
      }

      return Pair.create(Type.ERROR, prefix + child + "' cannot be used because it exists as file.");
    }

    try
    {
      new File(parentFolder, childFolder).getCanonicalFile();
    }
    catch (Exception ex)
    {
      return Pair.create(Type.ERROR, prefix + childFolder + "' cannot be created.");
    }

    return null;
  }

  private File getLogFile()
  {
    return performer.getLogFile();
  }

  private void openInstallLog()
  {
    File installationLogFile = getLogFile();
    dialog.showInstallationLog(installationLogFile);
  }

  private boolean isInstallLogAvailable()
  {
    return performer != null && getLogFile().exists();
  }

  private SimpleCheckbox createCheckbox(Composite parent, String text)
  {
    final SimpleCheckbox checkbox = new SimpleCheckbox(parent);
    checkbox.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
    checkbox.setText(text);
    checkbox.setChecked(true);
    return checkbox;
  }

  /**
   * @author Ed Merks
   */
  @SuppressWarnings("restriction")
  private class DownloadArtifactLister implements org.eclipse.equinox.internal.provisional.p2.core.eventbus.SynchronousProvisioningListener
  {
    public synchronized void notify(EventObject event)
    {
      if (event instanceof DownloadArtifactEvent)
      {
        DownloadArtifactEvent downloadArtifactEvent = (DownloadArtifactEvent)event;
        URI artifactURI = URI.createURI(downloadArtifactEvent.getArtifactURI().toString());
        if (downloadArtifactEvent.isCompleted())
        {
          progress.removeArtifactURI(artifactURI);
        }
        else
        {
          progress.addArtifactURI(artifactURI);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class UnloggedException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;

    public UnloggedException(String message)
    {
      super(message);
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

    private final List<VariableTask> unresolvedVariables = new ArrayList<VariableTask>();

    private final OS os;

    private final String vmPath;

    public OS getOS()
    {
      return os;
    }

    public SimplePrompter(OS os, String vmPath)
    {
      this.os = os;
      this.vmPath = vmPath;
    }

    public String getVMPath()
    {
      return vmPath;
    }

    public List<VariableTask> getUnresolvedVariables()
    {
      return unresolvedVariables;
    }

    public boolean promptVariables(List<? extends SetupTaskContext> performers)
    {
      for (SetupTaskContext performer : performers)
      {
        List<VariableTask> unresolvedVariables = ((SetupTaskPerformer)performer).getUnresolvedVariables();
        for (VariableTask variable : unresolvedVariables)
        {
          String value = getValue(variable);
          if (value == null)
          {
            this.unresolvedVariables.add(variable);
          }
        }
      }

      return unresolvedVariables.isEmpty();
    }

    public String getValue(VariableTask variable)
    {
      String name = variable.getName();
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

    private volatile double lastWork;

    private volatile long reportWarningTimeout;

    private volatile boolean canceled;

    private volatile boolean done;

    private String lastName;

    private final Set<URI> artifactURIs = new HashSet<URI>();

    private Set<URI> lastArtifactURIs;

    public synchronized void addArtifactURI(URI uri)
    {
      artifactURIs.add(uri);
    }

    public synchronized void removeArtifactURI(URI uri)
    {
      artifactURIs.remove(uri);
    }

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
      Set<URI> artifactURIs;

      synchronized (this)
      {
        name = this.name;
        totalWork = this.totalWork;
        work = this.work;
        canceled = this.canceled;
        done = this.done;
        artifactURIs = new HashSet<URI>(this.artifactURIs);
      }

      if (!canceled && !done)
      {
        String safeName = StringUtil.safe(name);

        if (PROGRESS_WATCHDOG_TIMEOUT != 0)
        {
          long now = System.currentTimeMillis();
          boolean slowArtifactDownload = !artifactURIs.isEmpty() && artifactURIs.equals(lastArtifactURIs);
          if ((lastWork == work || slowArtifactDownload) && !isModalShellInForeground())
          {
            if (now >= reportWarningTimeout)
            {
              if (slowArtifactDownload)
              {
                StringBuilder message = new StringBuilder();
                Set<URI> hosts = new HashSet<URI>();
                for (URI artifactURI : artifactURIs)
                {
                  URI hostURI = URI.createHierarchicalURI(artifactURI.scheme(), artifactURI.authority(), null, null, null);
                  if (hosts.add(hostURI))
                  {
                    if (message.length() != 0)
                    {
                      message.append(", ");
                    }

                    message.append(hostURI);
                  }
                }

                if (hosts.size() > 1)
                {
                  message.insert(0, "the following hosts: ");
                }

                message.insert(0, "Artifact download is progressing very slowly from ");
                dialog.showMessage(message.toString().replace(' ', '\u00a0'), Type.WARNING, false);
              }
              else
              {
                dialog.showMessage(("The installation process is taking longer than usual: " + safeName).replace(' ', '\u00a0'), Type.WARNING, false);
              }

              installButton.setProgressAnimationSpeed(0.4f);
              resetWatchdogTimer(now);
            }
          }
          else
          {
            installButton.startProgressAnimation();
            installButton.setProgressAnimationSpeed(1);
            dialog.clearMessage();
            resetWatchdogTimer(now);
          }

          lastWork = work;
          lastArtifactURIs = artifactURIs;
        }

        double progress = work / totalWork;

        try
        {
          installButton.setProgress((float)progress);

          if (!ObjectUtil.equals(name, lastName))
          {
            lastName = name;
            installButton.setToolTipText(safeName);
          }
        }
        catch (SWTException ex)
        {
          return;
        }

        reschedule();
      }
    }

    private boolean isModalShellInForeground()
    {
      Shell mainShell = getShell();
      Shell[] shells = getDisplay().getShells();

      for (Shell shell : shells)
      {
        if (shell != mainShell)
        {
          int style = shell.getStyle();
          if ((style & SWT.APPLICATION_MODAL) != 0 || (style & SWT.PRIMARY_MODAL) != 0 || (style & SWT.SYSTEM_MODAL) != 0)
          {
            return true;
          }
        }
      }

      return false;
    }

    private void resetWatchdogTimer(long now)
    {
      reportWarningTimeout = now + PROGRESS_WATCHDOG_TIMEOUT * 1000;
    }

    private void reschedule()
    {
      UIUtil.timerExec(100, getDisplay(), this);
    }

    private void schedule()
    {
      resetWatchdogTimer(System.currentTimeMillis());
      UIUtil.asyncExec(getDisplay(), this);
    }
  }

  /**
   * @author Ed Merks
   */
  private static class FileCompletionSupport extends KeyAdapter implements VerifyListener
  {
    private final Text folderText;

    private Shell shell;

    public FileCompletionSupport(Text text)
    {
      folderText = text;
      if (OS.INSTANCE.isMac())
      {
        folderText.addKeyListener(this);
      }
      else
      {
        folderText.addVerifyListener(this);
      }
    }

    protected void setFolderTextAndSelect(String folder)
    {
      folderText.setText(folder);
      int length = folder.length();
      folderText.setSelection(new Point(length, length));
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
      if (e.keyCode == ' ' && e.stateMask == SWT.CTRL)
      {
        e.doit = false;
        open();
      }
    }

    public void verifyText(VerifyEvent e)
    {
      if (e.keyCode == ' ' && e.stateMask == SWT.CTRL)
      {
        e.doit = false;
        open();
      }
    }

    private void open()
    {
      if (shell == null)
      {
        new Dialog(folderText.getShell())
        {
          private org.eclipse.swt.widgets.List list;

          private ModifyListener modifyListener = new ModifyListener()
          {
            public void modifyText(ModifyEvent e)
            {
              updater.schedule();
            }
          };

          private FocusListener focusListener = new FocusAdapter()
          {
            @Override
            public void focusLost(FocusEvent e)
            {
              checkFocus();
            }
          };

          private MouseListener mouseListener = new MouseAdapter()
          {
            @Override
            public void mouseDown(MouseEvent e)
            {
              checkFocus();
            }
          };

          private ControlListener controlListener = new ControlListener()
          {
            public void controlResized(ControlEvent e)
            {
              shell.dispose();
            }

            public void controlMoved(ControlEvent e)
            {
              shell.dispose();
            }
          };

          private KeyListener keyListener = new KeyAdapter()
          {
            @Override
            public void keyReleased(KeyEvent e)
            {
              if (e.keyCode == SWT.ARROW_UP)
              {
                int itemCount = list.getItemCount();
                if (itemCount > 0)
                {
                  shell.setFocus();
                  list.select(itemCount - 1);
                  list.showSelection();
                }
              }
              else if (e.keyCode == SWT.ARROW_DOWN)
              {
                int itemCount = list.getItemCount();
                if (itemCount > 0)
                {
                  shell.setFocus();
                  int selectionIndex = list.getSelectionIndex();
                  list.select(selectionIndex + 1);
                }
              }
              else if (e.keyCode == '\r')
              {
                int selectionIndex = list.getSelectionIndex();
                if (selectionIndex != -1)
                {
                  String folder = list.getItem(selectionIndex);
                  File file = new File(folder);
                  if (!folder.endsWith(File.separator) && file.isDirectory() && !IOUtil.canWriteFolder(file))
                  {
                    folder += File.separator;
                  }

                  setFolderTextAndSelect(folder);
                }
              }
            }
          };

          /**
           * A class for delayed update of the list.
           * @author Ed Merks
           */
          class Updater extends UIUtil.DelayedRunnable
          {
            public Updater(Shell shell)
            {
              super(shell, 350);
            }

            @Override
            protected void perform()
            {
              update();
            }
          }

          private Updater updater;

          private void checkFocus()
          {
            UIUtil.asyncExec(list, new Runnable()
            {
              public void run()
              {
                if (shell.getDisplay().getActiveShell() != shell)
                {
                  shell.dispose();
                }
              }
            });
          }

          public void open()
          {
            Shell parent = getParent();
            shell = new Shell(parent, SWT.MODELESS | SWT.NO_TRIM);
            Control wrapper = folderText.getParent();
            Rectangle folderTextBounds = wrapper.getBounds();
            Point position = wrapper.getParent().toDisplay(folderTextBounds.x, folderTextBounds.y + folderTextBounds.height);
            shell.setBounds(position.x, position.y, folderTextBounds.width, 100);
            shell.setLayout(new FillLayout());

            updater = new Updater(shell);

            list = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);

            list.addMouseListener(new MouseAdapter()
            {
              @Override
              public void mouseUp(MouseEvent e)
              {
                int selectionIndex = list.getSelectionIndex();
                if (selectionIndex != -1)
                {
                  apply(list.getItem(selectionIndex));
                }
              }
            });

            list.addKeyListener(new KeyAdapter()
            {
              @Override
              public void keyReleased(KeyEvent e)
              {
                if (e.keyCode == '\r')
                {
                  int selectionIndex = list.getSelectionIndex();
                  if (selectionIndex != -1)
                  {
                    apply(list.getItem(selectionIndex));
                  }
                }
              }
            });

            shell.addShellListener(new ShellAdapter()
            {
              @Override
              public void shellDeactivated(ShellEvent e)
              {
                shell.dispose();
              }
            });

            shell.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                folderText.removeModifyListener(modifyListener);
                folderText.removeFocusListener(focusListener);
                folderText.removeMouseListener(mouseListener);
                folderText.removeKeyListener(keyListener);
                if (!folderText.getShell().isDisposed())
                {
                  folderText.getShell().removeControlListener(controlListener);
                }

                shell = null;
              }
            });

            folderText.addModifyListener(modifyListener);
            folderText.addFocusListener(focusListener);
            folderText.addMouseListener(mouseListener);
            folderText.addKeyListener(keyListener);
            folderText.getShell().addControlListener(controlListener);

            update();

            shell.setVisible(true);
          }

          private void update()
          {
            if (!list.isDisposed())
            {
              list.removeAll();
              Point selection = folderText.getSelection();
              String folderLiteral = StringUtil.trimLeft(folderText.getText().substring(0, selection.x));
              File folder = IOUtil.getCanonicalFile(new File(folderLiteral));
              final String name = folder.getName().toLowerCase();
              if (folderLiteral.length() == 0)
              {
                File[] roots = File.listRoots();
                if (roots != null)
                {
                  for (File root : roots)
                  {
                    list.add(root.getPath());
                  }
                }
              }
              else
              {
                boolean isExplicitFolder = folderLiteral.endsWith("/") || folderLiteral.endsWith("\\");
                final String nameFilter = isExplicitFolder ? "" : name;

                File searchFolder = isExplicitFolder ? folder : folder.getParentFile();
                if (searchFolder != null)
                {
                  File[] folders = searchFolder.listFiles(new FileFilter()
                  {
                    public boolean accept(File file)
                    {
                      return file.isDirectory() && file.getName().toLowerCase().startsWith(nameFilter);
                    }
                  });

                  if (folders != null)
                  {
                    for (File root : folders)
                    {
                      list.add(root.getPath());
                    }
                  }
                }
                else
                {
                  File[] roots = File.listRoots();
                  if (roots != null)
                  {
                    String lowerCaseFolderLiteral = folderLiteral.toLowerCase();
                    for (File root : roots)
                    {
                      String path = root.getPath();
                      if (path.length() == 1)
                      {
                        File[] folders = root.listFiles(new FileFilter()
                        {
                          public boolean accept(File file)
                          {
                            return file.isDirectory() && file.getName().toLowerCase().startsWith(nameFilter);
                          }
                        });

                        if (folders != null)
                        {
                          for (File child : folders)
                          {
                            list.add(child.getPath());
                          }
                        }
                      }
                      else
                      {
                        if (path.toLowerCase().startsWith(lowerCaseFolderLiteral))
                        {
                          list.add(root.getPath());
                        }
                      }
                    }
                  }
                }
              }
            }

            if (list.getItemCount() != 0)
            {
              list.select(0);
            }

            int width = list.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;

            Rectangle clientArea = shell.getClientArea();
            Rectangle newClientArea = shell.computeTrim(clientArea.x, clientArea.y, width, clientArea.height);
            Rectangle bounds = shell.getBounds();
            if (bounds.width < newClientArea.width)
            {
              bounds.width = newClientArea.width;
              shell.setBounds(bounds);
            }
          }

          private void apply(String folder)
          {
            File file = new File(folder);
            if (!folder.endsWith(File.separator) && file.isDirectory() && !IOUtil.canWriteFolder(file))
            {
              folder += File.separator;
            }

            setFolderTextAndSelect(folder);
            shell.dispose();
          }
        }.open();
      }
    }
  }
}

/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.p2.internal.ui.P2UIPlugin;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer.ExecutableInfo;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.installer.DesktopSupport.ShortcutType;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.ui.wizards.ConfigurationProcessor;
import org.eclipse.oomph.setup.ui.wizards.ExtensionsDialog;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage.ConfigurationListener;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Installer extends SetupWizard
{
  private final Installer.SelectionMemento selectionMemento;

  private final Set<URI> delayedResourceURIs = new LinkedHashSet<URI>();

  private final Map<ConfigurationListener, ToolItem> extensionButtons = new HashMap<ConfigurationListener, ToolItem>();

  private final Listener openListener;

  private boolean indexLoaded;

  public Installer(Installer.SelectionMemento theSelectionMemento, UIServices serviceUI)
  {
    selectionMemento = theSelectionMemento;
    setTrigger(Trigger.BOOTSTRAP);
    getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
    setSetupContext(SetupContext.createUserOnly(getResourceSet()));
    setWindowTitle(PropertiesUtil.getProductName());

    openListener = new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        handleArgument(event.text);
      }
    };

    Display display = Display.getDefault();
    display.addListener(SWT.OpenDocument, openListener);
    display.addListener(SWT.OpenUrl, openListener);

    String targetOS = PropertiesUtil.getProperty("oomph.setup.target.osgi.os", OS.INSTANCE.getOsgiOS()); //$NON-NLS-1$
    String targetWS = PropertiesUtil.getProperty("oomph.setup.target.osgi.ws", OS.INSTANCE.getOsgiWS()); //$NON-NLS-1$
    String targetArch = PropertiesUtil.getProperty("oomph.setup.target.osgi.arch", OS.INSTANCE.getOsgiArch()); //$NON-NLS-1$

    for (OS os : OS.INSTANCES)
    {
      if (targetOS.equals(os.getOsgiOS()) && targetWS.equals(os.getOsgiWS()) && targetArch.equals(os.getOsgiArch()))
      {
        setOS(os);
        break;
      }
    }
  }

  public void handleArgument(String argument)
  {
    handleURI(URISchemeUtil.getResourceURI(argument));
  }

  public void handleURI(URI uri)
  {
    delayedResourceURIs.add(uri);
  }

  protected boolean canHandleDelayedResourceURIs()
  {
    // Avoid doing anything until the index is loaded.
    if (!indexLoaded)
    {
      return false;
    }

    if (isSimple())
    {
      // Only this page supports drag and drop of configurations, so only process for that page.
      SimpleInstallerDialog simpleInstallerDialog = (SimpleInstallerDialog)getShell();
      return simpleInstallerDialog.getTopPage() instanceof SimpleProductPage;
    }

    // Only these pages support drag and drop of configurations, so only process for these pages.
    IWizardPage currentPage = getCurrentPage();
    return currentPage instanceof ProductPage || currentPage instanceof ProjectPage;
  }

  public Installer.SelectionMemento getSelectionMemento()
  {
    return selectionMemento;
  }

  @Override
  public String getHelpPath()
  {
    return HELP_FOLDER + "DocInstallWizard.html"; //$NON-NLS-1$
  }

  @Override
  public void addPages()
  {
    addPage(new ProductPage(selectionMemento));
    addPage(new ProjectPage(selectionMemento));
    addPage(new ExtensionPage());
    super.addPages();

    getShell().getDisplay().asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        loadIndex();
      }
    });
  }

  @Override
  protected ProgressPage createProgressPage()
  {
    return new ProgressPage()
    {
      @Override
      protected boolean canCreateDesktopShortcut()
      {
        return KeepInstallerUtil.getDesktopSupport() != null;
      }

      @Override
      protected void createDesktopShortcut()
      {
        try
        {
          SetupTaskPerformer performer = getPerformer();
          ProductVersion productVersion = performer.getSetupContext().getInstallation().getProductVersion();
          Product product = productVersion.getProduct();
          ExecutableInfo info = performer.getExecutableInfo();
          File executable = info.getExecutable();
          File productFolder = executable.getParentFile();
          File productContainerFolder = productFolder.getParentFile();
          File productContainerContainerFolder = productContainerFolder == null ? null : productContainerFolder.getParentFile();
          String shortcut = productContainerFolder == null || productContainerContainerFolder == null ? IOUtil.encodeFileName(productFolder.toString())
              : productContainerFolder.getName() + " - " + IOUtil.encodeFileName(productContainerContainerFolder.toString()); //$NON-NLS-1$

          String appName = null;
          Scope scope = productVersion;
          while (scope != null)
          {
            Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
            if (annotation != null)
            {
              appName = annotation.getDetails().get(AnnotationConstants.KEY_APP_NAME);
              if (appName != null)
              {
                break;
              }
            }

            scope = scope.getParentScope();
          }

          KeepInstallerUtil.createShortCut(ShortcutType.DESKTOP, null, executable, shortcut, product.getDescription(), product.getName(), appName);
        }
        catch (Exception ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }
      }
    };
  }

  @Override
  protected void indexLoaded(Index index)
  {
    super.indexLoaded(index);
    getCatalogManager().indexLoaded(index);

    IWizardPage currentPage = getCurrentPage();
    if (currentPage instanceof ProjectPage)
    {
      ((ProjectPage)currentPage).gotoPreviousPage();
    }

    indexLoaded = true;
  }

  @Override
  public void addAppliedConfigurationResource(Resource configurationResource)
  {
    super.addAppliedConfigurationResource(configurationResource);

    if (isSimple())
    {
      SimpleInstallerDialog simpleInstallerDialog = (SimpleInstallerDialog)getShell();
      simpleInstallerDialog.setExtensionsAvailable(true);
    }
    else
    {
      for (Map.Entry<ConfigurationListener, ToolItem> entry : extensionButtons.entrySet())
      {
        ToolItem toolItem = entry.getValue();
        if (toolItem == null)
        {
          entry.setValue(createExtensionsButton(entry.getKey()));
        }
        entry.getKey().checkConfigurationAvailability();
      }
    }
  }

  @Override
  public void removeAppliedConfigurationResource(Resource configurationResource)
  {
    super.removeAppliedConfigurationResource(configurationResource);
    if (getAppliedConfigurationResources().isEmpty())
    {
      if (isSimple())
      {
        SimpleInstallerDialog simpleInstallerDialog = (SimpleInstallerDialog)getShell();
        simpleInstallerDialog.setExtensionsAvailable(false);
      }
      else
      {
        for (final Map.Entry<ConfigurationListener, ToolItem> entry : extensionButtons.entrySet())
        {
          ToolItem toolItem = entry.getValue();
          toolItem.dispose();
          entry.setValue(null);
          entry.getKey().checkConfigurationAvailability();
        }
      }
    }
  }

  @Override
  public void addConfigurationListener(ConfigurationListener configurationListener)
  {
    extensionButtons.put(configurationListener, null);
  }

  private ToolItem createExtensionsButton(ConfigurationListener configurationListener)
  {
    ToolBar toolBar = configurationListener.getToolBar();
    int index = toolBar.getItemCount() > 0 && toolBar.getItem(0).getData("ConfigurationSelectionAdapter") != null ? 1 : 0; //$NON-NLS-1$
    ToolItem extensionsButton = new ToolItem(toolBar, SWT.NONE, index);
    extensionsButton.setToolTipText(SimpleInstallerDialog.EXTENSIONS_MENU_ITEM_DESCRIPTION);
    extensionsButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/extension_notification_overlay.png")); //$NON-NLS-1$
    AccessUtil.setKey(extensionsButton, "manageExtensions"); //$NON-NLS-1$
    extensionsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Collection<? extends Resource> appliedConfigurationResources = getAppliedConfigurationResources();
        ExtensionsDialog extensionsDialog = new ExtensionsDialog(getShell(), appliedConfigurationResources);
        if (extensionsDialog.open() == Window.OK)
        {
          Installation installation = getSetupContext().getInstallation();
          boolean reset = false;
          Collection<? extends Resource> result = extensionsDialog.getResult();
          for (Resource resource : appliedConfigurationResources)
          {
            if (!result.contains(resource))
            {
              removeAppliedConfigurationResource(resource);
              if (installation != null)
              {
                URI uri = resource.getURI();
                if (MarketPlaceListing.isMarketPlaceListing(uri))
                {
                  for (SetupTask setupTask : installation.getSetupTasks())
                  {
                    if (setupTask instanceof MacroTask)
                    {
                      Macro macro = ((MacroTask)setupTask).getMacro();
                      if (macro == null || macro.eResource() == null || macro.eResource().getURI().equals(uri))
                      {
                        EcoreUtil.delete(macro);
                        break;
                      }
                    }
                  }
                }
                else
                {
                  reset = true;
                }
              }
            }
          }

          IWizardContainer container = getContainer();
          if (reset && container instanceof InstallerDialog)
          {
            InstallerDialog installerDialog = (InstallerDialog)container;
            installerDialog.reset();
          }
        }
      }
    });

    extensionButtons.put(configurationListener, extensionsButton);

    toolBar.getParent().layout(true);
    toolBar.layout(true);

    return extensionsButton;
  }

  @Override
  public void setPerformer(SetupTaskPerformer performer)
  {
    super.setPerformer(performer);

    if (performer != null)
    {
      performer.put(P2Task.UIServicesInitializer.class, new P2Task.UIServicesInitializer()
      {
        @Override
        public void init(UIServices serviceUI)
        {
          P2UIPlugin.init(serviceUI, getShell().getDisplay());
        }
      });
    }
  }

  public enum MissingIndexStatus
  {
    IGNORE, RETRY, EXIT
  }

  public Installer.MissingIndexStatus handleMissingIndex(Shell shell)
  {
    int answer = new MessageDialog(shell, Messages.Installer_NetworkProblem_title, null, Messages.Installer_TalogNotLoaded_message, MessageDialog.ERROR,
        new String[] { Messages.Installer_Retry_label, Messages.Installer_Configure_label, Messages.Installer_Exit_label }, 0).open();
    switch (answer)
    {
      case 2:
      {
        return Installer.MissingIndexStatus.EXIT;
      }

      case 1:
      {
        new NetworkConnectionsDialog(shell).open();
      }
      //$FALL-THROUGH$

      case 0:
      {
        ResourceSet resourceSet = getResourceSet();
        URI currentIndexLocation = resourceSet.getURIConverter().normalize(SetupContext.INDEX_SETUP_URI);
        ECFURIHandlerImpl.clearExpectedETags();
        reloadIndex(currentIndexLocation);
        return Installer.MissingIndexStatus.RETRY;
      }

      default:
      {
        return Installer.MissingIndexStatus.IGNORE;
      }
    }
  }

  public void runEventLoop(Shell shell)
  {
    // Use our own event loop so we can process delayed events.
    // This ensures that we do not process delayed events while any other modal dialog is in its own event loop.
    Display display = shell.getDisplay();
    while (!shell.isDisposed())
    {
      try
      {
        if (!display.readAndDispatch())
        {
          if (canHandleDelayedResourceURIs() && !delayedResourceURIs.isEmpty())
          {
            // Consume the resources one at a time.
            Iterator<URI> it = delayedResourceURIs.iterator();
            URI resourceURI = it.next();
            it.remove();
            getTransferSupport().urisDropped(Collections.singleton(resourceURI));
          }
          else
          {
            display.sleep();
          }
        }
      }
      catch (Throwable throwable)
      {
        SetupInstallerPlugin.INSTANCE.log(throwable);
      }
    }

    if (!display.isDisposed())
    {
      display.update();
    }

    // Clean up out listeners, because we might restart a new installer but with the same display instance.
    display.removeListener(SWT.OpenDocument, openListener);
    display.removeListener(SWT.OpenUrl, openListener);
  }

  public void switchToNewUserHome(String userHome)
  {
    String launcher = OS.getCurrentLauncher(false);
    if (launcher != null)
    {
      List<String> command = new ArrayList<>();
      command.add(new File(launcher).getAbsolutePath());

      command.addAll(Arrays.asList(Platform.getApplicationArgs()));

      Configuration configuration = getConfiguration();
      if (configuration != null)
      {
        Resource resource = configuration.eResource();
        if (resource != null)
        {
          command.add(resource.getURI().toString());
        }
      }

      File newUserHome = new File(userHome);
      File oldUserHome = new File(PropertiesUtil.getUserHome());
      if (!newUserHome.equals(oldUserHome))
      {
        for (String fileToCopy : List.of(".eclipse/org.eclipse.equinox.security/secure_storage")) //$NON-NLS-1$
        {
          File newFile = new File(userHome, fileToCopy);
          if (!newFile.isFile())
          {
            File oldFile = new File(oldUserHome, fileToCopy);
            if (oldFile.isFile())
            {
              IOUtil.copyFile(oldFile, newFile);
            }
          }
        }
      }

      command.add("-vmargs"); //$NON-NLS-1$
      command.add("-Duser.home=" + userHome); //$NON-NLS-1$
      command.add("-D" + SetupProperties.PROP_SETUP_USER_HOME_ORIGINAL + "=" + PropertiesUtil.getUserHome()); //$NON-NLS-1$ //$NON-NLS-2$
      command.add("-D" + SetupProperties.PROP_INSTALLER_SWITCH_USER_HOME + "=false"); //$NON-NLS-1$ //$NON-NLS-2$
      command.add("-D" + SetupProperties.PROP_INSTALLER_KEEP + "=false"); //$NON-NLS-1$ //$NON-NLS-2$
      command.add(ConfigurationProcessor.SETUP_USER_HOME_REDIRECT_COMMAND_LINE_ARGUMENT);

      try
      {
        Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

}

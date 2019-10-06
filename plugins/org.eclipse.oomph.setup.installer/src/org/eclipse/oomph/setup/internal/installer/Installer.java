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

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.p2.core.CertificateConfirmer;
import org.eclipse.oomph.p2.internal.ui.P2ServiceUI;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ExtensionsDialog;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage.ConfigurationListener;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Installer extends SetupWizard
{
  private final SelectionMemento selectionMemento;

  public final UIServices uiServices;

  private final Set<URI> delayedResourceURIs = new LinkedHashSet<URI>();

  private final Map<ConfigurationListener, ToolItem> extensionButtons = new HashMap<ConfigurationListener, ToolItem>();

  private final Listener openListener;

  private boolean indexLoaded;

  public Installer(SelectionMemento theSelectionMemento)
  {
    selectionMemento = theSelectionMemento;
    setTrigger(Trigger.BOOTSTRAP);
    getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
    setSetupContext(SetupContext.createUserOnly(getResourceSet()));
    setWindowTitle(PropertiesUtil.getProductName());
    uiServices = new P2ServiceUI()
    {
      @Override
      protected Confirmer getUnsignedContentConfirmer()
      {
        return UnsignedContentDialog.createUnsignedContentConfirmer(getUser(), false);
      }

      @Override
      protected CertificateConfirmer getCertificateConfirmer()
      {
        return SetupCoreUtil.createCertificateConfirmer(getUser(), false);
      }

      @Override
      protected UIServices getDelegate()
      {
        return null;
      }
    };

    openListener = new Listener()
    {
      public void handleEvent(Event event)
      {
        handleArgument(event.text);
      }
    };

    Display display = Display.getDefault();
    display.addListener(SWT.OpenDocument, openListener);
    display.addListener(SWT.OpenUrl, openListener);
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

  public UIServices getUiServices()
  {
    return uiServices;
  }

  public SelectionMemento getSelectionMemento()
  {
    return selectionMemento;
  }

  @Override
  public String getHelpPath()
  {
    return HELP_FOLDER + "DocInstallWizard.html";
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
      public void run()
      {
        loadIndex();
      }
    });
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
    int index = toolBar.getItemCount() > 0 && toolBar.getItem(0).getData("ConfigurationSelectionAdapter") != null ? 1 : 0;
    ToolItem extensionsButton = new ToolItem(toolBar, SWT.NONE, index);
    extensionsButton.setToolTipText(SimpleInstallerDialog.EXTENSIONS_MENU_ITEM_DESCRIPTION);
    extensionsButton.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/extension_notification_overlay.png"));
    AccessUtil.setKey(extensionsButton, "manageExtensions");
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
      performer.put(UIServices.class, getUiServices());
    }
  }

  public boolean handleMissingIndex(Shell shell)
  {
    int answer = new MessageDialog(shell, "Network Problem", null,
        "The catalog could not be loaded. Please ensure that you have network access and, if needed, have configured your network proxy.", MessageDialog.ERROR,
        new String[] { "Retry", "Configure Network Proxy...", "Exit" }, 0).open();
    switch (answer)
    {
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
        return true;
      }

      default:
      {
        return false;
      }
    }
  }

  public void runEventLoop(Shell shell)
  {
    // Use our own even loop so we can process delayed events.
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
}

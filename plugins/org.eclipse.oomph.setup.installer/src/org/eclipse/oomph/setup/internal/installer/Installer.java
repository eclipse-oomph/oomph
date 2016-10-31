/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.internal.ui.P2ServiceUI;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.ui.wizards.ProjectPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class Installer extends SetupWizard
{
  public static final P2ServiceUI SERVICE_UI = new P2ServiceUI();

  private final SelectionMemento selectionMemento;

  public Installer(SelectionMemento theSelectionMemento)
  {
    selectionMemento = theSelectionMemento;
    setTrigger(Trigger.BOOTSTRAP);
    getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
    setSetupContext(SetupContext.createUserOnly(getResourceSet()));
    setWindowTitle(PropertiesUtil.getProductName());
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
  }

  @Override
  public void setPerformer(SetupTaskPerformer performer)
  {
    super.setPerformer(performer);

    if (performer != null)
    {
      performer.put(UIServices.class, SERVICE_UI);
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
}

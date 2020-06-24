/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import java.util.Set;

/**
 * @author Eike Stepper
 */
public class RefreshCacheHandler extends AbstractDropdownItemHandler
{
  public RefreshCacheHandler()
  {
    super("RefreshCache", Messages.RefreshCacheHandler_text); //$NON-NLS-1$
  }

  public void run()
  {
    try
    {
      Set<? extends URI> uris = ECFURIHandlerImpl.clearExpectedETags();
      MarketPlaceListing.flush();
      Job mirror = ECFURIHandlerImpl.mirror(uris);
      IWorkbench workbench = PlatformUI.getWorkbench();
      IProgressService progressService = workbench.getProgressService();
      progressService.showInDialog(null, mirror);
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex);
    }
  }
}

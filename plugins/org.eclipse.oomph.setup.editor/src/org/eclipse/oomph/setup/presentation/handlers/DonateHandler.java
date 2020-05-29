/*
 * Copyright (c) 2020 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import javax.net.ssl.SSLEngineResult.Status;

import java.net.URL;

/**
 * @author Ed Merks
 */
public class DonateHandler extends AbstractHandler
{
  public DonateHandler()
  {
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
    try
    {
      String donating = SetupPropertyTester.getDonating();
      int style = IWorkbenchBrowserSupport.AS_EDITOR;
      if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.donate.navigation", "false")))
      {
        style |= IWorkbenchBrowserSupport.NAVIGATION_BAR;
      }

      if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.donate.location", "false")))
      {
        style |= IWorkbenchBrowserSupport.LOCATION_BAR;
      }

      IWebBrowser browser = browserSupport.createBrowser(style, "donate", SetupEditorPlugin.INSTANCE.getString("_UI_Donate_label"), donating);
      browser.openURL(new URL(donating));
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    return Status.OK;
  }
}

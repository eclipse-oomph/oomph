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
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
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
    final IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
    try
    {
      final String donating = SetupPropertyTester.getDonating();
      int style = IWorkbenchBrowserSupport.AS_EDITOR;
      if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.donate.navigation", "false"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      {
        style |= IWorkbenchBrowserSupport.NAVIGATION_BAR;
      }

      if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.donate.location", "false"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      {
        style |= IWorkbenchBrowserSupport.LOCATION_BAR;
      }

      IWebBrowser browser = browserSupport.createBrowser(style, "donate", SetupEditorPlugin.INSTANCE.getString("_UI_Donate_label"), donating); //$NON-NLS-1$ //$NON-NLS-2$
      browser.openURL(new URL(donating));

      // Look for the internal browser instance.
      IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      if (workbenchWindow != null)
      {
        IWorkbenchPage activePage = workbenchWindow.getActivePage();
        if (activePage != null)
        {
          IWorkbenchPart activePart = activePage.getActivePart();
          if (activePart != null)
          {
            try
            {
              // Extract the browser reflectively because it's all hidden implementation detail.
              Object webBrowser = ReflectUtil.getValue("webBrowser", activePart); //$NON-NLS-1$
              Browser newBrowser = ReflectUtil.getValue("browser", webBrowser); //$NON-NLS-1$

              // Add a listener for when a new browser window is opened so that we can open an external browser.
              newBrowser.addOpenWindowListener(new OpenWindowListener()
              {
                public void open(WindowEvent event)
                {
                  // Listen for the URL changing for this new browser.
                  final Browser newBrowser = event.browser;
                  newBrowser.addLocationListener(new LocationAdapter()
                  {
                    @Override
                    public void changing(LocationEvent event)
                    {
                      event.doit = false;

                      // Close this browser's shell.
                      UIUtil.asyncExec(newBrowser, new Runnable()
                      {
                        public void run()
                        {
                          newBrowser.getShell().close();
                        }
                      });

                      try
                      {
                        // Instead open the link in an external browser.
                        IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.AS_EXTERNAL, "external_donate", //$NON-NLS-1$
                            SetupEditorPlugin.INSTANCE.getString("_UI_Donate_label"), donating); //$NON-NLS-1$
                        browser.openURL(new URL(event.location));
                      }
                      catch (Exception ex)
                      {
                        SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
                      }
                    }
                  });
                }
              });
            }
            catch (Exception ex)
            {
              // Ignore reflection exceptions.
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    return Status.OK;
  }
}

/*
 * Copyright (c) 2025 Eclipse contributor and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.util.Request;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import java.net.MalformedURLException;
import java.net.URL;

public class ProblemHandler extends AbstractHandler
{
  public ProblemHandler()
  {
  }

  @Override
  public void setEnabled(Object evaluationContext)
  {
    setBaseEnabled(SetupPropertyTester.getProblem() != null);
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {

    final IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
    try
    {
      String problem = SetupPropertyTester.getProblem();
      URL url = getEnhancedURL(problem);
      IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.AS_EXTERNAL, "problem", //$NON-NLS-1$
          SetupEditorPlugin.INSTANCE.getString("_UI_ReportProblem_label"), problem); //$NON-NLS-1$
      browser.openURL(url);
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    return Status.OK;
  }

  @SuppressWarnings("nls")
  private URL getEnhancedURL(String url) throws MalformedURLException
  {
    Request request = new Request(url);
    IProduct product = Platform.getProduct();
    if (product != null)
    {
      request.put("product-id", product.getId());
      Bundle definingBundle = product.getDefiningBundle();
      if (definingBundle != null)
      {
        String symbolicName = definingBundle.getSymbolicName();
        request.put("bundle-id", symbolicName);
        Version version = definingBundle.getVersion();
        request.put("bundle-version", version.toString());
      }
    }

    request.put("java.vendor", System.getProperty("java.vendor"));
    request.put("java.version", System.getProperty("java.version"));

    return new URL(request.getURI().toString());
  }
}

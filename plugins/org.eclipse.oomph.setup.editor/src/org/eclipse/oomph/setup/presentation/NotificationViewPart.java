/*
 * Copyright (c) 2025 Eclipse contributor and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;

import java.net.URL;
import java.nio.file.Path;

public class NotificationViewPart extends ViewPart
{
  public static final String VIEW_ID = "org.eclipse.oomph.setup.presentation.NotificationViewPart"; //$NON-NLS-1$

  protected Browser browser;

  protected Path location;

  public NotificationViewPart()
  {
  }

  public void setUrl(String url)
  {
    URI uri = URI.createURI(url);
    Composite parent = browser.getParent();
    Color foreground = parent.getForeground();
    Color background = parent.getBackground();
    String colorQueryParameters = //
        "color=" + URI.encodeQuery(getColor(foreground), true) + '&' + // //$NON-NLS-1$
            "background-color=" + URI.encodeQuery(getColor(background), true); //$NON-NLS-1$
    String query = uri.query();
    uri = uri.trimQuery().appendQuery(query == null ? colorQueryParameters : query + '&' + colorQueryParameters);
    browser.setUrl(uri.toString());
  }

  @Override
  public void setPartName(String partName)
  {
    super.setPartName(partName);
  }

  @Override
  public void createPartControl(final Composite parent)
  {
    browser = new Browser(parent, SWT.NONE);
    browser.setJavascriptEnabled(true);

    browser.addTitleListener(event -> {
      if (!event.title.isBlank())
      {
        setPartName(event.title);
      }
    });

    browser.addProgressListener(new ProgressListener()
    {
      @Override
      public void completed(ProgressEvent event)
      {
        browser.removeProgressListener(this);
        browser.setVisible(true);
        UIUtil.asyncExec(() -> {
          browser.addLocationListener(new LocationListener()
          {
            @Override
            public void changing(LocationEvent event)
            {
              handleChanging(event);
            }

            @Override
            public void changed(LocationEvent event)
            {
            }
          });
        });
      }

      @Override
      public void changed(ProgressEvent event)
      {
      }
    });

    browser.setVisible(false);
  }

  @Override
  public void setFocus()
  {
    browser.setFocus();
  }

  protected void handleChanging(LocationEvent event)
  {
    URI location = URI.createURI(event.location);
    String scheme = location.scheme();
    if (scheme != null && scheme.startsWith("eclipse+")) //$NON-NLS-1$
    {
      event.doit = false;
      if ("eclipse+setup".equals(scheme)) //$NON-NLS-1$
      {
        URI setupURI = resolveEclipseURI(location);
        new Job(Messages.NotificationViewPart_ApplyConfigureJob_label)
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
            SetupContext setupContext = SetupContext.create(resourceSet);
            Resource resource = resourceSet.getResource(setupURI, true);

            Configuration configuration = (Configuration)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.CONFIGURATION);
            Installation installation = configuration.getInstallation();
            if (installation != null)
            {
              setupContext.getInstallation().getSetupTasks().addAll(installation.getSetupTasks());
            }

            Workspace workspace = configuration.getWorkspace();
            if (workspace != null)
            {
              setupContext.getWorkspace().getSetupTasks().addAll(workspace.getSetupTasks());
            }

            if (installation != null || workspace != null)
            {
              UIUtil.asyncExec(() -> {
                Shell handlingShell = SetupPropertyTester.getHandlingShell();
                if (handlingShell != null)
                {
                  if (handlingShell.isVisible())
                  {
                    handlingShell.setVisible(true);
                  }

                  handlingShell.setFocus();
                }
                else
                {
                  SetupWizard.Updater.perform(setupContext);
                }
              });
            }

            return Status.OK_STATUS;
          }
        }.schedule();
      }
      else if ("eclipse+external".equals(scheme)) //$NON-NLS-1$
      {
        int style = IWorkbenchBrowserSupport.AS_EXTERNAL;
        final IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
        IWebBrowser browser;
        try
        {
          browser = browserSupport.createBrowser(style, "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          browser.openURL(new URL(resolveEclipseURI(location).toString()));
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
      }
    }
  }

  private URI resolveEclipseURI(URI location)
  {
    String opaquePart = location.opaquePart();
    if (opaquePart != null)
    {
      URI underlyingURI = URI.createURI(opaquePart);
      if (underlyingURI.isRelative())
      {
        URI baseURI = URI.createURI(browser.getUrl());
        underlyingURI = underlyingURI.resolve(baseURI);
      }

      return underlyingURI;
    }

    return location;
  }

  private static String getColor(Color color)
  {
    StringBuilder result = new StringBuilder(9);
    result.append('#');
    int red = color.getRed();
    if (red < 16)
    {
      result.append('0');
    }
    result.append(Integer.toHexString(red));
    int green = color.getGreen();
    if (green < 16)
    {
      result.append('0');
    }
    result.append(Integer.toHexString(green));
    int blue = color.getBlue();
    if (blue < 16)
    {
      result.append('0');
    }
    result.append(Integer.toHexString(blue));
    int alpha = color.getAlpha();
    if (alpha < 16)
    {
      result.append('0');
    }
    result.append(Integer.toHexString(alpha));
    return result.toString();
  }

}

/*
 * Copyright (c) 2025 Eclipse contributor and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.setup.AnnotationConstants;
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
import org.eclipse.oomph.util.Request;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NotificationViewPart extends ViewPart
{
  public static final String VIEW_ID = "org.eclipse.oomph.setup.presentation.NotificationView"; //$NON-NLS-1$

  private final URIConverter uriConverter = new ExtensibleURIConverterImpl();

  private Browser browser;

  private Annotation notification;

  private WorkbenchListener workbenchListener;

  public NotificationViewPart()
  {
    SetupCoreUtil.configureRedirections(uriConverter.getURIMap());
  }

  @Override
  public void init(IViewSite site, IMemento memento) throws PartInitException
  {
    workbenchListener = new WorkbenchListener(site.getWorkbenchWindow().getWorkbench());
    super.init(site, memento);
  }

  public void setNotification(Annotation notification)
  {
    this.notification = notification;
    if (notification != null)
    {
      if (shouldMaximize())
      {
        setPartState(IWorkbenchPage.STATE_MAXIMIZED);
      }

      setUrl(getNotificationURI());
    }
  }

  private boolean shouldMaximize()
  {
    return notification != null && "true".equals(notification.getDetails().get("maximize")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private void restore()
  {
    if (shouldMaximize())
    {
      setPartState(IWorkbenchPage.STATE_RESTORED);
    }
  }

  private void setPartState(int state)
  {
    IWorkbenchPage page = getSite().getPage();
    IWorkbenchPartReference reference = page.getReference(this);
    if (reference != null)
    {
      page.setPartState(reference, state);
    }
  }

  private String getNotificationURI()
  {
    return notification.getDetails().get(AnnotationConstants.KEY_URI);
  }

  @SuppressWarnings("nls")
  private void setUrl(String url)
  {
    Request request = new Request(url);
    IProduct product = Platform.getProduct();
    if (product != null)
    {
      request.put("product-id", product.getId());
      String name = product.getName();
      if (name != null)
      {
        request.put("product-name", name);
      }
      String application = product.getApplication();
      if (application != null)
      {
        request.put("application-id", application);
      }

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

    Composite parent = browser.getParent();
    Color foreground = parent.getForeground();
    Color background = parent.getBackground();
    request.put("color", getColor(foreground));
    request.put("background-color", getColor(background));

    URI enhancedURI = request.getURI();
    browser.setUrl(uriConverter.normalize(enhancedURI).toString());
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
        restore();
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
                  if (!handlingShell.isVisible())
                  {
                    handlingShell.setVisible(true);
                  }

                  handlingShell.setFocus();
                }
                else
                {
                  Map<String, String> oldSystemProperties = new LinkedHashMap<>();
                  Annotation systemPropertiesAnnotation = configuration.getAnnotation(AnnotationConstants.ANNOTATION_SYSTEM_PROPERTIES);
                  if (systemPropertiesAnnotation != null)
                  {
                    for (Map.Entry<String, String> entry : systemPropertiesAnnotation.getDetails())
                    {
                      oldSystemProperties.put(entry.getKey(), System.setProperty(entry.getKey(), entry.getValue()));
                    }
                  }

                  try
                  {
                    SetupWizard.Updater.perform(setupContext);
                  }
                  finally
                  {
                    for (Map.Entry<String, String> entry : oldSystemProperties.entrySet())
                    {
                      if (entry.getValue() == null)
                      {
                        System.clearProperty(entry.getKey());
                      }
                      else
                      {
                        System.setProperty(entry.getKey(), entry.getValue());
                      }
                    }
                  }
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
      else if ("eclipse+command".equals(scheme)) //$NON-NLS-1$
      {
        String opaquePart = location.opaquePart();
        if ("close".equals(opaquePart)) //$NON-NLS-1$
        {
          hide(null);
        }
        else if ("dismiss".equals(opaquePart)) //$NON-NLS-1$
        {
          hide(SetupContext.GLOBAL_SETUPS_LOCATION_URI);
        }
        else if ("dismiss-installation".equals(opaquePart)) //$NON-NLS-1$
        {
          hide(SetupContext.CONFIGURATION_STATE_LOCATION_URI);
        }
        else if ("dismiss-workspace".equals(opaquePart)) //$NON-NLS-1$
        {
          hide(SetupContext.WORKSPACE_STATE_LOCATION_URI);
        }
      }
    }
  }

  @Override
  public void dispose()
  {
    restore();
    if (workbenchListener != null)
    {
      workbenchListener.dispose();
    }
    super.dispose();
  }

  private void hide()
  {
    restore();
    getSite().getPage().hideView(this);
  }

  private void hide(URI uri)
  {
    hide();
    if (uri != null && notification != null)
    {
      SetupUIPlugin.rememberNotificationURI(getNotificationURI(), uri);
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

  private final class WorkbenchListener implements IWorkbenchListener
  {
    private final IWorkbench workbench;

    public WorkbenchListener(IWorkbench workbench)
    {
      this.workbench = workbench;
      workbench.addWorkbenchListener(this);
    }

    @Override
    public boolean preShutdown(IWorkbench workbench, boolean forced)
    {
      restore();
      return true;
    }

    @Override
    public void postShutdown(IWorkbench workbench)
    {
    }

    public void dispose()
    {
      workbench.removeWorkbenchListener(this);
    }
  }

}

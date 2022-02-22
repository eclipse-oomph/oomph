/*
 * Copyright (c) 2018 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class EarlyStart implements IStartup
{
  public static final String EXTENSION_POINT = "org.eclipse.oomph.ui.deferredEarlyStart"; //$NON-NLS-1$

  public static final boolean DEFERR = "true".equals(PropertiesUtil.getProperty(EXTENSION_POINT, "true")); //$NON-NLS-1$//$NON-NLS-2$

  private static final String PRIORITY_PROPERTY = EXTENSION_POINT + ".priority"; //$NON-NLS-1$

  private static final String SCHEDULE_PROPERTY = EXTENSION_POINT + ".schedule"; //$NON-NLS-1$

  private static final int PRIORITY;

  private static final long SCHEDULE;

  static
  {
    Map<String, Integer> priorities = new LinkedHashMap<>();
    priorities.put("INTERACTIVE", Job.INTERACTIVE); //$NON-NLS-1$
    priorities.put("SHORT", Job.SHORT); //$NON-NLS-1$
    priorities.put("LONG", Job.LONG); //$NON-NLS-1$
    priorities.put("BUILD", Job.BUILD); //$NON-NLS-1$
    priorities.put("DECORATE", Job.DECORATE); //$NON-NLS-1$
    String priorityProperty = PropertiesUtil.getProperty(PRIORITY_PROPERTY, "DECORATE"); //$NON-NLS-1$
    Integer priority = priorities.get(priorityProperty);
    if (priority == null)
    {
      UIPlugin.INSTANCE.log(NLS.bind(Messages.EarlyStart_valueMustBeOneOf, new Object[] { priorityProperty, PRIORITY_PROPERTY, priorities.keySet() }));
      PRIORITY = Job.DECORATE;
    }
    else
    {
      PRIORITY = priority;
    }

    String scheduleProperty = PropertiesUtil.getProperty(SCHEDULE_PROPERTY, "5000"); //$NON-NLS-1$
    long schedule;
    try
    {
      schedule = Long.parseLong(scheduleProperty);
      if (schedule < 0L)
      {
        UIPlugin.INSTANCE.log(NLS.bind(Messages.EarlyStart_valueMustBeNonNegative, scheduleProperty, SCHEDULE_PROPERTY));
        schedule = 5000;
      }
    }
    catch (RuntimeException ex)
    {
      UIPlugin.INSTANCE.log(NLS.bind(Messages.EarlyStart_valueMustBeNonNegative, scheduleProperty, SCHEDULE_PROPERTY));
      schedule = 5000L;
    }

    SCHEDULE = schedule;
  }

  @Override
  public void earlyStartup()
  {
    if (DEFERR)
    {
      Job job = new Job(Messages.EarlyStart_jobName)
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          return EarlyStart.this.run(monitor);
        }
      };

      job.setPriority(PRIORITY);
      job.schedule(SCHEDULE);
    }
    else
    {
      run(new NullProgressMonitor());
    }
  }

  protected IStatus run(IProgressMonitor monitor)
  {
    IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT))
    {
      try
      {
        IStartup startup = (IStartup)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
        startup.earlyStartup();
      }
      catch (Throwable throwable)
      {
        UIPlugin.INSTANCE.log(throwable);
      }
    }

    String gitCloneLocation = System.getProperty("org.eclipse.oomph.nls.report"); //$NON-NLS-1$
    if (gitCloneLocation != null)
    {
      File folder = new File(gitCloneLocation);
      if (folder.isDirectory())
      {
        NLSReport nlsReport = new NLSReport(folder);
        final File report = nlsReport.report();
        UIUtil.asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            try
            {
              IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
              IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.AS_EDITOR, "donate", "NLS Report", "Oomph's NLS Report"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              browser.openURL(report.toURI().toURL());
            }
            catch (Exception ex)
            {
              throw new RuntimeException(ex);
            }
          }
        });
      }
    }

    return Status.OK_STATUS;
  }

}

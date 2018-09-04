/*
 * Copyright (c) 2018 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class EarlyStart implements IStartup
{
  public static final String EXTENSION_POINT = "org.eclipse.oomph.ui.deferredEarlyStart";

  public static final boolean DEFERR = "true".equals(PropertiesUtil.getProperty(EXTENSION_POINT, "true"));

  private static final String PRIORITY_PROPERTY = EXTENSION_POINT + ".priority";

  private static final String SCHEDULE_PROPERTY = EXTENSION_POINT + ".schedule";

  private static final int PRIORITY;

  private static final long SCHEDULE;

  static
  {
    Map<String, Integer> priorities = new LinkedHashMap<String, Integer>();
    priorities.put("INTERACTIVE", Job.INTERACTIVE);
    priorities.put("SHORT", Job.SHORT);
    priorities.put("LONG", Job.LONG);
    priorities.put("BUILD", Job.BUILD);
    priorities.put("DECORATE", Job.DECORATE);
    String priorityProperty = PropertiesUtil.getProperty(PRIORITY_PROPERTY, "DECORATE");
    Integer priority = priorities.get(priorityProperty);
    if (priority == null)
    {
      UIPlugin.INSTANCE.log("The value '" + priorityProperty + "' of the property " + PRIORITY_PROPERTY + " must be one of " + priorities.keySet());
      PRIORITY = Job.DECORATE;
    }
    else
    {
      PRIORITY = priority;
    }

    String scheduleProperty = PropertiesUtil.getProperty(SCHEDULE_PROPERTY, "5000");
    long schedule;
    try
    {
      schedule = Long.parseLong(scheduleProperty);
      if (schedule < 0L)
      {
        UIPlugin.INSTANCE.log("The value '" + scheduleProperty + "' of the property " + SCHEDULE_PROPERTY + " must be a non-negative long value");
        schedule = 5000;
      }
    }
    catch (RuntimeException ex)
    {
      UIPlugin.INSTANCE.log("The value '" + scheduleProperty + "' of the property " + SCHEDULE_PROPERTY + " must be a non-negative long value");
      schedule = 5000L;
    }

    SCHEDULE = schedule;
  }

  public void earlyStartup()
  {
    if (DEFERR)
    {
      Job job = new Job("Deferred Early Start")
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
        IStartup startup = (IStartup)configurationElement.createExecutableExtension("class");
        startup.earlyStartup();
      }
      catch (Throwable throwable)
      {
        UIPlugin.INSTANCE.log(throwable);
      }
    }

    return Status.OK_STATUS;
  }

}

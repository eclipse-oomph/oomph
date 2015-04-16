/*
 * Copyright (c) 2014 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

import org.osgi.service.prefs.Preferences;

/**
 *
 * @author Ed Merks
 */
public class UIPropertyTester extends PropertyTester
{
  public static final String SHOW_OFFLINE = "showOffline";

  private static final Preferences PREFERENCES = UIPlugin.INSTANCE.getInstancePreferences();

  // This is a nasty workaround for bug 464582 (Toolbar contributions are missing after startup).
  private static boolean initialized;

  static
  {
    ((IEclipsePreferences)PREFERENCES).addPreferenceChangeListener(new IEclipsePreferences.IPreferenceChangeListener()
    {
      public void preferenceChange(final IEclipsePreferences.PreferenceChangeEvent event)
      {
        if (SHOW_OFFLINE.equals(event.getKey()))
        {
          requestEvaluation("org.eclipse.oomph.ui." + SHOW_OFFLINE, "true".equals(event.getNewValue()));
        }
      }
    });

    // This is a nasty workaround for bug 464582 (Toolbar contributions are missing after startup).
    if (!initialized)
    {
      new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            sleep(2000);
            initialized = true;
            UIPropertyTester.requestEvaluation("org.eclipse.oomph.ui." + SHOW_OFFLINE, true);
          }
          catch (Throwable ex)
          {
            ex.printStackTrace();
          }
        }
      }.start();
    }
  }

  public UIPropertyTester()
  {
  }

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    if (SHOW_OFFLINE.equals(property))
    {
      boolean value = initialized ? PREFERENCES.getBoolean(SHOW_OFFLINE, false) : false;
      return expectedValue.equals(value);
    }

    return false;
  }

  public static void requestEvaluation(final String id, final boolean layout)
  {
    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        if (PlatformUI.isWorkbenchRunning())
        {
          final IWorkbench workbench = PlatformUI.getWorkbench();
          if (workbench != null)
          {
            IEvaluationService service = workbench.getService(IEvaluationService.class);
            if (service != null)
            {
              service.requestEvaluation(id);

              if (layout)
              {
                UIUtil.asyncExec(new Runnable()
                {
                  public void run()
                  {
                    for (IWorkbenchWindow workbenchWindow : workbench.getWorkbenchWindows())
                    {
                      Shell shell = workbenchWindow.getShell();
                      if (shell != null)
                      {
                        shell.layout(true, true);
                      }
                    }
                  }
                });
              }
            }
          }
        }
      }
    });
  }
}

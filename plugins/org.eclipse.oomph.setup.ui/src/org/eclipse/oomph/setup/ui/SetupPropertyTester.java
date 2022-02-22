/*
 * Copyright (c) 2014, 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.internal.ui.UIPropertyTester;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

import org.osgi.service.prefs.Preferences;

/**
 * @author Ed Merks
 */
public class SetupPropertyTester extends PropertyTester
{
  public static final String PREFIX = "org.eclipse.oomph.setup.ui."; //$NON-NLS-1$

  public static final String STARTING = "starting"; //$NON-NLS-1$

  public static final String PERFORMING = "performing"; //$NON-NLS-1$

  public static final String HANDLING = "handling"; //$NON-NLS-1$

  public static final String SYNC_ENABLED = "syncEnabled"; //$NON-NLS-1$

  public static final String SHOW_TOOL_BAR_CONTRIBUTIONS = "showToolBarContributions"; //$NON-NLS-1$

  public static final String SHOW_PROGRESS_IN_WIZARD = "showProgressInWizard"; //$NON-NLS-1$

  public static final String DONATING = "donating"; //$NON-NLS-1$

  private static final Preferences PREFERENCES = SetupUIPlugin.INSTANCE.getInstancePreferences();

  private static boolean starting;

  private static IStatus performingStatus;

  private static Shell performingShell;

  private static Shell handlingShell;

  private static boolean started;

  private static String donating;

  static
  {
    ((IEclipsePreferences)PREFERENCES).addPreferenceChangeListener(new IEclipsePreferences.IPreferenceChangeListener()
    {
      @Override
      public void preferenceChange(final IEclipsePreferences.PreferenceChangeEvent event)
      {
        if (SHOW_TOOL_BAR_CONTRIBUTIONS.equals(event.getKey()))
        {
          UIPropertyTester.requestEvaluation(PREFIX + SHOW_TOOL_BAR_CONTRIBUTIONS, "true".equals(event.getNewValue())); //$NON-NLS-1$
        }
      }
    });

    // This is a nasty workaround for bug 464582 (Toolbar contributions are missing after startup).
    UIUtil.timerExec(2000, new Runnable()
    {
      @Override
      public void run()
      {
        UIPropertyTester.requestEvaluation(PREFIX + SHOW_TOOL_BAR_CONTRIBUTIONS, true);
      }
    });
  }

  public SetupPropertyTester()
  {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    if (STARTING.equals(property))
    {
      return testStarting(receiver, args, expectedValue);
    }

    if (PERFORMING.equals(property))
    {
      return testPerforming(receiver, args, expectedValue);
    }

    if (HANDLING.equals(property))
    {
      return testHandling(receiver, args, expectedValue);
    }

    if (SYNC_ENABLED.equals(property))
    {
      return testSyncEnabled(receiver, args, expectedValue);
    }

    if (SHOW_TOOL_BAR_CONTRIBUTIONS.equals(property))
    {
      return testShowToolBarContributions(receiver, args, expectedValue);
    }

    if (DONATING.equals(property))
    {
      return testDonating(receiver, args, expectedValue);
    }

    return false;
  }

  private boolean testStarting(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(starting);
  }

  private boolean testPerforming(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(performingShell != null);
  }

  private boolean testHandling(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(handlingShell != null);
  }

  private boolean testSyncEnabled(Object receiver, Object[] args, Object expectedValue)
  {
    if (!started)
    {
      return false;
    }

    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(SynchronizerManager.INSTANCE.isSyncEnabled());
  }

  private boolean testShowToolBarContributions(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    boolean value = PREFERENCES.getBoolean(SHOW_TOOL_BAR_CONTRIBUTIONS, false);
    return expectedValue.equals(value);
  }

  private boolean testDonating(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(donating != null);
  }

  public static void setStarting(boolean starting)
  {
    if (!starting)
    {
      started = true;
      UIPropertyTester.requestEvaluation(PREFIX + SYNC_ENABLED, false);
    }

    SetupPropertyTester.starting = starting;
    UIPropertyTester.requestEvaluation(PREFIX + STARTING, false);
  }

  public static IStatus getPerformingStatus()
  {
    return performingStatus;
  }

  public static void setPerformingStatus(IStatus performingStatus)
  {
    SetupPropertyTester.performingStatus = performingStatus;
  }

  public static Shell getPerformingShell()
  {
    return performingShell;
  }

  public static void setPerformingShell(Shell shell)
  {
    SetupPropertyTester.performingShell = shell;

    if (shell != null)
    {
      shell.addDisposeListener(new DisposeListener()
      {
        @Override
        public void widgetDisposed(DisposeEvent e)
        {
          setPerformingStatus(null);
          setPerformingShell(null);
        }
      });
    }

    UIPropertyTester.requestEvaluation(PREFIX + PERFORMING, shell != null);
  }

  public static Shell getHandlingShell()
  {
    return handlingShell;
  }

  public static void setHandlingShell(Shell shell)
  {
    SetupPropertyTester.handlingShell = shell;

    if (shell != null)
    {
      shell.addDisposeListener(new DisposeListener()
      {
        @Override
        public void widgetDisposed(DisposeEvent e)
        {
          setHandlingShell(null);
        }
      });
    }

    UIPropertyTester.requestEvaluation(PREFIX + HANDLING, false);
  }

  public static String getDonating()
  {
    return donating;
  }

  public static void setDonating(String donating)
  {
    SetupPropertyTester.donating = donating;
    UIPropertyTester.requestEvaluation(PREFIX + DONATING, false);
  }

  public static boolean isShowProgressInWizard()
  {
    return PREFERENCES.getBoolean(SHOW_PROGRESS_IN_WIZARD, false);
  }
}

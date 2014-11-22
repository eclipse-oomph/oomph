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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

/**
 * @author Ed Merks
 */
public class SetupPropertyTester extends PropertyTester
{
  private static boolean starting;

  private static Shell handlingShell;

  private static Shell performingShell;

  public static void setStarting(boolean starting)
  {
    SetupPropertyTester.starting = starting;
    requestEvaluation("org.eclipse.oomph.setup.ui.starting");
  }

  private boolean testStarting(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(starting);
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
      shell.setVisible(false);
      shell.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          setPerformingShell(null);
        }
      });
    }

    requestEvaluation("org.eclipse.oomph.setup.ui.performing");
  }

  private boolean testPerforming(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(performingShell != null);
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
        public void widgetDisposed(DisposeEvent e)
        {
          setHandlingShell(null);
        }
      });
    }

    requestEvaluation("org.eclipse.oomph.setup.ui.handling");
  }

  private boolean testHandling(Object receiver, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(handlingShell != null);
  }

  private static void requestEvaluation(String id)
  {
    if (PlatformUI.isWorkbenchRunning())
    {
      UIUtil.syncExec(new Runnable()
      {
        public void run()
        {
          final IWorkbench workbench = PlatformUI.getWorkbench();
          if (workbench != null)
          {
            IEvaluationService service = (IEvaluationService)workbench.getService(IEvaluationService.class);
            if (service != null)
            {
              service.requestEvaluation("org.eclipse.oomph.setup.ui.performing");
            }
          }
        }
      });
    }
  }

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    if ("starting".equals(property))
    {
      return testStarting(receiver, args, expectedValue);
    }

    if ("performing".equals(property))
    {
      return testPerforming(receiver, args, expectedValue);
    }

    if ("handling".equals(property))
    {
      return testHandling(receiver, args, expectedValue);
    }

    return false;
  }
}

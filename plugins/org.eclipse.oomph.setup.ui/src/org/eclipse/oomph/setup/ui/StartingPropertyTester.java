/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

/**
 * @author Ed Merks
 */
public class StartingPropertyTester extends PropertyTester
{
  private static boolean starting;

  public StartingPropertyTester()
  {
  }

  public static void setStarting(boolean starting)
  {
    StartingPropertyTester.starting = starting;

    final IWorkbench workbench = PlatformUI.getWorkbench();
    if (workbench != null)
    {
      IEvaluationService service = (IEvaluationService)workbench.getService(IEvaluationService.class);
      if (service != null)
      {
        service.requestEvaluation("org.eclipse.oomph.setup.ui.starting");
      }
    }
  }

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    if (expectedValue == null)
    {
      expectedValue = Boolean.TRUE;
    }

    return expectedValue.equals(starting);
  }
}

/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eike Stepper
 */
public abstract class InactivityDetector
{
  private final ActivityAdapter activityAdapter = new ActivityAdapter()
  {
    @Override
    protected void handleActivity()
    {
      resetLastActivity();

      if (inactive)
      {
        inactive = false;
        handleInactivity(display, inactive);
      }
    }
  };

  private final Runnable detectorRunnable = new Runnable()
  {
    public void run()
    {
      if (System.currentTimeMillis() - lastActivity > inactivityThreshold)
      {
        if (!inactive)
        {
          inactive = true;
          handleInactivity(display, inactive);
        }
      }

      if (!display.isDisposed())
      {
        display.timerExec(detectorInterval, this);
      }
    }
  };

  private final int detectorInterval;

  private final int inactivityThreshold;

  private boolean inactive;

  private long lastActivity;

  private Display display;

  public InactivityDetector(int detectorInterval, int inactivityThreshold)
  {
    this.detectorInterval = detectorInterval;
    this.inactivityThreshold = inactivityThreshold;
    resetLastActivity();
  }

  public final boolean isInactive()
  {
    return inactive;
  }

  public final void monitor(final Control control)
  {
    final Display controlDisplay = control.getDisplay();
    controlDisplay.asyncExec(new Runnable()
    {
      public void run()
      {
        if (!control.isDisposed())
        {
          activityAdapter.attach(control);

          if (display == null)
          {
            display = controlDisplay;
            display.timerExec(detectorInterval, detectorRunnable);
          }
        }
      }
    });
  }

  protected abstract void handleInactivity(Display display, boolean inactive);

  private void resetLastActivity()
  {
    lastActivity = System.currentTimeMillis();
  }
}

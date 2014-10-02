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
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ed Merks
 */
public final class AccessUtil
{
  public static Image captureControl(Control control)
  {
    Rectangle bounds;
    GC gc;
    if (control instanceof Shell)
    {
      Shell shell = (Shell)control;
      bounds = shell.getBounds();
      bounds.x -= 12;
      bounds.y -= 11;
      bounds.width += 28;
      bounds.height += 28;
      gc = new GC(control.getDisplay());
    }
    else
    {
      Point size = control.getSize();
      bounds = new Rectangle(0, 0, size.x, size.y);
      gc = new GC(control);
    }

    final Image image = new Image(control.getDisplay(), bounds.width, bounds.height);
    gc.copyArea(image, bounds.x, bounds.y);
    gc.dispose();
    return image;
  }

  public static void busyWait(int milliseconds)
  {
    busyWait(milliseconds, null);
  }

  public static void busyWait(final int milliseconds, final Runnable runnable)
  {
    final Display display = UIUtil.getDisplay();
    final AtomicBoolean done = new AtomicBoolean();
    new Thread()
    {
      @Override
      public void run()
      {
        try
        {
          // Block on the runnable, if there is one.
          if (runnable != null)
          {
            runnable.run();
          }

          // Wait 2 more seconds.
          UIUtil.asyncExec(new Runnable()
          {
            public void run()
            {
              UIUtil.timerExec(milliseconds, new Runnable()
              {
                public void run()
                {
                  // Ensure that the sleeping display loop wakes up.
                  done.set(true);
                }
              });
            }
          });
        }
        catch (Exception ex)
        {
          // Ignore
        }
      }
    }.start();

    while (!display.isDisposed() && !done.get())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }
  }

  private AccessUtil()
  {
  }
}

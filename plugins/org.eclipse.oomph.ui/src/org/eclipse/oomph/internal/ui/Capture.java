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

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Capture<T>
{
  protected abstract T create(Shell shell);

  protected abstract Shell getShell(T element);

  protected abstract void open(T element);

  public Image capture()
  {
    final AtomicReference<Image> image = new AtomicReference<Image>();
    final Display display = PlatformUI.getWorkbench().getDisplay();
    final org.eclipse.jface.dialogs.Dialog backgroundDialog = new org.eclipse.jface.dialogs.Dialog((Shell)null)
    {
      {
        setShellStyle(SWT.MAX);
      }

      @Override
      protected Control createDialogArea(Composite parent)
      {
        Control control = super.createDialogArea(parent);
        control.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        return control;
      }
    };

    display.asyncExec(new Runnable()
    {
      public void run()
      {
        final Shell backgroundShell = backgroundDialog.getShell();
        backgroundShell.setMaximized(true);

        final T element = create(backgroundShell);
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            preprocess(element);
            AccessUtil.busyWait(2000, new Runnable()
            {
              public void run()
              {
                while (!isReady(element))
                {
                  try
                  {
                    Thread.sleep(1000);
                  }
                  catch (InterruptedException ex)
                  {
                    // Ignore.
                  }
                }
              }
            });

            postProcess(element);
            AccessUtil.busyWait(1000);

            image.set(capture(element));

            getShell(element).close();
            backgroundShell.close();
          }
        });

        open(element);
      }
    });

    backgroundDialog.open();

    return image.get();
  }

  protected Control getControl(T element)
  {
    return getShell(element);
  }

  protected Image capture(T element)
  {
    return AccessUtil.captureControl(getControl(element));
  }

  protected void preprocess(T element)
  {
  }

  protected boolean isReady(T element)
  {
    return true;
  }

  protected void postProcess(T element)
  {
  }

  public static abstract class Window<W extends org.eclipse.jface.window.Window> extends Capture<W>
  {
    @Override
    protected Shell getShell(W window)
    {
      return window.getShell();
    }

    @Override
    protected void open(W window)
    {
      window.open();
    }
  }

  public static abstract class Dialog<D extends org.eclipse.swt.widgets.Dialog> extends Capture<D>
  {
    @Override
    protected Shell getShell(D dialog)
    {
      return dialog.getParent();
    }

    @Override
    protected void open(D dialog)
    {
      ReflectUtil.invokeMethod(ReflectUtil.getMethod(dialog.getClass(), "open"), dialog);
    }
  }
}

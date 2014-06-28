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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public final class UIUtil
{
  private UIUtil()
  {
  }

  public static Display getDisplay()
  {
    Display display = Display.getCurrent();
    if (display == null)
    {
      try
      {
        display = PlatformUI.getWorkbench().getDisplay();
      }
      catch (Throwable ignore)
      {
        //$FALL-THROUGH$
      }
    }

    if (display == null)
    {
      display = Display.getDefault();
    }

    if (display == null)
    {
      display = new Display();
    }

    return display;
  }

  public static Shell getShell()
  {
    final Shell[] shell = { null };

    final Display display = getDisplay();
    display.syncExec(new Runnable()
    {
      public void run()
      {
        shell[0] = display.getActiveShell();

        if (shell[0] == null)
        {
          try
          {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window == null)
            {
              IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
              if (windows.length != 0)
              {
                window = windows[0];
              }
            }

            if (window != null)
            {
              shell[0] = window.getShell();
            }
          }
          catch (Throwable ignore)
          {
            //$FALL-THROUGH$
          }
        }

        Shell[] shells = display.getShells();
        if (shells.length > 0)
        {
          shell[0] = shells[0];
        }
      }
    });

    return shell[0];
  }

  public static GridData applyGridData(Control control)
  {
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    control.setLayoutData(data);
    return data;
  }

  public static GridData grabVertical(GridData data)
  {
    data.grabExcessVerticalSpace = true;
    data.verticalAlignment = GridData.FILL;
    return data;
  }

  public static void runInProgressDialog(Shell shell, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell)
    {
      @Override
      protected Point getInitialSize()
      {
        Point calculatedSize = super.getInitialSize();
        if (calculatedSize.x < 800)
        {
          calculatedSize.x = 800;
        }

        return calculatedSize;
      }
    };

    dialog.run(true, true, runnable);
  }

  public static void handleException(Throwable ex)
  {
    UIPlugin.INSTANCE.log(ex);
    ErrorDialog.open(ex);
  }

  public static void exec(Display display, boolean async, Runnable runnable)
  {
    if (async)
    {
      asyncExec(display, runnable);
    }
    else
    {
      if (Display.getCurrent() == display)
      {
        runnable.run();
      }
      else
      {
        display.syncExec(runnable);
      }
    }
  }

  public static void asyncExec(final Runnable runnable)
  {
    final Display display = getDisplay();
    if (display != null)
    {
      asyncExec(display, runnable);
    }
  }

  public static void asyncExec(final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.asyncExec(new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static IDialogSettings getOrCreateSection(IDialogSettings settings, String sectionName)
  {
    IDialogSettings section = settings.getSection(sectionName);
    if (section == null)
    {
      section = settings.addNewSection(sectionName);
    }

    return section;
  }
}

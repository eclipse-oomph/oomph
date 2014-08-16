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

import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class UICallback extends UserCallback
{
  private final Shell shell;

  private final String shellText;

  public UICallback(Shell shell, String shellText)
  {
    this.shell = shell;
    this.shellText = shellText;
  }

  public Shell getShell()
  {
    return shell;
  }

  public String getShellText()
  {
    return shellText;
  }

  @Override
  public void execInUI(boolean async, Runnable runnable)
  {
    UIUtil.exec(shell.getDisplay(), async, runnable);
  }

  @Override
  public boolean runInProgressDialog(boolean async, final IRunnable runnable)
  {
    final IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        runnable.run(monitor);
      }
    };

    UIUtil.exec(shell.getDisplay(), async, new Runnable()
    {
      public void run()
      {
        try
        {
          UIUtil.runInProgressDialog(shell, runnableWithProgress);
        }
        catch (InvocationTargetException ex)
        {
          UIUtil.handleException(ex.getCause());
        }
        catch (InterruptedException ex)
        {
          // Ignore.
        }
      }
    });

    return false;
  }

  @Override
  public void information(boolean async, final String message)
  {
    UIUtil.exec(shell.getDisplay(), async, new Runnable()
    {
      public void run()
      {
        MessageDialog.openInformation(null, shellText, message);
      }
    });
  }

  @Override
  public boolean question(final String message)
  {
    final AtomicBoolean result = new AtomicBoolean();
    shell.getDisplay().syncExec(new Runnable()
    {
      public void run()
      {
        boolean confirmation = MessageDialog.openQuestion(null, shellText, message);
        result.set(confirmation);
      }
    });

    return result.get();
  }
}

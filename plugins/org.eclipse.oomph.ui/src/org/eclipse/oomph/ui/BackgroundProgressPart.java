/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class BackgroundProgressPart extends ProgressMonitorPart
{
  private final Updater updater = new Updater();

  public BackgroundProgressPart(Composite parent, Layout layout)
  {
    super(parent, layout);
  }

  public BackgroundProgressPart(Composite parent, Layout layout, int progressIndicatorHeight)
  {
    super(parent, layout, progressIndicatorHeight);
  }

  public BackgroundProgressPart(Composite parent, Layout layout, boolean createStopButton)
  {
    super(parent, layout, createStopButton);
  }

  @Override
  public void beginTask(final String name, final int totalWork)
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.beginTask(name, totalWork);
      }
    });
  }

  @Override
  public void setTaskName(final String name)
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.setTaskName(name);
      }
    });
  }

  @Override
  public void subTask(final String name)
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.subTask(name);
      }
    });
  }

  @Override
  public void done()
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.done();
      }
    });
  }

  @Override
  public void internalWorked(final double work)
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.internalWorked(work);
      }
    });
  }

  @Override
  public void worked(final int work)
  {
    updater.update(new Runnable()
    {
      public void run()
      {
        BackgroundProgressPart.super.worked(work);
      }
    });
  }

  /**
   * @author Ed Merks
   */
  private static class Updater implements Runnable
  {
    private List<Runnable> runnables;

    public void run()
    {
      List<Runnable> currentRunnables;
      synchronized (this)
      {
        currentRunnables = runnables;
        runnables = null;
      }

      for (Runnable runnable : currentRunnables)
      {
        runnable.run();
      }
    }

    public void update(Runnable runnable)
    {
      boolean dispatch;
      synchronized (this)
      {
        if (runnables == null)
        {
          runnables = new ArrayList<Runnable>();
          dispatch = true;
        }
        else
        {
          dispatch = false;
        }

        runnables.add(runnable);
      }

      if (dispatch)
      {
        UIUtil.asyncExec(this);
      }
    }
  }
}

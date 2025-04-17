/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * @author Eike Stepper
 */
public class SynchronizerJob extends Job
{
  private final Synchronizer synchronizer;

  private final boolean deferLocal;

  private Throwable exception;

  private Synchronization synchronization;

  private FinishHandler finishHandler;

  private boolean finished;

  private boolean awaitCanceled;

  public SynchronizerJob(Synchronizer synchronizer, boolean deferLocal)
  {
    super(Messages.SynchronizerJob_job);
    this.synchronizer = synchronizer;
    this.deferLocal = deferLocal;
  }

  public Synchronizer getSynchronizer()
  {
    return synchronizer;
  }

  public boolean isDeferLocal()
  {
    return deferLocal;
  }

  public FinishHandler getFinishHandler()
  {
    return finishHandler;
  }

  public void setFinishHandler(FinishHandler finishHandler)
  {
    this.finishHandler = finishHandler;
  }

  public Throwable getException()
  {
    return exception;
  }

  public Synchronization getSynchronization()
  {
    return synchronization;
  }

  public Synchronization awaitSynchronization(IProgressMonitor monitor)
  {
    while (!finished && exception == null)
    {
      if (monitor.isCanceled())
      {
        awaitCanceled = true;
        break;
      }

      synchronized (this)
      {
        try
        {
          wait(100); // Give the user a chance to cancel the monitor.
        }
        catch (InterruptedException ex)
        {
          return null;
        }
      }
    }

    return synchronization;
  }

  public void stopSynchronization()
  {
    cancel();
    disposeSynchronization(synchronization);
  }

  @Override
  protected IStatus run(IProgressMonitor monitor)
  {
    try
    {
      Synchronization result = synchronizer.synchronize(deferLocal);

      if (Boolean.getBoolean("org.eclipse.oomph.setup.sync.SynchronizerJob.testDelay")) //$NON-NLS-1$
      {
        for (int i = 0; i < 150 && !monitor.isCanceled() && !awaitCanceled; i++)
        {
          Thread.sleep(100);
        }
      }

      if (monitor.isCanceled())
      {
        disposeSynchronization(result);
        throw new OperationCanceledException();
      }

      handleFinish(null);

      synchronized (this)
      {
        synchronization = result;
        finished = true;
        notifyAll();
      }
    }
    catch (Throwable t)
    {
      handleFinish(t);

      synchronized (this)
      {
        exception = t;
        notifyAll();
      }
    }

    return Status.OK_STATUS;
  }

  private void handleFinish(Throwable ex)
  {
    if (finishHandler != null)
    {
      try
      {
        finishHandler.handleFinish(ex);
      }
      catch (Throwable t)
      {
        SetupSyncPlugin.INSTANCE.log(t);
      }
    }
  }

  private static void disposeSynchronization(Synchronization synchronization)
  {
    if (synchronization != null)
    {
      try
      {
        synchronization.dispose();
      }
      catch (Throwable t)
      {
        SetupSyncPlugin.INSTANCE.log(t);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface FinishHandler
  {
    public void handleFinish(Throwable ex) throws Exception;
  }
}

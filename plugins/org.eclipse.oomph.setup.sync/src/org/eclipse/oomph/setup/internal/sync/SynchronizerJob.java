/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Eike Stepper
 */
public class SynchronizerJob extends Job
{
  private final CountDownLatch done = new CountDownLatch(1);

  private final Synchronizer synchronizer;

  private final boolean deferLocal;

  private SynchronizerService service;

  private Synchronization synchronization;

  private Throwable exception;

  public SynchronizerJob(Synchronizer synchronizer, boolean deferLocal)
  {
    super("Synchronizing");
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

  public SynchronizerService getService()
  {
    return service;
  }

  public void setService(SynchronizerService service)
  {
    this.service = service;
  }

  public Synchronization getSynchronization()
  {
    return synchronization;
  }

  public Synchronization awaitSynchronization(long timeout, IProgressMonitor monitor)
  {
    long now = System.currentTimeMillis();
    long end = now + timeout;

    while (!monitor.isCanceled())
    {
      try
      {
        if (done.await(Math.max(timeout, 100), TimeUnit.MILLISECONDS))
        {
          return synchronization;
        }

        now = System.currentTimeMillis();
        timeout = end - now;
        if (timeout <= 0)
        {
          break;
        }
      }
      catch (InterruptedException ex)
      {
        break;
      }
    }

    return null;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor)
  {
    try
    {
      synchronization = synchronizer.synchronize(deferLocal);
      done.countDown();
    }
    catch (Throwable ex)
    {
      exception = ex;
    }

    return Status.OK_STATUS;
  }

  public Throwable getException()
  {
    return exception;
  }
}

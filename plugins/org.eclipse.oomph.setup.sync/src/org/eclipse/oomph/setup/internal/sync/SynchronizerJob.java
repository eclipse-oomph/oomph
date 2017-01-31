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
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.spi.ICredentialsProvider;

/**
 * @author Eike Stepper
 */
public class SynchronizerJob extends Job
{
  private final Synchronizer synchronizer;

  private final boolean deferLocal;

  private IStorageService service;

  private ICredentialsProvider credentialsProvider;

  private Throwable exception;

  private Synchronization synchronization;

  private FinishHandler finishHandler;

  private boolean finished;

  private boolean awaitCanceled;

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

  public IStorageService getService()
  {
    return service;
  }

  public void setService(IStorageService service)
  {
    this.service = service;
  }

  public ICredentialsProvider getCredentialsProvider()
  {
    return credentialsProvider;
  }

  public void setCredentialsProvider(ICredentialsProvider credentialsProvider)
  {
    this.credentialsProvider = credentialsProvider;
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
    RemoteDataProvider remoteDataProvider = (RemoteDataProvider)synchronizer.getRemoteSnapshot().getDataProvider();
    IStorage storage = remoteDataProvider.getStorage();
    ICredentialsProvider oldCredentialsProvider = storage.getCredentialsProvider();
    if (credentialsProvider != null)
    {
      storage.setCredentialsProvider(credentialsProvider);
    }

    try
    {
      Synchronization result = synchronizer.synchronize(deferLocal);

      if (Boolean.getBoolean("org.eclipse.oomph.setup.sync.SynchronizerJob.testDelay"))
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
    finally
    {
      if (credentialsProvider != null)
      {
        storage.setCredentialsProvider(oldCredentialsProvider);
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

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

import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.util.ConcurrentArray;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class Synchronizer
{
  private static final LocalLock LOCAL_LOCK = new LocalLock();

  private final ConcurrentArray<SnychronizerListener> listeners = new ConcurrentArray<SnychronizerListener>()
  {
    @Override
    protected SnychronizerListener[] newArray(int length)
    {
      return new SnychronizerListener[length];
    }
  };

  private final Snapshot localSnapshot;

  private final Snapshot remoteSnapshot;

  private Synchronization synchronization;

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
  {
    localSnapshot = new Snapshot(localDataProvider, syncFolder);
    remoteSnapshot = new Snapshot(remoteDataProvider, syncFolder);
  }

  public Snapshot getLocalSnapshot()
  {
    return localSnapshot;
  }

  public Snapshot getRemoteSnapshot()
  {
    return remoteSnapshot;
  }

  public synchronized Synchronization synchronize() throws IOException, SnychronizerException
  {
    if (synchronization != null)
    {
      return null;
    }

    LOCAL_LOCK.acquire();
    synchronization = createSynchronization();
    return synchronization;
  }

  protected Synchronization createSynchronization() throws IOException
  {
    return new Synchronization(this);
  }

  public void addListener(SnychronizerListener listener)
  {
    listeners.add(listener);
  }

  public void removeListener(SnychronizerListener listener)
  {
    listeners.remove(listener);
  }

  protected void syncStarted()
  {
    for (SnychronizerListener listener : listeners.get())
    {
      try
      {
        listener.syncStarted(synchronization);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void actionResolved(SyncAction action, String id)
  {
    for (SnychronizerListener listener : listeners.get())
    {
      try
      {
        listener.actionResolved(synchronization, action, id);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void commitStarted()
  {
    for (SnychronizerListener listener : listeners.get())
    {
      try
      {
        listener.commitStarted(synchronization);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void commitFinished(Throwable exception)
  {
    for (SnychronizerListener listener : listeners.get())
    {
      try
      {
        listener.commitFinished(synchronization, exception);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected synchronized void releaseLock()
  {
    if (synchronization != null)
    {
      Synchronization oldSynchronization = synchronization;
      synchronization = null;

      LOCAL_LOCK.release();

      for (SnychronizerListener listener : listeners.get())
      {
        try
        {
          listener.lockReleased(oldSynchronization);
        }
        catch (Throwable ex)
        {
          SetupSyncPlugin.INSTANCE.log(ex);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocalLock
  {
    public LocalLock()
    {
    }

    public void acquire()
    {
    }

    public void release()
    {
    }
  }
}

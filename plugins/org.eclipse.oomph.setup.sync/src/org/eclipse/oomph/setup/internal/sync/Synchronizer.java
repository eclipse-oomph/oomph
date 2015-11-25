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

import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.util.ConcurrentArray;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class Synchronizer
{
  private static final LocalLock LOCAL_LOCK = new LocalLock();

  private final ConcurrentArray<SynchronizerListener> listeners = new ConcurrentArray<SynchronizerListener>()
  {
    @Override
    protected SynchronizerListener[] newArray(int length)
    {
      return new SynchronizerListener[length];
    }
  };

  private final Snapshot localSnapshot;

  private final Snapshot remoteSnapshot;

  private Synchronization synchronization;

  public Synchronizer(Snapshot localSnapshot, Snapshot remoteSnapshot)
  {
    this.localSnapshot = localSnapshot;
    this.remoteSnapshot = remoteSnapshot;
  }

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
  {
    this(new Snapshot(localDataProvider, syncFolder), new Snapshot(remoteDataProvider, syncFolder));
  }

  public Snapshot getLocalSnapshot()
  {
    return localSnapshot;
  }

  public Snapshot getRemoteSnapshot()
  {
    return remoteSnapshot;
  }

  public Synchronization synchronize() throws IOException, SynchronizerException
  {
    return synchronize(false);
  }

  public synchronized Synchronization synchronize(boolean deferLocal) throws IOException, SynchronizerException
  {
    if (synchronization != null)
    {
      return null;
    }

    // TODO Implement the user lock.
    LOCAL_LOCK.acquire();

    synchronization = createSynchronization(deferLocal);
    return synchronization;
  }

  protected Synchronization createSynchronization(boolean deferLocal) throws IOException
  {
    return new Synchronization(this, deferLocal);
  }

  public void copyFilesTo(File target)
  {
    if (localSnapshot != null)
    {
      localSnapshot.copyFilesTo(target);
    }

    if (remoteSnapshot != null)
    {
      remoteSnapshot.copyFilesTo(target);
    }
  }

  public void copyFilesFrom(File source)
  {
    if (localSnapshot != null)
    {
      localSnapshot.copyFilesFrom(source);
    }

    if (remoteSnapshot != null)
    {
      remoteSnapshot.copyFilesFrom(source);
    }
  }

  public void addListener(SynchronizerListener listener)
  {
    listeners.add(listener);
  }

  public void removeListener(SynchronizerListener listener)
  {
    listeners.remove(listener);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + localSnapshot + " <--> " + remoteSnapshot + "]";
  }

  protected void syncStarted()
  {
    for (SynchronizerListener listener : listeners.get())
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

  protected void workingCopyCreated(WorkingCopy workingCopy)
  {
    for (SynchronizerListener listener : listeners.get())
    {
      try
      {
        listener.workingCopyCreated(synchronization, workingCopy);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void actionsComputed(Map<String, SyncAction> actions)
  {
    for (SynchronizerListener listener : listeners.get())
    {
      try
      {
        listener.actionsComputed(synchronization, actions);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void actionResolved(SyncAction action, String id)
  {
    for (SynchronizerListener listener : listeners.get())
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
    for (SynchronizerListener listener : listeners.get())
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
    for (SynchronizerListener listener : listeners.get())
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

      for (SynchronizerListener listener : listeners.get())
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

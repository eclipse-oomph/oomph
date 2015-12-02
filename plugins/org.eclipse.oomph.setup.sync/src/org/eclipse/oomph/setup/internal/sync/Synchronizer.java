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

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.internal.sync.DataProvider.Location;
import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.util.LockFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Eike Stepper
 */
public class Synchronizer
{
  private final List<SynchronizerListener> listeners = new CopyOnWriteArrayList<SynchronizerListener>();

  private final Snapshot localSnapshot;

  private final Snapshot remoteSnapshot;

  private LockFile lockFile;

  private Synchronization synchronization;

  public Synchronizer(Snapshot localSnapshot, Snapshot remoteSnapshot)
  {
    this.localSnapshot = localSnapshot;
    this.remoteSnapshot = remoteSnapshot;
  }

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
  {
    this(localDataProvider, remoteDataProvider, syncFolder, Snapshot.DEFAULT_INCREMENTAL);
  }

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder, boolean incremental)
  {
    this(new Snapshot(localDataProvider, syncFolder, incremental), new Snapshot(remoteDataProvider, syncFolder, incremental));
  }

  public Snapshot getLocalSnapshot()
  {
    return localSnapshot;
  }

  public Snapshot getRemoteSnapshot()
  {
    return remoteSnapshot;
  }

  public LockFile getLockFile()
  {
    return lockFile;
  }

  public void setLockFile(LockFile lockFile)
  {
    this.lockFile = lockFile;
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

    if (lockFile != null)
    {
      try
      {
        lockFile.lock();
      }
      catch (Exception ex)
      {
        throw new LockException(lockFile.getFile());
      }
    }

    try
    {
      synchronization = createSynchronization(deferLocal);
    }
    catch (IOException ex)
    {
      doReleaseLock();
      throw ex;
    }
    catch (RuntimeException ex)
    {
      doReleaseLock();
      throw ex;
    }
    catch (Error ex)
    {
      doReleaseLock();
      throw ex;
    }

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

  public SynchronizerListener[] getListeners()
  {
    return listeners.toArray(new SynchronizerListener[listeners.size()]);
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
    for (SynchronizerListener listener : listeners)
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
    for (SynchronizerListener listener : listeners)
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

  protected void tasksCollected(Synchronization synchronization, Location location, Map<String, SetupTask> oldTasks, Map<String, SetupTask> newTasks)
  {
    for (SynchronizerListener listener : listeners)
    {
      try
      {
        listener.tasksCollected(synchronization, location, oldTasks, newTasks);
      }
      catch (Throwable ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }
  }

  protected void actionsComputed(Map<String, SyncAction> actions)
  {
    for (SynchronizerListener listener : listeners)
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
    for (SynchronizerListener listener : listeners)
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
    for (SynchronizerListener listener : listeners)
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
    for (SynchronizerListener listener : listeners)
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

      doReleaseLock();

      for (SynchronizerListener listener : listeners)
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

  private void doReleaseLock()
  {
    if (lockFile != null)
    {
      lockFile.unlock();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class LockException extends SynchronizerException
  {
    private static final long serialVersionUID = 1L;

    public LockException(File file)
    {
      super("Another synchronization process has locked " + file);
    }
  }
}

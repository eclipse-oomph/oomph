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

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.ConflictException;
import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.util.ConcurrentArray;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Synchronizer
{
  private static final EClass USER_TYPE = SetupPackage.Literals.USER;

  private static final EClass REMOTE_DATA_TYPE = SyncPackage.Literals.REMOTE_DATA;

  private static final LocalLock LOCAL_LOCK = new LocalLock();

  private final Snapshot localSnapshot;

  private final Snapshot remoteSnapshot;

  private final ResourceSet resourceSet = SyncUtil.createResourceSet();

  private final Set<String> ids = new HashSet<String>();

  private int lastID;

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
  {
    localSnapshot = new Snapshot(localDataProvider, syncFolder);
    remoteSnapshot = new Snapshot(remoteDataProvider, syncFolder);
  }

  public Synchronization synchronize() throws IOException
  {
    LOCAL_LOCK.acquire();

    WorkingCopy localWorkingCopy = localSnapshot.createWorkingCopy();
    WorkingCopy remoteWorkingCopy = remoteSnapshot.createWorkingCopy();

    // Compute remote deltas first to make sure that new local tasks don't pick remotely existing IDs.
    Map<String, SyncDelta> remoteDeltas = computeDeltas(remoteWorkingCopy, REMOTE_DATA_TYPE);
    Map<String, SyncDelta> localDeltas = computeDeltas(localWorkingCopy, USER_TYPE);

    Map<String, SyncAction> actions = computeSyncActions(localDeltas, remoteDeltas);
    return new Synchronization(localWorkingCopy, remoteWorkingCopy, actions);
  }

  private Map<String, SyncDelta> computeDeltas(WorkingCopy workingCopy, EClass eClass)
  {
    Snapshot snapshot = workingCopy.getSnapshot();

    File oldFile = snapshot.getOldFile();
    if (!oldFile.exists())
    {
      SyncUtil.inititalizeFile(oldFile, eClass, resourceSet);
    }

    File tmpFile = workingCopy.getTmpFile();
    if (!tmpFile.exists())
    {
      File newFile = snapshot.getNewFile();
      if (!newFile.exists())
      {
        SyncUtil.inititalizeFile(tmpFile, eClass, resourceSet);
      }
      else
      {
        IOUtil.copyFile(newFile, tmpFile);
      }
    }

    SetupTaskContainer oldData = loadObject(oldFile, eClass);
    SetupTaskContainer newData = loadObject(tmpFile, eClass);

    return compareTasks(oldData, newData);
  }

  private Map<String, SyncAction> computeSyncActions(Map<String, SyncDelta> localDeltas, Map<String, SyncDelta> remoteDeltas)
  {
    Map<String, SyncAction> actions = new HashMap<String, SyncAction>();

    for (Map.Entry<String, SyncDelta> localEntry : localDeltas.entrySet())
    {
      String id = localEntry.getKey();

      SyncDelta localDelta = localEntry.getValue();
      SyncDelta remoteDelta = remoteDeltas.remove(id);

      SyncAction action = compareDeltas(localDelta, remoteDelta);
      if (action != null)
      {
        actions.put(id, action);
      }
    }

    for (SyncDelta remoteDelta : remoteDeltas.values())
    {
      String id = remoteDelta.getID();
      SyncAction action = compareDeltas(null, remoteDelta);
      actions.put(id, action);
    }

    return actions;
  }

  private SyncAction compareDeltas(SyncDelta localDelta, SyncDelta remoteDelta)
  {
    SyncDeltaType localDeltaType = localDelta == null ? SyncDeltaType.UNCHANGED : localDelta.getType();
    SyncDeltaType remoteDeltaType = remoteDelta == null ? SyncDeltaType.UNCHANGED : remoteDelta.getType();

    SyncActionType actionType = compareDeltaTypes(localDeltaType, remoteDeltaType);
    if (actionType == SyncActionType.NONE)
    {
      PreferenceTask localPreference = (PreferenceTask)localDelta.getNewTask();
      PreferenceTask remotePreference = (PreferenceTask)remoteDelta.getNewTask();

      // The comparison has returned a Changed/Changed delta conflict, so compare the values.
      if (ObjectUtil.equals(localPreference.getValue(), remotePreference.getValue()))
      {
        // Ignore unchanged values.
        actionType = null;
      }
      else
      {
        actionType = SyncActionType.CONFLICT;
      }
    }

    if (actionType != null)
    {
      return SyncFactory.eINSTANCE.createSyncAction(localDelta, remoteDelta, actionType);
    }

    return null;
  }

  private SyncActionType compareDeltaTypes(SyncDeltaType localDeltaType, SyncDeltaType remoteDeltaType)
  {
    switch (localDeltaType)
    {
      case UNCHANGED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return null;

          case CHANGED:
            return SyncActionType.SET_REMOTE;

          case REMOVED:
            return SyncActionType.REMOVE;
        }
        break;

      case CHANGED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return SyncActionType.SET_LOCAL;

          case CHANGED:
            // Will be changed to CONFLICT or null by the caller.
            return SyncActionType.NONE;

          case REMOVED:
            return SyncActionType.CONFLICT;
        }
        break;

      case REMOVED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return SyncActionType.REMOVE;

          case CHANGED:
            return SyncActionType.CONFLICT;

          case REMOVED:
            return null;
        }
        break;
    }

    throw new IllegalArgumentException();
  }

  private Map<String, SyncDelta> compareTasks(SetupTaskContainer oldTaskContainer, SetupTaskContainer newTaskContainer)
  {
    Map<String, SyncDelta> deltas = new HashMap<String, SyncDelta>();

    Map<String, SetupTask> oldTasks = collectTasks(oldTaskContainer);
    Map<String, SetupTask> newTasks = collectTasks(newTaskContainer);

    for (Map.Entry<String, SetupTask> oldEntry : oldTasks.entrySet())
    {
      String id = oldEntry.getKey();

      SetupTask oldTask = oldEntry.getValue();
      SetupTask newTask = newTasks.remove(id);

      SyncDelta delta = compareTasks(id, oldTask, newTask);
      if (delta != null)
      {
        deltas.put(id, delta);
      }
    }

    for (Map.Entry<String, SetupTask> newEntry : newTasks.entrySet())
    {
      String id = newEntry.getKey();
      SetupTask newTask = newEntry.getValue();

      SyncDelta delta = compareTasks(id, null, newTask);
      deltas.put(id, delta);
    }

    return deltas;
  }

  private SyncDelta compareTasks(String id, SetupTask oldTask, SetupTask newTask)
  {
    if (oldTask == null)
    {
      if (newTask == null)
      {
        return null;
      }

      return SyncFactory.eINSTANCE.createSyncDelta(id, oldTask, newTask, SyncDeltaType.CHANGED);
    }

    if (newTask == null)
    {
      return SyncFactory.eINSTANCE.createSyncDelta(id, oldTask, newTask, SyncDeltaType.REMOVED);
    }

    PreferenceTask oldPreference = (PreferenceTask)oldTask;
    PreferenceTask newPreference = (PreferenceTask)newTask;

    if (!ObjectUtil.equals(oldPreference.getKey(), newPreference.getKey()))
    {
      // Ignore changed keys.
      return null;
    }

    if (ObjectUtil.equals(oldPreference.getValue(), newPreference.getValue()))
    {
      // Ignore unchanged values.
      return null;
    }

    return SyncFactory.eINSTANCE.createSyncDelta(id, oldPreference, newPreference, SyncDeltaType.CHANGED);
  }

  private Map<String, SetupTask> collectTasks(SetupTaskContainer taskContainer)
  {
    Map<String, SetupTask> tasks = new HashMap<String, SetupTask>();
    collectTasks(taskContainer.getSetupTasks(), tasks);
    return tasks;
  }

  private void collectTasks(EList<SetupTask> tasks, Map<String, SetupTask> result)
  {
    for (SetupTask task : tasks)
    {
      String id = task.getID();
      if (!StringUtil.isEmpty(id))
      {
        // Make sure existing IDs are not reused.
        ids.add(id);
      }

      if (isSychronizable(task))
      {
        if (StringUtil.isEmpty(id))
        {
          id = createID();
          task.setID(id);
        }

        if (result.put(id, task) != null)
        {
          throw new DuplicateIDException(id);
        }
      }
      else if (task instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)task;
        collectTasks(compoundTask.getSetupTasks(), result);
      }
    }
  }

  private boolean isSychronizable(SetupTask task)
  {
    return task instanceof PreferenceTask;
  }

  private String createID()
  {
    for (int i = lastID + 1; i < Integer.MAX_VALUE; i++)
    {
      String id = "sync" + i;
      if (ids.add(id))
      {
        lastID = i;
        return id;
      }
    }

    throw new IllegalStateException("Too many IDs");
  }

  private <T extends EObject> T loadObject(File file, EClass eClass)
  {
    URI uri = URI.createFileURI(file.getAbsolutePath());
    Resource resource = resourceSet.getResource(uri, true);
    return BaseUtil.getObjectByType(resource.getContents(), eClass);
  }

  /**
   * @author Eike Stepper
   */
  public static class SnychronizerException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;

    public SnychronizerException()
    {
    }

    public SnychronizerException(String message, Throwable cause)
    {
      super(message, cause);
    }

    public SnychronizerException(String message)
    {
      super(message);
    }

    public SnychronizerException(Throwable cause)
    {
      super(cause);
    }

  }

  /**
   * @author Eike Stepper
   */
  public static class DuplicateIDException extends SnychronizerException
  {
    private static final long serialVersionUID = 1L;

    public DuplicateIDException(String id)
    {
      super("Duplicate ID: " + id);
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

  /**
   * @author Eike Stepper
   */
  public final class Synchronization
  {
    private final ConcurrentArray<CommitListener> listeners = new ConcurrentArray<CommitListener>()
    {
      @Override
      protected CommitListener[] newArray(int length)
      {
        return new CommitListener[length];
      }
    };

    private final WorkingCopy localWorkingCopy;

    private final WorkingCopy remoteWorkingCopy;

    private final Map<String, SyncAction> actions;

    private boolean committed;

    private boolean disposed;

    private Synchronization(WorkingCopy localWorkingCopy, WorkingCopy remoteWorkingCopy, Map<String, SyncAction> actions)
    {
      this.localWorkingCopy = localWorkingCopy;
      this.remoteWorkingCopy = remoteWorkingCopy;
      this.actions = actions;
    }

    public Synchronizer getSynchronizer()
    {
      return Synchronizer.this;
    }

    public void addCommitListener(CommitListener listener)
    {
      listeners.add(listener);
    }

    public void removeCommitListener(CommitListener listener)
    {
      listeners.remove(listener);
    }

    public Map<String, SyncAction> getActions()
    {
      return actions;
    }

    public void commit() throws IOException, ConflictException
    {
      if (!committed && !disposed)
      {
        committed = true;

        try
        {
          applyActions(remoteWorkingCopy, REMOTE_DATA_TYPE);
          remoteWorkingCopy.commit();

          applyActions(localWorkingCopy, USER_TYPE);
          localWorkingCopy.commit();

          notifyCommitListeners(null);
        }
        catch (IOException ex)
        {
          notifyCommitListeners(ex);
          throw ex;
        }
        catch (RuntimeException ex)
        {
          notifyCommitListeners(ex);
          throw ex;
        }
        catch (Error ex)
        {
          notifyCommitListeners(ex);
          throw ex;
        }
      }
    }

    public void dispose()
    {
      if (!disposed)
      {
        disposed = true;
        LOCAL_LOCK.release();

        try
        {
          localWorkingCopy.dispose();
        }
        catch (Throwable ex)
        {
          SetupSyncPlugin.INSTANCE.log(ex);
        }

        try
        {
          remoteWorkingCopy.dispose();
        }
        catch (Throwable ex)
        {
          SetupSyncPlugin.INSTANCE.log(ex);
        }
      }
    }

    private void applyActions(WorkingCopy workingCopy, EClass eClass)
    {
      File file = workingCopy.getTmpFile();

      SetupTaskContainer taskContainer = loadObject(file, eClass);
      Map<String, SetupTask> tasks = collectTasks(taskContainer);

      for (Map.Entry<String, SyncAction> entry : actions.entrySet())
      {
        String id = entry.getKey();
        SyncAction action = entry.getValue();
        SyncActionType type = action.getEffectiveType();

        switch (type)
        {
          case NONE:
            // Do nothing.
            break;

          case SET_LOCAL:
            applySetAction(taskContainer, tasks, id, action.getLocalDelta());
            break;

          case SET_REMOTE:
            applySetAction(taskContainer, tasks, id, action.getRemoteDelta());
            break;

          case REMOVE:
            applyRemoveAction(taskContainer, tasks, action.getLocalDelta());
            applyRemoveAction(taskContainer, tasks, action.getRemoteDelta());
            break;

          default:
            break;
        }
      }

      BaseUtil.saveEObject(taskContainer);
    }

    private void applySetAction(SetupTaskContainer taskContainer, Map<String, SetupTask> tasks, String id, SyncDelta delta)
    {
      if (delta != null)
      {
        SetupTask newTask = delta.getNewTask();
        if (newTask != null)
        {
          newTask = EcoreUtil.copy(newTask);
          newTask.setID(id);
          newTask.getRestrictions().clear();
          newTask.getPredecessors().clear();
          newTask.getSuccessors().clear();

          SetupTask oldTask = tasks.get(id);
          if (oldTask != null)
          {
            EcoreUtil.replace(oldTask, newTask);
          }
          else
          {
            taskContainer.getSetupTasks().add(newTask);
          }
        }
      }
    }

    private void applyRemoveAction(SetupTaskContainer taskContainer, Map<String, SetupTask> tasks, SyncDelta delta)
    {
      if (delta != null)
      {
        SetupTask oldTask = delta.getOldTask();
        if (oldTask != null)
        {
          EcoreUtil.remove(oldTask);
        }
      }
    }

    private void notifyCommitListeners(Throwable exception)
    {
      for (CommitListener commitListener : listeners.get())
      {
        try
        {
          commitListener.synchronizationCommitted(this, exception);
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
  public interface CommitListener
  {
    public void synchronizationCommitted(Synchronization synchronization, Throwable t);
  }
}

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

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.ConflictException;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class Synchronizer
{
  private final DataProvider localDataProvider;

  private final DataProvider remoteDataProvider;

  private final File syncFolder;

  private final ResourceSet resourceSet;

  public Synchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
  {
    this.localDataProvider = localDataProvider;
    this.remoteDataProvider = remoteDataProvider;
    this.syncFolder = syncFolder;

    resourceSet = SetupCoreUtil.createResourceSet();
    Resource.Factory factory = new BaseResourceFactoryImpl();

    Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
    extensionToFactoryMap.put("xml", factory);
    extensionToFactoryMap.put("tmp", factory);
  }

  public Synchronization synchronize() throws IOException
  {
    LocalLock lock = acquireLocalLock();

    try
    {
      Snapshot localSnapshot = acquireLocalData();
      Snapshot remoteSnapshot = acquireRemoteData();

      Map<String, SyncDelta> localDeltas = computeLocalDeltas(localSnapshot);
      Map<String, SyncDelta> remoteDeltas = computeRemoteDeltas(remoteSnapshot);

      Map<String, SyncAction> actions = computeSyncActions(localDeltas, remoteDeltas);

      return new Synchronization(localSnapshot, remoteSnapshot, actions);
    }
    finally
    {
      lock.release();
    }
  }

  private LocalLock acquireLocalLock()
  {
    return new LocalLock();
  }

  private Snapshot acquireLocalData() throws IOException
  {
    return new Snapshot(localDataProvider, syncFolder);
  }

  private Snapshot acquireRemoteData() throws IOException
  {
    return new Snapshot(remoteDataProvider, syncFolder);
  }

  private Map<String, SyncDelta> computeLocalDeltas(Snapshot localSnapshot)
  {
    File oldFile = localSnapshot.getOldFile();
    File newFile = localSnapshot.getNewFile();

    RemoteData oldData = loadObject(oldFile, SetupPackage.Literals.USER);
    RemoteData newData = loadObject(newFile, SetupPackage.Literals.USER);

    return compareTasks(oldData, newData);
  }

  private Map<String, SyncDelta> computeRemoteDeltas(Snapshot remoteSnapshot) throws IOException
  {
    File oldFile = remoteSnapshot.getOldFile();
    File newFile = remoteSnapshot.getNewFile();

    RemoteData oldData = loadObject(oldFile, SyncPackage.Literals.REMOTE_DATA);
    RemoteData newData = loadObject(newFile, SyncPackage.Literals.REMOTE_DATA);

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

  private <T extends EObject> T loadObject(File file, EClassifier classifier)
  {
    URI uri = URI.createFileURI(file.getAbsolutePath());
    Resource resource = BaseUtil.loadResourceSafely(resourceSet, uri);
    return BaseUtil.getObjectByType(resource.getContents(), classifier);
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

      SyncDelta delta = compareTasks(oldTask, newTask);
      if (delta != null)
      {
        deltas.put(id, delta);
      }
    }

    for (SetupTask newTask : newTasks.values())
    {
      String id = newTask.getID();
      SyncDelta delta = compareTasks(null, newTask);
      deltas.put(id, delta);
    }

    return deltas;
  }

  private SyncDelta compareTasks(SetupTask oldTask, SetupTask newTask)
  {
    if (oldTask == null)
    {
      if (newTask == null)
      {
        return null;
      }

      return SyncFactory.eINSTANCE.createSyncDelta(oldTask, newTask, SyncDeltaType.CHANGED);
    }

    if (newTask == null)
    {
      return SyncFactory.eINSTANCE.createSyncDelta(oldTask, newTask, SyncDeltaType.REMOVED);
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

    return SyncFactory.eINSTANCE.createSyncDelta(oldPreference, newPreference, SyncDeltaType.CHANGED);
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
      if (task.getRestrictions().isEmpty())
      {
        if (task instanceof PreferenceTask)
        {
          PreferenceTask preferenceTask = (PreferenceTask)task;
          String id = preferenceTask.getID();

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

    public void release()
    {
    }
  }

  /**
   * @author Eike Stepper
   */
  public final class Synchronization
  {
    private final Snapshot localSnapshot;

    private final Snapshot remoteSnapshot;

    private final Map<String, SyncAction> actions;

    private boolean done;

    public Synchronization(Snapshot localSnapshot, Snapshot remoteSnapshot, Map<String, SyncAction> actions)
    {
      this.localSnapshot = localSnapshot;
      this.remoteSnapshot = remoteSnapshot;
      this.actions = actions;
    }

    public Map<String, SyncAction> getActions()
    {
      return actions;
    }

    public boolean isDone()
    {
      return done;
    }

    public void commit() throws IOException, ConflictException
    {
      if (!done)
      {
        try
        {
          applyActions(remoteSnapshot, SyncPackage.Literals.REMOTE_DATA);
          remoteSnapshot.commit();

          applyActions(localSnapshot, SetupPackage.Literals.USER);
          localSnapshot.commit();
        }
        finally
        {
          cleanup();
        }
      }
    }

    private void applyActions(Snapshot snapshot, EClassifier classifier)
    {
      File file = snapshot.getNewFile();
      SetupTaskContainer taskContainer = loadObject(file, classifier);
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
    }

    private void applySetAction(SetupTaskContainer taskContainer, Map<String, SetupTask> tasks, String id, SyncDelta delta)
    {
      if (delta != null)
      {
        SetupTask newTask = delta.getNewTask();
        if (newTask != null)
        {
          newTask = EcoreUtil.copy(newTask);
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

    private void cleanup()
    {
      if (!done)
      {
        localSnapshot.dispose();
        remoteSnapshot.dispose();
        done = true;
      }
    }
  }
}

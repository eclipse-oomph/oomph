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
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
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
public class Synchronization
{
  public static final EClass USER_TYPE = SetupPackage.Literals.USER;

  public static final EClass REMOTE_DATA_TYPE = SyncPackage.Literals.REMOTE_DATA;

  private final ResourceSet resourceSet = SyncUtil.createResourceSet();

  private final Set<String> ids = new HashSet<String>();

  private final Map<String, String> preferenceIDs = new HashMap<String, String>();

  private final Synchronizer synchronizer;

  private final WorkingCopy remoteWorkingCopy;

  private final EMap<String, SyncPolicy> remotePolicies;

  private final Map<String, SyncDelta> remoteDeltas;

  private WorkingCopy localWorkingCopy;

  private Map<String, SyncAction> actions;

  private Map<String, SyncAction> unresolvedActions;

  private boolean committed;

  private boolean disposed;

  private int lastID;

  public Synchronization(Synchronizer synchronizer, boolean deferLocal) throws IOException
  {
    this.synchronizer = synchronizer;
    synchronizer.syncStarted();

    remoteWorkingCopy = createRemoteWorkingCopy();
    synchronizer.workingCopyCreated(remoteWorkingCopy);

    remotePolicies = getPolicies(remoteWorkingCopy);

    // Compute remote deltas first to make sure that new local tasks don't pick remotely existing IDs.
    remoteDeltas = computeRemoteDeltas(remoteWorkingCopy);

    if (!deferLocal)
    {
      synchronizeLocal();
    }
  }

  public Synchronizer getSynchronizer()
  {
    return synchronizer;
  }

  public EMap<String, SyncPolicy> getRemotePolicies()
  {
    return remotePolicies;
  }

  public Map<String, SyncAction> synchronizeLocal() throws IOException
  {
    if (localWorkingCopy != null)
    {
      localWorkingCopy.dispose();
    }

    // Compute local deltas.
    localWorkingCopy = createLocalWorkingCopy();
    synchronizer.workingCopyCreated(localWorkingCopy);

    Map<String, SyncDelta> localDeltas = computeLocalDeltas(localWorkingCopy);

    // Compute sync actions.
    actions = computeSyncActions(localDeltas, remoteDeltas);
    synchronizer.actionsComputed(actions);

    return actions;
  }

  private WorkingCopy createRemoteWorkingCopy() throws IOException
  {
    Snapshot snapshot = synchronizer.getRemoteSnapshot();
    return createWorkingCopy(snapshot, REMOTE_DATA_TYPE);
  }

  private WorkingCopy createLocalWorkingCopy() throws IOException
  {
    Snapshot snapshot = synchronizer.getLocalSnapshot();
    return createWorkingCopy(snapshot, USER_TYPE);
  }

  private WorkingCopy createWorkingCopy(Snapshot snapshot, EClass eClass) throws IOException
  {
    WorkingCopy workingCopy = snapshot.createWorkingCopy();

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

    return workingCopy;
  }

  private EMap<String, SyncPolicy> getPolicies(WorkingCopy remoteWorkingCopy)
  {
    File file = remoteWorkingCopy.getTmpFile();
    RemoteData remoteData = loadObject(file, REMOTE_DATA_TYPE);
    return remoteData.getPolicies();
  }

  private boolean isIncluded(String id)
  {
    return SyncPolicy.EXCLUDE != remotePolicies.get(id);
  }

  private Map<String, SyncDelta> computeRemoteDeltas(WorkingCopy remoteWorkingCopy)
  {
    return computeDeltas(remoteWorkingCopy, REMOTE_DATA_TYPE);
  }

  private Map<String, SyncDelta> computeLocalDeltas(WorkingCopy localWorkingCopy)
  {
    return computeDeltas(localWorkingCopy, USER_TYPE);
  }

  private Map<String, SyncDelta> computeDeltas(WorkingCopy workingCopy, EClass eClass)
  {
    Snapshot snapshot = workingCopy.getSnapshot();

    File oldFile = snapshot.getOldFile();
    File tmpFile = workingCopy.getTmpFile();

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

    for (Map.Entry<String, SyncAction> entry : actions.entrySet())
    {
      String id = entry.getKey();
      SyncAction action = entry.getValue();
      new ActionAdapter(action, id);
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
      if (isIncluded(id))
      {
        SetupTask oldTask = oldEntry.getValue();
        SetupTask newTask = newTasks.remove(id);

        SyncDelta delta = compareTasks(id, oldTask, newTask);
        if (delta != null)
        {
          deltas.put(id, delta);
        }
      }
    }

    for (Map.Entry<String, SetupTask> newEntry : newTasks.entrySet())
    {
      String id = newEntry.getKey();
      if (isIncluded(id))
      {
        SetupTask newTask = newEntry.getValue();

        SyncDelta delta = compareTasks(id, null, newTask);
        deltas.put(id, delta);
      }
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
      String id = rememberID(task);

      if (isSychronizable(task))
      {
        if (StringUtil.isEmpty(id))
        {
          id = getPreferenceID(task);

          if (StringUtil.isEmpty(id))
          {
            id = createID();
          }
          else
          {
            ids.add(id);
          }

          task.setID(id);
          rememberPreferenceID(task);
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

  private String rememberID(SetupTask task)
  {
    String id = task.getID();
    if (!StringUtil.isEmpty(id))
    {
      // Make sure existing IDs are not reused.
      ids.add(id);

      rememberPreferenceID(task);
    }

    return id;
  }

  private void rememberPreferenceID(SetupTask task)
  {
    String id = task.getID();
    if (!StringUtil.isEmpty(id))
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        String key = preferenceTask.getKey();

        if (!StringUtil.isEmpty(key))
        {
          preferenceIDs.put(key, id);
        }
      }
    }
  }

  private String getPreferenceID(SetupTask task)
  {
    if (task instanceof PreferenceTask)
    {
      PreferenceTask preferenceTask = (PreferenceTask)task;
      String key = preferenceTask.getKey();

      if (!StringUtil.isEmpty(key))
      {
        return preferenceIDs.get(key);
      }
    }

    return null;
  }

  private <T extends EObject> T loadObject(File file, EClass eClass)
  {
    URI uri = URI.createFileURI(file.getAbsolutePath());
    Resource resource = resourceSet.getResource(uri, true);
    return BaseUtil.getObjectByType(resource.getContents(), eClass);
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

  public String getID(SyncAction action)
  {
    String id = action.getID();
    if (id != null)
    {
      return id;
    }

    ActionAdapter adapter = (ActionAdapter)EcoreUtil.getAdapter(action.eAdapters(), ActionAdapter.class);
    if (adapter != null)
    {
      return adapter.getID();
    }

    return null;
  }

  public Map<String, SyncAction> getActions()
  {
    return actions;
  }

  public Map<String, SyncAction> getUnresolvedActions()
  {
    if (unresolvedActions == null)
    {
      unresolvedActions = new HashMap<String, SyncAction>();

      for (Map.Entry<String, SyncAction> entry : actions.entrySet())
      {
        SyncAction action = entry.getValue();

        if (action.getEffectiveType() == SyncActionType.CONFLICT)
        {
          String id = entry.getKey();
          unresolvedActions.put(id, action);
        }
      }
    }

    return unresolvedActions;
  }

  public Synchronization resolve(String id, SyncActionType resolvedType)
  {
    SyncAction action = actions.get(id);
    if (action != null)
    {
      action.setResolvedType(resolvedType);
    }

    return this;
  }

  public void commit() throws IOException, NotCurrentException
  {
    if (!committed && !disposed)
    {
      committed = true;
      doCommit();
    }
  }

  private void doCommit() throws IOException, NotCurrentException
  {
    synchronizer.commitStarted();

    try
    {
      applyActions(remoteWorkingCopy, REMOTE_DATA_TYPE);
      remoteWorkingCopy.commit();

      applyActions(localWorkingCopy, USER_TYPE);
      localWorkingCopy.commit();

      synchronizer.commitFinished(null);
    }
    catch (IOException ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
    catch (RuntimeException ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
    catch (Error ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
    finally
    {
      doDispose();
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
        case CONFLICT:
          throw new ConflictException(action);

        case SET_LOCAL:
          include(id);
          applySetAction(taskContainer, tasks, id, action.getLocalDelta());
          break;

        case SET_REMOTE:
          include(id);
          applySetAction(taskContainer, tasks, id, action.getRemoteDelta());
          break;

        case REMOVE:
          include(id);
          applyRemoveAction(taskContainer, tasks, action.getLocalDelta());
          applyRemoveAction(taskContainer, tasks, action.getRemoteDelta());
          break;

        case EXCLUDE:
          exclude(id);
          applyRemoveAction(taskContainer, tasks, action.getRemoteDelta());
          break;

        default:
          // Do nothing.
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
      String id = delta.getID();

      SetupTask oldTask = tasks.get(id);
      if (oldTask != null)
      {
        EcoreUtil.remove(oldTask);
      }
    }
  }

  private void include(String id)
  {
    remotePolicies.put(id, SyncPolicy.INCLUDE);
  }

  private void exclude(String id)
  {
    remotePolicies.put(id, SyncPolicy.EXCLUDE);
  }

  public void dispose()
  {
    if (!disposed)
    {
      doDispose();
    }
  }

  private void doDispose()
  {
    disposed = true;

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

    try
    {
      synchronizer.releaseLock();
    }
    catch (Throwable ex)
    {
      SetupSyncPlugin.INSTANCE.log(ex);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ActionAdapter extends AdapterImpl
  {
    private final String id;

    public ActionAdapter(SyncAction action, String id)
    {
      this.id = id;
      action.eAdapters().add(this);
    }

    public String getID()
    {
      return id;
    }

    @Override
    public void notifyChanged(Notification msg)
    {
      if (msg.getFeature() == SyncPackage.Literals.SYNC_ACTION__RESOLVED_TYPE && !msg.isTouch())
      {
        unresolvedActions = null;

        SyncAction action = (SyncAction)getTarget();
        synchronizer.actionResolved(action, id);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class DuplicateIDException extends SynchronizerException
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
  public static class ConflictException extends SynchronizerException
  {
    private static final long serialVersionUID = 1L;

    public ConflictException(SyncAction action)
    {
      super("Conflict: " + action);
    }
  }
}

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
package org.eclipse.oomph.setup.sync.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.sync.DataProvider;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerAdapter;
import org.eclipse.oomph.setup.internal.sync.SynchronizerException;
import org.eclipse.oomph.setup.internal.sync.SynchronizerListener;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNull;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class TestWorkstation
{
  private static final boolean REUSE_USER_HOMES = true;

  private static TestWorkstation lastWorkstation;

  private final ResourceSet resourceSet = SyncUtil.createResourceSet();

  private final Map<Integer, TestWorkstation> workstations;

  private final int id;

  private final File userHome;

  private final File userSetup;

  private final TestSynchronizer synchronizer;

  private File remoteFile;

  private User user;

  public TestWorkstation(Map<Integer, TestWorkstation> workstations, int id) throws Exception
  {
    this.id = id;
    this.workstations = workstations;

    userHome = createUserHome();
    userSetup = new File(userHome, "user.setup");

    DataProvider localDataProvider = new LocalDataProvider(userSetup);
    DataProvider remoteDataProvider = TestServer.getRemoteDataProvider();
    synchronizer = new TestSynchronizer(localDataProvider, remoteDataProvider, userHome);

    log("Create workstation " + userHome);
  }

  public File getUserHome()
  {
    return userHome;
  }

  public User getUser()
  {
    if (user == null)
    {
      URI uri = URI.createFileURI(userSetup.getAbsolutePath());

      if (resourceSet.getURIConverter().exists(uri, null))
      {
        user = loadObject(uri, SetupPackage.Literals.USER);
      }
      else
      {
        user = SetupFactory.eINSTANCE.createUser();
        user.setName("Test User");

        Resource resource = resourceSet.createResource(uri);
        resource.getContents().add(user);
      }
    }

    return user;
  }

  public List<PreferenceTask> getPreferenceTasks()
  {
    List<PreferenceTask> result = new ArrayList<PreferenceTask>();
    collectPreferenceTasks(getUser().getSetupTasks(), result);
    return result;
  }

  public String get(String key)
  {
    return getPreference(getUser().getSetupTasks(), key);
  }

  public TestWorkstation set(String key, String value)
  {
    log("Set " + key + " = " + value);
    EList<SetupTask> tasks = getUser().getSetupTasks();
    for (SetupTask task : tasks)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        if (key.equals(preferenceTask.getKey()))
        {
          preferenceTask.setValue(value);
          return this;
        }
      }
    }

    PreferenceTask preferenceTask = SetupFactory.eINSTANCE.createPreferenceTask();
    preferenceTask.setKey(key);
    preferenceTask.setValue(value);
    tasks.add(preferenceTask);
    return this;
  }

  public TestWorkstation remove(String key)
  {
    log("Remove " + key);
    for (SetupTask task : getUser().getSetupTasks())
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        if (key.equals(preferenceTask.getKey()))
        {
          EcoreUtil.remove(preferenceTask);
          return this;
        }
      }
    }

    return this;
  }

  public TestWorkstation save()
  {
    if (user != null)
    {
      log("Save");
      BaseUtil.saveEObject(user);
    }

    return this;
  }

  public TestSynchronization synchronize() throws Exception
  {
    return synchronizer.synchronize();
  }

  public TestWorkstation commit() throws Exception
  {
    return synchronize().commitAnd();
  }

  public TestSynchronization commitFail(FailureHandler handler) throws Exception
  {
    return synchronize().commitFail(handler);
  }

  public TestWorkstation assertCount(int count)
  {
    assertThat(getPreferenceTasks().size(), CoreMatchers.is(count));
    return this;
  }

  public TestWorkstation assertSet(String key, String value)
  {
    assertThat(get(key), CoreMatchers.is(value));
    return this;
  }

  public TestWorkstation assertRemoved(String key)
  {
    assertThat(get(key), IsNull.nullValue());
    return this;
  }

  public TestWorkstation assertIncluded(String key) throws IOException
  {
    Map<String, SyncPolicy> preferencePolicies = getPreferencePolicies();
    assertThat(preferencePolicies.get(key), CoreMatchers.is(SyncPolicy.INCLUDE));
    return this;
  }

  public TestWorkstation assertExcluded(String key) throws IOException
  {
    Map<String, SyncPolicy> preferencePolicies = getPreferencePolicies();
    assertThat(preferencePolicies.get(key), CoreMatchers.is(SyncPolicy.EXCLUDE));
    return this;
  }

  public TestWorkstation assertNoPolicy(String key) throws IOException
  {
    Map<String, SyncPolicy> preferencePolicies = getPreferencePolicies();
    assertThat(preferencePolicies.get(key), IsNull.nullValue());
    return this;
  }

  public Map<String, SyncPolicy> getPreferencePolicies() throws IOException
  {
    RemoteData remoteData = getRemoteData();
    EMap<String, SyncPolicy> policies = remoteData.getPolicies();

    Map<String, SyncPolicy> preferencePolicies = new HashMap<String, SyncPolicy>();

    // Make sure that even policies of remotely removed tasks can be found by preference key.
    for (TestWorkstation workstation : workstations.values())
    {
      if (workstation != this)
      {
        collectPreferencePolicies(preferencePolicies, policies, workstation.getPreferenceTasks());
      }
    }

    collectPreferencePolicies(preferencePolicies, policies, getPreferenceTasks());
    collectPreferencePolicies(preferencePolicies, policies, remoteData.getSetupTasks());
    return preferencePolicies;
  }

  private void collectPreferencePolicies(Map<String, SyncPolicy> preferencePolicies, EMap<String, SyncPolicy> policies, List<? extends SetupTask> tasks)
  {
    for (SetupTask task : tasks)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        String id = preferenceTask.getID();
        SyncPolicy policy = policies.get(id);
        if (policy != null)
        {
          String key = preferenceTask.getKey();
          preferencePolicies.put(key, policy);
        }
      }
    }
  }

  public RemoteData getRemoteData() throws IOException
  {
    if (remoteFile == null)
    {
      remoteFile = File.createTempFile("remote-data-", ".tmp");
      remoteFile.deleteOnExit();

      DataProvider dataProvider = synchronizer.getRemoteSnapshot().getDataProvider();
      dataProvider.update(remoteFile);
    }

    return loadObject(URI.createFileURI(remoteFile.getAbsolutePath()), Synchronization.REMOTE_DATA_TYPE);
  }

  public TestWorkstation log(Object msg)
  {
    if (this != lastWorkstation)
    {
      AbstractTest.log();
      AbstractTest.log(this + ":");
    }

    lastWorkstation = this;
    AbstractTest.log("   " + msg);
    return this;
  }

  @Override
  public String toString()
  {
    return "Workstation " + id;
  }

  private <T extends EObject> T loadObject(URI uri, EClassifier classifier)
  {
    Resource resource = resourceSet.getResource(uri, true);
    return BaseUtil.getObjectByType(resource.getContents(), classifier);
  }

  private File createUserHome()
  {
    if (TestWorkstation.REUSE_USER_HOMES)
    {
      File folder = new File(PropertiesUtil.getTmpDir(), "test-user-" + id);
      IOUtil.deleteBestEffort(folder, false);
      folder.mkdirs();
      return folder;
    }

    File folder = SynchronizerTests.createTempFolder();
    folder.deleteOnExit();
    return folder;
  }

  private String getPreference(EList<SetupTask> tasks, String key)
  {
    for (SetupTask task : tasks)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        if (key.equals(preferenceTask.getKey()))
        {
          return preferenceTask.getValue();
        }
      }
      else if (task instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)task;
        String value = getPreference(compoundTask.getSetupTasks(), key);
        if (value != null)
        {
          return value;
        }
      }
    }

    return null;
  }

  private void collectPreferenceTasks(EList<SetupTask> tasks, List<PreferenceTask> result)
  {
    for (SetupTask task : tasks)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        result.add(preferenceTask);
      }
      else if (task instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)task;
        collectPreferenceTasks(compoundTask.getSetupTasks(), result);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface FailureHandler
  {
    public void handleFailure(Exception ex) throws Exception;

    /**
     * @author Eike Stepper
     */
    public static class Expect implements FailureHandler
    {
      private final Class<? extends Exception> expectedException;

      public Expect(Class<? extends Exception> expectedException)
      {
        this.expectedException = expectedException;
      }

      public final void handleFailure(Exception ex) throws Exception
      {
        assertThat(ex, CoreMatchers.is(instanceOf(expectedException)));
        handleFailure();
      }

      protected void handleFailure() throws Exception
      {
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public final class TestSynchronizer extends Synchronizer
  {
    private final SynchronizerListener listener = new SynchronizerAdapter()
    {
      @Override
      public void syncStarted(Synchronization synchronization)
      {
        log("Synchronize");
      }

      @Override
      public void actionResolved(Synchronization synchronization, SyncAction action, String id)
      {
        log("Resolve " + id + " from " + action.getComputedType() + " to " + action.getResolvedType());
      }

      @Override
      public void commitStarted(Synchronization synchronization)
      {
        log("Commit");
      }

      @Override
      public void commitFinished(Synchronization synchronization, Throwable t)
      {
        if (t != null)
        {
          log(t.getMessage());
        }
        else
        {
          if (remoteFile != null)
          {
            remoteFile.delete();
            remoteFile = null;
          }
        }

        user = null;
        URI uri = URI.createFileURI(userSetup.getAbsolutePath());
        Resource resource = resourceSet.getResource(uri, false);
        if (resource != null)
        {
          resourceSet.getResources().remove(resource);
        }
      }
    };

    public TestSynchronizer(DataProvider localDataProvider, DataProvider remoteDataProvider, File syncFolder)
    {
      super(localDataProvider, remoteDataProvider, syncFolder);
      addListener(listener);
    }

    public TestWorkstation ws()
    {
      return TestWorkstation.this;
    }

    @Override
    protected TestSynchronization createSynchronization(boolean deferLocal) throws IOException
    {
      return new TestSynchronization(this, deferLocal);
    }

    @Override
    public synchronized TestSynchronization synchronize() throws IOException, SynchronizerException
    {
      return (TestSynchronization)super.synchronize();
    }
  }

  /**
   * @author Eike Stepper
   */
  public final class TestSynchronization extends Synchronization
  {
    public TestSynchronization(Synchronizer synchronizer, boolean deferLocal) throws IOException
    {
      super(synchronizer, deferLocal);
    }

    @Override
    public TestSynchronizer getSynchronizer()
    {
      return (TestSynchronizer)super.getSynchronizer();
    }

    public SyncAction getPreferenceAction(String key)
    {
      for (Map.Entry<String, SyncAction> entry : getActions().entrySet())
      {
        SyncAction action = entry.getValue();
        if (isPreferenceAction(key, action))
        {
          return action;
        }
      }

      return null;
    }

    private boolean isPreferenceAction(String key, SyncAction action)
    {
      if (action != null)
      {
        if (isPreferenceDelta(key, action.getLocalDelta()))
        {
          return true;
        }

        if (isPreferenceDelta(key, action.getRemoteDelta()))
        {
          return true;
        }
      }

      return false;
    }

    private boolean isPreferenceDelta(String key, SyncDelta delta)
    {
      if (delta != null)
      {
        if (isPreferenceTask(key, delta.getOldTask()))
        {
          return true;
        }

        if (isPreferenceTask(key, delta.getNewTask()))
        {
          return true;
        }
      }

      return false;
    }

    private boolean isPreferenceTask(String key, SetupTask task)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        if (key.equals(preferenceTask.getKey()))
        {
          return true;
        }
      }

      return false;
    }

    @Override
    public TestSynchronization resolve(String id, SyncActionType resolvedType)
    {
      super.resolve(id, resolvedType);
      return this;
    }

    public TestSynchronization resolvePreference(String key, SyncActionType resolvedType)
    {
      SyncAction action = getPreferenceAction(key);
      if (action != null)
      {
        String id = getID(action);
        if (id != null)
        {
          resolve(id, resolvedType);
        }
      }

      return this;
    }

    public TestSynchronization exclude(String key)
    {
      SyncAction action = getPreferenceAction(key);
      if (action != null)
      {
        String id = getID(action);
        if (id != null)
        {
          resolve(id, SyncActionType.EXCLUDE);
        }
      }

      return this;
    }

    public TestSynchronization pickLocal(String key)
    {
      SyncAction action = getPreferenceAction(key);
      if (action != null)
      {
        pick(action, action.getLocalDelta(), SyncActionType.SET_LOCAL, SyncActionType.REMOVE_LOCAL);
      }

      return this;
    }

    public TestSynchronization pickRemote(String key)
    {
      SyncAction action = getPreferenceAction(key);
      if (action != null)
      {
        pick(action, action.getRemoteDelta(), SyncActionType.SET_REMOTE, SyncActionType.REMOVE_REMOTE);
      }

      return this;
    }

    private void pick(SyncAction action, SyncDelta delta, SyncActionType setActionType, SyncActionType removeActionType)
    {
      String id = getID(action);
      if (id != null)
      {
        if (delta.getNewTask() == null)
        {
          resolve(id, removeActionType);
        }
        else
        {
          resolve(id, setActionType);
        }
      }
    }

    public TestWorkstation commitAnd() throws IOException, NotCurrentException
    {
      commit();
      return TestWorkstation.this;
    }

    public TestSynchronization commitFail(FailureHandler handler) throws Exception
    {
      try
      {
        commit();
      }
      catch (Exception ex)
      {
        handler.handleFailure(ex);
        return this;
      }

      Assert.fail("Commit should have failed");
      return this;
    }

    public TestSynchronization assertActions(int count)
    {
      assertThat(getActions().size(), CoreMatchers.is(count));
      return this;
    }

    public TestSynchronization assertUnresolved(int count)
    {
      assertThat(getUnresolvedActions().size(), CoreMatchers.is(count));
      return this;
    }

    public TestSynchronization assertComputed(String key, SyncActionType type)
    {
      assertThat(getPreferenceAction(key).getComputedType(), CoreMatchers.is(type));
      return this;
    }

    public TestSynchronization assertResolved(String key, SyncActionType type)
    {
      assertThat(getPreferenceAction(key).getResolvedType(), CoreMatchers.is(type));
      return this;
    }

    public TestSynchronization assertEffective(String key, SyncActionType type)
    {
      assertThat(getPreferenceAction(key).getEffectiveType(), CoreMatchers.is(type));
      return this;
    }
  }
}

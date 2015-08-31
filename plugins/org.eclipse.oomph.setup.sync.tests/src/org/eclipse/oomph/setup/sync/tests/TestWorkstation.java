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
import org.eclipse.oomph.setup.internal.sync.SnychronizerException;
import org.eclipse.oomph.setup.internal.sync.SnychronizerListener;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;
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

  private final int id;

  private final File userHome;

  private final File userSetup;

  private User user;

  private final TestSynchronizer synchronizer;

  public TestWorkstation(int id) throws Exception
  {
    this.id = id;
    userHome = createUserHome();
    userSetup = new File(userHome, "user.setup");
    log("Create workstation " + userHome);

    DataProvider localDataProvider = new LocalDataProvider(userSetup);
    DataProvider remoteDataProvider = TestServer.getRemoteDataProvider();
    synchronizer = new TestSynchronizer(localDataProvider, remoteDataProvider, userHome);
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
    if (user != null)
    {
      log("Remove " + key);
      for (SetupTask task : user.getSetupTasks())
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
    private final SnychronizerListener listener = new SnychronizerListener()
    {
      public void syncStarted(Synchronization synchronization)
      {
        log("Synchronize");
      }

      public void actionResolved(Synchronization synchronization, SyncAction action, String id)
      {
        log("Resolve " + id + " from " + action.getComputedType() + " to " + action.getResolvedType());
      }

      public void commitStarted(Synchronization synchronization)
      {
        log("Commit");
      }

      public void commitFinished(Synchronization synchronization, Throwable t)
      {
        if (t != null)
        {
          log(t.getMessage());
        }

        user = null;
        URI uri = URI.createFileURI(userSetup.getAbsolutePath());
        Resource resource = resourceSet.getResource(uri, false);
        if (resource != null)
        {
          resourceSet.getResources().remove(resource);
        }
      }

      public void lockReleased(Synchronization synchronization)
      {
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
    protected TestSynchronization createSynchronization() throws IOException
    {
      return new TestSynchronization(this);
    }

    @Override
    public synchronized TestSynchronization synchronize() throws IOException, SnychronizerException
    {
      return (TestSynchronization)super.synchronize();
    }
  }

  /**
   * @author Eike Stepper
   */
  public final class TestSynchronization extends Synchronization
  {
    public TestSynchronization(TestSynchronizer synchronizer) throws IOException
    {
      super(synchronizer);
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

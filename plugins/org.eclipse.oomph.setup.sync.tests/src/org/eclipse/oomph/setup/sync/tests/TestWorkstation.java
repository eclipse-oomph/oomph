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

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.sync.DataProvider;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.Synchronizer.CommitListener;
import org.eclipse.oomph.setup.internal.sync.Synchronizer.Synchronization;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class TestWorkstation
{
  private final int id;

  private final File userHome;

  private final File userSetup;

  private User user;

  static final boolean DEBUG = true;

  public TestWorkstation(int id)
  {
    this.id = id;
    userHome = createUserHome();
    userSetup = new File(userHome, "user.setup");
    System.out.println("Created " + this + ": " + userHome);
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

      if (SynchronizerTests.RESOURCE_SET.getURIConverter().exists(uri, null))
      {
        user = loadObject(uri, SetupPackage.Literals.USER);
      }
      else
      {
        user = SetupFactory.eINSTANCE.createUser();
        user.setName("Test User");

        Resource resource = SynchronizerTests.RESOURCE_SET.createResource(uri);
        resource.getContents().add(user);
      }
    }

    return user;
  }

  public void saveUser()
  {
    if (user != null)
    {
      BaseUtil.saveEObject(user);
    }
  }

  public List<PreferenceTask> getPreferenceTasks()
  {
    List<PreferenceTask> result = new ArrayList<PreferenceTask>();
    collectPreferenceTasks(getUser().getSetupTasks(), result);
    return result;
  }

  public String getPreference(String key)
  {
    return getPreference(getUser().getSetupTasks(), key);
  }

  public void setPreference(String key, String value)
  {
    EList<SetupTask> tasks = getUser().getSetupTasks();
    for (SetupTask task : tasks)
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        if (key.equals(preferenceTask.getKey()))
        {
          preferenceTask.setValue(value);
          return;
        }
      }
    }

    PreferenceTask preferenceTask = SetupFactory.eINSTANCE.createPreferenceTask();
    preferenceTask.setKey(key);
    preferenceTask.setValue(value);
    tasks.add(preferenceTask);
  }

  public void removePreference(String key)
  {
    if (user != null)
    {
      for (SetupTask task : user.getSetupTasks())
      {
        if (task instanceof PreferenceTask)
        {
          PreferenceTask preferenceTask = (PreferenceTask)task;
          if (key.equals(preferenceTask.getKey()))
          {
            EcoreUtil.remove(preferenceTask);
            return;
          }
        }
      }
    }
  }

  public Synchronization synchronize() throws Exception
  {
    DataProvider localDataProvider = new LocalDataProvider(userSetup);
    DataProvider remoteDataProvider = TestServer.getRemoteDataProvider();
    Synchronizer synchronizer = new Synchronizer(localDataProvider, remoteDataProvider, userHome);

    Synchronization synchronization = synchronizer.synchronize();
    synchronization.addCommitListener(new CommitListener()
    {
      public void synchronizationCommitted(Synchronization synchronization, Throwable t)
      {
        user = null;
        URI uri = URI.createFileURI(userSetup.getAbsolutePath());
        Resource resource = SynchronizerTests.RESOURCE_SET.getResource(uri, false);
        if (resource != null)
        {
          SynchronizerTests.RESOURCE_SET.getResources().remove(resource);
        }
      }
    });

    return synchronization;
  }

  @Override
  public String toString()
  {
    return "Workstation " + id;
  }

  private <T extends EObject> T loadObject(URI uri, EClassifier classifier)
  {
    Resource resource = SynchronizerTests.RESOURCE_SET.getResource(uri, true);
    return BaseUtil.getObjectByType(resource.getContents(), classifier);
  }

  private File createUserHome()
  {
    if (TestWorkstation.DEBUG)
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
}

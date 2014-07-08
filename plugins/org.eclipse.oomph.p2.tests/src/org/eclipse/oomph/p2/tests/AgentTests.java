/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.p2.internal.core.ProfileImpl;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public class AgentTests extends AbstractP2Test
{
  @Test
  public void testDefaultAgent() throws Exception
  {
    {
      AgentManager agentManager = P2Util.getAgentManager();
      assertThat(agentManager.getAgents().size(), is(1));

      Agent agent = agentManager.getAgents().iterator().next();
      assertThat(agent.getBundlePools().size(), is(1));
      assertThat(agent.getProfiles().size(), is(0));
    }

    AgentManagerImpl.instance = new AgentManagerImpl(getUserHome());

    {
      AgentManager agentManager = P2Util.getAgentManager();
      assertThat(agentManager.getAgents().size(), is(1));

      Agent agent = agentManager.getAgents().iterator().next();
      assertThat(agent.getBundlePools().size(), is(1));
      assertThat(agent.getProfiles().size(), is(0));
    }
  }

  @Test
  public void testCreateProfile() throws Exception
  {
    Agent agent = getAgent();

    for (int i = 1; i <= 10; i++)
    {
      String id = "profile" + i;
      Profile profile = agent.addProfile(id, "Test").create();
      assertThat(profile.getProfileId(), is(id));
      assertThat(profile.getType(), is("Test"));
      assertThat(agent.getProfiles().size(), is(i));
      assertThat(agent.getProfileRegistry().getProfiles().length, is(i));
    }

    // The rest of this test checks if profile delegates are loaded lazily
    agent = getFreshAgent();
    assertThat(agent.getProfiles().size(), is(10));

    for (int i = 1; i <= 10; i++)
    {
      String id = "profile" + i;
      ProfileImpl profile = (ProfileImpl)agent.getProfile(id);
      assertThat(profile.getProfileId(), is(id));
      assertThat(profile.getType(), is("Test"));
      assertThat(profile.getDelegate(false), nullValue());
    }

    ProfileImpl profileNEW = (ProfileImpl)agent.addProfile("profileNEW", "Test").create();
    assertThat(profileNEW.getDelegate(false), not(nullValue()));

    for (int i = 1; i <= 10; i++)
    {
      String id = "profile" + i;
      ProfileImpl profile = (ProfileImpl)agent.getProfile(id);
      assertThat(profile.getDelegate(false), nullValue());
    }
  }

  @Test
  public void testImmutableProfileProperties() throws Exception
  {
    Agent agent = getAgent();

    Profile profile = agent.addProfile("profile1", "Test").create();

    for (String key : Arrays.asList(Profile.PROP_INSTALL_FEATURES, Profile.PROP_INSTALL_FOLDER, Profile.PROP_CACHE, Profile.PROP_PROFILE_TYPE,
        Profile.PROP_PROFILE_DEFINITION))
    {
      try
      {
        commitProfileTransaction(profile.change().setProfileProperty(key, "some value"), false);
        Assert.fail("IllegalArgumentException expected");
      }
      catch (IllegalArgumentException expected)
      {
        // Success
      }
    }
  }

  @Test
  public void testChangeProfileProperties() throws Exception
  {
    Agent agent = getAgent();

    // Create
    Profile profile = agent.addProfile("profile1", "Test").set("test", "Test Value").create();
    IProfile delegate = ((ProfileImpl)profile).getDelegate(false);
    assertThat(profile.getProperties().size(), is(4)); // type, environments, nl, test
    assertThat(profile.getProperty("test"), is("Test Value"));

    // Change
    commitProfileTransaction(profile.change().setProfileProperty("test", "Changed Value"), true);
    assertThat(profile.getProperties().size(), is(4)); // type, environments, nl, test
    assertThat(profile.getProperty("test"), is("Changed Value"));

    Profile p = agent.getProfile("profile1");
    assertThat(p, is(profile));
    assertThat(((ProfileImpl)p).getDelegate(false), sameInstance(delegate));

    // Add
    commitProfileTransaction(profile.change().setProfileProperty("test2", "New Property"), true);
    assertThat(profile.getProperties().size(), is(5)); // type, environments, nl, test, test2
    assertThat(profile.getProperty("test2"), is("New Property"));

    // Remove
    commitProfileTransaction(profile.change().removeProfileProperty("test2"), true);
    assertThat(profile.getProperties().size(), is(4)); // type, environments, nl, test
    assertThat(profile.getProperty("test2"), nullValue());
  }

  @Test
  public void testDeleteProfile() throws Exception
  {
    Agent agent = getAgent();

    Profile profile = agent.addProfile("profile1", "Test").create();
    assertThat(agent.getProfile("profile1"), sameInstance(profile));
    assertThat(profile.getLocation().isDirectory(), is(true));

    profile.delete(true);
    assertThat(agent.getProfile("profile1"), nullValue());
    assertThat(profile.getLocation().exists(), is(false));
  }

  @Test
  public void testUseDeletedProfile() throws Exception
  {
    Agent agent = getAgent();

    Profile profile = agent.addProfile("profile1", "Test1").create();
    File location = profile.getLocation();

    boolean deleted = IOUtil.deleteBestEffort(location);
    assertThat(deleted, is(true));
    assertThat(location.exists(), is(false));

    profile = agent.getProfile("profile1");
    assertThat(profile, nullValue());
    assertThat(agent.getProfiles().size(), is(0));
    assertThat(agent.getProfileRegistry().getProfiles().length, is(0));

    profile = agent.addProfile("profile1", "Test2").create();
    assertThat(profile.getProfileId(), is("profile1"));
    assertThat(profile.getType(), is("Test2"));
    assertThat(agent.getProfiles().size(), is(1));
    assertThat(agent.getProfileRegistry().getProfiles().length, is(1));
    assertThat(location.isDirectory(), is(true));
  }

  @Test
  public void testInstallExceptions() throws Exception
  {
    Agent agent = getAgent();
    Profile profile = agent.addProfile("profile-app1", "Installation").create();

    // Install without repository
    ProfileTransaction transaction = profile.change();
    transaction.getProfileDefinition().getRequirements().add(P2Factory.eINSTANCE.createRequirement("com.jcraft.jsch"));

    try
    {
      commitProfileTransaction(transaction, false);
      Assert.fail("CoreException expected");
    }
    catch (CoreException expected)
    {
      // Success
    }
  }

  @Test
  public void testInstallAndUpdateFeature() throws Exception
  {
    Agent agent = getAgent();
    File installFolder = new File(getUserHome(), "app1");

    String oldVersion = "org.eclipse.net4j.util_4.2.0.v20130601-1611";
    String newVersion = "org.eclipse.net4j.util_4.2.1.v20140218-1709";

    ProfileCreator creator = agent.addProfile("profile-app1", "Installation");
    Profile profile = creator.setCacheFolder(installFolder).setInstallFolder(installFolder).setInstallFeatures(true).create();

    // Install
    ProfileTransaction transaction1 = profile.change();
    ProfileDefinition profileDefinition = transaction1.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("org.eclipse.net4j.util.feature.group"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(CDO_OLD.toURI().toString()));

    commitProfileTransaction(transaction1, true);
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(new File(installFolder, "artifacts.xml").isFile(), is(true));
    assertThat(new File(installFolder, "p2").exists(), is(false));

    File features = new File(installFolder, "features");
    assertThat(features.isDirectory(), is(true));
    assertThat(features.list().length, is(1));
    assertThat(new File(features, oldVersion).isDirectory(), is(true));

    // Update (replace old version)
    ProfileTransaction transaction2 = profile.change();
    transaction2.getProfileDefinition().getRepositories().add(P2Factory.eINSTANCE.createRepository(CDO_NEW.toURI().toString()));

    commitProfileTransaction(transaction2, true);
    assertThat(features.list().length, is(1));
    assertThat(new File(features, oldVersion).exists(), is(false));
    assertThat(new File(features, newVersion).isDirectory(), is(true));

    // No update (keep new version)
    ProfileTransaction transaction3 = profile.change().setRemoveExistingInstallableUnits(true);
    commitProfileTransaction(transaction3, true);
    assertThat(features.list().length, is(1));
    assertThat(new File(features, newVersion).isDirectory(), is(true));
  }

  @Test
  public void testInstallAndUpdateSingleton() throws Exception
  {
    Agent agent = getAgent();
    File installFolder = new File(getUserHome(), "app1");

    String oldVersion = "org.eclipse.net4j.util_3.3.0.v20130601-1611.jar";
    String newVersion = "org.eclipse.net4j.util_3.3.1.v20140218-1709.jar";

    ProfileCreator creator = agent.addProfile("profile-app1", "Installation");
    Profile profile = creator.setCacheFolder(installFolder).setInstallFolder(installFolder).create();

    // Install
    ProfileTransaction transaction1 = profile.change();
    ProfileDefinition profileDefinition = transaction1.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("org.eclipse.net4j.util"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(CDO_OLD.toURI().toString()));

    commitProfileTransaction(transaction1, true);
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(new File(installFolder, "artifacts.xml").isFile(), is(true));
    assertThat(new File(installFolder, "p2").exists(), is(false));
    assertThat(new File(installFolder, "features").isDirectory(), is(false));

    File plugins = new File(installFolder, "plugins");
    assertThat(plugins.isDirectory(), is(true));
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, oldVersion).isFile(), is(true));

    // Update (replace old version)
    ProfileTransaction transaction2 = profile.change();
    transaction2.getProfileDefinition().getRepositories().add(P2Factory.eINSTANCE.createRepository(CDO_NEW.toURI().toString()));

    commitProfileTransaction(transaction2, true);
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, oldVersion).isFile(), is(false));
    assertThat(new File(plugins, newVersion).isFile(), is(true));

    // No update (keep new version)
    ProfileTransaction transaction3 = profile.change().setRemoveExistingInstallableUnits(true);
    commitProfileTransaction(transaction3, true);
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, newVersion).isFile(), is(true));
  }

  @Test
  public void testInstallAndUpdateNonSingleton() throws Exception
  {
    Agent agent = getAgent();
    File installFolder = new File(getUserHome(), "app1");

    String oldVersion = "com.jcraft.jsch_0.1.46.v201205102330.jar";
    String newVersion = "com.jcraft.jsch_0.1.50.v201310081430.jar";

    ProfileCreator creator = agent.addProfile("profile-app1", "Installation");
    Profile profile = creator.setCacheFolder(installFolder).setInstallFolder(installFolder).create();

    // Install
    ProfileTransaction transaction1 = profile.change();
    ProfileDefinition profileDefinition = transaction1.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("com.jcraft.jsch"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(PLATFORM_OLD.toURI().toString()));

    commitProfileTransaction(transaction1, true);
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(new File(installFolder, "artifacts.xml").isFile(), is(true));
    assertThat(new File(installFolder, "p2").exists(), is(false));
    assertThat(new File(installFolder, "features").isDirectory(), is(false));

    File plugins = new File(installFolder, "plugins");
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, oldVersion).isFile(), is(true));

    // Update (add new version)
    ProfileTransaction transaction2 = profile.change();
    transaction2.getProfileDefinition().getRepositories().add(P2Factory.eINSTANCE.createRepository(PLATFORM_NEW.toURI().toString()));

    commitProfileTransaction(transaction2, true);
    assertThat(plugins.list().length, is(2));
    assertThat(new File(plugins, oldVersion).isFile(), is(true));
    assertThat(new File(plugins, newVersion).isFile(), is(true));

    // Update (remove old version)
    ProfileTransaction transaction3 = profile.change().setRemoveExistingInstallableUnits(true);
    commitProfileTransaction(transaction3, true);
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, newVersion).isFile(), is(true));
  }

  @Test
  public void testInstallWithoutChange() throws Exception
  {
    Agent agent = getAgent();
    File installFolder = new File(getUserHome(), "app1");

    String oldVersion = "com.jcraft.jsch_0.1.46.v201205102330.jar";

    ProfileCreator creator = agent.addProfile("profile-app1", "Installation");
    Profile profile = creator.setCacheFolder(installFolder).setInstallFolder(installFolder).create();

    // Install
    ProfileTransaction transaction1 = profile.change();
    ProfileDefinition profileDefinition = transaction1.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("com.jcraft.jsch"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(PLATFORM_OLD.toURI().toString()));

    commitProfileTransaction(transaction1, true);
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(new File(installFolder, "artifacts.xml").isFile(), is(true));
    assertThat(new File(installFolder, "p2").exists(), is(false));
    assertThat(new File(installFolder, "features").isDirectory(), is(false));

    File plugins = new File(installFolder, "plugins");
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, oldVersion).isFile(), is(true));

    // Update (no change)
    ProfileTransaction transaction2 = profile.change();
    commitProfileTransaction(transaction2, false);
    assertThat(plugins.list().length, is(1));
    assertThat(new File(plugins, oldVersion).isFile(), is(true));
  }

  @Test
  public void testDetectProfileDefinitionCreation() throws Exception
  {
    Agent agent = getAgent();
    IProfileRegistry profileRegistry = agent.getProfileRegistry();
    profileRegistry.addProfile("profile1");
    agent.refreshProfiles();

    Profile profile = agent.getProfile("profile1");
    ProfileTransaction transaction = profile.change();
    Resolution resolution = transaction.resolve(null, LOGGER);
    assertThat(resolution, nullValue());
  }

  // @Test
  public void testInstallStandalone() throws Exception
  {
    Agent agent = getAgent();
    File installFolder = new File(getUserHome(), "app1");

    ProfileCreator creator = agent.addProfile("profile-app1", "Installation");
    Profile profile = creator.setCacheFolder(installFolder).setInstallFolder(installFolder).setInstallFeatures(true).create();

    ProfileTransaction transaction = profile.change();
    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("org.eclipse.sdk.ide"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(PLATFORM_NEW.toURI().toString()));

    commitProfileTransaction(transaction, true);
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(new File(installFolder, "p2").exists(), is(false));

    File features = new File(installFolder, "features");
    assertThat(features.isDirectory(), is(true));
    assertThat(features.list().length, is(26)); // TODO This might fail on different platforms

    File plugins = new File(installFolder, "plugins");
    assertThat(plugins.isDirectory(), is(true));
    assertThat(plugins.list().length, is(436)); // TODO This might fail on different platforms

    File artifacts = new File(installFolder, "artifacts.xml");
    assertThat(artifacts.isFile(), is(true));
  }
}

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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IEngine;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class AgentImpl extends AgentManagerElementImpl implements Agent
{
  public static final String ENGINE_PATH = "org.eclipse.equinox.p2.engine";

  private final AgentManagerImpl agentManager;

  private final File location;

  private final PersistentMap<BundlePool> bundlePoolMap;

  private final PersistentMap<Profile> profileMap;

  private IProvisioningAgent provisioningAgent;

  private LazyProfileRegistry profileRegistry;

  private IMetadataRepositoryManager metadataRepositoryManager;

  private IArtifactRepositoryManager artifactRepositoryManager;

  private IEngine engine;

  private IPlanner planner;

  public AgentImpl(AgentManagerImpl agentManager, File location)
  {
    this.agentManager = agentManager;
    this.location = location;

    bundlePoolMap = new PersistentMap<BundlePool>(new File(location, "pools.info"))
    {
      @Override
      protected BundlePool createElement(String key, String extraInfo)
      {
        return new BundlePoolImpl(AgentImpl.this, new File(key));
      }

      @Override
      protected void initializeFirstTime()
      {
        IProfileRegistry profileRegistry = getProfileRegistry();
        for (IProfile delegate : profileRegistry.getProfiles())
        {
          String cache = delegate.getProperty(Profile.PROP_CACHE);
          if (cache != null)
          {
            addElement(cache, null);
          }
        }
      }
    };

    profileMap = new PersistentMap<Profile>(new File(location, "profiles.info"))
    {
      @Override
      protected Profile createElement(String profileID, String extraInfo)
      {
        List<String> tokens = StringUtil.explode(extraInfo, "|");
        String type = tokens.get(0);
        BundlePool bundlePool = getBundlePool(tokens.get(1));
        File installFolder = AgentImpl.getFile(tokens.get(2));
        File referencer = AgentImpl.getFile(tokens.get(3));

        return new ProfileImpl(AgentImpl.this, bundlePool, profileID, type, installFolder, referencer);
      }

      @Override
      protected void initializeFirstTime()
      {
        IProfileRegistry profileRegistry = getProfileRegistry();
        for (IProfile delegate : profileRegistry.getProfiles())
        {
          String key = delegate.getProfileId();
          String extraInfo = AgentImpl.getProfileExtraInfo(delegate);
          addElement(key, extraInfo);
        }
      }
    };

    bundlePoolMap.load();
    profileMap.load();
  }

  @Override
  public String getElementType()
  {
    return "agent";
  }

  public AgentManager getAgentManager()
  {
    return agentManager;
  }

  public File getLocation()
  {
    return location;
  }

  public boolean isValid()
  {
    return isValid(location);
  }

  public boolean isCurrent()
  {
    return agentManager.getCurrentAgent() == this;
  }

  public boolean isUsed()
  {
    for (BundlePool bundlePool : getBundlePools())
    {
      if (bundlePool.isUsed())
      {
        return true;
      }
    }

    for (Profile profile : getProfiles())
    {
      if (profile.isUsed())
      {
        return true;
      }
    }

    return false;
  }

  @Override
  protected void doDelete()
  {
    agentManager.deleteAgent(this);
  }

  public PersistentMap<BundlePool> getBundlePoolMap()
  {
    return bundlePoolMap;
  }

  public Set<File> getBundlePoolLocations()
  {
    return AgentManagerImpl.getLocations(bundlePoolMap.getElementKeys());
  }

  public Collection<BundlePool> getBundlePools()
  {
    return bundlePoolMap.getElements();
  }

  public BundlePool getBundlePool(String path)
  {
    return bundlePoolMap.getElement(path);
  }

  public BundlePool getBundlePool(File location)
  {
    return bundlePoolMap.getElement(location.getAbsolutePath());
  }

  public BundlePool addBundlePool(File location)
  {
    return bundlePoolMap.addElement(location.getAbsolutePath(), null);
  }

  public void deleteBundlePool(BundlePool bundlePool)
  {
    bundlePoolMap.removeElement(bundlePool.getLocation().getAbsolutePath());

    int xxx;
    // TODO Delete artifacts from disk
  }

  public synchronized LazyProfileRegistry getProfileRegistry()
  {
    getProvisioningAgent();
    return profileRegistry;
  }

  public PersistentMap<Profile> getProfileMap()
  {
    return profileMap;
  }

  public Set<String> getAllProfileIDs()
  {
    return profileMap.getElementKeys();
  }

  public Collection<Profile> getAllProfiles()
  {
    return profileMap.getElements();
  }

  public Set<String> getProfileIDs()
  {
    return getProfileIDs(null);
  }

  public Set<String> getProfileIDs(BundlePool bundlePool)
  {
    Set<String> ids = new HashSet<String>();
    for (Profile profile : getAllProfiles())
    {
      if (profile.getBundlePool() == bundlePool)
      {
        ids.add(profile.getProfileId());
      }
    }

    return ids;
  }

  public Collection<Profile> getProfiles()
  {
    return getProfiles(null);
  }

  public Collection<Profile> getProfiles(BundlePool bundlePool)
  {
    Collection<Profile> profiles = getAllProfiles();
    for (Iterator<Profile> it = profiles.iterator(); it.hasNext();)
    {
      Profile profile = it.next();
      if (profile.getBundlePool() != bundlePool)
      {
        it.remove();
      }
    }

    return profiles;
  }

  public Profile getCurrentProfile()
  {
    IProfile delegate = getProfileRegistry().getProfile(IProfileRegistry.SELF);
    if (delegate != null)
    {
      String profileID = delegate.getProfileId();
      return getProfile(profileID);
    }

    return null;
  }

  public Profile getProfile(String id)
  {
    Profile profile = profileMap.getElement(id);
    if (profile == null)
    {
      if (provisioningAgent == null)
      {
        // Trigger creation of the LazyProfileRegistry and, hence, refreshing of the profileMap
        getProvisioningAgent();

        // Then try again
        profile = profileMap.getElement(id);
      }
    }
    else
    {
      if (!profile.isValid())
      {
        profileMap.removeElement(id);
        getProfileRegistry().removeProfile(id);
        profile = null;
      }
    }

    return profile;
  }

  public ProfileCreator addProfile(String id, String type)
  {
    return new ProfileCreatorImpl(this, id, type)
    {
      private static final long serialVersionUID = 1L;

      @Override
      protected Profile doCreateProfile()
      {
        return AgentImpl.this.createProfile(this);
      }
    };
  }

  public Profile createProfile(ProfileCreator creator)
  {
    if (creator.getEnvironments() == null)
    {
      creator.addOS(Platform.getOS());
      creator.addWS(Platform.getWS());
      creator.addArch(Platform.getOSArch());
      creator.addLanguage(Platform.getNL());
    }

    try
    {
      String profileID = creator.getProfileID();

      IProfile delegate = getProfileRegistry().addProfile(profileID, creator);
      String extraInfo = getProfileExtraInfo(delegate);

      ProfileImpl profile = (ProfileImpl)profileMap.addElement(profileID, extraInfo);
      profile.setDelegate(delegate);
      return profile;
    }
    catch (ProvisionException ex)
    {
      throw new P2Exception(ex);
    }
  }

  public void deleteProfile(Profile profile)
  {
    String profileID = profile.getProfileId();
    profileMap.removeElement(profileID);
    getProfileRegistry().removeProfile(profileID);
  }

  public void initializeProvisioningAgent(IProvisioningAgent provisioningAgent)
  {
    if (this.provisioningAgent == null)
    {
      IAgentLocation agentLocation = (IAgentLocation)provisioningAgent.getService(IAgentLocation.SERVICE_NAME);
      File directory = LazyProfileRegistry.getDefaultRegistryDirectory(agentLocation);

      profileRegistry = new LazyProfileRegistry(provisioningAgent, directory);
      provisioningAgent.registerService(IProfileRegistry.SERVICE_NAME, profileRegistry);

      this.provisioningAgent = provisioningAgent;
    }
  }

  public synchronized IProvisioningAgent getProvisioningAgent()
  {
    if (provisioningAgent == null)
    {
      File location = getLocation();
      initializeProvisioningAgent(getOrCreateProvisioningAgent(location));
    }

    return provisioningAgent;
  }

  public synchronized IMetadataRepositoryManager getMetadataRepositoryManager()
  {
    if (metadataRepositoryManager == null)
    {
      metadataRepositoryManager = (IMetadataRepositoryManager)getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
      if (metadataRepositoryManager == null)
      {
        throw new IllegalStateException("Metadata respository manager could not be loaded");
      }
    }

    return metadataRepositoryManager;
  }

  public synchronized IArtifactRepositoryManager getArtifactRepositoryManager()
  {
    if (artifactRepositoryManager == null)
    {
      artifactRepositoryManager = (IArtifactRepositoryManager)getProvisioningAgent().getService(IArtifactRepositoryManager.SERVICE_NAME);
      if (artifactRepositoryManager == null)
      {
        throw new IllegalStateException("Artifact respository manager could not be loaded");
      }
    }

    return artifactRepositoryManager;
  }

  public synchronized IEngine getEngine()
  {
    if (engine == null)
    {
      engine = (IEngine)getProvisioningAgent().getService(IEngine.SERVICE_NAME);
      if (engine == null)
      {
        throw new IllegalStateException("Engine could not be loaded");
      }
    }

    return engine;
  }

  public synchronized IPlanner getPlanner()
  {
    if (planner == null)
    {
      planner = (IPlanner)getProvisioningAgent().getService(IPlanner.SERVICE_NAME);
      if (planner == null)
      {
        throw new IllegalStateException("Planner could not be loaded");
      }
    }

    return planner;
  }

  @Override
  public String toString()
  {
    return getLocation().getAbsolutePath();
  }

  private static File getFile(String path)
  {
    if (StringUtil.isEmpty(path))
    {
      return null;
    }

    return new File(path);
  }

  public static boolean isValid(File location)
  {
    return new File(location, ENGINE_PATH).isDirectory();
  }

  public static String getProfileType(IProfile delegate)
  {
    String type = delegate.getProperty(Profile.PROP_PROFILE_TYPE);
    if (type == null)
    {
      // This check is done for legacy reasons. The current targlet.ui plugin contributes a profileType extension.
      if (delegate.getProperty("targlet.container.id") != null)
      {
        return "Targlet";
      }

      if (delegate.getProperty(Profile.PROP_INSTALL_FOLDER) != null)
      {
        return "Installation";
      }
    }

    return type;
  }

  public static String getProfileExtraInfo(IProfile delegate)
  {
    List<String> tokens = new ArrayList<String>();
    tokens.add(getProfileType(delegate));
    tokens.add(delegate.getProperty(Profile.PROP_CACHE));
    tokens.add(delegate.getProperty(Profile.PROP_INSTALL_FOLDER));
    tokens.add(delegate.getProperty(Profile.PROP_PROFILE_REFERENCER));
    return StringUtil.implode(tokens, '|');
  }

  public static IProvisioningAgent getOrCreateProvisioningAgent(File location)
  {
    BundleContext context = P2CorePlugin.INSTANCE.getBundleContext();
    ServiceReference<IProvisioningAgent> agentRef = null;
    IProvisioningAgent provisioningAgent = null;

    try
    {
      Collection<ServiceReference<IProvisioningAgent>> agentRefs = null;

      try
      {
        agentRefs = context.getServiceReferences(IProvisioningAgent.class, "(locationURI=" + location.toURI() + ")");
      }
      catch (InvalidSyntaxException ex)
      {
        // Can't happen because we write the filter ourselves
      }

      if (agentRefs != null && !agentRefs.isEmpty())
      {
        agentRef = agentRefs.iterator().next();
        provisioningAgent = context.getService(agentRef);
      }
    }
    catch (Exception ex)
    {
      provisioningAgent = null;
    }
    finally
    {
      if (agentRef != null)
      {
        context.ungetService(agentRef);
      }
    }

    if (provisioningAgent == null)
    {
      location.mkdirs();
      ServiceReference<IProvisioningAgentProvider> providerRef = null;

      try
      {
        providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
        if (providerRef != null)
        {
          IProvisioningAgentProvider provider = context.getService(providerRef);
          provisioningAgent = provider.createAgent(location.toURI());
        }
      }
      catch (ProvisionException ex)
      {
        throw new P2Exception(ex);
      }
      finally
      {
        if (providerRef != null)
        {
          context.ungetService(providerRef);
        }
      }
    }

    if (provisioningAgent == null)
    {
      throw new P2Exception("Provisioning agent could not be loaded from " + location);
    }

    return provisioningAgent;
  }
}

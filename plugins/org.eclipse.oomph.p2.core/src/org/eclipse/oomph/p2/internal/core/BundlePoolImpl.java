/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class BundlePoolImpl extends AgentManagerElementImpl implements BundlePool
{
  private final Agent agent;

  private File location;

  private String path;

  private IFileArtifactRepository fileArtifactRepository;

  public BundlePoolImpl(AgentImpl agent, File location)
  {
    this.agent = agent;
    setLocation(location);
  }

  @Override
  public String getElementType()
  {
    return "bundle pool";
  }

  public AgentManager getAgentManager()
  {
    return agent.getAgentManager();
  }

  public Agent getAgent()
  {
    return agent;
  }

  public File getLocation()
  {
    return location;
  }

  public void setLocation(File location)
  {
    this.location = location;
    path = location.getAbsolutePath();
  }

  public Set<String> getClients()
  {
    return ((AgentManagerImpl)agent.getAgentManager()).getClientsFor(path);
  }

  public boolean isValid()
  {
    return true;
  }

  public boolean isCurrent()
  {
    if (!agent.isCurrent())
    {
      return false;
    }

    Profile currentProfile = agent.getCurrentProfile();
    return currentProfile != null && currentProfile.getBundlePool() == this;
  }

  public boolean isUsed()
  {
    if (!getProfiles().isEmpty())
    {
      return true;
    }

    return false;
  }

  @Override
  protected void doDelete()
  {
    ((AgentImpl)agent).deleteBundlePool(this);
  }

  public synchronized IFileArtifactRepository getFileArtifactRepository()
  {
    if (fileArtifactRepository == null)
    {
      IArtifactRepositoryManager artifactRepositoryManager = agent.getArtifactRepositoryManager();
      URI uri = location.toURI();

      try
      {
        if (artifactRepositoryManager.contains(uri))
        {
          fileArtifactRepository = (IFileArtifactRepository)artifactRepositoryManager.loadRepository(uri, null);
        }
      }
      catch (CoreException ex)
      {
        //$FALL-THROUGH$
      }

      if (fileArtifactRepository == null)
      {
        try
        {
          fileArtifactRepository = (IFileArtifactRepository)artifactRepositoryManager.createRepository(uri, "Shared Bundle Pool",
              IArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY, null);
        }
        catch (ProvisionException ex)
        {
          throw new P2Exception("Bundle pool " + location + " could not be loaded");
        }
      }
    }

    return fileArtifactRepository;
  }

  public Set<String> getProfileIDs()
  {
    return ((AgentImpl)agent).getProfileIDs(this);
  }

  public Collection<Profile> getProfiles()
  {
    return ((AgentImpl)agent).getProfiles(this);
  }

  public Profile getProfile(String id)
  {
    Profile profile = agent.getProfile(id);
    if (profile != null && profile.getBundlePool() == this)
    {
      return profile;
    }

    return null;
  }

  public ProfileCreator addProfile(String id, String type)
  {
    return new ProfileCreatorImpl(this, id, type)
    {
      private static final long serialVersionUID = 1L;

      @Override
      public ProfileCreator setCacheFolder(File value)
      {
        if (!location.equals(value))
        {
          throw new IllegalArgumentException("Cache folder of a pooled profile cannot be changed: " + value);
        }

        return this;
      }

      @Override
      protected Profile doCreateProfile()
      {
        this.set(Profile.PROP_CACHE, path);
        return ((AgentImpl)agent).createProfile(this);
      }
    };
  }

  @Override
  public String toString()
  {
    return getLocation().getAbsolutePath();
  }
}

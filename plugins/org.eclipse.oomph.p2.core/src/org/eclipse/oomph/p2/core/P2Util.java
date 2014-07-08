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
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.p2.internal.core.P2CorePlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.internal.repository.mirroring.Mirroring;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public final class P2Util
{
  private static final String SIMPLE_METADATA_REPOSITORY = IMetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY;

  private static final String SIMPLE_ARTIFACT_REPOSITORY = IArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY;

  private P2Util()
  {
  }

  public static synchronized AgentManager getAgentManager()
  {
    if (AgentManagerImpl.instance == null)
    {
      AgentManagerImpl.instance = new AgentManagerImpl();
    }

    return AgentManagerImpl.instance;
  }

  public static File getAgentLocation(IProvisioningAgent agent)
  {
    IAgentLocation location = (IAgentLocation)agent.getService(IAgentLocation.SERVICE_NAME);
    return URIUtil.toFile(location.getRootLocation());
  }

  public static IProvisioningAgent getCurrentProvisioningAgent()
  {
    return getAgentManager().getCurrentAgent().getProvisioningAgent();
  }

  public static Agent createAgent(File agentLocation)
  {
    return new AgentImpl((AgentManagerImpl)P2Util.getAgentManager(), agentLocation);
  }

  public static Set<String> getKnownRepositories(IRepositoryManager<?> manager)
  {
    Set<String> result = new HashSet<String>();
    for (URI uri : manager.getKnownRepositories(IRepositoryManager.REPOSITORIES_NON_SYSTEM))
    {
      result.add(uri.toString());
    }

    return result;
  }

  public static void mirrorRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException
  {
    mirrorMetadataRepository(sourceURI, targetURI, filter, monitor);
    mirrorArtifactRepository(sourceURI, targetURI, filter, monitor);
  }

  public static void mirrorMetadataRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException
  {
    List<URI> repositoriesToRemove = new ArrayList<URI>();
    repositoriesToRemove.add(targetURI);

    IMetadataRepositoryManager manager = getAgentManager().getCurrentAgent().getMetadataRepositoryManager();
    if (!manager.contains(sourceURI))
    {
      repositoriesToRemove.add(sourceURI);
    }

    try
    {
      IMetadataRepository sourceRepository = manager.loadRepository(sourceURI, 0, monitor);
      String name = sourceRepository.getName();
      if (name == null)
      {
        name = sourceURI.toString();
      }

      IMetadataRepository targetRepository = manager.createRepository(targetURI, name, SIMPLE_METADATA_REPOSITORY, sourceRepository.getProperties());
      targetRepository.setProperty(IRepository.PROP_COMPRESSED, "true");

      List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>();
      for (IInstallableUnit iu : sourceRepository.query(QueryUtil.createIUAnyQuery(), null))
      {
        if (filter == null || filter.matches(iu))
        {
          ius.add(iu);
        }
      }

      targetRepository.addInstallableUnits(ius);
    }
    finally
    {
      for (URI uri : repositoriesToRemove)
      {
        manager.removeRepository(uri);
      }
    }
  }

  public static void mirrorArtifactRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException
  {
    List<URI> repositoriesToRemove = new ArrayList<URI>();
    repositoriesToRemove.add(targetURI);

    Agent agent = getAgentManager().getCurrentAgent();
    IArtifactRepositoryManager manager = agent.getArtifactRepositoryManager();
    if (!manager.contains(sourceURI))
    {
      repositoriesToRemove.add(sourceURI);
    }

    try
    {
      IArtifactRepository sourceRepository = manager.loadRepository(sourceURI, 0, monitor);
      String name = sourceRepository.getName();
      if (name == null)
      {
        name = sourceURI.toString();
      }

      IArtifactRepository targetRepository = manager.createRepository(targetURI, name, SIMPLE_ARTIFACT_REPOSITORY, sourceRepository.getProperties());
      targetRepository.setProperty(IRepository.PROP_COMPRESSED, "true");

      List<IArtifactKey> keys = new ArrayList<IArtifactKey>();
      for (IArtifactKey key : sourceRepository.query(ArtifactKeyQuery.ALL_KEYS, null))
      {
        if (filter == null || filter.matches(key))
        {
          keys.add(key);
        }
      }

      Transport transport = (Transport)agent.getProvisioningAgent().getService(Transport.SERVICE_NAME);

      Mirroring mirror = new Mirroring(sourceRepository, targetRepository, true);
      mirror.setCompare(false);
      mirror.setTransport(transport);
      mirror.setArtifactKeys(keys.toArray(new IArtifactKey[keys.size()]));

      IStatus result = mirror.run(true, false);
      P2CorePlugin.INSTANCE.coreException(result);
    }
    finally
    {
      for (URI uri : repositoriesToRemove)
      {
        manager.removeRepository(uri);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface VersionedIdFilter
  {
    public boolean matches(IVersionedId versionedId);
  }
}

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
import org.eclipse.oomph.p2.internal.core.ProfileReferencerImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class P2Util
{
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

  public static ProfileReferencer createProfileReferencer(File file, boolean directory)
  {
    return new ProfileReferencerImpl(file, directory);
  }

  public static void mirrorMetadataRepository(URI sourceURI, URI targetURI, IProgressMonitor monitor) throws ProvisionException
  {
    List<URI> repositoriesToRemove = new ArrayList<URI>();
    repositoriesToRemove.add(targetURI);

    IMetadataRepositoryManager metadataRepositoryManager = getAgentManager().getCurrentAgent().getMetadataRepositoryManager();
    if (!metadataRepositoryManager.contains(sourceURI))
    {
      repositoriesToRemove.add(sourceURI);
    }

    try
    {
      IMetadataRepository sourceRepository = metadataRepositoryManager.loadRepository(sourceURI, 0, monitor);

      String name = sourceRepository.getName();
      if (name == null)
      {
        name = sourceURI.toString();
      }

      IMetadataRepository targetRepository = metadataRepositoryManager.createRepository(targetURI, name, IMetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY,
          sourceRepository.getProperties());
      targetRepository.setProperty(IRepository.PROP_COMPRESSED, "true");

      IQueryResult<IInstallableUnit> ius = sourceRepository.query(QueryUtil.createIUAnyQuery(), null);
      targetRepository.addInstallableUnits(ius.toUnmodifiableSet());
    }
    finally
    {
      for (URI uri : repositoriesToRemove)
      {
        metadataRepositoryManager.removeRepository(uri);
      }
    }
  }
}

/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.tests;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.P2Util.VersionedIdFilter;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.p2.internal.core.P2CorePlugin;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.p2.internal.repository.mirroring.Mirroring;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import org.junit.BeforeClass;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public abstract class AbstractP2Test extends AbstractTest
{
  protected static final String TMP = PropertiesUtil.getProperty("java.io.tmpdir");

  private static final String CDO = "p2-test-mirror-001-cdo";

  private static final String PLATFORM = "p2-test-mirror-001-platform";

  private static final String SIMPLE_METADATA_REPOSITORY = IMetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY;

  private static final String SIMPLE_ARTIFACT_REPOSITORY = IArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY;

  private static final VersionedIdFilter CDO_FILTER = new VersionedIdFilter()
  {
    @Override
    public boolean matches(IVersionedId versionedId)
    {
      String id = versionedId.getId();
      return id.startsWith("org.eclipse.net4j.util") || id.startsWith("org.apache");
    }
  };

  private static final VersionedIdFilter PLATFORM_FILTER = new VersionedIdFilter()
  {
    @Override
    public boolean matches(IVersionedId versionedId)
    {
      String id = versionedId.getId();
      return id.startsWith("com.jcraft.jsch") || id.startsWith("org.apache") || id.startsWith("a.jre");
    }
  };

  public static final File CDO_OLD = new File(TMP, CDO + "-old");

  public static final File CDO_NEW = new File(TMP, CDO + "-new");

  public static final File PLATFORM_OLD = new File(TMP, PLATFORM + "-old");

  public static final File PLATFORM_NEW = new File(TMP, PLATFORM + "-new");

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    AgentManagerImpl.instance = new AgentManagerImpl(getUserHome());
  }

  @Override
  public void tearDown() throws Exception
  {
    AgentManagerImpl.instance = null;
    super.tearDown();
  }

  protected Agent getAgent()
  {
    AgentManager agentManager = P2Util.getAgentManager();
    return agentManager.getAgents().iterator().next();
  }

  protected Agent getFreshAgent()
  {
    AgentManagerImpl.instance = new AgentManagerImpl(getUserHome());
    return getAgent();
  }

  protected void commitProfileTransaction(ProfileTransaction transaction, boolean expectedChange) throws CoreException
  {
    commitProfileTransaction(transaction, expectedChange, LOGGER);
  }

  protected void commitProfileTransaction(ProfileTransaction transaction, boolean expectedChange, IProgressMonitor monitor) throws CoreException
  {
    boolean actualChange = transaction.commit(monitor);
    if (actualChange != expectedChange)
    {
      if (expectedChange)
      {
        throw new AssertionError("Profile should have changed, but didn't");
      }

      throw new AssertionError("Profile should not have changed, but did");
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    mirror("https://download.eclipse.org/modeling/emf/cdo/drops/R20130918-0029", CDO_OLD, CDO_FILTER);
    mirror("https://download.eclipse.org/modeling/emf/cdo/drops/R20140218-1655", CDO_NEW, CDO_FILTER);
    mirror("https://download.eclipse.org/eclipse/updates/4.3/R-4.3.1-201309111000", PLATFORM_OLD, PLATFORM_FILTER);
    mirror("https://download.eclipse.org/eclipse/updates/4.3/R-4.3.2-201402211700", PLATFORM_NEW, PLATFORM_FILTER);
  }

  private static void mirror(String repo, File local, VersionedIdFilter filter) throws Exception
  {
    if (!local.isDirectory())
    {
      LOGGER.setTaskName("Creating test mirror of " + repo + " under " + local);
      mirrorRepository(new URI(repo), local.toURI(), filter, LOGGER);
      LOGGER.setTaskName(null);
    }
  }

  public static void mirrorArtifactRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException
  {
    List<URI> repositoriesToRemove = new ArrayList<>();
    repositoriesToRemove.add(targetURI);

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
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

      List<IArtifactKey> keys = new ArrayList<>();
      for (IArtifactKey key : P2Util.asIterable(sourceRepository.query(ArtifactKeyQuery.ALL_KEYS, null)))
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

  public static void mirrorRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException

  {
    mirrorMetadataRepository(sourceURI, targetURI, filter, monitor);
    mirrorArtifactRepository(sourceURI, targetURI, filter, monitor);
  }

  public static void mirrorMetadataRepository(URI sourceURI, URI targetURI, VersionedIdFilter filter, IProgressMonitor monitor) throws CoreException
  {
    List<URI> repositoriesToRemove = new ArrayList<>();
    repositoriesToRemove.add(targetURI);

    IMetadataRepositoryManager manager = P2Util.getAgentManager().getCurrentAgent().getMetadataRepositoryManager();
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

      List<IInstallableUnit> ius = new ArrayList<>();
      for (IInstallableUnit iu : P2Util.asIterable(sourceRepository.query(QueryUtil.createIUAnyQuery(), null)))
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
}

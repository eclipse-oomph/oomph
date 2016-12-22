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
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.p2.internal.core.CachingTransport;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

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

  public static Set<String> getKnownRepositories(IRepositoryManager<?> manager)
  {
    Set<String> result = new HashSet<String>();
    for (URI uri : manager.getKnownRepositories(IRepositoryManager.REPOSITORIES_NON_SYSTEM))
    {
      result.add(uri.toString());
    }

    return result;
  }

  public static File getCacheFile(URI uri)
  {
    Agent agent = getAgentManager().getCurrentAgent();

    IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
    if (manager instanceof CachingRepositoryManager)
    {
      CachingTransport transport = ((CachingRepositoryManager<?>)manager).getTransport();
      if (transport != null)
      {
        return transport.getCacheFile(uri);
      }
    }

    return null;
  }

  @SuppressWarnings("all")
  public static <T> Iterable<T> asIterable(final IQueryResult<T> queryResult)
  {
    if (queryResult instanceof Iterable<?>)
    {
      return queryResult;
    }

    return new Iterable<T>()
    {
      public Iterator<T> iterator()
      {
        return queryResult.iterator();
      }
    };
  }

  public static String getName(IInstallableUnit iu)
  {
    String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
    if (StringUtil.isEmpty(name))
    {
      return iu.getId();
    }

    return name;
  }

  @SuppressWarnings("unused")
  private static InstallableUnitDescription createDescription(IInstallableUnit iu)
  {
    InstallableUnitDescription description = new MetadataFactory.InstallableUnitDescription();

    description.setId(iu.getId());

    description.setVersion(iu.getVersion());

    Collection<IArtifactKey> artifacts = iu.getArtifacts();
    description.setArtifacts(artifacts.toArray(new IArtifactKey[artifacts.size()]));

    Collection<IProvidedCapability> providedCapabilities = iu.getProvidedCapabilities();
    description.setCapabilities(providedCapabilities.toArray(new IProvidedCapability[providedCapabilities.size()]));

    description.setCopyright(iu.getCopyright());

    IMatchExpression<IInstallableUnit> filter = iu.getFilter();
    description.setFilter(filter);

    Collection<ILicense> licenses = iu.getLicenses();
    description.setLicenses(licenses.toArray(new ILicense[licenses.size()]));

    Collection<IRequirement> metaRequirements = iu.getMetaRequirements();
    description.setMetaRequirements(metaRequirements.toArray(new IRequirement[metaRequirements.size()]));

    Collection<IRequirement> requirements = iu.getRequirements();
    description.setRequirements(requirements.toArray(new IRequirement[requirements.size()]));

    description.setSingleton(iu.isSingleton());

    description.setTouchpointType(iu.getTouchpointType());
    description.setUpdateDescriptor(iu.getUpdateDescriptor());

    for (Iterator<Entry<String, String>> iterator = iu.getProperties().entrySet().iterator(); iterator.hasNext();)
    {
      Entry<String, String> entry = iterator.next();
      description.setProperty(entry.getKey(), entry.getValue());
    }

    for (ITouchpointData touchpointData : iu.getTouchpointData())
    {
      description.addTouchpointData(touchpointData);
    }

    return description;
  }

  /**
   * @author Eike Stepper
   */
  public interface VersionedIdFilter
  {
    public boolean matches(IVersionedId versionedId);
  }
}

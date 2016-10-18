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
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class AgentManagerImpl implements AgentManager
{
  private static final String AGENT_SUFFIX = ":agent";

  public static AgentManager instance;

  private final PersistentMap<Agent> agentMap;

  private final File defaultAgentLocation;

  private final File defaultsFile;

  private Agent currentAgent;

  private boolean currentAgentInMap;

  public AgentManagerImpl()
  {
    this(new File(PropertiesUtil.getUserHome()));
  }

  public AgentManagerImpl(final File userHome)
  {
    defaultAgentLocation = new File(userHome, ".p2");

    File folder = P2CorePlugin.getUserStateFolder(userHome);
    File infoFile = new File(folder, "agents.info");
    defaultsFile = new File(folder, "defaults.info");

    agentMap = new PersistentMap<Agent>(infoFile)
    {
      @Override
      protected Agent loadElement(String key, String extraInfo)
      {
        File location = new File(key);
        if (AgentImpl.isValid(location))
        {
          return super.loadElement(key, extraInfo);
        }

        return null;
      }

      @Override
      protected Agent createElement(String key, String extraInfo)
      {
        return new AgentImpl(AgentManagerImpl.this, new File(key));
      }

      @Override
      protected void initializeFirstTime()
      {
        initializeFirstTime(defaultAgentLocation);
        initializeFirstTime(new File(userHome, "p2"));
        initializeFirstTime(new File(userHome, ".eclipse"));
        initializeFirstTime(new File(userHome, "eclipse"));

        if (getElements().isEmpty())
        {
          addAgent(defaultAgentLocation);
        }

        Collection<BundlePool> bundlePools = getBundlePools();
        if (bundlePools.isEmpty())
        {
          Agent agent = getAgent(defaultAgentLocation);
          if (agent == null)
          {
            Collection<Agent> agents = getAgents(); // Is not null because of addAgent() above.
            agent = agents.iterator().next();
          }

          File poolLocation = new File(agent.getLocation(), BundlePool.DEFAULT_NAME);
          agent.addBundlePool(poolLocation);
        }
      }

      private void initializeFirstTime(File location)
      {
        if (IOUtil.isValidFolder(location))
        {
          if (AgentImpl.isValid(location))
          {
            addAgent(location);
          }
          else
          {
            for (File child : location.listFiles())
            {
              initializeFirstTime(child);
            }
          }
        }
      }
    };

    agentMap.load();
  }

  public PersistentMap<Agent> getAgentMap()
  {
    return agentMap;
  }

  public void dispose()
  {
    for (Agent agent : getAgents())
    {
      ((AgentImpl)agent).dispose();
    }

    if (!currentAgentInMap && currentAgent != null)
    {
      ((AgentImpl)currentAgent).dispose();
    }
  }

  public synchronized Agent getCurrentAgent()
  {
    if (currentAgent == null)
    {
      BundleContext context = P2CorePlugin.INSTANCE.getBundleContext();
      ServiceReference<?> reference = context.getServiceReference(IProvisioningAgent.class);
      if (reference == null)
      {
        throw new P2Exception("Current provisioning agent could not be loaded");
      }

      try
      {
        IProvisioningAgent provisioningAgent = (IProvisioningAgent)context.getService(reference);
        File agentLocation = P2Util.getAgentLocation(provisioningAgent);
        Agent agent = getAgent(agentLocation);
        if (agent != null)
        {
          currentAgent = agent;
          currentAgentInMap = true;
        }
        else
        {
          currentAgent = new AgentImpl(this, agentLocation);
        }

        ((AgentImpl)currentAgent).initializeProvisioningAgent(provisioningAgent);
      }
      finally
      {
        context.ungetService(reference);
      }
    }

    return currentAgent;
  }

  public File getDefaultAgentLocation()
  {
    return defaultAgentLocation;
  }

  public Set<File> getAgentLocations()
  {
    return getLocations(agentMap.getElementKeys());
  }

  public Collection<Agent> getAgents()
  {
    return agentMap.getElements();
  }

  public Agent getAgent(File location)
  {
    return agentMap.getElement(location.getAbsolutePath());
  }

  public Agent addAgent(File location)
  {
    return agentMap.addElement(location.getAbsolutePath(), null);
  }

  public void deleteAgent(Agent agent)
  {
    agentMap.removeElement(agent.getLocation().getAbsolutePath());

    // TODO Delete artifacts from disk
  }

  public void refreshAgents(IProgressMonitor monitor)
  {
    monitor.beginTask("Refreshing agents...", 1 + 20);

    try
    {
      agentMap.refresh();
      monitor.worked(1);

      Collection<Agent> agents = getAgents();
      refreshAgents(agents, MonitorUtil.create(monitor, 20));
    }
    finally
    {
      monitor.done();
    }
  }

  private void refreshAgents(Collection<Agent> agents, IProgressMonitor monitor)
  {
    monitor.beginTask("", 21 * agents.size());

    try
    {
      for (Agent agent : agents)
      {
        P2CorePlugin.checkCancelation(monitor);
        monitor.subTask("Refreshing " + agent.getLocation());

        agent.refreshBundlePools(MonitorUtil.create(monitor, 1));
        agent.refreshProfiles(MonitorUtil.create(monitor, 20));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  public Collection<BundlePool> getBundlePools()
  {
    List<BundlePool> bundlePools = new ArrayList<BundlePool>();
    for (Agent agent : getAgents())
    {
      bundlePools.addAll(agent.getBundlePools());
    }

    return bundlePools;
  }

  public BundlePool getBundlePool(File location) throws P2Exception
  {
    for (Agent agent : getAgents())
    {
      BundlePool bundlePool = agent.getBundlePool(location);
      if (bundlePool != null)
      {
        return bundlePool;
      }
    }

    return null;
  }

  private BundlePool getBundlePool(String path) throws P2Exception
  {
    File location = new File(path);
    if (location.isDirectory())
    {
      return getBundlePool(location);
    }

    return null;
  }

  public BundlePool getDefaultBundlePool(String client) throws P2Exception
  {
    Properties defaults = loadDefaults();
    BundlePool bundlePool = restoreBundlePool(client, defaults);
    if (bundlePool != null)
    {
      return bundlePool;
    }

    for (Object otherClient : defaults.keySet())
    {
      String clientId = (String)otherClient;

      // Skip agent locations.
      if (clientId != null && !clientId.equals(client) && !clientId.endsWith(AGENT_SUFFIX))
      {
        bundlePool = restoreBundlePool(clientId, defaults);
        if (bundlePool != null)
        {
          return bundlePool;
        }
      }
    }

    return getDefaultBundlePool();
  }

  private BundlePool getDefaultBundlePool() throws P2Exception
  {
    File defaultPoolLocation = new File(defaultAgentLocation, BundlePool.DEFAULT_NAME);
    BundlePool bundlePool = getBundlePool(defaultPoolLocation);
    if (bundlePool != null)
    {
      return bundlePool;
    }

    Agent agent = addAgent(defaultAgentLocation);
    Collection<BundlePool> bundlePools = agent.getBundlePools();
    if (!bundlePools.isEmpty())
    {
      return bundlePools.iterator().next();
    }

    return agent.addBundlePool(defaultPoolLocation);
  }

  public void setDefaultBundlePool(String client, BundlePool bundlePool)
  {
    Properties defaults = loadDefaults();
    defaults.put(client, bundlePool.getLocation().getAbsolutePath());
    defaults.put(client + AGENT_SUFFIX, bundlePool.getAgent().getLocation().getAbsolutePath());
    saveDefaults(defaults);
  }

  private BundlePool restoreBundlePool(String client, Properties defaults)
  {
    String location = defaults.getProperty(client);
    if (location != null)
    {
      BundlePool bundlePool = getBundlePool(location);
      if (bundlePool == null)
      {
        String agentLocation = defaults.getProperty(client + AGENT_SUFFIX);
        if (agentLocation != null)
        {
          File agentDir = new File(agentLocation);

          Agent agent = addAgent(agentDir);
          if (agent != null)
          {
            File poolDir = new File(location);
            bundlePool = agent.addBundlePool(poolDir);
          }
        }
      }
      return bundlePool;
    }
    return null;
  }

  public Set<String> getClientsFor(String bundlePoolLocation)
  {
    Set<String> clients = new HashSet<String>();

    Properties defaults = loadDefaults();
    for (Map.Entry<Object, Object> entry : defaults.entrySet())
    {
      String client = (String)entry.getKey();

      // Skip agent locations.
      if (client != null && !client.endsWith(AGENT_SUFFIX))
      {
        String location = (String)entry.getValue();
        if (location.equals(bundlePoolLocation))
        {
          clients.add(client);
        }
      }
    }

    return clients;
  }

  private Properties loadDefaults()
  {
    Properties defaults = new Properties();
    if (defaultsFile.exists())
    {
      InputStream stream = null;

      try
      {
        stream = new FileInputStream(defaultsFile);
        defaults.load(stream);
      }
      catch (IOException ex)
      {
        throw new P2Exception(ex);
      }
      finally
      {
        IOUtil.close(stream);
      }
    }

    return defaults;
  }

  private void saveDefaults(Properties defaults)
  {
    OutputStream stream = null;

    try
    {
      stream = new FileOutputStream(defaultsFile);
      defaults.store(stream, "P2 clients store their default bundle pool locations here");
    }
    catch (IOException ex)
    {
      throw new P2Exception(ex);
    }
    finally
    {
      IOUtil.close(stream);
    }
  }

  public static Set<File> getLocations(Set<String> keys)
  {
    Set<File> locations = new HashSet<File>();
    for (String key : keys)
    {
      locations.add(new File(key));
    }

    return locations;
  }
}

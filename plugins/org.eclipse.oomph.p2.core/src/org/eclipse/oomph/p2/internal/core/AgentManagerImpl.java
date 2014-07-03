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
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

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
  public static AgentManager instance;

  private final PersistentMap<Agent> agentMap;

  private final File defaultsFile;

  private Agent currentAgent;

  public AgentManagerImpl()
  {
    this(new File(PropertiesUtil.USER_HOME));
  }

  public AgentManagerImpl(final File userHome)
  {
    File folder = P2CorePlugin.getUserStateFolder(userHome);
    File infoFile = new File(folder, "agents.info");
    defaultsFile = new File(folder, "defaults.info");

    agentMap = new PersistentMap<Agent>(infoFile)
    {
      @Override
      protected Agent createElement(String key, String extraInfo)
      {
        return new AgentImpl(AgentManagerImpl.this, new File(key));
      }

      @Override
      protected void initializeFirstTime()
      {
        File defaultLocation = new File(userHome, ".p2");

        initializeFirstTime(defaultLocation);
        initializeFirstTime(new File(userHome, "p2"));
        initializeFirstTime(new File(userHome, ".eclipse"));
        initializeFirstTime(new File(userHome, "eclipse"));

        if (getElements().isEmpty())
        {
          addAgent(defaultLocation);
        }

        if (getBundlePools().isEmpty())
        {
          Agent agent = getAgent(defaultLocation);
          if (agent == null)
          {
            agent = getAgents().iterator().next();
          }

          File poolLocation = new File(agent.getLocation(), "pool");
          agent.addBundlePool(poolLocation);
        }
      }

      private void initializeFirstTime(File location)
      {
        if (location.isDirectory())
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

    int xxx;
    // TODO Delete artifacts from disk
  }

  public boolean refreshAgents()
  {
    return agentMap.refresh();
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

    throw new P2Exception("Bundle pool " + location + " could not be loaded");
  }

  public BundlePool getDefaultBundlePool(String client) throws P2Exception
  {
    Properties defaults = loadDefaults();
    String location = (String)defaults.get(client);
    if (location == null)
    {
      if (defaults.isEmpty())
      {
        Collection<BundlePool> bundlePools = getBundlePools();
        if (bundlePools.isEmpty())
        {
          return null;
        }

        location = bundlePools.iterator().next().getLocation().getAbsolutePath();
      }
      else
      {
        location = (String)defaults.values().iterator().next();
      }

      defaults.put(client, location);
      saveDefaults(defaults);
    }

    return getBundlePool(new File(location));
  }

  public void setDefaultBundlePool(String client, BundlePool bundlePool)
  {
    Properties defaults = loadDefaults();
    defaults.put(client, bundlePool.getLocation().getAbsolutePath());
    saveDefaults(defaults);
  }

  public Set<String> getClientsFor(String bundlePoolLocation)
  {
    Set<String> clients = new HashSet<String>();

    Properties defaults = loadDefaults();
    for (Map.Entry<Object, Object> entry : defaults.entrySet())
    {
      String client = (String)entry.getKey();
      String location = (String)entry.getValue();
      if (location.equals(bundlePoolLocation))
      {
        clients.add(client);
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

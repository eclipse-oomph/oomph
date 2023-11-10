/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
import org.eclipse.osgi.util.NLS;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
  private static final String AGENT_SUFFIX = ":agent"; //$NON-NLS-1$

  public static AgentManager instance;

  private final Charset charset;

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
    defaultAgentLocation = new File(userHome, ".p2"); //$NON-NLS-1$
    File folder = P2CorePlugin.getUserStateFolder(userHome);
    File encodingFile = new File(folder, "encoding.info"); //$NON-NLS-1$
    Charset charset;
    try
    {
      charset = new PersistentMap<Charset>(encodingFile, StandardCharsets.UTF_8)
      {
        {
          load();
        }

        @Override
        protected Charset createElement(String key, String extraInfo)
        {
          return Charset.forName(key);
        }

        @Override
        protected void initializeFirstTime()
        {
          addElement(Charset.defaultCharset().name(), null);
        }
      }.getElements().iterator().next();
    }
    catch (Exception ex)
    {
      charset = Charset.defaultCharset();
    }

    this.charset = charset;

    File infoFile = new File(folder, "agents.info"); //$NON-NLS-1$
    defaultsFile = new File(folder, "defaults.info"); //$NON-NLS-1$

    agentMap = new PersistentMap<Agent>(infoFile, charset)
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
        initializeFirstTime(new File(userHome, "p2")); //$NON-NLS-1$
        initializeFirstTime(new File(userHome, ".eclipse")); //$NON-NLS-1$
        initializeFirstTime(new File(userHome, "eclipse")); //$NON-NLS-1$

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

  public Charset getCharset()
  {
    return charset;
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

  @Override
  public synchronized Agent getCurrentAgent()
  {
    if (currentAgent == null)
    {
      BundleContext context = P2CorePlugin.INSTANCE.getBundleContext();
      ServiceReference<?> reference = context.getServiceReference(IProvisioningAgent.class);
      if (reference == null)
      {
        throw new P2Exception(Messages.AgentManagerImpl_AgentNotLoaded_exception);
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

  @Override
  public File getDefaultAgentLocation()
  {
    return defaultAgentLocation;
  }

  @Override
  public Set<File> getAgentLocations()
  {
    return getLocations(agentMap.getElementKeys());
  }

  @Override
  public Collection<Agent> getAgents()
  {
    return agentMap.getElements();
  }

  @Override
  public Agent getAgent(File location)
  {
    return agentMap.getElement(location.getAbsolutePath());
  }

  @Override
  public Agent addAgent(File location)
  {
    return agentMap.addElement(location.getAbsolutePath(), null);
  }

  public void deleteAgent(Agent agent)
  {
    agentMap.removeElement(agent.getLocation().getAbsolutePath());

    // TODO Delete artifacts from disk
  }

  @Override
  public void refreshAgents(IProgressMonitor monitor)
  {
    monitor.beginTask(Messages.AgentManagerImpl_RefreshingAgents_task, 1 + 20);

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
    monitor.beginTask("", 21 * agents.size()); //$NON-NLS-1$

    try
    {
      for (Agent agent : agents)
      {
        P2CorePlugin.checkCancelation(monitor);
        monitor.subTask(NLS.bind(Messages.AgentManagerImpl_Refreshing_task, agent.getLocation()));

        agent.refreshBundlePools(MonitorUtil.create(monitor, 1));
        agent.refreshProfiles(MonitorUtil.create(monitor, 20));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  @Override
  public Collection<BundlePool> getBundlePools()
  {
    List<BundlePool> bundlePools = new ArrayList<>();
    for (Agent agent : getAgents())
    {
      bundlePools.addAll(agent.getBundlePools());
    }

    return bundlePools;
  }

  @Override
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

  @Override
  public BundlePool getDefaultBundlePool(String client) throws P2Exception
  {
    Properties defaults = loadDefaults();
    BundlePool bundlePool = null;
    try
    {
      bundlePool = restoreBundlePool(client, defaults);
      if (bundlePool != null)
      {
        return bundlePool;
      }
    }
    catch (Exception ex)
    {
      // If loaded defaults can't be used to create the bundle pool and the agent, compute a new default.
      P2CorePlugin.INSTANCE.log(ex);
    }

    for (Object otherClient : defaults.keySet())
    {
      String clientId = (String)otherClient;

      // Skip agent locations.
      if (clientId != null && !clientId.equals(client) && !clientId.endsWith(AGENT_SUFFIX))
      {
        try
        {
          bundlePool = restoreBundlePool(clientId, defaults);
          if (bundlePool != null)
          {
            return bundlePool;
          }
        }
        catch (Exception ex)
        {
          // If loaded defaults can't be used to create the bundle pool and the agent, compute a new default.
          P2CorePlugin.INSTANCE.log(ex);
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

  @Override
  public void setDefaultBundlePool(String client, BundlePool bundlePool)
  {
    Properties defaults = loadDefaults();
    String bundlePoolLocation = bundlePool.getLocation().getAbsolutePath();
    Object oldBundlePoolLocation = defaults.put(client, bundlePoolLocation);
    String agentLocation = bundlePool.getAgent().getLocation().getAbsolutePath();
    Object oldAgentLocation = defaults.put(client + AGENT_SUFFIX, agentLocation);
    if (!bundlePoolLocation.equals(oldBundlePoolLocation) || !agentLocation.equals(oldAgentLocation))
    {
      saveDefaults(defaults);
    }
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
    Set<String> clients = new HashSet<>();

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
      defaults.store(stream, Messages.AgentManagerImpl_Client_message);
    }
    catch (IOException ex)
    {
      // Only log an exception because failing to save the defaults is not catastrophic.
      P2CorePlugin.INSTANCE.log(ex);
    }
    finally
    {
      IOUtil.close(stream);
    }
  }

  public static Set<File> getLocations(Set<String> keys)
  {
    Set<File> locations = new HashSet<>();
    for (String key : keys)
    {
      locations.add(new File(key));
    }

    return locations;
  }
}

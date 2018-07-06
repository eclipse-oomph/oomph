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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Eike Stepper
 */
public final class TargletContainerDescriptorManager
{
  public static final String WORKSPACE_LOCATION = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();

  private static final String STATE_PATH = ".metadata/.plugins/" + TargletsCorePlugin.INSTANCE.getSymbolicName();

  private static final String WORKSPACE_STATE_RELATIVE_PATH = STATE_PATH + "/descriptors.bin"; //$NON-NLS-1$

  private static final String WORKSPACE_REFERENCER_RELATIVE_PATH = STATE_PATH + "/profiles.txt"; //$NON-NLS-1$

  private static final File WORKSPACE_STATE_FILE = new File(WORKSPACE_LOCATION, WORKSPACE_STATE_RELATIVE_PATH);

  public static final File WORKSPACE_REFERENCER_FILE = new File(WORKSPACE_LOCATION, WORKSPACE_REFERENCER_RELATIVE_PATH);

  private static TargletContainerDescriptorManager instance;

  private final CountDownLatch initialized = new CountDownLatch(1);

  private Throwable initializationProblem;

  private Map<String, TargletContainerDescriptor> descriptors;

  private TargletContainerDescriptorManager() throws CoreException
  {
    initializeSync(null);

    // new Job("Initialize Targlet Containers")
    // {
    // @Override
    // protected IStatus run(IProgressMonitor monitor)
    // {
    // initializeSync(monitor);
    // return Status.OK_STATUS;
    // }
    // }.schedule();
  }

  private void initialize(IProgressMonitor monitor) throws CoreException
  {
    if (WORKSPACE_STATE_FILE.exists())
    {
      try
      {
        descriptors = loadDescriptors(WORKSPACE_STATE_FILE);
      }
      catch (Exception ex)
      {
        TargletsCorePlugin.INSTANCE.log(ex);
      }
    }

    if (descriptors == null)
    {
      descriptors = new HashMap<String, TargletContainerDescriptor>();
    }

    // TODO must reenable this.
    // saveDescriptors(monitor);
    //
    // Set<String> workspaces = new HashSet<String>();
    // workspaces.add(WORKSPACE_LOCATION);
    //
    // Set<String> workingDigests = new HashSet<String>();
    // addWorkingDigests(workingDigests, descriptors);
    //
    // IProfileRegistry profileRegistry = AgentUtil.getProfileRegistry();
    // for (IProfile profile : profileRegistry.getProfiles())
    // {
    // try
    // {
    // String workspace = profile.getProperty(PROP_TARGLET_CONTAINER_WORKSPACE);
    // if (workspace != null)
    // {
    // if (workspaces.add(workspace))
    // {
    // File file = new File(workspace, WORKSPACE_STATE_RELATIVE_PATH);
    // if (file.exists())
    // {
    // Map<String, TargletContainerDescriptor> workspaceDescriptors = loadDescriptors(file);
    // addWorkingDigests(workingDigests, workspaceDescriptors);
    // }
    // }
    //
    // String digest = profile.getProperty(PROP_TARGLET_CONTAINER_DIGEST);
    // if (!workingDigests.contains(digest))
    // {
    // String profileID = profile.getProfileId();
    // profileRegistry.removeProfile(profileID);
    // TargletsCorePlugin.INSTANCE.log("Profile " + profileID + " for workspace " + workspace + " removed");
    // }
    // }
    // }
    // catch (Exception ex)
    // {
    // TargletsCorePlugin.INSTANCE.log(ex);
    // }
    // }
  }

  private void waitUntilInitialized(IProgressMonitor monitor) throws CoreException
  {
    try
    {
      for (;;)
      {
        TargletsCorePlugin.checkCancelation(monitor);

        if (initialized.await(100, TimeUnit.MILLISECONDS))
        {
          break;
        }
      }
    }
    catch (InterruptedException ex)
    {
      throw new RuntimeException(ex);
    }

    if (initializationProblem != null)
    {
      TargletsCorePlugin.INSTANCE.coreException(initializationProblem);
    }
  }

  public TargletContainerDescriptor getDescriptor(String id, IProgressMonitor monitor) throws CoreException
  {
    waitUntilInitialized(monitor);

    TargletContainerDescriptor descriptor = descriptors.get(id);
    if (descriptor == null)
    {
      File poolLocation = getDefaultPoolLocation();
      descriptor = new TargletContainerDescriptor(id, poolLocation);

      descriptors.put(id, descriptor);
      saveDescriptors(monitor);
    }

    return descriptor;
  }

  public void removeDescriptor(String id)
  {
    if (descriptors.remove(id) != null)
    {
      saveDescriptors(new NullProgressMonitor());
    }
  }

  public IProfileChangeRequest createProfileChangeRequest(ITargletContainerDescriptor descriptor, IProfile profile)
  {
    return descriptor.getBundlePool().getAgent().getPlanner().createChangeRequest(profile);
  }

  public void saveDescriptors(IProgressMonitor monitor)
  {
    // Remove unused descriptors
    Set<String> containerIDs = getContainerIDs(monitor);
    descriptors.keySet().retainAll(containerIDs);

    WORKSPACE_STATE_FILE.getParentFile().mkdirs();
    OutputStream outputStream = null;
    List<String> ids = new ArrayList<String>();

    try
    {
      outputStream = new FileOutputStream(WORKSPACE_STATE_FILE);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
      options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
      options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

      EObjectOutputStream stream = new BinaryResourceImpl.EObjectOutputStream(outputStream, options);
      stream.writeInt(descriptors.size());

      for (ITargletContainerDescriptor descriptor : descriptors.values())
      {
        ((TargletContainerDescriptor)descriptor).write(stream);

        String id = descriptor.getWorkingProfileID();
        if (id != null)
        {
          ids.add(id);
        }
      }

      stream.flush();
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.close(outputStream);
    }

    Collections.sort(ids);

    WORKSPACE_REFERENCER_FILE.getParentFile().mkdirs();
    IOUtil.writeLines(WORKSPACE_REFERENCER_FILE, null, ids);
  }

  private void initializeSync(IProgressMonitor monitor)
  {
    try
    {
      initialize(monitor);
    }
    catch (Throwable t)
    {
      initializationProblem = t;
      TargletsCorePlugin.INSTANCE.log(t);
    }
    finally
    {
      initialized.countDown();
    }
  }

  private static Map<String, TargletContainerDescriptor> loadDescriptors(File file)
  {
    FileInputStream in = null;

    try
    {
      in = new FileInputStream(file);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
      options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
      options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

      EObjectInputStream stream = new BinaryResourceImpl.EObjectInputStream(in, options);
      int size = stream.readInt();

      Map<String, TargletContainerDescriptor> result = new HashMap<String, TargletContainerDescriptor>();
      for (int i = 0; i < size; i++)
      {
        TargletContainerDescriptor descriptor = new TargletContainerDescriptor(stream);
        result.put(descriptor.getID(), descriptor);
      }

      return result;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.close(in);
    }
  }

  // private static void addWorkingDigests(Set<String> workingDigests, Map<String, TargletContainerDescriptor> descriptors)
  // {
  // for (TargletContainerDescriptor descriptor : descriptors.values())
  // {
  // workingDigests.add(descriptor.getWorkingDigest());
  // }
  // }

  public static Set<String> getContainerIDs(final IProgressMonitor monitor)
  {
    try
    {
      return TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Set<String>>()
      {
        public Set<String> run(ITargetPlatformService service) throws CoreException
        {
          Set<String> ids = new HashSet<String>();

          for (ITargetHandle targetHandle : service.getTargets(monitor))
          {
            try
            {
              ITargetDefinition target = targetHandle.getTargetDefinition();
              ITargetLocation[] targetLocations = target.getTargetLocations();
              if (targetLocations != null)
              {
                for (ITargetLocation location : targetLocations)
                {
                  if (location instanceof ITargletContainer)
                  {
                    ITargletContainer targletContainer = (ITargletContainer)location;
                    String id = targletContainer.getID();
                    ids.add(id);
                  }
                }
              }
            }
            catch (Exception ex)
            {
              TargletsCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
            }
          }

          return ids;
        }
      });
    }
    catch (CoreException ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

  public static ITargletContainer getContainer(final String id)
  {
    try
    {
      return TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<ITargletContainer>()
      {
        public ITargletContainer run(ITargetPlatformService service) throws CoreException
        {
          for (ITargetHandle targetHandle : service.getTargets(new NullProgressMonitor()))
          {
            try
            {
              ITargetDefinition target = targetHandle.getTargetDefinition();
              ITargetLocation[] targetLocations = target.getTargetLocations();
              if (targetLocations != null)
              {
                for (ITargetLocation location : targetLocations)
                {
                  if (location instanceof ITargletContainer)
                  {
                    final ITargletContainer targletContainer = (ITargletContainer)location;
                    if (targletContainer.getID().equals(id))
                    {
                      return targletContainer;
                    }
                  }
                }
              }
            }
            catch (Exception ex)
            {
              TargletsCorePlugin.INSTANCE.log(ex);
            }
          }

          return null;
        }
      });
    }
    catch (CoreException ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

  public static File getDefaultPoolLocation()
  {
    AgentManager agentManager = P2Util.getAgentManager();
    String client = TargletsCorePlugin.INSTANCE.getSymbolicName();

    BundlePool bundlePool = agentManager.getDefaultBundlePool(client);
    if (bundlePool == null)
    {
      File agentLocation = agentManager.getDefaultAgentLocation();
      Agent agent = agentManager.getAgent(agentLocation);
      if (agent == null)
      {
        // Create default agent if needed.
        agent = agentManager.addAgent(agentLocation);
      }

      for (BundlePool pool : agent.getBundlePools())
      {
        // Return any pool if one exists.
        return pool.getLocation();
      }

      // Create and return default pool if agent is empty.
      File poolLocation = new File(agentLocation, BundlePool.DEFAULT_NAME);
      bundlePool = agent.addBundlePool(poolLocation);
    }

    return bundlePool.getLocation();
  }

  public static synchronized TargletContainerDescriptorManager getInstance() throws CoreException
  {
    if (instance == null)
    {
      instance = new TargletContainerDescriptorManager();
    }

    return instance;
  }
}

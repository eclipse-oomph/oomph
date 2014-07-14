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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.p2.engine.DownloadManager;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitOperand;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitPhase;
import org.eclipse.equinox.internal.p2.engine.Phase;
import org.eclipse.equinox.internal.p2.engine.PhaseSet;
import org.eclipse.equinox.internal.p2.engine.phases.Collect;
import org.eclipse.equinox.internal.p2.engine.phases.Install;
import org.eclipse.equinox.internal.p2.engine.phases.Property;
import org.eclipse.equinox.internal.p2.engine.phases.Uninstall;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IEngine;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRequest;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Eike Stepper
 */
public final class TargletContainerManager
{
  public static final String WORKSPACE_LOCATION = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();

  private static final String WORKSPACE_STATE_RELATIVE_PATH = ".metadata/.plugins/" + TargletsCorePlugin.INSTANCE.getSymbolicName() //$NON-NLS-1$
      + "/targlet-containers.state"; //$NON-NLS-1$

  private static final String WORKSPACE_REFERENCER_RELATIVE_PATH = ".metadata/.plugins/" + TargletsCorePlugin.INSTANCE.getSymbolicName() //$NON-NLS-1$
      + "/targlet-profiles.txt"; //$NON-NLS-1$

  private static final File WORKSPACE_STATE_FILE = new File(WORKSPACE_LOCATION, WORKSPACE_STATE_RELATIVE_PATH);

  public static final File WORKSPACE_REFERENCER_FILE = new File(WORKSPACE_LOCATION, WORKSPACE_REFERENCER_RELATIVE_PATH);

  private static TargletContainerManager instance;

  private final CountDownLatch initialized = new CountDownLatch(1);

  private Throwable initializationProblem;

  private Map<String, TargletContainerDescriptor> descriptors;

  private static final String NATIVE_ARTIFACTS = "nativeArtifacts"; //$NON-NLS-1$

  private static final String NATIVE_TYPE = "org.eclipse.equinox.p2.native"; //$NON-NLS-1$

  private static final String PARM_OPERAND = "operand"; //$NON-NLS-1$

  private TargletContainerManager() throws CoreException
  {
    new Job("Initialize Targlet Containers")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
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

        return Status.OK_STATUS;
      }
    }.schedule();
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

    int xxx;
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
        if (monitor.isCanceled())
        {
          throw new OperationCanceledException();
        }

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

  public IProfileChangeRequest createProfileChangeRequest(TargletContainerDescriptor descriptor, IProfile profile)
  {
    return descriptor.getBundlePool().getAgent().getPlanner().createChangeRequest(profile);
  }

  public void planAndInstall(TargletContainerDescriptor descriptor, IProfileChangeRequest request, ProvisioningContext context, IProgressMonitor monitor)
      throws CoreException
  {
    BundlePool bundlePool = descriptor.getBundlePool();
    Agent agent = bundlePool.getAgent();

    IProvisioningPlan plan = agent.getPlanner().getProvisioningPlan(request, context, monitor);
    if (!plan.getStatus().isOK())
    {
      throw new ProvisionException(plan.getStatus());
    }

    IEngine engine = agent.getEngine();
    IPhaseSet phaseSet = createPhaseSet(bundlePool);

    @SuppressWarnings("restriction")
    IStatus status = org.eclipse.equinox.internal.provisional.p2.director.PlanExecutionHelper.executePlan(plan, engine, phaseSet, context, monitor);

    if (!status.isOK())
    {
      throw new ProvisionException(status);
    }
  }

  public void saveDescriptors(IProgressMonitor monitor)
  {
    // Remove unused descriptors
    Set<String> containerIDs = getContainerIDs(monitor);
    descriptors.keySet().retainAll(containerIDs);

    WORKSPACE_STATE_FILE.getParentFile().mkdirs();
    FileOutputStream out = null;

    try
    {
      out = new FileOutputStream(WORKSPACE_STATE_FILE);

      ObjectOutputStream stream = new ObjectOutputStream(out);
      stream.writeObject(descriptors);
      stream.close();
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.close(out);
    }

    List<String> ids = new ArrayList<String>();
    for (TargletContainerDescriptor descriptor : descriptors.values())
    {
      String id = descriptor.getWorkingProfileID();
      if (id != null)
      {
        ids.add(id);
      }
    }

    Collections.sort(ids);

    WORKSPACE_REFERENCER_FILE.getParentFile().mkdirs();
    IOUtil.writeLines(WORKSPACE_REFERENCER_FILE, null, ids);
  }

  private static Map<String, TargletContainerDescriptor> loadDescriptors(File file)
  {
    FileInputStream in = null;

    try
    {
      in = new FileInputStream(file);
      ObjectInputStream stream = new ObjectInputStream(in);

      @SuppressWarnings("unchecked")
      Map<String, TargletContainerDescriptor> result = (Map<String, TargletContainerDescriptor>)stream.readObject();
      return result;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    catch (ClassNotFoundException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.close(in);
    }
  }

  private static Set<String> getContainerIDs(IProgressMonitor monitor)
  {
    ITargetPlatformService service = null;

    try
    {
      service = TargletsCorePlugin.INSTANCE.getService(ITargetPlatformService.class);
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
              if (location instanceof TargletContainer)
              {
                TargletContainer targletContainer = (TargletContainer)location;
                String id = targletContainer.getID();
                ids.add(id);
              }
            }
          }
        }
        catch (Exception ex)
        {
          TargletsCorePlugin.INSTANCE.log(ex);
        }
      }

      return ids;
    }
    finally
    {
      TargletsCorePlugin.INSTANCE.ungetService(service);
    }
  }

  // private static void addWorkingDigests(Set<String> workingDigests, Map<String, TargletContainerDescriptor> descriptors)
  // {
  // for (TargletContainerDescriptor descriptor : descriptors.values())
  // {
  // workingDigests.add(descriptor.getWorkingDigest());
  // }
  // }

  public static IPhaseSet createPhaseSet(BundlePool bundlePool)
  {
    List<Phase> phases = new ArrayList<Phase>(4);
    phases.add(new Collect(100));
    phases.add(new Property(1));
    phases.add(new Uninstall(50, true));
    phases.add(new Install(50));
    phases.add(new CollectNativesPhase(bundlePool, 100));

    return new PhaseSet(phases.toArray(new Phase[phases.size()]));
  }

  public static File getDefaultPoolLocation()
  {
    AgentManager agentManager = P2Util.getAgentManager();
    String client = TargletsCorePlugin.INSTANCE.getSymbolicName();

    BundlePool bundlePool = agentManager.getDefaultBundlePool(client);
    if (bundlePool == null)
    {
      throw new P2Exception("No default bundle pool configured for " + client);
    }

    return bundlePool.getLocation();
  }

  public static synchronized TargletContainerManager getInstance() throws CoreException
  {
    if (instance == null)
    {
      instance = new TargletContainerManager();
    }

    return instance;
  }

  /**
   * @author Pascal Rapicault
   */
  private static final class CollectNativesPhase extends InstallableUnitPhase
  {
    private final BundlePool bundlePool;

    public CollectNativesPhase(BundlePool bundlePool, int weight)
    {
      super(NATIVE_ARTIFACTS, weight);
      this.bundlePool = bundlePool;
    }

    @Override
    protected List<ProvisioningAction> getActions(InstallableUnitOperand operand)
    {
      IInstallableUnit installableUnit = operand.second();
      if (installableUnit != null && installableUnit.getTouchpointType().getId().equals(NATIVE_TYPE))
      {
        List<ProvisioningAction> list = new ArrayList<ProvisioningAction>(1);
        list.add(new CollectNativesAction(bundlePool));
        return list;
      }

      return null;
    }

    @Override
    protected IStatus initializePhase(IProgressMonitor monitor, IProfile profile, Map<String, Object> parameters)
    {
      parameters.put(NATIVE_ARTIFACTS, new ArrayList<Object>());
      parameters.put(PARM_PROFILE, profile);
      return null;
    }

    @Override
    protected IStatus completePhase(IProgressMonitor monitor, IProfile profile, Map<String, Object> parameters)
    {
      @SuppressWarnings("unchecked")
      List<IArtifactRequest> artifactRequests = (List<IArtifactRequest>)parameters.get(NATIVE_ARTIFACTS);
      ProvisioningContext context = (ProvisioningContext)parameters.get(PARM_CONTEXT);
      IProvisioningAgent agent = (IProvisioningAgent)parameters.get(PARM_AGENT);
      DownloadManager downloadManager = new DownloadManager(context, agent);
      for (Iterator<IArtifactRequest> it = artifactRequests.iterator(); it.hasNext();)
      {
        downloadManager.add(it.next());
      }

      return downloadManager.start(monitor);
    }
  }

  /**
   * @author Pascal Rapicault
   */
  private static final class CollectNativesAction extends ProvisioningAction
  {
    private final BundlePool bundlePool;

    public CollectNativesAction(BundlePool bundlePool)
    {
      this.bundlePool = bundlePool;
    }

    @Override
    public IStatus execute(Map<String, Object> parameters)
    {
      InstallableUnitOperand operand = (InstallableUnitOperand)parameters.get(PARM_OPERAND);
      IInstallableUnit installableUnit = operand.second();
      if (installableUnit == null)
      {
        return Status.OK_STATUS;
      }

      try
      {
        Collection<?> toDownload = installableUnit.getArtifacts();
        if (toDownload == null)
        {
          return Status.OK_STATUS;
        }

        @SuppressWarnings("unchecked")
        List<IArtifactRequest> artifactRequests = (List<IArtifactRequest>)parameters.get(NATIVE_ARTIFACTS);

        IArtifactRepositoryManager manager = bundlePool.getAgent().getArtifactRepositoryManager();
        IFileArtifactRepository fileArtifactRepository = bundlePool.getFileArtifactRepository();

        for (Iterator<?> it = toDownload.iterator(); it.hasNext();)
        {
          IArtifactKey keyToDownload = (IArtifactKey)it.next();
          IArtifactRequest request = manager.createMirrorRequest(keyToDownload, fileArtifactRepository, null, null);
          artifactRequests.add(request);
        }
      }
      catch (Exception ex)
      {
        return TargletsCorePlugin.INSTANCE.getStatus(ex);
      }

      return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(Map<String, Object> parameters)
    {
      // Nothing to do for now
      return Status.OK_STATUS;
    }
  }
}

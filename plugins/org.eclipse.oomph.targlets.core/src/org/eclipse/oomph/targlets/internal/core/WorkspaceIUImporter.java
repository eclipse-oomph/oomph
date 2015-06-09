/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor;
import org.eclipse.oomph.targlets.core.TargletContainerEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.pde.TargetPlatformListener;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class WorkspaceIUImporter
{
  public static final WorkspaceIUImporter INSTANCE = new WorkspaceIUImporter();

  public static final Object WORKSPACE_IU_IMPORT_FAMILY = new Object();

  private final TargetPlatformListener listener = new TargetPlatformListener()
  {
    public void targetDefinitionActivated(ITargetDefinition oldTargetDefinition, ITargetDefinition newTargetDefinition) throws Exception
    {
      if (newTargetDefinition != null)
      {
        Job job = new ImportProjectsJob();
        job.schedule();
      }
    }
  };

  private WorkspaceIUImporter()
  {
  }

  public void updateWorkspace(final IProgressMonitor monitor) throws CoreException
  {
    TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Object>()
    {
      public Object run(ITargetPlatformService service) throws CoreException
      {
        Set<WorkspaceIUInfo> workspaceIUInfos = new HashSet<WorkspaceIUInfo>();
        List<Pair<ITargletContainer, ITargletContainerDescriptor>> targletContainerInfos = new ArrayList<Pair<ITargletContainer, ITargletContainerDescriptor>>();

        ITargetDefinition targetDefinition = TargetPlatformUtil.getActiveTargetDefinition(service);
        if (targetDefinition != null)
        {
          ITargetLocation[] targetLocations = targetDefinition.getTargetLocations();
          if (targetLocations != null)
          {
            for (ITargetLocation location : targetLocations)
            {
              if (location instanceof ITargletContainer)
              {
                ITargletContainer container = (ITargletContainer)location;
                ITargletContainerDescriptor descriptor = container.getDescriptor();
                if (descriptor != null && descriptor.getUpdateProblem() == null)
                {
                  targletContainerInfos.add(Pair.create(container, descriptor));

                  Collection<WorkspaceIUInfo> workingProjects = descriptor.getWorkingProjects();
                  if (workingProjects != null)
                  {
                    workspaceIUInfos.addAll(workingProjects);
                  }
                }
              }
            }
          }
        }

        Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> importResults;
        if (workspaceIUInfos.isEmpty())
        {
          importResults = new HashMap<WorkspaceIUInfo, ResourcesUtil.ImportResult>();
        }
        else
        {
          importResults = updateWorkspace(workspaceIUInfos, monitor);
        }

        for (Pair<ITargletContainer, ITargletContainerDescriptor> pair : targletContainerInfos)
        {
          ITargletContainer container = pair.getElement1();
          ITargletContainerDescriptor descriptor = pair.getElement2();

          Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> containerImportResults = new HashMap<WorkspaceIUInfo, ResourcesUtil.ImportResult>();
          Collection<WorkspaceIUInfo> workingProjects = descriptor.getWorkingProjects();
          if (workingProjects != null)
          {
            containerImportResults.putAll(importResults);
            containerImportResults.keySet().retainAll(workingProjects);
          }

          TargletContainerEvent event = new WorkspaceUpdateFinishedEvent(container, descriptor, containerImportResults);
          TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, monitor);
        }

        return null;
      }
    });
  }

  private Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> updateWorkspace(final Set<WorkspaceIUInfo> workspaceIUInfos, IProgressMonitor monitor)
      throws CoreException
  {
    final Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> importResults = new HashMap<WorkspaceIUInfo, ResourcesUtil.ImportResult>();

    ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
    {
      @SuppressWarnings("restriction")
      public void run(IProgressMonitor monitor) throws CoreException
      {
        // JDT lazily creates the external folder links,
        // but if many projects are imported, the resource deltas are processed early,
        // and then JDT creates ExternalPackageFragmentRoot instances for which linkedFolder.getLocation() is null,
        // which causes null pointer exceptions in that class' hashCode method, this.externalPath.hashCode(),
        // which completely screws up PDEs own notification handling
        // causing it to handle the same deltas more the once, making a mess of the PDE's model.
        // JDT doesn't create the links until the AutoBuildJob runs, which happens very late in the overall processing cycle.
        org.eclipse.jdt.internal.core.ExternalFoldersManager externalFoldersManager = org.eclipse.jdt.internal.core.ExternalFoldersManager
            .getExternalFoldersManager();
        for (IPluginModelBase pluginModelBase : PluginRegistry.getExternalModels())
        {
          String installLocation = pluginModelBase.getInstallLocation();
          if (installLocation != null)
          {
            if (new File(installLocation).isDirectory())
            {
              externalFoldersManager.createLinkFolder(new Path(installLocation), false, null);
            }
          }
        }

        for (WorkspaceIUInfo info : workspaceIUInfos)
        {
          ResourcesUtil.ImportResult result = info.importIntoWorkspace(monitor);
          importResults.put(info, result);
        }
      }
    }, monitor);

    return importResults;
  }

  void start()
  {
    TargetPlatformUtil.addListener(listener);
  }

  void stop()
  {
    TargetPlatformUtil.removeListener(listener);
  }

  /**
   * @author Eike Stepper
   */
  private static final class ImportProjectsJob extends Job
  {
    private ImportProjectsJob()
    {
      super("Import projects");
    }

    @Override
    public boolean belongsTo(Object family)
    {
      return family == WORKSPACE_IU_IMPORT_FAMILY;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
      try
      {
        INSTANCE.updateWorkspace(monitor);
        return Status.OK_STATUS;
      }
      catch (Exception ex)
      {
        return TargletsCorePlugin.INSTANCE.getStatus(ex);
      }
    }
  }
}

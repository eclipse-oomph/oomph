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
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.resources.ResourcesUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class TargletContainerEvent extends EventObject
{
  private static final long serialVersionUID = 1L;

  private final transient ITargletContainerDescriptor descriptor;

  public TargletContainerEvent(ITargletContainer source, ITargletContainerDescriptor descriptor)
  {
    super(source);
    this.descriptor = descriptor;
  }

  @Override
  public final ITargletContainer getSource()
  {
    return (ITargletContainer)super.getSource();
  }

  public final ITargletContainerDescriptor getDescriptor()
  {
    return descriptor;
  }

  /**
   * @author Eike Stepper
   */
  public static class IDChangedEvent extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    private final String oldID;

    public IDChangedEvent(ITargletContainer source, ITargletContainerDescriptor descriptor, String oldID)
    {
      super(source, descriptor);
      this.oldID = oldID;
    }

    public String getOldID()
    {
      return oldID;
    }

    @Override
    public String toString()
    {
      return "TargletsChangedEvent[source=" + getSource().getID() + ", oldID=" + oldID + "]";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class TargletsChangedEvent extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    public TargletsChangedEvent(ITargletContainer source, ITargletContainerDescriptor descriptor)
    {
      super(source, descriptor);
    }

    @Override
    public String toString()
    {
      return "TargletsChangedEvent[source=" + getSource().getID() + "]";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ProfileUpdateSucceededEvent extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    private final transient Profile profile;

    private final transient IProvisioningPlan provisioningPlan;

    private final transient IInstallableUnit artificialRoot;

    private final transient List<IMetadataRepository> metadataRepositories;

    private final transient Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos;

    public ProfileUpdateSucceededEvent(ITargletContainer source, ITargletContainerDescriptor descriptor, Profile profile, IInstallableUnit artificialRoot,
        List<IMetadataRepository> metadataRepositories, IProvisioningPlan provisioningPlan, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos)
    {
      super(source, descriptor);
      this.profile = profile;
      this.provisioningPlan = provisioningPlan;
      this.artificialRoot = artificialRoot;
      this.metadataRepositories = metadataRepositories;
      this.workspaceIUInfos = workspaceIUInfos;
    }

    public final Profile getProfile()
    {
      return profile;
    }

    public final IProvisioningPlan getProvisioningPlan()
    {
      return provisioningPlan;
    }

    public final IInstallableUnit getArtificialRoot()
    {
      return artificialRoot;
    }

    public final List<IMetadataRepository> getMetadataRepositories()
    {
      return metadataRepositories;
    }

    public final Map<IInstallableUnit, WorkspaceIUInfo> getWorkspaceIUInfos()
    {
      return workspaceIUInfos;
    }

    @Override
    public String toString()
    {
      return "ProfileUpdateSucceededEvent[source=" + getSource().getID() + ", metadataRepositories=" + metadataRepositories + ", profile=" + profile
          + ", provisioningPlan=" + provisioningPlan + ", workspaceIUInfos=" + workspaceIUInfos + "]";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ProfileUpdateFailedEvent extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    private final transient IStatus updateProblem;

    public ProfileUpdateFailedEvent(ITargletContainer source, ITargletContainerDescriptor descriptor, IStatus updateProblem)
    {
      super(source, descriptor);
      this.updateProblem = updateProblem;
    }

    public final IStatus getUpdateProblem()
    {
      return updateProblem;
    }

    @Override
    public String toString()
    {
      return "ProfileUpdateFailedEvent[source=" + getSource().getID() + ", updateProblem=" + updateProblem + "]";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class WorkspaceUpdateFinishedEvent extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    private final transient Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> importResults;

    public WorkspaceUpdateFinishedEvent(ITargletContainer source, ITargletContainerDescriptor descriptor,
        Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> importResults)
    {
      super(source, descriptor);
      this.importResults = Collections.unmodifiableMap(importResults);
    }

    public final Map<WorkspaceIUInfo, ResourcesUtil.ImportResult> getImportResults()
    {
      return importResults;
    }

    @Override
    public String toString()
    {
      return "WorkspaceUpdateFinishedEvent[source=" + getSource().getID() + ", importResults=" + importResults + "]";
    }
  }
}

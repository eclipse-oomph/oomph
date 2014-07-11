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
import org.eclipse.oomph.targlets.internal.core.TargletContainer;

import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.io.File;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class TargletContainerEvent extends EventObject
{
  private static final long serialVersionUID = 1L;

  public TargletContainerEvent(TargletContainer targletContainer)
  {
    super(targletContainer);
  }

  @Override
  public final TargletContainer getSource()
  {
    return (TargletContainer)super.getSource();
  }

  /**
   * @author Eike Stepper
   */
  public static class TargletContainerChanged extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    public TargletContainerChanged(TargletContainer targletContainer)
    {
      super(targletContainer);
    }

    @Override
    public String toString()
    {
      return "TargletContainerChanged[source=" + getSource().getID() + "]";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class TargletContainerUpdated extends TargletContainerEvent
  {
    private static final long serialVersionUID = 1L;

    private final transient Profile profile;

    private final transient List<IMetadataRepository> metadataRepositories;

    private final transient IProvisioningPlan provisioningPlan;

    private final transient Map<IInstallableUnit, File> projectLocations;

    public TargletContainerUpdated(TargletContainer source, Profile profile, List<IMetadataRepository> metadataRepositories,
        IProvisioningPlan provisioningPlan, Map<IInstallableUnit, File> projectLocations)
    {
      super(source);
      this.profile = profile;
      this.metadataRepositories = metadataRepositories;
      this.provisioningPlan = provisioningPlan;
      this.projectLocations = projectLocations;
    }

    public Profile getProfile()
    {
      return profile;
    }

    public List<IMetadataRepository> getMetadataRepositories()
    {
      return metadataRepositories;
    }

    public IProvisioningPlan getProvisioningPlan()
    {
      return provisioningPlan;
    }

    public Map<IInstallableUnit, File> getProjectLocations()
    {
      return projectLocations;
    }

    @Override
    public String toString()
    {
      return "TargletContainerUpdated[source=" + getSource().getID() + ", metadataRepositories=" + metadataRepositories + ", profile=" + profile
          + ", provisioningPlan=" + provisioningPlan + ", projectLocations=" + projectLocations + "]";
    }
  }
}

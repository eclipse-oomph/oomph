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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IEngine;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface Agent extends AgentManagerElement, ProfileContainer
{
  public Set<File> getBundlePoolLocations();

  public Collection<BundlePool> getBundlePools();

  public BundlePool getBundlePool(File location);

  public BundlePool addBundlePool(File location);

  public void refreshBundlePools(IProgressMonitor monitor);

  public Set<String> getAllProfileIDs();

  public Collection<Profile> getAllProfiles();

  public Profile getCurrentProfile();

  public void refreshProfiles(IProgressMonitor monitor);

  public IProvisioningAgent getProvisioningAgent();

  public IMetadataRepositoryManager getMetadataRepositoryManager();

  public IArtifactRepositoryManager getArtifactRepositoryManager();

  public IProfileRegistry getProfileRegistry();

  public IEngine getEngine();

  public IPlanner getPlanner();

  public void flushRepositoryCaches();
}

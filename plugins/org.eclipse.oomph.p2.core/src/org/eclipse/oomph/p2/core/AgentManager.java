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
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.P2Exception;

import org.eclipse.core.runtime.IProgressMonitor;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface AgentManager
{
  public static final String PROP_BUNDLE_POOL_LOCATION = "oomph.p2.pool";

  public Agent getCurrentAgent();

  public File getDefaultAgentLocation();

  public Set<File> getAgentLocations();

  public Collection<Agent> getAgents();

  public Agent getAgent(File location);

  public Agent addAgent(File location);

  public void refreshAgents(IProgressMonitor monitor);

  public Collection<BundlePool> getBundlePools();

  public BundlePool getBundlePool(File location) throws P2Exception;

  public BundlePool getDefaultBundlePool(String client) throws P2Exception;

  public void setDefaultBundlePool(String client, BundlePool bundlePool);
}

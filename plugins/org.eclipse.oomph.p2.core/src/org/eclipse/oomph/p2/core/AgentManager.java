/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
  /**
   * Specifies the location of the shared pool as a system property.
   * The special value "@none" indicates that no shared pool should be used.
   */
  public static final String PROP_BUNDLE_POOL_LOCATION = "oomph.p2.pool";

  public static final String BUNDLE_POOL_LOCATION_NONE = "@none";

  public static final String PROP_FLUSH = "oomph.p2.flush";

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

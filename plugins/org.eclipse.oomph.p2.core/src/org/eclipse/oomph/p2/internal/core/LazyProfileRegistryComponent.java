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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.spi.IAgentServiceFactory;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.osgi.storage.StorageUtil;

import java.io.File;

/**
 * Instantiates default instances of {@link IProfileRegistry}.
 */
@SuppressWarnings("restriction")
public class LazyProfileRegistryComponent implements IAgentServiceFactory
{
  public Object createService(IProvisioningAgent agent)
  {
    IAgentLocation location = (IAgentLocation)agent.getService(IAgentLocation.SERVICE_NAME);
    File directory = LazyProfileRegistry.getDefaultRegistryDirectory(location);
    boolean canWrite = StorageUtil.canWrite(directory);
    SimpleProfileRegistry registry = canWrite ? new LazyProfileRegistry(agent, directory) : new SimpleProfileRegistry(agent, directory);
    registry.setEventBus((IProvisioningEventBus)agent.getService(IProvisioningEventBus.SERVICE_NAME));
    return registry;
  }
}

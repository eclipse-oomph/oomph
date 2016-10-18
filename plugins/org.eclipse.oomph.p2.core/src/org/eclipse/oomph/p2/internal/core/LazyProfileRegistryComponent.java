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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.spi.IAgentServiceFactory;
import org.eclipse.equinox.p2.engine.IProfileRegistry;

import java.io.File;

/**
 * Instantiates default instances of {@link IProfileRegistry}.
 *
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class LazyProfileRegistryComponent implements IAgentServiceFactory
{
  public static final String PROP_LAZY_PROFILE_REGISTRY = "oomph.p2.lazy.profile.registry";

  public Object createService(IProvisioningAgent agent)
  {
    IAgentLocation location = (IAgentLocation)agent.getService(IAgentLocation.SERVICE_NAME);
    File directory = LazyProfileRegistry.getDefaultRegistryDirectory(location);

    SimpleProfileRegistry registry = null;

    boolean isLazySupported = !"false".equals(PropertiesUtil.getProperty(PROP_LAZY_PROFILE_REGISTRY));
    if (isLazySupported)
    {
      try
      {
        registry = new LazyProfileRegistry(agent, directory, IOUtil.canWriteFolder(directory));
      }
      catch (Throwable ex)
      {
        P2CorePlugin.INSTANCE.log(ex);
      }
    }

    if (registry == null)
    {
      registry = new SimpleProfileRegistry(agent, directory);
    }

    registry.setEventBus((IProvisioningEventBus)agent.getService(IProvisioningEventBus.SERVICE_NAME));
    return registry;
  }
}

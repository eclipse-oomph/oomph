package org.eclipse.oomph.p2.internal.core;

import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.spi.IAgentServiceFactory;
import org.eclipse.equinox.p2.engine.IProfileRegistry;

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
    LazyProfileRegistry registry = new LazyProfileRegistry(agent, directory);
    registry.setEventBus((IProvisioningEventBus)agent.getService(IProvisioningEventBus.SERVICE_NAME));
    return registry;
  }
}

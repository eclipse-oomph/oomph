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

import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.spi.IAgentServiceFactory;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.osgi.storage.StorageUtil;

import java.io.File;
import java.io.IOException;

/**
 * Instantiates default instances of {@link IProfileRegistry}.
 */
@SuppressWarnings("restriction")
public class LazyProfileRegistryComponent implements IAgentServiceFactory
{
  public static final String PROP_LAZY_PROFILE_REGISTRY = "oomph.p2.lazy.profile.registry";

  public Object createService(IProvisioningAgent agent)
  {
    IAgentLocation location = (IAgentLocation)agent.getService(IAgentLocation.SERVICE_NAME);
    File directory = LazyProfileRegistry.getDefaultRegistryDirectory(location);

    boolean isLazySupported = !"false".equals(PropertiesUtil.getProperty(PROP_LAZY_PROFILE_REGISTRY)) && OsgiHelper.canWrite(directory);

    SimpleProfileRegistry registry;
    if (isLazySupported)
    {
      registry = new LazyProfileRegistry(agent, directory);
    }
    else
    {
      registry = new SimpleProfileRegistry(agent, directory);
    }

    registry.setEventBus((IProvisioningEventBus)agent.getService(IProvisioningEventBus.SERVICE_NAME));
    return registry;
  }

  private static class OsgiHelper
  {
    public static boolean canWrite(File installDir)
    {
      try
      {
        return StorageUtil.canWrite(installDir);
      }
      catch (NoClassDefFoundError ex)
      {
        if (!installDir.canWrite() || !installDir.isDirectory())
        {
          return false;
        }

        File fileTest = null;
        try
        {
          fileTest = File.createTempFile("test", ".dll", installDir);
        }
        catch (IOException e)
        {
          return false;
        }
        finally
        {
          if (fileTest != null)
          {
            fileTest.delete();
          }
        }

        return true;
      }
    }
  }
}

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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.TargletEvent;
import org.eclipse.oomph.targlets.core.TargletListener;
import org.eclipse.oomph.util.ConcurrentArray;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class TargletListenerRegistryImpl implements TargletListener.Registry
{
  public static final TargletListenerRegistryImpl INSTANCE = new TargletListenerRegistryImpl();

  private final ConcurrentArray<TargletListener> listeners = new ConcurrentArray<TargletListener>()
  {
    @Override
    protected TargletListener[] newArray(int length)
    {
      return new TargletListener[length];
    }
  };

  private final ExtensionPointHandler extensionPointHandler = new ExtensionPointHandler();

  private TargletListenerRegistryImpl()
  {
  }

  public void start() throws Exception
  {
    extensionPointHandler.start();
  }

  public void stop() throws Exception
  {
    extensionPointHandler.stop();
    listeners.clear();
  }

  public void addListener(TargletListener listener)
  {
    listeners.add(listener);
  }

  public void removeListener(TargletListener listener)
  {
    listeners.remove(listener);
  }

  public void notifyProfileUpdate(Targlet targlet, Profile profile, List<IMetadataRepository> metadataRepositories, IProvisioningPlan provisioningPlan,
      Map<IInstallableUnit, File> projectLocations)
  {
    TargletEvent event = new TargletEvent.ProfileUpdate(targlet, profile, metadataRepositories, provisioningPlan, projectLocations);

    for (TargletListener listener : listeners.get())
    {
      try
      {
        listener.handleTargletEvent(event);
      }
      catch (Exception ex)
      {
        TargletsCorePlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ExtensionPointHandler implements IRegistryEventListener
  {
    public static final String EXTENSION_POINT = "org.eclipse.oomph.targlets.core.targletListeners";

    private final Map<IConfigurationElement, TargletListener> listeners = new HashMap<IConfigurationElement, TargletListener>();

    public ExtensionPointHandler()
    {
    }

    public void start() throws Exception
    {
      IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
      for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT))
      {
        added(configurationElement);
      }

      extensionRegistry.addListener(this, EXTENSION_POINT);
    }

    public void stop() throws Exception
    {
      Platform.getExtensionRegistry().removeListener(this);

      for (TargletListener listener : listeners.values())
      {
        INSTANCE.removeListener(listener);
      }

      listeners.clear();
    }

    public void added(IExtensionPoint[] extensionPoints)
    {
      // Do nothing
    }

    public void removed(IExtensionPoint[] extensionPoints)
    {
      // Do nothing
    }

    public void added(IExtension[] extensions)
    {
      for (IExtension extension : extensions)
      {
        for (IConfigurationElement configurationElement : extension.getConfigurationElements())
        {
          added(configurationElement);
        }
      }
    }

    public void removed(IExtension[] extensions)
    {
      for (IExtension extension : extensions)
      {
        for (IConfigurationElement configurationElement : extension.getConfigurationElements())
        {
          removed(configurationElement);
        }
      }
    }

    private void added(IConfigurationElement configurationElement)
    {
      try
      {
        TargletListener listener = (TargletListener)configurationElement.createExecutableExtension("class");
        listeners.put(configurationElement, listener);
        INSTANCE.addListener(listener);
      }
      catch (Exception ex)
      {
        TargletsCorePlugin.INSTANCE.log(ex);
      }
    }

    private void removed(IConfigurationElement configurationElement)
    {
      TargletListener listener = listeners.remove(configurationElement);
      if (listener != null)
      {
        INSTANCE.removeListener(listener);
      }
    }
  }
}

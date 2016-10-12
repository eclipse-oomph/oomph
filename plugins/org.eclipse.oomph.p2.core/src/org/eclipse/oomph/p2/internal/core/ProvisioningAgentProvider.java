/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks- initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.internal.registry.osgi.OSGIUtils;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.internal.p2.core.DefaultAgentProvider;
import org.eclipse.equinox.p2.core.IProvisioningAgent;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author Ed Merks
 */
@SuppressWarnings("restriction")
public class ProvisioningAgentProvider extends DefaultAgentProvider
{
  @Override
  public void activate(BundleContext context)
  {
    super.activate(org.eclipse.equinox.internal.p2.core.Activator.getContext());
  }

  @Override
  public IProvisioningAgent createAgent(URI location)
  {
    // The location is null if this is the current self agent.
    if (location == null)
    {
      // The agent location created by p2 should be non-null at this point.
      if (org.eclipse.equinox.internal.p2.core.Activator.agentDataLocation != null)
      {
        URI rootLocation = org.eclipse.equinox.internal.p2.core.Activator.agentDataLocation.getRootLocation();
        if (rootLocation != null)
        {
          // There should be a path.
          String path = rootLocation.getPath();
          if (path != null)
          {
            // If the folder corresponding to this path is not writable, then p2 has computed a bad agent location.
            // This happens in the case of an install that's using a shared pool.
            File folder = new File(path);
            if (!IOUtil.canWriteFolder(folder))
            {
              try
              {
                // We'll compute the path relative to the configuration area.
                // For a read-only installation, this will be a surrogate installation in ~/.eclipse.
                URI defaultLocation = URIUtil.append(
                    URIUtil.fromString(org.eclipse.equinox.internal.p2.core.Activator.getContext().getProperty(OSGIUtils.PROP_CONFIG_AREA) + "../p2"), "");

                // Replace the bad agent location with this good one.
                ReflectUtil.setValue("location", org.eclipse.equinox.internal.p2.core.Activator.agentDataLocation, defaultLocation);
              }
              catch (URISyntaxException ex)
              {
                // We don't expect an ill-formed URI.
              }
            }
          }
        }
      }
    }
    else
    {
      // P2 will always creates a path that assumes there agent is nested in the installation, relative to the configuration folder there.
      String path = location.getRawPath();
      if (path != null && path.endsWith("/../p2"))
      {
        // If there is no directory existing at this path, then likely we have a shared pool installation.
        File folder = new File(path);
        if (!folder.isDirectory())
        {
          // Check if there is a config.ini existing in the configuration folder.
          File configIniFile = new File(folder.getParentFile().getParentFile(), "config.ini");
          if (configIniFile.isFile())
          {
            // Load the properties from it and fetch the data area property.
            // That's where the shared agent is really definitely located.
            Map<String, String> properties = PropertiesUtil.loadProperties(configIniFile);
            String dataArea = properties.get("eclipse.p2.data.area");
            if (dataArea != null)
            {
              // If will generally be a file URI, but double check just in case.
              if (dataArea.startsWith("file:/"))
              {
                // The path should generally correspond to an existing folder.
                File dataAreaFolder = new File(dataArea.substring("file:/".length() - 1));
                if (dataAreaFolder.isDirectory())
                {
                  try
                  {
                    // This is our actual existing shared agent location.
                    location = URIUtil.fromString(AgentImpl.createFileURI(dataAreaFolder));
                  }
                  catch (URISyntaxException ex)
                  {
                    // Ignore.
                  }
                }
              }
            }
          }
        }
      }
    }

    // Call super with the potentially corrected location.
    return super.createAgent(location);
  }
}

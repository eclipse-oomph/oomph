/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.provider.BaseEditPlugin;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class SetupCorePlugin extends OomphPlugin
{
  private static final String DEFAULT_UPDATE_URL = "https://download.eclipse.org/oomph/updates/milestone/latest"; //$NON-NLS-1$

  public static final String UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_UPDATE_URL, DEFAULT_UPDATE_URL).replace('\\', '/');

  public static final SetupCorePlugin INSTANCE = new SetupCorePlugin();

  private static Implementation plugin;

  public SetupCorePlugin()
  {
    super(new ResourceLocator[] { BaseEditPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public Map<String, String> getImplicitWorkspaceVariables()
  {
    return getImplicitVariables(getInstancePreferences().node("variables")); //$NON-NLS-1$
  }

  public void setImplicitWorkspaceVariables(Map<String, String> implicitVariables)
  {
    setImplicitVariables(getInstancePreferences().node("variables"), implicitVariables); //$NON-NLS-1$
  }

  public Map<String, String> getImplicitInstallationVariables()
  {
    return getImplicitVariables(getConfigurationPreferences().node("variables")); //$NON-NLS-1$
  }

  public void setImplicitInstallationVariables(Map<String, String> implicitVariables)
  {
    setImplicitVariables(getConfigurationPreferences().node("variables"), implicitVariables); //$NON-NLS-1$
  }

  private static Map<String, String> getImplicitVariables(Preferences variables)
  {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();

    try
    {
      for (String key : variables.keys())
      {
        String value = variables.get(key, null);
        if (value != null)
        {
          result.put(key, value);
        }
      }
    }
    catch (BackingStoreException ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }
    return result;
  }

  private static void setImplicitVariables(Preferences variables, Map<String, String> implicitVariables)
  {
    try
    {
      for (String key : variables.keys())
      {
        variables.remove(key);
      }

      for (Map.Entry<String, String> entry : implicitVariables.entrySet())
      {
        variables.put(entry.getKey(), entry.getValue());
      }
      variables.flush();
    }
    catch (BackingStoreException ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipsePlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      StringFilterRegistry.INSTANCE.initContributions();
    }
  }
}

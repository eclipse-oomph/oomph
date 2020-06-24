/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.presentation.sync;

import org.eclipse.oomph.projectconfig.presentation.ProjectConfigEditorPlugin;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Eike Stepper
 */
public final class ProjectConfigSynchronizerPreferences
{
  private static final String PREFERENCES_NODE_NAME = ProjectConfigEditorPlugin.INSTANCE.getSymbolicName();

  private static final Preferences PREFERENCES = InstanceScope.INSTANCE.getNode(PREFERENCES_NODE_NAME);

  private static final String CONFIGURATION_MANAGEMENT = "configurationManagement"; //$NON-NLS-1$

  private static final String CONFIGURATION_VALIDATION = "configurationValidation"; //$NON-NLS-1$

  private static final String PROPERTY_MODIFICATION_HANDLING = "propertyModificationHandling"; //$NON-NLS-1$

  private static final String EDIT_KEY = "edit"; //$NON-NLS-1$

  private static final String PROPAGATE_KEY = "propagate"; //$NON-NLS-1$

  private ProjectConfigSynchronizerPreferences()
  {
  }

  public static void flush()
  {
    try
    {
      PREFERENCES.flush();
    }
    catch (BackingStoreException ex)
    {
      ProjectConfigEditorPlugin.INSTANCE.log(ex);
    }
  }

  public static boolean isConfigurationManagementAutomatic()
  {
    return "automatic".equals(getPreference(CONFIGURATION_MANAGEMENT, "manual")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static void setConfigurationManagementAutomatic(boolean automatic)
  {
    setPreference(CONFIGURATION_MANAGEMENT, automatic ? "automatic" : "manual"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static boolean isConfigurationValidationPrompt()
  {
    return "prompt".equals(getPreference(CONFIGURATION_VALIDATION, "prompt")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static void setConfigurationValidationPrompt(boolean prompt)
  {
    setPreference(CONFIGURATION_VALIDATION, prompt ? "prompt" : "ignore"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static PropertyModificationHandling getPropertyModificationHandling()
  {
    return PropertyModificationHandling.valueOf(getPreference(PROPERTY_MODIFICATION_HANDLING, PropertyModificationHandling.PROMPT.toString()));
  }

  public static void setPropertyModificationHandling(PropertyModificationHandling propertyModificationHandling)
  {
    setPreference(PROPERTY_MODIFICATION_HANDLING, propertyModificationHandling.toString());
  }

  public static boolean isPropagate()
  {
    return PREFERENCES.getBoolean(ProjectConfigSynchronizerPreferences.PROPAGATE_KEY, false);
  }

  public static void setPropagate(boolean propagate)
  {
    PREFERENCES.putBoolean(ProjectConfigSynchronizerPreferences.PROPAGATE_KEY, propagate);

  }

  public static boolean isEdit()
  {
    return PREFERENCES.getBoolean(ProjectConfigSynchronizerPreferences.EDIT_KEY, false);
  }

  public static void setEdit(boolean edit)
  {
    PREFERENCES.putBoolean(ProjectConfigSynchronizerPreferences.EDIT_KEY, edit);

  }

  private static String getPreference(String key, String defaultValue)
  {
    return Platform.getPreferencesService().getString(PREFERENCES_NODE_NAME, key, defaultValue, new IScopeContext[] { InstanceScope.INSTANCE });
  }

  private static void setPreference(String key, String value)
  {
    PREFERENCES.put(key, value);
  }

  public enum PropertyModificationHandling
  {
    OVERWRITE, PROPAGATE, PROMPT
  }
}

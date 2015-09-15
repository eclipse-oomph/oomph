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
package org.eclipse.oomph.internal.util;

import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author Eike Stepper
 */
public final class UtilPlugin extends OomphPlugin
{
  public static final UtilPlugin INSTANCE = new UtilPlugin();

  private static Implementation plugin;

  public static final ToggleStateAccessor DEFAULT_TOGGLE_STATE_ACCESSOR = CommonPlugin.IS_ECLIPSE_RUNNING ? new ToggleStateAccessor()
  {
    private final IEclipsePreferences preferences = Platform.getPreferencesService().getRootNode();

    private String getPreferenceKey(String id)
    {
      return "/instance/org.eclipse.ui.workbench//org.eclipse.ui.commands/state/" + id + "/org.eclipse.ui.commands.toggleState";
    }

    public void setEnabled(String id, boolean enabled)
    {
      preferences.putBoolean(getPreferenceKey(id), enabled);
    }

    public boolean isEnabled(String id)
    {
      return preferences.getBoolean(getPreferenceKey(id), false);
    }
  } : null;

  private static ToggleStateAccessor toggleStateAccessor = DEFAULT_TOGGLE_STATE_ACCESSOR;

  public UtilPlugin()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static ToggleStateAccessor getToggleStateAccessor()
  {
    return toggleStateAccessor;
  }

  public static void setToggleStateAccessor(ToggleStateAccessor toggleStateAccessor)
  {
    UtilPlugin.toggleStateAccessor = toggleStateAccessor;
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
  }

  /**
   * @author Eike Stepper
   */
  public interface ToggleStateAccessor
  {
    public boolean isEnabled(String id);

    public void setEnabled(String id, boolean enabled);
  }
}

/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.presentation;

import org.eclipse.oomph.base.provider.BaseEditPlugin;
import org.eclipse.oomph.predicates.provider.PredicatesEditPlugin;
import org.eclipse.oomph.preferences.provider.PreferencesEditPlugin;
import org.eclipse.oomph.projectconfig.presentation.sync.ProjectConfigSynchronizer;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Project Config editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class ProjectConfigEditorPlugin extends EMFPlugin
{
  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final ProjectConfigEditorPlugin INSTANCE = new ProjectConfigEditorPlugin();

  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static Implementation plugin;

  /**
   * Create the instance.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectConfigEditorPlugin()
  {
    super(new ResourceLocator[] { PredicatesEditPlugin.INSTANCE, PreferencesEditPlugin.INSTANCE, BaseEditPlugin.INSTANCE, });
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  public static Implementation getPlugin()
  {
    return plugin;
  }

  /**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static class Implementation extends EclipseUIPlugin
  {
    private static ProjectConfigSynchronizer projectConfigSynchronizer;

    public static void setProjectConfigSynchronizer(ProjectConfigSynchronizer projectConfigSynchronizer)
    {
      Implementation.projectConfigSynchronizer = projectConfigSynchronizer;
    }

    /**
     * Creates an instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Implementation()
    {
      super();

      // Remember the static instance.
      //
      plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      projectConfigSynchronizer.stop();

      super.stop(context);
    }

    public void update()
    {
      projectConfigSynchronizer.update();
    }
  }
}

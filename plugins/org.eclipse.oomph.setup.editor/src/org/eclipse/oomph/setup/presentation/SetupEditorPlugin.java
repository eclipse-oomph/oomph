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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.p2.provider.P2EditPlugin;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * This is the central singleton for the Setup editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated not
 */
public final class SetupEditorPlugin extends OomphUIPlugin
{
  public static final String PLUGIN_ID = "org.eclipse.oomph.setup.editor";

  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final SetupEditorPlugin INSTANCE = new SetupEditorPlugin();

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
   * @generated NOT
   */
  public SetupEditorPlugin()
  {
    super(new ResourceLocator[] { SetupUIPlugin.INSTANCE, P2EditPlugin.INSTANCE, });
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
  }

  public static IStatus getStatus(Throwable t)
  {
    if (t instanceof CoreException)
    {
      CoreException coreException = (CoreException)t;
      return coreException.getStatus();
    }

    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(IStatus.ERROR, INSTANCE.getSymbolicName(), msg, t);
  }

}

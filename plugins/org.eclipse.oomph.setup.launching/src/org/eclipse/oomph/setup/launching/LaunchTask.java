/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.launching;

import org.eclipse.oomph.setup.SetupTask;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Launch Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.launching.LaunchTask#getLauncher <em>Launcher</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.launching.LaunchingPackage#getLaunchTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface LaunchTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Launcher</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Launcher</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Launcher</em>' attribute.
   * @see #setLauncher(String)
   * @see org.eclipse.oomph.setup.launching.LaunchingPackage#getLaunchTask_Launcher()
   * @model required="true"
   * @generated
   */
  String getLauncher();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.launching.LaunchTask#getLauncher <em>Launcher</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Launcher</em>' attribute.
   * @see #getLauncher()
   * @generated
   */
  void setLauncher(String value);

} // LaunchTask

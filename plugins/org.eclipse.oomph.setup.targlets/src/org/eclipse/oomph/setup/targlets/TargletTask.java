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
package org.eclipse.oomph.setup.targlets;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.targlets.Targlet;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Targlet Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTarglets <em>Targlets</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargletURIs <em>Targlet UR Is</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.targlets.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.targlets.feature.group org.eclipse.oomph.setup.targlets.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface TargletTask extends SetupTask
{

  /**
   * Returns the value of the '<em><b>Targlets</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.targlets.Targlet}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Targlets</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Targlets</em>' containment reference list.
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_Targlets()
   * @model containment="true"
   *        extendedMetaData="name='targlet'"
   * @generated
   */
  EList<Targlet> getTarglets();

  /**
   * Returns the value of the '<em><b>Targlet UR Is</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Targlet UR Is</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Targlet UR Is</em>' attribute list.
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_TargletURIs()
   * @model extendedMetaData="name='targletURI'"
   * @generated
   */
  EList<String> getTargletURIs();
} // TargletTask

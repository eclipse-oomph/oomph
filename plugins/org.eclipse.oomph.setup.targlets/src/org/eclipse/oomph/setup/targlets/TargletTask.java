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
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTarglets <em>Targlets</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargletURIs <em>Targlet UR Is</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getOperatingSystem <em>Operating System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getWindowingSystem <em>Windowing System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getArchitecture <em>Architecture</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getLocale <em>Locale</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider text='Targlets'"
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

  /**
   * Returns the value of the '<em><b>Operating System</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operating System</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operating System</em>' attribute.
   * @see #setOperatingSystem(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_OperatingSystem()
   * @model
   * @generated
   */
  String getOperatingSystem();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getOperatingSystem <em>Operating System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operating System</em>' attribute.
   * @see #getOperatingSystem()
   * @generated
   */
  void setOperatingSystem(String value);

  /**
   * Returns the value of the '<em><b>Windowing System</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Windowing System</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Windowing System</em>' attribute.
   * @see #setWindowingSystem(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_WindowingSystem()
   * @model
   * @generated
   */
  String getWindowingSystem();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getWindowingSystem <em>Windowing System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Windowing System</em>' attribute.
   * @see #getWindowingSystem()
   * @generated
   */
  void setWindowingSystem(String value);

  /**
   * Returns the value of the '<em><b>Architecture</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Architecture</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Architecture</em>' attribute.
   * @see #setArchitecture(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_Architecture()
   * @model
   * @generated
   */
  String getArchitecture();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getArchitecture <em>Architecture</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Architecture</em>' attribute.
   * @see #getArchitecture()
   * @generated
   */
  void setArchitecture(String value);

  /**
   * Returns the value of the '<em><b>Locale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Locale</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Locale</em>' attribute.
   * @see #setLocale(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_Locale()
   * @model
   * @generated
   */
  String getLocale();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getLocale <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Locale</em>' attribute.
   * @see #getLocale()
   * @generated
   */
  void setLocale(String value);
} // TargletTask

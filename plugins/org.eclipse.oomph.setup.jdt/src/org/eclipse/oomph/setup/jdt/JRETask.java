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
package org.eclipse.oomph.setup.jdt;

import org.eclipse.oomph.setup.SetupTask;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>JRE Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.1' type='JRE' label='JRE 1.1 Location' description='The location of a JDK or JRE compatible with Java 1.1.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.2' type='JRE' label='JRE 1.2 Location' description='The location of a JDK or JRE compatible with Java 1.2.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.3' type='JRE' label='JRE 1.3 Location' description='The location of a JDK or JRE compatible with Java 1.3.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.4' type='JRE' label='JRE 1.4 Location' description='The location of a JDK or JRE compatible with Java 1.4.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.5' type='JRE' label='JRE 1.5 Location' description='The location of a JDK or JRE compatible with Java 1.5.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.6' type='JRE' label='JRE 1.6 Location' description='The location of a JDK or JRE compatible with Java 1.6.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.7' type='JRE' label='JRE 1.7 Location' description='The location of a JDK or JRE compatible with Java 1.7.'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.8' type='JRE' label='JRE 1.8 Location' description='The location of a JDK or JRE compatible with Java 1.8.'"
 * @generated
 */
public interface JRETask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #setVersion(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_Version()
   * @model required="true"
   * @generated
   */
  String getVersion();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #getVersion()
   * @generated
   */
  void setVersion(String value);

  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see #setLocation(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_Location()
   * @model required="true"
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

} // JRETask

/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getVMInstallType <em>VM Install Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#isExecutionEnvironmentDefault <em>Execution Environment Default</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getVMArguments <em>VM Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRETask#getJRELibraries <em>JRE Libraries</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.1' type='JRE' label='JRE 1.1 Location' description='The location of a JDK or JRE compatible with Java 1.1.' version='JRE-1.1'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.2' type='JRE' label='JRE 1.2 Location' description='The location of a JDK or JRE compatible with Java 1.2.' version='J2SE-1.2'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.3' type='JRE' label='JRE 1.3 Location' description='The location of a JDK or JRE compatible with Java 1.3.' version='J2SE-1.3'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.4' type='JRE' label='JRE 1.4 Location' description='The location of a JDK or JRE compatible with Java 1.4.' version='J2SE-1.4'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.5' type='JRE' label='JRE 1.5 Location' description='The location of a JDK or JRE compatible with Java 1.5.' version='J2SE-1.5'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.6' type='JRE' label='JRE 1.6 Location' description='The location of a JDK or JRE compatible with Java 1.6.' version='JavaSE-1.6'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.7' type='JRE' label='JRE 1.7 Location' description='The location of a JDK or JRE compatible with Java 1.7.' version='JavaSE-1.7'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-1.8' type='JRE' label='JRE 1.8 Location' description='The location of a JDK or JRE compatible with Java 1.8.' version='JavaSE-1.8'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-9' type='JRE' label='JRE 9 Location' description='The location of a JDK or JRE compatible with Java 9.' version='JavaSE-9'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-10' type='JRE' label='JRE 10 Location' description='The location of a JDK or JRE compatible with Java 10.' version='JavaSE-10'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-11' type='JRE' label='JRE 11 Location' description='The location of a JDK or JRE compatible with Java 11.' version='JavaSE-11'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-12' type='JRE' label='JRE 12 Location' description='The location of a JDK or JRE compatible with Java 12.' version='JavaSE-12'"
 *        annotation="http://www.eclipse.org/oomph/setup/Variable name='jre.location-13' type='JRE' label='JRE 13 Location' description='The location of a JDK or JRE compatible with Java 13.' version='JavaSE-13'"
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

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>VM Install Type</b></em>' attribute.
   * The default value is <code>"org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>VM Install Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>VM Install Type</em>' attribute.
   * @see #setVMInstallType(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_VMInstallType()
   * @model default="org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType" required="true"
   *        extendedMetaData="kind='attribute' name='vmInstallType'"
   * @generated
   */
  String getVMInstallType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#getVMInstallType <em>VM Install Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>VM Install Type</em>' attribute.
   * @see #getVMInstallType()
   * @generated
   */
  void setVMInstallType(String value);

  /**
   * Returns the value of the '<em><b>Execution Environment Default</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Execution Environment Default</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Execution Environment Default</em>' attribute.
   * @see #setExecutionEnvironmentDefault(boolean)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_ExecutionEnvironmentDefault()
   * @model default="true"
   * @generated
   */
  boolean isExecutionEnvironmentDefault();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#isExecutionEnvironmentDefault <em>Execution Environment Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Execution Environment Default</em>' attribute.
   * @see #isExecutionEnvironmentDefault()
   * @generated
   */
  void setExecutionEnvironmentDefault(boolean value);

  /**
   * Returns the value of the '<em><b>VM Arguments</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>VM Arguments</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>VM Arguments</em>' attribute.
   * @see #setVMArguments(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_VMArguments()
   * @model extendedMetaData="kind='attribute' name='vmArguments'"
   * @generated
   */
  String getVMArguments();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRETask#getVMArguments <em>VM Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>VM Arguments</em>' attribute.
   * @see #getVMArguments()
   * @generated
   */
  void setVMArguments(String value);

  /**
   * Returns the value of the '<em><b>JRE Libraries</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.jdt.JRELibrary}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>JRE Libraries</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * A list of JRE Library JARs for which external annotations are being supplied.
   * <!-- end-model-doc -->
   * @return the value of the '<em>JRE Libraries</em>' containment reference list.
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRETask_JRELibraries()
   * @model containment="true"
   *        extendedMetaData="name='jreLibrary'"
   * @generated
   */
  EList<JRELibrary> getJRELibraries();

} // JRETask

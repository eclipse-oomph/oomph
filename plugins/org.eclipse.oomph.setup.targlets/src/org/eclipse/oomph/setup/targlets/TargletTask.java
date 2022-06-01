/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getComposedTargets <em>Composed Targets</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTarglets <em>Targlets</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargletURIs <em>Targlet UR Is</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getOperatingSystem <em>Operating System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getWindowingSystem <em>Windowing System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getArchitecture <em>Architecture</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getLocale <em>Locale</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getProgramArguments <em>Program Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getVMArguments <em>VM Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getImplicitDependencies <em>Implicit Dependencies</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargetName <em>Target Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.TargletTask#isActivateTarget <em>Activate Target</em>}</li>
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
   * Returns the value of the '<em><b>Composed Targets</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The names of other target definitions to be composed with the target container's targlets
   * <!-- end-model-doc -->
   * @return the value of the '<em>Composed Targets</em>' attribute list.
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_ComposedTargets()
   * @model extendedMetaData="kind='element' name='composedTarget'"
   * @generated
   */
  EList<String> getComposedTargets();

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
   *        annotation="http://www.eclipse.org/oomph/setup/Redirect"
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

  /**
   * Returns the value of the '<em><b>Program Arguments</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Program Arguments</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Program Arguments</em>' attribute.
   * @see #setProgramArguments(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_ProgramArguments()
   * @model
   * @generated
   */
  String getProgramArguments();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getProgramArguments <em>Program Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Program Arguments</em>' attribute.
   * @see #getProgramArguments()
   * @generated
   */
  void setProgramArguments(String value);

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
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_VMArguments()
   * @model extendedMetaData="kind='attribute' name='vmArguments'"
   * @generated
   */
  String getVMArguments();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getVMArguments <em>VM Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>VM Arguments</em>' attribute.
   * @see #getVMArguments()
   * @generated
   */
  void setVMArguments(String value);

  /**
   * Returns the value of the '<em><b>Implicit Dependencies</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.targlets.ImplicitDependency}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Implicit Dependencies</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Implicit Dependencies</em>' containment reference list.
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_ImplicitDependencies()
   * @model containment="true"
   *        extendedMetaData="name='implicitDependency'"
   * @generated
   */
  EList<ImplicitDependency> getImplicitDependencies();

  /**
   * Returns the value of the '<em><b>Target Name</b></em>' attribute.
   * The default value is <code>"Modular Target"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Name</em>' attribute.
   * @see #setTargetName(String)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_TargetName()
   * @model default="Modular Target"
   * @generated
   */
  String getTargetName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargetName <em>Target Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Name</em>' attribute.
   * @see #getTargetName()
   * @generated
   */
  void setTargetName(String value);

  /**
   * Returns the value of the '<em><b>Activate Target</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Activate Target</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Activate Target</em>' attribute.
   * @see #setActivateTarget(boolean)
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#getTargletTask_ActivateTarget()
   * @model default="true"
   * @generated
   */
  boolean isActivateTarget();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.targlets.TargletTask#isActivateTarget <em>Activate Target</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Activate Target</em>' attribute.
   * @see #isActivateTarget()
   * @generated
   */
  void setActivateTarget(boolean value);
} // TargletTask

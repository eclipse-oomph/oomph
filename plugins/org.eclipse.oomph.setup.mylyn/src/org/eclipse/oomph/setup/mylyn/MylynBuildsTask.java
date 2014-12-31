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
package org.eclipse.oomph.setup.mylyn;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mylyn Builds Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getConnectorKind <em>Connector Kind</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getServerURL <em>Server URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getUserID <em>User ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getPassword <em>Password</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getBuildPlans <em>Build Plans</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/Enablement installableUnits='org.eclipse.mylyn.tasks.core org.eclipse.mylyn.tasks.ui'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface MylynBuildsTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Connector Kind</b></em>' attribute.
   * The default value is <code>"org.eclipse.mylyn.hudson"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Connector Kind</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Connector Kind</em>' attribute.
   * @see #setConnectorKind(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask_ConnectorKind()
   * @model default="org.eclipse.mylyn.hudson" required="true"
   * @generated
   */
  String getConnectorKind();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getConnectorKind <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Connector Kind</em>' attribute.
   * @see #getConnectorKind()
   * @generated
   */
  void setConnectorKind(String value);

  /**
   * Returns the value of the '<em><b>Server URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Server URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Server URL</em>' attribute.
   * @see #setServerURL(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask_ServerURL()
   * @model required="true"
   * @generated
   */
  String getServerURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getServerURL <em>Server URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Server URL</em>' attribute.
   * @see #getServerURL()
   * @generated
   */
  void setServerURL(String value);

  /**
   * Returns the value of the '<em><b>Build Plans</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.mylyn.BuildPlan}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Build Plans</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Build Plans</em>' containment reference list.
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask_BuildPlans()
   * @model containment="true" required="true"
   *        extendedMetaData="name='buildPlan'"
   * @generated
   */
  EList<BuildPlan> getBuildPlans();

  /**
   * Returns the value of the '<em><b>User ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>User ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>User ID</em>' attribute.
   * @see #setUserID(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask_UserID()
   * @model
   * @generated
   */
  String getUserID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getUserID <em>User ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>User ID</em>' attribute.
   * @see #getUserID()
   * @generated
   */
  void setUserID(String value);

  /**
   * Returns the value of the '<em><b>Password</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Password</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Password</em>' attribute.
   * @see #setPassword(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynBuildsTask_Password()
   * @model
   * @generated
   */
  String getPassword();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getPassword <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Password</em>' attribute.
   * @see #getPassword()
   * @generated
   */
  void setPassword(String value);

} // MylynBuildsTask

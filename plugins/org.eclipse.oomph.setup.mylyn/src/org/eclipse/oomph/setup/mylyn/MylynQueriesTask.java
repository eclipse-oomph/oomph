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
package org.eclipse.oomph.setup.mylyn;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mylyn Queries Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getConnectorKind <em>Connector Kind</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getRepositoryURL <em>Repository URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getUserID <em>User ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getPassword <em>Password</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getQueries <em>Queries</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/Enablement installableUnits='org.eclipse.mylyn.tasks.core org.eclipse.mylyn.tasks.ui'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface MylynQueriesTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Connector Kind</b></em>' attribute.
   * The default value is <code>"bugzilla"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Connector Kind</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Connector Kind</em>' attribute.
   * @see #setConnectorKind(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask_ConnectorKind()
   * @model default="bugzilla" required="true"
   * @generated
   */
  String getConnectorKind();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getConnectorKind <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Connector Kind</em>' attribute.
   * @see #getConnectorKind()
   * @generated
   */
  void setConnectorKind(String value);

  /**
   * Returns the value of the '<em><b>Repository URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repository URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repository URL</em>' attribute.
   * @see #setRepositoryURL(String)
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask_RepositoryURL()
   * @model required="true"
   * @generated
   */
  String getRepositoryURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getRepositoryURL <em>Repository URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Repository URL</em>' attribute.
   * @see #getRepositoryURL()
   * @generated
   */
  void setRepositoryURL(String value);

  /**
   * Returns the value of the '<em><b>Queries</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.mylyn.Query}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.mylyn.Query#getTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Queries</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Queries</em>' containment reference list.
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask_Queries()
   * @see org.eclipse.oomph.setup.mylyn.Query#getTask
   * @model opposite="task" containment="true"
   *        extendedMetaData="name='query'"
   * @generated
   */
  EList<Query> getQueries();

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
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask_UserID()
   * @model
   * @generated
   */
  String getUserID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getUserID <em>User ID</em>}' attribute.
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
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#getMylynQueriesTask_Password()
   * @model
   * @generated
   */
  String getPassword();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getPassword <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Password</em>' attribute.
   * @see #getPassword()
   * @generated
   */
  void setPassword(String value);

} // MylynQueriesTask

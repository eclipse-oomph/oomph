/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.mylyn;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.mylyn.MylynFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Mylyn.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.mylyn.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.mylyn.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.mylyn.p2' repository='http://download.eclipse.org/mylyn/releases/latest'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.mylyn.edit/icons/full/obj16'"
 * @generated
 */
public interface MylynPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "mylyn";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/mylyn/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "mylyn";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MylynPackage eINSTANCE = org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl <em>Queries Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getMylynQueriesTask()
   * @generated
   */
  int MYLYN_QUERIES_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Connector Kind</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__CONNECTOR_KIND = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Repository URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__REPOSITORY_URL = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>User ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__USER_ID = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Password</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__PASSWORD = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Queries</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK__QUERIES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Queries Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_QUERIES_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl <em>Builds Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getMylynBuildsTask()
   * @generated
   */
  int MYLYN_BUILDS_TASK = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Connector Kind</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__CONNECTOR_KIND = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Server URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__SERVER_URL = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>User ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__USER_ID = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Password</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__PASSWORD = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Build Plans</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK__BUILD_PLANS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Builds Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MYLYN_BUILDS_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.mylyn.impl.BuildPlanImpl <em>Build Plan</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.mylyn.impl.BuildPlanImpl
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getBuildPlan()
   * @generated
   */
  int BUILD_PLAN = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILD_PLAN__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILD_PLAN__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Build Plan</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILD_PLAN_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.mylyn.impl.QueryImpl <em>Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.mylyn.impl.QueryImpl
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getQuery()
   * @generated
   */
  int QUERY = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Task</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__TASK = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Summary</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__SUMMARY = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__URL = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Attributes</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__ATTRIBUTES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.mylyn.impl.QueryAttributeImpl <em>Query Attribute</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.mylyn.impl.QueryAttributeImpl
   * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getQueryAttribute()
   * @generated
   */
  int QUERY_ATTRIBUTE = 4;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY_ATTRIBUTE__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY_ATTRIBUTE__VALUE = 1;

  /**
   * The number of structural features of the '<em>Query Attribute</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY_ATTRIBUTE_FEATURE_COUNT = 2;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask <em>Queries Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Queries Task</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask
   * @generated
   */
  EClass getMylynQueriesTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getConnectorKind <em>Connector Kind</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Connector Kind</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getConnectorKind()
   * @see #getMylynQueriesTask()
   * @generated
   */
  EAttribute getMylynQueriesTask_ConnectorKind();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getRepositoryURL <em>Repository URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Repository URL</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getRepositoryURL()
   * @see #getMylynQueriesTask()
   * @generated
   */
  EAttribute getMylynQueriesTask_RepositoryURL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getUserID <em>User ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>User ID</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getUserID()
   * @see #getMylynQueriesTask()
   * @generated
   */
  EAttribute getMylynQueriesTask_UserID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getPassword <em>Password</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Password</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getPassword()
   * @see #getMylynQueriesTask()
   * @generated
   */
  EAttribute getMylynQueriesTask_Password();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getQueries <em>Queries</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Queries</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynQueriesTask#getQueries()
   * @see #getMylynQueriesTask()
   * @generated
   */
  EReference getMylynQueriesTask_Queries();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask <em>Builds Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Builds Task</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask
   * @generated
   */
  EClass getMylynBuildsTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getConnectorKind <em>Connector Kind</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Connector Kind</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getConnectorKind()
   * @see #getMylynBuildsTask()
   * @generated
   */
  EAttribute getMylynBuildsTask_ConnectorKind();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getServerURL <em>Server URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Server URL</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getServerURL()
   * @see #getMylynBuildsTask()
   * @generated
   */
  EAttribute getMylynBuildsTask_ServerURL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getUserID <em>User ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>User ID</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getUserID()
   * @see #getMylynBuildsTask()
   * @generated
   */
  EAttribute getMylynBuildsTask_UserID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getPassword <em>Password</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Password</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getPassword()
   * @see #getMylynBuildsTask()
   * @generated
   */
  EAttribute getMylynBuildsTask_Password();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getBuildPlans <em>Build Plans</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Build Plans</em>'.
   * @see org.eclipse.oomph.setup.mylyn.MylynBuildsTask#getBuildPlans()
   * @see #getMylynBuildsTask()
   * @generated
   */
  EReference getMylynBuildsTask_BuildPlans();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.mylyn.BuildPlan <em>Build Plan</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Build Plan</em>'.
   * @see org.eclipse.oomph.setup.mylyn.BuildPlan
   * @generated
   */
  EClass getBuildPlan();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.BuildPlan#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.mylyn.BuildPlan#getName()
   * @see #getBuildPlan()
   * @generated
   */
  EAttribute getBuildPlan_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.mylyn.Query <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Query</em>'.
   * @see org.eclipse.oomph.setup.mylyn.Query
   * @generated
   */
  EClass getQuery();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.mylyn.Query#getTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Task</em>'.
   * @see org.eclipse.oomph.setup.mylyn.Query#getTask()
   * @see #getQuery()
   * @generated
   */
  EReference getQuery_Task();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.Query#getSummary <em>Summary</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Summary</em>'.
   * @see org.eclipse.oomph.setup.mylyn.Query#getSummary()
   * @see #getQuery()
   * @generated
   */
  EAttribute getQuery_Summary();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.mylyn.Query#getURL <em>URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URL</em>'.
   * @see org.eclipse.oomph.setup.mylyn.Query#getURL()
   * @see #getQuery()
   * @generated
   */
  EAttribute getQuery_URL();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.setup.mylyn.Query#getAttributes <em>Attributes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Attributes</em>'.
   * @see org.eclipse.oomph.setup.mylyn.Query#getAttributes()
   * @see #getQuery()
   * @generated
   */
  EReference getQuery_Attributes();

  /**
   * Returns the meta object for class '{@link java.util.Map.Entry <em>Query Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Query Attribute</em>'.
   * @see java.util.Map.Entry
   * @model keyDataType="org.eclipse.emf.ecore.EString" keyRequired="true"
   *        valueDataType="org.eclipse.emf.ecore.EString"
   * @generated
   */
  EClass getQueryAttribute();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see java.util.Map.Entry
   * @see #getQueryAttribute()
   * @generated
   */
  EAttribute getQueryAttribute_Key();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see java.util.Map.Entry
   * @see #getQueryAttribute()
   * @generated
   */
  EAttribute getQueryAttribute_Value();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MylynFactory getMylynFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl <em>Queries Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getMylynQueriesTask()
     * @generated
     */
    EClass MYLYN_QUERIES_TASK = eINSTANCE.getMylynQueriesTask();

    /**
     * The meta object literal for the '<em><b>Connector Kind</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_QUERIES_TASK__CONNECTOR_KIND = eINSTANCE.getMylynQueriesTask_ConnectorKind();

    /**
     * The meta object literal for the '<em><b>Repository URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_QUERIES_TASK__REPOSITORY_URL = eINSTANCE.getMylynQueriesTask_RepositoryURL();

    /**
     * The meta object literal for the '<em><b>User ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_QUERIES_TASK__USER_ID = eINSTANCE.getMylynQueriesTask_UserID();

    /**
     * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_QUERIES_TASK__PASSWORD = eINSTANCE.getMylynQueriesTask_Password();

    /**
     * The meta object literal for the '<em><b>Queries</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MYLYN_QUERIES_TASK__QUERIES = eINSTANCE.getMylynQueriesTask_Queries();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl <em>Builds Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getMylynBuildsTask()
     * @generated
     */
    EClass MYLYN_BUILDS_TASK = eINSTANCE.getMylynBuildsTask();

    /**
     * The meta object literal for the '<em><b>Connector Kind</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_BUILDS_TASK__CONNECTOR_KIND = eINSTANCE.getMylynBuildsTask_ConnectorKind();

    /**
     * The meta object literal for the '<em><b>Server URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_BUILDS_TASK__SERVER_URL = eINSTANCE.getMylynBuildsTask_ServerURL();

    /**
     * The meta object literal for the '<em><b>User ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_BUILDS_TASK__USER_ID = eINSTANCE.getMylynBuildsTask_UserID();

    /**
     * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MYLYN_BUILDS_TASK__PASSWORD = eINSTANCE.getMylynBuildsTask_Password();

    /**
     * The meta object literal for the '<em><b>Build Plans</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MYLYN_BUILDS_TASK__BUILD_PLANS = eINSTANCE.getMylynBuildsTask_BuildPlans();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.mylyn.impl.BuildPlanImpl <em>Build Plan</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.mylyn.impl.BuildPlanImpl
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getBuildPlan()
     * @generated
     */
    EClass BUILD_PLAN = eINSTANCE.getBuildPlan();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BUILD_PLAN__NAME = eINSTANCE.getBuildPlan_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.mylyn.impl.QueryImpl <em>Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.mylyn.impl.QueryImpl
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getQuery()
     * @generated
     */
    EClass QUERY = eINSTANCE.getQuery();

    /**
     * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERY__TASK = eINSTANCE.getQuery_Task();

    /**
     * The meta object literal for the '<em><b>Summary</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERY__SUMMARY = eINSTANCE.getQuery_Summary();

    /**
     * The meta object literal for the '<em><b>URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERY__URL = eINSTANCE.getQuery_URL();

    /**
     * The meta object literal for the '<em><b>Attributes</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERY__ATTRIBUTES = eINSTANCE.getQuery_Attributes();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.mylyn.impl.QueryAttributeImpl <em>Query Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.mylyn.impl.QueryAttributeImpl
     * @see org.eclipse.oomph.setup.mylyn.impl.MylynPackageImpl#getQueryAttribute()
     * @generated
     */
    EClass QUERY_ATTRIBUTE = eINSTANCE.getQueryAttribute();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERY_ATTRIBUTE__KEY = eINSTANCE.getQueryAttribute_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERY_ATTRIBUTE__VALUE = eINSTANCE.getQueryAttribute_Value();

  }

} // MylynPackage

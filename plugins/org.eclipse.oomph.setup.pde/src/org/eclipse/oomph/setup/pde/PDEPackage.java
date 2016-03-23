/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde;

import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.oomph.setup.pde.PDEFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/PDE.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.pde.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.pde.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.pde.edit/icons/full/obj16'"
 * @generated
 */
public interface PDEPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "pde";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/pde/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "pde";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  PDEPackage eINSTANCE = org.eclipse.oomph.setup.pde.impl.PDEPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.pde.impl.TargetPlatformTaskImpl <em>Target Platform Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.pde.impl.TargetPlatformTaskImpl
   * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getTargetPlatformTask()
   * @generated
   */
  int TARGET_PLATFORM_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK__NAME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Target Platform Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_PLATFORM_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl <em>Abstract API Baseline Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl
   * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAbstractAPIBaselineTask()
   * @generated
   */
  int ABSTRACT_API_BASELINE_TASK = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__NAME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__VERSION = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Activate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK__ACTIVATE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Abstract API Baseline Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl <em>API Baseline Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl
   * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAPIBaselineTask()
   * @generated
   */
  int API_BASELINE_TASK = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__ANNOTATIONS = ABSTRACT_API_BASELINE_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__ID = ABSTRACT_API_BASELINE_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__DESCRIPTION = ABSTRACT_API_BASELINE_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__SCOPE_TYPE = ABSTRACT_API_BASELINE_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__EXCLUDED_TRIGGERS = ABSTRACT_API_BASELINE_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__DISABLED = ABSTRACT_API_BASELINE_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__PREDECESSORS = ABSTRACT_API_BASELINE_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__SUCCESSORS = ABSTRACT_API_BASELINE_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__RESTRICTIONS = ABSTRACT_API_BASELINE_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__FILTER = ABSTRACT_API_BASELINE_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__NAME = ABSTRACT_API_BASELINE_TASK__NAME;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__VERSION = ABSTRACT_API_BASELINE_TASK__VERSION;

  /**
   * The feature id for the '<em><b>Activate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__ACTIVATE = ABSTRACT_API_BASELINE_TASK__ACTIVATE;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__LOCATION = ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Remote URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK__REMOTE_URI = ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>API Baseline Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_TASK_FEATURE_COUNT = ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.pde.impl.APIBaselineFromTargetTaskImpl <em>API Baseline From Target Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.pde.impl.APIBaselineFromTargetTaskImpl
   * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAPIBaselineFromTargetTask()
   * @generated
   */
  int API_BASELINE_FROM_TARGET_TASK = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__ANNOTATIONS = ABSTRACT_API_BASELINE_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__ID = ABSTRACT_API_BASELINE_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__DESCRIPTION = ABSTRACT_API_BASELINE_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__SCOPE_TYPE = ABSTRACT_API_BASELINE_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__EXCLUDED_TRIGGERS = ABSTRACT_API_BASELINE_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__DISABLED = ABSTRACT_API_BASELINE_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__PREDECESSORS = ABSTRACT_API_BASELINE_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__SUCCESSORS = ABSTRACT_API_BASELINE_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__RESTRICTIONS = ABSTRACT_API_BASELINE_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__FILTER = ABSTRACT_API_BASELINE_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__NAME = ABSTRACT_API_BASELINE_TASK__NAME;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__VERSION = ABSTRACT_API_BASELINE_TASK__VERSION;

  /**
   * The feature id for the '<em><b>Activate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__ACTIVATE = ABSTRACT_API_BASELINE_TASK__ACTIVATE;

  /**
   * The feature id for the '<em><b>Target Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK__TARGET_NAME = ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>API Baseline From Target Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int API_BASELINE_FROM_TARGET_TASK_FEATURE_COUNT = ABSTRACT_API_BASELINE_TASK_FEATURE_COUNT + 1;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.pde.TargetPlatformTask <em>Target Platform Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Target Platform Task</em>'.
   * @see org.eclipse.oomph.setup.pde.TargetPlatformTask
   * @generated
   */
  EClass getTargetPlatformTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.TargetPlatformTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.pde.TargetPlatformTask#getName()
   * @see #getTargetPlatformTask()
   * @generated
   */
  EAttribute getTargetPlatformTask_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask <em>Abstract API Baseline Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Abstract API Baseline Task</em>'.
   * @see org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask
   * @generated
   */
  EClass getAbstractAPIBaselineTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getName()
   * @see #getAbstractAPIBaselineTask()
   * @generated
   */
  EAttribute getAbstractAPIBaselineTask_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getVersion()
   * @see #getAbstractAPIBaselineTask()
   * @generated
   */
  EAttribute getAbstractAPIBaselineTask_Version();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#isActivate <em>Activate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Activate</em>'.
   * @see org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#isActivate()
   * @see #getAbstractAPIBaselineTask()
   * @generated
   */
  EAttribute getAbstractAPIBaselineTask_Activate();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.pde.APIBaselineTask <em>API Baseline Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>API Baseline Task</em>'.
   * @see org.eclipse.oomph.setup.pde.APIBaselineTask
   * @generated
   */
  EClass getAPIBaselineTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.setup.pde.APIBaselineTask#getLocation()
   * @see #getAPIBaselineTask()
   * @generated
   */
  EAttribute getAPIBaselineTask_Location();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getRemoteURI <em>Remote URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Remote URI</em>'.
   * @see org.eclipse.oomph.setup.pde.APIBaselineTask#getRemoteURI()
   * @see #getAPIBaselineTask()
   * @generated
   */
  EAttribute getAPIBaselineTask_RemoteURI();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask <em>API Baseline From Target Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>API Baseline From Target Task</em>'.
   * @see org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask
   * @generated
   */
  EClass getAPIBaselineFromTargetTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask#getTargetName <em>Target Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target Name</em>'.
   * @see org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask#getTargetName()
   * @see #getAPIBaselineFromTargetTask()
   * @generated
   */
  EAttribute getAPIBaselineFromTargetTask_TargetName();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  PDEFactory getPDEFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.pde.impl.TargetPlatformTaskImpl <em>Target Platform Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.pde.impl.TargetPlatformTaskImpl
     * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getTargetPlatformTask()
     * @generated
     */
    EClass TARGET_PLATFORM_TASK = eINSTANCE.getTargetPlatformTask();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGET_PLATFORM_TASK__NAME = eINSTANCE.getTargetPlatformTask_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl <em>Abstract API Baseline Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl
     * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAbstractAPIBaselineTask()
     * @generated
     */
    EClass ABSTRACT_API_BASELINE_TASK = eINSTANCE.getAbstractAPIBaselineTask();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_API_BASELINE_TASK__NAME = eINSTANCE.getAbstractAPIBaselineTask_Name();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_API_BASELINE_TASK__VERSION = eINSTANCE.getAbstractAPIBaselineTask_Version();

    /**
     * The meta object literal for the '<em><b>Activate</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_API_BASELINE_TASK__ACTIVATE = eINSTANCE.getAbstractAPIBaselineTask_Activate();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl <em>API Baseline Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl
     * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAPIBaselineTask()
     * @generated
     */
    EClass API_BASELINE_TASK = eINSTANCE.getAPIBaselineTask();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute API_BASELINE_TASK__LOCATION = eINSTANCE.getAPIBaselineTask_Location();

    /**
     * The meta object literal for the '<em><b>Remote URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute API_BASELINE_TASK__REMOTE_URI = eINSTANCE.getAPIBaselineTask_RemoteURI();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.pde.impl.APIBaselineFromTargetTaskImpl <em>API Baseline From Target Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.pde.impl.APIBaselineFromTargetTaskImpl
     * @see org.eclipse.oomph.setup.pde.impl.PDEPackageImpl#getAPIBaselineFromTargetTask()
     * @generated
     */
    EClass API_BASELINE_FROM_TARGET_TASK = eINSTANCE.getAPIBaselineFromTargetTask();

    /**
     * The meta object literal for the '<em><b>Target Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute API_BASELINE_FROM_TARGET_TASK__TARGET_NAME = eINSTANCE.getAPIBaselineFromTargetTask_TargetName();

  }

} // PDEPackage

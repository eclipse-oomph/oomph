/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.oomph.setup.sync.SyncFactory
 * @model kind="package"
 * @generated
 */
public interface SyncPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "sync";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/sync/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "sync";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SyncPackage eINSTANCE = org.eclipse.oomph.setup.sync.impl.SyncPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteDataImpl <em>Remote Data</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.RemoteDataImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteData()
   * @generated
   */
  int REMOTE_DATA = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_DATA__ANNOTATIONS = SetupPackage.SETUP_TASK_CONTAINER__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_DATA__SETUP_TASKS = SetupPackage.SETUP_TASK_CONTAINER__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Policies</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_DATA__POLICIES = SetupPackage.SETUP_TASK_CONTAINER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Remote Data</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_DATA_FEATURE_COUNT = SetupPackage.SETUP_TASK_CONTAINER_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.StringToSyncPolicyMapEntryImpl <em>String To Sync Policy Map Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.StringToSyncPolicyMapEntryImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getStringToSyncPolicyMapEntry()
   * @generated
   */
  int STRING_TO_SYNC_POLICY_MAP_ENTRY = 1;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_SYNC_POLICY_MAP_ENTRY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_SYNC_POLICY_MAP_ENTRY__VALUE = 1;

  /**
   * The number of structural features of the '<em>String To Sync Policy Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_SYNC_POLICY_MAP_ENTRY_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl <em>Delta</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDelta()
   * @generated
   */
  int SYNC_DELTA = 2;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__ID = 0;

  /**
   * The feature id for the '<em><b>Old Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__OLD_TASK = 1;

  /**
   * The feature id for the '<em><b>New Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__NEW_TASK = 2;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__TYPE = 3;

  /**
   * The number of structural features of the '<em>Delta</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl <em>Action</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncActionImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncAction()
   * @generated
   */
  int SYNC_ACTION = 3;

  /**
   * The feature id for the '<em><b>Local Delta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__LOCAL_DELTA = 0;

  /**
   * The feature id for the '<em><b>Remote Delta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__REMOTE_DELTA = 1;

  /**
   * The feature id for the '<em><b>Computed Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__COMPUTED_TYPE = 2;

  /**
   * The feature id for the '<em><b>Resolved Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__RESOLVED_TYPE = 3;

  /**
   * The feature id for the '<em><b>Effective Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__EFFECTIVE_TYPE = 4;

  /**
   * The number of structural features of the '<em>Action</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncPolicy <em>Policy</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncPolicy
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncPolicy()
   * @generated
   */
  int SYNC_POLICY = 4;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncDeltaType <em>Delta Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDeltaType()
   * @generated
   */
  int SYNC_DELTA_TYPE = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncActionType <em>Action Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncActionType()
   * @generated
   */
  int SYNC_ACTION_TYPE = 6;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.RemoteData <em>Remote Data</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Remote Data</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteData
   * @generated
   */
  EClass getRemoteData();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.setup.sync.RemoteData#getPolicies <em>Policies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Policies</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteData#getPolicies()
   * @see #getRemoteData()
   * @generated
   */
  EReference getRemoteData_Policies();

  /**
   * Returns the meta object for class '{@link java.util.Map.Entry <em>String To Sync Policy Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String To Sync Policy Map Entry</em>'.
   * @see java.util.Map.Entry
   * @model keyDataType="org.eclipse.emf.ecore.EString" keyRequired="true"
   *        valueDataType="org.eclipse.oomph.setup.sync.SyncPolicy" valueRequired="true"
   * @generated
   */
  EClass getStringToSyncPolicyMapEntry();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see java.util.Map.Entry
   * @see #getStringToSyncPolicyMapEntry()
   * @generated
   */
  EAttribute getStringToSyncPolicyMapEntry_Key();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see java.util.Map.Entry
   * @see #getStringToSyncPolicyMapEntry()
   * @generated
   */
  EAttribute getStringToSyncPolicyMapEntry_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncDelta <em>Delta</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Delta</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta
   * @generated
   */
  EClass getSyncDelta();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncDelta#getOldTask <em>Old Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Old Task</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getOldTask()
   * @see #getSyncDelta()
   * @generated
   */
  EReference getSyncDelta_OldTask();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncDelta#getNewTask <em>New Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>New Task</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getNewTask()
   * @see #getSyncDelta()
   * @generated
   */
  EReference getSyncDelta_NewTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncDelta#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getType()
   * @see #getSyncDelta()
   * @generated
   */
  EAttribute getSyncDelta_Type();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncDelta#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getID()
   * @see #getSyncDelta()
   * @generated
   */
  EAttribute getSyncDelta_ID();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncAction <em>Action</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Action</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction
   * @generated
   */
  EClass getSyncAction();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncAction#getLocalDelta <em>Local Delta</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Local Delta</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getLocalDelta()
   * @see #getSyncAction()
   * @generated
   */
  EReference getSyncAction_LocalDelta();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncAction#getRemoteDelta <em>Remote Delta</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Remote Delta</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getRemoteDelta()
   * @see #getSyncAction()
   * @generated
   */
  EReference getSyncAction_RemoteDelta();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncAction#getComputedType <em>Computed Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Computed Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getComputedType()
   * @see #getSyncAction()
   * @generated
   */
  EAttribute getSyncAction_ComputedType();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncAction#getResolvedType <em>Resolved Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resolved Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getResolvedType()
   * @see #getSyncAction()
   * @generated
   */
  EAttribute getSyncAction_ResolvedType();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncAction#getEffectiveType <em>Effective Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Effective Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getEffectiveType()
   * @see #getSyncAction()
   * @generated
   */
  EAttribute getSyncAction_EffectiveType();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.sync.SyncPolicy <em>Policy</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Policy</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncPolicy
   * @generated
   */
  EEnum getSyncPolicy();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.sync.SyncDeltaType <em>Delta Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Delta Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @generated
   */
  EEnum getSyncDeltaType();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.sync.SyncActionType <em>Action Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Action Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @generated
   */
  EEnum getSyncActionType();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SyncFactory getSyncFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteDataImpl <em>Remote Data</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.RemoteDataImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteData()
     * @generated
     */
    EClass REMOTE_DATA = eINSTANCE.getRemoteData();

    /**
     * The meta object literal for the '<em><b>Policies</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REMOTE_DATA__POLICIES = eINSTANCE.getRemoteData_Policies();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.StringToSyncPolicyMapEntryImpl <em>String To Sync Policy Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.StringToSyncPolicyMapEntryImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getStringToSyncPolicyMapEntry()
     * @generated
     */
    EClass STRING_TO_SYNC_POLICY_MAP_ENTRY = eINSTANCE.getStringToSyncPolicyMapEntry();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_TO_SYNC_POLICY_MAP_ENTRY__KEY = eINSTANCE.getStringToSyncPolicyMapEntry_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_TO_SYNC_POLICY_MAP_ENTRY__VALUE = eINSTANCE.getStringToSyncPolicyMapEntry_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl <em>Delta</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDelta()
     * @generated
     */
    EClass SYNC_DELTA = eINSTANCE.getSyncDelta();

    /**
     * The meta object literal for the '<em><b>Old Task</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_DELTA__OLD_TASK = eINSTANCE.getSyncDelta_OldTask();

    /**
     * The meta object literal for the '<em><b>New Task</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_DELTA__NEW_TASK = eINSTANCE.getSyncDelta_NewTask();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_DELTA__TYPE = eINSTANCE.getSyncDelta_Type();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_DELTA__ID = eINSTANCE.getSyncDelta_ID();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl <em>Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncActionImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncAction()
     * @generated
     */
    EClass SYNC_ACTION = eINSTANCE.getSyncAction();

    /**
     * The meta object literal for the '<em><b>Local Delta</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_ACTION__LOCAL_DELTA = eINSTANCE.getSyncAction_LocalDelta();

    /**
     * The meta object literal for the '<em><b>Remote Delta</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_ACTION__REMOTE_DELTA = eINSTANCE.getSyncAction_RemoteDelta();

    /**
     * The meta object literal for the '<em><b>Computed Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ACTION__COMPUTED_TYPE = eINSTANCE.getSyncAction_ComputedType();

    /**
     * The meta object literal for the '<em><b>Resolved Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ACTION__RESOLVED_TYPE = eINSTANCE.getSyncAction_ResolvedType();

    /**
     * The meta object literal for the '<em><b>Effective Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ACTION__EFFECTIVE_TYPE = eINSTANCE.getSyncAction_EffectiveType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.SyncPolicy <em>Policy</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.SyncPolicy
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncPolicy()
     * @generated
     */
    EEnum SYNC_POLICY = eINSTANCE.getSyncPolicy();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.SyncDeltaType <em>Delta Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.SyncDeltaType
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDeltaType()
     * @generated
     */
    EEnum SYNC_DELTA_TYPE = eINSTANCE.getSyncDeltaType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.SyncActionType <em>Action Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.SyncActionType
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncActionType()
     * @generated
     */
    EEnum SYNC_ACTION_TYPE = eINSTANCE.getSyncActionType();

  }

} // SyncPackage

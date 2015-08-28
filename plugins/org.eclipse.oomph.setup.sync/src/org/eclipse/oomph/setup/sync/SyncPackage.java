/**
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
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteSnapshotImpl <em>Remote Snapshot</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.RemoteSnapshotImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteSnapshot()
   * @generated
   */
  int REMOTE_SNAPSHOT = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SNAPSHOT__ANNOTATIONS = SetupPackage.SETUP_TASK_CONTAINER__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SNAPSHOT__SETUP_TASKS = SetupPackage.SETUP_TASK_CONTAINER__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Policies</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SNAPSHOT__POLICIES = SetupPackage.SETUP_TASK_CONTAINER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Remote Snapshot</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SNAPSHOT_FEATURE_COUNT = SetupPackage.SETUP_TASK_CONTAINER_FEATURE_COUNT + 1;

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
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__TYPE = 0;

  /**
   * The number of structural features of the '<em>Delta</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA_FEATURE_COUNT = 1;

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
   * The feature id for the '<em><b>Remotedelta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__REMOTEDELTA = 1;

  /**
   * The feature id for the '<em><b>Suggested Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__SUGGESTED_TYPE = 2;

  /**
   * The feature id for the '<em><b>Resolved Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION__RESOLVED_TYPE = 3;

  /**
   * The number of structural features of the '<em>Action</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ACTION_FEATURE_COUNT = 4;

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
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.RemoteSnapshot <em>Remote Snapshot</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Remote Snapshot</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteSnapshot
   * @generated
   */
  EClass getRemoteSnapshot();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.setup.sync.RemoteSnapshot#getPolicies <em>Policies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Policies</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteSnapshot#getPolicies()
   * @see #getRemoteSnapshot()
   * @generated
   */
  EReference getRemoteSnapshot_Policies();

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
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncAction#getRemotedelta <em>Remotedelta</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Remotedelta</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getRemotedelta()
   * @see #getSyncAction()
   * @generated
   */
  EReference getSyncAction_Remotedelta();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncAction#getSuggestedType <em>Suggested Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Suggested Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncAction#getSuggestedType()
   * @see #getSyncAction()
   * @generated
   */
  EAttribute getSyncAction_SuggestedType();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteSnapshotImpl <em>Remote Snapshot</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.RemoteSnapshotImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteSnapshot()
     * @generated
     */
    EClass REMOTE_SNAPSHOT = eINSTANCE.getRemoteSnapshot();

    /**
     * The meta object literal for the '<em><b>Policies</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REMOTE_SNAPSHOT__POLICIES = eINSTANCE.getRemoteSnapshot_Policies();

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
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_DELTA__TYPE = eINSTANCE.getSyncDelta_Type();

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
     * The meta object literal for the '<em><b>Remotedelta</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_ACTION__REMOTEDELTA = eINSTANCE.getSyncAction_Remotedelta();

    /**
     * The meta object literal for the '<em><b>Suggested Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ACTION__SUGGESTED_TYPE = eINSTANCE.getSyncAction_SuggestedType();

    /**
     * The meta object literal for the '<em><b>Resolved Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ACTION__RESOLVED_TYPE = eINSTANCE.getSyncAction_ResolvedType();

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

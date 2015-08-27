/**
 */
package org.eclipse.oomph.setup.sync;

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
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl <em>State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncStateImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncState()
   * @generated
   */
  int SYNC_STATE = 0;

  /**
   * The feature id for the '<em><b>Local Time Stamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_STATE__LOCAL_TIME_STAMP = 0;

  /**
   * The feature id for the '<em><b>Local Snapshot</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_STATE__LOCAL_SNAPSHOT = 1;

  /**
   * The feature id for the '<em><b>Remote Time Stamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_STATE__REMOTE_TIME_STAMP = 2;

  /**
   * The feature id for the '<em><b>Remote Snapshot</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_STATE__REMOTE_SNAPSHOT = 3;

  /**
   * The number of structural features of the '<em>State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_STATE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncSnapshotImpl <em>Snapshot</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncSnapshotImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncSnapshot()
   * @generated
   */
  int SYNC_SNAPSHOT = 1;

  /**
   * The feature id for the '<em><b>Items</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_SNAPSHOT__ITEMS = 0;

  /**
   * The number of structural features of the '<em>Snapshot</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_SNAPSHOT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncItemImpl <em>Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncItemImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItem()
   * @generated
   */
  int SYNC_ITEM = 2;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ITEM__TYPE = 0;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ITEM__KEY = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ITEM__VALUE = 2;

  /**
   * The number of structural features of the '<em>Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_ITEM_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteSyncItemImpl <em>Remote Sync Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.RemoteSyncItemImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteSyncItem()
   * @generated
   */
  int REMOTE_SYNC_ITEM = 3;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SYNC_ITEM__TYPE = SYNC_ITEM__TYPE;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SYNC_ITEM__KEY = SYNC_ITEM__KEY;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SYNC_ITEM__VALUE = SYNC_ITEM__VALUE;

  /**
   * The feature id for the '<em><b>Policy</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SYNC_ITEM__POLICY = SYNC_ITEM_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Remote Sync Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REMOTE_SYNC_ITEM_FEATURE_COUNT = SYNC_ITEM_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl <em>Action</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncActionImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncAction()
   * @generated
   */
  int SYNC_ACTION = 4;

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
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl <em>Delta</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDelta()
   * @generated
   */
  int SYNC_DELTA = 5;

  /**
   * The feature id for the '<em><b>Old Item</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__OLD_ITEM = 0;

  /**
   * The feature id for the '<em><b>New Item</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__NEW_ITEM = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA__TYPE = 2;

  /**
   * The number of structural features of the '<em>Delta</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYNC_DELTA_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncItemType <em>Item Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncItemType
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItemType()
   * @generated
   */
  int SYNC_ITEM_TYPE = 6;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncItemPolicy <em>Item Policy</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncItemPolicy
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItemPolicy()
   * @generated
   */
  int SYNC_ITEM_POLICY = 7;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncDeltaType <em>Delta Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDeltaType()
   * @generated
   */
  int SYNC_DELTA_TYPE = 8;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.sync.SyncActionType <em>Action Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncActionType()
   * @generated
   */
  int SYNC_ACTION_TYPE = 9;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncState <em>State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>State</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncState
   * @generated
   */
  EClass getSyncState();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncState#getLocalTimeStamp <em>Local Time Stamp</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Local Time Stamp</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncState#getLocalTimeStamp()
   * @see #getSyncState()
   * @generated
   */
  EAttribute getSyncState_LocalTimeStamp();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncState#getLocalSnapshot <em>Local Snapshot</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Local Snapshot</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncState#getLocalSnapshot()
   * @see #getSyncState()
   * @generated
   */
  EReference getSyncState_LocalSnapshot();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteTimeStamp <em>Remote Time Stamp</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Remote Time Stamp</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncState#getRemoteTimeStamp()
   * @see #getSyncState()
   * @generated
   */
  EAttribute getSyncState_RemoteTimeStamp();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteSnapshot <em>Remote Snapshot</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Remote Snapshot</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncState#getRemoteSnapshot()
   * @see #getSyncState()
   * @generated
   */
  EReference getSyncState_RemoteSnapshot();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncSnapshot <em>Snapshot</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Snapshot</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncSnapshot
   * @generated
   */
  EClass getSyncSnapshot();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.sync.SyncSnapshot#getItems <em>Items</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Items</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncSnapshot#getItems()
   * @see #getSyncSnapshot()
   * @generated
   */
  EReference getSyncSnapshot_Items();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncItem <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Item</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItem
   * @generated
   */
  EClass getSyncItem();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncItem#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItem#getType()
   * @see #getSyncItem()
   * @generated
   */
  EAttribute getSyncItem_Type();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncItem#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItem#getKey()
   * @see #getSyncItem()
   * @generated
   */
  EAttribute getSyncItem_Key();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.SyncItem#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItem#getValue()
   * @see #getSyncItem()
   * @generated
   */
  EAttribute getSyncItem_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.RemoteSyncItem <em>Remote Sync Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Remote Sync Item</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteSyncItem
   * @generated
   */
  EClass getRemoteSyncItem();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.sync.RemoteSyncItem#getPolicy <em>Policy</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Policy</em>'.
   * @see org.eclipse.oomph.setup.sync.RemoteSyncItem#getPolicy()
   * @see #getRemoteSyncItem()
   * @generated
   */
  EAttribute getRemoteSyncItem_Policy();

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
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.sync.SyncDelta <em>Delta</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Delta</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta
   * @generated
   */
  EClass getSyncDelta();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncDelta#getOldItem <em>Old Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Old Item</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getOldItem()
   * @see #getSyncDelta()
   * @generated
   */
  EReference getSyncDelta_OldItem();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.sync.SyncDelta#getNewItem <em>New Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>New Item</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncDelta#getNewItem()
   * @see #getSyncDelta()
   * @generated
   */
  EReference getSyncDelta_NewItem();

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
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.sync.SyncItemType <em>Item Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Item Type</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItemType
   * @generated
   */
  EEnum getSyncItemType();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.sync.SyncItemPolicy <em>Item Policy</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Item Policy</em>'.
   * @see org.eclipse.oomph.setup.sync.SyncItemPolicy
   * @generated
   */
  EEnum getSyncItemPolicy();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl <em>State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncStateImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncState()
     * @generated
     */
    EClass SYNC_STATE = eINSTANCE.getSyncState();

    /**
     * The meta object literal for the '<em><b>Local Time Stamp</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_STATE__LOCAL_TIME_STAMP = eINSTANCE.getSyncState_LocalTimeStamp();

    /**
     * The meta object literal for the '<em><b>Local Snapshot</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_STATE__LOCAL_SNAPSHOT = eINSTANCE.getSyncState_LocalSnapshot();

    /**
     * The meta object literal for the '<em><b>Remote Time Stamp</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_STATE__REMOTE_TIME_STAMP = eINSTANCE.getSyncState_RemoteTimeStamp();

    /**
     * The meta object literal for the '<em><b>Remote Snapshot</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_STATE__REMOTE_SNAPSHOT = eINSTANCE.getSyncState_RemoteSnapshot();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncSnapshotImpl <em>Snapshot</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncSnapshotImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncSnapshot()
     * @generated
     */
    EClass SYNC_SNAPSHOT = eINSTANCE.getSyncSnapshot();

    /**
     * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_SNAPSHOT__ITEMS = eINSTANCE.getSyncSnapshot_Items();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncItemImpl <em>Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncItemImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItem()
     * @generated
     */
    EClass SYNC_ITEM = eINSTANCE.getSyncItem();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ITEM__TYPE = eINSTANCE.getSyncItem_Type();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ITEM__KEY = eINSTANCE.getSyncItem_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_ITEM__VALUE = eINSTANCE.getSyncItem_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.RemoteSyncItemImpl <em>Remote Sync Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.RemoteSyncItemImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getRemoteSyncItem()
     * @generated
     */
    EClass REMOTE_SYNC_ITEM = eINSTANCE.getRemoteSyncItem();

    /**
     * The meta object literal for the '<em><b>Policy</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REMOTE_SYNC_ITEM__POLICY = eINSTANCE.getRemoteSyncItem_Policy();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl <em>Delta</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncDelta()
     * @generated
     */
    EClass SYNC_DELTA = eINSTANCE.getSyncDelta();

    /**
     * The meta object literal for the '<em><b>Old Item</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_DELTA__OLD_ITEM = eINSTANCE.getSyncDelta_OldItem();

    /**
     * The meta object literal for the '<em><b>New Item</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYNC_DELTA__NEW_ITEM = eINSTANCE.getSyncDelta_NewItem();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYNC_DELTA__TYPE = eINSTANCE.getSyncDelta_Type();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.SyncItemType <em>Item Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.SyncItemType
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItemType()
     * @generated
     */
    EEnum SYNC_ITEM_TYPE = eINSTANCE.getSyncItemType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.sync.SyncItemPolicy <em>Item Policy</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.sync.SyncItemPolicy
     * @see org.eclipse.oomph.setup.sync.impl.SyncPackageImpl#getSyncItemPolicy()
     * @generated
     */
    EEnum SYNC_ITEM_POLICY = eINSTANCE.getSyncItemPolicy();

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

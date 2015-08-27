/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.sync.RemoteSyncItem;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncItem;
import org.eclipse.oomph.setup.sync.SyncItemPolicy;
import org.eclipse.oomph.setup.sync.SyncItemType;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncSnapshot;
import org.eclipse.oomph.setup.sync.SyncState;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SyncPackageImpl extends EPackageImpl implements SyncPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass syncStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass syncSnapshotEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass syncItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSyncItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass syncActionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass syncDeltaEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum syncItemTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum syncItemPolicyEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum syncDeltaTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum syncActionTypeEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.oomph.setup.sync.SyncPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SyncPackageImpl()
  {
    super(eNS_URI, SyncFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link SyncPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SyncPackage init()
  {
    if (isInited)
    {
      return (SyncPackage)EPackage.Registry.INSTANCE.getEPackage(SyncPackage.eNS_URI);
    }

    // Obtain or create and register package
    SyncPackageImpl theSyncPackage = (SyncPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SyncPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SyncPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theSyncPackage.createPackageContents();

    // Initialize created meta-data
    theSyncPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSyncPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(SyncPackage.eNS_URI, theSyncPackage);
    return theSyncPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSyncState()
  {
    return syncStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncState_LocalTimeStamp()
  {
    return (EAttribute)syncStateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncState_LocalSnapshot()
  {
    return (EReference)syncStateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncState_RemoteTimeStamp()
  {
    return (EAttribute)syncStateEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncState_RemoteSnapshot()
  {
    return (EReference)syncStateEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSyncSnapshot()
  {
    return syncSnapshotEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncSnapshot_Items()
  {
    return (EReference)syncSnapshotEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSyncItem()
  {
    return syncItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncItem_Type()
  {
    return (EAttribute)syncItemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncItem_Key()
  {
    return (EAttribute)syncItemEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncItem_Value()
  {
    return (EAttribute)syncItemEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSyncItem()
  {
    return remoteSyncItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSyncItem_Policy()
  {
    return (EAttribute)remoteSyncItemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSyncAction()
  {
    return syncActionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncAction_LocalDelta()
  {
    return (EReference)syncActionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncAction_Remotedelta()
  {
    return (EReference)syncActionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncAction_SuggestedType()
  {
    return (EAttribute)syncActionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncAction_ResolvedType()
  {
    return (EAttribute)syncActionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSyncDelta()
  {
    return syncDeltaEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncDelta_OldItem()
  {
    return (EReference)syncDeltaEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncDelta_NewItem()
  {
    return (EReference)syncDeltaEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncDelta_Type()
  {
    return (EAttribute)syncDeltaEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSyncItemType()
  {
    return syncItemTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSyncItemPolicy()
  {
    return syncItemPolicyEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSyncDeltaType()
  {
    return syncDeltaTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSyncActionType()
  {
    return syncActionTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncFactory getSyncFactory()
  {
    return (SyncFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
    {
      return;
    }
    isCreated = true;

    // Create classes and their features
    syncStateEClass = createEClass(SYNC_STATE);
    createEAttribute(syncStateEClass, SYNC_STATE__LOCAL_TIME_STAMP);
    createEReference(syncStateEClass, SYNC_STATE__LOCAL_SNAPSHOT);
    createEAttribute(syncStateEClass, SYNC_STATE__REMOTE_TIME_STAMP);
    createEReference(syncStateEClass, SYNC_STATE__REMOTE_SNAPSHOT);

    syncSnapshotEClass = createEClass(SYNC_SNAPSHOT);
    createEReference(syncSnapshotEClass, SYNC_SNAPSHOT__ITEMS);

    syncItemEClass = createEClass(SYNC_ITEM);
    createEAttribute(syncItemEClass, SYNC_ITEM__TYPE);
    createEAttribute(syncItemEClass, SYNC_ITEM__KEY);
    createEAttribute(syncItemEClass, SYNC_ITEM__VALUE);

    remoteSyncItemEClass = createEClass(REMOTE_SYNC_ITEM);
    createEAttribute(remoteSyncItemEClass, REMOTE_SYNC_ITEM__POLICY);

    syncActionEClass = createEClass(SYNC_ACTION);
    createEReference(syncActionEClass, SYNC_ACTION__LOCAL_DELTA);
    createEReference(syncActionEClass, SYNC_ACTION__REMOTEDELTA);
    createEAttribute(syncActionEClass, SYNC_ACTION__SUGGESTED_TYPE);
    createEAttribute(syncActionEClass, SYNC_ACTION__RESOLVED_TYPE);

    syncDeltaEClass = createEClass(SYNC_DELTA);
    createEReference(syncDeltaEClass, SYNC_DELTA__OLD_ITEM);
    createEReference(syncDeltaEClass, SYNC_DELTA__NEW_ITEM);
    createEAttribute(syncDeltaEClass, SYNC_DELTA__TYPE);

    // Create enums
    syncItemTypeEEnum = createEEnum(SYNC_ITEM_TYPE);
    syncItemPolicyEEnum = createEEnum(SYNC_ITEM_POLICY);
    syncDeltaTypeEEnum = createEEnum(SYNC_DELTA_TYPE);
    syncActionTypeEEnum = createEEnum(SYNC_ACTION_TYPE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
    {
      return;
    }
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    remoteSyncItemEClass.getESuperTypes().add(getSyncItem());

    // Initialize classes and features; add operations and parameters
    initEClass(syncStateEClass, SyncState.class, "SyncState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSyncState_LocalTimeStamp(), ecorePackage.getELong(), "localTimeStamp", null, 1, 1, SyncState.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncState_LocalSnapshot(), getSyncSnapshot(), null, "localSnapshot", null, 1, 1, SyncState.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncState_RemoteTimeStamp(), ecorePackage.getELong(), "remoteTimeStamp", null, 1, 1, SyncState.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncState_RemoteSnapshot(), getSyncSnapshot(), null, "remoteSnapshot", null, 1, 1, SyncState.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncSnapshotEClass, SyncSnapshot.class, "SyncSnapshot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSyncSnapshot_Items(), getSyncItem(), null, "items", null, 0, -1, SyncSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncItemEClass, SyncItem.class, "SyncItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSyncItem_Type(), getSyncItemType(), "type", null, 1, 1, SyncItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncItem_Key(), ecorePackage.getEString(), "key", null, 1, 1, SyncItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncItem_Value(), ecorePackage.getEString(), "value", null, 0, 1, SyncItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSyncItemEClass, RemoteSyncItem.class, "RemoteSyncItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSyncItem_Policy(), getSyncItemPolicy(), "policy", null, 0, 1, RemoteSyncItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncActionEClass, SyncAction.class, "SyncAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSyncAction_LocalDelta(), getSyncDelta(), null, "localDelta", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncAction_Remotedelta(), getSyncDelta(), null, "remotedelta", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncAction_SuggestedType(), getSyncActionType(), "suggestedType", null, 1, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncAction_ResolvedType(), getSyncActionType(), "resolvedType", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncDeltaEClass, SyncDelta.class, "SyncDelta", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSyncDelta_OldItem(), getSyncItem(), null, "oldItem", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncDelta_NewItem(), getSyncItem(), null, "newItem", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncDelta_Type(), getSyncDeltaType(), "type", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(syncItemTypeEEnum, SyncItemType.class, "SyncItemType");
    addEEnumLiteral(syncItemTypeEEnum, SyncItemType.PREFERENCE);
    addEEnumLiteral(syncItemTypeEEnum, SyncItemType.VARIABLE);
    addEEnumLiteral(syncItemTypeEEnum, SyncItemType.OTHER);

    initEEnum(syncItemPolicyEEnum, SyncItemPolicy.class, "SyncItemPolicy");
    addEEnumLiteral(syncItemPolicyEEnum, SyncItemPolicy.EXCLUDE);
    addEEnumLiteral(syncItemPolicyEEnum, SyncItemPolicy.INCLUDE);

    initEEnum(syncDeltaTypeEEnum, SyncDeltaType.class, "SyncDeltaType");
    addEEnumLiteral(syncDeltaTypeEEnum, SyncDeltaType.UNCHANGED);
    addEEnumLiteral(syncDeltaTypeEEnum, SyncDeltaType.CHANGED);
    addEEnumLiteral(syncDeltaTypeEEnum, SyncDeltaType.REMOVED);

    initEEnum(syncActionTypeEEnum, SyncActionType.class, "SyncActionType");
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.NONE);
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.SET_LOCAL);
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.SET_REMOTE);
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.REMOVE);
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.CONFLICT);
    addEEnumLiteral(syncActionTypeEEnum, SyncActionType.EXCLUDE);

    // Create resource
    createResource(eNS_URI);
  }

} // SyncPackageImpl

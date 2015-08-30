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
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncPolicy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import java.util.Map;

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
  private EClass remoteDataEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringToSyncPolicyMapEntryEClass = null;

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
  private EClass syncActionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum syncPolicyEEnum = null;

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

    // Initialize simple dependencies
    SetupPackage.eINSTANCE.eClass();

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
  public EClass getRemoteData()
  {
    return remoteDataEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRemoteData_Policies()
  {
    return (EReference)remoteDataEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringToSyncPolicyMapEntry()
  {
    return stringToSyncPolicyMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringToSyncPolicyMapEntry_Key()
  {
    return (EAttribute)stringToSyncPolicyMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringToSyncPolicyMapEntry_Value()
  {
    return (EAttribute)stringToSyncPolicyMapEntryEClass.getEStructuralFeatures().get(1);
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
  public EReference getSyncDelta_OldTask()
  {
    return (EReference)syncDeltaEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSyncDelta_NewTask()
  {
    return (EReference)syncDeltaEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncDelta_Type()
  {
    return (EAttribute)syncDeltaEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncDelta_ID()
  {
    return (EAttribute)syncDeltaEClass.getEStructuralFeatures().get(0);
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
  public EReference getSyncAction_RemoteDelta()
  {
    return (EReference)syncActionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSyncAction_ComputedType()
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
  public EAttribute getSyncAction_EffectiveType()
  {
    return (EAttribute)syncActionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSyncPolicy()
  {
    return syncPolicyEEnum;
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
    remoteDataEClass = createEClass(REMOTE_DATA);
    createEReference(remoteDataEClass, REMOTE_DATA__POLICIES);

    stringToSyncPolicyMapEntryEClass = createEClass(STRING_TO_SYNC_POLICY_MAP_ENTRY);
    createEAttribute(stringToSyncPolicyMapEntryEClass, STRING_TO_SYNC_POLICY_MAP_ENTRY__KEY);
    createEAttribute(stringToSyncPolicyMapEntryEClass, STRING_TO_SYNC_POLICY_MAP_ENTRY__VALUE);

    syncDeltaEClass = createEClass(SYNC_DELTA);
    createEAttribute(syncDeltaEClass, SYNC_DELTA__ID);
    createEReference(syncDeltaEClass, SYNC_DELTA__OLD_TASK);
    createEReference(syncDeltaEClass, SYNC_DELTA__NEW_TASK);
    createEAttribute(syncDeltaEClass, SYNC_DELTA__TYPE);

    syncActionEClass = createEClass(SYNC_ACTION);
    createEReference(syncActionEClass, SYNC_ACTION__LOCAL_DELTA);
    createEReference(syncActionEClass, SYNC_ACTION__REMOTE_DELTA);
    createEAttribute(syncActionEClass, SYNC_ACTION__COMPUTED_TYPE);
    createEAttribute(syncActionEClass, SYNC_ACTION__RESOLVED_TYPE);
    createEAttribute(syncActionEClass, SYNC_ACTION__EFFECTIVE_TYPE);

    // Create enums
    syncPolicyEEnum = createEEnum(SYNC_POLICY);
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

    // Obtain other dependent packages
    SetupPackage theSetupPackage = (SetupPackage)EPackage.Registry.INSTANCE.getEPackage(SetupPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    remoteDataEClass.getESuperTypes().add(theSetupPackage.getSetupTaskContainer());

    // Initialize classes and features; add operations and parameters
    initEClass(remoteDataEClass, RemoteData.class, "RemoteData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRemoteData_Policies(), getStringToSyncPolicyMapEntry(), null, "policies", null, 0, -1, RemoteData.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringToSyncPolicyMapEntryEClass, Map.Entry.class, "StringToSyncPolicyMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringToSyncPolicyMapEntry_Key(), ecorePackage.getEString(), "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStringToSyncPolicyMapEntry_Value(), getSyncPolicy(), "value", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncDeltaEClass, SyncDelta.class, "SyncDelta", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSyncDelta_ID(), ecorePackage.getEString(), "iD", null, 1, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncDelta_OldTask(), theSetupPackage.getSetupTask(), null, "oldTask", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncDelta_NewTask(), theSetupPackage.getSetupTask(), null, "newTask", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncDelta_Type(), getSyncDeltaType(), "type", null, 0, 1, SyncDelta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(syncActionEClass, SyncAction.class, "SyncAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSyncAction_LocalDelta(), getSyncDelta(), null, "localDelta", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSyncAction_RemoteDelta(), getSyncDelta(), null, "remoteDelta", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncAction_ComputedType(), getSyncActionType(), "computedType", null, 1, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncAction_ResolvedType(), getSyncActionType(), "resolvedType", null, 0, 1, SyncAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSyncAction_EffectiveType(), getSyncActionType(), "effectiveType", "None", 1, 1, SyncAction.class, !IS_TRANSIENT, IS_VOLATILE,
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(syncPolicyEEnum, SyncPolicy.class, "SyncPolicy");
    addEEnumLiteral(syncPolicyEEnum, SyncPolicy.INCLUDE);
    addEEnumLiteral(syncPolicyEEnum, SyncPolicy.EXCLUDE);

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

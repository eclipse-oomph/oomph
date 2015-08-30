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

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncPolicy;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SyncFactoryImpl extends EFactoryImpl implements SyncFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static SyncFactory init()
  {
    try
    {
      SyncFactory theSyncFactory = (SyncFactory)EPackage.Registry.INSTANCE.getEFactory(SyncPackage.eNS_URI);
      if (theSyncFactory != null)
      {
        return theSyncFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new SyncFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case SyncPackage.REMOTE_DATA:
        return createRemoteData();
      case SyncPackage.STRING_TO_SYNC_POLICY_MAP_ENTRY:
        return (EObject)createStringToSyncPolicyMapEntry();
      case SyncPackage.SYNC_DELTA:
        return createSyncDelta();
      case SyncPackage.SYNC_ACTION:
        return createSyncAction();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case SyncPackage.SYNC_POLICY:
        return createSyncPolicyFromString(eDataType, initialValue);
      case SyncPackage.SYNC_DELTA_TYPE:
        return createSyncDeltaTypeFromString(eDataType, initialValue);
      case SyncPackage.SYNC_ACTION_TYPE:
        return createSyncActionTypeFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case SyncPackage.SYNC_POLICY:
        return convertSyncPolicyToString(eDataType, instanceValue);
      case SyncPackage.SYNC_DELTA_TYPE:
        return convertSyncDeltaTypeToString(eDataType, instanceValue);
      case SyncPackage.SYNC_ACTION_TYPE:
        return convertSyncActionTypeToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteData createRemoteData()
  {
    RemoteDataImpl remoteData = new RemoteDataImpl();
    return remoteData;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map.Entry<String, SyncPolicy> createStringToSyncPolicyMapEntry()
  {
    StringToSyncPolicyMapEntryImpl stringToSyncPolicyMapEntry = new StringToSyncPolicyMapEntryImpl();
    return stringToSyncPolicyMapEntry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta createSyncDelta()
  {
    SyncDeltaImpl syncDelta = new SyncDeltaImpl();
    return syncDelta;
  }

  public SyncDelta createSyncDelta(String id, SetupTask oldTask, SetupTask newTask, SyncDeltaType type)
  {
    SyncDeltaImpl syncDelta = new SyncDeltaImpl();
    syncDelta.setID(id);
    syncDelta.setOldTask(oldTask);
    syncDelta.setNewTask(newTask);
    syncDelta.setType(type);
    return syncDelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncAction createSyncAction()
  {
    SyncActionImpl syncAction = new SyncActionImpl();
    return syncAction;
  }

  public SyncAction createSyncAction(SyncDelta localDelta, SyncDelta remoteDelta, SyncActionType computedType)
  {
    SyncActionImpl syncAction = new SyncActionImpl();
    syncAction.setLocalDelta(localDelta);
    syncAction.setRemoteDelta(remoteDelta);
    syncAction.setComputedType(computedType);
    return syncAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncPolicy createSyncPolicyFromString(EDataType eDataType, String initialValue)
  {
    SyncPolicy result = SyncPolicy.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertSyncPolicyToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDeltaType createSyncDeltaTypeFromString(EDataType eDataType, String initialValue)
  {
    SyncDeltaType result = SyncDeltaType.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertSyncDeltaTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncActionType createSyncActionTypeFromString(EDataType eDataType, String initialValue)
  {
    SyncActionType result = SyncActionType.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertSyncActionTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncPackage getSyncPackage()
  {
    return (SyncPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static SyncPackage getPackage()
  {
    return SyncPackage.eINSTANCE;
  }

} // SyncFactoryImpl

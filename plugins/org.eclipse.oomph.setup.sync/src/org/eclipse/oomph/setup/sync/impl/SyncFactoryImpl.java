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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

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
      case SyncPackage.SYNC_STATE:
        return createSyncState();
      case SyncPackage.SYNC_SNAPSHOT:
        return createSyncSnapshot();
      case SyncPackage.SYNC_ITEM:
        return createSyncItem();
      case SyncPackage.REMOTE_SYNC_ITEM:
        return createRemoteSyncItem();
      case SyncPackage.SYNC_ACTION:
        return createSyncAction();
      case SyncPackage.SYNC_DELTA:
        return createSyncDelta();
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
      case SyncPackage.SYNC_ITEM_TYPE:
        return createSyncItemTypeFromString(eDataType, initialValue);
      case SyncPackage.SYNC_ITEM_POLICY:
        return createSyncItemPolicyFromString(eDataType, initialValue);
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
      case SyncPackage.SYNC_ITEM_TYPE:
        return convertSyncItemTypeToString(eDataType, instanceValue);
      case SyncPackage.SYNC_ITEM_POLICY:
        return convertSyncItemPolicyToString(eDataType, instanceValue);
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
  public SyncState createSyncState()
  {
    SyncStateImpl syncState = new SyncStateImpl();
    return syncState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncSnapshot createSyncSnapshot()
  {
    SyncSnapshotImpl syncSnapshot = new SyncSnapshotImpl();
    return syncSnapshot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItem createSyncItem()
  {
    SyncItemImpl syncItem = new SyncItemImpl();
    return syncItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSyncItem createRemoteSyncItem()
  {
    RemoteSyncItemImpl remoteSyncItem = new RemoteSyncItemImpl();
    return remoteSyncItem;
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

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItemType createSyncItemTypeFromString(EDataType eDataType, String initialValue)
  {
    SyncItemType result = SyncItemType.get(initialValue);
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
  public String convertSyncItemTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItemPolicy createSyncItemPolicyFromString(EDataType eDataType, String initialValue)
  {
    SyncItemPolicy result = SyncItemPolicy.get(initialValue);
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
  public String convertSyncItemPolicyToString(EDataType eDataType, Object instanceValue)
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

/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getLocalDelta <em>Local Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getRemotedelta <em>Remotedelta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getSuggestedType <em>Suggested Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getResolvedType <em>Resolved Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyncActionImpl extends MinimalEObjectImpl.Container implements SyncAction
{
  /**
   * The cached value of the '{@link #getLocalDelta() <em>Local Delta</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocalDelta()
   * @generated
   * @ordered
   */
  protected SyncDelta localDelta;

  /**
   * The cached value of the '{@link #getRemotedelta() <em>Remotedelta</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemotedelta()
   * @generated
   * @ordered
   */
  protected SyncDelta remotedelta;

  /**
   * The default value of the '{@link #getSuggestedType() <em>Suggested Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSuggestedType()
   * @generated
   * @ordered
   */
  protected static final SyncActionType SUGGESTED_TYPE_EDEFAULT = SyncActionType.NONE;

  /**
   * The cached value of the '{@link #getSuggestedType() <em>Suggested Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSuggestedType()
   * @generated
   * @ordered
   */
  protected SyncActionType suggestedType = SUGGESTED_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getResolvedType() <em>Resolved Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedType()
   * @generated
   * @ordered
   */
  protected static final SyncActionType RESOLVED_TYPE_EDEFAULT = SyncActionType.NONE;

  /**
   * The cached value of the '{@link #getResolvedType() <em>Resolved Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedType()
   * @generated
   * @ordered
   */
  protected SyncActionType resolvedType = RESOLVED_TYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SyncActionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SyncPackage.Literals.SYNC_ACTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta getLocalDelta()
  {
    if (localDelta != null && localDelta.eIsProxy())
    {
      InternalEObject oldLocalDelta = (InternalEObject)localDelta;
      localDelta = (SyncDelta)eResolveProxy(oldLocalDelta);
      if (localDelta != oldLocalDelta)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_ACTION__LOCAL_DELTA, oldLocalDelta, localDelta));
        }
      }
    }
    return localDelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta basicGetLocalDelta()
  {
    return localDelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocalDelta(SyncDelta newLocalDelta)
  {
    SyncDelta oldLocalDelta = localDelta;
    localDelta = newLocalDelta;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__LOCAL_DELTA, oldLocalDelta, localDelta));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta getRemotedelta()
  {
    if (remotedelta != null && remotedelta.eIsProxy())
    {
      InternalEObject oldRemotedelta = (InternalEObject)remotedelta;
      remotedelta = (SyncDelta)eResolveProxy(oldRemotedelta);
      if (remotedelta != oldRemotedelta)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_ACTION__REMOTEDELTA, oldRemotedelta, remotedelta));
        }
      }
    }
    return remotedelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta basicGetRemotedelta()
  {
    return remotedelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemotedelta(SyncDelta newRemotedelta)
  {
    SyncDelta oldRemotedelta = remotedelta;
    remotedelta = newRemotedelta;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__REMOTEDELTA, oldRemotedelta, remotedelta));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncActionType getSuggestedType()
  {
    return suggestedType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSuggestedType(SyncActionType newSuggestedType)
  {
    SyncActionType oldSuggestedType = suggestedType;
    suggestedType = newSuggestedType == null ? SUGGESTED_TYPE_EDEFAULT : newSuggestedType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__SUGGESTED_TYPE, oldSuggestedType, suggestedType));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncActionType getResolvedType()
  {
    return resolvedType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setResolvedType(SyncActionType newResolvedType)
  {
    SyncActionType oldResolvedType = resolvedType;
    resolvedType = newResolvedType == null ? RESOLVED_TYPE_EDEFAULT : newResolvedType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__RESOLVED_TYPE, oldResolvedType, resolvedType));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        if (resolve)
        {
          return getLocalDelta();
        }
        return basicGetLocalDelta();
      case SyncPackage.SYNC_ACTION__REMOTEDELTA:
        if (resolve)
        {
          return getRemotedelta();
        }
        return basicGetRemotedelta();
      case SyncPackage.SYNC_ACTION__SUGGESTED_TYPE:
        return getSuggestedType();
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        return getResolvedType();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        setLocalDelta((SyncDelta)newValue);
        return;
      case SyncPackage.SYNC_ACTION__REMOTEDELTA:
        setRemotedelta((SyncDelta)newValue);
        return;
      case SyncPackage.SYNC_ACTION__SUGGESTED_TYPE:
        setSuggestedType((SyncActionType)newValue);
        return;
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        setResolvedType((SyncActionType)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        setLocalDelta((SyncDelta)null);
        return;
      case SyncPackage.SYNC_ACTION__REMOTEDELTA:
        setRemotedelta((SyncDelta)null);
        return;
      case SyncPackage.SYNC_ACTION__SUGGESTED_TYPE:
        setSuggestedType(SUGGESTED_TYPE_EDEFAULT);
        return;
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        setResolvedType(RESOLVED_TYPE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        return localDelta != null;
      case SyncPackage.SYNC_ACTION__REMOTEDELTA:
        return remotedelta != null;
      case SyncPackage.SYNC_ACTION__SUGGESTED_TYPE:
        return suggestedType != SUGGESTED_TYPE_EDEFAULT;
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        return resolvedType != RESOLVED_TYPE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (suggestedType: ");
    result.append(suggestedType);
    result.append(", resolvedType: ");
    result.append(resolvedType);
    result.append(')');
    return result.toString();
  }

} // SyncActionImpl

/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getLocalDelta <em>Local Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getRemoteDelta <em>Remote Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getComputedType <em>Computed Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getResolvedType <em>Resolved Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncActionImpl#getEffectiveType <em>Effective Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyncActionImpl extends MinimalEObjectImpl.Container implements SyncAction
{
  /**
   * The default value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

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
   * The cached value of the '{@link #getRemoteDelta() <em>Remote Delta</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteDelta()
   * @generated
   * @ordered
   */
  protected SyncDelta remoteDelta;

  /**
   * The default value of the '{@link #getComputedType() <em>Computed Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComputedType()
   * @generated
   * @ordered
   */
  protected static final SyncActionType COMPUTED_TYPE_EDEFAULT = SyncActionType.NONE;

  /**
   * The cached value of the '{@link #getComputedType() <em>Computed Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComputedType()
   * @generated
   * @ordered
   */
  protected SyncActionType computedType = COMPUTED_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getResolvedType() <em>Resolved Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedType()
   * @generated NOT
   * @ordered
   */
  protected static final SyncActionType RESOLVED_TYPE_EDEFAULT = null;

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
   * The default value of the '{@link #getEffectiveType() <em>Effective Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEffectiveType()
   * @generated
   * @ordered
   */
  protected static final SyncActionType EFFECTIVE_TYPE_EDEFAULT = SyncActionType.NONE;

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
   * @generated NOT
   */
  public String getID()
  {
    if (localDelta != null)
    {
      String id = localDelta.getID();
      if (id != null)
      {
        return id;
      }
    }

    if (remoteDelta != null)
    {
      String id = remoteDelta.getID();
      if (id != null)
      {
        return id;
      }
    }

    return null;
  }

  /**
   * @ADDED
   */
  public Map.Entry<String, String> getPreference()
  {
    SyncDelta delta = getLocalDelta();
    if (delta == null)
    {
      delta = getRemoteDelta();
    }

    if (delta != null)
    {
      SetupTask task = delta.getNewTask();
      if (task == null)
      {
        task = delta.getOldTask();
      }

      if (task instanceof PreferenceTask)
      {
        final PreferenceTask preferenceTask = (PreferenceTask)task;
        return new Map.Entry<String, String>()
        {
          public String getKey()
          {
            return preferenceTask.getKey();
          }

          public String getValue()
          {
            return preferenceTask.getValue();
          }

          public String setValue(String value)
          {
            throw new UnsupportedOperationException();
          }
        };
      }
    }

    return null;
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
  public SyncDelta getRemoteDelta()
  {
    if (remoteDelta != null && remoteDelta.eIsProxy())
    {
      InternalEObject oldRemoteDelta = (InternalEObject)remoteDelta;
      remoteDelta = (SyncDelta)eResolveProxy(oldRemoteDelta);
      if (remoteDelta != oldRemoteDelta)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_ACTION__REMOTE_DELTA, oldRemoteDelta, remoteDelta));
        }
      }
    }
    return remoteDelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncDelta basicGetRemoteDelta()
  {
    return remoteDelta;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteDelta(SyncDelta newRemoteDelta)
  {
    SyncDelta oldRemoteDelta = remoteDelta;
    remoteDelta = newRemoteDelta;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__REMOTE_DELTA, oldRemoteDelta, remoteDelta));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncActionType getComputedType()
  {
    return computedType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComputedType(SyncActionType newComputedType)
  {
    SyncActionType oldComputedType = computedType;
    computedType = newComputedType == null ? COMPUTED_TYPE_EDEFAULT : newComputedType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_ACTION__COMPUTED_TYPE, oldComputedType, computedType));
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
   * @generated NOT
   */
  public SyncActionType getEffectiveType()
  {
    if (resolvedType != null)
    {
      return resolvedType;
    }

    return computedType;
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
      case SyncPackage.SYNC_ACTION__ID:
        return getID();
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        if (resolve)
        {
          return getLocalDelta();
        }
        return basicGetLocalDelta();
      case SyncPackage.SYNC_ACTION__REMOTE_DELTA:
        if (resolve)
        {
          return getRemoteDelta();
        }
        return basicGetRemoteDelta();
      case SyncPackage.SYNC_ACTION__COMPUTED_TYPE:
        return getComputedType();
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        return getResolvedType();
      case SyncPackage.SYNC_ACTION__EFFECTIVE_TYPE:
        return getEffectiveType();
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
      case SyncPackage.SYNC_ACTION__REMOTE_DELTA:
        setRemoteDelta((SyncDelta)newValue);
        return;
      case SyncPackage.SYNC_ACTION__COMPUTED_TYPE:
        setComputedType((SyncActionType)newValue);
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
      case SyncPackage.SYNC_ACTION__REMOTE_DELTA:
        setRemoteDelta((SyncDelta)null);
        return;
      case SyncPackage.SYNC_ACTION__COMPUTED_TYPE:
        setComputedType(COMPUTED_TYPE_EDEFAULT);
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
      case SyncPackage.SYNC_ACTION__ID:
        return ID_EDEFAULT == null ? getID() != null : !ID_EDEFAULT.equals(getID());
      case SyncPackage.SYNC_ACTION__LOCAL_DELTA:
        return localDelta != null;
      case SyncPackage.SYNC_ACTION__REMOTE_DELTA:
        return remoteDelta != null;
      case SyncPackage.SYNC_ACTION__COMPUTED_TYPE:
        return computedType != COMPUTED_TYPE_EDEFAULT;
      case SyncPackage.SYNC_ACTION__RESOLVED_TYPE:
        return resolvedType != RESOLVED_TYPE_EDEFAULT;
      case SyncPackage.SYNC_ACTION__EFFECTIVE_TYPE:
        return getEffectiveType() != EFFECTIVE_TYPE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (id: ");
    result.append(getID());
    result.append(", computedType: ");
    result.append(computedType);
    result.append(", resolvedType: ");
    result.append(resolvedType);

    Map.Entry<String, String> preference = getPreference();
    if (preference != null)
    {
      result.append(", ");
      result.append(preference.getKey());
      result.append(": ");
      result.append(preference.getValue());
    }

    result.append(')');
    return result.toString();
  }

} // SyncActionImpl

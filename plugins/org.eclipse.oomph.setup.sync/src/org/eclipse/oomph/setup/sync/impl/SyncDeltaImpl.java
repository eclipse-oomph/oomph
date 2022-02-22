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

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delta</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getOldTask <em>Old Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getNewTask <em>New Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyncDeltaImpl extends MinimalEObjectImpl.Container implements SyncDelta
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
   * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected String iD = ID_EDEFAULT;

  /**
   * The cached value of the '{@link #getOldTask() <em>Old Task</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOldTask()
   * @generated
   * @ordered
   */
  protected SetupTask oldTask;

  /**
   * The cached value of the '{@link #getNewTask() <em>New Task</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNewTask()
   * @generated
   * @ordered
   */
  protected SetupTask newTask;

  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final SyncDeltaType TYPE_EDEFAULT = SyncDeltaType.UNCHANGED;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected SyncDeltaType type = TYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SyncDeltaImpl()
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
    return SyncPackage.Literals.SYNC_DELTA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getID()
  {
    return iD;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setID(String newID)
  {
    String oldID = iD;
    iD = newID;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__ID, oldID, iD));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SetupTask getOldTask()
  {
    if (oldTask != null && oldTask.eIsProxy())
    {
      InternalEObject oldOldTask = (InternalEObject)oldTask;
      oldTask = (SetupTask)eResolveProxy(oldOldTask);
      if (oldTask != oldOldTask)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_DELTA__OLD_TASK, oldOldTask, oldTask));
        }
      }
    }
    return oldTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTask basicGetOldTask()
  {
    return oldTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOldTask(SetupTask newOldTask)
  {
    SetupTask oldOldTask = oldTask;
    oldTask = newOldTask;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__OLD_TASK, oldOldTask, oldTask));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SetupTask getNewTask()
  {
    if (newTask != null && newTask.eIsProxy())
    {
      InternalEObject oldNewTask = (InternalEObject)newTask;
      newTask = (SetupTask)eResolveProxy(oldNewTask);
      if (newTask != oldNewTask)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_DELTA__NEW_TASK, oldNewTask, newTask));
        }
      }
    }
    return newTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTask basicGetNewTask()
  {
    return newTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNewTask(SetupTask newNewTask)
  {
    SetupTask oldNewTask = newTask;
    newTask = newNewTask;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__NEW_TASK, oldNewTask, newTask));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SyncDeltaType getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(SyncDeltaType newType)
  {
    SyncDeltaType oldType = type;
    type = newType == null ? TYPE_EDEFAULT : newType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__TYPE, oldType, type));
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
      case SyncPackage.SYNC_DELTA__ID:
        return getID();
      case SyncPackage.SYNC_DELTA__OLD_TASK:
        if (resolve)
        {
          return getOldTask();
        }
        return basicGetOldTask();
      case SyncPackage.SYNC_DELTA__NEW_TASK:
        if (resolve)
        {
          return getNewTask();
        }
        return basicGetNewTask();
      case SyncPackage.SYNC_DELTA__TYPE:
        return getType();
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
      case SyncPackage.SYNC_DELTA__ID:
        setID((String)newValue);
        return;
      case SyncPackage.SYNC_DELTA__OLD_TASK:
        setOldTask((SetupTask)newValue);
        return;
      case SyncPackage.SYNC_DELTA__NEW_TASK:
        setNewTask((SetupTask)newValue);
        return;
      case SyncPackage.SYNC_DELTA__TYPE:
        setType((SyncDeltaType)newValue);
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
      case SyncPackage.SYNC_DELTA__ID:
        setID(ID_EDEFAULT);
        return;
      case SyncPackage.SYNC_DELTA__OLD_TASK:
        setOldTask((SetupTask)null);
        return;
      case SyncPackage.SYNC_DELTA__NEW_TASK:
        setNewTask((SetupTask)null);
        return;
      case SyncPackage.SYNC_DELTA__TYPE:
        setType(TYPE_EDEFAULT);
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
      case SyncPackage.SYNC_DELTA__ID:
        return ID_EDEFAULT == null ? iD != null : !ID_EDEFAULT.equals(iD);
      case SyncPackage.SYNC_DELTA__OLD_TASK:
        return oldTask != null;
      case SyncPackage.SYNC_DELTA__NEW_TASK:
        return newTask != null;
      case SyncPackage.SYNC_DELTA__TYPE:
        return type != TYPE_EDEFAULT;
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (iD: "); //$NON-NLS-1$
    result.append(iD);
    result.append(", type: "); //$NON-NLS-1$
    result.append(type);
    result.append(')');
    return result.toString();
  }

} // SyncDeltaImpl

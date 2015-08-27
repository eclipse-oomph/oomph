/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncItem;
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
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getOldItem <em>Old Item</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getNewItem <em>New Item</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncDeltaImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyncDeltaImpl extends MinimalEObjectImpl.Container implements SyncDelta
{
  /**
   * The cached value of the '{@link #getOldItem() <em>Old Item</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOldItem()
   * @generated
   * @ordered
   */
  protected SyncItem oldItem;

  /**
   * The cached value of the '{@link #getNewItem() <em>New Item</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNewItem()
   * @generated
   * @ordered
   */
  protected SyncItem newItem;

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
  public SyncItem getOldItem()
  {
    if (oldItem != null && oldItem.eIsProxy())
    {
      InternalEObject oldOldItem = (InternalEObject)oldItem;
      oldItem = (SyncItem)eResolveProxy(oldOldItem);
      if (oldItem != oldOldItem)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_DELTA__OLD_ITEM, oldOldItem, oldItem));
        }
      }
    }
    return oldItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItem basicGetOldItem()
  {
    return oldItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOldItem(SyncItem newOldItem)
  {
    SyncItem oldOldItem = oldItem;
    oldItem = newOldItem;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__OLD_ITEM, oldOldItem, oldItem));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItem getNewItem()
  {
    if (newItem != null && newItem.eIsProxy())
    {
      InternalEObject oldNewItem = (InternalEObject)newItem;
      newItem = (SyncItem)eResolveProxy(oldNewItem);
      if (newItem != oldNewItem)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_DELTA__NEW_ITEM, oldNewItem, newItem));
        }
      }
    }
    return newItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItem basicGetNewItem()
  {
    return newItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNewItem(SyncItem newNewItem)
  {
    SyncItem oldNewItem = newItem;
    newItem = newNewItem;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_DELTA__NEW_ITEM, oldNewItem, newItem));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
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
      case SyncPackage.SYNC_DELTA__OLD_ITEM:
        if (resolve)
        {
          return getOldItem();
        }
        return basicGetOldItem();
      case SyncPackage.SYNC_DELTA__NEW_ITEM:
        if (resolve)
        {
          return getNewItem();
        }
        return basicGetNewItem();
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
      case SyncPackage.SYNC_DELTA__OLD_ITEM:
        setOldItem((SyncItem)newValue);
        return;
      case SyncPackage.SYNC_DELTA__NEW_ITEM:
        setNewItem((SyncItem)newValue);
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
      case SyncPackage.SYNC_DELTA__OLD_ITEM:
        setOldItem((SyncItem)null);
        return;
      case SyncPackage.SYNC_DELTA__NEW_ITEM:
        setNewItem((SyncItem)null);
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
      case SyncPackage.SYNC_DELTA__OLD_ITEM:
        return oldItem != null;
      case SyncPackage.SYNC_DELTA__NEW_ITEM:
        return newItem != null;
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

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (type: ");
    result.append(type);
    result.append(')');
    return result.toString();
  }

} // SyncDeltaImpl

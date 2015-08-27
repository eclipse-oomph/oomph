/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.sync.RemoteSyncItem;
import org.eclipse.oomph.setup.sync.SyncItemPolicy;
import org.eclipse.oomph.setup.sync.SyncPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Sync Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.RemoteSyncItemImpl#getPolicy <em>Policy</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RemoteSyncItemImpl extends SyncItemImpl implements RemoteSyncItem
{
  /**
   * The default value of the '{@link #getPolicy() <em>Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPolicy()
   * @generated
   * @ordered
   */
  protected static final SyncItemPolicy POLICY_EDEFAULT = SyncItemPolicy.EXCLUDE;

  /**
   * The cached value of the '{@link #getPolicy() <em>Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPolicy()
   * @generated
   * @ordered
   */
  protected SyncItemPolicy policy = POLICY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RemoteSyncItemImpl()
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
    return SyncPackage.Literals.REMOTE_SYNC_ITEM;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncItemPolicy getPolicy()
  {
    return policy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPolicy(SyncItemPolicy newPolicy)
  {
    SyncItemPolicy oldPolicy = policy;
    policy = newPolicy == null ? POLICY_EDEFAULT : newPolicy;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.REMOTE_SYNC_ITEM__POLICY, oldPolicy, policy));
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
      case SyncPackage.REMOTE_SYNC_ITEM__POLICY:
        return getPolicy();
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
      case SyncPackage.REMOTE_SYNC_ITEM__POLICY:
        setPolicy((SyncItemPolicy)newValue);
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
      case SyncPackage.REMOTE_SYNC_ITEM__POLICY:
        setPolicy(POLICY_EDEFAULT);
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
      case SyncPackage.REMOTE_SYNC_ITEM__POLICY:
        return policy != POLICY_EDEFAULT;
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
    result.append(" (policy: ");
    result.append(policy);
    result.append(')');
    return result.toString();
  }

} // RemoteSyncItemImpl

/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.impl.SetupTaskContainerImpl;
import org.eclipse.oomph.setup.sync.RemoteSnapshot;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncPolicy;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Snapshot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.RemoteSnapshotImpl#getPolicies <em>Policies</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RemoteSnapshotImpl extends SetupTaskContainerImpl implements RemoteSnapshot
{
  /**
   * The cached value of the '{@link #getPolicies() <em>Policies</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPolicies()
   * @generated
   * @ordered
   */
  protected EMap<String, SyncPolicy> policies;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RemoteSnapshotImpl()
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
    return SyncPackage.Literals.REMOTE_SNAPSHOT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EMap<String, SyncPolicy> getPolicies()
  {
    if (policies == null)
    {
      policies = new EcoreEMap<String, SyncPolicy>(SyncPackage.Literals.STRING_TO_SYNC_POLICY_MAP_ENTRY, StringToSyncPolicyMapEntryImpl.class, this,
          SyncPackage.REMOTE_SNAPSHOT__POLICIES);
    }
    return policies;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SyncPackage.REMOTE_SNAPSHOT__POLICIES:
        return ((InternalEList<?>)getPolicies()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case SyncPackage.REMOTE_SNAPSHOT__POLICIES:
        if (coreType)
        {
          return getPolicies();
        }
        else
        {
          return getPolicies().map();
        }
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
      case SyncPackage.REMOTE_SNAPSHOT__POLICIES:
        ((EStructuralFeature.Setting)getPolicies()).set(newValue);
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
      case SyncPackage.REMOTE_SNAPSHOT__POLICIES:
        getPolicies().clear();
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
      case SyncPackage.REMOTE_SNAPSHOT__POLICIES:
        return policies != null && !policies.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // RemoteSnapshotImpl

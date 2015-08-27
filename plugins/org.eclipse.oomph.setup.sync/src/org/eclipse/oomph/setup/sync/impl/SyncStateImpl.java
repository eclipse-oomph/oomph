/**
 */
package org.eclipse.oomph.setup.sync.impl;

import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncSnapshot;
import org.eclipse.oomph.setup.sync.SyncState;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl#getLocalTimeStamp <em>Local Time Stamp</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl#getLocalSnapshot <em>Local Snapshot</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl#getRemoteTimeStamp <em>Remote Time Stamp</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.impl.SyncStateImpl#getRemoteSnapshot <em>Remote Snapshot</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyncStateImpl extends MinimalEObjectImpl.Container implements SyncState
{
  /**
   * The default value of the '{@link #getLocalTimeStamp() <em>Local Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocalTimeStamp()
   * @generated
   * @ordered
   */
  protected static final long LOCAL_TIME_STAMP_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getLocalTimeStamp() <em>Local Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocalTimeStamp()
   * @generated
   * @ordered
   */
  protected long localTimeStamp = LOCAL_TIME_STAMP_EDEFAULT;

  /**
   * The cached value of the '{@link #getLocalSnapshot() <em>Local Snapshot</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocalSnapshot()
   * @generated
   * @ordered
   */
  protected SyncSnapshot localSnapshot;

  /**
   * The default value of the '{@link #getRemoteTimeStamp() <em>Remote Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteTimeStamp()
   * @generated
   * @ordered
   */
  protected static final long REMOTE_TIME_STAMP_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getRemoteTimeStamp() <em>Remote Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteTimeStamp()
   * @generated
   * @ordered
   */
  protected long remoteTimeStamp = REMOTE_TIME_STAMP_EDEFAULT;

  /**
   * The cached value of the '{@link #getRemoteSnapshot() <em>Remote Snapshot</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteSnapshot()
   * @generated
   * @ordered
   */
  protected SyncSnapshot remoteSnapshot;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SyncStateImpl()
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
    return SyncPackage.Literals.SYNC_STATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getLocalTimeStamp()
  {
    return localTimeStamp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocalTimeStamp(long newLocalTimeStamp)
  {
    long oldLocalTimeStamp = localTimeStamp;
    localTimeStamp = newLocalTimeStamp;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_STATE__LOCAL_TIME_STAMP, oldLocalTimeStamp, localTimeStamp));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncSnapshot getLocalSnapshot()
  {
    if (localSnapshot != null && localSnapshot.eIsProxy())
    {
      InternalEObject oldLocalSnapshot = (InternalEObject)localSnapshot;
      localSnapshot = (SyncSnapshot)eResolveProxy(oldLocalSnapshot);
      if (localSnapshot != oldLocalSnapshot)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT, oldLocalSnapshot, localSnapshot));
        }
      }
    }
    return localSnapshot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncSnapshot basicGetLocalSnapshot()
  {
    return localSnapshot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocalSnapshot(SyncSnapshot newLocalSnapshot)
  {
    SyncSnapshot oldLocalSnapshot = localSnapshot;
    localSnapshot = newLocalSnapshot;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT, oldLocalSnapshot, localSnapshot));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getRemoteTimeStamp()
  {
    return remoteTimeStamp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteTimeStamp(long newRemoteTimeStamp)
  {
    long oldRemoteTimeStamp = remoteTimeStamp;
    remoteTimeStamp = newRemoteTimeStamp;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_STATE__REMOTE_TIME_STAMP, oldRemoteTimeStamp, remoteTimeStamp));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncSnapshot getRemoteSnapshot()
  {
    if (remoteSnapshot != null && remoteSnapshot.eIsProxy())
    {
      InternalEObject oldRemoteSnapshot = (InternalEObject)remoteSnapshot;
      remoteSnapshot = (SyncSnapshot)eResolveProxy(oldRemoteSnapshot);
      if (remoteSnapshot != oldRemoteSnapshot)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT, oldRemoteSnapshot, remoteSnapshot));
        }
      }
    }
    return remoteSnapshot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncSnapshot basicGetRemoteSnapshot()
  {
    return remoteSnapshot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteSnapshot(SyncSnapshot newRemoteSnapshot)
  {
    SyncSnapshot oldRemoteSnapshot = remoteSnapshot;
    remoteSnapshot = newRemoteSnapshot;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT, oldRemoteSnapshot, remoteSnapshot));
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
      case SyncPackage.SYNC_STATE__LOCAL_TIME_STAMP:
        return getLocalTimeStamp();
      case SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT:
        if (resolve)
        {
          return getLocalSnapshot();
        }
        return basicGetLocalSnapshot();
      case SyncPackage.SYNC_STATE__REMOTE_TIME_STAMP:
        return getRemoteTimeStamp();
      case SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT:
        if (resolve)
        {
          return getRemoteSnapshot();
        }
        return basicGetRemoteSnapshot();
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
      case SyncPackage.SYNC_STATE__LOCAL_TIME_STAMP:
        setLocalTimeStamp((Long)newValue);
        return;
      case SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT:
        setLocalSnapshot((SyncSnapshot)newValue);
        return;
      case SyncPackage.SYNC_STATE__REMOTE_TIME_STAMP:
        setRemoteTimeStamp((Long)newValue);
        return;
      case SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT:
        setRemoteSnapshot((SyncSnapshot)newValue);
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
      case SyncPackage.SYNC_STATE__LOCAL_TIME_STAMP:
        setLocalTimeStamp(LOCAL_TIME_STAMP_EDEFAULT);
        return;
      case SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT:
        setLocalSnapshot((SyncSnapshot)null);
        return;
      case SyncPackage.SYNC_STATE__REMOTE_TIME_STAMP:
        setRemoteTimeStamp(REMOTE_TIME_STAMP_EDEFAULT);
        return;
      case SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT:
        setRemoteSnapshot((SyncSnapshot)null);
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
      case SyncPackage.SYNC_STATE__LOCAL_TIME_STAMP:
        return localTimeStamp != LOCAL_TIME_STAMP_EDEFAULT;
      case SyncPackage.SYNC_STATE__LOCAL_SNAPSHOT:
        return localSnapshot != null;
      case SyncPackage.SYNC_STATE__REMOTE_TIME_STAMP:
        return remoteTimeStamp != REMOTE_TIME_STAMP_EDEFAULT;
      case SyncPackage.SYNC_STATE__REMOTE_SNAPSHOT:
        return remoteSnapshot != null;
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
    result.append(" (localTimeStamp: ");
    result.append(localTimeStamp);
    result.append(", remoteTimeStamp: ");
    result.append(remoteTimeStamp);
    result.append(')');
    return result.toString();
  }

} // SyncStateImpl

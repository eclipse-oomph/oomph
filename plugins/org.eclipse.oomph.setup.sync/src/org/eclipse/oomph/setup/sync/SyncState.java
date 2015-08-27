/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncState#getLocalTimeStamp <em>Local Time Stamp</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncState#getLocalSnapshot <em>Local Snapshot</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteTimeStamp <em>Remote Time Stamp</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteSnapshot <em>Remote Snapshot</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncState()
 * @model
 * @generated
 */
public interface SyncState extends EObject
{
  /**
   * Returns the value of the '<em><b>Local Time Stamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Local Time Stamp</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Local Time Stamp</em>' attribute.
   * @see #setLocalTimeStamp(long)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncState_LocalTimeStamp()
   * @model required="true"
   * @generated
   */
  long getLocalTimeStamp();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncState#getLocalTimeStamp <em>Local Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Local Time Stamp</em>' attribute.
   * @see #getLocalTimeStamp()
   * @generated
   */
  void setLocalTimeStamp(long value);

  /**
   * Returns the value of the '<em><b>Local Snapshot</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Local Snapshot</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Local Snapshot</em>' reference.
   * @see #setLocalSnapshot(SyncSnapshot)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncState_LocalSnapshot()
   * @model required="true"
   * @generated
   */
  SyncSnapshot getLocalSnapshot();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncState#getLocalSnapshot <em>Local Snapshot</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Local Snapshot</em>' reference.
   * @see #getLocalSnapshot()
   * @generated
   */
  void setLocalSnapshot(SyncSnapshot value);

  /**
   * Returns the value of the '<em><b>Remote Time Stamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote Time Stamp</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote Time Stamp</em>' attribute.
   * @see #setRemoteTimeStamp(long)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncState_RemoteTimeStamp()
   * @model required="true"
   * @generated
   */
  long getRemoteTimeStamp();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteTimeStamp <em>Remote Time Stamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote Time Stamp</em>' attribute.
   * @see #getRemoteTimeStamp()
   * @generated
   */
  void setRemoteTimeStamp(long value);

  /**
   * Returns the value of the '<em><b>Remote Snapshot</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote Snapshot</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote Snapshot</em>' reference.
   * @see #setRemoteSnapshot(SyncSnapshot)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncState_RemoteSnapshot()
   * @model required="true"
   * @generated
   */
  SyncSnapshot getRemoteSnapshot();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncState#getRemoteSnapshot <em>Remote Snapshot</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote Snapshot</em>' reference.
   * @see #getRemoteSnapshot()
   * @generated
   */
  void setRemoteSnapshot(SyncSnapshot value);

} // SyncState

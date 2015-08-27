/**
 */
package org.eclipse.oomph.setup.sync;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Sync Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.RemoteSyncItem#getPolicy <em>Policy</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getRemoteSyncItem()
 * @model
 * @generated
 */
public interface RemoteSyncItem extends SyncItem
{
  /**
   * Returns the value of the '<em><b>Policy</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncItemPolicy}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Policy</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Policy</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncItemPolicy
   * @see #setPolicy(SyncItemPolicy)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getRemoteSyncItem_Policy()
   * @model
   * @generated
   */
  SyncItemPolicy getPolicy();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.RemoteSyncItem#getPolicy <em>Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Policy</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncItemPolicy
   * @see #getPolicy()
   * @generated
   */
  void setPolicy(SyncItemPolicy value);

} // RemoteSyncItem

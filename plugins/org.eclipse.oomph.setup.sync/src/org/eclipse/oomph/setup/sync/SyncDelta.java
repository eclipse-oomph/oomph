/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delta</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncDelta#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta()
 * @model
 * @generated
 */
public interface SyncDelta extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncDeltaType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @see #setType(SyncDeltaType)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta_Type()
   * @model
   * @generated
   */
  SyncDeltaType getType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncDelta#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @see #getType()
   * @generated
   */
  void setType(SyncDeltaType value);

} // SyncDelta

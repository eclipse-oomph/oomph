/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncItem#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncItem#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncItem#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncItem()
 * @model
 * @generated
 */
public interface SyncItem extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncItemType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncItemType
   * @see #setType(SyncItemType)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncItem_Type()
   * @model required="true"
   * @generated
   */
  SyncItemType getType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncItem#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncItemType
   * @see #getType()
   * @generated
   */
  void setType(SyncItemType value);

  /**
   * Returns the value of the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Key</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Key</em>' attribute.
   * @see #setKey(String)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncItem_Key()
   * @model required="true"
   * @generated
   */
  String getKey();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncItem#getKey <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Key</em>' attribute.
   * @see #getKey()
   * @generated
   */
  void setKey(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncItem_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncItem#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // SyncItem

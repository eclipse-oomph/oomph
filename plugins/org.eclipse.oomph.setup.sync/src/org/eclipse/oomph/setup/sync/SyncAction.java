/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getLocalDelta <em>Local Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getRemotedelta <em>Remotedelta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getSuggestedType <em>Suggested Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getResolvedType <em>Resolved Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction()
 * @model
 * @generated
 */
public interface SyncAction extends EObject
{
  /**
   * Returns the value of the '<em><b>Local Delta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Local Delta</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Local Delta</em>' reference.
   * @see #setLocalDelta(SyncDelta)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_LocalDelta()
   * @model
   * @generated
   */
  SyncDelta getLocalDelta();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncAction#getLocalDelta <em>Local Delta</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Local Delta</em>' reference.
   * @see #getLocalDelta()
   * @generated
   */
  void setLocalDelta(SyncDelta value);

  /**
   * Returns the value of the '<em><b>Remotedelta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remotedelta</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remotedelta</em>' reference.
   * @see #setRemotedelta(SyncDelta)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_Remotedelta()
   * @model
   * @generated
   */
  SyncDelta getRemotedelta();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncAction#getRemotedelta <em>Remotedelta</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remotedelta</em>' reference.
   * @see #getRemotedelta()
   * @generated
   */
  void setRemotedelta(SyncDelta value);

  /**
   * Returns the value of the '<em><b>Suggested Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncActionType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Suggested Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Suggested Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #setSuggestedType(SyncActionType)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_SuggestedType()
   * @model required="true"
   * @generated
   */
  SyncActionType getSuggestedType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncAction#getSuggestedType <em>Suggested Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Suggested Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #getSuggestedType()
   * @generated
   */
  void setSuggestedType(SyncActionType value);

  /**
   * Returns the value of the '<em><b>Resolved Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncActionType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Resolved Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #setResolvedType(SyncActionType)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_ResolvedType()
   * @model
   * @generated
   */
  SyncActionType getResolvedType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncAction#getResolvedType <em>Resolved Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #getResolvedType()
   * @generated
   */
  void setResolvedType(SyncActionType value);

} // SyncAction

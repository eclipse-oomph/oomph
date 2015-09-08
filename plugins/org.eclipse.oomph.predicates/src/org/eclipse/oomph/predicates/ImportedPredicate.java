/**
 */
package org.eclipse.oomph.predicates;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imported Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.ImportedPredicate#isAccessible <em>Accessible</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getImportedPredicate()
 * @model
 * @generated
 */
public interface ImportedPredicate extends Predicate
{

  /**
   * Returns the value of the '<em><b>Accessible</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Accessible</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Accessible</em>' attribute.
   * @see #setAccessible(boolean)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getImportedPredicate_Accessible()
   * @model default="true"
   * @generated
   */
  boolean isAccessible();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.ImportedPredicate#isAccessible <em>Accessible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Accessible</em>' attribute.
   * @see #isAccessible()
   * @generated
   */
  void setAccessible(boolean value);
} // ImportedPredicate

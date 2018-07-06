/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Builder Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.BuilderPredicate#getBuilder <em>Builder</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getBuilderPredicate()
 * @model
 * @generated
 */
public interface BuilderPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Builder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Builder</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Builder</em>' attribute.
   * @see #setBuilder(String)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getBuilderPredicate_Builder()
   * @model required="true"
   * @generated
   */
  String getBuilder();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.BuilderPredicate#getBuilder <em>Builder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Builder</em>' attribute.
   * @see #getBuilder()
   * @generated
   */
  void setBuilder(String value);

} // BuilderPredicate

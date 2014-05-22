/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Nature Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.NaturePredicate#getNature <em>Nature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getNaturePredicate()
 * @model
 * @generated
 */
public interface NaturePredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Nature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Nature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nature</em>' attribute.
   * @see #setNature(String)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getNaturePredicate_Nature()
   * @model required="true"
   * @generated
   */
  String getNature();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.NaturePredicate#getNature <em>Nature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nature</em>' attribute.
   * @see #getNature()
   * @generated
   */
  void setNature(String value);

} // NaturePredicate

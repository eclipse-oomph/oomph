/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>And Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.AndPredicate#getOperands <em>Operands</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getAndPredicate()
 * @model
 * @generated
 */
public interface AndPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Operands</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.predicates.Predicate}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operands</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operands</em>' containment reference list.
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getAndPredicate_Operands()
   * @model containment="true"
   *        extendedMetaData="name='operand'"
   * @generated
   */
  EList<Predicate> getOperands();

} // AndPredicate

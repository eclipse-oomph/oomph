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
package org.eclipse.oomph.workingsets;

import org.eclipse.oomph.predicates.Predicate;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inclusion Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.workingsets.InclusionPredicate#getIncludedWorkingSets <em>Included Working Sets</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.workingsets.WorkingSetsPackage#getInclusionPredicate()
 * @model
 * @generated
 */
public interface InclusionPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Included Working Sets</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.workingsets.WorkingSet}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Included Working Sets</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Included Working Sets</em>' reference list.
   * @see org.eclipse.oomph.workingsets.WorkingSetsPackage#getInclusionPredicate_IncludedWorkingSets()
   * @model extendedMetaData="name='includedWorkingSet'"
   * @generated
   */
  EList<WorkingSet> getIncludedWorkingSets();

} // InclusionPredicate

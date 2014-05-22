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
package org.eclipse.oomph.projectconfig;

import org.eclipse.oomph.predicates.Predicate;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inclusion Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.InclusionPredicate#getIncludedPreferenceProfiles <em>Included Preference Profiles</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getInclusionPredicate()
 * @model
 * @generated
 */
public interface InclusionPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Included Preference Profiles</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.PreferenceProfile}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Included Preference Profiles</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Included Preference Profiles</em>' reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getInclusionPredicate_IncludedPreferenceProfiles()
   * @model
   * @generated
   */
  EList<PreferenceProfile> getIncludedPreferenceProfiles();

} // InclusionPredicate

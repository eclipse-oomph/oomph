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
package org.eclipse.oomph.projectconfig;

import org.eclipse.oomph.predicates.Predicate;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exclusion Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.ExclusionPredicate#getExcludedPreferenceProfiles <em>Excluded Preference Profiles</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getExclusionPredicate()
 * @model
 * @generated
 */
public interface ExclusionPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Excluded Preference Profiles</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.PreferenceProfile}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Excluded Preference Profiles</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Excluded Preference Profiles</em>' reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getExclusionPredicate_ExcludedPreferenceProfiles()
   * @model
   * @generated
   */
  EList<PreferenceProfile> getExcludedPreferenceProfiles();

} // ExclusionPredicate

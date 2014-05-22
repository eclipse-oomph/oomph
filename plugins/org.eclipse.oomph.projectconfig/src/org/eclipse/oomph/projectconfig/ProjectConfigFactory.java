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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage
 * @generated
 */
public interface ProjectConfigFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ProjectConfigFactory eINSTANCE = org.eclipse.oomph.projectconfig.impl.ProjectConfigFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Workspace Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Workspace Configuration</em>'.
   * @generated
   */
  WorkspaceConfiguration createWorkspaceConfiguration();

  /**
   * Returns a new object of class '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Project</em>'.
   * @generated
   */
  Project createProject();

  /**
   * Returns a new object of class '<em>Preference Profile</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Preference Profile</em>'.
   * @generated
   */
  PreferenceProfile createPreferenceProfile();

  /**
   * Returns a new object of class '<em>Preference Filter</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Preference Filter</em>'.
   * @generated
   */
  PreferenceFilter createPreferenceFilter();

  /**
   * Returns a new object of class '<em>Inclusion Predicate</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Inclusion Predicate</em>'.
   * @generated
   */
  InclusionPredicate createInclusionPredicate();

  /**
   * Returns a new object of class '<em>Exclusion Predicate</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Exclusion Predicate</em>'.
   * @generated
   */
  ExclusionPredicate createExclusionPredicate();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ProjectConfigPackage getProjectConfigPackage();

} // ProjectConfigFactory

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

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.preferences.Property;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Preference Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getPreferenceFilters <em>Preference Filters</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getReferentProjects <em>Referent Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getProject <em>Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getPredicates <em>Predicates</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile()
 * @model
 * @generated
 */
public interface PreferenceProfile extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Preference Filters</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.PreferenceFilter}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.PreferenceFilter#getPreferenceProfile <em>Preference Profile</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Preference Filters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preference Filters</em>' containment reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile_PreferenceFilters()
   * @see org.eclipse.oomph.projectconfig.PreferenceFilter#getPreferenceProfile
   * @model opposite="preferenceProfile" containment="true"
   *        extendedMetaData="name='preferenceFilter'"
   * @generated
   */
  EList<PreferenceFilter> getPreferenceFilters();

  /**
   * Returns the value of the '<em><b>Referent Projects</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.Project}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.Project#getPreferenceProfileReferences <em>Preference Profile References</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Referent Projects</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Referent Projects</em>' reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile_ReferentProjects()
   * @see org.eclipse.oomph.projectconfig.Project#getPreferenceProfileReferences
   * @model opposite="preferenceProfileReferences"
   * @generated
   */
  EList<Project> getReferentProjects();

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Project</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.Project#getPreferenceProfiles <em>Preference Profiles</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project</em>' container reference.
   * @see #setProject(Project)
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile_Project()
   * @see org.eclipse.oomph.projectconfig.Project#getPreferenceProfiles
   * @model opposite="preferenceProfiles" transient="false"
   * @generated
   */
  Project getProject();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getProject <em>Project</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Project</em>' container reference.
   * @see #getProject()
   * @generated
   */
  void setProject(Project value);

  /**
   * Returns the value of the '<em><b>Predicates</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.predicates.Predicate}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Predicates</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predicates</em>' containment reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getPreferenceProfile_Predicates()
   * @model containment="true"
   *        extendedMetaData="name='predicate'"
   * @generated
   */
  EList<Predicate> getPredicates();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model projectDataType="org.eclipse.oomph.predicates.Project"
   * @generated
   */
  boolean matches(IProject project);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model pathDataType="org.eclipse.oomph.preferences.URI"
   * @generated
   */
  Property getProperty(URI path);

} // PreferenceProfile

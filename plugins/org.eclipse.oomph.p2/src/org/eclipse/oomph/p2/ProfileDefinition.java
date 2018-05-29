/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profile Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.ProfileDefinition#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.ProfileDefinition#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.ProfileDefinition#isIncludeSourceBundles <em>Include Source Bundles</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.p2.P2Package#getProfileDefinition()
 * @model
 * @generated
 */
public interface ProfileDefinition extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Requirement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Requirements</em>' containment reference list.
   * @see org.eclipse.oomph.p2.P2Package#getProfileDefinition_Requirements()
   * @model containment="true"
   *        extendedMetaData="name='requirement'"
   * @generated
   */
  EList<Requirement> getRequirements();

  /**
   * Returns the value of the '<em><b>Repositories</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Repository}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repositories</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repositories</em>' containment reference list.
   * @see org.eclipse.oomph.p2.P2Package#getProfileDefinition_Repositories()
   * @model containment="true"
   *        extendedMetaData="name='repository'"
   * @generated
   */
  EList<Repository> getRepositories();

  /**
   * Returns the value of the '<em><b>Include Source Bundles</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include Source Bundles</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include Source Bundles</em>' attribute.
   * @see #setIncludeSourceBundles(boolean)
   * @see org.eclipse.oomph.p2.P2Package#getProfileDefinition_IncludeSourceBundles()
   * @model
   * @generated
   */
  boolean isIncludeSourceBundles();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.ProfileDefinition#isIncludeSourceBundles <em>Include Source Bundles</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include Source Bundles</em>' attribute.
   * @see #isIncludeSourceBundles()
   * @generated
   */
  void setIncludeSourceBundles(boolean value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model requirementsMany="true"
   * @generated
   */
  void setRequirements(EList<Requirement> requirements);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model repositoriesMany="true"
   * @generated
   */
  void setRepositories(EList<Repository> repositories);

} // ProfileDefinition

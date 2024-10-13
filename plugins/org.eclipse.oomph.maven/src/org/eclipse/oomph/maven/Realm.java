/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Realm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Realm#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Realm#getProjects <em>Projects</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getRealm()
 * @model
 * @generated
 */
public interface Realm extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Source Locators</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.resources.SourceLocator}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source Locators</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getRealm_SourceLocators()
   * @model containment="true"
   * @generated
   */
  EList<SourceLocator> getSourceLocators();

  /**
   * Returns the value of the '<em><b>Projects</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Project}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Project#getRealm <em>Realm</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Projects</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getRealm_Projects()
   * @see org.eclipse.oomph.maven.Project#getRealm
   * @model opposite="realm" containment="true" transient="true"
   * @generated
   */
  EList<Project> getProjects();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void reconcile();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model coordinateRequired="true"
   * @generated
   */
  Project getProject(Coordinate coordinate);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model coordinateRequired="true"
   * @generated
   */
  Project getProjectIgnoreVersion(Coordinate coordinate);

} // Realm

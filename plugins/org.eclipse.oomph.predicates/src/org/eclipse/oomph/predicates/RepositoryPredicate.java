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

import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.RepositoryPredicate#getProject <em>Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.predicates.RepositoryPredicate#getRelativePathPattern <em>Relative Path Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.predicates.RepositoryPredicate#isIncludeNestedRepositories <em>Include Nested Repositories</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getRepositoryPredicate()
 * @model
 * @generated
 */
public interface RepositoryPredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>Project</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project</em>' attribute.
   * @see #setProject(IProject)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getRepositoryPredicate_Project()
   * @model dataType="org.eclipse.oomph.predicates.Project"
   * @generated
   */
  IProject getProject();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.RepositoryPredicate#getProject <em>Project</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Project</em>' attribute.
   * @see #getProject()
   * @generated
   */
  void setProject(IProject value);

  /**
   * Returns the value of the '<em><b>Relative Path Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Relative Path Pattern</em>' attribute.
   * @see #setRelativePathPattern(String)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getRepositoryPredicate_RelativePathPattern()
   * @model
   * @generated
   */
  String getRelativePathPattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.RepositoryPredicate#getRelativePathPattern <em>Relative Path Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Relative Path Pattern</em>' attribute.
   * @see #getRelativePathPattern()
   * @generated
   */
  void setRelativePathPattern(String value);

  /**
   * Returns the value of the '<em><b>Include Nested Repositories</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include Nested Repositories</em>' attribute.
   * @see #setIncludeNestedRepositories(boolean)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getRepositoryPredicate_IncludeNestedRepositories()
   * @model
   * @generated
   */
  boolean isIncludeNestedRepositories();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.RepositoryPredicate#isIncludeNestedRepositories <em>Include Nested Repositories</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include Nested Repositories</em>' attribute.
   * @see #isIncludeNestedRepositories()
   * @generated
   */
  void setIncludeNestedRepositories(boolean value);

} // RepositoryPredicate

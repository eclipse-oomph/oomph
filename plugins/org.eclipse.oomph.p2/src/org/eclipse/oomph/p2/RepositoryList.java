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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.RepositoryList#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.RepositoryList#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.p2.P2Package#getRepositoryList()
 * @model
 * @generated
 */
public interface RepositoryList extends ModelElement
{
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
   * @see org.eclipse.oomph.p2.P2Package#getRepositoryList_Repositories()
   * @model containment="true"
   *        extendedMetaData="name='repository'"
   * @generated
   */
  EList<Repository> getRepositories();

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
   * @see org.eclipse.oomph.p2.P2Package#getRepositoryList_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.RepositoryList#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // RepositoryList

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
package org.eclipse.oomph.predicates;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.FilePredicate#getFilePattern <em>File Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.predicates.FilePredicate#getContentPattern <em>Content Pattern</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getFilePredicate()
 * @model
 * @generated
 */
public interface FilePredicate extends Predicate
{
  /**
   * Returns the value of the '<em><b>File Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>File Pattern</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>File Pattern</em>' attribute.
   * @see #setFilePattern(String)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getFilePredicate_FilePattern()
   * @model required="true"
   * @generated
   */
  String getFilePattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.FilePredicate#getFilePattern <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>File Pattern</em>' attribute.
   * @see #getFilePattern()
   * @generated
   */
  void setFilePattern(String value);

  /**
   * Returns the value of the '<em><b>Content Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Content Pattern</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Content Pattern</em>' attribute.
   * @see #setContentPattern(String)
   * @see org.eclipse.oomph.predicates.PredicatesPackage#getFilePredicate_ContentPattern()
   * @model
   * @generated
   */
  String getContentPattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.predicates.FilePredicate#getContentPattern <em>Content Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Content Pattern</em>' attribute.
   * @see #getContentPattern()
   * @generated
   */
  void setContentPattern(String value);

} // FilePredicate

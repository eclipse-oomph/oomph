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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.Repository#getURL <em>URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Repository#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.p2.P2Package#getRepository()
 * @model
 * @generated
 */
public interface Repository extends ModelElement
{
  /**
   * Returns the value of the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>URL</em>' attribute.
   * @see #setURL(String)
   * @see org.eclipse.oomph.p2.P2Package#getRepository_URL()
   * @model required="true"
   *        extendedMetaData="kind='attribute' name='url'"
   * @generated
   */
  String getURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Repository#getURL <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>URL</em>' attribute.
   * @see #getURL()
   * @generated
   */
  void setURL(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The default value is <code>"Combined"</code>.
   * The literals are from the enumeration {@link org.eclipse.oomph.p2.RepositoryType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.p2.RepositoryType
   * @see #setType(RepositoryType)
   * @see org.eclipse.oomph.p2.P2Package#getRepository_Type()
   * @model default="Combined" required="true"
   * @generated
   */
  RepositoryType getType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Repository#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.p2.RepositoryType
   * @see #getType()
   * @generated
   */
  void setType(RepositoryType value);

} // Repository

/*
 * Copyright (c) 2019 Ed Merks (Berlin) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Parameter#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Parameter#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Parameter#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getParameter_Name()
   * @model required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/NoExpand"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Parameter#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see #setDescription(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getParameter_Description()
   * @model dataType="org.eclipse.oomph.base.Text"
   *        extendedMetaData="kind='element'"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Parameter#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * Returns the value of the '<em><b>Default Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Value</em>' attribute.
   * @see #setDefaultValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getParameter_DefaultValue()
   * @model
   * @generated
   */
  String getDefaultValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Parameter#getDefaultValue <em>Default Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Value</em>' attribute.
   * @see #getDefaultValue()
   * @generated
   */
  void setDefaultValue(String value);

} // Parameter

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
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.AttributeRule#getAttributeURI <em>Attribute URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.AttributeRule#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getAttributeRule()
 * @model
 * @generated
 */
public interface AttributeRule extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Attribute URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attribute URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attribute URI</em>' attribute.
   * @see #setAttributeURI(URI)
   * @see org.eclipse.oomph.setup.SetupPackage#getAttributeRule_AttributeURI()
   * @model dataType="org.eclipse.oomph.base.URI"
   * @generated
   */
  URI getAttributeURI();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.AttributeRule#getAttributeURI <em>Attribute URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Attribute URI</em>' attribute.
   * @see #getAttributeURI()
   * @generated
   */
  void setAttributeURI(URI value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getAttributeRule_Value()
   * @model required="true"
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.AttributeRule#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // AttributeRule

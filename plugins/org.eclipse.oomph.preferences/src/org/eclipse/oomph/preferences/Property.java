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
package org.eclipse.oomph.preferences;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.Property#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.Property#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.Property#isNonDefault <em>Non Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.preferences.PreferencesPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends PreferenceItem
{
  /**
   * Returns the value of the '<em><b>Parent</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.preferences.PreferenceNode#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parent</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parent</em>' container reference.
   * @see #setParent(PreferenceNode)
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getProperty_Parent()
   * @see org.eclipse.oomph.preferences.PreferenceNode#getProperties
   * @model opposite="properties" transient="false"
   * @generated
   */
  PreferenceNode getParent();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.preferences.Property#getParent <em>Parent</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parent</em>' container reference.
   * @see #getParent()
   * @generated
   */
  void setParent(PreferenceNode value);

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
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getProperty_Value()
   * @model dataType="org.eclipse.oomph.preferences.EscapedString"
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.preferences.Property#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Non Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Non Default</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Non Default</em>' attribute.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getProperty_NonDefault()
   * @model transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  boolean isNonDefault();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  Property getAncestor();

} // Property

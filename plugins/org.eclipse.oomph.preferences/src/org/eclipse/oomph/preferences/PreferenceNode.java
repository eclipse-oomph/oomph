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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Preference Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceNode#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceNode#getChildren <em>Children</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceNode#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceNode#getLocation <em>Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceNode()
 * @model
 * @generated
 */
public interface PreferenceNode extends PreferenceItem
{
  /**
   * Returns the value of the '<em><b>Children</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.preferences.PreferenceNode}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.preferences.PreferenceNode#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Children</em>' containment reference list.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceNode_Children()
   * @see org.eclipse.oomph.preferences.PreferenceNode#getParent
   * @model opposite="parent" containment="true"
   *        extendedMetaData="name='child'"
   * @generated
   */
  EList<PreferenceNode> getChildren();

  /**
   * Returns the value of the '<em><b>Parent</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.preferences.PreferenceNode#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parent</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parent</em>' container reference.
   * @see #setParent(PreferenceNode)
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceNode_Parent()
   * @see org.eclipse.oomph.preferences.PreferenceNode#getChildren
   * @model opposite="children" transient="false"
   * @generated
   */
  PreferenceNode getParent();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.preferences.PreferenceNode#getParent <em>Parent</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parent</em>' container reference.
   * @see #getParent()
   * @generated
   */
  void setParent(PreferenceNode value);

  /**
   * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.preferences.Property}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.preferences.Property#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Properties</em>' containment reference list.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceNode_Properties()
   * @see org.eclipse.oomph.preferences.Property#getParent
   * @model opposite="parent" containment="true" keys="name"
   *        extendedMetaData="name='property'"
   * @generated
   */
  EList<Property> getProperties();

  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceNode_Location()
   * @model transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  String getLocation();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  PreferenceNode getNode(String name);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model pathDataType="org.eclipse.oomph.preferences.URI"
   * @generated
   */
  PreferenceNode getNode(URI path);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  Property getProperty(String name);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model pathDataType="org.eclipse.oomph.preferences.URI"
   * @generated
   */
  Property getProperty(URI path);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  PreferenceNode getAncestor();

} // PreferenceNode

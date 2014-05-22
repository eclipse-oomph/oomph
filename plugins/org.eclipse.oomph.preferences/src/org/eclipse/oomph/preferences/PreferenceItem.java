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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Preference Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getRoot <em>Root</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getScope <em>Scope</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getAbsolutePath <em>Absolute Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getRelativePath <em>Relative Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.PreferenceItem#getAncestor <em>Ancestor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem()
 * @model abstract="true"
 * @generated
 */
public interface PreferenceItem extends EObject
{
  /**
   * Returns the value of the '<em><b>Root</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Root</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Root</em>' reference.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_Root()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  PreferenceNode getRoot();

  /**
   * Returns the value of the '<em><b>Absolute Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Absolute Path</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Absolute Path</em>' attribute.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_AbsolutePath()
   * @model dataType="org.eclipse.oomph.preferences.URI" required="true" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  URI getAbsolutePath();

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
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.preferences.PreferenceItem#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Relative Path</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Relative Path</em>' attribute.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_RelativePath()
   * @model dataType="org.eclipse.oomph.preferences.URI" required="true" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  URI getRelativePath();

  /**
   * Returns the value of the '<em><b>Ancestor</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ancestor</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ancestor</em>' reference.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_Ancestor()
   * @model resolveProxies="false" changeable="false" volatile="true" derived="true"
   * @generated
   */
  PreferenceItem getAncestor();

  /**
   * Returns the value of the '<em><b>Scope</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Scope</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Scope</em>' reference.
   * @see org.eclipse.oomph.preferences.PreferencesPackage#getPreferenceItem_Scope()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  PreferenceNode getScope();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  PreferenceNode getParent();

} // PreferenceItem

/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.git;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Config Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.ConfigProperty#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.ConfigProperty#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.ConfigProperty#isForce <em>Force</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.ConfigProperty#isRecursive <em>Recursive</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.git.GitPackage#getConfigProperty()
 * @model
 * @generated
 */
public interface ConfigProperty extends EObject
{
  /**
   * Returns the value of the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Key</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Key</em>' attribute.
   * @see #setKey(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getConfigProperty_Key()
   * @model required="true"
   * @generated
   */
  String getKey();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.ConfigProperty#getKey <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Key</em>' attribute.
   * @see #getKey()
   * @generated
   */
  void setKey(String value);

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
   * @see org.eclipse.oomph.setup.git.GitPackage#getConfigProperty_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.ConfigProperty#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Force</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Force</em>' attribute.
   * @see #setForce(boolean)
   * @see org.eclipse.oomph.setup.git.GitPackage#getConfigProperty_Force()
   * @model
   * @generated
   */
  boolean isForce();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.ConfigProperty#isForce <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Force</em>' attribute.
   * @see #isForce()
   * @generated
   */
  void setForce(boolean value);

  /**
   * Returns the value of the '<em><b>Recursive</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Recursive</em>' attribute.
   * @see #setRecursive(boolean)
   * @see org.eclipse.oomph.setup.git.GitPackage#getConfigProperty_Recursive()
   * @model default="true"
   * @generated
   */
  boolean isRecursive();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.ConfigProperty#isRecursive <em>Recursive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Recursive</em>' attribute.
   * @see #isRecursive()
   * @generated
   */
  void setRecursive(boolean value);

} // ConfigProperty

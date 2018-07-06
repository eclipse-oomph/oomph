/*
 * Copyright (c) 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dropin Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.DropinLocation#getRootFolder <em>Root Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.DropinLocation#isRecursive <em>Recursive</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getDropinLocation()
 * @model
 * @generated
 */
public interface DropinLocation extends EObject
{
  /**
   * Returns the value of the '<em><b>Root Folder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Root Folder</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Root Folder</em>' attribute.
   * @see #setRootFolder(String)
   * @see org.eclipse.oomph.targlets.TargletPackage#getDropinLocation_RootFolder()
   * @model required="true"
   * @generated
   */
  String getRootFolder();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.DropinLocation#getRootFolder <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Root Folder</em>' attribute.
   * @see #getRootFolder()
   * @generated
   */
  void setRootFolder(String value);

  /**
   * Returns the value of the '<em><b>Recursive</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Recursive</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Recursive</em>' attribute.
   * @see #setRecursive(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getDropinLocation_Recursive()
   * @model default="true"
   * @generated
   */
  boolean isRecursive();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.DropinLocation#isRecursive <em>Recursive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Recursive</em>' attribute.
   * @see #isRecursive()
   * @generated
   */
  void setRecursive(boolean value);

} // DropinLocation

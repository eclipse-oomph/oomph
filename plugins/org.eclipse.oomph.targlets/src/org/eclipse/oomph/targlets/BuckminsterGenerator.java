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
package org.eclipse.oomph.targlets;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Buckminster Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.BuckminsterGenerator#isSaveAsComponent <em>Save As Component</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getBuckminsterGenerator()
 * @model
 * @generated
 */
public interface BuckminsterGenerator extends IUGenerator
{
  /**
   * Returns the value of the '<em><b>Save As Component</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Save As Component</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Save As Component</em>' attribute.
   * @see #setSaveAsComponent(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getBuckminsterGenerator_SaveAsComponent()
   * @model
   * @generated
   */
  boolean isSaveAsComponent();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.BuckminsterGenerator#isSaveAsComponent <em>Save As Component</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Save As Component</em>' attribute.
   * @see #isSaveAsComponent()
   * @generated
   */
  void setSaveAsComponent(boolean value);

} // BuckminsterGenerator

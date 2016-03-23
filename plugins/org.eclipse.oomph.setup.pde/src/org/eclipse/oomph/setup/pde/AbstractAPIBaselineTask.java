/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde;

import org.eclipse.oomph.setup.SetupTask;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract API Baseline Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#isActivate <em>Activate</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.pde.PDEPackage#getAbstractAPIBaselineTask()
 * @model abstract="true"
 * @generated
 */
public interface AbstractAPIBaselineTask extends SetupTask
{
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
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAbstractAPIBaselineTask_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #setVersion(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAbstractAPIBaselineTask_Version()
   * @model required="true"
   * @generated
   */
  String getVersion();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #getVersion()
   * @generated
   */
  void setVersion(String value);

  /**
   * Returns the value of the '<em><b>Activate</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Activate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Activate</em>' attribute.
   * @see #setActivate(boolean)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAbstractAPIBaselineTask_Activate()
   * @model default="true"
   * @generated
   */
  boolean isActivate();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask#isActivate <em>Activate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Activate</em>' attribute.
   * @see #isActivate()
   * @generated
   */
  void setActivate(boolean value);

} // AbstractAPIBaselineTask

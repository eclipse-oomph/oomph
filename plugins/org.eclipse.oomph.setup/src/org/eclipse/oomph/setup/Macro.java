/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Macro</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Macro#getLogicalContainer <em>Logical Container</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Macro#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getMacro()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='NoRecursion'"
 * @generated
 */
public interface Macro extends Scope
{
  /**
   * Returns the value of the '<em><b>Logical Container</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logical Container</em>' reference.
   * @see #setLogicalContainer(MacroTask)
   * @see org.eclipse.oomph.setup.SetupPackage#getMacro_LogicalContainer()
   * @model resolveProxies="false" transient="true"
   * @generated
   */
  MacroTask getLogicalContainer();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Macro#getLogicalContainer <em>Logical Container</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Logical Container</em>' reference.
   * @see #getLogicalContainer()
   * @generated
   */
  void setLogicalContainer(MacroTask value);

  /**
   * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Parameter}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameters</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getMacro_Parameters()
   * @model containment="true" keys="name"
   *        extendedMetaData="kind='element' name='parameter'"
   * @generated
   */
  EList<Parameter> getParameters();

} // Macro

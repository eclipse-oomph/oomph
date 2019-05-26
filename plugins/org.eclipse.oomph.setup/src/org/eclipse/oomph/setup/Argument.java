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
 * A representation of the model object '<em><b>Argument</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Argument#getMacroTask <em>Macro Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Argument#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Argument#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getArgument()
 * @model features="name"
 *        nameDataType="org.eclipse.emf.ecore.EString" nameVolatile="true" nameSuppressedGetVisibility="true" nameSuppressedSetVisibility="true" nameSuppressedIsSetVisibility="true" nameSuppressedUnsetVisibility="true"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore constraints='ConsistentParameterBinding'"
 * @generated
 */
public interface Argument extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Macro Task</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.MacroTask#getArguments <em>Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Macro Task</em>' container reference.
   * @see org.eclipse.oomph.setup.SetupPackage#getArgument_MacroTask()
   * @see org.eclipse.oomph.setup.MacroTask#getArguments
   * @model opposite="arguments" resolveProxies="false" changeable="false"
   * @generated
   */
  MacroTask getMacroTask();

  /**
   * Returns the value of the '<em><b>Parameter</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameter</em>' reference.
   * @see #setParameter(Parameter)
   * @see org.eclipse.oomph.setup.SetupPackage#getArgument_Parameter()
   * @model unsettable="true" required="true" suppressedIsSetVisibility="true" suppressedUnsetVisibility="true"
   * @generated
   */
  Parameter getParameter();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Argument#getParameter <em>Parameter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameter</em>' reference.
   * @see #getParameter()
   * @generated
   */
  void setParameter(Parameter value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getArgument_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Argument#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // Argument

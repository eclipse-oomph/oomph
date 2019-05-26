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
 * A representation of the model object '<em><b>Macro Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.MacroTask#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.MacroTask#getMacro <em>Macro</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getMacroTask()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='IDRequired ArgumentsCorrespondToParameters'"
 * @generated
 */
public interface MacroTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Argument}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Argument#getMacroTask <em>Macro Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Arguments</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getMacroTask_Arguments()
   * @see org.eclipse.oomph.setup.Argument#getMacroTask
   * @model opposite="macroTask" containment="true"
   *        extendedMetaData="kind='element' name='argument'"
   * @generated
   */
  EList<Argument> getArguments();

  /**
   * Returns the value of the '<em><b>Macro</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Macro</em>' reference.
   * @see #setMacro(Macro)
   * @see org.eclipse.oomph.setup.SetupPackage#getMacroTask_Macro()
   * @model required="true"
   *        extendedMetaData="kind='attribute'"
   * @generated
   */
  Macro getMacro();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.MacroTask#getMacro <em>Macro</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Macro</em>' reference.
   * @see #getMacro()
   * @generated
   */
  void setMacro(Macro value);

} // MacroTask

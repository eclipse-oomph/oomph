/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage
 * @generated
 */
public interface WorkbenchFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  WorkbenchFactory eINSTANCE = org.eclipse.oomph.setup.workbench.impl.WorkbenchFactoryImpl.init();

  /**
   * Returns a new object of class '<em>File Associations Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>File Associations Task</em>'.
   * @generated
   */
  FileAssociationsTask createFileAssociationsTask();

  /**
   * Returns a new object of class '<em>File Mapping</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>File Mapping</em>'.
   * @generated
   */
  FileMapping createFileMapping();

  /**
   * Returns a new object of class '<em>File Editor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>File Editor</em>'.
   * @generated
   */
  FileEditor createFileEditor();

  /**
   * Returns a new object of class '<em>Key Binding Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Key Binding Task</em>'.
   * @generated
   */
  KeyBindingTask createKeyBindingTask();

  /**
   * Returns a new object of class '<em>Key Binding Context</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Key Binding Context</em>'.
   * @generated
   */
  KeyBindingContext createKeyBindingContext();

  /**
   * Returns a new object of class '<em>Command Parameter</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Command Parameter</em>'.
   * @generated
   */
  CommandParameter createCommandParameter();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  WorkbenchPackage getWorkbenchPackage();

} // WorkbenchFactory

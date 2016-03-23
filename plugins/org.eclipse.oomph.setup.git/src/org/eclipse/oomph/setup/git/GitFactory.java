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
package org.eclipse.oomph.setup.git;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.git.GitPackage
 * @generated
 */
public interface GitFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  GitFactory eINSTANCE = org.eclipse.oomph.setup.git.impl.GitFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Clone Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Clone Task</em>'.
   * @generated
   */
  GitCloneTask createGitCloneTask();

  /**
   * Returns a new object of class '<em>Config Section</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Config Section</em>'.
   * @generated
   */
  ConfigSection createConfigSection();

  /**
   * Returns a new object of class '<em>Config Subsection</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Config Subsection</em>'.
   * @generated
   */
  ConfigSubsection createConfigSubsection();

  /**
   * Returns a new object of class '<em>Config Property</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Config Property</em>'.
   * @generated
   */
  ConfigProperty createConfigProperty();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  GitPackage getGitPackage();

} // GitFactory

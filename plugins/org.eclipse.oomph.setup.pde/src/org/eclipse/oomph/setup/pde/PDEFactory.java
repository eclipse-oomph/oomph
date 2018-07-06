/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.pde.PDEPackage
 * @generated
 */
public interface PDEFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  PDEFactory eINSTANCE = org.eclipse.oomph.setup.pde.impl.PDEFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Target Platform Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Target Platform Task</em>'.
   * @generated
   */
  TargetPlatformTask createTargetPlatformTask();

  /**
   * Returns a new object of class '<em>API Baseline Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>API Baseline Task</em>'.
   * @generated
   */
  APIBaselineTask createAPIBaselineTask();

  /**
   * Returns a new object of class '<em>API Baseline From Target Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>API Baseline From Target Task</em>'.
   * @generated
   */
  APIBaselineFromTargetTask createAPIBaselineFromTargetTask();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  PDEPackage getPDEPackage();

} // PDEFactory

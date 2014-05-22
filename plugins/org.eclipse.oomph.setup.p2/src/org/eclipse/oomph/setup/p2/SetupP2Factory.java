/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.p2;

import org.eclipse.emf.ecore.EFactory;

import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.p2.SetupP2Package
 * @generated
 */
public interface SetupP2Factory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SetupP2Factory eINSTANCE = org.eclipse.oomph.setup.p2.impl.SetupP2FactoryImpl.init();

  /**
   * Returns a new object of class '<em>P2 Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>P2 Task</em>'.
   * @generated
   */
  P2Task createP2Task();

  P2Task createP2Task(String[] ius, String[] repositories);

  P2Task createP2Task(String[] ius, String[] repositories, Set<String> existingIUs);

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  SetupP2Package getSetupP2Package();

} // SetupP2Factory

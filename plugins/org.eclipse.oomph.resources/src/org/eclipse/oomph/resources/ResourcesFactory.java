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
package org.eclipse.oomph.resources;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.resources.ResourcesPackage
 * @generated
 */
public interface ResourcesFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ResourcesFactory eINSTANCE = org.eclipse.oomph.resources.impl.ResourcesFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Source Locator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Source Locator</em>'.
   * @generated
   */
  SourceLocator createSourceLocator();

  /**
   * Returns a new object of class '<em>Eclipse Project Factory</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Eclipse Project Factory</em>'.
   * @generated
   */
  EclipseProjectFactory createEclipseProjectFactory();

  /**
   * Returns a new object of class '<em>Maven Project Factory</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Maven Project Factory</em>'.
   * @generated
   */
  MavenProjectFactory createMavenProjectFactory();

  /**
   * Returns a new object of class '<em>Dynamic Maven Project Factory</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Dynamic Maven Project Factory</em>'.
   * @generated
   */
  DynamicMavenProjectFactory createDynamicMavenProjectFactory();

  SourceLocator createSourceLocator(String rootFolder);

  SourceLocator createSourceLocator(String rootFolder, boolean locateNestedProjects);

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ResourcesPackage getResourcesPackage();

} // ResourcesFactory

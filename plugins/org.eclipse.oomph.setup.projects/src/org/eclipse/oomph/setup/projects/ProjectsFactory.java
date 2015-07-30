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
package org.eclipse.oomph.setup.projects;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.projects.ProjectsPackage
 * @generated
 */
public interface ProjectsFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ProjectsFactory eINSTANCE = org.eclipse.oomph.setup.projects.impl.ProjectsFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Import Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Import Task</em>'.
   * @generated
   */
  ProjectsImportTask createProjectsImportTask();

  /**
   * Returns a new object of class '<em>Build Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Build Task</em>'.
   * @generated
   */
  ProjectsBuildTask createProjectsBuildTask();

  /**
   * Returns a new object of class '<em>Path Variable Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Path Variable Task</em>'.
   * @generated
   */
  PathVariableTask createPathVariableTask();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ProjectsPackage getProjectsPackage();

} // ProjectsFactory

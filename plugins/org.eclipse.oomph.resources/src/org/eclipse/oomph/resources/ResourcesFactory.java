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
package org.eclipse.oomph.resources;

import org.eclipse.oomph.internal.resources.ExternalProject.AbstractXMLDescriptionFactory.Eclipse;
import org.eclipse.oomph.internal.resources.ExternalProject.AbstractXMLDescriptionFactory.Maven;

import org.eclipse.emf.ecore.EFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;

import java.io.File;

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

  IProject loadProject(File folder);

  IProject loadProject(File folder, ProjectDescriptionFactory... factories);

  /**
   * @author Eike Stepper
   */
  public interface ProjectDescriptionFactory
  {
    public static final ProjectDescriptionFactory ECLIPSE = new Eclipse();

    public static final ProjectDescriptionFactory MAVEN = new Maven();

    public IProjectDescription createDescription(File folder) throws Exception;
  }

} // ResourcesFactory

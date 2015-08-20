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
package org.eclipse.oomph.targlets;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EFactory;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.targlets.TargletPackage
 * @generated
 */
public interface TargletFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TargletFactory eINSTANCE = org.eclipse.oomph.targlets.impl.TargletFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Container</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Container</em>'.
   * @generated
   */
  TargletContainer createTargletContainer();

  /**
   * Returns a new object of class '<em>Targlet</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Targlet</em>'.
   * @generated
   */
  Targlet createTarglet();

  Targlet createTarglet(String name);

  Targlet copyTarglet(Targlet source);

  EList<Targlet> copyTarglets(Collection<? extends Targlet> targlets);

  /**
   * Returns a new object of class '<em>Component Extension</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Component Extension</em>'.
   * @generated
   */
  ComponentExtension createComponentExtension();

  /**
   * Returns a new object of class '<em>Component Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Component Definition</em>'.
   * @generated
   */
  ComponentDefinition createComponentDefinition();

  /**
   * Returns a new object of class '<em>Feature Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Feature Generator</em>'.
   * @generated
   */
  FeatureGenerator createFeatureGenerator();

  /**
   * Returns a new object of class '<em>Plugin Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Plugin Generator</em>'.
   * @generated
   */
  PluginGenerator createPluginGenerator();

  /**
   * Returns a new object of class '<em>Component Def Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Component Def Generator</em>'.
   * @generated
   */
  ComponentDefGenerator createComponentDefGenerator();

  /**
   * Returns a new object of class '<em>Component Ext Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Component Ext Generator</em>'.
   * @generated
   */
  ComponentExtGenerator createComponentExtGenerator();

  /**
   * Returns a new object of class '<em>CSpec Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>CSpec Generator</em>'.
   * @generated
   */
  CSpecGenerator createCSpecGenerator();

  /**
   * Returns a new object of class '<em>CSpex Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>CSpex Generator</em>'.
   * @generated
   */
  CSpexGenerator createCSpexGenerator();

  /**
   * Returns a new object of class '<em>Site Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Site Generator</em>'.
   * @generated
   */
  SiteGenerator createSiteGenerator();

  /**
   * Returns a new object of class '<em>Category Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Category Generator</em>'.
   * @generated
   */
  CategoryGenerator createCategoryGenerator();

  /**
   * Returns a new object of class '<em>Product Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Product Generator</em>'.
   * @generated
   */
  ProductGenerator createProductGenerator();

  /**
   * Returns a new object of class '<em>Project Name Generator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Project Name Generator</em>'.
   * @generated
   */
  ProjectNameGenerator createProjectNameGenerator();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  TargletPackage getTargletPackage();

} // TargletFactory

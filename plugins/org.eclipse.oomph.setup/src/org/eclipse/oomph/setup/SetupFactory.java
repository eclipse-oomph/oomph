/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage
 * @generated
 */
public interface SetupFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SetupFactory eINSTANCE = org.eclipse.oomph.setup.impl.SetupFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Index</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Index</em>'.
   * @generated
   */
  Index createIndex();

  /**
   * Returns a new object of class '<em>Catalog Selection</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Catalog Selection</em>'.
   * @generated
   */
  CatalogSelection createCatalogSelection();

  /**
   * Returns a new object of class '<em>User</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>User</em>'.
   * @generated
   */
  User createUser();

  /**
   * Returns a new object of class '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Project</em>'.
   * @generated
   */
  Project createProject();

  /**
   * Returns a new object of class '<em>Stream</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Stream</em>'.
   * @generated
   */
  Stream createStream();

  /**
   * Returns a new object of class '<em>Installation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Installation</em>'.
   * @generated
   */
  Installation createInstallation();

  /**
   * Returns a new object of class '<em>Product Catalog</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Product Catalog</em>'.
   * @generated
   */
  ProductCatalog createProductCatalog();

  /**
   * Returns a new object of class '<em>Product</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Product</em>'.
   * @generated
   */
  Product createProduct();

  /**
   * Returns a new object of class '<em>Product Version</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Product Version</em>'.
   * @generated
   */
  ProductVersion createProductVersion();

  /**
   * Returns a new object of class '<em>Project Catalog</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Project Catalog</em>'.
   * @generated
   */
  ProjectCatalog createProjectCatalog();

  /**
   * Returns a new object of class '<em>Installation Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Installation Task</em>'.
   * @generated
   */
  InstallationTask createInstallationTask();

  /**
   * Returns a new object of class '<em>Workspace Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Workspace Task</em>'.
   * @generated
   */
  WorkspaceTask createWorkspaceTask();

  /**
   * Returns a new object of class '<em>Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Configuration</em>'.
   * @generated
   */
  Configuration createConfiguration();

  /**
   * Returns a new object of class '<em>Compound Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Compound Task</em>'.
   * @generated
   */
  CompoundTask createCompoundTask();

  CompoundTask createCompoundTask(String name);

  /**
   * Returns a new object of class '<em>Variable Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Task</em>'.
   * @generated
   */
  VariableTask createVariableTask();

  /**
   * Returns a new object of class '<em>Resource Copy Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Resource Copy Task</em>'.
   * @generated
   */
  ResourceCopyTask createResourceCopyTask();

  /**
   * Returns a new object of class '<em>Text Modify Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Text Modify Task</em>'.
   * @generated
   */
  TextModifyTask createTextModifyTask();

  /**
   * Returns a new object of class '<em>Text Modification</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Text Modification</em>'.
   * @generated
   */
  TextModification createTextModification();

  /**
   * Returns a new object of class '<em>String Substitution Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Substitution Task</em>'.
   * @generated
   */
  StringSubstitutionTask createStringSubstitutionTask();

  /**
   * Returns a new object of class '<em>Attribute Rule</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Attribute Rule</em>'.
   * @generated
   */
  AttributeRule createAttributeRule();

  /**
   * Returns a new object of class '<em>Location Catalog</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Location Catalog</em>'.
   * @generated
   */
  LocationCatalog createLocationCatalog();

  /**
   * Returns a new object of class '<em>Redirection Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Redirection Task</em>'.
   * @generated
   */
  RedirectionTask createRedirectionTask();

  /**
   * Returns a new object of class '<em>Variable Choice</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Choice</em>'.
   * @generated
   */
  VariableChoice createVariableChoice();

  /**
   * Returns a new object of class '<em>Resource Creation Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Resource Creation Task</em>'.
   * @generated
   */
  ResourceCreationTask createResourceCreationTask();

  /**
   * Returns a new object of class '<em>Eclipse Ini Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Eclipse Ini Task</em>'.
   * @generated
   */
  EclipseIniTask createEclipseIniTask();

  /**
   * Returns a new object of class '<em>Workspace</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Workspace</em>'.
   * @generated
   */
  Workspace createWorkspace();

  /**
   * Returns a new object of class '<em>Link Location Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Link Location Task</em>'.
   * @generated
   */
  LinkLocationTask createLinkLocationTask();

  /**
   * Returns a new object of class '<em>Preference Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Preference Task</em>'.
   * @generated
   */
  PreferenceTask createPreferenceTask();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  SetupPackage getSetupPackage();

} // SetupFactory

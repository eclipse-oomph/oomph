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

import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.resources.ResourcesFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Resources.ecore'"
 * @generated
 */
public interface ResourcesPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "resources";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/resources/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "resources";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ResourcesPackage eINSTANCE = org.eclipse.oomph.resources.impl.ResourcesPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl <em>Source Locator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.impl.SourceLocatorImpl
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getSourceLocator()
   * @generated
   */
  int SOURCE_LOCATOR = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Root Folder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__ROOT_FOLDER = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Locate Nested Projects</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Predicates</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__PREDICATES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Source Locator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Source Locator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.resources.SourceLocator <em>Source Locator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Source Locator</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator
   * @generated
   */
  EClass getSourceLocator();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.resources.SourceLocator#getRootFolder <em>Root Folder</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Root Folder</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator#getRootFolder()
   * @see #getSourceLocator()
   * @generated
   */
  EAttribute getSourceLocator_RootFolder();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.resources.SourceLocator#isLocateNestedProjects <em>Locate Nested Projects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Locate Nested Projects</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator#isLocateNestedProjects()
   * @see #getSourceLocator()
   * @generated
   */
  EAttribute getSourceLocator_LocateNestedProjects();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.resources.SourceLocator#getPredicates <em>Predicates</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Predicates</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator#getPredicates()
   * @see #getSourceLocator()
   * @generated
   */
  EReference getSourceLocator_Predicates();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ResourcesFactory getResourcesFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl <em>Source Locator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.impl.SourceLocatorImpl
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getSourceLocator()
     * @generated
     */
    EClass SOURCE_LOCATOR = eINSTANCE.getSourceLocator();

    /**
     * The meta object literal for the '<em><b>Root Folder</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCE_LOCATOR__ROOT_FOLDER = eINSTANCE.getSourceLocator_RootFolder();

    /**
     * The meta object literal for the '<em><b>Locate Nested Projects</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS = eINSTANCE.getSourceLocator_LocateNestedProjects();

    /**
     * The meta object literal for the '<em><b>Predicates</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOURCE_LOCATOR__PREDICATES = eINSTANCE.getSourceLocator_Predicates();

  }

} // ResourcesPackage

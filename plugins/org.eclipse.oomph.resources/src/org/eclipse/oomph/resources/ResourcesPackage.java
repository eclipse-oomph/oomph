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

import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
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
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.resources.edit/icons/full/obj16'"
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
   * The feature id for the '<em><b>Excluded Paths</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__EXCLUDED_PATHS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Project Factories</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__PROJECT_FACTORIES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Predicates</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__PREDICATES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Locate Nested Projects</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Source Locator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR___MATCHES__IPROJECT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Load Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR___LOAD_PROJECT__ELIST_BACKENDCONTAINER_IPROGRESSMONITOR = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Handle Projects</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR___HANDLE_PROJECTS__ELIST_PROJECTHANDLER_MULTISTATUS_IPROGRESSMONITOR = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>Source Locator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_LOCATOR_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.resources.impl.ProjectFactoryImpl <em>Project Factory</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.impl.ProjectFactoryImpl
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProjectFactory()
   * @generated
   */
  int PROJECT_FACTORY = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Excluded Paths</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY__EXCLUDED_PATHS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Create Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Is Excluded Path</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FACTORY_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.resources.impl.XMLProjectFactoryImpl <em>XML Project Factory</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.impl.XMLProjectFactoryImpl
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getXMLProjectFactory()
   * @generated
   */
  int XML_PROJECT_FACTORY = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY__ANNOTATIONS = PROJECT_FACTORY__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Excluded Paths</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY__EXCLUDED_PATHS = PROJECT_FACTORY__EXCLUDED_PATHS;

  /**
   * The number of structural features of the '<em>XML Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY_FEATURE_COUNT = PROJECT_FACTORY_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY___GET_ANNOTATION__STRING = PROJECT_FACTORY___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Create Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR = PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR;

  /**
   * The operation id for the '<em>Is Excluded Path</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER = PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER;

  /**
   * The number of operations of the '<em>XML Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XML_PROJECT_FACTORY_OPERATION_COUNT = PROJECT_FACTORY_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.resources.impl.EclipseProjectFactoryImpl <em>Eclipse Project Factory</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.impl.EclipseProjectFactoryImpl
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getEclipseProjectFactory()
   * @generated
   */
  int ECLIPSE_PROJECT_FACTORY = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY__ANNOTATIONS = XML_PROJECT_FACTORY__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Excluded Paths</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY__EXCLUDED_PATHS = XML_PROJECT_FACTORY__EXCLUDED_PATHS;

  /**
   * The number of structural features of the '<em>Eclipse Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY_FEATURE_COUNT = XML_PROJECT_FACTORY_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY___GET_ANNOTATION__STRING = XML_PROJECT_FACTORY___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Create Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR = XML_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR;

  /**
   * The operation id for the '<em>Is Excluded Path</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER = XML_PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER;

  /**
   * The number of operations of the '<em>Eclipse Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_PROJECT_FACTORY_OPERATION_COUNT = XML_PROJECT_FACTORY_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.resources.impl.MavenProjectFactoryImpl <em>Maven Project Factory</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.impl.MavenProjectFactoryImpl
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getMavenProjectFactory()
   * @generated
   */
  int MAVEN_PROJECT_FACTORY = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY__ANNOTATIONS = XML_PROJECT_FACTORY__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Excluded Paths</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY__EXCLUDED_PATHS = XML_PROJECT_FACTORY__EXCLUDED_PATHS;

  /**
   * The number of structural features of the '<em>Maven Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY_FEATURE_COUNT = XML_PROJECT_FACTORY_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY___GET_ANNOTATION__STRING = XML_PROJECT_FACTORY___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Create Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR = XML_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR;

  /**
   * The operation id for the '<em>Is Excluded Path</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER = XML_PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER;

  /**
   * The number of operations of the '<em>Maven Project Factory</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_PROJECT_FACTORY_OPERATION_COUNT = XML_PROJECT_FACTORY_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Project Handler</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.ProjectHandler
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProjectHandler()
   * @generated
   */
  int PROJECT_HANDLER = 5;

  /**
   * The meta object id for the '<em>Backend Container</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.resources.backend.BackendContainer
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getBackendContainer()
   * @generated
   */
  int BACKEND_CONTAINER = 6;

  /**
   * The meta object id for the '<em>Multi Status</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.core.runtime.MultiStatus
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getMultiStatus()
   * @generated
   */
  int MULTI_STATUS = 7;

  /**
   * The meta object id for the '<em>Progress Monitor</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.core.runtime.IProgressMonitor
   * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProgressMonitor()
   * @generated
   */
  int PROGRESS_MONITOR = 8;

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
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.resources.SourceLocator#getExcludedPaths <em>Excluded Paths</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Excluded Paths</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator#getExcludedPaths()
   * @see #getSourceLocator()
   * @generated
   */
  EAttribute getSourceLocator_ExcludedPaths();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.resources.SourceLocator#getProjectFactories <em>Project Factories</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Project Factories</em>'.
   * @see org.eclipse.oomph.resources.SourceLocator#getProjectFactories()
   * @see #getSourceLocator()
   * @generated
   */
  EReference getSourceLocator_ProjectFactories();

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
   * Returns the meta object for the '{@link org.eclipse.oomph.resources.SourceLocator#matches(org.eclipse.core.resources.IProject) <em>Matches</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Matches</em>' operation.
   * @see org.eclipse.oomph.resources.SourceLocator#matches(org.eclipse.core.resources.IProject)
   * @generated
   */
  EOperation getSourceLocator__Matches__IProject();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.resources.SourceLocator#loadProject(org.eclipse.emf.common.util.EList, org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.core.runtime.IProgressMonitor) <em>Load Project</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Load Project</em>' operation.
   * @see org.eclipse.oomph.resources.SourceLocator#loadProject(org.eclipse.emf.common.util.EList, org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.core.runtime.IProgressMonitor)
   * @generated
   */
  EOperation getSourceLocator__LoadProject__EList_BackendContainer_IProgressMonitor();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.resources.SourceLocator#handleProjects(org.eclipse.emf.common.util.EList, org.eclipse.oomph.resources.ProjectHandler, org.eclipse.core.runtime.MultiStatus, org.eclipse.core.runtime.IProgressMonitor) <em>Handle Projects</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Handle Projects</em>' operation.
   * @see org.eclipse.oomph.resources.SourceLocator#handleProjects(org.eclipse.emf.common.util.EList, org.eclipse.oomph.resources.ProjectHandler, org.eclipse.core.runtime.MultiStatus, org.eclipse.core.runtime.IProgressMonitor)
   * @generated
   */
  EOperation getSourceLocator__HandleProjects__EList_ProjectHandler_MultiStatus_IProgressMonitor();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.resources.ProjectFactory <em>Project Factory</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project Factory</em>'.
   * @see org.eclipse.oomph.resources.ProjectFactory
   * @generated
   */
  EClass getProjectFactory();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.resources.ProjectFactory#getExcludedPaths <em>Excluded Paths</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Excluded Paths</em>'.
   * @see org.eclipse.oomph.resources.ProjectFactory#getExcludedPaths()
   * @see #getProjectFactory()
   * @generated
   */
  EAttribute getProjectFactory_ExcludedPaths();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.resources.ProjectFactory#createProject(org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.core.runtime.IProgressMonitor) <em>Create Project</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Create Project</em>' operation.
   * @see org.eclipse.oomph.resources.ProjectFactory#createProject(org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.core.runtime.IProgressMonitor)
   * @generated
   */
  EOperation getProjectFactory__CreateProject__BackendContainer_BackendContainer_IProgressMonitor();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.resources.ProjectFactory#isExcludedPath(org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.oomph.resources.backend.BackendContainer) <em>Is Excluded Path</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Is Excluded Path</em>' operation.
   * @see org.eclipse.oomph.resources.ProjectFactory#isExcludedPath(org.eclipse.oomph.resources.backend.BackendContainer, org.eclipse.oomph.resources.backend.BackendContainer)
   * @generated
   */
  EOperation getProjectFactory__IsExcludedPath__BackendContainer_BackendContainer();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.resources.XMLProjectFactory <em>XML Project Factory</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>XML Project Factory</em>'.
   * @see org.eclipse.oomph.resources.XMLProjectFactory
   * @generated
   */
  EClass getXMLProjectFactory();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.resources.EclipseProjectFactory <em>Eclipse Project Factory</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Eclipse Project Factory</em>'.
   * @see org.eclipse.oomph.resources.EclipseProjectFactory
   * @generated
   */
  EClass getEclipseProjectFactory();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.resources.MavenProjectFactory <em>Maven Project Factory</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Maven Project Factory</em>'.
   * @see org.eclipse.oomph.resources.MavenProjectFactory
   * @generated
   */
  EClass getMavenProjectFactory();

  /**
   * Returns the meta object for data type '{@link org.eclipse.oomph.resources.ProjectHandler <em>Project Handler</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Project Handler</em>'.
   * @see org.eclipse.oomph.resources.ProjectHandler
   * @model instanceClass="org.eclipse.oomph.resources.ProjectHandler" serializeable="false"
   * @generated
   */
  EDataType getProjectHandler();

  /**
   * Returns the meta object for data type '{@link org.eclipse.oomph.resources.backend.BackendContainer <em>Backend Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Backend Container</em>'.
   * @see org.eclipse.oomph.resources.backend.BackendContainer
   * @model instanceClass="org.eclipse.oomph.resources.backend.BackendContainer" serializeable="false"
   * @generated
   */
  EDataType getBackendContainer();

  /**
   * Returns the meta object for data type '{@link org.eclipse.core.runtime.MultiStatus <em>Multi Status</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Multi Status</em>'.
   * @see org.eclipse.core.runtime.MultiStatus
   * @model instanceClass="org.eclipse.core.runtime.MultiStatus" serializeable="false"
   * @generated
   */
  EDataType getMultiStatus();

  /**
   * Returns the meta object for data type '{@link org.eclipse.core.runtime.IProgressMonitor <em>Progress Monitor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Progress Monitor</em>'.
   * @see org.eclipse.core.runtime.IProgressMonitor
   * @model instanceClass="org.eclipse.core.runtime.IProgressMonitor" serializeable="false"
   * @generated
   */
  EDataType getProgressMonitor();

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
     * The meta object literal for the '<em><b>Excluded Paths</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCE_LOCATOR__EXCLUDED_PATHS = eINSTANCE.getSourceLocator_ExcludedPaths();

    /**
     * The meta object literal for the '<em><b>Project Factories</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOURCE_LOCATOR__PROJECT_FACTORIES = eINSTANCE.getSourceLocator_ProjectFactories();

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

    /**
     * The meta object literal for the '<em><b>Matches</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation SOURCE_LOCATOR___MATCHES__IPROJECT = eINSTANCE.getSourceLocator__Matches__IProject();

    /**
     * The meta object literal for the '<em><b>Load Project</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation SOURCE_LOCATOR___LOAD_PROJECT__ELIST_BACKENDCONTAINER_IPROGRESSMONITOR = eINSTANCE
        .getSourceLocator__LoadProject__EList_BackendContainer_IProgressMonitor();

    /**
     * The meta object literal for the '<em><b>Handle Projects</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation SOURCE_LOCATOR___HANDLE_PROJECTS__ELIST_PROJECTHANDLER_MULTISTATUS_IPROGRESSMONITOR = eINSTANCE
        .getSourceLocator__HandleProjects__EList_ProjectHandler_MultiStatus_IProgressMonitor();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.resources.impl.ProjectFactoryImpl <em>Project Factory</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.impl.ProjectFactoryImpl
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProjectFactory()
     * @generated
     */
    EClass PROJECT_FACTORY = eINSTANCE.getProjectFactory();

    /**
     * The meta object literal for the '<em><b>Excluded Paths</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECT_FACTORY__EXCLUDED_PATHS = eINSTANCE.getProjectFactory_ExcludedPaths();

    /**
     * The meta object literal for the '<em><b>Create Project</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR = eINSTANCE
        .getProjectFactory__CreateProject__BackendContainer_BackendContainer_IProgressMonitor();

    /**
     * The meta object literal for the '<em><b>Is Excluded Path</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER = eINSTANCE
        .getProjectFactory__IsExcludedPath__BackendContainer_BackendContainer();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.resources.impl.XMLProjectFactoryImpl <em>XML Project Factory</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.impl.XMLProjectFactoryImpl
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getXMLProjectFactory()
     * @generated
     */
    EClass XML_PROJECT_FACTORY = eINSTANCE.getXMLProjectFactory();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.resources.impl.EclipseProjectFactoryImpl <em>Eclipse Project Factory</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.impl.EclipseProjectFactoryImpl
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getEclipseProjectFactory()
     * @generated
     */
    EClass ECLIPSE_PROJECT_FACTORY = eINSTANCE.getEclipseProjectFactory();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.resources.impl.MavenProjectFactoryImpl <em>Maven Project Factory</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.impl.MavenProjectFactoryImpl
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getMavenProjectFactory()
     * @generated
     */
    EClass MAVEN_PROJECT_FACTORY = eINSTANCE.getMavenProjectFactory();

    /**
     * The meta object literal for the '<em>Project Handler</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.ProjectHandler
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProjectHandler()
     * @generated
     */
    EDataType PROJECT_HANDLER = eINSTANCE.getProjectHandler();

    /**
     * The meta object literal for the '<em>Backend Container</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.resources.backend.BackendContainer
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getBackendContainer()
     * @generated
     */
    EDataType BACKEND_CONTAINER = eINSTANCE.getBackendContainer();

    /**
     * The meta object literal for the '<em>Multi Status</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.core.runtime.MultiStatus
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getMultiStatus()
     * @generated
     */
    EDataType MULTI_STATUS = eINSTANCE.getMultiStatus();

    /**
     * The meta object literal for the '<em>Progress Monitor</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.core.runtime.IProgressMonitor
     * @see org.eclipse.oomph.resources.impl.ResourcesPackageImpl#getProgressMonitor()
     * @generated
     */
    EDataType PROGRESS_MONITOR = eINSTANCE.getProgressMonitor();

  }

} // ResourcesPackage

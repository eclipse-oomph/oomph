/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.MavenProjectFactory;
import org.eclipse.oomph.resources.ProjectFactory;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.XMLProjectFactory;
import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.MultiStatus;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ResourcesPackageImpl extends EPackageImpl implements ResourcesPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sourceLocatorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectFactoryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass xmlProjectFactoryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass eclipseProjectFactoryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mavenProjectFactoryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType projectHandlerEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType backendContainerEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType multiStatusEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType progressMonitorEDataType = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.oomph.resources.ResourcesPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ResourcesPackageImpl()
  {
    super(eNS_URI, ResourcesFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link ResourcesPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ResourcesPackage init()
  {
    if (isInited)
    {
      return (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);
    }

    // Obtain or create and register package
    ResourcesPackageImpl theResourcesPackage = (ResourcesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ResourcesPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ResourcesPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    PredicatesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theResourcesPackage.createPackageContents();

    // Initialize created meta-data
    theResourcesPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theResourcesPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ResourcesPackage.eNS_URI, theResourcesPackage);
    return theResourcesPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSourceLocator()
  {
    return sourceLocatorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSourceLocator_RootFolder()
  {
    return (EAttribute)sourceLocatorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSourceLocator_ExcludedPaths()
  {
    return (EAttribute)sourceLocatorEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSourceLocator_ProjectFactories()
  {
    return (EReference)sourceLocatorEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSourceLocator_LocateNestedProjects()
  {
    return (EAttribute)sourceLocatorEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSourceLocator_Predicates()
  {
    return (EReference)sourceLocatorEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getSourceLocator__Matches__IProject()
  {
    return sourceLocatorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getSourceLocator__LoadProject__EList_BackendContainer_IProgressMonitor()
  {
    return sourceLocatorEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getSourceLocator__HandleProjects__EList_ProjectHandler_MultiStatus_IProgressMonitor()
  {
    return sourceLocatorEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectFactory()
  {
    return projectFactoryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectFactory_ExcludedPaths()
  {
    return (EAttribute)projectFactoryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getProjectFactory__CreateProject__BackendContainer_BackendContainer_IProgressMonitor()
  {
    return projectFactoryEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getProjectFactory__IsExcludedPath__BackendContainer_BackendContainer()
  {
    return projectFactoryEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getXMLProjectFactory()
  {
    return xmlProjectFactoryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getEclipseProjectFactory()
  {
    return eclipseProjectFactoryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMavenProjectFactory()
  {
    return mavenProjectFactoryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getProjectHandler()
  {
    return projectHandlerEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getBackendContainer()
  {
    return backendContainerEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMultiStatus()
  {
    return multiStatusEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getProgressMonitor()
  {
    return progressMonitorEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourcesFactory getResourcesFactory()
  {
    return (ResourcesFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
    {
      return;
    }
    isCreated = true;

    // Create classes and their features
    sourceLocatorEClass = createEClass(SOURCE_LOCATOR);
    createEAttribute(sourceLocatorEClass, SOURCE_LOCATOR__ROOT_FOLDER);
    createEAttribute(sourceLocatorEClass, SOURCE_LOCATOR__EXCLUDED_PATHS);
    createEReference(sourceLocatorEClass, SOURCE_LOCATOR__PROJECT_FACTORIES);
    createEReference(sourceLocatorEClass, SOURCE_LOCATOR__PREDICATES);
    createEAttribute(sourceLocatorEClass, SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS);
    createEOperation(sourceLocatorEClass, SOURCE_LOCATOR___MATCHES__IPROJECT);
    createEOperation(sourceLocatorEClass, SOURCE_LOCATOR___LOAD_PROJECT__ELIST_BACKENDCONTAINER_IPROGRESSMONITOR);
    createEOperation(sourceLocatorEClass, SOURCE_LOCATOR___HANDLE_PROJECTS__ELIST_PROJECTHANDLER_MULTISTATUS_IPROGRESSMONITOR);

    projectFactoryEClass = createEClass(PROJECT_FACTORY);
    createEAttribute(projectFactoryEClass, PROJECT_FACTORY__EXCLUDED_PATHS);
    createEOperation(projectFactoryEClass, PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_BACKENDCONTAINER_IPROGRESSMONITOR);
    createEOperation(projectFactoryEClass, PROJECT_FACTORY___IS_EXCLUDED_PATH__BACKENDCONTAINER_BACKENDCONTAINER);

    xmlProjectFactoryEClass = createEClass(XML_PROJECT_FACTORY);

    eclipseProjectFactoryEClass = createEClass(ECLIPSE_PROJECT_FACTORY);

    mavenProjectFactoryEClass = createEClass(MAVEN_PROJECT_FACTORY);

    // Create data types
    projectHandlerEDataType = createEDataType(PROJECT_HANDLER);
    backendContainerEDataType = createEDataType(BACKEND_CONTAINER);
    multiStatusEDataType = createEDataType(MULTI_STATUS);
    progressMonitorEDataType = createEDataType(PROGRESS_MONITOR);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
    {
      return;
    }
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    BasePackage theBasePackage = (BasePackage)EPackage.Registry.INSTANCE.getEPackage(BasePackage.eNS_URI);
    PredicatesPackage thePredicatesPackage = (PredicatesPackage)EPackage.Registry.INSTANCE.getEPackage(PredicatesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    sourceLocatorEClass.getESuperTypes().add(theBasePackage.getModelElement());
    projectFactoryEClass.getESuperTypes().add(theBasePackage.getModelElement());
    xmlProjectFactoryEClass.getESuperTypes().add(getProjectFactory());
    eclipseProjectFactoryEClass.getESuperTypes().add(getXMLProjectFactory());
    mavenProjectFactoryEClass.getESuperTypes().add(getXMLProjectFactory());

    // Initialize classes, features, and operations; add parameters
    initEClass(sourceLocatorEClass, SourceLocator.class, "SourceLocator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSourceLocator_RootFolder(), ecorePackage.getEString(), "rootFolder", null, 1, 1, SourceLocator.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSourceLocator_ExcludedPaths(), ecorePackage.getEString(), "excludedPaths", null, 0, -1, SourceLocator.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSourceLocator_ProjectFactories(), getProjectFactory(), null, "projectFactories", null, 0, -1, SourceLocator.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSourceLocator_Predicates(), thePredicatesPackage.getPredicate(), null, "predicates", null, 0, -1, SourceLocator.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSourceLocator_LocateNestedProjects(), ecorePackage.getEBoolean(), "locateNestedProjects", null, 0, 1, SourceLocator.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getSourceLocator__Matches__IProject(), ecorePackage.getEBoolean(), "matches", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, thePredicatesPackage.getProject(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getSourceLocator__LoadProject__EList_BackendContainer_IProgressMonitor(), thePredicatesPackage.getProject(), "loadProject", 0, 1,
        IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getProjectFactory(), "defaultProjectFactories", 0, -1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getBackendContainer(), "backendContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getProgressMonitor(), "monitor", 0, 1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getSourceLocator__HandleProjects__EList_ProjectHandler_MultiStatus_IProgressMonitor(), null, "handleProjects", 0, 1, IS_UNIQUE,
        IS_ORDERED);
    addEParameter(op, getProjectFactory(), "defaultProjectFactories", 0, -1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getProjectHandler(), "projectHandler", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getMultiStatus(), "status", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getProgressMonitor(), "monitor", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(projectFactoryEClass, ProjectFactory.class, "ProjectFactory", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProjectFactory_ExcludedPaths(), ecorePackage.getEString(), "excludedPaths", null, 0, -1, ProjectFactory.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getProjectFactory__CreateProject__BackendContainer_BackendContainer_IProgressMonitor(), thePredicatesPackage.getProject(),
        "createProject", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getBackendContainer(), "rootContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getBackendContainer(), "backendContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getProgressMonitor(), "monitor", 0, 1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getProjectFactory__IsExcludedPath__BackendContainer_BackendContainer(), ecorePackage.getEBoolean(), "isExcludedPath", 0, 1, IS_UNIQUE,
        IS_ORDERED);
    addEParameter(op, getBackendContainer(), "rootContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getBackendContainer(), "backendContainer", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(xmlProjectFactoryEClass, XMLProjectFactory.class, "XMLProjectFactory", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(eclipseProjectFactoryEClass, EclipseProjectFactory.class, "EclipseProjectFactory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mavenProjectFactoryEClass, MavenProjectFactory.class, "MavenProjectFactory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    // Initialize data types
    initEDataType(projectHandlerEDataType, ProjectHandler.class, "ProjectHandler", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(backendContainerEDataType, BackendContainer.class, "BackendContainer", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(multiStatusEDataType, MultiStatus.class, "MultiStatus", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(progressMonitorEDataType, IProgressMonitor.class, "ProgressMonitor", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Resources.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore";
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Resources.ecore" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/base/LabelProvider</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createLabelProviderAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/base/LabelProvider";
    addAnnotation(this, source, new String[] { "imageBaseURI",
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.resources.edit/icons/full/obj16" });
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
    addAnnotation(getSourceLocator_ExcludedPaths(), source, new String[] { "name", "excludedPath" });
    addAnnotation(getSourceLocator_ProjectFactories(), source, new String[] { "name", "projectFactory" });
    addAnnotation(getSourceLocator_Predicates(), source, new String[] { "name", "predicate" });
    addAnnotation(getProjectFactory_ExcludedPaths(), source, new String[] { "name", "excludedPath" });
  }

} // ResourcesPackageImpl

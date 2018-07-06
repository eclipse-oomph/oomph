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
package org.eclipse.oomph.setup.projects.impl;

import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.projects.PathVariableTask;
import org.eclipse.oomph.setup.projects.ProjectsBuildTask;
import org.eclipse.oomph.setup.projects.ProjectsFactory;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectsPackageImpl extends EPackageImpl implements ProjectsPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectsImportTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectsBuildTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass pathVariableTaskEClass = null;

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
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ProjectsPackageImpl()
  {
    super(eNS_URI, ProjectsFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link ProjectsPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ProjectsPackage init()
  {
    if (isInited)
    {
      return (ProjectsPackage)EPackage.Registry.INSTANCE.getEPackage(ProjectsPackage.eNS_URI);
    }

    // Obtain or create and register package
    ProjectsPackageImpl theProjectsPackage = (ProjectsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ProjectsPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ProjectsPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    ResourcesPackage.eINSTANCE.eClass();
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theProjectsPackage.createPackageContents();

    // Initialize created meta-data
    theProjectsPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theProjectsPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ProjectsPackage.eNS_URI, theProjectsPackage);
    return theProjectsPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectsImportTask()
  {
    return projectsImportTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectsImportTask_SourceLocators()
  {
    return (EReference)projectsImportTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectsBuildTask()
  {
    return projectsBuildTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectsBuildTask_Predicates()
  {
    return (EReference)projectsBuildTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectsBuildTask_OnlyNewProjects()
  {
    return (EAttribute)projectsBuildTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectsBuildTask_Refresh()
  {
    return (EAttribute)projectsBuildTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectsBuildTask_Clean()
  {
    return (EAttribute)projectsBuildTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectsBuildTask_Build()
  {
    return (EAttribute)projectsBuildTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPathVariableTask()
  {
    return pathVariableTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPathVariableTask_Name()
  {
    return (EAttribute)pathVariableTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPathVariableTask_URI()
  {
    return (EAttribute)pathVariableTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsFactory getProjectsFactory()
  {
    return (ProjectsFactory)getEFactoryInstance();
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
    pathVariableTaskEClass = createEClass(PATH_VARIABLE_TASK);
    createEAttribute(pathVariableTaskEClass, PATH_VARIABLE_TASK__NAME);
    createEAttribute(pathVariableTaskEClass, PATH_VARIABLE_TASK__URI);

    projectsImportTaskEClass = createEClass(PROJECTS_IMPORT_TASK);
    createEReference(projectsImportTaskEClass, PROJECTS_IMPORT_TASK__SOURCE_LOCATORS);

    projectsBuildTaskEClass = createEClass(PROJECTS_BUILD_TASK);
    createEReference(projectsBuildTaskEClass, PROJECTS_BUILD_TASK__PREDICATES);
    createEAttribute(projectsBuildTaskEClass, PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS);
    createEAttribute(projectsBuildTaskEClass, PROJECTS_BUILD_TASK__REFRESH);
    createEAttribute(projectsBuildTaskEClass, PROJECTS_BUILD_TASK__CLEAN);
    createEAttribute(projectsBuildTaskEClass, PROJECTS_BUILD_TASK__BUILD);
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
    SetupPackage theSetupPackage = (SetupPackage)EPackage.Registry.INSTANCE.getEPackage(SetupPackage.eNS_URI);
    ResourcesPackage theResourcesPackage = (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);
    PredicatesPackage thePredicatesPackage = (PredicatesPackage)EPackage.Registry.INSTANCE.getEPackage(PredicatesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    pathVariableTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    projectsImportTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    projectsBuildTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(pathVariableTaskEClass, PathVariableTask.class, "PathVariableTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPathVariableTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, PathVariableTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPathVariableTask_URI(), ecorePackage.getEString(), "uRI", null, 0, 1, PathVariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(projectsImportTaskEClass, ProjectsImportTask.class, "ProjectsImportTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProjectsImportTask_SourceLocators(), theResourcesPackage.getSourceLocator(), null, "sourceLocators", null, 1, -1,
        ProjectsImportTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    initEClass(projectsBuildTaskEClass, ProjectsBuildTask.class, "ProjectsBuildTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProjectsBuildTask_Predicates(), thePredicatesPackage.getPredicate(), null, "predicates", null, 0, -1, ProjectsBuildTask.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProjectsBuildTask_OnlyNewProjects(), ecorePackage.getEBoolean(), "onlyNewProjects", null, 0, 1, ProjectsBuildTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProjectsBuildTask_Refresh(), ecorePackage.getEBoolean(), "refresh", null, 0, 1, ProjectsBuildTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProjectsBuildTask_Clean(), ecorePackage.getEBoolean(), "clean", null, 0, 1, ProjectsBuildTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProjectsBuildTask_Build(), ecorePackage.getEBoolean(), "build", "true", 0, 1, ProjectsBuildTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Projects.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/oomph/setup/Redirect
    createRedirectAnnotations();
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Projects.ecore" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Enablement</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEnablementAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Enablement";
    addAnnotation(this, source, new String[] { "variableName", "setup.projects.p2", "repository", "${oomph.update.url}", "installableUnits",
        "org.eclipse.oomph.setup.projects.feature.group" });
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.projects.edit/icons/full/obj16" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/ValidTriggers</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createValidTriggersAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/ValidTriggers";
    addAnnotation(pathVariableTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
    addAnnotation(projectsImportTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
    addAnnotation(projectsBuildTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
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
    addAnnotation(getPathVariableTask_URI(), source, new String[] { "kind", "attribute", "name", "uri" });
    addAnnotation(getProjectsImportTask_SourceLocators(), source, new String[] { "name", "sourceLocator" });
    addAnnotation(getProjectsBuildTask_Predicates(), source, new String[] { "name", "predicate" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Redirect</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRedirectAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Redirect";
    addAnnotation(getPathVariableTask_URI(), source, new String[] {});
  }

} // ProjectsPackageImpl

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
package org.eclipse.oomph.projectconfig.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.projectconfig.ExclusionPredicate;
import org.eclipse.oomph.projectconfig.InclusionPredicate;
import org.eclipse.oomph.projectconfig.PreferenceFilter;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigFactory;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.util.ProjectConfigValidator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectConfigPackageImpl extends EPackageImpl implements ProjectConfigPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass workspaceConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preferenceProfileEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preferenceFilterEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inclusionPredicateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass exclusionPredicateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType patternEDataType = null;

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
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ProjectConfigPackageImpl()
  {
    super(eNS_URI, ProjectConfigFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link ProjectConfigPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ProjectConfigPackage init()
  {
    if (isInited)
    {
      return (ProjectConfigPackage)EPackage.Registry.INSTANCE.getEPackage(ProjectConfigPackage.eNS_URI);
    }

    // Obtain or create and register package
    ProjectConfigPackageImpl theProjectConfigPackage = (ProjectConfigPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ProjectConfigPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ProjectConfigPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    PredicatesPackage.eINSTANCE.eClass();
    PreferencesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theProjectConfigPackage.createPackageContents();

    // Initialize created meta-data
    theProjectConfigPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(theProjectConfigPackage, new EValidator.Descriptor()
    {
      public EValidator getEValidator()
      {
        return ProjectConfigValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    theProjectConfigPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ProjectConfigPackage.eNS_URI, theProjectConfigPackage);
    return theProjectConfigPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWorkspaceConfiguration()
  {
    return workspaceConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspaceConfiguration_Projects()
  {
    return (EReference)workspaceConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspaceConfiguration_DefaultPreferenceNode()
  {
    return (EReference)workspaceConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspaceConfiguration_InstancePreferenceNode()
  {
    return (EReference)workspaceConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getWorkspaceConfiguration__ApplyPreferenceProfiles()
  {
    return workspaceConfigurationEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getWorkspaceConfiguration__UpdatePreferenceProfileReferences()
  {
    return workspaceConfigurationEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getWorkspaceConfiguration__GetProject__String()
  {
    return workspaceConfigurationEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProject()
  {
    return projectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_Configuration()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_PreferenceProfiles()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_PreferenceNode()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_PreferenceProfileReferences()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getProject__GetProperty__URI()
  {
    return projectEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPreferenceProfile()
  {
    return preferenceProfileEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceProfile_PreferenceFilters()
  {
    return (EReference)preferenceProfileEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceProfile_ReferentProjects()
  {
    return (EReference)preferenceProfileEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPreferenceProfile_Name()
  {
    return (EAttribute)preferenceProfileEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceProfile_Project()
  {
    return (EReference)preferenceProfileEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceProfile_Predicates()
  {
    return (EReference)preferenceProfileEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getPreferenceProfile__Matches__IProject()
  {
    return preferenceProfileEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getPreferenceProfile__GetProperty__URI()
  {
    return preferenceProfileEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPreferenceFilter()
  {
    return preferenceFilterEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceFilter_PreferenceNode()
  {
    return (EReference)preferenceFilterEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceFilter_PreferenceProfile()
  {
    return (EReference)preferenceFilterEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPreferenceFilter_Inclusions()
  {
    return (EAttribute)preferenceFilterEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPreferenceFilter_Exclusions()
  {
    return (EAttribute)preferenceFilterEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreferenceFilter_Properties()
  {
    return (EReference)preferenceFilterEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getPreferenceFilter__Matches__String()
  {
    return preferenceFilterEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getPreferenceFilter__GetProperty__String()
  {
    return preferenceFilterEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInclusionPredicate()
  {
    return inclusionPredicateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInclusionPredicate_IncludedPreferenceProfiles()
  {
    return (EReference)inclusionPredicateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExclusionPredicate()
  {
    return exclusionPredicateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExclusionPredicate_ExcludedPreferenceProfiles()
  {
    return (EReference)exclusionPredicateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getPattern()
  {
    return patternEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectConfigFactory getProjectConfigFactory()
  {
    return (ProjectConfigFactory)getEFactoryInstance();
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
    workspaceConfigurationEClass = createEClass(WORKSPACE_CONFIGURATION);
    createEReference(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION__PROJECTS);
    createEReference(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION__DEFAULT_PREFERENCE_NODE);
    createEReference(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION__INSTANCE_PREFERENCE_NODE);
    createEOperation(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION___APPLY_PREFERENCE_PROFILES);
    createEOperation(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION___UPDATE_PREFERENCE_PROFILE_REFERENCES);
    createEOperation(workspaceConfigurationEClass, WORKSPACE_CONFIGURATION___GET_PROJECT__STRING);

    projectEClass = createEClass(PROJECT);
    createEReference(projectEClass, PROJECT__CONFIGURATION);
    createEReference(projectEClass, PROJECT__PREFERENCE_PROFILES);
    createEReference(projectEClass, PROJECT__PREFERENCE_NODE);
    createEReference(projectEClass, PROJECT__PREFERENCE_PROFILE_REFERENCES);
    createEOperation(projectEClass, PROJECT___GET_PROPERTY__URI);

    preferenceProfileEClass = createEClass(PREFERENCE_PROFILE);
    createEReference(preferenceProfileEClass, PREFERENCE_PROFILE__PREFERENCE_FILTERS);
    createEReference(preferenceProfileEClass, PREFERENCE_PROFILE__REFERENT_PROJECTS);
    createEAttribute(preferenceProfileEClass, PREFERENCE_PROFILE__NAME);
    createEReference(preferenceProfileEClass, PREFERENCE_PROFILE__PROJECT);
    createEReference(preferenceProfileEClass, PREFERENCE_PROFILE__PREDICATES);
    createEOperation(preferenceProfileEClass, PREFERENCE_PROFILE___MATCHES__IPROJECT);
    createEOperation(preferenceProfileEClass, PREFERENCE_PROFILE___GET_PROPERTY__URI);

    preferenceFilterEClass = createEClass(PREFERENCE_FILTER);
    createEReference(preferenceFilterEClass, PREFERENCE_FILTER__PREFERENCE_NODE);
    createEReference(preferenceFilterEClass, PREFERENCE_FILTER__PREFERENCE_PROFILE);
    createEAttribute(preferenceFilterEClass, PREFERENCE_FILTER__INCLUSIONS);
    createEAttribute(preferenceFilterEClass, PREFERENCE_FILTER__EXCLUSIONS);
    createEReference(preferenceFilterEClass, PREFERENCE_FILTER__PROPERTIES);
    createEOperation(preferenceFilterEClass, PREFERENCE_FILTER___MATCHES__STRING);
    createEOperation(preferenceFilterEClass, PREFERENCE_FILTER___GET_PROPERTY__STRING);

    inclusionPredicateEClass = createEClass(INCLUSION_PREDICATE);
    createEReference(inclusionPredicateEClass, INCLUSION_PREDICATE__INCLUDED_PREFERENCE_PROFILES);

    exclusionPredicateEClass = createEClass(EXCLUSION_PREDICATE);
    createEReference(exclusionPredicateEClass, EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES);

    // Create data types
    patternEDataType = createEDataType(PATTERN);
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
    PreferencesPackage thePreferencesPackage = (PreferencesPackage)EPackage.Registry.INSTANCE.getEPackage(PreferencesPackage.eNS_URI);
    PredicatesPackage thePredicatesPackage = (PredicatesPackage)EPackage.Registry.INSTANCE.getEPackage(PredicatesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    workspaceConfigurationEClass.getESuperTypes().add(theBasePackage.getModelElement());
    projectEClass.getESuperTypes().add(theBasePackage.getModelElement());
    preferenceProfileEClass.getESuperTypes().add(theBasePackage.getModelElement());
    preferenceFilterEClass.getESuperTypes().add(theBasePackage.getModelElement());
    inclusionPredicateEClass.getESuperTypes().add(thePredicatesPackage.getPredicate());
    exclusionPredicateEClass.getESuperTypes().add(thePredicatesPackage.getPredicate());

    // Initialize classes, features, and operations; add parameters
    initEClass(workspaceConfigurationEClass, WorkspaceConfiguration.class, "WorkspaceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getWorkspaceConfiguration_Projects(), getProject(), getProject_Configuration(), "projects", null, 0, -1, WorkspaceConfiguration.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getWorkspaceConfiguration_DefaultPreferenceNode(), thePreferencesPackage.getPreferenceNode(), null, "defaultPreferenceNode", null, 1, 1,
        WorkspaceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEReference(getWorkspaceConfiguration_InstancePreferenceNode(), thePreferencesPackage.getPreferenceNode(), null, "instancePreferenceNode", null, 1, 1,
        WorkspaceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    initEOperation(getWorkspaceConfiguration__ApplyPreferenceProfiles(), null, "applyPreferenceProfiles", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEOperation(getWorkspaceConfiguration__UpdatePreferenceProfileReferences(), null, "updatePreferenceProfileReferences", 0, 1, IS_UNIQUE, IS_ORDERED);

    EOperation op = initEOperation(getWorkspaceConfiguration__GetProject__String(), getProject(), "getProject", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(projectEClass, Project.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProject_Configuration(), getWorkspaceConfiguration(), getWorkspaceConfiguration_Projects(), "configuration", null, 1, 1, Project.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_PreferenceProfiles(), getPreferenceProfile(), getPreferenceProfile_Project(), "preferenceProfiles", null, 0, -1, Project.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_PreferenceNode(), thePreferencesPackage.getPreferenceNode(), null, "preferenceNode", null, 1, 1, Project.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_PreferenceProfileReferences(), getPreferenceProfile(), getPreferenceProfile_ReferentProjects(), "preferenceProfileReferences",
        null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    op = initEOperation(getProject__GetProperty__URI(), thePreferencesPackage.getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, thePreferencesPackage.getURI(), "path", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(preferenceProfileEClass, PreferenceProfile.class, "PreferenceProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPreferenceProfile_PreferenceFilters(), getPreferenceFilter(), getPreferenceFilter_PreferenceProfile(), "preferenceFilters", null, 0, -1,
        PreferenceProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEReference(getPreferenceProfile_ReferentProjects(), getProject(), getProject_PreferenceProfileReferences(), "referentProjects", null, 0, -1,
        PreferenceProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEAttribute(getPreferenceProfile_Name(), ecorePackage.getEString(), "name", null, 1, 1, PreferenceProfile.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceProfile_Project(), getProject(), getProject_PreferenceProfiles(), "project", null, 0, 1, PreferenceProfile.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceProfile_Predicates(), thePredicatesPackage.getPredicate(), null, "predicates", null, 0, -1, PreferenceProfile.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getPreferenceProfile__Matches__IProject(), ecorePackage.getEBoolean(), "matches", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, thePredicatesPackage.getProject(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getPreferenceProfile__GetProperty__URI(), thePreferencesPackage.getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, thePreferencesPackage.getURI(), "path", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(preferenceFilterEClass, PreferenceFilter.class, "PreferenceFilter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPreferenceFilter_PreferenceNode(), thePreferencesPackage.getPreferenceNode(), null, "preferenceNode", null, 1, 1, PreferenceFilter.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceFilter_PreferenceProfile(), getPreferenceProfile(), getPreferenceProfile_PreferenceFilters(), "preferenceProfile", null, 1, 1,
        PreferenceFilter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEAttribute(getPreferenceFilter_Inclusions(), getPattern(), "inclusions", ".*", 1, 1, PreferenceFilter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPreferenceFilter_Exclusions(), getPattern(), "exclusions", "", 1, 1, PreferenceFilter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceFilter_Properties(), thePreferencesPackage.getProperty(), null, "properties", null, 0, -1, PreferenceFilter.class, IS_TRANSIENT,
        IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    op = initEOperation(getPreferenceFilter__Matches__String(), ecorePackage.getEBoolean(), "matches", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, ecorePackage.getEString(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getPreferenceFilter__GetProperty__String(), thePreferencesPackage.getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(inclusionPredicateEClass, InclusionPredicate.class, "InclusionPredicate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInclusionPredicate_IncludedPreferenceProfiles(), getPreferenceProfile(), null, "includedPreferenceProfiles", null, 0, -1,
        InclusionPredicate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    initEClass(exclusionPredicateEClass, ExclusionPredicate.class, "ExclusionPredicate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getExclusionPredicate_ExcludedPreferenceProfiles(), getPreferenceProfile(), null, "excludedPreferenceProfiles", null, 0, -1,
        ExclusionPredicate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    // Initialize data types
    initEDataType(patternEDataType, Pattern.class, "Pattern", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.projectconfig.edit/icons/full/obj16" });
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
    addAnnotation(getWorkspaceConfiguration_Projects(), source, new String[] { "name", "project" });
    addAnnotation(getProject_PreferenceProfiles(), source, new String[] { "name", "preferenceProfile" });
    addAnnotation(getPreferenceProfile_PreferenceFilters(), source, new String[] { "name", "preferenceFilter" });
    addAnnotation(getPreferenceProfile_Predicates(), source, new String[] { "name", "predicate" });
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
    addAnnotation(projectEClass, source,
        new String[] { "constraints", "AllPreferencesManaged PreferenceProfileReferencesSpecifyUniqueProperties AllPropertiesHaveManagedValue" });
  }

} // ProjectConfigPackageImpl

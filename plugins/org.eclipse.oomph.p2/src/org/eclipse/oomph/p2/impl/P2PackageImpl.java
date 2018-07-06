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
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.p2.Configuration;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.RepositoryType;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.RequirementType;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class P2PackageImpl extends EPackageImpl implements P2Package
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass profileDefinitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass requirementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass repositoryListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass repositoryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum repositoryTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum versionSegmentEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum requirementTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType versionEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType versionRangeEDataType = null;

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
   * @see org.eclipse.oomph.p2.P2Package#eNS_URI
   * @see #init()
   * @generated
   */
  private P2PackageImpl()
  {
    super(eNS_URI, P2Factory.eINSTANCE);
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
   * <p>This method is used to initialize {@link P2Package#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static P2Package init()
  {
    if (isInited)
    {
      return (P2Package)EPackage.Registry.INSTANCE.getEPackage(P2Package.eNS_URI);
    }

    // Obtain or create and register package
    P2PackageImpl theP2Package = (P2PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof P2PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
        : new P2PackageImpl());

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theP2Package.createPackageContents();

    // Initialize created meta-data
    theP2Package.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theP2Package.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(P2Package.eNS_URI, theP2Package);
    return theP2Package;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProfileDefinition()
  {
    return profileDefinitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProfileDefinition_Requirements()
  {
    return (EReference)profileDefinitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProfileDefinition_Repositories()
  {
    return (EReference)profileDefinitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProfileDefinition_IncludeSourceBundles()
  {
    return (EAttribute)profileDefinitionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getProfileDefinition__SetRequirements__EList()
  {
    return profileDefinitionEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getProfileDefinition__SetRepositories__EList()
  {
    return profileDefinitionEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConfiguration()
  {
    return configurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_WS()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_OS()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Arch()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRequirement()
  {
    return requirementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_ID()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Name()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Namespace()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_VersionRange()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Optional()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Filter()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Type()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRequirement_Greedy()
  {
    return (EAttribute)requirementEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getRequirement__SetVersionRange__Version_VersionSegment()
  {
    return requirementEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRepositoryList()
  {
    return repositoryListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRepositoryList_Repositories()
  {
    return (EReference)repositoryListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRepositoryList_Name()
  {
    return (EAttribute)repositoryListEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRepository()
  {
    return repositoryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRepository_URL()
  {
    return (EAttribute)repositoryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRepository_Type()
  {
    return (EAttribute)repositoryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getRepositoryType()
  {
    return repositoryTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getVersionSegment()
  {
    return versionSegmentEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getRequirementType()
  {
    return requirementTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getVersion()
  {
    return versionEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getVersionRange()
  {
    return versionRangeEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public P2Factory getP2Factory()
  {
    return (P2Factory)getEFactoryInstance();
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
    profileDefinitionEClass = createEClass(PROFILE_DEFINITION);
    createEReference(profileDefinitionEClass, PROFILE_DEFINITION__REQUIREMENTS);
    createEReference(profileDefinitionEClass, PROFILE_DEFINITION__REPOSITORIES);
    createEAttribute(profileDefinitionEClass, PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES);
    createEOperation(profileDefinitionEClass, PROFILE_DEFINITION___SET_REQUIREMENTS__ELIST);
    createEOperation(profileDefinitionEClass, PROFILE_DEFINITION___SET_REPOSITORIES__ELIST);

    configurationEClass = createEClass(CONFIGURATION);
    createEAttribute(configurationEClass, CONFIGURATION__WS);
    createEAttribute(configurationEClass, CONFIGURATION__OS);
    createEAttribute(configurationEClass, CONFIGURATION__ARCH);

    requirementEClass = createEClass(REQUIREMENT);
    createEAttribute(requirementEClass, REQUIREMENT__ID);
    createEAttribute(requirementEClass, REQUIREMENT__NAME);
    createEAttribute(requirementEClass, REQUIREMENT__NAMESPACE);
    createEAttribute(requirementEClass, REQUIREMENT__VERSION_RANGE);
    createEAttribute(requirementEClass, REQUIREMENT__OPTIONAL);
    createEAttribute(requirementEClass, REQUIREMENT__GREEDY);
    createEAttribute(requirementEClass, REQUIREMENT__FILTER);
    createEAttribute(requirementEClass, REQUIREMENT__TYPE);
    createEOperation(requirementEClass, REQUIREMENT___SET_VERSION_RANGE__VERSION_VERSIONSEGMENT);

    repositoryListEClass = createEClass(REPOSITORY_LIST);
    createEReference(repositoryListEClass, REPOSITORY_LIST__REPOSITORIES);
    createEAttribute(repositoryListEClass, REPOSITORY_LIST__NAME);

    repositoryEClass = createEClass(REPOSITORY);
    createEAttribute(repositoryEClass, REPOSITORY__URL);
    createEAttribute(repositoryEClass, REPOSITORY__TYPE);

    // Create enums
    repositoryTypeEEnum = createEEnum(REPOSITORY_TYPE);
    versionSegmentEEnum = createEEnum(VERSION_SEGMENT);
    requirementTypeEEnum = createEEnum(REQUIREMENT_TYPE);

    // Create data types
    versionEDataType = createEDataType(VERSION);
    versionRangeEDataType = createEDataType(VERSION_RANGE);
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

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    profileDefinitionEClass.getESuperTypes().add(theBasePackage.getModelElement());
    configurationEClass.getESuperTypes().add(theBasePackage.getModelElement());
    requirementEClass.getESuperTypes().add(theBasePackage.getModelElement());
    repositoryListEClass.getESuperTypes().add(theBasePackage.getModelElement());
    repositoryEClass.getESuperTypes().add(theBasePackage.getModelElement());

    // Initialize classes, features, and operations; add parameters
    initEClass(profileDefinitionEClass, ProfileDefinition.class, "ProfileDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProfileDefinition_Requirements(), getRequirement(), null, "requirements", null, 0, -1, ProfileDefinition.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProfileDefinition_Repositories(), getRepository(), null, "repositories", null, 0, -1, ProfileDefinition.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProfileDefinition_IncludeSourceBundles(), ecorePackage.getEBoolean(), "includeSourceBundles", null, 0, 1, ProfileDefinition.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getProfileDefinition__SetRequirements__EList(), null, "setRequirements", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getRequirement(), "requirements", 0, -1, IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getProfileDefinition__SetRepositories__EList(), null, "setRepositories", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getRepository(), "repositories", 0, -1, IS_UNIQUE, IS_ORDERED);

    initEClass(configurationEClass, Configuration.class, "Configuration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConfiguration_WS(), ecorePackage.getEString(), "wS", "ANY", 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_OS(), ecorePackage.getEString(), "oS", "ANY", 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_Arch(), ecorePackage.getEString(), "arch", "ANY", 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(requirementEClass, Requirement.class, "Requirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRequirement_ID(), ecorePackage.getEString(), "iD", null, 1, 1, Requirement.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Name(), ecorePackage.getEString(), "name", null, 1, 1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Namespace(), ecorePackage.getEString(), "namespace", "org.eclipse.equinox.p2.iu", 1, 1, Requirement.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_VersionRange(), getVersionRange(), "versionRange", "0.0.0", 0, 1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Greedy(), ecorePackage.getEBoolean(), "greedy", "true", 0, 1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Filter(), ecorePackage.getEString(), "filter", null, 0, 1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRequirement_Type(), getRequirementType(), "type", null, 0, 1, Requirement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    op = initEOperation(getRequirement__SetVersionRange__Version_VersionSegment(), null, "setVersionRange", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getVersion(), "version", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getVersionSegment(), "segment", 0, 1, IS_UNIQUE, IS_ORDERED);

    initEClass(repositoryListEClass, RepositoryList.class, "RepositoryList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRepositoryList_Repositories(), getRepository(), null, "repositories", null, 0, -1, RepositoryList.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRepositoryList_Name(), ecorePackage.getEString(), "name", null, 0, 1, RepositoryList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(repositoryEClass, Repository.class, "Repository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRepository_URL(), ecorePackage.getEString(), "uRL", null, 1, 1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRepository_Type(), getRepositoryType(), "type", "Combined", 1, 1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(repositoryTypeEEnum, RepositoryType.class, "RepositoryType");
    addEEnumLiteral(repositoryTypeEEnum, RepositoryType.METADATA);
    addEEnumLiteral(repositoryTypeEEnum, RepositoryType.ARTIFACT);
    addEEnumLiteral(repositoryTypeEEnum, RepositoryType.COMBINED);

    initEEnum(versionSegmentEEnum, VersionSegment.class, "VersionSegment");
    addEEnumLiteral(versionSegmentEEnum, VersionSegment.MAJOR);
    addEEnumLiteral(versionSegmentEEnum, VersionSegment.MINOR);
    addEEnumLiteral(versionSegmentEEnum, VersionSegment.MICRO);
    addEEnumLiteral(versionSegmentEEnum, VersionSegment.QUALIFIER);

    initEEnum(requirementTypeEEnum, RequirementType.class, "RequirementType");
    addEEnumLiteral(requirementTypeEEnum, RequirementType.NONE);
    addEEnumLiteral(requirementTypeEEnum, RequirementType.FEATURE);
    addEEnumLiteral(requirementTypeEEnum, RequirementType.PROJECT);

    // Initialize data types
    initEDataType(versionEDataType, Version.class, "Version", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(versionRangeEDataType, VersionRange.class, "VersionRange", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/oomph/setup/Redirect
    createRedirectAnnotations();
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
    addAnnotation(this, source,
        new String[] { "imageBaseURI", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.p2.edit/icons/full/obj16" });
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
    addAnnotation(getProfileDefinition_Requirements(), source, new String[] { "name", "requirement" });
    addAnnotation(getProfileDefinition_Repositories(), source, new String[] { "name", "repository" });
    addAnnotation(getConfiguration_WS(), source, new String[] { "kind", "attribute", "name", "ws" });
    addAnnotation(getConfiguration_OS(), source, new String[] { "kind", "attribute", "name", "os" });
    addAnnotation(getRequirement_ID(), source, new String[] { "kind", "attribute", "name", "id" });
    addAnnotation(getRepositoryList_Repositories(), source, new String[] { "name", "repository" });
    addAnnotation(getRepository_URL(), source, new String[] { "kind", "attribute", "name", "url" });
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
    addAnnotation(getRepository_URL(), source, new String[] {});
  }

} // P2PackageImpl

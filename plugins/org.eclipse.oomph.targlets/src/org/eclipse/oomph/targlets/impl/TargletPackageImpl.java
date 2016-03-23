/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.targlets.CSpecGenerator;
import org.eclipse.oomph.targlets.CSpexGenerator;
import org.eclipse.oomph.targlets.CategoryGenerator;
import org.eclipse.oomph.targlets.ComponentDefGenerator;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtGenerator;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.DropinLocation;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.PluginGenerator;
import org.eclipse.oomph.targlets.ProductGenerator;
import org.eclipse.oomph.targlets.ProjectNameGenerator;
import org.eclipse.oomph.targlets.SiteGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletContainer;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletPackageImpl extends EPackageImpl implements TargletPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass targletContainerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass targletEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass componentExtensionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass componentDefinitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iuGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass featureGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass pluginGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass componentDefGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass componentExtGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass cSpecGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass cSpexGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass siteGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass categoryGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass productGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectNameGeneratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dropinLocationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType installableUnitEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType stringToVersionMapEDataType = null;

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
   * @see org.eclipse.oomph.targlets.TargletPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private TargletPackageImpl()
  {
    super(eNS_URI, TargletFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link TargletPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static TargletPackage init()
  {
    if (isInited)
    {
      return (TargletPackage)EPackage.Registry.INSTANCE.getEPackage(TargletPackage.eNS_URI);
    }

    // Obtain or create and register package
    TargletPackageImpl theTargletPackage = (TargletPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TargletPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TargletPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    P2Package.eINSTANCE.eClass();
    ResourcesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theTargletPackage.createPackageContents();

    // Initialize created meta-data
    theTargletPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theTargletPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(TargletPackage.eNS_URI, theTargletPackage);
    return theTargletPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTargletContainer()
  {
    return targletContainerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletContainer_ID()
  {
    return (EAttribute)targletContainerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTargletContainer_Targlets()
  {
    return (EReference)targletContainerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTarglet()
  {
    return targletEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_Name()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_Requirements()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_SourceLocators()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_InstallableUnitGenerators()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_DropinLocations()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(11);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_RepositoryLists()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_ActiveRepositoryListName()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_ActiveRepositoryList()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_ActiveRepositories()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_IncludeSources()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_IncludeAllPlatforms()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_IncludeAllRequirements()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getComponentExtension()
  {
    return componentExtensionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getComponentExtension_Requirements()
  {
    return (EReference)componentExtensionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getComponentDefinition()
  {
    return componentDefinitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getComponentDefinition_ID()
  {
    return (EAttribute)componentDefinitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getComponentDefinition_Version()
  {
    return (EAttribute)componentDefinitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIUGenerator()
  {
    return iuGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getIUGenerator__GenerateIUs__IProject_String_Map_EList()
  {
    return iuGeneratorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFeatureGenerator()
  {
    return featureGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPluginGenerator()
  {
    return pluginGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getComponentDefGenerator()
  {
    return componentDefGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getComponentExtGenerator()
  {
    return componentExtGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCSpecGenerator()
  {
    return cSpecGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCSpexGenerator()
  {
    return cSpexGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSiteGenerator()
  {
    return siteGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCategoryGenerator()
  {
    return categoryGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProductGenerator()
  {
    return productGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectNameGenerator()
  {
    return projectNameGeneratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDropinLocation()
  {
    return dropinLocationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDropinLocation_RootFolder()
  {
    return (EAttribute)dropinLocationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDropinLocation_Recursive()
  {
    return (EAttribute)dropinLocationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getInstallableUnit()
  {
    return installableUnitEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getStringToVersionMap()
  {
    return stringToVersionMapEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletFactory getTargletFactory()
  {
    return (TargletFactory)getEFactoryInstance();
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
    targletContainerEClass = createEClass(TARGLET_CONTAINER);
    createEAttribute(targletContainerEClass, TARGLET_CONTAINER__ID);
    createEReference(targletContainerEClass, TARGLET_CONTAINER__TARGLETS);

    targletEClass = createEClass(TARGLET);
    createEAttribute(targletEClass, TARGLET__NAME);
    createEReference(targletEClass, TARGLET__REQUIREMENTS);
    createEReference(targletEClass, TARGLET__SOURCE_LOCATORS);
    createEReference(targletEClass, TARGLET__INSTALLABLE_UNIT_GENERATORS);
    createEReference(targletEClass, TARGLET__REPOSITORY_LISTS);
    createEAttribute(targletEClass, TARGLET__ACTIVE_REPOSITORY_LIST_NAME);
    createEReference(targletEClass, TARGLET__ACTIVE_REPOSITORY_LIST);
    createEReference(targletEClass, TARGLET__ACTIVE_REPOSITORIES);
    createEAttribute(targletEClass, TARGLET__INCLUDE_SOURCES);
    createEAttribute(targletEClass, TARGLET__INCLUDE_ALL_PLATFORMS);
    createEAttribute(targletEClass, TARGLET__INCLUDE_ALL_REQUIREMENTS);
    createEReference(targletEClass, TARGLET__DROPIN_LOCATIONS);

    componentExtensionEClass = createEClass(COMPONENT_EXTENSION);
    createEReference(componentExtensionEClass, COMPONENT_EXTENSION__REQUIREMENTS);

    componentDefinitionEClass = createEClass(COMPONENT_DEFINITION);
    createEAttribute(componentDefinitionEClass, COMPONENT_DEFINITION__ID);
    createEAttribute(componentDefinitionEClass, COMPONENT_DEFINITION__VERSION);

    iuGeneratorEClass = createEClass(IU_GENERATOR);
    createEOperation(iuGeneratorEClass, IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST);

    featureGeneratorEClass = createEClass(FEATURE_GENERATOR);

    pluginGeneratorEClass = createEClass(PLUGIN_GENERATOR);

    componentDefGeneratorEClass = createEClass(COMPONENT_DEF_GENERATOR);

    componentExtGeneratorEClass = createEClass(COMPONENT_EXT_GENERATOR);

    cSpecGeneratorEClass = createEClass(CSPEC_GENERATOR);

    cSpexGeneratorEClass = createEClass(CSPEX_GENERATOR);

    siteGeneratorEClass = createEClass(SITE_GENERATOR);

    categoryGeneratorEClass = createEClass(CATEGORY_GENERATOR);

    productGeneratorEClass = createEClass(PRODUCT_GENERATOR);

    projectNameGeneratorEClass = createEClass(PROJECT_NAME_GENERATOR);

    dropinLocationEClass = createEClass(DROPIN_LOCATION);
    createEAttribute(dropinLocationEClass, DROPIN_LOCATION__ROOT_FOLDER);
    createEAttribute(dropinLocationEClass, DROPIN_LOCATION__RECURSIVE);

    // Create data types
    installableUnitEDataType = createEDataType(INSTALLABLE_UNIT);
    stringToVersionMapEDataType = createEDataType(STRING_TO_VERSION_MAP);
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
    P2Package theP2Package = (P2Package)EPackage.Registry.INSTANCE.getEPackage(P2Package.eNS_URI);
    ResourcesPackage theResourcesPackage = (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);
    PredicatesPackage thePredicatesPackage = (PredicatesPackage)EPackage.Registry.INSTANCE.getEPackage(PredicatesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    targletContainerEClass.getESuperTypes().add(theBasePackage.getModelElement());
    targletEClass.getESuperTypes().add(theBasePackage.getModelElement());
    componentExtensionEClass.getESuperTypes().add(theBasePackage.getModelElement());
    componentDefinitionEClass.getESuperTypes().add(getComponentExtension());
    iuGeneratorEClass.getESuperTypes().add(theBasePackage.getModelElement());
    featureGeneratorEClass.getESuperTypes().add(getIUGenerator());
    pluginGeneratorEClass.getESuperTypes().add(getIUGenerator());
    componentDefGeneratorEClass.getESuperTypes().add(getIUGenerator());
    componentExtGeneratorEClass.getESuperTypes().add(getIUGenerator());
    cSpecGeneratorEClass.getESuperTypes().add(getIUGenerator());
    cSpexGeneratorEClass.getESuperTypes().add(getIUGenerator());
    siteGeneratorEClass.getESuperTypes().add(getIUGenerator());
    categoryGeneratorEClass.getESuperTypes().add(getSiteGenerator());
    productGeneratorEClass.getESuperTypes().add(getIUGenerator());
    projectNameGeneratorEClass.getESuperTypes().add(getIUGenerator());

    // Initialize classes, features, and operations; add parameters
    initEClass(targletContainerEClass, TargletContainer.class, "TargletContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTargletContainer_ID(), ecorePackage.getEString(), "iD", null, 0, 1, TargletContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTargletContainer_Targlets(), getTarglet(), null, "targlets", null, 0, -1, TargletContainer.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(targletEClass, Targlet.class, "Targlet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTarglet_Name(), ecorePackage.getEString(), "name", null, 1, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_Requirements(), theP2Package.getRequirement(), null, "requirements", null, 0, -1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_SourceLocators(), theResourcesPackage.getSourceLocator(), null, "sourceLocators", null, 0, -1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_InstallableUnitGenerators(), getIUGenerator(), null, "installableUnitGenerators", null, 0, -1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_RepositoryLists(), theP2Package.getRepositoryList(), null, "repositoryLists", null, 0, -1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_ActiveRepositoryListName(), ecorePackage.getEString(), "activeRepositoryListName", null, 0, 1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_ActiveRepositoryList(), theP2Package.getRepositoryList(), null, "activeRepositoryList", null, 0, 1, Targlet.class, IS_TRANSIENT,
        IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_ActiveRepositories(), theP2Package.getRepository(), null, "activeRepositories", null, 0, -1, Targlet.class, IS_TRANSIENT,
        IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_IncludeSources(), ecorePackage.getEBoolean(), "includeSources", "true", 0, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_IncludeAllPlatforms(), ecorePackage.getEBoolean(), "includeAllPlatforms", null, 0, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_IncludeAllRequirements(), ecorePackage.getEBoolean(), "includeAllRequirements", "true", 0, 1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_DropinLocations(), getDropinLocation(), null, "dropinLocations", null, 0, -1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(componentExtensionEClass, ComponentExtension.class, "ComponentExtension", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getComponentExtension_Requirements(), theP2Package.getRequirement(), null, "requirements", null, 0, -1, ComponentExtension.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(componentDefinitionEClass, ComponentDefinition.class, "ComponentDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getComponentDefinition_ID(), ecorePackage.getEString(), "iD", null, 1, 1, ComponentDefinition.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getComponentDefinition_Version(), theP2Package.getVersion(), "version", "1.0.0", 0, 1, ComponentDefinition.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iuGeneratorEClass, IUGenerator.class, "IUGenerator", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    EOperation op = initEOperation(getIUGenerator__GenerateIUs__IProject_String_Map_EList(), null, "generateIUs", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, thePredicatesPackage.getProject(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, ecorePackage.getEString(), "qualifierReplacement", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getStringToVersionMap(), "iuVersions", 0, 1, IS_UNIQUE, IS_ORDERED);
    addEParameter(op, getInstallableUnit(), "result", 0, -1, IS_UNIQUE, IS_ORDERED);
    addEException(op, theBasePackage.getException());

    initEClass(featureGeneratorEClass, FeatureGenerator.class, "FeatureGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(pluginGeneratorEClass, PluginGenerator.class, "PluginGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(componentDefGeneratorEClass, ComponentDefGenerator.class, "ComponentDefGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(componentExtGeneratorEClass, ComponentExtGenerator.class, "ComponentExtGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(cSpecGeneratorEClass, CSpecGenerator.class, "CSpecGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(cSpexGeneratorEClass, CSpexGenerator.class, "CSpexGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(siteGeneratorEClass, SiteGenerator.class, "SiteGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(categoryGeneratorEClass, CategoryGenerator.class, "CategoryGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(productGeneratorEClass, ProductGenerator.class, "ProductGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(projectNameGeneratorEClass, ProjectNameGenerator.class, "ProjectNameGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(dropinLocationEClass, DropinLocation.class, "DropinLocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDropinLocation_RootFolder(), ecorePackage.getEString(), "rootFolder", null, 1, 1, DropinLocation.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDropinLocation_Recursive(), ecorePackage.getEBoolean(), "recursive", "true", 0, 1, DropinLocation.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize data types
    initEDataType(installableUnitEDataType, IInstallableUnit.class, "InstallableUnit", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(stringToVersionMapEDataType, Map.class, "StringToVersionMap", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS,
        "java.util.Map<java.lang.String, org.eclipse.equinox.p2.metadata.Version>");

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Targlets.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/oomph/setup/NoExpand
    createNoExpandAnnotations();
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Targlets.ecore" });
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
        new String[] { "imageBaseURI", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.targlets.edit/icons/full/obj16" });
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
    addAnnotation(getTargletContainer_ID(), source, new String[] { "kind", "attribute", "name", "id" });
    addAnnotation(getTargletContainer_Targlets(), source, new String[] { "name", "targlet" });
    addAnnotation(getTarglet_Requirements(), source, new String[] { "name", "requirement" });
    addAnnotation(getTarglet_SourceLocators(), source, new String[] { "name", "sourceLocator" });
    addAnnotation(getTarglet_InstallableUnitGenerators(), source, new String[] { "name", "installableUnitGenerator" });
    addAnnotation(getTarglet_RepositoryLists(), source, new String[] { "name", "repositoryList" });
    addAnnotation(getTarglet_ActiveRepositoryListName(), source, new String[] { "kind", "attribute", "name", "activeRepositoryList" });
    addAnnotation(getTarglet_ActiveRepositoryList(), source, new String[] { "name", "activeRepository" });
    addAnnotation(getTarglet_ActiveRepositories(), source, new String[] { "name", "activeRepository" });
    addAnnotation(getTarglet_DropinLocations(), source, new String[] { "name", "dropinLocation" });
    addAnnotation(getComponentExtension_Requirements(), source, new String[] { "name", "requirement" });
    addAnnotation(getComponentDefinition_ID(), source, new String[] { "kind", "attribute", "name", "id" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/NoExpand</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createNoExpandAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/NoExpand";
    addAnnotation(getTargletContainer_ID(), source, new String[] {});
  }

} // TargletPackageImpl

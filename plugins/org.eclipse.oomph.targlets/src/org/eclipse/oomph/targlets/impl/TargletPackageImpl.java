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
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

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
public class TargletPackageImpl extends EPackageImpl implements TargletPackage
{
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
    TargletPackageImpl theTargletPackage = (TargletPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TargletPackageImpl ? EPackage.Registry.INSTANCE
        .get(eNS_URI) : new TargletPackageImpl());

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
  public EReference getTarglet_RepositoryLists()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_ActiveRepositoryList()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTarglet_ActiveRepositories()
  {
    return (EReference)targletEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_IncludeSources()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTarglet_IncludeAllPlatforms()
  {
    return (EAttribute)targletEClass.getEStructuralFeatures().get(7);
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
    targletEClass = createEClass(TARGLET);
    createEAttribute(targletEClass, TARGLET__NAME);
    createEReference(targletEClass, TARGLET__REQUIREMENTS);
    createEReference(targletEClass, TARGLET__SOURCE_LOCATORS);
    createEReference(targletEClass, TARGLET__REPOSITORY_LISTS);
    createEAttribute(targletEClass, TARGLET__ACTIVE_REPOSITORY_LIST);
    createEReference(targletEClass, TARGLET__ACTIVE_REPOSITORIES);
    createEAttribute(targletEClass, TARGLET__INCLUDE_SOURCES);
    createEAttribute(targletEClass, TARGLET__INCLUDE_ALL_PLATFORMS);

    componentExtensionEClass = createEClass(COMPONENT_EXTENSION);
    createEReference(componentExtensionEClass, COMPONENT_EXTENSION__REQUIREMENTS);

    componentDefinitionEClass = createEClass(COMPONENT_DEFINITION);
    createEAttribute(componentDefinitionEClass, COMPONENT_DEFINITION__ID);
    createEAttribute(componentDefinitionEClass, COMPONENT_DEFINITION__VERSION);
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

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    targletEClass.getESuperTypes().add(theBasePackage.getModelElement());
    componentExtensionEClass.getESuperTypes().add(theBasePackage.getModelElement());
    componentDefinitionEClass.getESuperTypes().add(getComponentExtension());

    // Initialize classes, features, and operations; add parameters
    initEClass(targletEClass, Targlet.class, "Targlet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTarglet_Name(), ecorePackage.getEString(), "name", null, 1, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_Requirements(), theP2Package.getRequirement(), null, "requirements", null, 0, -1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_SourceLocators(), theResourcesPackage.getSourceLocator(), null, "sourceLocators", null, 0, -1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_RepositoryLists(), theP2Package.getRepositoryList(), null, "repositoryLists", null, 0, -1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_ActiveRepositoryList(), ecorePackage.getEString(), "activeRepositoryList", null, 0, 1, Targlet.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTarglet_ActiveRepositories(), theP2Package.getRepository(), null, "activeRepositories", null, 0, -1, Targlet.class, IS_TRANSIENT,
        IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_IncludeSources(), ecorePackage.getEBoolean(), "includeSources", "true", 0, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTarglet_IncludeAllPlatforms(), ecorePackage.getEBoolean(), "includeAllPlatforms", null, 0, 1, Targlet.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(componentExtensionEClass, ComponentExtension.class, "ComponentExtension", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getComponentExtension_Requirements(), theP2Package.getRequirement(), null, "requirements", null, 0, -1, ComponentExtension.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(componentDefinitionEClass, ComponentDefinition.class, "ComponentDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getComponentDefinition_ID(), ecorePackage.getEString(), "iD", null, 1, 1, ComponentDefinition.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getComponentDefinition_Version(), theP2Package.getVersion(), "version", "1.0.0", 0, 1, ComponentDefinition.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Targlets.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/LabelProvider
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Targlets.ecore" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/LabelProvider</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createLabelProviderAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/LabelProvider";
    addAnnotation(this, source, new String[] { "imageBaseURI",
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.targlets.edit/icons/full/obj16" });
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
    addAnnotation(getTarglet_Requirements(), source, new String[] { "name", "requirement" });
    addAnnotation(getTarglet_SourceLocators(), source, new String[] { "name", "sourceLocator" });
    addAnnotation(getTarglet_RepositoryLists(), source, new String[] { "name", "repositoryList" });
    addAnnotation(getTarglet_ActiveRepositories(), source, new String[] { "name", "activeRepository" });
    addAnnotation(getComponentExtension_Requirements(), source, new String[] { "name", "requirement" });
    addAnnotation(getComponentDefinition_ID(), source, new String[] { "kind", "attribute", "name", "id" });
  }

} // TargletPackageImpl

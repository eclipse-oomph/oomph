/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.maven.ConstraintType;
import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;
import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.POMXMLUtil;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.resources.ResourcesPackage;

import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MavenPackageImpl extends EPackageImpl implements MavenPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass realmEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass domElementEClass = null;

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
  private EClass coordinateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass parentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dependencyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertyReferenceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum constraintTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType documentEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType elementEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType elementEditEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType textRegionEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType xPathEDataType = null;

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
   * @see org.eclipse.oomph.maven.MavenPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private MavenPackageImpl()
  {
    super(eNS_URI, MavenFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link MavenPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static MavenPackage init()
  {
    if (isInited)
    {
      return (MavenPackage)EPackage.Registry.INSTANCE.getEPackage(MavenPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredMavenPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    MavenPackageImpl theMavenPackage = registeredMavenPackage instanceof MavenPackageImpl ? (MavenPackageImpl)registeredMavenPackage : new MavenPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();
    PredicatesPackage.eINSTANCE.eClass();
    ResourcesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theMavenPackage.createPackageContents();

    // Initialize created meta-data
    theMavenPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(theMavenPackage, new EValidator.Descriptor()
    {
      @Override
      public EValidator getEValidator()
      {
        return MavenValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    theMavenPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(MavenPackage.eNS_URI, theMavenPackage);
    return theMavenPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getRealm()
  {
    return realmEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getRealm_SuppressedConstraints()
  {
    return (EAttribute)realmEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getRealm_SourceLocators()
  {
    return (EReference)realmEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getRealm_Projects()
  {
    return (EReference)realmEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getRealm__Reconcile()
  {
    return realmEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getRealm__GetProject__Coordinate()
  {
    return realmEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getRealm__GetProjectIgnoreVersion__Coordinate()
  {
    return realmEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDOMElement()
  {
    return domElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDOMElement_Element()
  {
    return (EAttribute)domElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDOMElement_PropertyReferences()
  {
    return (EReference)domElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getDOMElement__GetElement__SegmentSequence()
  {
    return domElementEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getDOMElement__GetElementEdits()
  {
    return domElementEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProject()
  {
    return projectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProject_Location()
  {
    return (EAttribute)projectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_Realm()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_Parent()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_Dependencies()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_ManagedDependencies()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_Properties()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_IncomingParentReferences()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProject_IncomingDependencyReferences()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getProject__GetProperty__String()
  {
    return projectEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getProject__GetManagedDependency__Dependency()
  {
    return projectEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCoordinate()
  {
    return coordinateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCoordinate_GroupId()
  {
    return (EAttribute)coordinateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCoordinate_ArtifactId()
  {
    return (EAttribute)coordinateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCoordinate_Version()
  {
    return (EAttribute)coordinateEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCoordinate_ExpandedGroupId()
  {
    return (EAttribute)coordinateEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCoordinate_ExpandedVersion()
  {
    return (EAttribute)coordinateEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getParent()
  {
    return parentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParent_Project()
  {
    return (EReference)parentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParent_ResolvedProject()
  {
    return (EReference)parentEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDependency()
  {
    return dependencyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDependency_ResolvedProject()
  {
    return (EReference)dependencyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDependency_ResolvedManagedDependency()
  {
    return (EReference)dependencyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDependency_IncomingResolvedManagedDependencies()
  {
    return (EReference)dependencyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProperty()
  {
    return propertyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProperty_Key()
  {
    return (EAttribute)propertyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProperty_Value()
  {
    return (EAttribute)propertyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProperty_ExpandedValue()
  {
    return (EAttribute)propertyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProperty_IncomingResolvedPropertyReferences()
  {
    return (EReference)propertyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getPropertyReference()
  {
    return propertyReferenceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPropertyReference_Name()
  {
    return (EAttribute)propertyReferenceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPropertyReference_ResolvedProperty()
  {
    return (EReference)propertyReferenceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EEnum getConstraintType()
  {
    return constraintTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getDocument()
  {
    return documentEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getParent_RelativePath()
  {
    return (EAttribute)parentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getElement()
  {
    return elementEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getElementEdit()
  {
    return elementEditEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getTextRegion()
  {
    return textRegionEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getXPath()
  {
    return xPathEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MavenFactory getMavenFactory()
  {
    return (MavenFactory)getEFactoryInstance();
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
    realmEClass = createEClass(REALM);
    createEAttribute(realmEClass, REALM__SUPPRESSED_CONSTRAINTS);
    createEReference(realmEClass, REALM__SOURCE_LOCATORS);
    createEReference(realmEClass, REALM__PROJECTS);
    createEOperation(realmEClass, REALM___RECONCILE);
    createEOperation(realmEClass, REALM___GET_PROJECT__COORDINATE);
    createEOperation(realmEClass, REALM___GET_PROJECT_IGNORE_VERSION__COORDINATE);

    domElementEClass = createEClass(DOM_ELEMENT);
    createEAttribute(domElementEClass, DOM_ELEMENT__ELEMENT);
    createEReference(domElementEClass, DOM_ELEMENT__PROPERTY_REFERENCES);
    createEOperation(domElementEClass, DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE);
    createEOperation(domElementEClass, DOM_ELEMENT___GET_ELEMENT_EDITS);

    coordinateEClass = createEClass(COORDINATE);
    createEAttribute(coordinateEClass, COORDINATE__GROUP_ID);
    createEAttribute(coordinateEClass, COORDINATE__ARTIFACT_ID);
    createEAttribute(coordinateEClass, COORDINATE__VERSION);
    createEAttribute(coordinateEClass, COORDINATE__EXPANDED_GROUP_ID);
    createEAttribute(coordinateEClass, COORDINATE__EXPANDED_VERSION);

    projectEClass = createEClass(PROJECT);
    createEAttribute(projectEClass, PROJECT__LOCATION);
    createEReference(projectEClass, PROJECT__REALM);
    createEReference(projectEClass, PROJECT__PARENT);
    createEReference(projectEClass, PROJECT__DEPENDENCIES);
    createEReference(projectEClass, PROJECT__MANAGED_DEPENDENCIES);
    createEReference(projectEClass, PROJECT__PROPERTIES);
    createEReference(projectEClass, PROJECT__INCOMING_PARENT_REFERENCES);
    createEReference(projectEClass, PROJECT__INCOMING_DEPENDENCY_REFERENCES);
    createEOperation(projectEClass, PROJECT___GET_PROPERTY__STRING);
    createEOperation(projectEClass, PROJECT___GET_MANAGED_DEPENDENCY__DEPENDENCY);

    parentEClass = createEClass(PARENT);
    createEReference(parentEClass, PARENT__PROJECT);
    createEAttribute(parentEClass, PARENT__RELATIVE_PATH);
    createEReference(parentEClass, PARENT__RESOLVED_PROJECT);

    dependencyEClass = createEClass(DEPENDENCY);
    createEReference(dependencyEClass, DEPENDENCY__RESOLVED_PROJECT);
    createEReference(dependencyEClass, DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY);
    createEReference(dependencyEClass, DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES);

    propertyEClass = createEClass(PROPERTY);
    createEAttribute(propertyEClass, PROPERTY__KEY);
    createEAttribute(propertyEClass, PROPERTY__VALUE);
    createEAttribute(propertyEClass, PROPERTY__EXPANDED_VALUE);
    createEReference(propertyEClass, PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES);

    propertyReferenceEClass = createEClass(PROPERTY_REFERENCE);
    createEAttribute(propertyReferenceEClass, PROPERTY_REFERENCE__NAME);
    createEReference(propertyReferenceEClass, PROPERTY_REFERENCE__RESOLVED_PROPERTY);

    // Create enums
    constraintTypeEEnum = createEEnum(CONSTRAINT_TYPE);

    // Create data types
    documentEDataType = createEDataType(DOCUMENT);
    elementEDataType = createEDataType(ELEMENT);
    elementEditEDataType = createEDataType(ELEMENT_EDIT);
    textRegionEDataType = createEDataType(TEXT_REGION);
    xPathEDataType = createEDataType(XPATH);
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
    ResourcesPackage theResourcesPackage = (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    realmEClass.getESuperTypes().add(theBasePackage.getModelElement());
    domElementEClass.getESuperTypes().add(theBasePackage.getModelElement());
    coordinateEClass.getESuperTypes().add(getDOMElement());
    projectEClass.getESuperTypes().add(getCoordinate());
    parentEClass.getESuperTypes().add(getCoordinate());
    dependencyEClass.getESuperTypes().add(getCoordinate());
    propertyEClass.getESuperTypes().add(getDOMElement());
    propertyReferenceEClass.getESuperTypes().add(getDOMElement());

    // Initialize classes, features, and operations; add parameters
    initEClass(realmEClass, Realm.class, "Realm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getRealm_SuppressedConstraints(), getConstraintType(), "suppressedConstraints", null, 0, -1, Realm.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRealm_SourceLocators(), theResourcesPackage.getSourceLocator(), null, "sourceLocators", null, 0, -1, Realm.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRealm_Projects(), getProject(), getProject_Realm(), "projects", null, 0, -1, Realm.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getRealm__Reconcile(), null, "reconcile", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    EOperation op = initEOperation(getRealm__GetProject__Coordinate(), getProject(), "getProject", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getCoordinate(), "coordinate", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getRealm__GetProjectIgnoreVersion__Coordinate(), getProject(), "getProjectIgnoreVersion", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getCoordinate(), "coordinate", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(domElementEClass, DOMElement.class, "DOMElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getDOMElement_Element(), getElement(), "element", null, 1, 1, DOMElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDOMElement_PropertyReferences(), getPropertyReference(), null, "propertyReferences", null, 0, -1, DOMElement.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getDOMElement__GetElement__SegmentSequence(), getElement(), "getElement", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getXPath(), "xpath", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getDOMElement__GetElementEdits(), null, "getElementEdits", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    EGenericType g1 = createEGenericType(ecorePackage.getEMap());
    EGenericType g2 = createEGenericType(getDocument());
    g1.getETypeArguments().add(g2);
    g2 = createEGenericType(ecorePackage.getEMap());
    g1.getETypeArguments().add(g2);
    EGenericType g3 = createEGenericType(getTextRegion());
    g2.getETypeArguments().add(g3);
    g3 = createEGenericType(getElementEdit());
    g2.getETypeArguments().add(g3);
    initEOperation(op, g1);

    initEClass(coordinateEClass, Coordinate.class, "Coordinate", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getCoordinate_GroupId(), ecorePackage.getEString(), "groupId", null, 1, 1, Coordinate.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getCoordinate_ArtifactId(), ecorePackage.getEString(), "artifactId", null, 1, 1, Coordinate.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getCoordinate_Version(), ecorePackage.getEString(), "version", null, 0, 1, Coordinate.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getCoordinate_ExpandedGroupId(), ecorePackage.getEString(), "expandedGroupId", null, 1, 1, Coordinate.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getCoordinate_ExpandedVersion(), ecorePackage.getEString(), "expandedVersion", null, 0, 1, Coordinate.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(projectEClass, Project.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getProject_Location(), ecorePackage.getEString(), "location", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_Realm(), getRealm(), getRealm_Projects(), "realm", null, 0, 1, Project.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_Parent(), getParent(), getParent_Project(), "parent", null, 0, 1, Project.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_Dependencies(), getDependency(), null, "dependencies", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_ManagedDependencies(), getDependency(), null, "managedDependencies", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_Properties(), getProperty(), null, "properties", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_IncomingParentReferences(), getParent(), getParent_ResolvedProject(), "incomingParentReferences", null, 0, -1, Project.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_IncomingDependencyReferences(), getDependency(), getDependency_ResolvedProject(), "incomingDependencyReferences", null, 0, -1, //$NON-NLS-1$
        Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getProject__GetProperty__String(), getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getProject__GetManagedDependency__Dependency(), getDependency(), "getManagedDependency", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getDependency(), "dependency", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(parentEClass, Parent.class, "Parent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getParent_Project(), getProject(), getProject_Parent(), "project", null, 0, 1, Parent.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParent_RelativePath(), ecorePackage.getEString(), "relativePath", null, 0, 1, Parent.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getParent_ResolvedProject(), getProject(), getProject_IncomingParentReferences(), "resolvedProject", null, 1, 1, Parent.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dependencyEClass, Dependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getDependency_ResolvedProject(), getProject(), getProject_IncomingDependencyReferences(), "resolvedProject", null, 0, 1, Dependency.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDependency_ResolvedManagedDependency(), getDependency(), getDependency_IncomingResolvedManagedDependencies(), "resolvedManagedDependency", //$NON-NLS-1$
        null, 0, 1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEReference(getDependency_IncomingResolvedManagedDependencies(), getDependency(), getDependency_ResolvedManagedDependency(),
        "incomingResolvedManagedDependencies", null, 0, -1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, //$NON-NLS-1$
        !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertyEClass, Property.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getProperty_Key(), ecorePackage.getEString(), "key", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getProperty_Value(), ecorePackage.getEString(), "value", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getProperty_ExpandedValue(), ecorePackage.getEString(), "expandedValue", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getProperty_IncomingResolvedPropertyReferences(), getPropertyReference(), getPropertyReference_ResolvedProperty(),
        "incomingResolvedPropertyReferences", null, 0, -1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, //$NON-NLS-1$
        !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertyReferenceEClass, PropertyReference.class, "PropertyReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getPropertyReference_Name(), ecorePackage.getEString(), "name", null, 0, 1, PropertyReference.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPropertyReference_ResolvedProperty(), getProperty(), getProperty_IncomingResolvedPropertyReferences(), "resolvedProperty", null, 1, 1, //$NON-NLS-1$
        PropertyReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(constraintTypeEEnum, ConstraintType.class, "ConstraintType"); //$NON-NLS-1$
    addEEnumLiteral(constraintTypeEEnum, ConstraintType.VALID_RELATIVE_PARENT);
    addEEnumLiteral(constraintTypeEEnum, ConstraintType.RESOLVES_IN_REALM);

    // Initialize data types
    initEDataType(documentEDataType, Document.class, "Document", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(elementEDataType, Element.class, "Element", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(elementEditEDataType, MavenValidator.ElementEdit.class, "ElementEdit", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(textRegionEDataType, POMXMLUtil.TextRegion.class, "TextRegion", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(xPathEDataType, SegmentSequence.class, "XPath", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
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
    String source = "http://www.eclipse.org/oomph/base/LabelProvider"; //$NON-NLS-1$
    addAnnotation(this, source,
        new String[] { "imageBaseURI", "https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.maven.edit/icons/full/obj16" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(this, new boolean[] { true }, "http://www.eclipse.org/oomph/base/LabelProvider", //$NON-NLS-1$
        new String[] { "imageBaseURI", "https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.maven.edit/icons/full/obj16" //$NON-NLS-1$ //$NON-NLS-2$
        });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore"; //$NON-NLS-1$
    addAnnotation(parentEClass, source, new String[] { "constraints", "ResolvesInRealm ValidRelativePath" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(dependencyEClass, source, new String[] { "constraints", "ResolvesInRealm" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

} // MavenPackageImpl

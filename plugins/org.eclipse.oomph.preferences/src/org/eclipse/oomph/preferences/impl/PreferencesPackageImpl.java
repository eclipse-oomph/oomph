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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.preferences.PreferenceItem;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesValidator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferencesPackageImpl extends EPackageImpl implements PreferencesPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preferenceItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preferenceNodeEClass = null;

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
  private EDataType escapedStringEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType uriEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType preferenceNodeNameEDataType = null;

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
   * @see org.eclipse.oomph.preferences.PreferencesPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private PreferencesPackageImpl()
  {
    super(eNS_URI, PreferencesFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link PreferencesPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static PreferencesPackage init()
  {
    if (isInited)
    {
      return (PreferencesPackage)EPackage.Registry.INSTANCE.getEPackage(PreferencesPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredPreferencesPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    PreferencesPackageImpl thePreferencesPackage = registeredPreferencesPackage instanceof PreferencesPackageImpl
        ? (PreferencesPackageImpl)registeredPreferencesPackage
        : new PreferencesPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    thePreferencesPackage.createPackageContents();

    // Initialize created meta-data
    thePreferencesPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(thePreferencesPackage, new EValidator.Descriptor()
    {
      @Override
      public EValidator getEValidator()
      {
        return PreferencesValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    thePreferencesPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(PreferencesPackage.eNS_URI, thePreferencesPackage);
    return thePreferencesPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getPreferenceItem()
  {
    return preferenceItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceItem_Root()
  {
    return (EReference)preferenceItemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPreferenceItem_AbsolutePath()
  {
    return (EAttribute)preferenceItemEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPreferenceItem_Name()
  {
    return (EAttribute)preferenceItemEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPreferenceItem_RelativePath()
  {
    return (EAttribute)preferenceItemEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceItem_Ancestor()
  {
    return (EReference)preferenceItemEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceItem_Scope()
  {
    return (EReference)preferenceItemEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceItem__GetParent()
  {
    return preferenceItemEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getPreferenceNode()
  {
    return preferenceNodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceNode_Children()
  {
    return (EReference)preferenceNodeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceNode_Parent()
  {
    return (EReference)preferenceNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPreferenceNode_Properties()
  {
    return (EReference)preferenceNodeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPreferenceNode_Location()
  {
    return (EAttribute)preferenceNodeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceNode__GetNode__String()
  {
    return preferenceNodeEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceNode__GetNode__URI()
  {
    return preferenceNodeEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceNode__GetProperty__String()
  {
    return preferenceNodeEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceNode__GetProperty__URI()
  {
    return preferenceNodeEClass.getEOperations().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getPreferenceNode__GetAncestor()
  {
    return preferenceNodeEClass.getEOperations().get(4);
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
  public EReference getProperty_Parent()
  {
    return (EReference)propertyEClass.getEStructuralFeatures().get(0);
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
  public EAttribute getProperty_NonDefault()
  {
    return (EAttribute)propertyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProperty_Secure()
  {
    return (EAttribute)propertyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getProperty__GetAncestor()
  {
    return propertyEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getEscapedString()
  {
    return escapedStringEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getURI()
  {
    return uriEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getPreferenceNodeName()
  {
    return preferenceNodeNameEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PreferencesFactory getPreferencesFactory()
  {
    return (PreferencesFactory)getEFactoryInstance();
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
    preferenceItemEClass = createEClass(PREFERENCE_ITEM);
    createEReference(preferenceItemEClass, PREFERENCE_ITEM__ROOT);
    createEReference(preferenceItemEClass, PREFERENCE_ITEM__SCOPE);
    createEAttribute(preferenceItemEClass, PREFERENCE_ITEM__ABSOLUTE_PATH);
    createEAttribute(preferenceItemEClass, PREFERENCE_ITEM__NAME);
    createEAttribute(preferenceItemEClass, PREFERENCE_ITEM__RELATIVE_PATH);
    createEReference(preferenceItemEClass, PREFERENCE_ITEM__ANCESTOR);
    createEOperation(preferenceItemEClass, PREFERENCE_ITEM___GET_PARENT);

    preferenceNodeEClass = createEClass(PREFERENCE_NODE);
    createEReference(preferenceNodeEClass, PREFERENCE_NODE__PARENT);
    createEReference(preferenceNodeEClass, PREFERENCE_NODE__CHILDREN);
    createEReference(preferenceNodeEClass, PREFERENCE_NODE__PROPERTIES);
    createEAttribute(preferenceNodeEClass, PREFERENCE_NODE__LOCATION);
    createEOperation(preferenceNodeEClass, PREFERENCE_NODE___GET_NODE__STRING);
    createEOperation(preferenceNodeEClass, PREFERENCE_NODE___GET_NODE__URI);
    createEOperation(preferenceNodeEClass, PREFERENCE_NODE___GET_PROPERTY__STRING);
    createEOperation(preferenceNodeEClass, PREFERENCE_NODE___GET_PROPERTY__URI);
    createEOperation(preferenceNodeEClass, PREFERENCE_NODE___GET_ANCESTOR);

    propertyEClass = createEClass(PROPERTY);
    createEReference(propertyEClass, PROPERTY__PARENT);
    createEAttribute(propertyEClass, PROPERTY__VALUE);
    createEAttribute(propertyEClass, PROPERTY__NON_DEFAULT);
    createEAttribute(propertyEClass, PROPERTY__SECURE);
    createEOperation(propertyEClass, PROPERTY___GET_ANCESTOR);

    // Create data types
    escapedStringEDataType = createEDataType(ESCAPED_STRING);
    uriEDataType = createEDataType(URI);
    preferenceNodeNameEDataType = createEDataType(PREFERENCE_NODE_NAME);
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
    preferenceItemEClass.getESuperTypes().add(theBasePackage.getModelElement());
    preferenceNodeEClass.getESuperTypes().add(getPreferenceItem());
    propertyEClass.getESuperTypes().add(getPreferenceItem());

    // Initialize classes, features, and operations; add parameters
    initEClass(preferenceItemEClass, PreferenceItem.class, "PreferenceItem", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getPreferenceItem_Root(), getPreferenceNode(), null, "root", null, 0, 1, PreferenceItem.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceItem_Scope(), getPreferenceNode(), null, "scope", null, 0, 1, PreferenceItem.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getPreferenceItem_AbsolutePath(), getURI(), "absolutePath", null, 1, 1, PreferenceItem.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getPreferenceItem_Name(), ecorePackage.getEString(), "name", "", 1, 1, PreferenceItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPreferenceItem_RelativePath(), getURI(), "relativePath", null, 1, 1, PreferenceItem.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceItem_Ancestor(), getPreferenceItem(), null, "ancestor", null, 0, 1, PreferenceItem.class, IS_TRANSIENT, IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEOperation(getPreferenceItem__GetParent(), getPreferenceNode(), "getParent", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(preferenceNodeEClass, PreferenceNode.class, "PreferenceNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getPreferenceNode_Parent(), getPreferenceNode(), getPreferenceNode_Children(), "parent", null, 0, 1, PreferenceNode.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPreferenceNode_Children(), getPreferenceNode(), getPreferenceNode_Parent(), "children", null, 0, -1, PreferenceNode.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getPreferenceNode_Children().getEKeys().add(getPreferenceItem_Name());
    initEReference(getPreferenceNode_Properties(), getProperty(), getProperty_Parent(), "properties", null, 0, -1, PreferenceNode.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getPreferenceNode_Properties().getEKeys().add(getPreferenceItem_Name());
    initEAttribute(getPreferenceNode_Location(), ecorePackage.getEString(), "location", null, 0, 1, PreferenceNode.class, IS_TRANSIENT, IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getPreferenceNode__GetNode__String(), getPreferenceNode(), "getNode", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getPreferenceNode__GetNode__URI(), getPreferenceNode(), "getNode", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getURI(), "path", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getPreferenceNode__GetProperty__String(), getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    op = initEOperation(getPreferenceNode__GetProperty__URI(), getProperty(), "getProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getURI(), "path", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEOperation(getPreferenceNode__GetAncestor(), getPreferenceNode(), "getAncestor", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(propertyEClass, Property.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProperty_Parent(), getPreferenceNode(), getPreferenceNode_Properties(), "parent", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProperty_Value(), getEscapedString(), "value", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProperty_NonDefault(), ecorePackage.getEBoolean(), "nonDefault", null, 0, 1, Property.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getProperty_Secure(), ecorePackage.getEBoolean(), "secure", null, 0, 1, Property.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getProperty__GetAncestor(), getProperty(), "getAncestor", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    // Initialize data types
    initEDataType(escapedStringEDataType, String.class, "EscapedString", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(uriEDataType, org.eclipse.emf.common.util.URI.class, "URI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(preferenceNodeNameEDataType, String.class, "PreferenceNodeName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
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
    addAnnotation(this, source, new String[] { "imageBaseURI", //$NON-NLS-1$
        "https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.preferences.edit/icons/full/obj16" //$NON-NLS-1$
    });
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
    addAnnotation(getPreferenceNode_Children(), source, new String[] { "name", "child" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getPreferenceNode_Properties(), source, new String[] { "name", "property" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(preferenceNodeNameEDataType, source, new String[] { "pattern", "[^/]+" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

} // PreferencesPackageImpl

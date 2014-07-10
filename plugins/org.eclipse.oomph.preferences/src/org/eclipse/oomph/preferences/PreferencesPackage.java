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
package org.eclipse.oomph.preferences;

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
 * @see org.eclipse.oomph.preferences.PreferencesFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.preferences.edit/icons/full/obj16'"
 * @generated
 */
public interface PreferencesPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "preferences";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/Oomph/preferences/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "preferences";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  PreferencesPackage eINSTANCE = org.eclipse.oomph.preferences.impl.PreferencesPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl <em>Preference Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.preferences.impl.PreferenceItemImpl
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceItem()
   * @generated
   */
  int PREFERENCE_ITEM = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Root</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__ROOT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Scope</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__SCOPE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Absolute Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__ABSOLUTE_PATH = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__RELATIVE_PATH = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Ancestor</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM__ANCESTOR = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Preference Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Parent</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM___GET_PARENT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Preference Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_ITEM_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl <em>Preference Node</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.preferences.impl.PreferenceNodeImpl
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceNode()
   * @generated
   */
  int PREFERENCE_NODE = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__ANNOTATIONS = PREFERENCE_ITEM__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Root</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__ROOT = PREFERENCE_ITEM__ROOT;

  /**
   * The feature id for the '<em><b>Scope</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__SCOPE = PREFERENCE_ITEM__SCOPE;

  /**
   * The feature id for the '<em><b>Absolute Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__ABSOLUTE_PATH = PREFERENCE_ITEM__ABSOLUTE_PATH;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__NAME = PREFERENCE_ITEM__NAME;

  /**
   * The feature id for the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__RELATIVE_PATH = PREFERENCE_ITEM__RELATIVE_PATH;

  /**
   * The feature id for the '<em><b>Ancestor</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__ANCESTOR = PREFERENCE_ITEM__ANCESTOR;

  /**
   * The feature id for the '<em><b>Parent</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__PARENT = PREFERENCE_ITEM_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__CHILDREN = PREFERENCE_ITEM_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__PROPERTIES = PREFERENCE_ITEM_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE__LOCATION = PREFERENCE_ITEM_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Preference Node</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE_FEATURE_COUNT = PREFERENCE_ITEM_FEATURE_COUNT + 4;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_ANNOTATION__STRING = PREFERENCE_ITEM___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Parent</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_PARENT = PREFERENCE_ITEM___GET_PARENT;

  /**
   * The operation id for the '<em>Get Node</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_NODE__STRING = PREFERENCE_ITEM_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Get Node</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_NODE__URI = PREFERENCE_ITEM_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Get Property</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_PROPERTY__STRING = PREFERENCE_ITEM_OPERATION_COUNT + 2;

  /**
   * The operation id for the '<em>Get Property</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_PROPERTY__URI = PREFERENCE_ITEM_OPERATION_COUNT + 3;

  /**
   * The operation id for the '<em>Get Ancestor</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE___GET_ANCESTOR = PREFERENCE_ITEM_OPERATION_COUNT + 4;

  /**
   * The number of operations of the '<em>Preference Node</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_NODE_OPERATION_COUNT = PREFERENCE_ITEM_OPERATION_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.preferences.impl.PropertyImpl <em>Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.preferences.impl.PropertyImpl
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getProperty()
   * @generated
   */
  int PROPERTY = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ANNOTATIONS = PREFERENCE_ITEM__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Root</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ROOT = PREFERENCE_ITEM__ROOT;

  /**
   * The feature id for the '<em><b>Scope</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__SCOPE = PREFERENCE_ITEM__SCOPE;

  /**
   * The feature id for the '<em><b>Absolute Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ABSOLUTE_PATH = PREFERENCE_ITEM__ABSOLUTE_PATH;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__NAME = PREFERENCE_ITEM__NAME;

  /**
   * The feature id for the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__RELATIVE_PATH = PREFERENCE_ITEM__RELATIVE_PATH;

  /**
   * The feature id for the '<em><b>Ancestor</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ANCESTOR = PREFERENCE_ITEM__ANCESTOR;

  /**
   * The feature id for the '<em><b>Parent</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__PARENT = PREFERENCE_ITEM_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__VALUE = PREFERENCE_ITEM_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Non Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__NON_DEFAULT = PREFERENCE_ITEM_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Secure</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__SECURE = PREFERENCE_ITEM_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_FEATURE_COUNT = PREFERENCE_ITEM_FEATURE_COUNT + 4;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_ANNOTATION__STRING = PREFERENCE_ITEM___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Parent</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_PARENT = PREFERENCE_ITEM___GET_PARENT;

  /**
   * The operation id for the '<em>Get Ancestor</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_ANCESTOR = PREFERENCE_ITEM_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_OPERATION_COUNT = PREFERENCE_ITEM_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '<em>Escaped String</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getEscapedString()
   * @generated
   */
  int ESCAPED_STRING = 3;

  /**
   * The meta object id for the '<em>URI</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.URI
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getURI()
   * @generated
   */
  int URI = 4;

  /**
   * The meta object id for the '<em>Preference Node Name</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceNodeName()
   * @generated
   */
  int PREFERENCE_NODE_NAME = 5;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.preferences.PreferenceItem <em>Preference Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Preference Item</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem
   * @generated
   */
  EClass getPreferenceItem();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.preferences.PreferenceItem#getRoot <em>Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Root</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getRoot()
   * @see #getPreferenceItem()
   * @generated
   */
  EReference getPreferenceItem_Root();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.PreferenceItem#getAbsolutePath <em>Absolute Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Absolute Path</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getAbsolutePath()
   * @see #getPreferenceItem()
   * @generated
   */
  EAttribute getPreferenceItem_AbsolutePath();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.PreferenceItem#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getName()
   * @see #getPreferenceItem()
   * @generated
   */
  EAttribute getPreferenceItem_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.PreferenceItem#getRelativePath <em>Relative Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Relative Path</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getRelativePath()
   * @see #getPreferenceItem()
   * @generated
   */
  EAttribute getPreferenceItem_RelativePath();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.preferences.PreferenceItem#getAncestor <em>Ancestor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Ancestor</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getAncestor()
   * @see #getPreferenceItem()
   * @generated
   */
  EReference getPreferenceItem_Ancestor();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.preferences.PreferenceItem#getScope <em>Scope</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Scope</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getScope()
   * @see #getPreferenceItem()
   * @generated
   */
  EReference getPreferenceItem_Scope();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceItem#getParent() <em>Get Parent</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Parent</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceItem#getParent()
   * @generated
   */
  EOperation getPreferenceItem__GetParent();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.preferences.PreferenceNode <em>Preference Node</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Preference Node</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceNode
   * @generated
   */
  EClass getPreferenceNode();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.preferences.PreferenceNode#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Children</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getChildren()
   * @see #getPreferenceNode()
   * @generated
   */
  EReference getPreferenceNode_Children();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.preferences.PreferenceNode#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Parent</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getParent()
   * @see #getPreferenceNode()
   * @generated
   */
  EReference getPreferenceNode_Parent();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.preferences.PreferenceNode#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Properties</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getProperties()
   * @see #getPreferenceNode()
   * @generated
   */
  EReference getPreferenceNode_Properties();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.PreferenceNode#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getLocation()
   * @see #getPreferenceNode()
   * @generated
   */
  EAttribute getPreferenceNode_Location();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceNode#getNode(java.lang.String) <em>Get Node</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Node</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getNode(java.lang.String)
   * @generated
   */
  EOperation getPreferenceNode__GetNode__String();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceNode#getNode(org.eclipse.emf.common.util.URI) <em>Get Node</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Node</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getNode(org.eclipse.emf.common.util.URI)
   * @generated
   */
  EOperation getPreferenceNode__GetNode__URI();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceNode#getProperty(java.lang.String) <em>Get Property</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Property</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getProperty(java.lang.String)
   * @generated
   */
  EOperation getPreferenceNode__GetProperty__String();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceNode#getProperty(org.eclipse.emf.common.util.URI) <em>Get Property</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Property</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getProperty(org.eclipse.emf.common.util.URI)
   * @generated
   */
  EOperation getPreferenceNode__GetProperty__URI();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.PreferenceNode#getAncestor() <em>Get Ancestor</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Ancestor</em>' operation.
   * @see org.eclipse.oomph.preferences.PreferenceNode#getAncestor()
   * @generated
   */
  EOperation getPreferenceNode__GetAncestor();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.preferences.Property <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property</em>'.
   * @see org.eclipse.oomph.preferences.Property
   * @generated
   */
  EClass getProperty();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.preferences.Property#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Parent</em>'.
   * @see org.eclipse.oomph.preferences.Property#getParent()
   * @see #getProperty()
   * @generated
   */
  EReference getProperty_Parent();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.Property#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.preferences.Property#getValue()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Value();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.Property#isNonDefault <em>Non Default</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Non Default</em>'.
   * @see org.eclipse.oomph.preferences.Property#isNonDefault()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_NonDefault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.preferences.Property#isSecure <em>Secure</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Secure</em>'.
   * @see org.eclipse.oomph.preferences.Property#isSecure()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Secure();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.preferences.Property#getAncestor() <em>Get Ancestor</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Ancestor</em>' operation.
   * @see org.eclipse.oomph.preferences.Property#getAncestor()
   * @generated
   */
  EOperation getProperty__GetAncestor();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>Escaped String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Escaped String</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   * @generated
   */
  EDataType getEscapedString();

  /**
   * Returns the meta object for data type '{@link org.eclipse.emf.common.util.URI <em>URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>URI</em>'.
   * @see org.eclipse.emf.common.util.URI
   * @model instanceClass="org.eclipse.emf.common.util.URI"
   * @generated
   */
  EDataType getURI();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>Preference Node Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Preference Node Name</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   *        extendedMetaData="pattern='[^/]+'"
   * @generated
   */
  EDataType getPreferenceNodeName();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  PreferencesFactory getPreferencesFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl <em>Preference Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.preferences.impl.PreferenceItemImpl
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceItem()
     * @generated
     */
    EClass PREFERENCE_ITEM = eINSTANCE.getPreferenceItem();

    /**
     * The meta object literal for the '<em><b>Root</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_ITEM__ROOT = eINSTANCE.getPreferenceItem_Root();

    /**
     * The meta object literal for the '<em><b>Absolute Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_ITEM__ABSOLUTE_PATH = eINSTANCE.getPreferenceItem_AbsolutePath();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_ITEM__NAME = eINSTANCE.getPreferenceItem_Name();

    /**
     * The meta object literal for the '<em><b>Relative Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_ITEM__RELATIVE_PATH = eINSTANCE.getPreferenceItem_RelativePath();

    /**
     * The meta object literal for the '<em><b>Ancestor</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_ITEM__ANCESTOR = eINSTANCE.getPreferenceItem_Ancestor();

    /**
     * The meta object literal for the '<em><b>Scope</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_ITEM__SCOPE = eINSTANCE.getPreferenceItem_Scope();

    /**
     * The meta object literal for the '<em><b>Get Parent</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_ITEM___GET_PARENT = eINSTANCE.getPreferenceItem__GetParent();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl <em>Preference Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.preferences.impl.PreferenceNodeImpl
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceNode()
     * @generated
     */
    EClass PREFERENCE_NODE = eINSTANCE.getPreferenceNode();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_NODE__CHILDREN = eINSTANCE.getPreferenceNode_Children();

    /**
     * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_NODE__PARENT = eINSTANCE.getPreferenceNode_Parent();

    /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PREFERENCE_NODE__PROPERTIES = eINSTANCE.getPreferenceNode_Properties();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_NODE__LOCATION = eINSTANCE.getPreferenceNode_Location();

    /**
     * The meta object literal for the '<em><b>Get Node</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_NODE___GET_NODE__STRING = eINSTANCE.getPreferenceNode__GetNode__String();

    /**
     * The meta object literal for the '<em><b>Get Node</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_NODE___GET_NODE__URI = eINSTANCE.getPreferenceNode__GetNode__URI();

    /**
     * The meta object literal for the '<em><b>Get Property</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_NODE___GET_PROPERTY__STRING = eINSTANCE.getPreferenceNode__GetProperty__String();

    /**
     * The meta object literal for the '<em><b>Get Property</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_NODE___GET_PROPERTY__URI = eINSTANCE.getPreferenceNode__GetProperty__URI();

    /**
     * The meta object literal for the '<em><b>Get Ancestor</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREFERENCE_NODE___GET_ANCESTOR = eINSTANCE.getPreferenceNode__GetAncestor();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.preferences.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.preferences.impl.PropertyImpl
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getProperty()
     * @generated
     */
    EClass PROPERTY = eINSTANCE.getProperty();

    /**
     * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY__PARENT = eINSTANCE.getProperty_Parent();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

    /**
     * The meta object literal for the '<em><b>Non Default</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__NON_DEFAULT = eINSTANCE.getProperty_NonDefault();

    /**
     * The meta object literal for the '<em><b>Secure</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__SECURE = eINSTANCE.getProperty_Secure();

    /**
     * The meta object literal for the '<em><b>Get Ancestor</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROPERTY___GET_ANCESTOR = eINSTANCE.getProperty__GetAncestor();

    /**
     * The meta object literal for the '<em>Escaped String</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getEscapedString()
     * @generated
     */
    EDataType ESCAPED_STRING = eINSTANCE.getEscapedString();

    /**
     * The meta object literal for the '<em>URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.URI
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getURI()
     * @generated
     */
    EDataType URI = eINSTANCE.getURI();

    /**
     * The meta object literal for the '<em>Preference Node Name</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.oomph.preferences.impl.PreferencesPackageImpl#getPreferenceNodeName()
     * @generated
     */
    EDataType PREFERENCE_NODE_NAME = eINSTANCE.getPreferenceNodeName();

  }

} // PreferencesPackage

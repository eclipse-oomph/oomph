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
package org.eclipse.oomph.targlets;

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
 * @see org.eclipse.oomph.targlets.TargletFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Targlets.ecore'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.targlets.edit/icons/full/obj16'"
 * @generated
 */
public interface TargletPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "targlets";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/targlets/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "targlets";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TargletPackage eINSTANCE = org.eclipse.oomph.targlets.impl.TargletPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.TargletContainerImpl <em>Container</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.TargletContainerImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getTargletContainer()
   * @generated
   */
  int TARGLET_CONTAINER = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER__ID = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Targlets</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER__TARGLETS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Container</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Container</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_CONTAINER_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.TargletImpl <em>Targlet</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.TargletImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getTarglet()
   * @generated
   */
  int TARGLET = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__REQUIREMENTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Source Locators</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__SOURCE_LOCATORS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Installable Unit Generators</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INSTALLABLE_UNIT_GENERATORS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Repository Lists</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__REPOSITORY_LISTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Active Repository List Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ACTIVE_REPOSITORY_LIST_NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Active Repository List</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ACTIVE_REPOSITORY_LIST = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Active Repositories</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ACTIVE_REPOSITORIES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Include Sources</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INCLUDE_SOURCES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Include All Platforms</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INCLUDE_ALL_PLATFORMS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Include All Requirements</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INCLUDE_ALL_REQUIREMENTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 10;

  /**
   * The feature id for the '<em><b>Dropin Locations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__DROPIN_LOCATIONS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 11;

  /**
   * The number of structural features of the '<em>Targlet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 12;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Targlet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ComponentExtensionImpl <em>Component Extension</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ComponentExtensionImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentExtension()
   * @generated
   */
  int COMPONENT_EXTENSION = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXTENSION__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXTENSION__REQUIREMENTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Component Extension</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXTENSION_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXTENSION___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Component Extension</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXTENSION_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ComponentDefinitionImpl <em>Component Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ComponentDefinitionImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentDefinition()
   * @generated
   */
  int COMPONENT_DEFINITION = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION__ANNOTATIONS = COMPONENT_EXTENSION__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION__REQUIREMENTS = COMPONENT_EXTENSION__REQUIREMENTS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION__ID = COMPONENT_EXTENSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION__VERSION = COMPONENT_EXTENSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Component Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION_FEATURE_COUNT = COMPONENT_EXTENSION_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION___GET_ANNOTATION__STRING = COMPONENT_EXTENSION___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Component Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEFINITION_OPERATION_COUNT = COMPONENT_EXTENSION_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.IUGenerator <em>IU Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.IUGenerator
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getIUGenerator()
   * @generated
   */
  int IU_GENERATOR = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>IU Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>IU Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.FeatureGeneratorImpl <em>Feature Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.FeatureGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getFeatureGenerator()
   * @generated
   */
  int FEATURE_GENERATOR = 5;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Feature Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Feature Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.PluginGeneratorImpl <em>Plugin Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.PluginGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getPluginGenerator()
   * @generated
   */
  int PLUGIN_GENERATOR = 6;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Plugin Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Plugin Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ComponentDefGeneratorImpl <em>Component Def Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ComponentDefGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentDefGenerator()
   * @generated
   */
  int COMPONENT_DEF_GENERATOR = 7;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEF_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Component Def Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEF_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEF_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEF_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Component Def Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_DEF_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ComponentExtGeneratorImpl <em>Component Ext Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ComponentExtGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentExtGenerator()
   * @generated
   */
  int COMPONENT_EXT_GENERATOR = 8;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXT_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Component Ext Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXT_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXT_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Component Ext Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_EXT_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.CSpecGeneratorImpl <em>CSpec Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.CSpecGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCSpecGenerator()
   * @generated
   */
  int CSPEC_GENERATOR = 9;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEC_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>CSpec Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEC_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEC_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEC_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>CSpec Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEC_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.CSpexGeneratorImpl <em>CSpex Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.CSpexGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCSpexGenerator()
   * @generated
   */
  int CSPEX_GENERATOR = 10;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEX_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>CSpex Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEX_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEX_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEX_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>CSpex Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CSPEX_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.SiteGeneratorImpl <em>Site Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.SiteGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getSiteGenerator()
   * @generated
   */
  int SITE_GENERATOR = 11;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITE_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Site Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITE_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITE_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITE_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Site Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITE_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.CategoryGeneratorImpl <em>Category Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.CategoryGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCategoryGenerator()
   * @generated
   */
  int CATEGORY_GENERATOR = 12;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATEGORY_GENERATOR__ANNOTATIONS = SITE_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Category Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATEGORY_GENERATOR_FEATURE_COUNT = SITE_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATEGORY_GENERATOR___GET_ANNOTATION__STRING = SITE_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATEGORY_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = SITE_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Category Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATEGORY_GENERATOR_OPERATION_COUNT = SITE_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ProductGeneratorImpl <em>Product Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ProductGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getProductGenerator()
   * @generated
   */
  int PRODUCT_GENERATOR = 13;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Product Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Product Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ProjectNameGeneratorImpl <em>Project Name Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ProjectNameGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getProjectNameGenerator()
   * @generated
   */
  int PROJECT_NAME_GENERATOR = 14;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_NAME_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Project Name Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_NAME_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_NAME_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IUs</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_NAME_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST;

  /**
   * The number of operations of the '<em>Project Name Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_NAME_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.DropinLocationImpl <em>Dropin Location</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.DropinLocationImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getDropinLocation()
   * @generated
   */
  int DROPIN_LOCATION = 15;

  /**
   * The feature id for the '<em><b>Root Folder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DROPIN_LOCATION__ROOT_FOLDER = 0;

  /**
   * The feature id for the '<em><b>Recursive</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DROPIN_LOCATION__RECURSIVE = 1;

  /**
   * The number of structural features of the '<em>Dropin Location</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DROPIN_LOCATION_FEATURE_COUNT = 2;

  /**
   * The number of operations of the '<em>Dropin Location</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DROPIN_LOCATION_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '<em>Installable Unit</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.equinox.p2.metadata.IInstallableUnit
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getInstallableUnit()
   * @generated
   */
  int INSTALLABLE_UNIT = 16;

  /**
   * The meta object id for the '<em>String To Version Map</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.Map
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getStringToVersionMap()
   * @generated
   */
  int STRING_TO_VERSION_MAP = 17;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.TargletContainer <em>Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Container</em>'.
   * @see org.eclipse.oomph.targlets.TargletContainer
   * @generated
   */
  EClass getTargletContainer();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.TargletContainer#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.targlets.TargletContainer#getID()
   * @see #getTargletContainer()
   * @generated
   */
  EAttribute getTargletContainer_ID();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.TargletContainer#getTarglets <em>Targlets</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Targlets</em>'.
   * @see org.eclipse.oomph.targlets.TargletContainer#getTarglets()
   * @see #getTargletContainer()
   * @generated
   */
  EReference getTargletContainer_Targlets();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.Targlet <em>Targlet</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Targlet</em>'.
   * @see org.eclipse.oomph.targlets.Targlet
   * @generated
   */
  EClass getTarglet();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getName()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.Targlet#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getRequirements()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_Requirements();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.Targlet#getSourceLocators <em>Source Locators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Source Locators</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getSourceLocators()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_SourceLocators();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.Targlet#getInstallableUnitGenerators <em>Installable Unit Generators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Installable Unit Generators</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getInstallableUnitGenerators()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_InstallableUnitGenerators();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.Targlet#getDropinLocations <em>Dropin Locations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Dropin Locations</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getDropinLocations()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_DropinLocations();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.Targlet#getRepositoryLists <em>Repository Lists</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Repository Lists</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getRepositoryLists()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_RepositoryLists();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryListName <em>Active Repository List Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Active Repository List Name</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getActiveRepositoryListName()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_ActiveRepositoryListName();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryList <em>Active Repository List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Active Repository List</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getActiveRepositoryList()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_ActiveRepositoryList();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositories <em>Active Repositories</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Active Repositories</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getActiveRepositories()
   * @see #getTarglet()
   * @generated
   */
  EReference getTarglet_ActiveRepositories();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#isIncludeSources <em>Include Sources</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Include Sources</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#isIncludeSources()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_IncludeSources();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllPlatforms <em>Include All Platforms</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Include All Platforms</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#isIncludeAllPlatforms()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_IncludeAllPlatforms();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllRequirements <em>Include All Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Include All Requirements</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#isIncludeAllRequirements()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_IncludeAllRequirements();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ComponentExtension <em>Component Extension</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Component Extension</em>'.
   * @see org.eclipse.oomph.targlets.ComponentExtension
   * @generated
   */
  EClass getComponentExtension();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.targlets.ComponentExtension#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see org.eclipse.oomph.targlets.ComponentExtension#getRequirements()
   * @see #getComponentExtension()
   * @generated
   */
  EReference getComponentExtension_Requirements();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ComponentDefinition <em>Component Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Component Definition</em>'.
   * @see org.eclipse.oomph.targlets.ComponentDefinition
   * @generated
   */
  EClass getComponentDefinition();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.ComponentDefinition#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.targlets.ComponentDefinition#getID()
   * @see #getComponentDefinition()
   * @generated
   */
  EAttribute getComponentDefinition_ID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.ComponentDefinition#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see org.eclipse.oomph.targlets.ComponentDefinition#getVersion()
   * @see #getComponentDefinition()
   * @generated
   */
  EAttribute getComponentDefinition_Version();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.IUGenerator <em>IU Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IU Generator</em>'.
   * @see org.eclipse.oomph.targlets.IUGenerator
   * @generated
   */
  EClass getIUGenerator();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.targlets.IUGenerator#generateIUs(org.eclipse.core.resources.IProject, java.lang.String, java.util.Map, org.eclipse.emf.common.util.EList) <em>Generate IUs</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Generate IUs</em>' operation.
   * @see org.eclipse.oomph.targlets.IUGenerator#generateIUs(org.eclipse.core.resources.IProject, java.lang.String, java.util.Map, org.eclipse.emf.common.util.EList)
   * @generated
   */
  EOperation getIUGenerator__GenerateIUs__IProject_String_Map_EList();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.FeatureGenerator <em>Feature Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Feature Generator</em>'.
   * @see org.eclipse.oomph.targlets.FeatureGenerator
   * @generated
   */
  EClass getFeatureGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.PluginGenerator <em>Plugin Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Plugin Generator</em>'.
   * @see org.eclipse.oomph.targlets.PluginGenerator
   * @generated
   */
  EClass getPluginGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ComponentDefGenerator <em>Component Def Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Component Def Generator</em>'.
   * @see org.eclipse.oomph.targlets.ComponentDefGenerator
   * @generated
   */
  EClass getComponentDefGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ComponentExtGenerator <em>Component Ext Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Component Ext Generator</em>'.
   * @see org.eclipse.oomph.targlets.ComponentExtGenerator
   * @generated
   */
  EClass getComponentExtGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.CSpecGenerator <em>CSpec Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>CSpec Generator</em>'.
   * @see org.eclipse.oomph.targlets.CSpecGenerator
   * @generated
   */
  EClass getCSpecGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.CSpexGenerator <em>CSpex Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>CSpex Generator</em>'.
   * @see org.eclipse.oomph.targlets.CSpexGenerator
   * @generated
   */
  EClass getCSpexGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.SiteGenerator <em>Site Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Site Generator</em>'.
   * @see org.eclipse.oomph.targlets.SiteGenerator
   * @generated
   */
  EClass getSiteGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.CategoryGenerator <em>Category Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Category Generator</em>'.
   * @see org.eclipse.oomph.targlets.CategoryGenerator
   * @generated
   */
  EClass getCategoryGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ProductGenerator <em>Product Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Product Generator</em>'.
   * @see org.eclipse.oomph.targlets.ProductGenerator
   * @generated
   */
  EClass getProductGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ProjectNameGenerator <em>Project Name Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project Name Generator</em>'.
   * @see org.eclipse.oomph.targlets.ProjectNameGenerator
   * @generated
   */
  EClass getProjectNameGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.DropinLocation <em>Dropin Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Dropin Location</em>'.
   * @see org.eclipse.oomph.targlets.DropinLocation
   * @generated
   */
  EClass getDropinLocation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.DropinLocation#getRootFolder <em>Root Folder</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Root Folder</em>'.
   * @see org.eclipse.oomph.targlets.DropinLocation#getRootFolder()
   * @see #getDropinLocation()
   * @generated
   */
  EAttribute getDropinLocation_RootFolder();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.DropinLocation#isRecursive <em>Recursive</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Recursive</em>'.
   * @see org.eclipse.oomph.targlets.DropinLocation#isRecursive()
   * @see #getDropinLocation()
   * @generated
   */
  EAttribute getDropinLocation_Recursive();

  /**
   * Returns the meta object for data type '{@link org.eclipse.equinox.p2.metadata.IInstallableUnit <em>Installable Unit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Installable Unit</em>'.
   * @see org.eclipse.equinox.p2.metadata.IInstallableUnit
   * @model instanceClass="org.eclipse.equinox.p2.metadata.IInstallableUnit" serializeable="false"
   * @generated
   */
  EDataType getInstallableUnit();

  /**
   * Returns the meta object for data type '{@link java.util.Map <em>String To Version Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>String To Version Map</em>'.
   * @see java.util.Map
   * @model instanceClass="java.util.Map<java.lang.String, org.eclipse.equinox.p2.metadata.Version>" serializeable="false"
   * @generated
   */
  EDataType getStringToVersionMap();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  TargletFactory getTargletFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.TargletContainerImpl <em>Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.TargletContainerImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getTargletContainer()
     * @generated
     */
    EClass TARGLET_CONTAINER = eINSTANCE.getTargletContainer();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_CONTAINER__ID = eINSTANCE.getTargletContainer_ID();

    /**
     * The meta object literal for the '<em><b>Targlets</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET_CONTAINER__TARGLETS = eINSTANCE.getTargletContainer_Targlets();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.TargletImpl <em>Targlet</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.TargletImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getTarglet()
     * @generated
     */
    EClass TARGLET = eINSTANCE.getTarglet();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__NAME = eINSTANCE.getTarglet_Name();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__REQUIREMENTS = eINSTANCE.getTarglet_Requirements();

    /**
     * The meta object literal for the '<em><b>Source Locators</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__SOURCE_LOCATORS = eINSTANCE.getTarglet_SourceLocators();

    /**
     * The meta object literal for the '<em><b>Installable Unit Generators</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__INSTALLABLE_UNIT_GENERATORS = eINSTANCE.getTarglet_InstallableUnitGenerators();

    /**
     * The meta object literal for the '<em><b>Dropin Locations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__DROPIN_LOCATIONS = eINSTANCE.getTarglet_DropinLocations();

    /**
     * The meta object literal for the '<em><b>Repository Lists</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__REPOSITORY_LISTS = eINSTANCE.getTarglet_RepositoryLists();

    /**
     * The meta object literal for the '<em><b>Active Repository List Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__ACTIVE_REPOSITORY_LIST_NAME = eINSTANCE.getTarglet_ActiveRepositoryListName();

    /**
     * The meta object literal for the '<em><b>Active Repository List</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__ACTIVE_REPOSITORY_LIST = eINSTANCE.getTarglet_ActiveRepositoryList();

    /**
     * The meta object literal for the '<em><b>Active Repositories</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__ACTIVE_REPOSITORIES = eINSTANCE.getTarglet_ActiveRepositories();

    /**
     * The meta object literal for the '<em><b>Include Sources</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__INCLUDE_SOURCES = eINSTANCE.getTarglet_IncludeSources();

    /**
     * The meta object literal for the '<em><b>Include All Platforms</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__INCLUDE_ALL_PLATFORMS = eINSTANCE.getTarglet_IncludeAllPlatforms();

    /**
     * The meta object literal for the '<em><b>Include All Requirements</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__INCLUDE_ALL_REQUIREMENTS = eINSTANCE.getTarglet_IncludeAllRequirements();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ComponentExtensionImpl <em>Component Extension</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ComponentExtensionImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentExtension()
     * @generated
     */
    EClass COMPONENT_EXTENSION = eINSTANCE.getComponentExtension();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMPONENT_EXTENSION__REQUIREMENTS = eINSTANCE.getComponentExtension_Requirements();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ComponentDefinitionImpl <em>Component Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ComponentDefinitionImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentDefinition()
     * @generated
     */
    EClass COMPONENT_DEFINITION = eINSTANCE.getComponentDefinition();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMPONENT_DEFINITION__ID = eINSTANCE.getComponentDefinition_ID();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMPONENT_DEFINITION__VERSION = eINSTANCE.getComponentDefinition_Version();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.IUGenerator <em>IU Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.IUGenerator
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getIUGenerator()
     * @generated
     */
    EClass IU_GENERATOR = eINSTANCE.getIUGenerator();

    /**
     * The meta object literal for the '<em><b>Generate IUs</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation IU_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST = eINSTANCE.getIUGenerator__GenerateIUs__IProject_String_Map_EList();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.FeatureGeneratorImpl <em>Feature Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.FeatureGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getFeatureGenerator()
     * @generated
     */
    EClass FEATURE_GENERATOR = eINSTANCE.getFeatureGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.PluginGeneratorImpl <em>Plugin Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.PluginGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getPluginGenerator()
     * @generated
     */
    EClass PLUGIN_GENERATOR = eINSTANCE.getPluginGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ComponentDefGeneratorImpl <em>Component Def Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ComponentDefGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentDefGenerator()
     * @generated
     */
    EClass COMPONENT_DEF_GENERATOR = eINSTANCE.getComponentDefGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ComponentExtGeneratorImpl <em>Component Ext Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ComponentExtGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentExtGenerator()
     * @generated
     */
    EClass COMPONENT_EXT_GENERATOR = eINSTANCE.getComponentExtGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.CSpecGeneratorImpl <em>CSpec Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.CSpecGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCSpecGenerator()
     * @generated
     */
    EClass CSPEC_GENERATOR = eINSTANCE.getCSpecGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.CSpexGeneratorImpl <em>CSpex Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.CSpexGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCSpexGenerator()
     * @generated
     */
    EClass CSPEX_GENERATOR = eINSTANCE.getCSpexGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.SiteGeneratorImpl <em>Site Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.SiteGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getSiteGenerator()
     * @generated
     */
    EClass SITE_GENERATOR = eINSTANCE.getSiteGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.CategoryGeneratorImpl <em>Category Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.CategoryGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getCategoryGenerator()
     * @generated
     */
    EClass CATEGORY_GENERATOR = eINSTANCE.getCategoryGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ProductGeneratorImpl <em>Product Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ProductGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getProductGenerator()
     * @generated
     */
    EClass PRODUCT_GENERATOR = eINSTANCE.getProductGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ProjectNameGeneratorImpl <em>Project Name Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ProjectNameGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getProjectNameGenerator()
     * @generated
     */
    EClass PROJECT_NAME_GENERATOR = eINSTANCE.getProjectNameGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.DropinLocationImpl <em>Dropin Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.DropinLocationImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getDropinLocation()
     * @generated
     */
    EClass DROPIN_LOCATION = eINSTANCE.getDropinLocation();

    /**
     * The meta object literal for the '<em><b>Root Folder</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DROPIN_LOCATION__ROOT_FOLDER = eINSTANCE.getDropinLocation_RootFolder();

    /**
     * The meta object literal for the '<em><b>Recursive</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DROPIN_LOCATION__RECURSIVE = eINSTANCE.getDropinLocation_Recursive();

    /**
     * The meta object literal for the '<em>Installable Unit</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.equinox.p2.metadata.IInstallableUnit
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getInstallableUnit()
     * @generated
     */
    EDataType INSTALLABLE_UNIT = eINSTANCE.getInstallableUnit();

    /**
     * The meta object literal for the '<em>String To Version Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Map
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getStringToVersionMap()
     * @generated
     */
    EDataType STRING_TO_VERSION_MAP = eINSTANCE.getStringToVersionMap();

  }

} // TargletPackage

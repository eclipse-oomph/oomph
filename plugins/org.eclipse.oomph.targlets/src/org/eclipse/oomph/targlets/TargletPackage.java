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
   * The feature id for the '<em><b>Active Repository List</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ACTIVE_REPOSITORY_LIST = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Active Repositories</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__ACTIVE_REPOSITORIES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Include Sources</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INCLUDE_SOURCES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Include All Platforms</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET__INCLUDE_ALL_PLATFORMS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The number of structural features of the '<em>Targlet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 9;

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
   * The operation id for the '<em>Generate IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Modify IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>IU Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IU_GENERATOR_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

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
   * The operation id for the '<em>Generate IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP;

  /**
   * The operation id for the '<em>Modify IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP;

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
   * The operation id for the '<em>Generate IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP;

  /**
   * The operation id for the '<em>Modify IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP;

  /**
   * The number of operations of the '<em>Plugin Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLUGIN_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.ComponentGeneratorImpl <em>Component Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.ComponentGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentGenerator()
   * @generated
   */
  int COMPONENT_GENERATOR = 7;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Component Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP;

  /**
   * The operation id for the '<em>Modify IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP;

  /**
   * The number of operations of the '<em>Component Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPONENT_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.targlets.impl.BuckminsterGeneratorImpl <em>Buckminster Generator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.targlets.impl.BuckminsterGeneratorImpl
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getBuckminsterGenerator()
   * @generated
   */
  int BUCKMINSTER_GENERATOR = 8;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR__ANNOTATIONS = IU_GENERATOR__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Save As Component</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT = IU_GENERATOR_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Buckminster Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR_FEATURE_COUNT = IU_GENERATOR_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR___GET_ANNOTATION__STRING = IU_GENERATOR___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Generate IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP;

  /**
   * The operation id for the '<em>Modify IU</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP;

  /**
   * The number of operations of the '<em>Buckminster Generator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUCKMINSTER_GENERATOR_OPERATION_COUNT = IU_GENERATOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Installable Unit</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.equinox.p2.metadata.IInstallableUnit
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getInstallableUnit()
   * @generated
   */
  int INSTALLABLE_UNIT = 9;

  /**
   * The meta object id for the '<em>String To Version Map</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.Map
   * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getStringToVersionMap()
   * @generated
   */
  int STRING_TO_VERSION_MAP = 10;

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
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryList <em>Active Repository List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Active Repository List</em>'.
   * @see org.eclipse.oomph.targlets.Targlet#getActiveRepositoryList()
   * @see #getTarglet()
   * @generated
   */
  EAttribute getTarglet_ActiveRepositoryList();

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
   * Returns the meta object for the '{@link org.eclipse.oomph.targlets.IUGenerator#generateIU(org.eclipse.core.resources.IProject, java.lang.String, java.util.Map) <em>Generate IU</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Generate IU</em>' operation.
   * @see org.eclipse.oomph.targlets.IUGenerator#generateIU(org.eclipse.core.resources.IProject, java.lang.String, java.util.Map)
   * @generated
   */
  EOperation getIUGenerator__GenerateIU__IProject_String_Map();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.targlets.IUGenerator#modifyIU(org.eclipse.equinox.p2.metadata.IInstallableUnit, org.eclipse.core.resources.IProject, java.lang.String, java.util.Map) <em>Modify IU</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Modify IU</em>' operation.
   * @see org.eclipse.oomph.targlets.IUGenerator#modifyIU(org.eclipse.equinox.p2.metadata.IInstallableUnit, org.eclipse.core.resources.IProject, java.lang.String, java.util.Map)
   * @generated
   */
  EOperation getIUGenerator__ModifyIU__IInstallableUnit_IProject_String_Map();

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
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.ComponentGenerator <em>Component Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Component Generator</em>'.
   * @see org.eclipse.oomph.targlets.ComponentGenerator
   * @generated
   */
  EClass getComponentGenerator();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.targlets.BuckminsterGenerator <em>Buckminster Generator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Buckminster Generator</em>'.
   * @see org.eclipse.oomph.targlets.BuckminsterGenerator
   * @generated
   */
  EClass getBuckminsterGenerator();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.targlets.BuckminsterGenerator#isSaveAsComponent <em>Save As Component</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Save As Component</em>'.
   * @see org.eclipse.oomph.targlets.BuckminsterGenerator#isSaveAsComponent()
   * @see #getBuckminsterGenerator()
   * @generated
   */
  EAttribute getBuckminsterGenerator_SaveAsComponent();

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
     * The meta object literal for the '<em><b>Repository Lists</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET__REPOSITORY_LISTS = eINSTANCE.getTarglet_RepositoryLists();

    /**
     * The meta object literal for the '<em><b>Active Repository List</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET__ACTIVE_REPOSITORY_LIST = eINSTANCE.getTarglet_ActiveRepositoryList();

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
     * The meta object literal for the '<em><b>Generate IU</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation IU_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP = eINSTANCE.getIUGenerator__GenerateIU__IProject_String_Map();

    /**
     * The meta object literal for the '<em><b>Modify IU</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation IU_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP = eINSTANCE.getIUGenerator__ModifyIU__IInstallableUnit_IProject_String_Map();

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
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.ComponentGeneratorImpl <em>Component Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.ComponentGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getComponentGenerator()
     * @generated
     */
    EClass COMPONENT_GENERATOR = eINSTANCE.getComponentGenerator();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.targlets.impl.BuckminsterGeneratorImpl <em>Buckminster Generator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.targlets.impl.BuckminsterGeneratorImpl
     * @see org.eclipse.oomph.targlets.impl.TargletPackageImpl#getBuckminsterGenerator()
     * @generated
     */
    EClass BUCKMINSTER_GENERATOR = eINSTANCE.getBuckminsterGenerator();

    /**
     * The meta object literal for the '<em><b>Save As Component</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT = eINSTANCE.getBuckminsterGenerator_SaveAsComponent();

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

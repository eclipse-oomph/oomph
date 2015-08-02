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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.oomph.p2.P2Factory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.p2.edit/icons/full/obj16'"
 * @generated
 */
public interface P2Package extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "p2";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/p2/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "p2";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  P2Package eINSTANCE = org.eclipse.oomph.p2.impl.P2PackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl <em>Profile Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.impl.ProfileDefinitionImpl
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getProfileDefinition()
   * @generated
   */
  int PROFILE_DEFINITION = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION__REQUIREMENTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Repositories</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION__REPOSITORIES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Include Source Bundles</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Profile Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Set Requirements</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION___SET_REQUIREMENTS__ELIST = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Set Repositories</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION___SET_REPOSITORIES__ELIST = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>Profile Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROFILE_DEFINITION_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.impl.ConfigurationImpl <em>Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.impl.ConfigurationImpl
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getConfiguration()
   * @generated
   */
  int CONFIGURATION = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>WS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION__WS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>OS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION__OS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Arch</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION__ARCH = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.impl.RequirementImpl <em>Requirement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.impl.RequirementImpl
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRequirement()
   * @generated
   */
  int REQUIREMENT = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__ID = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__NAMESPACE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Version Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__VERSION_RANGE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Optional</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__OPTIONAL = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__FILTER = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT__TYPE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Requirement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Set Version Range</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT___SET_VERSION_RANGE__VERSION_VERSIONSEGMENT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Requirement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.impl.RepositoryListImpl <em>Repository List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.impl.RepositoryListImpl
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepositoryList()
   * @generated
   */
  int REPOSITORY_LIST = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.impl.RepositoryImpl <em>Repository</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.impl.RepositoryImpl
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepository()
   * @generated
   */
  int REPOSITORY = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Repositories</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST__REPOSITORIES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Repository List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Repository List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_LIST_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY__URL = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY__TYPE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Repository</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Repository</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.RepositoryType <em>Repository Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.RepositoryType
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepositoryType()
   * @generated
   */
  int REPOSITORY_TYPE = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.VersionSegment <em>Version Segment</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.VersionSegment
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersionSegment()
   * @generated
   */
  int VERSION_SEGMENT = 6;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.p2.RequirementType <em>Requirement Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.p2.RequirementType
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRequirementType()
   * @generated
   */
  int REQUIREMENT_TYPE = 7;

  /**
   * The meta object id for the '<em>Version</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.equinox.p2.metadata.Version
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersion()
   * @generated
   */
  int VERSION = 8;

  /**
   * The meta object id for the '<em>Version Range</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.equinox.p2.metadata.VersionRange
   * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersionRange()
   * @generated
   */
  int VERSION_RANGE = 9;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.p2.ProfileDefinition <em>Profile Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Profile Definition</em>'.
   * @see org.eclipse.oomph.p2.ProfileDefinition
   * @generated
   */
  EClass getProfileDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.p2.ProfileDefinition#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see org.eclipse.oomph.p2.ProfileDefinition#getRequirements()
   * @see #getProfileDefinition()
   * @generated
   */
  EReference getProfileDefinition_Requirements();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.p2.ProfileDefinition#getRepositories <em>Repositories</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Repositories</em>'.
   * @see org.eclipse.oomph.p2.ProfileDefinition#getRepositories()
   * @see #getProfileDefinition()
   * @generated
   */
  EReference getProfileDefinition_Repositories();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.ProfileDefinition#isIncludeSourceBundles <em>Include Source Bundles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Include Source Bundles</em>'.
   * @see org.eclipse.oomph.p2.ProfileDefinition#isIncludeSourceBundles()
   * @see #getProfileDefinition()
   * @generated
   */
  EAttribute getProfileDefinition_IncludeSourceBundles();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.p2.ProfileDefinition#setRequirements(org.eclipse.emf.common.util.EList) <em>Set Requirements</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Set Requirements</em>' operation.
   * @see org.eclipse.oomph.p2.ProfileDefinition#setRequirements(org.eclipse.emf.common.util.EList)
   * @generated
   */
  EOperation getProfileDefinition__SetRequirements__EList();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.p2.ProfileDefinition#setRepositories(org.eclipse.emf.common.util.EList) <em>Set Repositories</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Set Repositories</em>' operation.
   * @see org.eclipse.oomph.p2.ProfileDefinition#setRepositories(org.eclipse.emf.common.util.EList)
   * @generated
   */
  EOperation getProfileDefinition__SetRepositories__EList();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.p2.Configuration <em>Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Configuration</em>'.
   * @see org.eclipse.oomph.p2.Configuration
   * @generated
   */
  EClass getConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Configuration#getWS <em>WS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>WS</em>'.
   * @see org.eclipse.oomph.p2.Configuration#getWS()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_WS();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Configuration#getOS <em>OS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>OS</em>'.
   * @see org.eclipse.oomph.p2.Configuration#getOS()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_OS();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Configuration#getArch <em>Arch</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Arch</em>'.
   * @see org.eclipse.oomph.p2.Configuration#getArch()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_Arch();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.p2.Requirement <em>Requirement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Requirement</em>'.
   * @see org.eclipse.oomph.p2.Requirement
   * @generated
   */
  EClass getRequirement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.p2.Requirement
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_ID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.p2.Requirement#getName()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#getNamespace <em>Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace</em>'.
   * @see org.eclipse.oomph.p2.Requirement#getNamespace()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_Namespace();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#getVersionRange <em>Version Range</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version Range</em>'.
   * @see org.eclipse.oomph.p2.Requirement#getVersionRange()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_VersionRange();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#isOptional <em>Optional</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Optional</em>'.
   * @see org.eclipse.oomph.p2.Requirement#isOptional()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_Optional();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#getFilter <em>Filter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Filter</em>'.
   * @see org.eclipse.oomph.p2.Requirement#getFilter()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_Filter();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Requirement#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.p2.Requirement#getType()
   * @see #getRequirement()
   * @generated
   */
  EAttribute getRequirement_Type();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.p2.Requirement#setVersionRange(org.eclipse.equinox.p2.metadata.Version, org.eclipse.oomph.p2.VersionSegment) <em>Set Version Range</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Set Version Range</em>' operation.
   * @see org.eclipse.oomph.p2.Requirement#setVersionRange(org.eclipse.equinox.p2.metadata.Version, org.eclipse.oomph.p2.VersionSegment)
   * @generated
   */
  EOperation getRequirement__SetVersionRange__Version_VersionSegment();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.p2.RepositoryList <em>Repository List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Repository List</em>'.
   * @see org.eclipse.oomph.p2.RepositoryList
   * @generated
   */
  EClass getRepositoryList();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.p2.RepositoryList#getRepositories <em>Repositories</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Repositories</em>'.
   * @see org.eclipse.oomph.p2.RepositoryList#getRepositories()
   * @see #getRepositoryList()
   * @generated
   */
  EReference getRepositoryList_Repositories();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.RepositoryList#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.p2.RepositoryList#getName()
   * @see #getRepositoryList()
   * @generated
   */
  EAttribute getRepositoryList_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.p2.Repository <em>Repository</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Repository</em>'.
   * @see org.eclipse.oomph.p2.Repository
   * @generated
   */
  EClass getRepository();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Repository#getURL <em>URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URL</em>'.
   * @see org.eclipse.oomph.p2.Repository#getURL()
   * @see #getRepository()
   * @generated
   */
  EAttribute getRepository_URL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.p2.Repository#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.p2.Repository#getType()
   * @see #getRepository()
   * @generated
   */
  EAttribute getRepository_Type();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.p2.RepositoryType <em>Repository Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Repository Type</em>'.
   * @see org.eclipse.oomph.p2.RepositoryType
   * @generated
   */
  EEnum getRepositoryType();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.p2.VersionSegment <em>Version Segment</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Version Segment</em>'.
   * @see org.eclipse.oomph.p2.VersionSegment
   * @generated
   */
  EEnum getVersionSegment();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.p2.RequirementType <em>Requirement Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Requirement Type</em>'.
   * @see org.eclipse.oomph.p2.RequirementType
   * @generated
   */
  EEnum getRequirementType();

  /**
   * Returns the meta object for data type '{@link org.eclipse.equinox.p2.metadata.Version <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Version</em>'.
   * @see org.eclipse.equinox.p2.metadata.Version
   * @model instanceClass="org.eclipse.equinox.p2.metadata.Version"
   * @generated
   */
  EDataType getVersion();

  /**
   * Returns the meta object for data type '{@link org.eclipse.equinox.p2.metadata.VersionRange <em>Version Range</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Version Range</em>'.
   * @see org.eclipse.equinox.p2.metadata.VersionRange
   * @model instanceClass="org.eclipse.equinox.p2.metadata.VersionRange"
   * @generated
   */
  EDataType getVersionRange();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  P2Factory getP2Factory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl <em>Profile Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.impl.ProfileDefinitionImpl
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getProfileDefinition()
     * @generated
     */
    EClass PROFILE_DEFINITION = eINSTANCE.getProfileDefinition();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROFILE_DEFINITION__REQUIREMENTS = eINSTANCE.getProfileDefinition_Requirements();

    /**
     * The meta object literal for the '<em><b>Repositories</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROFILE_DEFINITION__REPOSITORIES = eINSTANCE.getProfileDefinition_Repositories();

    /**
     * The meta object literal for the '<em><b>Include Source Bundles</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES = eINSTANCE.getProfileDefinition_IncludeSourceBundles();

    /**
     * The meta object literal for the '<em><b>Set Requirements</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROFILE_DEFINITION___SET_REQUIREMENTS__ELIST = eINSTANCE.getProfileDefinition__SetRequirements__EList();

    /**
     * The meta object literal for the '<em><b>Set Repositories</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROFILE_DEFINITION___SET_REPOSITORIES__ELIST = eINSTANCE.getProfileDefinition__SetRepositories__EList();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.impl.ConfigurationImpl <em>Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.impl.ConfigurationImpl
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getConfiguration()
     * @generated
     */
    EClass CONFIGURATION = eINSTANCE.getConfiguration();

    /**
     * The meta object literal for the '<em><b>WS</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIGURATION__WS = eINSTANCE.getConfiguration_WS();

    /**
     * The meta object literal for the '<em><b>OS</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIGURATION__OS = eINSTANCE.getConfiguration_OS();

    /**
     * The meta object literal for the '<em><b>Arch</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIGURATION__ARCH = eINSTANCE.getConfiguration_Arch();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.impl.RequirementImpl <em>Requirement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.impl.RequirementImpl
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRequirement()
     * @generated
     */
    EClass REQUIREMENT = eINSTANCE.getRequirement();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__ID = eINSTANCE.getRequirement_ID();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__NAME = eINSTANCE.getRequirement_Name();

    /**
     * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__NAMESPACE = eINSTANCE.getRequirement_Namespace();

    /**
     * The meta object literal for the '<em><b>Version Range</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__VERSION_RANGE = eINSTANCE.getRequirement_VersionRange();

    /**
     * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__OPTIONAL = eINSTANCE.getRequirement_Optional();

    /**
     * The meta object literal for the '<em><b>Filter</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__FILTER = eINSTANCE.getRequirement_Filter();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REQUIREMENT__TYPE = eINSTANCE.getRequirement_Type();

    /**
     * The meta object literal for the '<em><b>Set Version Range</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation REQUIREMENT___SET_VERSION_RANGE__VERSION_VERSIONSEGMENT = eINSTANCE.getRequirement__SetVersionRange__Version_VersionSegment();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.impl.RepositoryListImpl <em>Repository List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.impl.RepositoryListImpl
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepositoryList()
     * @generated
     */
    EClass REPOSITORY_LIST = eINSTANCE.getRepositoryList();

    /**
     * The meta object literal for the '<em><b>Repositories</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REPOSITORY_LIST__REPOSITORIES = eINSTANCE.getRepositoryList_Repositories();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REPOSITORY_LIST__NAME = eINSTANCE.getRepositoryList_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.impl.RepositoryImpl <em>Repository</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.impl.RepositoryImpl
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepository()
     * @generated
     */
    EClass REPOSITORY = eINSTANCE.getRepository();

    /**
     * The meta object literal for the '<em><b>URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REPOSITORY__URL = eINSTANCE.getRepository_URL();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REPOSITORY__TYPE = eINSTANCE.getRepository_Type();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.RepositoryType <em>Repository Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.RepositoryType
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRepositoryType()
     * @generated
     */
    EEnum REPOSITORY_TYPE = eINSTANCE.getRepositoryType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.VersionSegment <em>Version Segment</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.VersionSegment
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersionSegment()
     * @generated
     */
    EEnum VERSION_SEGMENT = eINSTANCE.getVersionSegment();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.p2.RequirementType <em>Requirement Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.p2.RequirementType
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getRequirementType()
     * @generated
     */
    EEnum REQUIREMENT_TYPE = eINSTANCE.getRequirementType();

    /**
     * The meta object literal for the '<em>Version</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.equinox.p2.metadata.Version
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersion()
     * @generated
     */
    EDataType VERSION = eINSTANCE.getVersion();

    /**
     * The meta object literal for the '<em>Version Range</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.equinox.p2.metadata.VersionRange
     * @see org.eclipse.oomph.p2.impl.P2PackageImpl#getVersionRange()
     * @generated
     */
    EDataType VERSION_RANGE = eINSTANCE.getVersionRange();

  }

} // P2Package

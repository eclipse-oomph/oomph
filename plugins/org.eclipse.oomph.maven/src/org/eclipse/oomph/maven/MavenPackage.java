/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

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
 * @see org.eclipse.oomph.maven.MavenFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.maven.edit/icons/full/obj16'"
 * @generated
 */
public interface MavenPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "maven"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "https://www.eclipse.org/Oomph/maven/1.0"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "maven"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MavenPackage eINSTANCE = org.eclipse.oomph.maven.impl.MavenPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.RealmImpl <em>Realm</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.RealmImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getRealm()
   * @generated
   */
  int REALM = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Source Locators</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM__SOURCE_LOCATORS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Projects</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM__PROJECTS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Realm</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Reconcile</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM___RECONCILE = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Get Project</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM___GET_PROJECT__COORDINATE = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Get Project Ignore Version</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM___GET_PROJECT_IGNORE_VERSION__COORDINATE = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>Realm</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REALM_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.DOMElementImpl <em>DOM Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.DOMElementImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDOMElement()
   * @generated
   */
  int DOM_ELEMENT = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT__ELEMENT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT__PROPERTY_REFERENCES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>DOM Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT___GET_ELEMENT_EDITS = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>DOM Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOM_ELEMENT_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.CoordinateImpl <em>Coordinate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.CoordinateImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getCoordinate()
   * @generated
   */
  int COORDINATE = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__ANNOTATIONS = DOM_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__ELEMENT = DOM_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__PROPERTY_REFERENCES = DOM_ELEMENT__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__GROUP_ID = DOM_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Artifact Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__ARTIFACT_ID = DOM_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__VERSION = DOM_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Expanded Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__EXPANDED_GROUP_ID = DOM_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Expanded Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE__EXPANDED_VERSION = DOM_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Coordinate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE_FEATURE_COUNT = DOM_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE___GET_ANNOTATION__STRING = DOM_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE___GET_ELEMENT__SEGMENTSEQUENCE = DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE___GET_ELEMENT_EDITS = DOM_ELEMENT___GET_ELEMENT_EDITS;

  /**
   * The number of operations of the '<em>Coordinate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COORDINATE_OPERATION_COUNT = DOM_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.ProjectImpl <em>Project</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.ProjectImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getProject()
   * @generated
   */
  int PROJECT = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__ANNOTATIONS = COORDINATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__ELEMENT = COORDINATE__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PROPERTY_REFERENCES = COORDINATE__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__GROUP_ID = COORDINATE__GROUP_ID;

  /**
   * The feature id for the '<em><b>Artifact Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__ARTIFACT_ID = COORDINATE__ARTIFACT_ID;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__VERSION = COORDINATE__VERSION;

  /**
   * The feature id for the '<em><b>Expanded Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__EXPANDED_GROUP_ID = COORDINATE__EXPANDED_GROUP_ID;

  /**
   * The feature id for the '<em><b>Expanded Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__EXPANDED_VERSION = COORDINATE__EXPANDED_VERSION;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__LOCATION = COORDINATE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Realm</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__REALM = COORDINATE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Parent</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PARENT = COORDINATE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__DEPENDENCIES = COORDINATE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Managed Dependencies</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__MANAGED_DEPENDENCIES = COORDINATE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PROPERTIES = COORDINATE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Incoming Parent References</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__INCOMING_PARENT_REFERENCES = COORDINATE_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Incoming Dependency References</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__INCOMING_DEPENDENCY_REFERENCES = COORDINATE_FEATURE_COUNT + 7;

  /**
   * The number of structural features of the '<em>Project</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FEATURE_COUNT = COORDINATE_FEATURE_COUNT + 8;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT___GET_ANNOTATION__STRING = COORDINATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT___GET_ELEMENT__SEGMENTSEQUENCE = COORDINATE___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT___GET_ELEMENT_EDITS = COORDINATE___GET_ELEMENT_EDITS;

  /**
   * The operation id for the '<em>Get Property</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT___GET_PROPERTY__STRING = COORDINATE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Get Managed Dependency</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT___GET_MANAGED_DEPENDENCY__DEPENDENCY = COORDINATE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>Project</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_OPERATION_COUNT = COORDINATE_OPERATION_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.ParentImpl <em>Parent</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.ParentImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getParent()
   * @generated
   */
  int PARENT = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__ANNOTATIONS = COORDINATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__ELEMENT = COORDINATE__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__PROPERTY_REFERENCES = COORDINATE__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__GROUP_ID = COORDINATE__GROUP_ID;

  /**
   * The feature id for the '<em><b>Artifact Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__ARTIFACT_ID = COORDINATE__ARTIFACT_ID;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__VERSION = COORDINATE__VERSION;

  /**
   * The feature id for the '<em><b>Expanded Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__EXPANDED_GROUP_ID = COORDINATE__EXPANDED_GROUP_ID;

  /**
   * The feature id for the '<em><b>Expanded Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__EXPANDED_VERSION = COORDINATE__EXPANDED_VERSION;

  /**
   * The feature id for the '<em><b>Project</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__PROJECT = COORDINATE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__RELATIVE_PATH = COORDINATE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Resolved Project</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT__RESOLVED_PROJECT = COORDINATE_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Parent</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT_FEATURE_COUNT = COORDINATE_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT___GET_ANNOTATION__STRING = COORDINATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT___GET_ELEMENT__SEGMENTSEQUENCE = COORDINATE___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT___GET_ELEMENT_EDITS = COORDINATE___GET_ELEMENT_EDITS;

  /**
   * The number of operations of the '<em>Parent</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARENT_OPERATION_COUNT = COORDINATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.DependencyImpl <em>Dependency</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.DependencyImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDependency()
   * @generated
   */
  int DEPENDENCY = 5;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__ANNOTATIONS = COORDINATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__ELEMENT = COORDINATE__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__PROPERTY_REFERENCES = COORDINATE__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__GROUP_ID = COORDINATE__GROUP_ID;

  /**
   * The feature id for the '<em><b>Artifact Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__ARTIFACT_ID = COORDINATE__ARTIFACT_ID;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__VERSION = COORDINATE__VERSION;

  /**
   * The feature id for the '<em><b>Expanded Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__EXPANDED_GROUP_ID = COORDINATE__EXPANDED_GROUP_ID;

  /**
   * The feature id for the '<em><b>Expanded Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__EXPANDED_VERSION = COORDINATE__EXPANDED_VERSION;

  /**
   * The feature id for the '<em><b>Resolved Project</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__RESOLVED_PROJECT = COORDINATE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Resolved Managed Dependency</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY = COORDINATE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Incoming Resolved Managed Dependencies</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES = COORDINATE_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Dependency</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY_FEATURE_COUNT = COORDINATE_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY___GET_ANNOTATION__STRING = COORDINATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY___GET_ELEMENT__SEGMENTSEQUENCE = COORDINATE___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY___GET_ELEMENT_EDITS = COORDINATE___GET_ELEMENT_EDITS;

  /**
   * The number of operations of the '<em>Dependency</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEPENDENCY_OPERATION_COUNT = COORDINATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.PropertyImpl <em>Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.PropertyImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getProperty()
   * @generated
   */
  int PROPERTY = 6;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ANNOTATIONS = DOM_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ELEMENT = DOM_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__PROPERTY_REFERENCES = DOM_ELEMENT__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__KEY = DOM_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__VALUE = DOM_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Expanded Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__EXPANDED_VALUE = DOM_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Incoming Resolved Property References</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES = DOM_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_FEATURE_COUNT = DOM_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_ANNOTATION__STRING = DOM_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_ELEMENT__SEGMENTSEQUENCE = DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY___GET_ELEMENT_EDITS = DOM_ELEMENT___GET_ELEMENT_EDITS;

  /**
   * The number of operations of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_OPERATION_COUNT = DOM_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.maven.impl.PropertyReferenceImpl <em>Property Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.impl.PropertyReferenceImpl
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getPropertyReference()
   * @generated
   */
  int PROPERTY_REFERENCE = 7;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE__ANNOTATIONS = DOM_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE__ELEMENT = DOM_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Property References</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE__PROPERTY_REFERENCES = DOM_ELEMENT__PROPERTY_REFERENCES;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE__NAME = DOM_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Resolved Property</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE__RESOLVED_PROPERTY = DOM_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Property Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE_FEATURE_COUNT = DOM_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE___GET_ANNOTATION__STRING = DOM_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Element</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE___GET_ELEMENT__SEGMENTSEQUENCE = DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE;

  /**
   * The operation id for the '<em>Get Element Edits</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE___GET_ELEMENT_EDITS = DOM_ELEMENT___GET_ELEMENT_EDITS;

  /**
   * The number of operations of the '<em>Property Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_REFERENCE_OPERATION_COUNT = DOM_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Document</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.w3c.dom.Document
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDocument()
   * @generated
   */
  int DOCUMENT = 8;

  /**
   * The meta object id for the '<em>Element</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.w3c.dom.Element
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getElement()
   * @generated
   */
  int ELEMENT = 9;

  /**
   * The meta object id for the '<em>Element Edit</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.util.MavenValidator.ElementEdit
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getElementEdit()
   * @generated
   */
  int ELEMENT_EDIT = 10;

  /**
   * The meta object id for the '<em>Text Region</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getTextRegion()
   * @generated
   */
  int TEXT_REGION = 11;

  /**
   * The meta object id for the '<em>XPath</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.SegmentSequence
   * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getXPath()
   * @generated
   */
  int XPATH = 12;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Realm <em>Realm</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Realm</em>'.
   * @see org.eclipse.oomph.maven.Realm
   * @generated
   */
  EClass getRealm();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.Realm#getSourceLocators <em>Source Locators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Source Locators</em>'.
   * @see org.eclipse.oomph.maven.Realm#getSourceLocators()
   * @see #getRealm()
   * @generated
   */
  EReference getRealm_SourceLocators();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.Realm#getProjects <em>Projects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Projects</em>'.
   * @see org.eclipse.oomph.maven.Realm#getProjects()
   * @see #getRealm()
   * @generated
   */
  EReference getRealm_Projects();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.Realm#reconcile() <em>Reconcile</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Reconcile</em>' operation.
   * @see org.eclipse.oomph.maven.Realm#reconcile()
   * @generated
   */
  EOperation getRealm__Reconcile();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.Realm#getProject(org.eclipse.oomph.maven.Coordinate) <em>Get Project</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Project</em>' operation.
   * @see org.eclipse.oomph.maven.Realm#getProject(org.eclipse.oomph.maven.Coordinate)
   * @generated
   */
  EOperation getRealm__GetProject__Coordinate();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.Realm#getProjectIgnoreVersion(org.eclipse.oomph.maven.Coordinate) <em>Get Project Ignore Version</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Project Ignore Version</em>' operation.
   * @see org.eclipse.oomph.maven.Realm#getProjectIgnoreVersion(org.eclipse.oomph.maven.Coordinate)
   * @generated
   */
  EOperation getRealm__GetProjectIgnoreVersion__Coordinate();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.DOMElement <em>DOM Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>DOM Element</em>'.
   * @see org.eclipse.oomph.maven.DOMElement
   * @generated
   */
  EClass getDOMElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.DOMElement#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element</em>'.
   * @see org.eclipse.oomph.maven.DOMElement#getElement()
   * @see #getDOMElement()
   * @generated
   */
  EAttribute getDOMElement_Element();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.DOMElement#getPropertyReferences <em>Property References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Property References</em>'.
   * @see org.eclipse.oomph.maven.DOMElement#getPropertyReferences()
   * @see #getDOMElement()
   * @generated
   */
  EReference getDOMElement_PropertyReferences();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.DOMElement#getElement(org.eclipse.emf.common.util.SegmentSequence) <em>Get Element</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Element</em>' operation.
   * @see org.eclipse.oomph.maven.DOMElement#getElement(org.eclipse.emf.common.util.SegmentSequence)
   * @generated
   */
  EOperation getDOMElement__GetElement__SegmentSequence();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.DOMElement#getElementEdits() <em>Get Element Edits</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Element Edits</em>' operation.
   * @see org.eclipse.oomph.maven.DOMElement#getElementEdits()
   * @generated
   */
  EOperation getDOMElement__GetElementEdits();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Project <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project</em>'.
   * @see org.eclipse.oomph.maven.Project
   * @generated
   */
  EClass getProject();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Project#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.maven.Project#getLocation()
   * @see #getProject()
   * @generated
   */
  EAttribute getProject_Location();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.maven.Project#getRealm <em>Realm</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Realm</em>'.
   * @see org.eclipse.oomph.maven.Project#getRealm()
   * @see #getProject()
   * @generated
   */
  EReference getProject_Realm();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.maven.Project#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Parent</em>'.
   * @see org.eclipse.oomph.maven.Project#getParent()
   * @see #getProject()
   * @generated
   */
  EReference getProject_Parent();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.Project#getDependencies <em>Dependencies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Dependencies</em>'.
   * @see org.eclipse.oomph.maven.Project#getDependencies()
   * @see #getProject()
   * @generated
   */
  EReference getProject_Dependencies();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.Project#getManagedDependencies <em>Managed Dependencies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Managed Dependencies</em>'.
   * @see org.eclipse.oomph.maven.Project#getManagedDependencies()
   * @see #getProject()
   * @generated
   */
  EReference getProject_ManagedDependencies();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.maven.Project#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Properties</em>'.
   * @see org.eclipse.oomph.maven.Project#getProperties()
   * @see #getProject()
   * @generated
   */
  EReference getProject_Properties();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.maven.Project#getIncomingParentReferences <em>Incoming Parent References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Incoming Parent References</em>'.
   * @see org.eclipse.oomph.maven.Project#getIncomingParentReferences()
   * @see #getProject()
   * @generated
   */
  EReference getProject_IncomingParentReferences();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.maven.Project#getIncomingDependencyReferences <em>Incoming Dependency References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Incoming Dependency References</em>'.
   * @see org.eclipse.oomph.maven.Project#getIncomingDependencyReferences()
   * @see #getProject()
   * @generated
   */
  EReference getProject_IncomingDependencyReferences();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.Project#getProperty(java.lang.String) <em>Get Property</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Property</em>' operation.
   * @see org.eclipse.oomph.maven.Project#getProperty(java.lang.String)
   * @generated
   */
  EOperation getProject__GetProperty__String();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.maven.Project#getManagedDependency(org.eclipse.oomph.maven.Dependency) <em>Get Managed Dependency</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Managed Dependency</em>' operation.
   * @see org.eclipse.oomph.maven.Project#getManagedDependency(org.eclipse.oomph.maven.Dependency)
   * @generated
   */
  EOperation getProject__GetManagedDependency__Dependency();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Coordinate <em>Coordinate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Coordinate</em>'.
   * @see org.eclipse.oomph.maven.Coordinate
   * @generated
   */
  EClass getCoordinate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Coordinate#getGroupId <em>Group Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Group Id</em>'.
   * @see org.eclipse.oomph.maven.Coordinate#getGroupId()
   * @see #getCoordinate()
   * @generated
   */
  EAttribute getCoordinate_GroupId();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Coordinate#getArtifactId <em>Artifact Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Artifact Id</em>'.
   * @see org.eclipse.oomph.maven.Coordinate#getArtifactId()
   * @see #getCoordinate()
   * @generated
   */
  EAttribute getCoordinate_ArtifactId();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Coordinate#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see org.eclipse.oomph.maven.Coordinate#getVersion()
   * @see #getCoordinate()
   * @generated
   */
  EAttribute getCoordinate_Version();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Coordinate#getExpandedGroupId <em>Expanded Group Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Expanded Group Id</em>'.
   * @see org.eclipse.oomph.maven.Coordinate#getExpandedGroupId()
   * @see #getCoordinate()
   * @generated
   */
  EAttribute getCoordinate_ExpandedGroupId();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Coordinate#getExpandedVersion <em>Expanded Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Expanded Version</em>'.
   * @see org.eclipse.oomph.maven.Coordinate#getExpandedVersion()
   * @see #getCoordinate()
   * @generated
   */
  EAttribute getCoordinate_ExpandedVersion();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Parent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parent</em>'.
   * @see org.eclipse.oomph.maven.Parent
   * @generated
   */
  EClass getParent();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.maven.Parent#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Project</em>'.
   * @see org.eclipse.oomph.maven.Parent#getProject()
   * @see #getParent()
   * @generated
   */
  EReference getParent_Project();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.maven.Parent#getResolvedProject <em>Resolved Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Resolved Project</em>'.
   * @see org.eclipse.oomph.maven.Parent#getResolvedProject()
   * @see #getParent()
   * @generated
   */
  EReference getParent_ResolvedProject();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Dependency <em>Dependency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Dependency</em>'.
   * @see org.eclipse.oomph.maven.Dependency
   * @generated
   */
  EClass getDependency();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.maven.Dependency#getResolvedProject <em>Resolved Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Resolved Project</em>'.
   * @see org.eclipse.oomph.maven.Dependency#getResolvedProject()
   * @see #getDependency()
   * @generated
   */
  EReference getDependency_ResolvedProject();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency <em>Resolved Managed Dependency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Resolved Managed Dependency</em>'.
   * @see org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency()
   * @see #getDependency()
   * @generated
   */
  EReference getDependency_ResolvedManagedDependency();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.maven.Dependency#getIncomingResolvedManagedDependencies <em>Incoming Resolved Managed Dependencies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Incoming Resolved Managed Dependencies</em>'.
   * @see org.eclipse.oomph.maven.Dependency#getIncomingResolvedManagedDependencies()
   * @see #getDependency()
   * @generated
   */
  EReference getDependency_IncomingResolvedManagedDependencies();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.Property <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property</em>'.
   * @see org.eclipse.oomph.maven.Property
   * @generated
   */
  EClass getProperty();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Property#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see org.eclipse.oomph.maven.Property#getKey()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Key();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Property#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.maven.Property#getValue()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Value();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Property#getExpandedValue <em>Expanded Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Expanded Value</em>'.
   * @see org.eclipse.oomph.maven.Property#getExpandedValue()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_ExpandedValue();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.maven.Property#getIncomingResolvedPropertyReferences <em>Incoming Resolved Property References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Incoming Resolved Property References</em>'.
   * @see org.eclipse.oomph.maven.Property#getIncomingResolvedPropertyReferences()
   * @see #getProperty()
   * @generated
   */
  EReference getProperty_IncomingResolvedPropertyReferences();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.maven.PropertyReference <em>Property Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property Reference</em>'.
   * @see org.eclipse.oomph.maven.PropertyReference
   * @generated
   */
  EClass getPropertyReference();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.PropertyReference#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.maven.PropertyReference#getName()
   * @see #getPropertyReference()
   * @generated
   */
  EAttribute getPropertyReference_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.maven.PropertyReference#getResolvedProperty <em>Resolved Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Resolved Property</em>'.
   * @see org.eclipse.oomph.maven.PropertyReference#getResolvedProperty()
   * @see #getPropertyReference()
   * @generated
   */
  EReference getPropertyReference_ResolvedProperty();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Document <em>Document</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Document</em>'.
   * @see org.w3c.dom.Document
   * @model instanceClass="org.w3c.dom.Document"
   * @generated
   */
  EDataType getDocument();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.maven.Parent#getRelativePath <em>Relative Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Relative Path</em>'.
   * @see org.eclipse.oomph.maven.Parent#getRelativePath()
   * @see #getParent()
   * @generated
   */
  EAttribute getParent_RelativePath();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Element <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Element</em>'.
   * @see org.w3c.dom.Element
   * @model instanceClass="org.w3c.dom.Element"
   * @generated
   */
  EDataType getElement();

  /**
   * Returns the meta object for data type '{@link org.eclipse.oomph.maven.util.MavenValidator.ElementEdit <em>Element Edit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Element Edit</em>'.
   * @see org.eclipse.oomph.maven.util.MavenValidator.ElementEdit
   * @model instanceClass="org.eclipse.oomph.maven.util.MavenValidator$ElementEdit"
   * @generated
   */
  EDataType getElementEdit();

  /**
   * Returns the meta object for data type '{@link org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion <em>Text Region</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Text Region</em>'.
   * @see org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion
   * @model instanceClass="org.eclipse.oomph.maven.util.POMXMLUtil$TextRegion"
   * @generated
   */
  EDataType getTextRegion();

  /**
   * Returns the meta object for data type '{@link org.eclipse.emf.common.util.SegmentSequence <em>XPath</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>XPath</em>'.
   * @see org.eclipse.emf.common.util.SegmentSequence
   * @model instanceClass="org.eclipse.emf.common.util.SegmentSequence"
   * @generated
   */
  EDataType getXPath();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MavenFactory getMavenFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.RealmImpl <em>Realm</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.RealmImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getRealm()
     * @generated
     */
    EClass REALM = eINSTANCE.getRealm();

    /**
     * The meta object literal for the '<em><b>Source Locators</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REALM__SOURCE_LOCATORS = eINSTANCE.getRealm_SourceLocators();

    /**
     * The meta object literal for the '<em><b>Projects</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REALM__PROJECTS = eINSTANCE.getRealm_Projects();

    /**
     * The meta object literal for the '<em><b>Reconcile</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation REALM___RECONCILE = eINSTANCE.getRealm__Reconcile();

    /**
     * The meta object literal for the '<em><b>Get Project</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation REALM___GET_PROJECT__COORDINATE = eINSTANCE.getRealm__GetProject__Coordinate();

    /**
     * The meta object literal for the '<em><b>Get Project Ignore Version</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation REALM___GET_PROJECT_IGNORE_VERSION__COORDINATE = eINSTANCE.getRealm__GetProjectIgnoreVersion__Coordinate();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.DOMElementImpl <em>DOM Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.DOMElementImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDOMElement()
     * @generated
     */
    EClass DOM_ELEMENT = eINSTANCE.getDOMElement();

    /**
     * The meta object literal for the '<em><b>Element</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOM_ELEMENT__ELEMENT = eINSTANCE.getDOMElement_Element();

    /**
     * The meta object literal for the '<em><b>Property References</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOM_ELEMENT__PROPERTY_REFERENCES = eINSTANCE.getDOMElement_PropertyReferences();

    /**
     * The meta object literal for the '<em><b>Get Element</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE = eINSTANCE.getDOMElement__GetElement__SegmentSequence();

    /**
     * The meta object literal for the '<em><b>Get Element Edits</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation DOM_ELEMENT___GET_ELEMENT_EDITS = eINSTANCE.getDOMElement__GetElementEdits();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.ProjectImpl <em>Project</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.ProjectImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getProject()
     * @generated
     */
    EClass PROJECT = eINSTANCE.getProject();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECT__LOCATION = eINSTANCE.getProject_Location();

    /**
     * The meta object literal for the '<em><b>Realm</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__REALM = eINSTANCE.getProject_Realm();

    /**
     * The meta object literal for the '<em><b>Parent</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__PARENT = eINSTANCE.getProject_Parent();

    /**
     * The meta object literal for the '<em><b>Dependencies</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__DEPENDENCIES = eINSTANCE.getProject_Dependencies();

    /**
     * The meta object literal for the '<em><b>Managed Dependencies</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__MANAGED_DEPENDENCIES = eINSTANCE.getProject_ManagedDependencies();

    /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__PROPERTIES = eINSTANCE.getProject_Properties();

    /**
     * The meta object literal for the '<em><b>Incoming Parent References</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__INCOMING_PARENT_REFERENCES = eINSTANCE.getProject_IncomingParentReferences();

    /**
     * The meta object literal for the '<em><b>Incoming Dependency References</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__INCOMING_DEPENDENCY_REFERENCES = eINSTANCE.getProject_IncomingDependencyReferences();

    /**
     * The meta object literal for the '<em><b>Get Property</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROJECT___GET_PROPERTY__STRING = eINSTANCE.getProject__GetProperty__String();

    /**
     * The meta object literal for the '<em><b>Get Managed Dependency</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PROJECT___GET_MANAGED_DEPENDENCY__DEPENDENCY = eINSTANCE.getProject__GetManagedDependency__Dependency();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.CoordinateImpl <em>Coordinate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.CoordinateImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getCoordinate()
     * @generated
     */
    EClass COORDINATE = eINSTANCE.getCoordinate();

    /**
     * The meta object literal for the '<em><b>Group Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COORDINATE__GROUP_ID = eINSTANCE.getCoordinate_GroupId();

    /**
     * The meta object literal for the '<em><b>Artifact Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COORDINATE__ARTIFACT_ID = eINSTANCE.getCoordinate_ArtifactId();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COORDINATE__VERSION = eINSTANCE.getCoordinate_Version();

    /**
     * The meta object literal for the '<em><b>Expanded Group Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COORDINATE__EXPANDED_GROUP_ID = eINSTANCE.getCoordinate_ExpandedGroupId();

    /**
     * The meta object literal for the '<em><b>Expanded Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COORDINATE__EXPANDED_VERSION = eINSTANCE.getCoordinate_ExpandedVersion();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.ParentImpl <em>Parent</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.ParentImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getParent()
     * @generated
     */
    EClass PARENT = eINSTANCE.getParent();

    /**
     * The meta object literal for the '<em><b>Project</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARENT__PROJECT = eINSTANCE.getParent_Project();

    /**
     * The meta object literal for the '<em><b>Resolved Project</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARENT__RESOLVED_PROJECT = eINSTANCE.getParent_ResolvedProject();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.DependencyImpl <em>Dependency</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.DependencyImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDependency()
     * @generated
     */
    EClass DEPENDENCY = eINSTANCE.getDependency();

    /**
     * The meta object literal for the '<em><b>Resolved Project</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEPENDENCY__RESOLVED_PROJECT = eINSTANCE.getDependency_ResolvedProject();

    /**
     * The meta object literal for the '<em><b>Resolved Managed Dependency</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY = eINSTANCE.getDependency_ResolvedManagedDependency();

    /**
     * The meta object literal for the '<em><b>Incoming Resolved Managed Dependencies</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES = eINSTANCE.getDependency_IncomingResolvedManagedDependencies();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.PropertyImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getProperty()
     * @generated
     */
    EClass PROPERTY = eINSTANCE.getProperty();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__KEY = eINSTANCE.getProperty_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

    /**
     * The meta object literal for the '<em><b>Expanded Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__EXPANDED_VALUE = eINSTANCE.getProperty_ExpandedValue();

    /**
     * The meta object literal for the '<em><b>Incoming Resolved Property References</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES = eINSTANCE.getProperty_IncomingResolvedPropertyReferences();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.maven.impl.PropertyReferenceImpl <em>Property Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.impl.PropertyReferenceImpl
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getPropertyReference()
     * @generated
     */
    EClass PROPERTY_REFERENCE = eINSTANCE.getPropertyReference();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY_REFERENCE__NAME = eINSTANCE.getPropertyReference_Name();

    /**
     * The meta object literal for the '<em><b>Resolved Property</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY_REFERENCE__RESOLVED_PROPERTY = eINSTANCE.getPropertyReference_ResolvedProperty();

    /**
     * The meta object literal for the '<em>Document</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3c.dom.Document
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getDocument()
     * @generated
     */
    EDataType DOCUMENT = eINSTANCE.getDocument();

    /**
     * The meta object literal for the '<em><b>Relative Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARENT__RELATIVE_PATH = eINSTANCE.getParent_RelativePath();

    /**
     * The meta object literal for the '<em>Element</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3c.dom.Element
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getElement()
     * @generated
     */
    EDataType ELEMENT = eINSTANCE.getElement();

    /**
     * The meta object literal for the '<em>Element Edit</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.util.MavenValidator.ElementEdit
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getElementEdit()
     * @generated
     */
    EDataType ELEMENT_EDIT = eINSTANCE.getElementEdit();

    /**
     * The meta object literal for the '<em>Text Region</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getTextRegion()
     * @generated
     */
    EDataType TEXT_REGION = eINSTANCE.getTextRegion();

    /**
     * The meta object literal for the '<em>XPath</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.SegmentSequence
     * @see org.eclipse.oomph.maven.impl.MavenPackageImpl#getXPath()
     * @generated
     */
    EDataType XPATH = eINSTANCE.getXPath();

  }

} // MavenPackage

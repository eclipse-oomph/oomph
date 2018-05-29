/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates;

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
 * @see org.eclipse.oomph.predicates.PredicatesFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Predicates.ecore'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.predicates.edit/icons/full/obj16'"
 * @generated
 */
public interface PredicatesPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "predicates";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/predicates/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "predicates";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  PredicatesPackage eINSTANCE = org.eclipse.oomph.predicates.impl.PredicatesPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.PredicateImpl <em>Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.PredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getPredicate()
   * @generated
   */
  int PREDICATE = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREDICATE__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The number of structural features of the '<em>Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREDICATE_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREDICATE___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREDICATE___MATCHES__IRESOURCE = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREDICATE_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.NamePredicateImpl <em>Name Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.NamePredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNamePredicate()
   * @generated
   */
  int NAME_PREDICATE = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE__PATTERN = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Name Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Name Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAME_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.CommentPredicateImpl <em>Comment Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.CommentPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getCommentPredicate()
   * @generated
   */
  int COMMENT_PREDICATE = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE__PATTERN = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Comment Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Comment Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMENT_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.LocationPredicateImpl <em>Location Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.LocationPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getLocationPredicate()
   * @generated
   */
  int LOCATION_PREDICATE = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE__PATTERN = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Location Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Location Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl <em>Repository Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getRepositoryPredicate()
   * @generated
   */
  int REPOSITORY_PREDICATE = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Project</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE__PROJECT = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Repository Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Repository Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPOSITORY_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.AndPredicateImpl <em>And Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.AndPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getAndPredicate()
   * @generated
   */
  int AND_PREDICATE = 5;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Operands</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE__OPERANDS = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>And Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>And Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.OrPredicateImpl <em>Or Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.OrPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getOrPredicate()
   * @generated
   */
  int OR_PREDICATE = 6;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Operands</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE__OPERANDS = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Or Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Or Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.NotPredicateImpl <em>Not Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.NotPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNotPredicate()
   * @generated
   */
  int NOT_PREDICATE = 7;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE__OPERAND = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Not Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Not Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NOT_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.NaturePredicateImpl <em>Nature Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.NaturePredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNaturePredicate()
   * @generated
   */
  int NATURE_PREDICATE = 8;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Nature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE__NATURE = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Nature Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Nature Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NATURE_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.BuilderPredicateImpl <em>Builder Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.BuilderPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getBuilderPredicate()
   * @generated
   */
  int BUILDER_PREDICATE = 9;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Builder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE__BUILDER = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Builder Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Builder Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BUILDER_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.FilePredicateImpl <em>File Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.FilePredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getFilePredicate()
   * @generated
   */
  int FILE_PREDICATE = 10;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>File Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE__FILE_PATTERN = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Content Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE__CONTENT_PATTERN = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>File Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>File Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.predicates.impl.ImportedPredicateImpl <em>Imported Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.predicates.impl.ImportedPredicateImpl
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getImportedPredicate()
   * @generated
   */
  int IMPORTED_PREDICATE = 11;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE__ANNOTATIONS = PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Accessible</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE__ACCESSIBLE = PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Imported Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE___GET_ANNOTATION__STRING = PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE___MATCHES__IRESOURCE = PREDICATE___MATCHES__IRESOURCE;

  /**
   * The number of operations of the '<em>Imported Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORTED_PREDICATE_OPERATION_COUNT = PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Project</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.core.resources.IProject
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getProject()
   * @generated
   */
  int PROJECT = 12;

  /**
   * The meta object id for the '<em>Resource</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.core.resources.IResource
   * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getResource()
   * @generated
   */
  int RESOURCE = 13;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.Predicate <em>Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Predicate</em>'.
   * @see org.eclipse.oomph.predicates.Predicate
   * @generated
   */
  EClass getPredicate();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.predicates.Predicate#matches(org.eclipse.core.resources.IResource) <em>Matches</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Matches</em>' operation.
   * @see org.eclipse.oomph.predicates.Predicate#matches(org.eclipse.core.resources.IResource)
   * @generated
   */
  EOperation getPredicate__Matches__IResource();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.NamePredicate <em>Name Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Name Predicate</em>'.
   * @see org.eclipse.oomph.predicates.NamePredicate
   * @generated
   */
  EClass getNamePredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.NamePredicate#getPattern <em>Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pattern</em>'.
   * @see org.eclipse.oomph.predicates.NamePredicate#getPattern()
   * @see #getNamePredicate()
   * @generated
   */
  EAttribute getNamePredicate_Pattern();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.CommentPredicate <em>Comment Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Comment Predicate</em>'.
   * @see org.eclipse.oomph.predicates.CommentPredicate
   * @generated
   */
  EClass getCommentPredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.CommentPredicate#getPattern <em>Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pattern</em>'.
   * @see org.eclipse.oomph.predicates.CommentPredicate#getPattern()
   * @see #getCommentPredicate()
   * @generated
   */
  EAttribute getCommentPredicate_Pattern();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.LocationPredicate <em>Location Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Location Predicate</em>'.
   * @see org.eclipse.oomph.predicates.LocationPredicate
   * @generated
   */
  EClass getLocationPredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.LocationPredicate#getPattern <em>Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pattern</em>'.
   * @see org.eclipse.oomph.predicates.LocationPredicate#getPattern()
   * @see #getLocationPredicate()
   * @generated
   */
  EAttribute getLocationPredicate_Pattern();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.RepositoryPredicate <em>Repository Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Repository Predicate</em>'.
   * @see org.eclipse.oomph.predicates.RepositoryPredicate
   * @generated
   */
  EClass getRepositoryPredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.RepositoryPredicate#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Project</em>'.
   * @see org.eclipse.oomph.predicates.RepositoryPredicate#getProject()
   * @see #getRepositoryPredicate()
   * @generated
   */
  EAttribute getRepositoryPredicate_Project();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.AndPredicate <em>And Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>And Predicate</em>'.
   * @see org.eclipse.oomph.predicates.AndPredicate
   * @generated
   */
  EClass getAndPredicate();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.predicates.AndPredicate#getOperands <em>Operands</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Operands</em>'.
   * @see org.eclipse.oomph.predicates.AndPredicate#getOperands()
   * @see #getAndPredicate()
   * @generated
   */
  EReference getAndPredicate_Operands();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.OrPredicate <em>Or Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Or Predicate</em>'.
   * @see org.eclipse.oomph.predicates.OrPredicate
   * @generated
   */
  EClass getOrPredicate();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.predicates.OrPredicate#getOperands <em>Operands</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Operands</em>'.
   * @see org.eclipse.oomph.predicates.OrPredicate#getOperands()
   * @see #getOrPredicate()
   * @generated
   */
  EReference getOrPredicate_Operands();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.NotPredicate <em>Not Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Not Predicate</em>'.
   * @see org.eclipse.oomph.predicates.NotPredicate
   * @generated
   */
  EClass getNotPredicate();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.predicates.NotPredicate#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see org.eclipse.oomph.predicates.NotPredicate#getOperand()
   * @see #getNotPredicate()
   * @generated
   */
  EReference getNotPredicate_Operand();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.NaturePredicate <em>Nature Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Nature Predicate</em>'.
   * @see org.eclipse.oomph.predicates.NaturePredicate
   * @generated
   */
  EClass getNaturePredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.NaturePredicate#getNature <em>Nature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Nature</em>'.
   * @see org.eclipse.oomph.predicates.NaturePredicate#getNature()
   * @see #getNaturePredicate()
   * @generated
   */
  EAttribute getNaturePredicate_Nature();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.BuilderPredicate <em>Builder Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Builder Predicate</em>'.
   * @see org.eclipse.oomph.predicates.BuilderPredicate
   * @generated
   */
  EClass getBuilderPredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.BuilderPredicate#getBuilder <em>Builder</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Builder</em>'.
   * @see org.eclipse.oomph.predicates.BuilderPredicate#getBuilder()
   * @see #getBuilderPredicate()
   * @generated
   */
  EAttribute getBuilderPredicate_Builder();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.FilePredicate <em>File Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File Predicate</em>'.
   * @see org.eclipse.oomph.predicates.FilePredicate
   * @generated
   */
  EClass getFilePredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.FilePredicate#getFilePattern <em>File Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>File Pattern</em>'.
   * @see org.eclipse.oomph.predicates.FilePredicate#getFilePattern()
   * @see #getFilePredicate()
   * @generated
   */
  EAttribute getFilePredicate_FilePattern();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.FilePredicate#getContentPattern <em>Content Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Content Pattern</em>'.
   * @see org.eclipse.oomph.predicates.FilePredicate#getContentPattern()
   * @see #getFilePredicate()
   * @generated
   */
  EAttribute getFilePredicate_ContentPattern();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.predicates.ImportedPredicate <em>Imported Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Imported Predicate</em>'.
   * @see org.eclipse.oomph.predicates.ImportedPredicate
   * @generated
   */
  EClass getImportedPredicate();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.predicates.ImportedPredicate#isAccessible <em>Accessible</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Accessible</em>'.
   * @see org.eclipse.oomph.predicates.ImportedPredicate#isAccessible()
   * @see #getImportedPredicate()
   * @generated
   */
  EAttribute getImportedPredicate_Accessible();

  /**
   * Returns the meta object for data type '{@link org.eclipse.core.resources.IProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Project</em>'.
   * @see org.eclipse.core.resources.IProject
   * @model instanceClass="org.eclipse.core.resources.IProject"
   * @generated
   */
  EDataType getProject();

  /**
   * Returns the meta object for data type '{@link org.eclipse.core.resources.IResource <em>Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Resource</em>'.
   * @see org.eclipse.core.resources.IResource
   * @model instanceClass="org.eclipse.core.resources.IResource"
   * @generated
   */
  EDataType getResource();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  PredicatesFactory getPredicatesFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.PredicateImpl <em>Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.PredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getPredicate()
     * @generated
     */
    EClass PREDICATE = eINSTANCE.getPredicate();

    /**
     * The meta object literal for the '<em><b>Matches</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation PREDICATE___MATCHES__IRESOURCE = eINSTANCE.getPredicate__Matches__IResource();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.NamePredicateImpl <em>Name Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.NamePredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNamePredicate()
     * @generated
     */
    EClass NAME_PREDICATE = eINSTANCE.getNamePredicate();

    /**
     * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NAME_PREDICATE__PATTERN = eINSTANCE.getNamePredicate_Pattern();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.CommentPredicateImpl <em>Comment Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.CommentPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getCommentPredicate()
     * @generated
     */
    EClass COMMENT_PREDICATE = eINSTANCE.getCommentPredicate();

    /**
     * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMMENT_PREDICATE__PATTERN = eINSTANCE.getCommentPredicate_Pattern();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.LocationPredicateImpl <em>Location Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.LocationPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getLocationPredicate()
     * @generated
     */
    EClass LOCATION_PREDICATE = eINSTANCE.getLocationPredicate();

    /**
     * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOCATION_PREDICATE__PATTERN = eINSTANCE.getLocationPredicate_Pattern();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl <em>Repository Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getRepositoryPredicate()
     * @generated
     */
    EClass REPOSITORY_PREDICATE = eINSTANCE.getRepositoryPredicate();

    /**
     * The meta object literal for the '<em><b>Project</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REPOSITORY_PREDICATE__PROJECT = eINSTANCE.getRepositoryPredicate_Project();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.AndPredicateImpl <em>And Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.AndPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getAndPredicate()
     * @generated
     */
    EClass AND_PREDICATE = eINSTANCE.getAndPredicate();

    /**
     * The meta object literal for the '<em><b>Operands</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AND_PREDICATE__OPERANDS = eINSTANCE.getAndPredicate_Operands();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.OrPredicateImpl <em>Or Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.OrPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getOrPredicate()
     * @generated
     */
    EClass OR_PREDICATE = eINSTANCE.getOrPredicate();

    /**
     * The meta object literal for the '<em><b>Operands</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OR_PREDICATE__OPERANDS = eINSTANCE.getOrPredicate_Operands();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.NotPredicateImpl <em>Not Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.NotPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNotPredicate()
     * @generated
     */
    EClass NOT_PREDICATE = eINSTANCE.getNotPredicate();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference NOT_PREDICATE__OPERAND = eINSTANCE.getNotPredicate_Operand();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.NaturePredicateImpl <em>Nature Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.NaturePredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getNaturePredicate()
     * @generated
     */
    EClass NATURE_PREDICATE = eINSTANCE.getNaturePredicate();

    /**
     * The meta object literal for the '<em><b>Nature</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NATURE_PREDICATE__NATURE = eINSTANCE.getNaturePredicate_Nature();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.BuilderPredicateImpl <em>Builder Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.BuilderPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getBuilderPredicate()
     * @generated
     */
    EClass BUILDER_PREDICATE = eINSTANCE.getBuilderPredicate();

    /**
     * The meta object literal for the '<em><b>Builder</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BUILDER_PREDICATE__BUILDER = eINSTANCE.getBuilderPredicate_Builder();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.FilePredicateImpl <em>File Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.FilePredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getFilePredicate()
     * @generated
     */
    EClass FILE_PREDICATE = eINSTANCE.getFilePredicate();

    /**
     * The meta object literal for the '<em><b>File Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILE_PREDICATE__FILE_PATTERN = eINSTANCE.getFilePredicate_FilePattern();

    /**
     * The meta object literal for the '<em><b>Content Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILE_PREDICATE__CONTENT_PATTERN = eINSTANCE.getFilePredicate_ContentPattern();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.predicates.impl.ImportedPredicateImpl <em>Imported Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.predicates.impl.ImportedPredicateImpl
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getImportedPredicate()
     * @generated
     */
    EClass IMPORTED_PREDICATE = eINSTANCE.getImportedPredicate();

    /**
     * The meta object literal for the '<em><b>Accessible</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPORTED_PREDICATE__ACCESSIBLE = eINSTANCE.getImportedPredicate_Accessible();

    /**
     * The meta object literal for the '<em>Project</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.core.resources.IProject
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getProject()
     * @generated
     */
    EDataType PROJECT = eINSTANCE.getProject();

    /**
     * The meta object literal for the '<em>Resource</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.core.resources.IResource
     * @see org.eclipse.oomph.predicates.impl.PredicatesPackageImpl#getResource()
     * @generated
     */
    EDataType RESOURCE = eINSTANCE.getResource();

  }

} // PredicatesPackage

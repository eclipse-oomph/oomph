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
package org.eclipse.oomph.workingsets;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.predicates.PredicatesPackage;

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
 * @see org.eclipse.oomph.workingsets.WorkingSetsFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/WorkingSets.ecore'"
 * @generated
 */
public interface WorkingSetsPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "workingsets";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/workingsets/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "workingsets";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  WorkingSetsPackage eINSTANCE = org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.workingsets.impl.WorkingSetImpl <em>Working Set</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetImpl
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getWorkingSet()
   * @generated
   */
  int WORKING_SET = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET__NAME = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Predicates</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET__PREDICATES = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET__ID = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Working Set</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET___MATCHES__IPROJECT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Working Set</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.workingsets.impl.WorkingSetGroupImpl <em>Working Set Group</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetGroupImpl
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getWorkingSetGroup()
   * @generated
   */
  int WORKING_SET_GROUP = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Working Sets</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP__WORKING_SETS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Working Set Group</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP___GET_ANNOTATION__STRING = BasePackage.MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Get Working Set</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP___GET_WORKING_SET__STRING = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Working Set Group</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKING_SET_GROUP_OPERATION_COUNT = BasePackage.MODEL_ELEMENT_OPERATION_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.workingsets.impl.ExclusionPredicateImpl <em>Exclusion Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.workingsets.impl.ExclusionPredicateImpl
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getExclusionPredicate()
   * @generated
   */
  int EXCLUSION_PREDICATE = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.workingsets.impl.InclusionPredicateImpl <em>Inclusion Predicate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.workingsets.impl.InclusionPredicateImpl
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getInclusionPredicate()
   * @generated
   */
  int INCLUSION_PREDICATE = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE__ANNOTATIONS = PredicatesPackage.PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Included Working Sets</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE__INCLUDED_WORKING_SETS = PredicatesPackage.PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Inclusion Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE_FEATURE_COUNT = PredicatesPackage.PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE___GET_ANNOTATION__STRING = PredicatesPackage.PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE___MATCHES__IPROJECT = PredicatesPackage.PREDICATE___MATCHES__IPROJECT;

  /**
   * The number of operations of the '<em>Inclusion Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUSION_PREDICATE_OPERATION_COUNT = PredicatesPackage.PREDICATE_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE__ANNOTATIONS = PredicatesPackage.PREDICATE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Excluded Working Sets</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS = PredicatesPackage.PREDICATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Exclusion Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE_FEATURE_COUNT = PredicatesPackage.PREDICATE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE___GET_ANNOTATION__STRING = PredicatesPackage.PREDICATE___GET_ANNOTATION__STRING;

  /**
   * The operation id for the '<em>Matches</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE___MATCHES__IPROJECT = PredicatesPackage.PREDICATE___MATCHES__IPROJECT;

  /**
   * The number of operations of the '<em>Exclusion Predicate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCLUSION_PREDICATE_OPERATION_COUNT = PredicatesPackage.PREDICATE_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Project</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.core.resources.IProject
   * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getProject()
   * @generated
   */
  int PROJECT = 4;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.workingsets.WorkingSet <em>Working Set</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Working Set</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSet
   * @generated
   */
  EClass getWorkingSet();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.workingsets.WorkingSet#getPredicates <em>Predicates</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Predicates</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSet#getPredicates()
   * @see #getWorkingSet()
   * @generated
   */
  EReference getWorkingSet_Predicates();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.workingsets.WorkingSet#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSet#getID()
   * @see #getWorkingSet()
   * @generated
   */
  EAttribute getWorkingSet_ID();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.workingsets.WorkingSet#matches(org.eclipse.core.resources.IProject) <em>Matches</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Matches</em>' operation.
   * @see org.eclipse.oomph.workingsets.WorkingSet#matches(org.eclipse.core.resources.IProject)
   * @generated
   */
  EOperation getWorkingSet__Matches__IProject();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.workingsets.WorkingSet#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSet#getName()
   * @see #getWorkingSet()
   * @generated
   */
  EAttribute getWorkingSet_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.workingsets.WorkingSetGroup <em>Working Set Group</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Working Set Group</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSetGroup
   * @generated
   */
  EClass getWorkingSetGroup();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.workingsets.WorkingSetGroup#getWorkingSets <em>Working Sets</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Working Sets</em>'.
   * @see org.eclipse.oomph.workingsets.WorkingSetGroup#getWorkingSets()
   * @see #getWorkingSetGroup()
   * @generated
   */
  EReference getWorkingSetGroup_WorkingSets();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.workingsets.WorkingSetGroup#getWorkingSet(java.lang.String) <em>Get Working Set</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Working Set</em>' operation.
   * @see org.eclipse.oomph.workingsets.WorkingSetGroup#getWorkingSet(java.lang.String)
   * @generated
   */
  EOperation getWorkingSetGroup__GetWorkingSet__String();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.workingsets.ExclusionPredicate <em>Exclusion Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Exclusion Predicate</em>'.
   * @see org.eclipse.oomph.workingsets.ExclusionPredicate
   * @generated
   */
  EClass getExclusionPredicate();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.workingsets.ExclusionPredicate#getExcludedWorkingSets <em>Excluded Working Sets</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Excluded Working Sets</em>'.
   * @see org.eclipse.oomph.workingsets.ExclusionPredicate#getExcludedWorkingSets()
   * @see #getExclusionPredicate()
   * @generated
   */
  EReference getExclusionPredicate_ExcludedWorkingSets();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.workingsets.InclusionPredicate <em>Inclusion Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Inclusion Predicate</em>'.
   * @see org.eclipse.oomph.workingsets.InclusionPredicate
   * @generated
   */
  EClass getInclusionPredicate();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.workingsets.InclusionPredicate#getIncludedWorkingSets <em>Included Working Sets</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Included Working Sets</em>'.
   * @see org.eclipse.oomph.workingsets.InclusionPredicate#getIncludedWorkingSets()
   * @see #getInclusionPredicate()
   * @generated
   */
  EReference getInclusionPredicate_IncludedWorkingSets();

  /**
   * Returns the meta object for data type '{@link org.eclipse.core.resources.IProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Project</em>'.
   * @see org.eclipse.core.resources.IProject
   * @model instanceClass="org.eclipse.core.resources.IProject" serializeable="false"
   * @generated
   */
  EDataType getProject();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  WorkingSetsFactory getWorkingSetsFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.workingsets.impl.WorkingSetImpl <em>Working Set</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetImpl
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getWorkingSet()
     * @generated
     */
    EClass WORKING_SET = eINSTANCE.getWorkingSet();

    /**
     * The meta object literal for the '<em><b>Predicates</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference WORKING_SET__PREDICATES = eINSTANCE.getWorkingSet_Predicates();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WORKING_SET__ID = eINSTANCE.getWorkingSet_ID();

    /**
     * The meta object literal for the '<em><b>Matches</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation WORKING_SET___MATCHES__IPROJECT = eINSTANCE.getWorkingSet__Matches__IProject();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WORKING_SET__NAME = eINSTANCE.getWorkingSet_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.workingsets.impl.WorkingSetGroupImpl <em>Working Set Group</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetGroupImpl
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getWorkingSetGroup()
     * @generated
     */
    EClass WORKING_SET_GROUP = eINSTANCE.getWorkingSetGroup();

    /**
     * The meta object literal for the '<em><b>Working Sets</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference WORKING_SET_GROUP__WORKING_SETS = eINSTANCE.getWorkingSetGroup_WorkingSets();

    /**
     * The meta object literal for the '<em><b>Get Working Set</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation WORKING_SET_GROUP___GET_WORKING_SET__STRING = eINSTANCE.getWorkingSetGroup__GetWorkingSet__String();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.workingsets.impl.ExclusionPredicateImpl <em>Exclusion Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.workingsets.impl.ExclusionPredicateImpl
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getExclusionPredicate()
     * @generated
     */
    EClass EXCLUSION_PREDICATE = eINSTANCE.getExclusionPredicate();

    /**
     * The meta object literal for the '<em><b>Excluded Working Sets</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS = eINSTANCE.getExclusionPredicate_ExcludedWorkingSets();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.workingsets.impl.InclusionPredicateImpl <em>Inclusion Predicate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.workingsets.impl.InclusionPredicateImpl
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getInclusionPredicate()
     * @generated
     */
    EClass INCLUSION_PREDICATE = eINSTANCE.getInclusionPredicate();

    /**
     * The meta object literal for the '<em><b>Included Working Sets</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INCLUSION_PREDICATE__INCLUDED_WORKING_SETS = eINSTANCE.getInclusionPredicate_IncludedWorkingSets();

    /**
     * The meta object literal for the '<em>Project</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.core.resources.IProject
     * @see org.eclipse.oomph.workingsets.impl.WorkingSetsPackageImpl#getProject()
     * @generated
     */
    EDataType PROJECT = eINSTANCE.getProject();

  }

} // WorkingSetsPackage

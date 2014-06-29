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
package org.eclipse.oomph.setup.p2;

import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.p2.SetupP2Factory
 * @model kind="package"
 * @generated
 */
public interface SetupP2Package extends EPackage
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
  String eNS_URI = "http://www.eclipse.org/oomph/setup/p2/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "setup.p2";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SetupP2Package eINSTANCE = org.eclipse.oomph.setup.p2.impl.SetupP2PackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl <em>P2 Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.p2.impl.P2TaskImpl
   * @see org.eclipse.oomph.setup.p2.impl.SetupP2PackageImpl#getP2Task()
   * @generated
   */
  int P2_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__LABEL = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__REQUIREMENTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Repositories</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__REPOSITORIES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>License Confirmation Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__LICENSE_CONFIRMATION_DISABLED = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Merge Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK__MERGE_DISABLED = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>P2 Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int P2_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.p2.P2Task <em>P2 Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>P2 Task</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task
   * @generated
   */
  EClass getP2Task();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.p2.P2Task#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task#getLabel()
   * @see #getP2Task()
   * @generated
   */
  EAttribute getP2Task_Label();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.p2.P2Task#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task#getRequirements()
   * @see #getP2Task()
   * @generated
   */
  EReference getP2Task_Requirements();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.p2.P2Task#getRepositories <em>Repositories</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Repositories</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task#getRepositories()
   * @see #getP2Task()
   * @generated
   */
  EReference getP2Task_Repositories();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.p2.P2Task#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>License Confirmation Disabled</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task#isLicenseConfirmationDisabled()
   * @see #getP2Task()
   * @generated
   */
  EAttribute getP2Task_LicenseConfirmationDisabled();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.p2.P2Task#isMergeDisabled <em>Merge Disabled</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Merge Disabled</em>'.
   * @see org.eclipse.oomph.setup.p2.P2Task#isMergeDisabled()
   * @see #getP2Task()
   * @generated
   */
  EAttribute getP2Task_MergeDisabled();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SetupP2Factory getSetupP2Factory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl <em>P2 Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.p2.impl.P2TaskImpl
     * @see org.eclipse.oomph.setup.p2.impl.SetupP2PackageImpl#getP2Task()
     * @generated
     */
    EClass P2_TASK = eINSTANCE.getP2Task();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute P2_TASK__LABEL = eINSTANCE.getP2Task_Label();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference P2_TASK__REQUIREMENTS = eINSTANCE.getP2Task_Requirements();

    /**
     * The meta object literal for the '<em><b>Repositories</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference P2_TASK__REPOSITORIES = eINSTANCE.getP2Task_Repositories();

    /**
     * The meta object literal for the '<em><b>License Confirmation Disabled</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute P2_TASK__LICENSE_CONFIRMATION_DISABLED = eINSTANCE.getP2Task_LicenseConfirmationDisabled();

    /**
     * The meta object literal for the '<em><b>Merge Disabled</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute P2_TASK__MERGE_DISABLED = eINSTANCE.getP2Task_MergeDisabled();

  }

} // SetupP2Package

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
package org.eclipse.oomph.setup.targlets;

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
 * @see org.eclipse.oomph.setup.targlets.SetupTargletsFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/SetupTarglets.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.targlets.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.targlets.feature.group org.eclipse.oomph.setup.targlets.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.targlets.edit/icons/full/obj16'"
 * @generated
 */
public interface SetupTargletsPackage extends EPackage
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
  String eNS_URI = "http://www.eclipse.org/oomph/setup/targlets/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "setup.targlets";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SetupTargletsPackage eINSTANCE = org.eclipse.oomph.setup.targlets.impl.SetupTargletsPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl <em>Targlet Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl
   * @see org.eclipse.oomph.setup.targlets.impl.SetupTargletsPackageImpl#getTargletTask()
   * @generated
   */
  int TARGLET_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Targlets</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__TARGLETS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Targlet UR Is</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__TARGLET_UR_IS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Operating System</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__OPERATING_SYSTEM = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Windowing System</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__WINDOWING_SYSTEM = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Architecture</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__ARCHITECTURE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Locale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__LOCALE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Program Arguments</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__PROGRAM_ARGUMENTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>VM Arguments</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__VM_ARGUMENTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Implicit Dependencies</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK__IMPLICIT_DEPENDENCIES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 8;

  /**
   * The number of structural features of the '<em>Targlet Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGLET_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 9;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.targlets.impl.ImplicitDependencyImpl <em>Implicit Dependency</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.targlets.impl.ImplicitDependencyImpl
   * @see org.eclipse.oomph.setup.targlets.impl.SetupTargletsPackageImpl#getImplicitDependency()
   * @generated
   */
  int IMPLICIT_DEPENDENCY = 1;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPLICIT_DEPENDENCY__ID = 0;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPLICIT_DEPENDENCY__VERSION = 1;

  /**
   * The number of structural features of the '<em>Implicit Dependency</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPLICIT_DEPENDENCY_FEATURE_COUNT = 2;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.targlets.TargletTask <em>Targlet Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Targlet Task</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask
   * @generated
   */
  EClass getTargletTask();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.targlets.TargletTask#getTarglets <em>Targlets</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Targlets</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getTarglets()
   * @see #getTargletTask()
   * @generated
   */
  EReference getTargletTask_Targlets();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.setup.targlets.TargletTask#getTargletURIs <em>Targlet UR Is</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Targlet UR Is</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getTargletURIs()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_TargletURIs();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getOperatingSystem <em>Operating System</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operating System</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getOperatingSystem()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_OperatingSystem();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getWindowingSystem <em>Windowing System</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Windowing System</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getWindowingSystem()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_WindowingSystem();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getArchitecture <em>Architecture</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Architecture</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getArchitecture()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_Architecture();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getLocale <em>Locale</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Locale</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getLocale()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_Locale();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getProgramArguments <em>Program Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Program Arguments</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getProgramArguments()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_ProgramArguments();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.TargletTask#getVMArguments <em>VM Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>VM Arguments</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getVMArguments()
   * @see #getTargletTask()
   * @generated
   */
  EAttribute getTargletTask_VMArguments();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.targlets.TargletTask#getImplicitDependencies <em>Implicit Dependencies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Implicit Dependencies</em>'.
   * @see org.eclipse.oomph.setup.targlets.TargletTask#getImplicitDependencies()
   * @see #getTargletTask()
   * @generated
   */
  EReference getTargletTask_ImplicitDependencies();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.targlets.ImplicitDependency <em>Implicit Dependency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Implicit Dependency</em>'.
   * @see org.eclipse.oomph.setup.targlets.ImplicitDependency
   * @generated
   */
  EClass getImplicitDependency();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.ImplicitDependency#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.targlets.ImplicitDependency#getID()
   * @see #getImplicitDependency()
   * @generated
   */
  EAttribute getImplicitDependency_ID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.targlets.ImplicitDependency#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see org.eclipse.oomph.setup.targlets.ImplicitDependency#getVersion()
   * @see #getImplicitDependency()
   * @generated
   */
  EAttribute getImplicitDependency_Version();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SetupTargletsFactory getSetupTargletsFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl <em>Targlet Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl
     * @see org.eclipse.oomph.setup.targlets.impl.SetupTargletsPackageImpl#getTargletTask()
     * @generated
     */
    EClass TARGLET_TASK = eINSTANCE.getTargletTask();

    /**
     * The meta object literal for the '<em><b>Targlets</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET_TASK__TARGLETS = eINSTANCE.getTargletTask_Targlets();

    /**
     * The meta object literal for the '<em><b>Targlet UR Is</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__TARGLET_UR_IS = eINSTANCE.getTargletTask_TargletURIs();

    /**
     * The meta object literal for the '<em><b>Operating System</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__OPERATING_SYSTEM = eINSTANCE.getTargletTask_OperatingSystem();

    /**
     * The meta object literal for the '<em><b>Windowing System</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__WINDOWING_SYSTEM = eINSTANCE.getTargletTask_WindowingSystem();

    /**
     * The meta object literal for the '<em><b>Architecture</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__ARCHITECTURE = eINSTANCE.getTargletTask_Architecture();

    /**
     * The meta object literal for the '<em><b>Locale</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__LOCALE = eINSTANCE.getTargletTask_Locale();

    /**
     * The meta object literal for the '<em><b>Program Arguments</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__PROGRAM_ARGUMENTS = eINSTANCE.getTargletTask_ProgramArguments();

    /**
     * The meta object literal for the '<em><b>VM Arguments</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGLET_TASK__VM_ARGUMENTS = eINSTANCE.getTargletTask_VMArguments();

    /**
     * The meta object literal for the '<em><b>Implicit Dependencies</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGLET_TASK__IMPLICIT_DEPENDENCIES = eINSTANCE.getTargletTask_ImplicitDependencies();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.targlets.impl.ImplicitDependencyImpl <em>Implicit Dependency</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.targlets.impl.ImplicitDependencyImpl
     * @see org.eclipse.oomph.setup.targlets.impl.SetupTargletsPackageImpl#getImplicitDependency()
     * @generated
     */
    EClass IMPLICIT_DEPENDENCY = eINSTANCE.getImplicitDependency();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPLICIT_DEPENDENCY__ID = eINSTANCE.getImplicitDependency_ID();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPLICIT_DEPENDENCY__VERSION = eINSTANCE.getImplicitDependency_Version();

  }

} // SetupTargletsPackage

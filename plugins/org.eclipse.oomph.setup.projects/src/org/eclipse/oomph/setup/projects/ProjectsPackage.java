/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projects;

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
 * @see org.eclipse.oomph.setup.projects.ProjectsFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='https://raw.githubusercontent.com/eclipse-oomph/oomph/master/setups/models/Projects.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.projects.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.projects.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.setup.projects.edit/icons/full/obj16'"
 * @generated
 */
public interface ProjectsPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "projects"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/projects/1.0"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "projects"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ProjectsPackage eINSTANCE = org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.projects.impl.ProjectsImportTaskImpl <em>Import Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.projects.impl.ProjectsImportTaskImpl
   * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getProjectsImportTask()
   * @generated
   */
  int PROJECTS_IMPORT_TASK = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl <em>Build Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl
   * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getProjectsBuildTask()
   * @generated
   */
  int PROJECTS_BUILD_TASK = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.projects.impl.PathVariableTaskImpl <em>Path Variable Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.projects.impl.PathVariableTaskImpl
   * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getPathVariableTask()
   * @generated
   */
  int PATH_VARIABLE_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__NAME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK__URI = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Path Variable Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_VARIABLE_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Force</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__FORCE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Source Locators</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK__SOURCE_LOCATORS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Import Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_IMPORT_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Predicates</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__PREDICATES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Only New Projects</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Refresh</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__REFRESH = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Clean</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__CLEAN = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Build</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK__BUILD = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Build Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECTS_BUILD_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.projects.ProjectsImportTask <em>Import Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import Task</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsImportTask
   * @generated
   */
  EClass getProjectsImportTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#isForce <em>Force</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Force</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsImportTask#isForce()
   * @see #getProjectsImportTask()
   * @generated
   */
  EAttribute getProjectsImportTask_Force();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#getSourceLocators <em>Source Locators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Source Locators</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsImportTask#getSourceLocators()
   * @see #getProjectsImportTask()
   * @generated
   */
  EReference getProjectsImportTask_SourceLocators();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask <em>Build Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Build Task</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask
   * @generated
   */
  EClass getProjectsBuildTask();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#getPredicates <em>Predicates</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Predicates</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask#getPredicates()
   * @see #getProjectsBuildTask()
   * @generated
   */
  EReference getProjectsBuildTask_Predicates();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isOnlyNewProjects <em>Only New Projects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Only New Projects</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask#isOnlyNewProjects()
   * @see #getProjectsBuildTask()
   * @generated
   */
  EAttribute getProjectsBuildTask_OnlyNewProjects();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isRefresh <em>Refresh</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Refresh</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask#isRefresh()
   * @see #getProjectsBuildTask()
   * @generated
   */
  EAttribute getProjectsBuildTask_Refresh();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isClean <em>Clean</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Clean</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask#isClean()
   * @see #getProjectsBuildTask()
   * @generated
   */
  EAttribute getProjectsBuildTask_Clean();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isBuild <em>Build</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Build</em>'.
   * @see org.eclipse.oomph.setup.projects.ProjectsBuildTask#isBuild()
   * @see #getProjectsBuildTask()
   * @generated
   */
  EAttribute getProjectsBuildTask_Build();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.projects.PathVariableTask <em>Path Variable Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Path Variable Task</em>'.
   * @see org.eclipse.oomph.setup.projects.PathVariableTask
   * @generated
   */
  EClass getPathVariableTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.PathVariableTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.projects.PathVariableTask#getName()
   * @see #getPathVariableTask()
   * @generated
   */
  EAttribute getPathVariableTask_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.projects.PathVariableTask#getURI <em>URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URI</em>'.
   * @see org.eclipse.oomph.setup.projects.PathVariableTask#getURI()
   * @see #getPathVariableTask()
   * @generated
   */
  EAttribute getPathVariableTask_URI();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ProjectsFactory getProjectsFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.projects.impl.ProjectsImportTaskImpl <em>Import Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.projects.impl.ProjectsImportTaskImpl
     * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getProjectsImportTask()
     * @generated
     */
    EClass PROJECTS_IMPORT_TASK = eINSTANCE.getProjectsImportTask();

    /**
     * The meta object literal for the '<em><b>Force</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECTS_IMPORT_TASK__FORCE = eINSTANCE.getProjectsImportTask_Force();

    /**
     * The meta object literal for the '<em><b>Source Locators</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECTS_IMPORT_TASK__SOURCE_LOCATORS = eINSTANCE.getProjectsImportTask_SourceLocators();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl <em>Build Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl
     * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getProjectsBuildTask()
     * @generated
     */
    EClass PROJECTS_BUILD_TASK = eINSTANCE.getProjectsBuildTask();

    /**
     * The meta object literal for the '<em><b>Predicates</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECTS_BUILD_TASK__PREDICATES = eINSTANCE.getProjectsBuildTask_Predicates();

    /**
     * The meta object literal for the '<em><b>Only New Projects</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS = eINSTANCE.getProjectsBuildTask_OnlyNewProjects();

    /**
     * The meta object literal for the '<em><b>Refresh</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECTS_BUILD_TASK__REFRESH = eINSTANCE.getProjectsBuildTask_Refresh();

    /**
     * The meta object literal for the '<em><b>Clean</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECTS_BUILD_TASK__CLEAN = eINSTANCE.getProjectsBuildTask_Clean();

    /**
     * The meta object literal for the '<em><b>Build</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECTS_BUILD_TASK__BUILD = eINSTANCE.getProjectsBuildTask_Build();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.projects.impl.PathVariableTaskImpl <em>Path Variable Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.projects.impl.PathVariableTaskImpl
     * @see org.eclipse.oomph.setup.projects.impl.ProjectsPackageImpl#getPathVariableTask()
     * @generated
     */
    EClass PATH_VARIABLE_TASK = eINSTANCE.getPathVariableTask();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH_VARIABLE_TASK__NAME = eINSTANCE.getPathVariableTask_Name();

    /**
     * The meta object literal for the '<em><b>URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH_VARIABLE_TASK__URI = eINSTANCE.getPathVariableTask_URI();

  }

} // ProjectsPackage

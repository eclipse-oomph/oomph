/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.maven;

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
 * @see org.eclipse.oomph.setup.maven.MavenFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Maven.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.maven.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.maven.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.m2e.p2' repository='http://download.eclipse.org/technology/m2e/milestones/1.5' installableUnits='org.eclipse.m2e.feature.feature.group' releaseTrainAlternate='true'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.webtools.p2' repository='http://download.eclipse.org/webtools/repository/luna' releaseTrainAlternate='true'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.maven.edit/icons/full/obj16'"
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
  String eNAME = "maven";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/maven/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "maven";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MavenPackage eINSTANCE = org.eclipse.oomph.setup.maven.impl.MavenPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl <em>Import Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl
   * @see org.eclipse.oomph.setup.maven.impl.MavenPackageImpl#getMavenImportTask()
   * @generated
   */
  int MAVEN_IMPORT_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Source Locators</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__SOURCE_LOCATORS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Project Name Template</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Profiles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK__PROFILES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Import Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_IMPORT_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl <em>Update Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl
   * @see org.eclipse.oomph.setup.maven.impl.MavenPackageImpl#getMavenUpdateTask()
   * @generated
   */
  int MAVEN_UPDATE_TASK = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Project Name Patterns</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Offline</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__OFFLINE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Update Snapshots</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Update Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAVEN_UPDATE_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.maven.MavenImportTask <em>Import Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import Task</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenImportTask
   * @generated
   */
  EClass getMavenImportTask();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.maven.MavenImportTask#getSourceLocators <em>Source Locators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Source Locators</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenImportTask#getSourceLocators()
   * @see #getMavenImportTask()
   * @generated
   */
  EReference getMavenImportTask_SourceLocators();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.maven.MavenImportTask#getProjectNameTemplate <em>Project Name Template</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Project Name Template</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenImportTask#getProjectNameTemplate()
   * @see #getMavenImportTask()
   * @generated
   */
  EAttribute getMavenImportTask_ProjectNameTemplate();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.setup.maven.MavenImportTask#getProfiles <em>Profiles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Profiles</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenImportTask#getProfiles()
   * @see #getMavenImportTask()
   * @generated
   */
  EAttribute getMavenImportTask_Profiles();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask <em>Update Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Update Task</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenUpdateTask
   * @generated
   */
  EClass getMavenUpdateTask();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#getProjectNamePatterns <em>Project Name Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Project Name Patterns</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenUpdateTask#getProjectNamePatterns()
   * @see #getMavenUpdateTask()
   * @generated
   */
  EAttribute getMavenUpdateTask_ProjectNamePatterns();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isOffline <em>Offline</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Offline</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenUpdateTask#isOffline()
   * @see #getMavenUpdateTask()
   * @generated
   */
  EAttribute getMavenUpdateTask_Offline();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isUpdateSnapshots <em>Update Snapshots</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Update Snapshots</em>'.
   * @see org.eclipse.oomph.setup.maven.MavenUpdateTask#isUpdateSnapshots()
   * @see #getMavenUpdateTask()
   * @generated
   */
  EAttribute getMavenUpdateTask_UpdateSnapshots();

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
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl <em>Import Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl
     * @see org.eclipse.oomph.setup.maven.impl.MavenPackageImpl#getMavenImportTask()
     * @generated
     */
    EClass MAVEN_IMPORT_TASK = eINSTANCE.getMavenImportTask();

    /**
     * The meta object literal for the '<em><b>Source Locators</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MAVEN_IMPORT_TASK__SOURCE_LOCATORS = eINSTANCE.getMavenImportTask_SourceLocators();

    /**
     * The meta object literal for the '<em><b>Project Name Template</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE = eINSTANCE.getMavenImportTask_ProjectNameTemplate();

    /**
     * The meta object literal for the '<em><b>Profiles</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAVEN_IMPORT_TASK__PROFILES = eINSTANCE.getMavenImportTask_Profiles();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl <em>Update Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl
     * @see org.eclipse.oomph.setup.maven.impl.MavenPackageImpl#getMavenUpdateTask()
     * @generated
     */
    EClass MAVEN_UPDATE_TASK = eINSTANCE.getMavenUpdateTask();

    /**
     * The meta object literal for the '<em><b>Project Name Patterns</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS = eINSTANCE.getMavenUpdateTask_ProjectNamePatterns();

    /**
     * The meta object literal for the '<em><b>Offline</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAVEN_UPDATE_TASK__OFFLINE = eINSTANCE.getMavenUpdateTask_Offline();

    /**
     * The meta object literal for the '<em><b>Update Snapshots</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS = eINSTANCE.getMavenUpdateTask_UpdateSnapshots();

  }

} // MavenPackage

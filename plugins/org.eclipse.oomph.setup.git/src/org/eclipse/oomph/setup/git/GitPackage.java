/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.git;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.git.GitFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Git.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.git.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.git.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.egit.p2' repository='http://download.eclipse.org/egit/updates' installableUnits='org.eclipse.egit.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.git.edit/icons/full/obj16'"
 * @generated
 */
public interface GitPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "git";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/git/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "git";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  GitPackage eINSTANCE = org.eclipse.oomph.setup.git.impl.GitPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl <em>Clone Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl
   * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getGitCloneTask()
   * @generated
   */
  int GIT_CLONE_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__LOCATION = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Remote Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__REMOTE_NAME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Remote URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__REMOTE_URI = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Push URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__PUSH_URI = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Checkout Branch</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__CHECKOUT_BRANCH = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Recursive</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__RECURSIVE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Config Sections</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__CONFIG_SECTIONS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Restrict To Checkout Branch</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH = SetupPackage.SETUP_TASK_FEATURE_COUNT + 7;

  /**
   * The number of structural features of the '<em>Clone Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIT_CLONE_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 8;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.git.impl.ConfigSubsectionImpl <em>Config Subsection</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.git.impl.ConfigSubsectionImpl
   * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigSubsection()
   * @generated
   */
  int CONFIG_SUBSECTION = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SUBSECTION__NAME = 0;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SUBSECTION__PROPERTIES = 1;

  /**
   * The number of structural features of the '<em>Config Subsection</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SUBSECTION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.git.impl.ConfigSectionImpl <em>Config Section</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.git.impl.ConfigSectionImpl
   * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigSection()
   * @generated
   */
  int CONFIG_SECTION = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SECTION__NAME = CONFIG_SUBSECTION__NAME;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SECTION__PROPERTIES = CONFIG_SUBSECTION__PROPERTIES;

  /**
   * The feature id for the '<em><b>Subsections</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SECTION__SUBSECTIONS = CONFIG_SUBSECTION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Config Section</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_SECTION_FEATURE_COUNT = CONFIG_SUBSECTION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.git.impl.ConfigPropertyImpl <em>Config Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.git.impl.ConfigPropertyImpl
   * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigProperty()
   * @generated
   */
  int CONFIG_PROPERTY = 3;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_PROPERTY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_PROPERTY__VALUE = 1;

  /**
   * The number of structural features of the '<em>Config Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_PROPERTY_FEATURE_COUNT = 2;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.git.GitCloneTask <em>Clone Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Clone Task</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask
   * @generated
   */
  EClass getGitCloneTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getLocation()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_Location();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteName <em>Remote Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Remote Name</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getRemoteName()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_RemoteName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteURI <em>Remote URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Remote URI</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getRemoteURI()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_RemoteURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#getPushURI <em>Push URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Push URI</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getPushURI()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_PushURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#getCheckoutBranch <em>Checkout Branch</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Checkout Branch</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getCheckoutBranch()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_CheckoutBranch();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#isRecursive <em>Recursive</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Recursive</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#isRecursive()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_Recursive();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.git.GitCloneTask#getConfigSections <em>Config Sections</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Config Sections</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#getConfigSections()
   * @see #getGitCloneTask()
   * @generated
   */
  EReference getGitCloneTask_ConfigSections();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.GitCloneTask#isRestrictToCheckoutBranch <em>Restrict To Checkout Branch</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Restrict To Checkout Branch</em>'.
   * @see org.eclipse.oomph.setup.git.GitCloneTask#isRestrictToCheckoutBranch()
   * @see #getGitCloneTask()
   * @generated
   */
  EAttribute getGitCloneTask_RestrictToCheckoutBranch();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.git.ConfigSection <em>Config Section</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Config Section</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigSection
   * @generated
   */
  EClass getConfigSection();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.git.ConfigSection#getSubsections <em>Subsections</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Subsections</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigSection#getSubsections()
   * @see #getConfigSection()
   * @generated
   */
  EReference getConfigSection_Subsections();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.git.ConfigSubsection <em>Config Subsection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Config Subsection</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigSubsection
   * @generated
   */
  EClass getConfigSubsection();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.ConfigSubsection#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigSubsection#getName()
   * @see #getConfigSubsection()
   * @generated
   */
  EAttribute getConfigSubsection_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.git.ConfigSubsection#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Properties</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigSubsection#getProperties()
   * @see #getConfigSubsection()
   * @generated
   */
  EReference getConfigSubsection_Properties();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.git.ConfigProperty <em>Config Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Config Property</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigProperty
   * @generated
   */
  EClass getConfigProperty();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.ConfigProperty#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigProperty#getKey()
   * @see #getConfigProperty()
   * @generated
   */
  EAttribute getConfigProperty_Key();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.git.ConfigProperty#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.git.ConfigProperty#getValue()
   * @see #getConfigProperty()
   * @generated
   */
  EAttribute getConfigProperty_Value();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  GitFactory getGitFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl <em>Clone Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl
     * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getGitCloneTask()
     * @generated
     */
    EClass GIT_CLONE_TASK = eINSTANCE.getGitCloneTask();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__LOCATION = eINSTANCE.getGitCloneTask_Location();

    /**
     * The meta object literal for the '<em><b>Remote Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__REMOTE_NAME = eINSTANCE.getGitCloneTask_RemoteName();

    /**
     * The meta object literal for the '<em><b>Remote URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__REMOTE_URI = eINSTANCE.getGitCloneTask_RemoteURI();

    /**
     * The meta object literal for the '<em><b>Push URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__PUSH_URI = eINSTANCE.getGitCloneTask_PushURI();

    /**
     * The meta object literal for the '<em><b>Checkout Branch</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__CHECKOUT_BRANCH = eINSTANCE.getGitCloneTask_CheckoutBranch();

    /**
     * The meta object literal for the '<em><b>Recursive</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__RECURSIVE = eINSTANCE.getGitCloneTask_Recursive();

    /**
     * The meta object literal for the '<em><b>Config Sections</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GIT_CLONE_TASK__CONFIG_SECTIONS = eINSTANCE.getGitCloneTask_ConfigSections();

    /**
     * The meta object literal for the '<em><b>Restrict To Checkout Branch</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH = eINSTANCE.getGitCloneTask_RestrictToCheckoutBranch();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.git.impl.ConfigSectionImpl <em>Config Section</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.git.impl.ConfigSectionImpl
     * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigSection()
     * @generated
     */
    EClass CONFIG_SECTION = eINSTANCE.getConfigSection();

    /**
     * The meta object literal for the '<em><b>Subsections</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONFIG_SECTION__SUBSECTIONS = eINSTANCE.getConfigSection_Subsections();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.git.impl.ConfigSubsectionImpl <em>Config Subsection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.git.impl.ConfigSubsectionImpl
     * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigSubsection()
     * @generated
     */
    EClass CONFIG_SUBSECTION = eINSTANCE.getConfigSubsection();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIG_SUBSECTION__NAME = eINSTANCE.getConfigSubsection_Name();

    /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONFIG_SUBSECTION__PROPERTIES = eINSTANCE.getConfigSubsection_Properties();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.git.impl.ConfigPropertyImpl <em>Config Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.git.impl.ConfigPropertyImpl
     * @see org.eclipse.oomph.setup.git.impl.GitPackageImpl#getConfigProperty()
     * @generated
     */
    EClass CONFIG_PROPERTY = eINSTANCE.getConfigProperty();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIG_PROPERTY__KEY = eINSTANCE.getConfigProperty_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONFIG_PROPERTY__VALUE = eINSTANCE.getConfigProperty_Value();

  }

} // GitPackage

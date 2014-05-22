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
package org.eclipse.oomph.setup.workbench;

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
 * @see org.eclipse.oomph.setup.workbench.WorkbenchFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/cdo/org.eclipse.oomph.git/plain/setups/models/Workbench.ecore'"
 * @generated
 */
public interface WorkbenchPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "workbench";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/workbench/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "workbench";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  WorkbenchPackage eINSTANCE = org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.FileAssociationsTaskImpl <em>File Associations Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.FileAssociationsTaskImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileAssociationsTask()
   * @generated
   */
  int FILE_ASSOCIATIONS_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK__MAPPINGS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>File Associations Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_ASSOCIATIONS_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.FileMappingImpl <em>File Mapping</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.FileMappingImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileMapping()
   * @generated
   */
  int FILE_MAPPING = 1;

  /**
   * The feature id for the '<em><b>File Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_MAPPING__FILE_PATTERN = 0;

  /**
   * The feature id for the '<em><b>Default Editor ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_MAPPING__DEFAULT_EDITOR_ID = 1;

  /**
   * The feature id for the '<em><b>Editors</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_MAPPING__EDITORS = 2;

  /**
   * The number of structural features of the '<em>File Mapping</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_MAPPING_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.FileEditorImpl <em>File Editor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.FileEditorImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileEditor()
   * @generated
   */
  int FILE_EDITOR = 2;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_EDITOR__ID = 0;

  /**
   * The number of structural features of the '<em>File Editor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILE_EDITOR_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl <em>Key Binding Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getKeyBindingTask()
   * @generated
   */
  int KEY_BINDING_TASK = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Scheme</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__SCHEME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Contexts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__CONTEXTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Platform</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__PLATFORM = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Locale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__LOCALE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Keys</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__KEYS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Command</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__COMMAND = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Command Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK__COMMAND_PARAMETERS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Key Binding Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingContextImpl <em>Key Binding Context</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.KeyBindingContextImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getKeyBindingContext()
   * @generated
   */
  int KEY_BINDING_CONTEXT = 4;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_CONTEXT__ID = 0;

  /**
   * The number of structural features of the '<em>Key Binding Context</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int KEY_BINDING_CONTEXT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.workbench.impl.CommandParameterImpl <em>Command Parameter</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.workbench.impl.CommandParameterImpl
   * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getCommandParameter()
   * @generated
   */
  int COMMAND_PARAMETER = 5;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMAND_PARAMETER__ID = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMAND_PARAMETER__VALUE = 1;

  /**
   * The number of structural features of the '<em>Command Parameter</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMAND_PARAMETER_FEATURE_COUNT = 2;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.FileAssociationsTask <em>File Associations Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File Associations Task</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileAssociationsTask
   * @generated
   */
  EClass getFileAssociationsTask();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.workbench.FileAssociationsTask#getMappings <em>Mappings</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Mappings</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileAssociationsTask#getMappings()
   * @see #getFileAssociationsTask()
   * @generated
   */
  EReference getFileAssociationsTask_Mappings();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.FileMapping <em>File Mapping</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File Mapping</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileMapping
   * @generated
   */
  EClass getFileMapping();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.FileMapping#getFilePattern <em>File Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>File Pattern</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileMapping#getFilePattern()
   * @see #getFileMapping()
   * @generated
   */
  EAttribute getFileMapping_FilePattern();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.FileMapping#getDefaultEditorID <em>Default Editor ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default Editor ID</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileMapping#getDefaultEditorID()
   * @see #getFileMapping()
   * @generated
   */
  EAttribute getFileMapping_DefaultEditorID();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.workbench.FileMapping#getEditors <em>Editors</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Editors</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileMapping#getEditors()
   * @see #getFileMapping()
   * @generated
   */
  EReference getFileMapping_Editors();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.FileEditor <em>File Editor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File Editor</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileEditor
   * @generated
   */
  EClass getFileEditor();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.FileEditor#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.workbench.FileEditor#getID()
   * @see #getFileEditor()
   * @generated
   */
  EAttribute getFileEditor_ID();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask <em>Key Binding Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Key Binding Task</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask
   * @generated
   */
  EClass getKeyBindingTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getScheme <em>Scheme</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Scheme</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getScheme()
   * @see #getKeyBindingTask()
   * @generated
   */
  EAttribute getKeyBindingTask_Scheme();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getContexts <em>Contexts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Contexts</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getContexts()
   * @see #getKeyBindingTask()
   * @generated
   */
  EReference getKeyBindingTask_Contexts();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getPlatform <em>Platform</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Platform</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getPlatform()
   * @see #getKeyBindingTask()
   * @generated
   */
  EAttribute getKeyBindingTask_Platform();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getLocale <em>Locale</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Locale</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getLocale()
   * @see #getKeyBindingTask()
   * @generated
   */
  EAttribute getKeyBindingTask_Locale();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getKeys <em>Keys</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Keys</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getKeys()
   * @see #getKeyBindingTask()
   * @generated
   */
  EAttribute getKeyBindingTask_Keys();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommand <em>Command</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Command</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommand()
   * @see #getKeyBindingTask()
   * @generated
   */
  EAttribute getKeyBindingTask_Command();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommandParameters <em>Command Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Command Parameters</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommandParameters()
   * @see #getKeyBindingTask()
   * @generated
   */
  EReference getKeyBindingTask_CommandParameters();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.KeyBindingContext <em>Key Binding Context</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Key Binding Context</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingContext
   * @generated
   */
  EClass getKeyBindingContext();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.KeyBindingContext#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.workbench.KeyBindingContext#getID()
   * @see #getKeyBindingContext()
   * @generated
   */
  EAttribute getKeyBindingContext_ID();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.workbench.CommandParameter <em>Command Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Command Parameter</em>'.
   * @see org.eclipse.oomph.setup.workbench.CommandParameter
   * @generated
   */
  EClass getCommandParameter();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.CommandParameter#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.workbench.CommandParameter#getID()
   * @see #getCommandParameter()
   * @generated
   */
  EAttribute getCommandParameter_ID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.workbench.CommandParameter#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.workbench.CommandParameter#getValue()
   * @see #getCommandParameter()
   * @generated
   */
  EAttribute getCommandParameter_Value();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  WorkbenchFactory getWorkbenchFactory();

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
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.FileAssociationsTaskImpl <em>File Associations Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.FileAssociationsTaskImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileAssociationsTask()
     * @generated
     */
    EClass FILE_ASSOCIATIONS_TASK = eINSTANCE.getFileAssociationsTask();

    /**
     * The meta object literal for the '<em><b>Mappings</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILE_ASSOCIATIONS_TASK__MAPPINGS = eINSTANCE.getFileAssociationsTask_Mappings();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.FileMappingImpl <em>File Mapping</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.FileMappingImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileMapping()
     * @generated
     */
    EClass FILE_MAPPING = eINSTANCE.getFileMapping();

    /**
     * The meta object literal for the '<em><b>File Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILE_MAPPING__FILE_PATTERN = eINSTANCE.getFileMapping_FilePattern();

    /**
     * The meta object literal for the '<em><b>Default Editor ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILE_MAPPING__DEFAULT_EDITOR_ID = eINSTANCE.getFileMapping_DefaultEditorID();

    /**
     * The meta object literal for the '<em><b>Editors</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILE_MAPPING__EDITORS = eINSTANCE.getFileMapping_Editors();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.FileEditorImpl <em>File Editor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.FileEditorImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getFileEditor()
     * @generated
     */
    EClass FILE_EDITOR = eINSTANCE.getFileEditor();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILE_EDITOR__ID = eINSTANCE.getFileEditor_ID();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl <em>Key Binding Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getKeyBindingTask()
     * @generated
     */
    EClass KEY_BINDING_TASK = eINSTANCE.getKeyBindingTask();

    /**
     * The meta object literal for the '<em><b>Scheme</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_TASK__SCHEME = eINSTANCE.getKeyBindingTask_Scheme();

    /**
     * The meta object literal for the '<em><b>Contexts</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference KEY_BINDING_TASK__CONTEXTS = eINSTANCE.getKeyBindingTask_Contexts();

    /**
     * The meta object literal for the '<em><b>Platform</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_TASK__PLATFORM = eINSTANCE.getKeyBindingTask_Platform();

    /**
     * The meta object literal for the '<em><b>Locale</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_TASK__LOCALE = eINSTANCE.getKeyBindingTask_Locale();

    /**
     * The meta object literal for the '<em><b>Keys</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_TASK__KEYS = eINSTANCE.getKeyBindingTask_Keys();

    /**
     * The meta object literal for the '<em><b>Command</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_TASK__COMMAND = eINSTANCE.getKeyBindingTask_Command();

    /**
     * The meta object literal for the '<em><b>Command Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference KEY_BINDING_TASK__COMMAND_PARAMETERS = eINSTANCE.getKeyBindingTask_CommandParameters();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingContextImpl <em>Key Binding Context</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.KeyBindingContextImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getKeyBindingContext()
     * @generated
     */
    EClass KEY_BINDING_CONTEXT = eINSTANCE.getKeyBindingContext();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute KEY_BINDING_CONTEXT__ID = eINSTANCE.getKeyBindingContext_ID();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.workbench.impl.CommandParameterImpl <em>Command Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.workbench.impl.CommandParameterImpl
     * @see org.eclipse.oomph.setup.workbench.impl.WorkbenchPackageImpl#getCommandParameter()
     * @generated
     */
    EClass COMMAND_PARAMETER = eINSTANCE.getCommandParameter();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMMAND_PARAMETER__ID = eINSTANCE.getCommandParameter_ID();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMMAND_PARAMETER__VALUE = eINSTANCE.getCommandParameter_Value();

  }

} // WorkbenchPackage

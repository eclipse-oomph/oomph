/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key Binding Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getScheme <em>Scheme</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getContexts <em>Contexts</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getPlatform <em>Platform</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getLocale <em>Locale</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getKeys <em>Keys</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommand <em>Command</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommandParameters <em>Command Parameters</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface KeyBindingTask extends SetupTask
{

  /**
   * Returns the value of the '<em><b>Scheme</b></em>' attribute.
   * The default value is <code>"org.eclipse.ui.defaultAcceleratorConfiguration"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Scheme</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Scheme</em>' attribute.
   * @see #setScheme(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Scheme()
   * @model default="org.eclipse.ui.defaultAcceleratorConfiguration" required="true"
   * @generated
   */
  String getScheme();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getScheme <em>Scheme</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Scheme</em>' attribute.
   * @see #getScheme()
   * @generated
   */
  void setScheme(String value);

  /**
   * Returns the value of the '<em><b>Contexts</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.workbench.KeyBindingContext}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Contexts</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Contexts</em>' containment reference list.
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Contexts()
   * @model containment="true" required="true"
   *        extendedMetaData="name='context'"
   * @generated
   */
  EList<KeyBindingContext> getContexts();

  /**
   * Returns the value of the '<em><b>Platform</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Platform</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Platform</em>' attribute.
   * @see #setPlatform(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Platform()
   * @model
   * @generated
   */
  String getPlatform();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getPlatform <em>Platform</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Platform</em>' attribute.
   * @see #getPlatform()
   * @generated
   */
  void setPlatform(String value);

  /**
   * Returns the value of the '<em><b>Locale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Locale</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Locale</em>' attribute.
   * @see #setLocale(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Locale()
   * @model
   * @generated
   */
  String getLocale();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getLocale <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Locale</em>' attribute.
   * @see #getLocale()
   * @generated
   */
  void setLocale(String value);

  /**
   * Returns the value of the '<em><b>Keys</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Keys</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Keys</em>' attribute.
   * @see #setKeys(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Keys()
   * @model required="true"
   * @generated
   */
  String getKeys();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getKeys <em>Keys</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Keys</em>' attribute.
   * @see #getKeys()
   * @generated
   */
  void setKeys(String value);

  /**
   * Returns the value of the '<em><b>Command</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Command</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Command</em>' attribute.
   * @see #setCommand(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_Command()
   * @model required="true"
   * @generated
   */
  String getCommand();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingTask#getCommand <em>Command</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Command</em>' attribute.
   * @see #getCommand()
   * @generated
   */
  void setCommand(String value);

  /**
   * Returns the value of the '<em><b>Command Parameters</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.workbench.CommandParameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Command Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Command Parameters</em>' containment reference list.
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingTask_CommandParameters()
   * @model containment="true"
   *        extendedMetaData="name='commandParameter'"
   * @generated
   */
  EList<CommandParameter> getCommandParameters();
} // KeyBindingTask

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
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getScopeType <em>Scope Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getExcludedTriggers <em>Excluded Triggers</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#isDisabled <em>Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getPredecessors <em>Predecessors</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getSuccessors <em>Successors</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getRestrictions <em>Restrictions</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.SetupTask#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask()
 * @model abstract="true"
 * @generated
 */
public interface SetupTask extends ModelElement
{
  public static final int PRIORITY_REDIRECTION = 100;

  public static final int PRIORITY_INSTALLATION = 200;

  public static final int PRIORITY_CONFIGURATION = 300;

  public static final int PRIORITY_EARLY = 400;

  public static final int PRIORITY_DEFAULT = 500;

  public static final int PRIORITY_LATE = 600;

  /**
   * Returns the value of the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>ID</em>' attribute.
   * @see #setID(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_ID()
   * @model id="true" dataType="org.eclipse.oomph.base.ID"
   *        extendedMetaData="kind='attribute' name='id'"
   * @generated
   */
  String getID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.SetupTask#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ID</em>' attribute.
   * @see #getID()
   * @generated
   */
  void setID(String value);

  /**
   * Returns the value of the '<em><b>Predecessors</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.SetupTask}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predecessors</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Predecessors()
   * @model extendedMetaData="name='predecessor'"
   * @generated
   */
  EList<SetupTask> getPredecessors();

  /**
   * Returns the value of the '<em><b>Successors</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.SetupTask}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Successors</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Successors</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Successors()
   * @model extendedMetaData="name='successor'"
   * @generated
   */
  EList<SetupTask> getSuccessors();

  /**
   * Returns the value of the '<em><b>Restrictions</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Scope}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Restrictions</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Restrictions</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Restrictions()
   * @model extendedMetaData="name='restriction'"
   * @generated
   */
  EList<Scope> getRestrictions();

  /**
   * Returns the value of the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filter</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filter</em>' attribute.
   * @see #setFilter(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Filter()
   * @model dataType="org.eclipse.oomph.setup.Filter"
   * @generated
   */
  String getFilter();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.SetupTask#getFilter <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filter</em>' attribute.
   * @see #getFilter()
   * @generated
   */
  void setFilter(String value);

  /**
   * Returns the value of the '<em><b>Scope Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.ScopeType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Scope</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Scope Type</em>' attribute.
   * @see org.eclipse.oomph.setup.ScopeType
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_ScopeType()
   * @model transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  ScopeType getScopeType();

  /**
   * Returns the value of the '<em><b>Excluded Triggers</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Excluded Triggers</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Excluded Triggers</em>' attribute.
   * @see #setExcludedTriggers(Set)
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_ExcludedTriggers()
   * @model default="" dataType="org.eclipse.oomph.setup.TriggerSet" required="true"
   * @generated
   */
  Set<Trigger> getExcludedTriggers();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.SetupTask#getExcludedTriggers <em>Excluded Triggers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Excluded Triggers</em>' attribute.
   * @see #getExcludedTriggers()
   * @generated
   */
  void setExcludedTriggers(Set<Trigger> value);

  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Documentation</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see #setDescription(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Description()
   * @model dataType="org.eclipse.oomph.base.Text"
   *        extendedMetaData="kind='element'"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.SetupTask#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  Scope getScope();

  /**
   * Returns the value of the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Disabled</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Disabled</em>' attribute.
   * @see #setDisabled(boolean)
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTask_Disabled()
   * @model
   * @generated
   */
  boolean isDisabled();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.SetupTask#isDisabled <em>Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Disabled</em>' attribute.
   * @see #isDisabled()
   * @generated
   */
  void setDisabled(boolean value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  boolean requires(SetupTask setupTask);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation" dataType="org.eclipse.oomph.setup.TriggerSet" required="true"
   * @generated
   */
  Set<Trigger> getValidTriggers();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation" dataType="org.eclipse.oomph.setup.TriggerSet" required="true"
   * @generated
   */
  Set<Trigger> getTriggers();

  int getPriority();

  Object getOverrideToken();

  void overrideFor(SetupTask overriddenTask);

  void consolidate();

  int getProgressMonitorWork();

  boolean isNeeded(SetupTaskContext context) throws Exception;

  void perform(SetupTaskContext context) throws Exception;

  void dispose();

} // SetupTask

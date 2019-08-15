/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getStorageURI <em>Storage URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getChoices <em>Choices</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask()
 * @model features="storePromptedValue"
 *        storePromptedValueDefault="true" storePromptedValueDataType="org.eclipse.emf.ecore.EBoolean" storePromptedValueTransient="true" storePromptedValueVolatile="true" storePromptedValueDerived="true" storePromptedValueSuppressedGetVisibility="true" storePromptedValueSuppressedSetVisibility="true"
 *        storePromptedValueAnnotation="http://www.eclipse.org/oomph/setup/NoExpand"
 * @generated
 */
public interface VariableTask extends SetupTask
{
  URI DEFAULT_STORAGE_URI = URI.createURI("scope://");

  URI WORKSPACE_STORAGE_URI = URI.createURI("scope://Workspace");

  URI INSTALLATION_STORAGE_URI = URI.createURI("scope://Installation");

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The default value is <code>"STRING"</code>.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.VariableType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.VariableType
   * @see #setType(VariableType)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Type()
   * @model default="STRING" required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/NoExpand"
   * @generated
   */
  VariableType getType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.VariableType
   * @see #getType()
   * @generated
   */
  void setType(VariableType value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Default Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Value</em>' attribute.
   * @see #setDefaultValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_DefaultValue()
   * @model
   * @generated
   */
  String getDefaultValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getDefaultValue <em>Default Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Value</em>' attribute.
   * @see #getDefaultValue()
   * @generated
   */
  void setDefaultValue(String value);

  /**
   * Returns the value of the '<em><b>Storage URI</b></em>' attribute.
   * The default value is <code>"scope://"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Storage URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Storage URI</em>' attribute.
   * @see #setStorageURI(URI)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_StorageURI()
   * @model default="scope://" dataType="org.eclipse.oomph.base.URI"
   *        annotation="http://www.eclipse.org/oomph/setup/NoExpand"
   * @generated
   */
  URI getStorageURI();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getStorageURI <em>Storage URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Storage URI</em>' attribute.
   * @see #getStorageURI()
   * @generated
   */
  void setStorageURI(URI value);

  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Label</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Choices</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.VariableChoice}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Choices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Choices</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Choices()
   * @model containment="true" resolveProxies="true"
   *        extendedMetaData="name='choice'"
   * @generated
   */
  EList<VariableChoice> getChoices();

} // VariableTask

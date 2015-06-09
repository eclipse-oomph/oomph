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
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scope</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Scope#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Scope#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Scope#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Scope#getQualifiedName <em>Qualified Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getScope()
 * @model abstract="true"
 * @generated
 */
public interface Scope extends SetupTaskContainer
{
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
   * @see org.eclipse.oomph.setup.SetupPackage#getScope_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Scope#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

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
   * @see org.eclipse.oomph.setup.SetupPackage#getScope_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Scope#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Description</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see #setDescription(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getScope_Description()
   * @model dataType="org.eclipse.oomph.base.Text"
   *        extendedMetaData="kind='element'"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Scope#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Qualified Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Qualified Name</em>' attribute.
   * @see org.eclipse.oomph.setup.SetupPackage#getScope_QualifiedName()
   * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  String getQualifiedName();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  Scope getParentScope();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation" required="true"
   * @generated
   */
  ScopeType getType();

} // Scope

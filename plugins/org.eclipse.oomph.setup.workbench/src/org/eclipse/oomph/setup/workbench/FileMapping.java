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

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.FileMapping#getFilePattern <em>File Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.FileMapping#getDefaultEditorID <em>Default Editor ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.FileMapping#getEditors <em>Editors</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getFileMapping()
 * @model
 * @generated
 */
public interface FileMapping extends ModelElement
{
  /**
   * Returns the value of the '<em><b>File Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>File Pattern</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>File Pattern</em>' attribute.
   * @see #setFilePattern(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getFileMapping_FilePattern()
   * @model required="true"
   * @generated
   */
  String getFilePattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.FileMapping#getFilePattern <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>File Pattern</em>' attribute.
   * @see #getFilePattern()
   * @generated
   */
  void setFilePattern(String value);

  /**
   * Returns the value of the '<em><b>Default Editor ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Editor ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Editor ID</em>' attribute.
   * @see #setDefaultEditorID(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getFileMapping_DefaultEditorID()
   * @model
   * @generated
   */
  String getDefaultEditorID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.FileMapping#getDefaultEditorID <em>Default Editor ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Editor ID</em>' attribute.
   * @see #getDefaultEditorID()
   * @generated
   */
  void setDefaultEditorID(String value);

  /**
   * Returns the value of the '<em><b>Editors</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.workbench.FileEditor}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Editors</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Editors</em>' containment reference list.
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getFileMapping_Editors()
   * @model containment="true"
   *        extendedMetaData="name='editor'"
   * @generated
   */
  EList<FileEditor> getEditors();

} // FileMapping

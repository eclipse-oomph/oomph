/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Modify Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.TextModifyTask#getURL <em>URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.TextModifyTask#getModifications <em>Modifications</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.TextModifyTask#getEncoding <em>Encoding</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getTextModifyTask()
 * @model
 * @generated
 */
public interface TextModifyTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>URL</em>' attribute.
   * @see #setURL(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getTextModifyTask_URL()
   * @model required="true"
   *        extendedMetaData="kind='attribute' name='url'"
   * @generated
   */
  String getURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.TextModifyTask#getURL <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>URL</em>' attribute.
   * @see #getURL()
   * @generated
   */
  void setURL(String value);

  /**
   * Returns the value of the '<em><b>Modifications</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.TextModification}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Modifications</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Modifications</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getTextModifyTask_Modifications()
   * @model containment="true" resolveProxies="true"
   *        extendedMetaData="name='modification'"
   * @generated
   */
  EList<TextModification> getModifications();

  /**
   * Returns the value of the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Encoding</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Encoding</em>' attribute.
   * @see #setEncoding(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getTextModifyTask_Encoding()
   * @model
   * @generated
   */
  String getEncoding();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.TextModifyTask#getEncoding <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Encoding</em>' attribute.
   * @see #getEncoding()
   * @generated
   */
  void setEncoding(String value);

} // TextModifyTask

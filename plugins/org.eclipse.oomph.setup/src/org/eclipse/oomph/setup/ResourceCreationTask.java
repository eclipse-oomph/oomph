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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Creation Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.ResourceCreationTask#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.ResourceCreationTask#getTargetURL <em>Target URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.ResourceCreationTask#getEncoding <em>Encoding</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getResourceCreationTask()
 * @model
 * @generated
 */
public interface ResourceCreationTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Content</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Content</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Content</em>' attribute.
   * @see #setContent(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getResourceCreationTask_Content()
   * @model required="true"
   * @generated
   */
  String getContent();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.ResourceCreationTask#getContent <em>Content</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Content</em>' attribute.
   * @see #getContent()
   * @generated
   */
  void setContent(String value);

  /**
   * Returns the value of the '<em><b>Target URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target URL</em>' attribute.
   * @see #setTargetURL(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getResourceCreationTask_TargetURL()
   * @model required="true"
   * @generated
   */
  String getTargetURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.ResourceCreationTask#getTargetURL <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target URL</em>' attribute.
   * @see #getTargetURL()
   * @generated
   */
  void setTargetURL(String value);

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
   * @see org.eclipse.oomph.setup.SetupPackage#getResourceCreationTask_Encoding()
   * @model
   * @generated
   */
  String getEncoding();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.ResourceCreationTask#getEncoding <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Encoding</em>' attribute.
   * @see #getEncoding()
   * @generated
   */
  void setEncoding(String value);

} // ResourceCreationTask

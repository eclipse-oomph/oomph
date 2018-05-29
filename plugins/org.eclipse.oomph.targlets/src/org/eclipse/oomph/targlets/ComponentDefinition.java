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
package org.eclipse.oomph.targlets;

import org.eclipse.equinox.p2.metadata.Version;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.ComponentDefinition#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.ComponentDefinition#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getComponentDefinition()
 * @model
 * @generated
 */
public interface ComponentDefinition extends ComponentExtension
{
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
   * @see org.eclipse.oomph.targlets.TargletPackage#getComponentDefinition_ID()
   * @model required="true"
   *        extendedMetaData="kind='attribute' name='id'"
   * @generated
   */
  String getID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.ComponentDefinition#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ID</em>' attribute.
   * @see #getID()
   * @generated
   */
  void setID(String value);

  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * The default value is <code>"1.0.0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #setVersion(Version)
   * @see org.eclipse.oomph.targlets.TargletPackage#getComponentDefinition_Version()
   * @model default="1.0.0" dataType="org.eclipse.oomph.p2.Version"
   * @generated
   */
  Version getVersion();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.ComponentDefinition#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #getVersion()
   * @generated
   */
  void setVersion(Version value);

} // ComponentDefinition

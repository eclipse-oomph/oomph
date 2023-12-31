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
package org.eclipse.oomph.targlets;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.TargletContainer#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.TargletContainer#getComposedTargets <em>Composed Targets</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.TargletContainer#getTarglets <em>Targlets</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getTargletContainer()
 * @model
 * @generated
 */
public interface TargletContainer extends ModelElement
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
   * @see org.eclipse.oomph.targlets.TargletPackage#getTargletContainer_ID()
   * @model id="true"
   *        extendedMetaData="kind='attribute' name='id'"
   *        annotation="http://www.eclipse.org/oomph/setup/NoExpand"
   * @generated
   */
  String getID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.TargletContainer#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ID</em>' attribute.
   * @see #getID()
   * @generated
   */
  void setID(String value);

  /**
   * Returns the value of the '<em><b>Composed Targets</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The names of other target definitions to be composed with the target container's targlets
   * <!-- end-model-doc -->
   * @return the value of the '<em>Composed Targets</em>' attribute list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTargletContainer_ComposedTargets()
   * @model extendedMetaData="kind='element' name='composedTarget'"
   * @generated
   */
  EList<String> getComposedTargets();

  /**
   * Returns the value of the '<em><b>Targlets</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.targlets.Targlet}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Targlets</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Targlets</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTargletContainer_Targlets()
   * @model containment="true"
   *        extendedMetaData="name='targlet'"
   * @generated
   */
  EList<Targlet> getTarglets();

} // TargletContainer

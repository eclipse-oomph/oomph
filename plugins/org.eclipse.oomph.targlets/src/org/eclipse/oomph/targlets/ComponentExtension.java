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
import org.eclipse.oomph.p2.Requirement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Extension</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.ComponentExtension#getRequirements <em>Requirements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getComponentExtension()
 * @model
 * @generated
 */
public interface ComponentExtension extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Requirement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Requirements</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getComponentExtension_Requirements()
   * @model containment="true"
   *        extendedMetaData="name='requirement'"
   * @generated
   */
  EList<Requirement> getRequirements();

} // ComponentExtension

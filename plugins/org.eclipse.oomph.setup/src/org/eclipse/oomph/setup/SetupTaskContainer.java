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

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.SetupTaskContainer#getSetupTasks <em>Setup Tasks</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getSetupTaskContainer()
 * @model abstract="true"
 * @generated
 */
public interface SetupTaskContainer extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Setup Tasks</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.SetupTask}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Setup Tasks</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Setup Tasks</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getSetupTaskContainer_SetupTasks()
   * @model containment="true" resolveProxies="true"
   *        extendedMetaData="name='setupTask'"
   * @generated
   */
  EList<SetupTask> getSetupTasks();

} // SetupTaskContainer

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
package org.eclipse.oomph.workingsets;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Working Set Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.workingsets.WorkingSetGroup#getWorkingSets <em>Working Sets</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.workingsets.WorkingSetsPackage#getWorkingSetGroup()
 * @model
 * @generated
 */
public interface WorkingSetGroup extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Working Sets</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.workingsets.WorkingSet}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Working Sets</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Working Sets</em>' containment reference list.
   * @see org.eclipse.oomph.workingsets.WorkingSetsPackage#getWorkingSetGroup_WorkingSets()
   * @model containment="true" keys="name"
   *        extendedMetaData="name='workingSet'"
   * @generated
   */
  EList<WorkingSet> getWorkingSets();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  WorkingSet getWorkingSet(String name);

} // WorkingSetGroup

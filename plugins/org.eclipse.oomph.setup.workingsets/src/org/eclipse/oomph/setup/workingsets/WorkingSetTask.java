/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workingsets;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.workingsets.WorkingSet;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Working Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workingsets.WorkingSetTask#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workingsets.WorkingSetTask#getWorkingSets <em>Working Sets</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage#getWorkingSetTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider text='Working Sets'"
 * @generated
 */
public interface WorkingSetTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prefix</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prefix</em>' attribute.
   * @see #setPrefix(String)
   * @see org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage#getWorkingSetTask_Prefix()
   * @model
   * @generated
   */
  String getPrefix();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workingsets.WorkingSetTask#getPrefix <em>Prefix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prefix</em>' attribute.
   * @see #getPrefix()
   * @generated
   */
  void setPrefix(String value);

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
   * @see org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage#getWorkingSetTask_WorkingSets()
   * @model containment="true"
   *        extendedMetaData="name='workingSet'"
   * @generated
   */
  EList<WorkingSet> getWorkingSets();

} // SetWorkingTask

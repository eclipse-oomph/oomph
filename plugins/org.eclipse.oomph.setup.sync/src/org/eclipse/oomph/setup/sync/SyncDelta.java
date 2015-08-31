/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delta</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncDelta#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncDelta#getOldTask <em>Old Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncDelta#getNewTask <em>New Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncDelta#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta()
 * @model
 * @generated
 */
public interface SyncDelta extends EObject
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
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta_ID()
   * @model id="true" required="true" suppressedSetVisibility="true"
   * @generated
   */
  String getID();

  /**
   * Returns the value of the '<em><b>Old Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Old Task</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Old Task</em>' reference.
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta_OldTask()
   * @model suppressedSetVisibility="true"
   * @generated
   */
  SetupTask getOldTask();

  /**
   * Returns the value of the '<em><b>New Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>New Task</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>New Task</em>' reference.
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta_NewTask()
   * @model suppressedSetVisibility="true"
   * @generated
   */
  SetupTask getNewTask();

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncDeltaType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncDeltaType
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncDelta_Type()
   * @model suppressedSetVisibility="true"
   * @generated
   */
  SyncDeltaType getType();

} // SyncDelta

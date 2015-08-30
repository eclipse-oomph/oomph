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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getLocalDelta <em>Local Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getRemoteDelta <em>Remote Delta</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getComputedType <em>Computed Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getResolvedType <em>Resolved Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.sync.SyncAction#getEffectiveType <em>Effective Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction()
 * @model
 * @generated
 */
public interface SyncAction extends EObject
{
  /**
   * Returns the value of the '<em><b>Local Delta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Local Delta</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Local Delta</em>' reference.
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_LocalDelta()
   * @model suppressedSetVisibility="true"
   * @generated
   */
  SyncDelta getLocalDelta();

  /**
   * Returns the value of the '<em><b>Remote Delta</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote Delta</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote Delta</em>' reference.
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_RemoteDelta()
   * @model suppressedSetVisibility="true"
   * @generated
   */
  SyncDelta getRemoteDelta();

  /**
   * Returns the value of the '<em><b>Computed Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncActionType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Computed Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Computed Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_ComputedType()
   * @model required="true" suppressedSetVisibility="true"
   * @generated
   */
  SyncActionType getComputedType();

  /**
   * Returns the value of the '<em><b>Resolved Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncActionType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Resolved Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #setResolvedType(SyncActionType)
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_ResolvedType()
   * @model
   * @generated
   */
  SyncActionType getResolvedType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.sync.SyncAction#getResolvedType <em>Resolved Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see #getResolvedType()
   * @generated
   */
  void setResolvedType(SyncActionType value);

  /**
   * Returns the value of the '<em><b>Effective Type</b></em>' attribute.
   * The default value is <code>"None"</code>.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.sync.SyncActionType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Effective Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Effective Type</em>' attribute.
   * @see org.eclipse.oomph.setup.sync.SyncActionType
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncAction_EffectiveType()
   * @model default="None" required="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  SyncActionType getEffectiveType();

} // SyncAction

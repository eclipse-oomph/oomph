/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.oomph.setup.SetupTaskContainer;

import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Snapshot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.sync.RemoteData#getPolicies <em>Policies</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getRemoteData()
 * @model
 * @generated
 */
public interface RemoteData extends SetupTaskContainer
{
  /**
   * Returns the value of the '<em><b>Policies</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link org.eclipse.oomph.setup.sync.SyncPolicy},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Policies</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Policies</em>' map.
   * @see org.eclipse.oomph.setup.sync.SyncPackage#getRemoteData_Policies()
   * @model mapType="org.eclipse.oomph.setup.sync.StringToSyncPolicyMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.oomph.setup.sync.SyncPolicy>"
   *        extendedMetaData="name='policy'"
   * @generated
   */
  EMap<String, SyncPolicy> getPolicies();

} // RemoteSnapshot

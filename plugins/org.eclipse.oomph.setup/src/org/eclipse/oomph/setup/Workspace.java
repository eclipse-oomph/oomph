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
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Workspace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Workspace#getStreams <em>Streams</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getWorkspace()
 * @model
 * @generated
 */
public interface Workspace extends Scope
{
  /**
   * Returns the value of the '<em><b>Streams</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Stream}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Branches</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Streams</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getWorkspace_Streams()
   * @model extendedMetaData="name='stream'"
   * @generated
   */
  EList<Stream> getStreams();

} // Workspace

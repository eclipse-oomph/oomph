/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Configuration#getInstallation <em>Installation</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Configuration#getWorkspace <em>Workspace</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getConfiguration()
 * @model
 * @generated
 */
public interface Configuration extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Installation</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Installation</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Installation</em>' containment reference.
   * @see #setInstallation(Installation)
   * @see org.eclipse.oomph.setup.SetupPackage#getConfiguration_Installation()
   * @model containment="true" resolveProxies="true"
   * @generated
   */
  Installation getInstallation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Configuration#getInstallation <em>Installation</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Installation</em>' containment reference.
   * @see #getInstallation()
   * @generated
   */
  void setInstallation(Installation value);

  /**
   * Returns the value of the '<em><b>Workspace</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Workspace</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Workspace</em>' containment reference.
   * @see #setWorkspace(Workspace)
   * @see org.eclipse.oomph.setup.SetupPackage#getConfiguration_Workspace()
   * @model containment="true" resolveProxies="true"
   * @generated
   */
  Workspace getWorkspace();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Configuration#getWorkspace <em>Workspace</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Workspace</em>' containment reference.
   * @see #getWorkspace()
   * @generated
   */
  void setWorkspace(Workspace value);

} // Configuration

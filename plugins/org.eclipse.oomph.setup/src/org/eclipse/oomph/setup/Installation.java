/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Installation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Installation#getProductVersion <em>Product Version</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getInstallation()
 * @model
 * @generated
 */
public interface Installation extends Scope
{
  /**
   * Returns the value of the '<em><b>Product Version</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Version</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Product Version</em>' reference.
   * @see #setProductVersion(ProductVersion)
   * @see org.eclipse.oomph.setup.SetupPackage#getInstallation_ProductVersion()
   * @model required="true"
   * @generated
   */
  ProductVersion getProductVersion();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Installation#getProductVersion <em>Product Version</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Product Version</em>' reference.
   * @see #getProductVersion()
   * @generated
   */
  void setProductVersion(ProductVersion value);

} // Installation

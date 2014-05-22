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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Preferences</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.User#getAttributeRules <em>Attribute Rules</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.User#getAcceptedLicenses <em>Accepted Licenses</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getUser()
 * @model
 * @generated
 */
public interface User extends Scope
{
  /**
   * Returns the value of the '<em><b>Accepted Licenses</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.oomph.setup.LicenseInfo}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Accepted Licenses</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Accepted Licenses</em>' attribute list.
   * @see org.eclipse.oomph.setup.SetupPackage#getUser_AcceptedLicenses()
   * @model dataType="org.eclipse.oomph.setup.LicenseInfo"
   *        extendedMetaData="name='acceptedLicense'"
   * @generated
   */
  EList<LicenseInfo> getAcceptedLicenses();

  /**
   * Returns the value of the '<em><b>Attribute Rules</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.AttributeRule}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attribute Rules</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attribute Rules</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getUser_AttributeRules()
   * @model containment="true" resolveProxies="true" keys="attributeURI"
   *        extendedMetaData="name='attributeRule'"
   * @generated
   */
  EList<AttributeRule> getAttributeRules();

} // Preferences

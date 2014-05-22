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
package org.eclipse.oomph.setup.p2;

import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Install Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.p2.P2Task#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.P2Task#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.P2Task#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.P2Task#isMergeDisabled <em>Merge Disabled</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.p2.SetupP2Package#getP2Task()
 * @model
 * @generated
 */
public interface P2Task extends SetupTask
{
  public static final String PROP_SKIP = "org.eclipse.oomph.setup.p2.skip";

  /**
   * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Requirement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Requirements</em>' containment reference list.
   * @see org.eclipse.oomph.setup.p2.SetupP2Package#getP2Task_Requirements()
   * @model containment="true"
   * @generated
   */
  EList<Requirement> getRequirements();

  /**
   * Returns the value of the '<em><b>Repositories</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Repository}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repositories</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repositories</em>' containment reference list.
   * @see org.eclipse.oomph.setup.p2.SetupP2Package#getP2Task_Repositories()
   * @model containment="true"
   * @generated
   */
  EList<Repository> getRepositories();

  /**
   * Returns the value of the '<em><b>License Confirmation Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Disable License Confirmation</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>License Confirmation Disabled</em>' attribute.
   * @see #setLicenseConfirmationDisabled(boolean)
   * @see org.eclipse.oomph.setup.p2.SetupP2Package#getP2Task_LicenseConfirmationDisabled()
   * @model
   * @generated
   */
  boolean isLicenseConfirmationDisabled();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.p2.P2Task#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>License Confirmation Disabled</em>' attribute.
   * @see #isLicenseConfirmationDisabled()
   * @generated
   */
  void setLicenseConfirmationDisabled(boolean value);

  /**
   * Returns the value of the '<em><b>Merge Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Merge Disabled</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Merge Disabled</em>' attribute.
   * @see #setMergeDisabled(boolean)
   * @see org.eclipse.oomph.setup.p2.SetupP2Package#getP2Task_MergeDisabled()
   * @model
   * @generated
   */
  boolean isMergeDisabled();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.p2.P2Task#isMergeDisabled <em>Merge Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Merge Disabled</em>' attribute.
   * @see #isMergeDisabled()
   * @generated
   */
  void setMergeDisabled(boolean value);

} // InstallTask

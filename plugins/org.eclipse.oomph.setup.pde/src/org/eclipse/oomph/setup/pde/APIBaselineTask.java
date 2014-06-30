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
package org.eclipse.oomph.setup.pde;

import org.eclipse.oomph.setup.SetupTask;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Api Baseline Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getRemoteURI <em>Remote URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 *        annotation="http://www.eclipse.org/oomph/setup/Icon uri='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.pde.edit/icons/full/obj16/APIBaselineTask.gif'"
 * @generated
 */
public interface APIBaselineTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #setVersion(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_Version()
   * @model required="true"
   * @generated
   */
  String getVersion();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #getVersion()
   * @generated
   */
  void setVersion(String value);

  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see #setLocation(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_Location()
   * @model default="" required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/Variable type='STRING' label='API baseline location rule' description='The rule for the absolute folder location where the API baseline is located' explicitType='FOLDER' explicitLabel='${@id.name}-${@id.version} API baseline location' explicitDescription='The absolute folder location where the ${@id.name}-${@id.version} API baseline is located'"
   *        annotation="http://www.eclipse.org/oomph/setup/RuleVariable name='api.baselines.root' type='FOLDER' label='Root API baselines folder' description='The root API baselines folder where all the API baselines are located' storePromptedValue='true'"
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

  /**
   * Returns the value of the '<em><b>Remote URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote URI</em>' attribute.
   * @see #setRemoteURI(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_RemoteURI()
   * @model required="true"
   * @generated
   */
  String getRemoteURI();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getRemoteURI <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote URI</em>' attribute.
   * @see #getRemoteURI()
   * @generated
   */
  void setRemoteURI(String value);

} // ApiBaselineTask

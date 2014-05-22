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
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getContainerFolder <em>Container Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getZipLocation <em>Zip Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.pde.p2' repository='${releng.url}' installableUnits='org.eclipse.oomph.setup.pde.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface APIBaselineTask extends SetupTask
{
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
   * Returns the value of the '<em><b>Container Folder</b></em>' attribute.
   * The default value is <code>"${setup.project.dir/.baselines}"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Container Folder</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Container Folder</em>' attribute.
   * @see #setContainerFolder(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_ContainerFolder()
   * @model default="${setup.project.dir/.baselines}" required="true"
   * @generated
   */
  String getContainerFolder();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getContainerFolder <em>Container Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Container Folder</em>' attribute.
   * @see #getContainerFolder()
   * @generated
   */
  void setContainerFolder(String value);

  /**
   * Returns the value of the '<em><b>Zip Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Zip Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Zip Location</em>' attribute.
   * @see #setZipLocation(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineTask_ZipLocation()
   * @model required="true"
   * @generated
   */
  String getZipLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineTask#getZipLocation <em>Zip Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Zip Location</em>' attribute.
   * @see #getZipLocation()
   * @generated
   */
  void setZipLocation(String value);

} // ApiBaselineTask

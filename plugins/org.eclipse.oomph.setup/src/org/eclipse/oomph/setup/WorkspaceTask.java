/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Workspace Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.WorkspaceTask#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getWorkspaceTask()
 * @model
 * @generated
 */
public interface WorkspaceTask extends SetupTask
{
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
   * @see org.eclipse.oomph.setup.SetupPackage#getWorkspaceTask_Location()
   * @model default="" required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/Variable filter='canonical' type='STRING' label='Workspace location rule' description='The rule for the absolute folder location where the workspace is located' storageURI='null' explicitType='FOLDER' explicitLabel='Workspace location' explicitDescription='The absolute folder location where the workspace is located'"
   *        annotation="http://www.eclipse.org/oomph/setup/RuleVariable name='workspace.container.root' type='FOLDER' label='Root workspace-container folder' defaultValue='${user.home}' description='The root workspace-container folder where all the workspaces are located' storageURI='scope://'"
   *        annotation="http://www.eclipse.org/oomph/setup/RuleVariable name='workspace.id' type='STRING' label='Workspace folder name' defaultValue='${scope.project.name|workspaceID}' description='The name of the workspace folder within the root workspace-container folder where the workspaces are located'"
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.WorkspaceTask#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

} // WorkspaceTask

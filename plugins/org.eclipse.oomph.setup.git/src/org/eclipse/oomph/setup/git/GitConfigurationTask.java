/**
 * Copyright (c) Eclispe contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.git;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.GitConfigurationTask#getRemoteURIPattern <em>Remote URI Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitConfigurationTask#getConfigSections <em>Config Sections</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.git.GitPackage#getGitConfigurationTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface GitConfigurationTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Remote URI Pattern</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote URI Pattern</em>' attribute.
   * @see #setRemoteURIPattern(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitConfigurationTask_RemoteURIPattern()
   * @model default="" required="true"
   * @generated
   */
  String getRemoteURIPattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitConfigurationTask#getRemoteURIPattern <em>Remote URI Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote URI Pattern</em>' attribute.
   * @see #getRemoteURIPattern()
   * @generated
   */
  void setRemoteURIPattern(String value);

  /**
   * Returns the value of the '<em><b>Config Sections</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.git.ConfigSection}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Config Sections</em>' containment reference list.
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitConfigurationTask_ConfigSections()
   * @model containment="true"
   * @generated
   */
  EList<ConfigSection> getConfigSections();

} // GitConfigurationTask

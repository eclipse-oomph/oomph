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
package org.eclipse.oomph.setup.projectset;

import org.eclipse.oomph.setup.SetupTask;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Set Import Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projectset.ProjectSetImportTask#getURL <em>URL</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.projectset.ProjectSetPackage#getProjectSetImportTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.projectset.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.projectset.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface ProjectSetImportTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>URL</em>' attribute.
   * @see #setURL(String)
   * @see org.eclipse.oomph.setup.projectset.ProjectSetPackage#getProjectSetImportTask_URL()
   * @model required="true"
   * @generated
   */
  String getURL();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projectset.ProjectSetImportTask#getURL <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>URL</em>' attribute.
   * @see #getURL()
   * @generated
   */
  void setURL(String value);

} // ProjectSetImportTask

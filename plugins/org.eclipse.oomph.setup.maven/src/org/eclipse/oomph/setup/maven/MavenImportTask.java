/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.maven;

import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Maven Import Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenImportTask#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenImportTask#getProjectNameTemplate <em>Project Name Template</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenImportTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface MavenImportTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Source Locators</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.resources.SourceLocator}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source Locators</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source Locators</em>' containment reference list.
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenImportTask_SourceLocators()
   * @model containment="true" required="true"
   *        extendedMetaData="name='sourceLocator'"
   * @generated
   */
  EList<SourceLocator> getSourceLocators();

  /**
   * Returns the value of the '<em><b>Project Name Template</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Name Template</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Name Template</em>' attribute.
   * @see #setProjectNameTemplate(String)
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenImportTask_ProjectNameTemplate()
   * @model
   * @generated
   */
  String getProjectNameTemplate();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.maven.MavenImportTask#getProjectNameTemplate <em>Project Name Template</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Project Name Template</em>' attribute.
   * @see #getProjectNameTemplate()
   * @generated
   */
  void setProjectNameTemplate(String value);

} // MavenImportTask

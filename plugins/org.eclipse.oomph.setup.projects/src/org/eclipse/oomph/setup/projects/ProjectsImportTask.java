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
package org.eclipse.oomph.setup.projects;

import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Import Project Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#isForce <em>Force</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#getSourceLocators <em>Source Locators</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsImportTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface ProjectsImportTask extends SetupTask
{

  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsImportTask_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Force</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Force</em>' attribute.
   * @see #setForce(boolean)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsImportTask_Force()
   * @model
   * @generated
   */
  boolean isForce();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsImportTask#isForce <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Force</em>' attribute.
   * @see #isForce()
   * @generated
   */
  void setForce(boolean value);

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
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsImportTask_SourceLocators()
   * @model containment="true" required="true"
   *        extendedMetaData="name='sourceLocator'"
   * @generated
   */
  EList<SourceLocator> getSourceLocators();
} // ProjectsImportTask

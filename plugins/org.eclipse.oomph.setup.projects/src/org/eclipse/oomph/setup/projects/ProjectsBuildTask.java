/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projects;

import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Build Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isOnlyNewProjects <em>Only New Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isRefresh <em>Refresh</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isClean <em>Clean</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isBuild <em>Build</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface ProjectsBuildTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Predicates</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.predicates.Predicate}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Predicates</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predicates</em>' containment reference list.
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask_Predicates()
   * @model containment="true"
   *        extendedMetaData="name='predicate'"
   * @generated
   */
  EList<Predicate> getPredicates();

  /**
   * Returns the value of the '<em><b>Only New Projects</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Only New Projects</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Only New Projects</em>' attribute.
   * @see #setOnlyNewProjects(boolean)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask_OnlyNewProjects()
   * @model
   * @generated
   */
  boolean isOnlyNewProjects();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isOnlyNewProjects <em>Only New Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Only New Projects</em>' attribute.
   * @see #isOnlyNewProjects()
   * @generated
   */
  void setOnlyNewProjects(boolean value);

  /**
   * Returns the value of the '<em><b>Refresh</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Refresh</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Refresh</em>' attribute.
   * @see #setRefresh(boolean)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask_Refresh()
   * @model
   * @generated
   */
  boolean isRefresh();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isRefresh <em>Refresh</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Refresh</em>' attribute.
   * @see #isRefresh()
   * @generated
   */
  void setRefresh(boolean value);

  /**
   * Returns the value of the '<em><b>Clean</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Clean</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Clean</em>' attribute.
   * @see #setClean(boolean)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask_Clean()
   * @model
   * @generated
   */
  boolean isClean();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isClean <em>Clean</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Clean</em>' attribute.
   * @see #isClean()
   * @generated
   */
  void setClean(boolean value);

  /**
   * Returns the value of the '<em><b>Build</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Build</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Build</em>' attribute.
   * @see #setBuild(boolean)
   * @see org.eclipse.oomph.setup.projects.ProjectsPackage#getProjectsBuildTask_Build()
   * @model default="true"
   * @generated
   */
  boolean isBuild();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.projects.ProjectsBuildTask#isBuild <em>Build</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Build</em>' attribute.
   * @see #isBuild()
   * @generated
   */
  void setBuild(boolean value);

} // ProjectsBuildTask

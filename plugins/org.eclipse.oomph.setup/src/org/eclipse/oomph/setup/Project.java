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
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Project#getStreams <em>Streams</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Project#getProjectContainer <em>Project Container</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Project#getLogicalProjectContainer <em>Logical Project Container</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Project#getParentProject <em>Parent Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Project#getProjectCatalog <em>Project Catalog</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends ProjectContainer
{
  /**
   * Returns the value of the '<em><b>Streams</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Stream}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Stream#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Branches</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Streams</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getProject_Streams()
   * @see org.eclipse.oomph.setup.Stream#getProject
   * @model opposite="project" containment="true" resolveProxies="true" keys="name"
   *        extendedMetaData="name='stream'"
   * @generated
   */
  EList<Stream> getStreams();

  /**
   * Returns the value of the '<em><b>Project Container</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.ProjectContainer#getProjects <em>Projects</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Container</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Container</em>' container reference.
   * @see #setProjectContainer(ProjectContainer)
   * @see org.eclipse.oomph.setup.SetupPackage#getProject_ProjectContainer()
   * @see org.eclipse.oomph.setup.ProjectContainer#getProjects
   * @model opposite="projects"
   * @generated
   */
  ProjectContainer getProjectContainer();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Project#getProjectContainer <em>Project Container</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Project Container</em>' container reference.
   * @see #getProjectContainer()
   * @generated
   */
  void setProjectContainer(ProjectContainer value);

  /**
   * Returns the value of the '<em><b>Logical Project Container</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Logical Project Container</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logical Project Container</em>' reference.
   * @see #setLogicalProjectContainer(ProjectContainer)
   * @see org.eclipse.oomph.setup.SetupPackage#getProject_LogicalProjectContainer()
   * @model
   * @generated
   */
  ProjectContainer getLogicalProjectContainer();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Project#getLogicalProjectContainer <em>Logical Project Container</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Logical Project Container</em>' reference.
   * @see #getLogicalProjectContainer()
   * @generated
   */
  void setLogicalProjectContainer(ProjectContainer value);

  /**
   * Returns the value of the '<em><b>Parent Project</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parent Project</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parent Project</em>' reference.
   * @see org.eclipse.oomph.setup.SetupPackage#getProject_ParentProject()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  Project getParentProject();

  /**
   * Returns the value of the '<em><b>Project Catalog</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Catalog</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Catalog</em>' reference.
   * @see org.eclipse.oomph.setup.SetupPackage#getProject_ProjectCatalog()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  ProjectCatalog getProjectCatalog();

} // Project

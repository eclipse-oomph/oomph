/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.MultiStatus;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Source Locator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.SourceLocator#getRootFolder <em>Root Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.SourceLocator#getExcludedPaths <em>Excluded Paths</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.SourceLocator#getProjectFactories <em>Project Factories</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.SourceLocator#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.SourceLocator#isLocateNestedProjects <em>Locate Nested Projects</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator()
 * @model
 * @generated
 */
public interface SourceLocator extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Root Folder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Root Folder</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Root Folder</em>' attribute.
   * @see #setRootFolder(String)
   * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator_RootFolder()
   * @model required="true"
   * @generated
   */
  String getRootFolder();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.resources.SourceLocator#getRootFolder <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Root Folder</em>' attribute.
   * @see #getRootFolder()
   * @generated
   */
  void setRootFolder(String value);

  /**
   * Returns the value of the '<em><b>Excluded Paths</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Excluded Paths</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Excluded Paths</em>' attribute list.
   * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator_ExcludedPaths()
   * @model extendedMetaData="name='excludedPath'"
   * @generated
   */
  EList<String> getExcludedPaths();

  /**
   * Returns the value of the '<em><b>Project Factories</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.resources.ProjectFactory}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Factories</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Factories</em>' containment reference list.
   * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator_ProjectFactories()
   * @model containment="true"
   *        extendedMetaData="name='projectFactory'"
   * @generated
   */
  EList<ProjectFactory> getProjectFactories();

  /**
   * Returns the value of the '<em><b>Locate Nested Projects</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Locate Nested Projects</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Locate Nested Projects</em>' attribute.
   * @see #setLocateNestedProjects(boolean)
   * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator_LocateNestedProjects()
   * @model
   * @generated
   */
  boolean isLocateNestedProjects();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.resources.SourceLocator#isLocateNestedProjects <em>Locate Nested Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Locate Nested Projects</em>' attribute.
   * @see #isLocateNestedProjects()
   * @generated
   */
  void setLocateNestedProjects(boolean value);

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
   * @see org.eclipse.oomph.resources.ResourcesPackage#getSourceLocator_Predicates()
   * @model containment="true"
   *        extendedMetaData="name='predicate'"
   * @generated
   */
  EList<Predicate> getPredicates();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model projectDataType="org.eclipse.oomph.predicates.Project"
   * @generated
   */
  boolean matches(IProject project);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.oomph.predicates.Project" defaultProjectFactoriesMany="true" backendContainerDataType="org.eclipse.oomph.resources.BackendContainer" monitorDataType="org.eclipse.oomph.resources.ProgressMonitor"
   * @generated
   */
  IProject loadProject(EList<ProjectFactory> defaultProjectFactories, BackendContainer backendContainer, IProgressMonitor monitor);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model defaultProjectFactoriesMany="true" projectHandlerDataType="org.eclipse.oomph.resources.ProjectHandler" statusDataType="org.eclipse.oomph.resources.MultiStatus" monitorDataType="org.eclipse.oomph.resources.ProgressMonitor"
   * @generated
   */
  void handleProjects(EList<ProjectFactory> defaultProjectFactories, ProjectHandler projectHandler, MultiStatus status, IProgressMonitor monitor);

} // SourceLocator

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
package org.eclipse.oomph.resources;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.ProjectFactory#getExcludedPaths <em>Excluded Paths</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getProjectFactory()
 * @model abstract="true"
 * @generated
 */
public interface ProjectFactory extends ModelElement
{
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
   * @see org.eclipse.oomph.resources.ResourcesPackage#getProjectFactory_ExcludedPaths()
   * @model extendedMetaData="name='excludedPath'"
   * @generated
   */
  EList<String> getExcludedPaths();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.oomph.predicates.Project" rootContainerDataType="org.eclipse.oomph.resources.BackendContainer" backendContainerDataType="org.eclipse.oomph.resources.BackendContainer" monitorDataType="org.eclipse.oomph.resources.ProgressMonitor"
   * @generated
   */
  IProject createProject(BackendContainer rootContainer, BackendContainer backendContainer, IProgressMonitor monitor);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model rootContainerDataType="org.eclipse.oomph.resources.BackendContainer" backendContainerDataType="org.eclipse.oomph.resources.BackendContainer"
   * @generated
   */
  boolean isExcludedPath(BackendContainer rootContainer, BackendContainer backendContainer);

} // ProjectFactory

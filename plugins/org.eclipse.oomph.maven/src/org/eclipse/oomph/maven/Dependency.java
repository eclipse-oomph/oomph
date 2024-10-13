/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Dependency#getResolvedProject <em>Resolved Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency <em>Resolved Managed Dependency</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Dependency#getIncomingResolvedManagedDependencies <em>Incoming Resolved Managed Dependencies</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getDependency()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='ResolvesInRealm'"
 * @generated
 */
public interface Dependency extends Coordinate
{
  String DEPENDENCY = "dependency"; //$NON-NLS-1$

  String DEPENDENCIES = "dependencies"; //$NON-NLS-1$

  String DEPENDENCY_MANAGEMENT = "dependencyManagement"; //$NON-NLS-1$

  /**
   * Returns the value of the '<em><b>Resolved Project</b></em>' reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Project#getIncomingDependencyReferences <em>Incoming Dependency References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Project</em>' reference.
   * @see #setResolvedProject(Project)
   * @see org.eclipse.oomph.maven.MavenPackage#getDependency_ResolvedProject()
   * @see org.eclipse.oomph.maven.Project#getIncomingDependencyReferences
   * @model opposite="incomingDependencyReferences"
   * @generated
   */
  Project getResolvedProject();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.Dependency#getResolvedProject <em>Resolved Project</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Project</em>' reference.
   * @see #getResolvedProject()
   * @generated
   */
  void setResolvedProject(Project value);

  /**
   * Returns the value of the '<em><b>Resolved Managed Dependency</b></em>' reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Dependency#getIncomingResolvedManagedDependencies <em>Incoming Resolved Managed Dependencies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Managed Dependency</em>' reference.
   * @see #setResolvedManagedDependency(Dependency)
   * @see org.eclipse.oomph.maven.MavenPackage#getDependency_ResolvedManagedDependency()
   * @see org.eclipse.oomph.maven.Dependency#getIncomingResolvedManagedDependencies
   * @model opposite="incomingResolvedManagedDependencies"
   * @generated
   */
  Dependency getResolvedManagedDependency();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency <em>Resolved Managed Dependency</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Managed Dependency</em>' reference.
   * @see #getResolvedManagedDependency()
   * @generated
   */
  void setResolvedManagedDependency(Dependency value);

  /**
   * Returns the value of the '<em><b>Incoming Resolved Managed Dependencies</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Dependency}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency <em>Resolved Managed Dependency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Incoming Resolved Managed Dependencies</em>' reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getDependency_IncomingResolvedManagedDependencies()
   * @see org.eclipse.oomph.maven.Dependency#getResolvedManagedDependency
   * @model opposite="resolvedManagedDependency"
   * @generated
   */
  EList<Dependency> getIncomingResolvedManagedDependencies();

} // Dependency

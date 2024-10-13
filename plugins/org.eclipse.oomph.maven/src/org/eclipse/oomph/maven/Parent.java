/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parent</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Parent#getProject <em>Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Parent#getRelativePath <em>Relative Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Parent#getResolvedProject <em>Resolved Project</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getParent()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='ResolvesInRealm ValidRelativePath'"
 * @generated
 */
public interface Parent extends Coordinate
{
  String PARENT = "parent"; //$NON-NLS-1$

  String RELATIVE_PATH = "relativePath"; //$NON-NLS-1$

  /**
   * Returns the value of the '<em><b>Project</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Project#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project</em>' container reference.
   * @see org.eclipse.oomph.maven.MavenPackage#getParent_Project()
   * @see org.eclipse.oomph.maven.Project#getParent
   * @model opposite="parent" resolveProxies="false" changeable="false"
   * @generated
   */
  Project getProject();

  /**
   * Returns the value of the '<em><b>Resolved Project</b></em>' reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Project#getIncomingParentReferences <em>Incoming Parent References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Project</em>' reference.
   * @see #setResolvedProject(Project)
   * @see org.eclipse.oomph.maven.MavenPackage#getParent_ResolvedProject()
   * @see org.eclipse.oomph.maven.Project#getIncomingParentReferences
   * @model opposite="incomingParentReferences" required="true"
   * @generated
   */
  Project getResolvedProject();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.Parent#getResolvedProject <em>Resolved Project</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Project</em>' reference.
   * @see #getResolvedProject()
   * @generated
   */
  void setResolvedProject(Project value);

  /**
   * Returns the value of the '<em><b>Relative Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Relative Path</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getParent_RelativePath()
   * @model transient="true" changeable="false" derived="true"
   * @generated
   */
  String getRelativePath();

} // Parent

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
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Project#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getRealm <em>Realm</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getDependencies <em>Dependencies</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getManagedDependencies <em>Managed Dependencies</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getIncomingParentReferences <em>Incoming Parent References</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Project#getIncomingDependencyReferences <em>Incoming Dependency References</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends Coordinate
{
  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see #setLocation(String)
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_Location()
   * @model required="true"
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.Project#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

  /**
   * Returns the value of the '<em><b>Realm</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Realm#getProjects <em>Projects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Realm</em>' container reference.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_Realm()
   * @see org.eclipse.oomph.maven.Realm#getProjects
   * @model opposite="projects" resolveProxies="false" changeable="false"
   * @generated
   */
  Realm getRealm();

  /**
   * Returns the value of the '<em><b>Parent</b></em>' containment reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Parent#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parent</em>' containment reference.
   * @see #setParent(Parent)
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_Parent()
   * @see org.eclipse.oomph.maven.Parent#getProject
   * @model opposite="project" containment="true" transient="true"
   * @generated
   */
  Parent getParent();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.Project#getParent <em>Parent</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parent</em>' containment reference.
   * @see #getParent()
   * @generated
   */
  void setParent(Parent value);

  /**
   * Returns the value of the '<em><b>Dependencies</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Dependency}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dependencies</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_Dependencies()
   * @model containment="true"
   * @generated
   */
  EList<Dependency> getDependencies();

  /**
   * Returns the value of the '<em><b>Managed Dependencies</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Dependency}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Managed Dependencies</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_ManagedDependencies()
   * @model containment="true"
   * @generated
   */
  EList<Dependency> getManagedDependencies();

  /**
   * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Property}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Properties</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_Properties()
   * @model containment="true"
   * @generated
   */
  EList<Property> getProperties();

  /**
   * Returns the value of the '<em><b>Incoming Parent References</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Parent}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Parent#getResolvedProject <em>Resolved Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Incoming Parent References</em>' reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_IncomingParentReferences()
   * @see org.eclipse.oomph.maven.Parent#getResolvedProject
   * @model opposite="resolvedProject"
   * @generated
   */
  EList<Parent> getIncomingParentReferences();

  /**
   * Returns the value of the '<em><b>Incoming Dependency References</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.Dependency}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Dependency#getResolvedProject <em>Resolved Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Incoming Dependency References</em>' reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProject_IncomingDependencyReferences()
   * @see org.eclipse.oomph.maven.Dependency#getResolvedProject
   * @model opposite="resolvedProject"
   * @generated
   */
  EList<Dependency> getIncomingDependencyReferences();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model keyRequired="true"
   * @generated
   */
  Property getProperty(String key);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dependencyRequired="true"
   * @generated
   */
  Dependency getManagedDependency(Dependency dependency);

} // Project

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
package org.eclipse.oomph.projectconfig;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.Property;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.Project#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.Project#getPreferenceProfiles <em>Preference Profiles</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.Project#getPreferenceNode <em>Preference Node</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.Project#getPreferenceProfileReferences <em>Preference Profile References</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getProject()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='AllPreferencesManaged PreferenceProfileReferencesSpecifyUniqueProperties AllPropertiesHaveManagedValue'"
 * @generated
 */
public interface Project extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Configuration</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.WorkspaceConfiguration#getProjects <em>Projects</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Configuration</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Configuration</em>' container reference.
   * @see #setConfiguration(WorkspaceConfiguration)
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getProject_Configuration()
   * @see org.eclipse.oomph.projectconfig.WorkspaceConfiguration#getProjects
   * @model opposite="projects" required="true" transient="false"
   * @generated
   */
  WorkspaceConfiguration getConfiguration();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.projectconfig.Project#getConfiguration <em>Configuration</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Configuration</em>' container reference.
   * @see #getConfiguration()
   * @generated
   */
  void setConfiguration(WorkspaceConfiguration value);

  /**
   * Returns the value of the '<em><b>Preference Profiles</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.PreferenceProfile}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Preference Profiles</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preference Profiles</em>' containment reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getProject_PreferenceProfiles()
   * @see org.eclipse.oomph.projectconfig.PreferenceProfile#getProject
   * @model opposite="project" containment="true"
   *        extendedMetaData="name='preferenceProfile'"
   * @generated
   */
  EList<PreferenceProfile> getPreferenceProfiles();

  /**
   * Returns the value of the '<em><b>Preference Node</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Preference Node</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preference Node</em>' reference.
   * @see #setPreferenceNode(PreferenceNode)
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getProject_PreferenceNode()
   * @model required="true"
   * @generated
   */
  PreferenceNode getPreferenceNode();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.projectconfig.Project#getPreferenceNode <em>Preference Node</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Preference Node</em>' reference.
   * @see #getPreferenceNode()
   * @generated
   */
  void setPreferenceNode(PreferenceNode value);

  /**
   * Returns the value of the '<em><b>Preference Profile References</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.projectconfig.PreferenceProfile}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.projectconfig.PreferenceProfile#getReferentProjects <em>Referent Projects</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Preference Profile References</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preference Profile References</em>' reference list.
   * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage#getProject_PreferenceProfileReferences()
   * @see org.eclipse.oomph.projectconfig.PreferenceProfile#getReferentProjects
   * @model opposite="referentProjects"
   * @generated
   */
  EList<PreferenceProfile> getPreferenceProfileReferences();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model pathDataType="org.eclipse.oomph.preferences.URI"
   * @generated
   */
  Property getProperty(URI path);

} // Project

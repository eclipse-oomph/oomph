/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Targlet</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getInstallableUnitGenerators <em>Installable Unit Generators</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getRepositoryLists <em>Repository Lists</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryListName <em>Active Repository List Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryList <em>Active Repository List</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositories <em>Active Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#isIncludeSources <em>Include Sources</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllPlatforms <em>Include All Platforms</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllRequirements <em>Include All Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#getDropinLocations <em>Dropin Locations</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.Targlet#isIncludeBinaryEquivalents <em>Include Binary Equivalents</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet()
 * @model
 * @generated
 */
public interface Targlet extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Requirement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Requirements</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_Requirements()
   * @model containment="true"
   *        extendedMetaData="name='requirement'"
   * @generated
   */
  EList<Requirement> getRequirements();

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
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_SourceLocators()
   * @model containment="true"
   *        extendedMetaData="name='sourceLocator'"
   * @generated
   */
  EList<SourceLocator> getSourceLocators();

  /**
   * Returns the value of the '<em><b>Installable Unit Generators</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.targlets.IUGenerator}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Installable Unit Generators</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Installable Unit Generators</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_InstallableUnitGenerators()
   * @model containment="true"
   *        extendedMetaData="name='installableUnitGenerator'"
   * @generated
   */
  EList<IUGenerator> getInstallableUnitGenerators();

  /**
   * Returns the value of the '<em><b>Dropin Locations</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.targlets.DropinLocation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Dropin Locations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dropin Locations</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_DropinLocations()
   * @model containment="true"
   *        extendedMetaData="name='dropinLocation'"
   * @generated
   */
  EList<DropinLocation> getDropinLocations();

  /**
   * Returns the value of the '<em><b>Include Binary Equivalents</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include Binary Equivalents</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include Binary Equivalents</em>' attribute.
   * @see #setIncludeBinaryEquivalents(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_IncludeBinaryEquivalents()
   * @model default="true"
   * @generated
   */
  boolean isIncludeBinaryEquivalents();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#isIncludeBinaryEquivalents <em>Include Binary Equivalents</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include Binary Equivalents</em>' attribute.
   * @see #isIncludeBinaryEquivalents()
   * @generated
   */
  void setIncludeBinaryEquivalents(boolean value);

  /**
   * Returns the value of the '<em><b>Repository Lists</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.RepositoryList}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repository Lists</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repository Lists</em>' containment reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_RepositoryLists()
   * @model containment="true"
   *        extendedMetaData="name='repositoryList'"
   * @generated
   */
  EList<RepositoryList> getRepositoryLists();

  /**
   * Returns the value of the '<em><b>Active Repository List Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Active Repository List</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Active Repository List Name</em>' attribute.
   * @see #setActiveRepositoryListName(String)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_ActiveRepositoryListName()
   * @model extendedMetaData="kind='attribute' name='activeRepositoryList'"
   * @generated
   */
  String getActiveRepositoryListName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#getActiveRepositoryListName <em>Active Repository List Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Active Repository List Name</em>' attribute.
   * @see #getActiveRepositoryListName()
   * @generated
   */
  void setActiveRepositoryListName(String value);

  /**
   * Returns the value of the '<em><b>Active Repository List</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Active Repository List</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Active Repository List</em>' reference.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_ActiveRepositoryList()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   *        extendedMetaData="name='activeRepository'"
   * @generated
   */
  RepositoryList getActiveRepositoryList();

  /**
   * Returns the value of the '<em><b>Active Repositories</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.p2.Repository}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Active Repositories</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Active Repositories</em>' reference list.
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_ActiveRepositories()
   * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
   *        extendedMetaData="name='activeRepository'"
   * @generated
   */
  EList<Repository> getActiveRepositories();

  /**
   * Returns the value of the '<em><b>Include Sources</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include Sources</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include Sources</em>' attribute.
   * @see #setIncludeSources(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_IncludeSources()
   * @model default="true"
   * @generated
   */
  boolean isIncludeSources();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#isIncludeSources <em>Include Sources</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include Sources</em>' attribute.
   * @see #isIncludeSources()
   * @generated
   */
  void setIncludeSources(boolean value);

  /**
   * Returns the value of the '<em><b>Include All Platforms</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include All Platforms</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include All Platforms</em>' attribute.
   * @see #setIncludeAllPlatforms(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_IncludeAllPlatforms()
   * @model
   * @generated
   */
  boolean isIncludeAllPlatforms();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllPlatforms <em>Include All Platforms</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include All Platforms</em>' attribute.
   * @see #isIncludeAllPlatforms()
   * @generated
   */
  void setIncludeAllPlatforms(boolean value);

  /**
   * Returns the value of the '<em><b>Include All Requirements</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include All Requirements</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include All Requirements</em>' attribute.
   * @see #setIncludeAllRequirements(boolean)
   * @see org.eclipse.oomph.targlets.TargletPackage#getTarglet_IncludeAllRequirements()
   * @model default="true"
   * @generated
   */
  boolean isIncludeAllRequirements();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.targlets.Targlet#isIncludeAllRequirements <em>Include All Requirements</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include All Requirements</em>' attribute.
   * @see #isIncludeAllRequirements()
   * @generated
   */
  void setIncludeAllRequirements(boolean value);

} // Targlet

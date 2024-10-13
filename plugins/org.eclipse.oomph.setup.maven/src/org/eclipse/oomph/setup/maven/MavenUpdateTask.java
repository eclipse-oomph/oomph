/*
 * Copyright (c) 2020 Martin Schreiber (Bachmann electronic GmbH) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Martin Schreiber - initial API and implementation
 */
package org.eclipse.oomph.setup.maven;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#getProjectNamePatterns <em>Project Name Patterns</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isOffline <em>Offline</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isUpdateSnapshots <em>Update Snapshots</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenUpdateTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface MavenUpdateTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenUpdateTask_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Project Name Patterns</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Name Patterns</em>' attribute list.
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenUpdateTask_ProjectNamePatterns()
   * @model extendedMetaData="kind='element' name='projectNamePattern'"
   * @generated
   */
  EList<String> getProjectNamePatterns();

  /**
   * Returns the value of the '<em><b>Offline</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Offline</em>' attribute.
   * @see #setOffline(boolean)
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenUpdateTask_Offline()
   * @model
   * @generated
   */
  boolean isOffline();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isOffline <em>Offline</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Offline</em>' attribute.
   * @see #isOffline()
   * @generated
   */
  void setOffline(boolean value);

  /**
   * Returns the value of the '<em><b>Update Snapshots</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Update Snapshots</em>' attribute.
   * @see #setUpdateSnapshots(boolean)
   * @see org.eclipse.oomph.setup.maven.MavenPackage#getMavenUpdateTask_UpdateSnapshots()
   * @model
   * @generated
   */
  boolean isUpdateSnapshots();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.maven.MavenUpdateTask#isUpdateSnapshots <em>Update Snapshots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Update Snapshots</em>' attribute.
   * @see #isUpdateSnapshots()
   * @generated
   */
  void setUpdateSnapshots(boolean value);

} // MavenUpdateTask

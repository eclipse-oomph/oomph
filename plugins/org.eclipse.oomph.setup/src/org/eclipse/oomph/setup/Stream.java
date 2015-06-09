/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Branch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Stream#getProject <em>Project</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getStream()
 * @model
 * @generated
 */
public interface Stream extends Scope
{
  /**
   * Returns the value of the '<em><b>Project</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Project#getStreams <em>Streams</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project</em>' container reference.
   * @see #setProject(Project)
   * @see org.eclipse.oomph.setup.SetupPackage#getStream_Project()
   * @see org.eclipse.oomph.setup.Project#getStreams
   * @model opposite="streams"
   * @generated
   */
  Project getProject();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Stream#getProject <em>Project</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Project</em>' container reference.
   * @see #getProject()
   * @generated
   */
  void setProject(Project value);

} // Branch

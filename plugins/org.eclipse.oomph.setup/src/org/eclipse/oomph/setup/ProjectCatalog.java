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
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.ProjectCatalog#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getProjectCatalog()
 * @model
 * @generated
 */
public interface ProjectCatalog extends ProjectContainer
{
  /**
   * Returns the value of the '<em><b>Index</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Index#getProjectCatalogs <em>Project Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Index</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Index</em>' container reference.
   * @see #setIndex(Index)
   * @see org.eclipse.oomph.setup.SetupPackage#getProjectCatalog_Index()
   * @see org.eclipse.oomph.setup.Index#getProjectCatalogs
   * @model opposite="projectCatalogs"
   * @generated
   */
  Index getIndex();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.ProjectCatalog#getIndex <em>Index</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Index</em>' container reference.
   * @see #getIndex()
   * @generated
   */
  void setIndex(Index value);

} // Configuration

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
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Index#getProductCatalogs <em>Product Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Index#getProjectCatalogs <em>Project Catalogs</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getIndex()
 * @model
 * @generated
 */
public interface Index extends EObject
{
  /**
   * Returns the value of the '<em><b>Project Catalogs</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.ProjectCatalog}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.ProjectCatalog#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Catalogs</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Catalogs</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getIndex_ProjectCatalogs()
   * @see org.eclipse.oomph.setup.ProjectCatalog#getIndex
   * @model opposite="index" containment="true" resolveProxies="true" keys="name"
   *        extendedMetaData="name='projectCatalog'"
   * @generated
   */
  EList<ProjectCatalog> getProjectCatalogs();

  /**
   * Returns the value of the '<em><b>Product Catalogs</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.ProductCatalog}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.ProductCatalog#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Catalogs</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Product Catalogs</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getIndex_ProductCatalogs()
   * @see org.eclipse.oomph.setup.ProductCatalog#getIndex
   * @model opposite="index" containment="true" resolveProxies="true" keys="name"
   *        extendedMetaData="name='productCatalog'"
   * @generated
   */
  EList<ProductCatalog> getProductCatalogs();

} // Index

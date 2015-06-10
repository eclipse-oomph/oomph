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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Product Catalog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.ProductCatalog#getIndex <em>Index</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.ProductCatalog#getProducts <em>Products</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getProductCatalog()
 * @model
 * @generated
 */
public interface ProductCatalog extends Scope
{
  /**
   * Returns the value of the '<em><b>Index</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Index#getProductCatalogs <em>Product Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Index</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Index</em>' container reference.
   * @see #setIndex(Index)
   * @see org.eclipse.oomph.setup.SetupPackage#getProductCatalog_Index()
   * @see org.eclipse.oomph.setup.Index#getProductCatalogs
   * @model opposite="productCatalogs"
   * @generated
   */
  Index getIndex();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.ProductCatalog#getIndex <em>Index</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Index</em>' container reference.
   * @see #getIndex()
   * @generated
   */
  void setIndex(Index value);

  /**
   * Returns the value of the '<em><b>Products</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.Product}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.Product#getProductCatalog <em>Product Catalog</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Products</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Products</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getProductCatalog_Products()
   * @see org.eclipse.oomph.setup.Product#getProductCatalog
   * @model opposite="productCatalog" containment="true" resolveProxies="true" keys="name"
   *        extendedMetaData="name='product'"
   * @generated
   */
  EList<Product> getProducts();

} // ProductCatalog

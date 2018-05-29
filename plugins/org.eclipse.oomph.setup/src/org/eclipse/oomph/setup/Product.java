/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
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
 * A representation of the model object '<em><b>Product</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Product#getProductCatalog <em>Product Catalog</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Product#getVersions <em>Versions</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getProduct()
 * @model
 * @generated
 */
public interface Product extends Scope
{
  /**
   * Returns the value of the '<em><b>Product Catalog</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.ProductCatalog#getProducts <em>Products</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Catalog</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Product Catalog</em>' container reference.
   * @see #setProductCatalog(ProductCatalog)
   * @see org.eclipse.oomph.setup.SetupPackage#getProduct_ProductCatalog()
   * @see org.eclipse.oomph.setup.ProductCatalog#getProducts
   * @model opposite="products" keys="name"
   * @generated
   */
  ProductCatalog getProductCatalog();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Product#getProductCatalog <em>Product Catalog</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Product Catalog</em>' container reference.
   * @see #getProductCatalog()
   * @generated
   */
  void setProductCatalog(ProductCatalog value);

  /**
   * Returns the value of the '<em><b>Versions</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.ProductVersion}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.setup.ProductVersion#getProduct <em>Product</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Versions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Versions</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getProduct_Versions()
   * @see org.eclipse.oomph.setup.ProductVersion#getProduct
   * @model opposite="product" containment="true" resolveProxies="true" keys="name" required="true"
   *        extendedMetaData="name='version'"
   * @generated
   */
  EList<ProductVersion> getVersions();

} // Product

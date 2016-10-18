/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.Index#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Index#getDiscoverablePackages <em>Discoverable Packages</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Index#getProductCatalogs <em>Product Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.Index#getProjectCatalogs <em>Project Catalogs</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getIndex()
 * @model
 * @generated
 */
public interface Index extends ModelElement
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
   * @see org.eclipse.oomph.setup.SetupPackage#getIndex_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.Index#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Discoverable Packages</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EPackage}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Discoverable Packages</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Discoverable Packages</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getIndex_DiscoverablePackages()
   * @model extendedMetaData="name='discoverablePackage'"
   * @generated
   */
  EList<EPackage> getDiscoverablePackages();

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

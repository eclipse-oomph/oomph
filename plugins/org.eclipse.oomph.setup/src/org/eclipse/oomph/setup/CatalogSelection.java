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
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Catalog Selection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.CatalogSelection#getProductCatalogs <em>Product Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.CatalogSelection#getProjectCatalogs <em>Project Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.CatalogSelection#getDefaultProductVersions <em>Default Product Versions</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams <em>Default Streams</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getCatalogSelection()
 * @model
 * @generated
 */
public interface CatalogSelection extends EObject
{
  /**
   * Returns the value of the '<em><b>Product Catalogs</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.ProductCatalog}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Catalogs</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Product Catalogs</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getCatalogSelection_ProductCatalogs()
   * @model keys="name"
   *        extendedMetaData="name='productCatalog'"
   * @generated
   */
  EList<ProductCatalog> getProductCatalogs();

  /**
   * Returns the value of the '<em><b>Project Catalogs</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.ProjectCatalog}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Project Catalogs</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Project Catalogs</em>' reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getCatalogSelection_ProjectCatalogs()
   * @model keys="name"
   *        extendedMetaData="name='projectCatalog'"
   * @generated
   */
  EList<ProjectCatalog> getProjectCatalogs();

  /**
   * Returns the value of the '<em><b>Default Product Versions</b></em>' map.
   * The key is of type {@link org.eclipse.oomph.setup.Product},
   * and the value is of type {@link org.eclipse.oomph.setup.ProductVersion},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Product Versions</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Product Versions</em>' map.
   * @see org.eclipse.oomph.setup.SetupPackage#getCatalogSelection_DefaultProductVersions()
   * @model mapType="org.eclipse.oomph.setup.ProductToProductVersionMapEntry<org.eclipse.oomph.setup.Product, org.eclipse.oomph.setup.ProductVersion>"
   *        extendedMetaData="name='defaultProductVersion'"
   * @generated
   */
  EMap<Product, ProductVersion> getDefaultProductVersions();

  /**
   * Returns the value of the '<em><b>Default Streams</b></em>' map.
   * The key is of type {@link org.eclipse.oomph.setup.Project},
   * and the value is of type {@link org.eclipse.oomph.setup.Stream},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Streams</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Streams</em>' map.
   * @see org.eclipse.oomph.setup.SetupPackage#getCatalogSelection_DefaultStreams()
   * @model mapType="org.eclipse.oomph.setup.ProjectToStreamMapEntry<org.eclipse.oomph.setup.Project, org.eclipse.oomph.setup.Stream>"
   *        extendedMetaData="name='defaultStream'"
   * @generated
   */
  EMap<Project, Stream> getDefaultStreams();

} // CatalogSelection

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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Catalog Selection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getProductCatalogs <em>Product Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getProjectCatalogs <em>Project Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getDefaultProductVersions <em>Default Product Versions</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getDefaultStreams <em>Default Streams</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CatalogSelectionImpl extends ModelElementImpl implements CatalogSelection
{
  /**
   * The cached value of the '{@link #getProductCatalogs() <em>Product Catalogs</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProductCatalogs()
   * @generated
   * @ordered
   */
  protected EList<ProductCatalog> productCatalogs;

  /**
   * The cached value of the '{@link #getProjectCatalogs() <em>Project Catalogs</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectCatalogs()
   * @generated
   * @ordered
   */
  protected EList<ProjectCatalog> projectCatalogs;

  /**
   * The cached value of the '{@link #getDefaultProductVersions() <em>Default Product Versions</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultProductVersions()
   * @generated
   * @ordered
   */
  protected EMap<Product, ProductVersion> defaultProductVersions;

  /**
   * The cached value of the '{@link #getDefaultStreams() <em>Default Streams</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultStreams()
   * @generated
   * @ordered
   */
  protected EMap<Project, Stream> defaultStreams;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CatalogSelectionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SetupPackage.Literals.CATALOG_SELECTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ProductCatalog> getProductCatalogs()
  {
    if (productCatalogs == null)
    {
      productCatalogs = new EObjectResolvingEList<ProductCatalog>(ProductCatalog.class, this, SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS);
    }
    return productCatalogs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ProjectCatalog> getProjectCatalogs()
  {
    if (projectCatalogs == null)
    {
      projectCatalogs = new EObjectResolvingEList<ProjectCatalog>(ProjectCatalog.class, this, SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS);
    }
    return projectCatalogs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EMap<Product, ProductVersion> getDefaultProductVersions()
  {
    if (defaultProductVersions == null)
    {
      defaultProductVersions = new EcoreEMap<Product, ProductVersion>(SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY,
          ProductToProductVersionMapEntryImpl.class, this, SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS);
    }
    return defaultProductVersions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EMap<Project, Stream> getDefaultStreams()
  {
    if (defaultStreams == null)
    {
      defaultStreams = new EcoreEMap<Project, Stream>(SetupPackage.Literals.PROJECT_TO_STREAM_MAP_ENTRY, ProjectToStreamMapEntryImpl.class, this,
          SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS);
    }
    return defaultStreams;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        return ((InternalEList<?>)getDefaultProductVersions()).basicRemove(otherEnd, msgs);
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        return ((InternalEList<?>)getDefaultStreams()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        return getProductCatalogs();
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        return getProjectCatalogs();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        if (coreType)
        {
          return getDefaultProductVersions();
        }
        else
        {
          return getDefaultProductVersions().map();
        }
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        if (coreType)
        {
          return getDefaultStreams();
        }
        else
        {
          return getDefaultStreams().map();
        }
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        getProductCatalogs().clear();
        getProductCatalogs().addAll((Collection<? extends ProductCatalog>)newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        getProjectCatalogs().clear();
        getProjectCatalogs().addAll((Collection<? extends ProjectCatalog>)newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        ((EStructuralFeature.Setting)getDefaultProductVersions()).set(newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        ((EStructuralFeature.Setting)getDefaultStreams()).set(newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        getProductCatalogs().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        getProjectCatalogs().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        getDefaultProductVersions().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        getDefaultStreams().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        return productCatalogs != null && !productCatalogs.isEmpty();
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        return projectCatalogs != null && !projectCatalogs.isEmpty();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        return defaultProductVersions != null && !defaultProductVersions.isEmpty();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        return defaultStreams != null && !defaultStreams.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // CatalogSelectionImpl

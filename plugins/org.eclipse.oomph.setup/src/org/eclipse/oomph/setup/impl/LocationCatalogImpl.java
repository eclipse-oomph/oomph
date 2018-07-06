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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Workspace;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Location Catalog</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.LocationCatalogImpl#getInstallations <em>Installations</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.LocationCatalogImpl#getWorkspaces <em>Workspaces</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationCatalogImpl extends MinimalEObjectImpl.Container implements LocationCatalog
{
  /**
   * The cached value of the '{@link #getInstallations() <em>Installations</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstallations()
   * @generated
   * @ordered
   */
  protected EMap<Installation, EList<Workspace>> installations;

  /**
   * The cached value of the '{@link #getWorkspaces() <em>Workspaces</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWorkspaces()
   * @generated
   * @ordered
   */
  protected EMap<Workspace, EList<Installation>> workspaces;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LocationCatalogImpl()
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
    return SetupPackage.Literals.LOCATION_CATALOG;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EMap<Installation, EList<Workspace>> getInstallations()
  {
    if (installations == null)
    {
      installations = new EcoreEMap<Installation, EList<Workspace>>(SetupPackage.Literals.INSTALLATION_TO_WORKSPACES_MAP_ENTRY,
          InstallationToWorkspacesMapEntryImpl.class, this, SetupPackage.LOCATION_CATALOG__INSTALLATIONS);
    }
    return installations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EMap<Workspace, EList<Installation>> getWorkspaces()
  {
    if (workspaces == null)
    {
      workspaces = new EcoreEMap<Workspace, EList<Installation>>(SetupPackage.Literals.WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY,
          WorkspaceToInstallationsMapEntryImpl.class, this, SetupPackage.LOCATION_CATALOG__WORKSPACES);
    }
    return workspaces;
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
      case SetupPackage.LOCATION_CATALOG__INSTALLATIONS:
        return ((InternalEList<?>)getInstallations()).basicRemove(otherEnd, msgs);
      case SetupPackage.LOCATION_CATALOG__WORKSPACES:
        return ((InternalEList<?>)getWorkspaces()).basicRemove(otherEnd, msgs);
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
      case SetupPackage.LOCATION_CATALOG__INSTALLATIONS:
        if (coreType)
        {
          return getInstallations();
        }
        else
        {
          return getInstallations().map();
        }
      case SetupPackage.LOCATION_CATALOG__WORKSPACES:
        if (coreType)
        {
          return getWorkspaces();
        }
        else
        {
          return getWorkspaces().map();
        }
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.LOCATION_CATALOG__INSTALLATIONS:
        ((EStructuralFeature.Setting)getInstallations()).set(newValue);
        return;
      case SetupPackage.LOCATION_CATALOG__WORKSPACES:
        ((EStructuralFeature.Setting)getWorkspaces()).set(newValue);
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
      case SetupPackage.LOCATION_CATALOG__INSTALLATIONS:
        getInstallations().clear();
        return;
      case SetupPackage.LOCATION_CATALOG__WORKSPACES:
        getWorkspaces().clear();
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
      case SetupPackage.LOCATION_CATALOG__INSTALLATIONS:
        return installations != null && !installations.isEmpty();
      case SetupPackage.LOCATION_CATALOG__WORKSPACES:
        return workspaces != null && !workspaces.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // LocationCatalogImpl

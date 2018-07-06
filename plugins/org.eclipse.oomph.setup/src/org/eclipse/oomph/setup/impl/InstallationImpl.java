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
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Installation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.InstallationImpl#getProductVersion <em>Product Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InstallationImpl extends ScopeImpl implements Installation
{
  /**
   * The cached value of the '{@link #getProductVersion() <em>Product Version</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProductVersion()
   * @generated
   * @ordered
   */
  protected ProductVersion productVersion;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InstallationImpl()
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
    return SetupPackage.Literals.INSTALLATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProductVersion getProductVersion()
  {
    if (productVersion != null && productVersion.eIsProxy())
    {
      InternalEObject oldProductVersion = (InternalEObject)productVersion;
      productVersion = (ProductVersion)eResolveProxy(oldProductVersion);
      if (productVersion != oldProductVersion)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.INSTALLATION__PRODUCT_VERSION, oldProductVersion, productVersion));
        }
      }
    }
    return productVersion;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProductVersion basicGetProductVersion()
  {
    return productVersion;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProductVersion(ProductVersion newProductVersion)
  {
    ProductVersion oldProductVersion = productVersion;
    productVersion = newProductVersion;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.INSTALLATION__PRODUCT_VERSION, oldProductVersion, productVersion));
    }
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
      case SetupPackage.INSTALLATION__PRODUCT_VERSION:
        if (resolve)
        {
          return getProductVersion();
        }
        return basicGetProductVersion();
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
      case SetupPackage.INSTALLATION__PRODUCT_VERSION:
        setProductVersion((ProductVersion)newValue);
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
      case SetupPackage.INSTALLATION__PRODUCT_VERSION:
        setProductVersion((ProductVersion)null);
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
      case SetupPackage.INSTALLATION__PRODUCT_VERSION:
        return productVersion != null;
    }
    return super.eIsSet(featureID);
  }

  @Override
  public ScopeType getType()
  {
    return ScopeType.INSTALLATION;
  }

} // InstallationImpl

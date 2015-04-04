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

import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Version</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProductVersionImpl#getProduct <em>Product</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProductVersionImpl#getRequiredJavaVersion <em>Required Java Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProductVersionImpl extends ScopeImpl implements ProductVersion
{
  /**
   * The default value of the '{@link #getRequiredJavaVersion() <em>Required Java Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequiredJavaVersion()
   * @generated
   * @ordered
   */
  protected static final String REQUIRED_JAVA_VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRequiredJavaVersion() <em>Required Java Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequiredJavaVersion()
   * @generated
   * @ordered
   */
  protected String requiredJavaVersion = REQUIRED_JAVA_VERSION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProductVersionImpl()
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
    return SetupPackage.Literals.PRODUCT_VERSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Product getProduct()
  {
    if (eContainerFeatureID() != SetupPackage.PRODUCT_VERSION__PRODUCT)
    {
      return null;
    }
    return (Product)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Product basicGetProduct()
  {
    if (eContainerFeatureID() != SetupPackage.PRODUCT_VERSION__PRODUCT)
    {
      return null;
    }
    return (Product)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProduct(Product newProduct, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newProduct, SetupPackage.PRODUCT_VERSION__PRODUCT, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProduct(Product newProduct)
  {
    if (newProduct != eInternalContainer() || eContainerFeatureID() != SetupPackage.PRODUCT_VERSION__PRODUCT && newProduct != null)
    {
      if (EcoreUtil.isAncestor(this, newProduct))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newProduct != null)
      {
        msgs = ((InternalEObject)newProduct).eInverseAdd(this, SetupPackage.PRODUCT__VERSIONS, Product.class, msgs);
      }
      msgs = basicSetProduct(newProduct, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PRODUCT_VERSION__PRODUCT, newProduct, newProduct));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRequiredJavaVersion()
  {
    return requiredJavaVersion;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRequiredJavaVersion(String newRequiredJavaVersion)
  {
    String oldRequiredJavaVersion = requiredJavaVersion;
    requiredJavaVersion = newRequiredJavaVersion;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PRODUCT_VERSION__REQUIRED_JAVA_VERSION, oldRequiredJavaVersion, requiredJavaVersion));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetProduct((Product)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
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
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        return basicSetProduct(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        return eInternalContainer().eInverseRemove(this, SetupPackage.PRODUCT__VERSIONS, Product.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
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
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        if (resolve)
        {
          return getProduct();
        }
        return basicGetProduct();
      case SetupPackage.PRODUCT_VERSION__REQUIRED_JAVA_VERSION:
        return getRequiredJavaVersion();
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
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        setProduct((Product)newValue);
        return;
      case SetupPackage.PRODUCT_VERSION__REQUIRED_JAVA_VERSION:
        setRequiredJavaVersion((String)newValue);
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
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        setProduct((Product)null);
        return;
      case SetupPackage.PRODUCT_VERSION__REQUIRED_JAVA_VERSION:
        setRequiredJavaVersion(REQUIRED_JAVA_VERSION_EDEFAULT);
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
      case SetupPackage.PRODUCT_VERSION__PRODUCT:
        return basicGetProduct() != null;
      case SetupPackage.PRODUCT_VERSION__REQUIRED_JAVA_VERSION:
        return REQUIRED_JAVA_VERSION_EDEFAULT == null ? requiredJavaVersion != null : !REQUIRED_JAVA_VERSION_EDEFAULT.equals(requiredJavaVersion);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (requiredJavaVersion: ");
    result.append(requiredJavaVersion);
    result.append(')');
    return result.toString();
  }

  @Override
  public ScopeType getType()
  {
    return ScopeType.PRODUCT_VERSION;
  }

  @Override
  public Scope getParentScope()
  {
    return getProduct();
  }

} // EclipseImpl

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

import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.User;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Preferences</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getAttributeRules <em>Attribute Rules</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getAcceptedLicenses <em>Accepted Licenses</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserImpl extends ScopeImpl implements User
{
  /**
   * The cached value of the '{@link #getAttributeRules() <em>Attribute Rules</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttributeRules()
   * @generated
   * @ordered
   */
  protected EList<AttributeRule> attributeRules;

  /**
   * The cached value of the '{@link #getAcceptedLicenses() <em>Accepted Licenses</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceptedLicenses()
   * @generated
   * @ordered
   */
  protected EList<LicenseInfo> acceptedLicenses;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected UserImpl()
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
    return SetupPackage.Literals.USER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<LicenseInfo> getAcceptedLicenses()
  {
    if (acceptedLicenses == null)
    {
      acceptedLicenses = new EDataTypeUniqueEList<LicenseInfo>(LicenseInfo.class, this, SetupPackage.USER__ACCEPTED_LICENSES);
    }
    return acceptedLicenses;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<AttributeRule> getAttributeRules()
  {
    if (attributeRules == null)
    {
      attributeRules = new EObjectContainmentEList.Resolving<AttributeRule>(AttributeRule.class, this, SetupPackage.USER__ATTRIBUTE_RULES);
    }
    return attributeRules;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public ScopeType getType()
  {
    return ScopeType.USER;
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return ((InternalEList<?>)getAttributeRules()).basicRemove(otherEnd, msgs);
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return getAttributeRules();
      case SetupPackage.USER__ACCEPTED_LICENSES:
        return getAcceptedLicenses();
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        getAttributeRules().clear();
        getAttributeRules().addAll((Collection<? extends AttributeRule>)newValue);
        return;
      case SetupPackage.USER__ACCEPTED_LICENSES:
        getAcceptedLicenses().clear();
        getAcceptedLicenses().addAll((Collection<? extends LicenseInfo>)newValue);
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        getAttributeRules().clear();
        return;
      case SetupPackage.USER__ACCEPTED_LICENSES:
        getAcceptedLicenses().clear();
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return attributeRules != null && !attributeRules.isEmpty();
      case SetupPackage.USER__ACCEPTED_LICENSES:
        return acceptedLicenses != null && !acceptedLicenses.isEmpty();
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
    result.append(" (acceptedLicenses: ");
    result.append(acceptedLicenses);
    result.append(')');
    return result.toString();
  }

} // PreferencesImpl

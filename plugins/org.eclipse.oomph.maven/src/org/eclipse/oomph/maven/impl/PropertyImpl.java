/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyImpl#getExpandedValue <em>Expanded Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyImpl#getIncomingResolvedPropertyReferences <em>Incoming Resolved Property References</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyImpl extends DOMElementImpl implements Property
{
  /**
   * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected static final String KEY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected String key = KEY_EDEFAULT;

  /**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected static final String VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected String value = VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getExpandedValue() <em>Expanded Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedValue()
   * @generated
   * @ordered
   */
  protected static final String EXPANDED_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getExpandedValue() <em>Expanded Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedValue()
   * @generated
   * @ordered
   */
  protected String expandedValue = EXPANDED_VALUE_EDEFAULT;

  /**
   * The cached value of the '{@link #getIncomingResolvedPropertyReferences() <em>Incoming Resolved Property References</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIncomingResolvedPropertyReferences()
   * @generated
   * @ordered
   */
  protected EList<PropertyReference> incomingResolvedPropertyReferences;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PropertyImpl()
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
    return MavenPackage.Literals.PROPERTY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getKey()
  {
    if (key == null)
    {
      key = getElement().getLocalName();
    }
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getValue()
  {
    if (value == null)
    {
      value = element.getTextContent().strip();
    }
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getExpandedValue()
  {
    if (expandedValue == null && getValue() != null)
    {
      expandedValue = expandProperties(getElement(), getValue());
    }

    return expandedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<PropertyReference> getIncomingResolvedPropertyReferences()
  {
    if (incomingResolvedPropertyReferences == null)
    {
      incomingResolvedPropertyReferences = new EObjectWithInverseResolvingEList<>(PropertyReference.class, this,
          MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES, MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY);
    }
    return incomingResolvedPropertyReferences;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingResolvedPropertyReferences()).basicAdd(otherEnd, msgs);
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
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        return ((InternalEList<?>)getIncomingResolvedPropertyReferences()).basicRemove(otherEnd, msgs);
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
      case MavenPackage.PROPERTY__KEY:
        return getKey();
      case MavenPackage.PROPERTY__VALUE:
        return getValue();
      case MavenPackage.PROPERTY__EXPANDED_VALUE:
        return getExpandedValue();
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        return getIncomingResolvedPropertyReferences();
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
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        getIncomingResolvedPropertyReferences().clear();
        getIncomingResolvedPropertyReferences().addAll((Collection<? extends PropertyReference>)newValue);
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
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        getIncomingResolvedPropertyReferences().clear();
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
      case MavenPackage.PROPERTY__KEY:
        return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
      case MavenPackage.PROPERTY__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case MavenPackage.PROPERTY__EXPANDED_VALUE:
        return EXPANDED_VALUE_EDEFAULT == null ? expandedValue != null : !EXPANDED_VALUE_EDEFAULT.equals(expandedValue);
      case MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES:
        return incomingResolvedPropertyReferences != null && !incomingResolvedPropertyReferences.isEmpty();
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (key: "); //$NON-NLS-1$
    result.append(key);
    result.append(", value: "); //$NON-NLS-1$
    result.append(value);
    result.append(", expandedValue: "); //$NON-NLS-1$
    result.append(expandedValue);
    result.append(')');
    return result.toString();
  }

} // PropertyImpl

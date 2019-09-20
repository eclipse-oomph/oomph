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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PropertyImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PropertyImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PropertyImpl#isNonDefault <em>Non Default</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PropertyImpl#isSecure <em>Secure</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyImpl extends PreferenceItemImpl implements Property
{
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
   * The default value of the '{@link #isNonDefault() <em>Non Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNonDefault()
   * @generated
   * @ordered
   */
  protected static final boolean NON_DEFAULT_EDEFAULT = false;

  /**
   * The default value of the '{@link #isSecure() <em>Secure</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSecure()
   * @generated
   * @ordered
   */
  protected static final boolean SECURE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isSecure() <em>Secure</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSecure()
   * @generated
   * @ordered
   */
  protected boolean secure = SECURE_EDEFAULT;

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
    return PreferencesPackage.Literals.PROPERTY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PreferenceNode getParent()
  {
    if (eContainerFeatureID() != PreferencesPackage.PROPERTY__PARENT)
    {
      return null;
    }
    return (PreferenceNode)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParent(PreferenceNode newParent, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newParent, PreferencesPackage.PROPERTY__PARENT, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParent(PreferenceNode newParent)
  {
    if (newParent != eInternalContainer() || eContainerFeatureID() != PreferencesPackage.PROPERTY__PARENT && newParent != null)
    {
      if (EcoreUtil.isAncestor(this, newParent))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newParent != null)
      {
        msgs = ((InternalEObject)newParent).eInverseAdd(this, PreferencesPackage.PREFERENCE_NODE__PROPERTIES, PreferenceNode.class, msgs);
      }
      msgs = basicSetParent(newParent, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PROPERTY__PARENT, newParent, newParent));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getValue()
  {
    return value;
  }

  public String getSecureValue()
  {
    return isSecure() ? PreferencesUtil.decrypt(getValue()) : getValue();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setValue(String newValue)
  {
    if (isSecure())
    {
      newValue = PreferencesUtil.encrypt(newValue);
    }

    String oldValue = value;
    value = newValue;

    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PROPERTY__VALUE, oldValue, value));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean isNonDefault()
  {
    Property ancestor = getAncestor();
    if (ancestor != null)
    {
      String ancestorValue = ancestor.getValue();
      String value = getValue();
      return value == null ? ancestorValue != null : !value.equals(ancestorValue);
    }

    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSecure()
  {
    return secure;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setSecure(boolean newSecure)
  {
    boolean oldSecure = secure;
    secure = newSecure;

    if (oldSecure != newSecure)
    {
      String oldValue = value;
      value = secure ? PreferencesUtil.encrypt(oldValue) : PreferencesUtil.decrypt(oldValue);
      if (eNotificationRequired())
      {
        eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PROPERTY__VALUE, oldValue, value));
      }
    }

    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PROPERTY__SECURE, oldSecure, secure));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Property getAncestor()
  {
    String name = getName();
    if (name == null)
    {
      return null;
    }

    PreferenceNode parent = getParent();
    if (parent == null)
    {
      return null;
    }

    for (PreferenceNode preferenceNode = parent.getAncestor(); preferenceNode != null; preferenceNode = preferenceNode.getAncestor())
    {
      Property property = preferenceNode.getProperty(name);
      if (property != null)
      {
        return property;
      }
    }

    return null;
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
      case PreferencesPackage.PROPERTY__PARENT:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetParent((PreferenceNode)otherEnd, msgs);
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
      case PreferencesPackage.PROPERTY__PARENT:
        return basicSetParent(null, msgs);
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
      case PreferencesPackage.PROPERTY__PARENT:
        return eInternalContainer().eInverseRemove(this, PreferencesPackage.PREFERENCE_NODE__PROPERTIES, PreferenceNode.class, msgs);
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
      case PreferencesPackage.PROPERTY__PARENT:
        return getParent();
      case PreferencesPackage.PROPERTY__VALUE:
        return getValue();
      case PreferencesPackage.PROPERTY__NON_DEFAULT:
        return isNonDefault();
      case PreferencesPackage.PROPERTY__SECURE:
        return isSecure();
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
      case PreferencesPackage.PROPERTY__PARENT:
        setParent((PreferenceNode)newValue);
        return;
      case PreferencesPackage.PROPERTY__VALUE:
        setValue((String)newValue);
        return;
      case PreferencesPackage.PROPERTY__SECURE:
        setSecure((Boolean)newValue);
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
      case PreferencesPackage.PROPERTY__PARENT:
        setParent((PreferenceNode)null);
        return;
      case PreferencesPackage.PROPERTY__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
      case PreferencesPackage.PROPERTY__SECURE:
        setSecure(SECURE_EDEFAULT);
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
      case PreferencesPackage.PROPERTY__PARENT:
        return getParent() != null;
      case PreferencesPackage.PROPERTY__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case PreferencesPackage.PROPERTY__NON_DEFAULT:
        return isNonDefault() != NON_DEFAULT_EDEFAULT;
      case PreferencesPackage.PROPERTY__SECURE:
        return secure != SECURE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case PreferencesPackage.PROPERTY___GET_ANCESTOR:
        return getAncestor();
    }
    return super.eInvoke(operationID, arguments);
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
    result.append(" (value: ");
    result.append(value);
    result.append(", secure: ");
    result.append(secure);
    result.append(')');
    return result.toString();
  }

  @Override
  public URI getAbsolutePath()
  {
    String name = getName();
    if (name == null)
    {
      return null;
    }

    PreferenceNode parent = getParent();
    if (parent == null)
    {
      return URI.createHierarchicalURI(null, null, null, new String[] { URI.encodeSegment(name, false) }, null, null);
    }

    URI parentAbsolutePath = parent.getAbsolutePath();
    if (parentAbsolutePath == null)
    {
      return null;
    }

    return parentAbsolutePath.appendSegment(URI.encodeSegment(name, false));
  }

  @Override
  public URI getRelativePath()
  {
    PreferenceNode parent = getParent();
    if (parent == null)
    {
      return null;
    }

    URI parentRelativePath = parent.getRelativePath();
    if (parentRelativePath == null)
    {
      return null;
    }

    return parentRelativePath.appendSegment(URI.encodeSegment(name, false));
  }

  @Override
  public PreferenceNode getScope()
  {
    PreferenceNode parent = getParent();
    if (parent != null)
    {
      return parent.getScope();
    }

    return null;
  }
} // PropertyImpl

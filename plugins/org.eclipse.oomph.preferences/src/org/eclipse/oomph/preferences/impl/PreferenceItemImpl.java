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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.preferences.PreferenceItem;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Preference Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getRoot <em>Root</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getScope <em>Scope</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getAbsolutePath <em>Absolute Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getRelativePath <em>Relative Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceItemImpl#getAncestor <em>Ancestor</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class PreferenceItemImpl extends ModelElementImpl implements PreferenceItem
{
  /**
   * The default value of the '{@link #getAbsolutePath() <em>Absolute Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAbsolutePath()
   * @generated
   * @ordered
   */
  protected static final URI ABSOLUTE_PATH_EDEFAULT = null;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getRelativePath() <em>Relative Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativePath()
   * @generated
   * @ordered
   */
  protected static final URI RELATIVE_PATH_EDEFAULT = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PreferenceItemImpl()
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
    return PreferencesPackage.Literals.PREFERENCE_ITEM;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public PreferenceNode getRoot()
  {
    PreferenceNode parent = getParent();
    if (parent == null)
    {
      if (this instanceof PreferenceNode)
      {
        return (PreferenceNode)this;
      }

      return null;
    }

    return parent.getRoot();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract URI getAbsolutePath();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PREFERENCE_ITEM__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract URI getRelativePath();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract PreferenceItem getAncestor();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract PreferenceNode getScope();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract PreferenceNode getParent();

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
      case PreferencesPackage.PREFERENCE_ITEM__ROOT:
        return getRoot();
      case PreferencesPackage.PREFERENCE_ITEM__SCOPE:
        return getScope();
      case PreferencesPackage.PREFERENCE_ITEM__ABSOLUTE_PATH:
        return getAbsolutePath();
      case PreferencesPackage.PREFERENCE_ITEM__NAME:
        return getName();
      case PreferencesPackage.PREFERENCE_ITEM__RELATIVE_PATH:
        return getRelativePath();
      case PreferencesPackage.PREFERENCE_ITEM__ANCESTOR:
        return getAncestor();
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
      case PreferencesPackage.PREFERENCE_ITEM__NAME:
        setName((String)newValue);
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
      case PreferencesPackage.PREFERENCE_ITEM__NAME:
        setName(NAME_EDEFAULT);
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
      case PreferencesPackage.PREFERENCE_ITEM__ROOT:
        return getRoot() != null;
      case PreferencesPackage.PREFERENCE_ITEM__SCOPE:
        return getScope() != null;
      case PreferencesPackage.PREFERENCE_ITEM__ABSOLUTE_PATH:
        return ABSOLUTE_PATH_EDEFAULT == null ? getAbsolutePath() != null : !ABSOLUTE_PATH_EDEFAULT.equals(getAbsolutePath());
      case PreferencesPackage.PREFERENCE_ITEM__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case PreferencesPackage.PREFERENCE_ITEM__RELATIVE_PATH:
        return RELATIVE_PATH_EDEFAULT == null ? getRelativePath() != null : !RELATIVE_PATH_EDEFAULT.equals(getRelativePath());
      case PreferencesPackage.PREFERENCE_ITEM__ANCESTOR:
        return getAncestor() != null;
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
      case PreferencesPackage.PREFERENCE_ITEM___GET_PARENT:
        return getParent();
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

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} // PreferenceItemImpl

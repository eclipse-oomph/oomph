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
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.TextModification;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Text Modification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.TextModificationImpl#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.TextModificationImpl#getSubstitutions <em>Substitutions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextModificationImpl extends ModelElementImpl implements TextModification
{
  /**
   * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPattern()
   * @generated
   * @ordered
   */
  protected static final String PATTERN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPattern()
   * @generated
   * @ordered
   */
  protected String pattern = PATTERN_EDEFAULT;

  /**
   * The cached value of the '{@link #getSubstitutions() <em>Substitutions</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubstitutions()
   * @generated
   * @ordered
   */
  protected EList<String> substitutions;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TextModificationImpl()
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
    return SetupPackage.Literals.TEXT_MODIFICATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPattern()
  {
    return pattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPattern(String newPattern)
  {
    String oldPattern = pattern;
    pattern = newPattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.TEXT_MODIFICATION__PATTERN, oldPattern, pattern));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getSubstitutions()
  {
    if (substitutions == null)
    {
      substitutions = new EDataTypeUniqueEList<String>(String.class, this, SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS);
    }
    return substitutions;
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
      case SetupPackage.TEXT_MODIFICATION__PATTERN:
        return getPattern();
      case SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS:
        return getSubstitutions();
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
      case SetupPackage.TEXT_MODIFICATION__PATTERN:
        setPattern((String)newValue);
        return;
      case SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS:
        getSubstitutions().clear();
        getSubstitutions().addAll((Collection<? extends String>)newValue);
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
      case SetupPackage.TEXT_MODIFICATION__PATTERN:
        setPattern(PATTERN_EDEFAULT);
        return;
      case SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS:
        getSubstitutions().clear();
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
      case SetupPackage.TEXT_MODIFICATION__PATTERN:
        return PATTERN_EDEFAULT == null ? pattern != null : !PATTERN_EDEFAULT.equals(pattern);
      case SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS:
        return substitutions != null && !substitutions.isEmpty();
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
    result.append(" (pattern: ");
    result.append(pattern);
    result.append(", substitutions: ");
    result.append(substitutions);
    result.append(')');
    return result.toString();
  }

} // TextModificationImpl

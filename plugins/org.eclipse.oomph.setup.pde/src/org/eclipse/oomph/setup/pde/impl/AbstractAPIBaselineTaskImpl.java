/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract API Baseline Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.AbstractAPIBaselineTaskImpl#isActivate <em>Activate</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractAPIBaselineTaskImpl extends SetupTaskImpl implements AbstractAPIBaselineTask
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

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
   * The default value of the '{@link #isActivate() <em>Activate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isActivate()
   * @generated
   * @ordered
   */
  protected static final boolean ACTIVATE_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isActivate() <em>Activate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isActivate()
   * @generated
   * @ordered
   */
  protected boolean activate = ACTIVATE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AbstractAPIBaselineTaskImpl()
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
    return PDEPackage.Literals.ABSTRACT_API_BASELINE_TASK;
  }

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
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isActivate()
  {
    return activate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActivate(boolean newActivate)
  {
    boolean oldActivate = activate;
    activate = newActivate;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE, oldActivate, activate));
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
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME:
        return getName();
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE:
        return isActivate();
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
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME:
        setName((String)newValue);
        return;
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE:
        setActivate((Boolean)newValue);
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
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME:
        setName(NAME_EDEFAULT);
        return;
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE:
        setActivate(ACTIVATE_EDEFAULT);
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
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE:
        return activate != ACTIVATE_EDEFAULT;
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
    result.append(" (name: ");
    result.append(name);
    result.append(", activate: ");
    result.append(activate);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 50;
  }

} // AbstractAPIBaselineTaskImpl

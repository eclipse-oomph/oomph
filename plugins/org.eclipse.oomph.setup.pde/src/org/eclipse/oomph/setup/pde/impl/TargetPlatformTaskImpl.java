/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.pde.TargetPlatformTask;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.pde.core.target.ITargetDefinition;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Target Platform Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.TargetPlatformTaskImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TargetPlatformTaskImpl extends SetupTaskImpl implements TargetPlatformTask
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

  private ITargetDefinition targetDefinition;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TargetPlatformTaskImpl()
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
    return PDEPackage.Literals.TARGET_PLATFORM_TASK;
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
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.TARGET_PLATFORM_TASK__NAME, oldName, name));
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
      case PDEPackage.TARGET_PLATFORM_TASK__NAME:
        return getName();
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
      case PDEPackage.TARGET_PLATFORM_TASK__NAME:
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
      case PDEPackage.TARGET_PLATFORM_TASK__NAME:
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
      case PDEPackage.TARGET_PLATFORM_TASK__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 50;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (context.getTrigger() == Trigger.MANUAL)
    {
      return true;
    }

    targetDefinition = TargetPlatformUtil.getTargetDefinition(getName());

    ITargetDefinition activeTargetDefinition = TargetPlatformUtil.getActiveTargetDefinition();
    return targetDefinition == null || activeTargetDefinition == null || !targetDefinition.getHandle().equals(activeTargetDefinition.getHandle());
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    if (targetDefinition == null)
    {
      targetDefinition = TargetPlatformUtil.getTargetDefinition(getName());
    }

    if (targetDefinition != null)
    {
      TargetPlatformUtil.activateTargetDefinition(targetDefinition, context.getProgressMonitor(true));
    }
  }

} // TargetPlatformTaskImpl

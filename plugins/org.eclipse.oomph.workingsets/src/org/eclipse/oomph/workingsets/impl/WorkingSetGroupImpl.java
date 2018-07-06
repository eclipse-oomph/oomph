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
package org.eclipse.oomph.workingsets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Working Set Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.workingsets.impl.WorkingSetGroupImpl#getWorkingSets <em>Working Sets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WorkingSetGroupImpl extends ModelElementImpl implements WorkingSetGroup
{
  /**
   * The cached value of the '{@link #getWorkingSets() <em>Working Sets</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWorkingSets()
   * @generated
   * @ordered
   */
  protected EList<WorkingSet> workingSets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WorkingSetGroupImpl()
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
    return WorkingSetsPackage.Literals.WORKING_SET_GROUP;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<WorkingSet> getWorkingSets()
  {
    if (workingSets == null)
    {
      workingSets = new EObjectContainmentEList<WorkingSet>(WorkingSet.class, this, WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS);
    }
    return workingSets;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public WorkingSet getWorkingSet(String name)
  {
    for (WorkingSet workingSet : getWorkingSets())
    {
      if (name.equals(workingSet.getName()))
      {
        return workingSet;
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
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS:
        return ((InternalEList<?>)getWorkingSets()).basicRemove(otherEnd, msgs);
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
      case WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS:
        return getWorkingSets();
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
      case WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS:
        getWorkingSets().clear();
        getWorkingSets().addAll((Collection<? extends WorkingSet>)newValue);
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
      case WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS:
        getWorkingSets().clear();
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
      case WorkingSetsPackage.WORKING_SET_GROUP__WORKING_SETS:
        return workingSets != null && !workingSets.isEmpty();
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
      case WorkingSetsPackage.WORKING_SET_GROUP___GET_WORKING_SET__STRING:
        return getWorkingSet((String)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

} // WorkingSetGroupImpl

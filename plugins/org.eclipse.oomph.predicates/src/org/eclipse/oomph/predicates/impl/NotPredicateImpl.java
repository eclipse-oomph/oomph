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
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.predicates.NotPredicate;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.IResource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Not Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.impl.NotPredicateImpl#getOperand <em>Operand</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotPredicateImpl extends PredicateImpl implements NotPredicate
{
  /**
   * The cached value of the '{@link #getOperand() <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperand()
   * @generated
   * @ordered
   */
  protected Predicate operand;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NotPredicateImpl()
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
    return PredicatesPackage.Literals.NOT_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Predicate getOperand()
  {
    return operand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOperand(Predicate newOperand, NotificationChain msgs)
  {
    Predicate oldOperand = operand;
    operand = newOperand;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PredicatesPackage.NOT_PREDICATE__OPERAND, oldOperand, newOperand);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperand(Predicate newOperand)
  {
    if (newOperand != operand)
    {
      NotificationChain msgs = null;
      if (operand != null)
      {
        msgs = ((InternalEObject)operand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PredicatesPackage.NOT_PREDICATE__OPERAND, null, msgs);
      }
      if (newOperand != null)
      {
        msgs = ((InternalEObject)newOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PredicatesPackage.NOT_PREDICATE__OPERAND, null, msgs);
      }
      msgs = basicSetOperand(newOperand, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.NOT_PREDICATE__OPERAND, newOperand, newOperand));
    }
  }

  /**
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    Predicate operand = getOperand();
    return operand == null || !operand.matches(resource);
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
      case PredicatesPackage.NOT_PREDICATE__OPERAND:
        return basicSetOperand(null, msgs);
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
      case PredicatesPackage.NOT_PREDICATE__OPERAND:
        return getOperand();
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
      case PredicatesPackage.NOT_PREDICATE__OPERAND:
        setOperand((Predicate)newValue);
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
      case PredicatesPackage.NOT_PREDICATE__OPERAND:
        setOperand((Predicate)null);
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
      case PredicatesPackage.NOT_PREDICATE__OPERAND:
        return operand != null;
    }
    return super.eIsSet(featureID);
  }

} // NotPredicateImpl

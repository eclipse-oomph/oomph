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
package org.eclipse.oomph.workingsets.impl;

import org.eclipse.oomph.predicates.impl.PredicateImpl;
import org.eclipse.oomph.workingsets.InclusionPredicate;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.core.resources.IResource;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inclusion Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.workingsets.impl.InclusionPredicateImpl#getIncludedWorkingSets <em>Included Working Sets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InclusionPredicateImpl extends PredicateImpl implements InclusionPredicate
{
  /**
   * The cached value of the '{@link #getIncludedWorkingSets() <em>Included Working Sets</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIncludedWorkingSets()
   * @generated
   * @ordered
   */
  protected EList<WorkingSet> includedWorkingSets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InclusionPredicateImpl()
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
    return WorkingSetsPackage.Literals.INCLUSION_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<WorkingSet> getIncludedWorkingSets()
  {
    if (includedWorkingSets == null)
    {
      includedWorkingSets = new EObjectResolvingEList<WorkingSet>(WorkingSet.class, this, WorkingSetsPackage.INCLUSION_PREDICATE__INCLUDED_WORKING_SETS);
    }
    return includedWorkingSets;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    for (WorkingSet workingSet : getIncludedWorkingSets())
    {
      if (workingSet.matches(resource))
      {
        return true;
      }
    }
    return false;
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
      case WorkingSetsPackage.INCLUSION_PREDICATE__INCLUDED_WORKING_SETS:
        return getIncludedWorkingSets();
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
      case WorkingSetsPackage.INCLUSION_PREDICATE__INCLUDED_WORKING_SETS:
        getIncludedWorkingSets().clear();
        getIncludedWorkingSets().addAll((Collection<? extends WorkingSet>)newValue);
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
      case WorkingSetsPackage.INCLUSION_PREDICATE__INCLUDED_WORKING_SETS:
        getIncludedWorkingSets().clear();
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
      case WorkingSetsPackage.INCLUSION_PREDICATE__INCLUDED_WORKING_SETS:
        return includedWorkingSets != null && !includedWorkingSets.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // InclusionPredicateImpl

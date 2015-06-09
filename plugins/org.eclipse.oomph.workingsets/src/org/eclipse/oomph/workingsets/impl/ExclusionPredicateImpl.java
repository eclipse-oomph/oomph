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
package org.eclipse.oomph.workingsets.impl;

import org.eclipse.oomph.predicates.impl.PredicateImpl;
import org.eclipse.oomph.workingsets.ExclusionPredicate;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.core.resources.IResource;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exclusion Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.workingsets.impl.ExclusionPredicateImpl#getExcludedWorkingSets <em>Excluded Working Sets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExclusionPredicateImpl extends PredicateImpl implements ExclusionPredicate
{
  /**
   * The cached value of the '{@link #getExcludedWorkingSets() <em>Excluded Working Sets</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExcludedWorkingSets()
   * @generated
   * @ordered
   */
  protected EList<WorkingSet> excludedWorkingSets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExclusionPredicateImpl()
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
    return WorkingSetsPackage.Literals.EXCLUSION_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<WorkingSet> getExcludedWorkingSets()
  {
    if (excludedWorkingSets == null)
    {
      excludedWorkingSets = new EObjectResolvingEList<WorkingSet>(WorkingSet.class, this, WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS);
    }
    return excludedWorkingSets;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    for (WorkingSet workingSet : getExcludedWorkingSets())
    {
      if (workingSet.matches(resource))
      {
        return false;
      }
    }

    return true;
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
      case WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS:
        return getExcludedWorkingSets();
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
      case WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS:
        getExcludedWorkingSets().clear();
        getExcludedWorkingSets().addAll((Collection<? extends WorkingSet>)newValue);
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
      case WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS:
        getExcludedWorkingSets().clear();
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
      case WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS:
        return excludedWorkingSets != null && !excludedWorkingSets.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // ExclusionPredicateImpl

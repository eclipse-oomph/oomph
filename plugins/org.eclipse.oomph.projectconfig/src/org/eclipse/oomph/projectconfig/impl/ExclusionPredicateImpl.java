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
package org.eclipse.oomph.projectconfig.impl;

import org.eclipse.oomph.predicates.impl.PredicateImpl;
import org.eclipse.oomph.projectconfig.ExclusionPredicate;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;

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
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.impl.ExclusionPredicateImpl#getExcludedPreferenceProfiles <em>Excluded Preference Profiles</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExclusionPredicateImpl extends PredicateImpl implements ExclusionPredicate
{
  /**
   * The cached value of the '{@link #getExcludedPreferenceProfiles() <em>Excluded Preference Profiles</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExcludedPreferenceProfiles()
   * @generated
   * @ordered
   */
  protected EList<PreferenceProfile> excludedPreferenceProfiles;

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
    return ProjectConfigPackage.Literals.EXCLUSION_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<PreferenceProfile> getExcludedPreferenceProfiles()
  {
    if (excludedPreferenceProfiles == null)
    {
      excludedPreferenceProfiles = new EObjectResolvingEList<PreferenceProfile>(PreferenceProfile.class, this,
          ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES);
    }
    return excludedPreferenceProfiles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    for (PreferenceProfile preferenceProfile : getExcludedPreferenceProfiles())
    {
      if (preferenceProfile.matches(resource.getProject()))
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
      case ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES:
        return getExcludedPreferenceProfiles();
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
      case ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES:
        getExcludedPreferenceProfiles().clear();
        getExcludedPreferenceProfiles().addAll((Collection<? extends PreferenceProfile>)newValue);
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
      case ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES:
        getExcludedPreferenceProfiles().clear();
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
      case ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES:
        return excludedPreferenceProfiles != null && !excludedPreferenceProfiles.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // ExclusionPredicateImpl

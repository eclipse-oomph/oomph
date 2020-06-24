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
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profile Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl#isIncludeSourceBundles <em>Include Source Bundles</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.ProfileDefinitionImpl#getProfileProperties <em>Profile Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProfileDefinitionImpl extends ModelElementImpl implements ProfileDefinition
{
  /**
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositories()
   * @generated
   * @ordered
   */
  protected EList<Repository> repositories;

  /**
   * The default value of the '{@link #isIncludeSourceBundles() <em>Include Source Bundles</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSourceBundles()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_SOURCE_BUNDLES_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIncludeSourceBundles() <em>Include Source Bundles</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSourceBundles()
   * @generated
   * @ordered
   */
  protected boolean includeSourceBundles = INCLUDE_SOURCE_BUNDLES_EDEFAULT;

  /**
   * The default value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected static final String PROFILE_PROPERTIES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected String profileProperties = PROFILE_PROPERTIES_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProfileDefinitionImpl()
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
    return P2Package.Literals.PROFILE_DEFINITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, P2Package.PROFILE_DEFINITION__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Repository> getRepositories()
  {
    if (repositories == null)
    {
      repositories = new EObjectContainmentEList<Repository>(Repository.class, this, P2Package.PROFILE_DEFINITION__REPOSITORIES);
    }
    return repositories;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeSourceBundles()
  {
    return includeSourceBundles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeSourceBundles(boolean newIncludeSourceBundles)
  {
    boolean oldIncludeSourceBundles = includeSourceBundles;
    includeSourceBundles = newIncludeSourceBundles;
    if (eNotificationRequired())
    {
      eNotify(
          new ENotificationImpl(this, Notification.SET, P2Package.PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES, oldIncludeSourceBundles, includeSourceBundles));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProfileProperties()
  {
    return profileProperties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProfileProperties(String newProfileProperties)
  {
    String oldProfileProperties = profileProperties;
    profileProperties = newProfileProperties;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.PROFILE_DEFINITION__PROFILE_PROPERTIES, oldProfileProperties, profileProperties));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setRequirements(EList<Requirement> requirements)
  {
    EList<Requirement> list = getRequirements();
    list.clear();
    list.addAll(EcoreUtil.copyAll(requirements));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setRepositories(EList<Repository> repositories)
  {
    EList<Repository> list = getRepositories();
    list.clear();
    list.addAll(EcoreUtil.copyAll(repositories));
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
      case P2Package.PROFILE_DEFINITION__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
      case P2Package.PROFILE_DEFINITION__REPOSITORIES:
        return ((InternalEList<?>)getRepositories()).basicRemove(otherEnd, msgs);
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
      case P2Package.PROFILE_DEFINITION__REQUIREMENTS:
        return getRequirements();
      case P2Package.PROFILE_DEFINITION__REPOSITORIES:
        return getRepositories();
      case P2Package.PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES:
        return isIncludeSourceBundles();
      case P2Package.PROFILE_DEFINITION__PROFILE_PROPERTIES:
        return getProfileProperties();
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
      case P2Package.PROFILE_DEFINITION__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case P2Package.PROFILE_DEFINITION__REPOSITORIES:
        getRepositories().clear();
        getRepositories().addAll((Collection<? extends Repository>)newValue);
        return;
      case P2Package.PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES:
        setIncludeSourceBundles((Boolean)newValue);
        return;
      case P2Package.PROFILE_DEFINITION__PROFILE_PROPERTIES:
        setProfileProperties((String)newValue);
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
      case P2Package.PROFILE_DEFINITION__REQUIREMENTS:
        getRequirements().clear();
        return;
      case P2Package.PROFILE_DEFINITION__REPOSITORIES:
        getRepositories().clear();
        return;
      case P2Package.PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES:
        setIncludeSourceBundles(INCLUDE_SOURCE_BUNDLES_EDEFAULT);
        return;
      case P2Package.PROFILE_DEFINITION__PROFILE_PROPERTIES:
        setProfileProperties(PROFILE_PROPERTIES_EDEFAULT);
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
      case P2Package.PROFILE_DEFINITION__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case P2Package.PROFILE_DEFINITION__REPOSITORIES:
        return repositories != null && !repositories.isEmpty();
      case P2Package.PROFILE_DEFINITION__INCLUDE_SOURCE_BUNDLES:
        return includeSourceBundles != INCLUDE_SOURCE_BUNDLES_EDEFAULT;
      case P2Package.PROFILE_DEFINITION__PROFILE_PROPERTIES:
        return PROFILE_PROPERTIES_EDEFAULT == null ? profileProperties != null : !PROFILE_PROPERTIES_EDEFAULT.equals(profileProperties);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case P2Package.PROFILE_DEFINITION___SET_REQUIREMENTS__ELIST:
        setRequirements((EList<Requirement>)arguments.get(0));
        return null;
      case P2Package.PROFILE_DEFINITION___SET_REPOSITORIES__ELIST:
        setRepositories((EList<Repository>)arguments.get(0));
        return null;
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
    result.append(" (includeSourceBundles: "); //$NON-NLS-1$
    result.append(includeSourceBundles);
    result.append(", profileProperties: "); //$NON-NLS-1$
    result.append(profileProperties);
    result.append(')');
    return result.toString();
  }

} // ProfileDefinitionImpl

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
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Targlet</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getRepositoryLists <em>Repository Lists</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getActiveRepositoryList <em>Active Repository List</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getActiveRepositories <em>Active Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeSources <em>Include Sources</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeAllPlatforms <em>Include All Platforms</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TargletImpl extends ModelElementImpl implements Targlet
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
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getSourceLocators() <em>Source Locators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceLocators()
   * @generated
   * @ordered
   */
  protected EList<SourceLocator> sourceLocators;

  /**
   * The cached value of the '{@link #getRepositoryLists() <em>Repository Lists</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositoryLists()
   * @generated
   * @ordered
   */
  protected EList<RepositoryList> repositoryLists;

  /**
   * The default value of the '{@link #getActiveRepositoryList() <em>Active Repository List</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveRepositoryList()
   * @generated
   * @ordered
   */
  protected static final String ACTIVE_REPOSITORY_LIST_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getActiveRepositoryList() <em>Active Repository List</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveRepositoryList()
   * @generated
   * @ordered
   */
  protected String activeRepositoryList = ACTIVE_REPOSITORY_LIST_EDEFAULT;

  /**
   * The default value of the '{@link #isIncludeSources() <em>Include Sources</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSources()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_SOURCES_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isIncludeSources() <em>Include Sources</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSources()
   * @generated
   * @ordered
   */
  protected boolean includeSources = INCLUDE_SOURCES_EDEFAULT;

  /**
   * The default value of the '{@link #isIncludeAllPlatforms() <em>Include All Platforms</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllPlatforms()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_ALL_PLATFORMS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIncludeAllPlatforms() <em>Include All Platforms</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllPlatforms()
   * @generated
   * @ordered
   */
  protected boolean includeAllPlatforms = INCLUDE_ALL_PLATFORMS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TargletImpl()
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
    return TargletPackage.Literals.TARGLET;
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
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__NAME, oldName, name));
    }
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
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, TargletPackage.TARGLET__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<SourceLocator> getSourceLocators()
  {
    if (sourceLocators == null)
    {
      sourceLocators = new EObjectContainmentEList<SourceLocator>(SourceLocator.class, this, TargletPackage.TARGLET__SOURCE_LOCATORS);
    }
    return sourceLocators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<RepositoryList> getRepositoryLists()
  {
    if (repositoryLists == null)
    {
      repositoryLists = new EObjectContainmentEList<RepositoryList>(RepositoryList.class, this, TargletPackage.TARGLET__REPOSITORY_LISTS);
    }
    return repositoryLists;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getActiveRepositoryList()
  {
    return activeRepositoryList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActiveRepositoryList(String newActiveRepositoryList)
  {
    String oldActiveRepositoryList = activeRepositoryList;
    activeRepositoryList = newActiveRepositoryList;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST, oldActiveRepositoryList, activeRepositoryList));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<Repository> getActiveRepositories()
  {
    EList<Repository> result = new EObjectEList<Repository>(Repository.class, this, TargletPackage.TARGLET__ACTIVE_REPOSITORIES);

    EList<RepositoryList> repositoryLists = getRepositoryLists();
    String name = getActiveRepositoryList();
    if (name == null && !repositoryLists.isEmpty())
    {
      result.addAll(repositoryLists.get(0).getRepositories());
      return result;
    }

    if (name != null)
    {
      for (RepositoryList repositoryList : repositoryLists)
      {
        if (name.equals(repositoryList.getName()))
        {
          result.addAll(repositoryList.getRepositories());
          break;
        }
      }
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeSources()
  {
    return includeSources;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeSources(boolean newIncludeSources)
  {
    boolean oldIncludeSources = includeSources;
    includeSources = newIncludeSources;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_SOURCES, oldIncludeSources, includeSources));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeAllPlatforms()
  {
    return includeAllPlatforms;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeAllPlatforms(boolean newIncludeAllPlatforms)
  {
    boolean oldIncludeAllPlatforms = includeAllPlatforms;
    includeAllPlatforms = newIncludeAllPlatforms;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS, oldIncludeAllPlatforms, includeAllPlatforms));
    }
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
      case TargletPackage.TARGLET__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return ((InternalEList<?>)getSourceLocators()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return ((InternalEList<?>)getRepositoryLists()).basicRemove(otherEnd, msgs);
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
      case TargletPackage.TARGLET__NAME:
        return getName();
      case TargletPackage.TARGLET__REQUIREMENTS:
        return getRequirements();
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return getSourceLocators();
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return getRepositoryLists();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        return getActiveRepositoryList();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORIES:
        return getActiveRepositories();
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        return isIncludeSources();
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        return isIncludeAllPlatforms();
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
      case TargletPackage.TARGLET__NAME:
        setName((String)newValue);
        return;
      case TargletPackage.TARGLET__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        getSourceLocators().clear();
        getSourceLocators().addAll((Collection<? extends SourceLocator>)newValue);
        return;
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        getRepositoryLists().clear();
        getRepositoryLists().addAll((Collection<? extends RepositoryList>)newValue);
        return;
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        setActiveRepositoryList((String)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        setIncludeSources((Boolean)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        setIncludeAllPlatforms((Boolean)newValue);
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
      case TargletPackage.TARGLET__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TargletPackage.TARGLET__REQUIREMENTS:
        getRequirements().clear();
        return;
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        getSourceLocators().clear();
        return;
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        getRepositoryLists().clear();
        return;
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        setActiveRepositoryList(ACTIVE_REPOSITORY_LIST_EDEFAULT);
        return;
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        setIncludeSources(INCLUDE_SOURCES_EDEFAULT);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        setIncludeAllPlatforms(INCLUDE_ALL_PLATFORMS_EDEFAULT);
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
      case TargletPackage.TARGLET__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TargletPackage.TARGLET__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return sourceLocators != null && !sourceLocators.isEmpty();
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return repositoryLists != null && !repositoryLists.isEmpty();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        return ACTIVE_REPOSITORY_LIST_EDEFAULT == null ? activeRepositoryList != null : !ACTIVE_REPOSITORY_LIST_EDEFAULT.equals(activeRepositoryList);
      case TargletPackage.TARGLET__ACTIVE_REPOSITORIES:
        return !getActiveRepositories().isEmpty();
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        return includeSources != INCLUDE_SOURCES_EDEFAULT;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        return includeAllPlatforms != INCLUDE_ALL_PLATFORMS_EDEFAULT;
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
    result.append(", activeRepositoryList: ");
    result.append(activeRepositoryList);
    result.append(", includeSources: ");
    result.append(includeSources);
    result.append(", includeAllPlatforms: ");
    result.append(includeAllPlatforms);
    result.append(')');
    return result.toString();
  }

} // TargletImpl

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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ProjectContainer;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectCatalogImpl#getProjects <em>Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectCatalogImpl#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectCatalogImpl extends ScopeImpl implements ProjectCatalog
{
  /**
   * The cached value of the '{@link #getProjects() <em>Projects</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjects()
   * @generated
   * @ordered
   */
  protected EList<Project> projects;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectCatalogImpl()
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
    return SetupPackage.Literals.PROJECT_CATALOG;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Index getIndex()
  {
    if (eContainerFeatureID() != SetupPackage.PROJECT_CATALOG__INDEX)
    {
      return null;
    }
    return (Index)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Index basicGetIndex()
  {
    if (eContainerFeatureID() != SetupPackage.PROJECT_CATALOG__INDEX)
    {
      return null;
    }
    return (Index)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIndex(Index newIndex, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newIndex, SetupPackage.PROJECT_CATALOG__INDEX, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIndex(Index newIndex)
  {
    if (newIndex != eInternalContainer() || eContainerFeatureID() != SetupPackage.PROJECT_CATALOG__INDEX && newIndex != null)
    {
      if (EcoreUtil.isAncestor(this, newIndex))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newIndex != null)
      {
        msgs = ((InternalEObject)newIndex).eInverseAdd(this, SetupPackage.INDEX__PROJECT_CATALOGS, Index.class, msgs);
      }
      msgs = basicSetIndex(newIndex, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PROJECT_CATALOG__INDEX, newIndex, newIndex));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public ProjectContainer getProjectContainer()
  {
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public ProjectCatalog getProjectCatalog()
  {
    return this;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Project> getProjects()
  {
    if (projects == null)
    {
      projects = new EObjectContainmentWithInverseEList.Resolving<Project>(Project.class, this, SetupPackage.PROJECT_CATALOG__PROJECTS,
          SetupPackage.PROJECT__PROJECT_CONTAINER);
    }
    return projects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public ScopeType getType()
  {
    return ScopeType.PROJECT_CATALOG;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getProjects()).basicAdd(otherEnd, msgs);
      case SetupPackage.PROJECT_CATALOG__INDEX:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetIndex((Index)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
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
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        return ((InternalEList<?>)getProjects()).basicRemove(otherEnd, msgs);
      case SetupPackage.PROJECT_CATALOG__INDEX:
        return basicSetIndex(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case SetupPackage.PROJECT_CATALOG__INDEX:
        return eInternalContainer().eInverseRemove(this, SetupPackage.INDEX__PROJECT_CATALOGS, Index.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
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
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        return getProjects();
      case SetupPackage.PROJECT_CATALOG__INDEX:
        if (resolve)
        {
          return getIndex();
        }
        return basicGetIndex();
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
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        getProjects().clear();
        getProjects().addAll((Collection<? extends Project>)newValue);
        return;
      case SetupPackage.PROJECT_CATALOG__INDEX:
        setIndex((Index)newValue);
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
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        getProjects().clear();
        return;
      case SetupPackage.PROJECT_CATALOG__INDEX:
        setIndex((Index)null);
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
      case SetupPackage.PROJECT_CATALOG__PROJECTS:
        return projects != null && !projects.isEmpty();
      case SetupPackage.PROJECT_CATALOG__INDEX:
        return basicGetIndex() != null;
    }
    return super.eIsSet(featureID);
  }

} // ConfigurationImpl

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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ProjectContainer;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;

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
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getProjects <em>Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getStreams <em>Streams</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getProjectContainer <em>Project Container</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getLogicalProjectContainer <em>Logical Project Container</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getParentProject <em>Parent Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ProjectImpl#getProjectCatalog <em>Project Catalog</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectImpl extends ScopeImpl implements Project
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
   * The cached value of the '{@link #getStreams() <em>Streams</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStreams()
   * @generated
   * @ordered
   */
  protected EList<Stream> streams;

  /**
   * The cached value of the '{@link #getLogicalProjectContainer() <em>Logical Project Container</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogicalProjectContainer()
   * @generated
   * @ordered
   */
  protected ProjectContainer logicalProjectContainer;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectImpl()
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
    return SetupPackage.Literals.PROJECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Stream> getStreams()
  {
    if (streams == null)
    {
      streams = new EObjectContainmentWithInverseEList.Resolving<Stream>(Stream.class, this, SetupPackage.PROJECT__STREAMS, SetupPackage.STREAM__PROJECT);
    }
    return streams;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectContainer getProjectContainer()
  {
    if (eContainerFeatureID() != SetupPackage.PROJECT__PROJECT_CONTAINER)
    {
      return null;
    }
    return (ProjectContainer)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectContainer basicGetProjectContainer()
  {
    if (eContainerFeatureID() != SetupPackage.PROJECT__PROJECT_CONTAINER)
    {
      return null;
    }
    return (ProjectContainer)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProjectContainer(ProjectContainer newProjectContainer, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newProjectContainer, SetupPackage.PROJECT__PROJECT_CONTAINER, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProjectContainer(ProjectContainer newProjectContainer)
  {
    if (newProjectContainer != eInternalContainer() || eContainerFeatureID() != SetupPackage.PROJECT__PROJECT_CONTAINER && newProjectContainer != null)
    {
      if (EcoreUtil.isAncestor(this, newProjectContainer))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newProjectContainer != null)
      {
        msgs = ((InternalEObject)newProjectContainer).eInverseAdd(this, SetupPackage.PROJECT_CONTAINER__PROJECTS, ProjectContainer.class, msgs);
      }
      msgs = basicSetProjectContainer(newProjectContainer, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PROJECT__PROJECT_CONTAINER, newProjectContainer, newProjectContainer));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectContainer getLogicalProjectContainer()
  {
    if (logicalProjectContainer != null && logicalProjectContainer.eIsProxy())
    {
      InternalEObject oldLogicalProjectContainer = (InternalEObject)logicalProjectContainer;
      logicalProjectContainer = (ProjectContainer)eResolveProxy(oldLogicalProjectContainer);
      if (logicalProjectContainer != oldLogicalProjectContainer)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER, oldLogicalProjectContainer,
              logicalProjectContainer));
        }
      }
    }
    return logicalProjectContainer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectContainer basicGetLogicalProjectContainer()
  {
    return logicalProjectContainer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLogicalProjectContainer(ProjectContainer newLogicalProjectContainer)
  {
    ProjectContainer oldLogicalProjectContainer = logicalProjectContainer;
    logicalProjectContainer = newLogicalProjectContainer;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER, oldLogicalProjectContainer,
          logicalProjectContainer));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Project getParentProject()
  {
    ProjectContainer projectContainer = getProjectContainer();
    if (projectContainer != null)
    {
      if (projectContainer instanceof Project)
      {
        return (Project)projectContainer;
      }

      return null;
    }

    projectContainer = getLogicalProjectContainer();
    if (projectContainer instanceof Project)
    {
      return (Project)projectContainer;
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public ProjectCatalog getProjectCatalog()
  {
    ProjectContainer projectContainer = getProjectContainer();
    if (projectContainer != null)
    {
      return projectContainer.getProjectCatalog();
    }

    projectContainer = getLogicalProjectContainer();
    if (projectContainer != null)
    {
      return projectContainer.getProjectCatalog();
    }

    return null;
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
      projects = new EObjectContainmentWithInverseEList.Resolving<Project>(Project.class, this, SetupPackage.PROJECT__PROJECTS,
          SetupPackage.PROJECT__PROJECT_CONTAINER);
    }
    return projects;
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
      case SetupPackage.PROJECT__PROJECTS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getProjects()).basicAdd(otherEnd, msgs);
      case SetupPackage.PROJECT__STREAMS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getStreams()).basicAdd(otherEnd, msgs);
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetProjectContainer((ProjectContainer)otherEnd, msgs);
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
      case SetupPackage.PROJECT__PROJECTS:
        return ((InternalEList<?>)getProjects()).basicRemove(otherEnd, msgs);
      case SetupPackage.PROJECT__STREAMS:
        return ((InternalEList<?>)getStreams()).basicRemove(otherEnd, msgs);
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        return basicSetProjectContainer(null, msgs);
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
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        return eInternalContainer().eInverseRemove(this, SetupPackage.PROJECT_CONTAINER__PROJECTS, ProjectContainer.class, msgs);
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
      case SetupPackage.PROJECT__PROJECTS:
        return getProjects();
      case SetupPackage.PROJECT__STREAMS:
        return getStreams();
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        if (resolve)
        {
          return getProjectContainer();
        }
        return basicGetProjectContainer();
      case SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER:
        if (resolve)
        {
          return getLogicalProjectContainer();
        }
        return basicGetLogicalProjectContainer();
      case SetupPackage.PROJECT__PARENT_PROJECT:
        return getParentProject();
      case SetupPackage.PROJECT__PROJECT_CATALOG:
        return getProjectCatalog();
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
      case SetupPackage.PROJECT__PROJECTS:
        getProjects().clear();
        getProjects().addAll((Collection<? extends Project>)newValue);
        return;
      case SetupPackage.PROJECT__STREAMS:
        getStreams().clear();
        getStreams().addAll((Collection<? extends Stream>)newValue);
        return;
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        setProjectContainer((ProjectContainer)newValue);
        return;
      case SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER:
        setLogicalProjectContainer((ProjectContainer)newValue);
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
      case SetupPackage.PROJECT__PROJECTS:
        getProjects().clear();
        return;
      case SetupPackage.PROJECT__STREAMS:
        getStreams().clear();
        return;
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        setProjectContainer((ProjectContainer)null);
        return;
      case SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER:
        setLogicalProjectContainer((ProjectContainer)null);
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
      case SetupPackage.PROJECT__PROJECTS:
        return projects != null && !projects.isEmpty();
      case SetupPackage.PROJECT__STREAMS:
        return streams != null && !streams.isEmpty();
      case SetupPackage.PROJECT__PROJECT_CONTAINER:
        return basicGetProjectContainer() != null;
      case SetupPackage.PROJECT__LOGICAL_PROJECT_CONTAINER:
        return logicalProjectContainer != null;
      case SetupPackage.PROJECT__PARENT_PROJECT:
        return getParentProject() != null;
      case SetupPackage.PROJECT__PROJECT_CATALOG:
        return getProjectCatalog() != null;
    }
    return super.eIsSet(featureID);
  }

  @Override
  public ScopeType getType()
  {
    return ScopeType.PROJECT;
  }

  @Override
  public Scope getParentScope()
  {
    Scope scope = getProjectContainer();
    if (scope == null)
    {
      scope = getLogicalProjectContainer();
    }

    return scope;
  }

} // ProjectImpl

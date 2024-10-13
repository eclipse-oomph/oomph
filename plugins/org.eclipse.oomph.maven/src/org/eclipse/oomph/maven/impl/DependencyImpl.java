/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Project;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.DependencyImpl#getResolvedProject <em>Resolved Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.DependencyImpl#getResolvedManagedDependency <em>Resolved Managed Dependency</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.DependencyImpl#getIncomingResolvedManagedDependencies <em>Incoming Resolved Managed Dependencies</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DependencyImpl extends CoordinateImpl implements Dependency
{
  /**
   * The cached value of the '{@link #getResolvedProject() <em>Resolved Project</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedProject()
   * @generated
   * @ordered
   */
  protected Project resolvedProject;

  /**
   * The cached value of the '{@link #getResolvedManagedDependency() <em>Resolved Managed Dependency</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedManagedDependency()
   * @generated
   * @ordered
   */
  protected Dependency resolvedManagedDependency;

  /**
   * The cached value of the '{@link #getIncomingResolvedManagedDependencies() <em>Incoming Resolved Managed Dependencies</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIncomingResolvedManagedDependencies()
   * @generated
   * @ordered
   */
  protected EList<Dependency> incomingResolvedManagedDependencies;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DependencyImpl()
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
    return MavenPackage.Literals.DEPENDENCY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Project getResolvedProject()
  {
    if (resolvedProject != null && resolvedProject.eIsProxy())
    {
      InternalEObject oldResolvedProject = (InternalEObject)resolvedProject;
      resolvedProject = (Project)eResolveProxy(oldResolvedProject);
      if (resolvedProject != oldResolvedProject)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MavenPackage.DEPENDENCY__RESOLVED_PROJECT, oldResolvedProject, resolvedProject));
        }
      }
    }
    return resolvedProject;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Project basicGetResolvedProject()
  {
    return resolvedProject;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetResolvedProject(Project newResolvedProject, NotificationChain msgs)
  {
    Project oldResolvedProject = resolvedProject;
    resolvedProject = newResolvedProject;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MavenPackage.DEPENDENCY__RESOLVED_PROJECT, oldResolvedProject,
          newResolvedProject);
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
  @Override
  public void setResolvedProject(Project newResolvedProject)
  {
    if (newResolvedProject != resolvedProject)
    {
      NotificationChain msgs = null;
      if (resolvedProject != null)
      {
        msgs = ((InternalEObject)resolvedProject).eInverseRemove(this, MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES, Project.class, msgs);
      }
      if (newResolvedProject != null)
      {
        msgs = ((InternalEObject)newResolvedProject).eInverseAdd(this, MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES, Project.class, msgs);
      }
      msgs = basicSetResolvedProject(newResolvedProject, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.DEPENDENCY__RESOLVED_PROJECT, newResolvedProject, newResolvedProject));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Dependency getResolvedManagedDependency()
  {
    if (resolvedManagedDependency != null && resolvedManagedDependency.eIsProxy())
    {
      InternalEObject oldResolvedManagedDependency = (InternalEObject)resolvedManagedDependency;
      resolvedManagedDependency = (Dependency)eResolveProxy(oldResolvedManagedDependency);
      if (resolvedManagedDependency != oldResolvedManagedDependency)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY, oldResolvedManagedDependency,
              resolvedManagedDependency));
        }
      }
    }
    return resolvedManagedDependency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Dependency basicGetResolvedManagedDependency()
  {
    return resolvedManagedDependency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetResolvedManagedDependency(Dependency newResolvedManagedDependency, NotificationChain msgs)
  {
    Dependency oldResolvedManagedDependency = resolvedManagedDependency;
    resolvedManagedDependency = newResolvedManagedDependency;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY,
          oldResolvedManagedDependency, newResolvedManagedDependency);
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
  @Override
  public void setResolvedManagedDependency(Dependency newResolvedManagedDependency)
  {
    if (newResolvedManagedDependency != resolvedManagedDependency)
    {
      NotificationChain msgs = null;
      if (resolvedManagedDependency != null)
      {
        msgs = ((InternalEObject)resolvedManagedDependency).eInverseRemove(this, MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES,
            Dependency.class, msgs);
      }
      if (newResolvedManagedDependency != null)
      {
        msgs = ((InternalEObject)newResolvedManagedDependency).eInverseAdd(this, MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES,
            Dependency.class, msgs);
      }
      msgs = basicSetResolvedManagedDependency(newResolvedManagedDependency, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY, newResolvedManagedDependency,
          newResolvedManagedDependency));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Dependency> getIncomingResolvedManagedDependencies()
  {
    if (incomingResolvedManagedDependencies == null)
    {
      incomingResolvedManagedDependencies = new EObjectWithInverseResolvingEList<>(Dependency.class, this,
          MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES, MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY);
    }
    return incomingResolvedManagedDependencies;
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        if (resolvedProject != null)
        {
          msgs = ((InternalEObject)resolvedProject).eInverseRemove(this, MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES, Project.class, msgs);
        }
        return basicSetResolvedProject((Project)otherEnd, msgs);
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        if (resolvedManagedDependency != null)
        {
          msgs = ((InternalEObject)resolvedManagedDependency).eInverseRemove(this, MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES,
              Dependency.class, msgs);
        }
        return basicSetResolvedManagedDependency((Dependency)otherEnd, msgs);
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingResolvedManagedDependencies()).basicAdd(otherEnd, msgs);
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        return basicSetResolvedProject(null, msgs);
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        return basicSetResolvedManagedDependency(null, msgs);
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        return ((InternalEList<?>)getIncomingResolvedManagedDependencies()).basicRemove(otherEnd, msgs);
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        if (resolve)
        {
          return getResolvedProject();
        }
        return basicGetResolvedProject();
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        if (resolve)
        {
          return getResolvedManagedDependency();
        }
        return basicGetResolvedManagedDependency();
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        return getIncomingResolvedManagedDependencies();
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        setResolvedProject((Project)newValue);
        return;
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        setResolvedManagedDependency((Dependency)newValue);
        return;
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        getIncomingResolvedManagedDependencies().clear();
        getIncomingResolvedManagedDependencies().addAll((Collection<? extends Dependency>)newValue);
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        setResolvedProject((Project)null);
        return;
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        setResolvedManagedDependency((Dependency)null);
        return;
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        getIncomingResolvedManagedDependencies().clear();
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
      case MavenPackage.DEPENDENCY__RESOLVED_PROJECT:
        return resolvedProject != null;
      case MavenPackage.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY:
        return resolvedManagedDependency != null;
      case MavenPackage.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES:
        return incomingResolvedManagedDependencies != null && !incomingResolvedManagedDependencies.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // DependencyImpl

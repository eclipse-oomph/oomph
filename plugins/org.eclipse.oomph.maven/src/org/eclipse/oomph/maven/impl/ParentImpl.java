/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.util.POMXMLUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parent</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.ParentImpl#getProject <em>Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ParentImpl#getRelativePath <em>Relative Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ParentImpl#getResolvedProject <em>Resolved Project</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParentImpl extends CoordinateImpl implements Parent
{
  /**
   * The default value of the '{@link #getRelativePath() <em>Relative Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativePath()
   * @generated
   * @ordered
   */
  protected static final String RELATIVE_PATH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRelativePath() <em>Relative Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativePath()
   * @generated
   * @ordered
   */
  protected String relativePath = RELATIVE_PATH_EDEFAULT;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParentImpl()
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
    return MavenPackage.Literals.PARENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Project getProject()
  {
    if (eContainerFeatureID() != MavenPackage.PARENT__PROJECT)
    {
      return null;
    }
    return (Project)eInternalContainer();
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
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MavenPackage.PARENT__RESOLVED_PROJECT, oldResolvedProject, resolvedProject));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MavenPackage.PARENT__RESOLVED_PROJECT, oldResolvedProject,
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
        msgs = ((InternalEObject)resolvedProject).eInverseRemove(this, MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES, Project.class, msgs);
      }
      if (newResolvedProject != null)
      {
        msgs = ((InternalEObject)newResolvedProject).eInverseAdd(this, MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES, Project.class, msgs);
      }
      msgs = basicSetResolvedProject(newResolvedProject, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.PARENT__RESOLVED_PROJECT, newResolvedProject, newResolvedProject));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MavenPackage.PARENT__PROJECT:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return eBasicSetContainer(otherEnd, MavenPackage.PARENT__PROJECT, msgs);
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        if (resolvedProject != null)
        {
          msgs = ((InternalEObject)resolvedProject).eInverseRemove(this, MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES, Project.class, msgs);
        }
        return basicSetResolvedProject((Project)otherEnd, msgs);
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
      case MavenPackage.PARENT__PROJECT:
        return eBasicSetContainer(null, MavenPackage.PARENT__PROJECT, msgs);
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        return basicSetResolvedProject(null, msgs);
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
      case MavenPackage.PARENT__PROJECT:
        return eInternalContainer().eInverseRemove(this, MavenPackage.PROJECT__PARENT, Project.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getRelativePath()
  {
    if (relativePath == null)
    {
      Element element = getElement(POMXMLUtil.xpath(RELATIVE_PATH));
      relativePath = element == null ? ".." : element.getTextContent().strip(); //$NON-NLS-1$
    }

    return relativePath;
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
      case MavenPackage.PARENT__PROJECT:
        return getProject();
      case MavenPackage.PARENT__RELATIVE_PATH:
        return getRelativePath();
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        if (resolve)
        {
          return getResolvedProject();
        }
        return basicGetResolvedProject();
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
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        setResolvedProject((Project)newValue);
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
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        setResolvedProject((Project)null);
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
      case MavenPackage.PARENT__PROJECT:
        return getProject() != null;
      case MavenPackage.PARENT__RELATIVE_PATH:
        return RELATIVE_PATH_EDEFAULT == null ? relativePath != null : !RELATIVE_PATH_EDEFAULT.equals(relativePath);
      case MavenPackage.PARENT__RESOLVED_PROJECT:
        return resolvedProject != null;
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (relativePath: "); //$NON-NLS-1$
    result.append(relativePath);
    result.append(')');
    return result.toString();
  }

} // ParentImpl

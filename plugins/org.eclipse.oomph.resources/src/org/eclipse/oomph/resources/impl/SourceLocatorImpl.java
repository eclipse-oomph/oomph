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
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IProject;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Source Locator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getRootFolder <em>Root Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#isLocateNestedProjects <em>Locate Nested Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getPredicates <em>Predicates</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SourceLocatorImpl extends ModelElementImpl implements SourceLocator
{
  /**
   * The default value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected static final String ROOT_FOLDER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected String rootFolder = ROOT_FOLDER_EDEFAULT;

  /**
   * The default value of the '{@link #isLocateNestedProjects() <em>Locate Nested Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLocateNestedProjects()
   * @generated
   * @ordered
   */
  protected static final boolean LOCATE_NESTED_PROJECTS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isLocateNestedProjects() <em>Locate Nested Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLocateNestedProjects()
   * @generated
   * @ordered
   */
  protected boolean locateNestedProjects = LOCATE_NESTED_PROJECTS_EDEFAULT;

  /**
   * The cached value of the '{@link #getPredicates() <em>Predicates</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredicates()
   * @generated
   * @ordered
   */
  protected EList<Predicate> predicates;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SourceLocatorImpl()
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
    return ResourcesPackage.Literals.SOURCE_LOCATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRootFolder()
  {
    return rootFolder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRootFolder(String newRootFolder)
  {
    String oldRootFolder = rootFolder;
    rootFolder = newRootFolder;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER, oldRootFolder, rootFolder));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isLocateNestedProjects()
  {
    return locateNestedProjects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocateNestedProjects(boolean newLocateNestedProjects)
  {
    boolean oldLocateNestedProjects = locateNestedProjects;
    locateNestedProjects = newLocateNestedProjects;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS, oldLocateNestedProjects,
          locateNestedProjects));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Predicate> getPredicates()
  {
    if (predicates == null)
    {
      predicates = new EObjectContainmentEList<Predicate>(Predicate.class, this, ResourcesPackage.SOURCE_LOCATOR__PREDICATES);
    }
    return predicates;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean matches(IProject project)
  {
    return ResourcesUtil.matchesPredicates(project, predicates);
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
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        return ((InternalEList<?>)getPredicates()).basicRemove(otherEnd, msgs);
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        return getRootFolder();
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        return isLocateNestedProjects();
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        return getPredicates();
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        setRootFolder((String)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        setLocateNestedProjects((Boolean)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        getPredicates().clear();
        getPredicates().addAll((Collection<? extends Predicate>)newValue);
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        setRootFolder(ROOT_FOLDER_EDEFAULT);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        setLocateNestedProjects(LOCATE_NESTED_PROJECTS_EDEFAULT);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        getPredicates().clear();
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        return ROOT_FOLDER_EDEFAULT == null ? rootFolder != null : !ROOT_FOLDER_EDEFAULT.equals(rootFolder);
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        return locateNestedProjects != LOCATE_NESTED_PROJECTS_EDEFAULT;
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        return predicates != null && !predicates.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ResourcesPackage.SOURCE_LOCATOR___MATCHES__IPROJECT:
        return matches((IProject)arguments.get(0));
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

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (rootFolder: ");
    result.append(rootFolder);
    result.append(", locateNestedProjects: ");
    result.append(locateNestedProjects);
    result.append(')');
    return result.toString();
  }

} // SourceLocatorImpl

/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.predicates.ImportedPredicate;
import org.eclipse.oomph.predicates.PredicatesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import java.net.URI;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Imported Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.impl.ImportedPredicateImpl#isAccessible <em>Accessible</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ImportedPredicateImpl extends PredicateImpl implements ImportedPredicate
{
  /**
   * The default value of the '{@link #isAccessible() <em>Accessible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAccessible()
   * @generated
   * @ordered
   */
  protected static final boolean ACCESSIBLE_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isAccessible() <em>Accessible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAccessible()
   * @generated
   * @ordered
   */
  protected boolean accessible = ACCESSIBLE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImportedPredicateImpl()
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
    return PredicatesPackage.Literals.IMPORTED_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isAccessible()
  {
    return accessible;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAccessible(boolean newAccessible)
  {
    boolean oldAccessible = accessible;
    accessible = newAccessible;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.IMPORTED_PREDICATE__ACCESSIBLE, oldAccessible, accessible));
    }
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
      case PredicatesPackage.IMPORTED_PREDICATE__ACCESSIBLE:
        return isAccessible();
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
      case PredicatesPackage.IMPORTED_PREDICATE__ACCESSIBLE:
        setAccessible((Boolean)newValue);
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
      case PredicatesPackage.IMPORTED_PREDICATE__ACCESSIBLE:
        setAccessible(ACCESSIBLE_EDEFAULT);
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
      case PredicatesPackage.IMPORTED_PREDICATE__ACCESSIBLE:
        return accessible != ACCESSIBLE_EDEFAULT;
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
    result.append(" (accessible: ");
    result.append(accessible);
    result.append(')');
    return result.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    if (resource != null)
    {
      try
      {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        URI location = resource.getLocationURI();

        if (resource.getType() == IResource.FILE)
        {
          IFile[] files = root.findFilesForLocationURI(location);
          if (files.length != 0)
          {
            return checkAccessible(files);
          }
        }
        else
        {
          IContainer[] containers = root.findContainersForLocationURI(location);
          if (containers.length != 0)
          {
            return checkAccessible(containers);
          }
        }
      }
      catch (Exception ex)
      {
        // Ignore
      }
    }

    return false;
  }

  private boolean checkAccessible(IResource[] resources)
  {
    if (isAccessible())
    {
      for (IResource resource : resources)
      {
        if (resource.isAccessible())
        {
          return true;
        }
      }

      return false;
    }

    return true;
  }

} // ImportedPredicateImpl

/*
 * Copyright (c) 2017 Adrian Price and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Adrian Price <aprice@tibco.com> - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt.impl;

import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRELibrary;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JRE Library</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl#getLibraryPath <em>Library Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl#getExternalAnnotationsPath <em>External Annotations Path</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JRELibraryImpl extends MinimalEObjectImpl.Container implements JRELibrary
{
  /**
   * The default value of the '{@link #getLibraryPath() <em>Library Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLibraryPath()
   * @generated
   * @ordered
   */
  protected static final String LIBRARY_PATH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLibraryPath() <em>Library Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLibraryPath()
   * @generated
   * @ordered
   */
  protected String libraryPath = LIBRARY_PATH_EDEFAULT;

  /**
   * The default value of the '{@link #getExternalAnnotationsPath() <em>External Annotations Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExternalAnnotationsPath()
   * @generated
   * @ordered
   */
  protected static final String EXTERNAL_ANNOTATIONS_PATH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getExternalAnnotationsPath() <em>External Annotations Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExternalAnnotationsPath()
   * @generated
   * @ordered
   */
  protected String externalAnnotationsPath = EXTERNAL_ANNOTATIONS_PATH_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JRELibraryImpl()
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
    return JDTPackage.Literals.JRE_LIBRARY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLibraryPath()
  {
    return libraryPath;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLibraryPath(String newLibraryPath)
  {
    String oldLibraryPath = libraryPath;
    libraryPath = newLibraryPath;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_LIBRARY__LIBRARY_PATH, oldLibraryPath, libraryPath));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getExternalAnnotationsPath()
  {
    return externalAnnotationsPath;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExternalAnnotationsPath(String newExternalAnnotationsPath)
  {
    String oldExternalAnnotationsPath = externalAnnotationsPath;
    externalAnnotationsPath = newExternalAnnotationsPath;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH, oldExternalAnnotationsPath,
          externalAnnotationsPath));
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
      case JDTPackage.JRE_LIBRARY__LIBRARY_PATH:
        return getLibraryPath();
      case JDTPackage.JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH:
        return getExternalAnnotationsPath();
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
      case JDTPackage.JRE_LIBRARY__LIBRARY_PATH:
        setLibraryPath((String)newValue);
        return;
      case JDTPackage.JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH:
        setExternalAnnotationsPath((String)newValue);
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
      case JDTPackage.JRE_LIBRARY__LIBRARY_PATH:
        setLibraryPath(LIBRARY_PATH_EDEFAULT);
        return;
      case JDTPackage.JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH:
        setExternalAnnotationsPath(EXTERNAL_ANNOTATIONS_PATH_EDEFAULT);
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
      case JDTPackage.JRE_LIBRARY__LIBRARY_PATH:
        return LIBRARY_PATH_EDEFAULT == null ? libraryPath != null : !LIBRARY_PATH_EDEFAULT.equals(libraryPath);
      case JDTPackage.JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH:
        return EXTERNAL_ANNOTATIONS_PATH_EDEFAULT == null ? externalAnnotationsPath != null
            : !EXTERNAL_ANNOTATIONS_PATH_EDEFAULT.equals(externalAnnotationsPath);
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
    result.append(" (libraryPath: ");
    result.append(libraryPath);
    result.append(", externalAnnotationsPath: ");
    result.append(externalAnnotationsPath);
    result.append(')');
    return result.toString();
  }

} // JRELibraryImpl

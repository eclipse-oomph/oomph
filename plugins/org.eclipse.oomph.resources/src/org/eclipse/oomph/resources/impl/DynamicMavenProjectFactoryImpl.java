/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.resources.DynamicMavenProjectFactory;
import org.eclipse.oomph.resources.ResourcesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dynamic Maven Project Factory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.impl.DynamicMavenProjectFactoryImpl#getXMLFileName <em>XML File Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DynamicMavenProjectFactoryImpl extends MavenProjectFactoryImpl implements DynamicMavenProjectFactory
{

  /**
   * The default value of the '{@link #getXMLFileName() <em>XML File Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXMLFileName()
   * @generated
   * @ordered
   */
  protected static final String XML_FILE_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getXMLFileName() <em>XML File Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXMLFileName()
   * @generated
   * @ordered
   */
  protected String xMLFileName = XML_FILE_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DynamicMavenProjectFactoryImpl()
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
    return ResourcesPackage.Literals.DYNAMIC_MAVEN_PROJECT_FACTORY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getXMLFileName()
  {
    return xMLFileName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setXMLFileName(String newXMLFileName)
  {
    String oldXMLFileName = xMLFileName;
    xMLFileName = newXMLFileName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY__XML_FILE_NAME, oldXMLFileName, xMLFileName));
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
      case ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY__XML_FILE_NAME:
        return getXMLFileName();
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
      case ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY__XML_FILE_NAME:
        setXMLFileName((String)newValue);
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
      case ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY__XML_FILE_NAME:
        setXMLFileName(XML_FILE_NAME_EDEFAULT);
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
      case ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY__XML_FILE_NAME:
        return XML_FILE_NAME_EDEFAULT == null ? xMLFileName != null : !XML_FILE_NAME_EDEFAULT.equals(xMLFileName);
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
    result.append(" (xMLFileName: "); //$NON-NLS-1$
    result.append(xMLFileName);
    result.append(')');
    return result.toString();
  }

} // DynamicMavenProjectFactoryImpl

/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.junit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Suite Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.junit.TestSuiteType#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuiteType#getPackageName <em>Package Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuiteType()
 * @model kind="class"
 *        extendedMetaData="name='testsuite_._type' kind='elementOnly'"
 * @generated
 */
public class TestSuiteType extends TestSuite
{
  /**
   * The default value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected static final int ID_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected int iD = ID_EDEFAULT;

  /**
   * This is true if the ID attribute has been set.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean iDESet;

  /**
   * The default value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackageName()
   * @generated
   * @ordered
   */
  protected static final String PACKAGE_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackageName()
   * @generated
   * @ordered
   */
  protected String packageName = PACKAGE_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TestSuiteType()
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
    return JUnitPackage.Literals.TEST_SUITE_TYPE;
  }

  /**
   * Returns the value of the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Starts at '0' for the first testsuite and is incremented by 1 for each following testsuite
   * <!-- end-model-doc -->
   * @return the value of the '<em>ID</em>' attribute.
   * @see #isSetID()
   * @see #unsetID()
   * @see #setID(int)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuiteType_ID()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
   *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
   * @generated
   */
  public int getID()
  {
    return iD;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuiteType#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @param newID the new value of the '<em>ID</em>' attribute.
   * @see #isSetID()
   * @see #unsetID()
   * @see #getID()
   * @generated
   */
  public void setID(int newID)
  {
    int oldID = iD;
    iD = newID;
    boolean oldIDESet = iDESet;
    iDESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE_TYPE__ID, oldID, iD, !oldIDESet));
    }
  }

  /**
   * Unsets the value of the '{@link org.eclipse.oomph.junit.TestSuiteType#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @see #isSetID()
   * @see #getID()
   * @see #setID(int)
   * @generated
   */
  public void unsetID()
  {
    int oldID = iD;
    boolean oldIDESet = iDESet;
    iD = ID_EDEFAULT;
    iDESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, JUnitPackage.TEST_SUITE_TYPE__ID, oldID, ID_EDEFAULT, oldIDESet));
    }
  }

  /**
   * Returns whether the value of the '{@link org.eclipse.oomph.junit.TestSuiteType#getID <em>ID</em>}' attribute is set.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @return whether the value of the '<em>ID</em>' attribute is set.
   * @see #unsetID()
   * @see #getID()
   * @see #setID(int)
   * @generated
   */
  public boolean isSetID()
  {
    return iDESet;
  }

  /**
   * Returns the value of the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Derived from testsuite/@name in the non-aggregated documents
   * <!-- end-model-doc -->
   * @return the value of the '<em>Package Name</em>' attribute.
   * @see #setPackageName(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuiteType_PackageName()
   * @model dataType="org.eclipse.emf.ecore.xml.type.Token" required="true"
   *        extendedMetaData="kind='attribute' name='package' namespace='##targetNamespace'"
   * @generated
   */
  public String getPackageName()
  {
    return packageName;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuiteType#getPackageName <em>Package Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newPackageName the new value of the '<em>Package Name</em>' attribute.
   * @see #getPackageName()
   * @generated
   */
  public void setPackageName(String newPackageName)
  {
    String oldPackageName = packageName;
    packageName = newPackageName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE_TYPE__PACKAGE_NAME, oldPackageName, packageName));
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
      case JUnitPackage.TEST_SUITE_TYPE__ID:
        return getID();
      case JUnitPackage.TEST_SUITE_TYPE__PACKAGE_NAME:
        return getPackageName();
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
      case JUnitPackage.TEST_SUITE_TYPE__ID:
        setID((Integer)newValue);
        return;
      case JUnitPackage.TEST_SUITE_TYPE__PACKAGE_NAME:
        setPackageName((String)newValue);
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
      case JUnitPackage.TEST_SUITE_TYPE__ID:
        unsetID();
        return;
      case JUnitPackage.TEST_SUITE_TYPE__PACKAGE_NAME:
        setPackageName(PACKAGE_NAME_EDEFAULT);
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
      case JUnitPackage.TEST_SUITE_TYPE__ID:
        return isSetID();
      case JUnitPackage.TEST_SUITE_TYPE__PACKAGE_NAME:
        return PACKAGE_NAME_EDEFAULT == null ? packageName != null : !PACKAGE_NAME_EDEFAULT.equals(packageName);
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
    result.append(" (iD: "); //$NON-NLS-1$
    if (iDESet)
    {
      result.append(iD);
    }
    else
    {
      result.append("<unset>"); //$NON-NLS-1$
    }
    result.append(", packageName: "); //$NON-NLS-1$
    result.append(packageName);
    result.append(')');
    return result.toString();
  }

} // TestSuiteType

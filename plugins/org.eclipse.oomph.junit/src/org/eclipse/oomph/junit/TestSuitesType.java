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

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Suites Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.junit.TestSuitesType#getTestSuites <em>Test Suites</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuitesType()
 * @model kind="class"
 *        extendedMetaData="name='testsuites_._type' kind='elementOnly'"
 * @generated
 */
public class TestSuitesType extends MinimalEObjectImpl.Container
{
  /**
   * The cached value of the '{@link #getTestSuites() <em>Test Suites</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTestSuites()
   * @generated
   * @ordered
   */
  protected EList<TestSuiteType> testSuites;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TestSuitesType()
  {
    super();
  }

  public void summarize()
  {
    int id = 0;
    for (TestSuiteType testSuite : getTestSuites())
    {
      testSuite.setID(id++);
      testSuite.summarize();
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return JUnitPackage.Literals.TEST_SUITES_TYPE;
  }

  /**
   * Returns the value of the '<em><b>Test Suites</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.junit.TestSuiteType}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Test Suites</em>' containment reference list.
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuitesType_TestSuites()
   * @model containment="true"
   *        extendedMetaData="kind='element' name='testsuite' namespace='##targetNamespace'"
   * @generated
   */
  public EList<TestSuiteType> getTestSuites()
  {
    if (testSuites == null)
    {
      testSuites = new EObjectContainmentEList<>(TestSuiteType.class, this, JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES);
    }
    return testSuites;
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
      case JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES:
        return ((InternalEList<?>)getTestSuites()).basicRemove(otherEnd, msgs);
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
      case JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES:
        return getTestSuites();
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
      case JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES:
        getTestSuites().clear();
        getTestSuites().addAll((Collection<? extends TestSuiteType>)newValue);
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
      case JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES:
        getTestSuites().clear();
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
      case JUnitPackage.TEST_SUITES_TYPE__TEST_SUITES:
        return testSuites != null && !testSuites.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // TestSuitesType

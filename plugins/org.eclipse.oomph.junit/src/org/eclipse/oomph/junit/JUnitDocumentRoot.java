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
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuite <em>Test Suite</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuites <em>Test Suites</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot()
 * @model kind="class"
 *        extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public class JUnitDocumentRoot extends MinimalEObjectImpl.Container
{
  /**
   * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMixed()
   * @generated
   * @ordered
   */
  protected FeatureMap mixed;

  /**
   * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXMLNSPrefixMap()
   * @generated
   * @ordered
   */
  protected EMap<String, String> xMLNSPrefixMap;

  /**
   * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXSISchemaLocation()
   * @generated
   * @ordered
   */
  protected EMap<String, String> xSISchemaLocation;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JUnitDocumentRoot()
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
    return JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT;
  }

  /**
   * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mixed</em>' attribute list.
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot_Mixed()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='elementWildcard' name=':mixed'"
   * @generated
   */
  public FeatureMap getMixed()
  {
    if (mixed == null)
    {
      mixed = new BasicFeatureMap(this, JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED);
    }
    return mixed;
  }

  /**
   * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link java.lang.String},
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>XMLNS Prefix Map</em>' map.
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot_XMLNSPrefixMap()
   * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
   *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
   * @generated
   */
  public EMap<String, String> getXMLNSPrefixMap()
  {
    if (xMLNSPrefixMap == null)
    {
      xMLNSPrefixMap = new EcoreEMap<String, String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this,
          JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    }
    return xMLNSPrefixMap;
  }

  /**
   * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link java.lang.String},
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>XSI Schema Location</em>' map.
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot_XSISchemaLocation()
   * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
   *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
   * @generated
   */
  public EMap<String, String> getXSISchemaLocation()
  {
    if (xSISchemaLocation == null)
    {
      xSISchemaLocation = new EcoreEMap<String, String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this,
          JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    }
    return xSISchemaLocation;
  }

  /**
   * Returns the value of the '<em><b>Test Suite</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Test Suite</em>' containment reference.
   * @see #setTestSuite(TestSuite)
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot_TestSuite()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='testsuite' namespace='##targetNamespace'"
   * @generated
   */
  public TestSuite getTestSuite()
  {
    return (TestSuite)getMixed().get(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITE, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTestSuite(TestSuite newTestSuite, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)getMixed()).basicAdd(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITE, newTestSuite, msgs);
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuite <em>Test Suite</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newTestSuite the new value of the '<em>Test Suite</em>' containment reference.
   * @see #getTestSuite()
   * @generated
   */
  public void setTestSuite(TestSuite newTestSuite)
  {
    ((FeatureMap.Internal)getMixed()).set(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITE, newTestSuite);
  }

  /**
   * Returns the value of the '<em><b>Test Suites</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Contains an aggregation of testsuite results
   * <!-- end-model-doc -->
   * @return the value of the '<em>Test Suites</em>' containment reference.
   * @see #setTestSuites(TestSuitesType)
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot_TestSuites()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='testsuites' namespace='##targetNamespace'"
   * @generated
   */
  public TestSuitesType getTestSuites()
  {
    return (TestSuitesType)getMixed().get(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITES, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTestSuites(TestSuitesType newTestSuites, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)getMixed()).basicAdd(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITES, newTestSuites, msgs);
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuites <em>Test Suites</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newTestSuites the new value of the '<em>Test Suites</em>' containment reference.
   * @see #getTestSuites()
   * @generated
   */
  public void setTestSuites(TestSuitesType newTestSuites)
  {
    ((FeatureMap.Internal)getMixed()).set(JUnitPackage.Literals.JUNIT_DOCUMENT_ROOT__TEST_SUITES, newTestSuites);
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
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED:
        return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITE:
        return basicSetTestSuite(null, msgs);
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITES:
        return basicSetTestSuites(null, msgs);
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
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED:
        if (coreType)
        {
          return getMixed();
        }
        return ((FeatureMap.Internal)getMixed()).getWrapper();
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        if (coreType)
        {
          return getXMLNSPrefixMap();
        }
        else
        {
          return getXMLNSPrefixMap().map();
        }
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        if (coreType)
        {
          return getXSISchemaLocation();
        }
        else
        {
          return getXSISchemaLocation().map();
        }
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITE:
        return getTestSuite();
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITES:
        return getTestSuites();
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
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED:
        ((FeatureMap.Internal)getMixed()).set(newValue);
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITE:
        setTestSuite((TestSuite)newValue);
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITES:
        setTestSuites((TestSuitesType)newValue);
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
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED:
        getMixed().clear();
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        getXMLNSPrefixMap().clear();
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        getXSISchemaLocation().clear();
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITE:
        setTestSuite((TestSuite)null);
        return;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITES:
        setTestSuites((TestSuitesType)null);
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
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__MIXED:
        return mixed != null && !mixed.isEmpty();
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITE:
        return getTestSuite() != null;
      case JUnitPackage.JUNIT_DOCUMENT_ROOT__TEST_SUITES:
        return getTestSuites() != null;
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
    result.append(" (mixed: ");
    result.append(mixed);
    result.append(')');
    return result.toString();
  }

} // JUnitDocumentRoot

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
package org.eclipse.oomph.junit.util;

import org.eclipse.oomph.junit.JUnitDocumentRoot;
import org.eclipse.oomph.junit.JUnitPackage;
import org.eclipse.oomph.junit.ProblemType;
import org.eclipse.oomph.junit.PropertiesType;
import org.eclipse.oomph.junit.PropertyType;
import org.eclipse.oomph.junit.TestCaseType;
import org.eclipse.oomph.junit.TestSuite;
import org.eclipse.oomph.junit.TestSuiteType;
import org.eclipse.oomph.junit.TestSuitesType;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.junit.JUnitPackage
 * @generated
 */
public class JUnitSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static JUnitPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = JUnitPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case JUnitPackage.JUNIT_DOCUMENT_ROOT:
      {
        JUnitDocumentRoot jUnitDocumentRoot = (JUnitDocumentRoot)theEObject;
        T result = caseJUnitDocumentRoot(jUnitDocumentRoot);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.TEST_SUITES_TYPE:
      {
        TestSuitesType testSuitesType = (TestSuitesType)theEObject;
        T result = caseTestSuitesType(testSuitesType);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.TEST_SUITE:
      {
        TestSuite testSuite = (TestSuite)theEObject;
        T result = caseTestSuite(testSuite);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.TEST_SUITE_TYPE:
      {
        TestSuiteType testSuiteType = (TestSuiteType)theEObject;
        T result = caseTestSuiteType(testSuiteType);
        if (result == null)
        {
          result = caseTestSuite(testSuiteType);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.TEST_CASE_TYPE:
      {
        TestCaseType testCaseType = (TestCaseType)theEObject;
        T result = caseTestCaseType(testCaseType);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.PROBLEM_TYPE:
      {
        ProblemType problemType = (ProblemType)theEObject;
        T result = caseProblemType(problemType);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.PROPERTIES_TYPE:
      {
        PropertiesType propertiesType = (PropertiesType)theEObject;
        T result = casePropertiesType(propertiesType);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case JUnitPackage.PROPERTY_TYPE:
      {
        PropertyType propertyType = (PropertyType)theEObject;
        T result = casePropertyType(propertyType);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      default:
        return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJUnitDocumentRoot(JUnitDocumentRoot object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Test Suites Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Test Suites Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTestSuitesType(TestSuitesType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Test Suite</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Test Suite</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTestSuite(TestSuite object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Test Suite Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Test Suite Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTestSuiteType(TestSuiteType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Test Case Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Test Case Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTestCaseType(TestCaseType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Properties Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Properties Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertiesType(PropertiesType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertyType(PropertyType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Problem Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Problem Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProblemType(ProblemType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} // JUnitSwitch

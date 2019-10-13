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

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.junit.JUnitPackage
 * @generated
 */
public class JUnitValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final JUnitValidator INSTANCE = new JUnitValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.junit";

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

  /**
   * The cached base package validator.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XMLTypeValidator xmlTypeValidator;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitValidator()
  {
    super();
    xmlTypeValidator = XMLTypeValidator.INSTANCE;
  }

  /**
   * Returns the package of this validator switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EPackage getEPackage()
  {
    return JUnitPackage.eINSTANCE;
  }

  /**
   * Calls <code>validateXXX</code> for the corresponding classifier of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    switch (classifierID)
    {
      case JUnitPackage.JUNIT_DOCUMENT_ROOT:
        return validateJUnitDocumentRoot((JUnitDocumentRoot)value, diagnostics, context);
      case JUnitPackage.TEST_SUITES_TYPE:
        return validateTestSuitesType((TestSuitesType)value, diagnostics, context);
      case JUnitPackage.TEST_SUITE:
        return validateTestSuite((TestSuite)value, diagnostics, context);
      case JUnitPackage.TEST_SUITE_TYPE:
        return validateTestSuiteType((TestSuiteType)value, diagnostics, context);
      case JUnitPackage.TEST_CASE_TYPE:
        return validateTestCaseType((TestCaseType)value, diagnostics, context);
      case JUnitPackage.PROBLEM_TYPE:
        return validateProblemType((ProblemType)value, diagnostics, context);
      case JUnitPackage.PROPERTIES_TYPE:
        return validatePropertiesType((PropertiesType)value, diagnostics, context);
      case JUnitPackage.PROPERTY_TYPE:
        return validatePropertyType((PropertyType)value, diagnostics, context);
      case JUnitPackage.ISO8601_DATE_TIME:
        return validateISO8601DateTime((Long)value, diagnostics, context);
      case JUnitPackage.NAME_TYPE:
        return validateNameType((String)value, diagnostics, context);
      case JUnitPackage.TIME:
        return validateTime((Double)value, diagnostics, context);
      default:
        return true;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateJUnitDocumentRoot(JUnitDocumentRoot jUnitDocumentRoot, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(jUnitDocumentRoot, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTestSuitesType(TestSuitesType testSuitesType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(testSuitesType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTestSuite(TestSuite testSuite, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(testSuite, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTestSuiteType(TestSuiteType testSuiteType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(testSuiteType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTestCaseType(TestCaseType testCaseType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(testCaseType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePropertiesType(PropertiesType propertiesType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(propertiesType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePropertyType(PropertyType propertyType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(propertyType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProblemType(ProblemType problemType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(problemType, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateISO8601DateTime(long iso8601DateTime, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateNameType(String nameType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    boolean result = validateNameType_MinLength(nameType, diagnostics, context);
    return result;
  }

  /**
   * Validates the MinLength constraint of '<em>Name Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateNameType_MinLength(String nameType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    int length = nameType.length();
    boolean result = length >= 1;
    if (!result && diagnostics != null)
    {
      reportMinLengthViolation(JUnitPackage.Literals.NAME_TYPE, nameType, length, 1, diagnostics, context);
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTime(double time, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    // TODO
    // Specialize this to return a resource locator for messages specific to this validator.
    // Ensure that you remove @generated or mark it @generated NOT
    return super.getResourceLocator();
  }

} // JUnitValidator

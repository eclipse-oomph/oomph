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

import org.eclipse.oomph.junit.util.JUnitValidator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * A model for <a href="https://windyroad.com.au/dl/Open%20Source/JUnit.xsd">JUnit XML</a>.
 * <!-- end-model-doc -->
 * @see org.eclipse.oomph.junit.JUnitFactory
 * @model kind="package"
 *        extendedMetaData="qualified='false'"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore conversionDelegates='http:///org/eclipse/emf/ecore/util/DateConversionDelegate'"
 * @generated
 */
public class JUnitPackage extends EPackageImpl
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String eNAME = "junit";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String eNS_URI = "http://www.eclipse.org/oomph/junit/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String eNS_PREFIX = "junit";

  /**
   * The package content type ID.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String eCONTENT_TYPE = "org.eclipse.oomph.junit";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final JUnitPackage eINSTANCE = org.eclipse.oomph.junit.JUnitPackage.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.JUnitDocumentRoot <em>Document Root</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot
   * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot()
   * @generated
   */
  public static final int JUNIT_DOCUMENT_ROOT = 0;

  /**
   * The feature id for the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT__MIXED = 0;

  /**
   * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

  /**
   * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

  /**
   * The feature id for the '<em><b>Test Suite</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT__TEST_SUITE = 3;

  /**
   * The feature id for the '<em><b>Test Suites</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT__TEST_SUITES = 4;

  /**
   * The number of structural features of the '<em>Document Root</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int JUNIT_DOCUMENT_ROOT_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.TestSuitesType <em>Test Suites Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.TestSuitesType
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuitesType()
   * @generated
   */
  public static final int TEST_SUITES_TYPE = 1;

  /**
   * The feature id for the '<em><b>Test Suites</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITES_TYPE__TEST_SUITES = 0;

  /**
   * The number of structural features of the '<em>Test Suites Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITES_TYPE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.TestSuite <em>Test Suite</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.TestSuite
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite()
   * @generated
   */
  public static final int TEST_SUITE = 2;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__PROPERTIES = 0;

  /**
   * The feature id for the '<em><b>Test Cases</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__TEST_CASES = 1;

  /**
   * The feature id for the '<em><b>System Out</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__SYSTEM_OUT = 2;

  /**
   * The feature id for the '<em><b>System Err</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__SYSTEM_ERR = 3;

  /**
   * The feature id for the '<em><b>Errors</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__ERRORS = 4;

  /**
   * The feature id for the '<em><b>Failures</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__FAILURES = 5;

  /**
   * The feature id for the '<em><b>Host Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__HOST_NAME = 6;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__NAME = 7;

  /**
   * The feature id for the '<em><b>Tests</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__TESTS = 8;

  /**
   * The feature id for the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__TIME = 9;

  /**
   * The feature id for the '<em><b>Timestamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE__TIMESTAMP = 10;

  /**
   * The number of structural features of the '<em>Test Suite</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_FEATURE_COUNT = 11;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.TestSuiteType <em>Test Suite Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.TestSuiteType
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuiteType()
   * @generated
   */
  public static final int TEST_SUITE_TYPE = 3;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__PROPERTIES = TEST_SUITE__PROPERTIES;

  /**
   * The feature id for the '<em><b>Test Cases</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__TEST_CASES = TEST_SUITE__TEST_CASES;

  /**
   * The feature id for the '<em><b>System Out</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__SYSTEM_OUT = TEST_SUITE__SYSTEM_OUT;

  /**
   * The feature id for the '<em><b>System Err</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__SYSTEM_ERR = TEST_SUITE__SYSTEM_ERR;

  /**
   * The feature id for the '<em><b>Errors</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__ERRORS = TEST_SUITE__ERRORS;

  /**
   * The feature id for the '<em><b>Failures</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__FAILURES = TEST_SUITE__FAILURES;

  /**
   * The feature id for the '<em><b>Host Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__HOST_NAME = TEST_SUITE__HOST_NAME;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__NAME = TEST_SUITE__NAME;

  /**
   * The feature id for the '<em><b>Tests</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__TESTS = TEST_SUITE__TESTS;

  /**
   * The feature id for the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__TIME = TEST_SUITE__TIME;

  /**
   * The feature id for the '<em><b>Timestamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__TIMESTAMP = TEST_SUITE__TIMESTAMP;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__ID = TEST_SUITE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE__PACKAGE_NAME = TEST_SUITE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Test Suite Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_SUITE_TYPE_FEATURE_COUNT = TEST_SUITE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.TestCaseType <em>Test Case Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.TestCaseType
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType()
   * @generated
   */
  public static final int TEST_CASE_TYPE = 4;

  /**
   * The feature id for the '<em><b>Error</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE__ERROR = 0;

  /**
   * The feature id for the '<em><b>Failure</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE__FAILURE = 1;

  /**
   * The feature id for the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE__CLASS_NAME = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE__NAME = 3;

  /**
   * The feature id for the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE__TIME = 4;

  /**
   * The number of structural features of the '<em>Test Case Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int TEST_CASE_TYPE_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.PropertiesType <em>Properties Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.PropertiesType
   * @see org.eclipse.oomph.junit.JUnitPackage#getPropertiesType()
   * @generated
   */
  public static final int PROPERTIES_TYPE = 6;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.PropertyType <em>Property Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.PropertyType
   * @see org.eclipse.oomph.junit.JUnitPackage#getPropertyType()
   * @generated
   */
  public static final int PROPERTY_TYPE = 7;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.junit.ProblemType <em>Problem Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.ProblemType
   * @see org.eclipse.oomph.junit.JUnitPackage#getProblemType()
   * @generated
   */
  public static final int PROBLEM_TYPE = 5;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROBLEM_TYPE__VALUE = 0;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROBLEM_TYPE__MESSAGE = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROBLEM_TYPE__TYPE = 2;

  /**
   * The number of structural features of the '<em>Problem Type</em>' class.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROBLEM_TYPE_FEATURE_COUNT = 3;

  /**
   * The feature id for the '<em><b>Property</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROPERTIES_TYPE__PROPERTY = 0;

  /**
   * The number of structural features of the '<em>Properties Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROPERTIES_TYPE_FEATURE_COUNT = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROPERTY_TYPE__NAME = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROPERTY_TYPE__VALUE = 1;

  /**
   * The number of structural features of the '<em>Property Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  public static final int PROPERTY_TYPE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '<em>ISO8601 Date Time</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.JUnitPackage#getISO8601DateTime()
   * @generated
   */
  public static final int ISO8601_DATE_TIME = 8;

  /**
   * The meta object id for the '<em>Name Type</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.oomph.junit.JUnitPackage#getNameType()
   * @generated
   */
  public static final int NAME_TYPE = 9;

  /**
   * The meta object id for the '<em>Time</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.junit.JUnitPackage#getTime()
   * @generated
   */
  public static final int TIME = 10;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jUnitDocumentRootEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass testSuitesTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass testSuiteEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass testSuiteTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass testCaseTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertiesTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertyTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass problemTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType iso8601DateTimeEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType nameTypeEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType timeEDataType = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.oomph.junit.JUnitPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private JUnitPackage()
  {
    super(eNS_URI, JUnitFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link JUnitPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static JUnitPackage init()
  {
    if (isInited)
    {
      return (JUnitPackage)EPackage.Registry.INSTANCE.getEPackage(JUnitPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredJUnitPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    JUnitPackage theJUnitPackage = registeredJUnitPackage instanceof JUnitPackage ? (JUnitPackage)registeredJUnitPackage : new JUnitPackage();

    isInited = true;

    // Initialize simple dependencies
    XMLTypePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theJUnitPackage.createPackageContents();

    // Initialize created meta-data
    theJUnitPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(theJUnitPackage, new EValidator.Descriptor()
    {
      public EValidator getEValidator()
      {
        return JUnitValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    theJUnitPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(JUnitPackage.eNS_URI, theJUnitPackage);
    return theJUnitPackage;
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.JUnitDocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Document Root</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot
   * @generated
   */
  public EClass getJUnitDocumentRoot()
  {
    return jUnitDocumentRootEClass;
  }

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getMixed <em>Mixed</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Mixed</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot#getMixed()
   * @see #getJUnitDocumentRoot()
   * @generated
   */
  public EAttribute getJUnitDocumentRoot_Mixed()
  {
    return (EAttribute)jUnitDocumentRootEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot#getXMLNSPrefixMap()
   * @see #getJUnitDocumentRoot()
   * @generated
   */
  public EReference getJUnitDocumentRoot_XMLNSPrefixMap()
  {
    return (EReference)jUnitDocumentRootEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XSI Schema Location</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot#getXSISchemaLocation()
   * @see #getJUnitDocumentRoot()
   * @generated
   */
  public EReference getJUnitDocumentRoot_XSISchemaLocation()
  {
    return (EReference)jUnitDocumentRootEClass.getEStructuralFeatures().get(2);
  }

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuite <em>Test Suite</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Test Suite</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuite()
   * @see #getJUnitDocumentRoot()
   * @generated
   */
  public EReference getJUnitDocumentRoot_TestSuite()
  {
    return (EReference)jUnitDocumentRootEClass.getEStructuralFeatures().get(3);
  }

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuites <em>Test Suites</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Test Suites</em>'.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot#getTestSuites()
   * @see #getJUnitDocumentRoot()
   * @generated
   */
  public EReference getJUnitDocumentRoot_TestSuites()
  {
    return (EReference)jUnitDocumentRootEClass.getEStructuralFeatures().get(4);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.TestSuitesType <em>Test Suites Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Test Suites Type</em>'.
   * @see org.eclipse.oomph.junit.TestSuitesType
   * @generated
   */
  public EClass getTestSuitesType()
  {
    return testSuitesTypeEClass;
  }

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.junit.TestSuitesType#getTestSuites <em>Test Suites</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Test Suites</em>'.
   * @see org.eclipse.oomph.junit.TestSuitesType#getTestSuites()
   * @see #getTestSuitesType()
   * @generated
   */
  public EReference getTestSuitesType_TestSuites()
  {
    return (EReference)testSuitesTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.TestSuite <em>Test Suite</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Test Suite</em>'.
   * @see org.eclipse.oomph.junit.TestSuite
   * @generated
   */
  public EClass getTestSuite()
  {
    return testSuiteEClass;
  }

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.junit.TestSuite#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Properties</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getProperties()
   * @see #getTestSuite()
   * @generated
   */
  public EReference getTestSuite_Properties()
  {
    return (EReference)testSuiteEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.junit.TestSuite#getTestCases <em>Test Cases</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Test Cases</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getTestCases()
   * @see #getTestSuite()
   * @generated
   */
  public EReference getTestSuite_TestCases()
  {
    return (EReference)testSuiteEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getSystemOut <em>System Out</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>System Out</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getSystemOut()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_SystemOut()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(2);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getSystemErr <em>System Err</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>System Err</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getSystemErr()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_SystemErr()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(3);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getErrors <em>Errors</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Errors</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getErrors()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Errors()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(4);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getFailures <em>Failures</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Failures</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getFailures()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Failures()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(5);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getHostName <em>Host Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Host Name</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getHostName()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_HostName()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(6);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getName()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Name()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(7);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getTests <em>Tests</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Tests</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getTests()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Tests()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(8);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getTime <em>Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Time</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getTime()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Time()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(9);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuite#getTimestamp <em>Timestamp</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Timestamp</em>'.
   * @see org.eclipse.oomph.junit.TestSuite#getTimestamp()
   * @see #getTestSuite()
   * @generated
   */
  public EAttribute getTestSuite_Timestamp()
  {
    return (EAttribute)testSuiteEClass.getEStructuralFeatures().get(10);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.TestSuiteType <em>Test Suite Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Test Suite Type</em>'.
   * @see org.eclipse.oomph.junit.TestSuiteType
   * @generated
   */
  public EClass getTestSuiteType()
  {
    return testSuiteTypeEClass;
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuiteType#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.junit.TestSuiteType#getID()
   * @see #getTestSuiteType()
   * @generated
   */
  public EAttribute getTestSuiteType_ID()
  {
    return (EAttribute)testSuiteTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestSuiteType#getPackageName <em>Package Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Package Name</em>'.
   * @see org.eclipse.oomph.junit.TestSuiteType#getPackageName()
   * @see #getTestSuiteType()
   * @generated
   */
  public EAttribute getTestSuiteType_PackageName()
  {
    return (EAttribute)testSuiteTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.TestCaseType <em>Test Case Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Test Case Type</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType
   * @generated
   */
  public EClass getTestCaseType()
  {
    return testCaseTypeEClass;
  }

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.junit.TestCaseType#getError <em>Error</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Error</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType#getError()
   * @see #getTestCaseType()
   * @generated
   */
  public EReference getTestCaseType_Error()
  {
    return (EReference)testCaseTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.oomph.junit.TestCaseType#getFailure <em>Failure</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Failure</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType#getFailure()
   * @see #getTestCaseType()
   * @generated
   */
  public EReference getTestCaseType_Failure()
  {
    return (EReference)testCaseTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestCaseType#getClassName <em>Class Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Class Name</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType#getClassName()
   * @see #getTestCaseType()
   * @generated
   */
  public EAttribute getTestCaseType_ClassName()
  {
    return (EAttribute)testCaseTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestCaseType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType#getName()
   * @see #getTestCaseType()
   * @generated
   */
  public EAttribute getTestCaseType_Name()
  {
    return (EAttribute)testCaseTypeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.TestCaseType#getTime <em>Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Time</em>'.
   * @see org.eclipse.oomph.junit.TestCaseType#getTime()
   * @see #getTestCaseType()
   * @generated
   */
  public EAttribute getTestCaseType_Time()
  {
    return (EAttribute)testCaseTypeEClass.getEStructuralFeatures().get(4);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.PropertiesType <em>Properties Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Properties Type</em>'.
   * @see org.eclipse.oomph.junit.PropertiesType
   * @generated
   */
  public EClass getPropertiesType()
  {
    return propertiesTypeEClass;
  }

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.junit.PropertiesType#getProperty <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Property</em>'.
   * @see org.eclipse.oomph.junit.PropertiesType#getProperty()
   * @see #getPropertiesType()
   * @generated
   */
  public EReference getPropertiesType_Property()
  {
    return (EReference)propertiesTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.PropertyType <em>Property Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property Type</em>'.
   * @see org.eclipse.oomph.junit.PropertyType
   * @generated
   */
  public EClass getPropertyType()
  {
    return propertyTypeEClass;
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.PropertyType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.junit.PropertyType#getName()
   * @see #getPropertyType()
   * @generated
   */
  public EAttribute getPropertyType_Name()
  {
    return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.PropertyType#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.junit.PropertyType#getValue()
   * @see #getPropertyType()
   * @generated
   */
  public EAttribute getPropertyType_Value()
  {
    return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.junit.ProblemType <em>Problem Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Problem Type</em>'.
   * @see org.eclipse.oomph.junit.ProblemType
   * @generated
   */
  public EClass getProblemType()
  {
    return problemTypeEClass;
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.ProblemType#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.junit.ProblemType#getValue()
   * @see #getProblemType()
   * @generated
   */
  public EAttribute getProblemType_Value()
  {
    return (EAttribute)problemTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.ProblemType#getMessage <em>Message</em>}'.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message</em>'.
   * @see org.eclipse.oomph.junit.ProblemType#getMessage()
   * @see #getProblemType()
   * @generated
   */
  public EAttribute getProblemType_Message()
  {
    return (EAttribute)problemTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.junit.ProblemType#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.junit.ProblemType#getType()
   * @see #getProblemType()
   * @generated
   */
  public EAttribute getProblemType_Type()
  {
    return (EAttribute)problemTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * Returns the meta object for data type '<em>ISO8601 Date Time</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>ISO8601 Date Time</em>'.
   * @model instanceClass="long"
   *        annotation="http:///org/eclipse/emf/ecore/util/DateConversionDelegate format='//SimpleDateFormat/yyyy-MM-dd\'T\'HH:mm:ss'"
   * @generated
   */
  public EDataType getISO8601DateTime()
  {
    return iso8601DateTimeEDataType;
  }

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>Name Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Name Type</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   *        extendedMetaData="name='name_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#token' minLength='1'"
   * @generated
   */
  public EDataType getNameType()
  {
    return nameTypeEDataType;
  }

  /**
   * Returns the meta object for data type '<em>Time</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Time</em>'.
   * @model instanceClass="double"
   * @generated
   */
  public EDataType getTime()
  {
    return timeEDataType;
  }

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  public JUnitFactory getJUnitFactory()
  {
    return (JUnitFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
    {
      return;
    }
    isCreated = true;

    // Create classes and their features
    jUnitDocumentRootEClass = createEClass(JUNIT_DOCUMENT_ROOT);
    createEAttribute(jUnitDocumentRootEClass, JUNIT_DOCUMENT_ROOT__MIXED);
    createEReference(jUnitDocumentRootEClass, JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    createEReference(jUnitDocumentRootEClass, JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    createEReference(jUnitDocumentRootEClass, JUNIT_DOCUMENT_ROOT__TEST_SUITE);
    createEReference(jUnitDocumentRootEClass, JUNIT_DOCUMENT_ROOT__TEST_SUITES);

    testSuitesTypeEClass = createEClass(TEST_SUITES_TYPE);
    createEReference(testSuitesTypeEClass, TEST_SUITES_TYPE__TEST_SUITES);

    testSuiteEClass = createEClass(TEST_SUITE);
    createEReference(testSuiteEClass, TEST_SUITE__PROPERTIES);
    createEReference(testSuiteEClass, TEST_SUITE__TEST_CASES);
    createEAttribute(testSuiteEClass, TEST_SUITE__SYSTEM_OUT);
    createEAttribute(testSuiteEClass, TEST_SUITE__SYSTEM_ERR);
    createEAttribute(testSuiteEClass, TEST_SUITE__ERRORS);
    createEAttribute(testSuiteEClass, TEST_SUITE__FAILURES);
    createEAttribute(testSuiteEClass, TEST_SUITE__HOST_NAME);
    createEAttribute(testSuiteEClass, TEST_SUITE__NAME);
    createEAttribute(testSuiteEClass, TEST_SUITE__TESTS);
    createEAttribute(testSuiteEClass, TEST_SUITE__TIME);
    createEAttribute(testSuiteEClass, TEST_SUITE__TIMESTAMP);

    testSuiteTypeEClass = createEClass(TEST_SUITE_TYPE);
    createEAttribute(testSuiteTypeEClass, TEST_SUITE_TYPE__ID);
    createEAttribute(testSuiteTypeEClass, TEST_SUITE_TYPE__PACKAGE_NAME);

    testCaseTypeEClass = createEClass(TEST_CASE_TYPE);
    createEReference(testCaseTypeEClass, TEST_CASE_TYPE__ERROR);
    createEReference(testCaseTypeEClass, TEST_CASE_TYPE__FAILURE);
    createEAttribute(testCaseTypeEClass, TEST_CASE_TYPE__CLASS_NAME);
    createEAttribute(testCaseTypeEClass, TEST_CASE_TYPE__NAME);
    createEAttribute(testCaseTypeEClass, TEST_CASE_TYPE__TIME);

    problemTypeEClass = createEClass(PROBLEM_TYPE);
    createEAttribute(problemTypeEClass, PROBLEM_TYPE__VALUE);
    createEAttribute(problemTypeEClass, PROBLEM_TYPE__MESSAGE);
    createEAttribute(problemTypeEClass, PROBLEM_TYPE__TYPE);

    propertiesTypeEClass = createEClass(PROPERTIES_TYPE);
    createEReference(propertiesTypeEClass, PROPERTIES_TYPE__PROPERTY);

    propertyTypeEClass = createEClass(PROPERTY_TYPE);
    createEAttribute(propertyTypeEClass, PROPERTY_TYPE__NAME);
    createEAttribute(propertyTypeEClass, PROPERTY_TYPE__VALUE);

    // Create data types
    iso8601DateTimeEDataType = createEDataType(ISO8601_DATE_TIME);
    nameTypeEDataType = createEDataType(NAME_TYPE);
    timeEDataType = createEDataType(TIME);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
    {
      return;
    }
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    testSuiteTypeEClass.getESuperTypes().add(getTestSuite());

    // Initialize classes and features; add operations and parameters
    initEClass(jUnitDocumentRootEClass, JUnitDocumentRoot.class, "JUnitDocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJUnitDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJUnitDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJUnitDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null,
        IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJUnitDocumentRoot_TestSuite(), getTestSuite(), null, "testSuite", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getJUnitDocumentRoot_TestSuites(), getTestSuitesType(), null, "testSuites", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(testSuitesTypeEClass, TestSuitesType.class, "TestSuitesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTestSuitesType_TestSuites(), getTestSuiteType(), null, "testSuites", null, 0, -1, TestSuitesType.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(testSuiteEClass, TestSuite.class, "TestSuite", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTestSuite_Properties(), getPropertiesType(), null, "properties", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTestSuite_TestCases(), getTestCaseType(), null, "testCases", null, 0, -1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_SystemOut(), ecorePackage.getEString(), "systemOut", null, 0, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_SystemErr(), ecorePackage.getEString(), "systemErr", null, 0, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Errors(), theXMLTypePackage.getInt(), "errors", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Failures(), theXMLTypePackage.getInt(), "failures", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_HostName(), getNameType(), "hostName", null, 0, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Name(), getNameType(), "name", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
        IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Tests(), theXMLTypePackage.getInt(), "tests", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Time(), getTime(), "time", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
        IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuite_Timestamp(), getISO8601DateTime(), "timestamp", null, 1, 1, TestSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(testSuiteTypeEClass, TestSuiteType.class, "TestSuiteType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTestSuiteType_ID(), theXMLTypePackage.getInt(), "iD", null, 1, 1, TestSuiteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestSuiteType_PackageName(), theXMLTypePackage.getToken(), "packageName", null, 1, 1, TestSuiteType.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(testCaseTypeEClass, TestCaseType.class, "TestCaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTestCaseType_Error(), getProblemType(), null, "error", null, 0, 1, TestCaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTestCaseType_Failure(), getProblemType(), null, "failure", null, 0, 1, TestCaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestCaseType_ClassName(), theXMLTypePackage.getToken(), "className", null, 1, 1, TestCaseType.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestCaseType_Name(), theXMLTypePackage.getToken(), "name", null, 1, 1, TestCaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTestCaseType_Time(), getTime(), "time", null, 1, 1, TestCaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(problemTypeEClass, ProblemType.class, "ProblemType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProblemType_Value(), ecorePackage.getEString(), "value", null, 0, 1, ProblemType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProblemType_Message(), theXMLTypePackage.getString(), "message", null, 0, 1, ProblemType.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProblemType_Type(), theXMLTypePackage.getString(), "type", null, 1, 1, ProblemType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertiesTypeEClass, PropertiesType.class, "PropertiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPropertiesType_Property(), getPropertyType(), null, "property", null, 0, -1, PropertiesType.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertyTypeEClass, PropertyType.class, "PropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPropertyType_Name(), getNameType(), "name", null, 1, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPropertyType_Value(), theXMLTypePackage.getString(), "value", null, 1, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize data types
    initEDataType(iso8601DateTimeEDataType, long.class, "ISO8601DateTime", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(nameTypeEDataType, String.class, "NameType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(timeEDataType, double.class, "Time", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http:///org/eclipse/emf/ecore/util/DateConversionDelegate
    createDateConversionDelegateAnnotations();
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
    addAnnotation(this, source, new String[] { "qualified", "false" });
    addAnnotation(jUnitDocumentRootEClass, source, new String[] { "name", "", "kind", "mixed" });
    addAnnotation(getJUnitDocumentRoot_Mixed(), source, new String[] { "kind", "elementWildcard", "name", ":mixed" });
    addAnnotation(getJUnitDocumentRoot_XMLNSPrefixMap(), source, new String[] { "kind", "attribute", "name", "xmlns:prefix" });
    addAnnotation(getJUnitDocumentRoot_XSISchemaLocation(), source, new String[] { "kind", "attribute", "name", "xsi:schemaLocation" });
    addAnnotation(getJUnitDocumentRoot_TestSuite(), source, new String[] { "kind", "element", "name", "testsuite", "namespace", "##targetNamespace" });
    addAnnotation(getJUnitDocumentRoot_TestSuites(), source, new String[] { "kind", "element", "name", "testsuites", "namespace", "##targetNamespace" });
    addAnnotation(testSuitesTypeEClass, source, new String[] { "name", "testsuites_._type", "kind", "elementOnly" });
    addAnnotation(getTestSuitesType_TestSuites(), source, new String[] { "kind", "element", "name", "testsuite", "namespace", "##targetNamespace" });
    addAnnotation(testSuiteEClass, source, new String[] { "name", "testsuite", "kind", "elementOnly" });
    addAnnotation(getTestSuite_Properties(), source, new String[] { "kind", "element", "name", "properties", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_TestCases(), source, new String[] { "kind", "element", "name", "testcase", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_SystemOut(), source, new String[] { "kind", "element", "name", "system-out", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_SystemErr(), source, new String[] { "kind", "element", "name", "system-err", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Errors(), source, new String[] { "kind", "attribute", "name", "errors", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Failures(), source, new String[] { "kind", "attribute", "name", "failures", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_HostName(), source, new String[] { "kind", "attribute", "name", "hostname", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Name(), source, new String[] { "kind", "attribute", "name", "name", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Tests(), source, new String[] { "kind", "attribute", "name", "tests", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Time(), source, new String[] { "kind", "attribute", "name", "time", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuite_Timestamp(), source, new String[] { "kind", "attribute", "name", "timestamp", "namespace", "##targetNamespace" });
    addAnnotation(testSuiteTypeEClass, source, new String[] { "name", "testsuite_._type", "kind", "elementOnly" });
    addAnnotation(getTestSuiteType_ID(), source, new String[] { "kind", "attribute", "name", "id", "namespace", "##targetNamespace" });
    addAnnotation(getTestSuiteType_PackageName(), source, new String[] { "kind", "attribute", "name", "package", "namespace", "##targetNamespace" });
    addAnnotation(testCaseTypeEClass, source, new String[] { "name", "testcase_._type", "kind", "elementOnly" });
    addAnnotation(getTestCaseType_Error(), source, new String[] { "kind", "element", "name", "error", "namespace", "##targetNamespace" });
    addAnnotation(getTestCaseType_Failure(), source, new String[] { "kind", "element", "name", "failure", "namespace", "##targetNamespace" });
    addAnnotation(getTestCaseType_ClassName(), source, new String[] { "kind", "attribute", "name", "classname", "namespace", "##targetNamespace" });
    addAnnotation(getTestCaseType_Name(), source, new String[] { "kind", "attribute", "name", "name", "namespace", "##targetNamespace" });
    addAnnotation(getTestCaseType_Time(), source, new String[] { "kind", "attribute", "name", "time", "namespace", "##targetNamespace" });
    addAnnotation(problemTypeEClass, source, new String[] { "name", "problem_._type", "kind", "simple" });
    addAnnotation(getProblemType_Value(), source, new String[] { "name", ":0", "kind", "simple" });
    addAnnotation(getProblemType_Message(), source, new String[] { "kind", "attribute", "name", "message", "namespace", "##targetNamespace" });
    addAnnotation(getProblemType_Type(), source, new String[] { "kind", "attribute", "name", "type", "namespace", "##targetNamespace" });
    addAnnotation(propertiesTypeEClass, source, new String[] { "name", "properties_._type", "kind", "elementOnly" });
    addAnnotation(getPropertiesType_Property(), source, new String[] { "kind", "element", "name", "property", "namespace", "##targetNamespace" });
    addAnnotation(propertyTypeEClass, source, new String[] { "name", "property_._type", "kind", "empty" });
    addAnnotation(getPropertyType_Name(), source, new String[] { "kind", "attribute", "name", "name", "namespace", "##targetNamespace" });
    addAnnotation(getPropertyType_Value(), source, new String[] { "kind", "attribute", "name", "value", "namespace", "##targetNamespace" });
    addAnnotation(nameTypeEDataType, source,
        new String[] { "name", "name_._type", "baseType", "http://www.eclipse.org/emf/2003/XMLType#token", "minLength", "1" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore";
    addAnnotation(this, source, new String[] { "conversionDelegates", "http:///org/eclipse/emf/ecore/util/DateConversionDelegate" });
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/DateConversionDelegate</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createDateConversionDelegateAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/DateConversionDelegate";
    addAnnotation(iso8601DateTimeEDataType, source, new String[] { "format", "//SimpleDateFormat/yyyy-MM-dd\'T\'HH:mm:ss" });
  }

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  public interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.JUnitDocumentRoot <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.JUnitDocumentRoot
     * @see org.eclipse.oomph.junit.JUnitPackage#getJUnitDocumentRoot()
     * @generated
     */
    public static final EClass JUNIT_DOCUMENT_ROOT = eINSTANCE.getJUnitDocumentRoot();

    /**
     * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute JUNIT_DOCUMENT_ROOT__MIXED = eINSTANCE.getJUnitDocumentRoot_Mixed();

    /**
     * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference JUNIT_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getJUnitDocumentRoot_XMLNSPrefixMap();

    /**
     * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference JUNIT_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getJUnitDocumentRoot_XSISchemaLocation();

    /**
     * The meta object literal for the '<em><b>Test Suite</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference JUNIT_DOCUMENT_ROOT__TEST_SUITE = eINSTANCE.getJUnitDocumentRoot_TestSuite();

    /**
     * The meta object literal for the '<em><b>Test Suites</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference JUNIT_DOCUMENT_ROOT__TEST_SUITES = eINSTANCE.getJUnitDocumentRoot_TestSuites();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.TestSuitesType <em>Test Suites Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.TestSuitesType
     * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuitesType()
     * @generated
     */
    public static final EClass TEST_SUITES_TYPE = eINSTANCE.getTestSuitesType();

    /**
     * The meta object literal for the '<em><b>Test Suites</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference TEST_SUITES_TYPE__TEST_SUITES = eINSTANCE.getTestSuitesType_TestSuites();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.TestSuite <em>Test Suite</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.TestSuite
     * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite()
     * @generated
     */
    public static final EClass TEST_SUITE = eINSTANCE.getTestSuite();

    /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference TEST_SUITE__PROPERTIES = eINSTANCE.getTestSuite_Properties();

    /**
     * The meta object literal for the '<em><b>Test Cases</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference TEST_SUITE__TEST_CASES = eINSTANCE.getTestSuite_TestCases();

    /**
     * The meta object literal for the '<em><b>System Out</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__SYSTEM_OUT = eINSTANCE.getTestSuite_SystemOut();

    /**
     * The meta object literal for the '<em><b>System Err</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__SYSTEM_ERR = eINSTANCE.getTestSuite_SystemErr();

    /**
     * The meta object literal for the '<em><b>Errors</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__ERRORS = eINSTANCE.getTestSuite_Errors();

    /**
     * The meta object literal for the '<em><b>Failures</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__FAILURES = eINSTANCE.getTestSuite_Failures();

    /**
     * The meta object literal for the '<em><b>Host Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__HOST_NAME = eINSTANCE.getTestSuite_HostName();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__NAME = eINSTANCE.getTestSuite_Name();

    /**
     * The meta object literal for the '<em><b>Tests</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__TESTS = eINSTANCE.getTestSuite_Tests();

    /**
     * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__TIME = eINSTANCE.getTestSuite_Time();

    /**
     * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE__TIMESTAMP = eINSTANCE.getTestSuite_Timestamp();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.TestSuiteType <em>Test Suite Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.TestSuiteType
     * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuiteType()
     * @generated
     */
    public static final EClass TEST_SUITE_TYPE = eINSTANCE.getTestSuiteType();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE_TYPE__ID = eINSTANCE.getTestSuiteType_ID();

    /**
     * The meta object literal for the '<em><b>Package Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_SUITE_TYPE__PACKAGE_NAME = eINSTANCE.getTestSuiteType_PackageName();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.TestCaseType <em>Test Case Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.TestCaseType
     * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType()
     * @generated
     */
    public static final EClass TEST_CASE_TYPE = eINSTANCE.getTestCaseType();

    /**
     * The meta object literal for the '<em><b>Error</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference TEST_CASE_TYPE__ERROR = eINSTANCE.getTestCaseType_Error();

    /**
     * The meta object literal for the '<em><b>Failure</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference TEST_CASE_TYPE__FAILURE = eINSTANCE.getTestCaseType_Failure();

    /**
     * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_CASE_TYPE__CLASS_NAME = eINSTANCE.getTestCaseType_ClassName();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_CASE_TYPE__NAME = eINSTANCE.getTestCaseType_Name();

    /**
     * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute TEST_CASE_TYPE__TIME = eINSTANCE.getTestCaseType_Time();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.PropertiesType <em>Properties Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.PropertiesType
     * @see org.eclipse.oomph.junit.JUnitPackage#getPropertiesType()
     * @generated
     */
    public static final EClass PROPERTIES_TYPE = eINSTANCE.getPropertiesType();

    /**
     * The meta object literal for the '<em><b>Property</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EReference PROPERTIES_TYPE__PROPERTY = eINSTANCE.getPropertiesType_Property();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.PropertyType <em>Property Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.PropertyType
     * @see org.eclipse.oomph.junit.JUnitPackage#getPropertyType()
     * @generated
     */
    public static final EClass PROPERTY_TYPE = eINSTANCE.getPropertyType();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute PROPERTY_TYPE__NAME = eINSTANCE.getPropertyType_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute PROPERTY_TYPE__VALUE = eINSTANCE.getPropertyType_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.junit.ProblemType <em>Problem Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.ProblemType
     * @see org.eclipse.oomph.junit.JUnitPackage#getProblemType()
     * @generated
     */
    public static final EClass PROBLEM_TYPE = eINSTANCE.getProblemType();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
    	 * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute PROBLEM_TYPE__VALUE = eINSTANCE.getProblemType_Value();

    /**
     * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
     * <!-- begin-user-doc -->
    	 * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute PROBLEM_TYPE__MESSAGE = eINSTANCE.getProblemType_Message();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
    	 * <!-- end-user-doc -->
     * @generated
     */
    public static final EAttribute PROBLEM_TYPE__TYPE = eINSTANCE.getProblemType_Type();

    /**
     * The meta object literal for the '<em>ISO8601 Date Time</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.JUnitPackage#getISO8601DateTime()
     * @generated
     */
    public static final EDataType ISO8601_DATE_TIME = eINSTANCE.getISO8601DateTime();

    /**
     * The meta object literal for the '<em>Name Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.oomph.junit.JUnitPackage#getNameType()
     * @generated
     */
    public static final EDataType NAME_TYPE = eINSTANCE.getNameType();

    /**
     * The meta object literal for the '<em>Time</em>' data type.
     * <!-- begin-user-doc -->
    	 * <!-- end-user-doc -->
     * @see org.eclipse.oomph.junit.JUnitPackage#getTime()
     * @generated
     */
    public static final EDataType TIME = eINSTANCE.getTime();

  }

} // JUnitPackage

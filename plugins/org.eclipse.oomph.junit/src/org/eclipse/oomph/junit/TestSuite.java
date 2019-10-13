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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Suite</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Contains the results of exexuting a testsuite
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getTestCases <em>Test Cases</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getSystemOut <em>System Out</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getSystemErr <em>System Err</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getErrors <em>Errors</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getFailures <em>Failures</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getHostName <em>Host Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getTests <em>Tests</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getTime <em>Time</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestSuite#getTimestamp <em>Timestamp</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite()
 * @model kind="class"
 *        extendedMetaData="name='testsuite' kind='elementOnly'"
 * @generated
 */
public class TestSuite extends MinimalEObjectImpl.Container
{
  /**
   * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProperties()
   * @generated
   * @ordered
   */
  protected PropertiesType properties;

  /**
   * The cached value of the '{@link #getTestCases() <em>Test Cases</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTestCases()
   * @generated
   * @ordered
   */
  protected EList<TestCaseType> testCases;

  /**
   * The default value of the '{@link #getSystemOut() <em>System Out</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemOut()
   * @generated
   * @ordered
   */
  protected static final String SYSTEM_OUT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSystemOut() <em>System Out</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemOut()
   * @generated
   * @ordered
   */
  protected String systemOut = SYSTEM_OUT_EDEFAULT;

  /**
   * The default value of the '{@link #getSystemErr() <em>System Err</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemErr()
   * @generated
   * @ordered
   */
  protected static final String SYSTEM_ERR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSystemErr() <em>System Err</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemErr()
   * @generated
   * @ordered
   */
  protected String systemErr = SYSTEM_ERR_EDEFAULT;

  /**
   * The default value of the '{@link #getErrors() <em>Errors</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getErrors()
   * @generated
   * @ordered
   */
  protected static final int ERRORS_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getErrors() <em>Errors</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getErrors()
   * @generated
   * @ordered
   */
  protected int errors = ERRORS_EDEFAULT;

  /**
   * This is true if the Errors attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean errorsESet;

  /**
   * The default value of the '{@link #getFailures() <em>Failures</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFailures()
   * @generated
   * @ordered
   */
  protected static final int FAILURES_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getFailures() <em>Failures</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFailures()
   * @generated
   * @ordered
   */
  protected int failures = FAILURES_EDEFAULT;

  /**
   * This is true if the Failures attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean failuresESet;

  /**
   * The default value of the '{@link #getHostName() <em>Host Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHostName()
   * @generated
   * @ordered
   */
  protected static final String HOST_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHostName() <em>Host Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHostName()
   * @generated
   * @ordered
   */
  protected String hostName = HOST_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getTests() <em>Tests</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTests()
   * @generated
   * @ordered
   */
  protected static final int TESTS_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getTests() <em>Tests</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTests()
   * @generated
   * @ordered
   */
  protected int tests = TESTS_EDEFAULT;

  /**
   * This is true if the Tests attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean testsESet;

  /**
   * The default value of the '{@link #getTime() <em>Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTime()
   * @generated
   * @ordered
   */
  protected static final double TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getTime() <em>Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTime()
   * @generated
   * @ordered
   */
  protected double time = TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimestamp()
   * @generated
   * @ordered
   */
  protected static final long TIMESTAMP_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimestamp()
   * @generated
   * @ordered
   */
  protected long timestamp = TIMESTAMP_EDEFAULT;

  /**
   * This is true if the Timestamp attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean timestampESet;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TestSuite()
  {
    super();
  }

  public void summarize()
  {
    int count = 0;
    int errorCount = 0;
    int failureCount = 0;
    double totalTime = 0.0;
    for (TestCaseType testCase : getTestCases())
    {
      ++count;

      ProblemType error = testCase.getError();
      if (error != null)
      {
        ++errorCount;
      }

      ProblemType failure = testCase.getFailure();
      if (failure != null)
      {
        ++failureCount;
      }

      double time = testCase.getTime();
      totalTime += time;
    }

    setTests(count);
    setErrors(errorCount);
    setFailures(failureCount);
    setTime(totalTime);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return JUnitPackage.Literals.TEST_SUITE;
  }

  /**
   * Returns the value of the '<em><b>Properties</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Properties (e.g., environment settings) set during test execution
   * <!-- end-model-doc -->
   * @return the value of the '<em>Properties</em>' containment reference.
   * @see #setProperties(PropertiesType)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Properties()
   * @model containment="true" required="true"
   *        extendedMetaData="kind='element' name='properties' namespace='##targetNamespace'"
   * @generated
   */
  public PropertiesType getProperties()
  {
    return properties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProperties(PropertiesType newProperties, NotificationChain msgs)
  {
    PropertiesType oldProperties = properties;
    properties = newProperties;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__PROPERTIES, oldProperties, newProperties);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getProperties <em>Properties</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newProperties the new value of the '<em>Properties</em>' containment reference.
   * @see #getProperties()
   * @generated
   */
  public void setProperties(PropertiesType newProperties)
  {
    if (newProperties != properties)
    {
      NotificationChain msgs = null;
      if (properties != null)
      {
        msgs = properties.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_SUITE__PROPERTIES, null, msgs);
      }
      if (newProperties != null)
      {
        msgs = newProperties.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_SUITE__PROPERTIES, null, msgs);
      }
      msgs = basicSetProperties(newProperties, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__PROPERTIES, newProperties, newProperties));
    }
  }

  /**
   * Returns the value of the '<em><b>Test Cases</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.junit.TestCaseType}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Test Cases</em>' containment reference list.
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_TestCases()
   * @model containment="true"
   *        extendedMetaData="kind='element' name='testcase' namespace='##targetNamespace'"
   * @generated
   */
  public EList<TestCaseType> getTestCases()
  {
    if (testCases == null)
    {
      testCases = new EObjectContainmentEList<TestCaseType>(TestCaseType.class, this, JUnitPackage.TEST_SUITE__TEST_CASES);
    }
    return testCases;
  }

  /**
   * Returns the value of the '<em><b>System Out</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Data that was written to standard out while the test was executed
   * <!-- end-model-doc -->
   * @return the value of the '<em>System Out</em>' attribute.
   * @see #setSystemOut(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_SystemOut()
   * @model extendedMetaData="kind='element' name='system-out' namespace='##targetNamespace'"
   * @generated
   */
  public String getSystemOut()
  {
    return systemOut;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getSystemOut <em>System Out</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newSystemOut the new value of the '<em>System Out</em>' attribute.
   * @see #getSystemOut()
   * @generated
   */
  public void setSystemOut(String newSystemOut)
  {
    String oldSystemOut = systemOut;
    systemOut = newSystemOut;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__SYSTEM_OUT, oldSystemOut, systemOut));
    }
  }

  /**
   * Returns the value of the '<em><b>System Err</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Data that was written to standard error while the test was executed
   * <!-- end-model-doc -->
   * @return the value of the '<em>System Err</em>' attribute.
   * @see #setSystemErr(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_SystemErr()
   * @model extendedMetaData="kind='element' name='system-err' namespace='##targetNamespace'"
   * @generated
   */
  public String getSystemErr()
  {
    return systemErr;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getSystemErr <em>System Err</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newSystemErr the new value of the '<em>System Err</em>' attribute.
   * @see #getSystemErr()
   * @generated
   */
  public void setSystemErr(String newSystemErr)
  {
    String oldSystemErr = systemErr;
    systemErr = newSystemErr;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__SYSTEM_ERR, oldSystemErr, systemErr));
    }
  }

  /**
   * Returns the value of the '<em><b>Errors</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The total number of tests in the suite that errorred. An errored test is one that had an unanticipated problem. e.g., an unchecked throwable; or a problem with the implementation of the test.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Errors</em>' attribute.
   * @see #isSetErrors()
   * @see #unsetErrors()
   * @see #setErrors(int)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Errors()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
   *        extendedMetaData="kind='attribute' name='errors' namespace='##targetNamespace'"
   * @generated
   */
  public int getErrors()
  {
    return errors;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getErrors <em>Errors</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newErrors the new value of the '<em>Errors</em>' attribute.
   * @see #isSetErrors()
   * @see #unsetErrors()
   * @see #getErrors()
   * @generated
   */
  public void setErrors(int newErrors)
  {
    int oldErrors = errors;
    errors = newErrors;
    boolean oldErrorsESet = errorsESet;
    errorsESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__ERRORS, oldErrors, errors, !oldErrorsESet));
    }
  }

  /**
   * Unsets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getErrors <em>Errors</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetErrors()
   * @see #getErrors()
   * @see #setErrors(int)
   * @generated
   */
  public void unsetErrors()
  {
    int oldErrors = errors;
    boolean oldErrorsESet = errorsESet;
    errors = ERRORS_EDEFAULT;
    errorsESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, JUnitPackage.TEST_SUITE__ERRORS, oldErrors, ERRORS_EDEFAULT, oldErrorsESet));
    }
  }

  /**
   * Returns whether the value of the '{@link org.eclipse.oomph.junit.TestSuite#getErrors <em>Errors</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Errors</em>' attribute is set.
   * @see #unsetErrors()
   * @see #getErrors()
   * @see #setErrors(int)
   * @generated
   */
  public boolean isSetErrors()
  {
    return errorsESet;
  }

  /**
   * Returns the value of the '<em><b>Failures</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The total number of tests in the suite that failed. A failure is a test which the code has explicitly failed by using the mechanisms for that purpose. e.g., via an assertEquals
   * <!-- end-model-doc -->
   * @return the value of the '<em>Failures</em>' attribute.
   * @see #isSetFailures()
   * @see #unsetFailures()
   * @see #setFailures(int)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Failures()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
   *        extendedMetaData="kind='attribute' name='failures' namespace='##targetNamespace'"
   * @generated
   */
  public int getFailures()
  {
    return failures;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getFailures <em>Failures</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newFailures the new value of the '<em>Failures</em>' attribute.
   * @see #isSetFailures()
   * @see #unsetFailures()
   * @see #getFailures()
   * @generated
   */
  public void setFailures(int newFailures)
  {
    int oldFailures = failures;
    failures = newFailures;
    boolean oldFailuresESet = failuresESet;
    failuresESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__FAILURES, oldFailures, failures, !oldFailuresESet));
    }
  }

  /**
   * Unsets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getFailures <em>Failures</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetFailures()
   * @see #getFailures()
   * @see #setFailures(int)
   * @generated
   */
  public void unsetFailures()
  {
    int oldFailures = failures;
    boolean oldFailuresESet = failuresESet;
    failures = FAILURES_EDEFAULT;
    failuresESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, JUnitPackage.TEST_SUITE__FAILURES, oldFailures, FAILURES_EDEFAULT, oldFailuresESet));
    }
  }

  /**
   * Returns whether the value of the '{@link org.eclipse.oomph.junit.TestSuite#getFailures <em>Failures</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Failures</em>' attribute is set.
   * @see #unsetFailures()
   * @see #getFailures()
   * @see #setFailures(int)
   * @generated
   */
  public boolean isSetFailures()
  {
    return failuresESet;
  }

  /**
   * Returns the value of the '<em><b>Host Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Host on which the tests were executed. 'localhost' should be used if the hostname cannot be determined.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Host Name</em>' attribute.
   * @see #setHostName(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_HostName()
   * @model dataType="org.eclipse.oomph.junit.NameType"
   *        extendedMetaData="kind='attribute' name='hostname' namespace='##targetNamespace'"
   * @generated
   */
  public String getHostName()
  {
    return hostName;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getHostName <em>Host Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newHostName the new value of the '<em>Host Name</em>' attribute.
   * @see #getHostName()
   * @generated
   */
  public void setHostName(String newHostName)
  {
    String oldHostName = hostName;
    hostName = newHostName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__HOST_NAME, oldHostName, hostName));
    }
  }

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Full class name of the test for non-aggregated testsuite documents. Class name without the package for aggregated testsuites documents
   * <!-- end-model-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Name()
   * @model dataType="org.eclipse.oomph.junit.NameType" required="true"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newName the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__NAME, oldName, name));
    }
  }

  /**
   * Returns the value of the '<em><b>Tests</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The total number of tests in the suite
   * <!-- end-model-doc -->
   * @return the value of the '<em>Tests</em>' attribute.
   * @see #isSetTests()
   * @see #unsetTests()
   * @see #setTests(int)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Tests()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
   *        extendedMetaData="kind='attribute' name='tests' namespace='##targetNamespace'"
   * @generated
   */
  public int getTests()
  {
    return tests;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTests <em>Tests</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newTests the new value of the '<em>Tests</em>' attribute.
   * @see #isSetTests()
   * @see #unsetTests()
   * @see #getTests()
   * @generated
   */
  public void setTests(int newTests)
  {
    int oldTests = tests;
    tests = newTests;
    boolean oldTestsESet = testsESet;
    testsESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__TESTS, oldTests, tests, !oldTestsESet));
    }
  }

  /**
   * Unsets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTests <em>Tests</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTests()
   * @see #getTests()
   * @see #setTests(int)
   * @generated
   */
  public void unsetTests()
  {
    int oldTests = tests;
    boolean oldTestsESet = testsESet;
    tests = TESTS_EDEFAULT;
    testsESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, JUnitPackage.TEST_SUITE__TESTS, oldTests, TESTS_EDEFAULT, oldTestsESet));
    }
  }

  /**
   * Returns whether the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTests <em>Tests</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Tests</em>' attribute is set.
   * @see #unsetTests()
   * @see #getTests()
   * @see #setTests(int)
   * @generated
   */
  public boolean isSetTests()
  {
    return testsESet;
  }

  /**
   * Returns the value of the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Time taken (in seconds) to execute the tests in the suite
   * <!-- end-model-doc -->
   * @return the value of the '<em>Time</em>' attribute.
   * @see #setTime(double)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Time()
   * @model dataType="org.eclipse.oomph.junit.Time" required="true"
   *        extendedMetaData="kind='attribute' name='time' namespace='##targetNamespace'"
   * @generated
   */
  public double getTime()
  {
    return time;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTime <em>Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newTime the new value of the '<em>Time</em>' attribute.
   * @see #getTime()
   * @generated
   */
  public void setTime(double newTime)
  {
    double oldTime = time;
    time = newTime;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__TIME, oldTime, time));
    }
  }

  /**
   * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * when the test was executed. Timezone may not be specified.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Timestamp</em>' attribute.
   * @see #isSetTimestamp()
   * @see #unsetTimestamp()
   * @see #setTimestamp(long)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestSuite_Timestamp()
   * @model unsettable="true" dataType="org.eclipse.oomph.junit.ISO8601DateTime" required="true"
   *        extendedMetaData="kind='attribute' name='timestamp' namespace='##targetNamespace'"
   * @generated
   */
  public long getTimestamp()
  {
    return timestamp;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTimestamp <em>Timestamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newTimestamp the new value of the '<em>Timestamp</em>' attribute.
   * @see #isSetTimestamp()
   * @see #unsetTimestamp()
   * @see #getTimestamp()
   * @generated
   */
  public void setTimestamp(long newTimestamp)
  {
    long oldTimestamp = timestamp;
    timestamp = newTimestamp;
    boolean oldTimestampESet = timestampESet;
    timestampESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_SUITE__TIMESTAMP, oldTimestamp, timestamp, !oldTimestampESet));
    }
  }

  /**
   * Unsets the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTimestamp <em>Timestamp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTimestamp()
   * @see #getTimestamp()
   * @see #setTimestamp(long)
   * @generated
   */
  public void unsetTimestamp()
  {
    long oldTimestamp = timestamp;
    boolean oldTimestampESet = timestampESet;
    timestamp = TIMESTAMP_EDEFAULT;
    timestampESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, JUnitPackage.TEST_SUITE__TIMESTAMP, oldTimestamp, TIMESTAMP_EDEFAULT, oldTimestampESet));
    }
  }

  /**
   * Returns whether the value of the '{@link org.eclipse.oomph.junit.TestSuite#getTimestamp <em>Timestamp</em>}' attribute is set.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @return whether the value of the '<em>Timestamp</em>' attribute is set.
   * @see #unsetTimestamp()
   * @see #getTimestamp()
   * @see #setTimestamp(long)
   * @generated
   */
  public boolean isSetTimestamp()
  {
    return timestampESet;
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
      case JUnitPackage.TEST_SUITE__PROPERTIES:
        return basicSetProperties(null, msgs);
      case JUnitPackage.TEST_SUITE__TEST_CASES:
        return ((InternalEList<?>)getTestCases()).basicRemove(otherEnd, msgs);
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
      case JUnitPackage.TEST_SUITE__PROPERTIES:
        return getProperties();
      case JUnitPackage.TEST_SUITE__TEST_CASES:
        return getTestCases();
      case JUnitPackage.TEST_SUITE__SYSTEM_OUT:
        return getSystemOut();
      case JUnitPackage.TEST_SUITE__SYSTEM_ERR:
        return getSystemErr();
      case JUnitPackage.TEST_SUITE__ERRORS:
        return getErrors();
      case JUnitPackage.TEST_SUITE__FAILURES:
        return getFailures();
      case JUnitPackage.TEST_SUITE__HOST_NAME:
        return getHostName();
      case JUnitPackage.TEST_SUITE__NAME:
        return getName();
      case JUnitPackage.TEST_SUITE__TESTS:
        return getTests();
      case JUnitPackage.TEST_SUITE__TIME:
        return getTime();
      case JUnitPackage.TEST_SUITE__TIMESTAMP:
        return getTimestamp();
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
      case JUnitPackage.TEST_SUITE__PROPERTIES:
        setProperties((PropertiesType)newValue);
        return;
      case JUnitPackage.TEST_SUITE__TEST_CASES:
        getTestCases().clear();
        getTestCases().addAll((Collection<? extends TestCaseType>)newValue);
        return;
      case JUnitPackage.TEST_SUITE__SYSTEM_OUT:
        setSystemOut((String)newValue);
        return;
      case JUnitPackage.TEST_SUITE__SYSTEM_ERR:
        setSystemErr((String)newValue);
        return;
      case JUnitPackage.TEST_SUITE__ERRORS:
        setErrors((Integer)newValue);
        return;
      case JUnitPackage.TEST_SUITE__FAILURES:
        setFailures((Integer)newValue);
        return;
      case JUnitPackage.TEST_SUITE__HOST_NAME:
        setHostName((String)newValue);
        return;
      case JUnitPackage.TEST_SUITE__NAME:
        setName((String)newValue);
        return;
      case JUnitPackage.TEST_SUITE__TESTS:
        setTests((Integer)newValue);
        return;
      case JUnitPackage.TEST_SUITE__TIME:
        setTime((Double)newValue);
        return;
      case JUnitPackage.TEST_SUITE__TIMESTAMP:
        setTimestamp((Long)newValue);
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
      case JUnitPackage.TEST_SUITE__PROPERTIES:
        setProperties((PropertiesType)null);
        return;
      case JUnitPackage.TEST_SUITE__TEST_CASES:
        getTestCases().clear();
        return;
      case JUnitPackage.TEST_SUITE__SYSTEM_OUT:
        setSystemOut(SYSTEM_OUT_EDEFAULT);
        return;
      case JUnitPackage.TEST_SUITE__SYSTEM_ERR:
        setSystemErr(SYSTEM_ERR_EDEFAULT);
        return;
      case JUnitPackage.TEST_SUITE__ERRORS:
        unsetErrors();
        return;
      case JUnitPackage.TEST_SUITE__FAILURES:
        unsetFailures();
        return;
      case JUnitPackage.TEST_SUITE__HOST_NAME:
        setHostName(HOST_NAME_EDEFAULT);
        return;
      case JUnitPackage.TEST_SUITE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case JUnitPackage.TEST_SUITE__TESTS:
        unsetTests();
        return;
      case JUnitPackage.TEST_SUITE__TIME:
        setTime(TIME_EDEFAULT);
        return;
      case JUnitPackage.TEST_SUITE__TIMESTAMP:
        unsetTimestamp();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("null")
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case JUnitPackage.TEST_SUITE__PROPERTIES:
        return properties != null;
      case JUnitPackage.TEST_SUITE__TEST_CASES:
        return testCases != null && !testCases.isEmpty();
      case JUnitPackage.TEST_SUITE__SYSTEM_OUT:
        return SYSTEM_OUT_EDEFAULT == null ? systemOut != null : !SYSTEM_OUT_EDEFAULT.equals(systemOut);
      case JUnitPackage.TEST_SUITE__SYSTEM_ERR:
        return SYSTEM_ERR_EDEFAULT == null ? systemErr != null : !SYSTEM_ERR_EDEFAULT.equals(systemErr);
      case JUnitPackage.TEST_SUITE__ERRORS:
        return isSetErrors();
      case JUnitPackage.TEST_SUITE__FAILURES:
        return isSetFailures();
      case JUnitPackage.TEST_SUITE__HOST_NAME:
        return HOST_NAME_EDEFAULT == null ? hostName != null : !HOST_NAME_EDEFAULT.equals(hostName);
      case JUnitPackage.TEST_SUITE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case JUnitPackage.TEST_SUITE__TESTS:
        return isSetTests();
      case JUnitPackage.TEST_SUITE__TIME:
        return time != TIME_EDEFAULT;
      case JUnitPackage.TEST_SUITE__TIMESTAMP:
        return isSetTimestamp();
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
    result.append(" (systemOut: ");
    result.append(systemOut);
    result.append(", systemErr: ");
    result.append(systemErr);
    result.append(", errors: ");
    if (errorsESet)
    {
      result.append(errors);
    }
    else
    {
      result.append("<unset>");
    }
    result.append(", failures: ");
    if (failuresESet)
    {
      result.append(failures);
    }
    else
    {
      result.append("<unset>");
    }
    result.append(", hostName: ");
    result.append(hostName);
    result.append(", name: ");
    result.append(name);
    result.append(", tests: ");
    if (testsESet)
    {
      result.append(tests);
    }
    else
    {
      result.append("<unset>");
    }
    result.append(", time: ");
    result.append(time);
    result.append(", timestamp: ");
    if (timestampESet)
    {
      result.append(timestamp);
    }
    else
    {
      result.append("<unset>");
    }
    result.append(')');
    return result.toString();
  }

} // TestSuite

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Case Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.junit.TestCaseType#getError <em>Error</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestCaseType#getFailure <em>Failure</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestCaseType#getClassName <em>Class Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestCaseType#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.junit.TestCaseType#getTime <em>Time</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType()
 * @model kind="class"
 *        extendedMetaData="name='testcase_._type' kind='elementOnly'"
 * @generated
 */
public class TestCaseType extends MinimalEObjectImpl.Container
{
  /**
   * The cached value of the '{@link #getError() <em>Error</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getError()
   * @generated
   * @ordered
   */
  protected ProblemType error;

  /**
   * The cached value of the '{@link #getFailure() <em>Failure</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFailure()
   * @generated
   * @ordered
   */
  protected ProblemType failure;

  /**
   * The default value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassName()
   * @generated
   * @ordered
   */
  protected static final String CLASS_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassName()
   * @generated
   * @ordered
   */
  protected String className = CLASS_NAME_EDEFAULT;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TestCaseType()
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
    return JUnitPackage.Literals.TEST_CASE_TYPE;
  }

  /**
   * Returns the value of the '<em><b>Error</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Indicates that the test errored.  An errored test is one that had an unanticipated problem. e.g., an unchecked throwable; or a problem with the implementation of the test. Contains as a text node relevant data for the error, e.g., a stack trace
   * <!-- end-model-doc -->
   * @return the value of the '<em>Error</em>' containment reference.
   * @see #setError(ProblemType)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType_Error()
   * @model containment="true"
   *        extendedMetaData="kind='element' name='error' namespace='##targetNamespace'"
   * @generated
   */
  public ProblemType getError()
  {
    return error;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetError(ProblemType newError, NotificationChain msgs)
  {
    ProblemType oldError = error;
    error = newError;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__ERROR, oldError, newError);
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
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestCaseType#getError <em>Error</em>}' containment reference.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @param newError the new value of the '<em>Error</em>' containment reference.
   * @see #getError()
   * @generated
   */
  public void setError(ProblemType newError)
  {
    if (newError != error)
    {
      NotificationChain msgs = null;
      if (error != null)
      {
        msgs = error.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_CASE_TYPE__ERROR, null, msgs);
      }
      if (newError != null)
      {
        msgs = newError.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_CASE_TYPE__ERROR, null, msgs);
      }
      msgs = basicSetError(newError, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__ERROR, newError, newError));
    }
  }

  /**
   * Returns the value of the '<em><b>Failure</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Indicates that the test failed. A failure is a test which the code has explicitly failed by using the mechanisms for that purpose. e.g., via an assertEquals. Contains as a text node relevant data for the failure, e.g., a stack trace
   * <!-- end-model-doc -->
   * @return the value of the '<em>Failure</em>' containment reference.
   * @see #setFailure(ProblemType)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType_Failure()
   * @model containment="true"
   *        extendedMetaData="kind='element' name='failure' namespace='##targetNamespace'"
   * @generated
   */
  public ProblemType getFailure()
  {
    return failure;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFailure(ProblemType newFailure, NotificationChain msgs)
  {
    ProblemType oldFailure = failure;
    failure = newFailure;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__FAILURE, oldFailure, newFailure);
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
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestCaseType#getFailure <em>Failure</em>}' containment reference.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @param newFailure the new value of the '<em>Failure</em>' containment reference.
   * @see #getFailure()
   * @generated
   */
  public void setFailure(ProblemType newFailure)
  {
    if (newFailure != failure)
    {
      NotificationChain msgs = null;
      if (failure != null)
      {
        msgs = failure.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_CASE_TYPE__FAILURE, null, msgs);
      }
      if (newFailure != null)
      {
        msgs = newFailure.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JUnitPackage.TEST_CASE_TYPE__FAILURE, null, msgs);
      }
      msgs = basicSetFailure(newFailure, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__FAILURE, newFailure, newFailure));
    }
  }

  /**
   * Returns the value of the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Full class name for the class the test method is in.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Class Name</em>' attribute.
   * @see #setClassName(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType_ClassName()
   * @model dataType="org.eclipse.emf.ecore.xml.type.Token" required="true"
   *        extendedMetaData="kind='attribute' name='classname' namespace='##targetNamespace'"
   * @generated
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestCaseType#getClassName <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param newClassName the new value of the '<em>Class Name</em>' attribute.
   * @see #getClassName()
   * @generated
   */
  public void setClassName(String newClassName)
  {
    String oldClassName = className;
    className = newClassName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__CLASS_NAME, oldClassName, className));
    }
  }

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Name of the test method
   * <!-- end-model-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType_Name()
   * @model dataType="org.eclipse.emf.ecore.xml.type.Token" required="true"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestCaseType#getName <em>Name</em>}' attribute.
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
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__NAME, oldName, name));
    }
  }

  /**
   * Returns the value of the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Time taken (in seconds) to execute the test
   * <!-- end-model-doc -->
   * @return the value of the '<em>Time</em>' attribute.
   * @see #setTime(double)
   * @see org.eclipse.oomph.junit.JUnitPackage#getTestCaseType_Time()
   * @model dataType="org.eclipse.oomph.junit.Time" required="true"
   *        extendedMetaData="kind='attribute' name='time' namespace='##targetNamespace'"
   * @generated
   */
  public double getTime()
  {
    return time;
  }

  /**
   * Sets the value of the '{@link org.eclipse.oomph.junit.TestCaseType#getTime <em>Time</em>}' attribute.
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
      eNotify(new ENotificationImpl(this, Notification.SET, JUnitPackage.TEST_CASE_TYPE__TIME, oldTime, time));
    }
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
      case JUnitPackage.TEST_CASE_TYPE__ERROR:
        return basicSetError(null, msgs);
      case JUnitPackage.TEST_CASE_TYPE__FAILURE:
        return basicSetFailure(null, msgs);
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
      case JUnitPackage.TEST_CASE_TYPE__ERROR:
        return getError();
      case JUnitPackage.TEST_CASE_TYPE__FAILURE:
        return getFailure();
      case JUnitPackage.TEST_CASE_TYPE__CLASS_NAME:
        return getClassName();
      case JUnitPackage.TEST_CASE_TYPE__NAME:
        return getName();
      case JUnitPackage.TEST_CASE_TYPE__TIME:
        return getTime();
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
      case JUnitPackage.TEST_CASE_TYPE__ERROR:
        setError((ProblemType)newValue);
        return;
      case JUnitPackage.TEST_CASE_TYPE__FAILURE:
        setFailure((ProblemType)newValue);
        return;
      case JUnitPackage.TEST_CASE_TYPE__CLASS_NAME:
        setClassName((String)newValue);
        return;
      case JUnitPackage.TEST_CASE_TYPE__NAME:
        setName((String)newValue);
        return;
      case JUnitPackage.TEST_CASE_TYPE__TIME:
        setTime((Double)newValue);
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
      case JUnitPackage.TEST_CASE_TYPE__ERROR:
        setError((ProblemType)null);
        return;
      case JUnitPackage.TEST_CASE_TYPE__FAILURE:
        setFailure((ProblemType)null);
        return;
      case JUnitPackage.TEST_CASE_TYPE__CLASS_NAME:
        setClassName(CLASS_NAME_EDEFAULT);
        return;
      case JUnitPackage.TEST_CASE_TYPE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case JUnitPackage.TEST_CASE_TYPE__TIME:
        setTime(TIME_EDEFAULT);
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
      case JUnitPackage.TEST_CASE_TYPE__ERROR:
        return error != null;
      case JUnitPackage.TEST_CASE_TYPE__FAILURE:
        return failure != null;
      case JUnitPackage.TEST_CASE_TYPE__CLASS_NAME:
        return CLASS_NAME_EDEFAULT == null ? className != null : !CLASS_NAME_EDEFAULT.equals(className);
      case JUnitPackage.TEST_CASE_TYPE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case JUnitPackage.TEST_CASE_TYPE__TIME:
        return time != TIME_EDEFAULT;
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
    result.append(" (className: ");
    result.append(className);
    result.append(", name: ");
    result.append(name);
    result.append(", time: ");
    result.append(time);
    result.append(')');
    return result.toString();
  }

} // TestCaseType

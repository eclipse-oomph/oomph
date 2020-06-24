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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.junit.JUnitPackage
 * @generated
 */
public class JUnitFactory extends EFactoryImpl
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final JUnitFactory eINSTANCE = init();

  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JUnitFactory init()
  {
    try
    {
      JUnitFactory theJUnitFactory = (JUnitFactory)EPackage.Registry.INSTANCE.getEFactory(JUnitPackage.eNS_URI);
      if (theJUnitFactory != null)
      {
        return theJUnitFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new JUnitFactory();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitFactory()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case JUnitPackage.JUNIT_DOCUMENT_ROOT:
        return createJUnitDocumentRoot();
      case JUnitPackage.TEST_SUITES_TYPE:
        return createTestSuitesType();
      case JUnitPackage.TEST_SUITE:
        return createTestSuite();
      case JUnitPackage.TEST_SUITE_TYPE:
        return createTestSuiteType();
      case JUnitPackage.TEST_CASE_TYPE:
        return createTestCaseType();
      case JUnitPackage.PROBLEM_TYPE:
        return createProblemType();
      case JUnitPackage.PROPERTIES_TYPE:
        return createPropertiesType();
      case JUnitPackage.PROPERTY_TYPE:
        return createPropertyType();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case JUnitPackage.ISO8601_DATE_TIME:
        return createISO8601DateTimeFromString(eDataType, initialValue);
      case JUnitPackage.NAME_TYPE:
        return createNameTypeFromString(eDataType, initialValue);
      case JUnitPackage.TIME:
        return createTimeFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case JUnitPackage.ISO8601_DATE_TIME:
        return convertISO8601DateTimeToString(eDataType, instanceValue);
      case JUnitPackage.NAME_TYPE:
        return convertNameTypeToString(eDataType, instanceValue);
      case JUnitPackage.TIME:
        return convertTimeToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitDocumentRoot createJUnitDocumentRoot()
  {
    JUnitDocumentRoot jUnitDocumentRoot = new JUnitDocumentRoot();
    return jUnitDocumentRoot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TestSuitesType createTestSuitesType()
  {
    TestSuitesType testSuitesType = new TestSuitesType();
    return testSuitesType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TestSuite createTestSuite()
  {
    TestSuite testSuite = new TestSuite();
    return testSuite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TestSuiteType createTestSuiteType()
  {
    TestSuiteType testSuiteType = new TestSuiteType();
    return testSuiteType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TestCaseType createTestCaseType()
  {
    TestCaseType testCaseType = new TestCaseType();
    return testCaseType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PropertiesType createPropertiesType()
  {
    PropertiesType propertiesType = new PropertiesType();
    return propertiesType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PropertyType createPropertyType()
  {
    PropertyType propertyType = new PropertyType();
    return propertyType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProblemType createProblemType()
  {
    ProblemType problemType = new ProblemType();
    return problemType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Long createISO8601DateTimeFromString(EDataType eDataType, String initialValue)
  {
    return (Long)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertISO8601DateTimeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createNameTypeFromString(EDataType eDataType, String initialValue)
  {
    return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.TOKEN, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertNameTypeToString(EDataType eDataType, Object instanceValue)
  {
    return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.TOKEN, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Double createTimeFromString(EDataType eDataType, String initialValue)
  {
    return initialValue == null ? null : Double.parseDouble(initialValue);
  }

  /**
  	 * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
  	 * @generated NOT
  	 */
  public String convertTimeToString(EDataType eDataType, Object instanceValue)
  {
    if (instanceValue == null)
    {
      return null;
    }

    DecimalFormat decimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)); //$NON-NLS-1$
    decimalFormat.setMaximumFractionDigits(6);
    return decimalFormat.format(instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitPackage getJUnitPackage()
  {
    return (JUnitPackage)getEPackage();
  }

} // JUnitFactory

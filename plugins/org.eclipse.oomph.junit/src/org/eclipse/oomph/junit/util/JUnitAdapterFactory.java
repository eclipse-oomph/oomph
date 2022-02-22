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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.junit.JUnitPackage
 * @generated
 */
public class JUnitAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static JUnitPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JUnitAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = JUnitPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JUnitSwitch<Adapter> modelSwitch = new JUnitSwitch<>()
  {
    @Override
    public Adapter caseJUnitDocumentRoot(JUnitDocumentRoot object)
    {
      return createJUnitDocumentRootAdapter();
    }

    @Override
    public Adapter caseTestSuitesType(TestSuitesType object)
    {
      return createTestSuitesTypeAdapter();
    }

    @Override
    public Adapter caseTestSuite(TestSuite object)
    {
      return createTestSuiteAdapter();
    }

    @Override
    public Adapter caseTestSuiteType(TestSuiteType object)
    {
      return createTestSuiteTypeAdapter();
    }

    @Override
    public Adapter caseTestCaseType(TestCaseType object)
    {
      return createTestCaseTypeAdapter();
    }

    @Override
    public Adapter caseProblemType(ProblemType object)
    {
      return createProblemTypeAdapter();
    }

    @Override
    public Adapter casePropertiesType(PropertiesType object)
    {
      return createPropertiesTypeAdapter();
    }

    @Override
    public Adapter casePropertyType(PropertyType object)
    {
      return createPropertyTypeAdapter();
    }

    @Override
    public Adapter defaultCase(EObject object)
    {
      return createEObjectAdapter();
    }
  };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.JUnitDocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.JUnitDocumentRoot
   * @generated
   */
  public Adapter createJUnitDocumentRootAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.TestSuitesType <em>Test Suites Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.TestSuitesType
   * @generated
   */
  public Adapter createTestSuitesTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.TestSuite <em>Test Suite</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.TestSuite
   * @generated
   */
  public Adapter createTestSuiteAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.TestSuiteType <em>Test Suite Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.TestSuiteType
   * @generated
   */
  public Adapter createTestSuiteTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.TestCaseType <em>Test Case Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.TestCaseType
   * @generated
   */
  public Adapter createTestCaseTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.PropertiesType <em>Properties Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.PropertiesType
   * @generated
   */
  public Adapter createPropertiesTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.PropertyType <em>Property Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.PropertyType
   * @generated
   */
  public Adapter createPropertyTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.junit.ProblemType <em>Problem Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.junit.ProblemType
   * @generated
   */
  public Adapter createProblemTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} // JUnitAdapterFactory

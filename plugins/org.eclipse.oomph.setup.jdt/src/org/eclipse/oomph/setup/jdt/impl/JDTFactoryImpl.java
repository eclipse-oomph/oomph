/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt.impl;

import org.eclipse.oomph.setup.jdt.JDTFactory;
import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRELibrary;
import org.eclipse.oomph.setup.jdt.JRETask;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JDTFactoryImpl extends EFactoryImpl implements JDTFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JDTFactory init()
  {
    try
    {
      JDTFactory theJDTFactory = (JDTFactory)EPackage.Registry.INSTANCE.getEFactory(JDTPackage.eNS_URI);
      if (theJDTFactory != null)
      {
        return theJDTFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new JDTFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JDTFactoryImpl()
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
      case JDTPackage.JRE_TASK:
        return createJRETask();
      case JDTPackage.JRE_LIBRARY:
        return createJRELibrary();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JRETask createJRETask()
  {
    JRETaskImpl jreTask = new JRETaskImpl();
    return jreTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JRELibrary createJRELibrary()
  {
    JRELibraryImpl jreLibrary = new JRELibraryImpl();
    return jreLibrary;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JDTPackage getJDTPackage()
  {
    return (JDTPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static JDTPackage getPackage()
  {
    return JDTPackage.eINSTANCE;
  }

} // JDTFactoryImpl

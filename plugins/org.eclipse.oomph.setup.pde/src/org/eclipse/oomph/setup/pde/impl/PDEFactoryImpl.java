/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask;
import org.eclipse.oomph.setup.pde.APIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEFactory;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.pde.TargetPlatformTask;

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
public class PDEFactoryImpl extends EFactoryImpl implements PDEFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PDEFactory init()
  {
    try
    {
      PDEFactory thePDEFactory = (PDEFactory)EPackage.Registry.INSTANCE.getEFactory(PDEPackage.eNS_URI);
      if (thePDEFactory != null)
      {
        return thePDEFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new PDEFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PDEFactoryImpl()
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
      case PDEPackage.TARGET_PLATFORM_TASK:
        return createTargetPlatformTask();
      case PDEPackage.API_BASELINE_TASK:
        return createAPIBaselineTask();
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK:
        return createAPIBaselineFromTargetTask();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargetPlatformTask createTargetPlatformTask()
  {
    TargetPlatformTaskImpl targetPlatformTask = new TargetPlatformTaskImpl();
    return targetPlatformTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public APIBaselineTask createAPIBaselineTask()
  {
    APIBaselineTaskImpl apiBaselineTask = new APIBaselineTaskImpl();
    return apiBaselineTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public APIBaselineFromTargetTask createAPIBaselineFromTargetTask()
  {
    APIBaselineFromTargetTaskImpl apiBaselineFromTargetTask = new APIBaselineFromTargetTaskImpl();
    return apiBaselineFromTargetTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PDEPackage getPDEPackage()
  {
    return (PDEPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static PDEPackage getPackage()
  {
    return PDEPackage.eINSTANCE;
  }

} // PDEFactoryImpl

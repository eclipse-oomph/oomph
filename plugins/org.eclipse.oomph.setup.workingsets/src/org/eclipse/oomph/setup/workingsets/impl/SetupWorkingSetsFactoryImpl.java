/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workingsets.impl;

import org.eclipse.oomph.setup.workingsets.SetupWorkingSetsFactory;
import org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage;
import org.eclipse.oomph.setup.workingsets.WorkingSetTask;

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
public class SetupWorkingSetsFactoryImpl extends EFactoryImpl implements SetupWorkingSetsFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static SetupWorkingSetsFactory init()
  {
    try
    {
      SetupWorkingSetsFactory theSetupWorkingSetsFactory = (SetupWorkingSetsFactory)EPackage.Registry.INSTANCE.getEFactory(SetupWorkingSetsPackage.eNS_URI);
      if (theSetupWorkingSetsFactory != null)
      {
        return theSetupWorkingSetsFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new SetupWorkingSetsFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupWorkingSetsFactoryImpl()
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK:
        return createWorkingSetTask();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkingSetTask createWorkingSetTask()
  {
    WorkingSetTaskImpl workingSetTask = new WorkingSetTaskImpl();
    return workingSetTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupWorkingSetsPackage getSetupWorkingSetsPackage()
  {
    return (SetupWorkingSetsPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static SetupWorkingSetsPackage getPackage()
  {
    return SetupWorkingSetsPackage.eINSTANCE;
  }

} // SetupWorkingSetsFactoryImpl

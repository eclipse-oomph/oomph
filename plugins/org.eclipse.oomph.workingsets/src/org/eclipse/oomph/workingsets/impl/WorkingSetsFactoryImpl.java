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
package org.eclipse.oomph.workingsets.impl;

import org.eclipse.oomph.workingsets.ExclusionPredicate;
import org.eclipse.oomph.workingsets.InclusionPredicate;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.WorkingSetsFactory;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

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
public class WorkingSetsFactoryImpl extends EFactoryImpl implements WorkingSetsFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static WorkingSetsFactory init()
  {
    try
    {
      WorkingSetsFactory theWorkingSetsFactory = (WorkingSetsFactory)EPackage.Registry.INSTANCE.getEFactory(WorkingSetsPackage.eNS_URI);
      if (theWorkingSetsFactory != null)
      {
        return theWorkingSetsFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new WorkingSetsFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkingSetsFactoryImpl()
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
      case WorkingSetsPackage.WORKING_SET:
        return createWorkingSet();
      case WorkingSetsPackage.WORKING_SET_GROUP:
        return createWorkingSetGroup();
      case WorkingSetsPackage.INCLUSION_PREDICATE:
        return createInclusionPredicate();
      case WorkingSetsPackage.EXCLUSION_PREDICATE:
        return createExclusionPredicate();
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
  public WorkingSet createWorkingSet()
  {
    WorkingSetImpl workingSet = new WorkingSetImpl();
    return workingSet;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public WorkingSetGroup createWorkingSetGroup()
  {
    WorkingSetGroupImpl workingSetGroup = new WorkingSetGroupImpl();
    return workingSetGroup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ExclusionPredicate createExclusionPredicate()
  {
    ExclusionPredicateImpl exclusionPredicate = new ExclusionPredicateImpl();
    return exclusionPredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InclusionPredicate createInclusionPredicate()
  {
    InclusionPredicateImpl inclusionPredicate = new InclusionPredicateImpl();
    return inclusionPredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public WorkingSetsPackage getWorkingSetsPackage()
  {
    return (WorkingSetsPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static WorkingSetsPackage getPackage()
  {
    return WorkingSetsPackage.eINSTANCE;
  }

} // WorkingSetsFactoryImpl

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
package org.eclipse.oomph.setup.mylyn.impl;

import org.eclipse.oomph.setup.mylyn.BuildPlan;
import org.eclipse.oomph.setup.mylyn.MylynBuildsTask;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynPackage;
import org.eclipse.oomph.setup.mylyn.MylynQueriesTask;
import org.eclipse.oomph.setup.mylyn.Query;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MylynFactoryImpl extends EFactoryImpl implements MylynFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MylynFactory init()
  {
    try
    {
      MylynFactory theMylynFactory = (MylynFactory)EPackage.Registry.INSTANCE.getEFactory(MylynPackage.eNS_URI);
      if (theMylynFactory != null)
      {
        return theMylynFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MylynFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynFactoryImpl()
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
      case MylynPackage.MYLYN_QUERIES_TASK:
        return createMylynQueriesTask();
      case MylynPackage.MYLYN_BUILDS_TASK:
        return createMylynBuildsTask();
      case MylynPackage.BUILD_PLAN:
        return createBuildPlan();
      case MylynPackage.QUERY:
        return createQuery();
      case MylynPackage.QUERY_ATTRIBUTE:
        return (EObject)createQueryAttribute();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynQueriesTask createMylynQueriesTask()
  {
    MylynQueriesTaskImpl mylynQueriesTask = new MylynQueriesTaskImpl();
    return mylynQueriesTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynBuildsTask createMylynBuildsTask()
  {
    MylynBuildsTaskImpl mylynBuildsTask = new MylynBuildsTaskImpl();
    return mylynBuildsTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BuildPlan createBuildPlan()
  {
    BuildPlanImpl buildPlan = new BuildPlanImpl();
    return buildPlan;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Query createQuery()
  {
    QueryImpl query = new QueryImpl();
    return query;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map.Entry<String, String> createQueryAttribute()
  {
    QueryAttributeImpl queryAttribute = new QueryAttributeImpl();
    return queryAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynPackage getMylynPackage()
  {
    return (MylynPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static MylynPackage getPackage()
  {
    return MylynPackage.eINSTANCE;
  }

} // MylynFactoryImpl

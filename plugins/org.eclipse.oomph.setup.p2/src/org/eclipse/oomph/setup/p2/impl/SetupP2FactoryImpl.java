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
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.p2.SetupP2Package;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupP2FactoryImpl extends EFactoryImpl implements SetupP2Factory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static SetupP2Factory init()
  {
    try
    {
      SetupP2Factory theSetupP2Factory = (SetupP2Factory)EPackage.Registry.INSTANCE.getEFactory(SetupP2Package.eNS_URI);
      if (theSetupP2Factory != null)
      {
        return theSetupP2Factory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new SetupP2FactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupP2FactoryImpl()
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
      case SetupP2Package.P2_TASK:
        return createP2Task();
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
  public P2Task createP2Task()
  {
    P2TaskImpl p2Task = new P2TaskImpl();
    return p2Task;
  }

  @Override
  public P2Task createP2Task(String[] ius, String[] urls, Set<String> existingIUs)
  {
    P2Task p2Task = createP2Task();

    EList<Requirement> requirements = p2Task.getRequirements();
    for (String id : ius)
    {
      if (existingIUs == null || !existingIUs.contains(id))
      {
        Requirement requirement = P2Factory.eINSTANCE.createRequirement(id);
        requirements.add(requirement);
      }
    }

    if (requirements.isEmpty())
    {
      return null;
    }

    EList<Repository> repositories = p2Task.getRepositories();
    for (String url : urls)
    {
      Repository repository = P2Factory.eINSTANCE.createRepository(url);
      repositories.add(repository);
    }

    return p2Task;
  }

  @Override
  public P2Task createP2Task(String[] ius, String[] repositories)
  {
    return createP2Task(ius, repositories, null);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SetupP2Package getSetupP2Package()
  {
    return (SetupP2Package)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static SetupP2Package getPackage()
  {
    return SetupP2Package.eINSTANCE;
  }

} // SetupP2FactoryImpl

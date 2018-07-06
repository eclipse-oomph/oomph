/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.targlets.impl;

import org.eclipse.oomph.setup.targlets.ImplicitDependency;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsPackage;
import org.eclipse.oomph.setup.targlets.TargletTask;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.equinox.p2.metadata.Version;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupTargletsFactoryImpl extends EFactoryImpl implements SetupTargletsFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static SetupTargletsFactory init()
  {
    try
    {
      SetupTargletsFactory theSetupTargletsFactory = (SetupTargletsFactory)EPackage.Registry.INSTANCE.getEFactory(SetupTargletsPackage.eNS_URI);
      if (theSetupTargletsFactory != null)
      {
        return theSetupTargletsFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new SetupTargletsFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTargletsFactoryImpl()
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
      case SetupTargletsPackage.TARGLET_TASK:
        return createTargletTask();
      case SetupTargletsPackage.IMPLICIT_DEPENDENCY:
        return createImplicitDependency();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletTask createTargletTask()
  {
    TargletTaskImpl targletTask = new TargletTaskImpl();
    return targletTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ImplicitDependency createImplicitDependency()
  {
    ImplicitDependencyImpl implicitDependency = new ImplicitDependencyImpl();
    return implicitDependency;
  }

  public ImplicitDependency createImplicitDependency(String id, Version version)
  {
    ImplicitDependency implicitDependency = createImplicitDependency();
    implicitDependency.setID(id);

    if (version != null)
    {
      implicitDependency.setVersion(version);
    }

    return implicitDependency;
  }

  public ImplicitDependency createImplicitDependency(String id, String versionString)
  {
    Version version = versionString == null ? null : Version.parseVersion(versionString);
    return createImplicitDependency(id, version);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTargletsPackage getSetupTargletsPackage()
  {
    return (SetupTargletsPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static SetupTargletsPackage getPackage()
  {
    return SetupTargletsPackage.eINSTANCE;
  }

} // SetupTargletsFactoryImpl

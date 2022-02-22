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
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.MavenProjectFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class ResourcesFactoryImpl extends EFactoryImpl implements ResourcesFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ResourcesFactory init()
  {
    try
    {
      ResourcesFactory theResourcesFactory = (ResourcesFactory)EPackage.Registry.INSTANCE.getEFactory(ResourcesPackage.eNS_URI);
      if (theResourcesFactory != null)
      {
        return theResourcesFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ResourcesFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourcesFactoryImpl()
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
      case ResourcesPackage.SOURCE_LOCATOR:
        return createSourceLocator();
      case ResourcesPackage.ECLIPSE_PROJECT_FACTORY:
        return createEclipseProjectFactory();
      case ResourcesPackage.MAVEN_PROJECT_FACTORY:
        return createMavenProjectFactory();
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
  public SourceLocator createSourceLocator()
  {
    SourceLocatorImpl sourceLocator = new SourceLocatorImpl();
    return sourceLocator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EclipseProjectFactory createEclipseProjectFactory()
  {
    EclipseProjectFactoryImpl eclipseProjectFactory = new EclipseProjectFactoryImpl();
    return eclipseProjectFactory;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MavenProjectFactory createMavenProjectFactory()
  {
    MavenProjectFactoryImpl mavenProjectFactory = new MavenProjectFactoryImpl();
    return mavenProjectFactory;
  }

  @Override
  public SourceLocator createSourceLocator(String rootFolder)
  {
    SourceLocator sourceLocator = createSourceLocator();
    sourceLocator.setRootFolder(rootFolder);
    return sourceLocator;
  }

  @Override
  public SourceLocator createSourceLocator(String rootFolder, boolean locateNestedProjects)
  {
    SourceLocator sourceLocator = createSourceLocator(rootFolder);
    sourceLocator.setLocateNestedProjects(locateNestedProjects);
    return sourceLocator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourcesPackage getResourcesPackage()
  {
    return (ResourcesPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ResourcesPackage getPackage()
  {
    return ResourcesPackage.eINSTANCE;
  }

} // ResourcesFactoryImpl

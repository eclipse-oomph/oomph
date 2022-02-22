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
package org.eclipse.oomph.setup.projectset.impl;

import org.eclipse.oomph.setup.projectset.ProjectSetFactory;
import org.eclipse.oomph.setup.projectset.ProjectSetImportTask;
import org.eclipse.oomph.setup.projectset.ProjectSetPackage;

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
public class ProjectSetFactoryImpl extends EFactoryImpl implements ProjectSetFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ProjectSetFactory init()
  {
    try
    {
      ProjectSetFactory theProjectSetFactory = (ProjectSetFactory)EPackage.Registry.INSTANCE.getEFactory(ProjectSetPackage.eNS_URI);
      if (theProjectSetFactory != null)
      {
        return theProjectSetFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ProjectSetFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectSetFactoryImpl()
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
      case ProjectSetPackage.PROJECT_SET_IMPORT_TASK:
        return createProjectSetImportTask();
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
  public ProjectSetImportTask createProjectSetImportTask()
  {
    ProjectSetImportTaskImpl projectSetImportTask = new ProjectSetImportTaskImpl();
    return projectSetImportTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProjectSetPackage getProjectSetPackage()
  {
    return (ProjectSetPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ProjectSetPackage getPackage()
  {
    return ProjectSetPackage.eINSTANCE;
  }

} // ProjectSetFactoryImpl

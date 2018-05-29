/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench.impl;

import org.eclipse.oomph.setup.workbench.CommandParameter;
import org.eclipse.oomph.setup.workbench.FileAssociationsTask;
import org.eclipse.oomph.setup.workbench.FileEditor;
import org.eclipse.oomph.setup.workbench.FileMapping;
import org.eclipse.oomph.setup.workbench.KeyBindingContext;
import org.eclipse.oomph.setup.workbench.KeyBindingTask;
import org.eclipse.oomph.setup.workbench.WorkbenchFactory;
import org.eclipse.oomph.setup.workbench.WorkbenchPackage;

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
public class WorkbenchFactoryImpl extends EFactoryImpl implements WorkbenchFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static WorkbenchFactory init()
  {
    try
    {
      WorkbenchFactory theWorkbenchFactory = (WorkbenchFactory)EPackage.Registry.INSTANCE.getEFactory(WorkbenchPackage.eNS_URI);
      if (theWorkbenchFactory != null)
      {
        return theWorkbenchFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new WorkbenchFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkbenchFactoryImpl()
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
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK:
        return createFileAssociationsTask();
      case WorkbenchPackage.FILE_MAPPING:
        return createFileMapping();
      case WorkbenchPackage.FILE_EDITOR:
        return createFileEditor();
      case WorkbenchPackage.KEY_BINDING_TASK:
        return createKeyBindingTask();
      case WorkbenchPackage.KEY_BINDING_CONTEXT:
        return createKeyBindingContext();
      case WorkbenchPackage.COMMAND_PARAMETER:
        return createCommandParameter();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FileAssociationsTask createFileAssociationsTask()
  {
    FileAssociationsTaskImpl fileAssociationsTask = new FileAssociationsTaskImpl();
    return fileAssociationsTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FileMapping createFileMapping()
  {
    FileMappingImpl fileMapping = new FileMappingImpl();
    return fileMapping;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FileEditor createFileEditor()
  {
    FileEditorImpl fileEditor = new FileEditorImpl();
    return fileEditor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public KeyBindingTask createKeyBindingTask()
  {
    KeyBindingTaskImpl keyBindingTask = new KeyBindingTaskImpl();
    return keyBindingTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public KeyBindingContext createKeyBindingContext()
  {
    KeyBindingContextImpl keyBindingContext = new KeyBindingContextImpl();
    return keyBindingContext;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CommandParameter createCommandParameter()
  {
    CommandParameterImpl commandParameter = new CommandParameterImpl();
    return commandParameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkbenchPackage getWorkbenchPackage()
  {
    return (WorkbenchPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static WorkbenchPackage getPackage()
  {
    return WorkbenchPackage.eINSTANCE;
  }

} // WorkbenchFactoryImpl

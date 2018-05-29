/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask;
import org.eclipse.oomph.setup.pde.APIBaselineTask;
import org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.pde.TargetPlatformTask;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.pde.PDEPackage
 * @generated
 */
public class PDESwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static PDEPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PDESwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = PDEPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case PDEPackage.TARGET_PLATFORM_TASK:
      {
        TargetPlatformTask targetPlatformTask = (TargetPlatformTask)theEObject;
        T result = caseTargetPlatformTask(targetPlatformTask);
        if (result == null)
        {
          result = caseSetupTask(targetPlatformTask);
        }
        if (result == null)
        {
          result = caseModelElement(targetPlatformTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PDEPackage.ABSTRACT_API_BASELINE_TASK:
      {
        AbstractAPIBaselineTask abstractAPIBaselineTask = (AbstractAPIBaselineTask)theEObject;
        T result = caseAbstractAPIBaselineTask(abstractAPIBaselineTask);
        if (result == null)
        {
          result = caseSetupTask(abstractAPIBaselineTask);
        }
        if (result == null)
        {
          result = caseModelElement(abstractAPIBaselineTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PDEPackage.API_BASELINE_TASK:
      {
        APIBaselineTask apiBaselineTask = (APIBaselineTask)theEObject;
        T result = caseAPIBaselineTask(apiBaselineTask);
        if (result == null)
        {
          result = caseAbstractAPIBaselineTask(apiBaselineTask);
        }
        if (result == null)
        {
          result = caseSetupTask(apiBaselineTask);
        }
        if (result == null)
        {
          result = caseModelElement(apiBaselineTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK:
      {
        APIBaselineFromTargetTask apiBaselineFromTargetTask = (APIBaselineFromTargetTask)theEObject;
        T result = caseAPIBaselineFromTargetTask(apiBaselineFromTargetTask);
        if (result == null)
        {
          result = caseAbstractAPIBaselineTask(apiBaselineFromTargetTask);
        }
        if (result == null)
        {
          result = caseSetupTask(apiBaselineFromTargetTask);
        }
        if (result == null)
        {
          result = caseModelElement(apiBaselineFromTargetTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      default:
        return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Target Platform Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Target Platform Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTargetPlatformTask(TargetPlatformTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Abstract API Baseline Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Abstract API Baseline Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAbstractAPIBaselineTask(AbstractAPIBaselineTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>API Baseline Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>API Baseline Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAPIBaselineTask(APIBaselineTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>API Baseline From Target Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>API Baseline From Target Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAPIBaselineFromTargetTask(APIBaselineFromTargetTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelElement(ModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSetupTask(SetupTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} // PDESwitch

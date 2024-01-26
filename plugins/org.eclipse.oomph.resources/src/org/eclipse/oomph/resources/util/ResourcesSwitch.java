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
package org.eclipse.oomph.resources.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.resources.DynamicMavenProjectFactory;
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.MavenProjectFactory;
import org.eclipse.oomph.resources.ProjectFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.XMLProjectFactory;

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
 * @see org.eclipse.oomph.resources.ResourcesPackage
 * @generated
 */
public class ResourcesSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ResourcesPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourcesSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ResourcesPackage.eINSTANCE;
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
      case ResourcesPackage.SOURCE_LOCATOR:
      {
        SourceLocator sourceLocator = (SourceLocator)theEObject;
        T result = caseSourceLocator(sourceLocator);
        if (result == null)
        {
          result = caseModelElement(sourceLocator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ResourcesPackage.PROJECT_FACTORY:
      {
        ProjectFactory projectFactory = (ProjectFactory)theEObject;
        T result = caseProjectFactory(projectFactory);
        if (result == null)
        {
          result = caseModelElement(projectFactory);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ResourcesPackage.XML_PROJECT_FACTORY:
      {
        XMLProjectFactory xmlProjectFactory = (XMLProjectFactory)theEObject;
        T result = caseXMLProjectFactory(xmlProjectFactory);
        if (result == null)
        {
          result = caseProjectFactory(xmlProjectFactory);
        }
        if (result == null)
        {
          result = caseModelElement(xmlProjectFactory);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ResourcesPackage.ECLIPSE_PROJECT_FACTORY:
      {
        EclipseProjectFactory eclipseProjectFactory = (EclipseProjectFactory)theEObject;
        T result = caseEclipseProjectFactory(eclipseProjectFactory);
        if (result == null)
        {
          result = caseXMLProjectFactory(eclipseProjectFactory);
        }
        if (result == null)
        {
          result = caseProjectFactory(eclipseProjectFactory);
        }
        if (result == null)
        {
          result = caseModelElement(eclipseProjectFactory);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ResourcesPackage.MAVEN_PROJECT_FACTORY:
      {
        MavenProjectFactory mavenProjectFactory = (MavenProjectFactory)theEObject;
        T result = caseMavenProjectFactory(mavenProjectFactory);
        if (result == null)
        {
          result = caseXMLProjectFactory(mavenProjectFactory);
        }
        if (result == null)
        {
          result = caseProjectFactory(mavenProjectFactory);
        }
        if (result == null)
        {
          result = caseModelElement(mavenProjectFactory);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ResourcesPackage.DYNAMIC_MAVEN_PROJECT_FACTORY:
      {
        DynamicMavenProjectFactory dynamicMavenProjectFactory = (DynamicMavenProjectFactory)theEObject;
        T result = caseDynamicMavenProjectFactory(dynamicMavenProjectFactory);
        if (result == null)
        {
          result = caseMavenProjectFactory(dynamicMavenProjectFactory);
        }
        if (result == null)
        {
          result = caseXMLProjectFactory(dynamicMavenProjectFactory);
        }
        if (result == null)
        {
          result = caseProjectFactory(dynamicMavenProjectFactory);
        }
        if (result == null)
        {
          result = caseModelElement(dynamicMavenProjectFactory);
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
   * Returns the result of interpreting the object as an instance of '<em>Source Locator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Source Locator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSourceLocator(SourceLocator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Factory</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Factory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectFactory(ProjectFactory object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XML Project Factory</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XML Project Factory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseXMLProjectFactory(XMLProjectFactory object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Eclipse Project Factory</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Eclipse Project Factory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEclipseProjectFactory(EclipseProjectFactory object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Maven Project Factory</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Maven Project Factory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMavenProjectFactory(MavenProjectFactory object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Dynamic Maven Project Factory</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dynamic Maven Project Factory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDynamicMavenProjectFactory(DynamicMavenProjectFactory object)
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

} // ResourcesSwitch

/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;

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
 * @see org.eclipse.oomph.maven.MavenPackage
 * @generated
 */
public class MavenSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MavenPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MavenPackage.eINSTANCE;
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
      case MavenPackage.REALM:
      {
        Realm realm = (Realm)theEObject;
        T result = caseRealm(realm);
        if (result == null)
        {
          result = caseModelElement(realm);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.DOM_ELEMENT:
      {
        DOMElement domElement = (DOMElement)theEObject;
        T result = caseDOMElement(domElement);
        if (result == null)
        {
          result = caseModelElement(domElement);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.COORDINATE:
      {
        Coordinate coordinate = (Coordinate)theEObject;
        T result = caseCoordinate(coordinate);
        if (result == null)
        {
          result = caseDOMElement(coordinate);
        }
        if (result == null)
        {
          result = caseModelElement(coordinate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.PROJECT:
      {
        Project project = (Project)theEObject;
        T result = caseProject(project);
        if (result == null)
        {
          result = caseCoordinate(project);
        }
        if (result == null)
        {
          result = caseDOMElement(project);
        }
        if (result == null)
        {
          result = caseModelElement(project);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.PARENT:
      {
        Parent parent = (Parent)theEObject;
        T result = caseParent(parent);
        if (result == null)
        {
          result = caseCoordinate(parent);
        }
        if (result == null)
        {
          result = caseDOMElement(parent);
        }
        if (result == null)
        {
          result = caseModelElement(parent);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.DEPENDENCY:
      {
        Dependency dependency = (Dependency)theEObject;
        T result = caseDependency(dependency);
        if (result == null)
        {
          result = caseCoordinate(dependency);
        }
        if (result == null)
        {
          result = caseDOMElement(dependency);
        }
        if (result == null)
        {
          result = caseModelElement(dependency);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.PROPERTY:
      {
        Property property = (Property)theEObject;
        T result = caseProperty(property);
        if (result == null)
        {
          result = caseDOMElement(property);
        }
        if (result == null)
        {
          result = caseModelElement(property);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case MavenPackage.PROPERTY_REFERENCE:
      {
        PropertyReference propertyReference = (PropertyReference)theEObject;
        T result = casePropertyReference(propertyReference);
        if (result == null)
        {
          result = caseDOMElement(propertyReference);
        }
        if (result == null)
        {
          result = caseModelElement(propertyReference);
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
   * Returns the result of interpreting the object as an instance of '<em>Realm</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Realm</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRealm(Realm object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>DOM Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>DOM Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDOMElement(DOMElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProject(Project object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Coordinate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Coordinate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCoordinate(Coordinate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parent</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parent</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParent(Parent object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Dependency</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dependency</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDependency(Dependency object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProperty(Property object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertyReference(PropertyReference object)
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

} // MavenSwitch

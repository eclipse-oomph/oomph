/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.predicates.AndPredicate;
import org.eclipse.oomph.predicates.BuilderPredicate;
import org.eclipse.oomph.predicates.CommentPredicate;
import org.eclipse.oomph.predicates.FilePredicate;
import org.eclipse.oomph.predicates.ImportedPredicate;
import org.eclipse.oomph.predicates.LocationPredicate;
import org.eclipse.oomph.predicates.NamePredicate;
import org.eclipse.oomph.predicates.NaturePredicate;
import org.eclipse.oomph.predicates.NotPredicate;
import org.eclipse.oomph.predicates.OrPredicate;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.predicates.RepositoryPredicate;

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
 * @see org.eclipse.oomph.predicates.PredicatesPackage
 * @generated
 */
public class PredicatesSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static PredicatesPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PredicatesSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = PredicatesPackage.eINSTANCE;
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
      case PredicatesPackage.PREDICATE:
      {
        Predicate predicate = (Predicate)theEObject;
        T result = casePredicate(predicate);
        if (result == null)
        {
          result = caseModelElement(predicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.NAME_PREDICATE:
      {
        NamePredicate namePredicate = (NamePredicate)theEObject;
        T result = caseNamePredicate(namePredicate);
        if (result == null)
        {
          result = casePredicate(namePredicate);
        }
        if (result == null)
        {
          result = caseModelElement(namePredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.COMMENT_PREDICATE:
      {
        CommentPredicate commentPredicate = (CommentPredicate)theEObject;
        T result = caseCommentPredicate(commentPredicate);
        if (result == null)
        {
          result = casePredicate(commentPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(commentPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.LOCATION_PREDICATE:
      {
        LocationPredicate locationPredicate = (LocationPredicate)theEObject;
        T result = caseLocationPredicate(locationPredicate);
        if (result == null)
        {
          result = casePredicate(locationPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(locationPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.REPOSITORY_PREDICATE:
      {
        RepositoryPredicate repositoryPredicate = (RepositoryPredicate)theEObject;
        T result = caseRepositoryPredicate(repositoryPredicate);
        if (result == null)
        {
          result = casePredicate(repositoryPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(repositoryPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.AND_PREDICATE:
      {
        AndPredicate andPredicate = (AndPredicate)theEObject;
        T result = caseAndPredicate(andPredicate);
        if (result == null)
        {
          result = casePredicate(andPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(andPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.OR_PREDICATE:
      {
        OrPredicate orPredicate = (OrPredicate)theEObject;
        T result = caseOrPredicate(orPredicate);
        if (result == null)
        {
          result = casePredicate(orPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(orPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.NOT_PREDICATE:
      {
        NotPredicate notPredicate = (NotPredicate)theEObject;
        T result = caseNotPredicate(notPredicate);
        if (result == null)
        {
          result = casePredicate(notPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(notPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.NATURE_PREDICATE:
      {
        NaturePredicate naturePredicate = (NaturePredicate)theEObject;
        T result = caseNaturePredicate(naturePredicate);
        if (result == null)
        {
          result = casePredicate(naturePredicate);
        }
        if (result == null)
        {
          result = caseModelElement(naturePredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.BUILDER_PREDICATE:
      {
        BuilderPredicate builderPredicate = (BuilderPredicate)theEObject;
        T result = caseBuilderPredicate(builderPredicate);
        if (result == null)
        {
          result = casePredicate(builderPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(builderPredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.FILE_PREDICATE:
      {
        FilePredicate filePredicate = (FilePredicate)theEObject;
        T result = caseFilePredicate(filePredicate);
        if (result == null)
        {
          result = casePredicate(filePredicate);
        }
        if (result == null)
        {
          result = caseModelElement(filePredicate);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case PredicatesPackage.IMPORTED_PREDICATE:
      {
        ImportedPredicate importedPredicate = (ImportedPredicate)theEObject;
        T result = caseImportedPredicate(importedPredicate);
        if (result == null)
        {
          result = casePredicate(importedPredicate);
        }
        if (result == null)
        {
          result = caseModelElement(importedPredicate);
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
   * Returns the result of interpreting the object as an instance of '<em>Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePredicate(Predicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Name Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Name Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamePredicate(NamePredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Comment Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Comment Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCommentPredicate(CommentPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Location Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Location Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocationPredicate(LocationPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Repository Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Repository Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRepositoryPredicate(RepositoryPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>And Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>And Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAndPredicate(AndPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrPredicate(OrPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Not Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Not Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNotPredicate(NotPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Nature Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Nature Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNaturePredicate(NaturePredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Builder Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Builder Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBuilderPredicate(BuilderPredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>File Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>File Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFilePredicate(FilePredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Imported Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Imported Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseImportedPredicate(ImportedPredicate object)
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

} // PredicatesSwitch

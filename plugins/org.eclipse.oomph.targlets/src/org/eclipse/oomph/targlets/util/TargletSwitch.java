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
package org.eclipse.oomph.targlets.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.targlets.CSpecGenerator;
import org.eclipse.oomph.targlets.CSpexGenerator;
import org.eclipse.oomph.targlets.CategoryGenerator;
import org.eclipse.oomph.targlets.ComponentDefGenerator;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtGenerator;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.DropinLocation;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.PluginGenerator;
import org.eclipse.oomph.targlets.ProductGenerator;
import org.eclipse.oomph.targlets.ProjectNameGenerator;
import org.eclipse.oomph.targlets.SiteGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletContainer;
import org.eclipse.oomph.targlets.TargletPackage;

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
 * @see org.eclipse.oomph.targlets.TargletPackage
 * @generated
 */
public class TargletSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static TargletPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = TargletPackage.eINSTANCE;
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
      case TargletPackage.TARGLET_CONTAINER:
      {
        TargletContainer targletContainer = (TargletContainer)theEObject;
        T result = caseTargletContainer(targletContainer);
        if (result == null)
        {
          result = caseModelElement(targletContainer);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.TARGLET:
      {
        Targlet targlet = (Targlet)theEObject;
        T result = caseTarglet(targlet);
        if (result == null)
        {
          result = caseModelElement(targlet);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.COMPONENT_EXTENSION:
      {
        ComponentExtension componentExtension = (ComponentExtension)theEObject;
        T result = caseComponentExtension(componentExtension);
        if (result == null)
        {
          result = caseModelElement(componentExtension);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.COMPONENT_DEFINITION:
      {
        ComponentDefinition componentDefinition = (ComponentDefinition)theEObject;
        T result = caseComponentDefinition(componentDefinition);
        if (result == null)
        {
          result = caseComponentExtension(componentDefinition);
        }
        if (result == null)
        {
          result = caseModelElement(componentDefinition);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.IU_GENERATOR:
      {
        IUGenerator iuGenerator = (IUGenerator)theEObject;
        T result = caseIUGenerator(iuGenerator);
        if (result == null)
        {
          result = caseModelElement(iuGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.FEATURE_GENERATOR:
      {
        FeatureGenerator featureGenerator = (FeatureGenerator)theEObject;
        T result = caseFeatureGenerator(featureGenerator);
        if (result == null)
        {
          result = caseIUGenerator(featureGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(featureGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.PLUGIN_GENERATOR:
      {
        PluginGenerator pluginGenerator = (PluginGenerator)theEObject;
        T result = casePluginGenerator(pluginGenerator);
        if (result == null)
        {
          result = caseIUGenerator(pluginGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(pluginGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.COMPONENT_DEF_GENERATOR:
      {
        ComponentDefGenerator componentDefGenerator = (ComponentDefGenerator)theEObject;
        T result = caseComponentDefGenerator(componentDefGenerator);
        if (result == null)
        {
          result = caseIUGenerator(componentDefGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(componentDefGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.COMPONENT_EXT_GENERATOR:
      {
        ComponentExtGenerator componentExtGenerator = (ComponentExtGenerator)theEObject;
        T result = caseComponentExtGenerator(componentExtGenerator);
        if (result == null)
        {
          result = caseIUGenerator(componentExtGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(componentExtGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.CSPEC_GENERATOR:
      {
        CSpecGenerator cSpecGenerator = (CSpecGenerator)theEObject;
        T result = caseCSpecGenerator(cSpecGenerator);
        if (result == null)
        {
          result = caseIUGenerator(cSpecGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(cSpecGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.CSPEX_GENERATOR:
      {
        CSpexGenerator cSpexGenerator = (CSpexGenerator)theEObject;
        T result = caseCSpexGenerator(cSpexGenerator);
        if (result == null)
        {
          result = caseIUGenerator(cSpexGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(cSpexGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.SITE_GENERATOR:
      {
        SiteGenerator siteGenerator = (SiteGenerator)theEObject;
        T result = caseSiteGenerator(siteGenerator);
        if (result == null)
        {
          result = caseIUGenerator(siteGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(siteGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.CATEGORY_GENERATOR:
      {
        CategoryGenerator categoryGenerator = (CategoryGenerator)theEObject;
        T result = caseCategoryGenerator(categoryGenerator);
        if (result == null)
        {
          result = caseSiteGenerator(categoryGenerator);
        }
        if (result == null)
        {
          result = caseIUGenerator(categoryGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(categoryGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.PRODUCT_GENERATOR:
      {
        ProductGenerator productGenerator = (ProductGenerator)theEObject;
        T result = caseProductGenerator(productGenerator);
        if (result == null)
        {
          result = caseIUGenerator(productGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(productGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.PROJECT_NAME_GENERATOR:
      {
        ProjectNameGenerator projectNameGenerator = (ProjectNameGenerator)theEObject;
        T result = caseProjectNameGenerator(projectNameGenerator);
        if (result == null)
        {
          result = caseIUGenerator(projectNameGenerator);
        }
        if (result == null)
        {
          result = caseModelElement(projectNameGenerator);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case TargletPackage.DROPIN_LOCATION:
      {
        DropinLocation dropinLocation = (DropinLocation)theEObject;
        T result = caseDropinLocation(dropinLocation);
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
   * Returns the result of interpreting the object as an instance of '<em>Container</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Container</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTargletContainer(TargletContainer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Targlet</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Targlet</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTarglet(Targlet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Component Extension</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Component Extension</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseComponentExtension(ComponentExtension object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Component Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Component Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseComponentDefinition(ComponentDefinition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IU Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IU Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIUGenerator(IUGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Feature Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Feature Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFeatureGenerator(FeatureGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Plugin Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Plugin Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePluginGenerator(PluginGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Component Def Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Component Def Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseComponentDefGenerator(ComponentDefGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Component Ext Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Component Ext Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseComponentExtGenerator(ComponentExtGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>CSpec Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>CSpec Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCSpecGenerator(CSpecGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>CSpex Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>CSpex Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCSpexGenerator(CSpexGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Site Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Site Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSiteGenerator(SiteGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Category Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Category Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCategoryGenerator(CategoryGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Product Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProductGenerator(ProductGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Name Generator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Name Generator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectNameGenerator(ProjectNameGenerator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Dropin Location</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dropin Location</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDropinLocation(DropinLocation object)
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

} // TargletSwitch

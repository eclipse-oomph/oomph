/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.targlets.TargletPackage
 * @generated
 */
public class TargletAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static TargletPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = TargletPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TargletSwitch<Adapter> modelSwitch = new TargletSwitch<Adapter>()
  {
    @Override
    public Adapter caseTargletContainer(TargletContainer object)
    {
      return createTargletContainerAdapter();
    }

    @Override
    public Adapter caseTarglet(Targlet object)
    {
      return createTargletAdapter();
    }

    @Override
    public Adapter caseComponentExtension(ComponentExtension object)
    {
      return createComponentExtensionAdapter();
    }

    @Override
    public Adapter caseComponentDefinition(ComponentDefinition object)
    {
      return createComponentDefinitionAdapter();
    }

    @Override
    public Adapter caseIUGenerator(IUGenerator object)
    {
      return createIUGeneratorAdapter();
    }

    @Override
    public Adapter caseFeatureGenerator(FeatureGenerator object)
    {
      return createFeatureGeneratorAdapter();
    }

    @Override
    public Adapter casePluginGenerator(PluginGenerator object)
    {
      return createPluginGeneratorAdapter();
    }

    @Override
    public Adapter caseComponentDefGenerator(ComponentDefGenerator object)
    {
      return createComponentDefGeneratorAdapter();
    }

    @Override
    public Adapter caseComponentExtGenerator(ComponentExtGenerator object)
    {
      return createComponentExtGeneratorAdapter();
    }

    @Override
    public Adapter caseCSpecGenerator(CSpecGenerator object)
    {
      return createCSpecGeneratorAdapter();
    }

    @Override
    public Adapter caseCSpexGenerator(CSpexGenerator object)
    {
      return createCSpexGeneratorAdapter();
    }

    @Override
    public Adapter caseSiteGenerator(SiteGenerator object)
    {
      return createSiteGeneratorAdapter();
    }

    @Override
    public Adapter caseCategoryGenerator(CategoryGenerator object)
    {
      return createCategoryGeneratorAdapter();
    }

    @Override
    public Adapter caseProductGenerator(ProductGenerator object)
    {
      return createProductGeneratorAdapter();
    }

    @Override
    public Adapter caseProjectNameGenerator(ProjectNameGenerator object)
    {
      return createProjectNameGeneratorAdapter();
    }

    @Override
    public Adapter caseDropinLocation(DropinLocation object)
    {
      return createDropinLocationAdapter();
    }

    @Override
    public Adapter caseModelElement(ModelElement object)
    {
      return createModelElementAdapter();
    }

    @Override
    public Adapter defaultCase(EObject object)
    {
      return createEObjectAdapter();
    }
  };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.TargletContainer <em>Container</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.TargletContainer
   * @generated
   */
  public Adapter createTargletContainerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.Targlet <em>Targlet</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.Targlet
   * @generated
   */
  public Adapter createTargletAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ComponentExtension <em>Component Extension</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ComponentExtension
   * @generated
   */
  public Adapter createComponentExtensionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ComponentDefinition <em>Component Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ComponentDefinition
   * @generated
   */
  public Adapter createComponentDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.IUGenerator <em>IU Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.IUGenerator
   * @generated
   */
  public Adapter createIUGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.FeatureGenerator <em>Feature Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.FeatureGenerator
   * @generated
   */
  public Adapter createFeatureGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.PluginGenerator <em>Plugin Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.PluginGenerator
   * @generated
   */
  public Adapter createPluginGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ComponentDefGenerator <em>Component Def Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ComponentDefGenerator
   * @generated
   */
  public Adapter createComponentDefGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ComponentExtGenerator <em>Component Ext Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ComponentExtGenerator
   * @generated
   */
  public Adapter createComponentExtGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.CSpecGenerator <em>CSpec Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.CSpecGenerator
   * @generated
   */
  public Adapter createCSpecGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.CSpexGenerator <em>CSpex Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.CSpexGenerator
   * @generated
   */
  public Adapter createCSpexGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.SiteGenerator <em>Site Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.SiteGenerator
   * @generated
   */
  public Adapter createSiteGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.CategoryGenerator <em>Category Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.CategoryGenerator
   * @generated
   */
  public Adapter createCategoryGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ProductGenerator <em>Product Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ProductGenerator
   * @generated
   */
  public Adapter createProductGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.ProjectNameGenerator <em>Project Name Generator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.ProjectNameGenerator
   * @generated
   */
  public Adapter createProjectNameGeneratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.targlets.DropinLocation <em>Dropin Location</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.targlets.DropinLocation
   * @generated
   */
  public Adapter createDropinLocationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.base.ModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.base.ModelElement
   * @generated
   */
  public Adapter createModelElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} // TargletAdapterFactory

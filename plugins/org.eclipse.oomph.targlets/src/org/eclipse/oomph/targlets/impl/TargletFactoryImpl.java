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
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.SourceLocator;
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
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletFactoryImpl extends EFactoryImpl implements TargletFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TargletFactory init()
  {
    try
    {
      TargletFactory theTargletFactory = (TargletFactory)EPackage.Registry.INSTANCE.getEFactory(TargletPackage.eNS_URI);
      if (theTargletFactory != null)
      {
        return theTargletFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new TargletFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletFactoryImpl()
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
      case TargletPackage.TARGLET_CONTAINER:
        return createTargletContainer();
      case TargletPackage.TARGLET:
        return createTarglet();
      case TargletPackage.COMPONENT_EXTENSION:
        return createComponentExtension();
      case TargletPackage.COMPONENT_DEFINITION:
        return createComponentDefinition();
      case TargletPackage.FEATURE_GENERATOR:
        return createFeatureGenerator();
      case TargletPackage.PLUGIN_GENERATOR:
        return createPluginGenerator();
      case TargletPackage.COMPONENT_DEF_GENERATOR:
        return createComponentDefGenerator();
      case TargletPackage.COMPONENT_EXT_GENERATOR:
        return createComponentExtGenerator();
      case TargletPackage.CSPEC_GENERATOR:
        return createCSpecGenerator();
      case TargletPackage.CSPEX_GENERATOR:
        return createCSpexGenerator();
      case TargletPackage.SITE_GENERATOR:
        return createSiteGenerator();
      case TargletPackage.CATEGORY_GENERATOR:
        return createCategoryGenerator();
      case TargletPackage.PRODUCT_GENERATOR:
        return createProductGenerator();
      case TargletPackage.PROJECT_NAME_GENERATOR:
        return createProjectNameGenerator();
      case TargletPackage.DROPIN_LOCATION:
        return createDropinLocation();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
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
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
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
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletContainer createTargletContainer()
  {
    TargletContainerImpl targletContainer = new TargletContainerImpl();
    return targletContainer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Targlet createTarglet()
  {
    TargletImpl targlet = new TargletImpl();
    return targlet;
  }

  public Targlet createTarglet(String name)
  {
    Targlet targlet = createTarglet();
    targlet.setName(name);
    return targlet;
  }

  public Targlet copyTarglet(Targlet source)
  {
    String activeRepositoryList = source.getActiveRepositoryListName();
    if (activeRepositoryList != null && activeRepositoryList.length() == 0)
    {
      activeRepositoryList = null;
    }

    Targlet targlet = createTarglet();
    targlet.setName(source.getName());
    targlet.setActiveRepositoryListName(activeRepositoryList);
    targlet.setIncludeSources(source.isIncludeSources());
    targlet.setIncludeAllPlatforms(source.isIncludeAllPlatforms());
    targlet.setIncludeAllRequirements(source.isIncludeAllRequirements());

    for (Annotation annotation : source.getAnnotations())
    {
      targlet.getAnnotations().add(EcoreUtil.copy(annotation));
    }

    for (Requirement requirement : source.getRequirements())
    {
      targlet.getRequirements().add(EcoreUtil.copy(requirement));
    }

    for (SourceLocator sourceLocator : source.getSourceLocators())
    {
      targlet.getSourceLocators().add(EcoreUtil.copy(sourceLocator));
    }

    for (RepositoryList repositoryList : source.getRepositoryLists())
    {
      targlet.getRepositoryLists().add(EcoreUtil.copy(repositoryList));
    }

    for (IUGenerator iuGenerator : source.getInstallableUnitGenerators())
    {
      targlet.getInstallableUnitGenerators().add(EcoreUtil.copy(iuGenerator));
    }

    for (DropinLocation dropinLocation : source.getDropinLocations())
    {
      targlet.getDropinLocations().add(EcoreUtil.copy(dropinLocation));
    }

    return targlet;
  }

  public EList<Targlet> copyTarglets(Collection<? extends Targlet> targlets)
  {
    EList<Targlet> result = new BasicEList<Targlet>();
    for (Targlet source : targlets)
    {
      result.add(copyTarglet(source));
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ComponentExtension createComponentExtension()
  {
    ComponentExtensionImpl componentExtension = new ComponentExtensionImpl();
    return componentExtension;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ComponentDefinition createComponentDefinition()
  {
    ComponentDefinitionImpl componentDefinition = new ComponentDefinitionImpl();
    return componentDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FeatureGenerator createFeatureGenerator()
  {
    FeatureGeneratorImpl featureGenerator = new FeatureGeneratorImpl();
    return featureGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PluginGenerator createPluginGenerator()
  {
    PluginGeneratorImpl pluginGenerator = new PluginGeneratorImpl();
    return pluginGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ComponentDefGenerator createComponentDefGenerator()
  {
    ComponentDefGeneratorImpl componentDefGenerator = new ComponentDefGeneratorImpl();
    return componentDefGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ComponentExtGenerator createComponentExtGenerator()
  {
    ComponentExtGeneratorImpl componentExtGenerator = new ComponentExtGeneratorImpl();
    return componentExtGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CSpecGenerator createCSpecGenerator()
  {
    CSpecGeneratorImpl cSpecGenerator = new CSpecGeneratorImpl();
    return cSpecGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CSpexGenerator createCSpexGenerator()
  {
    CSpexGeneratorImpl cSpexGenerator = new CSpexGeneratorImpl();
    return cSpexGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SiteGenerator createSiteGenerator()
  {
    SiteGeneratorImpl siteGenerator = new SiteGeneratorImpl();
    return siteGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CategoryGenerator createCategoryGenerator()
  {
    CategoryGeneratorImpl categoryGenerator = new CategoryGeneratorImpl();
    return categoryGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProductGenerator createProductGenerator()
  {
    ProductGeneratorImpl productGenerator = new ProductGeneratorImpl();
    return productGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectNameGenerator createProjectNameGenerator()
  {
    ProjectNameGeneratorImpl projectNameGenerator = new ProjectNameGeneratorImpl();
    return projectNameGenerator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DropinLocation createDropinLocation()
  {
    DropinLocationImpl dropinLocation = new DropinLocationImpl();
    return dropinLocation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletPackage getTargletPackage()
  {
    return (TargletPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static TargletPackage getPackage()
  {
    return TargletPackage.eINSTANCE;
  }

} // TargletFactoryImpl

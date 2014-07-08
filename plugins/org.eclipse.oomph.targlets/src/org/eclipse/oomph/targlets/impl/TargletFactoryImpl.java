/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
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
      case TargletPackage.TARGLET:
        return createTarglet();
      case TargletPackage.COMPONENT_EXTENSION:
        return createComponentExtension();
      case TargletPackage.COMPONENT_DEFINITION:
        return createComponentDefinition();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
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
    String activeRepositoryList = source.getActiveRepositoryList();
    if (activeRepositoryList != null && activeRepositoryList.length() == 0)
    {
      activeRepositoryList = null;
    }

    Targlet targlet = createTarglet();
    targlet.setName(source.getName());
    targlet.setActiveRepositoryList(activeRepositoryList);
    targlet.setIncludeSources(source.isIncludeSources());
    targlet.setIncludeAllPlatforms(source.isIncludeAllPlatforms());

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

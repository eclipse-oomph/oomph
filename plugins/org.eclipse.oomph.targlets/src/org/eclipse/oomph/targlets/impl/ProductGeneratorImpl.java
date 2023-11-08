/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ProductGenerator;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.internal.p2.publisher.eclipse.IProductDescriptor;
import org.eclipse.equinox.internal.p2.publisher.eclipse.ProductFile;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.Version;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Product Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ProductGeneratorImpl extends ModelElementImpl implements ProductGenerator
{
  private static final Set<String> FRAGMENT_SUFFIXES;

  static
  {
    Set<String> suffixes = new LinkedHashSet<String>();
    for (String os : Platform.knownOSValues())
    {
      suffixes.add('.' + os);
    }

    for (String arch : Platform.knownOSArchValues())
    {
      suffixes.add('.' + arch);
    }

    FRAGMENT_SUFFIXES = Collections.unmodifiableSet(suffixes);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProductGeneratorImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TargletPackage.Literals.PRODUCT_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void generateIUs(IProject project, final String qualifierReplacement, Map<String, Version> iuVersions, final EList<IInstallableUnit> result)
      throws Exception
  {
    Predicate<IFile> filter = new Predicate<>()
    {
      @Override
      public boolean apply(IFile file)
      {
        return "product".equals(file.getFileExtension()); //$NON-NLS-1$
      }
    };

    ResourcesUtil.runWithFiles(project, Path.EMPTY, filter, new ResourcesUtil.RunnableWithFile()
    {
      @Override
      public void run(File projectFolder, File file) throws Exception
      {
        IProductDescriptor productDescriptor = new ProductFile(file.getAbsolutePath());

        ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
        componentDefinition.setID(productDescriptor.getId());
        componentDefinition.setVersion(Version.create(productDescriptor.getVersion()));

        if (productDescriptor.useFeatures())
        {
          addRequirements(componentDefinition, productDescriptor.getFeatures(IProductDescriptor.INCLUDED_FEATURES | IProductDescriptor.ROOT_FEATURES),
              Requirement.FEATURE_SUFFIX);
        }

        addRequirements(componentDefinition, productDescriptor.getBundles(), ""); //$NON-NLS-1$

        IInstallableUnit iu = ComponentDefGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
        result.add(iu);
      }

      private void addRequirements(ComponentDefinition componentDefinition, List<IVersionedId> versionedIds, String idSuffix)
      {
        EList<Requirement> requirements = componentDefinition.getRequirements();

        for (IVersionedId versionedId : versionedIds)
        {
          String id = versionedId.getId() + idSuffix;
          Version version = versionedId.getVersion();

          Requirement requirement = P2Factory.eINSTANCE.createRequirement(id, version, true);

          int index = id.lastIndexOf('.');
          if (index != -1 && FRAGMENT_SUFFIXES.contains(id.substring(index)))
          {
            requirement.setOptional(true);
          }

          requirements.add(requirement);
        }
      }
    });
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case TargletPackage.PRODUCT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
        try
        {
          generateIUs((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2), (EList<IInstallableUnit>)arguments.get(3));
          return null;
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
    }
    return super.eInvoke(operationID, arguments);
  }

} // ProductGeneratorImpl

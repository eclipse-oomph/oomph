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
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.targlets.ComponentDefGenerator;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.targlets.util.VersionGenerator;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Def Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ComponentDefGeneratorImpl extends ModelElementImpl implements ComponentDefGenerator
{
  private static final IPath DEFINITION_PATH = new Path("component.def"); //$NON-NLS-1$

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComponentDefGeneratorImpl()
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
    return TargletPackage.Literals.COMPONENT_DEF_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception
  {
    IFile file = project.getFile(DEFINITION_PATH);
    if (file.exists())
    {
      InputStream inputStream = null;

      try
      {
        inputStream = file.getContents();

        Resource resource = new BaseResourceFactoryImpl().createResource(null);
        resource.load(inputStream, null);

        ComponentDefinition componentDefinition = BaseUtil.getObjectByType(resource.getContents(), TargletPackage.Literals.COMPONENT_DEFINITION);
        IInstallableUnit iu = generateIU(componentDefinition, qualifierReplacement);
        result.add(iu);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
      }
    }
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
      case TargletPackage.COMPONENT_DEF_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

  @SuppressWarnings("restriction")
  static IInstallableUnit generateIU(ComponentDefinition componentDefinition, String qualifierReplacement) throws Exception
  {
    String id = componentDefinition.getID();
    Version version = VersionGenerator.replaceQualifier(componentDefinition.getVersion(), qualifierReplacement);

    InstallableUnitDescription description = new InstallableUnitDescription();
    description.setId(id);
    description.setVersion(version);
    description.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, "true"); //$NON-NLS-1$
    description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, id, version)));
    description.setTouchpointType(org.eclipse.equinox.spi.p2.publisher.PublisherHelper.TOUCHPOINT_OSGI);
    description.setArtifacts(new IArtifactKey[0]);
    if (!id.endsWith(Requirement.FEATURE_SUFFIX))
    {
      description.addProvidedCapabilities(Arrays.asList(new IProvidedCapability[] { BundlesAction.BUNDLE_CAPABILITY,
          MetadataFactory.createProvidedCapability(BundlesAction.CAPABILITY_NS_OSGI_BUNDLE, id, version) }));
    }

    IInstallableUnit iu = MetadataFactory.createInstallableUnit(description);
    ComponentExtGeneratorImpl.modifyIU(componentDefinition, iu);
    return iu;
  }

} // ComponentDefGeneratorImpl

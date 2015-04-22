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

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.ComponentGenerator;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.targlets.util.VersionGenerator;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.ECollections;
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
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ComponentGeneratorImpl extends ModelElementImpl implements ComponentGenerator
{
  private static final IPath DEFINITION_PATH = new Path("component.def");

  private static final IPath EXTENSION_PATH = new Path("component.ext");

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComponentGeneratorImpl()
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
    return TargletPackage.Literals.COMPONENT_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<IInstallableUnit> generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
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
        return ECollections.singletonEList(iu);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void modifyIU(IInstallableUnit iu, IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
  {
    IFile file = project.getFile(EXTENSION_PATH);
    if (file.exists())
    {
      InputStream inputStream = null;

      try
      {
        inputStream = file.getContents();

        Resource resource = new BaseResourceFactoryImpl().createResource(null);
        resource.load(inputStream, null);

        ComponentExtension componentExtension = BaseUtil.getObjectByType(resource.getContents(), TargletPackage.Literals.COMPONENT_EXTENSION);
        modifyIU(componentExtension, iu);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
      }
    }
  }

  @SuppressWarnings("restriction")
  public static IInstallableUnit generateIU(ComponentDefinition componentDefinition, String qualifierReplacement) throws Exception
  {
    String id = componentDefinition.getID();
    Version version = VersionGenerator.replaceQualifier(componentDefinition.getVersion(), qualifierReplacement);

    InstallableUnitDescription description = new InstallableUnitDescription();
    description.setId(id);
    description.setVersion(version);
    description.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, "true");
    description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, id, version)));
    description.setTouchpointType(org.eclipse.equinox.spi.p2.publisher.PublisherHelper.TOUCHPOINT_OSGI);
    description.setArtifacts(new IArtifactKey[0]);
    if (!id.endsWith(".feature.group"))
    {
      description.addProvidedCapabilities(Arrays.asList(new IProvidedCapability[] { BundlesAction.BUNDLE_CAPABILITY,
          MetadataFactory.createProvidedCapability(BundlesAction.CAPABILITY_NS_OSGI_BUNDLE, id, version) }));
    }

    IInstallableUnit iu = MetadataFactory.createInstallableUnit(description);
    modifyIU(componentDefinition, iu);
    return iu;
  }

  public static void modifyIU(ComponentExtension componentExtension, IInstallableUnit host) throws Exception
  {
    // TODO It would be better to work with a new InstallableUnitDescription
    if (host instanceof org.eclipse.equinox.internal.p2.metadata.InstallableUnit)
    {
      org.eclipse.equinox.internal.p2.metadata.InstallableUnit iu = (org.eclipse.equinox.internal.p2.metadata.InstallableUnit)host;
      List<IRequirement> requirements = new ArrayList<IRequirement>(iu.getRequirements());

      for (Requirement requirement : componentExtension.getRequirements())
      {
        String id = requirement.getName();
        VersionRange versionRange = requirement.getVersionRange();

        String namespace;
        if (requirement.isFeature())
        {
          namespace = IInstallableUnit.NAMESPACE_IU_ID;
        }
        else
        {
          namespace = "osgi.bundle";
        }

        requirements.add(MetadataFactory.createRequirement(namespace, id, versionRange, null, requirement.isOptional(), true, true));
      }

      iu.setRequiredCapabilities(requirements.toArray(new IRequirement[requirements.size()]));
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
      case TargletPackage.COMPONENT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP:
        try
        {
          return generateIUs((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2));
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
      case TargletPackage.COMPONENT_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP:
        try
        {
          modifyIU((IInstallableUnit)arguments.get(0), (IProject)arguments.get(1), (String)arguments.get(2), (Map<String, Version>)arguments.get(3));
          return null;
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
    }
    return super.eInvoke(operationID, arguments);
  }

} // ComponentGeneratorImpl

/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.p2.RequirementType;
import org.eclipse.oomph.targlets.ComponentExtGenerator;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Ext Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ComponentExtGeneratorImpl extends ModelElementImpl implements ComponentExtGenerator
{
  private static final IPath EXTENSION_PATH = new Path("component.ext");

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComponentExtGeneratorImpl()
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
    return TargletPackage.Literals.COMPONENT_EXT_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception
  {
    if (!result.isEmpty())
    {
      IInstallableUnit mainIU = result.get(0);

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
          ComponentExtGeneratorImpl.modifyIU(componentExtension, mainIU);
        }
        finally
        {
          IOUtil.closeSilent(inputStream);
        }
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
      case TargletPackage.COMPONENT_EXT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

  static void modifyIU(ComponentExtension componentExtension, IInstallableUnit host) throws Exception
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
        if (requirement.getType() != RequirementType.NONE)
        {
          namespace = IInstallableUnit.NAMESPACE_IU_ID;
        }
        else
        {
          namespace = "osgi.bundle";
        }

        requirements.add(MetadataFactory.createRequirement(namespace, id, versionRange, requirement.getFilter(), requirement.isOptional(), true, true));
      }

      iu.setRequiredCapabilities(requirements.toArray(new IRequirement[requirements.size()]));
    }
  }

} // ComponentExtGeneratorImpl

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
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.SiteGenerator;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.p2.updatesite.SiteBundle;
import org.eclipse.equinox.internal.p2.updatesite.SiteFeature;
import org.eclipse.equinox.internal.p2.updatesite.SiteModel;
import org.eclipse.equinox.internal.p2.updatesite.UpdateSite;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Site Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class SiteGeneratorImpl extends ModelElementImpl implements SiteGenerator
{
  private static final IPath SITE_XML_PATH = new Path("site.xml");

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SiteGeneratorImpl()
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
    return TargletPackage.Literals.SITE_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(final IProject project, final String qualifierReplacement, Map<String, Version> iuVersions, final EList<IInstallableUnit> result)
      throws Exception
  {
    final IPath path = getPath();

    ResourcesUtil.runWithFile(project, path, new ResourcesUtil.RunnableWithFile()
    {
      public void run(File projectFolder, File file) throws Exception
      {
        final IProvisioningAgent provisioningAgent = P2Util.getCurrentProvisioningAgent();
        Transport transport = (Transport)provisioningAgent.getService(Transport.SERVICE_NAME);
        URI location = new File(projectFolder, path.toString()).toURI();

        UpdateSite updateSite = loadFile(location, transport);
        SiteModel site = updateSite.getSite();

        String name = project.getDescription().getName();

        ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
        componentDefinition.setID(name);
        componentDefinition.setVersion(Version.createOSGi(1, 0, 0, "qualifier"));

        EList<Requirement> requirements = componentDefinition.getRequirements();

        for (SiteFeature feature : site.getFeatures())
        {
          String id = feature.getFeatureIdentifier() + Requirement.FEATURE_SUFFIX;
          Version version = Version.create(feature.getFeatureVersion());
          requirements.add(P2Factory.eINSTANCE.createRequirement(id, version, true));
        }

        for (SiteBundle bundle : site.getBundles())
        {
          String id = bundle.getBundleIdentifier();
          Version version = Version.create(bundle.getBundleVersion());
          requirements.add(P2Factory.eINSTANCE.createRequirement(id, version, true));
        }

        IInstallableUnit iu = ComponentDefGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
        result.add(iu);
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
      case TargletPackage.SITE_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

  protected IPath getPath()
  {
    return SITE_XML_PATH;
  }

  protected UpdateSite loadFile(URI location, Transport transport) throws ProvisionException
  {
    return UpdateSite.load(location, transport, new NullProgressMonitor());
  }

} // SiteGeneratorImpl

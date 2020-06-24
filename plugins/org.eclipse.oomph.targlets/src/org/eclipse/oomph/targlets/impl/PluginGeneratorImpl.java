/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.PluginGenerator;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.targlets.util.VersionGenerator;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plugin Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class PluginGeneratorImpl extends ModelElementImpl implements PluginGenerator
{
  private static final IPath MANIFEST_PATH = new Path(JarFile.MANIFEST_NAME);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PluginGeneratorImpl()
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
    return TargletPackage.Literals.PLUGIN_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, final String qualifierReplacement, final Map<String, Version> iuVersions, final EList<IInstallableUnit> result)
      throws Exception
  {
    ResourcesUtil.runWithFile(project, MANIFEST_PATH, new ResourcesUtil.RunnableWithFile()
    {
      public void run(File projectFolder, File file) throws Exception
      {
        BundleGeneratorAction action = new BundleGeneratorAction();
        action.generateIUs(projectFolder, qualifierReplacement, iuVersions, result);
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
      case TargletPackage.PLUGIN_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

  /**
   * @author Eike Stepper
   */
  private static final class BundleGeneratorAction extends BundlesAction
  {
    public BundleGeneratorAction()
    {
      super((File[])null);
      setPublisherInfo(new PublisherInfo());
    }

    public void generateIUs(File projectFolder, String qualifierReplacement, Map<String, Version> ius, EList<IInstallableUnit> result) throws Exception
    {
      Dictionary<String, String> manifest = loadManifest(projectFolder);
      if (manifest == null)
      {
        return;
      }

      String version = manifest.get(org.osgi.framework.Constants.BUNDLE_VERSION);
      manifest.put(org.osgi.framework.Constants.BUNDLE_VERSION, VersionGenerator.replaceQualifier(version, qualifierReplacement));

      BundleDescription description = createBundleDescription(manifest, projectFolder);
      if (description == null)
      {
        return;
      }

      createAdviceFileAdvice(description, info);

      IInstallableUnit iu = createBundleIU(description, null, info);
      if (iu instanceof InstallableUnit)
      {
        InstallableUnit installableUnit = (InstallableUnit)iu;
        installableUnit.setArtifacts(new IArtifactKey[0]);

        List<IRequirement> requirements = new ArrayList<IRequirement>(iu.getRequirements());
        boolean relaxed = false;
        for (ListIterator<IRequirement> it = requirements.listIterator(); it.hasNext();)
        {
          IRequirement requirement = it.next();
          if (!P2Util.isSimpleRequiredCapability(requirement))
          {
            try
            {
              // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=532569 for why we relax this requirement.
              it.set(MetadataFactory.createRequirement(requirement.getMatches(), requirement.getFilter(), 0, requirement.getMax(), true));
              relaxed = true;
            }
            catch (RuntimeException exception)
            {
              //$FALL-THROUGH$
            }
          }
        }

        if (relaxed)
        {
          installableUnit.setRequiredCapabilities(requirements.toArray(new IRequirement[requirements.size()]));
        }

        File buildPropertiesFile = new File(projectFolder, "build.properties"); //$NON-NLS-1$
        if (buildPropertiesFile.exists())
        {
          Map<String, String> properties = PropertiesUtil.loadProperties(buildPropertiesFile);
          String additionalBundles = properties.get("additional.bundles"); //$NON-NLS-1$
          if (additionalBundles != null)
          {
            List<IRequirement> additionalRequirements = new ArrayList<IRequirement>();
            for (String bundle : additionalBundles.trim().split("\\s*,\\s*")) //$NON-NLS-1$
            {
              additionalRequirements
                  .add(MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, bundle, VersionRange.emptyRange, null, false, false, true));
            }

            if (!additionalRequirements.isEmpty())
            {
              additionalRequirements.addAll(0, installableUnit.getRequirements());
              installableUnit.setRequiredCapabilities(additionalRequirements.toArray(new IRequirement[additionalRequirements.size()]));
            }
          }
        }
      }

      result.add(iu);

      InstallableUnitDescription[] otherDescriptions = processAdditionalInstallableUnitsAdvice(iu, info);
      if (otherDescriptions != null)
      {
        for (InstallableUnitDescription otherDescription : otherDescriptions)
        {
          IInstallableUnit otherIU = MetadataFactory.createInstallableUnit(otherDescription);
          result.add(otherIU);
        }
      }
    }
  }

} // PluginGeneratorImpl

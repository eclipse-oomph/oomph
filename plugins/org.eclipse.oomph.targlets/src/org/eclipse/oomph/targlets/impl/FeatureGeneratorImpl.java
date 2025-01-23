/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.targlets.util.VersionGenerator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.internal.p2.metadata.OSGiVersion;
import org.eclipse.equinox.internal.p2.metadata.RequiredCapability;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.publisher.AdviceFileAdvice;
import org.eclipse.equinox.p2.publisher.IPublisherInfo;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.eclipse.Feature;
import org.eclipse.equinox.p2.publisher.eclipse.FeaturesAction;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class FeatureGeneratorImpl extends ModelElementImpl implements FeatureGenerator
{
  private static final IPath MANIFEST_PATH = new Path(FEATURE_XML);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FeatureGeneratorImpl()
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
    return TargletPackage.Literals.FEATURE_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void generateIUs(IProject project, final String qualifierReplacement, final Map<String, Version> iuVersions, final EList<IInstallableUnit> result)
      throws Exception
  {
    ResourcesUtil.runWithFile(project, MANIFEST_PATH, new ResourcesUtil.RunnableWithFile()
    {
      @Override
      public void run(File projectFolder, File file) throws Exception
      {
        FeatureGeneratorAction action = new FeatureGeneratorAction();
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
      case TargletPackage.FEATURE_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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
  private static final class FeatureGeneratorAction extends FeaturesAction
  {
    public FeatureGeneratorAction()
    {
      super((File[])null);
      setPublisherInfo(new PublisherInfo());
    }

    public void generateIUs(File projectFolder, String qualifierReplacement, final Map<String, Version> ius, EList<IInstallableUnit> result) throws Exception
    {
      Feature[] features = getFeatures(new File[] { projectFolder });
      if (features == null || features.length == 0)
      {
        return;
      }

      Feature feature = features[0];

      String version = feature.getVersion();
      feature.setVersion(VersionGenerator.replaceQualifier(version, qualifierReplacement));

      createAdviceFileAdvice(feature, info);

      feature.setEnvironment("", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

      List<IInstallableUnit> childIUs = Collections.emptyList();
      InstallableUnit iu = (InstallableUnit)createGroupIU(feature, childIUs, info);
      List<IRequirement> requirements = iu.getRequirements();

      String licenseFeature = feature.getLicenseFeature();
      String licenseFeatureVersion = feature.getLicenseFeatureVersion();
      boolean hasLicenseFeature = licenseFeature != null && licenseFeatureVersion != null;

      int size = requirements.size();
      IRequirement[] newRequirements = new IRequirement[size + (hasLicenseFeature ? 1 : 0)];

      if (hasLicenseFeature)
      {
        VersionRange osgiRange;

        Version osgiVersion = OSGiVersion.create(licenseFeatureVersion);
        if (osgiVersion.equals(OSGiVersion.emptyVersion))
        {
          osgiRange = VersionRange.emptyRange;
        }
        else
        {
          osgiRange = new VersionRange(osgiVersion, true, osgiVersion, true);

          VersionRange adjustedRange = adjustQualifier(osgiRange);
          if (adjustedRange != null)
          {
            osgiRange = adjustedRange;
          }
        }

        String namespace = IInstallableUnit.NAMESPACE_IU_ID;
        String name = licenseFeature + Requirement.FEATURE_SUFFIX;
        IRequirement requirement = MetadataFactory.createRequirement(namespace, name, osgiRange, null, true, false);
        newRequirements[size] = requirement;

        iu.setProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_ID, name);
        iu.setProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_VERSION_RANGE, osgiRange.toString());
      }

      // Adjust childIU requirements to support possible .qualifier specifications
      if (qualifierReplacement != null)
      {
        for (int i = 0; i < size; i++)
        {
          IRequirement requirement = requirements.get(i);
          if (requirement instanceof RequiredCapability && P2Util.isSimpleRequiredCapability(requirement))
          {
            RequiredCapability capability = (RequiredCapability)requirement;
            final VersionRange originalRange = capability.getRange();
            final VersionRange adjustedRange = adjustQualifier(originalRange);
            final String namespace = capability.getNamespace();
            final String name = capability.getName();
            final IMatchExpression<IInstallableUnit> filter = capability.getFilter();
            final int min = capability.getMin();
            final int max = capability.getMax();
            final boolean greedy = capability.isGreedy();
            if (adjustedRange != null)
            {
              requirement = MetadataFactory.createRequirement(namespace, name, adjustedRange, filter, min, max, greedy);
            }
            else
            {
              requirement = new IRequiredCapability()
              {
                private IRequirement delegate;

                private VersionRange versionRange;

                private IRequirement getDelegate()
                {
                  if (delegate == null)
                  {
                    delegate = MetadataFactory.createRequirement(namespace, name, getRange(), filter, min, max, greedy);
                  }

                  return delegate;
                }

                @Override
                public String getName()
                {
                  return name;
                }

                @Override
                public String getNamespace()
                {
                  return namespace;
                }

                @Override
                public VersionRange getRange()
                {
                  if (versionRange == null)
                  {
                    Version version = ius.get(name);
                    if (version != null)
                    {
                      versionRange = P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MICRO);
                    }
                    else
                    {
                      versionRange = originalRange;
                    }
                  }

                  return versionRange;
                }

                @Override
                public int getMin()
                {
                  return getDelegate().getMin();
                }

                @Override
                public int getMax()
                {
                  return getDelegate().getMax();
                }

                @Override
                public IMatchExpression<IInstallableUnit> getFilter()
                {
                  return getDelegate().getFilter();
                }

                @Override
                public IMatchExpression<IInstallableUnit> getMatches()
                {
                  return getDelegate().getMatches();
                }

                @Override
                public boolean isMatch(IInstallableUnit iu)
                {
                  return getDelegate().isMatch(iu);
                }

                @Override
                public boolean isGreedy()
                {
                  return getDelegate().isGreedy();
                }

                @Override
                public String getDescription()
                {
                  return getDelegate().getDescription();
                }

                @Override
                public String toString()
                {
                  if (delegate != null)
                  {
                    return delegate.toString();
                  }

                  return name + " " + versionRange; //$NON-NLS-1$
                }
              };
            }
          }

          newRequirements[i] = requirement;
        }

        iu.setRequiredCapabilities(newRequirements);
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

    private void createAdviceFileAdvice(Feature feature, IPublisherInfo publisherInfo)
    {
      // assume p2.inf is co-located with feature.xml
      String location = feature.getLocation();
      if (location != null)
      {
        String groupId = getTransformedId(feature.getId(), /* isPlugin */false, /* isGroup */true);
        AdviceFileAdvice advice = new AdviceFileAdvice(groupId, Version.parseVersion(feature.getVersion()), new Path(location), new Path("p2.inf")); //$NON-NLS-1$
        if (advice.containsAdvice())
        {
          publisherInfo.addAdvice(advice);
        }
      }
    }

    private static String getTransformedId(String original, boolean isPlugin, boolean isGroup)
    {
      return isPlugin ? original : original + (isGroup ? Requirement.FEATURE_SUFFIX : ".feature.jar"); //$NON-NLS-1$
    }

    private static VersionRange adjustQualifier(VersionRange range)
    {
      if (!VersionRange.emptyRange.equals(range))
      {
        Version minimum = range.getMinimum();
        if (minimum instanceof OSGiVersion)
        {
          OSGiVersion osgiMinimum = (OSGiVersion)minimum;
          if (osgiMinimum.equals(range.getMaximum()))
          {
            minimum = OSGiVersion.createOSGi(osgiMinimum.getMajor(), osgiMinimum.getMinor(), osgiMinimum.getMicro());
            Version maximum = OSGiVersion.createOSGi(osgiMinimum.getMajor(), osgiMinimum.getMinor(), osgiMinimum.getMicro() + 1);

            return new VersionRange(minimum, true, maximum, false);
          }
        }
      }

      return null;
    }
  }

} // FeatureGeneratorImpl

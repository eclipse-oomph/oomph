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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.internal.p2.metadata.OSGiVersion;
import org.eclipse.equinox.internal.p2.metadata.RequiredCapability;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.equinox.p2.publisher.eclipse.Feature;
import org.eclipse.equinox.p2.publisher.eclipse.FeaturesAction;
import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface IUGenerator
{
  public static final String IU_PROPERTY_SOURCE = "org.eclipse.oomph.targlet.source";

  public static final class VersionGenerator
  {
    private static final String QUALIFIER = "qualifier";

    private static final String QUALIFIER_SUFFIX = "." + QUALIFIER;

    private static final int QUALIFIER_LENGTH = QUALIFIER.length();

    public static String generateQualifierReplacement()
    {
      return new SimpleDateFormat("'v'yyyyMMdd-HHmmss").format(new Date());
    }

    public static String replaceQualifier(String version, String qualifierReplacement)
    {
      if (version != null && qualifierReplacement != null && version.endsWith(QUALIFIER_SUFFIX))
      {
        int length = version.length();
        StringBuilder result = new StringBuilder(length - QUALIFIER_LENGTH + qualifierReplacement.length());
        result.append(version, 0, length - QUALIFIER_LENGTH);
        result.append(qualifierReplacement);
        version = result.toString();
      }

      return version;
    }

    public static Version replaceQualifier(Version version, String qualifierReplacement)
    {
      if (version != null && version.isOSGiCompatible())
      {
        version = Version.create(replaceQualifier(version.toString(), qualifierReplacement));
      }

      return version;
    }
  }

  public IInstallableUnit generateIU(File location, String qualifierReplacement, Map<String, Version> ius) throws Exception;

  /**
   * @author Eike Stepper
   */
  public static final class BundleIUGenerator extends BundlesAction implements IUGenerator
  {
    public static final IUGenerator INSTANCE = new BundleIUGenerator();

    private BundleIUGenerator()
    {
      super((File[])null);
      setPublisherInfo(new PublisherInfo());
    }

    public IInstallableUnit generateIU(File location, String qualifierReplacement, Map<String, Version> ius) throws Exception
    {
      Dictionary<String, String> manifest = loadManifest(location);
      if (manifest == null)
      {
        return null;
      }

      String version = manifest.get(org.osgi.framework.Constants.BUNDLE_VERSION);
      manifest.put(org.osgi.framework.Constants.BUNDLE_VERSION, VersionGenerator.replaceQualifier(version, qualifierReplacement));

      BundleDescription description = createBundleDescription(manifest, location);
      if (description == null)
      {
        return null;
      }

      IInstallableUnit iu = createBundleIU(description, null, info);
      if (iu instanceof InstallableUnit)
      {
        ((InstallableUnit)iu).setArtifacts(new IArtifactKey[0]);
        ((InstallableUnit)iu).setProperty(IU_PROPERTY_SOURCE, Boolean.TRUE.toString());
      }

      return iu;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class FeatureIUGenerator extends FeaturesAction implements IUGenerator
  {
    public static final String PROP_REQUIRED_LICENCSE_FEATURE_ID = "org.eclipse.oomph.targlets.core.requiredLicenseFeatureID";

    public static final String PROP_REQUIRED_LICENCSE_FEATURE_VERSION_RANGE = "org.eclipse.oomph.targlets.core.requiredLicenseFeatureVersionRange";

    public static final IUGenerator INSTANCE = new FeatureIUGenerator();

    private FeatureIUGenerator()
    {
      super((File[])null);
      setPublisherInfo(new PublisherInfo());
    }

    public IInstallableUnit generateIU(File location, String qualifierReplacement, final Map<String, Version> ius) throws Exception
    {
      Feature[] features = getFeatures(new File[] { location });
      if (features == null || features.length == 0)
      {
        return null;
      }

      Feature feature = features[0];

      String version = feature.getVersion();
      feature.getId();
      feature.setVersion(VersionGenerator.replaceQualifier(version, qualifierReplacement));

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
        String name = licenseFeature + ".feature.group";
        IRequirement requirement = MetadataFactory.createRequirement(namespace, name, osgiRange, null, true, false);
        newRequirements[size] = requirement;

        iu.setProperty(PROP_REQUIRED_LICENCSE_FEATURE_ID, name);
        iu.setProperty(PROP_REQUIRED_LICENCSE_FEATURE_VERSION_RANGE, osgiRange.toString());
      }

      // Adjust childIU requirements to support possible .qualifier specifications
      if (qualifierReplacement != null)
      {
        for (int i = 0; i < size; i++)
        {
          IRequirement requirement = requirements.get(i);
          if (requirement instanceof RequiredCapability)
          {
            RequiredCapability capability = (RequiredCapability)requirement;
            final VersionRange originalRange = capability.getRange();
            final VersionRange adjustedRange = adjustQualifier(originalRange);
            final String namespace = capability.getNamespace();
            final String name = capability.getName();
            final IMatchExpression<IInstallableUnit> filter = capability.getFilter();
            final boolean optional = capability.getMin() == 0;
            final boolean multiple = capability.getMax() > 1;
            if (adjustedRange != null)
            {
              requirement = MetadataFactory.createRequirement(namespace, name, adjustedRange, filter, optional, multiple);
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
                    delegate = MetadataFactory.createRequirement(namespace, name, getRange(), filter, optional, multiple);
                  }

                  return delegate;
                }

                public String getName()
                {
                  return name;
                }

                public String getNamespace()
                {
                  return namespace;
                }

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

                public int getMin()
                {
                  return getDelegate().getMin();
                }

                public int getMax()
                {
                  return getDelegate().getMax();
                }

                public IMatchExpression<IInstallableUnit> getFilter()
                {
                  return getDelegate().getFilter();
                }

                public IMatchExpression<IInstallableUnit> getMatches()
                {
                  return getDelegate().getMatches();
                }

                public boolean isMatch(IInstallableUnit iu)
                {
                  return getDelegate().isMatch(iu);
                }

                public boolean isGreedy()
                {
                  return getDelegate().isGreedy();
                }

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

                  return name + " " + versionRange;
                }
              };
            }
          }

          newRequirements[i] = requirement;
        }

        iu.setRequiredCapabilities(newRequirements);
      }

      iu.setProperty(IU_PROPERTY_SOURCE, Boolean.TRUE.toString());
      return iu;
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
}

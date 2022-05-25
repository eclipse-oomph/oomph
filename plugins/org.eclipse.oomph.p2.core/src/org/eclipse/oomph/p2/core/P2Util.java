/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.p2.internal.core.CachingTransport;
import org.eclipse.oomph.p2.internal.core.P2CorePlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.URIUtil;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.ProfileScope;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnitFragment;
import org.eclipse.equinox.p2.metadata.IInstallableUnitPatch;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.IRequirementChange;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.expression.ExpressionUtil;
import org.eclipse.equinox.p2.metadata.expression.IExpression;
import org.eclipse.equinox.p2.metadata.expression.IFilterExpression;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.equinox.p2.repository.spi.PGPPublicKeyService;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.osgi.service.prefs.BackingStoreException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class P2Util
{

  public static final String X509_CERTIFICATE_FILE_EXTENSION = ".der"; //$NON-NLS-1$

  public static final String PGP_KEY_FILE_EXTENSION = ".asc"; //$NON-NLS-1$

  private static final String PREFERENCES_FILE_NAME = "preferences.properties"; //$NON-NLS-1$

  private static final Pattern OSGI_PROPERTY_FILTER = Pattern.compile("(!?)\\((osgi.arch|osgi.os|osgi.ws)=([^)]+)\\)"); //$NON-NLS-1$

  private P2Util()
  {
  }

  public static synchronized AgentManager getAgentManager()
  {
    if (AgentManagerImpl.instance == null)
    {
      AgentManagerImpl.instance = new AgentManagerImpl();
    }

    return AgentManagerImpl.instance;
  }

  public static File getAgentLocation(IProvisioningAgent agent)
  {
    IAgentLocation location = (IAgentLocation)agent.getService(IAgentLocation.SERVICE_NAME);
    return URIUtil.toFile(location.getRootLocation());
  }

  public static IProvisioningAgent getCurrentProvisioningAgent()
  {
    return getAgentManager().getCurrentAgent().getProvisioningAgent();
  }

  public static Agent createAgent(File agentLocation)
  {
    return new AgentImpl((AgentManagerImpl)P2Util.getAgentManager(), agentLocation);
  }

  public static Set<String> getKnownRepositories(IRepositoryManager<?> manager)
  {
    Set<String> result = new HashSet<>();
    for (URI uri : manager.getKnownRepositories(IRepositoryManager.REPOSITORIES_NON_SYSTEM))
    {
      result.add(uri.toString());
    }

    return result;
  }

  public static File getCacheFile(URI uri)
  {
    Agent agent = getAgentManager().getCurrentAgent();

    IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
    if (manager instanceof CachingRepositoryManager)
    {
      CachingTransport transport = ((CachingRepositoryManager<?>)manager).getTransport();
      if (transport != null)
      {
        return transport.getCacheFile(uri);
      }
    }

    return null;
  }

  public static void copyTrustPreferences(Profile sourceProfile, Profile targetProfile)
  {
    if (sourceProfile != null && targetProfile != null)
    {
      IEclipsePreferences source = getTrustPreferences(sourceProfile);
      IEclipsePreferences target = getTrustPreferences(targetProfile);
      try
      {
        for (String key : source.keys())
        {
          target.put(key, source.get(key, null));
        }

        target.flush();
      }
      catch (BackingStoreException ex)
      {
        P2CorePlugin.INSTANCE.log(ex);
      }
    }
  }

  @SuppressWarnings("restriction")
  public static IEclipsePreferences getTrustPreferences(Profile profile)
  {
    return new ProfileScope(profile.getProvisioningAgent().getService(IAgentLocation.class), profile.getProfileId())
        .getNode(org.eclipse.equinox.internal.p2.engine.EngineActivator.ID);
  }

  @SuppressWarnings("restriction")
  public static void mergeGlobalTrustPreferences(Profile profile)
  {
    synchronized (profile)
    {
      // Copy the global preference settings into the current agent's profile preferences.
      org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker currentCertificateChecker = createCertificateCheker(profile);

      File globalTrustFolder = getGlobalTrustFolder();

      boolean trustAlways = "true".equals(PropertiesUtil.getProperties(new File(globalTrustFolder, PREFERENCES_FILE_NAME)) //$NON-NLS-1$
          .getOrDefault(org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker.TRUST_ALWAYS_PROPERTY, "false")); //$NON-NLS-1$
      CertificateFactory certificateFactory = null;
      try
      {
        certificateFactory = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
      }
      catch (CertificateException ex)
      {
        P2CorePlugin.INSTANCE.log(ex);
      }

      org.eclipse.equinox.internal.p2.artifact.processors.pgp.PGPPublicKeyStore trustedKeys = new org.eclipse.equinox.internal.p2.artifact.processors.pgp.PGPPublicKeyStore();
      Set<Certificate> trustedCertificates = new LinkedHashSet<>();
      for (File file : globalTrustFolder.listFiles())
      {
        String name = file.getName();
        if (name.endsWith(PGP_KEY_FILE_EXTENSION))
        {
          trustedKeys.add(file);
        }
        else if (certificateFactory != null && name.endsWith(X509_CERTIFICATE_FILE_EXTENSION))
        {
          try (InputStream input = new BufferedInputStream(Files.newInputStream(file.toPath())))
          {
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(input);
            trustedCertificates.addAll(certificates);
          }
          catch (Exception ex)
          {
            P2CorePlugin.INSTANCE.log(ex);
          }
        }
      }

      trustedCertificates.addAll(currentCertificateChecker.getPreferenceTrustedCertificates());
      for (PGPPublicKey key : currentCertificateChecker.getPreferenceTrustedKeys().all())
      {
        trustedKeys.addKey(key);
      }

      currentCertificateChecker.setTrustAlways(trustAlways);
      currentCertificateChecker.persistTrustedCertificates(trustedCertificates);
      currentCertificateChecker.persistTrustedKeys(trustedKeys);
    }
  }

  @SuppressWarnings("restriction")
  public static void addedTrustedKeys(Profile profile, Collection<? extends PGPPublicKey> keys)
  {
    synchronized (profile)
    {
      // Copy the global preference settings into the current agent's profile preferences.
      org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker currentCertificateChecker = createCertificateCheker(profile);

      org.eclipse.equinox.internal.p2.artifact.processors.pgp.PGPPublicKeyStore preferenceTrustedKeys = currentCertificateChecker.getPreferenceTrustedKeys();
      keys.forEach(preferenceTrustedKeys::addKey);
      currentCertificateChecker.persistTrustedKeys(preferenceTrustedKeys);
    }
  }

  @SuppressWarnings("restriction")
  public static void saveGlobalTrustPreferences(Profile profile, boolean strict)
  {
    synchronized (profile)
    {
      org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker certificateChecker = createCertificateCheker(profile);

      boolean trustAlways = certificateChecker.isTrustAlways();
      Set<? extends Certificate> trustedCertificates = certificateChecker.getPreferenceTrustedCertificates();
      org.eclipse.equinox.internal.p2.artifact.processors.pgp.PGPPublicKeyStore trustedKeys = certificateChecker.getPreferenceTrustedKeys();

      File trustFolder = getGlobalTrustFolder();

      File propertiesFile = new File(trustFolder, PREFERENCES_FILE_NAME);
      Map<String, String> properties = PropertiesUtil.getProperties(propertiesFile);
      properties.put(org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker.TRUST_ALWAYS_PROPERTY, Boolean.toString(trustAlways));
      PropertiesUtil.saveProperties(propertiesFile, properties, false);

      Set<File> retainedFiles = new LinkedHashSet<>();

      for (Certificate certificate : trustedCertificates)
      {
        try
        {
          byte[] encoded = certificate.getEncoded();
          File certificateFile = new File(trustFolder,
              PGPPublicKeyService.toHex(IOUtil.getSHA1(new ByteArrayInputStream(encoded))) + X509_CERTIFICATE_FILE_EXTENSION);
          retainedFiles.add(certificateFile);
          if (!certificateFile.isFile())
          {
            try (OutputStream output = Files.newOutputStream(certificateFile.toPath()))
            {
              output.write(encoded);
            }
          }
        }
        catch (Exception ex)
        {
          P2CorePlugin.INSTANCE.log(ex);
        }
      }

      for (PGPPublicKey key : trustedKeys.all())
      {
        File keyFile = new File(trustFolder, PGPPublicKeyService.toHexFingerprint(key) + PGP_KEY_FILE_EXTENSION);
        retainedFiles.add(keyFile);
        if (!keyFile.isFile())
        {
          try (OutputStream output = new ArmoredOutputStream(new FileOutputStream(keyFile)))
          {
            key.encode(output);
          }
          catch (Exception ex)
          {
            P2CorePlugin.INSTANCE.log(ex);
          }
        }
      }

      if (strict)
      {
        for (File file : trustFolder.listFiles())
        {
          if (!retainedFiles.contains(file))
          {

            String name = file.getName();
            if (name.endsWith(PGP_KEY_FILE_EXTENSION) || name.endsWith(X509_CERTIFICATE_FILE_EXTENSION))
            {
              IOUtil.deleteBestEffort(file, false);
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("all")
  public static <T> Iterable<T> asIterable(final IQueryResult<T> queryResult)
  {
    if (queryResult instanceof Iterable<?>)
    {
      return Iterable.class.cast(queryResult);
    }

    return new Iterable<>()
    {
      @Override
      public Iterator<T> iterator()
      {
        return queryResult.iterator();
      }
    };
  }

  @SuppressWarnings("restriction")
  public static boolean isSimpleRequiredCapability(IRequirement requirement)
  {
    return requirement instanceof org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
  }

  public static Runnable preserveBundlePoolTimestamps(File bundlePoolLocation)
  {
    final File featuresFolder = new File(bundlePoolLocation, "features"); //$NON-NLS-1$
    final long featuresFolderLastModified = featuresFolder.lastModified();
    final File pluginsFolder = new File(bundlePoolLocation, "plugins"); //$NON-NLS-1$
    final long pluginsFolderLastModified = pluginsFolder.lastModified();

    return new Runnable()
    {
      @Override
      public void run()
      {
        if (featuresFolderLastModified != 0L)
        {
          featuresFolder.setLastModified(featuresFolderLastModified);
        }

        if (pluginsFolderLastModified != 0L)
        {
          pluginsFolder.setLastModified(pluginsFolderLastModified);
        }
      }
    };
  }

  public static String getName(IInstallableUnit iu)
  {
    String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
    if (StringUtil.isEmpty(name))
    {
      return iu.getId();
    }

    return name;
  }

  public static Map<String, String> toProfilePropertiesMap(String profileProperties)
  {
    Map<String, String> result = new LinkedHashMap<>();
    if (!StringUtil.isEmpty(profileProperties))
    {
      String[] properties = profileProperties.split(","); //$NON-NLS-1$
      for (String property : properties)
      {
        int index = property.indexOf('=');
        if (index == -1)
        {
          result.put(property, null);
        }
        else
        {
          result.put(property.substring(0, index), property.substring(index + 1));
        }
      }
    }

    return result;
  }

  public static String toProfilePropertiesString(Map<String, String> profileProperties)
  {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, String> entry : profileProperties.entrySet())
    {
      if (result.length() != 0)
      {
        result.append(","); //$NON-NLS-1$
      }

      String key = entry.getKey();
      result.append(key);
      String value = entry.getValue();
      if (value != null)
      {
        result.append(' ').append(value);
      }
    }

    return result.toString();
  }

  public static IInstallableUnit createGeneralizedIU(IInstallableUnit iu, boolean isIncludeAllPlatforms, boolean isIncludeAllRequirements,
      boolean isIncludeNegativeRequirements)
  {
    // If we're not including all platform, no generalization is needed.
    if (!isIncludeAllPlatforms && isIncludeAllRequirements && isIncludeNegativeRequirements)
    {
      return iu;
    }

    // Determine the generalized IU filter.
    IMatchExpression<IInstallableUnit> filter = iu.getFilter();
    IMatchExpression<IInstallableUnit> generalizedFilter = isIncludeAllPlatforms ? generalize(filter) : filter;
    boolean needsGeneralization = filter != generalizedFilter;

    // Determine the generalized requirement filters.
    Collection<IRequirement> requirements = iu.getRequirements();
    IRequirement[] generalizedRequirements = requirements.toArray(new IRequirement[requirements.size()]);
    for (int i = 0; i < generalizedRequirements.length; ++i)
    {
      IRequirement requirement = generalizedRequirements[i];
      IMatchExpression<IInstallableUnit> requirementFilter = requirement.getFilter();
      IMatchExpression<IInstallableUnit> generalizedRequirementFilter = isIncludeAllPlatforms ? generalize(requirementFilter) : requirementFilter;

      // If the filter needs generalization, create a clone of the requirement, with the generalized filter replacement.
      int max = requirement.getMax();
      int min = requirement.getMin();
      if (requirementFilter != generalizedRequirementFilter || !isIncludeAllRequirements && min != 0 || !isIncludeNegativeRequirements && max == 0)
      {
        needsGeneralization = true;
        // IRequirement generalizedRequirement = MetadataFactory.createRequirement(requirement.getMatches(), generalizedRequirementFilter,
        // requirement.getMin(), requirement.getMax(), requirement.isGreedy(), requirement.getDescription());
        IRequirement generalizedRequirement = MetadataFactory.createRequirement(requirement.getMatches(), generalizedRequirementFilter,
            requirementFilter != generalizedRequirementFilter || !isIncludeAllRequirements ? 0 : min,
            !isIncludeNegativeRequirements && max == 0 ? Integer.MAX_VALUE : max, true, requirement.getDescription());
        generalizedRequirements[i] = generalizedRequirement;
      }
    }

    // If none of the filters or slicer-mode lower bounds need generalization, the original IU can be used.
    if (!needsGeneralization)
    {
      return iu;
    }

    // Create a description that clones the IU with the generalized filter and slicer-mode lower bound replacements.
    InstallableUnitDescription description;

    if (iu instanceof IInstallableUnitFragment)
    {
      IInstallableUnitFragment installableUnitFragment = (IInstallableUnitFragment)iu;
      MetadataFactory.InstallableUnitFragmentDescription fragmentDescription = new MetadataFactory.InstallableUnitFragmentDescription();
      Collection<IRequirement> host = installableUnitFragment.getHost();
      fragmentDescription.setHost(host.toArray(new IRequirement[host.size()]));
      description = fragmentDescription;
    }
    else if (iu instanceof IInstallableUnitPatch)
    {
      IInstallableUnitPatch installableUnitPatch = (IInstallableUnitPatch)iu;
      MetadataFactory.InstallableUnitPatchDescription patchDescription = new MetadataFactory.InstallableUnitPatchDescription();
      patchDescription.setApplicabilityScope(installableUnitPatch.getApplicabilityScope());
      patchDescription.setLifeCycle(installableUnitPatch.getLifeCycle());
      List<IRequirementChange> requirementsChange = installableUnitPatch.getRequirementsChange();
      patchDescription.setRequirementChanges(requirementsChange.toArray(new IRequirementChange[requirementsChange.size()]));
      description = patchDescription;
    }
    else
    {
      description = new MetadataFactory.InstallableUnitDescription();
    }

    description.setId(iu.getId());

    description.setVersion(iu.getVersion());

    Collection<IArtifactKey> artifacts = iu.getArtifacts();
    description.setArtifacts(artifacts.toArray(new IArtifactKey[artifacts.size()]));

    Collection<IProvidedCapability> providedCapabilities = iu.getProvidedCapabilities();
    description.setCapabilities(providedCapabilities.toArray(new IProvidedCapability[providedCapabilities.size()]));

    description.setCopyright(iu.getCopyright());

    description.setFilter(generalizedFilter);

    Collection<ILicense> licenses = iu.getLicenses();
    description.setLicenses(licenses.toArray(new ILicense[licenses.size()]));

    Collection<IRequirement> metaRequirements = iu.getMetaRequirements();
    description.setMetaRequirements(metaRequirements.toArray(new IRequirement[metaRequirements.size()]));

    description.setRequirements(generalizedRequirements);

    description.setSingleton(iu.isSingleton());

    description.setTouchpointType(iu.getTouchpointType());
    description.setUpdateDescriptor(iu.getUpdateDescriptor());

    for (Iterator<Map.Entry<String, String>> iterator = iu.getProperties().entrySet().iterator(); iterator.hasNext();)
    {
      Map.Entry<String, String> entry = iterator.next();
      description.setProperty(entry.getKey(), entry.getValue());
    }

    for (ITouchpointData touchpointData : iu.getTouchpointData())
    {
      description.addTouchpointData(touchpointData);
    }

    return MetadataFactory.createInstallableUnit(description);
  }

  @SuppressWarnings("restriction")
  private static IMatchExpression<IInstallableUnit> generalize(IMatchExpression<IInstallableUnit> filter)
  {
    if (filter == null)
    {
      return null;
    }

    // Lazily determine if any parameter needs generalization.
    Object[] generalizedParameters = null;
    Object[] parameters = filter.getParameters();
    for (int i = 0; i < parameters.length; ++i)
    {
      Object parameter = parameters[i];
      if (parameter instanceof org.eclipse.equinox.internal.p2.metadata.expression.LDAPFilter)
      {
        String value = parameter.toString();
        Matcher matcher = OSGI_PROPERTY_FILTER.matcher(value);
        if (matcher.find())
        {
          // If the pattern matches, we need to generalize the parameters.
          if (generalizedParameters == null)
          {
            // Copy over all the parameters.
            // The ones that need generalization will be replaced.
            generalizedParameters = new Object[parameters.length];
            System.arraycopy(parameters, 0, generalizedParameters, 0, parameters.length);
          }

          // Build the replacement expression
          StringBuffer result = new StringBuffer();
          if (matcher.group(1).length() == 0)
          {
            matcher.appendReplacement(result, "($2=*)"); //$NON-NLS-1$
          }
          else
          {
            matcher.appendReplacement(result, "!($2=nothing)"); //$NON-NLS-1$
          }

          // Handle all the remaining matches the same way.
          while (matcher.find())
          {
            if (matcher.group(1).length() == 0)
            {
              matcher.appendReplacement(result, "($2=*)"); //$NON-NLS-1$
            }
            else
            {
              matcher.appendReplacement(result, "!($2=nothing)"); //$NON-NLS-1$
            }
          }

          // Complete the replacements, parse it back into an LDAP filter, and replace this parameter.
          matcher.appendTail(result);
          IFilterExpression ldap = ExpressionUtil.parseLDAP(result.toString());
          generalizedParameters[i] = ldap;
        }
      }
    }

    // If one of the parameters needed to be generalized...
    if (generalizedParameters != null)
    {
      // Parse the filter expression and create a new match expressions with the same filter but the generalized parameters.
      IExpression expression = ExpressionUtil.parse(filter.toString());
      return ExpressionUtil.getFactory().matchExpression(expression, generalizedParameters);
    }

    // Otherwise, return the original filter.
    return filter;
  }

  @SuppressWarnings("unused")
  private static InstallableUnitDescription createDescription(IInstallableUnit iu)
  {
    InstallableUnitDescription description = new MetadataFactory.InstallableUnitDescription();

    description.setId(iu.getId());

    description.setVersion(iu.getVersion());

    Collection<IArtifactKey> artifacts = iu.getArtifacts();
    description.setArtifacts(artifacts.toArray(new IArtifactKey[artifacts.size()]));

    Collection<IProvidedCapability> providedCapabilities = iu.getProvidedCapabilities();
    description.setCapabilities(providedCapabilities.toArray(new IProvidedCapability[providedCapabilities.size()]));

    description.setCopyright(iu.getCopyright());

    IMatchExpression<IInstallableUnit> filter = iu.getFilter();
    description.setFilter(filter);

    Collection<ILicense> licenses = iu.getLicenses();
    description.setLicenses(licenses.toArray(new ILicense[licenses.size()]));

    Collection<IRequirement> metaRequirements = iu.getMetaRequirements();
    description.setMetaRequirements(metaRequirements.toArray(new IRequirement[metaRequirements.size()]));

    Collection<IRequirement> requirements = iu.getRequirements();
    description.setRequirements(requirements.toArray(new IRequirement[requirements.size()]));

    description.setSingleton(iu.isSingleton());

    description.setTouchpointType(iu.getTouchpointType());
    description.setUpdateDescriptor(iu.getUpdateDescriptor());

    for (Iterator<Entry<String, String>> iterator = iu.getProperties().entrySet().iterator(); iterator.hasNext();)
    {
      Entry<String, String> entry = iterator.next();
      description.setProperty(entry.getKey(), entry.getValue());
    }

    for (ITouchpointData touchpointData : iu.getTouchpointData())
    {
      description.addTouchpointData(touchpointData);
    }

    return description;
  }

  @SuppressWarnings("restriction")
  private static org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker createCertificateCheker(Profile profile)
  {
    // Copy the global preference settings into the current agent's profile preferences.
    org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker certificateChecker = new org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker(
        profile.getProvisioningAgent());
    certificateChecker.setProfile(profile);
    return certificateChecker;
  }

  private static File getGlobalTrustFolder()
  {
    File folder = P2CorePlugin.getUserStateFolder(new File(PropertiesUtil.getUserHome()));
    File trustFolder = new File(folder, "trust"); //$NON-NLS-1$
    trustFolder.mkdirs();
    return trustFolder;
  }

  /**
   * @author Eike Stepper
   */
  public interface VersionedIdFilter
  {
    public boolean matches(IVersionedId versionedId);
  }
}

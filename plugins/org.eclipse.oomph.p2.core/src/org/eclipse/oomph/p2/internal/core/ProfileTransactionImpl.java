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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Confirmer.Confirmation;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.OfflineUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepository;
import org.eclipse.equinox.internal.p2.director.SimplePlanner;
import org.eclipse.equinox.internal.p2.engine.Operand;
import org.eclipse.equinox.internal.p2.engine.ProvisioningPlan;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.provisional.p2.director.PlanExecutionHelper;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.engine.IEngine;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.planner.ProfileInclusionRules;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ProfileTransactionImpl implements ProfileTransaction
{
  private static final String SOURCE_IU_ID = "org.eclipse.oomph.p2.source.container"; //$NON-NLS-1$

  private static final IRequirement BUNDLE_REQUIREMENT = MetadataFactory.createRequirement(
      "org.eclipse.equinox.p2.eclipse.type", "bundle", null, null, false, false, false); //$NON-NLS-1$ //$NON-NLS-2$

  private static final Set<String> IMMUTABLE_PROPERTIES = new HashSet<String>(Arrays.asList(Profile.PROP_INSTALL_FEATURES, Profile.PROP_INSTALL_FOLDER,
      Profile.PROP_CACHE, Profile.PROP_PROFILE_TYPE, Profile.PROP_PROFILE_DEFINITION));

  private final Profile profile;

  private final ProfileDefinition profileDefinition;

  private final ProfileDefinition cleanProfileDefinition;

  private final Map<String, String> profileProperties = new HashMap<String, String>();

  private final Map<String, String> cleanProfileProperties = new HashMap<String, String>();

  private final Map<IUPropertyKey, String> iuProperties = new HashMap<IUPropertyKey, String>();

  private final Map<IUPropertyKey, String> cleanIUProperties = new HashMap<IUPropertyKey, String>();

  private boolean removeAll;

  private boolean offline;

  private boolean mirrors;

  private boolean committed;

  public ProfileTransactionImpl(Profile profile)
  {
    this.profile = profile;

    cleanProfileDefinition = profile.getDefinition();
    profileDefinition = EcoreUtil.copy(cleanProfileDefinition);

    cleanProfileProperties.putAll(profile.getProperties());
    cleanProfileProperties.remove(Profile.PROP_INSTALL_FOLDER);
    cleanProfileProperties.remove(Profile.PROP_CACHE);
    cleanProfileProperties.remove(Profile.PROP_PROFILE_DEFINITION);
    profileProperties.putAll(cleanProfileProperties);

    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor())))
    {
      Map<String, String> properties = profile.getInstallableUnitProperties(iu);
      if (!properties.isEmpty())
      {
        for (Map.Entry<String, String> property : properties.entrySet())
        {
          String key = property.getKey();
          String value = property.getValue();
          cleanIUProperties.put(new IUPropertyKey(iu, key), value);
        }
      }
    }

    iuProperties.putAll(cleanIUProperties);

    mirrors = SimpleArtifactRepository.MIRRORS_ENABLED;
  }

  public Profile getProfile()
  {
    return profile;
  }

  public ProfileDefinition getProfileDefinition()
  {
    return profileDefinition;
  }

  public String getProfileProperty(String key)
  {
    return profileProperties.get(key);
  }

  public ProfileTransaction setProfileProperty(String key, String value)
  {
    if (IMMUTABLE_PROPERTIES.contains(key) && !ObjectUtil.equals(profileProperties.get(key), value))
    {
      throw new IllegalArgumentException("Property is immutable: " + key);
    }

    if (value != null)
    {
      profileProperties.put(key, value);
    }
    else
    {
      profileProperties.remove(key);
    }

    return this;
  }

  public ProfileTransaction removeProfileProperty(String key)
  {
    return setProfileProperty(key, null);
  }

  public String getInstallableUnitProperty(IInstallableUnit iu, String key)
  {
    return iuProperties.get(new IUPropertyKey(iu, key));
  }

  public ProfileTransaction setInstallableUnitProperty(IInstallableUnit iu, String key, String value)
  {
    IUPropertyKey propertyKey = new IUPropertyKey(iu, key);
    if (value != null)
    {
      iuProperties.put(propertyKey, value);
    }
    else
    {
      iuProperties.remove(propertyKey);
    }

    return this;
  }

  public ProfileTransaction removeInstallableUnitProperty(IInstallableUnit iu, String key)
  {
    return setInstallableUnitProperty(iu, key, null);
  }

  public boolean isRemoveExistingInstallableUnits()
  {
    return removeAll;
  }

  public ProfileTransaction setRemoveExistingInstallableUnits(boolean removeAll)
  {
    this.removeAll = removeAll;
    return this;
  }

  public boolean isOffline()
  {
    return offline;
  }

  public ProfileTransaction setOffline(boolean offline)
  {
    this.offline = offline;
    return this;
  }

  public boolean isMirrors()
  {
    return mirrors;
  }

  public ProfileTransaction setMirrors(boolean mirrors)
  {
    this.mirrors = mirrors;
    return this;
  }

  public boolean isDirty()
  {
    if (removeAll)
    {
      return true;
    }

    if (!profileProperties.equals(cleanProfileProperties))
    {
      return true;
    }

    if (!iuProperties.equals(cleanIUProperties))
    {
      return true;
    }

    return isProfileDefinitionChanged();
  }

  private boolean isProfileDefinitionChanged()
  {
    if (profileDefinition.isIncludeSourceBundles() != cleanProfileDefinition.isIncludeSourceBundles())
    {
      return true;
    }

    EqualityHelper equalityHelper = new EqualityHelper();
    if (!equals(equalityHelper, profileDefinition.getRequirements(), cleanProfileDefinition.getRequirements()))
    {
      return true;
    }

    if (!equals(equalityHelper, profileDefinition.getRepositories(), cleanProfileDefinition.getRepositories()))
    {
      return true;
    }

    return false;
  }

  public boolean commit() throws CoreException
  {
    return commit(null, null);
  }

  public boolean commit(IProgressMonitor monitor) throws CoreException
  {
    return commit(null, monitor);
  }

  public boolean commit(CommitContext commitContext, IProgressMonitor monitor) throws CoreException
  {
    if (!committed)
    {
      committed = true;

      Resolution resolution = resolve(commitContext, monitor);
      if (resolution != null)
      {
        return resolution.commit(monitor);
      }
    }

    return false;
  }

  public Resolution resolve(CommitContext commitContext, IProgressMonitor monitor) throws CoreException
  {
    final CommitContext context = commitContext == null ? new CommitContext() : commitContext;
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    final Agent agent = profile.getAgent();
    final List<Runnable> cleanup = new ArrayList<Runnable>();

    try
    {
      initOffline(cleanup);
      initMirrors(cleanup);

      List<IMetadataRepository> metadataRepositories = new ArrayList<IMetadataRepository>();
      Set<URI> artifactURIs = new HashSet<URI>();
      URI[] metadataURIs = collectRepositories(metadataRepositories, artifactURIs, cleanup, monitor);

      final ProfileImpl profileImpl = (ProfileImpl)profile;
      final IProfile delegate = profileImpl.getDelegate();
      final long timestamp = delegate.getTimestamp();

      IPlanner planner = agent.getPlanner();
      IProfileChangeRequest profileChangeRequest = planner.createChangeRequest(delegate);
      IInstallableUnit rootIU = adjustProfileChangeRequest(profileChangeRequest, monitor);

      final ProvisioningContext provisioningContext = context.createProvisioningContext(this, profileChangeRequest);
      provisioningContext.setMetadataRepositories(metadataURIs);
      provisioningContext.setArtifactRepositories(artifactURIs.toArray(new URI[artifactURIs.size()]));

      IQueryable<IInstallableUnit> metadata = provisioningContext.getMetadata(monitor);

      final IProvisioningPlan provisioningPlan = planner.getProvisioningPlan(profileChangeRequest, provisioningContext, monitor);
      P2CorePlugin.INSTANCE.coreException(provisioningPlan.getStatus());

      IQueryable<IInstallableUnit> additions = provisioningPlan.getAdditions();
      for (IRequirement requirement : rootIU.getRequirements())
      {
        if (requirement instanceof IRequiredCapability)
        {
          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
          for (IInstallableUnit installableUnit : P2Util.asIterable(additions.query(
              QueryUtil.createIUQuery(requiredCapability.getName(), requiredCapability.getRange()), null)))
          {
            provisioningPlan.setInstallableUnitProfileProperty(installableUnit, Profile.PROP_PROFILE_ROOT_IU, Boolean.TRUE.toString());
            provisioningPlan.setInstallableUnitProfileProperty(installableUnit, SimplePlanner.INCLUSION_RULES,
                ProfileInclusionRules.createStrictInclusionRule(installableUnit));
          }
        }
      }

      provisioningPlan.removeInstallableUnit(rootIU);

      if (profileDefinition.isIncludeSourceBundles())
      {
        IInstallableUnit sourceContainerIU = generateSourceContainerIU(provisioningPlan, metadata, monitor);
        provisioningPlan.addInstallableUnit(sourceContainerIU);
        provisioningPlan.setInstallableUnitProfileProperty(sourceContainerIU, Profile.PROP_PROFILE_ROOT_IU, Boolean.TRUE.toString());
      }

      if (!context.handleProvisioningPlan(provisioningPlan, metadataRepositories))
      {
        return null;
      }

      if (getOperandCount(provisioningPlan) == 0)
      {
        return null;
      }

      return new Resolution()
      {
        public IProvisioningPlan getProvisioningPlan()
        {
          return provisioningPlan;
        }

        public boolean commit(IProgressMonitor monitor) throws CoreException
        {
          try
          {
            IPhaseSet phaseSet = context.getPhaseSet(ProfileTransactionImpl.this);

            initUnsignedContentConfirmer(context, agent, cleanup);

            IEngine engine = agent.getEngine();
            IStatus status = PlanExecutionHelper.executePlan(provisioningPlan, engine, phaseSet, provisioningContext, monitor);
            P2CorePlugin.INSTANCE.coreException(status);

            profileImpl.setDefinition(profileDefinition);
            profileImpl.setDelegate(delegate); // TODO Seems redundant

            return delegate.getTimestamp() != timestamp;
          }
          finally
          {
            cleanup(cleanup);
          }
        }

        public void rollback()
        {
          cleanup(cleanup);
        }
      };
    }
    catch (Throwable t)
    {
      cleanup(cleanup);
      P2CorePlugin.INSTANCE.coreException(t);
      return null;
    }
  }

  private void initOffline(final List<Runnable> cleanup)
  {
    if (offline)
    {
      OfflineUtil.begin(true);
      cleanup.add(new Runnable()
      {
        public void run()
        {
          OfflineUtil.begin(false);
        }
      });
    }
  }

  private void initMirrors(final List<Runnable> cleanup)
  {
    final boolean wasMirrors = SimpleArtifactRepository.MIRRORS_ENABLED;
    if (mirrors != wasMirrors)
    {
      try
      {
        final Field mirrorsEnabledField = ReflectUtil.getField(SimpleArtifactRepository.class, "MIRRORS_ENABLED");
        ReflectUtil.setValue(mirrorsEnabledField, null, mirrors, true);

        cleanup.add(new Runnable()
        {
          public void run()
          {
            try
            {
              ReflectUtil.setValue(mirrorsEnabledField, null, wasMirrors, true);
            }
            catch (Throwable ex)
            {
              // Ignore
            }
          }
        });
      }
      catch (Throwable ex)
      {
        // Ignore
      }
    }
  }

  private void initUnsignedContentConfirmer(final CommitContext context, final Agent agent, final List<Runnable> cleanup)
  {
    final Confirmer unsignedContentConfirmer = context.getUnsignedContentConfirmer();
    if (unsignedContentConfirmer != null)
    {
      final IProvisioningAgent provisioningAgent = agent.getProvisioningAgent();
      final UIServices oldUIServices = (UIServices)provisioningAgent.getService(UIServices.SERVICE_NAME);
      final UIServices newUIServices = new UIServices()
      {
        @Override
        public AuthenticationInfo getUsernamePassword(String location)
        {
          if (oldUIServices != null)
          {
            return oldUIServices.getUsernamePassword(location);
          }

          return null;
        }

        @Override
        public AuthenticationInfo getUsernamePassword(String location, AuthenticationInfo previousInfo)
        {
          if (oldUIServices != null)
          {
            return oldUIServices.getUsernamePassword(location, previousInfo);
          }

          return null;
        }

        @Override
        public TrustInfo getTrustInfo(Certificate[][] untrustedChains, String[] unsignedDetail)
        {
          if (unsignedDetail != null && unsignedDetail.length != 0)
          {
            Confirmation confirmation = unsignedContentConfirmer.confirm(true, unsignedDetail);
            if (!confirmation.isConfirmed())
            {
              return new TrustInfo(new Certificate[0], false, false);
            }

            // We've checked trust already; prevent oldUIServices to check it again.
            unsignedDetail = null;
          }

          if (oldUIServices != null)
          {
            return oldUIServices.getTrustInfo(untrustedChains, unsignedDetail);
          }

          // The rest is copied from org.eclipse.equinox.internal.p2.director.app.DirectorApplication.AvoidTrustPromptService
          final Certificate[] trusted;
          if (untrustedChains == null)
          {
            trusted = null;
          }
          else
          {
            trusted = new Certificate[untrustedChains.length];
            for (int i = 0; i < untrustedChains.length; i++)
            {
              trusted[i] = untrustedChains[i][0];
            }
          }

          return new TrustInfo(trusted, false, true);
        }
      };

      provisioningAgent.registerService(UIServices.SERVICE_NAME, newUIServices);

      cleanup.add(new Runnable()
      {
        public void run()
        {
          provisioningAgent.unregisterService(UIServices.SERVICE_NAME, newUIServices);
          if (oldUIServices != null)
          {
            provisioningAgent.registerService(UIServices.SERVICE_NAME, oldUIServices);
          }
        }
      });
    }
  }

  private URI[] collectRepositories(List<IMetadataRepository> metadataRepositories, Set<URI> artifactURIs, List<Runnable> cleanup, IProgressMonitor monitor)
      throws CoreException
  {
    Agent agent = profile.getAgent();
    final IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
    Set<String> knownRepositories = P2Util.getKnownRepositories(manager);

    EList<Repository> repositories = profileDefinition.getRepositories();
    URI[] metadataURIs = new URI[repositories.size()];

    for (int i = 0; i < metadataURIs.length; i++)
    {
      P2CorePlugin.checkCancelation(monitor);

      try
      {
        Repository repository = repositories.get(i);
        String url = repository.getURL();
        final URI uri = new URI(url);

        if (!knownRepositories.contains(url))
        {
          cleanup.add(new Runnable()
          {
            public void run()
            {
              manager.removeRepository(uri);
            }
          });
        }

        IMetadataRepository metadataRepository = manager.loadRepository(uri, monitor);
        metadataRepositories.add(metadataRepository);

        metadataURIs[i] = uri;
        artifactURIs.add(uri);
      }
      catch (OperationCanceledException ex)
      {
        throw ex;
      }
      catch (CoreException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        throw new P2Exception(ex);
      }
    }

    for (BundlePool bundlePool : agent.getAgentManager().getBundlePools())
    {
      P2CorePlugin.checkCancelation(monitor);
      artifactURIs.add(bundlePool.getLocation().toURI());
    }

    return metadataURIs;
  }

  private IInstallableUnit adjustProfileChangeRequest(final IProfileChangeRequest request, IProgressMonitor monitor) throws CoreException
  {
    InstallableUnitDescription rootDescription = new InstallableUnitDescription();
    rootDescription.setId("artificial_root");
    rootDescription.setVersion(Version.createOSGi(1, 0, 0));
    rootDescription.setArtifacts(new IArtifactKey[0]);
    rootDescription.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, Boolean.TRUE.toString());
    rootDescription.setCapabilities(new IProvidedCapability[] { MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID,
        rootDescription.getId(), rootDescription.getVersion()) });
    List<IRequirement> rootRequirements = new ArrayList<IRequirement>();

    Map<String, IInstallableUnit> rootIUs = new HashMap<String, IInstallableUnit>();
    for (IInstallableUnit rootIU : P2Util.asIterable(profile.query(new UserVisibleRootQuery(), null)))
    {
      if (!removeAll)
      {
        String id = rootIU.getId();
        rootIUs.put(id, rootIU);

        VersionRange versionRange = getCleanVersionRange(rootIU);
        IRequirement rootRequirement = MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, id, versionRange, null, false, false);
        rootRequirements.add(rootRequirement);
      }

      request.remove(rootIU);
    }

    MultiStatus status = new MultiStatus(P2CorePlugin.INSTANCE.getSymbolicName(), 0, "Profile could not be changed", null);

    for (Requirement requirement : profileDefinition.getRequirements())
    {
      P2CorePlugin.checkCancelation(monitor);

      String id = requirement.getName();
      VersionRange versionRange = requirement.getVersionRange();
      boolean optional = requirement.isOptional();

      IRequirement rootRequirement = MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, id, versionRange, null, optional, false);
      rootRequirements.add(rootRequirement);
    }

    rootDescription.setRequirements(rootRequirements.toArray(new IRequirement[rootRequirements.size()]));

    IInstallableUnit rootUnit = MetadataFactory.createInstallableUnit(rootDescription);
    request.add(rootUnit);
    request.setInstallableUnitProfileProperty(rootUnit, Profile.PROP_PROFILE_ROOT_IU, Boolean.TRUE.toString());

    P2CorePlugin.INSTANCE.coreException(status);

    if (isProfileDefinitionChanged())
    {
      request.setProfileProperty(Profile.PROP_PROFILE_DEFINITION, ProfileImpl.definitionToXML(profileDefinition));
    }

    compare(cleanProfileProperties, profileProperties, new CompareHandler<String>()
    {
      public void handleAddition(String key, String value)
      {
        if (!IMMUTABLE_PROPERTIES.contains(key))
        {
          request.setProfileProperty(key, value);
        }
      }

      public void handleRemoval(String key)
      {
        if (!IMMUTABLE_PROPERTIES.contains(key))
        {
          request.removeProfileProperty(key);
        }
      }
    });

    compare(cleanIUProperties, iuProperties, new CompareHandler<IUPropertyKey>()
    {
      public void handleAddition(IUPropertyKey key, String value)
      {
        request.setInstallableUnitProfileProperty(key.getInstallableUnit(), key.getPropertyKey(), value);
      }

      public void handleRemoval(IUPropertyKey key)
      {
        request.removeInstallableUnitProfileProperty(key.getInstallableUnit(), key.getPropertyKey());
      }
    });

    return rootUnit;
  }

  private VersionRange getCleanVersionRange(IInstallableUnit rootIU)
  {
    String id = rootIU.getId();
    Version version = rootIU.getVersion();
    for (Requirement requirement : cleanProfileDefinition.getRequirements())
    {
      if (requirement.getName().equals(id))
      {
        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange == null || versionRange.isIncluded(version))
        {
          return versionRange;
        }
      }
    }

    return new VersionRange(version.toString());
  }

  public static int getOperandCount(IProvisioningPlan provisioningPlan)
  {
    // This is a workaround for http://bugs.eclipse.org/438714
    Method method = ReflectUtil.getMethod(ProvisioningPlan.class, "getOperands");
    Operand[] operands = (Operand[])ReflectUtil.invokeMethod(method, provisioningPlan);
    return operands == null ? 0 : operands.length;
  }

  private static void cleanup(List<Runnable> cleanup)
  {
    for (Runnable runnable : cleanup)
    {
      try
      {
        runnable.run();
      }
      catch (Throwable t)
      {
        P2CorePlugin.INSTANCE.log(t);
      }
    }

    cleanup.clear();
  }

  private static IInstallableUnit generateSourceContainerIU(IProvisioningPlan provisioningPlan, IQueryable<IInstallableUnit> metadata, IProgressMonitor monitor)
  {
    // Create and return an IU that has optional and greedy requirements on all source bundles
    // related to bundle IUs in the profile
    List<IRequirement> requirements = new ArrayList<IRequirement>();

    IQueryResult<IInstallableUnit> ius = provisioningPlan.getFutureState().query(QueryUtil.createIUAnyQuery(), monitor);
    for (IInstallableUnit iu : P2Util.asIterable(ius))
    {
      P2CorePlugin.checkCancelation(monitor);

      // TODO What about source features?
      if (iu.satisfies(BUNDLE_REQUIREMENT))
      {
        String id = iu.getId() + ".source";
        Version version = iu.getVersion();
        VersionRange versionRange = new VersionRange(version, true, version, true);

        IInstallableUnit sourceIU = queryInstallableUnit(metadata, id, versionRange, monitor);
        if (sourceIU != null)
        {
          provisioningPlan.addInstallableUnit(sourceIU);

          IRequirement sourceRequirement = MetadataFactory.createRequirement("osgi.bundle", id, versionRange, null, true, false, true);
          requirements.add(sourceRequirement);
        }
      }
    }

    Version sourceContainerIUVersion = getSourceContainerIUVersion(provisioningPlan.getProfile(), monitor);
    IProvidedCapability capability = MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, SOURCE_IU_ID, sourceContainerIUVersion);

    InstallableUnitDescription sourceIUDescription = new MetadataFactory.InstallableUnitDescription();
    sourceIUDescription.setSingleton(true);
    sourceIUDescription.setId(SOURCE_IU_ID);
    sourceIUDescription.setVersion(sourceContainerIUVersion);
    sourceIUDescription.addRequirements(requirements);
    sourceIUDescription.setCapabilities(new IProvidedCapability[] { capability });

    return MetadataFactory.createInstallableUnit(sourceIUDescription);
  }

  private static Version getSourceContainerIUVersion(IProfile profile, IProgressMonitor monitor)
  {
    IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(SOURCE_IU_ID);
    IQueryResult<IInstallableUnit> result = profile.query(query, monitor);
    if (result.isEmpty())
    {
      return Version.createOSGi(1, 0, 0);
    }

    IInstallableUnit currentSourceIU = result.iterator().next();
    Integer major = (Integer)currentSourceIU.getVersion().getSegment(0);
    return Version.createOSGi(major.intValue() + 1, 0, 0);
  }

  private static IInstallableUnit queryInstallableUnit(IQueryable<IInstallableUnit> metadata, String id, VersionRange versionRange, IProgressMonitor monitor)
  {
    IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(id, versionRange);
    IQuery<IInstallableUnit> latestQuery = QueryUtil.createLatestQuery(iuQuery);

    Iterator<IInstallableUnit> iterator = metadata.query(latestQuery, monitor).iterator();
    if (iterator.hasNext())
    {
      return iterator.next();
    }

    return null;
  }

  private static <T extends EObject> boolean equals(EqualityHelper equalityHelper, Collection<T> c1, Collection<T> c2)
  {
    for (T o : c1)
    {
      if (!contains(equalityHelper, c2, o))
      {
        return false;
      }
    }

    for (T o : c2)
    {
      if (!contains(equalityHelper, c1, o))
      {
        return false;
      }
    }

    return true;
  }

  private static <T extends EObject> boolean contains(EqualityHelper equalityHelper, Collection<T> c, T object)
  {
    for (T o : c)
    {
      if (equalityHelper.equals(object, o))
      {
        return true;
      }
    }

    return false;
  }

  private static <K> void compare(Map<K, String> clean, Map<K, String> dirty, CompareHandler<K> handler)
  {
    for (Map.Entry<K, String> entry : dirty.entrySet())
    {
      K key = entry.getKey();
      String dirtyValue = entry.getValue();
      String cleanValue = clean.get(key);

      if (cleanValue == null || !cleanValue.equals(dirtyValue))
      {
        handler.handleAddition(key, dirtyValue);
      }
    }

    for (Map.Entry<K, String> entry : clean.entrySet())
    {
      K key = entry.getKey();
      if (!dirty.containsKey(key))
      {
        handler.handleRemoval(key);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface CompareHandler<K>
  {
    public void handleAddition(K key, String value);

    public void handleRemoval(K key);
  }

  /**
   * @author Eike Stepper
   */
  private static final class IUPropertyKey
  {
    private static final int PRIME = 31;

    private final IInstallableUnit iu;

    private final String propertyKey;

    private final int hashCode;

    public IUPropertyKey(IInstallableUnit iu, String propertyKey)
    {
      this.iu = iu;
      this.propertyKey = propertyKey;
      hashCode = PRIME * (PRIME + iu.hashCode()) + propertyKey.hashCode();
    }

    public IInstallableUnit getInstallableUnit()
    {
      return iu;
    }

    public String getPropertyKey()
    {
      return propertyKey;
    }

    @Override
    public int hashCode()
    {
      return hashCode;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      IUPropertyKey other = (IUPropertyKey)obj;
      if (!iu.equals(other.iu))
      {
        return false;
      }

      if (!propertyKey.equals(other.propertyKey))
      {
        return false;
      }

      return true;
    }

    @Override
    public String toString()
    {
      return iu.toString() + " / " + propertyKey;
    }
  }
}

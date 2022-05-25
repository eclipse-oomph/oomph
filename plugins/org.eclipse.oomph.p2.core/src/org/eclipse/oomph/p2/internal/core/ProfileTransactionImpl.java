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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext.DeltaType;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext.ResolutionInfo;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager.Artifact.BetterMirrorSelector;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.ReflectUtil.ReflectionException;
import org.eclipse.oomph.util.WorkerPool;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository;
import org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepository;
import org.eclipse.equinox.internal.p2.director.ProfileChangeRequest;
import org.eclipse.equinox.internal.p2.director.SimplePlanner;
import org.eclipse.equinox.internal.p2.engine.CollectEvent;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitOperand;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitPropertyOperand;
import org.eclipse.equinox.internal.p2.engine.Operand;
import org.eclipse.equinox.internal.p2.engine.PropertyOperand;
import org.eclipse.equinox.internal.p2.engine.ProvisioningPlan;
import org.eclipse.equinox.internal.p2.engine.phases.CertificateChecker;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.touchpoint.natives.IBackupStore;
import org.eclipse.equinox.internal.p2.touchpoint.natives.NativeTouchpoint;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.SynchronousProvisioningListener;
import org.eclipse.equinox.internal.provisional.p2.director.PlanExecutionHelper;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
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
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.planner.ProfileInclusionRules;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactDescriptor;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRequest;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.equinox.p2.repository.spi.PGPPublicKeyService;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ProfileTransactionImpl implements ProfileTransaction
{
  public static final String PROP_ADDITIONAL_POOLS = "oomph.p2.additional.pools"; //$NON-NLS-1$

  public static final String ARTIFICIAL_ROOT_ID = "artificial_root"; //$NON-NLS-1$

  public static final String EXCLUDE_IU_PROPERTY = "oomph.exclude"; //$NON-NLS-1$

  public static final String SOURCE_IU_ID = "org.eclipse.oomph.p2.source.container"; //$NON-NLS-1$

  private static final String OSGI_RESOLVER_USES_MODE = "osgi.resolver.usesMode"; //$NON-NLS-1$

  private static final IRequirement BUNDLE_REQUIREMENT = MetadataFactory.createRequirement("org.eclipse.equinox.p2.eclipse.type", "bundle", null, null, false, //$NON-NLS-1$ //$NON-NLS-2$
      false, false);

  private static final Set<String> IMMUTABLE_PROPERTIES = new HashSet<>(Arrays.asList(Profile.PROP_INSTALL_FEATURES, Profile.PROP_INSTALL_FOLDER,
      Profile.PROP_CACHE, Profile.PROP_PROFILE_TYPE, Profile.PROP_PROFILE_DEFINITION));

  private final Profile profile;

  private final ProfileDefinition profileDefinition;

  private final ProfileDefinition cleanProfileDefinition;

  private final Map<String, String> profileProperties = new HashMap<>();

  private final Map<String, String> cleanProfileProperties = new HashMap<>();

  private final Map<IUPropertyKey, String> iuProperties = new HashMap<>();

  private final Map<IUPropertyKey, String> cleanIUProperties = new HashMap<>();

  private boolean removeAll;

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

  @Override
  public Profile getProfile()
  {
    return profile;
  }

  @Override
  public ProfileDefinition getProfileDefinition()
  {
    return profileDefinition;
  }

  @Override
  public String getProfileProperty(String key)
  {
    return profileProperties.get(key);
  }

  @Override
  public ProfileTransaction setProfileProperty(String key, String value)
  {
    if (IMMUTABLE_PROPERTIES.contains(key) && !ObjectUtil.equals(profileProperties.get(key), value))
    {
      throw new IllegalArgumentException(NLS.bind("Property is immutable: {0}", key)); //$NON-NLS-1$
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

  @Override
  public ProfileTransaction removeProfileProperty(String key)
  {
    return setProfileProperty(key, null);
  }

  @Override
  public String getInstallableUnitProperty(IInstallableUnit iu, String key)
  {
    return iuProperties.get(new IUPropertyKey(iu, key));
  }

  @Override
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

  @Override
  public ProfileTransaction removeInstallableUnitProperty(IInstallableUnit iu, String key)
  {
    return setInstallableUnitProperty(iu, key, null);
  }

  @Override
  public boolean isRemoveExistingInstallableUnits()
  {
    return removeAll;
  }

  @Override
  public ProfileTransaction setRemoveExistingInstallableUnits(boolean removeAll)
  {
    this.removeAll = removeAll;
    return this;
  }

  @Override
  public boolean isMirrors()
  {
    return mirrors;
  }

  @Override
  public ProfileTransaction setMirrors(boolean mirrors)
  {
    this.mirrors = mirrors;
    return this;
  }

  @Override
  public boolean isDirty()
  {
    if (removeAll || !profileProperties.equals(cleanProfileProperties) || !iuProperties.equals(cleanIUProperties))
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
    if (!equals(equalityHelper, profileDefinition.getRequirements(), cleanProfileDefinition.getRequirements())
        || !equals(equalityHelper, profileDefinition.getRepositories(), cleanProfileDefinition.getRepositories()))
    {
      return true;
    }

    return false;
  }

  @Override
  public boolean commit() throws CoreException
  {
    return commit(null, null);
  }

  @Override
  public boolean commit(IProgressMonitor monitor) throws CoreException
  {
    return commit(null, monitor);
  }

  @Override
  public boolean commit(CommitContext commitContext, IProgressMonitor monitor) throws CoreException
  {
    if (!committed)
    {
      committed = true;
      monitor.beginTask("", 2); //$NON-NLS-1$

      try
      {
        Resolution resolution = resolve(commitContext, MonitorUtil.create(monitor, 1));
        if (resolution != null)
        {
          return resolution.commit(MonitorUtil.create(monitor, 1));
        }

        monitor.worked(1);
      }
      finally
      {
        monitor.done();
      }
    }

    return false;
  }

  @Override
  public Resolution resolve(IProgressMonitor monitor) throws CoreException
  {
    return resolve(null, monitor);
  }

  @Override
  public Resolution resolve(CommitContext commitContext, IProgressMonitor monitor) throws CoreException
  {
    final CommitContext context = commitContext == null ? new CommitContext() : commitContext;
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    final Agent agent = profile.getAgent();
    final List<Runnable> cleanup = new ArrayList<>();

    boolean includeSourceBundles = profileDefinition.isIncludeSourceBundles();
    monitor.beginTask("", includeSourceBundles ? 75 : 70); //$NON-NLS-1$

    try
    {
      initMirrors(cleanup);

      final List<IMetadataRepository> metadataRepositories = new ArrayList<>();
      Set<URI> artifactURIs = new HashSet<>();
      URI[] metadataURIs = collectRepositories(metadataRepositories, artifactURIs, cleanup, MonitorUtil.create(monitor, 50));

      final ProfileImpl profileImpl = (ProfileImpl)profile;
      final IProfile delegate = profileImpl.getDelegate();
      final long timestamp = delegate.getTimestamp();

      IPlanner planner = agent.getPlanner();

      // In org.eclipse.equinox.internal.p2.director.SimplePlanner.getSolutionFor(ProfileChangeRequest, ProvisioningContext, IProgressMonitor)
      // The removals will be add the removed IUs to the slicer,
      // but we don'ts want to do that for targlets.
      boolean isTarglet = Profile.TYPE_TARGLET.equals(profile.getType());
      IProfileChangeRequest profileChangeRequest = isTarglet ? new ProfileChangeRequest(delegate)
      {
        @Override
        public Collection<IInstallableUnit> getRemovals()
        {
          // This inspects the stack to see if this method is being called by getSolutionFor,
          // In which case we want to return nothing so these removals aren't considered by the slicer.
          StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
          if ("getSolutionFor".equals(stackTrace[2].getMethodName())) //$NON-NLS-1$
          {
            return Collections.emptyList();
          }

          return super.getRemovals();
        }
      } : planner.createChangeRequest(delegate);

      final IInstallableUnit rootIU = adjustProfileChangeRequest(profileChangeRequest, MonitorUtil.create(monitor, 5));

      final ProvisioningContext provisioningContext = context.createProvisioningContext(this, profileChangeRequest);
      provisioningContext.setMetadataRepositories(metadataURIs);
      provisioningContext.setArtifactRepositories(artifactURIs.toArray(new URI[artifactURIs.size()]));

      // In org.eclipse.equinox.internal.p2.director.SimplePlanner.getSolutionFor(ProfileChangeRequest, ProvisioningContext, IProgressMonitor)
      // it will add all the profile's IUs to the slicer.
      // We don't want to do that for targlets, because we want to compute a new target platform without consider what existed before.
      if (isTarglet)
      {
        provisioningContext.setProperty("org.eclipse.equinox.p2.internal.profileius", Boolean.FALSE.toString()); //$NON-NLS-1$
      }

      IQueryable<IInstallableUnit> metadata = provisioningContext.getMetadata(MonitorUtil.create(monitor, 5));

      final IProvisioningPlan provisioningPlan = planner.getProvisioningPlan(profileChangeRequest, provisioningContext, MonitorUtil.create(monitor, 10));
      P2CorePlugin.INSTANCE.coreException(provisioningPlan.getStatus());

      IQueryable<IInstallableUnit> futureState = provisioningPlan.getFutureState();
      for (IRequirement requirement : rootIU.getRequirements())
      {
        if (P2Util.isSimpleRequiredCapability(requirement))
        {
          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
          for (IInstallableUnit installableUnit : P2Util
              .asIterable(futureState.query(QueryUtil.createIUQuery(requiredCapability.getName(), requiredCapability.getRange()), null)))
          {
            provisioningPlan.setInstallableUnitProfileProperty(installableUnit, Profile.PROP_PROFILE_ROOT_IU, Boolean.TRUE.toString());
            provisioningPlan.setInstallableUnitProfileProperty(installableUnit, SimplePlanner.INCLUSION_RULES,
                ProfileInclusionRules.createStrictInclusionRule(installableUnit));
          }
        }
      }

      if (includeSourceBundles)
      {
        IInstallableUnit sourceContainerIU = generateSourceContainerIU(provisioningPlan, metadata, MonitorUtil.create(monitor, 5));
        provisioningPlan.addInstallableUnit(sourceContainerIU);
        provisioningPlan.setInstallableUnitProfileProperty(sourceContainerIU, Profile.PROP_PROFILE_ROOT_IU, Boolean.TRUE.toString());
      }

      final Map<IInstallableUnit, CommitContext.DeltaType> iuDeltas = new HashMap<>();
      final Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas = new HashMap<>();
      computeOperandDeltas(provisioningPlan, iuDeltas, propertyDeltas);

      ResolutionInfo resolutionInfo = new ResolutionInfo()
      {
        @Override
        public IProvisioningPlan getProvisioningPlan()
        {
          return provisioningPlan;
        }

        @Override
        public IInstallableUnit getArtificialRoot()
        {
          return rootIU;
        }

        @Override
        public Map<IInstallableUnit, DeltaType> getIUDeltas()
        {
          return iuDeltas;
        }

        @Override
        public Map<IInstallableUnit, Map<String, Pair<Object, Object>>> getPropertyDeltas()
        {
          return propertyDeltas;
        }

        @Override
        public List<IMetadataRepository> getMetadataRepositories()
        {
          return metadataRepositories;
        }
      };

      if (!context.handleProvisioningPlan(resolutionInfo) || iuDeltas.isEmpty() && propertyDeltas.isEmpty())
      {
        return null;
      }

      return new Resolution()
      {
        private final ProvisioningListener provisioningListener = new ProvisioningListener();

        @Override
        public ProfileTransaction getProfileTransaction()
        {
          return ProfileTransactionImpl.this;
        }

        @Override
        public IProvisioningPlan getProvisioningPlan()
        {
          return provisioningPlan;
        }

        @Override
        public boolean commit(IProgressMonitor monitor) throws CoreException
        {
          final String oldUsesMode = System.setProperty(OSGI_RESOLVER_USES_MODE, "ignore"); //$NON-NLS-1$
          cleanup.add(new Runnable()
          {
            @Override
            public void run()
            {
              if (oldUsesMode == null)
              {
                System.clearProperty(OSGI_RESOLVER_USES_MODE);
              }
              else
              {
                System.setProperty(OSGI_RESOLVER_USES_MODE, oldUsesMode);
              }
            }
          });

          // We won't register our listener if better mirror selection is not enabled.
          IProvisioningEventBus eventBus = CachingRepositoryManager.isBetterMirrorSelection()
              ? (IProvisioningEventBus)agent.getProvisioningAgent().getService(IProvisioningEventBus.SERVICE_NAME)
              : null;

          try
          {
            if (eventBus != null)
            {
              provisioningListener.setMonitor(MonitorUtil.create(monitor, 1));
              eventBus.addListener(provisioningListener);
            }

            IPhaseSet phaseSet = context.getPhaseSet(ProfileTransactionImpl.this);

            init(context, agent, cleanup);

            IEngine engine = agent.getEngine();
            ensureSameBackupDevice(provisioningPlan);

            IStatus status = PlanExecutionHelper.executePlan(provisioningPlan, engine, phaseSet, provisioningContext,
                new ExecutePlanMonitor(monitor, provisioningPlan));

            context.handleExecutionResult(status);
            P2CorePlugin.INSTANCE.coreException(status);

            profileImpl.setDefinition(profileDefinition);
            return delegate.getTimestamp() != timestamp;
          }
          finally
          {
            if (eventBus != null)
            {
              eventBus.removeListener(provisioningListener);
            }

            cleanup(cleanup);
          }
        }

        @Override
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
    finally
    {
      monitor.done();
    }
  }

  private void computeOperandDeltas(final IProvisioningPlan provisioningPlan, Map<IInstallableUnit, CommitContext.DeltaType> iuDeltas,
      Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas)
  {
    // Undo (remove) the addition of our artificial root IU and compute the effective deltas (to remove redundancies in the operands).
    Field operandsField = ReflectUtil.getField(ProvisioningPlan.class, "operands"); //$NON-NLS-1$
    @SuppressWarnings("unchecked")
    List<Operand> operands = (List<Operand>)ReflectUtil.getValue(operandsField, provisioningPlan);
    for (Iterator<Operand> it = operands.iterator(); it.hasNext();)
    {
      Operand operand = it.next();
      if (operand instanceof InstallableUnitOperand)
      {
        InstallableUnitOperand iuOperand = (InstallableUnitOperand)operand;
        IInstallableUnit first = iuOperand.first();
        IInstallableUnit second = iuOperand.second();
        if (first == null)
        {
          if (second.getId().equals(ARTIFICIAL_ROOT_ID) || "true".equals(second.getProperty(EXCLUDE_IU_PROPERTY))) //$NON-NLS-1$
          {
            it.remove();
          }
          else
          {
            iuDeltas.put(second, CommitContext.DeltaType.ADDITION);
          }
        }
        else if (!first.equals(second))
        {
          iuDeltas.put(first, CommitContext.DeltaType.REMOVAL);
          if (second != null)
          {
            iuDeltas.put(second, CommitContext.DeltaType.ADDITION);
          }
        }
      }
      else if (operand instanceof InstallableUnitPropertyOperand)
      {
        InstallableUnitPropertyOperand iuPropertyOperand = (InstallableUnitPropertyOperand)operand;
        IInstallableUnit operandIU = iuPropertyOperand.getInstallableUnit();
        if (operandIU.getId().equals(ARTIFICIAL_ROOT_ID))
        {
          it.remove();
        }
        else
        {
          Object first = iuPropertyOperand.first();
          Object second = iuPropertyOperand.second();
          String key = iuPropertyOperand.getKey();
          populatePropertyDeltas(operandIU, first, second, key, propertyDeltas);
        }
      }
      else if (operand instanceof PropertyOperand)
      {
        PropertyOperand propertyOperand = (PropertyOperand)operand;
        Object first = propertyOperand.first();
        Object second = propertyOperand.second();
        String key = propertyOperand.getKey();
        populatePropertyDeltas(null, first, second, key, propertyDeltas);
      }
    }

    for (Iterator<Map.Entry<IInstallableUnit, Map<String, Pair<Object, Object>>>> it = propertyDeltas.entrySet().iterator(); it.hasNext();)
    {
      Map.Entry<IInstallableUnit, Map<String, Pair<Object, Object>>> entry = it.next();
      Set<Map.Entry<String, Pair<Object, Object>>> properties = entry.getValue().entrySet();
      for (Iterator<Map.Entry<String, Pair<Object, Object>>> it2 = properties.iterator(); it2.hasNext();)
      {
        Map.Entry<String, Pair<Object, Object>> property = it2.next();
        Pair<Object, Object> pair = property.getValue();
        if (ObjectUtil.equals(pair.getElement1(), pair.getElement2()))
        {
          it2.remove();
        }
      }

      if (properties.isEmpty())
      {
        it.remove();
      }
    }
  }

  private void populatePropertyDeltas(IInstallableUnit operandIU, Object first, Object second, String key,
      Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas)
  {
    Map<String, Pair<Object, Object>> propertyDelta = propertyDeltas.get(operandIU);
    if (propertyDelta == null)
    {
      propertyDelta = new HashMap<>();
      propertyDelta.put(key, new Pair<>(first, second));
      propertyDeltas.put(operandIU, propertyDelta);
    }
    else
    {
      Pair<Object, Object> pair = propertyDelta.get(key);
      if (pair == null)
      {
        propertyDelta.put(key, new Pair<>(first, second));
      }
      else
      {
        pair.setElement2(second);
      }
    }
  }

  private void initMirrors(final List<Runnable> cleanup)
  {
    final boolean wasMirrors = SimpleArtifactRepository.MIRRORS_ENABLED;
    if (mirrors != wasMirrors)
    {
      try
      {
        final Field mirrorsEnabledField = ReflectUtil.getField(SimpleArtifactRepository.class, "MIRRORS_ENABLED"); //$NON-NLS-1$
        ReflectUtil.setValue(mirrorsEnabledField, null, mirrors, true);

        cleanup.add(new Runnable()
        {
          @Override
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

  private void init(final CommitContext context, final Agent agent, final List<Runnable> cleanup)
  {
    context.init();

    // Check if the provisioning agent is the same one as the current (self) agent.
    final IProvisioningAgent provisioningAgent = agent.getProvisioningAgent();
    IProvisioningAgent selfAgent = P2Util.getCurrentProvisioningAgent();
    if (selfAgent != provisioningAgent)
    {
      // If not, then make sure the self agent uses the agent's key service because the latter is where new keys will be added and the former is where the UI
      // services will look them up.
      PGPPublicKeyService provisioningAgentKeyService = provisioningAgent.getService(PGPPublicKeyService.class);
      PGPPublicKeyService selfAgentKeyService = selfAgent.getService(PGPPublicKeyService.class);
      if (provisioningAgentKeyService != null)
      {
        selfAgent.registerService(PGPPublicKeyService.SERVICE_NAME, provisioningAgentKeyService);

        // Because we might have added some keys in the installer initialization, ensure that those are loaded into the key service.
        CertificateChecker certificateChecker = new CertificateChecker(provisioningAgent);
        certificateChecker.setProfile(profile);
        certificateChecker.getPreferenceTrustedKeys();

        cleanup.add(() -> {
          // Restore the key service to how it was.
          if (selfAgentKeyService == null)
          {
            selfAgent.unregisterService(PGPPublicKeyService.SERVICE_NAME, provisioningAgentKeyService);
          }
          else
          {
            selfAgent.registerService(PGPPublicKeyService.SERVICE_NAME, selfAgentKeyService);
          }
        });
      }
    }
  }

  private URI[] collectRepositories(List<IMetadataRepository> metadataRepositories, Set<URI> artifactURIs, List<Runnable> cleanup, IProgressMonitor monitor)
      throws CoreException
  {
    Agent agent = profile.getAgent();

    boolean removeRepositories = !"false".equals(PropertiesUtil.getProperty(AgentManager.PROP_FLUSH)); //$NON-NLS-1$
    if (removeRepositories)
    {
      agent.flushCachedRepositories();
    }

    final IMetadataRepositoryManager metadataRepositoryManager = agent.getMetadataRepositoryManager();
    Set<String> knownRepositories = P2Util.getKnownRepositories(metadataRepositoryManager);

    EList<Repository> repositories = profileDefinition.getRepositories();
    URI[] metadataURIs = new URI[repositories.size()];
    Set<URI> addedArtifactURIs = new HashSet<>();

    for (int i = 0; i < metadataURIs.length; i++)
    {
      try
      {
        Repository repository = repositories.get(i);
        String url = repository.getURL();
        final URI uri = new URI(url);

        if (removeRepositories && !knownRepositories.contains(url))
        {
          cleanup.add(new Runnable()
          {
            @Override
            public void run()
            {
              metadataRepositoryManager.removeRepository(uri);
            }
          });
        }

        metadataURIs[i] = uri;

        if (addedArtifactURIs.add(uri))
        {
          artifactURIs.add(uri);
        }
      }
      catch (Exception ex)
      {
        throw new P2Exception(ex);
      }
    }

    RepositoryLoader repositoryLoader = new RepositoryLoader(metadataRepositoryManager, metadataURIs);
    repositoryLoader.begin(monitor);
    ProvisionException exception = repositoryLoader.getException();
    if (exception != null)
    {
      throw exception;
    }

    if (repositoryLoader.isCanceled())
    {
      throw new OperationCanceledException();
    }

    for (int i = 0; i < metadataURIs.length; i++)
    {
      IMetadataRepository metadataRepository = metadataRepositoryManager.loadRepository(metadataURIs[i], null);
      metadataRepositories.add(metadataRepository);
    }

    if (!"false".equalsIgnoreCase(PropertiesUtil.getProperty(PROP_ADDITIONAL_POOLS))) //$NON-NLS-1$
    {
      for (BundlePool bundlePool : agent.getAgentManager().getBundlePools())
      {
        P2CorePlugin.checkCancelation(monitor);

        if (bundlePool != profile.getBundlePool())
        {
          URI uri = bundlePool.getLocation().toURI();
          if (addedArtifactURIs.add(uri))
          {
            artifactURIs.add(uri);
          }
        }
      }
    }

    return metadataURIs;
  }

  private IInstallableUnit adjustProfileChangeRequest(final IProfileChangeRequest request, IProgressMonitor monitor) throws CoreException
  {
    InstallableUnitDescription rootDescription = new InstallableUnitDescription();
    rootDescription.setId(ARTIFICIAL_ROOT_ID);
    rootDescription.setVersion(Version.createOSGi(1, 0, 0, "v" + System.currentTimeMillis())); //$NON-NLS-1$
    rootDescription.setSingleton(true);
    rootDescription.setArtifacts(new IArtifactKey[0]);
    rootDescription.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, Boolean.TRUE.toString());
    rootDescription.setCapabilities(new IProvidedCapability[] {
        MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, rootDescription.getId(), rootDescription.getVersion()) });
    List<IRequirement> rootRequirements = new ArrayList<>();

    Map<String, IInstallableUnit> rootIUs = new HashMap<>();
    Map<String, IRequirement> singletonRootRequirements = new HashMap<>();
    for (IInstallableUnit rootIU : P2Util.asIterable(profile.query(new UserVisibleRootQuery(), null)))
    {
      if (!removeAll)
      {
        String id = rootIU.getId();
        rootIUs.put(id, rootIU);

        VersionRange versionRange = getCleanVersionRange(rootIU);
        IMatchExpression<IInstallableUnit> filter = rootIU.getFilter();

        IRequirement rootRequirement = MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, id, versionRange, filter, false, false);
        rootRequirements.add(rootRequirement);
        if (rootIU.isSingleton() || "true".equals(rootIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP))) //$NON-NLS-1$
        {
          singletonRootRequirements.put(id, rootRequirement);
        }
      }

      request.remove(rootIU);
    }

    MultiStatus status = new MultiStatus(P2CorePlugin.INSTANCE.getSymbolicName(), 0, Messages.ProfileTransactionImpl_CouldNotBeChanged_message, null);

    for (Requirement requirement : profileDefinition.getRequirements())
    {
      P2CorePlugin.checkCancelation(monitor);

      IRequirement rootRequirement = requirement.toIRequirement();
      rootRequirements.add(rootRequirement);

      // If this is a requirement for an IU...
      String namespace = requirement.getNamespace();
      if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
      {
        // If the clean profile has a requirement for this same singleton IU.
        String name = requirement.getName();
        IRequirement singletonRootRequirement = singletonRootRequirements.get(name);
        if (singletonRootRequirement != null)
        {
          // Remove that other requirement because the new requirement we're trying to satisfy might well be in conflict with the version range of that
          // previously resolved requirement.
          // Even if it's not in conflict, this new requirement's version range is what should be satisfied, regardless of what the old requirement might be.
          rootRequirements.remove(singletonRootRequirement);
        }
      }
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

    Map<String, String> mergedProfileProperties = new LinkedHashMap<>(profileProperties);
    mergedProfileProperties.putAll(P2Util.toProfilePropertiesMap(profileDefinition.getProfileProperties()));
    compare(cleanProfileProperties, mergedProfileProperties, new CompareHandler<String>()
    {
      @Override
      public void handleAddition(String key, String value)
      {
        if (!IMMUTABLE_PROPERTIES.contains(key))
        {
          request.setProfileProperty(key, value);
        }
      }

      @Override
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
      @Override
      public void handleAddition(IUPropertyKey key, String value)
      {
        request.setInstallableUnitProfileProperty(key.getInstallableUnit(), key.getPropertyKey(), value);
      }

      @Override
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

  private void ensureSameBackupDevice(final IProvisioningPlan provisioningPlan) throws CoreException
  {
    // This is to handle the special case in Windows where the backup store tries to move the *.exe to a different device.
    // This fails when the *.exe is currently in use.
    // The strategy then copies the file instead and then to delete it, but that always fails.
    // If the backup store is on the same device, the move is successful and the *.exe can be successfully updated.
    // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=427148 for more details.
    if (profile.isCurrent() && Platform.OS_WIN32.equals(Platform.getOS()))
    {
      try
      {
        final Constructor<?> backStoreConstructor = ReflectUtil.getConstructor(
            CommonPlugin.loadClass("org.eclipse.equinox.p2.touchpoint.natives", "org.eclipse.equinox.internal.p2.touchpoint.natives.BackupStore"), File.class, //$NON-NLS-1$ //$NON-NLS-2$
            String.class);
        Location location = Platform.getInstallLocation();
        org.eclipse.emf.common.util.URI installationLocation = org.eclipse.emf.common.util.URI.createURI(FileLocator.resolve(location.getURL()).toString());
        org.eclipse.emf.common.util.URI tempDir = org.eclipse.emf.common.util.URI.createFileURI(PropertiesUtil.getProperty("java.io.tmpdir")); //$NON-NLS-1$
        if (!ObjectUtil.equals(installationLocation.device(), tempDir.device()))
        {
          Field field = ReflectUtil.getField(NativeTouchpoint.class, "backups"); //$NON-NLS-1$
          @SuppressWarnings("unchecked")
          Map<IProfile, IBackupStore> backups = (Map<IProfile, IBackupStore>)ReflectUtil.getValue(field, null);
          final File localTempFolder = new File(installationLocation.toFileString(), "backup"); //$NON-NLS-1$
          final IProfile planProfile = provisioningPlan.getProfile();
          backups.put(planProfile, new IBackupStore()
          {
            private IBackupStore delegate;

            @Override
            public boolean backup(File file) throws IOException
            {
              loadDelegate();
              return delegate.backup(file);
            }

            @Override
            public boolean backupDirectory(File file) throws IOException
            {
              loadDelegate();
              return delegate.backupDirectory(file);
            }

            @Override
            public void discard()
            {
              if (delegate == null)
              {
                return;
              }
              delegate.discard();
            }

            @Override
            public void restore() throws IOException
            {
              if (delegate == null)
              {
                return;
              }
              delegate.restore();
            }

            private void loadDelegate()
            {
              if (delegate != null)
              {
                return;
              }
              delegate = (IBackupStore)ReflectUtil.newInstance(backStoreConstructor, localTempFolder, NativeTouchpoint.escape(planProfile.getProfileId()));
            }

            @Override
            public String getBackupName()
            {
              loadDelegate();
              return delegate.getBackupName();
            }

            @Override
            public boolean backupCopy(File file) throws IOException
            {
              loadDelegate();
              return delegate.backupCopy(file);
            }

            @Override
            public void backupCopyAll(File file) throws IOException
            {
              loadDelegate();
              delegate.backupCopyAll(file);
            }

            @Override
            public void backupAll(File file) throws IOException
            {
              loadDelegate();
              delegate.backupAll(file);
            }
          });
        }
      }
      catch (IOException ex)
      {
        P2CorePlugin.INSTANCE.coreException(ex);
      }
      catch (ReflectionException ex)
      {
        //$FALL-THROUGH$
      }
      catch (ClassNotFoundException ex)
      {
        //$FALL-THROUGH$
      }
    }
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
    List<IRequirement> requirements = new ArrayList<>();

    IQueryResult<IInstallableUnit> ius = provisioningPlan.getFutureState().query(QueryUtil.createIUAnyQuery(), monitor);
    for (IInstallableUnit iu : P2Util.asIterable(ius))
    {
      P2CorePlugin.checkCancelation(monitor);

      // TODO What about source features?
      if (iu.satisfies(BUNDLE_REQUIREMENT))
      {
        String id = iu.getId() + ".source"; //$NON-NLS-1$
        Version version = iu.getVersion();
        VersionRange versionRange = new VersionRange(version, true, version, true);

        IInstallableUnit sourceIU = queryInstallableUnit(metadata, id, versionRange, monitor);
        if (sourceIU != null)
        {
          provisioningPlan.addInstallableUnit(sourceIU);

          IRequirement sourceRequirement = MetadataFactory.createRequirement("osgi.bundle", id, versionRange, null, true, false, true); //$NON-NLS-1$
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
  private static final class ProvisioningListener implements SynchronousProvisioningListener
  {
    private long startTime;

    private IProgressMonitor monitor;

    public void setMonitor(IProgressMonitor monitor)
    {
      this.monitor = monitor;
    }

    @Override
    public void notify(EventObject event)
    {
      if (event instanceof CollectEvent)
      {
        CollectEvent collectEvent = (CollectEvent)event;
        final IArtifactRepository repository = collectEvent.getRepository();
        // PGPSignatureVerifier.KNOWN_KEYS.add(new File("D:/Users/merks/pgp-keys/platform-rootkey.asc"));
        // PGPSignatureVerifier.KNOWN_KEYS.add(new File("D:/Users/merks/pgp-keys/platform-subkey.asc"));
        // if (repository != null)
        // {
        // System.err.println("###" + repository.getLocation());
        // Set<PGPPublicKey> repositoryPGPKeys = getRepositoryPGPKeys(repository);
        // for (PGPPublicKey pgpPublicKey : repositoryPGPKeys)
        // {
        // PGPSignatureVerifier.KNOWN_KEYS.addKey(pgpPublicKey);
        // }
        // System.err.println("###" + repositoryPGPKeys);
        // }

        IArtifactRequest[] requests = collectEvent.getDownloadRequests();
        if (requests != null && requests.length > 0)
        {
          if (collectEvent.getType() == CollectEvent.TYPE_REPOSITORY_START)
          {
            startTime = System.currentTimeMillis();
            if (repository != null)
            {
              monitor.subTask(NLS.bind(Messages.ProfileTransactionImpl_CollectingArtifacts_task, requests.length, repository.getLocation()));
              if (!repository.isModifiable())
              {
                // We want to have the smallest artifacts at the top of the requests array,
                // so that phase 1 probing on bad mirrors can't block for too long.
                Arrays.sort(requests, new Comparator<IArtifactRequest>()
                {
                  @Override
                  public int compare(IArtifactRequest o1, IArtifactRequest o2)
                  {
                    int size1 = getDownloadSize(o1);
                    int size2 = getDownloadSize(o2);
                    return size1 - size2;
                  }

                  private int getDownloadSize(IArtifactRequest request)
                  {
                    IArtifactKey artifactKey = request.getArtifactKey();
                    IArtifactDescriptor[] artifactDescriptors = repository.getArtifactDescriptors(artifactKey);
                    if (artifactDescriptors != null && artifactDescriptors.length != 0)
                    {
                      IArtifactDescriptor artifactDescriptor = artifactDescriptors[0];
                      String property = artifactDescriptor.getProperty(IArtifactDescriptor.DOWNLOAD_SIZE);
                      if (property != null)
                      {
                        try
                        {
                          long size = Long.parseLong(property);
                          if (size > Integer.MAX_VALUE)
                          {
                            return Integer.MAX_VALUE;
                          }

                          return (int)size;
                        }
                        catch (NumberFormatException ex)
                        {
                          //$FALL-THROUGH$
                        }
                      }
                    }

                    return Integer.MAX_VALUE;
                  }
                });

                int length = requests.length;
                if (length >= 4)
                {
                  // We don't want to leave the biggest artifacts at the end because then fewer threads than the maximum could be busy
                  // for a longer time. While, when downloading the big artifacts earlier, the full thread pool could be utilized.
                  // So we reverse the order of the second half of the request array. That still leaves the smallest ones at the top,
                  // so that phase 1 probing on bad mirrors can't block for too long.
                  List<IArtifactRequest> list = Arrays.asList(requests);
                  List<IArtifactRequest> subList = list.subList(length / 2, length);
                  Collections.reverse(subList);
                }
              }
            }
          }
          else if (collectEvent.getType() == CollectEvent.TYPE_REPOSITORY_END)
          {
            if (repository != null)
            {
              processStats(repository);

              NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
              monitor.subTask(NLS.bind(Messages.ProfileTransactionImpl_CollectingArtifactsFor_task,
                  new Object[] { requests.length, repository.getLocation(), numberFormat.format((System.currentTimeMillis() - startTime) / 1000.0) }));
            }
          }
        }
      }
    }

    private void processStats(IArtifactRepository repository)
    {
      if (repository instanceof SimpleArtifactRepository)
      {
        Object value = ReflectUtil.getValue("mirrors", repository); //$NON-NLS-1$
        if (value instanceof BetterMirrorSelector)
        {
          BetterMirrorSelector mirrorSelector = (BetterMirrorSelector)value;
          for (String stats : mirrorSelector.getStats())
          {
            monitor.subTask(stats);
          }
        }
      }
      else if (repository instanceof CompositeArtifactRepository)
      {
        CompositeArtifactRepository compositeArtifactRepository = (CompositeArtifactRepository)repository;
        for (IArtifactRepository childRepository : compositeArtifactRepository.getLoadedChildren())
        {
          processStats(childRepository);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ExecutePlanMonitor extends ProgressMonitorWrapper
  {
    private static final String INSTALLING_PREFIX = Messages.ProfileTransactionImpl_Installing_task + " "; //$NON-NLS-1$

    private final Map<String, LinkedList<Version>> versions = new HashMap<>();

    private final IQueryable<IInstallableUnit> additions;

    private ExecutePlanMonitor(IProgressMonitor monitor, IProvisioningPlan provisioningPlan)
    {
      super(monitor);
      additions = provisioningPlan.getAdditions();
    }

    @Override
    public void subTask(String name)
    {
      if (name.startsWith(INSTALLING_PREFIX))
      {
        String id = name.substring(INSTALLING_PREFIX.length());

        Version version = getVersion(id);
        if (version != null)
        {
          name += " [" + version + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
      }

      super.subTask(name);
    }

    private Version getVersion(String id)
    {
      LinkedList<Version> list = versions.get(id);
      if (list != null)
      {
        if (list.isEmpty())
        {
          return null;
        }

        return list.remove(0);
      }

      Version firstVersion = null;

      IQueryResult<IInstallableUnit> ius = additions.query(QueryUtil.createIUQuery(id), null);
      if (!ius.isEmpty())
      {
        for (IInstallableUnit iu : P2Util.asIterable(ius))
        {
          if (firstVersion == null)
          {
            firstVersion = iu.getVersion();
          }
          else
          {
            if (list == null)
            {
              list = new LinkedList<>();
              versions.put(id, list);
            }

            list.add(iu.getVersion());
          }
        }
      }

      return firstVersion;
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
      if (!iu.equals(other.iu) || !propertyKey.equals(other.propertyKey))
      {
        return false;
      }

      return true;
    }

    @Override
    public String toString()
    {
      return iu.toString() + " / " + propertyKey; //$NON-NLS-1$
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class RepositoryLoader extends WorkerPool<RepositoryLoader, URI, RepositoryLoader.Worker>
  {
    private final IMetadataRepositoryManager manager;

    private final URI[] uris;

    private final List<IMetadataRepository> metadataRepositories = Collections.synchronizedList(new ArrayList<IMetadataRepository>());

    private ProvisionException exception;

    public RepositoryLoader(IMetadataRepositoryManager manager, URI... uris)
    {
      this.manager = manager;
      this.uris = uris;
    }

    public void begin(IProgressMonitor monitor)
    {
      try
      {
        monitor.beginTask("", uris.length + 1); //$NON-NLS-1$
        monitor.worked(1);
        begin(Messages.ProfileTransactionImpl_LoadRepositories_task, monitor);
      }
      finally
      {
        monitor.done();
      }
    }

    public ProvisionException getException()
    {
      return exception;
    }

    @Override
    protected void run(String taskName, IProgressMonitor monitor)
    {
      perform(uris);
    }

    @Override
    protected Worker createWorker(URI key, int workerID, boolean secondary)
    {
      return new Worker(NLS.bind(Messages.ProfileTransactionImpl_RepositoryLoader_thread, key), this, key, workerID, secondary);
    }

    /**
     * @author Ed Merks
     */
    private static class Worker extends WorkerPool.Worker<URI, RepositoryLoader>
    {
      public Worker(String name, RepositoryLoader workPool, URI key, int id, boolean secondary)
      {
        super(name, workPool, key, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        RepositoryLoader workPool = getWorkPool();
        if (workPool.isCanceled())
        {
          return Status.CANCEL_STATUS;
        }

        try
        {
          IMetadataRepository metadataRepository = workPool.manager.loadRepository(getKey(), MonitorUtil.create(monitor, 1));
          workPool.metadataRepositories.add(metadataRepository);
          return Status.OK_STATUS;
        }
        catch (ProvisionException ex)
        {
          workPool.cancel();
          workPool.exception = ex;
          return P2CorePlugin.INSTANCE.getStatus(ex);
        }
        catch (OperationCanceledException ex)
        {
          return Status.CANCEL_STATUS;
        }
      }
    }
  }
}

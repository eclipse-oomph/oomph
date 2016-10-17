/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.internal.p2.SetupP2Plugin;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Package;
import org.eclipse.oomph.setup.p2.util.P2TaskUISevices;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Confirmer.Confirmation;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.ProvisioningListener;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Install Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isMergeDisabled <em>Merge Disabled</em>}</li>
 * </ul>
 *
 * @generated
 */
public class P2TaskImpl extends SetupTaskImpl implements P2Task
{
  private static final boolean SKIP = PropertiesUtil.isProperty(PROP_SKIP);

  // This is used only for documentation capture.
  private static final boolean FORCE = PropertiesUtil.isProperty("oomph.setup.p2.force");

  private static final Object FIRST_CALL_DETECTION_KEY = new Object();

  /**
   * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected static final String LABEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected String label = LABEL_EDEFAULT;

  /**
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositories()
   * @generated
   * @ordered
   */
  protected EList<Repository> repositories;

  /**
   * The default value of the '{@link #isLicenseConfirmationDisabled() <em>License Confirmation Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLicenseConfirmationDisabled()
   * @generated
   * @ordered
   */
  protected static final boolean LICENSE_CONFIRMATION_DISABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isLicenseConfirmationDisabled() <em>License Confirmation Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLicenseConfirmationDisabled()
   * @generated
   * @ordered
   */
  protected boolean licenseConfirmationDisabled = LICENSE_CONFIRMATION_DISABLED_EDEFAULT;

  /**
   * The default value of the '{@link #isMergeDisabled() <em>Merge Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMergeDisabled()
   * @generated
   * @ordered
   */
  protected static final boolean MERGE_DISABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isMergeDisabled() <em>Merge Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMergeDisabled()
   * @generated
   * @ordered
   */
  protected boolean mergeDisabled = MERGE_DISABLED_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected P2TaskImpl()
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
    return SetupP2Package.Literals.P2_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLabel(String newLabel)
  {
    String oldLabel = label;
    label = newLabel;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__LABEL, oldLabel, label));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, SetupP2Package.P2_TASK__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Repository> getRepositories()
  {
    if (repositories == null)
    {
      repositories = new EObjectContainmentEList<Repository>(Repository.class, this, SetupP2Package.P2_TASK__REPOSITORIES);
    }
    return repositories;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isLicenseConfirmationDisabled()
  {
    return licenseConfirmationDisabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLicenseConfirmationDisabled(boolean newLicenseConfirmationDisabled)
  {
    boolean oldLicenseConfirmationDisabled = licenseConfirmationDisabled;
    licenseConfirmationDisabled = newLicenseConfirmationDisabled;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED, oldLicenseConfirmationDisabled,
          licenseConfirmationDisabled));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isMergeDisabled()
  {
    return mergeDisabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMergeDisabled(boolean newMergeDisabled)
  {
    boolean oldMergeDisabled = mergeDisabled;
    mergeDisabled = newMergeDisabled;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__MERGE_DISABLED, oldMergeDisabled, mergeDisabled));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return ((InternalEList<?>)getRepositories()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        return getLabel();
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return getRequirements();
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return getRepositories();
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        return isLicenseConfirmationDisabled();
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        return isMergeDisabled();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        setLabel((String)newValue);
        return;
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case SetupP2Package.P2_TASK__REPOSITORIES:
        getRepositories().clear();
        getRepositories().addAll((Collection<? extends Repository>)newValue);
        return;
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        setLicenseConfirmationDisabled((Boolean)newValue);
        return;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        setMergeDisabled((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        setLabel(LABEL_EDEFAULT);
        return;
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        getRequirements().clear();
        return;
      case SetupP2Package.P2_TASK__REPOSITORIES:
        getRepositories().clear();
        return;
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        setLicenseConfirmationDisabled(LICENSE_CONFIRMATION_DISABLED_EDEFAULT);
        return;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        setMergeDisabled(MERGE_DISABLED_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return repositories != null && !repositories.isEmpty();
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        return licenseConfirmationDisabled != LICENSE_CONFIRMATION_DISABLED_EDEFAULT;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        return mergeDisabled != MERGE_DISABLED_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (label: ");
    result.append(label);
    result.append(", licenseConfirmationDisabled: ");
    result.append(licenseConfirmationDisabled);
    result.append(", mergeDisabled: ");
    result.append(mergeDisabled);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_INSTALLATION;
  }

  @Override
  public Object getOverrideToken()
  {
    if (isMergeDisabled())
    {
      return super.getOverrideToken();
    }

    return getClass();
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    P2Task overriddenP2Task = (P2Task)overriddenSetupTask;
    getRequirements().addAll(overriddenP2Task.getRequirements());
    getRepositories().addAll(overriddenP2Task.getRepositories());

    String overriddenLabel = overriddenP2Task.getLabel();
    if (!StringUtil.isEmpty(overriddenLabel))
    {
      String label = getLabel();
      if (!StringUtil.isEmpty(label) && !overriddenLabel.contains(label))
      {
        overriddenLabel += " + " + label;
      }

      setLabel(overriddenLabel);
    }
  }

  @Override
  public void consolidate()
  {
    Set<String> installableUnitKeys = new HashSet<String>();
    for (Iterator<Requirement> it = getRequirements().iterator(); it.hasNext();)
    {
      Requirement requirement = it.next();
      String name = requirement.getName();
      if (StringUtil.isEmpty(name) || !installableUnitKeys.add(name + "->" + requirement.getVersionRange().toString()))
      {
        it.remove();
      }
    }

    Set<String> repositoryKeys = new HashSet<String>();
    for (Iterator<Repository> it = getRepositories().iterator(); it.hasNext();)
    {
      Repository repository = it.next();
      String url = repository.getURL();
      if (StringUtil.isEmpty(url) || !repositoryKeys.add(url))
      {
        it.remove();
      }
    }
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  private void addUnknownRepository(IRepositoryManager<?> repositoryManager, Set<String> knownRepositories, String url)
  {
    if (!knownRepositories.contains(url))
    {
      try
      {
        repositoryManager.addRepository(new URI(url));
      }
      catch (Exception ex)
      {
        SetupP2Plugin.INSTANCE.log(ex);
      }
    }
  }

  private boolean isInstalled(Set<IInstallableUnit> installedUnits, String id, VersionRange versionRange)
  {
    for (IInstallableUnit installedUnit : installedUnits)
    {
      if (id.equals(installedUnit.getId()) && versionRange.isIncluded(installedUnit.getVersion()))
      {
        return true;
      }
    }

    return false;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (SKIP)
    {
      return FORCE;
    }

    Trigger trigger = context.getTrigger();
    if (trigger == Trigger.BOOTSTRAP)
    {
      return true;
    }

    if (context.isSelfHosting())
    {
      return false;
    }

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    if (profile == null)
    {
      // We're most likely in self hosting mode, where software updates are not really well supported.
      return FORCE;
    }

    IMetadataRepositoryManager metadataRepositoryManager = agent.getMetadataRepositoryManager();
    IArtifactRepositoryManager artifactRepositoryManager = agent.getArtifactRepositoryManager();

    Set<String> knownMetadataRepositories = P2Util.getKnownRepositories(metadataRepositoryManager);
    Set<String> knownArtifactRepositories = P2Util.getKnownRepositories(artifactRepositoryManager);

    for (Repository repository : getRepositories())
    {
      String url = repository.getURL();
      addUnknownRepository(metadataRepositoryManager, knownMetadataRepositories, url);
      addUnknownRepository(artifactRepositoryManager, knownArtifactRepositories, url);
    }

    if (trigger == Trigger.MANUAL)
    {
      return true;
    }

    Set<IInstallableUnit> installedUnits = getInstalledUnits(agent);
    Set<Requirement> unsatisifiedRequirements = new LinkedHashSet<Requirement>();
    for (Requirement requirement : getRequirements())
    {
      if (context.matchesFilterContext(requirement.getFilter()) && !requirement.isOptional())
      {
        String id = requirement.getName();
        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange == null)
        {
          versionRange = VersionRange.emptyRange;
        }

        if (!isInstalled(installedUnits, id, versionRange))
        {
          unsatisifiedRequirements.add(requirement);
        }
      }
    }

    P2TaskUISevices p2TaskUISevices = (P2TaskUISevices)context.get(P2TaskUISevices.class);
    if (trigger == Trigger.STARTUP && !unsatisifiedRequirements.isEmpty() && p2TaskUISevices != null)
    {
      return p2TaskUISevices.handleUnsatisfiedRequirements(unsatisifiedRequirements, installedUnits);
    }

    return !unsatisifiedRequirements.isEmpty();
  }

  public void perform(final SetupTaskContext context) throws Exception
  {
    File eclipseDir = context.getProductLocation();

    boolean offline = context.isOffline();
    context.log("Offline = " + offline);

    boolean mirrors = context.isMirrors();
    context.log("Mirrors = " + mirrors);

    EList<Requirement> requirements = getRequirements();
    EList<Repository> repositories = getRepositories();

    context.log("Resolving " + requirements.size() + (requirements.size() == 1 ? " requirement" : " requirements") + " from " + repositories.size()
        + (repositories.size() == 1 ? " repository" : " repositories") + " to " + eclipseDir.getAbsolutePath());

    for (Requirement requirement : requirements)
    {
      context.log("Requirement " + requirement);
    }

    for (Repository repository : repositories)
    {
      context.log("Repository " + repository);
    }

    String profileID = IOUtil.encodeFileName(eclipseDir.toString());

    Profile profile = getProfile(context, profileID);
    ProfileTransaction transaction = profile.change();

    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    profileDefinition.setRequirements(requirements);
    profileDefinition.setRepositories(repositories);

    ProfileTransaction.CommitContext commitContext = new ProfileTransaction.CommitContext()
    {
      @Override
      public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
      {
        try
        {
          processLicenses(context, info.getProvisioningPlan(), context.getProgressMonitor(false));
        }
        catch (Exception ex)
        {
          SetupP2Plugin.INSTANCE.coreException(ex);
        }

        return true;
      }

      @Override
      public Confirmer getUnsignedContentConfirmer()
      {
        return (Confirmer)context.get(Certificate.class);
      }
    };

    transaction.setMirrors(mirrors);

    IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();

    CacheUsageConfirmer cacheUsageConfirmer = (CacheUsageConfirmer)context.get(CacheUsageConfirmer.class);
    CacheUsageConfirmer oldCacheUsageConfirmer = (CacheUsageConfirmer)provisioningAgent.getService(CacheUsageConfirmer.SERVICE_NAME);

    IProvisioningEventBus eventBus = null;
    ProvisioningListener provisioningListener = (ProvisioningListener)context.get(ProvisioningListener.class);
    if (provisioningListener != null)
    {
      eventBus = (IProvisioningEventBus)provisioningAgent.getService(IProvisioningEventBus.SERVICE_NAME);
      if (eventBus != null)
      {
        eventBus.addListener(provisioningListener);
      }
    }

    boolean originalBetterMirrorSelection = CachingRepositoryManager.enableBetterMirrorSelection();

    try
    {
      if (cacheUsageConfirmer != null)
      {
        provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, cacheUsageConfirmer);
      }

      boolean profileChanged = transaction.commit(commitContext, context.getProgressMonitor(true));
      if (context.getTrigger() != Trigger.BOOTSTRAP)
      {
        if (profileChanged)
        {
          context.setRestartNeeded("New software has been installed.");
        }
        else
        {
          context.log("No software updates are available");
        }
      }
      else
      {
        context.put(Profile.class, profile);
      }
    }
    finally
    {
      CachingRepositoryManager.setBetterMirrorSelection(originalBetterMirrorSelection);

      if (eventBus != null)
      {
        eventBus.removeListener(provisioningListener);
      }

      if (cacheUsageConfirmer != null)
      {
        provisioningAgent.unregisterService(CacheUsageConfirmer.SERVICE_NAME, cacheUsageConfirmer);
        if (oldCacheUsageConfirmer != null)
        {
          provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, oldCacheUsageConfirmer);
        }
      }
    }
  }

  private Profile getProfile(final SetupTaskContext context, String profileID) throws Exception
  {
    Profile profile;
    if (context.getTrigger() == Trigger.BOOTSTRAP)
    {
      BundlePool bundlePool;

      String bundlePoolLocation = (String)context.get(AgentManager.PROP_BUNDLE_POOL_LOCATION);
      boolean sharedPool;
      if (bundlePoolLocation != null)
      {
        sharedPool = true;
        bundlePool = P2Util.getAgentManager().getBundlePool(new File(bundlePoolLocation));

        // TODO Remove the following two lines after bug 485018 has been fixed.
        File eclipseExtensionFeaturesFolder = new File(bundlePoolLocation, ".eclipseextension/features");
        eclipseExtensionFeaturesFolder.mkdirs();
      }
      else
      {
        sharedPool = false;
        File agentLocation = new File(context.getProductLocation(), "p2");
        Agent agent = P2Util.createAgent(agentLocation);
        bundlePool = agent.addBundlePool(agentLocation.getParentFile());
      }

      IProvisioningAgent currentAgent = P2Util.getCurrentProvisioningAgent();
      bundlePool.getAgent().getProvisioningAgent().registerService(IProvisioningAgent.INSTALLER_AGENT, currentAgent);

      profile = bundlePool.getProfile(profileID);
      if (profile != null && context.put(FIRST_CALL_DETECTION_KEY, Boolean.TRUE) == null)
      {
        profile.delete(true);
        profile = null;
      }

      if (profile == null)
      {
        OS os = context.getOS();

        ProfileCreator profileCreator = bundlePool.addProfile(profileID, Profile.TYPE_INSTALLATION);
        profileCreator.setInstallFeatures(true);
        profileCreator.setInstallFolder(context.getProductLocation());
        profileCreator.addOS(os.getOsgiOS());
        profileCreator.addWS(os.getOsgiWS());
        profileCreator.addArch(os.getOsgiArch());
        profileCreator.setRoaming(true);

        // This property is used in org.eclipse.oomph.p2.internal.core.LazyProfile.setProperty(String, String)
        // to ensure that when org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry.updateRoamingProfile(Profile)
        // updates the profile's install location, it doesn't also replace the shared pool location with a local pool.
        // Of course if this is an installation with a local pool, it should be replace during a roaming update.
        profileCreator.set(Profile.PROP_PROFILE_SHARED_POOL, sharedPool);

        profile = profileCreator.create();
      }

      UIServices uiServices = (UIServices)context.get(UIServices.class);
      if (uiServices != null)
      {
        IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();
        provisioningAgent.registerService(UIServices.SERVICE_NAME, uiServices);
      }

      return profile;
    }
    else
    {
      Agent agent = P2Util.getAgentManager().getCurrentAgent();
      profile = agent.getCurrentProfile();
    }

    return profile;
  }

  private void processLicenses(final SetupTaskContext context, IProvisioningPlan provisioningPlan, IProgressMonitor monitor) throws Exception
  {
    if (isLicenseConfirmationDisabled())
    {
      return;
    }

    User user = context.getUser();
    Confirmer licenseConfirmer = (Confirmer)context.get(ILicense.class);

    processLicenses(provisioningPlan, licenseConfirmer, user, false, monitor);
  }

  public static void processLicenses(IProvisioningPlan provisioningPlan, Confirmer licenseConfirmer, final User user, boolean saveChangedUser,
      IProgressMonitor monitor)
  {
    Set<LicenseInfo> acceptedLicenses = new HashSet<LicenseInfo>();
    if (user != null)
    {
      acceptedLicenses.addAll(user.getAcceptedLicenses());
    }

    final Map<ILicense, List<IInstallableUnit>> licensesToIUs = new HashMap<ILicense, List<IInstallableUnit>>();
    Set<Pair<ILicense, String>> set = new HashSet<Pair<ILicense, String>>();

    IQueryable<IInstallableUnit> queryable = provisioningPlan.getAdditions();
    IQueryResult<IInstallableUnit> result = queryable.query(QueryUtil.ALL_UNITS, monitor);
    for (IInstallableUnit iu : P2Util.asIterable(result))
    {
      Collection<ILicense> licenses = iu.getLicenses(null);
      for (ILicense license : licenses)
      {
        if ("license".equals(license.getBody()))
        {
          // Work around bug 463387.
          continue;
        }

        String uuid = license.getUUID();
        if (acceptedLicenses.contains(new LicenseInfo(uuid, null)))
        {
          continue;
        }

        String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
        if (name == null)
        {
          name = iu.getId();
        }

        if (!set.add(Pair.create(license, name)))
        {
          continue;
        }

        List<IInstallableUnit> ius = licensesToIUs.get(license);
        if (ius == null)
        {
          ius = new ArrayList<IInstallableUnit>();
          licensesToIUs.put(license, ius);
        }

        ius.add(iu);
      }
    }

    if (!licensesToIUs.isEmpty())
    {
      if (licenseConfirmer == null)
      {
        licenseConfirmer = Confirmer.DECLINE;
      }

      Confirmation confirmation = licenseConfirmer.confirm(false, licensesToIUs);
      if (!confirmation.isConfirmed())
      {
        throw new OperationCanceledException("Licenses have been declined");
      }

      if (user != null && confirmation.isRemember())
      {
        for (ILicense license : licensesToIUs.keySet())
        {
          String uuid = license.getUUID();
          String name = LicenseInfo.getFirstLine(license.getBody());

          LicenseInfo licenseInfo = new LicenseInfo(uuid, name);
          user.getAcceptedLicenses().add(licenseInfo);
        }

        if (saveChangedUser)
        {
          try
          {
            XMLResource xmlResource = (XMLResource)user.eResource();
            xmlResource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
          }
          catch (IOException ex)
          {
            SetupP2Plugin.INSTANCE.log(ex);
          }
        }
      }
    }
  }

  private static Set<IInstallableUnit> getInstalledUnits(Agent agent)
  {
    Set<IInstallableUnit> result = new HashSet<IInstallableUnit>();

    IProfileRegistry profileRegistry = agent.getProfileRegistry();
    IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
    if (profile != null)
    {
      IQueryResult<IInstallableUnit> queryResult = profile.query(QueryUtil.createIUAnyQuery(), null);
      for (IInstallableUnit requirement : P2Util.asIterable(queryResult))
      {
        result.add(requirement);
      }
    }

    return result;
  }

  public static void saveConfigIni(File file, Map<String, String> properties, Class<?> caller)
  {
    PropertiesUtil.saveProperties(file, properties, false, true, "This configuration file was written by: " + caller.getName());
  }
} // InstallTaskImpl

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
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.internal.p2.SetupP2Plugin;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.p2.SetupP2Package;
import org.eclipse.oomph.setup.util.DownloadUtil;
import org.eclipse.oomph.setup.util.OS;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Confirmer.Confirmation;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.internal.repository.tools.MirrorApplication;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Install Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isMergeDisabled <em>Merge Disabled</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class P2TaskImpl extends SetupTaskImpl implements P2Task
{
  private static final boolean SKIP = "true".equals(PropertiesUtil.getProperty(PROP_SKIP));

  private static final Object FIRST_CALL_DETECTION_KEY = new Object();

  @SuppressWarnings("unused")
  private static final Class<MirrorApplication> MIRROR_CLASS = MirrorApplication.class;

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
      if (!StringUtil.isEmpty(label))
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
      if (!installableUnitKeys.add(requirement.getName() + "->" + requirement.getVersionRange().toString()))
      {
        it.remove();
      }
    }

    Set<String> repositoryKeys = new HashSet<String>();
    for (Iterator<Repository> it = getRepositories().iterator(); it.hasNext();)
    {
      Repository repository = it.next();
      if (!repositoryKeys.add(repository.getURL()))
      {
        it.remove();
      }
    }
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
      return false;
    }

    Trigger trigger = context.getTrigger();
    if (trigger == Trigger.BOOTSTRAP)
    {
      return true;
    }

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    if (profile == null)
    {
      // We're most likely in self hosting mode, where software updates are not really well supported.
      return false;
    }

    IMetadataRepositoryManager metadataRepositoryManager = agent.getMetadataRepositoryManager();
    IArtifactRepositoryManager artifactRepositoryManager = agent.getArtifactRepositoryManager();

    Set<String> knownMetadataRepositories = P2Util.getKnownRepositories(metadataRepositoryManager);
    Set<String> knownArtifactRepositories = P2Util.getKnownRepositories(artifactRepositoryManager);

    for (Repository repository : getRepositories())
    {
      String url = context.redirect(repository.getURL());
      addUnknownRepository(metadataRepositoryManager, knownMetadataRepositories, url);
      addUnknownRepository(artifactRepositoryManager, knownArtifactRepositories, url);
    }

    if (trigger == Trigger.MANUAL)
    {
      return true;
    }

    Set<IInstallableUnit> installedUnits = getInstalledUnits(agent);
    for (Requirement requirement : getRequirements())
    {
      if (!requirement.isOptional())
      {
        String id = requirement.getName();
        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange == null)
        {
          versionRange = VersionRange.emptyRange;
        }

        if (!isInstalled(installedUnits, id, versionRange))
        {
          return true;
        }
      }
    }

    return false;
  }

  public void perform(final SetupTaskContext context) throws Exception
  {
    final IProgressMonitor monitor = new ProgressLogMonitor(context);

    File eclipseDir = context.getProductLocation();
    File eclipseIni = new File(eclipseDir, "eclipse.ini");
    boolean eclipseIniExisted = eclipseIni.exists();

    EList<Requirement> requirements = getRequirements();
    EList<Repository> repositories = getRepositories();

    context.log("Resolving " + requirements.size() + (requirements.size() == 1 ? " requirement" : " requirements") + " from " + repositories.size()
        + (repositories.size() == 1 ? " repository" : " repositories") + " to " + eclipseDir.getAbsolutePath());

    boolean offline = context.isOffline();
    context.log("Offline = " + offline);

    boolean mirrors = context.isMirrors();
    context.log("Mirrors = " + mirrors);

    for (Requirement requirement : requirements)
    {
      context.log("Requirement " + requirement);
    }

    for (Repository repository : repositories)
    {
      String url = context.redirect(repository.getURL());
      repository.setURL(url);
      context.log("Repository " + url);
    }

    String profileID = IOUtil.encodeFileName(eclipseDir.toString());

    Profile profile = getProfile(context, profileID);
    ProfileTransaction transaction = profile.change();
    transaction.getProfileDefinition().setRequirements(requirements);
    transaction.getProfileDefinition().setRepositories(repositories);

    ProfileTransaction.CommitContext commitContext = new ProfileTransaction.CommitContext()
    {
      @Override
      public boolean handleProvisioningPlan(IProvisioningPlan provisioningPlan, List<IMetadataRepository> metadataRepositories) throws CoreException
      {
        try
        {
          processLicenses(context, provisioningPlan, monitor);
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

    transaction.setOffline(offline);
    transaction.setMirrors(mirrors);

    boolean profileChanged = transaction.commit(commitContext, monitor);
    if (profileChanged && context.getTrigger() != Trigger.BOOTSTRAP)
    {
      context.setRestartNeeded("New software has been installed.");
    }

    if (eclipseIniExisted)
    {
      checkEclipseIniForDuplicates(context, eclipseIni);
    }

    updateSplash(context, profile);
  }

  private void updateSplash(final SetupTaskContext context, Profile profile)
  {
    BundlePool bundlePool = profile.getBundlePool();
    if (bundlePool != null)
    {
      File productConfigurationLocation = context.getProductConfigurationLocation();
      try
      {
        File configIniFile = new File(productConfigurationLocation, "config.ini");
        Map<String, String> properties = PropertiesUtil.loadProperties(configIniFile);
        String splashPath = properties.get("osgi.splashPath");
        if (splashPath != null)
        {
          org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI(splashPath);
          if ("platform".equals(uri.scheme()) && uri.segmentCount() >= 2 && "base".equals(uri.segment(0)))
          {
            for (IInstallableUnit installableUnit : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(uri.lastSegment()), null)))
            {
              for (IArtifactKey artifactKey : installableUnit.getArtifacts())
              {
                File artifactFile = bundlePool.getFileArtifactRepository().getArtifactFile(artifactKey);
                if (new File(artifactFile, "splash.bmp").exists())
                {
                  properties.put("osgi.splashPath", org.eclipse.emf.common.util.URI.createFileURI(artifactFile.toString()).toString());
                  PropertiesUtil.saveProperties(configIniFile, properties, false);

                  return;
                }
              }
            }
          }
        }
      }
      catch (IORuntimeException ex)
      {
        // Ignore.
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
      if (bundlePoolLocation != null)
      {
        bundlePool = P2Util.getAgentManager().getBundlePool(new File(bundlePoolLocation));
      }
      else
      {
        File agentLocation = new File(context.getProductLocation(), "p2");
        Agent agent = P2Util.createAgent(agentLocation);
        bundlePool = agent.addBundlePool(agentLocation.getParentFile());
      }

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
    Set<LicenseInfo> acceptedLicenses = new HashSet<LicenseInfo>(user.getAcceptedLicenses());

    final Map<ILicense, List<IInstallableUnit>> licensesToIUs = new HashMap<ILicense, List<IInstallableUnit>>();
    Set<Pair<ILicense, String>> set = new HashSet<Pair<ILicense, String>>();

    IQueryable<IInstallableUnit> queryable = provisioningPlan.getAdditions();
    IQueryResult<IInstallableUnit> result = queryable.query(QueryUtil.ALL_UNITS, monitor);
    for (IInstallableUnit iu : P2Util.asIterable(result))
    {
      Collection<ILicense> licenses = iu.getLicenses(null);
      for (ILicense license : licenses)
      {
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
        throw new UnsupportedOperationException("Licenses have been declined");
      }

      if (confirmation.isRemember())
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
            xmlResource.save(null);
          }
          catch (IOException ex)
          {
            SetupP2Plugin.INSTANCE.log(ex);
          }
        }
      }
    }
  }

  private static void checkEclipseIniForDuplicates(final SetupTaskContext context, File iniFile)
  {
    FileOutputStream out = null;

    try
    {
      org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createFileURI(iniFile.toString());
      String contents = DownloadUtil.load(context.getURIConverter(), uri, null);
      Pattern section = Pattern.compile("^(-vmargs)([\n\r]+.*)\\z|^(-[^\\n\\r]*[\\n\\r]*)((?:^[^-][^\\n\\r]*)*[\\n\\r]*)", Pattern.MULTILINE | Pattern.DOTALL);
      Map<String, String> map = new LinkedHashMap<String, String>();
      for (Matcher matcher = section.matcher(contents); matcher.find();)
      {
        String argument = matcher.group(3);
        String extension;
        if (argument == null)
        {
          argument = matcher.group(1);
          extension = matcher.group(2);
        }
        else
        {
          extension = matcher.group(4);
        }

        if (!argument.startsWith("--launcher.XXMaxPermSize") || !map.containsKey(argument))
        {
          map.put(argument, extension);
        }
      }

      StringBuilder newContents = new StringBuilder();
      for (Map.Entry<String, String> entry : map.entrySet())
      {
        newContents.append(entry.getKey());
        newContents.append(entry.getValue());
      }

      out = new FileOutputStream(iniFile);
      out.write(newContents.toString().getBytes());
    }
    catch (IOException ex)
    {
      // Ignore.
    }
    finally
    {
      IOUtil.close(out);
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

  @Override
  public MirrorRunnable mirror(final MirrorContext context, final File mirrorsDir, boolean includingLocals) throws Exception
  {
    throw new UnsupportedOperationException();

    // // There's no longer a predefined bundle pool location for Buckminster
    // return new MirrorRunnable()
    // {
    // public void run(IProgressMonitor monitor) throws Exception
    // {
    // String targetURL = URI.createFileURI(context.getP2PoolTPDir().toString()).toString();
    //
    // SlicingOptions slicingOptions = new SlicingOptions();
    // slicingOptions.latestVersionOnly(true);
    // slicingOptions.everythingGreedy(false);
    //
    // MirrorApplication app = new MirrorApplication()
    // {
    // @Override
    // public IStatus run(IProgressMonitor monitor) throws ProvisionException
    // {
    // IStatus mirrorStatus = Status.OK_STATUS;
    // monitor.beginTask("", 1 + 100 + 1000 + 100);
    //
    // try
    // {
    // initializeRepos(new SubProgressMonitor(monitor, 1));
    // initializeIUs();
    //
    // IQueryable<IInstallableUnit> slice = slice(new SubProgressMonitor(monitor, 100));
    //
    // mirrorStatus = mirrorArtifacts(slice, new SubProgressMonitor(monitor, 1000));
    // mirrorMetadata(slice, new SubProgressMonitor(monitor, 100));
    // }
    // finally
    // {
    // finalizeRepositories();
    // }
    //
    // if (mirrorStatus.isOK())
    // {
    // return Status.OK_STATUS;
    // }
    //
    // return mirrorStatus;
    // }
    //
    // private void initializeIUs()
    // {
    // Method method = ReflectUtil.getMethod(MIRROR_CLASS, "initializeIUs");
    // ReflectUtil.invokeMethod(method, this);
    // }
    //
    // @SuppressWarnings("unchecked")
    // private IQueryable<IInstallableUnit> slice(IProgressMonitor monitor)
    // {
    // Method method = ReflectUtil.getMethod(MIRROR_CLASS, "slice", IProgressMonitor.class);
    // return (IQueryable<IInstallableUnit>)ReflectUtil.invokeMethod(method, this, monitor);
    // }
    //
    // private IStatus mirrorArtifacts(IQueryable<IInstallableUnit> slice, IProgressMonitor monitor)
    // {
    // Method method = ReflectUtil.getMethod(MIRROR_CLASS, "mirrorArtifacts", IQueryable.class,
    // IProgressMonitor.class);
    // return (IStatus)ReflectUtil.invokeMethod(method, this, slice, monitor);
    // }
    //
    // private void mirrorMetadata(IQueryable<IInstallableUnit> slice, IProgressMonitor monitor)
    // {
    // Method method = ReflectUtil.getMethod(MIRROR_CLASS, "mirrorMetadata", IQueryable.class,
    // IProgressMonitor.class);
    // ReflectUtil.invokeMethod(method, this, slice, monitor);
    // }
    // };
    //
    // app.setIncludePacked(false);
    // app.setVerbose(true);
    // app.setSlicingOptions(slicingOptions);
    //
    // RepositoryDescriptor destination = new RepositoryDescriptor();
    // destination.setLocation(new URI(targetURL));
    // destination.setAppend(true);
    // app.addDestination(destination);
    //
    // initSourceRepos(app, context, targetURL);
    // initRootIUs(app);
    //
    // app.run(monitor);
    // }
    //
    // private void initSourceRepos(MirrorApplication app, final MirrorContext context, String targetURL)
    // throws URISyntaxException
    // {
    // for (P2Repository p2Repository : getP2Repositories())
    // {
    // String sourceURL = context.redirect(URI.createURI(p2Repository.getURL())).toString();
    //
    // RepositoryDescriptor descriptor = new RepositoryDescriptor();
    // descriptor.setLocation(new URI(sourceURL));
    // app.addSource(descriptor);
    //
    // context.addRedirection(sourceURL, targetURL);
    // }
    // }
    //
    // private void initRootIUs(MirrorApplication app)
    // {
    // EList<Requirement> installableUnits = getRequirements();
    // String[] rootIUs = new String[installableUnits.size()];
    // for (int i = 0; i < rootIUs.length; i++)
    // {
    // Requirement requirement = installableUnits.get(i);
    // rootIUs[i] = requirement.getID();
    //
    // VersionRange range = requirement.getVersionRange();
    // if (!VersionRange.emptyRange.equals(range))
    // {
    // rootIUs[i] += "/" + range;
    // }
    // }
    //
    // Field field = ReflectUtil.getField(MIRROR_CLASS, "rootIUs");
    // ReflectUtil.setValue(field, app, rootIUs);
    // }
    // };
  }

  @Override
  public void collectSniffers(List<Sniffer> sniffers)
  {
    sniffers.add(new BasicSniffer(this, "Creates a task to install the current software.")
    {
      public void sniff(SetupTaskContainer container, List<Sniffer> dependencies, IProgressMonitor monitor) throws Exception
      {
        Agent agent = P2Util.getAgentManager().getCurrentAgent();
        Set<IInstallableUnit> installedUnits = getInstalledUnits(agent);

        IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
        Set<String> knownRepositories = P2Util.getKnownRepositories(manager);
        if (installedUnits.isEmpty() && knownRepositories.isEmpty())
        {
          return;
        }

        P2Task task = SetupP2Factory.eINSTANCE.createP2Task();
        container.getSetupTasks().add(task);

        for (IInstallableUnit iu : installedUnits)
        {
          Requirement requirement = P2Factory.eINSTANCE.createRequirement(iu.getId());
          task.getRequirements().add(requirement);
        }

        for (String url : knownRepositories)
        {
          Repository repository = P2Factory.eINSTANCE.createRepository(url);
          task.getRepositories().add(repository);
        }
      }
    });
  }

} // InstallTaskImpl

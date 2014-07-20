/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 429520 - Support additional push URL
 */
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTask.Sniffer.SourcePathProvider;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.GitPackage;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.setup.util.FileUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Git Clone Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getRemoteName <em>Remote Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getRemoteURI <em>Remote URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getPushURI <em>Push URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getCheckoutBranch <em>Checkout Branch</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GitCloneTaskImpl extends SetupTaskImpl implements GitCloneTask
{
  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

  /**
   * The default value of the '{@link #getRemoteName() <em>Remote Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteName()
   * @generated
   * @ordered
   */
  protected static final String REMOTE_NAME_EDEFAULT = "origin";

  /**
   * The cached value of the '{@link #getRemoteName() <em>Remote Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteName()
   * @generated
   * @ordered
   */
  protected String remoteName = REMOTE_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURI()
   * @generated
   * @ordered
   */
  protected static final String REMOTE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURI()
   * @generated
   * @ordered
   */
  protected String remoteURI = REMOTE_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getPushURI() <em>Push URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPushURI()
   * @generated
   * @ordered
   */
  protected static final String PUSH_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPushURI() <em>Push URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPushURI()
   * @generated
   * @ordered
   */
  protected String pushURI = PUSH_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getCheckoutBranch() <em>Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckoutBranch()
   * @generated
   * @ordered
   */
  protected static final String CHECKOUT_BRANCH_EDEFAULT = "${scope.project.stream.name}";

  /**
   * The cached value of the '{@link #getCheckoutBranch() <em>Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckoutBranch()
   * @generated
   * @ordered
   */
  protected String checkoutBranch = CHECKOUT_BRANCH_EDEFAULT;

  private boolean workDirExisted;

  private File workDir;

  private boolean hasCheckout;

  private Git cachedGit;

  private Repository cachedRepository;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GitCloneTaskImpl()
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
    return GitPackage.Literals.GIT_CLONE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRemoteName()
  {
    return remoteName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteName(String newRemoteName)
  {
    String oldRemoteName = remoteName;
    remoteName = newRemoteName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__REMOTE_NAME, oldRemoteName, remoteName));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRemoteURI()
  {
    return remoteURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteURI(String newRemoteURI)
  {
    String oldRemoteURI = remoteURI;
    remoteURI = newRemoteURI;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__REMOTE_URI, oldRemoteURI, remoteURI));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCheckoutBranch()
  {
    return checkoutBranch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCheckoutBranch(String newCheckoutBranch)
  {
    String oldCheckoutBranch = checkoutBranch;
    checkoutBranch = newCheckoutBranch;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH, oldCheckoutBranch, checkoutBranch));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPushURI()
  {
    return pushURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPushURI(String newPushURI)
  {
    String oldPushURI = pushURI;
    pushURI = newPushURI;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__PUSH_URI, oldPushURI, pushURI));
    }
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
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        return getLocation();
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        return getRemoteName();
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        return getRemoteURI();
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        return getPushURI();
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        return getCheckoutBranch();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        setRemoteName((String)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        setRemoteURI((String)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        setPushURI((String)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        setCheckoutBranch((String)newValue);
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
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        setRemoteName(REMOTE_NAME_EDEFAULT);
        return;
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        setRemoteURI(REMOTE_URI_EDEFAULT);
        return;
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        setPushURI(PUSH_URI_EDEFAULT);
        return;
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        setCheckoutBranch(CHECKOUT_BRANCH_EDEFAULT);
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
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        return REMOTE_NAME_EDEFAULT == null ? remoteName != null : !REMOTE_NAME_EDEFAULT.equals(remoteName);
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        return REMOTE_URI_EDEFAULT == null ? remoteURI != null : !REMOTE_URI_EDEFAULT.equals(remoteURI);
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        return PUSH_URI_EDEFAULT == null ? pushURI != null : !PUSH_URI_EDEFAULT.equals(pushURI);
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        return CHECKOUT_BRANCH_EDEFAULT == null ? checkoutBranch != null : !CHECKOUT_BRANCH_EDEFAULT.equals(checkoutBranch);
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
    result.append(" (location: ");
    result.append(location);
    result.append(", remoteName: ");
    result.append(remoteName);
    result.append(", remoteURI: ");
    result.append(remoteURI);
    result.append(", pushURI: ");
    result.append(pushURI);
    result.append(", checkoutBranch: ");
    result.append(checkoutBranch);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String location = getLocation();

    workDir = new File(location);
    if (!workDir.isDirectory())
    {
      return true;
    }

    workDirExisted = true;

    if (workDir.list().length > 1)
    {
      return false;
    }

    context.log("Opening Git clone " + workDir);

    try
    {
      Git git = Git.open(workDir);
      if (!hasWorkTree(git))
      {
        FileUtil.rename(workDir);
        return true;
      }

      Repository repository = git.getRepository();
      String checkoutBranch = getCheckoutBranch();
      String remoteName = getRemoteName();
      String remoteURI = context.redirect(getRemoteURI());
      String pushURI = context.redirect(getPushURI());
      configureRepository(context, repository, checkoutBranch, remoteName, remoteURI, pushURI);

      hasCheckout = repository.getAllRefs().containsKey("refs/heads/" + checkoutBranch);
      if (!hasCheckout)
      {
        cachedGit = git;
        cachedRepository = repository;
        return true;
      }

      return false;
    }
    catch (Throwable ex)
    {
      if (!workDirExisted)
      {
        FileUtil.delete(workDir, new ProgressLogMonitor(context));
      }

      throw new Exception(ex);
    }
  }

  @Override
  public Object getOverrideToken()
  {
    String token = getLocation();
    if (StringUtil.isEmpty(token))
    {
      token = getRemoteURI();
    }

    return createToken(token);
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);
    // Just ignore the overrides for the same location as long as the checkout branch is identical
    GitCloneTask gitCloneTask = (GitCloneTask)overriddenSetupTask;
    if (!ObjectUtil.equals(gitCloneTask.getCheckoutBranch(), getCheckoutBranch()))
    {
      Annotation errorAnnotation = BaseFactory.eINSTANCE.createErrorAnnotation("Multiple different Git clones cannot be at the same location");
      getAnnotations().add(errorAnnotation);
    }
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    try
    {
      // Force start egit to make the clone appears in the repositories view and so projects are connected by the egit team provider.
      try
      {
        CommonPlugin.loadClass("org.eclipse.egit.ui", "org.eclipse.egit.ui.Activator");
      }
      catch (ClassNotFoundException ex)
      {
        // Ignore.
      }

      String checkoutBranch = getCheckoutBranch();
      String remoteName = getRemoteName();
      String remoteURI = context.redirect(getRemoteURI());

      if (cachedGit == null)
      {
        cachedGit = cloneRepository(context, workDir, checkoutBranch, remoteName, remoteURI);
        cachedRepository = cachedGit.getRepository();

        if (!URI.createURI(remoteURI).isFile())
        {
          String pushURI = context.redirect(getPushURI());
          configureRepository(context, cachedRepository, checkoutBranch, remoteName, remoteURI, pushURI);
        }
      }

      if (!hasCheckout)
      {
        createBranch(context, cachedGit, checkoutBranch, remoteName);
        checkout(context, cachedGit, checkoutBranch);
        resetHard(context, cachedGit);
      }
    }
    catch (Throwable ex)
    {
      if (!workDirExisted)
      {
        FileUtil.delete(workDir, new ProgressLogMonitor(context));
      }

      throw new Exception(ex);
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();

    if (cachedRepository != null)
    {
      cachedRepository.close();
    }
  }

  @Override
  public MirrorRunnable mirror(final MirrorContext context, File mirrorsDir, boolean includingLocals) throws Exception
  {
    String sourceURL = context.redirect(getRemoteURI());

    final File local = new File(mirrorsDir, IOUtil.encodeFileName(sourceURL));
    String localURL = URI.createFileURI(local.getAbsolutePath()).toString();
    context.addRedirection(sourceURL, localURL);

    if (local.exists())
    {
      return null;
    }

    return new MirrorRunnable()
    {
      public void run(IProgressMonitor monitor) throws Exception
      {
        File repo = new File(getLocation(), ".git");
        File localRepo = new File(local, ".git");

        IOUtil.copyTree(repo, localRepo);
      }
    };
  }

  @Override
  public void collectSniffers(List<Sniffer> sniffers)
  {
    sniffers.add(new CloneSniffer(true));
    sniffers.add(new CloneSniffer(false));
  }

  private static boolean hasWorkTree(Git git) throws Exception
  {
    try
    {
      StatusCommand statusCommand = git.status();
      statusCommand.call();
      return true;
    }
    catch (NoWorkTreeException ex)
    {
      return false;
    }
  }

  private static Git cloneRepository(SetupTaskContext context, File workDir, String checkoutBranch, String remoteName, String remoteURI) throws Exception
  {
    context.log("Cloning Git repo " + remoteURI + " to " + workDir);

    CloneCommand command = Git.cloneRepository();
    command.setNoCheckout(true);
    command.setURI(remoteURI);
    command.setRemote(remoteName);
    command.setBranchesToClone(Collections.singleton(checkoutBranch));
    command.setDirectory(workDir);
    command.setTimeout(60);
    command.setProgressMonitor(new ProgressLogWrapper(context));
    return command.call();
  }

  private static void configureRepository(SetupTaskContext context, Repository repository, String checkoutBranch, String remoteName, String remoteURI,
      String pushURI) throws Exception, IOException
  {
    StoredConfig config = repository.getConfig();

    boolean changed = false;
    changed |= configureLineEndingConversion(context, config);
    URI uri = URI.createURI(remoteURI);
    if ("git.eclipse.org".equals(uri.host()) && uri.port() != null)
    {
      changed |= addPushRefSpec(context, config, checkoutBranch, remoteName);
    }

    changed |= addPushURI(context, config, remoteName, pushURI);
    if (changed)
    {
      config.save();
    }
  }

  private static boolean configureLineEndingConversion(SetupTaskContext context, StoredConfig config) throws Exception
  {
    if (context.getOS().isLineEndingConversionNeeded())
    {
      if (context.isPerforming())
      {
        context.log("Setting " + ConfigConstants.CONFIG_KEY_AUTOCRLF + " = true");
      }

      config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF, AutoCRLF.TRUE);
      return true;
    }

    return false;
  }

  private static boolean addPushRefSpec(SetupTaskContext context, StoredConfig config, String checkoutBranch, String remoteName) throws Exception
  {
    String gerritQueue = "refs/for/" + checkoutBranch;
    for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
    {
      if (remoteName.equals(remoteConfig.getName()))
      {
        List<RefSpec> pushRefSpecs = remoteConfig.getPushRefSpecs();
        if (hasGerritPushRefSpec(pushRefSpecs, gerritQueue))
        {
          return false;
        }

        RefSpec refSpec = new RefSpec("HEAD:" + gerritQueue);

        if (context.isPerforming())
        {
          context.log("Adding push ref spec: " + refSpec);
        }

        remoteConfig.addPushRefSpec(refSpec);
        remoteConfig.update(config);
        return true;
      }
    }

    return false;
  }

  private static boolean addPushURI(SetupTaskContext context, StoredConfig config, String remoteName, String pushURI) throws Exception
  {
    boolean uriAdded = false;

    if (!StringUtil.isEmpty(pushURI))
    {
      URIish uri = new URIish(pushURI);
      for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
      {
        if (remoteName.equals(remoteConfig.getName()))
        {
          if (context.isPerforming())
          {
            context.log("Adding push URI: " + pushURI);
          }
          uriAdded = remoteConfig.addPushURI(uri);
          if (uriAdded)
          {
            remoteConfig.update(config);
          }
          break;
        }
      }
    }
    return uriAdded;
  }

  private static boolean hasGerritPushRefSpec(List<RefSpec> pushRefSpecs, String gerritQueue)
  {
    for (RefSpec refSpec : pushRefSpecs)
    {
      if (refSpec.getDestination().equals(gerritQueue))
      {
        return true;
      }
    }

    return false;
  }

  private static void createBranch(SetupTaskContext context, Git git, String checkoutBranch, String remoteName) throws Exception
  {
    context.log("Creating local branch " + checkoutBranch);

    CreateBranchCommand command = git.branchCreate();
    command.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
    command.setName(checkoutBranch);
    command.setStartPoint("refs/remotes/" + remoteName + "/" + checkoutBranch);
    command.call();

    StoredConfig config = git.getRepository().getConfig();
    config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, checkoutBranch, ConfigConstants.CONFIG_KEY_REBASE, true);
    config.save();
  }

  private static void checkout(SetupTaskContext context, Git git, String checkoutBranch) throws Exception
  {
    context.log("Checking out local branch " + checkoutBranch);

    CheckoutCommand command = git.checkout();
    command.setName(checkoutBranch);
    command.call();
  }

  private static void resetHard(SetupTaskContext context, Git git) throws Exception
  {
    context.log("Resetting hard");

    ResetCommand command = git.reset();
    command.setMode(ResetType.HARD);
    command.call();
  }

  /**
   * @author Eike Stepper
   */
  private static final class ProgressLogWrapper implements ProgressMonitor
  {
    private SetupTaskContext context;

    private String title;

    private int work;

    private int totalWork;

    public ProgressLogWrapper(SetupTaskContext context)
    {
      this.context = context;
    }

    public void update(int completed)
    {
      work += completed;
      if (totalWork != 0 && title != null)
      {
        context.log(title + " (" + Math.round(100f / totalWork * work) + "%)");
      }
    }

    public void start(int totalTasks)
    {
    }

    public boolean isCancelled()
    {
      return context.isCanceled();
    }

    public void endTask()
    {
      if (title != null)
      {
        context.log(title + " (100%)");
      }

      title = null;
      totalWork = 0;
      work = 0;
    }

    public void beginTask(String title, int totalWork)
    {
      if (title != null)
      {
        if (title.startsWith("remote: "))
        {
          title = title.substring(8);
        }

        context.log(title + " (0%)");
      }

      this.title = title;
      this.totalWork = totalWork;
      work = 0;
    }
  }

  /**
   * @author Eike Stepper
   */
  private class CloneSniffer extends BasicSniffer implements SourcePathProvider
  {
    private final Map<File, IPath> sourcePaths = new HashMap<File, IPath>();

    private boolean used;

    public CloneSniffer(boolean used)
    {
      super(GitCloneTaskImpl.this);
      setLabel((used ? "Used" : "Unused") + " Git clones");
      setDescription("Creates tasks for the " + StringUtil.uncap(getLabel()) + ".");
      this.used = used;
    }

    public Map<File, IPath> getSourcePaths()
    {
      return sourcePaths;
    }

    public void sniff(SetupTaskContainer container, List<Sniffer> dependencies, IProgressMonitor monitor) throws Exception
    {
      try
      {
        List<Repository> repositories = new ArrayList<Repository>();
        RepositoryUtil repositoryUtil = org.eclipse.egit.core.Activator.getDefault().getRepositoryUtil();
        for (String path : repositoryUtil.getConfiguredRepositories())
        {
          if (used == isUsed(new Path(path).removeLastSegments(1)))
          {
            Git git = Git.open(new File(path));
            Repository repository = git.getRepository();
            repositories.add(repository);
          }
        }

        if (!repositories.isEmpty())
        {
          monitor.beginTask("", repositories.size());

          for (Repository repository : repositories)
          {
            addTaskForRepository(container, repository);
            monitor.worked(1);
          }
        }
      }
      finally
      {
        monitor.done();
      }
    }

    private boolean isUsed(IPath path)
    {
      for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      {
        if (contains(path, project.getLocation()))
        {
          return true;
        }
      }

      return false;
    }

    private boolean contains(IPath container, IPath contained)
    {
      String[] containerSegments = container.segments();
      String[] containedSegments = contained.segments();
      if (containedSegments.length < containerSegments.length)
      {
        return false;
      }

      for (int i = 0; i < containerSegments.length; i++)
      {
        if (!containerSegments[i].equals(containedSegments[i]))
        {
          return false;
        }
      }

      return true;
    }

    private void addTaskForRepository(SetupTaskContainer container, Repository repository) throws Exception
    {
      StoredConfig config = repository.getConfig();
      RemoteConfig remoteConfig = getRemoteConfig(config);
      if (remoteConfig == null)
      {
        return;
      }

      File workTree = repository.getWorkTree();
      IPath relativePath = new Path("git").append(workTree.getName());

      int xxx;
      // TODO this isn't correct.
      String location = "${setup.branch.dir/" + relativePath + "}";
      sourcePaths.put(workTree, relativePath);

      String remoteURI = getRemoteURI(remoteConfig);

      URI uri = URI.createURI(remoteURI);
      if (uri.userInfo() != null)
      {
        String authority = uri.authority();
        int pos = authority.indexOf('@');
        authority = authority.substring(pos + 1);
        remoteURI = URI.createHierarchicalURI(uri.scheme(), authority, uri.device(), uri.segments(), uri.query(), uri.fragment()).toString();
      }

      GitCloneTask task = GitFactory.eINSTANCE.createGitCloneTask();
      task.setCheckoutBranch(repository.getBranch());
      task.setLocation(location);
      task.setRemoteName(remoteConfig.getName());
      task.setRemoteURI(remoteURI);

      CompoundTask compound = getCompound(container, getLabel());
      compound.getSetupTasks().add(task);
    }

    private RemoteConfig getRemoteConfig(StoredConfig config) throws URISyntaxException
    {
      RemoteConfig firstConfig = null;
      for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
      {
        if ("origin".equals(remoteConfig.getName()))
        {
          return remoteConfig;
        }

        if (firstConfig == null)
        {
          firstConfig = remoteConfig;
        }
      }

      return firstConfig;
    }

    private String getRemoteURI(RemoteConfig remoteConfig)
    {
      String remoteURI = getRemoteURI(remoteConfig.getPushURIs());
      if (StringUtil.isEmpty(remoteURI))
      {
        remoteURI = getRemoteURI(remoteConfig.getURIs());
      }

      return remoteURI;
    }

    private String getRemoteURI(List<URIish> uris)
    {
      if (uris == null || uris.isEmpty())
      {
        return "";
      }

      return uris.get(0).toString();
    }
  }

} // GitCloneTaskImpl

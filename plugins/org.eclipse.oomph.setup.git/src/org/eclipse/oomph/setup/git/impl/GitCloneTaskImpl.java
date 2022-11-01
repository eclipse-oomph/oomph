/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 429520 - Support additional push URL
 *    Ericsson AB (Julian Enoch) - Bug 462008 - Support submodules while cloning a Git repo
 */
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.git.ConfigProperty;
import org.eclipse.oomph.setup.git.ConfigSection;
import org.eclipse.oomph.setup.git.ConfigSubsection;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitPackage;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.util.FileUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.egit.core.settings.GitSettings;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ReflogCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.SubmoduleUpdateCommand;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.submodule.SubmoduleWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Git Clone Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getLocationQualifier <em>Location Qualifier</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getRemoteName <em>Remote Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getRemoteURI <em>Remote URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getPushURI <em>Push URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getCheckoutBranch <em>Checkout Branch</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#getConfigSections <em>Config Sections</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl#isRestrictToCheckoutBranch <em>Restrict To Checkout Branch</em>}</li>
 * </ul>
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
  protected static final String LOCATION_EDEFAULT = ""; //$NON-NLS-1$

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
   * The default value of the '{@link #getLocationQualifier() <em>Location Qualifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationQualifier()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_QUALIFIER_EDEFAULT = " "; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getLocationQualifier() <em>Location Qualifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationQualifier()
   * @generated
   * @ordered
   */
  protected String locationQualifier = LOCATION_QUALIFIER_EDEFAULT;

  /**
   * The default value of the '{@link #getRemoteName() <em>Remote Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteName()
   * @generated
   * @ordered
   */
  protected static final String REMOTE_NAME_EDEFAULT = "origin"; //$NON-NLS-1$

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
  protected static final String CHECKOUT_BRANCH_EDEFAULT = "${scope.project.stream.name}"; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getCheckoutBranch() <em>Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckoutBranch()
   * @generated
   * @ordered
   */
  protected String checkoutBranch = CHECKOUT_BRANCH_EDEFAULT;

  /**
   * The default value of the '{@link #isRecursive() <em>Recursive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRecursive()
   * @generated
   * @ordered
   */
  protected static final boolean RECURSIVE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isRecursive() <em>Recursive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRecursive()
   * @generated
   * @ordered
   */
  protected boolean recursive = RECURSIVE_EDEFAULT;

  /**
   * The cached value of the '{@link #getConfigSections() <em>Config Sections</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConfigSections()
   * @generated
   * @ordered
   */
  protected EList<ConfigSection> configSections;

  /**
   * The default value of the '{@link #isRestrictToCheckoutBranch() <em>Restrict To Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRestrictToCheckoutBranch()
   * @generated
   * @ordered
   */
  protected static final boolean RESTRICT_TO_CHECKOUT_BRANCH_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isRestrictToCheckoutBranch() <em>Restrict To Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRestrictToCheckoutBranch()
   * @generated
   * @ordered
   */
  protected boolean restrictToCheckoutBranch = RESTRICT_TO_CHECKOUT_BRANCH_EDEFAULT;

  private boolean workDirExisted;

  private File workDir;

  private boolean hasCheckout;

  private Git cachedGit;

  private Repository cachedRepository;

  private boolean bypassCloning;

  private int timeout;

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
  @Override
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public String getLocationQualifier()
  {
    return locationQualifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLocationQualifier(String newLocationQualifier)
  {
    String oldLocationQualifier = locationQualifier;
    locationQualifier = newLocationQualifier;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__LOCATION_QUALIFIER, oldLocationQualifier, locationQualifier));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getRemoteName()
  {
    return remoteName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public String getRemoteURI()
  {
    return remoteURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public String getCheckoutBranch()
  {
    return checkoutBranch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public boolean isRecursive()
  {
    return recursive;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRecursive(boolean newRecursive)
  {
    boolean oldRecursive = recursive;
    recursive = newRecursive;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__RECURSIVE, oldRecursive, recursive));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ConfigSection> getConfigSections()
  {
    if (configSections == null)
    {
      configSections = new EObjectContainmentEList<>(ConfigSection.class, this, GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS);
    }
    return configSections;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isRestrictToCheckoutBranch()
  {
    return restrictToCheckoutBranch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRestrictToCheckoutBranch(boolean newRestrictToCheckoutBranch)
  {
    boolean oldRestrictToCheckoutBranch = restrictToCheckoutBranch;
    restrictToCheckoutBranch = newRestrictToCheckoutBranch;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH, oldRestrictToCheckoutBranch,
          restrictToCheckoutBranch));
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
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
        return ((InternalEList<?>)getConfigSections()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getPushURI()
  {
    return pushURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
      case GitPackage.GIT_CLONE_TASK__LOCATION_QUALIFIER:
        return getLocationQualifier();
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        return getRemoteName();
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        return getRemoteURI();
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        return getPushURI();
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        return getCheckoutBranch();
      case GitPackage.GIT_CLONE_TASK__RECURSIVE:
        return isRecursive();
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
        return getConfigSections();
      case GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH:
        return isRestrictToCheckoutBranch();
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
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__LOCATION_QUALIFIER:
        setLocationQualifier((String)newValue);
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
      case GitPackage.GIT_CLONE_TASK__RECURSIVE:
        setRecursive((Boolean)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
        getConfigSections().clear();
        getConfigSections().addAll((Collection<? extends ConfigSection>)newValue);
        return;
      case GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH:
        setRestrictToCheckoutBranch((Boolean)newValue);
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
      case GitPackage.GIT_CLONE_TASK__LOCATION_QUALIFIER:
        setLocationQualifier(LOCATION_QUALIFIER_EDEFAULT);
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
      case GitPackage.GIT_CLONE_TASK__RECURSIVE:
        setRecursive(RECURSIVE_EDEFAULT);
        return;
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
        getConfigSections().clear();
        return;
      case GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH:
        setRestrictToCheckoutBranch(RESTRICT_TO_CHECKOUT_BRANCH_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("null")
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case GitPackage.GIT_CLONE_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case GitPackage.GIT_CLONE_TASK__LOCATION_QUALIFIER:
        return LOCATION_QUALIFIER_EDEFAULT == null ? locationQualifier != null : !LOCATION_QUALIFIER_EDEFAULT.equals(locationQualifier);
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
        return REMOTE_NAME_EDEFAULT == null ? remoteName != null : !REMOTE_NAME_EDEFAULT.equals(remoteName);
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
        return REMOTE_URI_EDEFAULT == null ? remoteURI != null : !REMOTE_URI_EDEFAULT.equals(remoteURI);
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
        return PUSH_URI_EDEFAULT == null ? pushURI != null : !PUSH_URI_EDEFAULT.equals(pushURI);
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
        return CHECKOUT_BRANCH_EDEFAULT == null ? checkoutBranch != null : !CHECKOUT_BRANCH_EDEFAULT.equals(checkoutBranch);
      case GitPackage.GIT_CLONE_TASK__RECURSIVE:
        return recursive != RECURSIVE_EDEFAULT;
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
        return configSections != null && !configSections.isEmpty();
      case GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH:
        return restrictToCheckoutBranch != RESTRICT_TO_CHECKOUT_BRANCH_EDEFAULT;
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (location: "); //$NON-NLS-1$
    result.append(location);
    result.append(", locationQualifier: "); //$NON-NLS-1$
    result.append(locationQualifier);
    result.append(", remoteName: "); //$NON-NLS-1$
    result.append(remoteName);
    result.append(", remoteURI: "); //$NON-NLS-1$
    result.append(remoteURI);
    result.append(", pushURI: "); //$NON-NLS-1$
    result.append(pushURI);
    result.append(", checkoutBranch: "); //$NON-NLS-1$
    result.append(checkoutBranch);
    result.append(", recursive: "); //$NON-NLS-1$
    result.append(recursive);
    result.append(", restrictToCheckoutBranch: "); //$NON-NLS-1$
    result.append(restrictToCheckoutBranch);
    result.append(')');
    return result.toString();
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
    if (!ObjectUtil.equals(gitCloneTask.getRemoteURI(), getRemoteURI()) || !ObjectUtil.equals(gitCloneTask.getCheckoutBranch(), getCheckoutBranch()))
    {
      Annotation errorAnnotation = BaseFactory.eINSTANCE.createErrorAnnotation(Messages.GitCloneTaskImpl_CloneCollision_message);
      getAnnotations().add(errorAnnotation);
    }
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  @Override
  public boolean isNeeded(final SetupTaskContext context) throws Exception
  {
    if (StringUtil.isEmpty(getRemoteURI()))
    {
      return false;
    }

    // If the EGit UI is available, this will contain the list of repositories that have been added to the repositories view.
    Set<Path> repositories = null;

    // Force start egit to make the clone appears in the repositories view and so projects are connected by the egit team provider.
    // Also use this as an opportunity to initialize the timeout based on EGit preferences
    try
    {
      repositories = new HashSet<>(GitSettings.getConfiguredRepositoryDirectories());
      timeout = GitSettings.getRemoteConnectionTimeout();
    }
    catch (Throwable ex)
    {
      // Ignore.
    }

    String location = getLocation();

    workDir = new File(location);
    if (!workDir.isDirectory())
    {
      return true;
    }

    workDirExisted = true;

    boolean needsToBeAdded = repositories != null && !repositories.contains(new File(workDir, ".git").toPath()); //$NON-NLS-1$
    if (workDir.list().length > 1)
    {
      // Even though cloning isn't needed, return true if the repository needs to be added to the repositories view.
      bypassCloning = true;
      return needsToBeAdded;
    }

    context.log(NLS.bind(Messages.GitCloneTaskImpl_OpeningClone_message, workDir));

    try
    {
      Git git = Git.open(workDir);
      if (!hasWorkTree(git) || !hasReflog(git))
      {
        FileUtil.rename(workDir);
        workDirExisted = false;
        return true;
      }

      Repository repository = git.getRepository();
      String checkoutBranch = getCheckoutBranch();
      String remoteName = getRemoteName();
      String remoteURI = getRemoteURI();
      String pushURI = getPushURI();
      configureRepository(context, repository, checkoutBranch, isRestrictToCheckoutBranch(), remoteName, remoteURI, pushURI, getConfigSections(),
          getGerritPatterns(context));

      hasCheckout = findRef(repository, Constants.R_HEADS + checkoutBranch) != null;
      if (!hasCheckout)
      {
        cachedGit = git;
        cachedRepository = repository;
        return true;
      }

      // Even though cloning isn't needed, return true if the repository needs to be added to the repositories view.
      bypassCloning = true;
      return needsToBeAdded;
    }
    catch (Throwable ex)
    {
      if (!workDirExisted)
      {
        FileUtil.delete(workDir, new NullProgressMonitor()
        {
          @Override
          public boolean isCanceled()
          {
            return context.isCanceled();
          }
        });
      }

      throw new Exception(ex);
    }
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    try
    {
      String checkoutBranch = getCheckoutBranch();
      String remoteName = getRemoteName();
      String remoteURI = getRemoteURI();
      Set<String> gerritPatterns = getGerritPatterns(context);

      IProgressMonitor monitor = context.getProgressMonitor(true);
      monitor.beginTask("", (cachedGit == null ? 51 : 0) + (!hasCheckout ? 3 : 0) + (isRecursive() ? 20 : 0)); //$NON-NLS-1$

      try
      {
        if (!bypassCloning)
        {
          if (cachedGit == null)
          {
            cachedGit = cloneRepository(context, workDir, checkoutBranch, isRestrictToCheckoutBranch(), remoteName, remoteURI, isRecursive(), timeout,
                MonitorUtil.create(monitor, 50));
            cachedRepository = cachedGit.getRepository();

            if (!URI.createURI(remoteURI).isFile())
            {
              String pushURI = getPushURI();
              configureRepository(context, cachedRepository, checkoutBranch, isRestrictToCheckoutBranch(), remoteName, remoteURI, pushURI, getConfigSections(),
                  gerritPatterns);
            }

            monitor.worked(1);
          }

          // If we get this far, but something else fails later, we don't want to delete the repo
          // because then we can't determine what's wrong with the current state.
          workDirExisted = true;

          if (!hasCheckout)
          {
            Ref branchRef = findRef(cachedGit.getRepository(), Constants.R_REMOTES + remoteName + "/" + checkoutBranch); //$NON-NLS-1$
            Ref tagRef = findRef(cachedGit.getRepository(), Constants.R_TAGS + checkoutBranch);
            if (branchRef == null && tagRef != null)
            {
              createTag(context, cachedGit, checkoutBranch);
              monitor.worked(1);

              checkoutTag(context, cachedGit, checkoutBranch);
              monitor.worked(1);
            }
            else
            {
              createBranch(context, cachedGit, checkoutBranch, remoteName);
              monitor.worked(1);

              checkoutBranch(context, cachedGit, checkoutBranch);
              monitor.worked(1);
            }

            resetHard(context, cachedGit);
            monitor.worked(1);
          }

          if (isRecursive())
          {
            addSubmodules(context, cachedGit, checkoutBranch, remoteName, gerritPatterns, MonitorUtil.create(monitor, 20));
          }
        }

        // Add the clone to the Git repositories view.
        GitSettings.addConfiguredRepository(new File(workDir, ".git").toPath()); //$NON-NLS-1$
      }
      finally
      {
        monitor.done();
      }
    }
    catch (Throwable ex)
    {
      if (!workDirExisted)
      {
        context.setTerminating();

        if (ex instanceof OperationCanceledException)
        {
          context.log(Messages.GitCloneTaskImpl_DeletingClone_message);
        }

        try
        {
          FileUtil.delete(workDir, new NullProgressMonitor()
          {
            @Override
            public boolean isCanceled()
            {
              return false;
            }
          });
        }
        catch (Exception deleteException)
        {
          context.log(deleteException);
        }
      }

      if (ex instanceof OperationCanceledException)
      {
        throw (OperationCanceledException)ex;
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

  private static boolean hasReflog(Git git) throws Exception
  {
    try
    {
      ReflogCommand reflogCommand = git.reflog();
      Collection<?> reflog = reflogCommand.call();
      return !reflog.isEmpty();
    }
    catch (InvalidRefNameException ex)
    {
      return false;
    }
  }

  private static Git cloneRepository(SetupTaskContext context, File workDir, String checkoutBranch, boolean restrictToCheckoutBranch, String remoteName,
      String remoteURI, boolean recursive, int timeout, IProgressMonitor monitor) throws Exception
  {
    context.log(NLS.bind(Messages.GitCloneTaskImpl_CloningRepo_message, remoteURI, workDir));

    CloneCommand command = Git.cloneRepository();
    command.setNoCheckout(true);
    command.setURI(remoteURI);
    command.setRemote(remoteName);
    command.setCloneAllBranches(!restrictToCheckoutBranch);
    command.setCloneSubmodules(recursive);
    if (restrictToCheckoutBranch)
    {
      command.setBranchesToClone(Collections.singleton(Constants.R_HEADS + checkoutBranch));
    }

    command.setDirectory(workDir);
    command.setTimeout(timeout <= 0 ? 60 : timeout);
    command.setProgressMonitor(new EclipseGitProgressTransformer(monitor));
    return command.call();
  }

  private static void configureRepository(SetupTaskContext context, Repository repository, String checkoutBranch, boolean restrictToCheckoutBranch,
      String remoteName, String remoteURI, String pushURI, List<? extends ConfigSection> configSections, Set<String> gerritPatterns)
      throws Exception, IOException
  {
    StoredConfig config = repository.getConfig();

    Map<String, Map<String, Map<String, List<String>>>> properties = new LinkedHashMap<>();

    for (ConfigSection section : configSections)
    {
      String sectionName = section.getName();
      if (!StringUtil.isEmpty(sectionName))
      {
        for (ConfigProperty property : section.getProperties())
        {
          handleProperty(properties, sectionName, null, property);
        }

        for (ConfigSubsection subsection : section.getSubsections())
        {
          String subsectionName = subsection.getName();
          if (subsectionName != null)
          {
            for (ConfigProperty property : subsection.getProperties())
            {
              handleProperty(properties, sectionName, subsectionName, property);
            }
          }
        }
      }
    }

    boolean changed = false;
    boolean hasAutoCRLFProperty = false;

    for (Map.Entry<String, Map<String, Map<String, List<String>>>> sectionEntry : properties.entrySet())
    {
      String sectionName = sectionEntry.getKey();
      for (Map.Entry<String, Map<String, List<String>>> subsectionEntry : sectionEntry.getValue().entrySet())
      {
        String subsectionName = subsectionEntry.getKey();
        for (Map.Entry<String, List<String>> propertyEntry : subsectionEntry.getValue().entrySet())
        {
          String key = propertyEntry.getKey();
          if ("core".equals(sectionName) && subsectionName == null && "autocrlf".equals(key)) //$NON-NLS-1$ //$NON-NLS-2$
          {
            hasAutoCRLFProperty = true;
          }

          List<String> value = propertyEntry.getValue();
          String[] oldValue = config.getStringList(sectionName, subsectionName, key);
          if (value.isEmpty())
          {
            config.unset(sectionName, subsectionName, key);
            changed |= oldValue.length != 0;
          }
          else
          {
            config.setStringList(sectionName, subsectionName, key, value);
            changed |= !Arrays.asList(oldValue).equals(value);
          }
        }
      }
    }

    if (!hasAutoCRLFProperty)
    {
      changed |= configureLineEndingConversion(context, config);
    }

    if (!gerritPatterns.isEmpty())
    {
      URI uri = URI.createURI(remoteURI);
      String uriString = uri.toString();
      for (String gerritPattern : gerritPatterns)
      {
        if (uriString.matches(gerritPattern))
        {
          changed |= addGerritPullRefSpec(context, config, remoteName);
          changed |= addGerritPushRefSpec(context, config, checkoutBranch, remoteName);
          break;
        }
      }
    }

    changed |= addPushURI(context, config, remoteName, pushURI);
    if (changed)
    {
      config.save();
    }

    if (restrictToCheckoutBranch)
    {
      changed |= setSingleFetchRefSpec(context, config, checkoutBranch, remoteName);
    }
  }

  private static Set<String> getGerritPatterns(SetupTaskContext context)
  {
    Set<String> gerritPatterns = new HashSet<>();
    for (Object key : context.keySet())
    {
      if (key instanceof String)
      {
        if (key.toString().endsWith(".gerrit.uri.pattern")) //$NON-NLS-1$
        {
          Object value = context.get(key);
          if (value instanceof String)
          {
            gerritPatterns.add(value.toString());
          }
        }
      }
    }
    return gerritPatterns;
  }

  /**
   * Adjust the fetch ref spec for a single branch clone.
   */
  private static boolean setSingleFetchRefSpec(SetupTaskContext context, StoredConfig config, String checkoutBranch, String remoteName) throws Exception
  {
    for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
    {
      if (remoteName.equals(remoteConfig.getName()))
      {
        RefSpec oldRrefSpec = new RefSpec();
        oldRrefSpec = oldRrefSpec.setForceUpdate(true);
        oldRrefSpec = oldRrefSpec.setSourceDestination(Constants.R_HEADS + "*", Constants.R_REMOTES + remoteName + "/*"); //$NON-NLS-1$ //$NON-NLS-2$

        final String src = Constants.R_HEADS + checkoutBranch;
        final String dst = Constants.R_REMOTES + remoteName + "/" + checkoutBranch; //$NON-NLS-1$
        RefSpec newRefSpec = new RefSpec();
        newRefSpec = newRefSpec.setForceUpdate(true);
        newRefSpec = newRefSpec.setSourceDestination(src, dst);

        if (remoteConfig.addFetchRefSpec(newRefSpec) && remoteConfig.removeFetchRefSpec(oldRrefSpec) && context.isPerforming())
        {
          context.log(Messages.GitCloneTaskImpl_FetchingRefSpec_message);
        }

        remoteConfig.update(config);
        return true;
      }
    }

    return false;
  }

  private static void handleProperty(Map<String, Map<String, Map<String, List<String>>>> properties, String sectionName, String subsectionName,
      ConfigProperty property)
  {
    String key = property.getKey();
    if (!StringUtil.isEmpty(key))
    {
      String value = property.getValue();
      if (StringUtil.isEmpty(value))
      {
        value = null;
      }

      Map<String, Map<String, List<String>>> subsections = properties.get(sectionName);
      if (subsections == null)
      {
        subsections = new LinkedHashMap<>();
        properties.put(sectionName, subsections);
      }

      Map<String, List<String>> sectionProperties = subsections.get(subsectionName);
      if (sectionProperties == null)
      {
        sectionProperties = new LinkedHashMap<>();
        subsections.put(subsectionName, sectionProperties);
      }

      List<String> list = sectionProperties.get(key);
      if (list == null)
      {
        list = new ArrayList<>();
        sectionProperties.put(key, list);
      }

      if (value != null)
      {
        list.add(value);
      }
    }
  }

  private static boolean configureLineEndingConversion(SetupTaskContext context, StoredConfig config) throws Exception
  {
    OS os = context.getOS();
    if (os.isLineEndingConversionNeeded())
    {
      if (context.isPerforming())
      {
        context.log(NLS.bind(Messages.GitCloneTaskImpl_SettingConfig_message, ConfigConstants.CONFIG_KEY_AUTOCRLF));
      }

      config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF, AutoCRLF.TRUE);
      return true;
    }

    return false;
  }

  private static boolean addGerritPullRefSpec(SetupTaskContext context, StoredConfig config, String remoteName) throws Exception
  {
    for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
    {
      if (remoteName.equals(remoteConfig.getName()))
      {
        RefSpec refSpec = new RefSpec("refs/notes/*:refs/notes/*"); //$NON-NLS-1$
        if (remoteConfig.addFetchRefSpec(refSpec) && context.isPerforming())
        {
          context.log(NLS.bind(Messages.GitCloneTaskImpl_AddingFetchRefSpec_message, refSpec));
        }

        remoteConfig.update(config);
        return true;
      }
    }

    return false;
  }

  private static boolean addGerritPushRefSpec(SetupTaskContext context, StoredConfig config, String checkoutBranch, String remoteName) throws Exception
  {
    for (RemoteConfig remoteConfig : RemoteConfig.getAllRemoteConfigs(config))
    {
      if (remoteName.equals(remoteConfig.getName()))
      {
        RefSpec refSpec = new RefSpec("HEAD:refs/for/" + checkoutBranch); //$NON-NLS-1$
        if (remoteConfig.addPushRefSpec(refSpec) && context.isPerforming())
        {
          context.log(NLS.bind(Messages.GitCloneTaskImpl_AddingPushRefSpec_message, refSpec));
        }

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
            context.log(NLS.bind(Messages.GitCloneTaskImpl_AddingPushURI_message, pushURI));
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

  private static void createBranch(SetupTaskContext context, Git git, String checkoutBranch, String remoteName) throws Exception
  {
    context.log(NLS.bind(Messages.GitCloneTaskImpl_CreatingLocalBranch_message, checkoutBranch));

    if (findRef(git.getRepository(), Constants.R_HEADS + checkoutBranch) == null)
    {
      CreateBranchCommand command = git.branchCreate();
      command.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
      command.setName(checkoutBranch);
      command.setStartPoint(Constants.R_REMOTES + remoteName + "/" + checkoutBranch); //$NON-NLS-1$
      command.call();
    }

    StoredConfig config = git.getRepository().getConfig();
    config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, checkoutBranch, ConfigConstants.CONFIG_KEY_REBASE, true);
    config.save();
  }

  private static void createTag(SetupTaskContext context, Git git, String checkoutTag) throws Exception
  {
    context.log(NLS.bind(Messages.GitCloneTaskImpl_CreatingLocalTab_message, checkoutTag));
    if (findRef(git.getRepository(), Constants.R_HEADS + checkoutTag) == null)
    {
      CreateBranchCommand command = git.branchCreate();
      command.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
      command.setName(checkoutTag);
      command.setStartPoint(Constants.R_TAGS + checkoutTag);
      command.call();
    }

    StoredConfig config = git.getRepository().getConfig();
    config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, checkoutTag, ConfigConstants.CONFIG_KEY_REBASE, true);
    config.save();
  }

  private static void checkoutTag(SetupTaskContext context, Git git, String checkoutTag) throws Exception
  {
    context.log(NLS.bind(Messages.GitCloneTaskImpl_CheckingOutLocalBranch_message, checkoutTag));

    CheckoutCommand command = git.checkout();
    command.setName(Constants.R_HEADS + checkoutTag);
    command.call();
  }

  private static void checkoutBranch(SetupTaskContext context, Git git, String checkoutBranch) throws Exception
  {
    context.log(NLS.bind(Messages.GitCloneTaskImpl_CheckingOutLocalBranch_message, checkoutBranch));

    CheckoutCommand command = git.checkout();
    command.setName(checkoutBranch);
    command.call();
  }

  private static void addSubmodules(SetupTaskContext context, Git git, String checkoutBranch, String remoteName, Set<String> gerritPatterns,
      IProgressMonitor monitor) throws Exception
  {
    context.log(Messages.GitCloneTaskImpl_AddingSubmodules_message);

    git.submoduleInit().call();

    SubmoduleUpdateCommand updateCommand = git.submoduleUpdate();
    updateCommand.setProgressMonitor(new EclipseGitProgressTransformer(monitor));
    updateCommand.call();
    if (!gerritPatterns.isEmpty())
    {
      submoduleConfigureGerrit(context, git, checkoutBranch, remoteName, gerritPatterns, monitor);
    }
  }

  private static void submoduleConfigureGerrit(SetupTaskContext context, Git git, String checkoutBranch, String remoteName, Set<String> gerritPatterns,
      IProgressMonitor monitor) throws Exception
  {
    Repository repo = git.getRepository();
    SubmoduleWalk generator = null;
    try
    {
      generator = SubmoduleWalk.forIndex(repo);
      while (generator.next())
      {
        String subRemoteURI = generator.getRemoteUrl();
        for (String gerritPattern : gerritPatterns)
        {
          if (subRemoteURI.matches(gerritPattern))
          {
            context.log(NLS.bind(Messages.GitCloneTaskImpl_ConfigureSubmodule_message, generator.getModuleName()));
            Repository subRepo = generator.getRepository();
            StoredConfig subConfig = subRepo.getConfig();
            addGerritPullRefSpec(context, subConfig, remoteName);
            addGerritPushRefSpec(context, subConfig, checkoutBranch, remoteName);
            subConfig.save();
            break;
          }
        }
      }
    }
    finally
    {
      if (generator != null)
      {
        generator.close();
      }
    }
  }

  private static void resetHard(SetupTaskContext context, Git git) throws Exception
  {
    context.log(Messages.GitCloneTaskImpl_ResettingHard_message);

    ResetCommand command = git.reset();
    command.setMode(ResetType.HARD);
    command.call();
  }

  @Deprecated
  private static final Ref findRef(Repository repository, String name) throws IOException
  {
    return repository.getRefDatabase().getRef(name);
  }

} // GitCloneTaskImpl

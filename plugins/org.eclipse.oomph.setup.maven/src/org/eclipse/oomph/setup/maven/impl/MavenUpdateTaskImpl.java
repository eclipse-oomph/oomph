/*
 * Copyright (c) 2020 Martin Schreiber (Bachmann electronic GmbH) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Martin Schreiber - initial API and implementation
 */
package org.eclipse.oomph.setup.maven.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.maven.MavenPackage;
import org.eclipse.oomph.setup.maven.MavenUpdateTask;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.MavenUpdateRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl#getProjectNamePatterns <em>Project Name Patterns</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl#isOffline <em>Offline</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenUpdateTaskImpl#isUpdateSnapshots <em>Update Snapshots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MavenUpdateTaskImpl extends SetupTaskImpl implements MavenUpdateTask
{

  /**
   * The cached value of the '{@link #getProjectNamePatterns() <em>Project Name Patterns</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectNamePatterns()
   * @generated
   * @ordered
   */
  protected EList<String> projectNamePatterns;

  /**
   * The default value of the '{@link #isOffline() <em>Offline</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOffline()
   * @generated
   * @ordered
   */
  protected static final boolean OFFLINE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOffline() <em>Offline</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOffline()
   * @generated
   * @ordered
   */
  protected boolean offline = OFFLINE_EDEFAULT;

  /**
   * The default value of the '{@link #isUpdateSnapshots() <em>Update Snapshots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpdateSnapshots()
   * @generated
   * @ordered
   */
  protected static final boolean UPDATE_SNAPSHOTS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUpdateSnapshots() <em>Update Snapshots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpdateSnapshots()
   * @generated
   * @ordered
   */
  protected boolean updateSnapshots = UPDATE_SNAPSHOTS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MavenUpdateTaskImpl()
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
    return MavenPackage.Literals.MAVEN_UPDATE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getProjectNamePatterns()
  {
    if (projectNamePatterns == null)
    {
      projectNamePatterns = new EDataTypeUniqueEList<String>(String.class, this, MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS);
    }
    return projectNamePatterns;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOffline()
  {
    return offline;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOffline(boolean newOffline)
  {
    boolean oldOffline = offline;
    offline = newOffline;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.MAVEN_UPDATE_TASK__OFFLINE, oldOffline, offline));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUpdateSnapshots()
  {
    return updateSnapshots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUpdateSnapshots(boolean newUpdateSnapshots)
  {
    boolean oldUpdateSnapshots = updateSnapshots;
    updateSnapshots = newUpdateSnapshots;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS, oldUpdateSnapshots, updateSnapshots));
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
      case MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS:
        return getProjectNamePatterns();
      case MavenPackage.MAVEN_UPDATE_TASK__OFFLINE:
        return isOffline();
      case MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS:
        return isUpdateSnapshots();
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
      case MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS:
        getProjectNamePatterns().clear();
        getProjectNamePatterns().addAll((Collection<? extends String>)newValue);
        return;
      case MavenPackage.MAVEN_UPDATE_TASK__OFFLINE:
        setOffline((Boolean)newValue);
        return;
      case MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS:
        setUpdateSnapshots((Boolean)newValue);
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
      case MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS:
        getProjectNamePatterns().clear();
        return;
      case MavenPackage.MAVEN_UPDATE_TASK__OFFLINE:
        setOffline(OFFLINE_EDEFAULT);
        return;
      case MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS:
        setUpdateSnapshots(UPDATE_SNAPSHOTS_EDEFAULT);
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
      case MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS:
        return projectNamePatterns != null && !projectNamePatterns.isEmpty();
      case MavenPackage.MAVEN_UPDATE_TASK__OFFLINE:
        return offline != OFFLINE_EDEFAULT;
      case MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS:
        return updateSnapshots != UPDATE_SNAPSHOTS_EDEFAULT;
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
    result.append(" (projectNamePatterns: ");
    result.append(projectNamePatterns);
    result.append(", offline: ");
    result.append(offline);
    result.append(", updateSnapshots: ");
    result.append(updateSnapshots);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return true;
  }

  private boolean matchesNameFilter(String projectName)
  {
    if (projectNamePatterns.isEmpty())
    {
      return true;
    }

    for (String pattern : getProjectNamePatterns())
    {
      if (Pattern.matches(pattern, projectName))
      {
        return true;
      }
    }

    return false;
  }

  private List<IProject> getFilteredProjects() throws CoreException
  {
    List<IProject> projects = new ArrayList<IProject>();
    for (IMavenProjectFacade projectFacade : MavenPlugin.getMavenProjectRegistry().getProjects())
    {
      if (matchesNameFilter(projectFacade.getProject().getName()))
      {
        projects.add(projectFacade.getProject());
      }
    }

    return projects;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    List<IProject> projects = getFilteredProjects();
    MavenUpdateRequest updateRequest = new MavenUpdateRequest(projects.toArray(new IProject[0]), isOffline(), isUpdateSnapshots());
    MavenPlugin.getMavenProjectRegistry().refresh(updateRequest);
  }

} // MavenUpdateTaskImpl

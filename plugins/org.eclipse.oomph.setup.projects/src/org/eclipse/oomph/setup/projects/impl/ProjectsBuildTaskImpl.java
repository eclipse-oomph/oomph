/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projects.impl;

import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesUtil;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.projects.ProjectsBuildTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Build Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl#isOnlyNewProjects <em>Only New Projects</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl#isRefresh <em>Refresh</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl#isClean <em>Clean</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsBuildTaskImpl#isBuild <em>Build</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectsBuildTaskImpl extends SetupTaskImpl implements ProjectsBuildTask
{
  /**
   * The cached value of the '{@link #getPredicates() <em>Predicates</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredicates()
   * @generated
   * @ordered
   */
  protected EList<Predicate> predicates;

  /**
   * The default value of the '{@link #isOnlyNewProjects() <em>Only New Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOnlyNewProjects()
   * @generated
   * @ordered
   */
  protected static final boolean ONLY_NEW_PROJECTS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOnlyNewProjects() <em>Only New Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOnlyNewProjects()
   * @generated
   * @ordered
   */
  protected boolean onlyNewProjects = ONLY_NEW_PROJECTS_EDEFAULT;

  private static final IWorkspaceRoot ROOT = EcorePlugin.getWorkspaceRoot();

  /**
   * The default value of the '{@link #isRefresh() <em>Refresh</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRefresh()
   * @generated
   * @ordered
   */
  protected static final boolean REFRESH_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isRefresh() <em>Refresh</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRefresh()
   * @generated
   * @ordered
   */
  protected boolean refresh = REFRESH_EDEFAULT;

  /**
   * The default value of the '{@link #isClean() <em>Clean</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isClean()
   * @generated
   * @ordered
   */
  protected static final boolean CLEAN_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isClean() <em>Clean</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isClean()
   * @generated
   * @ordered
   */
  protected boolean clean = CLEAN_EDEFAULT;

  /**
   * The default value of the '{@link #isBuild() <em>Build</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isBuild()
   * @generated
   * @ordered
   */
  protected static final boolean BUILD_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isBuild() <em>Build</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isBuild()
   * @generated
   * @ordered
   */
  protected boolean build = BUILD_EDEFAULT;

  private transient Set<IProject> existingProjects;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectsBuildTaskImpl()
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
    return ProjectsPackage.Literals.PROJECTS_BUILD_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Predicate> getPredicates()
  {
    if (predicates == null)
    {
      predicates = new EObjectContainmentEList<Predicate>(Predicate.class, this, ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES);
    }
    return predicates;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOnlyNewProjects()
  {
    return onlyNewProjects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnlyNewProjects(boolean newOnlyNewProjects)
  {
    boolean oldOnlyNewProjects = onlyNewProjects;
    onlyNewProjects = newOnlyNewProjects;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS, oldOnlyNewProjects, onlyNewProjects));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isRefresh()
  {
    return refresh;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRefresh(boolean newRefresh)
  {
    boolean oldRefresh = refresh;
    refresh = newRefresh;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH, oldRefresh, refresh));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isClean()
  {
    return clean;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setClean(boolean newClean)
  {
    boolean oldClean = clean;
    clean = newClean;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN, oldClean, clean));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isBuild()
  {
    return build;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBuild(boolean newBuild)
  {
    boolean oldBuild = build;
    build = newBuild;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectsPackage.PROJECTS_BUILD_TASK__BUILD, oldBuild, build));
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
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        return ((InternalEList<?>)getPredicates()).basicRemove(otherEnd, msgs);
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
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        return getPredicates();
      case ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS:
        return isOnlyNewProjects();
      case ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH:
        return isRefresh();
      case ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN:
        return isClean();
      case ProjectsPackage.PROJECTS_BUILD_TASK__BUILD:
        return isBuild();
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
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        getPredicates().clear();
        getPredicates().addAll((Collection<? extends Predicate>)newValue);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS:
        setOnlyNewProjects((Boolean)newValue);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH:
        setRefresh((Boolean)newValue);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN:
        setClean((Boolean)newValue);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__BUILD:
        setBuild((Boolean)newValue);
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
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        getPredicates().clear();
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS:
        setOnlyNewProjects(ONLY_NEW_PROJECTS_EDEFAULT);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH:
        setRefresh(REFRESH_EDEFAULT);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN:
        setClean(CLEAN_EDEFAULT);
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__BUILD:
        setBuild(BUILD_EDEFAULT);
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
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        return predicates != null && !predicates.isEmpty();
      case ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS:
        return onlyNewProjects != ONLY_NEW_PROJECTS_EDEFAULT;
      case ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH:
        return refresh != REFRESH_EDEFAULT;
      case ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN:
        return clean != CLEAN_EDEFAULT;
      case ProjectsPackage.PROJECTS_BUILD_TASK__BUILD:
        return build != BUILD_EDEFAULT;
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
    result.append(" (onlyNewProjects: ");
    result.append(onlyNewProjects);
    result.append(", refresh: ");
    result.append(refresh);
    result.append(", clean: ");
    result.append(clean);
    result.append(", build: ");
    result.append(build);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (!(isRefresh() || isClean() || isBuild()))
    {
      return false;
    }

    if (isOnlyNewProjects())
    {
      existingProjects = getProjects();
    }
    else
    {
      existingProjects = null;
    }

    // Other tasks can create/import new projects in their perform() method, so we defer all build work to ourperform() method.
    return true;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    Set<IProject> projects = getProjects();
    if (existingProjects != null)
    {
      projects.removeAll(existingProjects);
    }

    EList<Predicate> predicates = getPredicates();
    if (!predicates.isEmpty())
    {
      for (Iterator<IProject> it = projects.iterator(); it.hasNext();)
      {
        IProject project = it.next();
        if (!PredicatesUtil.matchesPredicates(project, predicates))
        {
          it.remove();
        }
      }
    }

    int size = projects.size();
    if (size == 0)
    {
      logNothingToDo(context);
      return;
    }

    IProgressMonitor monitor = context.getProgressMonitor(true);
    monitor.beginTask("", (isRefresh() ? size : 0) + (isClean() ? size : 0) + (isBuild() ? size : 0));

    try
    {
      if (isRefresh())
      {
        for (IProject project : projects)
        {
          context.log("Refreshing " + project.getName(), false);
          project.refreshLocal(IResource.DEPTH_INFINITE, MonitorUtil.create(monitor, 1));
        }
      }

      IBuildConfiguration[] buildsConfigs = null;

      if (isClean())
      {
        buildsConfigs = getBuildConfigs(projects);
        ROOT.getWorkspace().build(buildsConfigs, IncrementalProjectBuilder.CLEAN_BUILD, false, MonitorUtil.create(monitor, size));
      }

      if (isBuild())
      {
        if (buildsConfigs == null)
        {
          buildsConfigs = getBuildConfigs(projects);
        }

        ROOT.getWorkspace().build(buildsConfigs, IncrementalProjectBuilder.FULL_BUILD, false, MonitorUtil.create(monitor, size));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  private Set<IProject> getProjects()
  {
    Set<IProject> projects = new HashSet<IProject>();
    for (IProject project : ROOT.getProjects())
    {
      projects.add(project);
    }

    return projects;
  }

  private IBuildConfiguration[] getBuildConfigs(Set<IProject> projects) throws CoreException
  {
    IBuildConfiguration[] buildConfigs = new IBuildConfiguration[projects.size()];
    int i = 0;

    for (IProject project : projects)
    {
      buildConfigs[i++] = project.getBuildConfig(IBuildConfiguration.DEFAULT_CONFIG_NAME);
    }

    return buildConfigs;
  }

  private void logNothingToDo(SetupTaskContext context)
  {
    List<String> tokens = new ArrayList<String>();

    if (isRefresh())
    {
      tokens.add(" refresh");
    }

    if (isClean())
    {
      tokens.add(" clean");
    }

    if (isBuild())
    {
      tokens.add(" build");
    }

    context.log("Nothing to" + StringUtil.implode(tokens, ','));
  }

} // ProjectsBuildTaskImpl

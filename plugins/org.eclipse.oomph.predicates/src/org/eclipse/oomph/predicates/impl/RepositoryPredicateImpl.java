/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.predicates.RepositoryPredicate;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.egit.core.GitProvider;
import org.eclipse.egit.core.project.GitProjectData;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.team.core.RepositoryProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repository Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl#getProject <em>Project</em>}</li>
 *   <li>{@link org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl#getRelativePathPattern <em>Relative Path Pattern</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RepositoryPredicateImpl extends PredicateImpl implements RepositoryPredicate
{
  /**
   * The default value of the '{@link #getProject() <em>Project</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProject()
   * @generated
   * @ordered
   */
  protected static final IProject PROJECT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProject() <em>Project</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProject()
   * @generated
   * @ordered
   */
  protected IProject project = PROJECT_EDEFAULT;

  /**
   * The default value of the '{@link #getRelativePathPattern() <em>Relative Path Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativePathPattern()
   * @generated
   * @ordered
   */
  protected static final String RELATIVE_PATH_PATTERN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRelativePathPattern() <em>Relative Path Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativePathPattern()
   * @generated
   * @ordered
   */
  protected String relativePathPattern = RELATIVE_PATH_PATTERN_EDEFAULT;

  private Pattern compiledPattern;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RepositoryPredicateImpl()
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
    return PredicatesPackage.Literals.REPOSITORY_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IProject getProject()
  {
    return project;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProject(IProject newProject)
  {
    IProject oldProject = project;
    project = newProject;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.REPOSITORY_PREDICATE__PROJECT, oldProject, project));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRelativePathPattern()
  {
    return relativePathPattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRelativePathPatternGen(String newRelativePathPattern)
  {
    String oldRelativePathPattern = relativePathPattern;
    relativePathPattern = newRelativePathPattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN, oldRelativePathPattern,
          relativePathPattern));
    }
  }

  public void setRelativePathPattern(String newRelativePathPattern)
  {
    setRelativePathPatternGen(newRelativePathPattern);
    compiledPattern = null;
  }

  private Pattern getCompiledPattern()
  {
    if (compiledPattern == null)
    {
      compiledPattern = getPattern(getRelativePathPattern());
    }

    return compiledPattern;
  }

  private String getRepoDirAbsolutePath(IProject project)
  {
    if (project != null)
    {
      RepositoryProvider provider = RepositoryProvider.getProvider(project);
      if (provider == null)
      {
        URI locationURI = project.getLocationURI();
        if (locationURI != null && "file".equals(locationURI.getScheme())) //$NON-NLS-1$
        {
          org.eclipse.emf.common.util.URI emfURI = org.eclipse.emf.common.util.URI.createURI(locationURI.toString());
          for (File parent = new File(emfURI.toFileString()).getParentFile(); parent != null && parent.isDirectory(); parent = parent.getParentFile())
          {
            File gitFolder = new File(parent, ".git"); //$NON-NLS-1$
            if (new File(gitFolder, "index").exists()) //$NON-NLS-1$
            {
              return parent.toString();
            }
          }
        }
      }
      else
      {
        try
        {
          if (provider instanceof GitProvider)
          {
            GitProvider gitProvider = (GitProvider)provider;
            GitProjectData data = gitProvider.getData();
            RepositoryMapping repositoryMapping = data.getRepositoryMapping(project);
            File workTree = repositoryMapping.getWorkTree();
            return workTree == null ? null : workTree.toString();
          }
        }
        catch (NoClassDefFoundError ex)
        {
          // Ignore
        }

        try
        {
          // http://fossies.org/linux/privat/subclipse-1.6.18.tar.gz:a/subclipse-1.6.18/org.tigris.subversion.subclipse.core/src/org/tigris/subversion/subclipse/core/SVNTeamProvider.java
          Class<? extends RepositoryProvider> providerClass = provider.getClass();
          Method getSVNWorkspaceRootMethod = providerClass.getMethod("getSVNWorkspaceRoot"); //$NON-NLS-1$
          Object svnWorkspaceRoot = getSVNWorkspaceRootMethod.invoke(provider);
          Class<? extends Object> workspaceRootClass = svnWorkspaceRoot.getClass();
          Method getRepositoryMethod = workspaceRootClass.getMethod("getRepository"); //$NON-NLS-1$
          Object repositoryLocation = getRepositoryMethod.invoke(svnWorkspaceRoot);
          Class<? extends Object> repositoryLocationClass = repositoryLocation.getClass();
          Method getLocationMethod = repositoryLocationClass.getMethod("getLocation"); //$NON-NLS-1$
          Object location = getLocationMethod.invoke(repositoryLocation);
          return location == null ? null : location.toString();
        }
        catch (Throwable throwable)
        {
          // Ignore
        }

        try
        {
          // http://dev.eclipse.org/svnroot/technology/org.eclipse.subversive/trunk/org.eclipse.team.svn.core/src/org/eclipse/team/svn/core/SVNTeamProvider.java
          Class<? extends RepositoryProvider> providerClass = provider.getClass();
          Method getRepositoryLocationMethod = providerClass.getMethod("getRepositoryLocation"); //$NON-NLS-1$
          Object repositoryLocation = getRepositoryLocationMethod.invoke(provider);
          Class<? extends Object> repositoryLocationClass = repositoryLocation.getClass();
          Method getRepositoryRootUrlMethod = repositoryLocationClass.getMethod("getRepositoryRootUrl"); //$NON-NLS-1$
          Object respositoryRootURL = getRepositoryRootUrlMethod.invoke(repositoryLocation);
          return respositoryRootURL == null ? null : respositoryRootURL.toString();
        }
        catch (Throwable throwable)
        {
          // Ignore
        }

        return "Unknown repo"; //$NON-NLS-1$
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    if (resource == null)
    {
      return false;
    }

    String prototypeRepoDirAbsolutePath = getRepoDirAbsolutePath(getProject());
    String repoDirAbsolutePath = getRepoDirAbsolutePath(resource.getProject());

    if (!Objects.equals(prototypeRepoDirAbsolutePath, repoDirAbsolutePath))
    {
      return false;
    }

    if (repoDirAbsolutePath != null && getRelativePathPattern() != null)
    {
      IPath location = resource.getLocation();
      if (location != null)
      {
        String projectLocation = location.toPortableString();
        repoDirAbsolutePath = repoDirAbsolutePath.replace(File.separatorChar, '/');

        String relativePath = StringUtil.removePrefix(projectLocation, repoDirAbsolutePath);
        relativePath = StringUtil.removePrefix(relativePath, "/"); //$NON-NLS-1$

        if (getCompiledPattern().matcher(relativePath).matches())
        {
          return true;
        }
      }

      return false;
    }

    return true;
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
      case PredicatesPackage.REPOSITORY_PREDICATE__PROJECT:
        return getProject();
      case PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN:
        return getRelativePathPattern();
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
      case PredicatesPackage.REPOSITORY_PREDICATE__PROJECT:
        setProject((IProject)newValue);
        return;
      case PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN:
        setRelativePathPattern((String)newValue);
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
      case PredicatesPackage.REPOSITORY_PREDICATE__PROJECT:
        setProject(PROJECT_EDEFAULT);
        return;
      case PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN:
        setRelativePathPattern(RELATIVE_PATH_PATTERN_EDEFAULT);
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
      case PredicatesPackage.REPOSITORY_PREDICATE__PROJECT:
        return PROJECT_EDEFAULT == null ? project != null : !PROJECT_EDEFAULT.equals(project);
      case PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN:
        return RELATIVE_PATH_PATTERN_EDEFAULT == null ? relativePathPattern != null : !RELATIVE_PATH_PATTERN_EDEFAULT.equals(relativePathPattern);
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
    result.append(" (project: "); //$NON-NLS-1$
    result.append(project);
    result.append(", relativePathPattern: "); //$NON-NLS-1$
    result.append(relativePathPattern);
    result.append(')');
    return result.toString();
  }

} // RepositoryPredicateImpl

/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesUtil;
import org.eclipse.oomph.resources.ProjectFactory;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendException;
import org.eclipse.oomph.resources.backend.BackendResource;
import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Source Locator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getRootFolder <em>Root Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getExcludedPaths <em>Excluded Paths</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getProjectFactories <em>Project Factories</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link org.eclipse.oomph.resources.impl.SourceLocatorImpl#isLocateNestedProjects <em>Locate Nested Projects</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SourceLocatorImpl extends ModelElementImpl implements SourceLocator
{
  /**
   * The default value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected static final String ROOT_FOLDER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected String rootFolder = ROOT_FOLDER_EDEFAULT;

  /**
   * The cached value of the '{@link #getExcludedPaths() <em>Excluded Paths</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExcludedPaths()
   * @generated
   * @ordered
   */
  protected EList<String> excludedPaths;

  /**
   * The cached value of the '{@link #getProjectFactories() <em>Project Factories</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectFactories()
   * @generated
   * @ordered
   */
  protected EList<ProjectFactory> projectFactories;

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
   * The default value of the '{@link #isLocateNestedProjects() <em>Locate Nested Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLocateNestedProjects()
   * @generated
   * @ordered
   */
  protected static final boolean LOCATE_NESTED_PROJECTS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isLocateNestedProjects() <em>Locate Nested Projects</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLocateNestedProjects()
   * @generated
   * @ordered
   */
  protected boolean locateNestedProjects = LOCATE_NESTED_PROJECTS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SourceLocatorImpl()
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
    return ResourcesPackage.Literals.SOURCE_LOCATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRootFolder()
  {
    return rootFolder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRootFolder(String newRootFolder)
  {
    String oldRootFolder = rootFolder;
    rootFolder = newRootFolder;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER, oldRootFolder, rootFolder));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getExcludedPaths()
  {
    if (excludedPaths == null)
    {
      excludedPaths = new EDataTypeUniqueEList<String>(String.class, this, ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS);
    }
    return excludedPaths;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ProjectFactory> getProjectFactories()
  {
    if (projectFactories == null)
    {
      projectFactories = new EObjectContainmentEList<ProjectFactory>(ProjectFactory.class, this, ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES);
    }
    return projectFactories;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isLocateNestedProjects()
  {
    return locateNestedProjects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocateNestedProjects(boolean newLocateNestedProjects)
  {
    boolean oldLocateNestedProjects = locateNestedProjects;
    locateNestedProjects = newLocateNestedProjects;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS, oldLocateNestedProjects,
          locateNestedProjects));
    }
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
      predicates = new EObjectContainmentEList<Predicate>(Predicate.class, this, ResourcesPackage.SOURCE_LOCATOR__PREDICATES);
    }
    return predicates;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean matches(IProject project)
  {
    return PredicatesUtil.matchesPredicates(project, predicates);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IProject loadProject(EList<ProjectFactory> defaultProjectFactories, BackendContainer backendContainer, IProgressMonitor monitor)
  {
    BackendContainer rootContainer = SourceLocatorImpl.getRootContainer(this);
    if (rootContainer == null)
    {
      monitor.setTaskName("Skipping root folder '" + getRootFolder() + "' because it doesn't exist");
      return null;
    }

    return loadProject(this, defaultProjectFactories, rootContainer, backendContainer, monitor);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void handleProjects(EList<ProjectFactory> defaultProjectFactories, ProjectHandler projectHandler, MultiStatus status, IProgressMonitor monitor)
  {
    handleProjects(this, defaultProjectFactories, projectHandler, status, monitor);
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
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
        return ((InternalEList<?>)getProjectFactories()).basicRemove(otherEnd, msgs);
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        return getRootFolder();
      case ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS:
        return getExcludedPaths();
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
        return getProjectFactories();
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        return getPredicates();
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        return isLocateNestedProjects();
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        setRootFolder((String)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS:
        getExcludedPaths().clear();
        getExcludedPaths().addAll((Collection<? extends String>)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
        getProjectFactories().clear();
        getProjectFactories().addAll((Collection<? extends ProjectFactory>)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        getPredicates().clear();
        getPredicates().addAll((Collection<? extends Predicate>)newValue);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        setLocateNestedProjects((Boolean)newValue);
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        setRootFolder(ROOT_FOLDER_EDEFAULT);
        return;
      case ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS:
        getExcludedPaths().clear();
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
        getProjectFactories().clear();
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        getPredicates().clear();
        return;
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        setLocateNestedProjects(LOCATE_NESTED_PROJECTS_EDEFAULT);
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
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
        return ROOT_FOLDER_EDEFAULT == null ? rootFolder != null : !ROOT_FOLDER_EDEFAULT.equals(rootFolder);
      case ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS:
        return excludedPaths != null && !excludedPaths.isEmpty();
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
        return projectFactories != null && !projectFactories.isEmpty();
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
        return predicates != null && !predicates.isEmpty();
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        return locateNestedProjects != LOCATE_NESTED_PROJECTS_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ResourcesPackage.SOURCE_LOCATOR___MATCHES__IPROJECT:
        return matches((IProject)arguments.get(0));
      case ResourcesPackage.SOURCE_LOCATOR___LOAD_PROJECT__ELIST_BACKENDCONTAINER_IPROGRESSMONITOR:
        return loadProject((EList<ProjectFactory>)arguments.get(0), (BackendContainer)arguments.get(1), (IProgressMonitor)arguments.get(2));
      case ResourcesPackage.SOURCE_LOCATOR___HANDLE_PROJECTS__ELIST_PROJECTHANDLER_MULTISTATUS_IPROGRESSMONITOR:
        handleProjects((EList<ProjectFactory>)arguments.get(0), (ProjectHandler)arguments.get(1), (MultiStatus)arguments.get(2),
            (IProgressMonitor)arguments.get(3));
        return null;
    }
    return super.eInvoke(operationID, arguments);
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
    result.append(" (rootFolder: ");
    result.append(rootFolder);
    result.append(", excludedPaths: ");
    result.append(excludedPaths);
    result.append(", locateNestedProjects: ");
    result.append(locateNestedProjects);
    result.append(')');
    return result.toString();
  }

  public static void addStatus(MultiStatus status, OomphPlugin plugin, String file, Object object)
  {
    IStatus childStatus = plugin.getStatus(object);
    String message = childStatus.getMessage() + " (while processing " + file + ")";

    MultiStatus multiStatus = new MultiStatus(childStatus.getPlugin(), childStatus.getCode(), message, childStatus.getException());
    multiStatus.addAll(childStatus);
    status.add(multiStatus);
  }

  public static BackendContainer getRootContainer(SourceLocator sourceLocator)
  {
    String rootFolder = sourceLocator.getRootFolder();
    if (rootFolder != null)
    {
      BackendResource rootResource = BackendResource.get(rootFolder);
      if (rootResource instanceof BackendContainer)
      {
        return (BackendContainer)rootResource;
      }
    }

    return null;
  }

  public static IProject loadProject(SourceLocator sourceLocator, EList<ProjectFactory> defaultProjectFactories, BackendContainer rootContainer,
      BackendContainer backendContainer, IProgressMonitor monitor)
  {
    try
    {
      EList<ProjectFactory> effectiveProjectFactories = sourceLocator.getProjectFactories();
      if (effectiveProjectFactories.isEmpty() && defaultProjectFactories != null)
      {
        effectiveProjectFactories = defaultProjectFactories;
      }

      for (ProjectFactory projectFactory : effectiveProjectFactories)
      {
        IProject project = projectFactory.createProject(rootContainer, backendContainer, monitor);
        if (project != null)
        {
          return project;
        }
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }

    return null;
  }

  public static void handleProjects(final SourceLocator sourceLocator, final EList<ProjectFactory> defaultProjectFactories, final ProjectHandler projectHandler,
      final MultiStatus status, final IProgressMonitor monitor)
  {
    String rootFolder = sourceLocator.getRootFolder();
    if ("platform:/resource/".equals(rootFolder))
    {
      for (IProject project : EcorePlugin.getWorkspaceRoot().getProjects())
      {
        if (project.isAccessible() && !"External Plug-in Libraries".equals(project.getName()))
        {
          URI uri = CommonPlugin.resolve(URI.createPlatformResourceURI(project.getFullPath().toString(), true));
          if (uri.isFile())
          {
            SourceLocator projectSourceLocator = EcoreUtil.copy(sourceLocator);
            projectSourceLocator.setLocateNestedProjects(false);
            projectSourceLocator.setRootFolder(uri.toFileString());
            handleProjects(projectSourceLocator, defaultProjectFactories, projectHandler, status, monitor);
          }
        }
      }
    }
    else if (rootFolder != null && rootFolder.startsWith("platform:/resource/"))
    {
      URI uri = CommonPlugin.resolve(URI.createURI(rootFolder));
      if (uri.isFile())
      {
        SourceLocator resourceSourceLocator = EcoreUtil.copy(sourceLocator);
        resourceSourceLocator.setLocateNestedProjects(false);
        resourceSourceLocator.setRootFolder(uri.toFileString());
        handleProjects(resourceSourceLocator, defaultProjectFactories, projectHandler, status, monitor);
      }
    }
    else
    {
      doHandleProjects(sourceLocator, defaultProjectFactories, projectHandler, status, monitor);
    }
  }

  private static void doHandleProjects(final SourceLocator sourceLocator, final EList<ProjectFactory> defaultProjectFactories,
      final ProjectHandler projectHandler, final MultiStatus status, final IProgressMonitor monitor)
  {
    final BackendContainer rootContainer = SourceLocatorImpl.getRootContainer(sourceLocator);
    if (rootContainer == null)
    {
      monitor.setTaskName("Skipping root folder '" + sourceLocator.getRootFolder() + "' because it doesn't exist");
      return;
    }

    final Set<URI> excludedURIs = new HashSet<URI>();
    for (String path : sourceLocator.getExcludedPaths())
    {
      while (path.startsWith("/"))
      {
        path = path.substring(1);
      }

      excludedURIs.add(URI.createURI(path));
    }

    rootContainer.accept(new BackendResource.Visitor.Default()
    {
      @Override
      public boolean visitContainer(BackendContainer container, IProgressMonitor monitor) throws BackendException
      {
        ResourcesPlugin.checkCancelation(monitor);

        if (isExcludedPath(rootContainer, container))
        {
          return false;
        }

        IProject project = loadProject(sourceLocator, defaultProjectFactories, rootContainer, container, monitor);
        if (sourceLocator.matches(project))
        {
          try
          {
            projectHandler.handleProject(project, container);
          }
          catch (Exception ex)
          {
            SourceLocatorImpl.addStatus(status, ResourcesPlugin.INSTANCE, project.getName(), ex);
          }
        }

        if (project != null && !sourceLocator.isLocateNestedProjects())
        {
          return false;
        }

        return true;
      }

      private boolean isExcludedPath(BackendContainer rootContainer, BackendContainer backendContainer)
      {
        URI relativeURI = backendContainer.getRelativeURI(rootContainer);
        return relativeURI != null && excludedURIs.contains(relativeURI);
      }
    }, monitor);
  }

} // SourceLocatorImpl

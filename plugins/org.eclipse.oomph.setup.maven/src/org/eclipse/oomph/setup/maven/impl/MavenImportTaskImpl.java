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
package org.eclipse.oomph.setup.maven.impl;

import org.eclipse.oomph.resources.DynamicMavenProjectFactory;
import org.eclipse.oomph.resources.MavenProjectFactory;
import org.eclipse.oomph.resources.ProjectFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendResource;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.maven.MavenImportTask;
import org.eclipse.oomph.setup.maven.MavenPackage;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Maven Import Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl#getProjectNameTemplate <em>Project Name Template</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.maven.impl.MavenImportTaskImpl#getProfiles <em>Profiles</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MavenImportTaskImpl extends SetupTaskImpl implements MavenImportTask
{
  private static final IWorkspaceRoot ROOT = ResourcesPlugin.getWorkspace().getRoot();

  /**
   * The cached value of the '{@link #getSourceLocators() <em>Source Locators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceLocators()
   * @generated
   * @ordered
   */
  protected EList<SourceLocator> sourceLocators;

  /**
   * The default value of the '{@link #getProjectNameTemplate() <em>Project Name Template</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectNameTemplate()
   * @generated
   * @ordered
   */
  protected static final String PROJECT_NAME_TEMPLATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProjectNameTemplate() <em>Project Name Template</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectNameTemplate()
   * @generated
   * @ordered
   */
  protected String projectNameTemplate = PROJECT_NAME_TEMPLATE_EDEFAULT;

  /**
   * The cached value of the '{@link #getProfiles() <em>Profiles</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfiles()
   * @generated
   * @ordered
   */
  protected EList<String> profiles;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MavenImportTaskImpl()
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
    return MavenPackage.Literals.MAVEN_IMPORT_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<SourceLocator> getSourceLocators()
  {
    if (sourceLocators == null)
    {
      sourceLocators = new EObjectContainmentEList<>(SourceLocator.class, this, MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS);
    }
    return sourceLocators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getProjectNameTemplate()
  {
    return projectNameTemplate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setProjectNameTemplate(String newProjectNameTemplate)
  {
    String oldProjectNameTemplate = projectNameTemplate;
    projectNameTemplate = newProjectNameTemplate;
    if (eNotificationRequired())
    {
      eNotify(
          new ENotificationImpl(this, Notification.SET, MavenPackage.MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE, oldProjectNameTemplate, projectNameTemplate));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<String> getProfiles()
  {
    if (profiles == null)
    {
      profiles = new EDataTypeUniqueEList<>(String.class, this, MavenPackage.MAVEN_IMPORT_TASK__PROFILES);
    }
    return profiles;
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
      case MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS:
        return ((InternalEList<?>)getSourceLocators()).basicRemove(otherEnd, msgs);
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
      case MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS:
        return getSourceLocators();
      case MavenPackage.MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE:
        return getProjectNameTemplate();
      case MavenPackage.MAVEN_IMPORT_TASK__PROFILES:
        return getProfiles();
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
      case MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS:
        getSourceLocators().clear();
        getSourceLocators().addAll((Collection<? extends SourceLocator>)newValue);
        return;
      case MavenPackage.MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE:
        setProjectNameTemplate((String)newValue);
        return;
      case MavenPackage.MAVEN_IMPORT_TASK__PROFILES:
        getProfiles().clear();
        getProfiles().addAll((Collection<? extends String>)newValue);
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
      case MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS:
        getSourceLocators().clear();
        return;
      case MavenPackage.MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE:
        setProjectNameTemplate(PROJECT_NAME_TEMPLATE_EDEFAULT);
        return;
      case MavenPackage.MAVEN_IMPORT_TASK__PROFILES:
        getProfiles().clear();
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
      case MavenPackage.MAVEN_IMPORT_TASK__SOURCE_LOCATORS:
        return sourceLocators != null && !sourceLocators.isEmpty();
      case MavenPackage.MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE:
        return PROJECT_NAME_TEMPLATE_EDEFAULT == null ? projectNameTemplate != null : !PROJECT_NAME_TEMPLATE_EDEFAULT.equals(projectNameTemplate);
      case MavenPackage.MAVEN_IMPORT_TASK__PROFILES:
        return profiles != null && !profiles.isEmpty();
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
    result.append(" (projectNameTemplate: "); //$NON-NLS-1$
    result.append(projectNameTemplate);
    result.append(", profiles: "); //$NON-NLS-1$
    result.append(profiles);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    EList<SourceLocator> sourceLocators = getSourceLocators();
    if (sourceLocators.isEmpty())
    {
      return false;
    }

    if (context.getTrigger() != Trigger.MANUAL)
    {
      for (IProject project : ROOT.getProjects())
      {
        IPath projectFolder = project.getLocation();
        for (SourceLocator sourceLocator : sourceLocators)
        {
          Path rootFolder = new Path(sourceLocator.getRootFolder());
          if (rootFolder.isPrefixOf(projectFolder))
          {
            // In STARTUP trigger don't perform if there's already at least 1 project from the source locators
            return false;
          }
        }
      }
    }

    return true;
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    EList<SourceLocator> sourceLocators = getSourceLocators();
    int size = sourceLocators.size();

    IProgressMonitor monitor = context.getProgressMonitor(true);
    monitor.beginTask("", 2 * size); //$NON-NLS-1$

    try
    {
      MavenModelManager modelManager = MavenPlugin.getMavenModelManager();

      Set<MavenProjectInfo> projectInfos = new LinkedHashSet<>();
      for (SourceLocator sourceLocator : sourceLocators)
      {
        LocalProjectScanner projectScanner = createLocalProjectScanner(Collections.singletonList(sourceLocator.getRootFolder()), modelManager);
        processMavenProject(sourceLocator, projectInfos, projectScanner, MonitorUtil.create(monitor, 1));
      }

      if (projectInfos.isEmpty())
      {
        monitor.worked(size);
      }
      else
      {
        IProjectConfigurationManager projectConfigurationManager = MavenPlugin.getProjectConfigurationManager();
        ProjectImportConfiguration projectImportConfiguration = new ProjectImportConfiguration();

        String projectNameTemplate = getProjectNameTemplate();
        if (!StringUtil.isEmpty(projectNameTemplate))
        {
          projectImportConfiguration.setProjectNameTemplate(projectNameTemplate);
        }

        EList<String> profiles = getProfiles();
        if (!profiles.isEmpty())
        {
          for (MavenProjectInfo mavenProjectInfo : projectInfos)
          {
            mavenProjectInfo.addProfiles(profiles);
          }
        }

        projectConfigurationManager.importProjects(projectInfos, projectImportConfiguration, MonitorUtil.create(monitor, size));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  private LocalProjectScanner createLocalProjectScanner(List<String> folders, MavenModelManager modelManager)
  {
    try
    {
      Constructor<LocalProjectScanner> constructor = ReflectUtil.getConstructor(LocalProjectScanner.class, File.class, List.class, boolean.class,
          MavenModelManager.class);
      return constructor.newInstance(null, folders, false, modelManager);
    }
    catch (Exception ex)
    {
      return new LocalProjectScanner(folders, false, modelManager);
    }
  }

  private void processMavenProject(SourceLocator sourceLocator, Set<MavenProjectInfo> projectInfos, LocalProjectScanner projectScanner,
      IProgressMonitor monitor) throws InterruptedException
  {
    monitor.beginTask("", 2); //$NON-NLS-1$

    try
    {
      projectScanner.run(MonitorUtil.create(monitor, 1));

      List<MavenProjectInfo> projects = projectScanner.getProjects();
      processMavenProject(sourceLocator, projectInfos, projects, MonitorUtil.create(monitor, 1));
    }
    finally
    {
      monitor.done();
    }
  }

  private void processMavenProject(SourceLocator sourceLocator, Set<MavenProjectInfo> projectInfos, List<MavenProjectInfo> projects, IProgressMonitor monitor)
  {
    monitor.beginTask("", projects.size()); //$NON-NLS-1$

    try
    {
      for (MavenProjectInfo projectInfo : projects)
      {
        processMavenProject(sourceLocator, projectInfos, projectInfo, MonitorUtil.create(monitor, 1));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  private static void processMavenProject(SourceLocator sourceLocator, Set<MavenProjectInfo> projectInfos, MavenProjectInfo projectInfo,
      IProgressMonitor monitor)
  {
    monitor.beginTask("", 6); //$NON-NLS-1$

    try
    {
      File pomFile = projectInfo.getPomFile();
      String projectFolder = pomFile.getParent();
      BackendContainer backendContainer = (BackendContainer)BackendResource.get(projectFolder);
      if (isExcluded(sourceLocator, backendContainer))
      {
        return;
      }

      EList<ProjectFactory> factories = new BasicEList<>(MavenProjectFactory.LIST);
      String pomFileName = pomFile.getName();
      if (!pomFileName.equals(MavenProjectFactory.POM_XML))
      {
        // Check for alternate pom.xml file names (e.g., .polyglot.pom.tycho)
        DynamicMavenProjectFactory altFactory = ResourcesFactory.eINSTANCE.createDynamicMavenProjectFactory();
        altFactory.setXMLFileName(pomFileName);
        altFactory.getExcludedPaths().addAll(factories.get(0).getExcludedPaths());
        factories.add(altFactory);
      }

      IProject project = sourceLocator.loadProject(factories, backendContainer, MonitorUtil.create(monitor, 1));

      if (project != null)
      {
        if (sourceLocator.matches(project))
        {
          String projectName = project.getName();
          if (!ROOT.getProject(projectName).exists())
          {
            projectInfos.add(projectInfo);
          }
        }
      }

      if (sourceLocator.isLocateNestedProjects())
      {
        Collection<MavenProjectInfo> projects = projectInfo.getProjects();
        processMavenProject(sourceLocator, projectInfos, projects, MonitorUtil.create(monitor, 5));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  private static void processMavenProject(SourceLocator sourceLocator, Set<MavenProjectInfo> projectInfos, Collection<MavenProjectInfo> projects,
      IProgressMonitor monitor)
  {
    monitor.beginTask("", projects.size()); //$NON-NLS-1$

    try
    {
      for (MavenProjectInfo childProjectInfo : projects)
      {
        processMavenProject(sourceLocator, projectInfos, childProjectInfo, MonitorUtil.create(monitor, 1));
      }
    }
    finally
    {
      monitor.done();
    }
  }

  private static boolean isExcluded(SourceLocator sourceLocator, BackendContainer backendContainer)
  {
    EList<String> excludedPaths = sourceLocator.getExcludedPaths();
    if (excludedPaths.isEmpty())
    {
      return false;
    }

    String relativePath = backendContainer.getRelativeURI((BackendContainer)BackendResource.get(sourceLocator.getRootFolder())).toString();
    if (excludedPaths.contains(relativePath))
    {
      return true;
    }

    relativePath += "/"; //$NON-NLS-1$

    for (String excludedPath : excludedPaths)
    {
      if (excludedPath.startsWith(relativePath))
      {
        return true;
      }
    }

    return false;
  }

} // MavenImportTaskImpl

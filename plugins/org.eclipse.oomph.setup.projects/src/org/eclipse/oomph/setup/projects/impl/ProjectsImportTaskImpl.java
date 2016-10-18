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
package org.eclipse.oomph.setup.projects.impl;

import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.ResourcesUtil.ImportResult;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.impl.SourceLocatorImpl;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;
import org.eclipse.oomph.setup.projects.ProjectsPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.PropertyFile;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Import Project Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projects.impl.ProjectsImportTaskImpl#getSourceLocators <em>Source Locators</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectsImportTaskImpl extends SetupTaskImpl implements ProjectsImportTask
{
  private static final PropertyFile HISTORY = new PropertyFile(ProjectsPlugin.INSTANCE.getStateLocation().append("import-history.properties").toFile());

  private static final IWorkspaceRoot ROOT = EcorePlugin.getWorkspaceRoot();

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectsImportTaskImpl()
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
    return ProjectsPackage.Literals.PROJECTS_IMPORT_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<SourceLocator> getSourceLocators()
  {
    if (sourceLocators == null)
    {
      sourceLocators = new EObjectContainmentEList<SourceLocator>(SourceLocator.class, this, ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS);
    }
    return sourceLocators;
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
      case ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS:
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
      case ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS:
        return getSourceLocators();
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
      case ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS:
        getSourceLocators().clear();
        getSourceLocators().addAll((Collection<? extends SourceLocator>)newValue);
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
      case ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS:
        getSourceLocators().clear();
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
      case ProjectsPackage.PROJECTS_IMPORT_TASK__SOURCE_LOCATORS:
        return sourceLocators != null && !sourceLocators.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 50;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (context.getTrigger() == Trigger.MANUAL)
    {
      return true;
    }

    for (SourceLocator sourceLocator : getSourceLocators())
    {
      IProject[] projects = getProjects(sourceLocator);
      if (projects == null)
      {
        return true;
      }

      for (IProject project : projects)
      {
        if (!project.exists())
        {
          return true;
        }
      }
    }

    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    Map<BackendContainer, IProject> backendContainers = new HashMap<BackendContainer, IProject>();
    MultiStatus status = new MultiStatus(ProjectsPlugin.INSTANCE.getSymbolicName(), 0, "Projects Import Analysis", null);

    EList<SourceLocator> sourceLocators = getSourceLocators();
    int size = sourceLocators.size();

    IProgressMonitor monitor = context.getProgressMonitor(true);
    monitor.beginTask("", 2 * size);

    try
    {
      for (SourceLocator sourceLocator : sourceLocators)
      {
        String rootFolder = sourceLocator.getRootFolder();
        context.log("Importing projects from " + rootFolder);
        MultiStatus childStatus = new MultiStatus(ProjectsPlugin.INSTANCE.getSymbolicName(), 0, "Projects Import Analysis of '" + rootFolder + "'", null);

        try
        {
          ProjectHandler.Collector collector = new ProjectHandler.Collector();
          sourceLocator.handleProjects(EclipseProjectFactory.LIST, collector, childStatus, MonitorUtil.create(monitor, 1));
          if (childStatus.getSeverity() >= IStatus.ERROR)
          {
            status.add(childStatus);
          }
          else
          {
            Map<IProject, BackendContainer> projectMap = collector.getProjectMap();
            for (Map.Entry<IProject, BackendContainer> entry : projectMap.entrySet())
            {
              backendContainers.put(entry.getValue(), entry.getKey());
            }

            Set<IProject> projects = projectMap.keySet();
            if (projects.isEmpty())
            {
              context.log("No projects were found");
            }

            setProjects(sourceLocator, projects.toArray(new IProject[projectMap.size()]));
          }
        }
        catch (Exception ex)
        {
          SourceLocatorImpl.addStatus(status, ProjectsPlugin.INSTANCE, rootFolder, ex);
        }
      }

      importProjects(backendContainers, MonitorUtil.create(monitor, size));
    }
    finally
    {
      monitor.done();
    }

    ProjectsPlugin.INSTANCE.coreException(status);
  }

  private IProject[] getProjects(SourceLocator sourceLocator)
  {
    String key = getDigest(sourceLocator);
    String value = HISTORY.getProperty(key, null);
    if (value != null)
    {
      List<IProject> projects = new ArrayList<IProject>();
      for (String element : XMLTypeFactory.eINSTANCE.createNMTOKENS(value))
      {
        projects.add(ROOT.getProject(URI.decode(element)));
      }

      return projects.toArray(new IProject[projects.size()]);
    }

    return null;
  }

  private String getDigest(SourceLocator sourceLocator)
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try
    {
      EObjectOutputStream eObjectOutputStream = new BinaryResourceImpl.EObjectOutputStream(bytes, null);
      eObjectOutputStream.saveEObject((InternalEObject)sourceLocator, BinaryResourceImpl.EObjectOutputStream.Check.NOTHING);
      bytes.toByteArray();
      return XMLTypeFactory.eINSTANCE.convertBase64Binary(IOUtil.getSHA1(new ByteArrayInputStream(bytes.toByteArray())));
    }
    catch (IOException ex)
    {
      ProjectsPlugin.INSTANCE.log(ex);
    }
    catch (NoSuchAlgorithmException ex)
    {
      ProjectsPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  private void setProjects(SourceLocator sourceLocator, IProject[] projects)
  {
    String key = getDigest(sourceLocator);
    StringBuilder value = new StringBuilder();
    for (IProject project : projects)
    {
      if (value.length() != 0)
      {
        value.append(' ');
      }

      value.append(URI.encodeSegment(project.getName(), false));
    }

    HISTORY.setProperty(key, value.toString());
  }

  private static int importProjects(final Map<BackendContainer, IProject> backendContainers, IProgressMonitor monitor) throws CoreException
  {
    if (backendContainers.isEmpty())
    {
      return 0;
    }

    final AtomicInteger count = new AtomicInteger();

    IWorkspace workspace = org.eclipse.core.resources.ResourcesPlugin.getWorkspace();
    workspace.run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        SubMonitor progress = SubMonitor.convert(monitor, backendContainers.size()).detectCancelation();

        try
        {
          for (Map.Entry<BackendContainer, IProject> entry : backendContainers.entrySet())
          {
            BackendContainer backendContainer = entry.getKey();
            IProject project = entry.getValue();
            if (backendContainer.importIntoWorkspace(project, progress.newChild()) == ImportResult.IMPORTED)
            {
              count.incrementAndGet();
            }
          }
        }
        catch (Exception ex)
        {
          ResourcesPlugin.INSTANCE.coreException(ex);
        }
        finally
        {
          progress.done();
        }
      }
    }, monitor);

    return count.get();
  }

} // ProjectsImportTaskImpl

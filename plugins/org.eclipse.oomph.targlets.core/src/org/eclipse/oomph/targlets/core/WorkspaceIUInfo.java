/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendResource;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public final class WorkspaceIUInfo
{
  private URI backendContainerURI;

  private String projectName;

  public WorkspaceIUInfo(BackendContainer backendContainer, String projectName)
  {
    backendContainerURI = backendContainer.getAbsoluteURI();
    this.projectName = projectName;
  }

  public WorkspaceIUInfo(EObjectInputStream stream) throws IOException
  {
    backendContainerURI = stream.readURI();
    projectName = stream.readString();
  }

  public void write(EObjectOutputStream stream) throws IOException
  {
    stream.writeURI(backendContainerURI);
    stream.writeString(projectName);
  }

  public BackendContainer getBackendContainer()
  {
    return (BackendContainer)BackendResource.get(backendContainerURI);
  }

  public String getProjectName()
  {
    return projectName;
  }

  public File getLocation()
  {
    BackendContainer backendContainer = getBackendContainer();
    if (backendContainer.isLocal())
    {
      return new File(backendContainer.getLocation().toString());
    }

    String workspaceFolder = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    String projectFolderName = backendContainer.getName();

    return new File(workspaceFolder, projectFolderName);
  }

  public ResourcesUtil.ImportResult importIntoWorkspace(IProgressMonitor monitor)
  {
    try
    {
      File location = getLocation();

      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot root = workspace.getRoot();

      IProject project = root.getProject(projectName);
      if (project.exists())
      {
        File existingLocation = new File(project.getLocation().toOSString()).getCanonicalFile();
        if (!existingLocation.equals(location))
        {
          TargletsCorePlugin.INSTANCE.log("Project " + projectName + " exists in different location: " + existingLocation);
          return ResourcesUtil.ImportResult.EXISTED_DIFFERENT_LOCATION;
        }

        return ResourcesUtil.ImportResult.EXISTED;
      }

      monitor.setTaskName("Importing project " + projectName);

      BackendContainer backendContainer = getBackendContainer();
      backendContainer.importIntoWorkspace(project, monitor);

      if (!project.isOpen())
      {
        project.open(monitor);
      }

      return ResourcesUtil.ImportResult.IMPORTED;
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      monitor.subTask("Failed to import project from " + this + " (see error log for details)");
      return ResourcesUtil.ImportResult.ERROR;
    }
  }

  @Override
  public int hashCode()
  {
    return backendContainerURI.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    WorkspaceIUInfo other = (WorkspaceIUInfo)obj;
    return backendContainerURI == other.backendContainerURI;
  }

  @Override
  public String toString()
  {
    return backendContainerURI + "[" + projectName + "]";
  }
}

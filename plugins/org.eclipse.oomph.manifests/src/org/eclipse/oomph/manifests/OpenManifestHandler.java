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
package org.eclipse.oomph.manifests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author Eike Stepper
 */
public class OpenManifestHandler extends AbstractProjectHandler
{
  private static final ProjectType[] PROJECT_TYPES = {
      new ProjectType("org.eclipse.pde.PluginNature", "org.eclipse.pde.ui.manifestEditor", "META-INF/MANIFEST.MF"),
      new ProjectType("org.eclipse.pde.FeatureNature", "org.eclipse.pde.ui.featureEditor", "feature.xml"),
      new ProjectType("org.eclipse.pde.UpdateSiteNature", "org.eclipse.pde.ui.siteEditor", "site.xml") };

  public OpenManifestHandler()
  {
  }

  @Override
  protected void execute(IWorkbenchPage page, IProject project)
  {
    ProjectType projectType = getProjectType(project);
    if (projectType == null)
    {
      return;
    }

    IFile manifest = project.getFile(new Path(projectType.getManifestPath()));
    if (manifest.exists())
    {
      try
      {
        IEditorInput input = new FileEditorInput(manifest);
        page.openEditor(input, projectType.getEditorID(), true);
      }
      catch (PartInitException ex)
      {
        Activator.getDefault().getLog().log(ex.getStatus());
      }
    }
  }

  private ProjectType getProjectType(IProject project)
  {
    for (ProjectType projectType : PROJECT_TYPES)
    {
      try
      {
        if (project.hasNature(projectType.getNatureID()))
        {
          return projectType;
        }
      }
      catch (CoreException ex)
      {
        Activator.getDefault().getLog().log(ex.getStatus());
      }
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  private static final class ProjectType
  {
    private String natureID;

    private String editorID;

    private String manifestPath;

    public ProjectType(String natureID, String editorID, String manifestPath)
    {
      this.natureID = natureID;
      this.editorID = editorID;
      this.manifestPath = manifestPath;
    }

    public String getNatureID()
    {
      return natureID;
    }

    public String getEditorID()
    {
      return editorID;
    }

    public String getManifestPath()
    {
      return manifestPath;
    }
  }
}

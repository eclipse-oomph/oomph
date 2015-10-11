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

import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
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
      new ProjectType("org.eclipse.pde.UpdateSiteNature", "org.eclipse.pde.ui.siteEditor", "site.xml"),
      new ProjectType(null, "org.eclipse.pde.ui.categoryEditor", "category.xml") };

  public OpenManifestHandler()
  {
  }

  @Override
  protected boolean executeWithProject(IWorkbenchPage page, IProject project)
  {
    for (ProjectType projectType : PROJECT_TYPES)
    {
      try
      {
        String natureID = projectType.getNatureID();
        if (natureID == null || project.hasNature(natureID))
        {
          IFile manifest = project.getFile(new Path(projectType.getManifestPath()));
          if (manifest.exists())
          {
            try
            {
              IEditorInput input = new FileEditorInput(manifest);
              page.openEditor(input, projectType.getEditorID(), true);
              return true;
            }
            catch (PartInitException ex)
            {
              log(ex);
              return false;
            }
          }
        }
      }
      catch (CoreException ex)
      {
        log(ex);
      }
    }

    return false;
  }

  @Override
  @SuppressWarnings("restriction")
  protected void executeWithElement(IWorkbenchPage page, Object element) throws Exception
  {
    if (element == null)
    {
      IEditorPart editor = page.getActiveEditor();
      if (editor != null)
      {
        element = editor.getEditorInput();
      }
    }

    IPluginModelBase pluginModelBase = ObjectUtil.adapt(element, IPluginModelBase.class);
    if (pluginModelBase != null)
    {
      org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor.openPluginEditor(pluginModelBase);
      return;
    }

    BundleDescription bundleDescription = ObjectUtil.adapt(element, BundleDescription.class);
    if (bundleDescription != null)
    {
      org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor.openPluginEditor(bundleDescription);
      return;
    }

    IJavaElement javaElement = ObjectUtil.adapt(element, IJavaElement.class);
    while (javaElement != null && javaElement.getElementType() != IJavaElement.PACKAGE_FRAGMENT_ROOT)
    {
      javaElement = javaElement.getParent();
    }

    if (javaElement != null)
    {
      IPackageFragmentRoot root = (IPackageFragmentRoot)javaElement;
      IEditorInput editorInput = getManifestEditorInputStorage(root);
      if (editorInput != null)
      {
        org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor.openEditor(editorInput);
      }
    }
  }

  @SuppressWarnings("restriction")
  private IEditorInput getManifestEditorInputStorage(IPackageFragmentRoot root) throws JavaModelException
  {
    for (Object object : root.getNonJavaResources())
    {
      if (object instanceof IJarEntryResource)
      {
        IJarEntryResource jarEntryResource = (IJarEntryResource)object;
        if ("META-INF".equalsIgnoreCase(jarEntryResource.getName()))
        {
          for (IJarEntryResource child : jarEntryResource.getChildren())
          {
            if ("MANIFEST.MF".equalsIgnoreCase(child.getName()))
            {
              return new org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput(child);
            }
          }
        }
      }
      else if (object instanceof IContainer)
      {
        IContainer container = (IContainer)object;
        if ("META-INF".equalsIgnoreCase(container.getName()))
        {
          IFile file = container.getFile(new Path("MANIFEST.MF"));
          if (file.exists())
          {
            return new FileEditorInput(file);
          }
        }
      }
    }

    return null;
  }

  private static void log(CoreException ex)
  {
    Activator.getDefault().getLog().log(ex.getStatus());
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

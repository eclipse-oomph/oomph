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
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.oomph.internal.version.IVersionBuilderArguments;
import org.eclipse.oomph.internal.version.ReleaseManager;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.internal.version.VersionNature;
import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IReleaseManager;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.Activator;
import org.eclipse.oomph.version.ui.dialogs.ConfigurationDialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.IEditableModel;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class AddNatureAction extends AbstractAction<IVersionBuilderArguments>
{
  private static final String SRC_INCLUDES = "src.includes";

  public AddNatureAction()
  {
    super("Add Version Management");
  }

  @Override
  protected IVersionBuilderArguments promptArguments()
  {
    List<IProject> projects = new ArrayList<IProject>();
    for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof IProject)
      {
        projects.add((IProject)element);
      }
    }

    List<IProject> basePlugins = new ArrayList<IProject>();
    List<IProject> baseFeatures = new ArrayList<IProject>();
    for (int i = 0; i < projects.size(); ++i)
    {
      IProject project = projects.get(i);
      List<IModel> componentModels = VersionUtil.getComponentModels(project);
      for (IModel componentModel : componentModels)
      {
        boolean dependenciesIncluded = false;
        IElement element = IReleaseManager.INSTANCE.createElement(componentModel, true, false);
        IElement.Type elementType = element.getType();
        if (elementType == IElement.Type.FEATURE || elementType == IElement.Type.PRODUCT)
        {
          for (IElement child : element.getChildren())
          {
            IModel childComponentModel = ReleaseManager.INSTANCE.getComponentModel(child);
            if (childComponentModel != null)
            {
              IResource childComponentModelFile = childComponentModel.getUnderlyingResource();
              if (childComponentModelFile != null)
              {
                IProject childProject = childComponentModelFile.getProject();
                if (projects.contains(childProject))
                {
                  dependenciesIncluded = true;
                }
                else
                {
                  try
                  {
                    if (!childProject.hasNature(VersionNature.NATURE_ID))
                    {
                      projects.add(childProject);
                      dependenciesIncluded = true;
                    }
                  }
                  catch (CoreException ex)
                  {
                    Activator.log(ex);
                  }
                }
              }
            }
          }

          if (!dependenciesIncluded)
          {
            baseFeatures.add(project);
          }
        }
        else
        {
          IPluginBase pluginBase = ((IPluginModelBase)componentModel).getPluginBase();
          for (IPluginImport pluginImport : pluginBase.getImports())
          {
            String importedPluginID = pluginImport.getId();
            String version = pluginImport.getVersion();
            IPluginModelBase importedPlugin = PluginRegistry.findModel(importedPluginID, new VersionRange(version), null);
            if (importedPlugin != null)
            {
              IResource childComponentModelFile = importedPlugin.getUnderlyingResource();
              if (childComponentModelFile != null)
              {
                IProject childProject = childComponentModelFile.getProject();
                if (projects.contains(childProject))
                {
                  dependenciesIncluded = true;
                }
                else
                {
                  try
                  {
                    if (!childProject.hasNature(VersionNature.NATURE_ID))
                    {
                      projects.add(childProject);
                      dependenciesIncluded = true;
                    }
                  }
                  catch (CoreException ex)
                  {
                    Activator.log(ex);
                  }
                }
              }
            }
          }

          if (!dependenciesIncluded)
          {
            basePlugins.add(project);
          }
        }
      }
    }

    IProject candidate = null;
    for (IProject project : basePlugins)
    {
      try
      {
        if (project.hasNature("org.eclipse.jdt.core.javanature"))
        {
          candidate = project;
          break;
        }
      }
      catch (CoreException ex)
      {
        Activator.log(ex);
      }
    }

    if (candidate == null)
    {
      if (!basePlugins.isEmpty())
      {
        candidate = basePlugins.get(0);
      }
      else if (!baseFeatures.isEmpty())
      {
        candidate = baseFeatures.get(0);
      }
    }

    selection = new StructuredSelection(projects);
    VersionBuilderArguments arguments = new VersionBuilderArguments();
    if (candidate != null)
    {
      arguments.setReleasePath("/" + candidate.getName() + "/release.xml");
    }

    ConfigurationDialog dialog = new ConfigurationDialog(shell, arguments);
    if (dialog.open() == ConfigurationDialog.OK)
    {
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(dialog.getReleasePath()));
      IProject project = file.getProject();
      if (project.isAccessible())
      {
        IModel componentModel = VersionUtil.getComponentModel(project);
        if (componentModel instanceof IPluginModelBase)
        {
          try
          {
            IBuildModel buildModel = VersionUtil.getBuildModel(componentModel);
            if (buildModel instanceof IEditableModel)
            {
              IBuild build = buildModel.getBuild();
              if (build != null)
              {
                IBuildEntry entry = build.getEntry(SRC_INCLUDES);
                if (entry == null)
                {
                  entry = buildModel.getFactory().createEntry(SRC_INCLUDES);
                }

                String[] tokens = entry.getTokens();
                Set<String> value = new HashSet<String>();
                if (tokens != null)
                {
                  value.addAll(Arrays.asList(tokens));
                }

                String releasePath = file.getProjectRelativePath().makeRelative().removeFileExtension().addFileExtension("*").toString();
                if (!value.contains(releasePath))
                {
                  entry.addToken(releasePath);
                  ((IEditableModel)buildModel).save();
                }
              }
            }
          }
          catch (CoreException ex)
          {
            Activator.log(ex);
          }
        }
      }

      return dialog;
    }

    return null;
  }

  @Override
  protected void runWithArguments(IVersionBuilderArguments arguments) throws CoreException
  {
    for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof IProject)
      {
        IProject project = (IProject)element;
        arguments.applyTo(project);
      }
    }
  }
}

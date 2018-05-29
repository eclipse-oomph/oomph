/*
 * Copyright (c) 2017, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.dialogs.ConfigurationDialog;
import org.eclipse.oomph.version.ui.dialogs.ExtendedConfigurationDialog;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ConfigureBuildersAction extends AbstractAction<Map<IProject, VersionBuilderArguments>>
{
  private static final String TITLE = "Configure Version Management";

  private Map<IProject, VersionBuilderArguments> oldMap;

  public ConfigureBuildersAction()
  {
    super(TITLE);
  }

  @Override
  protected Map<IProject, VersionBuilderArguments> promptArguments()
  {
    IStructuredSelection structuredSelection = (IStructuredSelection)selection;
    Collection<String> releasePaths = new HashSet<String>();
    for (Object element : structuredSelection.toArray())
    {
      if (element instanceof IFile)
      {
        IFile file = (IFile)element;
        releasePaths.add(file.getFullPath().toString());
      }
    }

    if (!releasePaths.isEmpty())
    {
      oldMap = collectVersionBuilderArguments(releasePaths);

      ExtendedConfigurationDialog dialog = new ExtendedConfigurationDialog(shell, oldMap);
      if (dialog.open() == ConfigurationDialog.OK)
      {
        return dialog.getMap();
      }
    }

    return null;
  }

  @Override
  protected void runWithArguments(Map<IProject, VersionBuilderArguments> newMap) throws CoreException
  {
    for (Map.Entry<IProject, VersionBuilderArguments> entry : newMap.entrySet())
    {
      IProject project = entry.getKey();
      VersionBuilderArguments oldArguments = oldMap.get(project);
      if (oldArguments != null)
      {
        VersionBuilderArguments newArguments = entry.getValue();
        if (!newArguments.equals(oldArguments))
        {
          newArguments.applyTo(project);
        }
      }
    }
  }

  public static Map<IProject, VersionBuilderArguments> collectVersionBuilderArguments(Collection<String> releasePaths)
  {
    Map<IProject, VersionBuilderArguments> map = new HashMap<IProject, VersionBuilderArguments>();

    for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    {
      map.put(project, null);

      try
      {
        IProjectDescription description = project.getDescription();
        for (ICommand command : description.getBuildSpec())
        {
          if (VersionUtil.BUILDER_ID.equals(command.getBuilderName()))
          {
            VersionBuilderArguments arguments = new VersionBuilderArguments(command.getArguments());
            if (releasePaths.contains(arguments.getReleasePath()))
            {
              map.put(project, arguments);
            }
          }
        }
      }
      catch (CoreException ex)
      {
        ex.printStackTrace();
      }
    }

    return map;
  }
}

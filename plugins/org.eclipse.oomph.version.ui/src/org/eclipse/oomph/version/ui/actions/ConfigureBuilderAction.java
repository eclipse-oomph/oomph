/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.dialogs.ConfigurationDialog;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public class ConfigureBuilderAction extends AbstractAction<IVersionBuilderArguments>
{
  private static final String TITLE = "Configure Version Management";

  public ConfigureBuilderAction()
  {
    super(TITLE);
  }

  @Override
  protected IVersionBuilderArguments promptArguments()
  {
    VersionBuilderArguments arguments = null;

    OuterLoop: for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof IProject)
      {
        IProject project = (IProject)element;
        try
        {
          IProjectDescription description = project.getDescription();
          for (ICommand command : description.getBuildSpec())
          {
            if (VersionUtil.BUILDER_ID.equals(command.getBuilderName()))
            {
              VersionBuilderArguments commandArguments = new VersionBuilderArguments(command.getArguments());
              if (arguments != null)
              {
                if (!arguments.equals(commandArguments))
                {
                  if (!MessageDialog.openQuestion(shell, TITLE,
                      "The selected projects have different configurations. The configuration you specify will be applied to all selected projects.\n\nWould you like to proceed?"))
                  {
                    return null;
                  }

                  break OuterLoop;
                }
              }

              arguments = commandArguments;
            }
          }
        }
        catch (CoreException ex)
        {
          ex.printStackTrace();
        }
      }
    }

    ConfigurationDialog dialog = new ConfigurationDialog(shell, arguments);
    if (dialog.open() == ConfigurationDialog.OK)
    {
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

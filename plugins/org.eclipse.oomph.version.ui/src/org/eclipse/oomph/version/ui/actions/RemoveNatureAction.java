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
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.oomph.internal.version.VersionBuilderArguments;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Iterator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class RemoveNatureAction extends AbstractAction<Boolean>
{
  public RemoveNatureAction()
  {
    super("Remove Version Management");
  }

  @Override
  protected Boolean promptArguments()
  {
    return true;
  }

  @Override
  protected void runWithArguments(Boolean arguments) throws CoreException
  {
    for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof IProject)
      {
        IProject project = (IProject)element;
        removeNature(project);
      }
    }
  }

  public static void removeNature(IProject project) throws CoreException
  {
    IProjectDescription description = project.getDescription();

    List<String> ids = VersionBuilderArguments.getOtherNatures(description);
    description.setNatureIds(ids.toArray(new String[ids.size()]));

    project.setDescription(description, new NullProgressMonitor());
  }
}

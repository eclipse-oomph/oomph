/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import java.io.File;

public class WorkspaceProjectLocationsResolver implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String argument) throws CoreException
  {
    StringBuilder result = new StringBuilder();
    for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    {
      if (project.isAccessible() && !"External Plug-in Libraries".equals(project.getName()))
      {
        IPath location = project.getLocation();
        if (location != null)
        {
          if (result.length() != 0)
          {
            result.append(File.pathSeparatorChar);
          }

          result.append(location.toOSString());
        }
      }
    }

    return result.toString();
  }
}

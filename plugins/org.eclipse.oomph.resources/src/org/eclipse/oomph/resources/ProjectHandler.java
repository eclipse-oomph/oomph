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
package org.eclipse.oomph.resources;

import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.core.resources.IProject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface ProjectHandler
{
  public void handleProject(IProject project, BackendContainer backendContainer);

  /**
   * @author Eike Stepper
   */
  public static class Collector implements ProjectHandler
  {
    private final Map<IProject, BackendContainer> projectMap = new HashMap<IProject, BackendContainer>();

    public Collector()
    {
    }

    public void handleProject(IProject project, BackendContainer backendContainer)
    {
      projectMap.put(project, backendContainer);
    }

    public Map<IProject, BackendContainer> getProjectMap()
    {
      return projectMap;
    }
  }
}

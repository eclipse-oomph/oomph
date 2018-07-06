/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Marc-Andre Laperle (Ericsson) - Fix for bug 457505
 */
package org.eclipse.oomph.resources;

import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.core.resources.IProject;

import java.util.Collections;
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
    private final Map<IProject, BackendContainer> projectMap = Collections.synchronizedMap(new HashMap<IProject, BackendContainer>());

    public Collector()
    {
    }

    public void handleProject(IProject project, BackendContainer backendContainer)
    {
      if (!projectMap.containsKey(project))
      {
        projectMap.put(project, backendContainer);
      }
    }

    public Map<IProject, BackendContainer> getProjectMap()
    {
      return projectMap;
    }
  }
}

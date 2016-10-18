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
package org.eclipse.oomph.version;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.pde.core.IModel;

/**
 * The abstract base class that all version validator implementations must extend.
 *
 * @author Eike Stepper
 */
public abstract class VersionValidator
{
  public abstract String getVersion();

  public abstract void updateBuildState(IBuildState buildState, IRelease release, IProject project, IResourceDelta delta, IModel componentModel,
      IProgressMonitor monitor) throws Exception;

  public void abort(IBuildState buildState, IProject project, Exception exception, IProgressMonitor monitor) throws Exception
  {
    buildState.clearValidatorStates();
  }
}

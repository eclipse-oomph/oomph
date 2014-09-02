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
package org.eclipse.oomph.targlets.internal.core.variables;

import org.eclipse.oomph.p2.internal.core.LazyProfileRegistry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class TargletContainerProfileFile implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String containerID) throws CoreException
  {
    String profileDirectory = TargletContainerProfileDirectory.getProfileDirectory(containerID);
    if (profileDirectory != null)
    {
      File profileFile = LazyProfileRegistry.findLatestProfileFile(new File(profileDirectory));
      if (profileFile != null)
      {
        return profileFile.getAbsolutePath();
      }
    }

    return null;
  }
}

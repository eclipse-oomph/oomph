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

import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptor;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptorManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

/**
 * @author Eike Stepper
 */
public class TargletContainerProfileID implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String containerID) throws CoreException
  {
    TargletContainerDescriptor descriptor = TargletContainerDescriptorManager.getInstance().getDescriptor(containerID, new NullProgressMonitor());
    if (descriptor != null)
    {
      return descriptor.getWorkingProfileID();
    }

    return null;
  }
}

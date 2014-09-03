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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import org.osgi.framework.Bundle;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class BundleLocationVariableResolver implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String symbolicName) throws CoreException
  {
    Bundle bundle = Platform.getBundle(symbolicName);
    if (bundle != null)
    {
      try
      {
        File bundleFile = FileLocator.getBundleFile(bundle);
        if (bundleFile != null)
        {
          return bundleFile.getAbsolutePath();
        }
      }
      catch (IOException ex)
      {
        P2CorePlugin.INSTANCE.coreException(ex);
      }
    }

    return null;
  }
}

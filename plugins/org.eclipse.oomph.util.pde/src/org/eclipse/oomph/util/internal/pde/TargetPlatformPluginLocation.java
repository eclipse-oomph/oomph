/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util.internal.pde;

import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.equinox.frameworkadmin.BundleInfo;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.TargetBundle;

import java.io.File;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public class TargetPlatformPluginLocation implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String element) throws CoreException
  {
    try
    {
      if (!StringUtil.isEmpty(element))
      {
        int pos = element.lastIndexOf('_');
        String id = pos == -1 ? element : element.substring(0, pos);
        String version = pos == -1 ? null : element.substring(pos + 1);

        ITargetDefinition targetDefinition = TargetPlatformUtil.getActiveTargetDefinition();
        if (!targetDefinition.isResolved())
        {
          targetDefinition.resolve(new NullProgressMonitor());
        }

        TargetBundle[] bundles = targetDefinition.getAllBundles();
        if (bundles != null)
        {
          for (TargetBundle bundle : bundles)
          {
            BundleInfo info = bundle.getBundleInfo();
            if (info != null)
            {
              if (info.getSymbolicName().equals(id) && (version == null || version.equals(info.getVersion())))
              {
                URI location = info.getLocation();
                if (location != null)
                {
                  String scheme = location.getScheme();
                  if ("file".equals(scheme))
                  {
                    File file = new File(location.getPath());
                    return file.getAbsolutePath();
                  }
                }
              }
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      UtilPDEPlugin.INSTANCE.coreException(ex);
    }

    return null;
  }
}

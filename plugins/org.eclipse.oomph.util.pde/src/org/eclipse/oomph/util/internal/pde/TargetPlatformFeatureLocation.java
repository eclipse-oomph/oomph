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
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.TargetFeature;

/**
 * @author Eike Stepper
 */
public class TargetPlatformFeatureLocation implements IDynamicVariableResolver
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

        TargetFeature[] features = targetDefinition.getAllFeatures();
        if (features != null)
        {
          for (TargetFeature feature : features)
          {
            if (feature.getId().equals(id) && (version == null || version.equals(feature.getVersion())))
            {
              String location = feature.getLocation();
              if (location != null)
              {
                return location;
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

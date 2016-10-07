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
package org.eclipse.oomph.targlets.internal.core.variables;

import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.util.pde.TargetPlatformListener;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.core.plugin.TargetPlatform;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;

/**
 * @author Eike Stepper
 */
public class TargetPlatformBundlePoolInitializer extends ClasspathVariableInitializer
{
  public static final String VARIABLE = "TARGET_PLATFORM_BUNDLE_POOL"; //$NON-NLS-1$

  private static final TargetPlatformListener TARGET_PLATFORM_LISTENER = new TargetPlatformListener()
  {
    public void targetDefinitionActivated(ITargetDefinition oldTargetDefinition, ITargetDefinition newTargetDefinition) throws Exception
    {
      resetVariable();
    }
  };

  public TargetPlatformBundlePoolInitializer()
  {
  }

  @Override
  public void initialize(String variable)
  {
    resetVariable();
  }

  private static void resetVariable()
  {
    try
    {
      ITargetDefinition target = TargetPlatformUtil.getActiveTargetDefinition();
      if (target != null)
      {
        ITargetLocation[] targetLocations = target.getTargetLocations();
        if (targetLocations != null)
        {
          for (ITargetLocation location : targetLocations)
          {
            if (location instanceof TargletContainer)
            {
              TargletContainer container = (TargletContainer)location;
              BundlePool bundlePool = container.getDescriptor().getBundlePool();
              if (bundlePool != null)
              {
                JavaCore.setClasspathVariable(VARIABLE, new Path(bundlePool.getLocation().getAbsolutePath()), null);
                return;
              }
            }
          }
        }
      }

      JavaCore.setClasspathVariable(VARIABLE, new Path(TargetPlatform.getLocation()), null);
    }
    catch (CoreException ex)
    {
      //$FALL-THROUGH$
    }
  }

  public static void start()
  {
    TargetPlatformUtil.addListener(TARGET_PLATFORM_LISTENER);
  }

  public static void stop()
  {
    TargetPlatformUtil.removeListener(TARGET_PLATFORM_LISTENER);
  }
}

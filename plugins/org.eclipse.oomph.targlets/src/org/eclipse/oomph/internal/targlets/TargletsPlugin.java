/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.targlets;

import org.eclipse.oomph.internal.predicates.PredicatesPlugin;
import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.util.ResourceLocator;

/**
 * @author Eike Stepper
 */
public final class TargletsPlugin extends OomphPlugin
{
  public static final TargletsPlugin INSTANCE = new TargletsPlugin();

  private static Implementation plugin;

  public TargletsPlugin()
  {
    super(new ResourceLocator[] { PredicatesPlugin.INSTANCE, ResourcesPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipsePlugin
  {
    public Implementation()
    {
      plugin = this;
    }
  }
}

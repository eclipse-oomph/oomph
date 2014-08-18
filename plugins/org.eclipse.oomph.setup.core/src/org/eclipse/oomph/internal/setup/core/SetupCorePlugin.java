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
package org.eclipse.oomph.internal.setup.core;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.setup.core.util.SetupUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Eike Stepper
 */
public final class SetupCorePlugin extends OomphPlugin
{
  private static final String DEFAULT_UPDATE_URL = "http://download.eclipse.org/oomph/updates";

  public static final String UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_UPDATE_URL, DEFAULT_UPDATE_URL).replace('\\', '/');

  public static final SetupCorePlugin INSTANCE = new SetupCorePlugin();

  private static Implementation plugin;

  public SetupCorePlugin()
  {
    super(new ResourceLocator[] {});
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

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      int xxx;
      // This is only temporary to ensure that references from the user.setup are generally absolute URIs.
      ResourceSet resourceSet = SetupUtil.createResourceSet();
      if (resourceSet.getURIConverter().exists(SetupContext.USER_SETUP_LOCATION_URI, null))
      {
        Resource resource = BaseUtil.loadResourceSafely(resourceSet, SetupContext.USER_SETUP_LOCATION_URI);
        if (!resource.getContents().isEmpty())
        {
          resource.setURI(SetupContext.USER_SETUP_URI);
          try
          {
            resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
          }
          catch (IOException ex)
          {
            log(ex);
          }
        }
      }
    }
  }
}

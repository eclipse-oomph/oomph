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

import org.eclipse.oomph.util.AbstractOomphPlugin;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.ResourceLocator;

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class P2CorePlugin extends AbstractOomphPlugin
{
  public static final P2CorePlugin INSTANCE = new P2CorePlugin();

  private static Implementation plugin;

  public P2CorePlugin()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static File getUserStateFolder(File userHome)
  {
    File folder = new File(userHome, ".eclipse/org.eclipse.oomph.p2");

    try
    {
      if (!folder.exists())
      {
        File oldFolder = new File(folder.getAbsolutePath() + ".core");
        if (oldFolder.isDirectory())
        {
          IOUtil.copyTree(oldFolder, folder);

          String message = "The '" + folder.getName() + "' folder is used instead of this folder!";
          IOUtil.writeFile(new File(oldFolder, "readme.txt"), message.getBytes());
        }
      }
    }
    catch (Exception ex)
    {
      INSTANCE.log(ex);
    }

    return folder;
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

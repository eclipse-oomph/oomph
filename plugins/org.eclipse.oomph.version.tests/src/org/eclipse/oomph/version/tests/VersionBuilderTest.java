/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.tests;

import org.eclipse.oomph.util.OomphPlugin.BundleFile;

import org.eclipse.core.resources.ResourcesPlugin;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class VersionBuilderTest extends TestSuite
{
  private VersionBuilderTest()
  {
    super("VersionBuilderTest [Workspace: " + ResourcesPlugin.getWorkspace().getRoot().getLocation() + "]");

    BundleFile rootFile = VersionTestsPlugin.INSTANCE.getRootFile();
    if (!addTests(rootFile.getChild("test")))
    {
      addTests(rootFile.getChild("tests"));
    }
  }

  private boolean addTests(BundleFile container)
  {
    boolean added = false;
    for (BundleFile testFolder : container.getChildren())
    {
      if (testFolder.isDirectory())
      {
        addTest(new VersionBuilderExecutor(testFolder));
        added = true;
      }
    }

    return added;
  }

  public static Test suite()
  {
    return new VersionBuilderTest();
  }
}

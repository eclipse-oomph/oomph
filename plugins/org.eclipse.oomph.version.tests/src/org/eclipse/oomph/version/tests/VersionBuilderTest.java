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
package org.eclipse.oomph.version.tests;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class VersionBuilderTest extends TestSuite
{
  private static final IPath WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation();

  @SuppressWarnings("unused")
  private boolean openWorkspaceFolder;

  private VersionBuilderTest()
  {
    super("VersionBuilderTest [Workspace: " + WORKSPACE + "]");

    BundleFile rootFile = Activator.getRootFile();
    if (addTests(rootFile.getChild("test")))
    {
      openWorkspaceFolder = true;
    }
    else
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

  @Override
  public void run(TestResult result)
  {
    super.run(result);
    // if (openWorkspaceFolder)
    // {
    // try
    // {
    // // TODO Support operating systems other than Windows
    // Runtime.getRuntime().exec("explorer.exe \"" + WORKSPACE.toOSString() + "\"");
    // }
    // catch (Exception ex)
    // {
    // Activator.log(ex);
    // }
    // }
  }

  public static Test suite()
  {
    return new VersionBuilderTest();
  }
}

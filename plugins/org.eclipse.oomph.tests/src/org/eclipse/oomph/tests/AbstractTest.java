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
package org.eclipse.oomph.tests;

import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;

import org.eclipse.core.runtime.IProgressMonitor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Eike Stepper
 */
public abstract class AbstractTest
{
  private static final PrintStream LOG = System.out;

  public static final IProgressMonitor LOGGER = new IProgressMonitor()
  {
    private String message = "";

    public void beginTask(String name, int totalWork)
    {
      filter(name);
    }

    public void done()
    {
    }

    public void internalWorked(double work)
    {
    }

    public boolean isCanceled()
    {
      return false;
    }

    public void setCanceled(boolean value)
    {
    }

    public void setTaskName(String name)
    {
      filter(name);
    }

    public void subTask(String name)
    {
      filter(name);
    }

    public void worked(int work)
    {
    }

    private void filter(String message)
    {
      if (message == null)
      {
        message = "";
      }

      if (!message.equals(this.message))
      {
        log(message);
      }

      this.message = message;
    }
  };

  @Rule
  public TestName testName = new TestName();

  private File userHome;

  @Before
  public void setUp() throws Exception
  {
    log("=========================================================================================================================\n" + testName.getMethodName()
        + "\n=========================================================================================================================\n");
  }

  @After
  public void tearDown() throws Exception
  {
    LOGGER.setTaskName(null);
  }

  public static File createTempFolder()
  {
    try
    {
      File folder = File.createTempFile("test-", "");
      folder.delete();
      folder.mkdirs();
      return folder;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  public File getUserHome()
  {
    if (userHome == null)
    {
      userHome = createTempFolder();
    }

    return userHome;
  }

  public File getTestFolder(OomphPlugin plugin, String name)
  {
    try
    {
      File folder = new File(getUserHome(), name);

      if (plugin != null)
      {
        BundleFile rootFile = plugin.getRootFile();
        BundleFile testRoot = rootFile.getChild("tests");

        BundleFile child = testRoot.getChild(name);
        child.export(folder);
        log("Copied plugin://" + plugin.getSymbolicName() + "/" + child + " to " + folder);
      }
      else
      {
        IOUtil.mkdirs(folder);
        log("Created folder " + folder);
      }

      return folder;
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
  }

  public static void log()
  {
    LOG.println();
  }

  public static void log(Object object)
  {
    LOG.println(object);
  }

  public static org.hamcrest.Matcher<java.lang.Object> isNull()
  {
    return org.hamcrest.core.IsNull.nullValue();
  }
}

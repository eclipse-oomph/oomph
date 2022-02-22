/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.tests;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.IProgressMonitor;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class AbstractTest extends CoreMatchers
{
  private static final String[] FILTERS = { //
      "org.eclipse.jdt.internal.junit.runner.", //
      "org.eclipse.jdt.internal.junit.ui.", //
      "org.eclipse.jdt.internal.junit4.runner.", //
      "org.junit.", //
      "sun.reflect.", //
      "java.lang.reflect.Method.invoke(", //
      "junit.framework.Assert", //
      "junit.framework.TestCase", //
      "junit.framework.TestResult", //
      "junit.framework.TestResult$1", //
      "junit.framework.TestSuite", //
  };

  private static final PrintStream LOG = System.out;

  public static final IProgressMonitor LOGGER = new IProgressMonitor()
  {
    private String message = "";

    @Override
    public void beginTask(String name, int totalWork)
    {
      filter(name);
    }

    @Override
    public void done()
    {
    }

    @Override
    public void internalWorked(double work)
    {
    }

    @Override
    public boolean isCanceled()
    {
      return false;
    }

    @Override
    public void setCanceled(boolean value)
    {
    }

    @Override
    public void setTaskName(String name)
    {
      filter(name);
    }

    @Override
    public void subTask(String name)
    {
      filter(name);
    }

    @Override
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
  public final TestWatcher failurePrinter = new FailurePrinter();

  @Rule
  public final TestName testName = new TestName();

  private File userHome;

  @Before
  public void setUp() throws Exception
  {
    log("=========================================================================================================================\n" //
        + testName.getMethodName() + "\n" //
        + "=========================================================================================================================");
  }

  @After
  public void tearDown() throws Exception
  {
    log();
    LOGGER.setTaskName(null);
  }

  public File getUserHome()
  {
    if (userHome == null)
    {
      userHome = createTempFolder();
      System.setProperty("user.home", userHome.getAbsolutePath());
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

  public static File createTempFolder()
  {
    return IOUtil.createTempFolder("test-", false);
  }

  public static void log()
  {
    LOG.println();
  }

  public static void log(Object object)
  {
    if (object instanceof Throwable)
    {
      Throwable ex = (Throwable)object;
      printStackTrace(ex);
    }
    else
    {
      LOG.println(object);
    }
  }

  public static void printStackTrace(Throwable ex)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ex.printStackTrace(new PrintStream(baos));

    List<String> lines = IOUtil.readLines(new ByteArrayInputStream(baos.toByteArray()), "UTF-8");
    for (Iterator<String> it = lines.iterator(); it.hasNext();)
    {
      String line = it.next().trim();
      for (int i = 0; i < FILTERS.length; i++)
      {
        String filter = FILTERS[i];
        if (line.startsWith("at " + filter))
        {
          it.remove();
          continue;
        }
      }
    }

    System.err.println(StringUtil.implode(lines, '\n'));
  }

  public static org.hamcrest.Matcher<java.lang.Object> isNull()
  {
    return org.hamcrest.core.IsNull.nullValue();
  }

  /**
   * @author Eike Stepper
   */
  private static final class FailurePrinter extends TestWatcher
  {
    @Override
    protected void failed(Throwable ex, Description description)
    {
      printStackTrace(ex);
    }
  }
}

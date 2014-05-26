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
package org.eclipse.oomph.p2.tests;

import org.eclipse.core.runtime.IProgressMonitor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.File;

/**
 * @author Eike Stepper
 */
public abstract class AbstractTests
{
  public static final IProgressMonitor LOGGER = new IProgressMonitor()
  {
    private String message = "";

    public void beginTask(String name, int totalWork)
    {
      log(name);
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
      log(name);
    }

    public void subTask(String name)
    {
      log(name);
    }

    public void worked(int work)
    {
    }

    private void log(String message)
    {
      if (message == null)
      {
        message = "";
      }

      if (!message.equals(this.message))
      {
        System.out.println(message);
      }

      this.message = message;
    }
  };

  @Rule
  public TestName testName = new TestName();

  public File userHome;

  @Before
  public void setUp() throws Exception
  {
    userHome = File.createTempFile("p2-tests-", "");

    System.out.println("=========================================================================================================================\n"
        + testName.getMethodName() + " --> " + userHome
        + "\n=========================================================================================================================\n");

    userHome.delete();
    userHome.mkdirs();
  }

  @After
  public void tearDown() throws Exception
  {
    LOGGER.setTaskName(null);
  }
}

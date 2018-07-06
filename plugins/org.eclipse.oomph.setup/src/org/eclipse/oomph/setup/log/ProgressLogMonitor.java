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
package org.eclipse.oomph.setup.log;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.ProgressMonitorWrapper;

/**
 * @author Eike Stepper
 */
public class ProgressLogMonitor extends ProgressMonitorWrapper
{
  private final ProgressLog log;

  public ProgressLogMonitor(ProgressLog log, IProgressMonitor delegate)
  {
    super(delegate);
    this.log = log;
  }

  public ProgressLogMonitor(ProgressLog log)
  {
    this(log, new NullProgressMonitor());
  }

  @Override
  public boolean isCanceled()
  {
    return super.isCanceled() || log.isCanceled();
  }

  @Override
  public void beginTask(String name, int totalWork)
  {
    super.beginTask(name, totalWork);
    log(name);
  }

  @Override
  public void setTaskName(String name)
  {
    super.setTaskName(name);
    log(name);
  }

  @Override
  public void subTask(String name)
  {
    super.subTask(name);
    log(name);
  }

  private void log(String name)
  {
    try
    {
      log.log(name);
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }
}

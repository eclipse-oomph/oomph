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
package org.eclipse.oomph.setup.log;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * @author Eike Stepper
 */
public class ProgressLogProvider extends ProgressProvider
{
  private final ProgressLog log;

  private final ProgressProvider delegate;

  public ProgressLogProvider(ProgressLog log, ProgressProvider delegate)
  {
    this.log = log;
    this.delegate = delegate;
  }

  @Override
  public IProgressMonitor createMonitor(Job job)
  {
    return new ProgressLogMonitor(log, delegate.createMonitor(job));
  }

  @Override
  public IProgressMonitor createProgressGroup()
  {
    return new ProgressLogMonitor(log, delegate.createProgressGroup());
  }

  @Override
  public IProgressMonitor createMonitor(Job job, IProgressMonitor group, int ticks)
  {
    return new ProgressLogMonitor(log, delegate.createMonitor(job, group, ticks));
  }

  @Override
  public IProgressMonitor getDefaultMonitor()
  {
    return new ProgressLogMonitor(log, delegate.getDefaultMonitor());
  }
}

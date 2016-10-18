/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;
import org.eclipse.oomph.gitbash.Activator;
import org.eclipse.oomph.util.MonitorUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class PushDirectAction extends AbstractAction<Repository>
{
  public PushDirectAction()
  {
    super(Repository.class);
  }

  @Override
  protected void run(Shell shell, final Repository repository) throws Exception
  {
    new Job("Pushing directly")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        monitor.beginTask(getName(), 101);

        try
        {
          Git git = Git.wrap(repository);
          monitor.worked(1);

          git.push().setRemote("direct").setProgressMonitor(new EclipseGitProgressTransformer(MonitorUtil.create(monitor, 50))).call();

          monitor.setTaskName("Pulling");
          git.pull().setProgressMonitor(new EclipseGitProgressTransformer(MonitorUtil.create(monitor, 50))).call();

          return Status.OK_STATUS;
        }
        catch (Exception ex)
        {
          return Activator.getStatus(ex);
        }
        finally
        {
          monitor.done();
        }
      }
    }.schedule();
  }
}

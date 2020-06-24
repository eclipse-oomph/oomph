/*
 * Copyright (c) 2014, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.oomph.internal.setup.SetupPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class FileUtil
{
  private FileUtil()
  {
  }

  public static File rename(File from) throws IOException, InterruptedException
  {
    File to = new File(from.getParentFile(), from.getName() + "." + System.currentTimeMillis()); //$NON-NLS-1$
    rename(from, to);
    return to;
  }

  public static void rename(File from, File to) throws IOException, InterruptedException
  {
    if (from.exists())
    {
      for (int i = 0; i < 200; i++)
      {
        if (from.renameTo(to))
        {
          return;
        }

        Thread.sleep(10);
      }
    }

    throw new IOException(NLS.bind(Messages.FileUtil_CouldNotRename_exception, from.getAbsolutePath(), to.getAbsolutePath()));
  }

  public static void deleteAsync(final File file) throws IOException, InterruptedException
  {
    new Job(Messages.FileUtil_DeletingOldFilesJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          delete(file, monitor, false);
          return Status.OK_STATUS;
        }
        catch (Exception ex)
        {
          SetupPlugin.INSTANCE.log(ex);
          return SetupPlugin.INSTANCE.getStatus(ex);
        }
      }
    }.schedule();
  }

  public static void delete(File file, IProgressMonitor monitor) throws IOException, InterruptedException
  {
    delete(file, monitor, true);
  }

  private static void delete(File file, IProgressMonitor monitor, boolean verbose) throws IOException, InterruptedException
  {
    List<File> files = listAllFiles(file);
    int size = files.size();
    if (size == 0)
    {
      return;
    }

    if (verbose)
    {
      String message = ""; //$NON-NLS-1$
      if (file.isDirectory() && size > 1)
      {
        message = NLS.bind(Messages.FileUtil_DeletingFilesIn_task, file.getAbsolutePath());
      }

      monitor.beginTask(message, size);
    }

    try
    {
      Collections.reverse(files);
      for (File child : files)
      {
        SetupPlugin.checkCancelation(monitor);

        if (verbose)
        {
          String childPath = child.getAbsolutePath();
          monitor.setTaskName(NLS.bind(Messages.FileUtil_DeletingFile_task, childPath));
        }

        doDelete(child);
        monitor.worked(1);
      }
    }
    finally
    {
      if (verbose)
      {
        monitor.done();
      }
    }
  }

  private static void doDelete(File file) throws IOException, InterruptedException
  {
    for (int i = 0; i < 1000; i++)
    {
      if (file.delete())
      {
        return;
      }

      Thread.sleep(5);
    }

    throw new IOException(NLS.bind(Messages.FileUtil_CouldNotDelete_exception, file.getAbsolutePath()));
  }

  private static List<File> listAllFiles(File file)
  {
    List<File> result = new ArrayList<File>();
    if (file != null && file.exists())
    {
      listAllFiles(file, result);
    }

    return result;
  }

  private static void listAllFiles(File file, List<File> result)
  {
    result.add(file);
    if (file.isDirectory())
    {
      for (File child : file.listFiles())
      {
        listAllFiles(child, result);
      }
    }
  }
}

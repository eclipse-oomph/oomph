/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.setup.internal.sync.DataProvider.Location;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotFoundException;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class Snapshot
{
  public static final boolean DEFAULT_INCREMENTAL = true;

  private final DataProvider dataProvider;

  private final String info;

  private final File oldFile;

  private final File newFile;

  private final File tmpFile;

  public Snapshot(DataProvider dataProvider, File folder)
  {
    this(dataProvider, folder, DEFAULT_INCREMENTAL);
  }

  public Snapshot(DataProvider dataProvider, File folder, boolean incremental)
  {
    this.dataProvider = dataProvider;

    String prefix = dataProvider.getLocation().toString().toLowerCase();
    info = new File(folder, prefix + "-???.xml").toString();

    if (incremental)
    {
      oldFile = new File(folder, prefix + "-old.xml");
    }
    else
    {
      oldFile = null;
    }

    newFile = new File(folder, prefix + "-new.xml");
    tmpFile = new File(folder, prefix + "-tmp.xml");
  }

  public DataProvider getDataProvider()
  {
    return dataProvider;
  }

  public File getFolder()
  {
    return tmpFile.getParentFile();
  }

  public File getOldFile()
  {
    return oldFile;
  }

  public File getNewFile()
  {
    return newFile;
  }

  public void copyFilesTo(File target)
  {
    copyFileTo(target, oldFile);
    copyFileTo(target, newFile);

    for (File file : dataProvider.getExtraFiles())
    {
      copyFileTo(target, file);
    }
  }

  public void copyFilesFrom(File source)
  {
    copyFileFrom(source, oldFile);
    copyFileFrom(source, newFile);

    for (File file : dataProvider.getExtraFiles())
    {
      copyFileFrom(source, file);
    }
  }

  public WorkingCopy createWorkingCopy() throws IOException
  {
    try
    {
      dataProvider.retrieve(newFile);
    }
    catch (NotFoundException ex)
    {
      SyncUtil.deleteFile(newFile);
    }

    return new WorkingCopy(this);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + dataProvider + " --> " + info + "]";
  }

  private void doCommit(boolean updateDataProvider) throws IOException, NotCurrentException
  {
    if (!tmpFile.isFile())
    {
      throw new FileNotFoundException(tmpFile.getAbsolutePath());
    }

    try
    {
      if (updateDataProvider)
      {
        dataProvider.update(tmpFile, newFile);
      }

      if (oldFile != null)
      {
        moveTmpFileTo(oldFile);
        IOUtil.copyFile(oldFile, newFile);
      }
      else
      {
        moveTmpFileTo(newFile);
      }
    }
    catch (NotCurrentException ex)
    {
      moveTmpFileTo(newFile); // TODO Is this good??
      throw ex;
    }
  }

  private void moveTmpFileTo(File target) throws IOException
  {
    SyncUtil.deleteFile(target);
    if (!tmpFile.renameTo(target))
    {
      throw new IOException("Could not rename " + tmpFile + " to " + target);
    }
  }

  private static void copyFileTo(File target, File file)
  {
    if (file != null && file.isFile())
    {
      IOUtil.copyFile(file, new File(target, file.getName()));
    }
  }

  private static void copyFileFrom(File source, File file)
  {
    if (file != null)
    {
      File sourceFile = new File(source, file.getName());
      if (sourceFile.isFile())
      {
        IOUtil.copyFile(sourceFile, file);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class WorkingCopy
  {
    private final Snapshot snapshot;

    private boolean committed;

    private boolean disposed;

    private WorkingCopy(Snapshot snapshot)
    {
      this.snapshot = snapshot;
    }

    public Snapshot getSnapshot()
    {
      return snapshot;
    }

    public boolean isLocal()
    {
      return snapshot.getDataProvider().getLocation() == Location.LOCAL;
    }

    public File getTmpFile()
    {
      return snapshot.tmpFile;
    }

    public void commit(boolean updateDataProvider) throws IOException, NotCurrentException
    {
      if (!committed && !disposed)
      {
        committed = true;
        snapshot.doCommit(updateDataProvider);
      }
    }

    public void dispose()
    {
      if (!disposed)
      {
        disposed = true;

        try
        {
          SyncUtil.deleteFile(snapshot.tmpFile);
        }
        catch (Throwable ex)
        {
          SetupSyncPlugin.INSTANCE.log(ex);
        }
      }
    }
  }
}

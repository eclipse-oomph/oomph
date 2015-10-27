/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
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
  private final DataProvider dataProvider;

  private final String info;

  private final File oldFile;

  private final File newFile;

  private final File tmpFile;

  public Snapshot(DataProvider dataProvider, File folder)
  {
    this.dataProvider = dataProvider;

    String prefix = dataProvider.getLocation().toString().toLowerCase();
    info = new File(folder, prefix + "-???.xml").toString();

    oldFile = new File(folder, prefix + "-old.xml");
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

  public void copyFilesTo(File target) throws IOException
  {
    IOUtil.copyFile(oldFile, new File(target, oldFile.getName()));
    IOUtil.copyFile(newFile, new File(target, newFile.getName()));
  }

  public void copyFilesFrom(File source) throws IOException
  {
    IOUtil.copyFile(new File(source, oldFile.getName()), oldFile);
    IOUtil.copyFile(new File(source, newFile.getName()), newFile);
  }

  public WorkingCopy createWorkingCopy() throws IOException
  {
    try
    {
      dataProvider.update(newFile);
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

  private void doCommit(boolean post) throws IOException, NotCurrentException
  {
    if (!tmpFile.isFile())
    {
      throw new FileNotFoundException(tmpFile.getAbsolutePath());
    }

    try
    {
      if (post)
      {
        String baseVersion = SyncUtil.getDigest(newFile);
        dataProvider.post(tmpFile, baseVersion);
      }

      moveTmpFileTo(oldFile);

      IOUtil.copyFile(oldFile, newFile);
    }
    catch (NotCurrentException ex)
    {
      moveTmpFileTo(newFile);
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

    public void commit(boolean post) throws IOException, NotCurrentException
    {
      if (!committed && !disposed)
      {
        committed = true;
        snapshot.doCommit(post);
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

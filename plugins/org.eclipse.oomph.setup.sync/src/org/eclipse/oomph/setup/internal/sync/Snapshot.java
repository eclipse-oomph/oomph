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

import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.ConflictException;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class Snapshot
{
  private final DataProvider dataProvider;

  private final File oldFile;

  private final File newFile;

  public Snapshot(DataProvider dataProvider, File syncFolder) throws IOException
  {
    this.dataProvider = dataProvider;

    String prefix = dataProvider.getLocation().toString().toLowerCase();
    oldFile = new File(syncFolder, prefix + ".xml");
    newFile = new File(syncFolder, prefix + ".tmp");

    long timeStamp = oldFile.lastModified();

    long lastModified = dataProvider.get(timeStamp, newFile);
    if (lastModified != 0)
    {
      IOUtil.copyFile(newFile, oldFile);
      oldFile.setLastModified(lastModified);
    }
    else
    {
      IOUtil.copyFile(oldFile, newFile);
    }
  }

  public File getOldFile()
  {
    return oldFile;
  }

  public File getNewFile()
  {
    return newFile;
  }

  public boolean commit() throws IOException, ConflictException
  {
    if (newFile.isFile())
    {
      long timeStamp = oldFile.lastModified();
      if (timeStamp != 0)
      {
        ConflictException conflictException = null;

        try
        {
          dataProvider.post(timeStamp, newFile);
        }
        catch (ConflictException ex)
        {
          conflictException = ex;
        }

        IOUtil.deleteBestEffort(oldFile);
        newFile.renameTo(oldFile);

        if (conflictException != null)
        {
          throw conflictException;
        }

        return true;
      }
    }

    return false;
  }

  public void dispose()
  {
    IOUtil.deleteBestEffort(newFile);
  }
}

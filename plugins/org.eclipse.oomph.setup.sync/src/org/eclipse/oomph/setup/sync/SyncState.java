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
package org.eclipse.oomph.setup.sync;

import org.eclipse.oomph.setup.sync.client.Client;
import org.eclipse.oomph.setup.sync.client.Client.ConflictException;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class SyncState
{
  private final Client client;

  private final File originalFile;

  private final File workingCopy;

  public SyncState(Client client, File syncFolder)
  {
    this.client = client;
    originalFile = new File(syncFolder, "remote.xml");
    workingCopy = new File(syncFolder, "remote.tmp");
  }

  public File getWorkingCopy(boolean forceRefresh) throws IOException
  {
    long timeStamp = originalFile.lastModified();
    if (timeStamp == 0 || forceRefresh)
    {
      long lastModified = client.get(timeStamp, workingCopy);
      if (lastModified != 0)
      {
        IOUtil.copyFile(workingCopy, originalFile);
        originalFile.setLastModified(lastModified);
      }
      else
      {
        IOUtil.copyFile(originalFile, workingCopy);
      }
    }

    return workingCopy;
  }

  public boolean commit() throws IOException, ConflictException
  {
    if (workingCopy.isFile())
    {
      long timeStamp = originalFile.lastModified();
      if (timeStamp != 0)
      {
        ConflictException conflictException = null;

        try
        {
          client.post(timeStamp, workingCopy);
        }
        catch (ConflictException ex)
        {
          conflictException = ex;
        }

        IOUtil.deleteBestEffort(originalFile);
        workingCopy.renameTo(originalFile);

        if (conflictException != null)
        {
          throw conflictException;
        }

        return true;
      }
    }

    return false;
  }
}

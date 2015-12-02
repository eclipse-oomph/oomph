/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

/**
 * @author Eike Stepper
 */
public final class LockFile
{
  private final File file;

  private FileOutputStream stream;

  private FileLock lock;

  public LockFile(File file)
  {
    this.file = file;
  }

  public File getFile()
  {
    return file;
  }

  public synchronized boolean isLocked()
  {
    return stream != null;
  }

  public synchronized void lock() throws IOException
  {
    if (stream == null)
    {
      try
      {
        stream = new FileOutputStream(file);

        lock = stream.getChannel().tryLock();
        if (lock == null)
        {
          throw new IOException(file + " could not be locked");
        }
      }
      catch (IOException ex)
      {
        doRelease();
        throw ex;
      }
      catch (RuntimeException ex)
      {
        doRelease();
        throw ex;
      }
      catch (Error ex)
      {
        doRelease();
        throw ex;
      }
    }
  }

  public synchronized void unlock()
  {
    if (stream != null)
    {
      doRelease();
    }
  }

  private void doRelease()
  {
    if (lock != null)
    {
      try
      {
        lock.release();
      }
      catch (Throwable ex)
      {
        UtilPlugin.INSTANCE.log(ex);
      }
      finally
      {
        lock = null;
      }
    }

    if (stream != null)
    {
      try
      {
        IOUtil.close(stream);
      }
      catch (Throwable ex)
      {
        UtilPlugin.INSTANCE.log(ex);
      }
      finally
      {
        stream = null;
      }
    }

    try
    {
      file.delete();
    }
    catch (Throwable ex)
    {
      // Ignore
    }
  }
}

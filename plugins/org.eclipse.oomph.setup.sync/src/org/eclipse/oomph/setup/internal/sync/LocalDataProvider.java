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

import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class LocalDataProvider implements DataProvider
{
  private final File locaFile;

  public LocalDataProvider(File locaFile)
  {
    this.locaFile = locaFile;
  }

  public Location getLocation()
  {
    return Location.LOCAL;
  }

  public long get(long timeStamp, File file) throws IOException
  {
    long lastModified = locaFile.lastModified();
    if (lastModified == timeStamp)
    {
      return 0;
    }

    IOUtil.copyFile(locaFile, file);
    return lastModified;
  }

  public void post(long timeStamp, File file) throws IOException, ConflictException
  {
    long lastModified = locaFile.lastModified();
    if (lastModified == timeStamp)
    {
      IOUtil.copyFile(file, locaFile);
      lastModified = locaFile.lastModified();
    }
    else
    {
      IOUtil.copyFile(locaFile, file);
      file.setLastModified(lastModified);
      throw new ConflictException(locaFile.toURI());
    }
  }
}

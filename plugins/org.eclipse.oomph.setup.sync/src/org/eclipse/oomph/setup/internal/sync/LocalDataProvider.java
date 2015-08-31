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
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class LocalDataProvider implements DataProvider
{
  private final File localFile;

  public LocalDataProvider(File locaFile)
  {
    localFile = locaFile;
  }

  public Location getLocation()
  {
    return Location.LOCAL;
  }

  public boolean update(File file) throws IOException, NotFoundException
  {
    String localVersion = SyncUtil.getDigest(localFile);
    if (StringUtil.isEmpty(localVersion))
    {
      throw new NotFoundException(localFile.toURI());
    }

    String version = SyncUtil.getDigest(file);
    if (!ObjectUtil.equals(version, localVersion))
    {
      IOUtil.copyFile(localFile, file);
      return true;
    }

    return false;
  }

  public void post(File file, String baseVersion) throws IOException, NotCurrentException
  {
    String localVersion = SyncUtil.getDigest(localFile);
    if (StringUtil.isEmpty(localVersion) || ObjectUtil.equals(localVersion, baseVersion))
    {
      // OK.
      IOUtil.copyFile(file, localFile);
    }
    else
    {
      // Conflict.
      IOUtil.copyFile(localFile, file);
      throw new NotCurrentException(localFile.toURI());
    }
  }

  public boolean delete() throws IOException
  {
    if (!localFile.exists())
    {
      return false;
    }

    SyncUtil.deleteFile(localFile);
    return true;
  }
}

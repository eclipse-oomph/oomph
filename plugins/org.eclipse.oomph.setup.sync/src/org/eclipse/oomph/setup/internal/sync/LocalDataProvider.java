/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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

  public final Location getLocation()
  {
    return Location.LOCAL;
  }

  public final File getLocalFile()
  {
    return localFile;
  }

  public File[] getExtraFiles()
  {
    return NO_FILES;
  }

  public boolean retrieve(File file) throws IOException, NotFoundException
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

  public void update(File file, File baseFile) throws IOException, NotCurrentException
  {
    String localVersion = SyncUtil.getDigest(localFile);
    String baseVersion = SyncUtil.getDigest(baseFile);

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

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getLocalFile() + "]";
  }
}

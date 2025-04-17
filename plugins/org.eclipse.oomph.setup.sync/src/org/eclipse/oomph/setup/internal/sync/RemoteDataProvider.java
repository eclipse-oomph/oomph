/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.setup.internal.sync.Synchronization.ConflictException;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author Eike Stepper
 */
public class RemoteDataProvider implements DataProvider
{
  private final Path location;

  public RemoteDataProvider(Path location)
  {
    this.location = location;
  }

  @Override
  public final Location getLocation()
  {
    return Location.REMOTE;
  }

  public Path getStorageLocation()
  {
    return location;
  }

  public URI getURI()
  {
    return location.toUri();

  }

  @Override
  public boolean retrieve(File file) throws IOException, NotFoundException
  {
    try
    {
      InputStream contents = Files.newInputStream(location);
      boolean cached = contents instanceof FileInputStream;
      saveContents(contents, file);
      return !cached;
    }
    catch (NoSuchFileException ex)
    {
      throw new NotFoundException(file.toURI());
    }
  }

  @Override
  public void update(File file, File baseFile) throws IOException, NotCurrentException
  {
    try
    {
      Files.copy(file.toPath(), location, StandardCopyOption.REPLACE_EXISTING);
    }
    catch (ConflictException ex)
    {
      throw new NotCurrentException(getURI());
    }
  }

  @Override
  public File[] getExtraFiles()
  {
    return NO_FILES;
  }

  @Override
  public boolean delete() throws IOException
  {
    Files.delete(location);
    return true;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getURI() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static void saveContents(InputStream contents, File file) throws IOException
  {
    File parentFolder = file.getParentFile();
    if (parentFolder != null)
    {
      parentFolder.mkdirs();
    }

    OutputStream out = null;

    try
    {
      out = new FileOutputStream(file);

      IOUtil.copy(contents, out);
    }
    finally
    {
      IOUtil.closeSilent(out);
      IOUtil.closeSilent(contents);
    }
  }
}

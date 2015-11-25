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
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.userstorage.IBlob;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.util.ConflictException;
import org.eclipse.userstorage.util.FileStorageCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public class RemoteDataProvider implements DataProvider
{
  public static final String APPLICATION_TOKEN = "cNhDr0INs8T109P8h6E1r_GvU3I";

  public static final String KEY = "user_setup";

  private final IBlob blob;

  public RemoteDataProvider(IStorage storage)
  {
    blob = storage.getBlob(KEY);
    if (StringUtil.isEmpty(blob.getETag()))
    {
      // Avoid sending no/empty etag because then the service would unconditionally overwrite the blob.
      blob.setETag("<unknown-etag>");
    }
  }

  public final Location getLocation()
  {
    return Location.REMOTE;
  }

  public final URI getURI()
  {
    IStorageService service = getStorage().getService();
    if (service != null)
    {
      return service.getServiceURI();
    }

    return null;
  }

  public final IStorage getStorage()
  {
    return blob.getStorage();
  }

  public IBlob getBlob()
  {
    return blob;
  }

  public File[] getExtraFiles()
  {
    SyncStorageCache cache = (SyncStorageCache)getStorage().getCache();
    return new File[] { cache.getPropertiesFile() };
  }

  public boolean retrieve(File file) throws IOException, NotFoundException
  {
    InputStream contents = blob.getContents();
    if (contents == null)
    {
      throw new NotFoundException(getURI());
    }

    if (contents instanceof FileInputStream)
    {
      // NOT_MODIFIED
      IOUtil.closeSilent(contents);
      return false;
    }

    saveContents(contents, file);
    return true;
  }

  public void update(File file, File baseFile) throws IOException, NotCurrentException
  {
    try
    {
      blob.setContents(new FileInputStream(file));
    }
    catch (ConflictException ex)
    {
      throw new NotCurrentException(getURI());
    }
  }

  public boolean delete() throws IOException
  {
    return false;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getURI() + "]";
  }

  public static void saveContents(InputStream contents, File file) throws IOException
  {
    file.getParentFile().mkdirs();
    OutputStream out = null;

    try
    {
      out = new BufferedOutputStream(new FileOutputStream(file));

      IOUtil.copy(contents, out);
    }
    finally
    {
      IOUtil.closeSilent(out);
      IOUtil.closeSilent(contents);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class SyncStorageCache extends FileStorageCache.SingleApplication.SingleKey
  {
    private static final String FILE_NAME_PREFIX = "remote-new.xml";

    private static final String PROPERTIES_FILE_NAME = FILE_NAME_PREFIX + PROPERTIES;

    public SyncStorageCache(File folder)
    {
      super(folder, APPLICATION_TOKEN, KEY);
      setFileNamePrefix(FILE_NAME_PREFIX);
    }

    public File getPropertiesFile()
    {
      return new File(getFolder(), PROPERTIES_FILE_NAME);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AuthorizationRequiredException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public AuthorizationRequiredException(URI uri)
    {
      super("Forbidden: " + uri);
    }
  }
}

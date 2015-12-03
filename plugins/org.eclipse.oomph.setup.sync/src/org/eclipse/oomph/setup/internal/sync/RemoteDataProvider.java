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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Eike Stepper
 */
public class RemoteDataProvider implements DataProvider
{
  public static final String APPLICATION_TOKEN = "cNhDr0INs8T109P8h6E1r_GvU3I";

  public static final String KEY = "user_setup";

  private static final byte[] GZIP_MAGIC = { (byte)0x1f, (byte)0x8b };

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
    try
    {
      InputStream contents = blob.getContents();
      if (contents instanceof FileInputStream)
      {
        // NOT_MODIFIED
        IOUtil.closeSilent(contents);
        return false;
      }

      // BufferedInputStream bufferedInputStream = new BufferedInputStream(contents);

      byte[] gzipMagic = new byte[GZIP_MAGIC.length];
      int n = contents.read(gzipMagic);

      if (n == -1)
      {
        contents = new ByteArrayInputStream(new byte[0]);
      }
      else if (n < GZIP_MAGIC.length)
      {
        contents = new ByteArrayInputStream(gzipMagic, 0, n);
      }
      else
      {
        Vector<InputStream> streams = new Vector<InputStream>(2);
        streams.add(new ByteArrayInputStream(gzipMagic, 0, n));
        streams.add(contents);

        contents = new SequenceInputStream(streams.elements());

        if (Arrays.equals(gzipMagic, GZIP_MAGIC))
        {
          contents = new GZIPInputStream(contents);
        }
      }

      saveContents(contents, file);
      return true;
    }
    catch (org.eclipse.userstorage.util.NotFoundException ex)
    {
      throw new NotFoundException(getURI());
    }
  }

  public void update(File file, File baseFile) throws IOException, NotCurrentException
  {
    try
    {
      InputStream contents = new FileInputStream(file);

      if (!Boolean.getBoolean("org.eclipse.oomph.setup.sync.gzip.skip"))
      {
        contents = new DeflatingInputStream(contents);
      }

      blob.setContents(contents);
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

  /**
   * @author Eike Stepper
   */
  private static final class DeflatingInputStream extends InputStream
  {
    private InputStream in;

    private TEMPOutputStream temp = new TEMPOutputStream();

    private GZIPOutputStream gzip = new GZIPOutputStream(temp, true);

    public DeflatingInputStream(InputStream in) throws IOException
    {
      this.in = in;
    }

    @Override
    public int read() throws IOException
    {
      byte[] b = new byte[1];
      int n = read(b);
      return n == 1 ? b[0] : n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
      return temp.read(b, off, len);
    }

    @Override
    public void close() throws IOException
    {
      in.close();
      super.close();
    }

    /**
     * @author Eike Stepper
     */
    private final class TEMPOutputStream extends ByteArrayOutputStream
    {
      private final byte[] buffer = new byte[8192];

      public int read(byte[] b, int off, int len) throws IOException
      {
        while (count < len)
        {
          int n = in.read(buffer);
          if (n == -1)
          {
            gzip.close();
            break;
          }

          gzip.write(buffer, 0, n);
          gzip.flush();
        }

        int result = Math.min(len, count);
        System.arraycopy(buf, 0, b, off, result);

        count -= len;
        if (count <= 0)
        {
          count = 0;
        }
        else
        {
          System.arraycopy(buf, len, buf, 0, count);
        }

        return result == 0 ? -1 : result;
      }
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
}

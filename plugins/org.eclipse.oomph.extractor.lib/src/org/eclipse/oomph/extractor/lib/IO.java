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
package org.eclipse.oomph.extractor.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Eike Stepper
 */
public abstract class IO
{
  public static String readUTF8(File file) throws IOException
  {
    InputStream inputStream = new FileInputStream(file);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try
    {
      copy(inputStream, outputStream);
    }
    finally
    {
      close(inputStream);
    }

    return new String(outputStream.toByteArray(), "UTF-8"); //$NON-NLS-1$
  }

  public static void writeUTF8(File file, String contents) throws IOException
  {
    InputStream inputStream = new ByteArrayInputStream(contents.getBytes("UTF-8")); //$NON-NLS-1$
    OutputStream outputStream = new FileOutputStream(file);

    try
    {
      copy(inputStream, outputStream);
    }
    finally
    {
      close(outputStream);
    }
  }

  public static void unzip(InputStream stream, String targetFolder) throws IOException
  {
    byte[] buffer = new byte[8192];

    ZipInputStream zis = new ZipInputStream(stream);
    ZipEntry ze = zis.getNextEntry();

    while (ze != null)
    {
      String name = ze.getName();
      if (!"extractor.exe".equals(name) && !name.endsWith("/")) //$NON-NLS-1$ //$NON-NLS-2$
      {
        File file = new File(targetFolder, name);
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        int len;

        while ((len = zis.read(buffer)) > 0)
        {
          fos.write(buffer, 0, len);
        }

        fos.close();
      }

      ze = zis.getNextEntry();
    }

    zis.closeEntry();
    zis.close();
  }

  public static long copy(InputStream input, OutputStream output) throws IOException
  {
    byte[] buffer = new byte[8192];
    long length = 0;
    int n;

    while ((n = input.read(buffer)) != -1)
    {
      output.write(buffer, 0, n);
      length += n;
    }

    return length;
  }

  public static long drain(InputStream input) throws IOException
  {
    long count = 0;
    while (input.read() != -1)
    {
      ++count;
    }

    return count;
  }

  public static void close(InputStream closeable) throws IOException
  {
    if (closeable != null)
    {
      closeable.close();
    }
  }

  public static void close(OutputStream closeable) throws IOException
  {
    if (closeable != null)
    {
      closeable.close();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class KMPInputStream extends InputStream
  {
    private final InputStream in;

    private final int[] buffer;

    private final byte[] pattern;

    private final int[] failure;

    private int posPattern;

    private int posWrite;

    private int posRead;

    private int filled;

    private int unfilled;

    private boolean eof;

    public KMPInputStream(InputStream in, byte[] pattern)
    {
      this(in, pattern, failureOf(pattern));
    }

    public KMPInputStream(InputStream in, byte[] pattern, int[] failure)
    {
      this.in = in;
      this.pattern = pattern;
      this.failure = failure;
      buffer = new int[8192 + pattern.length];
      unfilled = pattern.length;
    }

    public int[] getFailure()
    {
      return failure;
    }

    @Override
    public int read() throws IOException
    {
      while (!eof && unfilled != 0)
      {
        int b = in.read();

        while (posPattern > 0 && pattern[posPattern] != b)
        {
          posPattern = failure[posPattern - 1];
        }

        if (pattern[posPattern] == b)
        {
          ++posPattern;
        }

        if (posPattern == pattern.length)
        {
          eof = true;
        }
        else
        {
          buffer[posWrite] = b;
          posWrite = ++posWrite % buffer.length;
          ++filled;
        }

        --unfilled;
      }

      if (filled < pattern.length)
      {
        return -1;
      }

      int b = buffer[posRead];
      posRead = ++posRead % buffer.length;

      --filled;
      unfilled = 1;

      return b;
    }

    private static int[] failureOf(byte[] pattern)
    {
      int[] failure = new int[pattern.length];
      int j = 0;

      for (int i = 1; i < pattern.length; i++)
      {
        while (j > 0 && pattern[j] != pattern[i])
        {
          j = failure[j - 1];
        }

        if (pattern[j] == pattern[i])
        {
          ++j;
        }

        failure[i] = j;
      }

      return failure;
    }
  }
}

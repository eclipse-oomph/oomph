/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.extractor.lib;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author Eike Stepper
 */
public final class BINExtractor extends IO
{
  private static final String NL = System.getProperty("line.separator");

  private static final String UTF_8 = "UTF-8";

  public static void main(String[] args) throws Exception
  {
    String executable = args[0];
    String targetFolder = args[1];
    String javaHome = null;

    boolean export = false;
    File markerFile = null;
    File extractorFile = null;
    File libdataFile = null;
    File descriptorFile = null;
    File zipFile = null;

    if (args.length > 2)
    {
      if ("-export".equals(args[2]))
      {
        export = true;
        markerFile = new File(args[3]);
        extractorFile = new File(args[4]);
        libdataFile = new File(args[5]);
        descriptorFile = new File(args[6]);
        zipFile = new File(targetFolder);
      }
      else
      {
        javaHome = args[2];
      }
    }

    InputStream in = new FileInputStream(executable);
    BINDescriptor descriptor;

    try
    {
      BufferedInputStream stream = new BufferedInputStream(in);

      byte[] pattern = BINMarker.getBytes();
      if (export)
      {
        copy(new ByteArrayInputStream(pattern), markerFile);
      }

      KMPInputStream kmpStream = new KMPInputStream(stream, pattern);
      int[] failure = kmpStream.getFailure();

      // Find the marker that's embedded via extractor.h
      if (export)
      {
        copy(kmpStream, extractorFile);
        copy(new ByteArrayInputStream(pattern), extractorFile);
      }
      else
      {
        drain(kmpStream);
      }

      // Find the marker after the extractor binary
      kmpStream = new KMPInputStream(stream, pattern, failure);
      if (export)
      {
        copy(kmpStream, extractorFile);
      }
      else
      {
        drain(kmpStream);
      }

      // Find the marker after the libdata.jar
      kmpStream = new KMPInputStream(stream, pattern, failure);
      if (export)
      {
        copy(kmpStream, libdataFile);
      }
      else
      {
        drain(kmpStream);
      }

      // Find the marker after the descriptor
      kmpStream = new KMPInputStream(stream, pattern, failure);
      descriptor = new BINDescriptor(kmpStream);
      if (export)
      {
        descriptor.write(descriptorFile);
      }

      // Find the marker after the product
      kmpStream = new KMPInputStream(stream, pattern, failure);
      if (export)
      {
        copy(kmpStream, zipFile);
      }
      else
      {
        unzip(kmpStream, targetFolder);
      }
    }
    finally
    {
      close(in);
    }

    if (!export)
    {
      if (javaHome != null)
      {
        adjustIni(targetFolder + File.separator + descriptor.getIniPath(), javaHome);
      }

      String launcher = targetFolder + File.separator + descriptor.getLauncherPath();
      Runtime.getRuntime().exec(launcher);
    }
  }

  private static void copy(InputStream source, File targetFile) throws IOException
  {
    File parentFile = targetFile.getParentFile();
    if (parentFile != null)
    {
      parentFile.mkdirs();
    }

    OutputStream target = new FileOutputStream(targetFile, true);

    try
    {
      copy(source, target);
    }
    finally
    {
      close(target);
    }
  }

  private static void adjustIni(String iniPath, String javaHome) throws IOException
  {
    File file = new File(iniPath);
    Vector lines = new Vector();

    if (file.exists())
    {
      InputStream in = new FileInputStream(file);
      Reader reader = new InputStreamReader(in, UTF_8);
      BufferedReader bufferedReader = new BufferedReader(reader);

      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        lines.add(line);
      }

      bufferedReader.close();
      reader.close();
      in.close();
    }

    String value = getVMPath(javaHome);
    String option = "-vm";
    int optionIndex = lines.indexOf(option);

    if (optionIndex != -1)
    {
      lines.set(optionIndex + 1, value);
    }
    else
    {
      int vmargsIndex = lines.indexOf("-vmargs");
      if (vmargsIndex == -1)
      {
        vmargsIndex = lines.size();
      }

      lines.add(vmargsIndex, option);
      lines.add(vmargsIndex + 1, value);
    }

    // lines.add("-Xdebug");
    // lines.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8123");

    OutputStream out = new FileOutputStream(file);

    Writer writer = new OutputStreamWriter(out, UTF_8);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);

    for (Enumeration it = lines.elements(); it.hasMoreElements();)
    {
      bufferedWriter.write((String)it.nextElement());
      bufferedWriter.write(NL);
    }

    bufferedWriter.close();
    writer.close();
    out.close();
  }

  private static String getVMPath(String javaHome)
  {
    if (javaHome.endsWith(".exe"))
    {
      return new File(javaHome).getParent();
    }

    return javaHome + File.separator + "bin";
  }
}

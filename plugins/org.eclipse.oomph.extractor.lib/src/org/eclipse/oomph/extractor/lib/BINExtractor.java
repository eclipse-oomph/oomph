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
import java.io.PrintStream;
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

  private static final boolean DEBUG = "true".equals(System.getProperty("org.eclipse.oomph.extractor.lib.BINExtractor.log"));

  public static void main(String[] args) throws Exception
  {
    if (args.length < 2)
    {
      exit();
    }

    String executable = args[0];
    String targetFolder = args[1];
    String javaHome = null;

    boolean export = false;
    File markerFile = null;
    File extractorFile = null;
    File libdataFile = null;
    File descriptorFile = null;
    File zipFile = null;
    File jreTarCabFile = null;
    int extraArgs = args.length;

    if (args.length > 2)
    {
      String arg = args[2];
      if ("-export".equals(arg))
      {
        if (args.length < 7)
        {
          exit();
        }

        export = true;
        markerFile = new File(args[3]);
        extractorFile = new File(args[4]);
        libdataFile = new File(args[5]);
        descriptorFile = new File(args[6]);
        zipFile = new File(targetFolder);
        if (args.length > 7)
        {
          jreTarCabFile = new File(args[7]);
        }
        else
        {
          jreTarCabFile = new File(zipFile.getAbsoluteFile().getParentFile(), "jre.tar.cab");
        }
      }
      else if ("--".equals(arg))
      {
        extraArgs = 3;
      }
      else
      {
        javaHome = arg;
        if (args.length > 3 && "--".equals(args[3]))
        {
          extraArgs = 4;
        }
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

      // Find the marker after the extractor binary
      KMPInputStream kmpStream = new KMPInputStream(stream, pattern);
      int[] failure = kmpStream.getFailure();
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

      if (export && descriptor.getFormat() == 2)
      {
        kmpStream = new KMPInputStream(stream, pattern, failure);
        copy(kmpStream, jreTarCabFile);
      }
    }
    finally
    {
      close(in);
    }

    if (!export)
    {
      Vector vmArgs = null;
      int vmArgStart = args.length;
      int vmArgCount = 0;
      for (int i = extraArgs; i < args.length; ++i)
      {
        String arg = args[i];
        if ("-vmargs".equals(arg))
        {
          vmArgStart = i;
          ++vmArgCount;
          vmArgs = new Vector();
        }
        else if (vmArgs != null)
        {
          ++vmArgCount;
          vmArgs.add(arg);
        }
      }

      PrintStream log = DEBUG ? new PrintStream(new File(targetFolder, "extractor.log"), "UTF-8") : null;
      if (DEBUG)
      {
        for (int i = 0; i < args.length; ++i)
        {
          log.println("arg[" + i + "]='" + args[i] + "'");
        }
        log.println();

        log.println("executable=" + executable);
        log.println("targetFolder=" + targetFolder);
        log.println("javaHome=" + javaHome);
        log.println("vmArgs=" + vmArgs);
        log.println();
      }

      if (javaHome != null || vmArgs != null && !vmArgs.isEmpty())
      {
        adjustIni(targetFolder + File.separator + descriptor.getIniPath(), javaHome, vmArgs);
      }

      String[] command = new String[args.length - extraArgs - vmArgCount + 1];
      String launcher = targetFolder + File.separator + descriptor.getLauncherPath();
      command[0] = launcher;
      for (int i = extraArgs, j = 1; i < vmArgStart; ++i, ++j)
      {
        command[j] = args[i];
      }

      if (DEBUG)
      {
        for (int i = 0; i < command.length; ++i)
        {
          log.println("command[" + i + "]='" + command[i] + "'");
        }

        log.close();
      }

      Runtime.getRuntime().exec(command);
    }
  }

  private static void exit()
  {
    System.out.println("Usage: <product>.exe <product.zip> -export <marker.txt> <extractor>.exe <extractor-lib>.jar <product-descriptor> [<jre.tar.cab>]");
    System.exit(1);
  }

  private static void copy(InputStream source, File targetFile) throws IOException
  {
    File parentFile = targetFile.getParentFile();
    if (parentFile != null)
    {
      parentFile.mkdirs();
    }

    OutputStream target = new FileOutputStream(targetFile);

    try
    {
      copy(source, target);
    }
    finally
    {
      close(target);
    }
  }

  private static void adjustIni(String iniPath, String javaHome, Vector vmArgs) throws IOException
  {
    File file = new File(iniPath);
    Vector lines = new Vector();

    if (file.exists())
    {
      InputStream in = new FileInputStream(file);
      Reader reader = new InputStreamReader(in);
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

    if (javaHome != null)
    {
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
    }

    if (vmArgs != null && !vmArgs.isEmpty())
    {
      int vmargsIndex = lines.indexOf("-vmargs");
      if (vmargsIndex == -1)
      {
        lines.add("-vmargs");
        vmargsIndex = lines.size();
      }

      for (Enumeration it = vmArgs.elements(); it.hasMoreElements();)
      {
        lines.add(it.nextElement());
      }
    }

    OutputStream out = new FileOutputStream(file);

    Writer writer = new OutputStreamWriter(out);
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

    return new File(javaHome, "bin").toString();
  }
}

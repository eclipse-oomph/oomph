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

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.ZIPUtil;
import org.eclipse.oomph.util.ZIPUtil.UnzipHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;

/**
 * @author Eike Stepper
 */
public final class BreeAnalyzer
{
  private BreeAnalyzer()
  {
  }

  public static void main(String[] args) throws IOException
  {
    final Map<String, Set<String>> result = new HashMap<String, Set<String>>();

    for (final File file : new File("C:/develop/installer-test/plugins").listFiles())
    {
      if (new File(file, "META-INF/MANIFEST.MF").exists())
      {
        processFile(result, file, new File(file, "META-INF/MANIFEST.MF"));
      }
      else if (file.getName().endsWith(".jar"))
      {
        ZIPUtil.unzip(file, new UnzipHandler()
        {
          public void unzipFile(String name, InputStream zipStream) throws IOException
          {
            if (name.equals("META-INF/MANIFEST.MF"))
            {
              processStream(result, file, zipStream);
            }
          }

          public void unzipDirectory(String name) throws IOException
          {
          }
        });
      }
    }

    for (Map.Entry<String, Set<String>> entry : result.entrySet())
    {
      System.out.println(entry.getKey());
      if (args.length != 0)
      {
        for (String plugin : entry.getValue())
        {
          System.out.println("    " + plugin);
        }
      }
    }
  }

  private static void processFile(Map<String, Set<String>> result, File plugin, File file) throws IOException
  {
    InputStream in = new FileInputStream(file);

    try
    {
      processStream(result, plugin, in);
    }
    finally
    {
      in.close();
    }
  }

  private static void processStream(Map<String, Set<String>> result, File plugin, InputStream in) throws IOException
  {
    Manifest manifest = new Manifest(in);
    String value = manifest.getMainAttributes().getValue("Bundle-RequiredExecutionEnvironment");
    if (value != null)
    {
      for (String bree : value.split(","))
      {
        CollectionUtil.add(result, bree, plugin.getName());
      }
    }
  }
}

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
package org.eclipse.oomph.jreinfo;

import org.eclipse.oomph.extractor.lib.JREData;
import org.eclipse.oomph.extractor.lib.JREValidator;
import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

import org.osgi.framework.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
final class InfoManager
{
  public static final InfoManager INSTANCE = new InfoManager();

  private static final String LIB_CLASS_PATH;

  private final Map<File, JRE> infos = new HashMap<>();

  private InfoManager()
  {
    loadInfos();
  }

  public synchronized void refresh()
  {
    loadInfos();
  }

  public synchronized JRE getInfo(File canonicalJavaHome)
  {
    JRE jre = infos.get(canonicalJavaHome);
    if (jre == null)
    {
      try
      {
        if (canonicalJavaHome.isDirectory())
        {
          JREData result = testJRE(canonicalJavaHome);
          if (result != null)
          {
            int major = result.getMajor();
            int minor = result.getMinor();
            int micro = result.getMicro();
            int bitness = result.getBitness();
            String arch = result.getArch();

            boolean jdk = JREInfo.isJDK(canonicalJavaHome) == 1;

            File executable = JRE.getExecutable(canonicalJavaHome);
            long lastModified = executable.lastModified();

            jre = new JRE(canonicalJavaHome, major, minor, micro, bitness, arch, jdk, lastModified);
            infos.put(canonicalJavaHome, jre);

            try
            {
              List<String> lines = new ArrayList<>();
              for (JRE info : infos.values())
              {
                lines.add(info.toLine());
              }

              IOUtil.writeLines(getCacheFile(), "UTF-8", lines); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
              JREInfoPlugin.INSTANCE.log(ex);
            }
          }
        }
      }
      catch (Exception ex)
      {
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }

    return jre;
  }

  private void loadInfos()
  {
    infos.clear();

    File cacheFile = getCacheFile();
    if (cacheFile.isFile())
    {
      try
      {
        for (String line : IOUtil.readLines(cacheFile, "UTF-8")) //$NON-NLS-1$
        {
          try
          {
            // Ignore if the arch is missing.
            String[] tokens = line.split(JRE.SEPARATOR);
            if (tokens.length > 7)
            {
              JRE jre = new JRE(line);
              if (jre.isValid())
              {
                infos.put(jre.getJavaHome(), jre);
              }
            }
          }
          catch (RuntimeException ex)
          {
            JREInfoPlugin.INSTANCE.log(new Status(IStatus.WARNING, JREInfoPlugin.INSTANCE.getSymbolicName(),
                NLS.bind(Messages.InfoManager_InvalidJREEntry_message, cacheFile, line), ex));
          }
        }
      }
      catch (Exception ex)
      {
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }
  }

  private static File getCacheFile()
  {
    return new File(JREInfoPlugin.INSTANCE.getUserLocation().append("infos.txt").toOSString()); //$NON-NLS-1$
  }

  private static JREData testJRE(File javaHome)
  {
    String executable = JRE.getExecutable(javaHome).getAbsolutePath();
    return testJRE(executable);
  }

  static JREData testJRE(String executable)
  {
    Process process = null;

    try
    {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command(executable, "-cp", LIB_CLASS_PATH, JREValidator.class.getName()); //$NON-NLS-1$

      process = builder.start();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line = bufferedReader.readLine();
      if (line != null)
      {
        return new JREData(line);
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }
    finally
    {
      if (process != null)
      {
        IOUtil.closeSilent(process.getInputStream());
        IOUtil.closeSilent(process.getOutputStream());
        IOUtil.closeSilent(process.getErrorStream());
      }
    }

    return null;
  }

  static
  {
    StringBuilder builder = new StringBuilder();

    try
    {
      Bundle bundle = Platform.getBundle("org.eclipse.oomph.extractor.lib"); //$NON-NLS-1$
      if (bundle != null)
      {
        List<File> classPath = OomphPlugin.getClassPath(bundle);
        if (classPath != null)
        {
          for (File file : classPath)
          {
            if (builder.length() != 0)
            {
              builder.append(File.pathSeparatorChar);
            }

            builder.append(file.getAbsolutePath());
          }
        }
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    LIB_CLASS_PATH = builder.toString();
  }
}

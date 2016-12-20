/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo;

import org.eclipse.oomph.extractor.lib.JREData;
import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class JREManager
{
  public static final OSType OS_TYPE = determineOSType();

  public static final int BITNESS = determineBitness();

  public static final boolean BITNESS_CHANGEABLE = BITNESS == 64 && OS.INSTANCE.is32BitAvailable();

  public static final String JAVA_EXECUTABLE = OS_TYPE == OSType.Win ? "java.exe" : "java";

  public static final String JAVA_COMPILER = OS_TYPE == OSType.Win ? "javac.exe" : "javac";

  public static final JREManager INSTANCE = new JREManager();

  private static final String PROP_DEFAULT_JRE = "oomph.default.jres";

  private final List<String> javaHomes = new ArrayList<String>();

  private String systemJavaHome;

  private JREManager()
  {
    try
    {
      loadJavaHomes();

      for (File defaultJavaHome : getDefaultJavaHomes())
      {
        addExtraJavaHomes(defaultJavaHome.toString(), new NullProgressMonitor());
      }
    }
    catch (Throwable ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }
  }

  private Set<File> getDefaultJavaHomes()
  {
    Set<File> result = new LinkedHashSet<File>();
    String defaultJREs = PropertiesUtil.getProperty(PROP_DEFAULT_JRE);
    if (defaultJREs != null)
    {
      try
      {
        for (String defaultJRE : defaultJREs.split(File.pathSeparator))
        {
          result.add(new File(defaultJRE).getCanonicalFile());
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return result;
  }

  private void addExtraJavaHomes(List<String> extraJavaHomes, File folder, boolean root, Set<JRE> result, IProgressMonitor monitor)
  {
    JREInfoPlugin.checkCancelation(monitor);
    String path = folder.getAbsolutePath();

    File[] childFolders = folder.listFiles(new FileFilter()
    {
      public boolean accept(File pathname)
      {
        return pathname.isDirectory();
      }
    });

    try
    {
      int children = childFolders == null ? 0 : childFolders.length;
      monitor.beginTask(root ? "Searching for VMs in " + path + "..." : "", 1 + children);
      monitor.subTask(path);

      if (!javaHomes.contains(path) && !extraJavaHomes.contains(path))
      {
        File executable = new File(folder, "bin/" + JAVA_EXECUTABLE);
        if (executable.isFile())
        {
          File canonicalFolder = folder.getCanonicalFile();

          JRE info = InfoManager.INSTANCE.getInfo(canonicalFolder);
          if (info != null && info.isValid())
          {
            extraJavaHomes.add(path);
            result.add(new JRE(folder, info));
          }
        }
      }

      monitor.worked(1);

      for (int i = 0; i < children; i++)
      {
        addExtraJavaHomes(extraJavaHomes, childFolders[i], false, result, MonitorUtil.create(monitor, 1));
      }
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
    finally
    {
      monitor.done();
    }
  }

  public synchronized Set<JRE> addExtraJavaHomes(String rootFolder, IProgressMonitor monitor)
  {
    Set<JRE> result = new HashSet<JRE>();

    File folder = new File(rootFolder);
    if (folder.isDirectory())
    {
      List<String> extraJavaHomes = loadExtraJavaHomes();
      addExtraJavaHomes(extraJavaHomes, folder, true, result, monitor);

      if (!result.isEmpty())
      {
        saveExtraJavaHomes(extraJavaHomes);
      }
    }

    return result;
  }

  public synchronized void removeExtraJavaHomes(String... javaHomes)
  {
    List<String> extraJavaHomes = loadExtraJavaHomes();
    if (extraJavaHomes.removeAll(Arrays.asList(javaHomes)))
    {
      saveExtraJavaHomes(extraJavaHomes);
    }
  }

  public synchronized void refresh(boolean refreshInfos)
  {
    if (refreshInfos)
    {
      InfoManager.INSTANCE.refresh();
    }

    loadJavaHomes();
  }

  public String getDefaultJRE(int bitness, String javaVersion)
  {
    Set<File> defaultJavaHomes = getDefaultJavaHomes();
    if (!defaultJavaHomes.isEmpty())
    {
      JREFilter jreFilter = new JREFilter(javaVersion, bitness, null);
      Map<File, JRE> jres = getJREs(jreFilter);
      for (File defaultJavaHome : defaultJavaHomes)
      {
        if (jres.containsKey(defaultJavaHome))
        {
          return defaultJavaHome.toString();
        }
      }
    }

    File defaultsFile = getDefaultsFile();
    Map<String, String> properties = PropertiesUtil.getProperties(defaultsFile);
    String javaHome = properties.get(getDefaultsKey(bitness, javaVersion));
    if (javaHome == null)
    {
      javaHome = properties.get(getDefaultsKey(bitness, null));
    }

    return javaHome;
  }

  public synchronized void setDefaultJRE(int bitness, String javaVersion, String javaHome)
  {
    File defaultsFile = getDefaultsFile();
    Map<String, String> properties = PropertiesUtil.getProperties(defaultsFile);
    properties.put(getDefaultsKey(bitness, javaVersion), javaHome);
    properties.put(getDefaultsKey(bitness, null), javaHome);
    PropertiesUtil.saveProperties(defaultsFile, properties, true);
  }

  public Map<File, JRE> getJREs()
  {
    return getJREs(null);
  }

  public synchronized JRE getSystemJRE()
  {
    if (OS.INSTANCE.isWin())
    {
      if (systemJavaHome == null)
      {
        // The native launcher augments the system PATH.
        // It optionally prefixes the PATH with folder entries based on the JVM it has decided to use.
        // These entries always use '/' instead of '\' so we can recognize the entries added by the native launcher.
        List<String> folders = new ArrayList<String>();
        String path = System.getenv("PATH");
        for (String folder : path.split(File.pathSeparator))
        {
          systemJavaHome = "";

          if (!StringUtil.isEmpty(folder))
          {
            if (folder.indexOf('/') == -1)
            {
              folders.add(folder);
            }
            else
            {
              // This tests if we are in debug mode.
              // In that case, the JRE for the selected JRE of the launcher is at the front of the list, but it uses proper '\'
              // So we ignore everything before the native launcher added entries.
              boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
              if (isDebug)
              {
                folders.clear();
              }
            }
          }
        }

        // The native launcher also adds the current working directory at the end of the path.
        // We should ignore this one too.
        int size = folders.size();
        if (size > 0)
        {
          folders.remove(size - 1);
        }

        for (String folder : folders)
        {
          JREData jreData = InfoManager.testJRE(new File(folder, JAVA_EXECUTABLE).toString());
          if (jreData != null)
          {
            systemJavaHome = jreData.getJavaHome();
            break;
          }
        }
      }

      if (!StringUtil.isEmpty(systemJavaHome))
      {
        return getJREs().get(new File(systemJavaHome));
      }
    }

    return null;
  }

  public Map<File, JRE> getJREs(JREFilter filter)
  {
    Set<File> folders = getJavaHomes();
    List<JRE> jres = getJREs(filter, folders);

    LinkedHashMap<File, JRE> result = new LinkedHashMap<File, JRE>();
    for (JRE jre : jres)
    {
      result.put(jre.getJavaHome(), jre);
    }

    return result;
  }

  public JRE[] getJREs(JREFilter filter, boolean extra)
  {
    Set<File> folders = toFile(extra ? loadExtraJavaHomes() : javaHomes);
    List<JRE> jres = getJREs(filter, folders);
    return jres.toArray(new JRE[jres.size()]);
  }

  private synchronized Set<File> getJavaHomes()
  {
    Set<File> all = new HashSet<File>();
    all.addAll(toFile(javaHomes));
    all.addAll(toFile(loadExtraJavaHomes()));
    return all;
  }

  private void loadJavaHomes()
  {
    javaHomes.clear();

    JREInfo info = JREInfo.getAll();
    while (info != null)
    {
      javaHomes.add(info.javaHome);
      info = info.next;
    }
  }

  private static List<String> loadExtraJavaHomes()
  {
    if (getCacheFile().isFile())
    {
      try
      {
        return IOUtil.readLines(getCacheFile(), "UTF-8");
      }
      catch (Exception ex)
      {
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }

    return new ArrayList<String>();
  }

  private static void saveExtraJavaHomes(List<String> paths)
  {
    try
    {
      IOUtil.writeLines(getCacheFile(), "UTF-8", paths);
    }
    catch (Exception ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }
  }

  private static File getCacheFile()
  {
    return new File(JREInfoPlugin.INSTANCE.getUserLocation().append("extra.txt").toOSString());
  }

  private static File getDefaultsFile()
  {
    return new File(JREInfoPlugin.INSTANCE.getUserLocation().append("defaults.properties").toOSString());
  }

  private static String getDefaultsKey(int bitness, String javaVersion)
  {
    return Integer.toString(bitness) + "/" + sanitizeKey(javaVersion);
  }

  private static String sanitizeKey(String javaVersion)
  {
    if (javaVersion == null)
    {
      return "*";
    }

    return javaVersion.replace(' ', '_').replace('/', '_').replace('\\', '_').replace('=', '_');
  }

  private static List<JRE> getJREs(JREFilter filter, Collection<File> javaHomes)
  {
    List<JRE> list = new ArrayList<JRE>();
    for (File javaHome : javaHomes)
    {
      try
      {
        File canonicalJavaHome = javaHome.getCanonicalFile();
        JRE info = InfoManager.INSTANCE.getInfo(canonicalJavaHome);
        if (info != null && info.isValid())
        {
          if (filter == null || info.isMatch(filter))
          {
            list.add(new JRE(javaHome, info));
          }
        }
      }
      catch (IOException ex)
      {
        JREInfoPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
    }

    Collections.sort(list);
    return list;
  }

  private static Set<File> toFile(Collection<String> paths)
  {
    Set<File> result = new HashSet<File>();
    for (String javaHome : paths)
    {
      result.add(new File(javaHome));
    }

    return result;
  }

  private static OSType determineOSType()
  {
    try
    {
      String os = Platform.getOS();

      if (Platform.OS_WIN32.equals(os))
      {
        System.loadLibrary("jreinfo.dll");
        return OSType.Win;
      }

      if (Platform.OS_MACOSX.equals(os))
      {
        return OSType.Mac;
      }

      if (Platform.OS_LINUX.equals(os))
      {
        return OSType.Linux;
      }
    }
    catch (Throwable ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }

    return OSType.Unsupported;
  }

  private static int determineBitness()
  {
    try
    {
      return JREData.determineBitness();
    }
    catch (Throwable ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }

    return 32;
  }
}

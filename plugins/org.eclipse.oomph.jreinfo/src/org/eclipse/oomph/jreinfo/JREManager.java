/*
 * Copyright (c) 2015-2017 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;

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

  public static final String JAVA_EXECUTABLE = OS_TYPE == OSType.Win ? "java.exe" : "java"; //$NON-NLS-1$ //$NON-NLS-2$

  public static final String JAVA_COMPILER = OS_TYPE == OSType.Win ? "javac.exe" : "javac"; //$NON-NLS-1$ //$NON-NLS-2$

  public static final JREManager INSTANCE = new JREManager();

  public static final int CURRENT_LTS_VERSION = Integer.valueOf(21).intValue();

  private static final String PROP_DEFAULT_JRE = "oomph.default.jres"; //$NON-NLS-1$

  private final List<String> javaHomes = new ArrayList<>();

  private final List<JRE.Descriptor> descriptors = new ArrayList<>();

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
    catch (Exception ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }
  }

  private Set<File> getDefaultJavaHomes()
  {
    Set<File> result = new LinkedHashSet<>();
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
      @Override
      public boolean accept(File pathname)
      {
        return pathname.isDirectory();
      }
    });

    try
    {
      int children = childFolders == null ? 0 : childFolders.length;
      monitor.beginTask(root ? NLS.bind(Messages.JREManager_Seraching_task, path) : "", 1 + children); //$NON-NLS-1$
      monitor.subTask(path);

      if (!javaHomes.contains(path) && !extraJavaHomes.contains(path))
      {
        File executable = new File(folder, "bin/" + JAVA_EXECUTABLE); //$NON-NLS-1$
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
    Set<JRE> result = new HashSet<>();

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
    String oldVersionSpecificJavaHome = properties.put(getDefaultsKey(bitness, javaVersion), javaHome);
    String oldJavaHome = properties.put(getDefaultsKey(bitness, null), javaHome);
    if (!javaHome.equals(oldVersionSpecificJavaHome) || !javaHome.equals(oldJavaHome))
    {
      try
      {
        PropertiesUtil.saveProperties(defaultsFile, properties, true);
      }
      catch (Exception ex)
      {
        // Log and exception because failing to save defaults isn't catastrophic.
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }
  }

  public synchronized void setJREs(Collection<JRE.Descriptor> jreDescriptors)
  {
    descriptors.clear();
    descriptors.addAll(jreDescriptors);
  }

  public Map<File, JRE> getJREs()
  {
    return getJREs(null);
  }

  public synchronized JRE getSystemJRE()
  {
    if (systemJavaHome == null)
    {
      if (OS.INSTANCE.isWin())
      {
        // The native launcher augments the system PATH.
        // It optionally prefixes the PATH with folder entries based on the JVM it has decided to use.
        // These entries always use '/' instead of '\' so we can recognize the entries added by the native launcher.
        List<String> folders = new ArrayList<>();
        String path = System.getenv("PATH"); //$NON-NLS-1$
        for (String folder : path.split(File.pathSeparator))
        {
          systemJavaHome = ""; //$NON-NLS-1$

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
              boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0; //$NON-NLS-1$
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
      else if (OS.INSTANCE.isLinux())
      {
        try
        {
          File javaExecuable = new File("/usr/bin/java").getCanonicalFile(); //$NON-NLS-1$
          JREData jreData = InfoManager.testJRE(javaExecuable.toString());
          if (jreData != null)
          {
            systemJavaHome = jreData.getJavaHome();
          }
        }
        catch (IOException ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    if (!StringUtil.isEmpty(systemJavaHome))
    {
      return getJREs().get(new File(systemJavaHome));
    }

    return null;
  }

  public Map<File, JRE> getJREs(JREFilter filter)
  {
    Set<File> folders = getJavaHomes();
    List<JRE> jres = getJREs(filter, folders);

    LinkedHashMap<File, JRE> result = new LinkedHashMap<>();
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

  public JRE[] getAllJREs(JREFilter filter)
  {
    List<JRE> jres = new ArrayList<>();
    for (JRE.Descriptor descriptor : descriptors)
    {
      JRE jre = new JRE(descriptor);
      if (jre.isMatch(filter))
      {
        jres.add(jre);
      }
    }

    Collections.sort(jres);

    jres.addAll(0, getJREs(filter).values());

    return jres.toArray(new JRE[jres.size()]);
  }

  private synchronized Set<File> getJavaHomes()
  {
    Set<File> all = new HashSet<>();
    all.addAll(toFile(javaHomes));
    all.addAll(toFile(loadExtraJavaHomes()));
    return all;
  }

  private void loadJavaHomes()
  {
    javaHomes.clear();

    File installerLocation = getInstallerLocation();
    JREInfo info = JREInfo.getAll();
    while (info != null)
    {
      // Duplicates and the JRE that is embedded in the installer itself.
      if (!javaHomes.contains(info.javaHome) && (installerLocation == null || !isAncestor(installerLocation, new File(info.javaHome))))
      {
        javaHomes.add(info.javaHome);
      }

      info = info.next;
    }
  }

  private boolean isAncestor(File parent, File child)
  {
    for (File file = IOUtil.getCanonicalFile(child); file != null; file = file.getParentFile())
    {
      if (parent.equals(file))
      {
        return true;
      }
    }

    return false;
  }

  private static List<String> loadExtraJavaHomes()
  {
    if (getCacheFile().isFile())
    {
      try
      {
        return IOUtil.readLines(getCacheFile(), "UTF-8"); //$NON-NLS-1$
      }
      catch (Exception ex)
      {
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }

    return new ArrayList<>();
  }

  private static void saveExtraJavaHomes(List<String> paths)
  {
    try
    {
      IOUtil.writeLines(getCacheFile(), "UTF-8", paths); //$NON-NLS-1$
    }
    catch (Exception ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }
  }

  private static File getCacheFile()
  {
    return new File(JREInfoPlugin.INSTANCE.getUserLocation().append("extra.txt").toOSString()); //$NON-NLS-1$
  }

  private static File getDefaultsFile()
  {
    return new File(JREInfoPlugin.INSTANCE.getUserLocation().append("defaults.properties").toOSString()); //$NON-NLS-1$
  }

  private static String getDefaultsKey(int bitness, String javaVersion)
  {
    return Integer.toString(bitness) + "/" + sanitizeKey(javaVersion); //$NON-NLS-1$
  }

  private static String sanitizeKey(String javaVersion)
  {
    if (javaVersion == null)
    {
      return "*"; //$NON-NLS-1$
    }

    return javaVersion.replace(' ', '_').replace('/', '_').replace('\\', '_').replace('=', '_');
  }

  private static List<JRE> getJREs(JREFilter filter, Collection<File> javaHomes)
  {
    List<JRE> list = new ArrayList<>();
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
        JREInfoPlugin.INSTANCE
            .log(new Status(IStatus.WARNING, JREInfoPlugin.INSTANCE.getSymbolicName(), NLS.bind(Messages.JREManager_Problem_message, javaHome), ex));
      }
    }

    Collections.sort(list);
    return list;
  }

  private static Set<File> toFile(Collection<String> paths)
  {
    Set<File> result = new HashSet<>();
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
    catch (RuntimeException ex)
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
    catch (RuntimeException ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }

    return 32;
  }

  private static File getInstallerLocation()
  {
    try
    {
      String productID = PropertiesUtil.getProductID();
      if ("org.eclipse.oomph.setup.installer.product".equals(productID)) //$NON-NLS-1$
      {
        Location location = Platform.getInstallLocation();
        if (location != null)
        {
          URI result = URI.createURI(FileLocator.resolve(location.getURL()).toString());
          if (result.isFile())
          {
            if (!result.hasTrailingPathSeparator())
            {
              result = result.appendSegment(""); //$NON-NLS-1$
            }
            return IOUtil.getCanonicalFile(new File(result.toFileString()));
          }
        }
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }
}

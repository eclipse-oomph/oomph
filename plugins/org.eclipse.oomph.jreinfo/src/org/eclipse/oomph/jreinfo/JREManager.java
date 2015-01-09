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
package org.eclipse.oomph.jreinfo;

import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class JREManager
{
  public static final JREManager INSTANCE = new JREManager();

  private final List<String> javaHomes = new ArrayList<String>();

  private JREManager()
  {
    loadJavaHomes();
  }

  public synchronized void addExtraJavaHomes(String... paths)
  {
    List<String> extraJavaHomes = loadExtraJavaHomes();
    if (extraJavaHomes.addAll(Arrays.asList(paths)))
    {
      saveExtraJavaHomes(extraJavaHomes);
    }
  }

  public synchronized void removeExtraJavaHomes(String... paths)
  {
    List<String> extraJavaHomes = loadExtraJavaHomes();
    if (extraJavaHomes.removeAll(Arrays.asList(paths)))
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

  public LinkedHashMap<File, JRE> getJREs()
  {
    return getJREs(0, 0, 0, null, null);
  }

  public LinkedHashMap<File, JRE> getJREs(int major, int minor, int micro, Integer bitness, Boolean jdk)
  {
    LinkedHashMap<File, JRE> jres = new LinkedHashMap<File, JRE>();
    for (JRE jre : getValidJREs())
    {
      if (jre.getMajor() < major)
      {
        continue;
      }

      if (jre.getMinor() < minor)
      {
        continue;
      }

      if (jre.getMicro() < micro)
      {
        continue;
      }

      if (bitness != null && jre.getBitness() != bitness)
      {
        continue;
      }

      if (jdk != null && jre.isJDK() != jdk)
      {
        continue;
      }

      jres.put(jre.getJavaHome(), jre);
    }

    return jres;
  }

  private List<JRE> getValidJREs()
  {
    List<JRE> list = new ArrayList<JRE>();
    for (File javaHome : getJavaHomes())
    {
      try
      {
        File canonicalJavaHome = javaHome.getCanonicalFile();
        JRE info = InfoManager.INSTANCE.getInfo(canonicalJavaHome);
        if (info != null && info.isValid())
        {
          list.add(new JRE(javaHome, info));
        }
      }
      catch (IOException ex)
      {
        JREInfoPlugin.INSTANCE.log(ex);
      }
    }

    Collections.sort(list);
    return list;
  }

  private synchronized Set<File> getJavaHomes()
  {
    Set<File> all = new HashSet<File>();
    getJavaHomes(all, javaHomes);
    getJavaHomes(all, loadExtraJavaHomes());
    return all;
  }

  private void getJavaHomes(Set<File> all, List<String> javaHomes)
  {
    for (String javaHome : javaHomes)
    {
      all.add(new File(javaHome));
    }
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

    return Collections.emptyList();
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
}

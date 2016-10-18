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

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class JRE implements Comparable<JRE>
{
  private static final String SEPARATOR = File.pathSeparator;

  private final File javaHome;

  private final int major;

  private final int minor;

  private final int micro;

  private final int bitness;

  private final boolean jdk;

  private final long lastModified;

  public JRE(File javaHome, int major, int minor, int micro, int bitness, boolean jdk, long lastModified)
  {
    this.javaHome = javaHome;
    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    this.jdk = jdk;
    this.lastModified = lastModified;
  }

  JRE(File javaHome, JRE info)
  {
    this.javaHome = javaHome;
    major = info.major;
    minor = info.minor;
    micro = info.micro;
    bitness = info.bitness;
    jdk = info.jdk;
    lastModified = info.lastModified;
  }

  JRE(String line)
  {
    String[] tokens = line.split(SEPARATOR);
    javaHome = new File(tokens[0]);
    major = Integer.parseInt(tokens[1]);
    minor = Integer.parseInt(tokens[2]);
    micro = Integer.parseInt(tokens[3]);
    bitness = Integer.parseInt(tokens[4]);
    jdk = Boolean.parseBoolean(tokens[5]);
    lastModified = Long.parseLong(tokens[6]);
  }

  public File getJavaHome()
  {
    return javaHome;
  }

  public File getJavaExecutable()
  {
    return getExecutable(javaHome);
  }

  public int getMajor()
  {
    return major;
  }

  public int getMinor()
  {
    return minor;
  }

  public int getMicro()
  {
    return micro;
  }

  public int getBitness()
  {
    return bitness;
  }

  public boolean isJDK()
  {
    return jdk;
  }

  public boolean isValid()
  {
    File executable = getJavaExecutable();
    if (!executable.isFile())
    {
      return false;
    }

    if (executable.lastModified() != lastModified)
    {
      return false;
    }

    return true;
  }

  public boolean isCurrent()
  {
    String systemJavaHome = System.getProperty("java.home");
    return javaHome.getPath().equals(systemJavaHome);
  }

  public boolean isMatch(JREFilter filter)
  {
    Integer filterBitness = filter.getBitness();
    if (filterBitness != null && bitness != filterBitness.intValue())
    {
      return false;
    }

    Boolean filterJDK = filter.isJDK();
    if (filterJDK != null && jdk != filterJDK.booleanValue())
    {
      return false;
    }

    Integer filterMajor = filter.getMajor();
    if (filterMajor == null)
    {
      return true;
    }

    if (major < filterMajor)
    {
      return false;
    }

    if (major > filterMajor)
    {
      return true;
    }

    Integer filterMinor = filter.getMinor();
    if (filterMinor == null)
    {
      return true;
    }

    if (minor < filterMinor)
    {
      return false;
    }

    if (minor > filterMinor)
    {
      return true;
    }

    Integer filterMicro = filter.getMicro();
    if (filterMicro == null)
    {
      return true;
    }

    if (micro < filterMicro)
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (javaHome == null ? 0 : javaHome.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    JRE other = (JRE)obj;
    if (javaHome == null)
    {
      if (other.javaHome != null)
      {
        return false;
      }
    }
    else if (!javaHome.equals(other.javaHome))
    {
      return false;
    }

    return true;
  }

  public int compareTo(JRE o)
  {
    int result = o.major - major;
    if (result == 0)
    {
      result = o.minor - minor;
      if (result == 0)
      {
        result = o.micro - micro;
        if (result == 0)
        {
          result = o.bitness - bitness;
          if (result == 0)
          {
            result = (o.jdk ? 1 : 0) - (jdk ? 1 : 0);
          }
        }
      }
    }

    return result;
  }

  @Override
  public String toString()
  {
    return javaHome.getPath() + (isCurrent() ? " (Current)" : "");
  }

  String toLine()
  {
    return javaHome.getAbsolutePath() + SEPARATOR + major + SEPARATOR + minor + SEPARATOR + micro + SEPARATOR + bitness + SEPARATOR + jdk + SEPARATOR
        + lastModified;
  }

  static File getExecutable(File javaHome)
  {
    return new File(javaHome, "bin/" + JREManager.JAVA_EXECUTABLE);
  }
}

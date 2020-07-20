/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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

  private final Descriptor descriptor;

  private final int major;

  private final int minor;

  private final int micro;

  private final int bitness;

  private final boolean jdk;

  private final long lastModified;

  public JRE(File javaHome, int major, int minor, int micro, int bitness, boolean jdk, long lastModified)
  {
    this.javaHome = javaHome;
    descriptor = null;
    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    this.jdk = jdk;
    this.lastModified = lastModified;
  }

  JRE(Descriptor descriptor)
  {
    javaHome = null;
    this.descriptor = descriptor;
    major = descriptor.getMajor();
    minor = descriptor.getMinor();
    micro = descriptor.getMicro();
    bitness = descriptor.getBitness();
    jdk = descriptor.isJDK();
    lastModified = -1;
  }

  JRE(File javaHome, JRE info)
  {
    this.javaHome = javaHome;
    descriptor = null;
    major = info.major;
    minor = info.minor;
    micro = info.micro;
    bitness = info.bitness;
    jdk = info.jdk;
    lastModified = info.lastModified;
  }

  JRE(String line)
  {
    descriptor = null;
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

  public Descriptor getDescriptor()
  {
    return descriptor;
  }

  public File getJavaExecutable()
  {
    return javaHome == null ? null : getExecutable(javaHome);
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
    if (descriptor != null)
    {
      return true;
    }

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
    if (descriptor != null)
    {
      return false;
    }

    String systemJavaHome = System.getProperty("java.home"); //$NON-NLS-1$
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

    Boolean filterDescriptor = filter.isDescriptor();
    if (filterDescriptor != null && descriptor != null != filterDescriptor.booleanValue())
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
    result = prime * result + (descriptor == null ? 0 : descriptor.hashCode());
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

    if (descriptor == null)
    {
      if (other.descriptor != null)
      {
        return false;
      }
    }
    else if (!descriptor.equals(other.descriptor))
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
    if (descriptor != null)
    {
      return descriptor.getLabel();
    }

    return javaHome.getPath() + (isCurrent() ? " " + Messages.JRE_Current_message : ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  String toLine()
  {
    return javaHome.getAbsolutePath() + SEPARATOR + major + SEPARATOR + minor + SEPARATOR + micro + SEPARATOR + bitness + SEPARATOR + jdk + SEPARATOR
        + lastModified;
  }

  static File getExecutable(File javaHome)
  {
    return new File(javaHome, "bin/" + JREManager.JAVA_EXECUTABLE); //$NON-NLS-1$
  }

  /**
   * @author Ed Merks
   */
  public final static class Descriptor
  {
    private String label;

    private final int major;

    private final int minor;

    private final int micro;

    private int bitness;

    private final boolean jdk;

    private final Object data;

    public Descriptor(String label, int major, int minor, int micro, int bitness, boolean jdk, Object data)
    {
      this.label = label;
      this.data = data;
      this.major = major;
      this.minor = minor;
      this.micro = micro;
      this.bitness = bitness;
      this.jdk = jdk;
    }

    public String getLabel()
    {
      return label;
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

    public Object getData()
    {
      return data;
    }

    @Override
    public String toString()
    {
      return label;
    }
  }
}

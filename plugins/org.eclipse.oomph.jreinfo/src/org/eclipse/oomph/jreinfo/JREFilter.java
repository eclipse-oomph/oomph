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

import org.eclipse.oomph.util.StringUtil;

/**
 * @author Eike Stepper
 */
public final class JREFilter
{
  private final Integer major;

  private final Integer minor;

  private final Integer micro;

  private final Integer bitness;

  private final Boolean jdk;

  public JREFilter()
  {
    this((Integer)null, (Integer)null, (Integer)null);
  }

  public JREFilter(Integer major, Integer minor, Integer micro)
  {
    this(major, minor, micro, null);
  }

  public JREFilter(Integer major, Integer minor, Integer micro, Integer bitness)
  {
    this(major, minor, micro, bitness, null);
  }

  public JREFilter(Integer major, Integer minor, Integer micro, Integer bitness, Boolean jdk)
  {
    if (major == null && minor != null)
    {
      throw new IllegalArgumentException("Minor can't be non-null if major is null");
    }

    if (minor == null && micro != null)
    {
      throw new IllegalArgumentException("Micro can't be non-null if minor is null");
    }

    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    this.jdk = jdk;
  }

  public JREFilter(int bitness)
  {
    this(null, null, null, bitness, null);
  }

  public JREFilter(boolean jdk)
  {
    this(null, null, null, null, jdk);
  }

  public JREFilter(String version, Integer bitness, Boolean jdk)
  {
    if (StringUtil.isEmpty(version))
    {
      major = null;
      minor = null;
      micro = null;
    }
    else
    {
      String[] tokens = version.split("\\.");
      major = getSegment(tokens, 0);
      minor = getSegment(tokens, 1);
      micro = getSegment(tokens, 2);
    }

    this.bitness = bitness;
    this.jdk = jdk;
  }

  public Integer getMajor()
  {
    return major;
  }

  public Integer getMinor()
  {
    return minor;
  }

  public Integer getMicro()
  {
    return micro;
  }

  public Integer getBitness()
  {
    return bitness;
  }

  public Boolean isJDK()
  {
    return jdk;
  }

  public String getQuery()
  {
    return safe(JREManager.OS_TYPE.ordinal()) + "_" + safe(major) + "_" + safe(minor) + "_" + safe(micro) + "_" + safe(bitness) + "_"
        + (Boolean.TRUE.equals(jdk) ? "1" : "0");
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    if (major != null)
    {
      builder.append(major);
    }

    if (minor != null)
    {
      builder.append("." + minor);
    }

    if (micro != null)
    {
      builder.append("." + micro);
    }

    if (bitness != null)
    {
      if (builder.length() != 0)
      {
        builder.append(" ");
      }

      builder.append(bitness);
      builder.append(" Bit");
    }

    if (builder.length() != 0)
    {
      builder.append(" ");
    }

    if (jdk != null)
    {
      builder.append(jdk ? " JDK" : " JRE");
    }
    else
    {
      builder.append("VM");
    }

    return builder.toString();
  }

  private static Integer getSegment(String[] tokens, int i)
  {
    if (tokens.length > i)
    {
      try
      {
        return Integer.parseInt(tokens[i]);
      }
      catch (NumberFormatException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return null;
  }

  private static String safe(Integer parameter)
  {
    return parameter == null ? "0" : parameter.toString();
  }
}

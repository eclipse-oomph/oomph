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

  private final Boolean descriptor;

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
      throw new IllegalArgumentException(Messages.JREFilter_BadMinor_exception);
    }

    if (minor == null && micro != null)
    {
      throw new IllegalArgumentException(Messages.JREFilter_BadMicro_exception);
    }

    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    this.jdk = jdk;
    descriptor = Boolean.FALSE;
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
    this(version, bitness, jdk, Boolean.FALSE);
  }

  public JREFilter(String version, Integer bitness, Boolean jdk, Boolean descriptor)
  {
    if (StringUtil.isEmpty(version))
    {
      major = null;
      minor = null;
      micro = null;
    }
    else
    {
      String[] tokens = version.split("\\."); //$NON-NLS-1$
      major = getSegment(tokens, 0);
      minor = getSegment(tokens, 1);
      micro = getSegment(tokens, 2);
    }

    this.bitness = bitness;
    this.jdk = jdk;
    this.descriptor = descriptor;
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

  public Boolean isDescriptor()
  {
    return descriptor;
  }

  public String getQuery()
  {
    return safe(JREManager.OS_TYPE.ordinal()) + "_" + safe(major) + "_" + safe(minor) + "_" + safe(micro) + "_" + safe(bitness) + "_" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        + (Boolean.TRUE.equals(jdk) ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$
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
      builder.append("." + minor); //$NON-NLS-1$
    }

    if (micro != null)
    {
      builder.append("." + micro); //$NON-NLS-1$
    }

    if (bitness != null)
    {
      if (builder.length() != 0)
      {
        builder.append(" "); //$NON-NLS-1$
      }

      builder.append(bitness);
      builder.append(" Bit"); //$NON-NLS-1$
    }

    if (builder.length() != 0)
    {
      builder.append(" "); //$NON-NLS-1$
    }

    if (jdk != null)
    {
      builder.append(jdk ? " JDK" : " JRE"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    else
    {
      builder.append("VM"); //$NON-NLS-1$
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
    return parameter == null ? "0" : parameter.toString(); //$NON-NLS-1$
  }
}

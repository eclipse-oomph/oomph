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
package org.eclipse.oomph.extractor.lib;

/**
 * @author Eike Stepper
 */
public final class JREData
{
  private final int major;

  private final int minor;

  private final int micro;

  private final int bitness;

  private final String javaHome;

  public JREData(int major, int minor, int micro, int bitness)
  {
    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    javaHome = "";
  }

  public JREData(String args)
  {
    this(args.split(" "));
  }

  public JREData(String[] args)
  {
    major = parseInt(args[0]);
    minor = parseInt(args[1]);
    micro = parseInt(args[2]);
    bitness = parseInt(args[3]);
    if (args.length > 4)
    {
      javaHome = args[4].replace("%25", "%").replace("%20", " ");
    }
    else
    {
      javaHome = "";
    }
  }

  public JREData()
  {
    String version = System.getProperty("java.version");
    String[] segments = version.split("\\.");

    if (segments.length > 0)
    {
      major = parseInt(segments[0]);

      if (segments.length > 1)
      {
        minor = parseInt(segments[1]);

        if (segments.length > 2)
        {
          micro = parseInt(segments[2]);
        }
        else
        {
          micro = 0;
        }
      }
      else
      {
        minor = 0;
        micro = 0;
      }
    }
    else
    {
      major = 0;
      minor = 0;
      micro = 0;
    }

    bitness = determineBitness();
    javaHome = System.getProperty("java.home");
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

  public String getJavaHome()
  {
    return javaHome;
  }

  public boolean satisfies(JREData requirement)
  {
    if (bitness != requirement.bitness)
    {
      return false;
    }

    if (major < requirement.major)
    {
      return false;
    }

    if (major > requirement.major)
    {
      return true;
    }

    if (minor < requirement.minor)
    {
      return false;
    }

    if (minor > requirement.minor)
    {
      return true;
    }

    if (micro < requirement.micro)
    {
      return false;
    }

    return true;
  }

  public String toString()
  {
    StringBuffer result = new StringBuffer();
    result.append(major);
    result.append(' ');
    result.append(minor);
    result.append(' ');
    result.append(micro);
    result.append(' ');
    result.append(bitness);

    if (!"".equals(javaHome))
    {
      result.append(' ');

      for (int i = 0, length = javaHome.length(); i < length; ++i)
      {
        char character = javaHome.charAt(i);
        switch (character)
        {
          case ' ':
          {
            result.append("%20");
            break;
          }
          case '%':
          {
            result.append("%25");
            break;
          }
          default:
          {
            result.append(character);
            break;
          }
        }
      }
    }

    return result.toString();
  }

  public static int determineBitness()
  {
    if ("64".equals(System.getProperty("sun.arch.data.model")))
    {
      return 64;
    }

    if ("64".equals(System.getProperty("com.ibm.vm.bitmode")))
    {
      return 64;
    }

    if (System.getProperty("os.arch").endsWith("64")) // Don't use contains() because of ARCH_IA64_32!
    {
      return 64;
    }

    return 32;
  }

  private static int parseInt(String string)
  {
    for (int i = 0; i < string.length(); i++)
    {
      if (!Character.isDigit(string.charAt(i)))
      {
        string = string.substring(0, i);
        break;
      }
    }

    return Integer.parseInt(string);
  }
}

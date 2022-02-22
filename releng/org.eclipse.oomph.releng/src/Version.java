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

/**
 * @author Eike Stepper
 */
public final class Version implements Comparable<Version>
{
  private final Integer major;

  private final Integer minor;

  private final Integer micro;

  public Version(String name)
  {
    for (int i = 0; i < name.length(); i++)
    {
      char c = name.charAt(i);
      if (!Character.isDigit(c) && c != '.')
      {
        name = name.substring(0, i);
        break;
      }
    }

    String[] segments = name.split("\\.");
    major = Integer.parseInt(segments[0]);
    minor = Integer.parseInt(segments[1]);
    micro = Integer.parseInt(segments[2]);
  }

  public boolean isGreaterThan(Version other)
  {
    return compareTo(other) > 0;
  }

  @Override
  public int compareTo(Version o)
  {
    int result = major.compareTo(o.major);
    if (result == 0)
    {
      result = minor.compareTo(o.minor);
      if (result == 0)
      {
        result = micro.compareTo(o.micro);
      }
    }

    return result;
  }

  @Override
  public String toString()
  {
    return major + "." + minor + "." + micro;
  }
}

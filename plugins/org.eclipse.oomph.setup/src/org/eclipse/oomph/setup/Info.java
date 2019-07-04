/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.util.StringUtil;

/**
 * @author Eike Stepper
 */
public class Info
{
  private static final String SEPARATOR = " ";

  private final String uuid;

  private final String name;

  protected Info(String string)
  {
    if (string == null)
    {
      throw new IllegalArgumentException("string is null"); //$NON-NLS-1$
    }

    int pos = string.indexOf(SEPARATOR);
    if (pos != -1)
    {
      uuid = string.substring(0, pos);
      name = string.substring(pos + 1).trim();
    }
    else
    {
      uuid = string;
      name = null;
    }
  }

  protected Info(String uuid, String name)
  {
    if (uuid == null)
    {
      throw new IllegalArgumentException("uuid is null"); //$NON-NLS-1$
    }

    if (uuid.indexOf(' ') != -1)
    {
      throw new IllegalArgumentException("uuid contains a space"); //$NON-NLS-1$
    }

    this.uuid = uuid;
    this.name = name;
  }

  public String getUUID()
  {
    return uuid;
  }

  public String getName()
  {
    return name;
  }

  public String getLabel()
  {
    if (!StringUtil.isEmpty(name))
    {
      return name;
    }

    return uuid;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (uuid == null ? 0 : uuid.hashCode());
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

    Info other = (Info)obj;
    if (uuid == null)
    {
      if (other.uuid != null)
      {
        return false;
      }
    }
    else if (!uuid.equals(other.uuid))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    String string = uuid;
    if (!StringUtil.isEmpty(name))
    {
      string += SEPARATOR + name;
    }

    return string;
  }
}

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

/**
 * @author Eike Stepper
 */
public final class LicenseInfo extends Info
{
  public LicenseInfo(String string)
  {
    super(string);
  }

  public LicenseInfo(String uuid, String name)
  {
    super(uuid, name);
  }

  public static String getFirstLine(String body)
  {
    int i = body.indexOf('\n');
    int j = body.indexOf('\r');
    if (i > 0)
    {
      if (j > 0)
      {
        return body.substring(0, i < j ? i : j);
      }

      return body.substring(0, i);
    }

    if (j > 0)
    {
      return body.substring(0, j);
    }

    return body;
  }
}

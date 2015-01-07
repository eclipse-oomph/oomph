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
package org.eclipse.oomph.extractor.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Eike Stepper
 */
public final class BINDescriptor
{
  private final JRE jre;

  private final int jdk;

  private final String launcherPath;

  private final String iniPath;

  public BINDescriptor(InputStream in) throws IOException
  {
    Reader reader = new InputStreamReader(in, "UTF-8");

    String string = readTokens(reader, 5);
    String[] tokens = string.split(" ");

    jre = new JRE(tokens);
    jdk = Integer.parseInt(tokens[4]);
    launcherPath = readTokens(reader, 1);
    iniPath = readTokens(reader, 1);
  }

  public JRE getJRE()
  {
    return jre;
  }

  public int getJDK()
  {
    return jdk;
  }

  public String getLauncherPath()
  {
    return launcherPath;
  }

  public String getIniPath()
  {
    return iniPath;
  }

  private static String readTokens(Reader reader, int count) throws IOException
  {
    StringBuilder builder = new StringBuilder();
    char[] buf = { 0 };
    boolean quotes = false;

    while (reader.read(buf) == 1)
    {
      if (buf[0] == '"')
      {
        quotes = !quotes;
        continue;
      }

      if (!quotes && Character.isWhitespace(buf[0]) && --count == 0)
      {
        break;
      }

      builder.append(buf[0]);
    }

    return builder.toString().trim();
  }
}

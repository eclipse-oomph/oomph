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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Eike Stepper
 */
public final class BINDescriptor
{
  private final int format;

  private final JREData jre;

  private final int jdk;

  private final String launcherPath;

  private final String iniPath;

  private final String productName;

  private final String productURI;

  private final String imageURI;

  public BINDescriptor(InputStream in) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

    format = readInt(reader);

    int major = readInt(reader);
    int minor = readInt(reader);
    int micro = readInt(reader);
    int bitness = readInt(reader);

    jre = new JREData(major, minor, micro, bitness);
    jdk = readInt(reader);

    launcherPath = reader.readLine();
    iniPath = reader.readLine();
    productName = reader.readLine();
    productURI = reader.readLine();
    imageURI = reader.readLine();
  }

  public int getFormat()
  {
    return format;
  }

  public JREData getJRE()
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

  public String getProductName()
  {
    return productName;
  }

  public String getProductURI()
  {
    return productURI;
  }

  public String getImageURI()
  {
    return imageURI;
  }

  private static int readInt(BufferedReader reader) throws IOException
  {
    return Integer.parseInt(reader.readLine());
  }
}

/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.extractor.lib.tests;

import org.eclipse.oomph.extractor.lib.BINDescriptor;
import org.eclipse.oomph.extractor.lib.JREData;

import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Eike Stepper
 */
public class BINDescriptorTest
{
  public static void main(String[] args) throws IOException
  {
    int major = 1;
    int minor = 7;
    int micro = 0;
    int bitness = 64;
    int jdk = 0;
    String launcherPath = "eclipse-installer.exe";
    String iniPath = "eclipse-installer.ini";

    String content = major + " " + minor + " " + micro + " " + bitness + " " + jdk + " " + launcherPath + " " + iniPath;
    InputStream stream = new ByteArrayInputStream(content.getBytes());
    BINDescriptor descriptor = new BINDescriptor(stream);

    JREData jre = descriptor.getJRE();
    Assert.assertEquals("major", major, jre.getMajor());
    Assert.assertEquals("minor", minor, jre.getMinor());
    Assert.assertEquals("micro", micro, jre.getMicro());
    Assert.assertEquals("bitness", bitness, jre.getBitness());
    Assert.assertEquals("jdk", jdk, descriptor.getJDK());
    Assert.assertEquals("launcherPath", launcherPath, descriptor.getLauncherPath());
    Assert.assertEquals("iniPath", iniPath, descriptor.getIniPath());

    System.out.println("SUCCESS");
  }
}

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author Eike Stepper
 */
public final class PropertiesUpdater extends IO
{
  public static void main(String[] args) throws Exception
  {
    String source = args[0];
    String target = args[1];

    Properties sourceProperties = new Properties();
    sourceProperties.load(new FileInputStream(source));

    FileInputStream targetInputStream = new FileInputStream(target);
    Properties targetProperties;
    try
    {
      targetProperties = new Properties();
      targetProperties.load(targetInputStream);
    }
    finally
    {
      close(targetInputStream);
    }

    FileOutputStream targetOutputStream = new FileOutputStream(target);

    try
    {
      targetProperties.putAll(sourceProperties);
      targetProperties.store(targetOutputStream, "This configuration file was written by: org.eclipse.oomph.extractor.lib.PropertiesUpdater");
    }
    finally
    {
      close(targetOutputStream);
    }
  }
}

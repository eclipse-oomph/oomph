/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class PropertyFile
{
  private final File file;

  public PropertyFile(File file)
  {
    this.file = file;
  }

  public String getProperty(String key, String defaultValue)
  {
    Map<String, String> properties = loadProperties();
    String value = properties.get(key);
    return value == null ? defaultValue : value;
  }

  public void setProperty(String key, String value)
  {
    Map<String, String> properties = loadProperties();
    addAndSaveProperty(properties, key, value);
  }

  public boolean compareAndSetProperty(String key, String value, String... expectedValues)
  {
    Map<String, String> properties = loadProperties();
    String existingValue = properties.get(key);

    if (expectedValues.length == 0)
    {
      if (ObjectUtil.equals(existingValue, null))
      {
        addAndSaveProperty(properties, key, value);
        return true;
      }
    }
    else
    {
      for (String expectedValue : expectedValues)
      {
        if (ObjectUtil.equals(existingValue, expectedValue))
        {
          addAndSaveProperty(properties, key, value);
          return true;
        }
      }
    }

    return false;
  }

  public void removeProperty(String key)
  {
    Map<String, String> properties = loadProperties();
    if (properties.remove(key) != null)
    {
      saveProperties(properties);
    }
  }

  public Map<String, String> loadProperties()
  {
    try
    {
      if (file.exists())
      {
        return PropertiesUtil.loadProperties(file);
      }
    }
    catch (RuntimeException ex)
    {
      // Ignore.
    }

    return new LinkedHashMap<String, String>();
  }

  public void saveProperties(Map<String, String> properties)
  {
    try
    {
      PropertiesUtil.saveProperties(file, properties, true);
    }
    catch (RuntimeException ex)
    {
      UtilPlugin.INSTANCE.log(ex);
    }
  }

  private void addAndSaveProperty(Map<String, String> properties, String key, String value)
  {
    properties.put(key, value.toString());
    saveProperties(properties);
  }
}

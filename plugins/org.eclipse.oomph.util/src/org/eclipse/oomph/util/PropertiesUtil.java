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
package org.eclipse.oomph.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class PropertiesUtil
{
  public static final String USER_HOME = getProperty("user.home", ".");

  public static final String[] EXPERT_FILTER = { "org.eclipse.ui.views.properties.expert" };

  private static final String TRUE = Boolean.TRUE.toString();

  private PropertiesUtil()
  {
  }

  public static Boolean getBoolean(String key)
  {
    String property = getProperty(key);
    if (property != null)
    {
      return TRUE.equalsIgnoreCase(property);
    }

    return null;
  }

  public static boolean isProperty(String key)
  {
    String property = getProperty(key);
    if (property != null)
    {
      return TRUE.equalsIgnoreCase(property);
    }

    return false;
  }

  public static String getProperty(String key)
  {
    return getProperty(key, null);
  }

  public static String getProperty(String key, String defaultValue)
  {
    String value = System.getProperty(key);
    if (value == null)
    {
      value = System.getenv(key);
      if (value == null && key.indexOf('.') != -1)
      {
        key = key.replace('.', '_');
        value = getProperty(key, defaultValue);
      }
    }

    if (value == null)
    {
      value = defaultValue;
    }

    return value;
  }

  public static void saveProperties(File file, Map<String, String> properties, boolean sort)
  {
    FileWriter fileWriter = null;

    try
    {
      fileWriter = new FileWriter(file);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

      List<String> keys = new ArrayList<String>(properties.keySet());
      if (sort)
      {
        Collections.sort(keys);
      }

      for (String key : keys)
      {
        String value = properties.get(key);
        String line = StringUtil.implode(Arrays.asList(key, value), '=');
        bufferedWriter.write(line);
        bufferedWriter.write(StringUtil.NL);
      }

      bufferedWriter.close();
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.closeSilent(fileWriter);
    }
  }

  public static Map<String, String> loadProperties(File file)
  {
    FileReader fileReader = null;
  
    try
    {
      fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
  
      Map<String, String> properties = new LinkedHashMap<String, String>();
  
      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        List<String> tokens = StringUtil.explode(line, "=");
        int size = tokens.size();
        if (size > 0)
        {
          String key = tokens.get(0);
          String value = null;
  
          if (size == 2)
          {
            value = tokens.get(1);
          }
          else if (size > 2)
          {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < size; i++)
            {
              if (builder.length() != 0)
              {
                builder.append("=");
              }
  
              builder.append(tokens.get(i));
            }
  
            value = builder.toString();
          }
  
          properties.put(key, value);
        }
      }
  
      return properties;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.closeSilent(fileReader);
    }
  }

  public static Map<String, String> getProperties(File file)
  {
    if (file.isFile())
    {
      return loadProperties(file);
    }

    return new LinkedHashMap<String, String>();
  }
}

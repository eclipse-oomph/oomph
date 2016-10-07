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
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class StringFilterRegistry
{
  public static final StringFilterRegistry INSTANCE = new StringFilterRegistry();

  private static final Pattern CAMEL_PATTERN = Pattern.compile("(?:[^\\p{Alnum}]+|^)(\\p{Lower})?");

  private final Map<String, StringFilter> filters = new HashMap<String, StringFilter>();

  private StringFilterRegistry()
  {
    registerFilter("file", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.createURI(value).toFileString();
      }
    });

    registerFilter("uri", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.createFileURI(value).toString();
      }
    });

    registerFilter("uriLastSegment", new StringFilter()
    {
      public String filter(String value)
      {
        URI uri = URI.createURI(value);
        if (!uri.isHierarchical())
        {
          uri = URI.createURI(uri.opaquePart());
        }

        return URI.decode(uri.lastSegment());
      }
    });

    registerFilter("gitRepository", new StringFilter()
    {
      public String filter(String value)
      {
        URI uri = URI.createURI(value);
        if (!uri.isHierarchical())
        {
          uri = URI.createURI(uri.opaquePart());
        }

        String result = URI.decode(uri.lastSegment());
        if (result != null && result.endsWith(".git"))
        {
          result = result.substring(0, result.length() - 4);
        }

        return result;
      }
    });

    registerFilter("username", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.encodeSegment(value, false).replace(":", "%3A").replace("@", "%40");
      }
    });

    registerFilter("canonical", new StringFilter()
    {
      public String filter(String value)
      {
        // Don't canonicalize the value if it contains a unexpanded variable reference.
        if (StringExpander.STRING_EXPANSION_PATTERN.matcher(value).find())
        {
          return value;
        }

        File file = new File(value).getAbsoluteFile();

        try
        {
          return file.getCanonicalPath();
        }
        catch (IOException ex)
        {
          return file.getAbsolutePath();
        }
      }
    });

    registerFilter("preferenceNode", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("/", "\\\\2f");
      }
    });

    registerFilter("length", new StringFilter()
    {
      public String filter(String value)
      {
        return Integer.toString(value.length());
      }
    });

    registerFilter("trim", new StringFilter()
    {
      public String filter(String value)
      {
        return value.trim();
      }
    });

    registerFilter("trimLeft", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.trimLeft(value);
      }
    });

    registerFilter("trimRight", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.trimRight(value);
      }
    });

    registerFilter("trimTrailingSlashes", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.trimTrailingSlashes(value);
      }
    });

    registerFilter("upper", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toUpperCase();
      }
    });

    registerFilter("lower", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toLowerCase();
      }
    });

    registerFilter("cap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.cap(value);
      }
    });

    registerFilter("allCap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.capAll(value);
      }
    });

    registerFilter("qualifiedName", new StringFilter()
    {
      public String filter(String value)
      {
        return value.trim().replaceAll("[^\\p{Alnum}]+", ".").toLowerCase();
      }
    });

    registerFilter("camel", new StringFilter()
    {
      public String filter(String value)
      {
        Matcher matcher = CAMEL_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();
        while (matcher.find())
        {
          String group = matcher.group(1);
          matcher.appendReplacement(result, group == null ? "" : group.toUpperCase());
        }

        matcher.appendTail(result);

        return result.toString();
      }
    });

    registerFilter("property", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "\\\\\\\\");
      }
    });

    registerFilter("path", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "/");
      }
    });

    registerFilter("basePath", new StringFilter()
    {
      public String filter(String value)
      {
        value = value.replaceAll("\\\\", "/");
        int pos = value.lastIndexOf('/');
        if (pos == -1)
        {
          return "";
        }

        return value.substring(0, pos);
      }
    });

    registerFilter("lastSegment", new StringFilter()
    {
      public String filter(String value)
      {
        int pos = Math.max(value.lastIndexOf('/'), value.lastIndexOf('\\'));
        if (pos == -1)
        {
          return value;
        }

        return value.substring(pos + 1);
      }
    });

    registerFilter("fileExtension", new StringFilter()
    {
      public String filter(String value)
      {
        int pos = value.lastIndexOf('.');
        if (pos == -1)
        {
          return "";
        }

        return value.substring(pos + 1);
      }
    });

    registerFilter("urlEncode", new StringFilter()
    {
      public String filter(String value)
      {
        try
        {
          return URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter("urlDecode", new StringFilter()
    {
      public String filter(String value)
      {
        try
        {
          return URLDecoder.decode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter("slashEncode", new StringFilter()
    {
      public String filter(String value)
      {
        try
        {
          return org.eclipse.equinox.internal.p2.engine.SlashEncode.encode(value);
        }
        catch (Throwable ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter("slashDecode", new StringFilter()
    {
      public String filter(String value)
      {
        try
        {
          return org.eclipse.equinox.internal.p2.engine.SlashEncode.decode(value);
        }
        catch (Throwable ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter("propertyValue", new StringFilter()
    {
      public String filter(String value)
      {
        PreferenceProperty preferenceProperty = new PreferencesUtil.PreferenceProperty(value);
        String result = preferenceProperty.get(null);
        return result == null ? "" : result;
      }
    });
  }

  public static void main(String[] args) throws UnsupportedEncodingException
  {
    System.out.println(URLEncoder.encode("file:/C:/develop/", "UTF-8"));
  }

  public String filter(String value, String filterName)
  {
    StringFilter filter = filters.get(filterName.toLowerCase());
    if (filter != null)
    {
      return filter.filter(value);
    }

    return value;
  }

  void initContributions()
  {
    if (SetupCorePlugin.INSTANCE.isOSGiRunning())
    {
      try
      {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

        for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor("org.eclipse.oomph.setup.core.stringFilters"))
        {
          String filterName = configurationElement.getAttribute("name");
          if (!filters.containsKey(filterName))
          {
            try
            {
              StringFilter filter = (StringFilter)configurationElement.createExecutableExtension("class");
              registerFilter(filterName, filter);
            }
            catch (Exception ex)
            {
              SetupCorePlugin.INSTANCE.log(ex);
            }
          }
        }
      }
      catch (Exception ex)
      {
        SetupCorePlugin.INSTANCE.log(ex);
      }
    }
  }

  private void registerFilter(String filterName, StringFilter filter)
  {
    filters.put(filterName.toLowerCase(), filter);
  }
}

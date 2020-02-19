/*
 * Copyright (c) 2015-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "file";
      }

      @Override
      public String getDescription()
      {
        return "Converts a file: URI to an OS-specific file system path.";
      }

      public String filter(String value)
      {
        return URI.createURI(value).toFileString();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "uri";
      }

      @Override
      public String getDescription()
      {
        return "Converts a file system path to a file: URI.";
      }

      public String filter(String value)
      {
        return URI.createFileURI(value).toString();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "uriLastSegment";
      }

      @Override
      public String getDescription()
      {
        return "Extracts the last path segment from a hierarchical URI or the authority from an opaque URI.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "gitRepository";
      }

      @Override
      public String getDescription()
      {
        return "Extracts the name of the repository from a Git URI (excluding a possible .git suffix).";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "username";
      }

      @Override
      public String getDescription()
      {
        return "Escapes all \"at\" symbols (@) of a String value, so that the result can be used in URI that contain a username.";
      }

      public String filter(String value)
      {
        return URI.encodeSegment(value, false).replace(":", "%3A").replace("@", "%40");
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "canonical";
      }

      @Override
      public String getDescription()
      {
        return "Canonicalizes a file system path.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "preferenceNode";
      }

      @Override
      public String getDescription()
      {
        return "Escapes all forward slashes (/) of a String value, so that the result can be used as a value in preference nodes.";
      }

      public String filter(String value)
      {
        return value.replaceAll("/", "\\\\2f");
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "length";
      }

      @Override
      public String getDescription()
      {
        return "Converts a String to a String that contains the alpha-numerical representation of the length of the original String.";
      }

      public String filter(String value)
      {
        return Integer.toString(value.length());
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "trim";
      }

      @Override
      public String getDescription()
      {
        return "Removes all whitespace from the beginning and the end of a String.";
      }

      public String filter(String value)
      {
        return value.trim();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "trimLeft";
      }

      @Override
      public String getDescription()
      {
        return "Removes all whitespace from the beginning of a String.";
      }

      public String filter(String value)
      {
        return StringUtil.trimLeft(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "trimRight";
      }

      @Override
      public String getDescription()
      {
        return "Removes all whitespace from the end of a String.";
      }

      public String filter(String value)
      {
        return StringUtil.trimRight(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "trimTrailingSlashes";
      }

      @Override
      public String getDescription()
      {
        return "Removes all slashes and backslashes from the end of a String.";
      }

      public String filter(String value)
      {
        return StringUtil.trimTrailingSlashes(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "upper";
      }

      @Override
      public String getDescription()
      {
        return "Converts all characters of a String value to upper-case.";
      }

      public String filter(String value)
      {
        return value.toUpperCase();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "lower";
      }

      @Override
      public String getDescription()
      {
        return "Converts all characters of a String value to lower-case.";
      }

      public String filter(String value)
      {
        return value.toLowerCase();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "cap";
      }

      @Override
      public String getDescription()
      {
        return "Capitalizes the first word of a String value.";
      }

      public String filter(String value)
      {
        return StringUtil.cap(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "capAll";
      }

      @Override
      public String getDescription()
      {
        return "Capitalizes all words of a String value.";
      }

      @Override
      public String[] getDeprecations()
      {
        return new String[] { "allCap" };
      }

      public String filter(String value)
      {
        return StringUtil.capAll(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "qualifiedName";
      }

      @Override
      public String getDescription()
      {
        return "Converts a camel case String value name to a qualified name.";
      }

      public String filter(String value)
      {
        return value.trim().replaceAll("[^\\p{Alnum}]+", ".").toLowerCase();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "camel";
      }

      @Override
      public String getDescription()
      {
        return "Converts a qualified name to camel case notation.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "property";
      }

      @Override
      public String getDescription()
      {
        return "Escapes all double back slashes (\\\\) of a String value, so that the result can be used as a value in properties.";
      }

      public String filter(String value)
      {
        return value.replaceAll("\\\\", "\\\\\\\\");
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "path";
      }

      @Override
      public String getDescription()
      {
        return "Extracts the path segments from a URI.";
      }

      public String filter(String value)
      {
        return value.replaceAll("\\\\", "/");
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "basePath";
      }

      @Override
      public String getDescription()
      {
        return "Removes the last segment from a file system path.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "lastSegment";
      }

      @Override
      public String getDescription()
      {
        return "Extracts the last segment from a file system path.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "fileExtension";
      }

      @Override
      public String getDescription()
      {
        return "Extracts the file extension from a URI or a file system path.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "pathEncode";
      }

      @Override
      public String getDescription()
      {
        return "Converts a file system path to a String value that can be used as a file name.";
      }

      public String filter(String value)
      {
        return IOUtil.encodeFileName(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "urlEncode";
      }

      @Override
      public String getDescription()
      {
        return "URL-encodes a String value.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "urlDecode";
      }

      @Override
      public String getDescription()
      {
        return "Decodes a URL.";
      }

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

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "slashEncode";
      }

      @Override
      public String getDescription()
      {
        return "Encodes all slashes and backslashes of a String value.";
      }

      @SuppressWarnings("restriction")
      public String filter(String value)
      {
        try
        {
          return org.eclipse.equinox.internal.security.storage.SlashEncode.encode(value);
        }
        catch (Throwable ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "slashDecode";
      }

      @Override
      public String getDescription()
      {
        return "Decodes a slashEncoded String value.";
      }

      @SuppressWarnings("restriction")
      public String filter(String value)
      {
        try
        {
          return org.eclipse.equinox.internal.security.storage.SlashEncode.decode(value);
        }
        catch (Throwable ex)
        {
          // Should not happen.
          return value;
        }
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "propertyValue";
      }

      @Override
      public String getDescription()
      {
        return "Interprets the String value as a preference property path and returns the value of that property.";
      }

      public String filter(String value)
      {
        PreferenceProperty preferenceProperty = new PreferencesUtil.PreferenceProperty(value);
        String result = preferenceProperty.get(null);
        return result == null ? "" : result;
      }
    });

    registerFilter(new DocumentedParameterizedStringFilter()
    {
      public String getName()
      {
        return "base64";
      }

      @Override
      public String getDescription()
      {
        return "Encodes the String value to its base64 representation.";
      }

      public String filter(String value)
      {
        return filter(value, null);
      }

      public String filter(String value, String argument)
      {
        try
        {
          Charset charset;
          if (value.startsWith("<?xml"))
          {
            String xmlEncoding = URIConverter.ReadableInputStream.getEncoding(value);
            charset = Charset.forName(xmlEncoding == null ? "UTF-8" : xmlEncoding);
          }
          else if (argument != null)
          {
            charset = Charset.forName(argument.toUpperCase());
          }
          else
          {
            charset = Charset.defaultCharset();
          }

          return XMLTypeFactory.eINSTANCE.convertBase64Binary(value.getBytes(charset));
        }
        catch (Exception ex)
        {
          return value;
        }
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "not";
      }

      @Override
      public String getDescription()
      {
        return "The boolean logical negation, i.e., 'false' if the value is 'true', and 'true' if the value is 'false' (or anything else).";
      }

      public String filter(String value)
      {
        return "true".equalsIgnoreCase(value) ? "false" : "true";
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      public String getName()
      {
        return "patternQuote";
      }

      @Override
      public String getDescription()
      {
        return "Quotes regular-expression constructs in the String value such that the result can be used as a literal string in patterns.";
      }

      public String filter(String value)
      {
        return Pattern.quote(value);
      }
    });
  }

  public static void main(String[] args) throws UnsupportedEncodingException
  {
    List<Map.Entry<String, StringFilter>> entries = new ArrayList<Map.Entry<String, StringFilter>>(INSTANCE.filters.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<String, StringFilter>>()
    {
      public int compare(Map.Entry<String, StringFilter> e1, Map.Entry<String, StringFilter> e2)
      {
        return e1.getKey().compareTo(e2.getKey());
      }
    });

    System.out.println("{| class=\"wikitable\"");
    boolean first = true;

    for (Map.Entry<String, StringFilter> entry : entries)
    {
      String name;
      String description;

      StringFilter filter = entry.getValue();
      if (filter instanceof StringFilterDocumentation)
      {
        name = ((StringFilterDocumentation)filter).getName();
        description = ((StringFilterDocumentation)filter).getDescription();

        boolean deprecated = !name.toLowerCase().equals(entry.getKey().toLowerCase());
        if (deprecated)
        {
          continue;
        }
      }
      else
      {
        name = entry.getKey();
        description = "";
      }

      if (first)
      {
        first = false;
      }
      else
      {
        System.out.println("|-");
      }

      System.out.println("| '''" + name + "''' || " + description);
    }

    System.out.println("|}");
  }

  public String filter(String value, String filterName)
  {
    StringFilter filter = filters.get(filterName.toLowerCase());
    if (filter == null)
    {
      int argumentIndex = filterName.indexOf('.');
      if (argumentIndex != -1)
      {
        filter = filters.get(filterName.substring(0, argumentIndex).toLowerCase());
        if (filter instanceof ParameterizedStringFilter)
        {
          ParameterizedStringFilter parameterizedStringFilter = (ParameterizedStringFilter)filter;
          return parameterizedStringFilter.filter(value, filterName.substring(argumentIndex + 1));
        }
      }

      return value;
    }

    return filter.filter(value);
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

  private void registerFilter(StringFilterDocumentation filter)
  {
    String name = filter.getName().toLowerCase();
    filters.put(name, (StringFilter)filter);

    String[] deprecations = filter.getDeprecations();
    if (deprecations != null)
    {
      for (String deprecatedName : deprecations)
      {
        filters.put(deprecatedName.toLowerCase(), (StringFilter)filter);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class DocumentedStringFilter implements StringFilter, StringFilterDocumentation
  {
    public String getDescription()
    {
      return null;
    }

    public String[] getDeprecations()
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class DocumentedParameterizedStringFilter implements ParameterizedStringFilter, StringFilterDocumentation
  {
    public String getDescription()
    {
      return null;
    }

    public String[] getDeprecations()
    {
      return null;
    }
  }
}

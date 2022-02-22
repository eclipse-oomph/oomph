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

  private static final Pattern CAMEL_PATTERN = Pattern.compile("(?:[^\\p{Alnum}]+|^)(\\p{Lower})?"); //$NON-NLS-1$

  private final Map<String, StringFilter> filters = new HashMap<>();

  private StringFilterRegistry()
  {
    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "file"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_File_description;
      }

      @Override
      public String filter(String value)
      {
        return URI.createURI(value).toFileString();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "uri"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_URI_description;
      }

      @Override
      public String filter(String value)
      {
        return URI.createFileURI(value).toString();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "uriLastSegment"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_URILastSegment_description;
      }

      @Override
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
      @Override
      public String getName()
      {
        return "gitRepository"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_GitRepository_description;
      }

      @Override
      public String filter(String value)
      {
        URI uri = URI.createURI(value);
        if (!uri.isHierarchical())
        {
          uri = URI.createURI(uri.opaquePart());
        }

        String result = URI.decode(uri.lastSegment());
        if (result != null && result.endsWith(".git")) //$NON-NLS-1$
        {
          result = result.substring(0, result.length() - 4);
        }

        return result;
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "username"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Username_description;
      }

      @Override
      public String filter(String value)
      {
        return URI.encodeSegment(value, false).replace(":", "%3A").replace("@", "%40"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "canonical"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Canonical_description;
      }

      @Override
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
      @Override
      public String getName()
      {
        return "preferenceNode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_PreferenceNode_description;
      }

      @Override
      public String filter(String value)
      {
        return value.replaceAll("/", "\\\\2f"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "length"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Length_description;
      }

      @Override
      public String filter(String value)
      {
        return Integer.toString(value.length());
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "trim"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Trim_description;
      }

      @Override
      public String filter(String value)
      {
        return value.trim();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "trimLeft"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_TrimLeft_description;
      }

      @Override
      public String filter(String value)
      {
        return StringUtil.trimLeft(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "trimRight"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_TrimRight_description;
      }

      @Override
      public String filter(String value)
      {
        return StringUtil.trimRight(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "trimTrailingSlashes"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_TrimTrailingSlashes_description;
      }

      @Override
      public String filter(String value)
      {
        return StringUtil.trimTrailingSlashes(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "upper"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Upper_description;
      }

      @Override
      public String filter(String value)
      {
        return value.toUpperCase();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "lower"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Lower_description;
      }

      @Override
      public String filter(String value)
      {
        return value.toLowerCase();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "cap"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Cap_description;
      }

      @Override
      public String filter(String value)
      {
        return StringUtil.cap(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "capAll"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_CapAll_description;
      }

      @Override
      public String[] getDeprecations()
      {
        return new String[] { "allCap" }; //$NON-NLS-1$
      }

      @Override
      public String filter(String value)
      {
        return StringUtil.capAll(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "qualifiedName"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_QualifiedName_description;
      }

      @Override
      public String filter(String value)
      {
        return value.trim().replaceAll("[^\\p{Alnum}]+", ".").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "camel"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Camel_description;
      }

      @Override
      public String filter(String value)
      {
        Matcher matcher = CAMEL_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();
        while (matcher.find())
        {
          String group = matcher.group(1);
          matcher.appendReplacement(result, group == null ? "" : group.toUpperCase()); //$NON-NLS-1$
        }

        matcher.appendTail(result);

        return result.toString();
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "property"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Property_description;
      }

      @Override
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "path"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Path_description;
      }

      @Override
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "basePath"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_BasePath_description;
      }

      @Override
      public String filter(String value)
      {
        value = value.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
        int pos = value.lastIndexOf('/');
        if (pos == -1)
        {
          return ""; //$NON-NLS-1$
        }

        return value.substring(0, pos);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "lastSegment"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_LastSegment_description;
      }

      @Override
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
      @Override
      public String getName()
      {
        return "fileExtension"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_FileExtension_description;
      }

      @Override
      public String filter(String value)
      {
        int pos = value.lastIndexOf('.');
        if (pos == -1)
        {
          return ""; //$NON-NLS-1$
        }

        return value.substring(pos + 1);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "pathEncode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_PathEncode_description;
      }

      @Override
      public String filter(String value)
      {
        return IOUtil.encodeFileName(value);
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "urlEncode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_URLEncode_description;
      }

      @Override
      public String filter(String value)
      {
        try
        {
          return URLEncoder.encode(value, "UTF-8"); //$NON-NLS-1$
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
      @Override
      public String getName()
      {
        return "urlDecode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_URLDecode_description;
      }

      @Override
      public String filter(String value)
      {
        try
        {
          return URLDecoder.decode(value, "UTF-8"); //$NON-NLS-1$
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
      @Override
      public String getName()
      {
        return "slashEncode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_SlashEncode_description;
      }

      @Override
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
      @Override
      public String getName()
      {
        return "slashDecode"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_SlashDecode_description;
      }

      @Override
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
      @Override
      public String getName()
      {
        return "propertyValue"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_PropertyValue_description;
      }

      @Override
      public String filter(String value)
      {
        PreferenceProperty preferenceProperty = new PreferencesUtil.PreferenceProperty(value);
        String result = preferenceProperty.get(null);
        return result == null ? "" : result; //$NON-NLS-1$
      }
    });

    registerFilter(new DocumentedParameterizedStringFilter()
    {
      @Override
      public String getName()
      {
        return "base64"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Base64_description;
      }

      @Override
      public String filter(String value)
      {
        return filter(value, null);
      }

      @Override
      public String filter(String value, String argument)
      {
        try
        {
          Charset charset;
          if (value.startsWith("<?xml")) //$NON-NLS-1$
          {
            String xmlEncoding = URIConverter.ReadableInputStream.getEncoding(value);
            charset = Charset.forName(xmlEncoding == null ? "UTF-8" : xmlEncoding); //$NON-NLS-1$
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
      @Override
      public String getName()
      {
        return "not"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_Not_description;
      }

      @Override
      public String filter(String value)
      {
        return "true".equalsIgnoreCase(value) ? "false" : "true"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    });

    registerFilter(new DocumentedStringFilter()
    {
      @Override
      public String getName()
      {
        return "patternQuote"; //$NON-NLS-1$
      }

      @Override
      public String getDescription()
      {
        return Messages.StringFilterRegistry_PatternQuote_description;
      }

      @Override
      public String filter(String value)
      {
        return Pattern.quote(value);
      }
    });
  }

  public static void main(String[] args) throws UnsupportedEncodingException
  {
    List<Map.Entry<String, StringFilter>> entries = new ArrayList<>(INSTANCE.filters.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<String, StringFilter>>()
    {
      @Override
      public int compare(Map.Entry<String, StringFilter> e1, Map.Entry<String, StringFilter> e2)
      {
        return e1.getKey().compareTo(e2.getKey());
      }
    });

    System.out.println("{| class=\"wikitable\""); //$NON-NLS-1$
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
        description = ""; //$NON-NLS-1$
      }

      if (first)
      {
        first = false;
      }
      else
      {
        System.out.println("|-"); //$NON-NLS-1$
      }

      System.out.println("| '''" + name + "''' || " + description); //$NON-NLS-1$ //$NON-NLS-2$
    }

    System.out.println("|}"); //$NON-NLS-1$
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

        for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor("org.eclipse.oomph.setup.core.stringFilters")) //$NON-NLS-1$
        {
          String filterName = configurationElement.getAttribute("name"); //$NON-NLS-1$
          if (!filters.containsKey(filterName))
          {
            try
            {
              StringFilter filter = (StringFilter)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
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
    @Override
    public String getDescription()
    {
      return null;
    }

    @Override
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
    @Override
    public String getDescription()
    {
      return null;
    }

    @Override
    public String[] getDeprecations()
    {
      return null;
    }
  }
}

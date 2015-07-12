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
package org.eclipse.oomph.version;

import org.eclipse.oomph.internal.version.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides static utility methods that may be helpful when dealing with {@link IMarker markers}.
 *
 * @author Eike Stepper
 */
public final class Markers
{
  public static final String MARKER_TYPE = "org.eclipse.oomph.version.VersionProblem";

  public static final String PROBLEM_TYPE = "problemType";

  public static final String DEVIATION_INFO = "deviation";

  public static final String RELEASE_PATH_PROBLEM = "release.path";

  public static final String MALFORMED_VERSION_PROBLEM = "malformed.version";

  public static final String SCHEMA_BUILDER_PROBLEM = "schema.builder";

  public static final String FEATURE_NATURE_PROBLEM = "feature.nature";

  public static final String VERSION_NATURE_PROBLEM = "version.nature";

  public static final String DEBUG_OPTION_PROBLEM = "debug.option";

  public static final String DEPENDENCY_RANGE_PROBLEM = "dependency.range";

  public static final String EXPORT_VERSION_PROBLEM = "export.version";

  public static final String UNREFERENCED_ELEMENT_PROBLEM = "unreferenced.element";

  public static final String FEATURE_CLOSURE_PROBLEM = "feature.closure";

  public static final String MAVEN_POM_PROBLEM = "maven.pom";

  public static final String COMPONENT_VERSION_PROBLEM = "component.version";

  public static final String VALIDATOR_CLASS_PROBLEM = "validator.class";

  public static final String RESOURCE_ATTRIBUTE = "<resource>";

  public static final String QUICK_FIX_PATTERN = "quickFixPattern";

  public static final String QUICK_FIX_REPLACEMENT = "quickFixReplacement";

  public static final String QUICK_FIX_ALTERNATIVE_REPLACEMENT = "quickFixAlternativeReplacement";

  public static final String QUICK_FIX_NATURE = "quickFixNature";

  public static final String QUICK_FIX_PROJECT = "quickFixProject";

  public static final String QUICK_FIX_REFERENCE = "quickFixReference";

  public static final String QUICK_FIX_CONFIGURE_OPTION = "quickFixConfigureOption";

  public static final String QUICK_FIX_CONFIGURE_VALUE = "quickFixConfigureValue";

  private static final Pattern NL_PATTERN = Pattern.compile("([\\n][\\r]?|[\\r][\\n]?)", Pattern.MULTILINE);

  private Markers()
  {
  }

  public static String getProblemType(IMarker marker)
  {
    return getAttribute(marker, PROBLEM_TYPE);
  }

  public static String getQuickFixPattern(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_PATTERN);
  }

  public static String getQuickFixReplacement(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_REPLACEMENT);
  }

  public static String getQuickFixAlternativeReplacement(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_ALTERNATIVE_REPLACEMENT);
  }

  public static String getQuickFixNature(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_NATURE);
  }

  public static String getQuickFixProject(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_PROJECT);
  }

  public static String getQuickFixReference(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_REFERENCE);
  }

  public static String getQuickFixConfigureOption(IMarker marker)
  {
    return getAttribute(marker, QUICK_FIX_CONFIGURE_OPTION);
  }

  public static String getQuickFixConfigureValue(IMarker marker)
  {
    String attribute = getAttribute(marker, QUICK_FIX_CONFIGURE_VALUE);
    if (attribute == null)
    {
      attribute = "true";
    }

    return attribute;
  }

  public static String getAttribute(IMarker marker, String attributeName)
  {
    Object value = getAttributeValue(marker, attributeName);
    return value == null ? null : value.toString();
  }

  public static Comparable<?> getAttributeValue(IMarker marker, String attributeName)
  {
    try
    {
      if (RESOURCE_ATTRIBUTE.equals(attributeName))
      {
        return marker.getResource().getFullPath().toString();
      }

      return (Comparable<?>)marker.getAttribute(attributeName);
    }
    catch (CoreException ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static int compareAttributes(String attributeName, IMarker m1, IMarker m2)
  {
    Comparable v1 = getAttributeValue(m1, attributeName);
    Comparable v2 = getAttributeValue(m2, attributeName);

    if (v1 == null)
    {
      if (v2 == null)
      {
        return 0;
      }

      return -1;
    }

    if (v2 == null)
    {
      return 1;
    }

    return v1.compareTo(v2);
  }

  public static IMarker addMarker(IResource resource, String message) throws CoreException
  {
    return addMarker(resource, message, IMarker.SEVERITY_ERROR);
  }

  public static IMarker addMarker(IResource resource, String message, int severity) throws CoreException
  {
    IMarker marker = resource.createMarker(MARKER_TYPE);
    marker.setAttribute(IMarker.MESSAGE, message);
    marker.setAttribute(IMarker.SEVERITY, severity);
    return marker;
  }

  public static IMarker addMarker(IResource resource, String message, int severity, int lineNumber) throws CoreException
  {
    IMarker marker = resource.createMarker(MARKER_TYPE);
    marker.setAttribute(IMarker.MESSAGE, message);
    marker.setAttribute(IMarker.SEVERITY, severity);
    if (lineNumber == -1)
    {
      lineNumber = 1;
    }

    marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
    return marker;
  }

  public static IMarker addMarker(IFile file, String message, int severity, int lineNumber, int charStart, int charEnd) throws CoreException
  {
    if (lineNumber < 1)
    {
      lineNumber = 1;
    }

    IMarker marker = file.createMarker(MARKER_TYPE);
    marker.setAttribute(IMarker.MESSAGE, message);
    marker.setAttribute(IMarker.SEVERITY, severity);
    marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
    marker.setAttribute(IMarker.CHAR_START, charStart);
    marker.setAttribute(IMarker.CHAR_END, charEnd);
    return marker;
  }

  public static IMarker addMarker(IFile file, String message, int severity, String regex) throws CoreException, IOException
  {
    String string = VersionUtil.getContents(file);

    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
    Matcher matcher = pattern.matcher(string);

    if (matcher.find())
    {
      int startChar = matcher.start(1);
      int endChar = matcher.end(1);

      matcher = NL_PATTERN.matcher(string);
      int line = 1;
      while (matcher.find())
      {
        if (matcher.start(1) > startChar)
        {
          break;
        }

        ++line;
      }

      return addMarker(file, message, severity, line, startChar, endChar);
    }

    return addMarker(file, message, severity);
  }

  public static void deleteAllMarkers(IResource resource, String... problemTypes) throws CoreException
  {
    if (!resource.exists())
    {
      return;
    }

    if (problemTypes.length == 0)
    {
      resource.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_INFINITE);
    }

    IMarker[] markers = resource.findMarkers(MARKER_TYPE, false, IResource.DEPTH_INFINITE);
    for (IMarker marker : markers)
    {
      Object value = marker.getAttribute(PROBLEM_TYPE);
      for (int i = 0; i < problemTypes.length; i++)
      {
        String problemType = problemTypes[i];
        if (problemType.equals(value))
        {
          marker.delete();
          break;
        }
      }
    }
  }
}

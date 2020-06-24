/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
@SuppressWarnings("nls")
public class NLSReport
{
  private static final List<String> REPORT_HEAD = Arrays.asList(//
      "<html>", //
      "<head>", //
      "<meta charset='utf-8'/>", //
      "<title>NLS Report</title>", //
      "<style>", //
      "table, th, td {", //
      "  border: 1px solid black;", //
      "  border-collapse: collapse;", //
      "  white-space: pre;", //
      "}", //
      "th, td {", //
      "  text-align: left;", //
      "}", //
      "</style>", //
      "</head>", //
      "<body>", //
      "<table>", //
      "<tr>", //
      "<th>", //
      "Property", //
      "</th>", //
      "<th>", //
      "Value", //
      "</th>", //
      "</tr>" //
  );

  private static final List<String> REPORT_TAIL = Arrays.asList(//
      "<table>", //
      "</body>", //
      "</html>" //
  );

  private final File oomphGitClone;

  private PrintStream out;

  public NLSReport(File oomphGitClone)
  {
    this.oomphGitClone = oomphGitClone;
  }

  public File report()
  {
    openReport();

    {
      File pluginsFolder = new File(oomphGitClone, "plugins");
      URI pluginsURI = URI.createFileURI(pluginsFolder.toString()).appendSegment("");
      List<File> allFiles = IOUtil.listBreadthFirst(pluginsFolder);
      Collections.sort(allFiles);
      for (File file : allFiles)
      {
        URI fileURI = URI.createFileURI(file.toString());
        URI relativeURI = fileURI.deresolve(pluginsURI);
        String pluginID = relativeURI.segment(0);
        String name = relativeURI.lastSegment();
        if (name.equals("Messages.java"))
        {
          String className = relativeURI.toString().replaceAll("^.*src/", "").replaceAll("\\.java", "").replace('/', '.');
          URI messagesURI = relativeURI.trimSegments(1).appendSegment("messages.properties");
          reportMessages(pluginID, className, messagesURI);
        }
        else if (name.equals("MANIFEST.MF") && relativeURI.segmentCount() == 3 && "META-INF".equals(relativeURI.segment(1)))
        {
          Map<String, String> properties = PropertiesUtil.loadProperties(file);
          String bundleActivator = properties.get("Bundle-Activator");
          String bundleLocalization = properties.get("Bundle-Localization");
          if (bundleLocalization != null)
          {
            File localizationFile = new File(pluginsFolder, pluginID + "/" + bundleLocalization + ".properties");
            URI localizationFileURI = URI.createFileURI(localizationFile.toString()).deresolve(pluginsURI);
            Map<String, String> localizationProperties = PropertiesUtil.loadProperties(localizationFile);
            reportActivator(pluginID, bundleActivator, localizationFileURI, localizationProperties);
          }
        }
        else if (name.equals("fragment.properties"))
        {
          Map<String, String> properties = PropertiesUtil.loadProperties(file);
          report(relativeURI, properties);
        }
      }
    }

    {
      File featuresFolder = new File(oomphGitClone, "features");
      URI featuresURI = URI.createFileURI(featuresFolder.toString()).appendSegment("");
      List<File> allFiles = IOUtil.listBreadthFirst(featuresFolder);
      Collections.sort(allFiles);
      for (File file : allFiles)
      {
        URI fileURI = URI.createFileURI(file.toString());
        URI relativeURI = fileURI.deresolve(featuresURI);
        String name = relativeURI.lastSegment();
        if (name.equals("feature.properties") && relativeURI.segmentCount() == 2)
        {
          Map<String, String> properties = PropertiesUtil.loadProperties(file);
          report(relativeURI, properties);
        }
      }
    }

    closeReport();

    return reportFile;
  }

  private void openReport()
  {
    try
    {
      reportFile = File.createTempFile("NLSReport", ".html");
      out = new PrintStream(reportFile, "UTF-8");
      for (String line : REPORT_HEAD)
      {
        out.println(line);
      }
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  private void closeReport()
  {
    for (String line : REPORT_TAIL)
    {
      out.println(line);
    }

    out.close();
  }

  private static final List<String> SUBSTITUTIONS = Arrays.asList("{0}", "{1}", "{2}", "{3}", "{4}");

  private void reportActivator(String pluginID, String bundleActivatorName, URI localizationFileURI, Map<String, String> localizationProperties)
  {
    for (Map.Entry<String, String> entry : localizationProperties.entrySet())
    {
      String value = entry.getValue();
      int subsitutionCount = getSubsitutionCount(value);
      if (subsitutionCount > 0)
      {
        Object[] substitutions = SUBSTITUTIONS.subList(0, subsitutionCount).toArray();
        String bound1 = NLS.bind(value, substitutions);
        String bound2 = NLS.bind(value, substitutions);
        if (!bound1.equals(bound2) || !bound1.equals(value))
        {
          throw new RuntimeException();
        }
      }
    }

    if (bundleActivatorName != null)
    {
      try
      {
        Class<?> bundleActivatorClass = CommonPlugin.loadClass(pluginID, bundleActivatorName);
        if (ResourceLocator.class.isAssignableFrom(bundleActivatorClass))
        {
          String containerClassName = bundleActivatorName.substring(0, bundleActivatorName.indexOf('$'));
          Class<?> containerClass = CommonPlugin.loadClass(pluginID, containerClassName);
          ResourceLocator plugin = ReflectUtil.getValue("plugin", containerClass);
          for (Map.Entry<String, String> entry : localizationProperties.entrySet())
          {
            String value = plugin.getString(entry.getKey());
            if (!value.equals(entry.getValue()))
            {
              throw new RuntimeException();
            }

            int subsitutionCount = getSubsitutionCount(value);
            if (subsitutionCount > 0)
            {
              Object[] substitutions = SUBSTITUTIONS.subList(0, subsitutionCount).toArray();
              String bound1 = NLS.bind(value, substitutions);
              String bound2 = plugin.getString(entry.getKey(), substitutions);
              if (!bound1.equals(bound2) || !bound1.equals(value))
              {
                throw new RuntimeException();
              }
            }
          }
        }
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    }

    report(localizationFileURI, localizationProperties);
  }

  private void report(URI localizationFileURI, Map<String, String> localizationProperties)
  {
    out.println("<tr>");
    out.print("<td colspan='2' style='font-weight: bold;'>");
    out.print(DiagnosticDecorator.escapeContent(localizationFileURI.toString()));
    out.println("</td>");
    out.println("</tr>");

    for (Map.Entry<String, String> entry : localizationProperties.entrySet())
    {
      out.println("<tr>");
      out.print("<td>");
      out.print(entry.getKey());
      out.println("</td>");
      out.print("<td>");
      out.print(DiagnosticDecorator.escapeContent(entry.getValue()));
      out.println("</td>");
      out.println("</tr>");
    }

  }

  private static final Pattern SUBSITUTION = Pattern.compile(" (?<!\\\\)\\{([0-9]+)\\}|^\\{([0-9]+)\\}");

  private File reportFile;

  private static int getSubsitutionCount(String message)
  {
    int count = 0;
    for (Matcher matcher = SUBSITUTION.matcher(message); matcher.find();)
    {
      String index = matcher.group(1);
      if (index == null)
      {
        index = matcher.group(2);
      }

      int value = Integer.parseInt(index);
      if (value > count)
      {
        value = count;
      }
    }

    return count;
  }

  private void reportMessages(String pluginID, String messagesClassName, URI messagesURI)
  {
    try
    {
      Class<?> messagesClass = CommonPlugin.loadClass(pluginID, messagesClassName);
      Field[] fields = messagesClass.getFields();
      Map<String, String> messages = new LinkedHashMap<String, String>();
      for (Field field : fields)
      {
        Object value = field.get(null);
        messages.put(field.getName(), value.toString());
      }

      report(messagesURI, messages);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
}

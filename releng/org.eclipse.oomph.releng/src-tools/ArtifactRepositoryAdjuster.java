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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stepper
 */
public final class ArtifactRepositoryAdjuster
{
  private static final String DOWNLOAD_PREFIX = "/home/data/httpd/download.eclipse.org/";

  private static final Pattern REPOSITORY_PATTERN = Pattern.compile("\\s*<repository.*?>\\s*");

  private static final Pattern FEATURE_PATTERN = Pattern
      .compile("\\s*<artifact\\s+classifier\\s*=\\s*['\"]org.eclipse.update.feature['\"]\\s+id\\s*=\\s*['\"](.*?)['\"].*?>\\s*");

  private ArtifactRepositoryAdjuster()
  {
  }

  public static void main(String[] args) throws Exception
  {
    File repositoryFolder = new File(args[0]).getCanonicalFile();
    String repositoryName = args[1];
    System.out.println(repositoryFolder);

    BufferedReader reader = new BufferedReader(new FileReader(new File(repositoryFolder, "artifacts.xml")));
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(repositoryFolder, "artifacts.out")));

    String relativePath = repositoryFolder.getAbsolutePath();
    if (relativePath.startsWith(DOWNLOAD_PREFIX))
    {
      relativePath = relativePath.substring(DOWNLOAD_PREFIX.length());
    }

    if (relativePath.startsWith("/"))
    {
      relativePath = relativePath.substring(1);
    }

    boolean repositoryFound = false;
    String line;

    while ((line = reader.readLine()) != null)
    {
      if (!repositoryFound)
      {
        Matcher matcher = REPOSITORY_PATTERN.matcher(line);
        if (matcher.matches())
        {
          line = line.replaceFirst("name=['\"].*?['\"]", "name='" + repositoryName + "'");
          writeLine(writer, line);

          Properties properties = new Properties(reader);
          properties.put("p2.mirrorsURL", "http://www.eclipse.org/downloads/download.php?file=/" + relativePath + "&amp;format=xml");
          properties.put("p2.statsURI", "http://download.eclipse.org/" + relativePath);
          properties.write(writer);

          repositoryFound = true;
          continue;
        }
      }
      else
      {
        Matcher matcher = FEATURE_PATTERN.matcher(line);
        if (matcher.matches())
        {
          writeLine(writer, line);
          String id = matcher.group(1);

          Properties properties = new Properties(reader);
          properties.put("download.stats", id);
          properties.write(writer);

          continue;
        }
      }

      writeLine(writer, line);
    }

    writer.close();
    reader.close();
  }

  private static void writeLine(BufferedWriter writer, String line) throws IOException
  {
    writer.write(line);
    writer.write('\n');
  }

  /**
   * @author Eike Stepper
   */
  private static final class Properties extends LinkedHashMap<String, String>
  {
    private static final long serialVersionUID = 1L;

    private static final Pattern PROPERTIES_PATTERN = Pattern.compile("(\\s*)<properties\\s+size\\s*=\\s*['\"]([0-9]+)['\"]\\s*>\\s*");

    private static final Pattern PROPERTY_PATTERN = Pattern
        .compile("(\\s*)<property\\s+name\\s*=\\s*['\"](.*?)['\"]\\s+value\\s*=\\s*['\"](.*?)['\"]\\s*/>\\s*");

    private String propertiesIndent;

    private String propertyIndent;

    public Properties(BufferedReader reader) throws IOException
    {
      String propertiesLine = reader.readLine();
      Matcher propertiesMatcher = PROPERTIES_PATTERN.matcher(propertiesLine);
      if (!propertiesMatcher.matches())
      {
        throw new IllegalStateException("No properties match: " + propertiesLine);
      }

      propertiesIndent = propertiesMatcher.group(1);
      int size = Integer.parseInt(propertiesMatcher.group(2));

      for (int i = 0; i < size; i++)
      {
        String propertyLine = reader.readLine();
        Matcher propertyMatcher = PROPERTY_PATTERN.matcher(propertyLine);
        if (!propertyMatcher.matches())
        {
          throw new IllegalStateException("No property match: " + propertyLine);
        }

        propertyIndent = propertyMatcher.group(1);
        put(propertyMatcher.group(2), propertyMatcher.group(3));
      }
    }

    public void write(BufferedWriter writer) throws IOException
    {
      writeLine(writer, propertiesIndent + "<properties size='" + size() + "'>");

      for (Map.Entry<String, String> entry : entrySet())
      {
        writeLine(writer, propertyIndent + "<property name='" + entry.getKey() + "' value='" + entry.getValue() + "'/>");
      }
    }
  }
}

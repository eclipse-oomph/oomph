
/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
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
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

  private static final Pattern FEATURE_PATTERN = Pattern.compile(
      "\\s*<artifact\\s+classifier\\s*=\\s*['\"]org.eclipse.update.feature['\"]\\s+id\\s*=\\s*['\"](org\\.eclipse\\.oomph.*?)['\"]\\s+version\\s*=\\s*['\"]([^'\"]*)['\"].*?>\\s*");

  private ArtifactRepositoryAdjuster()
  {
  }

  public static void main(String[] args) throws Exception
  {
    File repositoryFolder = new File(args[0]).getCanonicalFile();
    File repositoryFolderFinal = new File(args[1]).getCanonicalFile();
    String repositoryName = args[2];
    String buildType = args[3];

    File input = new File(repositoryFolder, "artifacts.xml");
    File output = new File(repositoryFolder, "artifacts.out");

    Version greatestVersion = getGreatestFeatureVersion(input);

    System.out.println("Adjusting " + input);

    BufferedReader reader = new BufferedReader(new FileReader(input));
    BufferedWriter writer = new BufferedWriter(new FileWriter(output));

    String relativePath = repositoryFolderFinal.getAbsolutePath();
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
          String newLine = line.replaceFirst("name=['\"].*?['\"]", "name='" + repositoryName + "'");
          if (!newLine.equals(line))
          {
            System.out.println("  repository.name = " + repositoryName);
            line = newLine;
          }

          writeLine(writer, line);

          Properties properties = new Properties(reader);

          boolean mirrored = !"nightly".equals(buildType);
          if (mirrored)
          {
            String mirrorsURL = "http://www.eclipse.org/downloads/download.php?file=/" + relativePath;
            System.out.println("  p2.mirrorsURL = " + mirrorsURL);
            properties.put("p2.mirrorsURL", mirrorsURL);
          }
          else
          {
            properties.remove("p2.mirrorsURL");
          }

          String statsURI = "http://download.eclipse.org/stats/oomph/feature/" + greatestVersion;
          System.out.println("  p2.statsURI = " + statsURI);
          properties.put("p2.statsURI", statsURI);

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
          System.out.println("  download.stats = " + id);

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

  private static Version getGreatestFeatureVersion(File input) throws IOException
  {
    Version greatestVersion = null;
    List<String> lines = readLines(input, "UTF-8");
    for (String line : lines)
    {
      Matcher matcher = FEATURE_PATTERN.matcher(line);
      if (matcher.matches())
      {
        Version version = new Version(matcher.group(2));
        if (greatestVersion == null || version.isGreaterThan(greatestVersion))
        {
          greatestVersion = version;
        }
      }
    }

    return greatestVersion;
  }

  private static List<String> readLines(File file, String charsetName) throws IOException
  {
    List<String> lines = new ArrayList<String>();

    if (file.exists())
    {
      InputStream in = null;
      Reader reader = null;
      BufferedReader bufferedReader = null;

      try
      {
        in = new FileInputStream(file);
        reader = charsetName == null ? new InputStreamReader(in) : new InputStreamReader(in, charsetName);
        bufferedReader = new BufferedReader(reader);

        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
          lines.add(line);
        }
      }
      finally
      {
        closeSilent(bufferedReader);
        closeSilent(reader);
        closeSilent(in);
      }
    }

    return lines;
  }

  private static void closeSilent(Closeable closeable)
  {
    try
    {
      if (closeable != null)
      {
        closeable.close();
      }
    }
    catch (Exception ex)
    {
      // Ignore.
    }
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

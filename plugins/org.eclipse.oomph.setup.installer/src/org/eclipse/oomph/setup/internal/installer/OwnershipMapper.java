/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.util.IOUtil;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class OwnershipMapper
{
  private static final String PROJECTS_NAME = "projects.txt";

  private static final boolean REFRESH_PROJECTS = Boolean.getBoolean("refresh.projects");

  private static final String EXEMPTION_RULES = System.getProperty("exemption.rules");

  private static final String ROOT = "ROOT";

  private static final String UNKNOWN = "UNKNOWN";

  private static final String IGNORE = "-";

  private static Path rootFolder;

  private static Set<String> projects;

  private static Map<Path, ExemptionRule> exemptionRules;

  private static Map<Path, String> mappings = new HashMap<Path, String>();

  public static void main(String[] args) throws Exception
  {
    rootFolder = Paths.get(args[0]);
    initProjects();
    initExemptionRules();

    Files.walkFileTree(rootFolder, new SimpleFileVisitor<Path>()
    {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
      {
        if (dir.equals(rootFolder))
        {
          mappings.put(rootFolder, ROOT);
        }
        else
        {
          Path path = rootFolder.relativize(dir);
          ExemptionRule exemptionRule = exemptionRules.get(path);
          if (exemptionRule != null)
          {
            String project = exemptionRule.getProject();
            if (project.equals(IGNORE))
            {
              return FileVisitResult.SKIP_SUBTREE;
            }

            mappings.put(dir, project);
            System.out.println(path + "\t" + project);
            return exemptionRule.isRecursive() ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
          }

          PosixFileAttributes attributes = Files.getFileAttributeView(dir, PosixFileAttributeView.class).readAttributes();
          String user = attributes.owner().getName();
          String group = attributes.group().getName();

          String project = mapFolder(dir, user, group);

          for (Path parent = dir.getParent(), stop = rootFolder.getParent(); parent != null && !parent.equals(stop); parent = parent.getParent())
          {
            String parentProject = mappings.get(parent);
            if (parentProject != null)
            {
              boolean unknown = UNKNOWN.equals(project);
              if (!parentProject.equals(project) && (ROOT.equals(parentProject) || !unknown))
              {
                mappings.put(dir, project);

                if (unknown)
                {
                  project += "\t" + user + "\t" + group;
                }

                System.out.println(path + "\t" + project);
              }

              break;
            }
          }
        }

        return FileVisitResult.CONTINUE;
      }
    });

    writeMappings();
  }

  private static void initProjects() throws Exception
  {
    File projectsFile = new File(PROJECTS_NAME);
    if (!projectsFile.exists() || REFRESH_PROJECTS)
    {
      projects = PMI.getProjects();
      IOUtil.writeLines(projectsFile, "UTF-8", new ArrayList<String>(projects));
    }
    else
    {
      List<String> lines = IOUtil.readLines(projectsFile, "UTF-8");
      projects = new HashSet<String>(lines);
    }
  }

  private static void initExemptionRules()
  {
    if (EXEMPTION_RULES != null)
    {
      exemptionRules = new LinkedHashMap<Path, ExemptionRule>();

      for (String line : EXEMPTION_RULES.split("\n"))
      {
        if (!line.isEmpty())
        {
          int lastSpace = line.lastIndexOf(' ');
          String path = line.substring(0, lastSpace);
          String project = line.substring(lastSpace + 1);

          Path dir;
          boolean recursive;
          if (path.endsWith("/"))
          {
            dir = Paths.get(path.substring(0, path.length() - 1));
            recursive = true;
          }
          else
          {
            dir = Paths.get(path);
            recursive = false;
          }

          ExemptionRule exemptionRule = new ExemptionRule(project, recursive);
          exemptionRules.put(dir, exemptionRule);
        }
      }
    }
  }

  private static String mapFolder(Path dir, String user, String group)
  {
    if (projects.contains(group))
    {
      return group;
    }

    if (user.startsWith("genie."))
    {
      String suffix = "." + user.substring("genie.".length());
      List<String> ids = getProjects(suffix);
      if (ids.size() == 1)
      {
        return ids.get(0);
      }
    }

    return UNKNOWN;
  }

  private static List<String> getProjects(String suffix)
  {
    List<String> result = new ArrayList<String>();
    for (String project : projects)
    {
      if (project.endsWith(suffix))
      {
        result.add(project);
      }
    }

    return result;
  }

  private static void writeMappings() throws IOException
  {
    Writer writer = new BufferedWriter(new FileWriter("mappings.txt"));
    Path[] dirs = mappings.keySet().toArray(new Path[mappings.size()]);
    Arrays.sort(dirs);

    for (Path dir : dirs)
    {
      String project = mappings.get(dir);
      if (!ROOT.equals(project) && !UNKNOWN.equals(project))
      {
        writer.write(dir.toString());
        writer.write("\t");
        writer.write(project);
        writer.write("\n");
      }
    }

    writer.close();
  }

  /**
   * @author Eike Stepper
   */
  private static final class PMI
  {
    private static final String URL = "https://projects.eclipse.org/list-of-projects";

    private static final Pattern PATTERN = Pattern.compile("<div[^>]+about=\"/projects/([^\"]+)\"[^>]+>");

    private static final String NEXT = "<li class=\"next\">";

    public static Set<String> getProjects() throws Exception
    {
      Set<String> projects = new HashSet<String>();
      for (int page = 0;; ++page)
      {
        String url = URL + "?page=" + page;
        System.out.println("Processing " + url);

        InputStream stream = new URL(url).openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
          IOUtil.copy(stream, baos);
        }
        finally
        {
          IOUtil.close(stream);
        }

        String content = new String(baos.toByteArray(), "UTF-8");
        if (processPage(content, projects))
        {
          break;
        }
      }

      return projects;
    }

    /**
     * @return <code>true</code> if this is the last page.
     */
    private static boolean processPage(String content, Set<String> projects)
    {
      Matcher matcher = PATTERN.matcher(content);
      int start = 0;

      while (matcher.find(start))
      {
        String project = matcher.group(1);
        projects.add(project);
        start = matcher.end();
      }

      return !content.contains(NEXT);
    }
  }

  /**
   * @author Stepper
   */
  private static final class ExemptionRule
  {
    private final String project;

    private final boolean recursive;

    public ExemptionRule(String project, boolean recursive)
    {
      this.project = project;
      this.recursive = recursive;
    }

    public String getProject()
    {
      return project;
    }

    public boolean isRecursive()
    {
      return recursive;
    }
  }
}

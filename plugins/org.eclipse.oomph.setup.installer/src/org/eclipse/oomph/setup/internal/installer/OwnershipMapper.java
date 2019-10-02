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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class OwnershipMapper
{
  private static final String PROJECTS_NAME = "projects.txt";

  private static final String FOLDERS_NAME = "folders.txt";

  private static final boolean REFRESH_PROJECTS = Boolean.getBoolean("refresh.projects");

  private static final boolean REFRESH_FOLDERS = Boolean.getBoolean("refresh.folders");

  private static Set<String> projects;

  private static List<String> folders;

  public static void main(String[] args) throws Exception
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

    File foldersFile = new File(FOLDERS_NAME);
    if (!foldersFile.exists() || REFRESH_FOLDERS)
    {
      folders = RootFolder.collectFolders(Paths.get(args[0]));
      IOUtil.writeLines(foldersFile, "UTF-8", folders);
    }
    else
    {
      folders = IOUtil.readLines(foldersFile, "UTF-8");
    }
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
        InputStream stream = new URL(URL + "?page=" + page).openStream();
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
        System.out.println(project);

        projects.add(project);
        start = matcher.end();
      }

      return !content.contains(NEXT);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class RootFolder
  {
    public static List<String> collectFolders(Path rootFolder) throws Exception
    {
      final List<String> folders = new ArrayList<String>();

      Files.walkFileTree(rootFolder, new SimpleFileVisitor<Path>()
      {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
        {
          PosixFileAttributes attributes = Files.getFileAttributeView(dir, PosixFileAttributeView.class).readAttributes();
          String line = dir + "\t" + attributes.owner().getName() + "\t" + attributes.group().getName();
          System.out.println(line);
          folders.add(line);
          return FileVisitResult.CONTINUE;
        }
      });

      return folders;
    }
  }

}

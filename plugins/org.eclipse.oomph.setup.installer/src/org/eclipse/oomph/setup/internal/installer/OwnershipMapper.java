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
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class OwnershipMapper
{
  private static final boolean DEBUG = System.getProperty("os.name").toLowerCase().contains("windows");

  private static final PosixFileAttributes DEBUG_ATTRIBUTES = DEBUG ? new DebugFileAttributes() : null;

  private static final FileFilter FOLDER_FILTER = new FileFilter()
  {
    public boolean accept(File pathname)
    {
      Path path = pathname.toPath();
      return !Files.isSymbolicLink(path) && Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(path);
    }
  };

  private static final ThreadFactory THREAD_FACTORY = new ThreadFactory()
  {
    public Thread newThread(Runnable r)
    {
      Thread thread = new Thread(r);
      thread.setDaemon(true);
      return thread;
    }
  };

  private static final String PROJECTS_NAME = "projects.txt";

  private static final boolean REFRESH_PROJECTS = Boolean.getBoolean("refresh.projects");

  private static final String EXEMPTION_RULES = DEBUG ? getDebugExemptions() : System.getProperty("exemption.rules");

  private static final String ROOT = "ROOT";

  private static final String UNKNOWN = "UNKNOWN";

  private static final String IGNORE = "-";

  private static Path rootFolder;

  private static Set<String> projects;

  private static Map<Path, ExemptionRule> exemptionRules = new LinkedHashMap<Path, ExemptionRule>();

  private static Map<Path, String> mappings = Collections.synchronizedMap(new HashMap<Path, String>());

  private static Map<Path, Long> timings = Collections.synchronizedMap(new HashMap<Path, Long>());

  public static void main(String[] args) throws Exception
  {
    rootFolder = Paths.get(args[0]);
    mappings.put(rootFolder, ROOT);
    System.out.println("Mapping " + rootFolder);
    System.out.println();

    initProjects();
    initExemptionRules();

    File[] topLevelFolders = rootFolder.toFile().listFiles(FOLDER_FILTER);
    if (topLevelFolders != null)
    {
      long start = System.currentTimeMillis();

      ExecutorService threadPool = Executors.newFixedThreadPool(20, THREAD_FACTORY);
      final CountDownLatch finished = new CountDownLatch(topLevelFolders.length);

      for (final File topLevelFolder : topLevelFolders)
      {
        threadPool.submit(new Runnable()
        {
          public void run()
          {
            try
            {
              processFolder(topLevelFolder.toPath());
            }
            catch (Exception ex)
            {
              printStackTrace(ex);
            }
            finally
            {
              finished.countDown();
            }
          }
        });
      }

      finished.await();
      timings.put(rootFolder, System.currentTimeMillis() - start);
      writeResults();
    }
  }

  private static void processFolder(Path folder) throws Exception
  {
    Path path = rootFolder.relativize(folder);
    long start = System.currentTimeMillis();

    try
    {
      ExemptionRule exemptionRule = exemptionRules.get(path);
      if (exemptionRule != null)
      {
        String project = exemptionRule.getProject();
        if (project.equals(IGNORE))
        {
          return;
        }

        mappings.put(folder, project);
        System.out.println(path + "\t" + project);

        if (exemptionRule.isRecursive())
        {
          return;
        }
      }
      else
      {
        PosixFileAttributes attributes = getAttributes(folder);
        String user = attributes.owner().getName();
        String group = attributes.group().getName();

        String project = mapFolder(folder, user, group);

        for (Path parent = folder.getParent(), stop = rootFolder.getParent(); parent != null && !parent.equals(stop); parent = parent.getParent())
        {
          String parentProject = mappings.get(parent);
          if (parentProject != null)
          {
            boolean unknown = UNKNOWN.equals(project);
            if (!parentProject.equals(project) && (ROOT.equals(parentProject) || !unknown))
            {
              mappings.put(folder, project);

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

      File[] childFolders = folder.toFile().listFiles(FOLDER_FILTER);
      if (childFolders != null)
      {
        for (File childFolder : childFolders)
        {
          try
          {
            processFolder(childFolder.toPath());
          }
          catch (Exception ex)
          {
            printStackTrace(ex);
          }
        }
      }
    }
    finally
    {
      timings.put(folder, System.currentTimeMillis() - start);
    }
  }

  private static String mapFolder(Path folder, String user, String group)
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

  private static PosixFileAttributes getAttributes(Path folder) throws IOException
  {
    if (DEBUG_ATTRIBUTES != null)
    {
      return DEBUG_ATTRIBUTES;
    }

    return Files.getFileAttributeView(folder, PosixFileAttributeView.class).readAttributes();
  }

  private static void writeResults() throws IOException
  {
    Writer writer = new BufferedWriter(new FileWriter("mappings.txt"));

    Path[] folders = mappings.keySet().toArray(new Path[mappings.size()]);
    Arrays.sort(folders);

    List<Path> exemptions = new ArrayList<Path>();
    Set<String> unmappedProjects = new HashSet<String>(projects);
    Path lastFolder = null;

    System.out.println();
    System.out.println("TIMINGS:");

    for (Path folder : folders)
    {
      String project = mappings.get(folder);
      Path relativeFolder = rootFolder.relativize(folder);

      Long timing = timings.get(folder);
      if (timing != null)
      {
        System.out.println(relativeFolder + " --> " + project + "\t" + timing);
      }

      if (!ROOT.equals(project) && !UNKNOWN.equals(project))
      {
        writer.write(relativeFolder.toString());
        writer.write("\t");
        writer.write(project);
        writer.write("\n");

        if (lastFolder != null && !relativeFolder.startsWith(lastFolder))
        {
          ExemptionRule existingRule = exemptionRules.get(lastFolder);
          if (existingRule == null || !existingRule.isRecursive())
          {
            exemptions.add(lastFolder);
          }
        }

        lastFolder = relativeFolder;
        unmappedProjects.remove(project);
      }
    }

    if (lastFolder != null)
    {
      exemptions.add(lastFolder);
    }

    writer.close();
    writer = new BufferedWriter(new FileWriter("exemptions.txt"));

    for (Path exemption : exemptions)
    {
      writer.write(exemption.toString());
      writer.write("/ ");
      writer.write(mappings.get(rootFolder.resolve(exemption)));
      writer.write("\n");
    }

    writer.close();

    if (!unmappedProjects.isEmpty())
    {
      System.out.println();
      System.out.println("UNMAPPED PROJECTS:");

      String[] array = unmappedProjects.toArray(new String[unmappedProjects.size()]);
      Arrays.sort(array);

      for (String unmappedProject : array)
      {
        System.out.println(unmappedProject);
      }
    }
  }

  private static void printStackTrace(Exception exception)
  {
    try
    {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(bytes);
      exception.printStackTrace(out);
      System.err.write(bytes.toByteArray());
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }

  private static void initProjects() throws Exception
  {
    File projectsFile = new File(PROJECTS_NAME);
    if (!projectsFile.exists() || REFRESH_PROJECTS)
    {
      projects = PMI.getProjects();
      IOUtil.writeLines(projectsFile, "UTF-8", new ArrayList<String>(projects));
      System.out.println();
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
      for (String line : EXEMPTION_RULES.split("\n"))
      {
        line = line.trim();

        if (!line.isEmpty())
        {
          int lastSpace = line.lastIndexOf(' ');
          String path = line.substring(0, lastSpace);
          String project = line.substring(lastSpace + 1);

          Path folder;
          boolean recursive;
          if (path.endsWith("/"))
          {
            folder = Paths.get(path.substring(0, path.length() - 1));
            recursive = true;
          }
          else
          {
            folder = Paths.get(path);
            recursive = IGNORE.equals(project);
          }

          ExemptionRule exemptionRule = new ExemptionRule(project, recursive);
          exemptionRules.put(folder, exemptionRule);
          System.out.println(folder + " --> " + exemptionRule);
        }
      }

      System.out.println();
    }
  }

  private static String getDebugExemptions()
  {
    return "   bin -\n" //
        + "   cdo-master/ CDO\n"//
        + "   oomph OOMPH";
  }

  /**
   * @author Stepper
   */
  private static final class DebugFileAttributes implements PosixFileAttributes
  {
    private static final Random RANDOM = new Random();

    public UserPrincipal owner()
    {
      return new UserPrincipal()
      {
        public String getName()
        {
          return "foo.bar";
        }
      };
    }

    public GroupPrincipal group()
    {
      return new GroupPrincipal()
      {
        public String getName()
        {
          int i = RANDOM.nextInt(4);
          if (i == 3)
          {
            return "foo.bar"; // UNKNOWN
          }

          return projects.toArray(new String[projects.size()])[i % 3];
        }
      };
    }

    public long size()
    {
      throw new UnsupportedOperationException();
    }

    public FileTime lastModifiedTime()
    {
      throw new UnsupportedOperationException();
    }

    public FileTime lastAccessTime()
    {
      throw new UnsupportedOperationException();
    }

    public boolean isSymbolicLink()
    {
      throw new UnsupportedOperationException();
    }

    public boolean isRegularFile()
    {
      throw new UnsupportedOperationException();
    }

    public boolean isOther()
    {
      throw new UnsupportedOperationException();
    }

    public boolean isDirectory()
    {
      throw new UnsupportedOperationException();
    }

    public Object fileKey()
    {
      throw new UnsupportedOperationException();
    }

    public FileTime creationTime()
    {
      throw new UnsupportedOperationException();
    }

    public Set<PosixFilePermission> permissions()
    {
      throw new UnsupportedOperationException();
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

    @Override
    public String toString()
    {
      if (IGNORE.equals(project))
      {
        return "ExemptionRule [IGNORE]";
      }

      return "ExemptionRule [project=" + project + ", recursive=" + recursive + "]";
    }

  }
}

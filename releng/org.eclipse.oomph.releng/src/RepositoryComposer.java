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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Stepper
 */
public final class RepositoryComposer
{
  private static final String REMOVE_MARKER = "REMOVE";

  private static final Comparator<String> ALPHA_COMPARATOR = new Comparator<String>()
  {
    public int compare(String n1, String n2)
    {
      return n2.compareTo(n1);
    }
  };

  private static final Comparator<String> VERSION_COMPARATOR = new Comparator<String>()
  {
    public int compare(String n1, String n2)
    {
      return new Version(n2).compareTo(new Version(n1));
    }
  };

  private RepositoryComposer()
  {
  }

  public static void main(String[] args) throws Exception
  {
    File downloadsFolder = new File(args[0]).getCanonicalFile();
    String buildType = args[1];
    String buildKey = args[2];
    String buildLabel = args[3];

    String folder = buildKey;
    if (buildLabel.length() != 0)
    {
      folder += "-" + buildLabel;
    }

    File dropsFolder = new File(downloadsFolder, "drops");
    File dropTypeFolder = new File(dropsFolder, buildType);
    File dropFolder = new File(dropTypeFolder, folder);

    File updatesFolder = new File(downloadsFolder, "updates.tmp");
    File updateTypeFolder = new File(updatesFolder, buildType);

    if ("release".equals(buildType))
    {
      composeRepositories(dropTypeFolder, updateTypeFolder, VERSION_COMPARATOR, Integer.MAX_VALUE);

      boolean milestonesChanged = false;
      File milestonesFolder = new File(dropsFolder, "milestone");
      File[] children = milestonesFolder.listFiles();
      if (children != null)
      {
        for (File child : children)
        {
          if (child.isDirectory() && child.getName().contains("-" + buildLabel + "-"))
          {
            scheduleRemoval(child);
            milestonesChanged = true;
          }
        }
      }

      if (milestonesChanged)
      {
        composeMilestoneRepositories(dropsFolder, new File(updatesFolder, "milestone"));
      }
    }
    else if ("milestone".equals(buildType))
    {
      composeMilestoneRepositories(dropsFolder, updateTypeFolder);
    }
    else if ("nightly".equals(buildType))
    {
      composeRepositories(dropTypeFolder, updateTypeFolder, ALPHA_COMPARATOR, 5);
    }

    composeRepository(updatesFolder, "Oomph All", getComposites(updatesFolder));
    composeRepository(new File(updatesFolder, "latest"), "Oomph Latest", Collections.singletonList(dropFolder));
  }

  private static void composeMilestoneRepositories(File dropsFolder, File updateTypeFolder) throws IOException
  {
    File milestonesFolder = new File(dropsFolder, "milestone");
    if (!composeRepositories(milestonesFolder, updateTypeFolder, ALPHA_COMPARATOR, Integer.MAX_VALUE))
    {
      List<File> drops = new ArrayList<File>();

      File releasesFolder = new File(dropsFolder, "release");
      List<String> names = getSortedChildren(releasesFolder, VERSION_COMPARATOR);
      if (!names.isEmpty())
      {
        drops.add(new File(releasesFolder, names.get(0)));
      }

      composeRepository(updateTypeFolder, "Oomph Milestones", drops);
      composeRepository(new File(updateTypeFolder, "latest"), "Oomph Latest Milestone", drops);
    }
  }

  private static boolean composeRepositories(File dropTypeFolder, File updateTypeFolder, Comparator<String> comparator, int max) throws IOException
  {
    List<String> names = getSortedChildren(dropTypeFolder, comparator);
    if (names.isEmpty())
    {
      return false;
    }

    List<File> drops = new ArrayList<File>();
    int count = 0;

    for (String name : names)
    {
      File drop = new File(dropTypeFolder, name);
      if (++count > max)
      {
        scheduleRemoval(drop);
      }
      else
      {
        drops.add(drop);
      }
    }

    String name = dropTypeFolder.getName();
    if ("release".equals(name))
    {
      name = "Releases";
    }
    else if ("milestone".equals(name))
    {
      name = "Milestones";
    }
    else if ("nightly".equals(name))
    {
      name = "Nightly Builds";
    }

    composeRepository(updateTypeFolder, "Oomph " + name, drops);
    composeRepository(new File(updateTypeFolder, "latest"), "Oomph Latest " + name.substring(0, name.length() - 1), Collections.singletonList(drops.get(0)));
    return true;
  }

  private static void composeRepository(File compositeFolder, String name, List<File> drops) throws IOException
  {
    long timestamp = System.currentTimeMillis();
    writeRepository(compositeFolder, true, name, timestamp, drops);
    writeRepository(compositeFolder, false, name, timestamp, drops);
  }

  private static void writeRepository(File compositeFolder, boolean metadata, String name, long timestamp, List<File> drops) throws IOException
  {
    String fileName;
    String processingInstruction;
    String type;
    if (metadata)
    {
      fileName = "compositeContent.xml";
      processingInstruction = "compositeMetadataRepository";
      type = "org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository";
    }
    else
    {
      fileName = "compositeArtifacts.xml";
      processingInstruction = "compositeArtifactRepository";
      type = "org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository";
    }

    compositeFolder.mkdirs();
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(compositeFolder, fileName)));

    writeLine(writer, "<?xml version='1.0' encoding='UTF-8'?>");
    writeLine(writer, "<?" + processingInstruction + " version='1.0.0'?>");
    writeLine(writer, "<repository name='" + name + "' type='" + type + "' version='1.0.0'>");
    writeLine(writer, "  <properties size='2'>");
    writeLine(writer, "    <property name='p2.timestamp' value='" + timestamp + "'/>");
    writeLine(writer, "    <property name='p2.compressed' value='false'/>");
    writeLine(writer, "  </properties>");
    writeLine(writer, "  <children size='" + drops.size() + "'>");

    for (File drop : drops)
    {
      String relativePath = drop.getPath();
      if (relativePath.startsWith("/"))
      {
        relativePath = getRelativePath(compositeFolder, drop);
      }

      writeLine(writer, "    <child location='" + relativePath + "'/>");
    }

    writeLine(writer, "  </children>");
    writeLine(writer, "</repository>");
    writer.close();
  }

  private static void writeLine(BufferedWriter writer, String line) throws IOException
  {
    writer.write(line);
    writer.write('\n');
  }

  private static String getRelativePath(File source, File target)
  {
    String targetPath = target.getAbsolutePath();
    StringBuilder builder = new StringBuilder();

    while (source != null)
    {
      String sourcePath = source.getAbsolutePath();
      if (targetPath.startsWith(sourcePath))
      {
        return builder.toString() + targetPath.substring(sourcePath.length() + 1);
      }

      builder.append("../");
      source = source.getParentFile();
    }

    throw new IllegalStateException();
  }

  private static List<File> getComposites(File updatesFolder)
  {
    List<File> composites = new ArrayList<File>();
    addComposite(updatesFolder, composites, "release");
    addComposite(updatesFolder, composites, "milestone");
    addComposite(updatesFolder, composites, "nightly");
    return composites;
  }

  private static void addComposite(File updatesFolder, List<File> composites, String name)
  {
    if (new File(updatesFolder, name).isDirectory())
    {
      composites.add(new File(name));
    }
  }

  private static List<String> getSortedChildren(File folder, Comparator<String> comparator)
  {
    List<String> names = new ArrayList<String>();

    File[] children = folder.listFiles();
    if (children != null)
    {
      for (File child : children)
      {
        if (child.isDirectory() && !new File(child, REMOVE_MARKER).exists())
        {
          names.add(child.getName());
        }
      }

      Collections.sort(names, comparator);
    }

    return names;
  }

  private static void scheduleRemoval(File folder) throws IOException
  {
    File marker = new File(folder, REMOVE_MARKER);
    marker.createNewFile();
  }

  /**
   * @author Eike Stepper
   */
  private static final class Version implements Comparable<Version>
  {
    private final Integer major;

    private final Integer minor;

    private final Integer micro;

    public Version(String name)
    {
      for (int i = 0; i < name.length(); i++)
      {
        char c = name.charAt(i);
        if (!Character.isDigit(c) && c != '.')
        {
          name = name.substring(0, i);
          break;
        }
      }

      String[] segments = name.split("\\.");
      major = Integer.parseInt(segments[0]);
      minor = Integer.parseInt(segments[1]);
      micro = Integer.parseInt(segments[2]);
    }

    public int compareTo(Version o)
    {
      int result = major.compareTo(o.major);
      if (result == 0)
      {
        result = minor.compareTo(o.minor);
        if (result == 0)
        {
          result = micro.compareTo(o.micro);
        }
      }

      return result;
    }

    @Override
    public String toString()
    {
      return major + "." + minor + "." + micro;
    }
  }
}
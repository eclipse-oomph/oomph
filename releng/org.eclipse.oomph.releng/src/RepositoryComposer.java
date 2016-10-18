
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
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * @author Stepper
 */
public final class RepositoryComposer
{
  private static final String EXCLUDE_MARKER = "EXCLUDE";

  private static final String REMOVE_MARKER = "REMOVE";

  private static final Comparator<String> ALPHA_COMPARATOR = new Comparator<String>()
  {
    public int compare(String n1, String n2)
    {
      if (n1 == null)
      {
        n1 = "";
      }

      if (n2 == null)
      {
        n2 = "";
      }

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

  private static final String NIGHTLY_TYPE = "nightly";

  private static final String MILESTONE_TYPE = "milestone";

  private static final String RELEASE_TYPE = "release";

  private static final String CLEANUP_TYPE = "cleanup";

  private static final byte[] BUFFER = new byte[8192];

  private static final String LATEST = "latest";

  private static final String DROP_PROPERTIES = "drop.properties";

  private RepositoryComposer()
  {
  }

  public static void main(String[] args) throws Exception
  {
    File downloadsFolder = new File(args[0]).getCanonicalFile();
    File dropsFolder = new File(downloadsFolder, "drops");
    File updatesFolder = new File(downloadsFolder, "updates.tmp");

    String buildType = args[1];
    boolean cleanup = CLEANUP_TYPE.equals(buildType);

    String buildKey = cleanup ? null : args[2];
    String buildLabel = cleanup ? null : args[3];

    if (cleanup || RELEASE_TYPE.equals(buildType))
    {
      composeRepositories(new File(dropsFolder, RELEASE_TYPE), new File(updatesFolder, RELEASE_TYPE), VERSION_COMPARATOR, Integer.MAX_VALUE);

      if (!cleanup)
      {
        boolean milestonesChanged = false;
        File milestonesFolder = new File(dropsFolder, MILESTONE_TYPE);
        File[] children = milestonesFolder.listFiles();
        if (children != null)
        {
          for (File child : children)
          {
            if (child.isDirectory() && child.getName().contains("-" + buildKey + "-"))
            {
              scheduleRemoval(child);
              milestonesChanged = true;
            }
          }
        }

        if (milestonesChanged)
        {
          composeMilestoneRepositories(dropsFolder, new File(updatesFolder, MILESTONE_TYPE));
        }
      }
    }

    if (cleanup || MILESTONE_TYPE.equals(buildType))
    {
      composeMilestoneRepositories(dropsFolder, new File(updatesFolder, MILESTONE_TYPE));
    }

    if (cleanup || NIGHTLY_TYPE.equals(buildType))
    {
      composeRepositories(new File(dropsFolder, NIGHTLY_TYPE), new File(updatesFolder, NIGHTLY_TYPE), ALPHA_COMPARATOR, 5);
    }

    composeRepository(updatesFolder, "Oomph All", getComposites(updatesFolder));

    String latestDropFolderSuffix;
    if (cleanup)
    {
      latestDropFolderSuffix = IO.readUTF8(new File(downloadsFolder, "updates/" + LATEST + "/" + DROP_PROPERTIES));
    }
    else
    {
      String folder = buildKey;
      if (buildLabel.length() != 0)
      {
        folder += "-" + buildLabel;
      }

      latestDropFolderSuffix = buildType + "/" + folder;
    }

    File latestDropFolder = new File(dropsFolder, latestDropFolderSuffix);
    File latestUpdatesFolder = new File(updatesFolder, LATEST);

    composeRepository(latestUpdatesFolder, "Oomph Latest", Collections.singletonList(latestDropFolder));
    IO.writeUTF8(new File(latestUpdatesFolder, DROP_PROPERTIES), latestDropFolderSuffix);
  }

  private static void composeMilestoneRepositories(File dropsFolder, File updateTypeFolder) throws IOException
  {
    File milestonesFolder = new File(dropsFolder, MILESTONE_TYPE);
    if (!composeRepositories(milestonesFolder, updateTypeFolder, ALPHA_COMPARATOR, Integer.MAX_VALUE))
    {
      List<File> releaseDrop = new ArrayList<File>();

      File releasesFolder = new File(dropsFolder, RELEASE_TYPE);
      List<String> names = getSortedChildren(releasesFolder, VERSION_COMPARATOR);
      if (!names.isEmpty())
      {
        releaseDrop.add(new File(releasesFolder, names.get(0)));
      }

      composeRepository(updateTypeFolder, "Oomph Milestones", releaseDrop);

      File latestUpdatesFolder = new File(updateTypeFolder, LATEST);
      composeRepository(latestUpdatesFolder, "Oomph Latest Milestone", releaseDrop);

      if (!names.isEmpty())
      {
        IO.writeUTF8(new File(latestUpdatesFolder, DROP_PROPERTIES), RELEASE_TYPE + "/" + names.get(0));
      }
    }
  }

  private static boolean composeRepositories(File dropTypeFolder, File updateTypeFolder, Comparator<String> comparator, int max) throws IOException
  {
    List<String> dropNames = getSortedChildren(dropTypeFolder, comparator);
    if (dropNames.isEmpty())
    {
      return false;
    }

    List<File> drops = new ArrayList<File>();
    int count = 0;

    for (String dropName : dropNames)
    {
      File drop = new File(dropTypeFolder, dropName);
      if (++count > max)
      {
        scheduleRemoval(drop);
      }
      else
      {
        drops.add(drop);
      }
    }

    String dropTypeLabel = "Build";
    String dropType = dropTypeFolder.getName();

    if (RELEASE_TYPE.equals(dropType))
    {
      dropTypeLabel = "Release";
    }
    else if (MILESTONE_TYPE.equals(dropType))
    {
      dropTypeLabel = "Milestone";
    }
    else if (NIGHTLY_TYPE.equals(dropType))
    {
      dropTypeLabel = "Nightly Build";
    }

    composeRepository(updateTypeFolder, "Oomph " + dropTypeLabel + "s", drops);

    List<File> latestDrop = Collections.emptyList();
    if (!drops.isEmpty())
    {
      latestDrop = Collections.singletonList(drops.get(0));
    }

    File latestUpdatesFolder = new File(updateTypeFolder, LATEST);
    composeRepository(latestUpdatesFolder, "Oomph Latest " + dropTypeLabel, latestDrop);

    if (!drops.isEmpty())
    {
      IO.writeUTF8(new File(latestUpdatesFolder, DROP_PROPERTIES), dropType + "/" + dropNames.get(0));
    }

    return true;
  }

  private static void composeRepository(File compositeFolder, String name, List<File> drops) throws IOException
  {
    System.out.println("Composing " + compositeFolder + ":");
    for (File drop : drops)
    {
      System.out.println("       of " + drop);
    }

    long timestamp = System.currentTimeMillis();
    writeRepository(compositeFolder, true, name, timestamp, drops);
    writeRepository(compositeFolder, false, name, timestamp, drops);
  }

  private static void writeRepository(File compositeFolder, boolean metadata, String name, long timestamp, List<File> drops) throws IOException
  {
    String entryName;
    String fileName;
    String processingInstruction;
    String type;
    if (metadata)
    {
      entryName = "compositeContent.xml";
      fileName = "compositeContent.jar";
      processingInstruction = "compositeMetadataRepository";
      type = "org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository";
    }
    else
    {
      entryName = "compositeArtifacts.xml";
      fileName = "compositeArtifacts.jar";
      processingInstruction = "compositeArtifactRepository";
      type = "org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository";
    }

    compositeFolder.mkdirs();
    File file = new File(compositeFolder, fileName);
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);
    jarOutputStream.putNextEntry(new ZipEntry(entryName));

    Writer out = new OutputStreamWriter(jarOutputStream, "UTF-8");
    BufferedWriter writer = new BufferedWriter(out);

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
      String relativePath;
      if (drop.isAbsolute())
      {
        relativePath = getRelativePath(compositeFolder, drop);
      }
      else
      {
        relativePath = drop.getPath().replace(File.separatorChar, '/');
      }

      writeLine(writer, "    <child location='" + relativePath + "'/>");
    }

    writeLine(writer, "  </children>");
    writeLine(writer, "</repository>");
    writer.flush();

    jarOutputStream.closeEntry();
    jarOutputStream.close();
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
        return builder.toString() + targetPath.substring(sourcePath.length() + 1).replace(File.separatorChar, '/');
      }

      builder.append("../");
      source = source.getParentFile();
    }

    throw new IllegalStateException();
  }

  private static List<File> getComposites(File updatesFolder)
  {
    List<File> composites = new ArrayList<File>();
    addComposite(updatesFolder, composites, RELEASE_TYPE);
    addComposite(updatesFolder, composites, MILESTONE_TYPE);
    addComposite(updatesFolder, composites, NIGHTLY_TYPE);
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
        if (child.isDirectory() && !new File(child, EXCLUDE_MARKER).exists() && !new File(child, REMOVE_MARKER).exists())
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
  private static final class IO
  {
    public static String readUTF8(File file) throws IOException
    {
      InputStream inputStream = new FileInputStream(file);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      try
      {
        copy(inputStream, outputStream);
      }
      finally
      {
        close(inputStream);
      }

      return new String(outputStream.toByteArray(), "UTF-8");
    }

    public static void writeUTF8(File file, String contents) throws IOException
    {
      InputStream inputStream = new ByteArrayInputStream(contents.getBytes("UTF-8"));
      OutputStream outputStream = new FileOutputStream(file);

      try
      {
        copy(inputStream, outputStream);
      }
      finally
      {
        close(outputStream);
      }
    }

    private static long copy(InputStream input, OutputStream output) throws IOException
    {
      long length = 0;
      int n;

      while ((n = input.read(BUFFER)) != -1)
      {
        output.write(BUFFER, 0, n);
        length += n;
      }

      return length;
    }

    private static void close(Closeable closeable) throws IOException
    {
      if (closeable != null)
      {
        closeable.close();
      }
    }
  }
}

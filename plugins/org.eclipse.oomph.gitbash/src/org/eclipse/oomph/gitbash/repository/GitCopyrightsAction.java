/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.GitBash;

import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class GitCopyrightsAction extends AbstractRepositoryAction
{
  private static final String[] IGNORED_PATHS = { "resourcemanager.java", "menucardtemplate.java", "org.eclipse.oomph.version.tests\\tests",
      "org.eclipse.net4j.jms.api" };

  private static final String[] REQUIRED_EXTENSIONS = { ".java", ".ant", "build.xml", "plugin.xml", "fragment.xml", "feature.xml", "plugin.properties",
      "fragment.properties", "feature.properties", "about.properties", "build.properties", "messages.properties", "copyright.txt", ".exsd" };

  private static final String[] OPTIONAL_EXTENSIONS = { ".properties", ".xml", ".html", ".ecore", ".genmodel" };

  private static final String[] IGNORED_MESSAGES = { "update copyrights", "updated copyrights", "adjust legal headers", "adjusted legal headers",
      "update legal headers", "updated legal headers", "adjust copyrights", "adjusted copyrights", "fix copyrights", "fixed copyrights", "fix legal headers",
      "fixed legal headers" };

  private static final Pattern COPYRIGHT_PATTERN = Pattern.compile("(.*?)Copyright \\(c\\) ([0-9 ,-]+) (.*?) and others.(.*)");

  private static final String BEGIN_COMMIT = "--BEGIN-COMMIT--";

  private static final String BEGIN_MESSAGE = "--BEGIN-SUMMARY--";

  // "--BEGIN-COMMIT--
  // committer date
  // message (multiline)
  // --BEGIN-SUMMARY--
  // summary (multiline)"
  private static final String OUTPUT_FORMAT = BEGIN_COMMIT + "%n%ci%n%B%n" + BEGIN_MESSAGE + "%n";

  private static final String NL = System.getProperty("line.separator");

  private File workTree;

  private int workTreeLength;

  private List<String> missingCopyrights = new ArrayList<String>();

  private int rewriteCount;

  private File outFile;

  @Override
  protected void run(Shell shell, File workTree) throws Exception
  {
    try
    {
      this.workTree = workTree;
      workTreeLength = workTree.getAbsolutePath().length() + 1;

      checkFolder(shell, workTree);
      System.out.println("Missing count: " + missingCopyrights.size());
      System.out.println("Rewrite count: " + rewriteCount);
    }
    finally
    {
      missingCopyrights.clear();
      rewriteCount = 0;
      outFile = null;
    }
  }

  private void checkFolder(Shell shell, File folder) throws Exception
  {
    for (File file : folder.listFiles())
    {
      String fileName = file.getName();
      if (file.isDirectory() && !fileName.equals(".git"))
      {
        checkFolder(shell, file);
      }
      else if (!fileName.equals(".gitlog"))
      {
        checkFile(shell, file);
      }
    }
  }

  private void checkFile(Shell shell, File file) throws Exception
  {
    if (hasString(file.getPath(), IGNORED_PATHS))
    {
      return;
    }

    boolean required = hasExtension(file, REQUIRED_EXTENSIONS);
    boolean optional = required || hasExtension(file, OPTIONAL_EXTENSIONS);

    if (optional)
    {
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      try
      {
        int lineNumber = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
          ++lineNumber;
          Matcher matcher = COPYRIGHT_PATTERN.matcher(line);
          if (matcher.matches())
          {
            String prefix = matcher.group(1);
            String dates = matcher.group(1);
            String owner = matcher.group(3);
            String suffix = matcher.group(4);

            rewriteFile(shell, file, lineNumber, prefix, dates, owner, suffix);
            return;
          }
        }

        if (required)
        {
          String path = getPath(file);
          missingCopyrights.add(path);
          System.out.println("COPYRIGHT MISSING: " + path);
        }
      }
      finally
      {
        bufferedReader.close();
        fileReader.close();
      }
    }
  }

  private String getPath(File file)
  {
    String path = file.getAbsolutePath().replace('\\', '/');
    return path.substring(workTreeLength);
  }

  private boolean hasString(String string, String[] strings)
  {
    for (int i = 0; i < strings.length; i++)
    {
      if (string.indexOf(strings[i]) != -1)
      {
        return true;
      }
    }

    return false;
  }

  private boolean hasExtension(File file, String[] extensions)
  {
    String fileName = file.getName();
    for (int i = 0; i < extensions.length; i++)
    {
      if (fileName.endsWith(extensions[i]))
      {
        return true;
      }
    }

    return false;
  }

  private void rewriteFile(Shell shell, File file, int copyrightLineNumber, String prefix, String dates, String owner, String suffix) throws Exception
  {
    String path = getPath(file);

    if (outFile == null)
    {
      outFile = File.createTempFile("git-", ".log");
    }

    try
    {
      GitBash.quiet = true;
      GitBash.executeCommand(shell, workTree, "git log --follow --name-only --format=\"" + OUTPUT_FORMAT + "\" -- \"" + path + "\" > \"/"
          + outFile.getAbsolutePath().replace(":", "").replace("\\", "/") + "\"");
    }
    finally
    {
      GitBash.quiet = false;
    }

    Set<Integer> years = parseOutFile();
    String newDates = formatYears(years);
    if (newDates.equals(dates))
    {
      return;
    }

    ++rewriteCount;
    String copyrightLine = prefix + "Copyright (c) " + newDates + " " + owner + " and OTHERS." + suffix;
    System.out.println(path + ": " + newDates);

    List<String> lines = readLines(file);
    lines.set(copyrightLineNumber, copyrightLine);

    writeLines(file, lines);
  }

  private List<String> readLines(File file) throws FileNotFoundException, IOException
  {
    Reader fileReader = null;
    BufferedReader bufferedReader = null;

    try
    {
      fileReader = new FileReader(file);
      bufferedReader = new BufferedReader(fileReader);

      List<String> lines = new ArrayList<String>();
      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        lines.add(line);
      }

      return lines;
    }
    finally
    {
      close(bufferedReader);
      close(fileReader);
    }
  }

  private void writeLines(File file, List<String> lines) throws IOException
  {
    Writer fileWriter = null;
    BufferedWriter bufferedWriter = null;

    try
    {
      fileWriter = new FileWriter(file);
      bufferedWriter = new BufferedWriter(fileWriter);

      for (String line : lines)
      {
        bufferedWriter.write(line);
        bufferedWriter.write(NL);
      }
    }
    finally
    {
      close(bufferedWriter);
      close(fileWriter);
    }
  }

  private void close(Closeable closeable)
  {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
  }

  private String formatYears(Collection<Integer> years)
  {
    class YearRange
    {
      private int begin;

      private int end;

      public YearRange(int begin)
      {
        this.begin = begin;
        end = begin;
      }

      public boolean add(int year)
      {
        if (year == end + 1)
        {
          end = year;
          return true;
        }

        return false;
      }

      @Override
      public String toString()
      {
        if (begin == end)
        {
          return "" + begin;
        }

        if (begin == end - 1)
        {
          return "" + begin + ", " + end;
        }

        return "" + begin + "-" + end;
      }
    }

    List<Integer> list = new ArrayList<Integer>(years);
    Collections.sort(list);

    List<YearRange> ranges = new ArrayList<YearRange>();
    YearRange lastRange = null;
    for (Integer year : list)
    {
      if (lastRange == null || !lastRange.add(year))
      {
        lastRange = new YearRange(year);
        ranges.add(lastRange);
      }
    }

    StringBuilder builder = new StringBuilder();
    for (YearRange range : ranges)
    {
      if (builder.length() != 0)
      {
        builder.append(", ");
      }

      builder.append(range);
    }

    return builder.toString();
  }

  private Set<Integer> parseOutFile() throws Exception
  {
    Set<Integer> years = new HashSet<Integer>();

    FileReader fileReader = new FileReader(outFile);
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    try
    {
      LogEntry logEntry;

      // Start of file. First line has to be "--BEGIN-COMMIT--"
      String line = bufferedReader.readLine();
      if (line != null)
      {
        if (!line.equals(BEGIN_COMMIT))
        {
          throw new IllegalStateException("Read unexpected line " + line + " at beginning of file " + outFile.getAbsolutePath());
        }

        // First line successfully read. Start processing of log entries
        processing: //
        for (;;)
        {
          String date = readLineSafe(bufferedReader);
          logEntry = new LogEntry(date);

          // Follows the message until the summary marker is read
          StringBuilder builder = new StringBuilder();
          while (!(line = readLineSafe(bufferedReader)).equals(BEGIN_MESSAGE))
          {
            builder.append(line);
            builder.append("\n");
          }

          logEntry.setMessage(builder.toString());

          summaryReading: //
          for (;;)
          {
            line = bufferedReader.readLine();
            if (line == null)
            {
              handleLogEntry(years, logEntry);
              break processing; // End of file reached
            }

            if (line.equals(BEGIN_COMMIT))
            {
              handleLogEntry(years, logEntry);
              break summaryReading; // End of summary section reached
            }

            if (line.trim().length() == 0)
            {
              continue; // Read over empty lines
            }
          }
        }
      }

      return years;
    }
    finally
    {
      bufferedReader.close();
      fileReader.close();
      outFile.delete();
    }
  }

  private String readLineSafe(BufferedReader bufferedReader) throws IOException
  {
    String result = bufferedReader.readLine();
    if (result == null)
    {
      throw new IllegalStateException("Unexpected end of stream");
    }

    return result;
  }

  private void handleLogEntry(Set<Integer> years, LogEntry logEntry)
  {
    String message = logEntry.getMessage().toLowerCase();
    if (hasString(message, IGNORED_MESSAGES))
    {
      return;
    }

    int year = Integer.parseInt(logEntry.getDate().substring(0, 4));
    years.add(year);
  }

  /**
   * @author Eike Stepper
   */
  public static class LogEntry
  {
    private String date;

    private String message;

    public LogEntry(String date)
    {
      this.date = date;
    }

    public String getDate()
    {
      return date;
    }

    public void setDate(String date)
    {
      this.date = date;
    }

    public String getMessage()
    {
      return message;
    }

    public void setMessage(String message)
    {
      this.message = message;
    }
  }
}

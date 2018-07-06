/*
 * Copyright (c) 2014, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;
import org.eclipse.oomph.gitbash.Activator;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class UpdateCopyrightsAction extends AbstractAction<Repository>
{
  private static final String[] DEFAULT_IGNORED_MESSAGE_VERBS = { "update", "adjust", "fix" };

  private static final String[] DEFAULT_IGNORED_MESSAGE_NOUNS = { "copyright", "legal header" };

  private static final String[] DEFAULT_IGNORED_PATHS = { "target", "resourcemanager.java" };

  private static final String[] DEFAULT_CHECK_FILES = { ".java", ".ant", "build.xml", "plugin.xml", "fragment.xml", "feature.xml", "plugin.properties",
      "fragment.properties", "feature.properties", "about.properties", "build.properties", "messages.properties", "copyright.txt", ".exsd",
      "org.eclipse.jdt.ui.prefs" };

  private static final String[] DEFAULT_UPDATE_FILES = { ".properties", ".xml", ".css", ".ecore", ".genmodel", ".mwe", ".xpt", ".ext" };

  private static final String DEFAULT_COPYRIGHT_PATTERN = "(.*?)Copyright \\(c\\) ([0-9 ,-]+) (.*?) and others\\.(.*)";

  private static final Calendar CALENDAR = GregorianCalendar.getInstance();

  private static final int CURRENT_YEAR = CALENDAR.get(Calendar.YEAR);

  private static final String CURRENT_YEAR_STRING = Integer.toString(CURRENT_YEAR);

  private static final IWorkbench WORKBENCH = PlatformUI.getWorkbench();

  private static final String NL = System.getProperty("line.separator");

  private Git git;

  private File workTree;

  private int workTreeLength;

  private String[] ignoredMessages;

  private String[] ignoredPaths;

  private Pattern[] checkFiles;

  private Pattern[] updateFiles;

  private Pattern copyrightPattern;

  private List<String> missingCopyrights = new ArrayList<String>();

  private int rewriteCount;

  public UpdateCopyrightsAction()
  {
    super(Repository.class);
  }

  protected boolean isCheckOnly()
  {
    return false;
  }

  @Override
  protected void run(final Shell shell, final Repository repository) throws Exception
  {
    IRunnableWithProgress runnable = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        try
        {
          git = new Git(repository);
          workTree = repository.getWorkTree();
          workTreeLength = workTree.getAbsolutePath().length() + 1;

          initProperties();

          final Map<File, Boolean> files = new TreeMap<File, Boolean>();
          collectFiles(files, workTree);

          monitor.beginTask(getTitle(), files.size());

          final long start = System.currentTimeMillis();
          checkFiles(files, start, monitor);

          shell.getDisplay().syncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                handleResult(shell, workTree, files.size(), formatDuration(start));
              }
              catch (Exception ex)
              {
                Activator.log(ex);
              }
            }
          });
        }
        catch (OperationCanceledException ex)
        {
          // Do nothing
        }
        catch (Exception ex)
        {
          throw new InvocationTargetException(ex);
        }
        finally
        {
          missingCopyrights.clear();
          rewriteCount = 0;
          workTreeLength = 0;
          workTree = null;
          git = null;
          monitor.done();
        }
      }
    };

    WORKBENCH.getProgressService().run(true, true, runnable);
  }

  private void initProperties()
  {
    Properties properties = new Properties();

    File file = new File(workTree, ".legalchecks");
    if (file.isFile())
    {
      InputStream in = null;

      try
      {
        in = new FileInputStream(file);
        properties.load(in);
      }
      catch (IOException ex)
      {
        Activator.log(ex);
      }
      finally
      {
        close(in);
      }
    }

    ignoredMessages = combineWords( //
        getStrings(properties, "ignored.message.verbs", DEFAULT_IGNORED_MESSAGE_VERBS), //
        getStrings(properties, "ignored.message.nouns", DEFAULT_IGNORED_MESSAGE_NOUNS));

    ignoredPaths = getStrings(properties, "ignored.paths", DEFAULT_IGNORED_PATHS);
    checkFiles = getPatterns(properties, "check.files", DEFAULT_CHECK_FILES);
    updateFiles = getPatterns(properties, "update.files", DEFAULT_UPDATE_FILES);

    String property = properties.getProperty("copyright.pattern");
    if (property == null)
    {
      property = DEFAULT_COPYRIGHT_PATTERN;
    }

    copyrightPattern = Pattern.compile(property);
  }

  private String[] getStrings(Properties properties, String key, String[] defaultValue)
  {
    String property = properties.getProperty(key);
    if (property == null)
    {
      return defaultValue;
    }

    String[] result = property.split(",");
    for (int i = 0; i < result.length; i++)
    {
      result[i] = result[i].trim();
    }

    return result;
  }

  private Pattern[] getPatterns(Properties properties, String key, String[] defaultValue)
  {
    String[] strings = getStrings(properties, key, defaultValue);

    Pattern[] result = new Pattern[strings.length];
    for (int i = 0; i < strings.length; i++)
    {
      String string = strings[i];
      string = string.replace(".", "\\.");
      string = string.replace("*", ".*");
      string = string.replace("?", ".");

      result[i] = Pattern.compile(string);
    }

    return result;
  }

  private void collectFiles(Map<File, Boolean> files, File folder) throws Exception
  {
    for (File file : folder.listFiles())
    {
      String path = getWorkTreeRelativePath(file);
      if (!hasString(path, ignoredPaths))
      {
        String name = file.getName();
        if (file.isDirectory())
        {
          if (name.equals(".git"))
          {
            continue;
          }

          if (name.equals("bin"))
          {
            continue;
          }

          collectFiles(files, file);
        }
        else
        {
          if (name.endsWith(".class"))
          {
            continue;
          }

          boolean required = matches(file, checkFiles);
          if (required)
          {
            files.put(file, true);
          }
          else if (!isCheckOnly() && matches(file, updateFiles))
          {
            files.put(file, false);
          }
        }
      }
    }
  }

  private void checkFiles(Map<File, Boolean> files, long start, IProgressMonitor monitor) throws Exception
  {
    int i = 0;
    int totalFiles = files.size();

    for (Map.Entry<File, Boolean> entry : files.entrySet())
    {
      if (monitor.isCanceled())
      {
        throw new OperationCanceledException();
      }

      File file = entry.getKey();
      boolean required = entry.getValue();

      checkFile(file, required);
      monitor.worked(1);

      double millis = System.currentTimeMillis() - start;
      double millisPerFile = millis / ++i;

      int remainingFiles = totalFiles - i;
      int remainingSeconds = (int)(millisPerFile * remainingFiles / 1000);
      int remainingMinutes = remainingSeconds / 60;
      remainingSeconds -= remainingMinutes * 60;

      monitor.subTask("Remaining files: " + remainingFiles + ", remaining minutes: " + remainingMinutes + ":" + String.format("%02d", remainingSeconds));
    }
  }

  private void checkFile(File file, boolean required) throws Exception
  {
    String path = getWorkTreeRelativePath(file);

    List<String> lines = new ArrayList<String>();
    boolean copyrightFound = false;
    boolean copyrightChanged = false;

    FileReader fileReader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    try
    {
      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        Matcher matcher = copyrightPattern.matcher(line);
        if (matcher.matches())
        {
          copyrightFound = true;
          if (isCheckOnly())
          {
            break;
          }

          String dates = matcher.group(2);
          if (dates.endsWith(CURRENT_YEAR_STRING))
          {
            // This file must have been rewritten already in the current year
            break;
          }

          String prefix = matcher.group(1);
          String owner = matcher.group(3);
          String suffix = matcher.group(4);

          String newLine = rewriteCopyright(path, line, prefix, dates, owner, suffix);
          if (newLine != line)
          {
            line = newLine;
            copyrightChanged = true;
          }
        }

        lines.add(line);
      }
    }
    finally
    {
      close(bufferedReader);
      close(fileReader);
    }

    if (required && !copyrightFound)
    {
      missingCopyrights.add(path);
    }

    if (copyrightChanged)
    {
      writeLines(file, lines);
      ++rewriteCount;
    }
  }

  private String rewriteCopyright(String path, String line, String prefix, String dates, String owner, String suffix) throws Exception
  {
    String newDates;

    if (path.endsWith("org.eclipse.jdt.ui.prefs"))
    {
      // Special handling of JDT copyright template.
      newDates = CURRENT_YEAR_STRING;
    }
    else if (path.endsWith("copyright.txt") || path.endsWith("org.eclipse.emf.cdo.license-feature/feature.properties")
        || suffix.equals(" All rights reserved.\\n\\"))
    {
      // Special handling of occurrences with a more global (then file-scoped) meaning.
      // Special handling of second occurrence in about.properties (which is visible in the UI).
      newDates = "2004-" + CURRENT_YEAR;
    }
    else
    {
      Set<Integer> years = new HashSet<Integer>();

      for (RevCommit commit : git.log().addPath(path).call())
      {
        String message = commit.getFullMessage();
        if (!hasString(message, ignoredMessages))
        {
          CALENDAR.setTimeInMillis(1000L * commit.getCommitTime());
          int year = CALENDAR.get(Calendar.YEAR);
          years.add(year);
        }
      }

      if (years.isEmpty())
      {
        // The file doesn't have a history, may be derived.
        return line;
      }

      newDates = formatYears(years);
    }

    if (newDates.equals(dates))
    {
      return line;
    }

    return prefix + "Copyright (c) " + newDates + " " + owner + " and others." + suffix;
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

  private String getTitle()
  {
    return (isCheckOnly() ? "Check" : "Update") + " Copyrights";
  }

  private String getWorkTreeRelativePath(File file)
  {
    String path = file.getAbsolutePath().replace('\\', '/');
    return path.substring(workTreeLength);
  }

  private boolean hasString(String string, String[] strings)
  {
    string = string.toLowerCase();
    for (int i = 0; i < strings.length; i++)
    {
      if (string.indexOf(strings[i]) != -1)
      {
        return true;
      }
    }

    return false;
  }

  private boolean matches(File file, Pattern[] patterns)
  {
    String fileName = file.getName().toLowerCase();
    for (int i = 0; i < patterns.length; i++)
    {
      if (patterns[i].matcher(fileName).matches())
      {
        return true;
      }
    }

    return false;
  }

  private void handleResult(Shell shell, File workTree, int fileCount, String duration) throws PartInitException
  {
    String message = "Copyrights missing: " + missingCopyrights.size();
    message += "\nCopyrights rewritten: " + rewriteCount;
    message += "\nFiles visited: " + fileCount;
    message += "\nTime needed: " + duration;

    int size = missingCopyrights.size();
    if (size != 0)
    {
      StringBuilder builder = new StringBuilder(message);
      message += "\n\nDo you want to open the files with missing copyrights in editors?";
      if (MessageDialog.openQuestion(shell, getTitle(), message))
      {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IWorkbenchPage page = WORKBENCH.getActiveWorkbenchWindow().getActivePage();

        builder.append("\n");
        for (String missingCopyright : missingCopyrights)
        {
          builder.append("\nMissing: ");
          builder.append(missingCopyright);

          File externalFile = new File(workTree, missingCopyright);
          IFile file = root.getFileForLocation(new Path(externalFile.getAbsolutePath()));
          if (file != null && file.isAccessible())
          {
            IDE.openEditor(page, file);
          }
          else
          {
            IFileStore fileStore = EFS.getLocalFileSystem().getStore(externalFile.toURI());
            if (fileStore != null)
            {
              IDE.openEditorOnFileStore(page, fileStore);
            }
          }
        }

        Activator.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, builder.toString()));
      }
    }
    else
    {
      MessageDialog.openInformation(shell, getTitle(), message);
      Activator.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
    }
  }

  private static String[] combineWords(String[] verbs, String[] nouns)
  {
    List<String> result = new ArrayList<String>();
    for (String noun : nouns)
    {
      for (String verb : verbs)
      {
        result.add(verb + " " + noun);

        if (verb.endsWith("e"))
        {
          verb += "d";
        }
        else
        {
          verb += "ed";
        }

        result.add(verb + " " + noun);
      }
    }

    return result.toArray(new String[result.size()]);
  }

  private static String formatDuration(long start)
  {
    double duration = System.currentTimeMillis() - start;

    String unit = "milliseconds";
    if (duration > 1000d)
    {
      duration = duration / 1000d;
      unit = "seconds";

      if (duration > 60d)
      {
        duration = duration / 60d;
        unit = "minutes";

        if (duration > 60d)
        {
          duration = duration / 60d;
          unit = "hours";
        }
      }
    }

    duration = Math.round(duration * 100d) / 100d;
    return duration + " " + unit;
  }

  private static void writeLines(File file, List<String> lines) throws IOException
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

  private static void close(Closeable closeable)
  {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      }
      catch (IOException ex)
      {
        Activator.log(ex);
      }
    }
  }
}

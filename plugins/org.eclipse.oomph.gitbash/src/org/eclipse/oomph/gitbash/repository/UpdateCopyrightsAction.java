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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class UpdateCopyrightsAction extends AbstractAction<Repository>
{
  private static final String[] IGNORED_PATHS = { "resourcemanager.java", "menucardtemplate.java", "org.eclipse.oomph.version.tests/tests",
      "org.eclipse.net4j.jms.api/src", "org/eclipse/net4j/util/ui/proposals/", "org.eclipse.emf.cdo.examples.installer/examples",
      "org.eclipse.net4j.examples.installer/examples" };

  private static final String[] REQUIRED_EXTENSIONS = { ".java", ".ant", "build.xml", "plugin.xml", "fragment.xml", "feature.xml", "plugin.properties",
      "fragment.properties", "feature.properties", "about.properties", "build.properties", "messages.properties", "copyright.txt", ".exsd",
      "org.eclipse.jdt.ui.prefs" };

  private static final String[] OPTIONAL_EXTENSIONS = { ".properties", ".xml", ".css", ".ecore", ".genmodel", ".mwe", ".xpt", ".ext" };

  private static final String[] IGNORED_MESSAGE_VERBS = { "update", "adjust", "fix" };

  private static final String[] IGNORED_MESSAGE_NOUNS = { "copyright", "legal header" };

  private static final String[] IGNORED_MESSAGES = combineWords(IGNORED_MESSAGE_VERBS, IGNORED_MESSAGE_NOUNS);

  private static final Pattern COPYRIGHT_PATTERN = Pattern.compile("(.*?)Copyright \\(c\\) ([0-9 ,-]+) (.*?) and others\\.(.*)");

  private static final Calendar CALENDAR = GregorianCalendar.getInstance();

  private static final int CURRENT_YEAR = CALENDAR.get(Calendar.YEAR);

  private static final String CURRENT_YEAR_STRING = Integer.toString(CURRENT_YEAR);

  private static final IWorkbench WORKBENCH = PlatformUI.getWorkbench();

  private static final String NL = System.getProperty("line.separator");

  private Git git;

  private File workTree;

  private int workTreeLength;

  private List<String> missingCopyrights = new ArrayList<String>();

  private int rewriteCount;

  private int fileCount;

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

          fileCount = countFiles(workTree);
          monitor.beginTask(getTitle(), fileCount);

          final long start = System.currentTimeMillis();
          checkFolder(monitor, workTree);

          shell.getDisplay().syncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                handleResult(shell, workTree, formatDuration(start));
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
          fileCount = 0;
          workTreeLength = 0;
          workTree = null;
          git = null;
          monitor.done();
        }
      }
    };

    WORKBENCH.getProgressService().run(true, true, runnable);
  }

  private int countFiles(File folder) throws Exception
  {
    int count = 0;
    for (File file : folder.listFiles())
    {
      String fileName = file.getName();
      if (file.isDirectory() && !fileName.equals(".git"))
      {
        count += countFiles(file);
      }
      else
      {
        String path = getPath(file);
        if (hasString(path, IGNORED_PATHS))
        {
          continue;
        }

        ++count;
      }
    }

    return count;
  }

  private void checkFolder(IProgressMonitor monitor, File folder) throws Exception
  {
    for (File file : folder.listFiles())
    {
      if (monitor.isCanceled())
      {
        throw new OperationCanceledException();
      }

      String fileName = file.getName();
      if (file.isDirectory() && !fileName.equals(".git"))
      {
        checkFolder(monitor, file);
      }
      else
      {
        if (checkFile(monitor, file))
        {
          monitor.worked(1);
        }
      }
    }
  }

  private boolean checkFile(IProgressMonitor monitor, File file) throws Exception
  {
    String path = getPath(file);
    if (!hasString(path, IGNORED_PATHS))
    {
      boolean required = hasExtension(file, REQUIRED_EXTENSIONS);
      boolean optional = required || hasExtension(file, OPTIONAL_EXTENSIONS);

      boolean checkOnly = isCheckOnly();
      if (checkOnly ? required : optional)
      {
        monitor.subTask(path);

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
            Matcher matcher = COPYRIGHT_PATTERN.matcher(line);
            if (matcher.matches())
            {
              copyrightFound = true;
              if (checkOnly)
              {
                break;
              }

              String prefix = matcher.group(1);
              String dates = matcher.group(2);
              String owner = matcher.group(3);
              String suffix = matcher.group(4);

              if (dates.endsWith(CURRENT_YEAR_STRING))
              {
                // This file must have been rewritten already in the current year
                break;
              }

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

      return true;
    }

    return false;
  }

  private String rewriteCopyright(String path, String line, String prefix, String dates, String owner, String suffix) throws Exception
  {
    String newDates;

    if (path.endsWith("org.eclipse.jdt.ui.prefs") || path.endsWith("copyright.txt") || path.endsWith("org.eclipse.emf.cdo.license-feature/feature.properties")
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
        if (!hasString(message, IGNORED_MESSAGES))
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

    return prefix + "Copyright (c) " + newDates + " " + owner + " and" + " others." + suffix;
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

  private String getPath(File file)
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

  private boolean hasExtension(File file, String[] extensions)
  {
    String fileName = file.getName().toLowerCase();
    for (int i = 0; i < extensions.length; i++)
    {
      if (fileName.endsWith(extensions[i]))
      {
        return true;
      }
    }

    return false;
  }

  private void handleResult(Shell shell, File workTree, String duration) throws PartInitException
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

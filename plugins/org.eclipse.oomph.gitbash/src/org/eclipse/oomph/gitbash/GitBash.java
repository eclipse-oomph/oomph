/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Eike Stepper
 */
public class GitBash
{
  private static final String DEFAULT_EXECUTABLE = "C:\\Program Files (x86)\\Git\\bin\\sh.exe";

  public static boolean quiet;

  /**
   * The path to the Git Bash executable.
   */
  private static String executable;

  public GitBash()
  {
  }

  public static synchronized String getExecutable(Shell shell)
  {
    if (executable == null)
    {
      File stateFile = Activator.getDefault().getStateLocation().append("git-bash.txt").toFile();
      if (stateFile.isFile())
      {
        executable = loadFile(stateFile);
      }

      if (executable == null)
      {
        executable = openInputDialog(DEFAULT_EXECUTABLE, shell);
        if (executable == null)
        {
          return null;
        }
      }

      if (!new File(executable).isFile())
      {
        executable = openInputDialog(executable, shell);
      }

      saveFile(stateFile, executable);
    }

    return executable;
  }

  public static void executeCommand(Shell shell, File workTree, String command)
  {
    try
    {
      String gitBash = getExecutable(shell);

      if (gitBash != null)
      {
        ProcessBuilder builder = new ProcessBuilder(gitBash, "--login", "-c", command);
        builder.directory(workTree);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
          output.append(line);
          output.append("\n");
        }

        int exitValue = process.waitFor();
        if (exitValue == 0)
        {
          String message = "Command '" + command + "' executed successfully";
          if (!quiet)
          {
            Activator.log(message + "\n" + output, IStatus.INFO);
            MessageDialog.openInformation(shell, "Git Bash", message);
          }
        }
        else
        {
          String message = "Command '" + command + "' failed: " + exitValue;
          if (!quiet)
          {
            Activator.log(message + "\n" + output, IStatus.ERROR);
            MessageDialog.openError(shell, "Git Bash", message);
          }
        }
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static String openInputDialog(String initial, Shell shell)
  {
    InputDialog dialog = new InputDialog(shell, "Git Bash", "Location:", initial, new IInputValidator()
    {
      public String isValid(String newText)
      {
        return new File(newText).isFile() ? null : "Not a file!";
      }
    });

    if (dialog.open() != InputDialog.OK)
    {
      return null;
    }

    return dialog.getValue();
  }

  private static String loadFile(File file)
  {
    FileReader in = null;

    try
    {
      in = new FileReader(file);
      return new BufferedReader(in).readLine();
    }
    catch (IOException ex)
    {
      return null;
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException ex)
        {
          // Ignore
        }
      }
    }
  }

  private static void saveFile(File file, String content)
  {
    FileWriter out = null;

    try
    {
      out = new FileWriter(file);
      out.write(content);
    }
    catch (IOException ex)
    {
      throw new IllegalStateException("Could not write to " + file, ex);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException ex)
        {
          // Ignore
        }
      }
    }
  }
}

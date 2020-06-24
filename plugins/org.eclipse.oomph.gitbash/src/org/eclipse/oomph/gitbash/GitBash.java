/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
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
  private static final String DEFAULT_EXECUTABLE = "C:\\Program Files (x86)\\Git\\bin\\sh.exe"; //$NON-NLS-1$

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
      File stateFile = Activator.getDefault().getStateLocation().append("git-bash.txt").toFile(); //$NON-NLS-1$
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
        ProcessBuilder builder = new ProcessBuilder(gitBash, "--login", "-c", command); //$NON-NLS-1$ //$NON-NLS-2$
        builder.directory(workTree);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
          output.append(line);
          output.append("\n"); //$NON-NLS-1$
        }

        int exitValue = process.waitFor();
        if (exitValue == 0)
        {
          String message = NLS.bind(Messages.GitBash_Success_message, command);
          if (!quiet)
          {
            Activator.log(message + "\n" + output, IStatus.INFO); //$NON-NLS-1$
            MessageDialog.openInformation(shell, Messages.GitBash_GitBash_title, message);
          }
        }
        else
        {
          String message = NLS.bind(Messages.GitBash_Failed_message, command, exitValue);
          if (!quiet)
          {
            Activator.log(message + "\n" + output, IStatus.ERROR); //$NON-NLS-1$
            MessageDialog.openError(shell, Messages.GitBash_GitBash_title, message);
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
    InputDialog dialog = new InputDialog(shell, Messages.GitBash_GitBash_title, Messages.GitBash_Location_label, initial, new IInputValidator()
    {
      public String isValid(String newText)
      {
        return new File(newText).isFile() ? null : Messages.GitBash_NotFile_imessage;
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
      throw new IllegalStateException(NLS.bind(Messages.GitBash_WriteFailuire_exception, file), ex);
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

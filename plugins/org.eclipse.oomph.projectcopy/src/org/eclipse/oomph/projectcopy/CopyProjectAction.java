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
package org.eclipse.oomph.projectcopy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Eike Stepper
 */
public class CopyProjectAction implements IObjectActionDelegate
{
  private static final IWorkspaceRoot ROOT = ResourcesPlugin.getWorkspace().getRoot();

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private Shell shell;

  private ISelection selection;

  public CopyProjectAction()
  {
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    shell = targetPart.getSite().getShell();
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }

  public void run(IAction action)
  {
    IProject source = (IProject)((IStructuredSelection)selection).getFirstElement();
    File folder = source.getLocation().toFile();

    try
    {
      final File parentFolder = folder.getParentFile();

      InputDialog dialog = new InputDialog(shell, "Copy Project", "Name of the new project:", source.getName(), new IInputValidator()
      {
        public String isValid(String newName)
        {
          IProject newProject = ROOT.getProject(newName);
          if (newProject.exists())
          {
            return "Project " + newName + " already exists.";
          }

          File newFolder = new File(parentFolder, newName);
          if (newFolder.exists())
          {
            return "Location " + newFolder.getAbsolutePath() + " already exists.";
          }

          return null;
        }
      });

      if (dialog.open() == Dialog.OK)
      {
        String newName = dialog.getValue();
        File newFolder = new File(parentFolder, newName);
        copyTree(folder, source.getName(), newName, folder, newFolder);
        importProject(newFolder, newName);
      }
    }
    catch (Exception ex)
    {
      Activator.log("Could not fully copy project " + source.getName(), ex);
    }
  }

  private static void copyTree(File folder, String oldName, String newName, File source, File target) throws IOException
  {
    if (source.isDirectory())
    {
      mkdirs(target);
      File[] files = source.listFiles();
      for (File file : files)
      {
        String name = file.getName();
        copyTree(folder, oldName, newName, new File(source, name), new File(target, name));
      }
    }
    else
    {
      if (source.equals(new File(folder, ".project")))
      {
        Replacer replacer = new Replacer("<name>" + oldName + "</name>", "<name>" + newName + "</name>");
        replacer.copy(source, target);
      }
      else if (source.equals(new File(new File(folder, "META-INF"), "MANIFEST.MF")))
      {
        Replacer replacer = new Replacer("Bundle-SymbolicName: " + oldName, "Bundle-SymbolicName: " + newName);
        replacer.copy(source, target);
      }
      else if (source.equals(new File(folder, "feature.xml")))
      {
        oldName = removeFeatureSuffix(oldName);
        newName = removeFeatureSuffix(newName);
        Replacer replacer = new Replacer("id=\"" + oldName + "\"", "id=\"" + newName + "\"");
        replacer.copy(source, target);
      }
      else
      {
        copyFile(source, target);
      }
    }
  }

  private static String removeFeatureSuffix(String name)
  {
    if (name.endsWith("-feature"))
    {
      name = name.substring(0, name.length() - "-feature".length());
    }

    return name;
  }

  private static void copyFile(File source, File target) throws IOException
  {
    mkdirs(target.getParentFile());
    FileInputStream input = null;
    FileOutputStream output = null;

    try
    {
      input = new FileInputStream(source);
      output = new FileOutputStream(target);
      copy(input, output);
    }
    finally
    {
      close(input, source);
      close(output, target);
    }
  }

  private static void copy(InputStream input, OutputStream output, byte buffer[]) throws IOException
  {
    int n;
    while ((n = input.read(buffer)) != -1)
    {
      output.write(buffer, 0, n);
    }
  }

  private static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException
  {
    copy(input, output, new byte[bufferSize]);
  }

  private static void copy(InputStream input, OutputStream output) throws IOException
  {
    copy(input, output, DEFAULT_BUFFER_SIZE);
  }

  private static void mkdirs(File folder) throws IOException
  {
    if (!folder.exists())
    {
      if (!folder.mkdirs())
      {
        throw new IOException("Unable to create directory " + folder.getAbsolutePath());
      }
    }
  }

  private static void importProject(final File folder, final String name) throws CoreException
  {
    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
    workspace.run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        IProjectDescription description = workspace.newProjectDescription(name);
        description.setLocation(new Path(folder.getAbsolutePath()));

        IProject project = workspace.getRoot().getProject(name);
        project.create(description, monitor);

        if (!project.isOpen())
        {
          project.open(monitor);
        }
      }
    }, new NullProgressMonitor());
  }

  private static void close(Closeable closeable, Object file)
  {
    try
    {
      if (closeable != null)
      {
        closeable.close();
      }
    }
    catch (IOException ex)
    {
      Activator.log("Could not close " + file, ex);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Replacer
  {
    private final String search;

    private final String replace;

    private boolean done;

    public Replacer(String search, String replace)
    {
      this.search = search;
      this.replace = replace;
    }

    public final void copy(File source, File target) throws IOException
    {
      BufferedReader reader = null;
      BufferedWriter writer = null;

      try
      {
        reader = new BufferedReader(new FileReader(source));
        writer = new BufferedWriter(new FileWriter(target));
        copyText(reader, writer);
      }
      finally
      {
        close(reader, source);
        close(writer, target);
      }
    }

    private void copyText(BufferedReader reader, BufferedWriter writer) throws IOException
    {
      String line;
      while ((line = reader.readLine()) != null)
      {
        if (!done)
        {
          int start = line.indexOf(search);
          if (start != -1)
          {
            line = line.substring(0, start) + replace + line.substring(start + search.length());
            done = true;
          }
        }

        writer.write(line);
        writer.newLine();
      }
    }
  }
}

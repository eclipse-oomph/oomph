/*
 * Copyright (c) 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectcopy;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementUpdater;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ProjectCopyAction implements IObjectActionDelegate
{
  private Shell shell;

  private ISelection selection;

  public ProjectCopyAction()
  {
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    shell = targetPart.getSite().getShell();
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }

  @Override
  public void run(IAction action)
  {
    final IProject source = (IProject)((IStructuredSelection)selection).getFirstElement();
    final File folder = source.getLocation().toFile();

    try
    {
      final IWorkspaceRoot ROOT = ResourcesPlugin.getWorkspace().getRoot();
      final File parentFolder = folder.getParentFile();

      InputDialog dialog = new InputDialog(shell, Messages.ProjectCopyAction_dialog_title, Messages.ProjectCopyAction_dialog_message, source.getName(),
          new IInputValidator()
          {
            @Override
            public String isValid(String newName)
            {
              try
              {
                IProject newProject = ROOT.getProject(newName);
                if (newProject.exists())
                {
                  return NLS.bind(Messages.ProjectCopyAction_dialog_nameValidator_projectAlreadyExists, newName);
                }

                File newFolder = new File(parentFolder, newName);
                if (newFolder.exists())
                {
                  return NLS.bind(Messages.ProjectCopyAction_dialog_nameValidator_locationAlreadyExists, newFolder.getAbsolutePath());
                }

                return null;
              }
              catch (Exception exception)
              {
                return exception.getLocalizedMessage();
              }
            }
          });

      if (dialog.open() == Dialog.OK)
      {
        final String newName = dialog.getValue();

        new Job(Messages.ProjectCopyAction_copyProjectJob_name)
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            try
            {
              ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
              {
                @Override
                public void run(IProgressMonitor monitor) throws CoreException
                {
                  try
                  {
                    File newFolder = new File(parentFolder, newName);
                    copyTree(folder, source.getName(), newName, folder, newFolder);
                    importProject(newFolder, newName);
                  }
                  catch (Exception ex)
                  {
                    ProjectCopyPlugin.INSTANCE.coreException(ex);
                  }
                }
              }, monitor);

              return Status.OK_STATUS;
            }
            catch (CoreException ex)
            {
              return ex.getStatus();
            }
          }
        }.schedule();
      }
    }
    catch (Exception ex)
    {
      ProjectCopyPlugin.INSTANCE.log(ex);
    }
  }

  private static void copyTree(File folder, String oldName, String newName, File source, File target) throws Exception
  {
    if (source.isDirectory())
    {
      IOUtil.mkdirs(target);
      File[] files = source.listFiles();
      for (File file : files)
      {
        String name = file.getName();
        copyTree(folder, oldName, newName, new File(source, name), new File(target, name));
      }
    }
    else
    {
      if (source.equals(new File(folder, ".project"))) //$NON-NLS-1$
      {
        Replacer replacer = new Replacer("<name>" + oldName + "</name>", "<name>" + newName + "</name>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        replacer.copy(source, target);
      }
      else if (source.equals(new File(folder, ".options"))) //$NON-NLS-1$
      {
        Replacer replacer = new Replacer(true);
        replacer.addSubstitution(oldName + "/", newName + "/"); //$NON-NLS-1$ //$NON-NLS-2$
        replacer.copy(source, target);
      }
      else if (source.equals(new File(new File(folder, "META-INF"), "MANIFEST.MF"))) //$NON-NLS-1$ //$NON-NLS-2$
      {
        Replacer replacer = new Replacer(true);
        replacer.addSubstitution("Bundle-SymbolicName: " + oldName, "Bundle-SymbolicName: " + newName); //$NON-NLS-1$ //$NON-NLS-2$
        replacer.addSubstitution("Automatic-Module-Name: " + oldName, "Automatic-Module-Name: " + newName); //$NON-NLS-1$ //$NON-NLS-2$
        replacer.copy(source, target);
      }
      else if (source.equals(new File(folder, "feature.xml"))) //$NON-NLS-1$
      {
        oldName = replaceFeatureSuffix(oldName, ""); //$NON-NLS-1$
        newName = replaceFeatureSuffix(newName, ""); //$NON-NLS-1$
        Replacer replacer = new Replacer("id=\"" + oldName + "\"", "id=\"" + newName + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        replacer.copy(source, target);
      }
      else if (source.equals(new File(folder, "component.def"))) //$NON-NLS-1$
      {
        oldName = replaceFeatureSuffix(oldName, ".feature.group"); //$NON-NLS-1$
        newName = replaceFeatureSuffix(newName, ".feature.group"); //$NON-NLS-1$
        Replacer replacer = new Replacer("id=\"" + oldName + "\"", "id=\"" + newName + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        replacer.copy(source, target);
      }
      else if (source.equals(new File(folder, "pom.xml"))) //$NON-NLS-1$
      {
        oldName = replaceFeatureSuffix(oldName, ""); //$NON-NLS-1$
        newName = replaceFeatureSuffix(newName, ""); //$NON-NLS-1$

        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
        Element rootElement = XMLUtil.loadRootElement(documentBuilder, source);

        ElementUpdater artifactIDUpdater = new ElementUpdater(rootElement, "artifactId"); //$NON-NLS-1$
        String oldContents = IOUtil.readUTF8(source);
        String newContents = artifactIDUpdater.update(oldContents, newName);
        IOUtil.writeUTF8(target, newContents);
      }
      else
      {
        IOUtil.copyFile(source, target);
      }
    }
  }

  private static String replaceFeatureSuffix(String name, String newSuffix)
  {
    if (name.endsWith("-feature")) //$NON-NLS-1$
    {
      name = name.substring(0, name.length() - "-feature".length()); //$NON-NLS-1$
      name += newSuffix;
    }

    return name;
  }

  private static void importProject(final File folder, final String name) throws CoreException
  {
    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
    workspace.run(new IWorkspaceRunnable()
    {
      @Override
      public void run(IProgressMonitor monitor) throws CoreException
      {
        Path locationPath = new Path(folder.getAbsolutePath());
        if (Platform.getLocation().isPrefixOf(locationPath))
        {
          locationPath = null;
        }

        IProjectDescription description = workspace.newProjectDescription(name);
        description.setLocation(locationPath);

        IProject project = workspace.getRoot().getProject(name);
        project.create(description, monitor);

        if (!project.isOpen())
        {
          project.open(monitor);
        }
      }
    }, new NullProgressMonitor());
  }

  /**
   * @author Eike Stepper
   */
  public static class Replacer
  {
    private final Map<String, String> substitutions = new LinkedHashMap<>();

    private final boolean multi;

    private boolean found;

    public Replacer(boolean multi)
    {
      this.multi = multi;
    }

    public Replacer(String search, String replace)
    {
      addSubstitution(search, replace);
      multi = false;
    }

    public void addSubstitution(String search, String replace)
    {
      substitutions.put(search, replace);
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
        IOUtil.close(reader);
        IOUtil.close(writer);
      }
    }

    private void copyText(BufferedReader reader, BufferedWriter writer) throws IOException
    {
      String line;
      while ((line = reader.readLine()) != null)
      {
        if (multi || !found)
        {
          for (Map.Entry<String, String> entry : substitutions.entrySet())
          {
            String search = entry.getKey();
            String replace = entry.getValue();

            int start = line.indexOf(search);
            if (start != -1)
            {
              line = line.substring(0, start) + replace + line.substring(start + search.length());
              found = true;
            }
          }
        }

        writer.write(line);
        writer.newLine();
      }
    }
  }
}

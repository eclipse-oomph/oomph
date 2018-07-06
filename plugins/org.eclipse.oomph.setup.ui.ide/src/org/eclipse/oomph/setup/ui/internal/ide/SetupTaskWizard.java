/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.emf.codegen.ecore.CodeGenEcorePlugin;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.presentation.GeneratorUIUtil;
import org.eclipse.emf.codegen.ecore.genmodel.util.GenModelUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import org.osgi.framework.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SetupTaskWizard extends Wizard implements INewWizard
{
  private static final String TEMPLATES = "templates";

  private SetupTaskWizardPage page;

  public SetupTaskWizard()
  {
    setNeedsProgressMonitor(true);
  }

  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
  }

  @Override
  public void addPages()
  {
    page = new SetupTaskWizardPage();
    addPage(page);
  }

  @Override
  public boolean performFinish()
  {
    final Map<String, String> variables = page.getVariables();

    IRunnableWithProgress op = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException
      {
        try
        {
          doFinish(variables, monitor);
        }
        catch (Exception e)
        {
          throw new InvocationTargetException(e);
        }
        finally
        {
          monitor.done();
        }
      }
    };

    try
    {
      getContainer().run(true, false, op);
    }
    catch (InterruptedException e)
    {
      return false;
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Throwable realException = e.getTargetException();
      MessageDialog.openError(getShell(), "Error", realException.getMessage());
      return false;
    }

    return true;
  }

  private void doFinish(Map<String, String> variables, IProgressMonitor monitor) throws Exception
  {
    String sourceRoot = TEMPLATES + "/";
    String targetRoot = new File(variables.get("@ContainerLocation@")) + "/";

    final List<File> projects = new ArrayList<File>();
    final List<File> genModels = new ArrayList<File>();
    final List<File> filesToOpen = new ArrayList<File>();
    generate(variables, sourceRoot.length(), targetRoot, sourceRoot, projects, genModels, filesToOpen, 0);

    final Shell shell = getShell();

    int importWork = projects.size();
    int generateWork = 10 * genModels.size();
    SubMonitor progress = SubMonitor.convert(monitor, importWork + generateWork).detectCancelation();

    ResourcesUtil.importProjects(projects, progress.newChild(importWork));

    ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        SubMonitor progress = SubMonitor.convert(monitor, genModels.size()).detectCancelation();

        try
        {
          for (File genModelFile : genModels)
          {
            IFile iFile = getIFile(genModelFile);
            progress.setTaskName("Generating " + iFile.getFullPath());

            URI uri = URI.createPlatformResourceURI(iFile.getFullPath().toString(), true);

            ResourceSetImpl resourceSet = new ResourceSetImpl();
            resourceSet.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap(true));

            Resource resource = resourceSet.getResource(uri, true);

            GenModel genModel = (GenModel)resource.getContents().get(0);
            genModel.reconcile();
            genModel.setCanGenerate(true);

            Generator generator = GenModelUtil.createGenerator(genModel);

            GeneratorUIUtil.GeneratorOperation operation = new GeneratorUIUtil.GeneratorOperation(shell);
            operation.addGeneratorAndArguments(generator, genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE,
                CodeGenEcorePlugin.INSTANCE.getString("_UI_ModelProject_name"));
            operation.addGeneratorAndArguments(generator, genModel, GenBaseGeneratorAdapter.EDIT_PROJECT_TYPE,
                CodeGenEcorePlugin.INSTANCE.getString("_UI_EditProject_name"));

            operation.run(progress.newChild());
          }
        }
        catch (Exception ex)
        {
          SetupUIIDEPlugin.INSTANCE.coreException(ex);
        }
        finally
        {
          progress.done();
        }
      }
    }, progress.newChild(generateWork));

    progress.setTaskName("Opening files for editing...");
    shell.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IFile first = null;

        for (File file : filesToOpen)
        {
          try
          {
            IFile iFile = getIFile(file);
            IDE.openEditor(page, iFile, true);

            if (first == null)
            {
              first = iFile;
            }
          }
          catch (PartInitException e)
          {
          }

          try
          {
            if (first != null)
            {
              IDE.openEditor(page, first, true);
            }
          }
          catch (PartInitException e)
          {
          }
        }
      }
    });
  }

  private void generate(Map<String, String> variables, int sourceRootLength, String targetRoot, String entry, List<File> projects, List<File> genModels,
      List<File> filesToOpen, int level) throws Exception
  {
    String expanded = expand(variables, entry);
    File file = new File(targetRoot + expanded.substring(sourceRootLength));

    if (level == 1)
    {
      projects.add(file);
    }

    Bundle bundle = SetupUIIDEPlugin.INSTANCE.getBundle();

    if (entry.endsWith("/"))
    {
      file.mkdirs();

      String path = entry.substring(0, entry.length() - 1);
      Enumeration<String> entries = bundle.getEntryPaths(path);
      if (entries != null)
      {
        while (entries.hasMoreElements())
        {
          String childEntry = entries.nextElement();
          generate(variables, sourceRootLength, targetRoot, childEntry, projects, genModels, filesToOpen, level + 1);
        }
      }
    }
    else
    {
      if (file.getName().endsWith(".genmodel"))
      {
        genModels.add(file);
        filesToOpen.add(file);
      }
      else if (file.getName().endsWith(".ecore"))
      {
        filesToOpen.add(file);
      }
      else if (file.getName().endsWith("TaskImpl.java"))
      {
        filesToOpen.add(file);
      }

      InputStream source = null;
      OutputStream target = null;

      try
      {
        URL url = bundle.getResource(entry);
        source = url.openStream();
        target = new FileOutputStream(file);

        if (isBinary(file))
        {
          IOUtil.copy(source, target);
        }
        else
        {
          BufferedReader reader = new BufferedReader(new InputStreamReader(source));
          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(target));

          String line;
          while ((line = reader.readLine()) != null)
          {
            line = expand(variables, line);
            writer.write(line);
            writer.write(StringUtil.NL);
          }

          writer.flush();
        }
      }
      finally
      {
        IOUtil.closeSilent(source);
        IOUtil.closeSilent(target);
      }
    }
  }

  private static IFile getIFile(File file)
  {
    IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(file.toURI());
    IFile iFile = files[0];
    return iFile;
  }

  private static boolean isBinary(File file)
  {
    String name = file.getName();
    if (name.endsWith(".gif"))
    {
      return true;
    }

    if (name.endsWith(".png"))
    {
      return true;
    }

    if (name.endsWith(".bmp"))
    {
      return true;
    }

    return false;
  }

  private static String expand(Map<String, String> variables, String string)
  {
    for (Map.Entry<String, String> variable : variables.entrySet())
    {
      String key = variable.getKey();
      String value = variable.getValue();
      string = expand(string, key, value);
    }

    return string;
  }

  private static String expand(String string, String key, String value)
  {
    for (;;)
    {
      String newString = string.replace(key, value);
      if (newString.equals(string))
      {
        break;
      }

      string = newString;
    }

    return string;
  }

  public static List<File> getProjectLocations(String projectName, String containerLocation)
  {
    List<File> projects = new ArrayList<File>();

    Bundle bundle = SetupUIIDEPlugin.INSTANCE.getBundle();
    Enumeration<String> entries = bundle.getEntryPaths(TEMPLATES);
    if (entries != null)
    {
      while (entries.hasMoreElements())
      {
        String entry = entries.nextElement();
        String expanded = expand(entry, "@ProjectName@", projectName);

        File file = new File(containerLocation, expanded.substring(TEMPLATES.length() + 1));
        projects.add(file);
      }
    }

    return projects;
  }
}

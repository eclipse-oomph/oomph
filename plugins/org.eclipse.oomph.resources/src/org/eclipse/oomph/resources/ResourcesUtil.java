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
package org.eclipse.oomph.resources;

import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.SubMonitor;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
public final class ResourcesUtil
{
  private ResourcesUtil()
  {
  }

  private static IWorkspace getWorkspace()
  {
    return org.eclipse.core.resources.ResourcesPlugin.getWorkspace();
  }

  public static String getProjectName(File folder) throws ParserConfigurationException, Exception
  {
    DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
    Element rootElement = XMLUtil.loadRootElement(documentBuilder, new File(folder, ".project"));

    final AtomicReference<String> projectName = new AtomicReference<String>();
    XMLUtil.handleChildElements(rootElement, new ElementHandler()
    {
      public void handleElement(Element element) throws Exception
      {
        if ("name".equals(element.getTagName()))
        {
          projectName.set(element.getTextContent().trim());
        }
      }
    });

    return projectName.get();
  }

  public static int importProjects(final Collection<File> projectLocations, IProgressMonitor monitor) throws CoreException
  {
    if (projectLocations.isEmpty())
    {
      return 0;
    }

    final AtomicInteger count = new AtomicInteger();
    getWorkspace().run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        SubMonitor progress = SubMonitor.convert(monitor, projectLocations.size()).detectCancelation();

        try
        {
          for (File folder : projectLocations)
          {
            if (importProject(folder, progress.newChild()))
            {
              count.incrementAndGet();
            }
          }
        }
        catch (Exception ex)
        {
          ResourcesPlugin.INSTANCE.coreException(ex);
        }
        finally
        {
          progress.done();
        }
      }
    }, monitor);

    return count.get();
  }

  private static boolean importProject(File folder, IProgressMonitor monitor) throws Exception
  {
    String name = getProjectName(folder);
    if (name != null && name.length() != 0)
    {
      File location = folder.getCanonicalFile();

      IWorkspace workspace = getWorkspace();
      IProject project = workspace.getRoot().getProject(name);
      if (project.exists())
      {
        File existingLocation = new File(project.getLocation().toOSString()).getCanonicalFile();
        if (!existingLocation.equals(location))
        {
          ResourcesPlugin.INSTANCE.log("Project " + name + " exists in different location: " + existingLocation);
          return false;
        }
      }
      else
      {
        monitor.setTaskName("Importing project " + name);

        Path locationPath = new Path(location.getAbsolutePath());
        if (Platform.getLocation().isPrefixOf(locationPath))
        {
          locationPath = null;
        }

        IProjectDescription projectDescription = workspace.newProjectDescription(name);
        projectDescription.setLocation(locationPath);

        project.create(projectDescription, monitor);
      }

      if (!project.isOpen())
      {
        project.open(monitor);
      }
    }

    return true;
  }

  public static Map<IProject, File> collectProjects(File folder, EList<Predicate> predicates, boolean locatedNestedProjects, IProgressMonitor monitor)
  {
    Map<IProject, File> results = new HashMap<IProject, File>();
    collectProjects(folder, predicates, locatedNestedProjects, results, monitor);
    return results;
  }

  private static void collectProjects(File folder, EList<Predicate> predicates, boolean locateNestedProjects, Map<IProject, File> results,
      IProgressMonitor monitor)
  {
    if (monitor != null && monitor.isCanceled())
    {
      throw new OperationCanceledException();
    }

    IProject project = ResourcesFactory.eINSTANCE.loadProject(folder);
    if (matchesPredicates(project, predicates))
    {
      results.put(project, folder);

      if (!locateNestedProjects)
      {
        return;
      }
    }

    File[] listFiles = folder.listFiles();
    if (listFiles != null)
    {
      for (int i = 0; i < listFiles.length; i++)
      {
        File file = listFiles[i];
        if (file.isDirectory())
        {
          collectProjects(file, predicates, locateNestedProjects, results, monitor);
        }
      }
    }
  }

  public static boolean matchesPredicates(IProject project, EList<Predicate> predicates)
  {
    if (project == null)
    {
      return false;
    }

    if (predicates == null || predicates.isEmpty())
    {
      return true;
    }

    for (Predicate predicate : predicates)
    {
      if (predicate.matches(project))
      {
        return true;
      }
    }

    return false;
  }

  public static void clearWorkspace() throws CoreException
  {
    final IWorkspace workspace = getWorkspace();
    workspace.run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        IWorkspaceRoot root = workspace.getRoot();
        for (IProject project : root.getProjects())
        {
          project.delete(true, null);
        }

        for (File file : root.getLocation().toFile().listFiles())
        {
          if (file.isDirectory() && !".metadata".equals(file.getName()))
          {
            IOUtil.deleteBestEffort(file);
          }
        }
      }
    }, null);
  }

  public static IMarker[] buildWorkspace(boolean clean, String markerType) throws CoreException
  {
    IWorkspace workspace = getWorkspace();

    if (clean)
    {
      workspace.build(IncrementalProjectBuilder.CLEAN_BUILD, null);
      workspace.build(IncrementalProjectBuilder.FULL_BUILD, null);
    }
    else
    {
      workspace.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
    }

    if (markerType == null)
    {
      return null;
    }

    return workspace.getRoot().findMarkers(markerType, false, IResource.DEPTH_INFINITE);
  }
}

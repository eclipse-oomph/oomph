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
package org.eclipse.oomph.resources;

import org.eclipse.oomph.internal.resources.ExternalResource;
import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesUtil;
import org.eclipse.oomph.resources.backend.BackendResource;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.SubMonitor;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
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

  public static BackendResource getBackendResource(IResource resource)
  {
    try
    {
      Object sessionProperty = resource.getSessionProperty(ExternalResource.BACKEND_RESOURCE_PROPERTY_NAME);
      if (sessionProperty instanceof BackendResource)
      {
        return (BackendResource)sessionProperty;
      }
    }
    catch (CoreException ex)
    {
      ResourcesPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  public static boolean isLocal(IResource resource)
  {
    BackendResource backendResource = getBackendResource(resource);
    if (backendResource != null)
    {
      return backendResource.isLocal();
    }

    URI locationURI = resource.getLocationURI();
    if (locationURI != null)
    {
      String scheme = locationURI.getScheme();
      return "file".equalsIgnoreCase(scheme);
    }

    return false;
  }

  public static void runWithFiles(IProject project, IPath path, org.eclipse.oomph.util.Predicate<IFile> filter, RunnableWithFile runnable) throws Exception
  {
    IFolder iFolder = project.getFolder(path);
    if (iFolder.exists())
    {
      for (IResource iResource : iFolder.members())
      {
        if (iResource.exists() && iResource instanceof IFile)
        {
          if (filter.apply((IFile)iResource))
          {
            runWithFile(project, iResource.getProjectRelativePath(), runnable);
          }
        }
      }
    }
  }

  public static void runWithFile(IProject project, IPath path, RunnableWithFile runnable) throws Exception
  {
    IFile iFile = project.getFile(path);
    if (iFile.exists())
    {
      boolean local = ResourcesUtil.isLocal(iFile);
      File projectFolder = null;

      try
      {
        File file;
        if (!local)
        {
          projectFolder = File.createTempFile("local-", "");
          projectFolder.delete();

          file = new File(projectFolder, path.toOSString());
          IOUtil.mkdirs(file.getParentFile());

          copyFile(iFile, file);
        }
        else
        {
          projectFolder = new File(project.getLocation().toOSString());
          file = new File(iFile.getLocation().toOSString());
        }

        runnable.run(projectFolder, file);
      }
      finally
      {
        if (!local)
        {
          IOUtil.deleteBestEffort(projectFolder);
        }
      }
    }
  }

  public static void copyFile(IFile source, File target) throws CoreException, FileNotFoundException
  {
    InputStream inputStream = null;
    OutputStream outputStream = null;

    try
    {
      inputStream = source.getContents();
      outputStream = new FileOutputStream(target);

      IOUtil.copy(inputStream, outputStream);
    }
    finally
    {
      IOUtil.closeSilent(outputStream);
      IOUtil.closeSilent(inputStream);
    }
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
            if (importProject(folder, progress.newChild()) == ImportResult.IMPORTED)
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

  public static ImportResult importProject(File projectLocation, IProgressMonitor monitor) throws Exception
  {
    String projectName = getProjectName(projectLocation);
    if (projectName == null || projectName.length() == 0)
    {
      ResourcesPlugin.INSTANCE.log("No project name for folder " + projectLocation);
      return ImportResult.ERROR;
    }

    return importProject(projectLocation, projectName, monitor);
  }

  public static ImportResult importProject(File projectLocation, String projectName, IProgressMonitor monitor) throws IOException, CoreException
  {
    File location = projectLocation.getCanonicalFile();

    IWorkspace workspace = getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    IProject project = root.getProject(projectName);
    if (project.exists())
    {
      File existingLocation = new File(project.getLocation().toOSString()).getCanonicalFile();
      if (!existingLocation.equals(location))
      {
        ResourcesPlugin.INSTANCE.log("Project " + projectName + " exists in different location: " + existingLocation);
        return ImportResult.EXISTED_DIFFERENT_LOCATION;
      }

      return ImportResult.EXISTED;
    }

    monitor.setTaskName("Importing project " + projectName);

    IPath locationPath = new Path(location.getAbsolutePath());
    IPath parentPath = locationPath.removeLastSegments(1);
    IPath defaultDefaultLocation = root.getLocation();
    if (isPrefixOf(parentPath, defaultDefaultLocation) && isPrefixOf(defaultDefaultLocation, parentPath))
    {
      locationPath = null;
    }

    IProjectDescription projectDescription = workspace.newProjectDescription(projectName);
    projectDescription.setLocation(locationPath);

    project.create(projectDescription, monitor);

    if (!project.isOpen())
    {
      project.open(monitor);
    }

    return ImportResult.IMPORTED;
  }

  @SuppressWarnings("restriction")
  private static boolean isPrefixOf(IPath location1, IPath location2)
  {
    return org.eclipse.core.internal.utils.FileUtil.isPrefixOf(location1, location2);
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

  /**
   * @deprecated Use {@link PredicatesUtil#matchesPredicates(IResource, EList)}.
   */
  @Deprecated
  public static boolean matchesPredicates(IProject project, EList<Predicate> predicates)
  {
    return PredicatesUtil.matchesPredicates(project, predicates);
  }

  /**
   * @author Eike Stepper
   */
  public interface RunnableWithFile
  {
    public void run(File folder, File file) throws Exception;
  }

  /**
   * @author Eike Stepper
   */
  public static enum ImportResult
  {
    EXISTED, EXISTED_DIFFERENT_LOCATION, IMPORTED, ERROR
  }
}

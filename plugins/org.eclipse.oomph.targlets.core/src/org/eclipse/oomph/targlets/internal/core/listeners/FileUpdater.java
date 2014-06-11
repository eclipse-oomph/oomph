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
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;

import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Eike Stepper
 */
public abstract class FileUpdater
{
  public FileUpdater()
  {
  }

  public boolean update(File file) throws Exception
  {
    IFile iFile = getIFile(file);
    String nl = getLineSeparator(iFile);

    String oldContents = getContents(file, iFile);
    String newContents = createNewContents(oldContents, nl);
    if (newContents != null && !newContents.equals(oldContents))
    {
      setContents(file, iFile, newContents);
      return true;
    }

    return false;
  }

  protected abstract String createNewContents(String oldContents, String nl);

  private static IFile getIFile(File file)
  {
    for (IFile iFile : ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(file.toURI()))
    {
      IProject project = iFile.getProject();
      if (project.isOpen())
      {
        return iFile;
      }
    }

    return null;
  }

  private static String getLineSeparator(IFile iFile)
  {
    try
    {
      if (iFile != null)
      {
        String projectName = iFile.getProject().getName();

        Preferences node = Platform.getPreferencesService().getRootNode().node(ProjectScope.SCOPE).node(projectName);
        if (node.nodeExists(Platform.PI_RUNTIME))
        {
          String value = node.node(Platform.PI_RUNTIME).get(Platform.PREF_LINE_SEPARATOR, null);
          if (value != null)
          {
            return value;
          }
        }
      }
    }
    catch (Exception e)
    {
      // Ignore
    }

    return PropertiesUtil.getProperty(Platform.PREF_LINE_SEPARATOR);
  }

  private static String getContents(File file, IFile iFile) throws Exception
  {
    InputStream inputStream = iFile == null ? new FileInputStream(file) : iFile.getContents();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try
    {
      IOUtil.copy(inputStream, outputStream);
    }
    finally
    {
      IOUtil.close(inputStream);
    }

    return new String(outputStream.toByteArray(), "UTF-8");
  }

  private static void setContents(File file, IFile iFile, String contents) throws Exception
  {
    InputStream inputStream = new ByteArrayInputStream(contents.getBytes("UTF-8"));
    if (iFile != null)
    {
      iFile.setContents(inputStream, true, true, new NullProgressMonitor());
    }
    else
    {
      OutputStream outputStream = new FileOutputStream(file);

      try
      {
        IOUtil.copy(inputStream, outputStream);
      }
      finally
      {
        IOUtil.close(outputStream);
      }
    }
  }
}

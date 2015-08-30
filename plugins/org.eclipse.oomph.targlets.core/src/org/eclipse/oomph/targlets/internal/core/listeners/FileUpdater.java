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
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.PlatformContentHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLContentHandlerImpl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class FileUpdater
{
  private static final URIConverter URI_CONVERTER = new ExtensibleURIConverterImpl(URIHandler.DEFAULT_HANDLERS,
      Arrays.asList(new ContentHandler[] { new PlatformContentHandlerImpl(), new XMLContentHandlerImpl() }));

  public FileUpdater()
  {
  }

  private URI getURI(File file)
  {
    for (IFile iFile : ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(file.toURI()))
    {
      IProject project = iFile.getProject();
      if (project.isOpen())
      {
        return URI.createPlatformResourceURI(iFile.getFullPath().toString(), true);
      }
    }

    return URI.createFileURI(file.toString());
  }

  protected Map<String, ?> getDescription(URI uri) throws IOException
  {
    return URI_CONVERTER.contentDescription(uri, null);
  }

  public boolean update(File file) throws Exception
  {
    URI uri = getURI(file);
    Map<String, ?> description = getDescription(uri);
    String nl = (String)description.get(ContentHandler.LINE_DELIMITER_PROPERTY);
    if (nl == null)
    {
      nl = System.getProperty("line.separator");
    }

    String encoding = (String)description.get(ContentHandler.CHARSET_PROPERTY);
    if (encoding == null)
    {
      encoding = "UTF-8";
    }

    String oldContents = URI_CONVERTER.exists(uri, null) ? getContents(uri, encoding) : null;
    String newContents = createNewContents(oldContents, encoding, nl);
    if (newContents != null && !newContents.equals(oldContents))
    {
      setContents(uri, encoding, newContents);
      return true;
    }

    return false;
  }

  protected abstract String createNewContents(String oldContents, String encoding, String nl);

  protected String getContents(URI uri, String encoding) throws IOException
  {
    InputStream inputStream = URI_CONVERTER.createInputStream(uri);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try
    {
      IOUtil.copy(inputStream, outputStream);
    }
    finally
    {
      IOUtil.close(inputStream);
    }

    return new String(outputStream.toByteArray(), encoding);
  }

  protected void setContents(URI uri, String encoding, String contents) throws IOException
  {
    InputStream inputStream = new ByteArrayInputStream(contents.getBytes(encoding));
    OutputStream outputStream = URI_CONVERTER.createOutputStream(uri);

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

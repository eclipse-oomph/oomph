/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.impl;

import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.util.ProjectConfigUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import org.osgi.service.prefs.BackingStoreException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ProjectConfigURIHandlerImpl extends URIHandlerImpl
{
  @Override
  public boolean canHandle(URI uri)
  {
    return ProjectConfigUtil.PROJECT_CONFIG_SCHEME.equals(uri.scheme());
  }

  @Override
  public InputStream createInputStream(URI uri, final Map<?, ?> options) throws IOException
  {
    if (uri.segmentCount() == 1)
    {
      class ProjectConfigInput extends InputStream implements URIConverter.Loadable
      {
        private InputStream in;

        public void loadResource(Resource resource) throws IOException
        {
          resource.getContents().addAll(ProjectConfigUtil.getWorkspaceConfiguration().eResource().getContents());
        }

        @Override
        public int read() throws IOException
        {
          if (in == null)
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ProjectConfigUtil.getWorkspaceConfiguration().eResource().save(out, null);
            in = new ByteArrayInputStream(out.toByteArray());
          }
          return in.read();
        }
      }

      return new ProjectConfigInput();
    }
    throw new IOException("No preference value available for ");
  }

  @Override
  public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
  {
    if (uri.segmentCount() == 1)
    {
      class ProjectConfigOutput extends OutputStream implements URIConverter.Saveable
      {
        public void saveResource(Resource resource) throws IOException
        {
          try
          {
            ProjectConfigUtil.saveWorkspaceConfiguration((WorkspaceConfiguration)resource.getContents().get(0));
          }
          catch (BackingStoreException ex)
          {
            throw new IOExceptionWithCause(ex);
          }
        }

        @Override
        public void write(int b) throws IOException
        {
          throw new IOException("Write not supported");
        }
      }

      return new ProjectConfigOutput();
    }

    throw new IOException("Output not supported");
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    throw new IOException("Delete not supported");
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    // TODO
    return false;
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    return Collections.emptyMap();
  }

  @Override
  public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
  {
    // Do nothing.
  }
}

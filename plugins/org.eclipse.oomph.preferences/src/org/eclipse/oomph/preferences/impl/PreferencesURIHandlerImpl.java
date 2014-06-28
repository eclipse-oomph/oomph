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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class PreferencesURIHandlerImpl extends URIHandlerImpl
{
  private static final IEclipsePreferences ROOT = Platform.getPreferencesService().getRootNode();

  @Override
  public boolean canHandle(URI uri)
  {
    return "preference".equals(uri.scheme());
  }

  protected static class PreferenceAccessor
  {
    private final Preferences preferences;

    private final String key;

    public PreferenceAccessor(URI uri)
    {
      Preferences node = ROOT;
      for (String name : uri.trimSegments(1).segments())
      {
        node = node.node(name);
      }
      key = uri.lastSegment();
      preferences = node;
    }

    public String get()
    {
      return preferences.get(key, null);

    }

    public void put(String value) throws IOException
    {
      preferences.put(key, value);
      flush();
    }

    public void remove() throws IOException
    {
      preferences.remove(key);
      flush();
    }

    private void flush() throws IOException
    {
      try
      {
        preferences.flush();
      }
      catch (BackingStoreException ex)
      {
        throw new IOExceptionWithCause(ex);
      }
    }
  }

  @Override
  public InputStream createInputStream(URI uri, final Map<?, ?> options) throws IOException
  {
    if (uri.segmentCount() == 1)
    {
      class PreferencesInput extends InputStream implements URIConverter.Loadable
      {
        private PreferenceNode preferencesNode = PreferencesUtil.getRootPreferenceNode(Boolean.TRUE.equals(options
            .get(PreferencesUtil.OPTION_SYNCHRONIZED_PREFERENCES)));

        private InputStream in;

        public void loadResource(Resource resource) throws IOException
        {
          resource.getContents().add(preferencesNode);
        }

        @Override
        public int read() throws IOException
        {
          if (in == null)
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            preferencesNode.eResource().save(out, null);
            in = new ByteArrayInputStream(out.toByteArray());
          }
          return in.read();
        }
      }

      return new PreferencesInput();
    }
    URI preferencePath = uri.trimSegments(1);
    String value = new PreferenceAccessor(preferencePath).get();
    if (value == null)
    {
      throw new IOException("No preference value available for " + preferencePath);
    }
    return new URIConverter.ReadableInputStream(value);
  }

  @Override
  public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
  {
    if (uri.segmentCount() == 1)
    {
      class PreferencesOutput extends OutputStream implements URIConverter.Saveable
      {
        public void saveResource(Resource resource) throws IOException
        {
          PreferenceNode root = EcoreUtil.copy((PreferenceNode)resource.getContents().get(0));
          Throwable throwable = null;
          try
          {
            Collection<? extends IEclipsePreferences> flush = PreferencesUtil.reconcile(root);
            for (IEclipsePreferences preferences : flush)
            {
              try
              {
                preferences.flush();
              }
              catch (Throwable ex)
              {
                throwable = ex;
              }
            }
          }
          catch (Throwable ex)
          {
            IOException ioException = new IOException();
            ioException.initCause(ex);
            throw ioException;
          }

          if (throwable != null)
          {
            IOException ioException = new IOException();
            ioException.initCause(throwable);
            throw ioException;
          }
        }

        @Override
        public void write(int b) throws IOException
        {
          throw new IOException("Streamed output not supported");
        }
      }

      return new PreferencesOutput();
    }

    final PreferenceAccessor accessor = new PreferenceAccessor(uri.trimSegments(1));
    return new ByteArrayOutputStream()
    {
      @Override
      public void close() throws IOException
      {
        accessor.put(new String(toByteArray(), "UTF-8"));
      }
    };
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    new PreferenceAccessor(uri.trimSegments(1)).remove();
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    return new PreferenceAccessor(uri.trimSegments(1)).get() != null;
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

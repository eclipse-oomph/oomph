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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.osgi.util.NLS;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class PreferencesURIHandlerImpl extends URIHandlerImpl
{
  private static final IEclipsePreferences ROOT = Platform.getPreferencesService().getRootNode();

  @Override
  public boolean canHandle(URI uri)
  {
    return "preference".equals(uri.scheme()); //$NON-NLS-1$
  }

  protected static class PreferenceAccessor
  {
    private final Preferences preferences;

    private final ISecurePreferences securePreferences;

    private final String key;

    private final boolean isEncrypted;

    public PreferenceAccessor(URI uri)
    {
      String[] segments = uri.segments();
      int last = segments.length - 1;

      if ("secure".equals(segments[0])) //$NON-NLS-1$
      {
        ISecurePreferences node = PreferencesUtil.getSecurePreferences();
        for (int i = 1; i < last; ++i)
        {
          node = node.node(segments[i]);
        }

        preferences = null;
        securePreferences = node;

        isEncrypted = !"encrypted=false".equals(uri.query()); //$NON-NLS-1$
      }
      else
      {
        Preferences node = ROOT;
        for (int i = 0; i < last; ++i)
        {
          node = node.node(segments[i]);
        }
        preferences = node;
        securePreferences = null;
        isEncrypted = false;
      }

      key = segments[last];
    }

    public String get() throws IOException
    {
      try
      {
        return securePreferences == null ? preferences.get(key, null) : securePreferences.get(key, null);
      }
      catch (StorageException ex)
      {
        throw new IOExceptionWithCause(ex);
      }
    }

    public void put(String value) throws IOException
    {
      if (securePreferences == null)
      {
        preferences.put(key, value);
      }
      else
      {
        try
        {
          boolean encrypt = isEncrypted;
          for (String key : securePreferences.keys())
          {
            if (key.equals(this.key))
            {
              encrypt = securePreferences.isEncrypted(key);
              break;
            }
          }

          securePreferences.put(key, value, encrypt);
        }
        catch (StorageException ex)
        {
          throw new IOExceptionWithCause(ex);
        }
      }

      flush();
    }

    public void remove() throws IOException
    {
      if (securePreferences == null)
      {
        preferences.remove(key);
      }
      else
      {
        securePreferences.remove(key);
      }

      flush();
    }

    private void flush() throws IOException
    {
      try
      {
        if (securePreferences == null)
        {
          preferences.flush();
        }
        else
        {
          securePreferences.flush();
        }
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
        private PreferenceNode preferencesNode = PreferencesUtil
            .getRootPreferenceNode(Boolean.TRUE.equals(options.get(PreferencesUtil.OPTION_SYNCHRONIZED_PREFERENCES)));

        private InputStream in;

        @Override
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
      throw new IOException("No preference value available for " + preferencePath); //$NON-NLS-1$
    }
    return new URIConverter.ReadableInputStream(value);
  }

  @Override
  public OutputStream createOutputStream(final URI uri, Map<?, ?> options) throws IOException
  {
    if (uri.segmentCount() == 1)
    {
      class PreferencesOutput extends OutputStream implements URIConverter.Saveable
      {
        @Override
        public void saveResource(Resource resource) throws IOException
        {
          Copier copier = new Copier()
          {
            private static final long serialVersionUID = 1L;

            @Override
            protected void copyAttribute(EAttribute eAttribute, EObject eObject, EObject copyEObject)
            {
              if (eAttribute == PreferencesPackage.Literals.PROPERTY__VALUE)
              {
                Property property = (Property)eObject;
                Property copyProperty = (Property)copyEObject;
                copyProperty.setValue(property.getSecureValue());
              }
              else
              {
                super.copyAttribute(eAttribute, eObject, copyEObject);
              }
            }
          };
          PreferenceNode root = (PreferenceNode)copier.copy(resource.getContents().get(0));
          copier.copyReferences();

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
            IOException ioException = new IOException(ex.getMessage());
            ioException.initCause(ex);
            throw ioException;
          }

          if (throwable != null)
          {
            IOException ioException = new IOException(throwable.getMessage());
            ioException.initCause(throwable);
            throw ioException;
          }
        }

        @Override
        public void write(int b) throws IOException
        {
          throw new IOException(NLS.bind(Messages.PreferencesURIHandlerImpl_UnsupportedStreaming_exception, uri));
        }
      }

      return new PreferencesOutput();
    }

    final PreferenceAccessor accessor = new PreferenceAccessor(uri.trimSegments(1));
    return new URIConverter.WriteableOutputStream(new StringWriter()
    {
      @Override
      public void close() throws IOException
      {
        super.close();
        accessor.put(toString());
      }
    }, "UTF-8"); //$NON-NLS-1$
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    new PreferenceAccessor(uri.trimSegments(1)).remove();
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    try
    {
      return new PreferenceAccessor(uri.trimSegments(1)).get() != null;
    }
    catch (IOException ex)
    {
      return false;
    }
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

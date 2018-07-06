/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util;

import org.eclipse.emf.common.util.URI;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class Request extends HashMap<String, String>
{
  private static final URI DEFAULT_URI = URI.createURI("about:");

  private static final long serialVersionUID = 1L;

  private transient URI uri;

  public Request()
  {
  }

  public Request(String uri)
  {
    this(uri == null ? null : URI.createURI(uri));
  }

  public Request(URI uri)
  {
    setURI(uri);
  }

  public Request(Request source)
  {
    this(source.getURI());
  }

  public URI getURI()
  {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, String> entry : entrySet())
    {
      if (builder.length() != 0)
      {
        builder.append("&");
      }

      builder.append(entry.getKey());
      String value = entry.getValue();
      if (value != null)
      {
        builder.append("=");
        builder.append(URI.encodeQuery(value, false));
      }
    }

    return uri.appendQuery(builder.toString());
  }

  public void setURI(URI uri)
  {
    if (uri == null)
    {
      uri = DEFAULT_URI;
    }

    clear();

    String query = uri.query();
    if (query != null)
    {
      String[] parameters = query.split("&");
      for (String parameter : parameters)
      {
        int pos = parameter.indexOf('=');
        if (pos == -1)
        {
          put(parameter, null);
        }
        else
        {
          String key = parameter.substring(0, pos);
          String value = URI.decode(parameter.substring(pos + 1));
          put(key, value);
        }
      }

      uri = uri.trimQuery();
    }

    this.uri = uri;
  }

  public Request copy()
  {
    return new Request(this);
  }

  @Override
  public int hashCode()
  {
    return getURI().hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    Request other = (Request)obj;
    return getURI().equals(other.getURI());
  }

  @Override
  public String toString()
  {
    return getURI().toString();
  }

  /**
   * @author Eike Stepper
   */
  public interface Handler
  {
    public static final Handler SYSTEM_BROWSER = new Handler()
    {
      public boolean handleRequest(Request request)
      {
        return OS.INSTANCE.openSystemBrowser(request.getURI().toString());
      }
    };

    public boolean handleRequest(Request request);

    /**
     * @author Eike Stepper
     */
    public static abstract class Modifier implements Handler
    {
      private final Handler delegate;

      public Modifier(Handler delegate)
      {
        this.delegate = delegate;
      }

      public boolean handleRequest(Request request)
      {
        request = request.copy();
        modify(request);

        return delegate.handleRequest(request);
      }

      protected abstract void modify(Request request);
    }
  }
}

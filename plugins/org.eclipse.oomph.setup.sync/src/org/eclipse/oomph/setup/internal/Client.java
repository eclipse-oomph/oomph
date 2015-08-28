/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal;

import org.eclipse.oomph.setup.sync.SyncState.ConflictException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.Credentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public class Client
{
  private static final String USER_AGENT_ID = PropertiesUtil.getProperty("oomph.setup.sync.user_agent_id", "oomph/sync");

  private static final boolean DEBUG = PropertiesUtil.isProperty("oomph.setup.sync.debug");

  private static final String HEADER_LAST_MODIFIED = "X-Last-Modified";

  private static final int STATUS_OK = 200;

  private static final int STATUS_NOT_MODIFIED = 304;

  private static final int STATUS_BAD_REQUEST = 400;

  private static final int STATUS_FORBIDDEN = 403;

  private static final int STATUS_CONFLICT = 409;

  private final URI uri;

  private final Executor executor;

  public Client(URI serviceURI, Credentials credentials)
  {
    uri = serviceURI;
    executor = Executor.newInstance().auth(credentials);
  }

  public long get(long timeStamp, File file) throws IOException
  {
    HttpEntity responseEntity = null;

    try
    {
      Request request = configureRequest(timeStamp, Request.Get(uri));

      HttpResponse response = sendRequest(request);
      responseEntity = response.getEntity();

      int status = getStatus(response);
      if (status == STATUS_NOT_MODIFIED)
      {
        EntityUtils.consume(responseEntity);
        return 0;
      }

      if (status == STATUS_OK)
      {
        saveContent(responseEntity, file);
        return getLastModified(response);
      }

      throw new BadResponseException(uri);
    }
    catch (IOException ex)
    {
      if (DEBUG && responseEntity != null)
      {
        responseEntity.writeTo(System.out);
      }

      throw ex;
    }
  }

  public void post(long timeStamp, File file) throws IOException, ConflictException
  {
    HttpEntity responseEntity = null;

    try
    {
      // @SuppressWarnings("deprecation")
      // HttpEntity requestEntity = MultipartEntityBuilder.create().addPart("userfile", new FileBody(file, "text/xml", "UTF-8")).build();

      HttpEntity requestEntity = MultipartEntityBuilder.create().addPart("userfile", new FileBody(file)).build();
      Request request = configureRequest(timeStamp, Request.Post(uri)).body(requestEntity);

      // Request request = configureRequest(timeStamp, Request.Post(uri)).addHeader("Content-Type", "text/xml").body(requestEntity);

      HttpResponse response = sendRequest(request);
      responseEntity = response.getEntity();
      int status = getStatus(response);

      if (status == STATUS_CONFLICT)
      {
        saveContent(responseEntity, file);

        long lastModified = getLastModified(response);
        file.setLastModified(lastModified);
        throw new ConflictException(uri);
      }

      if (status == STATUS_OK)
      {
        long lastModified = getLastModified(response);
        file.setLastModified(lastModified);
        return;
      }

      throw new BadResponseException(uri);
    }
    catch (IOException ex)
    {
      if (DEBUG && responseEntity != null)
      {
        responseEntity.writeTo(System.out);
      }

      throw ex;
    }
  }

  private Request configureRequest(long timeStamp, Request request)
  {
    return request //
        .viaProxy(ProxyUtil.getProxyHost(uri)) //
        .connectTimeout(3000) //
        .staleConnectionCheck(true) //
        .socketTimeout(10000) //
        .addHeader(HEADER_LAST_MODIFIED, Long.toString(timeStamp)) //
        .addHeader("User-Agent", USER_AGENT_ID);
  }

  private HttpResponse sendRequest(Request request) throws IOException
  {
    Response result = ProxyUtil.proxyAuthentication(executor, uri).execute(request);
    HttpResponse response = result.returnResponse();

    if (DEBUG)
    {
      System.out.println(response.getStatusLine());
      for (Header header : response.getAllHeaders())
      {
        System.out.println("   " + header);
      }
    }

    return response;
  }

  private int getStatus(HttpResponse response) throws IOException
  {
    StatusLine statusLine = response.getStatusLine();
    if (statusLine == null)
    {
      throw new BadResponseException(uri);
    }

    int status = statusLine.getStatusCode();
    if (status == STATUS_BAD_REQUEST)
    {
      throw new BadRequestException(uri);
    }

    if (status == STATUS_FORBIDDEN)
    {
      throw new ForbiddenException(uri);
    }

    return status;
  }

  private long getLastModified(HttpResponse response) throws IOException
  {
    Header[] headers = response.getHeaders(HEADER_LAST_MODIFIED);
    if (headers == null || headers.length == 0)
    {
      throw new BadResponseException(uri);
    }

    try
    {
      String header = headers[0].getValue();
      return Long.parseLong(header);
    }
    catch (Exception ex)
    {
      throw new BadResponseException(uri);
    }
  }

  private static void saveContent(HttpEntity entity, File file) throws IOException
  {
    InputStream content = null;
    OutputStream out = null;

    try
    {
      content = entity.getContent();
      out = new BufferedOutputStream(new FileOutputStream(file));

      IOUtil.copy(content, out);
    }
    finally
    {
      IOUtil.closeSilent(out);
      IOUtil.closeSilent(content);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class BadRequestException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public BadRequestException(URI uri)
    {
      super("Bad request: " + uri);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ForbiddenException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(URI uri)
    {
      super("Forbidden: " + uri);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class BadResponseException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public BadResponseException(URI uri)
    {
      super("Bad Response: " + uri);
    }
  }
}

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
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

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
public class RemoteDataProvider implements DataProvider
{
  private static final String USER_AGENT_ID = PropertiesUtil.getProperty("oomph.setup.sync.user_agent_id", "oomph/sync");

  private static final boolean DEBUG = PropertiesUtil.isProperty("oomph.setup.sync.debug");

  private static final String BASE_VERSION_HEADER = "X-Base-Version";

  private static final String VERSION_HEADER = "X-Version";

  private static final int OK = 200;

  private static final int NOT_MODIFIED = 304;

  private static final int BAD_REQUEST = 400;

  private static final int AUTHORIZATION_REQUIRED = 401;

  private static final int FORBIDDEN = 403;

  private static final int NOT_FOUND = 404;

  private static final int CONFLICT = 409;

  private final URI uri;

  private final Executor executor;

  public RemoteDataProvider(URI serviceURI, Executor executor)
  {
    uri = serviceURI;
    this.executor = executor;
  }

  public RemoteDataProvider(URI serviceURI, Credentials credentials)
  {
    this(serviceURI, Executor.newInstance().auth(credentials));
  }

  public Location getLocation()
  {
    return Location.REMOTE;
  }

  public URI getURI()
  {
    return uri;
  }

  public boolean update(File file) throws IOException, NotFoundException
  {
    HttpEntity responseEntity = null;

    try
    {
      String baseVersion = SyncUtil.getDigest(file);
      Request request = configureRequest(Request.Get(uri), baseVersion);

      HttpResponse response = sendRequest(request);
      responseEntity = response.getEntity();

      int status = getStatus(response);
      if (status == NOT_FOUND)
      {
        throw new NotFoundException(uri);
      }

      if (status == NOT_MODIFIED)
      {
        return false;
      }

      if (status == OK)
      {
        saveContent(responseEntity, file);
        return true;
      }

      throw BadResponseException.create(uri, response.getStatusLine());
    }
    catch (NotFoundException ex)
    {
      throw ex;
    }
    catch (IOException ex)
    {
      if (DEBUG && responseEntity != null)
      {
        responseEntity.writeTo(System.out);
      }

      throw ex;
    }
    finally
    {
      if (responseEntity != null)
      {
        EntityUtils.consume(responseEntity);
      }
    }
  }

  public void post(File file, String baseVersion) throws IOException, NotCurrentException
  {
    HttpEntity responseEntity = null;

    try
    {
      String version = SyncUtil.getDigest(file);

      HttpEntity requestEntity = MultipartEntityBuilder.create().addPart("userfile", new FileBody(file)).build();
      Request request = configureRequest(Request.Post(uri), baseVersion).addHeader(VERSION_HEADER, version).body(requestEntity);

      HttpResponse response = sendRequest(request);
      responseEntity = response.getEntity();
      int status = getStatus(response);

      if (status == CONFLICT)
      {
        saveContent(responseEntity, file);
        throw new NotCurrentException(uri);
      }

      if (status == OK)
      {
        return;
      }

      throw BadResponseException.create(uri, response.getStatusLine());
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

  public boolean delete() throws IOException
  {
    Request request = configureRequest(Request.Delete(uri), null);
    HttpResponse response = sendRequest(request);

    int status = getStatus(response);
    if (status == NOT_FOUND)
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getURI() + "]";
  }

  private Request configureRequest(Request request, String baseVersion)
  {
    return request //
        .viaProxy(SyncUtil.getProxyHost(uri)) //
        .connectTimeout(3000) //
        .staleConnectionCheck(true) //
        .socketTimeout(10000) //
        .addHeader(BASE_VERSION_HEADER, StringUtil.safe(baseVersion)) //
        .addHeader("User-Agent", USER_AGENT_ID);
  }

  private HttpResponse sendRequest(Request request) throws IOException
  {
    Response result = SyncUtil.proxyAuthentication(executor, uri).execute(request);
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
      throw new BadResponseException(uri, "No status returned");
    }

    int status = statusLine.getStatusCode();
    if (status == BAD_REQUEST)
    {
      throw new BadRequestException(uri);
    }

    if (status == FORBIDDEN)
    {
      throw new ForbiddenException(uri);
    }

    if (status == AUTHORIZATION_REQUIRED)
    {
      throw new AuthorizationRequiredException(uri);
    }

    return status;
  }

  private static void saveContent(HttpEntity entity, File file) throws IOException
  {
    file.getParentFile().mkdirs();

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
  public static class AuthorizationRequiredException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public AuthorizationRequiredException(URI uri)
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

    public BadResponseException(URI uri, String statusLine)
    {
      super("Bad response: " + uri + (StringUtil.isEmpty(statusLine) ? "" : " --> " + statusLine));
    }

    private static BadResponseException create(URI uri, StatusLine statusLine)
    {
      return new BadResponseException(uri, statusLine == null ? null : statusLine.toString());
    }
  }
}

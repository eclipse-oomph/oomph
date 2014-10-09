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
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.filetransfer.IFileTransferListener;
import org.eclipse.ecf.filetransfer.IRetrieveFileTransferContainerAdapter;
import org.eclipse.ecf.filetransfer.IRetrieveFileTransferOptions;
import org.eclipse.ecf.filetransfer.IncomingFileTransferException;
import org.eclipse.ecf.filetransfer.UserCancelledException;
import org.eclipse.ecf.filetransfer.events.IFileTransferEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveDoneEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveStartEvent;
import org.eclipse.ecf.provider.filetransfer.identity.FileTransferID;
import org.eclipse.ecf.provider.filetransfer.identity.FileTransferNamespace;
import org.eclipse.ecf.provider.filetransfer.util.ProxySetupHelper;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.core.UIServices.AuthenticationInfo;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Eike Stepper
 */
public class ECFURIHandlerImpl extends URIHandlerImpl
{
  public static final String OPTION_CACHE_HANDLING = "OPTION_CACHE_HANDLING";

  public enum CacheHandling
  {
    CACHE_ONLY, CACHE_WITHOUT_ETAG_CHECKING, CACHE_WITH_ETAG_CHECKING, CACHE_IGNORE
  }

  public static final String OPTION_AUTHORIZATION_HANDLER = "OPTION_AUTHORIZATION_HANDLER";

  public static final String OPTION_AUTHORIZATION = "OPTION_AUTHORIZATION";

  public interface AuthorizationHandler
  {
    public final class Authorization
    {
      public static final Authorization UNAUTHORIZED = new Authorization("", "");

      public static final Authorization UNAUTHORIZEABLE = new Authorization("", "");

      private final String user;

      private final String password;

      public Authorization(String user, String password)
      {
        this.user = user == null ? "" : user;
        this.password = obscure(password == null ? "" : password);
      }

      public String getUser()
      {
        return user;
      }

      public String getPassword()
      {
        return unobscure(password);
      }

      public String getAuthorization()
      {
        return "Basic " + obscure(user.length() == 0 ? getPassword() : user + ":" + getPassword());
      }

      public boolean isAuthorized()
      {
        return !"".equals(password);
      }

      public boolean isUnauthorizeable()
      {
        return this == UNAUTHORIZEABLE;
      }

      private String obscure(String string)
      {
        return XMLTypeFactory.eINSTANCE.convertBase64Binary(string.getBytes());
      }

      private String unobscure(String string)
      {
        return new String(XMLTypeFactory.eINSTANCE.createBase64Binary(string));
      }

      @Override
      public int hashCode()
      {
        final int prime = 31;
        int result = 1;
        result = prime * result + (user == null ? 0 : user.hashCode());
        result = prime * result + (password == null ? 0 : password.hashCode());
        return result;
      }

      @Override
      public boolean equals(Object obj)
      {
        if (this == obj)
        {
          return true;
        }

        if (obj == null)
        {
          return false;
        }

        if (getClass() != obj.getClass())
        {
          return false;
        }

        if (this == UNAUTHORIZEABLE)
        {
          return obj == UNAUTHORIZEABLE;
        }

        Authorization other = (Authorization)obj;
        if (!user.equals(other.user))
        {
          return false;
        }

        if (!password.equals(other.password))
        {
          return false;
        }

        return true;
      }

      @Override
      public String toString()
      {
        return this == UNAUTHORIZEABLE ? "Authorization [unauthorizeable]" : "Authorization [user=" + user + ", password=" + password + "]";
      }
    }

    public Authorization authorize(URI uri);

    public Authorization reauthorize(URI uri, Authorization authorization);
  }

  public static class AuthorizationHandlerImpl implements AuthorizationHandler
  {
    private final Map<String, Authorization> authorizations = new HashMap<String, Authorization>();

    private final UIServices uiServices;

    private ISecurePreferences securePreferences;

    public AuthorizationHandlerImpl(UIServices uiServices, ISecurePreferences securePreferences)
    {
      this.uiServices = uiServices;
      this.securePreferences = securePreferences;
    }

    public synchronized void clearCache()
    {
      authorizations.clear();
    }

    public synchronized Authorization authorize(URI uri)
    {
      String host = getHost(uri);
      if (host != null)
      {
        Authorization cachedAuthorization = authorizations.get(host);
        if (cachedAuthorization == Authorization.UNAUTHORIZEABLE)
        {
          return cachedAuthorization;
        }

        if (securePreferences != null)
        {
          try
          {
            ISecurePreferences node = securePreferences.node(host);
            String user = node.get("user", "");
            String password = node.get("password", "");

            Authorization authorization = new Authorization(user, password);
            if (authorization.isAuthorized())
            {
              authorizations.put(host, authorization);
              return authorization;
            }
          }
          catch (StorageException ex)
          {
            SetupCorePlugin.INSTANCE.log(ex);
          }
        }

        if (cachedAuthorization != null)
        {
          return cachedAuthorization;
        }
      }

      return Authorization.UNAUTHORIZED;
    }

    public synchronized Authorization reauthorize(URI uri, Authorization authorization)
    {
      // Double check that another thread hasn't already prompted and updated the secure store or has not already permanently failed to authorize.
      Authorization currentAuthorization = authorize(uri);
      if (!currentAuthorization.equals(authorization) || currentAuthorization == Authorization.UNAUTHORIZEABLE)
      {
        return currentAuthorization;
      }

      if (uiServices != null)
      {
        String host = getHost(uri);
        if (host != null)
        {
          AuthenticationInfo authenticationInfo = uiServices.getUsernamePassword(uri.toString());
          String user = authenticationInfo.getUserName();
          String password = authenticationInfo.getPassword();
          Authorization reauthorization = new Authorization(user, password);
          if (reauthorization.isAuthorized())
          {
            if (authenticationInfo.saveResult() && securePreferences != null)
            {
              try
              {
                ISecurePreferences node = securePreferences.node(host);
                node.put("user", user, false);
                node.put("password", password, true);
                node.flush();
              }
              catch (IOException ex)
              {
                SetupCorePlugin.INSTANCE.log(ex);
              }
              catch (StorageException ex)
              {
                SetupCorePlugin.INSTANCE.log(ex);
              }
            }

            authorizations.put(host, reauthorization);
            return reauthorization;
          }
          else
          {
            authorizations.put(host, Authorization.UNAUTHORIZEABLE);
            return Authorization.UNAUTHORIZEABLE;
          }
        }
      }

      return currentAuthorization;
    }
  }

  private static final URI CACHE_FOLDER = SetupContext.GLOBAL_STATE_LOCATION_URI.appendSegment("cache");

  private static final Map<URI, String> EXPECTED_ETAGS = new HashMap<URI, String>();

  public static int clearExpectedETags()
  {
    int size;
    synchronized (EXPECTED_ETAGS)
    {
      size = EXPECTED_ETAGS.size();
      EXPECTED_ETAGS.clear();
    }

    return size;
  }

  private static String getExpectedETag(URI uri)
  {
    synchronized (EXPECTED_ETAGS)
    {
      return EXPECTED_ETAGS.get(uri);
    }
  }

  private static void setExpectedETag(URI uri, String eTag)
  {
    synchronized (EXPECTED_ETAGS)
    {
      EXPECTED_ETAGS.put(uri, eTag);
    }
  }

  private static CacheHandling getCacheHandling(Map<?, ?> options)
  {
    CacheHandling cacheHandling = (CacheHandling)options.get(OPTION_CACHE_HANDLING);
    if (cacheHandling == null)
    {
      cacheHandling = CacheHandling.CACHE_WITH_ETAG_CHECKING;
    }

    return cacheHandling;
  }

  private static AuthorizationHandler getAuthorizatonHandler(Map<?, ?> options)
  {
    return (AuthorizationHandler)options.get(OPTION_AUTHORIZATION_HANDLER);
  }

  private static Authorization getAuthorizaton(Map<?, ?> options)
  {
    return (Authorization)options.get(OPTION_AUTHORIZATION);
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    if (uri.scheme().startsWith("http"))
    {
      Set<String> requestedAttributes = getRequestedAttributes(options);
      if (requestedAttributes != null && requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY) && requestedAttributes.size() == 1)
      {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
        return result;
      }
    }

    return super.getAttributes(uri, options);
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    return super.exists(uri, options);
  }

  @Override
  public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
  {
    CacheHandling cacheHandling = getCacheHandling(options);
    URIConverter uriConverter = getURIConverter(options);
    URI cacheURI = getCacheFile(uri);
    String eTag = cacheHandling == CacheHandling.CACHE_IGNORE ? null : getETag(uriConverter, cacheURI);
    String expectedETag = cacheHandling == CacheHandling.CACHE_IGNORE ? null : getExpectedETag(uri);
    if (expectedETag != null || cacheHandling == CacheHandling.CACHE_ONLY || cacheHandling == CacheHandling.CACHE_WITHOUT_ETAG_CHECKING)
    {
      if (cacheHandling == CacheHandling.CACHE_ONLY || cacheHandling == CacheHandling.CACHE_WITHOUT_ETAG_CHECKING ? eTag != null : expectedETag.equals(eTag))
      {
        try
        {
          return uriConverter.createInputStream(cacheURI, options);
        }
        catch (IOException ex)
        {
          // Perhaps another JVM is busy writing this file.
          // Proceed as if it doesn't exit.
        }
      }
    }

    String username;
    String password;

    String uriString = uri.toString();
    Proxy proxy = ProxySetupHelper.getProxy(uriString);
    if (proxy != null)
    {
      username = proxy.getUsername();
      password = proxy.getPassword();
    }
    else
    {
      username = null;
      password = null;
    }

    IContainer container = createContainer();

    AuthorizationHandler authorizatonHandler = getAuthorizatonHandler(options);
    Authorization authorization = getAuthorizaton(options);
    int triedReauthorization = 0;
    for (int i = 0;; ++i)
    {
      IRetrieveFileTransferContainerAdapter fileTransfer = (IRetrieveFileTransferContainerAdapter)container
          .getAdapter(IRetrieveFileTransferContainerAdapter.class);

      if (proxy != null)
      {
        fileTransfer.setProxy(proxy);

        if (username != null)
        {
          fileTransfer.setConnectContextForAuthentication(ConnectContextFactory.createUsernamePasswordConnectContext(username, password));
        }
        else if (password != null)
        {
          fileTransfer.setConnectContextForAuthentication(ConnectContextFactory.createPasswordConnectContext(password));
        }
      }

      FileTransferListener transferListener = new FileTransferListener(eTag);

      try
      {
        FileTransferID fileTransferID = new FileTransferID(new FileTransferNamespace(), new java.net.URI(uriString));
        Map<Object, Object> requestOptions = new HashMap<Object, Object>();
        requestOptions.put(IRetrieveFileTransferOptions.CONNECT_TIMEOUT, 10000);
        requestOptions.put(IRetrieveFileTransferOptions.READ_TIMEOUT, 10000);
        if (authorization != null && authorization.isAuthorized())
        {
          requestOptions.put(IRetrieveFileTransferOptions.REQUEST_HEADERS, Collections.singletonMap("Authorization", authorization.getAuthorization()));
        }

        fileTransfer.sendRetrieveRequest(fileTransferID, transferListener, requestOptions);
      }
      catch (URISyntaxException ex)
      {
        throw new IOExceptionWithCause(ex);
      }
      catch (IncomingFileTransferException ex)
      {
        throw new IOExceptionWithCause(ex);
      }

      try
      {
        transferListener.receiveLatch.await();
      }
      catch (InterruptedException ex)
      {
        throw new IOExceptionWithCause(ex);
      }

      if (transferListener.exception != null)
      {
        if (!(transferListener.exception instanceof UserCancelledException))
        {
          if (transferListener.exception.getCause() instanceof SocketTimeoutException && i <= 2)
          {
            continue;
          }

          if (authorizatonHandler != null && transferListener.exception instanceof IncomingFileTransferException)
          {
            IncomingFileTransferException incomingFileTransferException = (IncomingFileTransferException)transferListener.exception;
            if (incomingFileTransferException.getErrorCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
              if (authorization == null)
              {
                authorization = authorizatonHandler.authorize(uri);
                if (authorization.isAuthorized())
                {
                  --i;
                  continue;
                }
              }

              if (!authorization.isUnauthorizeable() && triedReauthorization++ < 3)
              {
                authorization = authorizatonHandler.reauthorize(uri, authorization);
                if (authorization.isAuthorized())
                {
                  --i;
                  continue;
                }
              }
            }
          }
        }

        if (!CacheHandling.CACHE_IGNORE.equals(cacheHandling)
            && uriConverter.exists(cacheURI, options)
            && (!(transferListener.exception instanceof IncomingFileTransferException) || ((IncomingFileTransferException)transferListener.exception)
                .getErrorCode() != HttpURLConnection.HTTP_NOT_FOUND))
        {
          setExpectedETag(uri, transferListener.eTag);
          return uriConverter.createInputStream(cacheURI, options);
        }

        throw new IOExceptionWithCause(transferListener.exception);
      }

      byte[] bytes = transferListener.out.toByteArray();

      try
      {
        BaseUtil.writeFile(uriConverter, options, cacheURI, bytes);
      }
      catch (IORuntimeException ex)
      {
        // Ignore attempts to write out to the cache file.
        // This may collide with another JVM doing exactly the same thing.
        transferListener.eTag = null;
      }
      finally
      {
        setETag(uriConverter, cacheURI, transferListener.eTag);
      }

      setExpectedETag(uri, transferListener.eTag);
      Map<Object, Object> response = getResponse(options);
      if (response != null)
      {
        response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, transferListener.lastModified);
      }

      return new ByteArrayInputStream(bytes);
    }
  }

  private IContainer createContainer() throws IOException
  {
    try
    {
      return ContainerFactory.getDefault().createContainer();
    }
    catch (ContainerCreateException ex)
    {
      throw new IOExceptionWithCause(ex);
    }
  }

  public static URI getCacheFile(URI uri)
  {
    return CACHE_FOLDER.appendSegment(IOUtil.encodeFileName(uri.toString()));
  }

  public static String getETag(URIConverter uriConverter, URI file)
  {
    URI eTagFile = file.appendFileExtension("etag");
    if (uriConverter.exists(eTagFile, null))
    {
      try
      {
        return new String(BaseUtil.readFile(uriConverter, null, eTagFile), "UTF-8");
      }
      catch (IORuntimeException ex)
      {
        // If we can't read the ETag, we'll just return null.
      }
      catch (UnsupportedEncodingException ex)
      {
        // All systems support UTF-8.
      }
    }

    return null;
  }

  public static void setETag(URIConverter uriConverter, URI file, String eTag)
  {
    try
    {
      if (eTag != null)
      {
        BaseUtil.writeFile(uriConverter, null, file.appendFileExtension("etag"), eTag.getBytes("UTF-8"));
      }
      else
      {
        BaseUtil.deleteFile(uriConverter, null, file);
      }
    }
    catch (IORuntimeException ex)
    {
      // If we can't write the ETag, perhaps some other process is writing it, but it's expected to write the same ETag value.
    }
    catch (UnsupportedEncodingException ex)
    {
      // All systems support UTF-8.
    }
  }

  private static String getHost(URI uri)
  {
    String authority = uri.authority();
    if (authority != null)
    {
      int i = authority.indexOf('@');
      int j = authority.indexOf(':', i + 1);
      return j < 0 ? authority.substring(i + 1) : authority.substring(i + 1, j);
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  private static final class FileTransferListener implements IFileTransferListener
  {
    public final CountDownLatch receiveLatch = new CountDownLatch(1);

    public final String expectedETag;

    public String eTag;

    public ByteArrayOutputStream out;

    public long lastModified;

    public Exception exception;

    public FileTransferListener(String expectedETag)
    {
      this.expectedETag = expectedETag;
    }

    public void handleTransferEvent(IFileTransferEvent event)
    {
      if (event instanceof IIncomingFileTransferReceiveStartEvent)
      {
        IIncomingFileTransferReceiveStartEvent receiveStartEvent = (IIncomingFileTransferReceiveStartEvent)event;

        out = new ByteArrayOutputStream();

        @SuppressWarnings("rawtypes")
        Map responseHeaders = receiveStartEvent.getResponseHeaders();
        if (responseHeaders != null)
        {
          eTag = (String)responseHeaders.get("ETag");

          String lastModifiedValue = (String)responseHeaders.get("Last-Modified");
          if (lastModifiedValue != null)
          {
            try
            {
              Date date = DateUtils.parseDate(lastModifiedValue.toString());
              lastModified = date.getTime();
              if (eTag == null)
              {
                eTag = Long.toString(lastModified);
              }
            }
            catch (DateParseException ex)
            {
            }
          }

          if (expectedETag != null && expectedETag.equals(eTag))
          {
            receiveStartEvent.cancel();

            // Older versions of ECF don't produce a IIncomingFileTransferReceiveDoneEvent.
            exception = new UserCancelledException();
            receiveLatch.countDown();
            return;
          }

          boolean setCookie = responseHeaders.get("Set-Cookie") != null;
          if (setCookie)
          {
            IncomingFileTransferException incomingFileTransferException = new IncomingFileTransferException(HttpURLConnection.HTTP_UNAUTHORIZED);
            incomingFileTransferException.fillInStackTrace();
            exception = incomingFileTransferException;
            receiveStartEvent.cancel();

            // Older versions of ECF don't produce a IIncomingFileTransferReceiveDoneEvent.
            // exception = new UserCancelledException();
            receiveLatch.countDown();
            return;
          }
        }

        try
        {
          receiveStartEvent.receive(out);
        }
        catch (IOException ex)
        {
          exception = ex;
        }
      }
      else if (event instanceof IIncomingFileTransferReceiveDoneEvent)
      {
        IIncomingFileTransferReceiveDoneEvent done = (IIncomingFileTransferReceiveDoneEvent)event;
        Exception ex = done.getException();
        if (ex != null && exception == null)
        {
          exception = ex;
        }

        receiveLatch.countDown();
      }
    }
  }
}

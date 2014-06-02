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
package org.eclipse.oomph.internal.setup.core.util;

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

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

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

  private static final URI CACHE_FOLDER = SetupContext.GLOBAL_STATE_LOCATION_URI.appendSegment("cache");

  private static final Map<URI, String> EXPECTED_ETAGS = new HashMap<URI, String>();

  public static void clearExpectedETags()
  {
    synchronized (EXPECTED_ETAGS)
    {
      EXPECTED_ETAGS.clear();
    }
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
    String expectedETag = CacheHandling.CACHE_IGNORE == null ? null : getExpectedETag(uri);
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
        fileTransfer.sendRetrieveRequest(fileTransferID, transferListener, Collections.emptyMap());
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
        if (transferListener.exception instanceof UserCancelledException || !(transferListener.exception.getCause() instanceof SocketTimeoutException) || i > 2)
        {
          if (uriConverter.exists(cacheURI, options))
          {
            return uriConverter.createInputStream(cacheURI, options);
          }

          throw new IOExceptionWithCause(transferListener.exception);
        }

        continue;
      }

      byte[] bytes = transferListener.out.toByteArray();

      try
      {
        EMFUtil.writeFile(uriConverter, options, cacheURI, bytes);
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
        return new String(EMFUtil.readFile(uriConverter, null, eTagFile), "UTF-8");
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
        EMFUtil.writeFile(uriConverter, null, file.appendFileExtension("etag"), eTag.getBytes("UTF-8"));
      }
      else
      {
        EMFUtil.deleteFile(uriConverter, null, file);
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
        if (ex != null)
        {
          exception = ex;
        }

        receiveLatch.countDown();
      }
    }
  }
}

/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.WorkerPool;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.core.util.ProxyAddress;
import org.eclipse.ecf.filetransfer.BrowseFileTransferException;
import org.eclipse.ecf.filetransfer.IFileTransferListener;
import org.eclipse.ecf.filetransfer.IIncomingFileTransfer;
import org.eclipse.ecf.filetransfer.IRemoteFile;
import org.eclipse.ecf.filetransfer.IRemoteFileInfo;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemBrowserContainerAdapter;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemListener;
import org.eclipse.ecf.filetransfer.IRetrieveFileTransferContainerAdapter;
import org.eclipse.ecf.filetransfer.IRetrieveFileTransferOptions;
import org.eclipse.ecf.filetransfer.IncomingFileTransferException;
import org.eclipse.ecf.filetransfer.UserCancelledException;
import org.eclipse.ecf.filetransfer.events.IFileTransferConnectStartEvent;
import org.eclipse.ecf.filetransfer.events.IFileTransferEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveDoneEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveStartEvent;
import org.eclipse.ecf.filetransfer.events.IRemoteFileSystemBrowseEvent;
import org.eclipse.ecf.filetransfer.events.IRemoteFileSystemEvent;
import org.eclipse.ecf.filetransfer.identity.IFileID;
import org.eclipse.ecf.provider.filetransfer.identity.FileTransferID;
import org.eclipse.ecf.provider.filetransfer.identity.FileTransferNamespace;
import org.eclipse.ecf.provider.filetransfer.util.ProxySetupHelper;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.core.UIServices.AuthenticationInfo;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.osgi.util.NLS;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.osgi.framework.Version;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class ECFURIHandlerImpl extends URIHandlerImpl implements URIResolver
{
  public static final String OPTION_CACHE_HANDLING = "OPTION_CACHE_HANDLING"; //$NON-NLS-1$

  public static final String OPTION_AUTHORIZATION_HANDLER = "OPTION_AUTHORIZATION_HANDLER"; //$NON-NLS-1$

  public static final String OPTION_AUTHORIZATION = "OPTION_AUTHORIZATION"; //$NON-NLS-1$

  public static final String OPTION_PROXY_AUTHORIZATION = "OPTION_PROXY_AUTHORIZATION"; //$NON-NLS-1$

  public static final String OPTION_MONITOR = "OPTION_MONITOR"; //$NON-NLS-1$

  public static final CookieStore COOKIE_STORE = FileTransferListener.DELEGATING_COOKIE_STORE;

  public static final String OPTION_LOGIN_URI = "OPTION_LOGIN_URI"; //$NON-NLS-1$

  public static final String OPTION_FORM_URI = "OPTION_FORM_URI"; //$NON-NLS-1$

  public static final String OPTION_BASIC_AUTHENTICATION = "OPTION_BASIC_AUTHENTICATION"; //$NON-NLS-1$

  private static final String FAILED_EXPECTED_ETAG = "-1"; //$NON-NLS-1$

  private static final URI CACHE_FOLDER = SetupContext.GLOBAL_STATE_LOCATION_URI.appendSegment("cache"); //$NON-NLS-1$

  private static final Map<URI, String> EXPECTED_ETAGS = new HashMap<URI, String>();

  private static final Map<URI, IOException> EXPECTED_EXCEPTIONS = Collections.synchronizedMap(new HashMap<URI, IOException>());

  private static final Map<URI, CountDownLatch> LOCKS = new HashMap<URI, CountDownLatch>();

  private static final boolean TEST_IO_EXCEPTION = false;

  private static final boolean TEST_SLOW_NETWORK = false;

  private static final boolean TRACE = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_ECF_TRACE);

  private static final String API_GITHUB_HOST = "api.github.com"; //$NON-NLS-1$

  private static final String CONTENT_TAG = "\"content\":\""; //$NON-NLS-1$

  private static final int CONNECT_TIMEOUT = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_ECF_CONNECT_TIMEOUT, 10000);

  private static final int READ_TIMEOUT = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_ECF_READ_TIMEOUT, 10000);

  private static final URI ACTUAL_INDEX_SETUP_ARCHIVE_LOCATION_URI = URI
      .createURI(SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI.toString().replace("http:", "https:")); //$NON-NLS-1$ //$NON-NLS-2$

  private static boolean loggedBlockedURI;

  private static final String USER_AGENT;
  static
  {
    String userAgentProperty = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_USER_AGENT);
    if (userAgentProperty == null)
    {
      StringBuilder userAgent = new StringBuilder("eclipse/oomph/"); //$NON-NLS-1$
      if (SetupUtil.INSTALLER_APPLICATION)
      {
        userAgent.append("installer/"); //$NON-NLS-1$
      }
      else if (SetupUtil.SETUP_ARCHIVER_APPLICATION)
      {
        userAgent.append("archiver/"); //$NON-NLS-1$
      }

      Version oomphVersion = SetupCorePlugin.INSTANCE.getBundle().getVersion();
      userAgent.append(oomphVersion);
      USER_AGENT = userAgent.toString();
    }
    else
    {
      USER_AGENT = userAgentProperty;
    }
  }

  private AuthorizationHandler defaultAuthorizationHandler;

  public ECFURIHandlerImpl(AuthorizationHandler defaultAuthorizationHandler)
  {
    this.defaultAuthorizationHandler = defaultAuthorizationHandler;
  }

  public URI resolve(URI uri)
  {
    return transform(uri, null);
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    if (uri.scheme().startsWith("http")) //$NON-NLS-1$
    {
      Set<String> requestedAttributes = getRequestedAttributes(options);
      if (requestedAttributes != null && requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY) && requestedAttributes.size() == 1)
      {
        // For performance reasons, this assumes that all http/https accessible files are read only.
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
        return result;
      }
    }

    return getRemoteAttributes(uri, options);
  }

  private Map<String, ?> getRemoteAttributes(URI uri, Map<?, ?> options)
  {
    if (uri.isPlatform())
    {
      return super.getAttributes(uri, options);
    }

    try
    {
      return new RemoteAttributionsConnectionHandler(uri, options).process();
    }
    catch (IOException ex)
    {
      return Collections.emptyMap();
    }
  }

  private final Map<String, ?> handleResponseAttributes(Set<String> requestedAttributes, Map<Object, Object> response)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY))
    {
      result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_TIME_STAMP))
    {
      Object timeStamp = response.get(URIConverter.RESPONSE_TIME_STAMP_PROPERTY);
      if (timeStamp != null)
      {
        result.put(URIConverter.ATTRIBUTE_TIME_STAMP, timeStamp);
      }
    }

    return result;
  }

  private final Map<String, ?> handleAttributes(Set<String> requestedAttributes, IRemoteFileInfo info)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY))
    {
      result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_TIME_STAMP))
    {
      result.put(URIConverter.ATTRIBUTE_TIME_STAMP, info.getLastModified());
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_LENGTH))
    {
      result.put(URIConverter.ATTRIBUTE_LENGTH, info.getLength());
    }

    return result;
  }

  private final Map<String, ?> handleAttributes(Set<String> requestedAttributes, Map<String, ?> attributes)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY))
    {
      result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_TIME_STAMP))
    {
      Object timeStamp = attributes.get(URIConverter.ATTRIBUTE_TIME_STAMP);
      if (timeStamp != null)
      {
        result.put(URIConverter.ATTRIBUTE_TIME_STAMP, timeStamp);
      }
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_LENGTH))
    {
      Object length = attributes.get(URIConverter.ATTRIBUTE_LENGTH);
      if (length != null)
      {
        result.put(URIConverter.ATTRIBUTE_LENGTH, length);
      }
    }

    return result;
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    return !getAttributes(uri, options).isEmpty();
  }

  @Override
  public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
  {
    if (uri.isPlatform())
    {
      return super.createInputStream(uri, options);
    }

    return new InputStreamConnectionHandler(uri, options).process();
  }

  private static CountDownLatch acquireLock(URI uri) throws IOException
  {
    CountDownLatch countDownLatch = null;
    synchronized (LOCKS)
    {
      countDownLatch = LOCKS.get(uri);
      if (countDownLatch == null)
      {
        countDownLatch = new CountDownLatch(1);
        LOCKS.put(uri, countDownLatch);
        return countDownLatch;
      }
    }

    try
    {
      countDownLatch.await();
      return acquireLock(uri);
    }
    catch (InterruptedException ex)
    {
      throw new IOExceptionWithCause(ex);
    }
  }

  private static void releaseLock(URI uri, CountDownLatch countDownLatch)
  {
    synchronized (LOCKS)
    {
      LOCKS.remove(uri);
    }

    countDownLatch.countDown();
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

  public static Set<? extends URI> clearExpectedETags()
  {
    Set<URI> result;
    synchronized (EXPECTED_ETAGS)
    {
      result = new HashSet<URI>(EXPECTED_ETAGS.keySet());
      EXPECTED_ETAGS.clear();
    }

    EXPECTED_EXCEPTIONS.clear();

    return result;
  }

  public static Job mirror(final Set<? extends URI> uris)
  {
    Job job = new Job(Messages.ECFURIHandlerImpl_ETagMirror_job)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        new ETagMirror().begin(uris, monitor);
        return Status.OK_STATUS;
      }
    };

    job.schedule();
    return job;
  }

  public static URI getCacheFile(URI uri)
  {
    return CACHE_FOLDER.appendSegment(
        IOUtil.encodeFileName((SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI.equals(uri) ? ACTUAL_INDEX_SETUP_ARCHIVE_LOCATION_URI : uri).toString()));
  }

  public static String getETag(URIConverter uriConverter, URI file)
  {
    if (uriConverter.exists(file, null))
    {
      URI eTagFile = file.appendFileExtension("etag"); //$NON-NLS-1$
      if (uriConverter.exists(eTagFile, null))
      {
        try
        {
          return new String(BaseUtil.readFile(uriConverter, null, eTagFile), "UTF-8"); //$NON-NLS-1$
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
    }

    return null;
  }

  private static void setETag(URIConverter uriConverter, URI file, String eTag)
  {
    try
    {
      if (eTag != null)
      {
        BaseUtil.writeFile(uriConverter, null, file.appendFileExtension("etag"), eTag.getBytes("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
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

  private AuthorizationHandler getAuthorizatonHandler(Map<?, ?> options)
  {
    if (options.containsKey(OPTION_AUTHORIZATION_HANDLER))
    {
      return (AuthorizationHandler)options.get(OPTION_AUTHORIZATION_HANDLER);
    }

    return defaultAuthorizationHandler;
  }

  private static String getHost(java.net.URI uri)
  {
    return uri.getHost();
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

  @SuppressWarnings("all")
  private static Date parseHTTPDate(String string)
  {
    try
    {
      return org.apache.http.impl.cookie.DateUtils.parseDate(string);
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }

  static String getExpectedETag(URI uri)
  {
    synchronized (EXPECTED_ETAGS)
    {
      String result = EXPECTED_ETAGS.get(uri);
      return result == null && SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI.equals(uri) ? EXPECTED_ETAGS.get(ACTUAL_INDEX_SETUP_ARCHIVE_LOCATION_URI) : result;
    }
  }

  static void setExpectedETag(URI uri, String eTag)
  {
    synchronized (EXPECTED_ETAGS)
    {
      String originalExpectedETag = EXPECTED_ETAGS.put(uri, eTag);
      if (eTag == null && originalExpectedETag != null)
      {
        EXPECTED_ETAGS.put(uri, originalExpectedETag);
      }
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

  private static Authorization getAuthorizaton(Map<?, ?> options)
  {
    return (Authorization)options.get(OPTION_AUTHORIZATION);
  }

  private static Authorization getProxyAuthorizaton(Map<?, ?> options)
  {
    return (Authorization)options.get(OPTION_PROXY_AUTHORIZATION);
  }

  private static IOException createIOException(String url, Throwable cause)
  {
    String message = cause.getMessage();
    if (message != null && message.contains(url))
    {
      if (cause instanceof IOException)
      {
        return (IOException)cause;
      }

      return new IOExceptionWithCause(cause);
    }

    return new IOExceptionWithCause((StringUtil.isEmpty(message) ? Messages.ECFURIHandlerImpl_Error_exception : message + ": ") + url, cause); //$NON-NLS-1$
  }

  /**
   * @author Ed Merks
   */
  public enum CacheHandling
  {
    CACHE_ONLY, CACHE_WITHOUT_ETAG_CHECKING, CACHE_WITH_ETAG_CHECKING, CACHE_IGNORE
  }

  /**
   * @author Ed Merks
   */
  public interface AuthorizationHandler
  {
    public Authorization authorize(URI uri);

    public Authorization reauthorize(URI uri, Authorization authorization);

    /**
     * @author Ed Merks
     */
    public static final class Authorization
    {
      public static final Authorization UNAUTHORIZED = new Authorization("", ""); //$NON-NLS-1$ //$NON-NLS-2$

      public static final Authorization UNAUTHORIZEABLE = new Authorization("", ""); //$NON-NLS-1$ //$NON-NLS-2$

      private final String user;

      private final String password;

      private boolean saved;

      public Authorization(String user, String password)
      {
        this.user = user == null ? "" : user; //$NON-NLS-1$
        this.password = obscure(password == null ? "" : password); //$NON-NLS-1$
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
        return "Basic " + obscure(user.length() == 0 ? getPassword() : user + ":" + getPassword()); //$NON-NLS-1$ //$NON-NLS-2$
      }

      public boolean isAuthorized()
      {
        return !"".equals(password); //$NON-NLS-1$
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

      public boolean isSaved()
      {
        return saved;
      }

      public void setSaved(boolean saved)
      {
        this.saved = saved;
      }

      @Override
      public String toString()
      {
        return this == UNAUTHORIZEABLE ? "Authorization [unauthorizeable]" //$NON-NLS-1$
            : "Authorization [user=" + user + ", password=" + password + "]" + (saved ? " saved" : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
      }

    }
  }

  /**
   * @author Ed Merks
   */
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
            String user = node.get("user", ""); //$NON-NLS-1$ //$NON-NLS-2$
            String password = node.get("password", ""); //$NON-NLS-1$ //$NON-NLS-2$

            Authorization authorization = new Authorization(user, password);
            if (authorization.isAuthorized())
            {
              authorization.setSaved(true);
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
      if (!currentAuthorization.equals(authorization) && currentAuthorization.isAuthorized() || currentAuthorization == Authorization.UNAUTHORIZEABLE)
      {
        return currentAuthorization;
      }

      if (uiServices != null)
      {
        String host = getHost(uri);
        if (host != null)
        {
          AuthenticationInfo currentAuthenticationInfo = new AuthenticationInfo(authorization.getUser(), authorization.getPassword(), authorization.isSaved());
          AuthenticationInfo authenticationInfo = uiServices.getUsernamePassword(uri.toString(), currentAuthenticationInfo);
          if (authenticationInfo != null && authenticationInfo != UIServices.AUTHENTICATION_PROMPT_CANCELED)
          {
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
                  node.put("user", user, false); //$NON-NLS-1$
                  node.put("password", password, true); //$NON-NLS-1$
                  node.flush();
                  reauthorization.setSaved(true);
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

    @Override
    public String toString()
    {
      StringBuilder result = new StringBuilder(super.toString());
      result.append(" authorizations: "); //$NON-NLS-1$
      result.append(authorizations);
      result.append(" securePreferences: "); //$NON-NLS-1$
      result.append(securePreferences);
      result.append(" uiServices: "); //$NON-NLS-1$
      result.append(uiServices);
      return result.toString();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class FileTransferListener implements IFileTransferListener, ConnectionListener
  {
    @SuppressWarnings("all")
    private static final org.apache.http.impl.client.BasicCookieStore COOKIE_STORE = new org.apache.http.impl.client.BasicCookieStore();

    private static final DelegatingCookieStore DELEGATING_COOKIE_STORE = new DelegatingCookieStore(COOKIE_STORE);

    public final CountDownLatch receiveLatch = new CountDownLatch(1);

    public final String expectedETag;

    public String eTag;

    public ByteArrayOutputStream out;

    public long lastModified;

    public Exception exception;

    private IProgressMonitor monitor;

    public FileTransferListener(String expectedETag, IProgressMonitor monitor)
    {
      this.expectedETag = expectedETag;
      this.monitor = monitor;
    }

    public void handleTransferEvent(IFileTransferEvent event)
    {
      if (event instanceof IFileTransferConnectStartEvent)
      {
        IFileTransferConnectStartEvent connectStartEvent = (IFileTransferConnectStartEvent)event;
        if (monitor != null && monitor.isCanceled())
        {
          connectStartEvent.cancel();

          // Older versions of ECF don't produce a IIncomingFileTransferReceiveDoneEvent.
          exception = new UserCancelledException();
          receiveLatch.countDown();
          return;
        }

        applyCookieStore(connectStartEvent);
      }
      else if (event instanceof IIncomingFileTransferReceiveStartEvent)
      {
        IIncomingFileTransferReceiveStartEvent receiveStartEvent = (IIncomingFileTransferReceiveStartEvent)event;
        if (monitor != null && monitor.isCanceled())
        {
          receiveStartEvent.cancel();

          // Older versions of ECF don't produce a IIncomingFileTransferReceiveDoneEvent.
          exception = new UserCancelledException();
          receiveLatch.countDown();
          return;
        }

        out = new ByteArrayOutputStream();

        @SuppressWarnings("rawtypes")
        Map responseHeaders = receiveStartEvent.getResponseHeaders();
        if (responseHeaders != null)
        {
          eTag = (String)responseHeaders.get("ETag"); //$NON-NLS-1$

          String lastModifiedValue = (String)responseHeaders.get("Last-Modified"); //$NON-NLS-1$
          if (lastModifiedValue != null)
          {
            Date date = parseHTTPDate(lastModifiedValue.toString());
            if (date != null)
            {
              lastModified = date.getTime();
              if (eTag == null)
              {
                eTag = Long.toString(lastModified);
              }
            }
          }

          if (lastModified == 0)
          {
            lastModified = System.currentTimeMillis();
            if (eTag == null)
            {
              eTag = Long.toString(lastModified);
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

    private static void applyCookieStore(final IFileTransferConnectStartEvent connectStartEvent)
    {
      IIncomingFileTransfer fileTransfer = ObjectUtil.adapt(connectStartEvent, IIncomingFileTransfer.class);
      final IFileID fileID = connectStartEvent.getFileID();
      try
      {
        if (fileTransfer != null)
        {
          Object httpClient = ReflectUtil.getValue("httpClient", fileTransfer); //$NON-NLS-1$

          if (TRACE)
          {
            System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_ManageCookies_message, fileID.getURI(), httpClient));
          }

          ReflectUtil.setValue("cookieStore", httpClient, new org.apache.http.client.CookieStore() //$NON-NLS-1$
          {
            @SuppressWarnings("all")
            public List<Cookie> getCookies()
            {
              return COOKIE_STORE.getCookies();
            }

            @SuppressWarnings("all")
            public boolean clearExpired(Date date)
            {
              synchronized (COOKIE_STORE)
              {
                List<Cookie> originalCookies = new ArrayList<Cookie>(COOKIE_STORE.getCookies());
                COOKIE_STORE.clearExpired(date);
                List<Cookie> remainingCookies = COOKIE_STORE.getCookies();
                originalCookies.removeAll(remainingCookies);

                for (Cookie cookie : originalCookies)
                {
                  HttpCookie httpCookie = createCookie(cookie);
                  DELEGATING_COOKIE_STORE.basicRemove(null, new HttpCookie(cookie.getName(), cookie.getValue()));
                }

                return !originalCookies.isEmpty();
              }
            }

            @SuppressWarnings("all")
            public void clear()
            {
              COOKIE_STORE.clear();
              DELEGATING_COOKIE_STORE.basicRemoveAll();
            }

            @SuppressWarnings("all")
            public void addCookie(Cookie cookie)
            {
              try
              {
                java.net.URI uri = fileID.getURI();
                HttpCookie httpCookie = createCookie(cookie);
                DELEGATING_COOKIE_STORE.basicAdd(uri, httpCookie);
              }
              catch (Exception ex)
              {
                // Ignore bad information.
              }

              COOKIE_STORE.addCookie(cookie);
            }

            public HttpCookie createCookie(Cookie cookie)
            {
              HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
              httpCookie.setDomain(cookie.getDomain());
              httpCookie.setPath(cookie.getPath());
              httpCookie.setVersion(cookie.getVersion());
              return httpCookie;
            }
          });

          @SuppressWarnings("deprecation")
          String[] permissiveDatePatterns = ReflectUtil.getValue("DEFAULT_DATE_PATTERNS", org.apache.http.impl.cookie.BrowserCompatSpec.class); //$NON-NLS-1$

          try
          {
            // The following ensures that more permissive date patterns are used to parse the expiration date of cookies.
            Object defaultParameters = ReflectUtil.getValue("defaultParams", httpClient); //$NON-NLS-1$

            if (TRACE)
            {
              System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_ManagingHandling_message, fileID.getURI()));
            }

            @SuppressWarnings("deprecation")
            String datePatternsParameterName = org.apache.http.cookie.params.CookieSpecPNames.DATE_PATTERNS;
            ReflectUtil.invokeMethod(ReflectUtil.getMethod(defaultParameters, "setParameter", String.class, Object.class), defaultParameters, //$NON-NLS-1$
                datePatternsParameterName, Arrays.asList(permissiveDatePatterns));
          }
          catch (Throwable throwable2)
          {
            if (TRACE)
            {
              System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_ManagingCookieHandling_message, fileID.getURI()));
            }

            // This is the case of the ECF implementation based on Apache 4.5.
            Object copiedSpecRegistry = ReflectUtil.getValue("cookieSpecRegistry", httpClient); //$NON-NLS-1$
            ConcurrentHashMap<String, CookieSpecProvider> map = ReflectUtil.getValue("map", copiedSpecRegistry); //$NON-NLS-1$
            for (Map.Entry<String, CookieSpecProvider> entry : map.entrySet())
            {
              final CookieSpecProvider cookieSpecProvider = entry.getValue();
              if (cookieSpecProvider instanceof org.apache.http.impl.cookie.DefaultCookieSpecProvider)
              {
                // Change the date patterns to be permissive.
                ReflectUtil.setValue("datepatterns", cookieSpecProvider, permissiveDatePatterns); //$NON-NLS-1$
              }
            }
          }
        }
      }
      catch (Throwable throwable)
      {
        if (TRACE)
        {
          try
          {
            System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_FailedToManage_message, fileID.getURI()));
          }
          catch (URISyntaxException ex)
          {
            System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_BadFileID_message, fileID));
          }
        }
      }
    }

    public void await() throws InterruptedException
    {
      receiveLatch.await();
    }

    public Exception getException()
    {
      return exception;
    }

    public boolean hasTransferException()
    {
      return exception instanceof IncomingFileTransferException;
    }

    public int getErrorCode()
    {
      return ((IncomingFileTransferException)exception).getErrorCode();
    }

    /**
     * @author Ed Merks
     */
    private static class DelegatingCookieStore implements CookieStore
    {
      private final CookieStore delegate = new CookieManager().getCookieStore();

      private final org.apache.http.impl.client.BasicCookieStore basicCookieStore;

      public DelegatingCookieStore(org.apache.http.impl.client.BasicCookieStore basicCookieStore)
      {
        this.basicCookieStore = basicCookieStore;
      }

      public void add(java.net.URI uri, HttpCookie httpCookie)
      {
        basicAdd(uri, httpCookie);
        basicCookieStore.addCookie(createCookie(uri, httpCookie));
      }

      public void basicAdd(java.net.URI uri, HttpCookie httpCookie)
      {
        if (TRACE)
        {
          System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_AddingCookie_message, uri, httpCookie));
        }

        delegate.add(uri, httpCookie);
      }

      public List<HttpCookie> get(java.net.URI uri)
      {
        return delegate.get(uri);
      }

      public List<HttpCookie> getCookies()
      {
        return delegate.getCookies();
      }

      public List<java.net.URI> getURIs()
      {
        return delegate.getURIs();
      }

      public boolean remove(java.net.URI uri, HttpCookie httpCookie)
      {
        org.apache.http.impl.cookie.BasicClientCookie basicClientCookie = createCookie(uri, httpCookie);
        basicClientCookie.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        basicCookieStore.addCookie(basicClientCookie);
        return basicRemove(uri, httpCookie);
      }

      public boolean basicRemove(java.net.URI uri, HttpCookie cookie)
      {
        if (TRACE)
        {
          System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_AddingCookie_message, uri, cookie));
        }

        return delegate.remove(uri, cookie);
      }

      public boolean removeAll()
      {
        basicCookieStore.clear();
        return basicRemoveAll();
      }

      public boolean basicRemoveAll()
      {
        return delegate.removeAll();
      }

      private org.apache.http.impl.cookie.BasicClientCookie createCookie(java.net.URI uri, HttpCookie httpCookie)
      {
        org.apache.http.impl.cookie.BasicClientCookie basicClientCookie = new org.apache.http.impl.cookie.BasicClientCookie(httpCookie.getName(),
            httpCookie.getValue());
        basicClientCookie.setPath(httpCookie.getPath());
        if (uri != null)
        {
          basicClientCookie.setDomain(uri.getHost());
        }

        if (httpCookie.hasExpired())
        {
          basicClientCookie.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        }

        return basicClientCookie;
      }

      @Override
      public String toString()
      {
        return getCookies().toString();
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class RemoteFileSystemListener implements IRemoteFileSystemListener, ConnectionListener
  {
    public final CountDownLatch receiveLatch = new CountDownLatch(1);

    public Exception exception;

    public IRemoteFileInfo info;

    public RemoteFileSystemListener()
    {
    }

    public void handleRemoteFileEvent(IRemoteFileSystemEvent event)
    {
      if (event instanceof IRemoteFileSystemBrowseEvent)
      {
        IRemoteFileSystemBrowseEvent browseEvent = (IRemoteFileSystemBrowseEvent)event;
        exception = browseEvent.getException();
        if (exception == null)
        {
          for (IRemoteFile remoteFile : browseEvent.getRemoteFiles())
          {
            info = remoteFile.getInfo();
            break;
          }
        }

        receiveLatch.countDown();
      }
    }

    public void await() throws InterruptedException
    {
      receiveLatch.await();
    }

    public Exception getException()
    {
      return exception;
    }

    public boolean hasTransferException()
    {
      return exception instanceof BrowseFileTransferException;
    }

    public int getErrorCode()
    {
      return ((BrowseFileTransferException)exception).getErrorCode();
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ETagMirror extends WorkerPool<ETagMirror, URI, ETagMirror.Worker>
  {
    private static final Map<Object, Object> OPTIONS;

    private static final URIConverter URI_CONVERTER;

    private static final String OPTION_ETAG_MIRROR = "OPTION_ETAG_MIRROR"; //$NON-NLS-1$

    static
    {
      ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
      OPTIONS = resourceSet.getLoadOptions();
      OPTIONS.put(OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITH_ETAG_CHECKING);
      URI_CONVERTER = resourceSet.getURIConverter();
      clearExpectedETags();
    }

    private Set<? extends URI> uris;

    private Map<Object, Object> options = new HashMap<Object, Object>(OPTIONS);

    public ETagMirror()
    {
      options.put(OPTION_ETAG_MIRROR, this);
    }

    @Override
    protected Worker createWorker(URI key, int workerID, boolean secondary)
    {
      return new Worker(NLS.bind(Messages.ECFURIHandlerImpl_ETagMirror_thread, key), this, key, workerID, secondary);
    }

    public void begin(Set<? extends URI> uris, final IProgressMonitor monitor)
    {
      options.put(OPTION_MONITOR, monitor);

      this.uris = uris;
      int size = uris.size();
      monitor.beginTask(NLS.bind(Messages.ECFURIHandlerImpl_MirroringResources_task, size), uris.size());
      super.begin(Messages.ECFURIHandlerImpl_Mirroring_task, monitor);
    }

    @Override
    protected void run(String taskName, IProgressMonitor monitor)
    {
      perform(uris);
    }

    protected void cacheUpdated(URI uri)
    {
    }

    /**
     * @author Ed Merks
     */
    private static class Worker extends WorkerPool.Worker<URI, ETagMirror>
    {
      protected Worker(String name, ETagMirror workPool, URI key, int id, boolean secondary)
      {
        super(name, workPool, key, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        URI key = getKey();
        ETagMirror workPool = getWorkPool();
        IProgressMonitor workpoolMonitor = workPool.getMonitor();

        try
        {
          workpoolMonitor.subTask(NLS.bind(Messages.ECFURIHandlerImpl_MirrorURL_task, key));
        }
        catch (Exception ex)
        {
          SetupCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
        }

        try
        {
          if (TEST_SLOW_NETWORK)
          {
            try
            {
              Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
              ex.printStackTrace();
            }
          }

          URI_CONVERTER.createInputStream(key, workPool.options).close();
        }
        catch (IOException ex)
        {
          SetupCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
        finally
        {
          try
          {
            workpoolMonitor.worked(1);
          }
          catch (Exception ex)
          {
            SetupCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }

        return Status.OK_STATUS;
      }
    }
  }

  /**
   * @author Ed Merks
   *
   * An interface implemented by {@link FileTransferListener} and {@link RemoteFileSystemListener} for provide common processing support in {@link ConnectionHandler#process()}.
   */
  public interface ConnectionListener
  {
    public void await() throws InterruptedException;

    public Exception getException();

    public boolean hasTransferException();

    public int getErrorCode();
  }

  /**
   * @author Ed Merks
   *
   * A handler to unify the common processing logic for {@link ECFURIHandlerImpl#getRemoteAttributes(URI, Map)} and {@link ECFURIHandlerImpl#createInputStream(URI, Map)}.
   */
  private abstract class ConnectionHandler<T>
  {
    final protected URI originalURI;

    protected URI uri;

    protected final Map<?, ?> options;

    protected String tracePrefix;

    protected Authorization forceAuthorization;

    public ConnectionHandler(URI uri, Map<?, ?> options) throws IOException
    {
      this.uri = uri;
      this.originalURI = uri;
      this.options = options;
    }

    /**
     * Performs all the processing for the connection and returns the result.
     */
    public T process() throws IOException
    {
      // First transform the URI, if necessary, extracting a login URI or form URI if one is needed.
      Map<Object, Object> transformedOptions = new HashMap<Object, Object>(options);
      uri = SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI.equals(uri) ? ACTUAL_INDEX_SETUP_ARCHIVE_LOCATION_URI : transform(originalURI, transformedOptions);

      // This is used to prefix all tracing statements.
      tracePrefix = NLS.bind(Messages.ECFURIHandlerImpl_ECFPrefix_message, uri);

      IProgressMonitor monitor = (IProgressMonitor)options.get(OPTION_MONITOR);

      CountDownLatch countDownLatch = acquireLock(uri);
      try
      {
        if (TEST_IO_EXCEPTION)
        {
          File folder = new File(CACHE_FOLDER.toFileString());
          if (folder.isDirectory())
          {
            System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_DeletingCache_message, folder));
            IOUtil.deleteBestEffort(folder);
          }

          throw new IOException(NLS.bind(Messages.ECFURIHandlerImpl_NetworkProblem_exception, uri));
        }

        // Setup the basic context for subsequent processing.
        CacheHandling cacheHandling = getCacheHandling(options);
        URIConverter uriConverter = getURIConverter(options);
        URI cacheURI = getCacheFile(uri);
        String eTag = cacheHandling == CacheHandling.CACHE_IGNORE ? null : getETag(uriConverter, cacheURI);
        String expectedETag = cacheHandling == CacheHandling.CACHE_IGNORE ? null : getExpectedETag(uri);

        if (TRACE)
        {
          System.out.println(tracePrefix + " uri=" + uri); //$NON-NLS-1$
          System.out.println(tracePrefix + " cacheURI=" + cacheURI); //$NON-NLS-1$
          System.out.println(tracePrefix + " eTag=" + eTag); //$NON-NLS-1$
          System.out.println(tracePrefix + " expectedETag=" + expectedETag); //$NON-NLS-1$
        }

        // To prevent Eclipse's Git server from being overload, because it can't scale to thousands of users, we block all direct access.
        String host = getHost(uri);
        boolean isBlockedEclipseGitURI = !SetupUtil.SETUP_ARCHIVER_APPLICATION && "git.eclipse.org".equals(host); //$NON-NLS-1$
        if (isBlockedEclipseGitURI && uriConverter.exists(cacheURI, options))
        {
          // If the file is in the cache, it's okay to use that cached version, so try that first.
          cacheHandling = CacheHandling.CACHE_ONLY;
        }

        // This is a URI that fails to load at all, so fail quickly.
        if (FAILED_EXPECTED_ETAG.equals(expectedETag))
        {
          throw EXPECTED_EXCEPTIONS.get(uri);
        }

        if (expectedETag != null || cacheHandling == CacheHandling.CACHE_ONLY || cacheHandling == CacheHandling.CACHE_WITHOUT_ETAG_CHECKING)
        {
          if (cacheHandling == CacheHandling.CACHE_ONLY || //
              cacheHandling == CacheHandling.CACHE_WITHOUT_ETAG_CHECKING ? eTag != null : expectedETag.equals(eTag))
          {
            try
            {
              return handleCache(uriConverter, cacheURI, expectedETag);
            }
            catch (IOException ex)
            {
              // Perhaps another JVM is busy writing this file.
              // Proceed as if it doesn't exist.
              if (TRACE)
              {
                System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_UnableToLoadCache_message);
              }
            }
          }
        }

        // In general all Eclipse-hosted setups should be in the Eclipse project or product catalog and therefore should be in
        // SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI or should already be in the cache from running the setup archiver application.
        if (isBlockedEclipseGitURI)
        {
          synchronized (this)
          {
            if (!loggedBlockedURI)
            {
              String launcher = OS.getCurrentLauncher(true);
              if (launcher == null)
              {
                launcher = "eclipse"; //$NON-NLS-1$
              }

              // We'll log a single warning for this case.
              SetupCorePlugin.INSTANCE.log(NLS.bind(Messages.ECFURIHandlerImpl_EclipseGitBlocked_meesage, uri, launcher), //
                  IStatus.WARNING);

              loggedBlockedURI = true;
            }
          }

          return handleEclipseGit();
        }

        URI loginURI = (URI)transformedOptions.get(OPTION_LOGIN_URI);
        if (loginURI != null)
        {
          try
          {
            if (TRACE)
            {
              System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_ReadingLogin_message, loginURI));
            }

            InputStream inputStream = createInputStream(loginURI, options);
            inputStream.close();
          }
          catch (IOException ex)
          {
            // Ignore this.
            // The main URI should still be attempted.
            // If it can't get authorization and isn't in the cache,
            // there will be an appropriate stack trace for that URI.
          }
        }

        // Determine the authorization handler for handling credentials.
        AuthorizationHandler authorizationHandler = getAuthorizatonHandler(options);

        URI formURI = (URI)transformedOptions.get(OPTION_FORM_URI);
        if (formURI != null)
        {
          try
          {
            if (TRACE)
            {
              System.out.println(NLS.bind(Messages.ECFURIHandlerImpl_ProcessingForm_message, formURI));
            }

            FormHandler formHandler = new FormHandler(formURI, uriConverter, authorizationHandler);
            formHandler.process();
          }
          catch (IOException ex)
          {
            // Ignore this.
            // The main URI should still be attempted.
            // If it can't get authorization and isn't in the cache,
            // there will be an appropriate stack trace for that URI.
          }
        }

        // Encapsulate all the information needed to access and process the URI.
        ProxyWrapper proxyWrapper = ProxyWrapper.create(uri);

        // Create the container for the connection.
        IContainer container = createContainer();

        // If we don't have an authorization in the options, but we have a handler,
        // we might as well get the authorization that might exist in the secure storage so our first access uses the right credentials up front.
        Authorization authorization = getAuthorizaton(options);
        if (authorization == null && authorizationHandler != null)
        {
          authorization = authorizationHandler.authorize(uri);
        }

        // If we are forcing basic authentication...
        boolean basicAuthentication = Boolean.TRUE.equals(transformedOptions.get(OPTION_BASIC_AUTHENTICATION));
        if (basicAuthentication)
        {
          // Ensure that we have an authorization, if possible.
          if ((authorization == null || !authorization.isAuthorized()) && authorizationHandler != null)
          {
            authorization = authorizationHandler.reauthorize(uri, authorization);
          }

          // Record it so that it's definitely passed in the request header.
          forceAuthorization = authorization;
        }

        // If we don't have a proxy authorization in the options, but we have a handler,
        // we might as well get the proxy authorization that might exist in the secure storage so our first access uses the right proxy credentials up front.
        Authorization proxyAuthorization = getProxyAuthorizaton(options);
        if (proxyWrapper.isProxified() && proxyAuthorization == null && authorizationHandler != null)
        {
          proxyAuthorization = authorizationHandler.authorize(proxyWrapper.getProxyURI());
        }

        // If we are using a proxy and it doesn't have an authorization, but we have a proxy authorization that is authorized,
        // use those credentials for the proxy.
        if (proxyWrapper.isProxified() && !proxyWrapper.hasAuthorization() && proxyAuthorization != null && proxyAuthorization.isAuthorized())
        {
          proxyWrapper.authorize(proxyAuthorization);
        }

        if (TRACE)
        {
          if (proxyWrapper.isProxified())
          {
            System.out.println(tracePrefix + " proxy=" + proxyWrapper); //$NON-NLS-1$
          }

          System.out.println(tracePrefix + " authorizationHandler=" + authorizationHandler); //$NON-NLS-1$
        }

        int triedReauthorization = 0;
        int triedProxyReauthorization = 0;
        for (int i = 0;; ++i)
        {
          if (TRACE)
          {
            System.out.println(tracePrefix + " trying=" + i); //$NON-NLS-1$
            System.out.println(tracePrefix + " triedReauthorization=" + triedReauthorization); //$NON-NLS-1$
            System.out.println(tracePrefix + " authorization=" + authorization); //$NON-NLS-1$
            System.out.println(tracePrefix + " triedProxyReauthorization=" + triedProxyReauthorization); //$NON-NLS-1$
            System.out.println(tracePrefix + " proxyAuthorization=" + proxyAuthorization); //$NON-NLS-1$
          }

          // Configure the connection and its associated listener.
          ConnectionListener transferListener = createConnectionListener(container, proxyWrapper, authorization, eTag, monitor);

          try
          {
            // Start the connection for the URI's associated transfer ID.
            FileTransferID fileTransferID = new FileTransferID(new FileTransferNamespace(), proxyWrapper.getURI());
            sendConnectionRequest(fileTransferID, host);
          }
          catch (ECFException ex)
          {
            if (TRACE)
            {
              System.out.println(tracePrefix + " " + ex.getClass().getSimpleName()); //$NON-NLS-1$
              ex.printStackTrace(System.out);
            }

            throw createIOException(uri.toString(), ex);
          }

          try
          {
            // Wait for the connection processing to complete.
            transferListener.await();
          }
          catch (InterruptedException ex)
          {
            if (TRACE)
            {
              System.out.println(tracePrefix + " InterruptedException"); //$NON-NLS-1$
              ex.printStackTrace(System.out);
            }

            throw createIOException(uri.toString(), ex);
          }

          // Handle any exception captured during the connection processing.
          Exception exception = transferListener.getException();
          if (exception != null)
          {
            if (TRACE)
            {
              System.out.println(tracePrefix + " transferLister.exception"); //$NON-NLS-1$
              exception.printStackTrace(System.out);
            }

            // If it's not a cancel exception...
            if (!(exception instanceof UserCancelledException))
            {
              // If it's a socked timeout exception, retry the socket 3 times before failing.
              if ((exception instanceof SocketTimeoutException || exception.getCause() instanceof SocketTimeoutException) && i < 2)
              {
                continue;
              }

              // If there an authorization handler and the listener's exception is the specialized exception associated with the listener.
              if (authorizationHandler != null && transferListener.hasTransferException())
              {
                // In this case the exception has an error code.
                int errorCode = transferListener.getErrorCode();
                if (TRACE)
                {
                  System.out.println(tracePrefix + " errorCode=" + errorCode); //$NON-NLS-1$
                }

                // If we have a proxy authentication problem...
                if (errorCode == HttpURLConnection.HTTP_PROXY_AUTH && proxyWrapper.isProxified())
                {
                  // Get the proxy authorization if we don't already have one.
                  if (proxyAuthorization == null)
                  {
                    proxyAuthorization = authorizationHandler.authorize(proxyWrapper.getProxyURI());

                    // If the proxy is already authorized, apply those credentials to the proxy.
                    if (proxyAuthorization.isAuthorized())
                    {
                      proxyWrapper.authorize(proxyAuthorization);
                      --i;
                      continue;
                    }
                  }

                  // If the proxy authorization remains authorizable, prompt for the password at most three times.
                  if (!proxyAuthorization.isUnauthorizeable() && triedProxyReauthorization++ < 3)
                  {
                    proxyAuthorization = authorizationHandler.reauthorize(proxyWrapper.getProxyURI(), proxyAuthorization);
                    if (proxyAuthorization.isAuthorized())
                    {
                      proxyWrapper.authorize(proxyAuthorization);
                      --i;
                      continue;
                    }
                  }
                }
                // We assume contents can be accessed via the github API https://developer.github.com/v3/repos/contents/#get-contents
                // That API, for security reasons, does not return HTTP_UNAUTHORIZED, so we need this special case for that host.
                else if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED
                    || API_GITHUB_HOST.equals(getHost(uri)) && errorCode == HttpURLConnection.HTTP_NOT_FOUND
                    || forceAuthorization != null && errorCode == HttpURLConnection.HTTP_FORBIDDEN || errorCode == HttpURLConnection.HTTP_NOT_FOUND)
                {
                  // Get the authorization if we don't already have one.
                  if (authorization == null)
                  {
                    authorization = authorizationHandler.authorize(uri);

                    // If it is authorized, use it now.
                    if (authorization.isAuthorized())
                    {
                      --i;
                      if (forceAuthorization != null)
                      {
                        forceAuthorization = authorization;
                      }

                      continue;
                    }
                  }

                  // If the authorization remains authorizable, prompt for the password at most three times.
                  if (!authorization.isUnauthorizeable() && triedReauthorization++ < 3)
                  {
                    authorization = authorizationHandler.reauthorize(uri, authorization);
                    if (authorization.isAuthorized())
                    {
                      --i;
                      if (forceAuthorization != null)
                      {
                        forceAuthorization = authorization;
                      }

                      continue;
                    }
                  }
                }
              }

              if (transferListener.hasTransferException())
              {
                // We can't do a HEAD request.
                int errorCode = transferListener.getErrorCode();
                if (errorCode == HttpURLConnection.HTTP_BAD_METHOD)
                {
                  T result = handleBadMethod(options);
                  if (result != null)
                  {
                    return result;
                  }
                }
              }
            }

            if (!CacheHandling.CACHE_IGNORE.equals(cacheHandling) && uriConverter.exists(cacheURI, options)
                && (!transferListener.hasTransferException() || transferListener.getErrorCode() != HttpURLConnection.HTTP_NOT_FOUND)
                || uri.equals(ACTUAL_INDEX_SETUP_ARCHIVE_LOCATION_URI) || loginURI != null)
            {
              return handleCache(uriConverter, cacheURI, eTag);
            }

            if (TRACE)
            {
              System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_Failing_message);
            }

            IOException ioException = createIOException(uri.toString(), transferListener.getException());
            EXPECTED_EXCEPTIONS.put(uri, ioException);
            setExpectedETag(uri, FAILED_EXPECTED_ETAG);

            throw ioException;
          }

          // Saves the credentials for the proxy if they've been authorized at some point during the connection processing.
          proxyWrapper.update();

          return handleResult(uriConverter, cacheURI);
        }
      }
      finally
      {
        releaseLock(uri, countDownLatch);
      }
    }

    protected abstract T handleCache(URIConverter uriConverter, URI cacheURI, String expectedETag) throws IOException;

    protected abstract T handleEclipseGit() throws IOException;

    protected abstract ConnectionListener createConnectionListener(IContainer container, ProxyWrapper proxyWrapper, Authorization authorization, String eTag,
        IProgressMonitor monitor);

    protected abstract void sendConnectionRequest(FileTransferID fileTransferID, String host) throws ECFException;

    protected T handleBadMethod(Map<?, ?> options)
    {
      return null;
    }

    protected abstract T handleResult(URIConverter uriConverter, URI cacheURI) throws IOException;
  }

  /**
   * @author Ed Merks
   *
   * A connection handler used by {@link ECFURIHandlerImpl#createInputStream(URI, Map)}.
   */
  private class InputStreamConnectionHandler extends ConnectionHandler<InputStream>
  {
    private IRetrieveFileTransferContainerAdapter fileTransfer;

    private FileTransferListener transferListener;

    public InputStreamConnectionHandler(URI uri, Map<?, ?> options) throws IOException
    {
      super(uri, options);
    }

    @Override
    protected InputStream handleCache(URIConverter uriConverter, URI cacheURI, String expectedETag) throws IOException
    {
      setExpectedETag(uri, transferListener == null ? expectedETag
          : transferListener.eTag == null ? expectedETag == null ? Long.toString(System.currentTimeMillis()) : expectedETag : transferListener.eTag);
      InputStream result = uriConverter.createInputStream(cacheURI, options);
      if (TRACE)
      {
        System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_ReturningCachedContents_message);
      }

      return result;
    }

    @Override
    protected InputStream handleEclipseGit() throws IOException
    {
      throw new IOException(NLS.bind(Messages.ECFURIHandlerImpl_EclipseGitAccessBlocked_message, uri));
    }

    @Override
    protected ConnectionListener createConnectionListener(IContainer container, ProxyWrapper proxyWrapper, Authorization authorization, String eTag,
        IProgressMonitor monitor)
    {
      fileTransfer = ObjectUtil.adapt(container, IRetrieveFileTransferContainerAdapter.class);

      fileTransfer.setProxy(proxyWrapper.getProxy());

      if (authorization != null && authorization.isAuthorized())
      {
        fileTransfer.setConnectContextForAuthentication(
            ConnectContextFactory.createUsernamePasswordConnectContext(authorization.getUser(), authorization.getPassword()));
      }

      transferListener = new FileTransferListener(eTag, monitor);
      return transferListener;
    }

    private void putRequestHeader(Map<Object, Object> requestOptions, String option, String value)
    {
      @SuppressWarnings("unchecked")
      Map<String, String> requestHeaders = (Map<String, String>)requestOptions.get(IRetrieveFileTransferOptions.REQUEST_HEADERS);
      if (requestHeaders == null)
      {
        requestHeaders = new HashMap<String, String>();
        requestOptions.put(IRetrieveFileTransferOptions.REQUEST_HEADERS, requestHeaders);
      }

      requestHeaders.put(option, value);
    }

    @Override
    protected void sendConnectionRequest(FileTransferID fileTransferID, String host) throws ECFException
    {
      Map<Object, Object> requestOptions = new HashMap<Object, Object>();
      requestOptions.put(IRetrieveFileTransferOptions.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
      requestOptions.put("org.eclipse.ecf.provider.filetransfer.httpclient4.retrieve.connectTimeout", CONNECT_TIMEOUT); //$NON-NLS-1$
      requestOptions.put(IRetrieveFileTransferOptions.READ_TIMEOUT, READ_TIMEOUT);
      requestOptions.put("org.eclipse.ecf.provider.filetransfer.httpclient4.retrieve.readTimeout", READ_TIMEOUT); //$NON-NLS-1$

      if (!StringUtil.isEmpty(USER_AGENT) && host != null && host.endsWith(".eclipse.org")) //$NON-NLS-1$
      {
        putRequestHeader(requestOptions, "User-Agent", USER_AGENT); //$NON-NLS-1$
      }

      if (forceAuthorization != null)
      {
        @SuppressWarnings("unchecked")
        Map<String, String> requestHeaders = (Map<String, String>)requestOptions.get(IRetrieveFileTransferOptions.REQUEST_HEADERS);
        if (requestHeaders == null)
        {
          requestHeaders = new HashMap<String, String>();
          requestOptions.put(IRetrieveFileTransferOptions.REQUEST_HEADERS, requestHeaders);
        }

        putRequestHeader(requestOptions, "Authorization", forceAuthorization.getAuthorization()); //$NON-NLS-1$

        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_ForcingBaseAuthentication_message + forceAuthorization);
        }
      }

      if (transferListener.expectedETag != null && getCacheHandling(options) != CacheHandling.CACHE_IGNORE)
      {
        putRequestHeader(requestOptions, "If-None-Match", transferListener.expectedETag); //$NON-NLS-1$

        if (TRACE)
        {
          System.out.println(tracePrefix + " using If-None-Match : " + transferListener.expectedETag); //$NON-NLS-1$
        }
      }

      fileTransfer.sendRetrieveRequest(fileTransferID, transferListener, requestOptions);
    }

    @Override
    protected InputStream handleResult(URIConverter uriConverter, URI cacheURI) throws IOException
    {
      byte[] bytes = transferListener.out.toByteArray();

      // In the case of the Github API, the bytes will be JSON that contains a "content" pair containing the Base64 encoding of the actual contents.
      if (API_GITHUB_HOST.equals(getHost(uri)))
      {
        // Find the start tag in the JSON value.
        String value = new String(bytes, "UTF-8"); //$NON-NLS-1$
        int start = value.indexOf(CONTENT_TAG);
        if (start != -1)
        {
          // Find the ending quote of the encoded contents.
          start += CONTENT_TAG.length();
          int end = value.indexOf('"', start);
          if (end != -1)
          {
            // The content is delimited by \n so split on that during the conversion.
            String content = value.substring(start, end);
            String[] split = content.split("\\\\n"); //$NON-NLS-1$

            // Write the converted bytes to a new stream and process those bytes instead.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (String line : split)
            {
              byte[] binary = XMLTypeFactory.eINSTANCE.createBase64Binary(line);
              out.write(binary);
            }

            out.close();
            bytes = out.toByteArray();
          }
        }
      }

      try
      {
        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_WritingCache_message);
        }

        BaseUtil.writeFile(uriConverter, options, cacheURI, bytes);
      }
      catch (IORuntimeException ex)
      {
        // Ignore attempts to write out to the cache file.
        // This may collide with another JVM doing exactly the same thing.
        transferListener.eTag = null;

        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_FailedWritingCache_message);
          ex.printStackTrace(System.out);
        }
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

      ETagMirror etagMirror = (ETagMirror)options.get(ETagMirror.OPTION_ETAG_MIRROR);
      if (etagMirror != null)
      {
        etagMirror.cacheUpdated(originalURI);
      }

      if (TRACE)
      {
        System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_ReturningSuccessfulResults_message);
      }

      return new ByteArrayInputStream(bytes);
    }
  }

  /**
   * @author Ed Merks
   *
   * A connection handler used by {@link ECFURIHandlerImpl#getRemoteAttributes(URI, Map)}
   */
  private class RemoteAttributionsConnectionHandler extends ConnectionHandler<Map<String, ?>>
  {
    private IRemoteFileSystemBrowserContainerAdapter fileBrowser;

    private RemoteFileSystemListener fileSystemListener;

    private final Set<String> requestedAttributes;

    public RemoteAttributionsConnectionHandler(URI uri, Map<?, ?> options) throws IOException
    {
      super(uri, options);

      requestedAttributes = getRequestedAttributes(options);
    }

    @Override
    protected Map<String, ?> handleCache(URIConverter uriConverter, URI cacheURI, String expectedETag) throws IOException
    {
      Map<String, ?> result = handleAttributes(requestedAttributes, uriConverter.getAttributes(cacheURI, options));
      return result;
    }

    @Override
    protected Map<String, ?> handleEclipseGit() throws IOException
    {
      return Collections.emptyMap();
    }

    @Override
    protected ConnectionListener createConnectionListener(IContainer container, ProxyWrapper proxyWrapper, Authorization authorization, String eTag,
        IProgressMonitor monitor)
    {
      fileBrowser = ObjectUtil.adapt(container, IRemoteFileSystemBrowserContainerAdapter.class);

      fileBrowser.setProxy(proxyWrapper.getProxy());

      if (authorization != null && authorization.isAuthorized())
      {
        fileBrowser.setConnectContextForAuthentication(
            ConnectContextFactory.createUsernamePasswordConnectContext(authorization.getUser(), authorization.getPassword()));
      }

      fileSystemListener = new RemoteFileSystemListener();
      return fileSystemListener;
    }

    @Override
    protected void sendConnectionRequest(FileTransferID fileTransferID, String host) throws ECFException
    {
      fileBrowser.sendBrowseRequest(fileTransferID, fileSystemListener);
    }

    @Override
    protected Map<String, ?> handleResult(URIConverter uriConverter, URI cacheURI) throws IOException
    {
      // In the case of the Github API, the bytes will be JSON that contains a "content" pair containing the Base64 encoding of the actual contents.
      if (API_GITHUB_HOST.equals(getHost(uri)))
      {
        // We should have a special case for that too.
      }

      return handleAttributes(requestedAttributes, fileSystemListener.info);
    }

    @Override
    protected Map<String, ?> handleBadMethod(Map<?, ?> options)
    {
      if (TRACE)
      {
        System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_UnsupportedHreadRequest_message);
      }

      // Try instead to create an input stream and use the response to get at least the timestamp property.
      Map<Object, Object> specializedOptions = new HashMap<Object, Object>(options);
      Map<Object, Object> response = new HashMap<Object, Object>();
      specializedOptions.put(URIConverter.OPTION_RESPONSE, response);
      try
      {
        InputStream inputStream = createInputStream(uri, specializedOptions);
        inputStream.close();
        System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_UsingResponseFromGetRequest_message);
        return handleResponseAttributes(requestedAttributes, response);
      }
      catch (IOException ex)
      {
        if (TRACE)
        {
          // This implies a GET request also fails, so continue the processing...
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_GetRequestFailed_message);
          ex.printStackTrace(System.out);
        }
      }

      return null;
    }
  }

  /**
   * @author Ed Merks
   *
   * https://example.com/gerrit/gitweb?p=EXAMPLE.git;a=blob_plain;f=Src/com.example.releng/example.setup;hb=HEAD
   */
  public static class Main
  {
    public void main(String[] args) throws Exception
    {
      // TODO
      // We might need to produce a result that is separated with & rather than ; for some servers?

      URI expectedURI = URI.createURI("https://example.com/gerrit/gitweb?p=EXAMPLE.git;a=blob_plain;f=Src/com.example.releng/example.setup;hb=HEAD"); //$NON-NLS-1$
      URI inputURI = URI.createURI(
          "https://user:password@example.com:1234/gerrit/gitweb/EXAMPLE.git/Src/com.example.releng/example.setup?oomph=b[0..1];oomph_login=s://a/[0..1]/'login';oomph-p=[2];oomph-f=[3..];a=blob_plain;hb=HEAD;" //$NON-NLS-1$
              + "oomph-s=s;" + // //$NON-NLS-1$
              "oomph-S=S;" + // //$NON-NLS-1$
              "oomph-u=u;" + // //$NON-NLS-1$
              "oomph-U=U;" + // //$NON-NLS-1$
              "oomph-h=h;" + // //$NON-NLS-1$
              "oomph--p=p;" + // //$NON-NLS-1$
              "oomph-P=P;" + // //$NON-NLS-1$
              "oomph-a=a;" + // //$NON-NLS-1$
              "oomph-b=b;" + // //$NON-NLS-1$
              "oomph-text='text';" + // //$NON-NLS-1$
              "oomph-quote='';" + // //$NON-NLS-1$
              "oomph-slash=/;" + // //$NON-NLS-1$
              "oomph-colon=:;" + // //$NON-NLS-1$
              "oomph-r1=[1];" + // //$NON-NLS-1$
              "oomph-r2=[2];" + // //$NON-NLS-1$
              "oomph-r3=[-1];" + // //$NON-NLS-1$
              "oomph-r4=[-3..-1];" + // //$NON-NLS-1$
              "oomph-r5=[-2..]" + // //$NON-NLS-1$
              "" // //$NON-NLS-1$
      );

      inputURI = URI.createURI(
          "https://example.com/gerrit/gitweb/EXAMPLE.git/Src/com.example.releng/example.setup?oomph=b[0..1];oomph_login=b[0]/'login';oomph-p=[2];oomph-f=[3..];a=blob_plain;hb=HEAD"); //$NON-NLS-1$
      new java.net.URI(inputURI.toString());
      HashMap<Object, Object> options = new HashMap<Object, Object>();
      URI uri = transform(inputURI, options);
      System.err.println(">   " + expectedURI); //$NON-NLS-1$
      System.err.println(">>  " + inputURI); //$NON-NLS-1$
      System.err.println(">>> " + uri); //$NON-NLS-1$
      System.err.println(">>>>" + options.get(OPTION_LOGIN_URI)); //$NON-NLS-1$
    }

    private static URI transform(URI uri, Map<Object, Object> options)
    {
      String query = uri.query();
      if (query != null)
      {
        List<String> parameters = StringUtil.explode(query, ";"); //$NON-NLS-1$
        if (parameters.isEmpty())
        {
          return uri;
        }

        Map<String, String> arguments = new LinkedHashMap<String, String>();
        for (String parameter : parameters)
        {
          List<String> assignment = StringUtil.explode(parameter, "="); //$NON-NLS-1$
          if (assignment.size() != 2)
          {
            return uri;
          }

          arguments.put(assignment.get(0), assignment.get(1));
        }

        URI outputURI = uri.trimQuery();
        URI loginURI = null;
        URI formURI = null;
        boolean basicAuthentication = false;
        Map<String, String> outputQuery = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : arguments.entrySet())
        {
          String key = entry.getKey();
          String value = entry.getValue();
          if ("oomph".equals(key)) //$NON-NLS-1$
          {
            String result = evaluate(value, uri);
            if (result != null)
            {
              outputURI = URI.createURI(result);
            }

            continue;
          }
          else if ("oomph_form".equals(key)) //$NON-NLS-1$
          {
            String result = evaluate(value, uri);
            if (result != null)
            {
              formURI = URI.createURI(result);
            }

            continue;
          }
          else if ("oomph_login".equals(key)) //$NON-NLS-1$
          {
            String result = evaluate(value, uri);
            if (result != null)
            {
              loginURI = URI.createURI(result);
            }

            continue;
          }
          else if ("oomph_basic_auth".equals(key)) //$NON-NLS-1$
          {
            basicAuthentication = "true".equals(value); //$NON-NLS-1$
            continue;
          }
          else if (key.startsWith("oomph-")) //$NON-NLS-1$
          {
            String result = evaluate(value, uri);
            if (result != null)
            {
              key = key.substring("oomph-".length()); //$NON-NLS-1$
              value = result;
            }
          }

          outputQuery.put(key, value);
        }

        if (!outputQuery.isEmpty())
        {
          StringBuilder queryResult = new StringBuilder();
          for (Map.Entry<String, String> entry : outputQuery.entrySet())
          {
            if (queryResult.length() != 0)
            {
              queryResult.append(';');
            }

            queryResult.append(entry.getKey()).append('=').append(entry.getValue());
          }

          outputURI = outputURI.appendQuery(queryResult.toString());
        }

        if (loginURI != null && options != null)
        {
          options.put(OPTION_LOGIN_URI, loginURI);
        }

        if (formURI != null && options != null)
        {
          options.put(OPTION_FORM_URI, formURI);
        }

        if (basicAuthentication && options != null)
        {
          options.put(OPTION_BASIC_AUTHENTICATION, Boolean.TRUE);
        }

        return outputURI;
      }

      return uri;
    }

    //
    // s -> URI.scheme
    // S -> URIscheme == null ? "" : URI.scheme ":"
    // u -> URI.userInfo
    // U -> URI.userInfo == null ? "" : URI.userInfo "@"
    // h -> URI.host
    // p -> URI.port
    // P -> URI.port == null ? "" : ":" URI.port
    // a -> URI.authority
    // b -> URI.scheme ( URI.isHierarchicial ? "//:" URI.authority "/" : ":")
    // o -> URI.opaqePart
    // [n{..{m}}] -> URI.segment(n) { m is present ? "/" URI.segment(m) : URI.segments.lastSegment} ** negative n or m -> URI.segmentCount + n
    // 'text' -> "text"
    // '' -> "'"
    // / -> "/"
    // : -> ":"
    // * fail
    //
    // ^((:?[^\r\n]*(?!property=value)(\r?\n))+)$
    //
    private static String evaluate(String expression, URI uri)
    {
      StringBuilder result = new StringBuilder();
      boolean quote = false;
      int segmentCount = uri.segmentCount();
      for (int i = 0, length = expression.length(); i < length; ++i)
      {
        char character = expression.charAt(i);
        if (quote && character != '\'' && (character != '%' || i + 2 > length || expression.charAt(i + 1) != '2' || expression.charAt(i + 2) != '7')
            && (character != '%' || i + 2 > length || expression.charAt(i + 1) != '2' || expression.charAt(i + 2) != '3'))
        {
          result.append(character);
        }
        else
        {
          switch (character)
          {
            case '\'':
            {
              if (i + 1 < length && expression.charAt(i + 1) == '\'')
              {
                ++i;
                result.append('\'');
              }
              else
              {
                quote = !quote;
              }
              break;
            }
            case '%':
            {
              if (i + 2 < length && expression.charAt(i + 1) == '2' && expression.charAt(i + 2) == '7')
              {
                if (i + 5 < length && expression.charAt(i + 3) == '%' && expression.charAt(i + 4) == '2' && expression.charAt(i + 5) == '7')
                {
                  i += 5;
                  result.append("%27"); //$NON-NLS-1$
                }
                else
                {
                  i += 2;
                  quote = !quote;
                }
                break;
              }

              if (i + 2 < length && expression.charAt(i + 1) == '2' && expression.charAt(i + 2) == '3')
              {
                result.append("#"); //$NON-NLS-1$
                i += 2;
                break;
              }

              return null;
            }
            case 's':
            {
              String scheme = uri.scheme();
              if (scheme == null)
              {
                return null;
              }
              result.append(scheme);
              break;
            }
            case 'S':
            {
              String scheme = uri.scheme();
              if (scheme != null)
              {
                result.append(scheme).append(':');
              }
              break;
            }
            case 'u':
            {
              String userInfo = uri.userInfo();
              if (userInfo == null)
              {
                return null;
              }
              result.append(userInfo);
              break;
            }
            case 'U':
            {
              String userInfo = uri.userInfo();
              if (userInfo != null)
              {
                result.append(userInfo).append('@');
              }
              break;
            }
            case 'h':
            {
              String host = uri.host();
              if (host == null)
              {
                return null;
              }

              result.append(host);
              break;
            }
            case 'p':
            {
              String port = uri.port();
              if (port == null)
              {
                return null;
              }
              result.append(port);
              break;
            }
            case 'P':
            {
              String port = uri.port();
              if (port != null)
              {
                result.append(':').append(port);
              }
              break;
            }
            case 'a':
            {
              String authority = uri.authority();
              if (authority == null)
              {
                return null;
              }
              result.append(authority);
              break;
            }
            case 'b':
            {
              String base = uri.isHierarchical() ? uri.trimSegments(segmentCount).trimQuery().trimFragment().toString() : uri.scheme() + ":"; //$NON-NLS-1$
              result.append(base);
              break;
            }
            case 'o':
            {
              String opaquePart = uri.opaquePart();
              if (opaquePart == null)
              {
                return null;
              }
              result.append(opaquePart);
              break;
            }
            case '/':
            case ':':
            {
              result.append(character);
              break;
            }
            case '[':
            {
              if (++i >= length)
              {
                return null;
              }

              character = expression.charAt(i);
              boolean negativeN = false;
              if (character == '-')
              {
                negativeN = true;
                if (++i >= length)
                {
                  return null;
                }
                character = expression.charAt(i);
              }

              if (character < '0' && character > '9')
              {
                return null;
              }

              int n = 0;
              for (;;)
              {
                if (character >= '0' && character <= '9')
                {
                  n = 10 * n + character - '0';
                  if (++i >= length)
                  {
                    return null;
                  }
                  character = expression.charAt(i);
                }
                else
                {
                  break;
                }
              }

              int m;
              boolean negativeM = false;
              if (character == '.')
              {
                if (++i >= length)
                {
                  return null;
                }

                character = expression.charAt(i);
                if (character != '.')
                {
                  return null;
                }

                if (++i >= length)
                {
                  return null;
                }

                character = expression.charAt(i);
                if (character == '-')
                {
                  m = 0;
                  negativeM = true;
                  if (++i >= length)
                  {
                    return null;
                  }

                  character = expression.charAt(i);
                  if (character < '0' && character > '9')
                  {
                    return null;
                  }
                }
                else if (character >= '0' && character <= '9')
                {
                  m = 0;
                }
                else
                {
                  m = 1;
                  negativeM = true;
                }

                for (;;)
                {
                  if (character >= '0' && character <= '9')
                  {
                    if (m == Integer.MIN_VALUE)
                    {
                      m = character - '0';
                    }
                    else
                    {
                      m = 10 * m + character - '0';
                    }

                    if (++i >= length)
                    {
                      return null;
                    }

                    character = expression.charAt(i);
                  }
                  else
                  {
                    break;
                  }
                }
              }
              else
              {
                m = n;
                negativeM = negativeN;
              }

              if (character != ']')
              {
                return null;
              }

              if (negativeN)
              {
                n = segmentCount - n;
              }

              if (negativeM)
              {
                m = segmentCount - m;
              }

              if (m == Integer.MIN_VALUE)
              {
                m = n;
              }
              else if (n >= segmentCount || m >= segmentCount || n > m)
              {
                return null;
              }

              for (int j = n; j <= m; ++j)
              {
                if (j != n)
                {
                  result.append('/');
                }

                result.append(uri.segment(j));
              }

              break;
            }
            default:
            {
              return null;
            }
          }
        }
      }

      if (quote)
      {
        return null;
      }

      return result.toString();
    }
  }

  public static URI transform(URI uri, Map<Object, Object> options)
  {
    return Main.transform(uri, options);
  }

  public static void saveProxies()
  {
    if (CommonPlugin.IS_ECLIPSE_RUNNING)
    {
      ProxyHelper.saveProxyData();
    }
  }

  /**
   * @author Ed Merks
   *
   * A wrapper class that holds the ECF proxy, the Core Net proxy data, the URI for that proxy, and the original URI being accessed.
   */
  private static class ProxyWrapper
  {
    private Proxy proxy;

    private final IProxyData proxyData;

    private final java.net.URI uri;

    private final URI proxyURI;

    private boolean authorized;

    public ProxyWrapper(Proxy proxy, IProxyData proxyData, java.net.URI uri)
    {
      this.proxy = proxy;
      this.proxyData = proxyData;
      this.uri = uri;

      if (proxy != null)
      {
        ProxyAddress address = proxy.getAddress();
        String hostName = address.getHostName();
        int port = address.getPort();
        Proxy.Type type = proxy.getType();
        proxyURI = URI.createURI(type.toString() + "://" + hostName + ":" + port); //$NON-NLS-1$ //$NON-NLS-2$
      }
      else
      {
        proxyURI = null;
      }
    }

    /**
     * Updates the wrapped proxy with the credentials from the authorization.
     */
    public void authorize(Authorization proxyAuthorization)
    {
      authorized = true;
      proxy = new Proxy(proxy.getType(), proxy.getAddress(), proxyAuthorization.getUser(), proxyAuthorization.getPassword());
    }

    /**
     * Creates a wrapper for the URI being accessed, optionally with proxy information, if it's needed and available.
     */
    public static ProxyWrapper create(URI uri)
    {
      java.net.URI javaNetURI = IOUtil.newURI(uri.toString());
      if (CommonPlugin.IS_ECLIPSE_RUNNING)
      {
        ProxyWrapper proxyWrapper = ProxyHelper.createProxyWrapper(javaNetURI);
        if (proxyWrapper != null)
        {
          return proxyWrapper;
        }
      }

      return new ProxyWrapper(null, null, javaNetURI);
    }

    /**
     * Returns whether this wrapper includes proxy information.
     */
    public boolean isProxified()
    {
      return proxy != null;
    }

    /**
     * Returns whether this wrapper includes proxy information that currently includes credentials.
     */
    public boolean hasAuthorization()
    {
      return proxy != null && !StringUtil.isEmpty(proxy.getPassword());
    }

    /**
     * Returns the wrapped ECF proxy.
     */
    public Proxy getProxy()
    {
      return proxy;
    }

    /**
     * Returns the URI that can be used to prompt for the proxy's credentials.
     */
    public URI getProxyURI()
    {
      return proxyURI;
    }

    /**
     * Returns the URI being processed.
     */
    private java.net.URI getURI()
    {
      return uri;
    }

    /**
     * Updates the Core Net proxy data with the credentials of the ECF proxy.
     */
    public void update()
    {
      if (authorized)
      {
        ProxyHelper.update(proxy, proxyData);
      }
    }

    @Override
    public String toString()
    {
      if (proxyData == null)
      {
        return "Unproxified"; //$NON-NLS-1$
      }

      // This is used for logging. We don't want to show the unobscured password in the log so we don't just use proxyData.toString.
      StringBuilder stringBuffer = new StringBuilder();
      stringBuffer.append("type: "); //$NON-NLS-1$
      stringBuffer.append(proxyData.getType());
      stringBuffer.append(" host: "); //$NON-NLS-1$
      stringBuffer.append(proxyData.getHost());
      stringBuffer.append(" port: "); //$NON-NLS-1$
      stringBuffer.append(proxyData.getPort());
      stringBuffer.append(" user: "); //$NON-NLS-1$
      stringBuffer.append(proxyData.getUserId());
      stringBuffer.append(" password: "); //$NON-NLS-1$
      String password = proxyData.getPassword();
      stringBuffer.append(password == null ? "" : XMLTypeFactory.eINSTANCE.convertBase64Binary(password.getBytes())); //$NON-NLS-1$
      stringBuffer.append(" reqAuth: "); //$NON-NLS-1$
      stringBuffer.append(proxyData.isRequiresAuthentication());
      return stringBuffer.toString();
    }
  }

  /**
   * @author Ed Merks
   *
   * This helper class is used only when Eclipse is running in which case the Core Net bundle should be available.
   */
  protected static class ProxyHelper
  {
    private static final Set<IProxyData> PROXY_DATA = new HashSet<IProxyData>();

    @SuppressWarnings("restriction")
    private static final IProxyService PROXY_MANAGER = org.eclipse.core.internal.net.ProxyManager.getProxyManager();

    /**
     * Creates a wrapper using ECF and the Core Net infrastructure.
     */
    public static ProxyWrapper createProxyWrapper(java.net.URI uri)
    {
      if (PROXY_MANAGER.isProxiesEnabled())
      {
        synchronized (PROXY_MANAGER)
        {
          // This replicates the logic in ProxySetupHelper.getProxy so that we can also record the proxy data from which the proxy is created.
          final IProxyData[] proxies = PROXY_MANAGER.select(uri);
          IProxyData selectedProxy = ProxySetupHelper.selectProxyFromProxies(uri.getScheme(), proxies);
          if (selectedProxy != null)
          {
            Proxy proxy = new Proxy(selectedProxy.getType().equalsIgnoreCase(IProxyData.SOCKS_PROXY_TYPE) ? Proxy.Type.SOCKS : Proxy.Type.HTTP,
                new ProxyAddress(selectedProxy.getHost(), selectedProxy.getPort()), selectedProxy.getUserId(), selectedProxy.getPassword());
            return new ProxyWrapper(proxy, selectedProxy, uri);
          }
        }
      }

      return null;
    }

    /**
     * Updates the proxy data with the credentials of the ECF proxy.
     */
    protected static void update(Proxy proxy, IProxyData proxyData)
    {
      if (SetupUtil.INSTALLER_APPLICATION || SetupUtil.SETUP_ARCHIVER_APPLICATION)
      {
        synchronized (PROXY_MANAGER)
        {
          proxyData.setUserid(proxy.getUsername());
          proxyData.setPassword(proxy.getPassword());
          PROXY_DATA.add(proxyData);
        }
      }
    }

    /**
     * Saves all the proxy data instances that have been used.
     * This is useful when the system proxies require authentication and the password has been prompted from the user.
     * In this case, we can switch the mode to Manual and save the proxies with their credentials so subsequent uses of the installer can reuse those saved credentials.
     */
    protected static void saveProxyData()
    {
      if (!PROXY_DATA.isEmpty())
      {
        synchronized (PROXY_MANAGER)
        {
          try
          {
            PROXY_MANAGER.setProxyData(PROXY_DATA.toArray(new IProxyData[PROXY_DATA.size()]));
            PROXY_MANAGER.setSystemProxiesEnabled(false);

            // This forces the preferences to be flushed.
            PROXY_MANAGER.setProxiesEnabled(false);
            PROXY_MANAGER.setProxiesEnabled(true);
          }
          catch (CoreException ex)
          {
            // Ignore.
          }
        }

        PROXY_DATA.clear();
      }
    }
  }

  /**
   *
   * @author Ed Merks
   */
  public static class FormHandler
  {
    private static final Map<URI, Authorization> AUTHORIZED_FORMS = Collections.synchronizedMap(new HashMap<URI, Authorization>());

    private static final Pattern FORM_PATTERN = Pattern.compile("<\\s*form\\s*([^>]*)>(.*?)</\\s*form\\s*>", Pattern.DOTALL); //$NON-NLS-1$

    private static final Pattern INPUT_ELEMENT_PATTERN = Pattern.compile("<\\s*input\\s*([^>]*)>"); //$NON-NLS-1$

    private static final Pattern ACTION_ATTRIBUTE_PATTERN = getAttributePattern("action"); //$NON-NLS-1$

    private static final Pattern ID_ATTRIBUTE_PATTERN = getAttributePattern("id"); //$NON-NLS-1$

    private static final Pattern NAME_ATTRIBUTE_PATTERN = getAttributePattern("name"); //$NON-NLS-1$

    private static final Pattern TYPE_ATTRIBUTE_PATTERN = getAttributePattern("type"); //$NON-NLS-1$

    private static final Pattern VALUE_ATTRIBUTE_PATTERN = getAttributePattern("value"); //$NON-NLS-1$

    private static final Pattern HEX_ENTITY_PATTERN = Pattern.compile("&#x([0-9]+);"); //$NON-NLS-1$

    private final URI formURI;

    private final URIConverter uriConverter;

    private final Map<String, String> parameterTypes = new LinkedHashMap<String, String>();

    private final Map<String, String> parameterValues = new LinkedHashMap<String, String>();

    private final Set<String> secureParameters = new LinkedHashSet<String>();

    private final AuthorizationHandler authorizationHandler;

    public FormHandler(URI formURI, URIConverter uriConverter, AuthorizationHandler authorizationHandler)
    {
      this.formURI = formURI;
      this.uriConverter = uriConverter;
      this.authorizationHandler = authorizationHandler;
    }

    public boolean process() throws IOException
    {
      boolean result = false;
      if (authorizationHandler != null)
      {
        Authorization authorization = authorizationHandler.authorize(formURI);
        URI formLockURI = URI.createURI("form:" + formURI); //$NON-NLS-1$
        CountDownLatch countDownLatch = acquireLock(formLockURI);
        try
        {
          // If we've previously successfully authorized with the same credentials...
          if (authorization.equals(AUTHORIZED_FORMS.get(formLockURI)))
          {
            if (!isRedo())
            {
              // Return here so that we don't needlessly put the same authorization back the map.
              return true;
            }

            java.net.URI javaNetFormURI;
            try
            {
              javaNetFormURI = new java.net.URI(formURI.toString());
            }
            catch (URISyntaxException ex)
            {
              throw new IOExceptionWithCause(ex);
            }

            for (HttpCookie httpCookie : COOKIE_STORE.get(javaNetFormURI))
            {
              COOKIE_STORE.remove(javaNetFormURI, httpCookie);
            }
          }

          URI formActionURI = readForm(formURI);

          if (!secureParameters.isEmpty() && formActionURI != null)
          {
            int limit = 3;
            if (!authorization.isAuthorized())
            {
              --limit;
              authorization = authorizationHandler.reauthorize(formURI, authorization);
            }

            Set<String> emptyKeys = new HashSet<String>();
            for (int i = 0; i < limit && !authorization.isUnauthorizeable(); ++i)
            {
              if (authorization.isAuthorized())
              {
                boolean consumedUser = false;
                for (Map.Entry<String, String> entry : parameterValues.entrySet())
                {
                  String key = entry.getKey();
                  String value = entry.getValue();
                  if (StringUtil.isEmpty(value) || emptyKeys.contains(key))
                  {
                    String type = parameterTypes.get(key);
                    if ("password".equals(type)) //$NON-NLS-1$
                    {
                      emptyKeys.add(key);
                      parameterValues.put(key, PreferencesUtil.encrypt(authorization.getPassword()));
                    }
                    else if (!consumedUser && "text".equals(type)) //$NON-NLS-1$
                    {
                      emptyKeys.add(key);
                      parameterValues.put(key, authorization.getUser());
                      consumedUser = true;
                    }
                  }
                }

                if (postForm(formActionURI))
                {
                  result = true;
                  break;
                }

                authorization = authorizationHandler.reauthorize(formURI, authorization);
              }
            }
          }
        }
        finally
        {
          if (result)
          {
            AUTHORIZED_FORMS.put(formLockURI, authorization);
          }

          releaseLock(formLockURI, countDownLatch);
        }
      }

      return result;
    }

    protected boolean isRedo()
    {
      return false;
    }

    private URI readForm(URI formURI) throws IOException
    {
      InputStream in = null;
      ByteArrayOutputStream out = null;
      try
      {
        String tracePrefix = NLS.bind(Messages.ECFURIHandlerImpl_ECFPrefix_message, formURI);
        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_ReadingForm_message);
        }

        // Ensure that the input stream really reads the remote form, not something from the cache.
        // This is to ensure that the cookies of the response header are properly processed.
        HashMap<Object, Object> options = new HashMap<Object, Object>();
        options.put(OPTION_CACHE_HANDLING, CacheHandling.CACHE_IGNORE);
        options.put(OPTION_AUTHORIZATION_HANDLER, null);
        in = uriConverter.createInputStream(formURI, options);

        // Copy the result into memory and convert it to string.
        out = new ByteArrayOutputStream();
        IOUtil.copy(in, out);
        String contents = out.toString("UTF-8"); //$NON-NLS-1$

        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_FindingFormContents_message);
        }

        String formID = formURI.fragment();

        // Look for the form.
        for (Matcher formMatcher = FORM_PATTERN.matcher(contents); formMatcher.find();)
        {
          parameterValues.clear();
          parameterTypes.clear();

          String formElement = formMatcher.group(1);
          String id = getAttributeValue(ID_ATTRIBUTE_PATTERN, formElement);
          if (formID != null && !formID.equals(id))
          {
            continue;
          }

          // The form must have an action attribute.
          String action = getAttributeValue(ACTION_ATTRIBUTE_PATTERN, formElement);
          if (action != null)
          {
            // This is the URI to which we must post the form data.
            URI formActionURI = URI.createURI(action).resolve(formURI);
            if (formActionURI != null)
            {
              String formContents = formMatcher.group(2);

              // Look for all the input elements of the form.
              for (Matcher inputMatcher = INPUT_ELEMENT_PATTERN.matcher(formContents); inputMatcher.find();)
              {
                String inputElement = inputMatcher.group(1);

                String name = getAttributeValue(NAME_ATTRIBUTE_PATTERN, inputElement);
                String type = getAttributeValue(TYPE_ATTRIBUTE_PATTERN, inputElement);
                String value = getAttributeValue(VALUE_ATTRIBUTE_PATTERN, inputElement);

                // Record the values of all well-formed inputs.
                if (name != null && type != null)
                {
                  parameterValues.put(name, value == null ? "" : value); //$NON-NLS-1$
                  parameterTypes.put(name, type);
                  if ("password".equals(type)) //$NON-NLS-1$
                  {
                    secureParameters.add(name);
                  }
                }
              }
            }

            if (!secureParameters.isEmpty())
            {
              if (TRACE)
              {
                StringBuilder details = new StringBuilder();
                for (Map.Entry<String, String> entry : parameterValues.entrySet())
                {
                  details.append(StringUtil.NL).append("   ").append(entry.getKey()).append("='").append(entry.getValue()).append("' type=") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                      .append(parameterTypes.get(entry.getKey()));
                }

                System.out.println(tracePrefix + " form parameters " + details); //$NON-NLS-1$
              }
              return formActionURI;
            }
          }
        }

        if (TRACE)
        {
          System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_FormContentsNotFound_message);
        }

        return null;
      }
      finally
      {
        IOUtil.closeSilent(in);
      }
    }

    private boolean postForm(URI formActionURI) throws IOException
    {
      boolean result = false;
      URL url = new URL(formActionURI.toString());

      String tracePrefix = NLS.bind(Messages.ECFURIHandlerImpl_ECFPrefix_message, formActionURI);
      if (TRACE)
      {
        System.out.println(tracePrefix + Messages.ECFURIHandlerImpl_PostingForm_message);
      }

      // Establish a connection that looks just like a browser connection to post the form data.
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();

      connection.setUseCaches(false);
      connection.setConnectTimeout(5000);
      connection.setRequestMethod("POST"); //$NON-NLS-1$
      connection.setRequestProperty("User-Agent", "Mozilla/5.0"); //$NON-NLS-1$ //$NON-NLS-2$
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);

      java.net.URI uri;
      try
      {
        uri = url.toURI();
      }
      catch (URISyntaxException ex)
      {
        throw new IOExceptionWithCause(ex);
      }

      // Be sure to add cookies, e.g., any cookies returned when the form was read earlier.
      List<HttpCookie> cookies = COOKIE_STORE.get(uri);
      for (HttpCookie httpCookie : cookies)
      {
        connection.addRequestProperty("Cookie", httpCookie.toString().replace("\"", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }

      if (TRACE)
      {
        StringBuilder details = new StringBuilder();
        for (HttpCookie httpCookie : cookies)
        {
          details.append(StringUtil.NL).append("    ").append(httpCookie); //$NON-NLS-1$
        }

        System.out.println(tracePrefix
            + (details.length() == 0 ? Messages.ECFURIHandlerImpl_UsingNoCookies_message : Messages.ECFURIHandlerImpl_UsingCookies_message + details));
      }

      handleProxy(connection);

      String form = getForm(parameterValues, secureParameters, true);

      // Write the form data to connection.
      DataOutputStream data = new DataOutputStream(connection.getOutputStream());

      if (TRACE)
      {
        System.out.println(tracePrefix + " posting parameters" + StringUtil.NL + "    " + getForm(parameterValues, secureParameters, false)); //$NON-NLS-1$ //$NON-NLS-2$
      }

      data.writeBytes(form);
      data.close();

      // If we receive a valid response...
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_MOVED_TEMP)
      {
        // Look for the cookies...
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> list = headerFields.get("Set-Cookie"); //$NON-NLS-1$
        if (list != null)
        {
          for (String value : list)
          {
            List<HttpCookie> httpCookies = HttpCookie.parse(value);
            for (HttpCookie httpCookie : httpCookies)
            {
              httpCookie.setDomain(getHost(uri));

              // Some sites serve up bogus expiry information that results in the cookie not being added to the store.
              if (httpCookie.getMaxAge() == 0)
              {
                httpCookie.setMaxAge(-1);
              }

              ECFURIHandlerImpl.COOKIE_STORE.add(uri, httpCookie);
              result = true;
            }
          }
        }

        // Allow subclasses to also inspect the header fields.
        if (result)
        {
          result = validHeaders(headerFields);
        }
      }

      return result;
    }

    protected boolean validHeaders(Map<String, List<String>> headers)
    {
      return true;
    }

    private static String getForm(Map<String, String> parameters, Collection<String> secureKeys, boolean secure)
    {
      StringBuilder form = new StringBuilder();
      for (Map.Entry<String, String> entry : parameters.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();
        if (secure && secureKeys != null && secureKeys.contains(key))
        {
          value = PreferencesUtil.decrypt(value);
        }

        if (form.length() != 0)
        {
          form.append('&');
        }

        form.append(key);
        form.append('=');
        try
        {
          form.append(URLEncoder.encode(value, "UTF-8")); //$NON-NLS-1$
        }
        catch (UnsupportedEncodingException ex)
        {
          // UTF-8 is always supported.
        }
      }

      return form.toString();
    }

    private void handleProxy(HttpURLConnection connection)
    {
      try
      {
        // If there are proxy settings, ensure that the proper proxy authorization is established.
        Proxy proxy = ProxySetupHelper.getProxy(formURI.toString());
        if (proxy != null)
        {
          Authorization authorization = new ECFURIHandlerImpl.AuthorizationHandler.Authorization(proxy.getUsername(), proxy.getPassword());
          if (authorization.isAuthorized())
          {
            connection.setRequestProperty("Proxy-Authorization", authorization.getAuthorization()); //$NON-NLS-1$
          }
        }
      }
      catch (NoClassDefFoundError ex)
      {
        // Ignore.
      }
    }

    private static Pattern getAttributePattern(String attributeName)
    {
      return Pattern.compile("\\s*" + attributeName + "\\s*=\\s*(\"[^\"]*\"|'[^']*')"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String getAttributeValue(Pattern attributePattern, String attributes)
    {
      Matcher matcher = attributePattern.matcher(attributes);
      if (matcher.find())
      {
        String quotedValue = matcher.group(1);
        for (Matcher entityMatcher = HEX_ENTITY_PATTERN.matcher(quotedValue); entityMatcher.find();)
        {
          quotedValue = quotedValue.replace(entityMatcher.group(), new String(Character.toChars(Integer.valueOf(entityMatcher.group(1), 16))));
        }
        return quotedValue.substring(1, quotedValue.length() - 1);
      }
      return null;
    }
  }
}

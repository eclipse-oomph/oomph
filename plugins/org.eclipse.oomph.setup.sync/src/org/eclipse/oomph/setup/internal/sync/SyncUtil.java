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

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.internal.net.ProxyManager;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public final class SyncUtil
{
  private static final IProxyService PROXY_MANAGER = getProxyManager();

  private SyncUtil()
  {
  }

  private static final String PROP_HTTP_AUTH_NTLM_DOMAIN = "http.auth.ntlm.domain";

  private static final String ENV_USER_DOMAIN = "USERDOMAIN";

  private static final String DOUBLE_BACKSLASH = "\\\\";

  public static ResourceSet createResourceSet()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new BaseResourceFactoryImpl());
    return resourceSet;
  }

  public static void inititalizeFile(File file, EClass eClass, ResourceSet resourceSet)
  {
    if (file.length() == 0)
    {
      EObject rootObject = EcoreUtil.create(eClass);

      Resource resource = resourceSet.createResource(org.eclipse.emf.common.util.URI.createFileURI(file.getAbsolutePath()));
      resource.getContents().add(rootObject);
      BaseUtil.saveEObject(rootObject);
    }
  }

  public static String getDigest(File file) throws IOException
  {
    if (!file.exists())
    {
      return null;
    }

    FileInputStream contents = null;

    try
    {
      contents = new FileInputStream(file);
      return HexUtil.bytesToHex(IOUtil.getSHA1(contents));
    }
    catch (IOException ex)
    {
      throw ex;
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new IOExceptionWithCause(ex);
    }
    finally
    {
      IOUtil.closeSilent(contents);
    }
  }

  public static void deleteFile(File file) throws IOException
  {
    if (file.isFile())
    {
      if (!file.delete())
      {
        throw new IOException("Could not delete file " + file);
      }
    }
  }

  public static HttpHost getProxyHost(URI uri)
  {
    IProxyData proxy = getProxyData(uri);
    if (proxy != null)
    {
      return new HttpHost(proxy.getHost(), proxy.getPort());
    }

    return null;
  }

  public static Executor proxyAuthentication(Executor executor, URI uri) throws IOException
  {
    IProxyData proxy = getProxyData(uri);
    if (proxy != null)
    {
      HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
      String proxyUserID = proxy.getUserId();
      if (proxyUserID != null)
      {
        String userID = getUserName(proxyUserID);
        String password = proxy.getPassword();
        String workstation = getWorkstation();
        String domain = getUserDomain(proxyUserID);
        return executor.auth(new AuthScope(proxyHost, AuthScope.ANY_REALM, "ntlm"), new NTCredentials(userID, password, workstation, domain))
            .auth(new AuthScope(proxyHost, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME), new UsernamePasswordCredentials(userID, password));
      }
    }

    return executor;
  }

  private static String getWorkstation()
  {
    try
    {
      return InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e)
    {
      return null;
    }
  }

  private static String getUserDomain(String userName)
  {
    String domain = System.getProperty(PROP_HTTP_AUTH_NTLM_DOMAIN);
    if (domain != null)
    {
      return domain;
    }

    domain = System.getenv(ENV_USER_DOMAIN);
    if (domain != null)
    {
      return domain;
    }

    if (userName != null)
    {
      int pos = userName.indexOf(DOUBLE_BACKSLASH);
      if (pos != -1)
      {
        return userName.substring(0, pos);
      }
    }

    return null;
  }

  private static String getUserName(String userName)
  {
    if (userName != null)
    {
      int pos = userName.indexOf(DOUBLE_BACKSLASH);
      if (pos != -1)
      {
        return userName.substring(pos + DOUBLE_BACKSLASH.length());
      }
    }

    return userName;
  }

  // public static String getProxyUser(URI uri)
  // {
  // IProxyData proxy = getProxyData(uri);
  // if (proxy != null)
  // {
  // String userID = proxy.getUserId();
  // if (userID != null)
  // {
  // return getUserName(userID);
  // }
  // }
  //
  // return null;
  // }
  //
  // public static String getProxyPassword(URI uri)
  // {
  // IProxyData proxy = getProxyData(uri);
  // if (proxy != null)
  // {
  // String userID = proxy.getUserId();
  // if (userID != null)
  // {
  // return proxy.getPassword();
  // }
  // }
  //
  // return null;
  // }

  private static IProxyData getProxyData(URI uri)
  {
    if (PROXY_MANAGER != null)
    {
      IProxyData[] proxies = PROXY_MANAGER.select(uri);
      if (proxies.length != 0)
      {
        return proxies[0];
      }
    }

    return null;
  }

  private static IProxyService getProxyManager()
  {
    try
    {
      if (CommonPlugin.IS_ECLIPSE_RUNNING)
      {
        return ProxyManager.getProxyManager();
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }
}

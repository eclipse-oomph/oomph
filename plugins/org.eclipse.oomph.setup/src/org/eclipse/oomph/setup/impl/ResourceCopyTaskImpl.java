/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.ResourceCopyTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ZIPUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Objects;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Copy Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl#isForce <em>Force</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl#getSourceURL <em>Source URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl#getTargetURL <em>Target URL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceCopyTaskImpl extends SetupTaskImpl implements ResourceCopyTask
{
  public static final String OPTION_ZIP_CACHE = "OPTION_ZIP_CACHE"; //$NON-NLS-1$

  /**
   * The default value of the '{@link #isForce() <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForce()
   * @generated
   * @ordered
   */
  protected static final boolean FORCE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isForce() <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForce()
   * @generated
   * @ordered
   */
  protected boolean force = FORCE_EDEFAULT;

  /**
   * The default value of the '{@link #getSourceURL() <em>Source URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceURL()
   * @generated
   * @ordered
   */
  protected static final String SOURCE_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSourceURL() <em>Source URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceURL()
   * @generated
   * @ordered
   */
  protected String sourceURL = SOURCE_URL_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected static final String TARGET_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected String targetURL = TARGET_URL_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ResourceCopyTaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SetupPackage.Literals.RESOURCE_COPY_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isForce()
  {
    return force;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setForce(boolean newForce)
  {
    boolean oldForce = force;
    force = newForce;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_COPY_TASK__FORCE, oldForce, force));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getSourceURL()
  {
    return sourceURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSourceURL(String newSourceURL)
  {
    String oldSourceURL = sourceURL;
    sourceURL = newSourceURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_COPY_TASK__SOURCE_URL, oldSourceURL, sourceURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getTargetURL()
  {
    return targetURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTargetURL(String newTargetURL)
  {
    String oldTargetURL = targetURL;
    targetURL = newTargetURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_COPY_TASK__TARGET_URL, oldTargetURL, targetURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SetupPackage.RESOURCE_COPY_TASK__FORCE:
        return isForce();
      case SetupPackage.RESOURCE_COPY_TASK__SOURCE_URL:
        return getSourceURL();
      case SetupPackage.RESOURCE_COPY_TASK__TARGET_URL:
        return getTargetURL();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.RESOURCE_COPY_TASK__FORCE:
        setForce((Boolean)newValue);
        return;
      case SetupPackage.RESOURCE_COPY_TASK__SOURCE_URL:
        setSourceURL((String)newValue);
        return;
      case SetupPackage.RESOURCE_COPY_TASK__TARGET_URL:
        setTargetURL((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.RESOURCE_COPY_TASK__FORCE:
        setForce(FORCE_EDEFAULT);
        return;
      case SetupPackage.RESOURCE_COPY_TASK__SOURCE_URL:
        setSourceURL(SOURCE_URL_EDEFAULT);
        return;
      case SetupPackage.RESOURCE_COPY_TASK__TARGET_URL:
        setTargetURL(TARGET_URL_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.RESOURCE_COPY_TASK__FORCE:
        return force != FORCE_EDEFAULT;
      case SetupPackage.RESOURCE_COPY_TASK__SOURCE_URL:
        return SOURCE_URL_EDEFAULT == null ? sourceURL != null : !SOURCE_URL_EDEFAULT.equals(sourceURL);
      case SetupPackage.RESOURCE_COPY_TASK__TARGET_URL:
        return TARGET_URL_EDEFAULT == null ? targetURL != null : !TARGET_URL_EDEFAULT.equals(targetURL);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (force: "); //$NON-NLS-1$
    result.append(force);
    result.append(", sourceURL: "); //$NON-NLS-1$
    result.append(sourceURL);
    result.append(", targetURL: "); //$NON-NLS-1$
    result.append(targetURL);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 2;
  }

  protected boolean isFile(URI uri)
  {
    if (uri.isFile())
    {
      return true;
    }

    URI archiveURI = getArchiveURI(uri);
    return archiveURI != null && archiveURI.isFile();
  }

  protected URI getArchiveURI(URI uri)
  {
    if (uri.isArchive())
    {
      String authority = uri.authority();
      if (authority != null && authority.endsWith("!")) //$NON-NLS-1$
      {
        URI archiveURI = URI.createURI(authority.substring(0, authority.length() - 1));
        archiveURI.appendQuery(uri.query());
        return archiveURI;
      }
    }

    return null;
  }

  protected boolean isFolder(URI uri)
  {
    return uri.hasTrailingPathSeparator() || uri.lastSegment() == null;
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    URI sourceURI = createResolvedURI(getSourceURL());
    URI targetURI = createResolvedURI(getTargetURL());

    if (sourceURI == null || targetURI == null)
    {
      return false;
    }

    if (Objects.equals(sourceURI, targetURI))
    {
      return false;
    }

    URIConverter uriConverter = context.getURIConverter();
    if (isFolder(targetURI))
    {
      if (isFolder(sourceURI))
      {
        if (isFile(uriConverter.normalize(targetURI)))
        {
          return isForce() || !uriConverter.exists(targetURI, null);
        }

        return true;
      }

      return isForce() || !uriConverter.exists(targetURI.appendSegment(sourceURI.lastSegment()), null);
    }

    return isForce() || !uriConverter.exists(targetURI, null);
  }

  protected void copy(URIConverter uriConverter, URI sourceURI, URI targetURI) throws IOException
  {
    InputStream input = null;
    OutputStream output = null;

    try
    {
      input = uriConverter.createInputStream(sourceURI, Collections.singletonMap(OPTION_ZIP_CACHE, Boolean.FALSE));
      output = uriConverter.createOutputStream(targetURI, null);
      IOUtil.copy(input, output);
    }
    finally
    {
      IOUtil.closeSilent(input);
      IOUtil.closeSilent(output);
    }
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    URI sourceURI = createResolvedURI(getSourceURL());
    URI targetURI = createResolvedURI(getTargetURL());
    URIConverter uriConverter = context.getURIConverter();

    URI normalizedSourceURI = uriConverter.normalize(sourceURI);
    URI normalizedTargetURI = uriConverter.normalize(targetURI);
    if (isFolder(targetURI))
    {
      if (isFolder(sourceURI))
      {
        if (normalizedSourceURI.isArchive() && isFile(normalizedTargetURI))
        {
          URI archiveURI = getArchiveURI(normalizedSourceURI);
          if (isFile(archiveURI))
          {
            context.log(NLS.bind(Messages.ResourceCopyTaskImpl_UnzippingResource_message, normalizedSourceURI, normalizedTargetURI));
            ZIPUtil.unzip(new File(archiveURI.toFileString()), new File(normalizedTargetURI.toFileString()));
          }
          else
          {
            File tempZipFile = File.createTempFile("archive", "zip"); //$NON-NLS-1$ //$NON-NLS-2$
            context.log(NLS.bind(Messages.ResourceCopyTaskImpl_DownloadingResource_message, uriConverter.normalize(archiveURI), tempZipFile));
            copy(uriConverter, archiveURI, URI.createFileURI(tempZipFile.getAbsolutePath()));

            context.log(NLS.bind(Messages.ResourceCopyTaskImpl_UnzippingTempFile_message, tempZipFile, normalizedTargetURI));
            ZIPUtil.unzip(tempZipFile, new File(normalizedTargetURI.toFileString()));
          }
        }
        else if (isFile(normalizedTargetURI) && isFile(normalizedSourceURI))
        {
          context.log(NLS.bind(Messages.ResourceCopyTaskImpl_CopyingFolder_message, normalizedSourceURI, normalizedTargetURI));
          IOUtil.copyTree(new File(normalizedSourceURI.toFileString()), new File(normalizedTargetURI.toFileString()));
        }
        else
        {
          context.log(NLS.bind(Messages.ResourceCopyTaskImpl_UnsupportedCopyingFolder_message, normalizedSourceURI, normalizedTargetURI));
        }
      }
      else if (uriConverter.exists(sourceURI, null))
      {
        URI targetResourceURI = targetURI.appendSegment(sourceURI.lastSegment());
        context.log(NLS.bind(Messages.ResourceCopyTaskImpl_CopyingResource_message, normalizedSourceURI, uriConverter.normalize(targetResourceURI)));
        copy(uriConverter, sourceURI, targetResourceURI);
      }
    }
    else if (uriConverter.exists(sourceURI, null))
    {
      context.log(NLS.bind(Messages.ResourceCopyTaskImpl_CopyingResource_message, normalizedSourceURI, normalizedTargetURI));
      copy(uriConverter, sourceURI, targetURI);
    }
    else
    {
      context.log(NLS.bind(Messages.ResourceCopyTaskImpl_CannotCopy_message, normalizedSourceURI, normalizedTargetURI));
    }
  }
} // ResourceCopyTaskImpl

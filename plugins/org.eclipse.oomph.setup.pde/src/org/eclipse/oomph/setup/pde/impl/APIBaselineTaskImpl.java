/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.setup.pde.APIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.util.DownloadUtil;
import org.eclipse.oomph.setup.util.FileUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.ZIPUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.pde.api.tools.internal.ApiBaselineManager;
import org.eclipse.pde.api.tools.internal.model.ApiModelFactory;
import org.eclipse.pde.api.tools.internal.provisional.ApiPlugin;
import org.eclipse.pde.api.tools.internal.provisional.IApiBaselineManager;
import org.eclipse.pde.api.tools.internal.provisional.model.IApiBaseline;

import java.io.File;
import java.io.InputStream;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Api Baseline Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getRemoteURI <em>Remote URI</em>}</li>
 * </ul>
 *
 * @generated
 */
public class APIBaselineTaskImpl extends AbstractAPIBaselineTaskImpl implements APIBaselineTask
{
  /**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected static final String VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected String version = VERSION_EDEFAULT;

  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

  /**
   * The default value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURI()
   * @generated
   * @ordered
   */
  protected static final String REMOTE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURI()
   * @generated
   * @ordered
   */
  protected String remoteURI = REMOTE_URI_EDEFAULT;

  private transient File remoteURIFile;

  private transient String baselineName;

  private transient File baselineDir;

  private transient IApiBaseline baseline;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected APIBaselineTaskImpl()
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
    return PDEPackage.Literals.API_BASELINE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVersion(String newVersion)
  {
    String oldVersion = version;
    version = newVersion;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_TASK__VERSION, oldVersion, version));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_TASK__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRemoteURI()
  {
    return remoteURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRemoteURI(String newRemoteURI)
  {
    String oldRemoteURI = remoteURI;
    remoteURI = newRemoteURI;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_TASK__REMOTE_URI, oldRemoteURI, remoteURI));
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
      case PDEPackage.API_BASELINE_TASK__VERSION:
        return getVersion();
      case PDEPackage.API_BASELINE_TASK__LOCATION:
        return getLocation();
      case PDEPackage.API_BASELINE_TASK__REMOTE_URI:
        return getRemoteURI();
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
      case PDEPackage.API_BASELINE_TASK__VERSION:
        setVersion((String)newValue);
        return;
      case PDEPackage.API_BASELINE_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case PDEPackage.API_BASELINE_TASK__REMOTE_URI:
        setRemoteURI((String)newValue);
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
      case PDEPackage.API_BASELINE_TASK__VERSION:
        setVersion(VERSION_EDEFAULT);
        return;
      case PDEPackage.API_BASELINE_TASK__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case PDEPackage.API_BASELINE_TASK__REMOTE_URI:
        setRemoteURI(REMOTE_URI_EDEFAULT);
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
      case PDEPackage.API_BASELINE_TASK__VERSION:
        return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
      case PDEPackage.API_BASELINE_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case PDEPackage.API_BASELINE_TASK__REMOTE_URI:
        return REMOTE_URI_EDEFAULT == null ? remoteURI != null : !REMOTE_URI_EDEFAULT.equals(remoteURI);
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

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (version: ");
    result.append(version);
    result.append(", location: ");
    result.append(location);
    result.append(", remoteURI: ");
    result.append(remoteURI);
    result.append(')');
    return result.toString();
  }

  protected transient boolean backupBaseline;

  protected transient boolean createBaseline;

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    ApiPlugin apiPlugin = ApiPlugin.getDefault();
    if (apiPlugin == null)
    {
      // Might be deactivated
      return false;
    }

    baselineName = getName() + "-" + getVersion();
    baselineDir = new File(getLocation());
    remoteURIFile = new File(baselineDir, "remoteURI.txt");

    IApiBaselineManager baselineManager = apiPlugin.getApiBaselineManager();
    IApiBaseline baseline = baselineManager.getApiBaseline(baselineName);
    if (baseline == null)
    {
      return true;
    }

    ((ApiBaselineManager)baselineManager).loadBaselineInfos(baseline);

    if (!baselineDir.isDirectory() || !new File(baseline.getLocation()).equals(baselineDir))
    {
      baselineManager.removeApiBaseline(baselineName);
      baseline.setName(baselineName + " " + System.currentTimeMillis());
      baselineManager.addApiBaseline(baseline);
      return true;
    }

    this.baseline = baseline;

    if (isActivate() && baselineManager.getDefaultApiBaseline() != baseline)
    {
      return true;
    }

    if (isDifferentRemoteURI())
    {
      return true;
    }

    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    IApiBaselineManager baselineManager = ApiPlugin.getDefault().getApiBaselineManager();
    if (baseline == null)
    {
      if (isDifferentRemoteURI())
      {
        FileUtil.delete(baselineDir, new ProgressLogMonitor(context));
      }

      if (!baselineDir.exists())
      {
        downloadAndUnzip(context);
      }

      IOUtil.writeFile(remoteURIFile, getRemoteURI().getBytes("UTF-8"));

      String location = baselineDir.toString();
      context.log("Creating API baseline from " + location);

      baseline = ApiModelFactory.newApiBaseline(baselineName, location);
      ApiModelFactory.addComponents(baseline, location, new ProgressLogMonitor(context));
      baselineManager.addApiBaseline(baseline);
    }

    if (isActivate())
    {
      context.log("Activating API baseline: " + baselineName);
      baselineManager.setDefaultApiBaseline(baselineName);
    }
  }

  private boolean isDifferentRemoteURI() throws Exception
  {
    if (remoteURIFile.exists())
    {
      String zipLocationURL = new String(IOUtil.readFile(remoteURIFile), "UTF-8");
      if (!ObjectUtil.equals(zipLocationURL, getRemoteURI()))
      {
        return true;
      }
    }

    return false;
  }

  private void downloadAndUnzip(final SetupTaskContext context) throws Exception
  {
    File zipFile = DownloadUtil.downloadURL(getRemoteURI(), context);

    baselineDir.mkdirs();
    ZIPUtil.unzip(zipFile, new ZIPUtil.FileSystemUnzipHandler(baselineDir, ZIPUtil.DEFAULT_BUFFER_SIZE)
    {
      @Override
      public void unzipFile(String name, InputStream zipStream)
      {
        context.log("Unzipping " + name);
        super.unzipFile(name, zipStream);
      }
    });
  }

} // ApiBaselineTaskImpl

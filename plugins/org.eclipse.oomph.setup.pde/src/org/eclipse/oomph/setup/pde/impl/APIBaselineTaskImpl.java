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
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
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

import org.eclipse.core.runtime.Path;
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
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getContainerFolder <em>Container Folder</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineTaskImpl#getZipLocation <em>Zip Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class APIBaselineTaskImpl extends SetupTaskImpl implements APIBaselineTask
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
   * The default value of the '{@link #getContainerFolder() <em>Container Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContainerFolder()
   * @generated
   * @ordered
   */
  protected static final String CONTAINER_FOLDER_EDEFAULT = "${setup.project.dir/.baselines}";

  /**
   * The cached value of the '{@link #getContainerFolder() <em>Container Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContainerFolder()
   * @generated
   * @ordered
   */
  protected String containerFolder = CONTAINER_FOLDER_EDEFAULT;

  /**
   * The default value of the '{@link #getZipLocation() <em>Zip Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getZipLocation()
   * @generated
   * @ordered
   */
  protected static final String ZIP_LOCATION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getZipLocation() <em>Zip Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getZipLocation()
   * @generated
   * @ordered
   */
  protected String zipLocation = ZIP_LOCATION_EDEFAULT;

  private transient APIBaselineHelper helper;

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
  public String getContainerFolder()
  {
    return containerFolder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setContainerFolder(String newContainerFolder)
  {
    String oldContainerFolder = containerFolder;
    containerFolder = newContainerFolder;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_TASK__CONTAINER_FOLDER, oldContainerFolder, containerFolder));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getZipLocation()
  {
    return zipLocation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setZipLocation(String newZipLocation)
  {
    String oldZipLocation = zipLocation;
    zipLocation = newZipLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_TASK__ZIP_LOCATION, oldZipLocation, zipLocation));
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
      case PDEPackage.API_BASELINE_TASK__CONTAINER_FOLDER:
        return getContainerFolder();
      case PDEPackage.API_BASELINE_TASK__ZIP_LOCATION:
        return getZipLocation();
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
      case PDEPackage.API_BASELINE_TASK__CONTAINER_FOLDER:
        setContainerFolder((String)newValue);
        return;
      case PDEPackage.API_BASELINE_TASK__ZIP_LOCATION:
        setZipLocation((String)newValue);
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
      case PDEPackage.API_BASELINE_TASK__CONTAINER_FOLDER:
        setContainerFolder(CONTAINER_FOLDER_EDEFAULT);
        return;
      case PDEPackage.API_BASELINE_TASK__ZIP_LOCATION:
        setZipLocation(ZIP_LOCATION_EDEFAULT);
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
      case PDEPackage.API_BASELINE_TASK__CONTAINER_FOLDER:
        return CONTAINER_FOLDER_EDEFAULT == null ? containerFolder != null : !CONTAINER_FOLDER_EDEFAULT.equals(containerFolder);
      case PDEPackage.API_BASELINE_TASK__ZIP_LOCATION:
        return ZIP_LOCATION_EDEFAULT == null ? zipLocation != null : !ZIP_LOCATION_EDEFAULT.equals(zipLocation);
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
    result.append(", containerFolder: ");
    result.append(containerFolder);
    result.append(", zipLocation: ");
    result.append(zipLocation);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return false;
    // String containerFolder = getContainerFolder();
    // String version = getVersion();
    // String zipLocation = context.redirect(getZipLocation());
    //
    // helper = new APIBaselineHelperImpl(containerFolder, version, zipLocation);
    // return helper.isNeeded(context);
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    helper.perform(context);
  }

  /**
   * @author Eike Stepper
   */
  private interface APIBaselineHelper
  {
    public boolean isNeeded(SetupTaskContext context) throws Exception;

    public void perform(SetupTaskContext context) throws Exception;
  }

  /**
   * @author Eike Stepper
   */
  @SuppressWarnings("unused")
  private static class APIBaselineHelperImpl implements APIBaselineHelper
  {
    private String containerFolder;

    private String version;

    private String zipLocation;

    private transient File zipLocationFile;

    private transient String baselineName;

    private transient File baselineDir;

    private transient IApiBaseline baseline;

    public APIBaselineHelperImpl(String containerFolder, String version, String zipLocation)
    {
      this.containerFolder = containerFolder;
      this.zipLocation = zipLocation;
      this.version = version;
    }

    public boolean isNeeded(SetupTaskContext context) throws Exception
    {
      ApiPlugin apiPlugin = ApiPlugin.getDefault();
      if (apiPlugin == null)
      {
        // Might be deactivated
        return false;
      }

      // if (StringUtil.isEmpty(containerFolder))
      // {
      // containerFolder = new File(context.getProjectDir(), ".baselines").getAbsolutePath();
      // }

      baselineName = context.getWorkspace().getStreams().get(0).getProject().getName() + " Baseline";
      baselineDir = new File(containerFolder, version);
      zipLocationFile = new File(baselineDir, "zip-location.txt");

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

      if (baselineManager.getDefaultApiBaseline() != baseline)
      {
        this.baseline = baseline;
        return true;
      }

      if (isDifferentZipLocation())
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
        if (isDifferentZipLocation())
        {
          FileUtil.delete(baselineDir, new ProgressLogMonitor(context));
        }

        if (!baselineDir.exists())
        {
          downloadAndUnzip(context);
        }

        IOUtil.writeFile(zipLocationFile, zipLocation.getBytes("UTF-8"));

        String location = baselineDir.toString();
        context.log("Creating API baseline from " + location);

        baseline = ApiModelFactory.newApiBaseline(baselineName, location);
        ApiModelFactory.addComponents(baseline, location, new ProgressLogMonitor(context));
        baselineManager.addApiBaseline(baseline);
      }

      context.log("Activating API baseline: " + baselineName);
      baselineManager.setDefaultApiBaseline(baselineName);
    }

    private boolean isDifferentZipLocation() throws Exception
    {
      if (zipLocationFile.exists())
      {
        String zipLocationURL = new String(IOUtil.readFile(zipLocationFile), "UTF-8");
        if (!ObjectUtil.equals(zipLocationURL, zipLocation))
        {
          return true;
        }
      }

      return false;
    }

    private void downloadAndUnzip(final SetupTaskContext context) throws Exception
    {
      final File baselinesDir = baselineDir.getParentFile();
      baselinesDir.mkdirs();

      File zipFile = DownloadUtil.downloadURL(zipLocation, context);

      final File[] rootDir = { null };
      ZIPUtil.unzip(zipFile, new ZIPUtil.FileSystemUnzipHandler(baselinesDir, ZIPUtil.DEFAULT_BUFFER_SIZE)
      {
        @Override
        public void unzipFile(String name, InputStream zipStream)
        {
          if (rootDir[0] == null)
          {
            rootDir[0] = new File(baselinesDir, new Path(name).segment(0));
          }

          context.log("Unzipping " + name);
          super.unzipFile(name, zipStream);
        }
      });

      rootDir[0].renameTo(baselineDir);
    }
  }

} // ApiBaselineTaskImpl

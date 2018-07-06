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

import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

import java.io.IOException;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Installation Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.InstallationTaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.InstallationTaskImpl#getRelativeProductFolder <em>Relative Product Folder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InstallationTaskImpl extends SetupTaskImpl implements InstallationTask
{
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
   * The default value of the '{@link #getRelativeProductFolder() <em>Relative Product Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativeProductFolder()
   * @generated
   * @ordered
   */
  protected static final String RELATIVE_PRODUCT_FOLDER_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getRelativeProductFolder() <em>Relative Product Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRelativeProductFolder()
   * @generated
   * @ordered
   */
  protected String relativeProductFolder = RELATIVE_PRODUCT_FOLDER_EDEFAULT;

  public static final String CONFIGURATION_FOLDER_NAME = "configuration";

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InstallationTaskImpl()
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
    return SetupPackage.Literals.INSTALLATION_TASK;
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.INSTALLATION_TASK__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRelativeProductFolder()
  {
    return relativeProductFolder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRelativeProductFolder(String newRelativeProductFolder)
  {
    String oldRelativeProductFolder = relativeProductFolder;
    relativeProductFolder = newRelativeProductFolder;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER, oldRelativeProductFolder,
          relativeProductFolder));
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
      case SetupPackage.INSTALLATION_TASK__LOCATION:
        return getLocation();
      case SetupPackage.INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER:
        return getRelativeProductFolder();
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
      case SetupPackage.INSTALLATION_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case SetupPackage.INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER:
        setRelativeProductFolder((String)newValue);
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
      case SetupPackage.INSTALLATION_TASK__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case SetupPackage.INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER:
        setRelativeProductFolder(RELATIVE_PRODUCT_FOLDER_EDEFAULT);
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
      case SetupPackage.INSTALLATION_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case SetupPackage.INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER:
        return RELATIVE_PRODUCT_FOLDER_EDEFAULT == null ? relativeProductFolder != null : !RELATIVE_PRODUCT_FOLDER_EDEFAULT.equals(relativeProductFolder);
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
    result.append(" (location: ");
    result.append(location);
    result.append(", relativeProductFolder: ");
    result.append(relativeProductFolder);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_INSTALLATION - 1;
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 0;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String relativeProductFolder = context.getRelativeProductFolder();
    Map<URI, URI> map = context.getURIConverter().getURIMap();
    map.put(URI.createURI("configuration:/"),
        context.getTrigger() == Trigger.BOOTSTRAP ? URI.createFileURI(getLocation() + "/" + relativeProductFolder + "/" + CONFIGURATION_FOLDER_NAME + "/")
            : getStaticConfigurationLocation().appendSegment(""));

    // context.put("installation.product.location", context.getProductLocation().getAbsolutePath());
    // context.put("installation.product.configuration.location")
    // context.put("installation.product.folder.name")
    // context.put("installation.product.relative.folder")
    //
    // if ("installation.product.location".equals(key))
    // {
    // String value = getProductLocation().getAbsolutePath();
    // put("installation.product.location", value);
    // return value;
    // }
    //
    // if ("installation.product.configuration.location".equals(key))
    // {
    // String value = getProductConfigurationLocation().getAbsolutePath();
    // put("installation.product.configuration.location", value);
    // return value;
    // }
    //
    // if ("installation.product.folder.name".equals(key))
    // {
    // String value = getProductFolderName();
    // put("installation.product.folder.name", value);
    // return value;
    // }
    //
    // if ("installation.product.relative.folder".equals(key))
    // {
    // String value = getRelativeProductFolder();
    // put("installation.product.relative.folder", value);
    // return value;
    // }

    return false;
  }

  private static URI getStaticConfigurationLocation()
  {
    try
    {
      Location location = Platform.getConfigurationLocation();
      URI result = URI.createURI(FileLocator.resolve(location.getURL()).toString());
      return result.hasTrailingPathSeparator() ? result.trimSegments(1) : result;
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public void perform(SetupTaskContext context) throws Exception
  {
  }

} // InstallationTaskImpl

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
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.setup.internal.sync.SetupSyncPlugin;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerCredentials;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.Date;

/**
 * @author Eike Stepper
 */
public final class SynchronizerManager
{
  public static final SynchronizerManager INSTANCE = new SynchronizerManager();

  public static final boolean ENABLED = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC);

  private static final File USER_SETUP = new File(SetupContext.USER_SETUP_LOCATION_URI.toFileString());

  private static final PropertyFile CONFIG = new PropertyFile(SetupSyncPlugin.INSTANCE.getUserLocation().append("sync.properties").toFile());

  private static final String CONNECTION_OFFERED = "connection.offered";

  private static final String SERVICE_URI = "service.uri";

  private static final String SYNC_ENABLED = "sync.enabled";

  private static final boolean DEBUG = false;

  private Boolean connectionOffered;

  private SynchronizerService service;

  private SynchronizerManager()
  {
  }

  private void connect(Shell shell)
  {
    try
    {
      SynchronizerService service = getService();

      SynchronizerLoginDialog dialog = new SynchronizerLoginDialog(shell, service);
      if (dialog.open() == SynchronizerLoginDialog.OK)
      {
        SynchronizerCredentials credentials = dialog.getCredentials();
        service.setCredentials(credentials);

        setSyncEnabled(true);
      }
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
    finally
    {
      CONFIG.setProperty(CONNECTION_OFFERED, new Date().toString());
      connectionOffered = true;
    }
  }

  public void offerFirstTimeConnect(Shell shell)
  {
    if (!ENABLED)
    {
      return;
    }

    if (DEBUG)
    {
      connectionOffered = null;
      CONFIG.removeProperty(CONNECTION_OFFERED);
    }

    if (connectionOffered == null)
    {
      String property = CONFIG.getProperty(CONNECTION_OFFERED, null);
      connectionOffered = property != null;
    }

    if (!connectionOffered)
    {
      connect(shell);
    }
  }

  public SynchronizerService getService()
  {
    if (service == null)
    {
      try
      {
        String property = CONFIG.getProperty(SERVICE_URI, null);
        if (property != null)
        {
          service = SynchronizerService.Registry.INSTANCE.getService(IOUtil.newURI(property));
        }
      }
      catch (Throwable ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }

      if (service == null)
      {
        service = SynchronizerService.Registry.ECLIPSE_SERVICE;
      }
    }

    return service;
  }

  public void setService(SynchronizerService service)
  {
    if (service == null)
    {
      CONFIG.removeProperty(SERVICE_URI);
    }
    else
    {
      CONFIG.setProperty(SERVICE_URI, service.getServiceURI().toString());
    }

    this.service = service;
  }

  public boolean isSyncEnabled()
  {
    try
    {
      return Boolean.parseBoolean(CONFIG.getProperty(SYNC_ENABLED, "false"));
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
      return false;
    }
  }

  public void setSyncEnabled(boolean enabled)
  {
    CONFIG.setProperty(SYNC_ENABLED, Boolean.toString(enabled));
  }

  public Synchronizer createSynchronizer()
  {
    return createSynchronizer(USER_SETUP);
  }

  public Synchronizer createSynchronizer(File userSetup)
  {
    SynchronizerService service = getService();
    File syncFolder = service.getSyncFolder();

    return createSynchronizer(userSetup, syncFolder);
  }

  public Synchronizer createSynchronizer(File userSetup, File syncFolder)
  {
    SynchronizerService service = getService();

    LocalDataProvider localDataProvider = new LocalDataProvider(userSetup);
    RemoteDataProvider remoteDataProvider = service.createDataProvider();

    return new Synchronizer(localDataProvider, remoteDataProvider, syncFolder);
  }
}

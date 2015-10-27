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
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.AuthorizationRequiredException;
import org.eclipse.oomph.setup.internal.sync.SetupSyncPlugin;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerCredentials;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService.CredentialsProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public final class SynchronizerManager
{
  public static final SynchronizerManager INSTANCE = new SynchronizerManager();

  public static final boolean ENABLED = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC);

  public static final long TIMEOUT = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_SYNC_TIMEOUT, 10000);

  private static final File USER_SETUP = new File(SetupContext.USER_SETUP_LOCATION_URI.toFileString());

  private static final PropertyFile CONFIG = new PropertyFile(SetupSyncPlugin.INSTANCE.getUserLocation().append("sync.properties").toFile());

  private static final String CONFIG_SERVICE_URI = "service.uri";

  private static final String CONFIG_SYNC_ENABLED = "sync.enabled";

  private static final String CONFIG_CONNECTION_OFFERED = "connection.offered";

  /**
   * If set to <code>true</code> the {@link #CONFIG_CONNECTION_OFFERED} property is cleared before each access,
   * which makes it easier to test/debug the opt-in workflow.
   * <p>
   * <b>Should never be committed with a <code>true</code> value!</b>
   */
  private static final boolean DEBUG_CONNECTION_OFFERED = false;

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
      CONFIG.setProperty(CONFIG_CONNECTION_OFFERED, new Date().toString());
      connectionOffered = true;
    }
  }

  public void offerFirstTimeConnect(Shell shell)
  {
    if (!ENABLED)
    {
      return;
    }

    if (DEBUG_CONNECTION_OFFERED)
    {
      connectionOffered = null;
      CONFIG.removeProperty(CONFIG_CONNECTION_OFFERED);
    }

    if (connectionOffered == null)
    {
      String property = CONFIG.getProperty(CONFIG_CONNECTION_OFFERED, null);
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
        String property = CONFIG.getProperty(CONFIG_SERVICE_URI, null);
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
        service = SynchronizerService.Registry.INSTANCE.getService(SynchronizerService.Registry.ECLIPSE_SERVICE_URI);
      }
    }

    return service;
  }

  public void setService(SynchronizerService service)
  {
    if (service == null)
    {
      CONFIG.removeProperty(CONFIG_SERVICE_URI);
    }
    else
    {
      CONFIG.setProperty(CONFIG_SERVICE_URI, service.getServiceURI().toString());
    }

    this.service = service;
  }

  public boolean isSyncEnabled()
  {
    try
    {
      return Boolean.parseBoolean(CONFIG.getProperty(CONFIG_SYNC_ENABLED, "false"));
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
      return false;
    }
  }

  public void setSyncEnabled(boolean enabled)
  {
    CONFIG.setProperty(CONFIG_SYNC_ENABLED, Boolean.toString(enabled));
  }

  /**
   * Returns a {@link Synchronizer} for the current service, or for the Eclipse.org service if there is no current service.
   *
   * @throws AuthorizationRequiredException
   */
  public Synchronizer createSynchronizer() throws AuthorizationRequiredException
  {
    return createSynchronizer(USER_SETUP);
  }

  /**
   * Returns a {@link Synchronizer} for the current service, or for the Eclipse.org service if there is no current service.
   *
   * @throws AuthorizationRequiredException
   */
  public Synchronizer createSynchronizer(File userSetup) throws AuthorizationRequiredException
  {
    SynchronizerService service = getService();
    File syncFolder = service.getSyncFolder();

    return createSynchronizer(userSetup, syncFolder);
  }

  /**
   * Returns a {@link Synchronizer} for the current service, or for the Eclipse.org service if there is no current service.
   *
   * @throws AuthorizationRequiredException
   */
  public Synchronizer createSynchronizer(File userSetup, File syncFolder) throws AuthorizationRequiredException
  {
    LocalDataProvider localDataProvider = new LocalDataProvider(userSetup);

    SynchronizerService service = getService();
    RemoteDataProvider remoteDataProvider = service.createDataProvider(UICredentialsProvider.INSTANCE);

    return new Synchronizer(localDataProvider, remoteDataProvider, syncFolder);
  }

  /**
   * Returns a {@link SynchronizationController} for the current service, or for the Eclipse.org service if there is no current service.
   * Returns <code>null</code> if the needed credentials are missing.
   */
  public SynchronizationController startSynchronization()
  {
    SynchronizationController controller = new SynchronizationController();
    if (controller.start(false))
    {
      return controller;
    }

    return null;
  }

  /**
   * Returns a {@link Synchronization} for the current service, or for the Eclipse.org service if there is no current service.
   * Returns <code>null</code> if the needed credentials are missing.
   */
  public Synchronization synchronize()
  {
    SynchronizationController synchronizationController = startSynchronization();
    if (synchronizationController != null)
    {
      return synchronizationController.await();
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  public static final class UICredentialsProvider implements CredentialsProvider
  {
    public static final UICredentialsProvider INSTANCE = new UICredentialsProvider();

    public SynchronizerCredentials provideCredentials(final SynchronizerService service)
    {
      final SynchronizerCredentials[] credentials = { null };

      try
      {
        final Shell shell = UIUtil.getShell();
        shell.getDisplay().syncExec(new Runnable()
        {
          public void run()
          {
            SynchronizerLoginDialog dialog = new SynchronizerLoginDialog(shell, service);
            if (dialog.open() == SynchronizerLoginDialog.OK)
            {
              credentials[0] = dialog.getCredentials();
            }
          }
        });
      }
      catch (Throwable ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }

      return credentials[0];
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class SynchronizationController
  {
    private SynchronizerJob synchronizerJob;

    private long startTime;

    public boolean start(boolean deferLocal)
    {
      if (ENABLED)
      {
        if (synchronizerJob == null && INSTANCE.isSyncEnabled())
        {
          SynchronizerService service = INSTANCE.getService();
          File syncFolder = service.getSyncFolder();

          try
          {
            Synchronizer synchronizer = INSTANCE.createSynchronizer(USER_SETUP, syncFolder);
            synchronizerJob = new SynchronizerJob(synchronizer, deferLocal);
            synchronizerJob.setService(service);
            synchronizerJob.schedule();

            startTime = System.currentTimeMillis();
          }
          catch (AuthorizationRequiredException ex)
          {
            SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }
      }

      return synchronizerJob != null;
    }

    public void stop()
    {
      if (!ENABLED)
      {
        return;
      }

      if (synchronizerJob != null)
      {
        synchronizerJob.cancel();
        synchronizerJob = null;
      }
    }

    public Synchronization await()
    {
      if (!ENABLED)
      {
        return null;
      }

      if (synchronizerJob != null)
      {
        final Synchronization[] result = { synchronizerJob.getSynchronization() };

        if (result[0] == null)
        {
          SynchronizerService service = synchronizerJob.getService();
          final String serviceLabel = service == null ? "remote service" : service.getLabel();

          try
          {
            final AtomicBoolean canceled = new AtomicBoolean();

            UIUtil.syncExec(new Runnable()
            {
              public void run()
              {
                try
                {
                  PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress()
                  {
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                    {
                      long timeout = TIMEOUT - (System.currentTimeMillis() - startTime);
                      result[0] = await(serviceLabel, timeout, monitor);
                    }
                  });
                }
                catch (InvocationTargetException ex)
                {
                  SetupUIPlugin.INSTANCE.log(ex);
                }
                catch (InterruptedException ex)
                {
                  canceled.set(true);
                }
              }
            });

            if (result[0] == null && !canceled.get())
            {
              Throwable exception = synchronizerJob.getException();
              if (exception != null)
              {
                throw exception;
              }

              throw new TimeoutException("Request to " + serviceLabel + " timed out.");
            }
          }
          catch (Throwable ex)
          {
            SetupUIPlugin.INSTANCE.log(ex);
          }
        }

        return result[0];
      }

      return null;
    }

    private Synchronization await(String serviceLabel, long timeout, IProgressMonitor monitor)
    {
      monitor.beginTask("Requesting data from " + serviceLabel + "...", IProgressMonitor.UNKNOWN);

      try
      {
        return synchronizerJob.awaitSynchronization(timeout, monitor);
      }
      finally
      {
        monitor.done();
      }
    }
  }
}

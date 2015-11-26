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
import org.eclipse.oomph.internal.ui.UIPropertyTester;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.AuthorizationRequiredException;
import org.eclipse.oomph.setup.internal.sync.SetupSyncPlugin;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.StorageFactory;
import org.eclipse.userstorage.spi.ICredentialsProvider;
import org.eclipse.userstorage.spi.StorageCache;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public final class SynchronizerManager
{
  public static final File SYNC_FOLDER = SetupSyncPlugin.INSTANCE.getUserLocation().toFile();

  public static final SynchronizerManager INSTANCE = new SynchronizerManager();

  public static final boolean ENABLED = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC);

  private static final File USER_SETUP = new File(SetupContext.USER_SETUP_LOCATION_URI.toFileString());

  private static final PropertyFile CONFIG = new PropertyFile(SetupSyncPlugin.INSTANCE.getUserLocation().append("sync.properties").toFile());

  private static final String CONFIG_SYNC_ENABLED = "sync.enabled";

  private static final String CONFIG_CONNECTION_OFFERED = "connection.offered";

  /**
   * If set to <code>true</code> the {@link #CONFIG_CONNECTION_OFFERED} property is cleared before each access,
   * which makes it easier to test/debug the opt-in workflow.
   * <p>
   * <b>Should never be committed with a <code>true</code> value!</b>
   */
  private static final boolean DEBUG_CONNECTION_OFFERED = false;

  private final IStorage storage;

  private Boolean connectionOffered;

  private SynchronizerManager()
  {
    StorageCache cache = new RemoteDataProvider.SyncStorageCache(SYNC_FOLDER);
    storage = StorageFactory.DEFAULT.create(RemoteDataProvider.APPLICATION_TOKEN, cache);
  }

  public IStorage getStorage()
  {
    return storage;
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

  public boolean setSyncEnabled(boolean enabled)
  {
    boolean changed;

    if (enabled)
    {
      changed = CONFIG.compareAndSetProperty(CONFIG_SYNC_ENABLED, "true", "false", null);
    }
    else
    {
      changed = CONFIG.compareAndSetProperty(CONFIG_SYNC_ENABLED, "false", "true");
    }

    if (changed)
    {
      try
      {
        UIPropertyTester.requestEvaluation(SetupPropertyTester.PREFIX + SetupPropertyTester.SYNC_ENABLED, false);
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
    }

    return changed;
  }

  /**
   * Returns a {@link Synchronizer} for the current service, or for the Eclipse.org service if there is no current service.
   *
   * @throws AuthorizationRequiredException
   */
  public Synchronizer createSynchronizer(File userSetup, File syncFolder) throws AuthorizationRequiredException
  {
    LocalDataProvider localDataProvider = new LocalDataProvider(userSetup);
    RemoteDataProvider remoteDataProvider = new RemoteDataProvider(storage);

    return new Synchronizer(localDataProvider, remoteDataProvider, syncFolder);
  }

  /**
   * Returns a {@link SynchronizationController} for the current service, or for the Eclipse.org service if there is no current service.
   * Returns <code>null</code> if the needed credentials are missing.
   */
  public SynchronizationController startSynchronization(boolean withCredentialsPrompt)
  {
    SynchronizationController controller = new SynchronizationController();
    if (controller.start(withCredentialsPrompt, false))
    {
      return controller;
    }

    return null;
  }

  /**
   * Returns a {@link Synchronization} for the current service, or for the Eclipse.org service if there is no current service.
   * Returns <code>null</code> if the needed credentials are missing.
   */
  public Synchronization synchronize(boolean withCredentialsPrompt)
  {
    SynchronizationController synchronizationController = startSynchronization(withCredentialsPrompt);
    if (synchronizationController != null)
    {
      return synchronizationController.await();
    }

    return null;
  }

  public void performSynchronization(Synchronization synchronization, boolean quiet, boolean local)
  {
    try
    {
      EMap<String, SyncPolicy> policies = synchronization.getRemotePolicies();
      Map<String, SyncAction> actions = synchronization.getActions();

      for (Iterator<Map.Entry<String, SyncAction>> it = actions.entrySet().iterator(); it.hasNext();)
      {
        Map.Entry<String, SyncAction> entry = it.next();
        String syncID = entry.getKey();
        SyncAction syncAction = entry.getValue();

        SyncPolicy policy = policies.get(syncID);
        if (policy == SyncPolicy.EXCLUDE)
        {
          it.remove();
          continue;
        }

        if (policy == null && quiet)
        {
          it.remove();
          continue;
        }

        SyncActionType type = syncAction.getComputedType();
        switch (type)
        {
          case SET_LOCAL: // Ignore LOCAL -> REMOTE actions.
          case REMOVE_LOCAL: // Ignore LOCAL -> REMOTE actions.
            if (local)
            {
              break;
            }

            //$FALL-THROUGH$

          case CONFLICT: // Ignore interactive actions.
            if (quiet)
            {
              break;
            }

            //$FALL-THROUGH$

          case EXCLUDE: // Should not occur.
          case NONE: // Should not occur.
            it.remove();
            continue;
        }
      }

      if (!actions.isEmpty())
      {
        try
        {
          synchronization.commit();
        }
        catch (NotCurrentException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.INFO);
        }
        catch (IOException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
      }
    }
    finally
    {
      synchronization.dispose();
    }
  }

  public void performFullSynchronization()
  {
    // This is probably not needed because the handler is only enabled after opt-in.
    // But it doesn't harm and it's safer for future changes.
    SynchronizerManager.INSTANCE.offerFirstTimeConnect(UIUtil.getShell());

    SynchronizationController synchronizationController = SynchronizerManager.INSTANCE.startSynchronization(true);
    if (synchronizationController != null)
    {
      Synchronization synchronization = synchronizationController.await();
      if (synchronization != null)
      {
        // SynchronizerManager.INSTANCE.performSynchronization(synchronization, false, false);
      }
    }
  }

  public boolean offerFirstTimeConnect(Shell shell)
  {
    if (!ENABLED)
    {
      return false;
    }

    IStorageService service = storage.getService();
    if (service == null)
    {
      return false;
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

    if (connectionOffered)
    {
      return false;
    }

    if (!connect(shell))
    {
      return false;
    }

    setSyncEnabled(true);
    return true;
  }

  private boolean connect(final Shell shell)
  {
    try
    {
      final boolean[] result = { false };

      shell.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          SynchronizerWelcomeDialog dialog = new SynchronizerWelcomeDialog(shell);
          result[0] = dialog.open() == SynchronizerWelcomeDialog.OK;
        }
      });

      return result[0];
    }
    catch (Throwable ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
      return false;
    }
    finally
    {
      CONFIG.setProperty(CONFIG_CONNECTION_OFFERED, new Date().toString());
      connectionOffered = true;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class SynchronizationController
  {
    private SynchronizerJob synchronizerJob;

    public boolean start(boolean withCredentialsPrompt, boolean deferLocal)
    {
      if (ENABLED)
      {
        if (synchronizerJob == null && INSTANCE.isSyncEnabled())
        {
          IStorageService service = INSTANCE.getStorage().getService();
          if (service == null)
          {
            return false;
          }

          try
          {
            Synchronizer synchronizer = INSTANCE.createSynchronizer(USER_SETUP, SYNC_FOLDER);
            synchronizerJob = new SynchronizerJob(synchronizer, deferLocal);
            synchronizerJob.setService(service);

            if (!withCredentialsPrompt)
            {
              synchronizerJob.setCredentialsProvider(ICredentialsProvider.CANCEL);
            }

            synchronizerJob.schedule();
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
          try
          {
            final AtomicBoolean canceled = new AtomicBoolean();
            final IStorageService service = synchronizerJob.getService();

            UIUtil.syncExec(new Runnable()
            {
              public void run()
              {
                try
                {
                  Shell shell = UIUtil.getShell();
                  ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

                  final Semaphore authenticationSemaphore = service.getAuthenticationSemaphore();
                  authenticationSemaphore.acquire();

                  dialog.run(true, true, new IRunnableWithProgress()
                  {
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                    {
                      authenticationSemaphore.release();

                      String serviceLabel = service.getServiceLabel();
                      result[0] = await(serviceLabel, monitor);
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
              if (exception == null || exception instanceof OperationCanceledException)
              {
                return null;
              }

              throw exception;
            }
          }
          catch (Throwable ex)
          {
            SetupUIPlugin.INSTANCE.log(ex);
          }
          finally
          {
            synchronizerJob = null;
          }
        }

        return result[0];
      }

      return null;
    }

    private Synchronization await(String serviceLabel, IProgressMonitor monitor)
    {
      monitor.beginTask("Requesting data from " + serviceLabel + "...", IProgressMonitor.UNKNOWN);

      try
      {
        return synchronizerJob.awaitSynchronization(monitor);
      }
      finally
      {
        monitor.done();
      }
    }
  }
}

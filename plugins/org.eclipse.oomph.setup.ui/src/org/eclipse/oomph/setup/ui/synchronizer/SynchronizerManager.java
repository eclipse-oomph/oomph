/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.sync.DataProvider.Location;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.setup.internal.sync.SetupSyncPlugin;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerAdapter;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.internal.sync.SynchronizerListener;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.LockFile;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public final class SynchronizerManager
{
  public static final File SYNC_FOLDER = SetupSyncPlugin.INSTANCE.getUserLocation().toFile();

  public static final SynchronizerManager INSTANCE = new SynchronizerManager();

  public static final boolean ENABLED = !PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC_SKIP);

  private static final File USER_SETUP = new File(SetupContext.USER_SETUP_LOCATION_URI.toFileString());

  private static final LockFile USER_SETUP_LOCK = new LockFile(new File(USER_SETUP.getParentFile(), USER_SETUP.getName() + ".lock"));

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

  private final RemoteDataProvider remoteDataProvider;

  private Boolean connectionOffered;

  private SynchronizerManager()
  {
    StorageCache cache = new RemoteDataProvider.SyncStorageCache(SYNC_FOLDER);
    storage = StorageFactory.DEFAULT.create(RemoteDataProvider.APPLICATION_TOKEN, cache);

    remoteDataProvider = new RemoteDataProvider(storage);
  }

  public IStorage getStorage()
  {
    return storage;
  }

  public RemoteDataProvider getRemoteDataProvider()
  {
    return remoteDataProvider;
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

  public Synchronizer createSynchronizer(File userSetup, File syncFolder)
  {
    LocalDataProvider localDataProvider = new LocalDataProvider(userSetup);

    Synchronizer synchronizer = new Synchronizer(localDataProvider, remoteDataProvider, syncFolder);
    synchronizer.setLockFile(USER_SETUP_LOCK);
    synchronizer.addListener(new SkipHandler());

    return synchronizer;
  }

  public SynchronizationController startSynchronization(boolean withProgressDialog, boolean withCredentialsPrompt, boolean deferLocal)
  {
    SynchronizationController controller = new SynchronizationController();
    if (controller.start(withProgressDialog, withCredentialsPrompt, deferLocal))
    {
      return controller;
    }

    return null;
  }

  public Synchronization synchronize(boolean withProgressDialog, boolean withCredentialsPrompt, boolean deferLocal)
  {
    SynchronizationController synchronizationController = startSynchronization(withProgressDialog, withCredentialsPrompt, deferLocal);
    if (synchronizationController != null)
    {
      return synchronizationController.await();
    }

    return null;
  }

  public Impact performSynchronization(Synchronization synchronization, boolean interactive, boolean remoteModifications)
  {
    try
    {
      EMap<String, SyncPolicy> policies = synchronization.getRemotePolicies();
      Map<String, SyncAction> actions = synchronization.getActions();
      Map<String, SyncAction> includedActions = new HashMap<String, SyncAction>();

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

        if (policy == null && !interactive)
        {
          it.remove();
          continue;
        }

        SyncActionType type = syncAction.getComputedType();
        switch (type)
        {
          case SET_LOCAL:
          case REMOVE_LOCAL:
            if (!remoteModifications)
            {
              // Ignore LOCAL -> REMOTE actions.
              it.remove();
              continue;
            }
            break;

          case CONFLICT:
            if (!interactive)
            {
              // Ignore interactive actions.
              it.remove();
              continue;
            }
            break;

          case EXCLUDE:
          case NONE:
            // Should not occur.
            it.remove();
            continue;
        }

        if (policy == SyncPolicy.INCLUDE && type != SyncActionType.CONFLICT)
        {
          it.remove();
          includedActions.put(syncID, syncAction);
        }
      }

      if (!actions.isEmpty() || !includedActions.isEmpty())
      {
        if (!actions.isEmpty())
        {
          SynchronizerDialog dialog = new SynchronizerDialog(UIUtil.getShell(), null, synchronization);
          if (dialog.open() != SynchronizerDialog.OK)
          {
            return null;
          }
        }

        actions.putAll(includedActions);

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

        return getSkipHandler(synchronization);
      }
    }
    finally
    {
      synchronization.dispose();
    }

    return null;
  }

  public Impact performFullSynchronization()
  {
    offerFirstTimeConnect(UIUtil.getShell());

    Synchronization synchronization = synchronize(true, true, false);
    if (synchronization != null)
    {
      return performSynchronization(synchronization, true, true);
    }

    return null;
  }

  /**
   * Returns <code>true</code> if offered the first time and offer was accepted, <code>false</code> otherwise.
   */
  public boolean offerFirstTimeConnect(Shell shell)
  {
    if (!ENABLED)
    {
      return false;
    }

    if (isSyncEnabled())
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

    Boolean answer = connect(shell, service);
    if (answer == null)
    {
      return false;
    }

    setSyncEnabled(answer);
    return answer;
  }

  private Boolean connect(final Shell shell, final IStorageService service)
  {
    final Boolean[] result = { null };

    try
    {
      shell.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          OptInDialog dialog = new OptInDialog(shell, service);
          dialog.open();
          result[0] = dialog.getAnswer();
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
      if (result[0] != null)
      {
        CONFIG.setProperty(CONFIG_CONNECTION_OFFERED, new Date().toString());
        connectionOffered = true;
      }
    }
  }

  private static SkipHandler getSkipHandler(Synchronization synchronization)
  {
    for (SynchronizerListener listener : synchronization.getSynchronizer().getListeners())
    {
      if (listener instanceof SkipHandler)
      {
        return (SkipHandler)listener;
      }
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  public static interface Impact
  {
    public boolean hasLocalImpact();

    public boolean hasRemoteImpact();
  }

  /**
   * @author Eike Stepper
   */
  private static final class SkipHandler extends SynchronizerAdapter implements Impact
  {
    private static final String CONFIG_SKIPPED_LOCAL = "skipped.local";

    private static final String CONFIG_SKIPPED_REMOTE = "skipped.remote";

    private static final String ID_SEPARATOR = " ";

    private final Set<Location> skippedLocations = new HashSet<Location>();

    private final Set<String> skippedLocal = new HashSet<String>();

    private final Set<String> skippedRemote = new HashSet<String>();

    private final Set<String> computedLocal = new HashSet<String>();

    private final Set<String> computedRemote = new HashSet<String>();

    private boolean localImpact;

    private boolean remoteImpact;

    public SkipHandler()
    {
    }

    public boolean hasLocalImpact()
    {
      return localImpact;
    }

    public boolean hasRemoteImpact()
    {
      return remoteImpact;
    }

    @Override
    public void tasksCollected(Synchronization synchronization, Location location, Map<String, SetupTask> oldTasks, Map<String, SetupTask> newTasks)
    {
      Set<String> skippedIDs = getSkippedIDs(location);
      oldTasks.keySet().removeAll(skippedIDs);
    }

    @Override
    public void actionsComputed(Synchronization synchronization, Map<String, SyncAction> actions)
    {
      computedLocal.clear();
      computedRemote.clear();

      analyzeImpact(actions, computedLocal, computedRemote);
    }

    @Override
    public void commitFinished(Synchronization synchronization, Throwable t)
    {
      if (t != null)
      {
        return;
      }

      Set<String> committedLocal = new HashSet<String>();
      Set<String> committedRemote = new HashSet<String>();

      Map<String, SyncAction> actions = synchronization.getActions();
      analyzeImpact(actions, committedLocal, committedRemote);

      localImpact = !committedRemote.isEmpty();
      remoteImpact = !committedLocal.isEmpty();

      computedLocal.removeAll(committedLocal);
      computedLocal.removeAll(committedRemote);

      computedRemote.removeAll(committedLocal);
      computedRemote.removeAll(committedRemote);

      setSkippedIDs(Location.LOCAL, computedLocal);
      setSkippedIDs(Location.REMOTE, computedRemote);
    }

    private Set<String> getSkippedIDs(Location location)
    {
      Set<String> skippedIDs = location.pick(skippedLocal, skippedRemote);

      if (skippedLocations.add(location))
      {
        String key = location.pick(CONFIG_SKIPPED_LOCAL, CONFIG_SKIPPED_REMOTE);
        String property = CONFIG.getProperty(key, null);
        if (property != null)
        {
          StringTokenizer tokenizer = new StringTokenizer(property, ID_SEPARATOR);
          while (tokenizer.hasMoreTokens())
          {
            String id = tokenizer.nextToken();
            skippedIDs.add(id);
          }
        }
      }

      return skippedIDs;
    }

    private void setSkippedIDs(Location location, Set<String> skippedIDs)
    {
      String key = location.pick(CONFIG_SKIPPED_LOCAL, CONFIG_SKIPPED_REMOTE);
      if (skippedIDs.isEmpty())
      {
        CONFIG.removeProperty(key);
      }
      else
      {
        List<String> list = new ArrayList<String>(skippedIDs);
        Collections.sort(list);

        StringBuilder builder = new StringBuilder();
        for (String id : list)
        {
          if (builder.length() != 0)
          {
            builder.append(ID_SEPARATOR);
          }

          builder.append(id);
        }

        CONFIG.setProperty(key, builder.toString());
      }
    }

    private static void analyzeImpact(Map<String, SyncAction> actions, Set<String> local, Set<String> remote)
    {
      for (Map.Entry<String, SyncAction> entry : actions.entrySet())
      {
        String id = entry.getKey();
        SyncAction action = entry.getValue();

        SyncActionType effectiveType = action.getEffectiveType();
        switch (effectiveType)
        {
          case SET_LOCAL:
          case REMOVE_LOCAL:
            local.add(id);
            break;

          case SET_REMOTE:
          case REMOVE_REMOTE:
            remote.add(id);
            break;

          case CONFLICT:
            local.add(id);
            remote.add(id);
            break;
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class SynchronizationController
  {
    private boolean withProgressDialog;

    private SynchronizerJob synchronizerJob;

    public boolean start(boolean withProgressDialog, boolean withCredentialsPrompt, boolean deferLocal)
    {
      this.withProgressDialog = withProgressDialog;
      if (ENABLED)
      {
        if (synchronizerJob == null && INSTANCE.isSyncEnabled())
        {
          IStorageService service = INSTANCE.getStorage().getService();
          if (service == null)
          {
            return false;
          }

          Synchronizer synchronizer = INSTANCE.createSynchronizer(USER_SETUP, SYNC_FOLDER);

          synchronizerJob = new SynchronizerJob(synchronizer, deferLocal);
          synchronizerJob.setService(service);

          if (!withCredentialsPrompt)
          {
            synchronizerJob.setCredentialsProvider(ICredentialsProvider.CANCEL);
          }

          synchronizerJob.schedule();
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
        synchronizerJob.stopSynchronization();
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
            final String serviceLabel = service.getServiceLabel();

            if (withProgressDialog)
            {
              final Semaphore authenticationSemaphore = service.getAuthenticationSemaphore();
              authenticationSemaphore.acquire();

              UIUtil.syncExec(new Runnable()
              {
                public void run()
                {
                  try
                  {
                    Shell shell = UIUtil.getShell();
                    ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

                    dialog.run(true, true, new IRunnableWithProgress()
                    {
                      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                      {
                        authenticationSemaphore.release();
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
            }
            else
            {
              final IProgressMonitor monitor = new NullProgressMonitor();

              Job watchDog = new Job("Synchronizer Watch Dog")
              {
                @Override
                protected IStatus run(IProgressMonitor monitor)
                {
                  try
                  {
                    Thread.sleep(10000);
                  }
                  catch (Throwable ex)
                  {
                    //$FALL-THROUGH$
                  }

                  monitor.setCanceled(true);
                  return Status.OK_STATUS;
                }
              };

              watchDog.setSystem(true);
              watchDog.schedule();

              result[0] = await(serviceLabel, monitor);
            }

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
            SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
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

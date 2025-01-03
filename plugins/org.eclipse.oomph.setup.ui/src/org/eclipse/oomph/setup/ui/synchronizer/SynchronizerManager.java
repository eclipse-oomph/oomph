/*
 * Copyright (c) 2015-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.StorageFactory;
import org.eclipse.userstorage.oauth.EclipseOAuthCredentialsProvider;
import org.eclipse.userstorage.spi.ICredentialsProvider;
import org.eclipse.userstorage.spi.StorageCache;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
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
import java.util.function.Consumer;

/**
 * @author Eike Stepper
 */
public final class SynchronizerManager
{
  public static final File SYNC_FOLDER = SetupSyncPlugin.INSTANCE.getUserLocation().toFile();

  public static final SynchronizerManager INSTANCE = new SynchronizerManager();

  public static final boolean ENABLED = !PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC_SKIP);

  private static final File USER_SETUP = new File(SetupContext.USER_SETUP_LOCATION_URI.toFileString());

  private static final LockFile USER_SETUP_LOCK = new LockFile(new File(USER_SETUP.getParentFile(), USER_SETUP.getName() + ".lock")); //$NON-NLS-1$

  private static final PropertyFile CONFIG = new PropertyFile(SetupSyncPlugin.INSTANCE.getUserLocation().append("sync.properties").toFile()); //$NON-NLS-1$

  private static final String CONFIG_SYNC_ENABLED = "sync.enabled"; //$NON-NLS-1$

  private static final String CONFIG_CONNECTION_SERVICE_DISCONTINUE = "connection.service.discontinue"; //$NON-NLS-1$

  private static final String CONNECTION_SERVICE_DISCONTINUE_ISSUME = "https://github.com/eclipse-oomph/oomph/issues/123"; //$NON-NLS-1$

  private final IStorage storage;

  private final RemoteDataProvider remoteDataProvider;

  private Boolean connectionServiceDiscontinue;

  private SynchronizerManager()
  {
    StorageCache cache = new RemoteDataProvider.SyncStorageCache(SYNC_FOLDER);
    storage = StorageFactory.DEFAULT.create(RemoteDataProvider.APPLICATION_TOKEN, cache);

    if (!PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SYNC_CREDENTIAL_PROVIDER_SKIP_DEFAULT))
    {
      ICredentialsProvider credentialProvider = createCredentialProvider();
      if (credentialProvider != null)
      {
        storage.setCredentialsProvider(credentialProvider);
      }
    }

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
      return Boolean.parseBoolean(CONFIG.getProperty(CONFIG_SYNC_ENABLED, "false")); //$NON-NLS-1$
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
      changed = CONFIG.compareAndSetProperty(CONFIG_SYNC_ENABLED, "true", "false", null); //$NON-NLS-1$ //$NON-NLS-2$
    }
    else
    {
      changed = CONFIG.compareAndSetProperty(CONFIG_SYNC_ENABLED, "false", "true"); //$NON-NLS-1$ //$NON-NLS-2$
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
      Map<String, SyncAction> includedActions = new HashMap<>();

      for (Iterator<Map.Entry<String, SyncAction>> it = actions.entrySet().iterator(); it.hasNext();)
      {
        Map.Entry<String, SyncAction> entry = it.next();
        String syncID = entry.getKey();
        SyncAction syncAction = entry.getValue();

        SyncPolicy policy = policies.get(syncID);
        if (policy == SyncPolicy.EXCLUDE || policy == null && !interactive)
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

  public void warnServiceDiscontinue(Shell shell, IStorageService service)
  {
    if (connectionServiceDiscontinue == null)
    {
      String property = CONFIG.getProperty(CONFIG_CONNECTION_SERVICE_DISCONTINUE, null);
      if (property != null)
      {
        try
        {
          Date previousReminder = (Date)EcoreFactory.eINSTANCE.createFromString(EcorePackage.Literals.EDATE, property);
          long previousReminderTime = previousReminder.getTime();
          long oneWeekAgo = System.currentTimeMillis() - Duration.ofDays(7).toMillis();
          if (previousReminderTime > oneWeekAgo)
          {
            connectionServiceDiscontinue = true;
          }
        }
        catch (RuntimeException ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    if (Boolean.TRUE.equals(connectionServiceDiscontinue))
    {
      return;
    }

    shell.getDisplay().asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        new ConnectionServiceDiscontinueDialog(shell, service, dialog -> {
          Boolean answer = dialog.getAnswer();
          if (answer != null)
          {
            CONFIG.setProperty(CONFIG_CONNECTION_SERVICE_DISCONTINUE, getDate());
            connectionServiceDiscontinue = true;
            setSyncEnabled(answer);
          }
        }).open();
      }
    });
  }

  /**
   * Returns <code>true</code> if offered the first time and offer was accepted, <code>false</code> otherwise.
   */
  public boolean offerFirstTimeConnect(Shell shell)
  {
    // The service is being discontinued so stop offering it.
    return false;
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

  private static ICredentialsProvider createCredentialProvider()
  {
    return new EclipseOAuthCredentialsProvider(new OAuthConstants());
  }

  private static String getDate()
  {
    return EcoreFactory.eINSTANCE.convertToString(EcorePackage.Literals.EDATE, new Date());
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
    private static final String CONFIG_SKIPPED_LOCAL = "skipped.local"; //$NON-NLS-1$

    private static final String CONFIG_SKIPPED_REMOTE = "skipped.remote"; //$NON-NLS-1$

    private static final String ID_SEPARATOR = " "; //$NON-NLS-1$

    private final Set<Location> skippedLocations = new HashSet<>();

    private final Set<String> skippedLocal = new HashSet<>();

    private final Set<String> skippedRemote = new HashSet<>();

    private final Set<String> computedLocal = new HashSet<>();

    private final Set<String> computedRemote = new HashSet<>();

    private boolean localImpact;

    private boolean remoteImpact;

    public SkipHandler()
    {
    }

    @Override
    public boolean hasLocalImpact()
    {
      return localImpact;
    }

    @Override
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

      Set<String> committedLocal = new HashSet<>();
      Set<String> committedRemote = new HashSet<>();

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
        List<String> list = new ArrayList<>(skippedIDs);
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

          INSTANCE.warnServiceDiscontinue(UIUtil.getShell(), service);

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
                @Override
                public void run()
                {
                  try
                  {
                    Shell shell = UIUtil.getShell();
                    ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

                    dialog.run(true, true, new IRunnableWithProgress()
                    {
                      @Override
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

              Job watchDog = new Job(Messages.SynchronizerManager_watchDogJob_name)
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

              SynchronizerManager.log(exception);
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
      monitor.beginTask(NLS.bind(Messages.SynchronizerManager_requestDataTask_name, serviceLabel), IProgressMonitor.UNKNOWN);

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

  public static void log(Throwable throwable)
  {
    final IStorage storage = SynchronizerManager.INSTANCE.getStorage();
    IStorageService service = storage.getService();
    String serviceLabel = service == null ? "Unknown" : service.getServiceLabel(); //$NON-NLS-1$
    SetupUIPlugin.INSTANCE.log(
        new Status(IStatus.WARNING, SetupUIPlugin.PLUGIN_ID, "Window -> Preferences -> Oomph -> Setup Tasks -> Preference Synchronizer -> 'Synchronize with " //$NON-NLS-1$
            + serviceLabel + "' is enabled and the synchronization has failed for the following reason:", throwable)); //$NON-NLS-1$
  }

  /**
   * @author Ed Merks
   */
  public static final class Availability
  {
    public static final boolean AVAILABLE;

    static
    {
      boolean available;
      try
      {
        available = SynchronizerManager.INSTANCE != null;
      }
      catch (NoClassDefFoundError error)
      {
        available = false;
      }

      AVAILABLE = available;
    }

    private Availability()
    {
    }
  }

  private static class ConnectionServiceDiscontinueDialog extends AbstractServiceDialog
  {
    private Boolean answer = false;

    private final Consumer<ConnectionServiceDiscontinueDialog> handler;

    public ConnectionServiceDiscontinueDialog(Shell parentShell, IStorageService service, Consumer<ConnectionServiceDiscontinueDialog> handler)
    {
      super(parentShell, service);
      setBlockOnOpen(false);
      setShellStyle(SWT.DIALOG_TRIM | getDefaultOrientation());
      this.handler = handler;
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings()
    {
      return null;
    }

    public Boolean getAnswer()
    {
      return answer;
    }

    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);
    }

    @Override
    protected void createUI(Composite parent, String serviceLabel, String shortLabel)
    {
      // Do you want to save your preferences on the {0} server so you can share them on other workstations?
      setMessage(NLS.bind(Messages.SynchronizerManager_ServiceDiscontinue_title, serviceLabel));

      Label label = new Label(parent, SWT.NONE);
      label.setText(Messages.SynchronizerManager_ServiceDiscontinueFindOutMore_label);

      Link link = new Link(parent, SWT.NONE);
      link.setText("<a>" + CONNECTION_SERVICE_DISCONTINUE_ISSUME + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
      link.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          OS.INSTANCE.openSystemBrowser(CONNECTION_SERVICE_DISCONTINUE_ISSUME);
        }
      });

      new Label(parent, SWT.NONE);

      Button noButton = new Button(parent, SWT.RADIO);
      noButton.setText(NLS.bind(Messages.SynchronizerManager_ServiceDisable_label, shortLabel));
      noButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          answer = false;
        }
      });

      Button yesButton = new Button(parent, SWT.RADIO);
      yesButton.setText(NLS.bind(Messages.SynchronizerManager_ServiceContinue_label, shortLabel));
      yesButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          answer = true;
        }
      });
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
      createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
      createButton(parent, IDialogConstants.CANCEL_ID, Messages.OptInDialog_askMeLaterButton_text, false);
    }

    @Override
    protected void cancelPressed()
    {
      answer = null;
      super.cancelPressed();
      handler.accept(this);
    }

    @Override
    protected void okPressed()
    {
      super.okPressed();
      handler.accept(this);
    }
  }
}

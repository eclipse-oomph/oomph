/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesRecorder;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.PreferenceTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.DataProvider;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider.AuthorizationRequiredException;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction.CommitHandler;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerDialog;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerDialog.PolicyAndValue;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.UICredentialsProvider;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public final class RecorderManager
{
  public static final RecorderManager INSTANCE = new RecorderManager();

  private static final IPersistentPreferenceStore SETUP_UI_PREFERENCES = (IPersistentPreferenceStore)SetupUIPlugin.INSTANCE.getPreferenceStore();

  private static final UserCallback USER_CALLBACK = new UserCallback()
  {
    @Override
    public void execInUI(boolean async, Runnable runnable)
    {
      UIUtil.syncExec(runnable);
    }
  };

  private static final URI USER_URI = SetupContext.USER_SETUP_URI.appendFragment("/");

  private static final URI USER_FILE_URI = SetupContext.resolveUser(SetupCoreUtil.createResourceSet().getURIConverter().normalize(SetupContext.USER_SETUP_URI));

  private static final boolean SYNC_FOLDER_RANDOM = PropertiesUtil.isProperty("oomph.setup.sync.folder.random");

  private static final boolean SYNC_FOLDER_KEEP = PropertiesUtil.isProperty("oomph.setup.sync.folder.keep");

  private final EarlySynchronization earlySynchronization = new EarlySynchronization();

  private static ToolItem toolItem;

  private final DisplayListener displayListener = new DisplayListener();

  private Display display;

  private PreferencesRecorder recorder;

  private IEditorPart editor;

  private boolean user = true;

  private RecorderManager()
  {
  }

  public void record(IEditorPart editor)
  {
    this.editor = editor;

    boolean wasEnabled = isRecorderEnabled();
    setRecorderEnabled(true);

    boolean wasUser = user;
    user = false;

    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, null, null, null);

    try
    {
      dialog.open();
    }
    finally
    {
      user = wasUser;
      this.editor = null;
      setRecorderEnabled(wasEnabled);
    }
  }

  public boolean isUser()
  {
    return user;
  }

  public boolean isRecorderEnabled()
  {
    String value = SETUP_UI_PREFERENCES.getString(SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER);
    if (StringUtil.isEmpty(value))
    {
      ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
      SetupContext setupContext = SetupContext.createUserOnly(resourceSet);
      User user = setupContext.getUser();

      boolean enabled = user.isPreferenceRecorderDefault();
      doSetRecorderEnabled(enabled);
      return enabled;
    }

    return Boolean.parseBoolean(value);
  }

  public void setRecorderEnabled(boolean enabled)
  {
    if (isRecorderEnabled() != enabled)
    {
      try
      {
        doSetRecorderEnabled(enabled);
      }
      finally
      {
        if (enabled)
        {
          if (recorder == null)
          {
            recorder = new PreferencesRecorder();
          }

          earlySynchronization.start(false);
        }
        else
        {
          if (recorder != null)
          {
            recorder.done();
            recorder = null;
          }

          earlySynchronization.stop();
        }
      }
    }
  }

  private void doSetRecorderEnabled(boolean enabled)
  {
    SETUP_UI_PREFERENCES.setValue(SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER, Boolean.toString(enabled));

    try
    {
      SETUP_UI_PREFERENCES.save();
    }
    catch (IOException ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
  }

  private Map<String, PreferenceTask> commitTransaction(RecorderTransaction transaction)
  {
    try
    {
      return transaction.commit();
    }
    finally
    {
      closeTransaction(transaction);
    }
  }

  private void closeTransaction(final RecorderTransaction transaction)
  {
    try
    {
      transaction.close();
    }
    finally
    {
      earlySynchronization.stop();
    }
  }

  public Scope getRecorderTargetObject(ResourceSet resourceSet)
  {
    URI recorderTarget = getRecorderTarget();
    return (Scope)resourceSet.getEObject(recorderTarget, true);
  }

  public Scope getRecorderTargetObject()
  {
    ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    return getRecorderTargetObject(resourceSet);
  }

  public URI getRecorderTarget()
  {
    String value = SETUP_UI_PREFERENCES.getString(SetupUIPlugin.PREF_PREFERENCE_RECORDER_TARGET);
    if (StringUtil.isEmpty(value))
    {
      return USER_URI;
    }

    URI uri = URI.createURI(value);
    return convertURI(uri);
  }

  public URI setRecorderTarget(URI uri)
  {
    uri = convertURI(uri);

    URI oldURI = getRecorderTarget();
    if (!ObjectUtil.equals(oldURI, uri))
    {
      SETUP_UI_PREFERENCES.setValue(SetupUIPlugin.PREF_PREFERENCE_RECORDER_TARGET, uri.toString());

      try
      {
        SETUP_UI_PREFERENCES.save();
      }
      catch (IOException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }

      return oldURI;
    }

    return null;
  }

  private void handleRecording(IEditorPart editorPart, Map<URI, String> values)
  {
    RecorderTransaction transaction = editorPart == null ? RecorderTransaction.open() : RecorderTransaction.open(user, editorPart);
    transaction.setPreferences(values);

    // In some cases (such as changing the recorder target in the current recorder transaction or missing credentials)
    // early synchronization has not been started, yet. We want to be safe and try to start it now (has no effect if already started).
    boolean started = earlySynchronization.start(true);

    SyncInfo syncInfo = started ? awaitEarlySynchronization() : null;
    Synchronization synchronization = syncInfo == null ? null : syncInfo.getSynchronization();

    try
    {
      boolean dialogNeeded = false;
      Set<URI> preferenceURIs = transaction.getPreferences().keySet();

      for (Iterator<URI> it = preferenceURIs.iterator(); it.hasNext();)
      {
        URI uri = it.next();
        String key = PreferencesFactory.eINSTANCE.convertURI(uri);

        if (synchronization != null)
        {
          String syncID = synchronization.getPreferenceIDs().get(key);
          SyncPolicy remotePolicy = synchronization.getRemotePolicies().get(syncID);
          if (remotePolicy == SyncPolicy.INCLUDE)
          {
            transaction.setPolicy(key, true); // Default to "record".
          }
        }

        Boolean localPolicy = transaction.getPolicy(key);
        if (localPolicy == null)
        {
          // Local policy is missing.
          transaction.setPolicy(key, true); // Default to "record".
          dialogNeeded = true; // And prompt below...
        }
        else if (!localPolicy)
        {
          // Local policy is "ignore".
          it.remove(); // Remove the preference change from the transaction.
        }
      }

      if (synchronization != null)
      {
        try
        {
          Map<String, PolicyAndValue> preferenceChanges = new HashMap<String, PolicyAndValue>();
          for (URI uri : preferenceURIs)
          {
            String key = PreferencesFactory.eINSTANCE.convertURI(uri);
            if (transaction.getPolicy(key))
            {
              String value = transaction.getPreferences().get(uri);
              preferenceChanges.put(key, new PolicyAndValue(value));
            }
            else
            {
              preferenceChanges.put(key, new PolicyAndValue());
            }
          }

          Set<String> preferenceKeys = SynchronizerDialog.adjustLocalSnapshot(synchronization, preferenceChanges);

          Map<String, SyncAction> syncActions = synchronization.synchronizeLocal();
          for (SyncAction syncAction : syncActions.values())
          {
            if (syncAction.getComputedType() == SyncActionType.CONFLICT)
            {
              Map.Entry<String, String> preference = syncAction.getPreference();
              if (preference != null && preferenceKeys.contains(preference.getKey()))
              {
                dialogNeeded = true;
                break;
              }
            }
          }
        }
        catch (IOException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
      }

      if (dialogNeeded)
      {
        if (!openSynchronizerDialog(transaction, synchronization))
        {
          return;
        }
      }

      if (synchronization != null)
      {
        // Ensure that the syncIDs are committed to the recorder target.
        final Map<String, String> preferenceIDs = synchronization.getPreferenceIDs();

        transaction.setCommitHandler(new CommitHandler()
        {
          public void handlePreferenceTask(PreferenceTask preferenceTask)
          {
            String key = preferenceTask.getKey();

            String syncID = preferenceIDs.get(key);
            if (syncID != null)
            {
              preferenceTask.setID(syncID);
            }
          }
        });
      }

      Map<String, PreferenceTask> preferenceTasks = commitTransaction(transaction);

      if (synchronization != null)
      {
        Scope recorderTarget = syncInfo.getRecorderTarget();
        File syncFolder = syncInfo.getSyncFolder();
        File tmpFolder = syncInfo.getTmpFolder();

        if (!dialogNeeded)
        {
          EMap<String, SyncPolicy> remotePolicies = synchronization.getRemotePolicies();
          boolean remotePoliciesMissing = false;

          for (PreferenceTask preferenceTask : preferenceTasks.values())
          {
            String taskID = preferenceTask.getID();
            if (taskID != null)
            {
              SyncPolicy remotePolicy = remotePolicies.get(taskID);
              if (remotePolicy == null)
              {
                // Remote policy is missing.
                remotePoliciesMissing = true; // Prompt below...
                break;
              }
            }
          }

          // Copy the committed recorder target to the temporary sync folder.
          RecorderManager.copyRecorderTarget(recorderTarget, tmpFolder);

          if (remotePoliciesMissing)
          {
            if (!openSynchronizerDialog(null, synchronization)) // Requires the copyRecorderTarget() call above.
            {
              return;
            }
          }
          else
          {
            try
            {
              Map<String, SyncAction> syncActions = synchronization.synchronizeLocal(); // Requires the copyRecorderTarget() call above.
              Map<String, String> preferenceIDs = synchronization.getPreferenceIDs();

              for (Iterator<Map.Entry<String, SyncAction>> it = syncActions.entrySet().iterator(); it.hasNext();)
              {
                Map.Entry<String, SyncAction> entry = it.next();
                String syncID = entry.getKey();
                SyncAction syncAction = entry.getValue();

                SyncPolicy remotePolicy = remotePolicies.get(syncID);
                if (remotePolicy == SyncPolicy.EXCLUDE)
                {
                  it.remove();
                  continue;
                }

                SyncActionType type = syncAction.getComputedType();
                switch (type)
                {
                  case SET_REMOTE: // Ignore REMOTE -> LOCAL actions.
                  case REMOVE_REMOTE: // Ignore REMOTE -> LOCAL actions.
                  case CONFLICT: // Ignore interactive actions.
                  case EXCLUDE: // Should not occur.
                  case NONE: // Should not occur.
                    it.remove();
                    continue;
                }

                Map.Entry<String, String> preference = syncAction.getPreference();
                if (preference == null || !preferenceIDs.containsKey(preference.getKey()))
                {
                  it.remove();
                  continue;
                }
              }
            }
            catch (IOException ex)
            {
              SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
              ErrorDialog.open(ex);
              return;
            }
          }
        }

        try
        {
          applyRemotePreferenceChanges(synchronization);

          synchronization.commit();

          Synchronizer synchronizer = synchronization.getSynchronizer();
          synchronizer.copyFilesTo(syncFolder);

          copyRecorderTargetBack(recorderTarget, tmpFolder);
        }
        catch (NotCurrentException ex)
        {
          ErrorDialog.open(ex);
        }
        catch (IOException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
          ErrorDialog.open(ex);
        }
        finally
        {
          if (!SYNC_FOLDER_KEEP)
          {
            IOUtil.deleteBestEffort(tmpFolder, SYNC_FOLDER_RANDOM);
          }
        }
      }
    }
    finally
    {
      if (synchronization != null)
      {
        synchronization.dispose();
      }
    }
  }

  private SyncInfo awaitEarlySynchronization()
  {
    SyncInfo syncInfo = earlySynchronization.await();
    if (syncInfo != null)
    {
      // Check if the recorder target is still the User scope (it could have been changed meanwhile).
      Scope recorderTarget = INSTANCE.getRecorderTargetObject();
      if (recorderTarget instanceof User)
      {
        return syncInfo;
      }
    }

    return null;
  }

  private boolean openSynchronizerDialog(final RecorderTransaction transaction, final Synchronization synchronization)
  {
    final boolean[] ok = { true };
    UIUtil.syncExec(display, new Runnable()
    {
      public void run()
      {
        Shell shell = UIUtil.getShell();

        AbstractRecorderDialog dialog = new SynchronizerDialog(shell, transaction, synchronization);
        int result = dialog.open();

        if (!dialog.isEnableRecorder())
        {
          setRecorderEnabled(false);
          ok[0] = false;
        }
        else if (result != AbstractRecorderDialog.OK)
        {
          ok[0] = false;
        }
      }
    });

    return ok[0];
  }

  private void applyRemotePreferenceChanges(Synchronization synchronization)
  {
    Map<String, SyncAction> syncActions = synchronization.getActions();
    if (syncActions != null)
    {
      for (SyncAction syncAction : syncActions.values())
      {
        if (syncAction.getEffectiveType() == SyncActionType.SET_REMOTE)
        {
          SyncDelta remoteDelta = syncAction.getRemoteDelta();
          SetupTask newTask = remoteDelta.getNewTask();
          if (newTask instanceof PreferenceTask)
          {
            PreferenceTask preferenceTask = (PreferenceTask)newTask;

            try
            {
              executePreferenceTask(preferenceTask);
            }
            catch (Exception ex)
            {
              SetupUIPlugin.INSTANCE.log(ex);
            }
          }
        }
      }
    }
  }

  private static URI convertURI(URI uri)
  {
    String fragment = uri.fragment();
    if (StringUtil.isEmpty(fragment))
    {
      fragment = "/";
    }

    URI resourceURI = uri.trimFragment();
    if (resourceURI.equals(USER_FILE_URI))
    {
      resourceURI = SetupContext.USER_SETUP_URI;
    }

    return resourceURI.appendFragment(fragment);
  }

  @SuppressWarnings("restriction")
  private static boolean isPreferenceDialog(Shell shell)
  {
    Object data = shell.getData();
    return data instanceof org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
  }

  @SuppressWarnings("restriction")
  private static void hookRecorderCheckbox(final Shell shell)
  {
    try
    {
      org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog dialog = (org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog)shell.getData();
      if (dialog.buttonBar instanceof Composite)
      {
        Composite buttonBar = (Composite)dialog.buttonBar;
        Control[] children = buttonBar.getChildren();
        if (children.length != 0)
        {
          Control child = children[0];
          if (child instanceof ToolBar)
          {
            ToolBar toolBar = (ToolBar)child;

            toolItem = new ToolItem(toolBar, SWT.PUSH);
            updateRecorderCheckboxState();

            toolItem.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                boolean enableRecorder = !INSTANCE.isRecorderEnabled();
                INSTANCE.setRecorderEnabled(enableRecorder);

                updateRecorderCheckboxState();
                RecorderPreferencePage.updateEnablement();

                if (enableRecorder)
                {
                  SynchronizerManager.INSTANCE.offerFirstTimeConnect(shell);
                }
              }
            });

            toolItem.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                toolItem = null;
              }
            });

            buttonBar.layout();
          }
        }
      }
    }
    catch (Throwable ex)
    {
      // Ignore.
    }
  }

  static void updateRecorderCheckboxState()
  {
    if (toolItem != null)
    {
      boolean recorderEnabled = INSTANCE.isRecorderEnabled();
      String state = recorderEnabled ? "enabled" : "disabled";
      String verb = !recorderEnabled ? "enable" : "disable";

      toolItem.setImage(SetupUIPlugin.INSTANCE.getSWTImage("recorder_" + state));
      toolItem.setToolTipText("Oomph preference recorder " + state + " - Push to " + verb);
    }
  }

  public static boolean executePreferenceTask(PreferenceTask task) throws Exception
  {
    return ((PreferenceTaskImpl)task).execute(USER_CALLBACK);
  }

  public static File copyRecorderTarget(Scope recorderTarget, File targetFolder)
  {
    URI uri = resolveRecorderTargetURI(recorderTarget);

    File source = new File(uri.toFileString());
    File target = new File(targetFolder, uri.lastSegment());

    IOUtil.copyFile(source, target);
    return target;
  }

  private static File copyRecorderTargetBack(Scope recorderTarget, File targetFolder)
  {
    URI uri = resolveRecorderTargetURI(recorderTarget);

    File source = new File(uri.toFileString());
    File target = new File(targetFolder, uri.lastSegment());

    IOUtil.copyFile(target, source);
    return target;
  }

  private static URI resolveRecorderTargetURI(Scope recorderTarget)
  {
    Resource resource = recorderTarget.eResource();
    URIConverter uriConverter = resource.getResourceSet().getURIConverter();

    URI uri = resource.getURI();
    uri = uriConverter.normalize(uri);
    uri = SetupContext.resolveUser(uri);
    return uri;
  }

  /**
   * @author Eike Stepper
   */
  public static class Lifecycle
  {
    public static void start(Display display)
    {
      INSTANCE.display = display;
      display.addListener(SWT.Skin, INSTANCE.displayListener);
    }

    public static void stop()
    {
      INSTANCE.displayListener.stop();

      if (INSTANCE.display != null)
      {
        UIUtil.asyncExec(INSTANCE.display, new Runnable()
        {
          public void run()
          {
            if (!INSTANCE.display.isDisposed())
            {
              INSTANCE.display.removeListener(SWT.Skin, INSTANCE.displayListener);
            }
          }
        });
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DisplayListener implements Listener
  {
    private boolean stopped;

    public void stop()
    {
      stopped = true;
    }

    public void handleEvent(Event event)
    {
      if (stopped)
      {
        return;
      }

      if (event.widget instanceof Shell)
      {
        final Shell shell = (Shell)event.widget;
        if (isPreferenceDialog(shell) && toolItem == null)
        {
          UIUtil.asyncExec(display, new Runnable()
          {
            public void run()
            {
              hookRecorderCheckbox(shell);
            }
          });

          if (isRecorderEnabled())
          {
            recorder = new PreferencesRecorder();
            earlySynchronization.start(false);
          }

          shell.addDisposeListener(new DisposeListener()
          {
            public void widgetDisposed(DisposeEvent e)
            {
              if (recorder == null)
              {
                return;
              }

              final Map<URI, String> values = recorder.done();
              recorder = null;
              for (Iterator<URI> it = values.keySet().iterator(); it.hasNext();)
              {
                URI uri = it.next();
                String pluginID = uri.segment(0);

                if (SetupUIPlugin.PLUGIN_ID.equals(pluginID))
                {
                  String lastSegment = uri.lastSegment();
                  if (SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER.equals(lastSegment) || SetupUIPlugin.PREF_PREFERENCE_RECORDER_TARGET.equals(lastSegment))
                  {
                    it.remove();
                  }
                }
              }

              if (values.isEmpty())
              {
                RecorderTransaction transaction = RecorderTransaction.getInstance();
                if (transaction != null)
                {
                  // Close a transaction that has been opened by the RecorderPreferencePage.
                  closeTransaction(transaction);
                }
              }
              else
              {
                Job job = new Job("Store preferences")
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    handleRecording(editor, values);
                    return Status.OK_STATUS;
                  }
                };

                job.setSystem(true);
                job.schedule();
              }
            }
          });
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class EarlySynchronization
  {
    private Scope recorderTarget;

    private File syncFolder;

    private File tmpFolder;

    private SynchronizerJob synchronizerJob;

    private long startTime;

    public EarlySynchronization()
    {
    }

    public boolean start(boolean force)
    {
      if (!SynchronizerManager.ENABLED)
      {
        return false;
      }

      if (synchronizerJob == null && SynchronizerManager.INSTANCE.isSyncEnabled())
      {
        recorderTarget = INSTANCE.getRecorderTargetObject();
        if (recorderTarget instanceof User)
        {
          tmpFolder = null;

          if (!SYNC_FOLDER_RANDOM)
          {
            try
            {
              tmpFolder = new File(PropertiesUtil.getTmpDir(), "oomph.setup.sync");
              tmpFolder.mkdirs();

              File[] tmpFiles = tmpFolder.listFiles();
              if (tmpFiles != null)
              {
                for (File file : tmpFiles)
                {
                  SyncUtil.deleteFile(file);
                }
              }
            }
            catch (IOException ex)
            {
              tmpFolder = null;
              SetupUIPlugin.INSTANCE.log(ex);
            }
          }

          if (tmpFolder == null)
          {
            tmpFolder = IOUtil.createTempFolder("sync-", true);
          }

          SynchronizerService service = SynchronizerManager.INSTANCE.getService();
          syncFolder = service.getSyncFolder();

          File[] files = syncFolder.listFiles();
          if (files != null)
          {
            for (File file : files)
            {
              IOUtil.copyTree(file, new File(tmpFolder, file.getName()));
            }
          }

          File target = RecorderManager.copyRecorderTarget(recorderTarget, tmpFolder);
          DataProvider local = new LocalDataProvider(target);

          try
          {
            DataProvider remote = service.createDataProvider(force ? UICredentialsProvider.INSTANCE : null);
            Synchronizer synchronizer = new Synchronizer(local, remote, tmpFolder);

            synchronizerJob = new SynchronizerJob(synchronizer, true);
            synchronizerJob.setService(service);
            synchronizerJob.schedule();

            startTime = System.currentTimeMillis();
          }
          catch (AuthorizationRequiredException ex)
          {
            if (force)
            {
              SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
            }
          }
        }
      }

      return synchronizerJob != null;
    }

    public void stop()
    {
      if (!SynchronizerManager.ENABLED)
      {
        return;
      }

      if (synchronizerJob != null)
      {
        synchronizerJob.cancel();
        synchronizerJob = null;
      }
    }

    public SyncInfo await()
    {
      if (!SynchronizerManager.ENABLED)
      {
        return null;
      }

      if (synchronizerJob != null)
      {
        final SyncInfo result = new SyncInfo();
        result.recorderTarget = recorderTarget;
        result.syncFolder = syncFolder;
        result.tmpFolder = tmpFolder;
        result.synchronization = synchronizerJob.getSynchronization();

        if (result.synchronization == null)
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
                      long timeout = SynchronizerManager.TIMEOUT - (System.currentTimeMillis() - startTime);
                      result.synchronization = await(serviceLabel, timeout, monitor);
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

            if (result.synchronization == null && !canceled.get())
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
          finally
          {
            synchronizerJob = null;
          }
        }

        return result;
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

  /**
   * @author Eike Stepper
   */
  private static final class SyncInfo
  {
    private Scope recorderTarget;

    private File syncFolder;

    private File tmpFolder;

    private Synchronization synchronization;

    public SyncInfo()
    {
    }

    public Scope getRecorderTarget()
    {
      return recorderTarget;
    }

    public File getSyncFolder()
    {
      return syncFolder;
    }

    public File getTmpFolder()
    {
      return tmpFolder;
    }

    public Synchronization getSynchronization()
    {
      return synchronization;
    }

    @Override
    public String toString()
    {
      return SyncInfo.class.getSimpleName() + "[" + EcoreUtil.getURI(recorderTarget) + " --> " + syncFolder + "]";
    }
  }
}

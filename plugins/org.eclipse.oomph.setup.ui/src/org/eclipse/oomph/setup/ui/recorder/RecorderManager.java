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
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesRecorder;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.PreferenceTaskImpl;
import org.eclipse.oomph.setup.impl.PreferenceTaskImpl.PreferenceHandler;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob.FinishHandler;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction.CommitHandler;
import org.eclipse.oomph.setup.ui.synchronizer.OptOutDialog;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerDialog;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerDialog.PolicyAndValue;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.ui.ButtonAnimator;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;
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
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
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
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.userstorage.IStorage;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.spi.ICredentialsProvider;
import org.eclipse.userstorage.util.ProtocolException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
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

  private static final URI USER_FILE_URI = SetupContext.resolve(SetupCoreUtil.createResourceSet().getURIConverter().normalize(SetupContext.USER_SETUP_URI));

  private static final boolean SYNC_FOLDER_FIXED = PropertiesUtil.isProperty("oomph.setup.sync.folder.fixed");

  private static final boolean SYNC_FOLDER_KEEP = PropertiesUtil.isProperty("oomph.setup.sync.folder.keep");

  private static final boolean SYNC_FOLDER_DEBUG = PropertiesUtil.isProperty("oomph.setup.sync.folder.debug");

  private final EarlySynchronization earlySynchronization = new EarlySynchronization();

  private static ToolItem recordItem;

  private static ToolItem initializeItem;

  private final DisplayListener displayListener = new DisplayListener();

  private Display display;

  private PreferencesRecorder recorder;

  private IEditorPart editor;

  private URI temporaryRecorderTarget;

  private Runnable reset;

  private RecorderManager()
  {
  }

  public void record(IEditorPart editor)
  {
    this.editor = editor;

    final boolean wasEnabled = isRecorderEnabled();
    setRecorderEnabled(true);

    // Defer this until the transaction has been processed.
    reset = new Runnable()
    {
      public void run()
      {
        RecorderManager.this.editor = null;
        setRecorderEnabled(wasEnabled);
        reset = null;
      }
    };

    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, null, null, null);
    dialog.open();
  }

  public void done()
  {
    setTemporaryRecorderTarget(null);
    if (reset != null)
    {
      reset.run();
    }
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

          startEarlySynchronization(false);
        }
        else
        {
          cancelRecording();
        }
      }
    }
  }

  public Set<String> getInitializedPreferencePages()
  {
    return getIDs(SetupUIPlugin.PREF_INITIALIZED_PREFERENCE_PAGES);
  }

  public void setInitializedPreferencePages(Set<String> ids)
  {
    setIDs(SetupUIPlugin.PREF_INITIALIZED_PREFERENCE_PAGES, ids);
  }

  public Set<String> getIgnoredPreferencePages()
  {
    return getIDs(SetupUIPlugin.PREF_IGNORED_PREFERENCE_PAGES);
  }

  public void setIgnoredPreferencePages(Set<String> ids)
  {
    setIDs(SetupUIPlugin.PREF_IGNORED_PREFERENCE_PAGES, ids);
  }

  private Set<String> getIDs(String key)
  {
    Set<String> result = new LinkedHashSet<String>();
    String value = SETUP_UI_PREFERENCES.getString(key);
    if (!StringUtil.isEmpty(value))
    {
      for (String id : value.split(" "))
      {
        result.add(id);
      }
    }

    return result;
  }

  private void setIDs(String key, Set<String> ids)
  {
    StringBuilder result = new StringBuilder();
    for (String id : ids)
    {
      if (result.length() != 0)
      {
        result.append(' ');
      }

      result.append(id);
    }

    SETUP_UI_PREFERENCES.setValue(key, result.toString());

    try
    {
      SETUP_UI_PREFERENCES.save();
    }
    catch (IOException ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
  }

  public void cancelRecording()
  {
    if (recorder != null)
    {
      recorder.done();
      recorder = null;
    }

    earlySynchronization.stop();
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
    if (temporaryRecorderTarget != null)
    {
      return temporaryRecorderTarget;
    }

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

  public void setTemporaryRecorderTarget(URI temporaryRecorderTarget)
  {
    this.temporaryRecorderTarget = temporaryRecorderTarget;
  }

  public boolean hasTemporaryRecorderTarget()
  {
    return temporaryRecorderTarget != null;
  }

  public boolean startEarlySynchronization(boolean interactive)
  {
    return earlySynchronization.start(interactive);
  }

  private SyncInfo awaitEarlySynchronization()
  {
    SyncInfo syncInfo = earlySynchronization.await();
    if (syncInfo != null)
    {
      // Check if the recorder target is still the User scope (it could have been changed meanwhile).
      Scope recorderTarget = getRecorderTargetObject();
      if (recorderTarget instanceof User)
      {
        return syncInfo;
      }
    }

    return null;
  }

  private void handleRecording(IEditorPart editorPart, Map<URI, Pair<String, String>> values)
  {
    try
    {
      SynchronizerManager.INSTANCE.offerFirstTimeConnect(UIUtil.getShell());

      RecorderTransaction transaction = editorPart == null ? RecorderTransaction.open() : RecorderTransaction.open(editorPart);
      transaction.setPreferences(values);

      // In some cases (such as changing the recorder target in the current recorder transaction or missing credentials)
      // early synchronization has not been started, yet. We want to be safe and try to start it now (has no effect if already started).
      boolean started = startEarlySynchronization(true);

      SyncInfo syncInfo = started ? awaitEarlySynchronization() : null;
      Synchronization synchronization = syncInfo == null ? null : syncInfo.getSynchronization();

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
          PreferenceHandler handler = PreferenceTaskImpl.PreferenceHandler.getHandler(uri);
          if (handler.isIgnored())
          {
            // Handler policy is "ignore".
            it.remove(); // Remove the preference change from the transaction.
          }
          else
          {
            // Handler policy is missing.
            transaction.setPolicy(key, true); // Default to "record".
            dialogNeeded = true; // And prompt below...
          }
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
              String value = transaction.getPreferences().get(uri).getElement2();
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
          transaction.close();
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

      try
      {
        transaction.commit();
      }
      finally
      {
        transaction.close();
      }

      if (synchronization != null)
      {
        Scope recorderTarget = syncInfo.getRecorderTarget();
        File tmpFolder = syncInfo.getTmpFolder();

        if (!dialogNeeded)
        {
          EMap<String, SyncPolicy> remotePolicies = synchronization.getRemotePolicies();
          boolean remotePoliciesMissing = false;

          for (Iterator<PreferenceTask> it = transaction.getCommitResult().values().iterator(); it.hasNext();)
          {
            PreferenceTask preferenceTask = it.next();

            String taskID = preferenceTask.getID();
            if (taskID != null)
            {
              SyncPolicy remotePolicy = remotePolicies.get(taskID);
              if (remotePolicy == null)
              {
                // Remote policy is missing.
                remotePoliciesMissing = true; // Prompt below...
              }
              else if (remotePolicy == SyncPolicy.EXCLUDE)
              {
                it.remove();
              }
            }
            else
            {
              it.remove();
            }
          }

          // Copy the committed recorder target to the temporary sync folder.
          copyRecorderTarget(recorderTarget, tmpFolder);

          if (remotePoliciesMissing)
          {
            if (!openSynchronizerDialog(transaction, synchronization)) // Requires the copyRecorderTarget() call above.
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
          synchronizer.copyFilesTo(SynchronizerManager.SYNC_FOLDER);

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
      }
    }
    finally
    {
      earlySynchronization.stop();
    }
  }

  private boolean openSynchronizerDialog(final RecorderTransaction transaction, final Synchronization synchronization)
  {
    final boolean[] ok = { true };
    UIUtil.syncExec(display, new Runnable()
    {
      public void run()
      {
        Shell shell = UIUtil.getShell();

        SynchronizerDialog dialog = new SynchronizerDialog(shell, transaction, synchronization);
        int result = dialog.open();

        if (!dialog.isEnableRecorder())
        {
          setRecorderEnabled(false);
          ok[0] = false;
        }
        else if (result != SynchronizerDialog.OK)
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
  private void hookRecorderToggleButton(final Shell shell)
  {
    try
    {
      final org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog dialog = (org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog)shell.getData();
      if (dialog.buttonBar instanceof Composite)
      {
        final Composite buttonBar = (Composite)dialog.buttonBar;
        Control[] children = buttonBar.getChildren();
        if (children.length != 0)
        {
          Control child = children[0];
          if (child instanceof ToolBar)
          {
            final ToolBar toolBar = (ToolBar)child;

            recordItem = new ToolItem(toolBar, SWT.PUSH);
            updateRecorderToggleButton();

            final PreferenceManager preferenceManager = dialog.getPreferenceManager();
            recordItem.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                boolean enableRecorder = !isRecorderEnabled();
                setRecorderEnabled(enableRecorder);

                updateRecorderToggleButton();
                RecorderPreferencePage.updateEnablement();

                if (enableRecorder)
                {
                  boolean firstTime = SynchronizerManager.INSTANCE.offerFirstTimeConnect(shell);
                  startEarlySynchronization(firstTime);

                  createInitializeItem(shell, toolBar, dialog, preferenceManager);
                  buttonBar.layout();
                }
                else if (initializeItem != null)
                {
                  initializeItem.dispose();
                }
              }
            });

            recordItem.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                recordItem = null;
              }
            });

            if (isRecorderEnabled())
            {
              createInitializeItem(shell, toolBar, dialog, preferenceManager);
            }

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

  void disposeInitializeItem()
  {
    if (initializeItem != null)
    {
      initializeItem.dispose();
    }
  }

  @SuppressWarnings("restriction")
  private void createInitializeItem(final Shell shell, ToolBar toolBar, final org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog dialog,
      final PreferenceManager preferenceManager)
  {
    if (hasPreferencePagesToInitialize(preferenceManager))
    {
      initializeItem = new ToolItem(toolBar, SWT.PUSH);
      initializeItem.setImage(SetupUIPlugin.INSTANCE.getSWTImage("bulb0.png"));
      initializeItem.setToolTipText("Initialize preference pages");

      final class Animator extends ButtonAnimator
      {
        public Animator(ToolItem toolItem)
        {
          super(SetupUIPlugin.INSTANCE, toolItem, "bulb.png", 8);
        }

        @Override
        public Shell getShell()
        {
          return shell;
        }

        @Override
        protected boolean doAnimate()
        {
          return true;
        }
      }

      new Animator(initializeItem).run();

      initializeItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          new PreferenceInitializationDialog(dialog, preferenceManager).open();
        }
      });

      initializeItem.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          initializeItem = null;
        }
      });
    }
  }

  private boolean hasPreferencePagesToInitialize(PreferenceManager preferenceManager)
  {
    Set<String> preferencePages = getInitializedPreferencePages();
    preferencePages.addAll(getIgnoredPreferencePages());
    List<IPreferenceNode> preferenceNodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER);
    for (IPreferenceNode element : preferenceNodes)
    {
      String id = element.getId();
      if (!preferencePages.contains(id))
      {
        return true;
      }
    }

    return false;
  }

  static void updateRecorderToggleButton()
  {
    if (recordItem != null)
    {
      boolean recorderEnabled = INSTANCE.isRecorderEnabled();
      String state = recorderEnabled ? "enabled" : "disabled";
      String verb = !recorderEnabled ? "enable" : "disable";

      recordItem.setImage(SetupUIPlugin.INSTANCE.getSWTImage("recorder_" + state));
      recordItem.setToolTipText("Oomph preference recorder " + state + " - Push to " + verb);
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

    if (SYNC_FOLDER_DEBUG)
    {

      SetupUIPlugin.INSTANCE.log("Copy recorder target to " + target);
    }

    return target;
  }

  private static File copyRecorderTargetBack(Scope recorderTarget, File targetFolder)
  {
    URI uri = resolveRecorderTargetURI(recorderTarget);

    File source = new File(uri.toFileString());
    File target = new File(targetFolder, uri.lastSegment());

    IOUtil.copyFile(target, source);

    if (SYNC_FOLDER_DEBUG)
    {
      SetupUIPlugin.INSTANCE.log("Copy recorder target back to " + source);
    }

    return target;
  }

  private static URI resolveRecorderTargetURI(Scope recorderTarget)
  {
    Resource resource = recorderTarget.eResource();
    URIConverter uriConverter = resource.getResourceSet().getURIConverter();

    URI uri = resource.getURI();
    uri = uriConverter.normalize(uri);
    uri = SetupContext.resolve(uri);
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
        if (isPreferenceDialog(shell) && recordItem == null)
        {
          UIUtil.asyncExec(display, new Runnable()
          {
            public void run()
            {
              hookRecorderToggleButton(shell);
            }
          });

          if (isRecorderEnabled())
          {
            recorder = new PreferencesRecorder();
            startEarlySynchronization(false);
          }

          shell.addDisposeListener(new DisposeListener()
          {
            public void widgetDisposed(DisposeEvent e)
            {
              final PreferencesRecorder finalRecorder = recorder;
              if (finalRecorder == null)
              {
                return;
              }

              UIUtil.asyncExec(new Runnable()
              {
                public void run()
                {
                  final Map<URI, Pair<String, String>> values = finalRecorder.done();
                  recorder = null;
                  for (Iterator<URI> it = values.keySet().iterator(); it.hasNext();)
                  {
                    URI uri = it.next();
                    String pluginID = uri.segment(0);

                    if (SetupUIPlugin.PLUGIN_ID.equals(pluginID))
                    {
                      String lastSegment = uri.lastSegment();
                      if (SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER.equals(lastSegment) //
                          || SetupUIPlugin.PREF_PREFERENCE_RECORDER_TARGET.equals(lastSegment) //
                          || SetupUIPlugin.PREF_IGNORED_PREFERENCE_PAGES.equals(lastSegment) //
                          || SetupUIPlugin.PREF_INITIALIZED_PREFERENCE_PAGES.equals(lastSegment))
                      {
                        it.remove();
                      }
                    }
                  }

                  if (values.isEmpty())
                  {
                    earlySynchronization.stop();
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
          });
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class EarlySynchronization implements FinishHandler
  {
    private Scope recorderTarget;

    private File tmpFolder;

    private SynchronizerJob synchronizerJob;

    public EarlySynchronization()
    {
    }

    public boolean start(boolean interactive)
    {
      if (!SynchronizerManager.ENABLED)
      {
        return false;
      }

      if (synchronizerJob == null && SynchronizerManager.INSTANCE.isSyncEnabled())
      {
        IStorage storage = SynchronizerManager.INSTANCE.getStorage();
        IStorageService service = storage.getService();
        if (service == null)
        {
          return false;
        }

        recorderTarget = INSTANCE.getRecorderTargetObject();
        if (recorderTarget instanceof User)
        {
          tmpFolder = null;

          if (SYNC_FOLDER_FIXED)
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

          if (SYNC_FOLDER_DEBUG)
          {
            SetupUIPlugin.INSTANCE.log("Early synchronization in " + tmpFolder);
          }

          File target = RecorderManager.copyRecorderTarget(recorderTarget, tmpFolder);

          Synchronizer synchronizer = SynchronizerManager.INSTANCE.createSynchronizer(target, tmpFolder);
          synchronizer.copyFilesFrom(SynchronizerManager.SYNC_FOLDER);

          synchronizerJob = new SynchronizerJob(synchronizer, true);
          synchronizerJob.setService(service);

          if (interactive)
          {
            synchronizerJob.setFinishHandler(this);
          }
          else
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
      if (!SynchronizerManager.ENABLED)
      {
        return;
      }

      if (synchronizerJob != null)
      {
        synchronizerJob.stopSynchronization();
        synchronizerJob = null;

        if (!SYNC_FOLDER_KEEP)
        {
          boolean deleted = IOUtil.deleteBestEffort(tmpFolder, !SYNC_FOLDER_FIXED);

          if (SYNC_FOLDER_DEBUG)
          {
            if (deleted)
            {
              SetupUIPlugin.INSTANCE.log("Deleted " + tmpFolder);
            }
            else
            {
              SetupUIPlugin.INSTANCE.log("Failed to delete " + tmpFolder);
            }
          }
        }
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
        result.tmpFolder = tmpFolder;
        result.synchronization = synchronizerJob.getSynchronization();

        if (result.synchronization == null)
        {
          Throwable earlyException = synchronizerJob.getException();
          if (earlyException instanceof OperationCanceledException)
          {
            // This means that the user couldn't be authenticated. Try again in UI thread below.
            stop();
            result.synchronization = null;

            start(true);
            result.tmpFolder = tmpFolder;
          }
          else if (earlyException != null)
          {
            SetupUIPlugin.INSTANCE.log(earlyException, IStatus.WARNING);
            return null;
          }

          try
          {
            final AtomicBoolean canceled = new AtomicBoolean();
            final IStorageService service = synchronizerJob.getService();

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

                      String serviceLabel = service.getServiceLabel();
                      result.synchronization = await(serviceLabel, monitor);
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
        }

        return result;
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

    public void handleFinish(Throwable ex) throws Exception
    {
      if (ex instanceof ProtocolException)
      {
        ProtocolException protocolException = (ProtocolException)ex;
        if (protocolException.getStatusCode() == 401)
        {
          UIUtil.syncExec(new Runnable()
          {
            public void run()
            {
              OptOutDialog dialog = new OptOutDialog(UIUtil.getShell(), synchronizerJob.getService());
              dialog.open();
              if (!dialog.getAnswer())
              {
                SynchronizerManager.INSTANCE.setSyncEnabled(false);
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
  private static final class SyncInfo
  {
    private Scope recorderTarget;

    private File tmpFolder;

    private Synchronization synchronization;

    public SyncInfo()
    {
    }

    public Scope getRecorderTarget()
    {
      return recorderTarget;
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
      return SyncInfo.class.getSimpleName() + "[" + EcoreUtil.getURI(recorderTarget) + " --> " + SynchronizerManager.SYNC_FOLDER + "]";
    }
  }
}

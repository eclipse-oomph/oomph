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

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesRecorder;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.DataProvider;
import org.eclipse.oomph.setup.internal.sync.LocalDataProvider;
import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.internal.sync.Synchronizer;
import org.eclipse.oomph.setup.internal.sync.SynchronizerJob;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerDialog;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
public final class RecorderManager
{
  public static final RecorderManager INSTANCE = new RecorderManager();

  private static final IPersistentPreferenceStore SETUP_UI_PREFERENCES = (IPersistentPreferenceStore)SetupUIPlugin.INSTANCE.getPreferenceStore();

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

          earlySynchronization.start();
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
    URIConverter uriConverter = resourceSet.getURIConverter();
    URI recorderTarget = getRecorderTarget();

    URI uri = normalize(uriConverter, recorderTarget);
    return (Scope)resourceSet.getEObject(uri, true);
  }

  public URI getRecorderTarget()
  {
    String value = SETUP_UI_PREFERENCES.getString(SetupUIPlugin.PREF_PREFERENCE_RECORDER_TARGET);
    if (StringUtil.isEmpty(value))
    {
      return SetupContext.USER_SETUP_URI;
    }

    return URI.createURI(value);
  }

  public URI setRecorderTarget(URI uri)
  {
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

  private void handleRecording(IEditorPart editorPart, final Shell shell, final Map<URI, String> values)
  {
    final RecorderTransaction transaction = editorPart == null ? RecorderTransaction.open() : RecorderTransaction.open(user, editorPart);

    try
    {
      for (Iterator<URI> it = values.keySet().iterator(); it.hasNext();)
      {
        URI uri = it.next();

        String path = PreferencesFactory.eINSTANCE.convertURI(uri);
        Boolean policy = transaction.getPolicy(path);
        if (policy == null)
        {
          transaction.setPolicy(path, true);
        }
        else if (!policy)
        {
          it.remove();
        }
      }

      earlySynchronization.start();

      if (transaction.isDirty())
      {
        final Synchronization synchronization = earlySynchronization.await();

        final boolean[] exitEarly = { false };
        UIUtil.syncExec(display, new Runnable()
        {
          public void run()
          {
            AbstractRecorderDialog dialog = createDialog();
            int result = dialog.open();

            if (!dialog.isEnableRecorder())
            {
              setRecorderEnabled(false);
              exitEarly[0] = true;
            }
            else if (result != AbstractRecorderDialog.OK)
            {
              exitEarly[0] = true;
            }
          }

          private AbstractRecorderDialog createDialog()
          {
            // if (synchronization == null)
            // {
            // return new RecorderPoliciesDialog(shell, transaction, values);
            // }

            return new SynchronizerDialog(shell, transaction, values, synchronization);
          }
        });

        if (exitEarly[0])
        {
          return;
        }
      }

      transaction.setPreferences(values);
      transaction.commit();

      Mars1.offerSynchronizer();
    }
    finally
    {
      closeTransaction(transaction);
    }
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

  public static URI normalize(URIConverter uriConverter, URI uri)
  {
    uri = uriConverter.normalize(uri);
    uri = SetupContext.resolveUser(uri);

    if (StringUtil.isEmpty(uri.fragment()))
    {
      uri = uri.appendFragment("/");
    }

    return uri;
  }

  public static File copyRecorderTarget(Scope recorderTarget, File tmpFolder)
  {
    URI uri = recorderTarget.eResource().getURI();
    File source = new File(uri.toFileString());
    File target = new File(tmpFolder, uri.lastSegment());
    IOUtil.copyFile(source, target);
    return target;
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
            earlySynchronization.start();
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

                Mars1.offerSynchronizer();
              }
              else
              {
                Job job = new Job("Store preferences")
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    handleRecording(editor, shell, values);
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
    private static final boolean DEBUG = false;

    private SynchronizerJob synchronizerJob;

    public void start()
    {
      if (!SynchronizerManager.ENABLED)
      {
        Mars1.checkAvailability();
        return;
      }

      if (synchronizerJob == null && SynchronizerManager.INSTANCE.isSyncEnabled())
      {
        ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
        Scope recorderTarget = INSTANCE.getRecorderTargetObject(resourceSet);

        if (recorderTarget instanceof User)
        {
          File tmpFolder;

          if (DEBUG)
          {
            tmpFolder = new File("C:/Users/Stepper/AppData/Local/Temp/sync");
            tmpFolder.mkdirs();

            File[] tmpFiles = tmpFolder.listFiles();
            if (tmpFiles != null)
            {
              for (File file : tmpFiles)
              {
                try
                {
                  SyncUtil.deleteFile(file);
                }
                catch (IOException ex)
                {
                  ex.printStackTrace();
                }
              }
            }
          }
          else
          {
            tmpFolder = IOUtil.createTempFolder("sync-", true);
          }

          SynchronizerService service = SynchronizerManager.INSTANCE.getService();
          File syncFolder = service.getSyncFolder();

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
          DataProvider remote = service.createDataProvider();
          Synchronizer synchronizer = new Synchronizer(local, remote, tmpFolder);

          synchronizerJob = new SynchronizerJob(synchronizer, true);
          synchronizerJob.setService(service);
          synchronizerJob.schedule();
        }
      }
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

    public Synchronization await()
    {
      if (!SynchronizerManager.ENABLED)
      {
        return null;
      }

      if (synchronizerJob != null)
      {
        final AtomicReference<Synchronization> synchronization = new AtomicReference<Synchronization>(synchronizerJob.getSynchronization());
        if (synchronization.get() == null)
        {
          SynchronizerService service = synchronizerJob.getService();
          final String serviceLabel = service == null ? "remote service" : service.getLabel();

          try
          {
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress()
            {
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                synchronization.set(await(serviceLabel, 2000, monitor));
              }
            });

            if (synchronization.get() == null)
            {
              ProgressMonitorDialog dialog = new ProgressMonitorDialog(UIUtil.getShell());
              dialog.run(true, true, new IRunnableWithProgress()
              {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                  synchronization.set(await(serviceLabel, 8000, monitor));
                }
              });
            }

            if (synchronization.get() == null)
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

        return synchronization.get();
      }

      return null;
    }

    private Synchronization await(String serviceLabel, int timeout, IProgressMonitor monitor)
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
  private static final class Mars1
  {
    private static final URI PROPERTIES_URI = URI.createURI("http://download.eclipse.org/oomph/epp/mars/mars1.sync.properties");

    private static final File PROPERTIES = SetupUIPlugin.INSTANCE.getConfigurationLocation().append("mars1.sync.properties").toFile();

    private static final String TITLE = "Oomph Preference Synchronizer";

    private static final String INSTALLED = "update.installed";

    private static final String REJECTED = "update.rejected";

    private static boolean checked;

    private static boolean available;

    public static void checkAvailability()
    {
      if (checked || available)
      {
        return;
      }

      checked = true;

      if (PROPERTIES.isFile())
      {
        available = true;
        return;
      }

      Job job = new Job("Synchronizer availability check")
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          InputStream in = null;
          OutputStream out = null;

          try
          {
            Map<Object, Object> options = new HashMap<Object, Object>();
            options.put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_IGNORE);

            in = SetupCoreUtil.createResourceSet().getURIConverter().createInputStream(PROPERTIES_URI, options);

            PROPERTIES.getParentFile().mkdirs();
            out = new FileOutputStream(PROPERTIES);

            IOUtil.copy(in, out);
            available = true;
          }
          catch (Throwable ex)
          {
            IOUtil.closeSilent(out);
            IOUtil.deleteBestEffort(PROPERTIES, true);
            out = null;
          }
          finally
          {
            IOUtil.closeSilent(out);
            IOUtil.closeSilent(in);
          }

          return Status.OK_STATUS;
        }
      };

      job.setSystem(true);
      job.schedule();
    }

    public static void offerSynchronizer()
    {
      if (!available)
      {
        return;
      }

      try
      {
        final Map<String, String> properties = PropertiesUtil.loadProperties(PROPERTIES);
        if (properties != null && !"true".equals(properties.get(REJECTED)))
        {
          Shell shell = UIUtil.getShell();
          if (MessageDialog.openQuestion(shell, TITLE, "Since " + properties.get("available.date")
              + " you can synchronize your preferences with your Eclipse.org account. Do you want to update Eclipse and use this new service?"))
          {
            final ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
            dialog.run(true, true, new IRunnableWithProgress()
            {
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                try
                {
                  Agent agent = P2Util.getAgentManager().getCurrentAgent();
                  Profile profile = agent.getCurrentProfile();
                  if (profile.change().commit(monitor))
                  {
                    properties.put(INSTALLED, "true");
                    PropertiesUtil.saveProperties(PROPERTIES, properties, false);

                    MessageDialog.openInformation(dialog.getShell(), TITLE, "Updates were installed. Press OK to restart.");
                    PlatformUI.getWorkbench().restart();
                  }
                  else
                  {
                    MessageDialog.openInformation(dialog.getShell(), TITLE, "No updates were installed.");
                  }
                }
                catch (Throwable ex)
                {
                  SetupUIPlugin.INSTANCE.log(ex);

                  if (!MessageDialog.openQuestion(dialog.getShell(), TITLE, "An error occured during the update. Do you want to try again later?"))
                  {
                    properties.put(REJECTED, "true");
                    PropertiesUtil.saveProperties(PROPERTIES, properties, false);
                  }
                }
              }
            });
          }
          else
          {
            properties.put(REJECTED, "true");
            PropertiesUtil.saveProperties(PROPERTIES, properties, false);
          }
        }
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }
    }
  }
}

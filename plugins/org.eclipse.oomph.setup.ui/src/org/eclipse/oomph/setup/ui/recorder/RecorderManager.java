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
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.ui.dialogs.PreferencesUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class RecorderManager
{
  public static final RecorderManager INSTANCE = new RecorderManager();

  private static final IPersistentPreferenceStore SETUP_UI_PREFERENCES = (IPersistentPreferenceStore)SetupUIPlugin.INSTANCE.getPreferenceStore();

  private static ToolItem toolItem;

  private final DisplayListener displayListener = new DisplayListener();

  private Display display;

  private PreferencesRecorder recorder;

  private IEditorPart editor;

  private RecorderManager()
  {
  }

  public void record(IEditorPart editor)
  {
    this.editor = editor;

    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, null, null, null);
    boolean recorderEnabled = isRecorderEnabled();
    setRecorderEnabled(true);
    try
    {
      dialog.open();
    }
    finally
    {
      this.editor = null;
      setRecorderEnabled(recorderEnabled);
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
        }
        else
        {
          if (recorder != null)
          {
            recorder.done();
            recorder = null;
          }
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

  private void handleRecording(IEditorPart editorPart, final Shell shell, final Map<URI, String> values)
  {
    final RecorderTransaction transaction = editorPart == null ? RecorderTransaction.open() : RecorderTransaction.open(editorPart);

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

      if (transaction.isDirty())
      {
        final boolean[] exitEarly = { false };
        UIUtil.syncExec(display, new Runnable()
        {
          public void run()
          {
            RecorderPoliciesDialog reviewDialog = new RecorderPoliciesDialog(shell, transaction, values);
            int result = reviewDialog.open();

            if (!reviewDialog.isEnablePreferenceRecorder())
            {
              setRecorderEnabled(false);
              exitEarly[0] = true;
            }
            else if (result != RecorderPoliciesDialog.OK)
            {
              exitEarly[0] = true;
            }
          }
        });

        if (exitEarly[0])
        {
          return;
        }
      }

      transaction.setPreferences(values);
      transaction.commit();
    }
    finally
    {
      transaction.close();
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
                boolean recorderEnabled = INSTANCE.isRecorderEnabled();
                INSTANCE.setRecorderEnabled(!recorderEnabled);

                updateRecorderCheckboxState();
                RecorderPreferencePage.updateEnablement();
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

                if (SetupUIPlugin.PLUGIN_ID.equals(pluginID) && SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER.equals(uri.lastSegment()))
                {
                  it.remove();
                }
              }
              if (values.isEmpty())
              {
                RecorderTransaction transaction = RecorderTransaction.getInstance();
                if (transaction != null)
                {
                  // Close a transaction that has been opened by the RecorderPreferencePage.
                  transaction.close();
                }
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
}

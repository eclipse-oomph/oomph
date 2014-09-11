/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
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

import org.osgi.service.prefs.BackingStoreException;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class RecorderManager
{
  public static final RecorderManager INSTANCE = new RecorderManager();

  private static final String SETUP_UI_NAME = SetupUIPlugin.INSTANCE.getSymbolicName();

  private static final IEclipsePreferences SETUP_UI_PREFERENCES = (IEclipsePreferences)SetupUIPlugin.INSTANCE.getInstancePreferences();

  private static ToolItem toolItem;

  private final Listener displayListener = new DisplayListener();

  private Display display;

  private PreferencesRecorder recorder;

  private RecorderManager()
  {
  }

  public boolean isRecorderEnabled()
  {
    return SETUP_UI_PREFERENCES.getBoolean(SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER, true);
  }

  public void setRecorderEnabled(boolean enabled)
  {
    if (isRecorderEnabled() != enabled)
    {
      SETUP_UI_PREFERENCES.putBoolean(SetupUIPlugin.PREF_ENABLE_PREFERENCE_RECORDER, enabled);

      try
      {
        SETUP_UI_PREFERENCES.flush();
      }
      catch (BackingStoreException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
      finally
      {
        if (isPreferenceDialog(display.getActiveShell()))
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
  }

  private void handleRecording(final Shell shell, final Map<URI, String> values)
  {
    final RecorderTransaction transaction = RecorderTransaction.open();

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
            if (reviewDialog.open() != RecorderPoliciesDialog.OK)
            {
              exitEarly[0] = true;
              return;
            }

            if (!reviewDialog.isEnablePreferenceRecorder())
            {
              setRecorderEnabled(false);
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
  private static void hookRecorderCheckbox(Shell shell)
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
      UIUtil.syncExec(INSTANCE.display, new Runnable()
      {
        public void run()
        {
          INSTANCE.display.removeListener(SWT.Skin, INSTANCE.displayListener);
        }
      });

      INSTANCE.display = null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class DisplayListener implements Listener
  {
    public void handleEvent(Event event)
    {
      if (event.widget instanceof Shell)
      {
        final Shell shell = (Shell)event.widget;
        if (isPreferenceDialog(shell))
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

              for (Iterator<URI> it = values.keySet().iterator(); it.hasNext();)
              {
                URI uri = it.next();
                String pluginID = uri.segment(0);

                if (SETUP_UI_NAME.equals(pluginID))
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
                    handleRecording(shell, values);
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

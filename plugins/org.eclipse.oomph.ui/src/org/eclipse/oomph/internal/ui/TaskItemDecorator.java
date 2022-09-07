/*
 * Copyright (c) 2014, 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TaskBar;
import org.eclipse.swt.widgets.TaskItem;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 *
 * @author Ed Merks
 */
public class TaskItemDecorator
{
  private static final IWorkbench WORKBENCH = PlatformUI.getWorkbench();

  private static final TaskBar SYSTEM_TASK_BAR = WORKBENCH.getDisplay().getSystemTaskBar();

  public static final String IDE_NODE = "org.eclipse.ui.ide"; //$NON-NLS-1$

  public static final String WORKSPACE_NAME = "WORKSPACE_NAME"; //$NON-NLS-1$

  private static final String SHOW_LOCATION_NAME = "SHOW_LOCATION_NAME"; //$NON-NLS-1$

  private static final IEclipsePreferences DEFAULT_IDE_PREFERENCES = DefaultScope.INSTANCE.getNode(IDE_NODE);

  private static final IEclipsePreferences IDE_PREFERENCES = InstanceScope.INSTANCE.getNode(IDE_NODE);

  public TaskItemDecorator()
  {
    if (SYSTEM_TASK_BAR != null)
    {
      IDE_PREFERENCES.addPreferenceChangeListener(new IEclipsePreferences.IPreferenceChangeListener()
      {
        @Override
        public void preferenceChange(PreferenceChangeEvent event)
        {
          String key = event.getKey();
          if (WORKSPACE_NAME.equals(key) || SHOW_LOCATION_NAME.equals(key))
          {
            update(getWorkspaceName());
          }
        }
      });

      WORKBENCH.addWindowListener(new IWindowListener()
      {
        @Override
        public void windowOpened(IWorkbenchWindow window)
        {
          Shell shell = window.getShell();
          if (shell != null && !shell.isDisposed())
          {
            update(shell, getWorkspaceName());
          }
        }

        @Override
        public void windowDeactivated(IWorkbenchWindow window)
        {
        }

        @Override
        public void windowClosed(IWorkbenchWindow window)
        {
        }

        @Override
        public void windowActivated(IWorkbenchWindow window)
        {
        }
      });

      update(getWorkspaceName());
    }
  }

  private String getWorkspaceName()
  {
    if (IDE_PREFERENCES.getBoolean(SHOW_LOCATION_NAME, DEFAULT_IDE_PREFERENCES.getBoolean(SHOW_LOCATION_NAME, true)))
    {
      return IDE_PREFERENCES.get(WORKSPACE_NAME, DEFAULT_IDE_PREFERENCES.get(WORKSPACE_NAME, "")); //$NON-NLS-1$
    }

    return ""; //$NON-NLS-1$
  }

  private void update(String label)
  {
    for (IWorkbenchWindow workbenchWindow : WORKBENCH.getWorkbenchWindows())
    {
      Shell shell = workbenchWindow.getShell();
      if (shell != null && !shell.isDisposed())
      {
        update(shell, label);
      }
    }
  }

  private void update(Shell shell, String label)
  {
    TaskItem item = SYSTEM_TASK_BAR.getItem(shell);
    if (item == null)
    {
      item = SYSTEM_TASK_BAR.getItem(null);
    }

    if (item != null)
    {
      item.setOverlayText(label);
    }
  }
}

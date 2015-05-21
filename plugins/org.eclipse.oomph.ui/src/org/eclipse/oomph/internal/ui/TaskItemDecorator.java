/*
 * Copyright (c) 2014 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

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

  public static final String IDE_NODE = "org.eclipse.ui.ide";

  public static final String WORKSPACE_NAME = "WORKSPACE_NAME";

  private static final IEclipsePreferences IDE_PREFERENCES = InstanceScope.INSTANCE.getNode(IDE_NODE);

  public TaskItemDecorator()
  {
    if (SYSTEM_TASK_BAR != null)
    {
      IDE_PREFERENCES.addPreferenceChangeListener(new IEclipsePreferences.IPreferenceChangeListener()
      {
        public void preferenceChange(PreferenceChangeEvent event)
        {
          if (WORKSPACE_NAME.equals(event.getKey()))
          {
            Object value = event.getNewValue();
            update(value == null ? "" : value.toString());
          }
        }
      });

      WORKBENCH.addWindowListener(new IWindowListener()
      {
        public void windowOpened(IWorkbenchWindow window)
        {
          Shell shell = window.getShell();
          if (shell != null && !shell.isDisposed())
          {
            update(shell, getWorkspaceName());
          }
        }

        public void windowDeactivated(IWorkbenchWindow window)
        {
        }

        public void windowClosed(IWorkbenchWindow window)
        {
        }

        public void windowActivated(IWorkbenchWindow window)
        {
        }
      });

      update(getWorkspaceName());
    }
  }

  private String getWorkspaceName()
  {
    return IDE_PREFERENCES.get(WORKSPACE_NAME, "");
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
